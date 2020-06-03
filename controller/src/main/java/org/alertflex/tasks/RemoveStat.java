/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alertflex.tasks;

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
import org.alertflex.entity.Project;
import org.alertflex.facade.NetStatFacade;
import org.alertflex.facade.NodeAlertsFacade;
import org.alertflex.facade.NodeMonitorFacade;
import org.alertflex.facade.NodeFiltersFacade;
import org.alertflex.facade.ProjectFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *
 * @author root
 */

@Singleton (name="removeStat")
@ConcurrencyManagement(CONTAINER)
@Startup

public class RemoveStat {
    
    @Resource
    private TimerService timerService;
    
    @EJB
    private ProjectFacade projectFacade;
    List<Project> projectList = null;
    
    @EJB
    private NetStatFacade netStatFacade;
    
    @EJB
    private NodeMonitorFacade nodeMonitorFacade;
    
    @EJB
    private NodeAlertsFacade nodeAlertsFacade;
    
    @EJB
    private NodeFiltersFacade nodeFiltersFacade;
    
    private static final Logger logger = LoggerFactory.getLogger(RemoveStat.class);
    
    static final long INIT_INTERVAL = 60000; // 1min
    static final long PERIODIC_INTERVAL = 3600000; // 1 hour
    static final long DAY = 86400000;
    
    @PostConstruct
    public void init() {
        
        timerService.createTimer(INIT_INTERVAL, PERIODIC_INTERVAL, "removeStat");
    }
    
    @Lock(LockType.WRITE)
    @AccessTimeout(value=500)
    @Timeout
    public void removeStatTimer(Timer timer) throws InterruptedException, Exception {
                
        projectList = projectFacade.findAll();
        
        if (projectList == null || projectList.isEmpty()) return;
        
        for(Project project: projectList) {
               
            if (project != null) {
                int timerange = project.getStatTimerange();
            
                if (timerange > 0) {
                    Date currentDate = new Date();
                    long millis = currentDate.getTime() - DAY*timerange;
                    currentDate.setTime(millis);
                    Timestamp dt = new Timestamp(currentDate.getTime());
                    // 
                    netStatFacade.delOldStat(project.getRefId(), dt);
                    
                    nodeMonitorFacade.delOldStat(project.getRefId(), dt);
                    
                    nodeAlertsFacade.delOldStat(project.getRefId(), dt);
                    
                    nodeFiltersFacade.delOldStat(project.getRefId(), dt);
                    
                }
            }
        }
        
    }
}
