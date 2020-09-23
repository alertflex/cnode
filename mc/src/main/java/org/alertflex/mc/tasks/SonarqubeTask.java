/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alertflex.mc.tasks;

import com.trilead.ssh2.SFTPv3Client;
import com.trilead.ssh2.SFTPv3FileHandle;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import static java.lang.Thread.sleep;
import java.net.HttpURLConnection;
import org.apache.commons.codec.binary.Base64;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import org.alertflex.mc.db.SonarScan;
import org.alertflex.mc.db.Project;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.alertflex.mc.db.Alert;
import org.alertflex.mc.db.AlertPriority;
import org.alertflex.mc.db.Credential;
import org.alertflex.mc.db.Hosts;
import org.alertflex.mc.db.ScanJob;
import org.alertflex.mc.db.ScriptJob;
import org.json.JSONException;


/**
 *
 * @author root
 */
public class SonarqubeTask {
    
    private static final Logger logger = LoggerFactory.getLogger(SonarqubeTask.class);
    
        
    private Project project = null;
    
    private ScanJobs scanJobs;
    
    List<SonarScan> ssList = null;
        
       
    public SonarqubeTask(ScanJobs sj) {
        
        this.scanJobs = sj;
        
    }
    
    public Boolean run(Project p, ScanJob sj) throws Exception {
        
        project = p;
        
        String sonarProject = sj.getValue1();
        
        String scriptJobName = sj.getValue2();
                                    
        if (!sonarProject.isEmpty()) {
            
            if (scriptJobName != null && !scriptJobName.isEmpty()) {
                
                ScriptJob scriptJob = scanJobs.getScriptJobFacade().findJobByName(p.getRefId(), scriptJobName);
                
                if (runScript(scriptJob)) {
                    
                    runScan(sonarProject);
                
                }
                        
            } else runScan(sonarProject);
            
            if (ssList != null && !ssList.isEmpty()) {
                
                saveSonarScan(sonarProject);
                
                return true;
            }
            
        }
        
        return false;
    }
    
    public void runScan(String sonarProject) {
        
        try {
            
            String pwd = project.getSonarPass();

            String authString = project.getSonarUser() + ":" + pwd;
            byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
            String authStringEnc = new String(authEncBytes);

            // URL url = new URL("http://192.168.1.27:9000/api/issues/search?types=VULNERABILITY");
            String urlStr = project.getSonarUrl() + "/api/issues/search?types=VULNERABILITY";
            URL url = new URL(urlStr);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Authorization", "Basic " + authStringEnc);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestMethod("GET");

            int responseCode = urlConnection.getResponseCode();
            if (responseCode > 226) {
                return;
            } else {

                BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                
                String report = response.toString();
                
                Date date = new Date();
                
                if (report == null || report.isEmpty()) return;
                        
                ssList = new ArrayList();

                JSONObject obj = new JSONObject(report);

                JSONArray issues = obj.getJSONArray("issues");

                for (int i = 0; i < issues.length(); i++) {

                    String sonarProjectName = issues.getJSONObject(i).getString("project");
                        
                    if (sonarProjectName.equals(sonarProject)) {
                            
                        SonarScan ss = new SonarScan();
                        
                        ss.setRefId(project.getRefId());
                            
                        ss.setProjectId(sonarProjectName);
                        
                        String issueKey = issues.getJSONObject(i).getString("key");
                        ss.setIssueKey(issueKey);
                        
                        String component = issues.getJSONObject(i).getString("component");
                        ss.setComponent(component);
                        
                        String message = issues.getJSONObject(i).getString("message");
                        ss.setMessage(message);
                        String status = issues.getJSONObject(i).getString("status");
                        ss.setStatus(status);
                        
                        String[] tagsArr = toStringArray(issues.getJSONObject(i).getJSONArray("tags"));
                        String issueTags = String.join(", ", tagsArr);
                        ss.setTags(issueTags);
                        
                        int line = issues.getJSONObject(i).getInt("line");
                        ss.setLine(line);
                        
                        String creationDate = issues.getJSONObject(i).getString("creationDate");
                        ss.setCreationDate(creationDate);
                        
                        String updateDate = issues.getJSONObject(i).getString("updateDate");
                        ss.setUpdateDate(updateDate);
                        
                        String issueSeverity = issues.getJSONObject(i).getString("severity");
                        ss.setIssueSeverity(issueSeverity);
                        
                        ss.setReportCreated(date);
                        ss.setReportUpdated(date);
                    
                        ssList.add(ss);
                        
                    }
                }
            }

            urlConnection.disconnect();

        } catch (Exception e) {
           logger.error("alertflex_mc_exception", e);
        }
    }
    
    private String[] toStringArray(JSONArray jsonArray) throws JSONException {
        
        String[] array = new String[jsonArray.length()];
        for (int i = 0; i < array.length; i++)
        array[i] = jsonArray.getString(i);
        return array;
    }
    
    
    public boolean runScript(ScriptJob sj) {
        
        if (sj == null) return false;
        
        Boolean res = false;
        
        Hosts host = null;
        String hostName = sj.getHost();
        
        if (hostName.equals("indef") || hostName.isEmpty()) {
        
            host = scanJobs.getHostsFacade().findHost(project.getRefId(), hostName);
            
            if (host != null) {
        
                Credential cred = scanJobs.getCredentialFacade().findCredential(project.getRefId(), host.getCred());
        
                if (cred == null) return false;
                
                String script = sj.getScript();
        
                String name = sj.getScriptName();
                
                if (!script.isEmpty() && !script.equals("indef") && !name.isEmpty() && !name.equals("indef")) 
                    res = copyLocalScript(host, cred, script, name);
            
                if(res) res = runRemoteScript(host, cred, name, sj.getWaitResult());
            }
            
        }
        
        return res;
    }
    
    
    Boolean copyLocalScript(Hosts h, Credential c, String script, String name) {
        
        boolean copyReady = true;
                
        try {
            
            String hostname = h.getAddress();
            int port = h.getPort();
            String username = c.getUsername();
            String password = c.getPass();
            String keyfile = c.getSslKey(); 
                        
            if (hostname.isEmpty()) copyReady = false;
            if (username.isEmpty()) copyReady = false;
            if (password.isEmpty() && keyfile.isEmpty()) copyReady = false;
                
            if (!copyReady) return false;
            
            com.trilead.ssh2.Connection conn;
            if (port != 0) conn = new com.trilead.ssh2.Connection(hostname, port);
            else conn = new com.trilead.ssh2.Connection(hostname);
            conn.connect();

            boolean isAuthenticated;
            if (keyfile == null || keyfile.isEmpty()) isAuthenticated = conn.authenticateWithPassword(username, password);
            else {
                isAuthenticated = conn.authenticateWithPublicKey(username, keyfile.toCharArray(), password);
            }
                
            if (isAuthenticated == false) return false;
                
            SFTPv3Client dstClient = new SFTPv3Client(conn);
            SFTPv3FileHandle destFile = dstClient.createFileTruncate(name); 
            
            byte[] arr = script.getBytes(StandardCharsets.UTF_8);
            
            if (arr.length >  32768) copyReady = false;
            else dstClient.write(destFile,0,arr,0,arr.length);
                     
            dstClient.closeFile(destFile); 
            
            conn.close();
        }
        catch (Exception e) { 
            logger.error("alertflex_mc_exception", e);
            return false;
        } 
        
        if (!copyReady) return false;
        
        return true;
    }
    
    Boolean runRemoteScript(Hosts h, Credential c, String cmd, Integer timer) {
        
        boolean copyReady = true;
        Integer status;
        
        try {
            
            String hostname = h.getAddress();
            int port = h.getPort();
            String username = c.getUsername();
            String password = c.getPass();
            String keyfile = c.getSslKey(); 
                        
            if (hostname.isEmpty()) copyReady = false;
            if (username.isEmpty()) copyReady = false;
            if (password.isEmpty() && keyfile.isEmpty()) copyReady = false;
                
            if (!copyReady) return false;
            
            com.trilead.ssh2.Connection conn;
            if (port != 0) conn = new com.trilead.ssh2.Connection(hostname, port);
            else conn = new com.trilead.ssh2.Connection(hostname);
            conn.connect();

            boolean isAuthenticated;
            if (keyfile == null || keyfile.isEmpty()) isAuthenticated = conn.authenticateWithPassword(username, password);
            else {
                isAuthenticated = conn.authenticateWithPublicKey(username, keyfile.toCharArray(), password);
            }
                
            if (isAuthenticated == false) return false;
                
            com.trilead.ssh2.Session sess = conn.openSession();
                
            sess.execCommand(cmd);
            
            int i = 0;
            
            do {
                
                sleep(1000);
                status = sess.getExitStatus();
                i++;
                
            } while (status == null && i < timer);
                
            sess.close();

            conn.close();
        }
        catch (Exception e) { 
            logger.error("alertflex_mc_exception", e);
            return false;
        } 
        
        if (status > 0) return false;
        
        return true;
    }
    
    
    public void saveSonarScan(String sonarProject) {
        
        for (SonarScan ss : ssList) {
            
            SonarScan ssExisting = scanJobs.getSonarScanFacade()
                .findVulnerability(project.getRefId(), sonarProject, ss.getIssueKey());
            
            if (ssExisting == null) {
                
                AlertPriority ap = scanJobs.getAlertPriorityFacade().findPriorityBySource(project.getRefId(), "Snyk");
                int sev = 0;
                String issueSev = ss.getIssueSeverity();
                
                if (!issueSev.isEmpty() && ap != null) {
                            
                    sev = ap.getSeverityDefault();
                            
                    if(ap.getText1().equals(issueSev)) {
                        sev = ap.getValue1();
                    } else if(ap.getText2().equals(issueSev)) {
                        sev = ap.getValue2();
                    } else if(ap.getText3().equals(issueSev)) {
                        sev = ap.getValue3();
                    } else if(ap.getText4().equals(issueSev)) {
                        sev = ap.getValue4();
                    } else if(ap.getText5().equals(issueSev)) {
                        sev = ap.getValue5();
                    } 
                    
                    ss.setSeverity(sev);
                    scanJobs.getSonarScanFacade().create(ss);
                            
                    if (sev >= ap.getSeverityThreshold()) { 
                
                        Alert a = new Alert();
                
                        a.setNodeId("controller");
                        a.setRefId(project.getRefId());
                        a.setAlertUuid(UUID.randomUUID().toString());
                        a.setAlertSource("SonarQube");
                        a.setAlertType("MISC");
                        a.setSensorId("controller");
                        a.setAlertSeverity(sev);
                        a.setDescription(ss.getMessage());
                        a.setEventId("1");
                        a.setEventSeverity(issueSev);
                        a.setLocation(ss.getProjectId());
                        a.setAction("indef");
                        a.setStatus("processed");
                        a.setInfo(ss.getIssueKey());
                        a.setFilter("indef");
                        a.setTimeEvent("");
                        Date d = new Date(); 
                        a.setTimeCollr(d);
                        a.setTimeCntrl(d);
                        a.setAgentName("indef");
                        a.setUserName("indef");
                        a.setCategories("sonarqube, " + ss.getTags());
                        a.setSrcIp("indef");
                        a.setDstIp("indef");
                        a.setDstPort(0);
                        a.setSrcPort(0);
                        a.setSrcHostname("indef");
                        a.setDstHostname("indef");
                        a.setFileName(ss.getComponent());
                        a.setFilePath("indef");
                        a.setHashMd5("indef");
                        a.setHashSha1("indef");
                        a.setHashSha256("indef");
                        a.setProcessId(0);
                        a.setProcessName("indef");
                        a.setProcessCmdline("indef");
                        a.setProcessPath("indef");
                        a.setUrlHostname("indef");
                        a.setUrlPath("indef");
                        a.setContainerId("indef");
                        a.setContainerName("indef");
                        a.setJsonEvent("indef");
                
                        scanJobs.getAlertFacade().create(a);
                    }
                }
            
            } else {
                Date d = new Date();
                ssExisting.setReportUpdated(d);
                scanJobs.getSonarScanFacade().edit(ssExisting);
            }
        }
    }
}