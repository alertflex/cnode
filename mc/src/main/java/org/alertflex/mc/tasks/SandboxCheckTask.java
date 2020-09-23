/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alertflex.mc.tasks;

import com.trilead.ssh2.SFTPv3Client;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.AccessTimeout;
import javax.ejb.ConcurrencyManagement;
import static javax.ejb.ConcurrencyManagementType.CONTAINER;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerService;
import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.alertflex.mc.db.Alert;
import org.alertflex.mc.db.AlertPriority;
import org.alertflex.mc.db.Credential;
import org.alertflex.mc.db.Hosts;
import org.alertflex.mc.db.Project;
import org.alertflex.mc.db.SandboxJob;
import org.alertflex.mc.db.SandboxTask;
import org.alertflex.mc.services.AlertFacade;
import org.alertflex.mc.services.AlertPriorityFacade;
import org.alertflex.mc.services.CredentialFacade;
import org.alertflex.mc.services.HostsFacade;
import org.alertflex.mc.services.ProjectFacade;
import org.alertflex.mc.services.SandboxJobFacade;
import org.alertflex.mc.services.SandboxTaskFacade;
import org.alertflex.mc.supp.SandboxFile;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author root
 */
//@ConcurrencyManagement(BEAN)
@Singleton (name="sandboxCheckTask")
@ConcurrencyManagement(CONTAINER)
@Startup
public class SandboxCheckTask  {
    
    @Resource
    private TimerService timerService;
    
    @EJB
    private AlertFacade alertFacade;
    
    @EJB
    private AlertPriorityFacade alertPriorityFacade;
    
    @EJB
    private SandboxJobFacade sandboxJobFacade;
    
    @EJB
    private SandboxTaskFacade sandboxTaskFacade;
    
    @EJB
    private CredentialFacade credentialFacade;
    
    @EJB
    private HostsFacade hostsFacade;
    
    @EJB
    private ProjectFacade projectFacade;
    private List<Project> projectList = null;
        
    private static final Logger logger = LoggerFactory.getLogger(SandboxCheckTask.class);
        
    static final int TIMER_INTERVAL = 60000; // 1 min
    
    @PostConstruct
    public void init() {
        
        Timer timer = timerService.createTimer(TIMER_INTERVAL, TIMER_INTERVAL, "sandboxCheckTask");
        
    }
    
    
    @Timeout
    @AccessTimeout(value=1)
    public void sandboxCheckTaskTimer (Timer timer) throws InterruptedException, Exception {
        
        projectList = projectFacade.findAll();
        
        if (projectList == null || projectList.isEmpty()) return;
        
        for (Project p: projectList) {
            
            List <SandboxTask> stList = sandboxTaskFacade.findActiveJobs(p.getRefId());
            
            if (stList != null) {
                
                for (SandboxTask st : stList) {
                    
                    int result = 0;
                    
                    switch (st.getSandboxType()) {
                        
                         case "Cuckoo":
                            result = ProceedTaskForCuckoo(p, st);
                            break;
                        
                        case "HybridAnalysis":
                            result = ProceedTaskForFalcon(p, st);
                            break;
                        case "VMRay":
                            result = ProceedTaskForVmray(p, st);
                            break;
                        
                    }
                    
                    if (result != 0) {
                        st.setStatus("reported");
                        Date d = new Date();
                        st.setTimeOfAction(d);
                        sandboxTaskFacade.edit(st);
                        
                        if (result == 2 || result == 3) {
                            if (!st.getSandboxJob().equals("manual") && !st.getSandboxJob().equals("incident")) {
                                SandboxJob sj = sandboxJobFacade.findJobByName(st.getRefId(), st.getSandboxJob());
                                if (sj != null) {
                                    if (sj.getDelInfect() > 0) DeleteFile(p, st.getHostName(), st.getFileName());
                                }
                            }
                        }
                    }
                }
            }
        } 
    }
    
    int ProceedTaskForCuckoo(Project p, SandboxTask st) {
        
        HttpClient client = new DefaultHttpClient();
        
        String host = p.getCuckooHost();
        String port = Integer.toString(p.getCuckooPort());
        String task = st.getSandboxId();
        String cuckooUrl = "http://" + host + ":" + port + "/tasks/view/" + task;
        
        try {
                
            HttpGet statusRequest = new HttpGet (cuckooUrl) ;
                
            HttpResponse response = client.execute(statusRequest) ;
                
            if (response != null) {
                    
                String json = EntityUtils.toString(response.getEntity(), "UTF-8");
                    
                JSONObject obj = new JSONObject(json);
                JSONObject taskJson = obj.getJSONObject("task");    
                String status = taskJson.getString("status");
                
                if (status.equals("reported")) {
                    
                    cuckooUrl = "http://" + host + ":" + port + "/tasks/report/" + task;
                    
                    HttpGet reportRequest = new HttpGet (cuckooUrl) ;
                
                    response = client.execute(reportRequest) ;
                    
                    if (response != null) {
                        
                        SandboxJob sj = null;
                        
                        if (!st.getSandboxJob().equals("manual") && !st.getSandboxJob().equals("incident")) {
                            sj = sandboxJobFacade.findJobByName(st.getRefId(), st.getSandboxJob());
                            if (sj == null) return 0;
                        }
                        
                        SandboxFile sf = new SandboxFile(sj,st.getFileName(),st.getInfo());
                        
                        json = EntityUtils.toString(response.getEntity(), "UTF-8");
                        
                        obj = new JSONObject(json);
                        JSONObject infoJson = obj.getJSONObject("info");    
                            
                        
                        Double score = infoJson.getDouble("score");
                        
                        AlertPriority ap = alertPriorityFacade.findPriorityBySource(st.getRefId(), "Cuckoo");
                        int sev = 0;
                        
                        if (ap != null) {
                            
                            sev = ap.getSeverityDefault();
                            
                            Double scoreMinor = (double) ap.getMinorThreshold();
                            Double scoreMajor = (double) ap.getMajorThreshold();
                            Double scoreCritical = (double) ap.getCriticalThreshold();
                        
                            if (score > scoreMinor && scoreMinor != 0) sev = 1;
                        
                            if (score > scoreMajor && scoreMajor != 0) sev = 2;
                        
                            if (score > scoreCritical && scoreCritical != 0) sev = 3;
                            
                            if (sev >= ap.getSeverityThreshold()) sendAlert(sj, st, sf,Double.toString(score), sev, "Cuckoo");
                        }
                        
                        return sev;
                        
                    }
                }
            }
        } catch (Exception e) {
            logger.error("alertflex_mc_exception", e);
        }
        
        return 0;
    }
    
    int ProceedTaskForFalcon(Project p, SandboxTask st) throws Exception {
        
        String task = st.getSandboxId();
        String falconUrl = p.getFalconUrl() + "/api/v2/report/" + task + "/state";
        HttpGet get = new HttpGet(falconUrl);
        
        String key = p.getFalconKey();
        
        get.setHeader("accept", "application/json");
        get.setHeader("user-agent", "Falcon Sandbox");
        get.setHeader("api-key", key);
        
        try {
                
            HttpClient client = HttpClientBuilder.create().disableContentCompression().build();
            HttpResponse response = client.execute(get);

            if (response != null) {
                    
                String json = EntityUtils.toString(response.getEntity(), "UTF-8");

                JSONObject obj = new JSONObject(json);
                String stateTask = obj.getString("state");


                if (stateTask.equals("SUCCESS")) {

                    falconUrl = p.getFalconUrl() + "/api/v2/report/" + task + "/summary";
                    get = new HttpGet(falconUrl);

                    get.setHeader("accept", "application/json");
                    get.setHeader("user-agent", "Falcon Sandbox");
                    get.setHeader("api-key", key);

                    response = client.execute(get);

                    if (response != null) {
                        
                        SandboxJob sj = null;
                        
                        if (!st.getSandboxJob().equals("manual") && !st.getSandboxJob().equals("incident")) {
                            sj = sandboxJobFacade.findJobByName(st.getRefId(), st.getSandboxJob());
                            if (sj == null) return 0;
                        }
                        
                        SandboxFile sf = new SandboxFile(sj,st.getFileName(),st.getInfo());
                        
                        json = EntityUtils.toString(response.getEntity(), "UTF-8");
                        obj = new JSONObject(json);
                            
                        String verdict = obj.getString("verdict");
                        
                        AlertPriority ap = alertPriorityFacade.findPriorityBySource(st.getRefId(), "HybridAnalysis");
                        int sev = 0;
                        
                        if (!verdict.isEmpty() && ap != null) {
                            
                            sev = ap.getSeverityDefault();
                            
                            if(ap.getText1().equals(verdict)) {
                                sev = ap.getValue1();
                            } else if(ap.getText2().equals(verdict)) {
                                sev = ap.getValue2();
                            } else if(ap.getText3().equals(verdict)) {
                                sev = ap.getValue3();
                            } else if(ap.getText4().equals(verdict)) {
                                sev = ap.getValue4();
                            } else if(ap.getText5().equals(verdict)) {
                                sev = ap.getValue5();
                            } 
                            
                            if (sev >= ap.getSeverityThreshold()) { 
                        
                                int threat_score = obj.getInt("threat_score");
                            
                                sendAlert(sj, st, sf, Integer.toString(threat_score), sev, "HybridAnalysis");
                            }
                        }
                        
                        return sev;
                    }
                    
                } else {
                    if (stateTask.equals("ERROR")) {
                        st.setStatus("error");
                        Date d = new Date();
                        st.setTimeOfAction(d);
                        sandboxTaskFacade.edit(st);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("alertflex_mc_exception", e);
        }
        
        return 0;
    }
    
    int ProceedTaskForVmray(Project p, SandboxTask st) throws Exception {
        
        String task = st.getSandboxId();
        String vmrayUrl = p.getVmrayUrl() + "/rest/submission/" + task;
        HttpGet get = new HttpGet(vmrayUrl);

        get.setHeader("accept", "application/json");
        String key = p.getVmrayKey();
        
        get.setHeader("Authorization", "api_key " + key);
        
        try {
                
            HttpClient client = HttpClientBuilder.create().disableContentCompression().build();
            HttpResponse response = client.execute(get);

            if (response != null) {

                String json = EntityUtils.toString(response.getEntity(), "UTF-8");

                // System.out.println(json);

                JSONObject obj = new JSONObject(json);
                String result = obj.getString("result");

                if (result.equals("ok")) {

                    JSONObject data = obj.getJSONObject("data");

                    Boolean submission_finished = data.getBoolean("submission_finished");

                    if (submission_finished) {
                        
                        SandboxJob sj = null;
                        
                        if (!st.getSandboxJob().equals("manual") && !st.getSandboxJob().equals("incident")) {
                            sj = sandboxJobFacade.findJobByName(st.getRefId(), st.getSandboxJob());
                            if (sj == null) return 0;
                        }
                        
                        SandboxFile sf = new SandboxFile(sj,st.getFileName(),st.getInfo());

                        String submission_severity = data.getString("submission_severity");

                        AlertPriority ap = alertPriorityFacade.findPriorityBySource(st.getRefId(), "VMRay");
                        int sev = 0;
                        
                        if (!submission_severity.isEmpty() && ap != null) {
                            
                            sev = ap.getSeverityDefault();
                            
                            if(ap.getText1().equals(submission_severity)) {
                                sev = ap.getValue1();
                            } else if(ap.getText2().equals(submission_severity)) {
                                sev = ap.getValue2();
                            } else if(ap.getText3().equals(submission_severity)) {
                                sev = ap.getValue3();
                            } else if(ap.getText4().equals(submission_severity)) {
                                sev = ap.getValue4();
                            } else if(ap.getText5().equals(submission_severity)) {
                                sev = ap.getValue5();
                            } 
                            
                            if (sev >= ap.getSeverityThreshold()) { 
                        
                                int submission_score = data.getInt("submission_score");
                        
                            
                                sendAlert(sj, st, sf, Integer.toString(submission_score), sev, "VMRay");
                            }
                        }
                                                        
                        return sev;
                    } 
                } 
            }

        } catch (Exception e) {
            logger.error("alertflex_mc_exception", e);
        }
        
        return 0;
    }
    
    void sendAlert(SandboxJob sj, SandboxTask st, SandboxFile sf, String score, int priority, String sandboxSource) {
        
        Alert a = new Alert();
        
        String verdict = "";
        
        switch (priority) {

            case 1: verdict = "no specific threat.";
                break;

            case 2: verdict = "suspicious.";
                break;

            case 3: verdict = "malicious.";
                break;

            default:
                verdict = "no specific threat.";
                priority = 0;
                break;
        }
        
        switch (st.getSandboxType()) {
                        
            case "Cuckoo":
                a.setInfo("For details see cuckoo report for task: " + st.getSandboxId());
                a.setDescription("File has been scanned in Cuckoo sandbox, verdict: " + verdict);
                break;
            case "HybridAnalysis":
                a.setInfo("For details see falcon report for task: " + st.getSandboxId());
                a.setDescription("File has been scanned in Falcon sandbox, verdict: " + verdict);
                break;
            case "VMRay":
                a.setInfo("For details see vmray report for task: " + st.getSandboxId());
                a.setDescription("File has been scanned in VMray sandbox, verdict: " + verdict);
                break;
                        
        }
        
        a.setNodeId("controller");
        a.setRefId(st.getRefId());
        a.setAlertUuid(UUID.randomUUID().toString());
        a.setAlertSource(sandboxSource);
        a.setAlertType("FILE");
        a.setSensorId("controller");
        a.setAlertSeverity(priority);
        a.setEventId("1");
        a.setEventSeverity(score);
        a.setLocation("indef");
        a.setAction("indef");
        a.setStatus("processed");
        a.setFilter("indef");
        a.setTimeEvent("");
        Date date = new Date(); 
        a.setTimeCollr(st.getTimeOfCreation());
        a.setTimeCntrl(date);
        a.setAgentName("indef");
        a.setUserName("indef");
        a.setCategories(st.getSandboxType());
        a.setSrcIp(sf.getSrcIp());
        a.setDstIp(sf.getDestIp());
        a.setDstPort(sf.getDestPort());
        a.setSrcPort(sf.getSrcPort());
        a.setSrcHostname("indef");
        a.setDstHostname("indef");
        a.setFileName(st.getFileName());
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
        a.setJsonEvent(sf.getJsonData());
                                                            
        alertFacade.create(a);
        
        sendAlertToMQ(a);
        
    }
    
    public void sendAlertToMQ(Alert a) {
        
        try {
            
            String strConnFactory = System.getProperty("AmqUrl", "");
            String user = System.getProperty("AmqUser", "");
            String pass = System.getProperty("AmqPwd", "");
            
            // Create a ConnectionFactory
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(strConnFactory);
            
            // Create a Connection
            Connection connection = connectionFactory.createConnection(user,pass);
            connection.start();

            // Create a Session
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // Create the destination (Topic or Queue)
            Destination destination = session.createQueue("jms/alertflex/alerts");

            // Create a MessageProducer from the Session to the Topic or Queue
            MessageProducer producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
            
            TextMessage message = session.createTextMessage();
            
            message.setIntProperty("msg_type", 2);
            message.setStringProperty("ref_id", a.getRefId());
            message.setStringProperty("alert_uuid", a.getAlertUuid());
            message.setText("empty");
            producer.send(message);
            
            // Clean up
            session.close();
            connection.close();
            
                
        } catch ( JMSException e) {
            logger.error("alertflex_mc_exception", e);
        } 
    }
    
     boolean DeleteFile (Project p, String hostName, String fileName) {
        
        boolean copyReady = true;
        
        try {
            
            Hosts h = hostsFacade.findHost(p.getRefId(), hostName);
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
            
            com.trilead.ssh2.Connection conn;
            if (port != 0) conn = new com.trilead.ssh2.Connection(hostname, port);
            else conn = new com.trilead.ssh2.Connection(hostname);
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
                
            clientFtpNode.rm(fileName);
        
            clientFtpNode.close();
            conn.close();
        }
        catch (Exception e) { 
            logger.error("alertflex_mc_exception", e);
            return false;
        } 
        
        return true;
    }
}

