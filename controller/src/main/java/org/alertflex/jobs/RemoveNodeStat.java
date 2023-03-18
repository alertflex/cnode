/*
 *   Copyright 2021 Oleg Zharkov
 *
 *   Licensed under the Apache License, Version 2.0 (the "License").
 *   You may not use this file except in compliance with the License.
 *   A copy of the License is located at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   or in the "license" file accompanying this file. This file is distributed
 *   on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 *   express or implied. See the License for the specific language governing
 *   permissions and limitations under the License.
 */

package org.alertflex.jobs;

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
import org.alertflex.entity.NodeAlerts;
import org.alertflex.entity.NodeMonitor;
import org.alertflex.entity.Project;
import org.alertflex.facade.NodeAlertsFacade;
import org.alertflex.facade.NodeMonitorFacade;
import org.alertflex.facade.ProjectFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton(name = "removeNodeStat")
@ConcurrencyManagement(CONTAINER)
@Startup

public class RemoveNodeStat {

    @Resource
    private TimerService timerService;

    @EJB
    private ProjectFacade projectFacade;
    List<Project> projectList = null;

    @EJB
    private NodeMonitorFacade nodeMonitorFacade;

    @EJB
    private NodeAlertsFacade nodeAlertsFacade;

    private static final Logger logger = LoggerFactory.getLogger(RemoveNodeStat.class);

    static final long INIT_INTERVAL = 60000; // 1min
    static final long PERIODIC_INTERVAL = 3600000; // 1 hour
    static final long DAY = 86400000;

    @PostConstruct
    public void init() {

        timerService.createTimer(INIT_INTERVAL, PERIODIC_INTERVAL, "removeNodeStat");
    }

    @Lock(LockType.WRITE)
    @AccessTimeout(value = 500)
    @Timeout
    public void removeStatTimer(Timer timer) throws InterruptedException, Exception {

        projectList = projectFacade.findAll();

        if (projectList == null || projectList.isEmpty()) {
            return;
        }

        for (Project project : projectList) {

            if (project != null) {
                int timerange = project.getNodeTimerange();

                if (timerange > 0) {
                    
                    Date currentDate = new Date();
                    long millis = currentDate.getTime() - DAY * timerange;
                    currentDate.setTime(millis);
                    Timestamp dt = new Timestamp(currentDate.getTime());
                    
                    // 
                    List<NodeMonitor> nodemonitorList = nodeMonitorFacade.getOldStat(project.getRefId(), dt);
                    
                    if (nodemonitorList != null) {
                        do {
                            for (NodeMonitor n: nodemonitorList) nodeMonitorFacade.remove(n);
                        
                            nodemonitorList = nodeMonitorFacade.getOldStat(project.getRefId(), dt);

                        } while (nodemonitorList != null && !nodemonitorList.isEmpty());
                    }

                    // 
                    List<NodeAlerts> nodealertsList = nodeAlertsFacade.getOldStat(project.getRefId(), dt);
                    
                    if (nodealertsList != null) {
                        do {
                            for (NodeAlerts n: nodealertsList) nodeAlertsFacade.remove(n);
                        
                            nodealertsList = nodeAlertsFacade.getOldStat(project.getRefId(), dt);

                        } while (nodealertsList != null && nodealertsList.isEmpty());
                    }

                }
            }
        }
    }
}
