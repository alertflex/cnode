/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alertflex.mc.tasks;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerService;
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
import org.alertflex.mc.db.Project;
import org.alertflex.mc.db.SandboxTask;
import org.alertflex.mc.services.ProjectFacade;
import org.alertflex.mc.services.SandboxTaskFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *
 * @author root
 */
@Singleton (name="removeSandboxTask")
@ConcurrencyManagement(CONTAINER)
@Startup

public class RemoveSandboxTask {
    
    @Resource
    private TimerService timerService;
    
    @EJB
    private ProjectFacade projectFacade;
    List<Project> projectList = null;
        
    @EJB
    private SandboxTaskFacade sandboxTaskFacade;
    
    private static final Logger logger = LoggerFactory.getLogger(RemoveSandboxTask.class);
    
    static final long INIT_INTERVAL = 60000; // 1min
    static final long PERIODIC_INTERVAL = 3600000; // 1 hour
    static final long DAY = 86400000;
    
    
    @PostConstruct
    public void init() {
        
        timerService.createTimer(INIT_INTERVAL, PERIODIC_INTERVAL, "removeSandboxTask");
    }
    
    @Lock(LockType.WRITE)
    @AccessTimeout(value=500)
    @Timeout
    public void removeSandboxTaskTimer(Timer timer) throws InterruptedException, Exception {
                
        projectList = projectFacade.findAll();
        
        if (projectList == null || projectList.isEmpty()) return;
        
        for(Project project: projectList) {
        
            if (project != null) {
                int timerange = project.getTaskTimerange();
            
                if (timerange > 0) {
                    Date currentDate = new Date();
                    long millis = currentDate.getTime() - DAY*timerange;
                    currentDate.setTime(millis);
                    Timestamp dt = new Timestamp(currentDate.getTime());
                    // 
                
                    sandboxTaskFacade.delOldTaskes(project.getRefId(), dt);
                }
            }
        }
    }
}
