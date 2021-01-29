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
 
package org.alertflex.tasks;

import org.alertflex.common.IprepProjects;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPOutputStream;
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
import javax.jms.BytesMessage;
import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import org.alertflex.entity.Attributes;
import org.alertflex.entity.Events;
import org.alertflex.entity.Project;
import org.alertflex.entity.Sensor;
import org.alertflex.facade.AttributesFacade;
import org.alertflex.facade.EventsFacade;
import org.alertflex.facade.ProjectFacade;
import org.alertflex.facade.SensorFacade;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton(name = "iprepUpdate")
@ConcurrencyManagement(CONTAINER)
@Startup
public class IprepUpdate {

    @Resource
    private TimerService timerService;

    @EJB
    private ProjectFacade projectFacade;
    List<Project> projectList;

    @EJB
    private AttributesFacade attributesFacade;

    @EJB
    private EventsFacade eventsFacade;

    @EJB
    private SensorFacade sensorFacade;

    private static final Logger logger = LoggerFactory.getLogger(IprepUpdate.class);

    static final int TIMER_INTERVAL_TASK = 1000;

    static final int SCROLL_INTERVAL = 100;

    static final String MISP_ATTR_TYPE = "ip-src";

    IprepProjects iprProjects;

    BufferedWriter writerWazuhMisp;
    BufferedWriter writerSuriMisp;
    
    byte[] wazuhIprep;
    byte[] suriIprep;
    
    @PostConstruct
    public void init() {

        Timer timer = timerService.createTimer(TIMER_INTERVAL_TASK, TIMER_INTERVAL_TASK, "iprepUpdate");

        iprProjects = new IprepProjects();
    }

    @Lock(LockType.WRITE)
    @AccessTimeout(value = 10)
    @Timeout
    public void IprepUpdateTimer(Timer timer) throws InterruptedException {

        List<Object> ipSrcList = new ArrayList();

        try {

            projectList = projectFacade.findAll();

            if (projectList == null || projectList.isEmpty()) {
                return;
            }

            for (Project p : projectList) {

                if (iprProjects.checkTimerCounter(p)) {

                    if (initFiles(p)) {

                        long c = attributesFacade.countIpSrc();

                        if (iprProjects.checkIpCounter(p, c)) {

                            int loops = (int) (c / SCROLL_INTERVAL);
                            int mod = (int) (c % SCROLL_INTERVAL);

                            if (loops > 0 || mod > 0) {

                                for (; loops > 0; loops--) {
                                    createIprepLists(p, attributesFacade.scrollAttributesByType(SCROLL_INTERVAL));
                                }

                                if (mod > 0) {
                                    createIprepLists(p, attributesFacade.scrollAttributesByType(mod));
                                }

                                iprProjects.setIpCounter(p, c);
                            }
                        }
                    }

                    List<Sensor> listSensors = sensorFacade.findSensorsByRef(p.getRefId());

                    // load file
                    wazuhIprep = compress(p, "controller/lists/wazuh-misp.ip");
                    suriIprep = compress(p, "controller/lists/suri-misp.ip");
                    
                    for (Sensor s : listSensors) {

                        if (s.getIprepUpdate() == 1) {
                            switch (s.getType()) {

                                case "suricata":
                                    uploadIprep(p, s, 0, suriIprep);
                                    break;
                                case "wazuh":
                                    uploadIprep(p, s, 1, wazuhIprep);
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                }

            }

        } catch (Exception e) {
            logger.error("alertflex_ctrl_exception", e);
        }
    }

    Boolean initFiles(Project p) {

        if (!iprProjects.dirStatus(p)) {
            return false;
        }

        attributesFacade.initAttributesByType(MISP_ATTR_TYPE);

        try {
            OpenOption[] options = new OpenOption[]{CREATE, TRUNCATE_EXISTING};

            Path pathWazuhMisp = Paths.get(iprProjects.getProjectListsDir(p) + "wazuh-misp.ip");
            writerWazuhMisp = Files.newBufferedWriter(pathWazuhMisp, StandardCharsets.UTF_8, options);

            Path pathSuriMisp = Paths.get(iprProjects.getProjectListsDir(p) + "suri-misp.ip");
            writerSuriMisp = Files.newBufferedWriter(pathSuriMisp, StandardCharsets.UTF_8, options);

        } catch (IOException e) {
            logger.error("alertflex_ctrl_exception", e);
            return false;
        }

        return true;
    }

    void createIprepLists(Project p, List<Object> lo) throws IOException {

        if (lo == null || lo.size() == 0) {
            return;
        }

        for (Object i : lo) {
            Attributes a = (Attributes) i;
            String ip = a.getValue1();
            Integer cat = p.getIprepCat();
            Events e = eventsFacade.find(a.getEventId());
            int sev = e.getThreatLevelId();

            String wazuh = ip + ":" + ip + "\n";
            writerWazuhMisp.write(wazuh);

            String suri = ip + "," + Integer.toString(cat) + "," + Integer.toString(sev) + "\n";
            writerSuriMisp.write(suri);

        }
    }

    public byte[] compress(Project project, String iprepPath) throws IOException {

        Path iprepFile = Paths.get(project.getProjectPath() + iprepPath);

        if (iprepFile != null) {

            String iprepPayload;

            List contents = Files.readAllLines(iprepFile);
            iprepPayload = "";

            for (Object content : contents) {
                iprepPayload = iprepPayload + content + "\n";
            }

            if ((iprepPayload == null) || (iprepPayload.length() == 0)) {
                return null;
            }

            ByteArrayOutputStream obj = new ByteArrayOutputStream();
            GZIPOutputStream gzip = new GZIPOutputStream(obj);
            gzip.write(iprepPayload.getBytes("UTF-8"));
            gzip.flush();
            gzip.close();
            return obj.toByteArray();
        }

        return null;
    }

    public void uploadIprep(Project p, Sensor s, int sensorType, byte[] listIprep) {

        String[] parsedSensorName = s.getSensorPK().getName().split("-");
        String sensorName = parsedSensorName[0];
        
        if (s == null) return;
        
        String node = s.getSensorPK().getNode();
        String probe = s.getProbe();

        if (!probe.isEmpty() && !probe.equals("indef") && !node.isEmpty() && listIprep.length > 0) {

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
                Session session = connection.createSession(false, javax.jms.Session.AUTO_ACKNOWLEDGE);
            
                // create query for request
                Destination destination = session.createQueue("jms/altprobe/" + node + "/" + probe);

                // Create a MessageProducer from the Session to the Topic or Queue
                MessageProducer producer = session.createProducer(destination);
                producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

                BytesMessage message = session.createBytesMessage();

                message.setStringProperty("ref_id", p.getRefId());
                message.setStringProperty("content_type", "iprep");
                message.setIntProperty("sensor_type", sensorType);

                message.writeBytes(listIprep);
                producer.send(message);

                // Clean up
                session.close();
                connection.close();

            } catch (JMSException e) {
                logger.error("alertflex_ctrl_exception", e);
            }
        }
    }
}
