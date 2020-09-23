
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
import org.alertflex.mc.services.ProjectFacade;
import java.util.List;
import org.alertflex.mc.db.ScanJob;
import org.alertflex.mc.services.AlertFacade;
import org.alertflex.mc.services.AlertPriorityFacade;
import org.alertflex.mc.services.CredentialFacade;
import org.alertflex.mc.services.HostsFacade;
import org.alertflex.mc.services.NessusScanFacade;
import org.alertflex.mc.services.NmapScanFacade;
import org.alertflex.mc.services.ScanJobFacade;
import org.alertflex.mc.services.ScriptJobFacade;
import org.alertflex.mc.services.SnykScanFacade;
import org.alertflex.mc.services.SonarScanFacade;
import org.alertflex.mc.services.ZapScanFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Oleg Zharkov
 */
//@ConcurrencyManagement(BEAN)
@Singleton (name="scanJobs")
@ConcurrencyManagement(CONTAINER)
@Startup
public class ScanJobs  {
    
    @Resource
    private TimerService timerService;
    
    @EJB
    private AlertFacade alertFacade;
    
    @EJB
    private AlertPriorityFacade alertPriorityFacade;
    
    @EJB
    private NessusScanFacade nessusScanFacade;
    
    @EJB
    private NmapScanFacade nmapScanFacade;
        
    @EJB
    private ZapScanFacade zapScanFacade;
    
    @EJB
    private SonarScanFacade sonarScanFacade;
    
    @EJB
    private SnykScanFacade snykScanFacade;
    
    @EJB
    private ScriptJobFacade scriptJobFacade;
    
    @EJB
    private CredentialFacade credentialFacade;
    
    @EJB
    private HostsFacade hostsFacade;
    
    @EJB
    private ProjectFacade projectFacade;
    List<Project> projectList;
    
    @EJB
    private ScanJobFacade scanJobFacade;
    List<ScanJob> scanJobList;
    
    JobsManager scanManager = null;
    
    private Project p = null;
        
    private static final Logger logger = LoggerFactory.getLogger(ScanJobs.class);
        
    static final int TIMER_INTERVAL_TASK = 1000;
        
    static final int SCROLL_INTERVAL = 100;
    
            
    @PostConstruct
    public void init() {
        
        Timer timer = timerService.createTimer(TIMER_INTERVAL_TASK, TIMER_INTERVAL_TASK, "sandboxJobs");
        
        scanManager = new JobsManager();
    }
    
    public AlertFacade getAlertFacade() {
        return alertFacade;
    }
    
    public AlertPriorityFacade getAlertPriorityFacade() {
        return alertPriorityFacade;
    }
    
    public NessusScanFacade getNessusScanFacade() {
        return nessusScanFacade;
    }
    
    public NmapScanFacade getNmapScanFacade() {
        return nmapScanFacade;
    }
        
    public ZapScanFacade getZapScanFacade() {
        return zapScanFacade;
    }
    
    public SonarScanFacade getSonarScanFacade() {
        return sonarScanFacade;
    }
    
    public SnykScanFacade getSnykScanFacade() {
        return snykScanFacade;
    }
    
    public ScriptJobFacade getScriptJobFacade() {
        return scriptJobFacade;
    }
    
    public HostsFacade getHostsFacade() {
        return hostsFacade;
    }
    
    public CredentialFacade getCredentialFacade() {
        return credentialFacade;
    }
    
    
    @Lock(LockType.WRITE)
    @AccessTimeout(value=10)
    @Timeout
    public void scanJobsTimer (Timer timer) throws InterruptedException {
        
        try {
            
            projectList = projectFacade.findAll();
            
            if (projectList == null || projectList.isEmpty()) return;
            
            for (Project p: projectList) {
                
                scanJobList = scanJobFacade.findJobByRef(p.getRefId());
                
                if (scanJobList == null || scanJobList.isEmpty()) continue;
                
                for (ScanJob sj : scanJobList) {
            
                    if (scanManager.checkTimerCounter(p.getRefId(), sj.getName(), "scan", sj.getTimerange())) {
                        
                        switch(sj.getScanType()) {
                            
                            case "Nessus":
                        
                                NessusTask nessusTask = new NessusTask(this);
                                nessusTask.run(p, sj);
                                break;
                            
                            case "Nmap":
                        
                                NmapTask nmapTask = new NmapTask(this);
                                nmapTask.run(p, sj);
                                break;
                                
                            case "SonarQube":
                                
                                SonarqubeTask st = new SonarqubeTask(this);
                                st.run(p, sj);
                                break;
                                
                            case "Snyk":
                                
                                SnykTask snykT = new SnykTask(this);
                                snykT.run(p, sj);
                                break;
                        
                            case "ZAP":
                        
                                ZapTask zt = new ZapTask(this);
                                zt.run(p, sj);
                                break;
                            
                            default:
                                break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("alertflex_mc_exception", e);
        }
    }
}   
