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
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.alertflex.entity.Alert;
import org.alertflex.entity.Project;
import org.alertflex.entity.NodeMonitor;
import org.alertflex.entity.Probe;
import org.alertflex.facade.ProjectFacade;
import org.alertflex.facade.AlertFacade;
import org.alertflex.facade.NodeMonitorFacade;
import org.alertflex.facade.ProbeFacade;
import org.apache.activemq.ActiveMQConnectionFactory;


@Singleton(name = "checkProbes")
@ConcurrencyManagement(CONTAINER)
@Startup
public class CheckProbes {

    @Resource
    private TimerService timerService;

    @EJB
    private ProjectFacade projectFacade;
    private List<Project> projectList = null;
    
    @EJB
    private ProbeFacade probeFacade;
    
    @EJB
    private NodeMonitorFacade nodeMonitorFacade;
    
    @EJB
    private AlertFacade alertFacade;
    
    private static final Logger logger = LoggerFactory.getLogger(CheckProbes.class);

    static final long MINUTE_INTERVAL = 60000; // 1min
    static final long HOUR_INTERVAL = 3600000; // 1 hour 3600000
    static final long DAY = 86400000;

    @PostConstruct
    public void init() {

        Timer timer = timerService.createTimer(MINUTE_INTERVAL, HOUR_INTERVAL, "checkProbes");

    }

    @Timeout
    @AccessTimeout(value = 1)
    public void checkProbesTimer(Timer timer) throws InterruptedException, Exception {

        projectList = projectFacade.findAll();

        if (projectList == null || projectList.isEmpty()) {
            return;
        }

        for (Project p : projectList) {

            List<Probe> listProbes = probeFacade.findProbesByRef(p.getRefId());
            
            int timerange = p.getSensorTimerange();

            if (listProbes != null && timerange > 0) {

                for (Probe probe: listProbes) {

                    long currentTime = new Date().getTime();
                    Date start = new Date(currentTime - timerange*HOUR_INTERVAL);
                    Date end = new Date(currentTime);
                    
                    List<NodeMonitor> nml = nodeMonitorFacade.findAllStatBetween(p.getRefId(), probe.getProbePK().getNode(), probe.getHostName(), start, end);
                            
                    if (nml != null && probe.getStatus() > 0) {
                        
                        boolean crsStat = false;
                        boolean hidsStat = false;
                        boolean nidsStat = false;
                        boolean wafStat = false; 
                                                        
                        for (NodeMonitor nm : nml) {
                            if (nm.getEventsCrs() > 0) crsStat = true;
                            if (nm.getEventsHids() > 0) hidsStat = true;
                            if (nm.getEventsNids() > 0) nidsStat = true;
                            if (nm.getEventsWaf() > 0) wafStat = true;
                        }
                        
                        switch (probe.getProbeType()) {
                            
                            case "Falco":
                                // if inactive set to active
                                if (crsStat && probe.getStatus() == 2) {
                                    probe.setStatus(1);
                                    probeFacade.edit(probe);
                                } else {
                                    // if active set to inactive
                                    if (!crsStat && probe.getStatus() == 1) {
                                        probe.setStatus(2);
                                        probeFacade.edit(probe);
                                        sendAlert(p, probe.getProbePK().getNode(), probe.getProbePK().getName());
                                    }
                                }
                                break;
                            case "Wazuh":
                                if (hidsStat && probe.getStatus() == 2) {
                                    probe.setStatus(1);
                                    probeFacade.edit(probe);
                                } else {
                                
                                    if (!hidsStat && probe.getStatus() == 1) {
                                        probe.setStatus(2);
                                        probeFacade.edit(probe);
                                        sendAlert(p, probe.getProbePK().getNode(), probe.getProbePK().getName());
                                    }
                                }
                                break;
                            case "Suricata":
                                if (nidsStat && probe.getStatus() == 2) {
                                    probe.setStatus(1);
                                    probeFacade.edit(probe);
                                } else {
                                
                                    if (!nidsStat && probe.getStatus() == 1) {
                                        probe.setStatus(2);
                                        probeFacade.edit(probe);
                                        sendAlert(p, probe.getProbePK().getNode(), probe.getProbePK().getName());
                                    }
                                }
                                break;
                            case "ModSecurity":
                                if (wafStat && probe.getStatus() == 2) {
                                    probe.setStatus(1);
                                    probeFacade.edit(probe);
                                } else {
                                
                                    if (!wafStat && probe.getStatus() == 1) {
                                        probe.setStatus(2);
                                        probeFacade.edit(probe);
                                        sendAlert(p, probe.getProbePK().getNode(), probe.getProbePK().getName());
                                    }
                                }
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
        }
    }
    
    void sendAlert(Project project, String node, String probe) {
        
        if (project.getSemActive() == 0) return;

        Alert a = new Alert();

        a.setRefId(project.getRefId());
        a.setNode(node);
        a.setAlertUuid(UUID.randomUUID().toString());
        a.setAlertType("MISC");
        a.setAlertSource("Alertflex");
        a.setProbe(probe);
        a.setAlertSeverity(2);
        a.setDescription("Status of probe: " + probe + " was set to inactive due to missing data from collector");
        a.setEventId("Sensor disabled");
        a.setEventSeverity("Major");
        a.setLocation("indef");
        a.setAction("indef");
        a.setStatus("processed");
        a.setInfo("indef");
        a.setFilter("indef");
        a.setTimeEvent("");
        Date date = new Date();
        a.setTimeCollr(date);
        a.setTimeCntrl(date);
        a.setAgentName("indef");
        a.setUserName("indef");
        a.setCategories("colector");
        a.setSrcIp("indef");
        a.setDstIp("indef");
        a.setDstPort(0);
        a.setSrcPort(0);
        a.setSrcHostname("indef");
        a.setDstHostname("indef");
        a.setRegValue("indef");
        a.setFileName("indef");
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
        a.setCloudInstance("indef");
        a.setIncidentExt("indef");

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
            Connection connection = connectionFactory.createConnection(user, pass);
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

        } catch (JMSException e) {
            logger.error("alertflex_mc_exception", e);
        }
    }
}
