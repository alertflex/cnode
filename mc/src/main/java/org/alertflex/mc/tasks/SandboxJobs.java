
package org.alertflex.mc.tasks;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.AccessTimeout;
import javax.ejb.ConcurrencyManagement;
import static javax.ejb.ConcurrencyManagementType.CONTAINER;
import javax.ejb.EJB;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerService;
import org.alertflex.mc.db.Project;
import org.alertflex.mc.db.SandboxJob;
import org.alertflex.mc.db.Credential;
import org.alertflex.mc.db.Hosts;
import org.alertflex.mc.services.CredentialFacade;
import org.alertflex.mc.services.HostsFacade;
import org.alertflex.mc.services.ProjectFacade;
import org.alertflex.mc.services.SandboxJobFacade;
import org.json.JSONArray;
import org.json.JSONObject;
import com.trilead.ssh2.Connection;
import com.trilead.ssh2.SFTPv3Client;
import com.trilead.ssh2.SFTPv3DirectoryEntry;
import com.trilead.ssh2.SFTPv3FileHandle;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import java.util.Date;
import java.util.List;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import java.util.UUID;
import org.alertflex.mc.db.SandboxTask;
import org.alertflex.mc.services.SandboxTaskFacade;
import org.alertflex.mc.supp.SandboxFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Oleg Zharkov
 */
//@ConcurrencyManagement(BEAN)
@Singleton (name="sandboxJobs")
@ConcurrencyManagement(CONTAINER)
@Startup
public class SandboxJobs  {
    
    @Resource
    private TimerService timerService;
    
    @EJB
    private CredentialFacade credentialFacade;
    
    @EJB
    private SandboxTaskFacade sandboxTaskFacade;
    
    @EJB
    private HostsFacade hostsFacade;
    
    @EJB
    private ProjectFacade projectFacade;
    List<Project> projectList;
    
    @EJB
    private SandboxJobFacade sandboxJobFacade;
    List<SandboxJob> sandboxJobList;
    
    JobsManager sandboxManager = null;
    
    private Project p = null;
        
    SandboxJob sj = null;
    
    String sandboxFilesDir = "";
    
    private static final Logger logger = LoggerFactory.getLogger(SandboxJobs.class);
        
    static final int TIMER_INTERVAL_TASK = 1000;
        
    static final int SCROLL_INTERVAL = 100;
    
            
    @PostConstruct
    public void init() {
        
        Timer timer = timerService.createTimer(TIMER_INTERVAL_TASK, TIMER_INTERVAL_TASK, "sandboxJobs");
        
        sandboxManager = new JobsManager();
    }
    
    
    @Lock(LockType.WRITE)
    @AccessTimeout(value=10)
    @Timeout
    public void sandboxJobsTimer (Timer timer) throws InterruptedException {
        
        try {
            
            projectList = projectFacade.findAll();
            
            if (projectList == null || projectList.isEmpty()) return;
            
            for (Project p: projectList) {
                
                sandboxJobList = sandboxJobFacade.findJobsByRef(p.getRefId());
                
                if (sandboxJobList == null || sandboxJobList.isEmpty()) continue;
                
                for (SandboxJob sj : sandboxJobList) {
                    
                    if (sj.getTimerange() != 0) {
            
                        if (sandboxManager.checkTimerCounter(p.getRefId(), sj.getName(), "sandbox", sj.getTimerange())) {
                            run(p,sj);
                        }
                        
                        if (sj.getTimerange() == 1) {
                            sj.setTimerange(0);
                            sandboxJobFacade.edit(sj);
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("alertflex_mc_exception", e);
        }
    }
    
    public Boolean run(Project project, SandboxJob sandboxJob) throws Exception {
        
        p = project;
        sj = sandboxJob;
        
        sandboxFilesDir = p.getProjectPath() + "sandbox/";
        Path sandboxFilesPath = Paths.get(sandboxFilesDir);
                        
        if (Files.notExists(sandboxFilesPath)) {
            try { Files.createDirectory(sandboxFilesPath); }
            catch (IOException e ) { 
                logger.error("alertflex_mc_exception", e);
                return false; 
            }
        }
        
        sandboxFilesDir = sandboxFilesDir + sj.getName() + "/";
        sandboxFilesPath = Paths.get(sandboxFilesDir);
        
        if (Files.notExists(sandboxFilesPath )) {
            try { Files.createDirectory(sandboxFilesPath); }
            catch (IOException e ) { 
                logger.error("alertflex_mc_exception", e);
                return false; 
            }
        }
            
        String sensorType = sandboxJob.getSensorType();
                        
        if (sensorType.equals("suricata")) return ManageSuriFiles();
        else return ManageFiles();
        
    }
    
    
    boolean ManageSuriFiles () {
        
        boolean copyReady = true;
        
        try {
            
            Hosts h = hostsFacade.findHost(p.getRefId(), sj.getHostName());
            Credential c = credentialFacade.findCredential(p.getRefId(), h.getCred());
            
            if (c == null || h == null) return false;
        
            String hostname = h.getAddress();
            int port = h.getPort();
            String username = c.getUsername();
            String password = c.getPass();
            String keyfile = c.getSslKey(); 
                        
            if (hostname.isEmpty()) copyReady = false;
            if (username.isEmpty()) copyReady = false;
            if (password.isEmpty() && keyfile.isEmpty()) copyReady = false;
                
            if (!copyReady) return false;
            
            Connection conn;
            if (port != 0) conn = new Connection(hostname, port);
            else conn = new Connection(hostname);
            conn.connect();

            boolean isAuthenticated;
            if (keyfile == null || keyfile.isEmpty()) isAuthenticated = conn.authenticateWithPassword(username, password);
            else {
                isAuthenticated = conn.authenticateWithPublicKey(username, keyfile.toCharArray(), password);
            }
                
            if (isAuthenticated == false) return false;
                
            SFTPv3Client clientFtpNode = new SFTPv3Client(conn);
            
            String[] dirLetters = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };
            
            String fileNameRegex = sj.getFileExt();
            
            List<SFTPv3DirectoryEntry> srcFilesSFTPv3Dir = null;
            
            int limitCheckFiles = sj.getFilesLimit();
            int counterFiles = 0;
        
            for (int i=0, j=0, k=0; i < 256; i++) {
                
                if (limitCheckFiles > 0) {
                    if (counterFiles >= limitCheckFiles) break;
                }
                    
                boolean isDirExist = true;
                // calculate source dir
                String srcFilesDir = sj.getFilePath() + dirLetters[k] + dirLetters[j] + "/";
                
                // get files in src dir
                try {
                    srcFilesSFTPv3Dir = clientFtpNode.ls(srcFilesDir);
                } catch (IOException ioex) {
                    isDirExist = false;
                }
                    
                if (j >= 15) {
                    j = 0;
                    k++;
                } else j++;
        
                if(!srcFilesSFTPv3Dir.isEmpty() && srcFilesSFTPv3Dir != null && isDirExist) {
                    
                    // check files
            
                    for (SFTPv3DirectoryEntry file: srcFilesSFTPv3Dir) {
                
                        if (!file.attributes.isDirectory()) {
                        
                            String suri_file_json = file.filename;
                            // check if file is json
                            String suri_file = GetSuriShaFileName(suri_file_json);
                                                
                            if (suri_file != null) {
        
                                // load content of json file
                                String sourceFileJson = srcFilesDir + suri_file_json;
                                SFTPv3FileHandle handleFile = clientFtpNode.openFileRO(sourceFileJson); 
                        
                                byte[] arr = new byte[4096]; 
                                    
                                int fi = clientFtpNode.read(handleFile, 0, arr, 0, arr.length);
                                clientFtpNode.closeFile(handleFile);
                                
                                
                                if (fi != -1) {
                        
                                    String sourceFile = srcFilesDir + suri_file;
                                                                
                                    SandboxFile sf = new SandboxFile(sj, sandboxFilesDir + suri_file, new String(arr,0, fi, "UTF-8"));
                            
                                    // check if state of file ready for downloading to sandbox - CLOSED or TRUNCATED
                                    if (sf.getState()) {
                                
                                        handleFile = clientFtpNode.openFileRO(sourceFile); 
                                                                
                                        Path destFilePath = Paths.get(sandboxFilesDir + suri_file);
                                        OutputStream out = new BufferedOutputStream(Files.newOutputStream(destFilePath, CREATE, TRUNCATE_EXISTING));
                            
                                        int fileOffset = 0; 
                                        i = 0; 
                                        arr = new byte[1024]; 
                                    
                                        while ((i = clientFtpNode.read(handleFile, fileOffset, arr, 0, arr.length)) != -1) {
                                            out.write(arr, 0, i);
                                            fileOffset += i;
                                        } 
                                
                                    
                                        out.close();
                                        clientFtpNode.closeFile(handleFile);
                                    
                                        switch (sj.getSandboxType()) {
                                        
                                            case "Cuckoo":
                                                uploadFileToCuckoo(p,sf,h.getName(),sourceFile);
                                                break;
                                            case "HybridAnalysis":
                                                uploadFileToFalcon(p,sf,h.getName(),sourceFile);
                                                break;
                                            case "VMRay":
                                                uploadFileToVmray(p,sf,h.getName(),sourceFile);
                                                break;
                                        
                                        }
                                        
                                        if (limitCheckFiles > 0) counterFiles++;
                                                   
                                        // delete files
                                        if (sj.getDelFile() == 1) {
                                            clientFtpNode.rm(sourceFileJson);
                                            clientFtpNode.rm(sourceFile);
                                        }
                                        
                                        Files.delete(destFilePath);
                                    }
                                }
                            }
                        }
                    }
                } 
            }
        
            clientFtpNode.close();
            conn.close();
        }
        catch (Exception e) { 
            logger.error("alertflex_mc_exception", e);
            return false;
        } 
        
        return true;
    }
    
    boolean ManageFiles () {
        
        boolean copyReady = true;
        
        try {
            
            Hosts h = hostsFacade.findHost(p.getRefId(), sj.getHostName());
            Credential c = credentialFacade.findCredential(p.getRefId(), h.getCred());
        
            if (c == null || h == null) return false;
        
            String hostname = h.getAddress();
            int port = h.getPort();
            String username = c.getUsername();
            String password = c.getPass();
            String keyfile = c.getSslKey(); 
                        
            if (hostname.isEmpty()) copyReady = false;
            if (username.isEmpty()) copyReady = false;
            if (password.isEmpty() && keyfile.isEmpty()) copyReady = false;
                
            if (!copyReady) return false;
            
            Connection conn;
            if (port != 0) conn = new Connection(hostname, port);
            else conn = new Connection(hostname);
            conn.connect();

            boolean isAuthenticated;
            if (keyfile == null || keyfile.isEmpty()) isAuthenticated = conn.authenticateWithPassword(username, password);
            else {
                isAuthenticated = conn.authenticateWithPublicKey(username, keyfile.toCharArray(), password);
            }
                
            if (isAuthenticated == false)
                throw new IOException("Authentication failed.");
                
            if (isAuthenticated == false) return false;
                
            SFTPv3Client clientFtpNode = new SFTPv3Client(conn);
                
            List<SFTPv3DirectoryEntry> nodeFilesDir = clientFtpNode.ls(sj.getFilePath());
        
            String fileExt = sj.getFileExt();
        
            if(!nodeFilesDir.isEmpty()) {
                
                int limit = sj.getFilesLimit();
                int fileCounter = 0;
            
                for (SFTPv3DirectoryEntry file: nodeFilesDir) {
                    
                    if (limit != 0) {
                        if (fileCounter <= limit ) fileCounter++;
                        else break;
                    }
                    
                    if (!file.attributes.isDirectory()) {
                        
                        String filename = file.filename;
                                                
                        if (filename.matches(fileExt)) {
                        
                            String sourceFile = sj.getFilePath() + file.filename;
                        
                            SFTPv3FileHandle handleNode = clientFtpNode.openFileRO(sourceFile); 
                            
                            Path destFilePath = Paths.get(sandboxFilesDir + file.filename);
                            OutputStream out = new BufferedOutputStream(Files.newOutputStream(destFilePath, CREATE, TRUNCATE_EXISTING));
                            
                            int fileOffset = 0; 
                            int i = 0; 
                            byte[] arr = new byte[1024]; 
                                    
                            while ((i = clientFtpNode.read(handleNode, fileOffset, arr, 0, arr.length)) != -1) {
                                out.write(arr, 0, i);
                                fileOffset += i;
                            } 
                                
                            clientFtpNode.closeFile(handleNode);
                            out.close();
                            
                            SandboxFile sf = new SandboxFile(sj, sandboxFilesDir + file.filename, "");
                            
                            switch (sj.getSandboxType()) {
                                
                                case "Cuckoo":
                                    uploadFileToCuckoo(p,sf,h.getName(),sourceFile);
                                case "HybridAnalysis":
                                    uploadFileToFalcon(p,sf,h.getName(),sourceFile);
                                    break;
                                case "VMRay":
                                    uploadFileToVmray(p,sf,h.getName(),sourceFile);
                                    break;
                                        
                            }
                        
                            Files.delete(destFilePath);
                            if (sj.getDelFile() == 1) clientFtpNode.rm(sourceFile);
                        }
                    }
                }
            }
        
            clientFtpNode.close();
            conn.close();
        }
        catch (Exception e) { 
            logger.error("alertflex_mc_exception", e);
            return false;
        } 
        
        return true;
    }
    
    String GetSuriShaFileName(String fileName) {
        
        String[] fileNames = fileName.split("\\.");
        
        if (fileNames.length == 4) {
            if (fileNames[3].equals("json")) return fileNames[0];
        }
        
        return null;
    }
    
    
    boolean uploadFileToCuckoo(Project p, SandboxFile sf, String hn, String fn) {
        
        HttpClient client = new DefaultHttpClient() ;
        String cuckooUrl = "http://" + p.getCuckooHost() + ":" + Integer.toString(p.getCuckooPort()) + "/tasks/create/file";
        
        try {
            HttpPost postRequest = new HttpPost (cuckooUrl) ;
            String fileName = sf.getFileName();
            File file = new File(fileName) ;
            
             //Set various attributes
            MultipartEntity multiPartEntity = new MultipartEntity () ;
            multiPartEntity.addPart("filename", new StringBody(fileName != null ? fileName : file.getName())) ;
            
            FileBody fileBody = new FileBody(file, "application/octect-stream") ;
            //Prepare payload
            multiPartEntity.addPart("file", fileBody) ;
  
            //Set to request body
            postRequest.setEntity(multiPartEntity) ;
             
            //Send request
            HttpResponse response = client.execute(postRequest) ;
             
            //Verify response if any
            if (response != null) {
                
                int res = response.getStatusLine().getStatusCode();
                
                if (res > 202) return false;
                
                String json = EntityUtils.toString(response.getEntity(), "UTF-8");
                
                JSONObject obj = new JSONObject(json);
                
                Integer ti = obj.getInt("task_id");
                
                if (ti > 0) CreateSandboxTask(sf, Integer.toString(ti), "Cuckoo", hn, fn);
                else return false;
            }
            
        } catch (Exception e) {
            logger.error("alertflex_mc_exception", e);
            return false;
        }
        
        return true;
    }
    
    boolean uploadFileToFalcon(Project p, SandboxFile sf, String hn, String fn) throws Exception {
        
        try {

            final String API_URI = p.getFalconUrl() + "/api/v2/submit/file";
            final HttpClient client = HttpClientBuilder.create().disableContentCompression().build();
            final HttpPost post = new HttpPost(API_URI);
            final String boundary = "---------------"+UUID.randomUUID().toString();
            
            post.setHeader("accept", "application/json");
            post.setHeader("user-agent", "Falcon Sandbox");
            String key = p.getFalconKey();
            post.setHeader("api-key", key);
            post.setHeader("Content-Type", ContentType.MULTIPART_FORM_DATA.getMimeType()+";boundary="+boundary);
            
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setBoundary(boundary);

            File file = new File(sf.getFileName());
            FileBody fileBody = new FileBody(file, ContentType.APPLICATION_OCTET_STREAM);
            builder.addPart("file", fileBody);

            StringBody stringBody = new StringBody("110", ContentType.MULTIPART_FORM_DATA);
            builder.addPart("environment_id", stringBody);

            final HttpEntity entity = builder.build();

            post.setEntity(entity);
            final HttpResponse response = client.execute(post);

            //Verify response if any
            if (response != null) {
                
                int res = response.getStatusLine().getStatusCode();
                
                if (res > 202) return false;
                
                String json = EntityUtils.toString(response.getEntity(), "UTF-8");

                JSONObject obj = new JSONObject(json);
                String jobId = obj.getString("job_id");
                
                if (!jobId.isEmpty() && jobId != null) CreateSandboxTask(sf, jobId, "HybridAnalysis", hn, fn);
                else return false;
            }

        } catch (IOException e) {
            logger.error("alertflex_mc_exception", e);
            return false;
        }
        
        return true;
    }
    
    boolean uploadFileToVmray(Project p, SandboxFile sf, String hn, String fn) throws Exception {
        
        try {

            final String API_URI = p.getVmrayUrl() + "/rest/sample/submit";
            final HttpClient client = HttpClientBuilder.create().disableContentCompression().build();
            final HttpPost post = new HttpPost(API_URI);
            final String boundary = "---------------"+UUID.randomUUID().toString();
            
            post.setHeader("accept", "application/json");
            String key = p.getVmrayKey();
            post.setHeader("Authorization", "api_key " + key);
            post.setHeader("Content-Type", ContentType.MULTIPART_FORM_DATA.getMimeType()+";boundary="+boundary);

            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setBoundary(boundary);
            
            
            File file = new File(sf.getFileName());
            FileBody fileBody = new FileBody(file, ContentType.APPLICATION_OCTET_STREAM);
            builder.addPart("sample_file", fileBody);

            final HttpEntity entity = builder.build();

            post.setEntity(entity);
            final HttpResponse response = client.execute(post);
            
            if (response != null) {

                String json = EntityUtils.toString(response.getEntity(), "UTF-8");

                JSONObject obj = new JSONObject(json);
                String result = obj.getString("result");

                if (result.equals("ok")) {

                    JSONObject data = obj.getJSONObject("data");

                    JSONArray submissions = data.getJSONArray("submissions");

                    JSONObject submission = submissions.getJSONObject(0);

                    Integer submission_id = submission.getInt("submission_id");

                    String jobId = Integer.toString(submission_id);
                    
                    if (!jobId.isEmpty() && jobId != null) CreateSandboxTask(sf, jobId, "VMRay", hn, fn);
                    else return false;
                
                } else {
                    return false;
                }
                
            } else {
                return false;
            }

        } catch (IOException e) {
            logger.error("alertflex_mc_exception", e);
            return false;
        }
        
        return true;
    }
    
    void CreateSandboxTask(SandboxFile sf, String id, String type, String hn, String fn) {
        
        try { 
            SandboxTask t = new SandboxTask();
            t.setRefId(sf.getSandboxJob().getRefId());
            t.setSandboxJob(sf.getSandboxJob().getName());
            t.setSandboxId(id);
            t.setHostName(hn);
            t.setFileName(fn);
            t.setSandboxType(type);
            t.setInfo(sf.getJsonData());
            t.setStatus("progress");
            Date d = new Date();
            t.setTimeOfCreation(d);
            t.setTimeOfAction(d);
            
            int info_size = sf.getJsonData().length();
            
            String info = sf.getJsonData();
            
            
            sandboxTaskFacade.create(t);
                                            
        } catch (Exception e) { 
            logger.error("alertflex_mc_exception", e);
        } 
    }
}   
