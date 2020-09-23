/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alertflex.mc.tasks;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.alertflex.mc.db.Project;
import org.alertflex.mc.services.AlertFacade;
import org.alertflex.mc.services.ProjectFacade;


/**
 *
 * @author root
 */
@Singleton (name="monitor")
@ConcurrencyManagement(CONTAINER)
@Startup

public class Monitor {
    
    @Resource
    private TimerService timerService;
    
    @EJB
    public AlertFacade alertFacade;
    
    @EJB
    private ProjectFacade projectFacade;
    private List<Project> projectList = null;
     
    private static final Logger logger = LoggerFactory.getLogger(Monitor.class);
    
    static final int interval = 10000;
    
    Map<String,Long> statusCriticalMap;
    Map<String,Long> statusMajorMap;
    Map<String,Long> statusMinorMap;
    Map<String,Long> statusNormalMap;
    
    String[] status;
    
    public Monitor() {
        
        statusCriticalMap = new HashMap<String,Long>();
        statusMajorMap = new HashMap<String,Long>();
        statusMinorMap = new HashMap<String,Long>();
        statusNormalMap = new HashMap<String,Long>();
        
        status = new String[] {"aggregated", "modified", "processed"};
        
    }
    
    @PostConstruct
    public void init() {
        timerService.createTimer(1, interval, "monitor");
        
    }
    
    @Lock(LockType.WRITE)
    @AccessTimeout(value=500)
    @Timeout
    public void monitorTimer(Timer timer) {
        
        projectList = projectFacade.findAll();
        
        for (Project project: projectList) {
            
            Long result = alertFacade.counterAlertsBySeverityStatus(project.getRefId(), 3, status);
            statusCriticalMap.put(project.getRefId(),result);
            
            result = alertFacade.counterAlertsBySeverityStatus(project.getRefId(), 2, status);
            statusMajorMap.put(project.getRefId(),result);
            
            result = alertFacade.counterAlertsBySeverityStatus(project.getRefId(), 1, status);
            statusMinorMap.put(project.getRefId(),result);
            
            result = alertFacade.counterAlertsBySeverityStatus(project.getRefId(), 0, status);
            statusNormalMap.put(project.getRefId(),result);
        }
    }
    
    public Long getNumNewCriticalAlerts (String project) {
        
        Long result = statusCriticalMap.get(project);
                
        return result;
    }
    
    public Long getNumNewMajorAlerts (String project) {
        
        Long result = statusMajorMap.get(project);
                
        return result;
    }
    
    public Long getNumNewMinorAlerts (String project) {
        
        Long result = statusMinorMap.get(project);
                
        return result;
    }
    
    public Long getNumNewNormalAlerts (String project) {
        
        Long result = statusNormalMap.get(project);
                
        return result;
    }
    
}