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

import org.alertflex.posture.AppSecret;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.ejb.Stateless;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import java.text.ParseException;
import java.util.zip.GZIPInputStream;
import javax.inject.Inject;
import javax.jms.BytesMessage;
import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.alertflex.logserver.FromElasticPool;
import org.alertflex.entity.Alert;
import org.alertflex.entity.Project;
import org.alertflex.facade.AgentFacade;
import org.alertflex.facade.NetworksFacade;
import org.alertflex.facade.HostsFacade;
import org.alertflex.facade.AlertFacade;
import org.alertflex.facade.AttributesFacade;
import org.alertflex.facade.EventsFacade;
import org.alertflex.facade.NodeAlertsFacade;
import org.alertflex.facade.NodeMonitorFacade;
import org.alertflex.facade.ProjectFacade;
import org.alertflex.facade.AgentVulFacade;
import org.alertflex.facade.AgentScaFacade;
import org.alertflex.facade.AlertPriorityFacade;
import org.alertflex.facade.ContainerFacade;
import org.alertflex.facade.PodFacade;
import org.alertflex.facade.NodeFacade;
import org.alertflex.facade.PostureTaskFacade;
import org.alertflex.facade.PostureAppsecretFacade;
import org.alertflex.facade.PostureDockerconfigFacade;
import org.alertflex.facade.PostureK8sconfigFacade;
import org.alertflex.facade.PostureAppvulnFacade;
import org.alertflex.facade.PostureDockervulnFacade;
import org.alertflex.facade.PostureTerraformFacade;
import org.alertflex.facade.PostureK8svulnFacade;
import org.alertflex.facade.PostureCloudformationFacade;
import org.alertflex.facade.PostureKubehunterFacade;
import org.alertflex.facade.PostureZapFacade;
import org.alertflex.facade.ProbeFacade;
import org.alertflex.logserver.ElasticSearch;
import org.alertflex.logserver.FromGraylogPool;
import org.alertflex.logserver.GrayLog;
import org.alertflex.posture.AppSbom;
import org.alertflex.posture.AppVuln;
import org.alertflex.posture.CloudFormation;
import org.alertflex.posture.DockerConfig;
import org.alertflex.posture.DockerSbom;
import org.alertflex.posture.DockerVuln;
import org.alertflex.posture.K8sConfig;
import org.alertflex.posture.K8sVuln;
import org.alertflex.posture.KubeHunter;
import org.alertflex.posture.Terraform;
import org.alertflex.posture.Zap;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")
    ,
        @ActivationConfigProperty(propertyName = "destination", propertyValue = "jms/alertflex/info"),})
@Stateless
public class InfoMessageBean implements MessageListener {

    private static final Logger logger = LogManager.getLogger(InfoMessageBean.class);

    @Inject
    @FromElasticPool
    ElasticSearch elasticFromPool;
    
    @Inject
    @FromGraylogPool
    GrayLog graylogFromPool;

    @EJB
    private ProjectFacade projectFacade;
    Project project;
    String ref_id;

    @EJB
    private NodeFacade nodeFacade;
    String node;
    String host;
    
    @EJB
    private HostsFacade hostsFacade;
    
    @EJB
    private ProbeFacade probeFacade;

    @EJB
    private AlertFacade alertFacade;

    @EJB
    private AlertPriorityFacade alertPriorityFacade;

    @EJB
    private NodeMonitorFacade nodeMonitorFacade;

    @EJB
    private NodeAlertsFacade nodeAlertsFacade;

    @EJB
    private AgentFacade agentFacade;
    
    @EJB
    private AgentVulFacade agentVulFacade;

    @EJB
    private AgentScaFacade agentScaFacade;
    
    @EJB 
    private PodFacade podFacade;
    
    @EJB
    private ContainerFacade containerFacade;

    @EJB
    private NetworksFacade homeNetworkFacade;
    
    @EJB
    private AttributesFacade attributesFacade;

    @EJB
    private EventsFacade eventsFacade;
    
    @EJB
    private PostureTaskFacade postureTaskFacade;
    
    @EJB
    private PostureAppsecretFacade postureAppsecretFacade;
    
    @EJB
    private PostureDockerconfigFacade postureDockerconfigFacade;
    
    @EJB
    private PostureK8sconfigFacade postureK8sconfigFacade;
    
    @EJB
    private PostureDockervulnFacade postureDockervulnFacade;
    
    @EJB
    private PostureK8svulnFacade postureK8svulnFacade;
    
    @EJB
    private PostureAppvulnFacade postureAppvulnFacade;
    
    @EJB
    private PostureKubehunterFacade postureKubehunterFacade;
    
    @EJB
    private PostureTerraformFacade postureTerraformFacade;
    
    @EJB
    private PostureCloudformationFacade postureCloudformationFacade;
    
    @EJB
    private PostureZapFacade postureZapFacade;

    public AgentFacade getAgentFacade() {
        return this.agentFacade;
    }
    
    public ContainerFacade getContainerFacade() {
        return this.containerFacade;
    }

    public ElasticSearch getElasticFromPool() {
        return elasticFromPool;
    }
    
    public GrayLog getGraylogFromPool() {
        return graylogFromPool;
    }

    public NetworksFacade getHomeNetworkFacade() {
        return this.homeNetworkFacade;
    }
    
    public HostsFacade getHostsFacade() {
        return this.hostsFacade;
    }

    public NodeFacade getNodeFacade() {
        return this.nodeFacade;
    }
    
    public ProbeFacade getProbeFacade() {
        return this.probeFacade;
    }

    public String getNode() {
        return this.node;
    }
    
    public String getHost() {
        return this.host;
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

    public AgentVulFacade getAgentVulFacade() {
        return this.agentVulFacade;
    }

    public AgentScaFacade getAgentScaFacade() {
        return this.agentScaFacade;
    }
    
    public PostureTaskFacade getPostureTaskFacade() {
        return this.postureTaskFacade;
    }
    
    public PostureAppsecretFacade getPostureAppsecretFacade() {
        return this.postureAppsecretFacade;
    }
    
    public PostureDockerconfigFacade getPostureDockerconfigFacade() {
        return this.postureDockerconfigFacade;
    }
    
    public PostureK8sconfigFacade getPostureK8sconfigFacade() {
        return this.postureK8sconfigFacade;
    }
    
    public PostureAppvulnFacade getPostureAppvulnFacade() {
        return this.postureAppvulnFacade;
    }
    
    public PostureDockervulnFacade getPostureDockervulnFacade() {
        return this.postureDockervulnFacade;
    }
    
    public PostureK8svulnFacade getPostureK8svulnFacade() {
        return this.postureK8svulnFacade;
    }
    
    public PostureKubehunterFacade getPostureKubehunterFacade() {
        return this.postureKubehunterFacade;
    }
    
    public PostureTerraformFacade getPostureTerraformFacade() {
        return this.postureTerraformFacade;
    }
    
    public PostureCloudformationFacade getPostureCloudformationFacade() {
        return this.postureCloudformationFacade;
    }
    
    public PostureZapFacade getPostureZapFacade() {
        return this.postureZapFacade;
    }
    
    public PodFacade getPodFacade() {
        return this.podFacade;
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

        int msgType;
        String uuid;
        
        try {

            if (message instanceof BytesMessage) {

                BytesMessage bytesMessage = (BytesMessage) message;

                msgType = bytesMessage.getIntProperty("msg_type");

                if (msgType == 0) {
                    return;
                }
                
                ref_id = bytesMessage.getStringProperty("ref_id");
                project = projectFacade.findProjectByRef(ref_id);
                
                if (project != null) {
                    
                    node = bytesMessage.getStringProperty("node_id");
                    host = bytesMessage.getStringProperty("host_name");
                    
                    byte[] buffer = new byte[(int) bytesMessage.getBodyLength()];
                    bytesMessage.readBytes(buffer);

                    String data = "";

                    data = decompress(buffer);

                    if (data.isEmpty()) {
                        return;
                    }
                    
                    String target;

                    switch (msgType) {
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
                            ProbesManagement pm = new ProbesManagement(this);
                            pm.setStatus(data);
                            break;
                            
                        case 4: // appSecret
                            AppSecret appSecret = new AppSecret(this);
                            uuid = bytesMessage.getStringProperty("uuid");
                            target = bytesMessage.getStringProperty("target");
                            appSecret.saveReport(data, target, uuid);
                            break;

                        case 5: // dockerConfig
                            DockerConfig dockerConfig = new DockerConfig(this);
                            uuid = bytesMessage.getStringProperty("uuid");
                            target = bytesMessage.getStringProperty("target");
                            dockerConfig.saveReport(data, target, uuid);
                            break;
                            
                        case 6: // k8sConfig
                            K8sConfig k8sConfig = new K8sConfig(this);
                            uuid = bytesMessage.getStringProperty("uuid");
                            target = bytesMessage.getStringProperty("target");
                            k8sConfig.saveReport(data, target, uuid);
                            break;
                            
                        case 7: // appVuln
                            AppVuln appVuln = new AppVuln(this);
                            uuid = bytesMessage.getStringProperty("uuid");
                            target = bytesMessage.getStringProperty("target");
                            appVuln.saveReport(data, target, uuid);
                            break;

                        case 8: // dockerVuln
                            DockerVuln dockerVuln = new DockerVuln(this);
                            uuid = bytesMessage.getStringProperty("uuid");
                            target = bytesMessage.getStringProperty("target");
                            dockerVuln.saveReport(data, target, uuid);
                            break;
                            
                        case 9: // k8sVuln
                            K8sVuln k8sVuln = new K8sVuln(this);
                            uuid = bytesMessage.getStringProperty("uuid");
                            target = bytesMessage.getStringProperty("target");
                            k8sVuln.saveReport(data, target, uuid);
                            break;
                            
                        case 10: // appSbom
                            AppSbom appSbom = new AppSbom(this);
                            uuid = bytesMessage.getStringProperty("uuid");
                            target = bytesMessage.getStringProperty("target");
                            appSbom.saveReport(data, target, uuid);
                            break;
                            
                        case 11: // dockerSbom
                            DockerSbom dockerSbom = new DockerSbom(this);
                            uuid = bytesMessage.getStringProperty("uuid");
                            target = bytesMessage.getStringProperty("target");
                            dockerSbom.saveReport(data, target, uuid);
                            break;
                            
                        case 12: // cloudFormation
                            CloudFormation cloudFormation = new CloudFormation(this);
                            uuid = bytesMessage.getStringProperty("uuid");
                            target = bytesMessage.getStringProperty("target");
                            cloudFormation.saveReport(data, target, uuid);
                            break;
                            
                        case 13: // terraform
                            Terraform terraform = new Terraform(this);
                            uuid = bytesMessage.getStringProperty("uuid");
                            target = bytesMessage.getStringProperty("target");
                            terraform.saveReport(data, target, uuid);
                            break;
                            
                        case 14: // kube-hunter
                            KubeHunter kubeHunter = new KubeHunter(this);
                            uuid = bytesMessage.getStringProperty("uuid");
                            target = bytesMessage.getStringProperty("target");
                            kubeHunter.saveReport(data, target, uuid);
                            break;
                                                        
                        case 15: // zap
                            Zap zap = new Zap(this);
                            uuid = bytesMessage.getStringProperty("uuid");
                            target = bytesMessage.getStringProperty("target");
                            zap.saveReport(data, target, uuid);
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

                msgType = textMessage.getIntProperty("msg_type");

                if (msgType == 1) {
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

    public void createAlert(Alert a) {

        // save alert in MySQL 
        if (project.getSemActive() > 0) {

            alertFacade.create(a);
            sendAlertToMQ(a);
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
            javax.jms.Destination destination = session.createQueue("jms/alertflex/alerts");

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
            
        }
    }
}
