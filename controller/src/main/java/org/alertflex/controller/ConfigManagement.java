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

package org.alertflex.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import org.alertflex.entity.Project;
import org.alertflex.facade.ProjectFacade;


@Singleton(name = "configManagement")
@ConcurrencyManagement(CONTAINER)
@Startup
public class ConfigManagement {

    @Resource
    private TimerService timerService;

    @EJB
    private ProjectFacade projectFacade;
        
    private static final Logger logger = LoggerFactory.getLogger(ConfigManagement.class);

    static final int TIMER_INTERVAL = 60000; // 1 min
    
    int alertTimerange = 0;
    int alertType = 0;
    int nodeTimerange = 0;
    int postureTimerange = 0;
    int iocCheck = 0;
    int prometheusStat = 0;
    int sendNetflow = 0;
    int sendIncident = 0;
    
    String elkHost = "";
    int elkPort = 0;
    String elkUser = "";
    String elkPass = "";
    String elkStorepass = "";
    String elkKeystore = "";
    String elkTruststore = "";
    
    String graylogHost = "";
    int graylogPort = 0;

    String hiveURL = "";
    String hiveKey = "";
    
    String gitlabURL = "";
    String gitlabKey = "";
    
    String sonarURL="";
    String sonarUser="";
    String sonarPass="";

    String trackURL="";
    String trackKey="";
    String trackProject="";
    String trackVersion="";

    @PostConstruct
    public void init() {

        Timer timer = timerService.createTimer(TIMER_INTERVAL, TIMER_INTERVAL, "configManagement");

    }

    @Timeout
    @AccessTimeout(value = 1)
    public void configManagementTimer(Timer timer) throws InterruptedException, Exception {
        
        List<Project> projects = projectFacade.findAll();
        
        if (projects == null || projects.isEmpty()) return;
        
        Project project = projects.get(0);
        
        if (project == null) return;
        
        Properties prop = new Properties();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(project.getProjectPath() + "project.properties");
        } catch (Exception e) {
            // e.printStackTrace();
            return;
        }
        try {
            prop.load(fis);
        } catch (IOException e) {
            // e.printStackTrace();
            return;
        }
        
        int updateProperties = Integer.parseInt(prop.getProperty("update_properties"));
        if (updateProperties == 0 ) return;

        alertTimerange = Integer.parseInt(prop.getProperty("alert_timerange"));
        project.setAlertTimerange(alertTimerange);
        
        alertType = Integer.parseInt(prop.getProperty("alert_type"));
        project.setAlertType(alertType);
                
        nodeTimerange = Integer.parseInt(prop.getProperty("node_timerange"));
        project.setNodeTimerange(nodeTimerange);
                
        postureTimerange = Integer.parseInt(prop.getProperty("posture_timerange"));
        project.setPostureTimerange(postureTimerange);
                
        iocCheck = Integer.parseInt(prop.getProperty("ioc_check"));
        project.setIocCheck(iocCheck);
                
        prometheusStat = Integer.parseInt(prop.getProperty("prometheus_stat"));
        project.setPrometheusStat(prometheusStat);
                
        sendNetflow = Integer.parseInt(prop.getProperty("send_netflow"));
        project.setSendNetflow(sendNetflow);
                        
        sendIncident = Integer.parseInt(prop.getProperty("send_incident"));
        project.setSendIncident(sendIncident);
                
        graylogHost = prop.getProperty("graylog_host");
        project.setGraylogHost(graylogHost);
                
        graylogPort = Integer.parseInt(prop.getProperty("graylog_port"));
        project.setGraylogPort(graylogPort);
        
        elkHost = prop.getProperty("elk_host");
        project.setElkHost(elkHost);
                
        elkPort = Integer.parseInt(prop.getProperty("elk_port"));
        project.setElkPort(elkPort);
                
        elkUser = prop.getProperty("elk_user");
        project.setElkUser(elkUser);
        
        elkPass = prop.getProperty("elk_pass");
        project.setElkPass(elkPass);
                
        elkStorepass = prop.getProperty("elk_storepass");
        project.setElkStorepass(elkStorepass);
                
        elkKeystore = prop.getProperty("elk_keystore");
        project.setElkKeystore(elkKeystore);
        
        elkTruststore = prop.getProperty("elk_keystore");
        project.setElkKeystore(elkKeystore);
        
        hiveURL = prop.getProperty("hive_url");
        project.setHiveUrl(hiveURL);
        
        hiveKey = prop.getProperty("hive_key");
        project.setHiveKey(hiveKey);
          
        gitlabURL = prop.getProperty("gitlab_url");
        project.setGitlabUrl(gitlabURL);
        
        gitlabKey = prop.getProperty("gitlab_key");
        project.setGitlabKey(gitlabKey);
        
        sonarURL = prop.getProperty("sonar_url");
        project.setSonarUrl(sonarURL);
        
        sonarUser = prop.getProperty("sonar_user");
        project.setSonarUser(sonarUser);
        
        sonarPass = prop.getProperty("sonar_pass");
        project.setSonarPass(sonarPass);
        
        trackURL = prop.getProperty("track_url");
        project.setTrackUrl(trackURL);
        
        trackKey = prop.getProperty("track_key");
        project.setTrackKey(trackKey);
        
        trackProject = prop.getProperty("track_project");
        project.setTrackProject(trackProject);
        
        trackVersion = prop.getProperty("track_version");
        project.setTrackVersion(trackVersion);
        
        projectFacade.edit(project);
        
    }
}
