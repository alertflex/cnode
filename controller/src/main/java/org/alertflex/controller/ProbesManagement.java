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

import java.text.SimpleDateFormat;
import org.alertflex.supp.ProjectRepository;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import org.alertflex.entity.Hosts;
import org.alertflex.entity.Project;
import org.alertflex.entity.Node;
import org.alertflex.entity.NodePK;
import org.alertflex.entity.Probe;
import org.alertflex.entity.ProbePK;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

public class ProbesManagement {

    private static final Logger logger = LogManager.getLogger(ProbesManagement.class);

    private InfoMessageBean eventBean;
    Project project;
    Node node;

    ProjectRepository pr;

    public ProbesManagement(InfoMessageBean eb) {
        this.eventBean = eb;
        this.project = eventBean.getProject();
        this.pr = new ProjectRepository(project);
    }

    public void setStatus(String data) {
        
        try {

            String ref = project.getRefId();
            String nodeName = eventBean.getNode();
            String hostName = eventBean.getHost();
            
            Node node = eventBean.getNodeFacade().findByNodeName(ref, nodeName);
            pr.initNode(nodeName);
            
            if (node == null) {

                NodePK nodePK = new NodePK(ref, nodeName);
                node = new Node();
                node.setNodePK(nodePK);
                node.setDescription("change desc");
                node.setNodeType("indef");
                node.setVpc("indef");
                node.setNetAcl("indef");
                node.setCommandsControl(0);
                node.setFiltersControl(1);
                eventBean.getNodeFacade().create(node);
            }
            
            Hosts host = eventBean.getHostsFacade().findHost(ref, nodeName, hostName);
            
            if (host == null) {

                host = new Hosts();
                host.setName(hostName);
                host.setNode(nodeName);
                host.setRefId(ref);
                host.setCred("dummmy");
                host.setAddress("127.0.0.1");
                host.setPort(22);
                host.setDescription("change desc");
                host.setHostType("collector");
                host.setAgent("indef");
                host.setCloudInstance("indef");
                eventBean.getHostsFacade().create(host);
            }
            
            
            List<String> listTypesProbes = new ArrayList();
            listTypesProbes.add("crs");
            listTypesProbes.add("hids");
            listTypesProbes.add("nids");
            listTypesProbes.add("waf");
            listTypesProbes.add("aws-waf");
            listTypesProbes.add("ips");
            listTypesProbes.add("docker");
            listTypesProbes.add("k8s");
            listTypesProbes.add("trivy");
            listTypesProbes.add("kube-hunter");
            listTypesProbes.add("nmap");
            listTypesProbes.add("nuclei");
            listTypesProbes.add("zap");
            
            
            HashMap<String, String> probeTypeCastMap = new HashMap(0);
            probeTypeCastMap.put("crs", "Falco");
            probeTypeCastMap.put("hids", "Wazuh");
            probeTypeCastMap.put("nids", "Suricata");
            probeTypeCastMap.put("waf", "ModSecurity");
            probeTypeCastMap.put("aws-waf", "AwsWaf");
            probeTypeCastMap.put("ips", "SuricataIPS");
            probeTypeCastMap.put("docker", "Docker");
            probeTypeCastMap.put("k8s", "Kubernetes");
            probeTypeCastMap.put("trivy", "Trivy");
            probeTypeCastMap.put("kube-hunter", "KubeHunter");
            probeTypeCastMap.put("nmap", "Nmap");
            probeTypeCastMap.put("nuclei", "Nuclei");
            probeTypeCastMap.put("zap", "ZAP");
            
            
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            JSONObject probesStates = new JSONObject(data);
            JSONObject probesData = probesStates.getJSONObject("data");
            
            for (String tp : listTypesProbes) {
                
                int status = probesData.getInt(tp);
                String probeName = hostName + "." + tp;
                    
                Probe p = eventBean.getProbeFacade().findProbeByName(ref, nodeName, probeName);
                if (p == null) {
                    
                    ProbePK probePK = new ProbePK(ref, nodeName, probeName);
                    p = new Probe(probePK);
                    p.setDescription("Change me");
                    p.setProbeType(probeTypeCastMap.get(tp));
                    p.setHostName(hostName);
                    p.setStatusUpdated(date);
                    p.setStatus(status);
                    eventBean.getProbeFacade().create(p);
                } else {
                    
                    if (status != p.getStatus() && (status == 0 || p.getStatus() == 0)) {
                    
                        p.setStatus(status);
                        p.setStatusUpdated(date);
                        eventBean.getProbeFacade().edit(p);
                        // send alert
                        sendAlert(project, nodeName, probeName);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("alertflex_ctrl_exception", e);
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
        a.setDescription("Status of Probe: " + probe + " was changed");
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

        eventBean.getAlertFacade().create(a);

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
