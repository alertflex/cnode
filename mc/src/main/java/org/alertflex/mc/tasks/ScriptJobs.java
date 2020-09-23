
package org.alertflex.mc.tasks;

import com.trilead.ssh2.SFTPv3Client;
import com.trilead.ssh2.SFTPv3FileHandle;
import static java.lang.Thread.sleep;
import java.nio.charset.StandardCharsets;
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
import org.alertflex.mc.services.CredentialFacade;
import org.alertflex.mc.services.HostsFacade;
import org.alertflex.mc.services.ProjectFacade;
import java.util.List;
import org.alertflex.mc.db.Credential;
import org.alertflex.mc.db.Hosts;
import org.alertflex.mc.db.ScriptJob;
import org.alertflex.mc.services.AlertFacade;
import org.alertflex.mc.services.ScriptJobFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Oleg Zharkov
 */
//@ConcurrencyManagement(BEAN)
@Singleton (name="scriptJobs")
@ConcurrencyManagement(CONTAINER)
@Startup
public class ScriptJobs  {
    
    @Resource
    private TimerService timerService;
    
    @EJB
    private AlertFacade alertFacade;
    
    @EJB
    private CredentialFacade credentialFacade;
    
    @EJB
    private HostsFacade hostsFacade;
    
    @EJB
    private ProjectFacade projectFacade;
    List<Project> projectList;
    
    @EJB
    private ScriptJobFacade scriptJobFacade;
    List<ScriptJob> scriptJobList;
    
    JobsManager scriptManager = null;
    
    private Project p = null;
        
    private static final Logger logger = LoggerFactory.getLogger(ScriptJobs.class);
        
    static final int TIMER_INTERVAL_TASK = 1000;
        
    static final int SCROLL_INTERVAL = 100;
    
            
    @PostConstruct
    public void init() {
        
        Timer timer = timerService.createTimer(TIMER_INTERVAL_TASK, TIMER_INTERVAL_TASK, "scriptJobs");
        
        scriptManager = new JobsManager();
    }
    
    public AlertFacade getAlertFacade() {
        return alertFacade;
    }
    
    @Lock(LockType.WRITE)
    @AccessTimeout(value=10)
    @Timeout
    public void scriptJobsTimer (Timer timer) throws InterruptedException {
        
        try {
            
            projectList = projectFacade.findAll();
            
            if (projectList == null || projectList.isEmpty()) return;
            
            for (Project p: projectList) {
                
                scriptJobList = scriptJobFacade.findJobByRef(p.getRefId());
                
                if (scriptJobList == null || scriptJobList.isEmpty()) continue;
                
                for (ScriptJob sj : scriptJobList) {
            
                    if (scriptManager.checkTimerCounter(p.getRefId(), sj.getName(), "script", sj.getTimerange())) {
                        
                        Hosts host = null;
                        String hostName = sj.getHost();
                    
                        if (hostName.equals("indef") || hostName.isEmpty()) {
                
                            host = hostsFacade.findHost(p.getRefId(), hostName);
                            
                            if (host != null) {
                
                                Credential cred = credentialFacade.findCredential(p.getRefId(), host.getCred());
                
                                if (cred == null) continue;
                                
                                String script = sj.getScript();
                    
                                String name = sj.getScriptName();
                        
                                if (!script.isEmpty() && !script.equals("indef") && !name.isEmpty() && !name.equals("indef")) 
                                    copyLocalScript(host, cred, script, name);
                            
                                runRemoteScript(host, cred, name, sj.getWaitResult());
                            }
                            
                        }
                    
                
                } else {
                        
                        
                    }
                }
            }
        } catch (Exception e) {
            logger.error("alertflex_mc_exception", e);
        }
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
}   
