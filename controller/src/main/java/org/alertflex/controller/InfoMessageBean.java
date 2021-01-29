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

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.ejb.Stateless;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.zip.GZIPInputStream;
import javax.inject.Inject;
import javax.jms.BytesMessage;
import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.alertflex.common.PojoAlertLogic;
import org.alertflex.logserver.FromElasticPool;
import org.alertflex.entity.Alert;
import org.alertflex.entity.Project;
import org.alertflex.facade.AgentFacade;
import org.alertflex.facade.HomeNetworkFacade;
import org.alertflex.facade.HostsFacade;
import org.alertflex.facade.AlertFacade;
import org.alertflex.facade.AttributesFacade;
import org.alertflex.facade.EventsFacade;
import org.alertflex.facade.NodeAlertsFacade;
import org.alertflex.facade.NodeMonitorFacade;
import org.alertflex.facade.NodeFiltersFacade;
import org.alertflex.facade.NetStatFacade;
import org.alertflex.facade.ProjectFacade;
import org.alertflex.facade.AgentVulFacade;
import org.alertflex.facade.AgentOpenscapFacade;
import org.alertflex.facade.AgentPackagesFacade;
import org.alertflex.facade.AgentProcessesFacade;
import org.alertflex.facade.AgentScaFacade;
import org.alertflex.facade.AlertPriorityFacade;
import org.alertflex.facade.ContainerFacade;
import org.alertflex.facade.DockerScanFacade;
import org.alertflex.facade.TrivyScanFacade;
import org.alertflex.facade.NodeFacade;
import org.alertflex.facade.SensorFacade;
import org.alertflex.logserver.ElasticSearch;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")
    ,
        @ActivationConfigProperty(propertyName = "destination", propertyValue = "jms/alertflex/info"),})
@Stateless
public class InfoMessageBean implements MessageListener {

    private static final Logger logger = LoggerFactory.getLogger(InfoMessageBean.class);

    @Inject
    @FromElasticPool
    ElasticSearch elasticFromPool;

    @EJB
    private AgentFacade agentFacade;
    
    @EJB
    private ContainerFacade containerFacade;

    @EJB
    private HomeNetworkFacade homeNetworkFacade;
    
    @EJB
    private HostsFacade hostsFacade;

    @EJB
    private AlertFacade alertFacade;

    @EJB
    private AlertPriorityFacade alertPriorityFacade;

    @EJB
    private NodeMonitorFacade nodeMonitorFacade;

    @EJB
    private NodeAlertsFacade nodeAlertsFacade;

    @EJB
    private NodeFiltersFacade nodeFiltersFacade;

    @EJB
    private NetStatFacade netStatFacade;

    @EJB
    private AgentVulFacade agentVulFacade;

    @EJB
    private AgentScaFacade agentScaFacade;

    @EJB
    private AgentPackagesFacade agentPackagesFacade;

    @EJB
    private AgentProcessesFacade agentProcessesFacade;

    @EJB
    private AgentOpenscapFacade agentOpenscapFacade;

    @EJB
    private DockerScanFacade dockerScanFacade;

    @EJB
    private TrivyScanFacade trivyScanFacade;

    @EJB
    private ProjectFacade projectFacade;
    Project project;
    String ref_id;

    @EJB
    private NodeFacade nodeFacade;
    String node;
    String probe;

    @EJB
    private SensorFacade sensorFacade;

    @EJB
    private AttributesFacade attributesFacade;

    @EJB
    private EventsFacade eventsFacade;

    public AgentFacade getAgentFacade() {
        return this.agentFacade;
    }
    
    public ContainerFacade getContainerFacade() {
        return this.containerFacade;
    }

    public ElasticSearch getElasticFromPool() {
        return elasticFromPool;
    }

    public HomeNetworkFacade getHomeNetworkFacade() {
        return this.homeNetworkFacade;
    }
    
    public HostsFacade getHostsFacade() {
        return this.hostsFacade;
    }

    public SensorFacade getSensorFacade() {
        return this.sensorFacade;
    }

    public NodeFacade getNodeFacade() {
        return this.nodeFacade;
    }

    public String getNode() {
        return this.node;
    }
    
    public String getProbe() {
        return this.probe;
    }

    public AlertFacade getAlertFacade() {
        return this.alertFacade;
    }

    public AlertPriorityFacade getAlertPriorityFacade() {
        return this.alertPriorityFacade;
    }

    public NodeMonitorFacade getNodeMonitorFacade() {
        return this.nodeMonitorFacade;
    }

    public NodeAlertsFacade getNodeAlertsFacade() {
        return this.nodeAlertsFacade;
    }

    public NodeFiltersFacade getNodeFiltersFacade() {
        return this.nodeFiltersFacade;
    }

    public NetStatFacade getNetStatFacade() {
        return this.netStatFacade;
    }

    public AgentVulFacade getAgentVulFacade() {
        return this.agentVulFacade;
    }

    public AgentScaFacade getAgentScaFacade() {
        return this.agentScaFacade;
    }

    public AgentOpenscapFacade getAgentOpenscapFacade() {
        return this.agentOpenscapFacade;
    }

    public AgentPackagesFacade getAgentPackagesFacade() {
        return this.agentPackagesFacade;
    }

    public AgentProcessesFacade getAgentProcessesFacade() {
        return this.agentProcessesFacade;
    }

    public DockerScanFacade getDockerScanFacade() {
        return this.dockerScanFacade;
    }

    public TrivyScanFacade getTrivyScanFacade() {
        return this.trivyScanFacade;
    }

    public Project getProject() {
        return this.project;
    }

    public String getRefId() {
        return this.ref_id;
    }

    public AttributesFacade getAttributesFacade() {
        return this.attributesFacade;
    }

    public EventsFacade getEventsFacade() {
        return this.eventsFacade;
    }

    @Override
    public void onMessage(Message message) {

        int msg_type;

        try {

            if (message instanceof BytesMessage) {

                BytesMessage bytesMessage = (BytesMessage) message;

                msg_type = bytesMessage.getIntProperty("msg_type");

                if (msg_type == 0) {
                    return;
                }

                ref_id = bytesMessage.getStringProperty("ref_id");
                project = projectFacade.findProjectByRef(ref_id);
                
                if (project != null) {
                    
                    node = bytesMessage.getStringProperty("node_id");
                    probe = bytesMessage.getStringProperty("probe_id");
                    
                    byte[] buffer = new byte[(int) bytesMessage.getBodyLength()];
                    bytesMessage.readBytes(buffer);

                    String data = "";

                    data = decompress(buffer);

                    if (data.isEmpty()) {
                        return;
                    }
                    
                    int sensor;

                    switch (msg_type) {
                        case 1:
                            StatsManagement sm = new StatsManagement(this);
                            sm.EvaluateStats(data);
                        break;

                        case 2:
                            //IndexRequest request = new IndexRequest("alertflex");
                            //IndexResponse indexResponse = elasticFromPool.index(request, RequestOptions.DEFAULT);
                            LogsManagement lm = new LogsManagement(this);
                            lm.EvaluateLogs(data);
                        break;

                        case 3: 
                            ConfigsManagement cm = new ConfigsManagement(this);
                            sensor = bytesMessage.getIntProperty("sensor");
                            cm.saveConfig(sensor, data);
                            break;

                        case 4: 
                            RulesManagement rm = new RulesManagement(this);
                            sensor = bytesMessage.getIntProperty("sensor");
                            String rule = bytesMessage.getStringProperty("rule");
                            rm.saveRule(sensor, rule, data);
                            break;

                        case 5: 
                            DockerBench db = new DockerBench(this);
                            db.saveReport(data);
                            break;

                        case 6: 
                            Trivy trivy = new Trivy(this);
                            trivy.saveReport(data);
                            break;

                        default:
                            break;
                    }
                }

                return;
            }
        } catch (IOException | JMSException | ParseException e) {
            logger.error("alertflex_ctrl_exception", e);
        }

        try {

            if (message instanceof TextMessage) {

                TextMessage textMessage = (TextMessage) message;

                msg_type = textMessage.getIntProperty("msg_type");

                if (msg_type == 1) {
                    ref_id = textMessage.getStringProperty("ref_id");
                    node = textMessage.getStringProperty("node_id");
                    String agent = textMessage.getStringProperty("agent_name");
                    String json = textMessage.getText();

                    project = projectFacade.findProjectByRef(ref_id);
                    if (project != null) {

                        AgentsManagement am = new AgentsManagement(this);
                        am.saveAgentKey(agent, json);
                    }
                }
            }
        } catch (JMSException e) {
            logger.error("alertflex_ctrl_exception", e);
        }
    }

    public String decompress(byte[] compressed) throws IOException {
        String outStr = "";
        if ((compressed == null) || (compressed.length == 0)) {
            return "";
        }
        if (isCompressed(compressed)) {
            GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(compressed));
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(gis));

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
                sb.append('\n');
            }
            outStr = sb.toString();
        } else {
            outStr = new String(compressed);
        }
        return outStr;
    }

    public boolean isCompressed(byte[] compressed) {
        return (compressed[0] == (byte) (GZIPInputStream.GZIP_MAGIC)) && (compressed[1] == (byte) (GZIPInputStream.GZIP_MAGIC >> 8));
    }

    void createAlert(Alert a) {

        // save alert in MySQL 
        if (project.getSemActive() > 0) {

            alertFacade.create(a);

            // send alert for response processing
            if (project.getSemActive() == 2) {
                sendAlertToMQ(a);
            }
        }

        // send alert to log server
        if (elasticFromPool != null) {
            elasticFromPool.SendAlertToLog(a);
        }
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
            
            BytesMessage message = session.createBytesMessage();
            PojoAlertLogic pal = new PojoAlertLogic(a);
            
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            
            oos.writeObject(pal);
                
            oos.close();
            bos.close();
                
            message.writeBytes(bos.toByteArray());
            producer.send(message);

            // Clean up
            session.close();
            connection.close();

        } catch (JMSException e) {
            logger.error("alertflex_ctrl_exception", e);
        } catch (IOException e) {
            logger.error("alertflex_ctrl_exception", e);
        }
    }

}
