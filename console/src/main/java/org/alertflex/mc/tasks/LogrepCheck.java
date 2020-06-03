
package org.alertflex.mc.tasks;

import java.util.ArrayList;
import java.util.List;
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
import org.alertflex.mc.db.ScanJob;
import org.alertflex.mc.services.ProjectFacade;
import org.alertflex.mc.services.ScanJobFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Oleg Zharkov
 */
//@ConcurrencyManagement(BEAN)
@Singleton (name="logrepCheck")
@ConcurrencyManagement(CONTAINER)
@Startup
public class LogrepCheck  {
    
    @Resource
    private TimerService timerService;
    
    @EJB
    private ProjectFacade projectFacade;
    List<Project> projectList;
    
    @EJB
    private ScanJobFacade scanJobFacade;
    List<ScanJob> scanJobList;
    
    LogrepProjects logrepProjects = null;
    
    private static final Logger logger = LoggerFactory.getLogger(LogrepCheck.class);
        
    static final int TIMER_INTERVAL_TASK = 1000;
        
    static final int SCROLL_INTERVAL = 100;
    
            
    @PostConstruct
    public void init() {
        
        Timer timer = timerService.createTimer(TIMER_INTERVAL_TASK, TIMER_INTERVAL_TASK, "logrepCheck");
        
        logrepProjects = new LogrepProjects();
    }
    
    
    @Lock(LockType.WRITE)
    @AccessTimeout(value=10)
    @Timeout
    public void LogrepCheckTimer (Timer timer) throws InterruptedException {
        
        try {
            
            projectList = projectFacade.findAll();
            
            if (projectList == null || projectList.isEmpty()) return;
            
            for (Project p: projectList) {
            
                if (logrepProjects.checkTimerCounter(p)) {
                    
                    scanJobList = scanJobFacade.findJobs(p.getRefId());
                    
                    if (scanJobList != null && !scanJobList.isEmpty()) {
                        
                        for (ScanJob sb: scanJobList) {
                            
                            if(sb.getScanType().equals("elastic")) {
                                ElasticTask et = new ElasticTask();
                                et.run(p, sb);
                            } else {
                                if(sb.getScanType().equals("graylog")) {
                                    GraylogTask gt = new GraylogTask();
                                    gt.run(p, sb);
                                }
                            }
                            
                        }
                        
                    }
                
                }
                
            }
            
        } catch (Exception e) {
            logger.error("alertflex_wrk_exception", e);
        }
    }
}   
