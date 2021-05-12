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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Calendar;
import javax.ejb.EJB;
import javax.ejb.ActivationConfigProperty;
import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.alertflex.entity.Project;
import org.alertflex.entity.Alert;
import org.alertflex.facade.AlertFacade;
import org.alertflex.facade.ProjectFacade;
import org.alertflex.facade.ResponseFacade;
import org.alertflex.entity.Response;
import org.alertflex.facade.AlertCategoryFacade;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.LoggerFactory;
import javax.ejb.MessageDriven;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.BytesMessage;
import javax.jms.MessageListener;
import org.alertflex.common.PojoAlertLogic;
import org.alertflex.logserver.ElasticSearch;
import org.alertflex.logserver.FromElasticPool;
import org.alertflex.logserver.FromGraylogPool;
import org.alertflex.logserver.GrayLog;


@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")
    ,
        @ActivationConfigProperty(propertyName = "destination", propertyValue = "jms/alertflex/alerts"),})
@Stateless
public class AlertsMessageBean implements MessageListener {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(AlertsMessageBean.class);

    @Inject
    @FromElasticPool
    ElasticSearch elasticFromPool;
    
    @Inject
    @FromGraylogPool
    GrayLog graylogFromPool;

    @EJB
    private ProjectFacade projectFacade;

    @EJB
    private AlertFacade alertFacade;

    @EJB
    private AlertCategoryFacade alertCategoryFacade;

    @EJB
    private ResponseFacade responseFacade;
    
    Alert a = null;
    
    @Override
    public void onMessage(Message message) {
        
        try {

            if (message instanceof TextMessage) {

                TextMessage textMessage = (TextMessage) message;

                int msg_type = ((TextMessage) message).getIntProperty("msg_type");

                if (msg_type != 1 && msg_type != 2) {
                    return;
                }

                String ref_id = ((TextMessage) message).getStringProperty("ref_id");

                Project project = projectFacade.findProjectByRef(ref_id);

                if (project != null) {

                    if (msg_type == 1) {

                        a = new Alert();

                        a.setRefId(ref_id);
                        a.setNodeId(((TextMessage) message).getStringProperty("node_id"));
                        a.setAlertUuid(((TextMessage) message).getStringProperty("alert_uuid"));
                        a.setAlertSource(((TextMessage) message).getStringProperty("alert_source"));
                        a.setAlertType(((TextMessage) message).getStringProperty("alert_type"));
                        a.setSensorId(((TextMessage) message).getStringProperty("sensor_id"));
                        a.setAlertSeverity(((TextMessage) message).getIntProperty("alert_severity"));
                        String desc = new String(((TextMessage) message).getStringProperty("description").getBytes("UTF-8"));
                        if (desc.length() >= 1024) {
                            String substrDesc = desc.substring(0, 1022);
                            a.setDescription(substrDesc);
                        } else {
                            a.setDescription(desc);
                        }
                        a.setEventId(((TextMessage) message).getStringProperty("event_id"));
                        int sev = ((TextMessage) message).getIntProperty("event_severity");
                        a.setEventSeverity(Integer.toString(sev));
                        String loc = ((TextMessage) message).getStringProperty("location");
                        if (loc.length() >= 1024) {
                            String substrLoc = loc.substring(0, 1022);
                            a.setLocation(substrLoc);
                        } else {
                            a.setLocation(loc);
                        }
                        a.setAction(((TextMessage) message).getStringProperty("action"));
                        a.setStatus(((TextMessage) message).getStringProperty("status"));
                        a.setFilter(((TextMessage) message).getStringProperty("filter"));
                        a.setInfo(((TextMessage) message).getStringProperty("info"));
                        a.setTimeEvent(((TextMessage) message).getStringProperty("event_time"));
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date = new Date();
                        date = formatter.parse(((TextMessage) message).getStringProperty("collr_time"));
                        a.setTimeCollr(date);
                        date = new Date();
                        a.setTimeCntrl(date);
                        a.setUserName(((TextMessage) message).getStringProperty("user_name"));
                        a.setAgentName(((TextMessage) message).getStringProperty("agent_name"));
                        a.setCategories(((TextMessage) message).getStringProperty("categories"));
                        a.setSrcIp(((TextMessage) message).getStringProperty("src_ip"));
                        a.setDstIp(((TextMessage) message).getStringProperty("dst_ip"));
                        a.setSrcHostname(((TextMessage) message).getStringProperty("src_hostname"));
                        a.setDstHostname(((TextMessage) message).getStringProperty("dst_hostname"));
                        a.setSrcPort(((TextMessage) message).getIntProperty("src_port"));
                        a.setDstPort(((TextMessage) message).getIntProperty("dst_port"));
                        a.setFileName(((TextMessage) message).getStringProperty("file_name"));
                        a.setFilePath(((TextMessage) message).getStringProperty("file_path"));
                        a.setHashMd5(((TextMessage) message).getStringProperty("hash_md5"));
                        a.setHashSha1(((TextMessage) message).getStringProperty("hash_sha1"));
                        a.setHashSha256(((TextMessage) message).getStringProperty("hash_sha256"));
                        a.setProcessId(((TextMessage) message).getIntProperty("process_id"));
                        a.setProcessName(((TextMessage) message).getStringProperty("process_name"));
                        a.setProcessCmdline(((TextMessage) message).getStringProperty("process_cmdline"));
                        a.setProcessPath(((TextMessage) message).getStringProperty("process_path"));
                        a.setUrlHostname(((TextMessage) message).getStringProperty("url_hostname"));
                        a.setUrlPath(((TextMessage) message).getStringProperty("url_path"));
                        a.setContainerId(((TextMessage) message).getStringProperty("container_id"));
                        a.setContainerName(((TextMessage) message).getStringProperty("container_name"));

                        // add json info to alert
                        if (project.getIncJson() == 1) {
                            a.setJsonEvent(((TextMessage) message).getText());
                        } else {
                            a.setJsonEvent("");
                        }

                        // enrich alert by new cat
                        List<String> newCats = alertCategoryFacade.findCatsByEvent(a.getAlertSource(), a.getEventId());
                        if (newCats != null && !newCats.isEmpty()) {

                            for (String cat : newCats) {
                                
                                if (a.getAlertSource().equals("Falco")) {
                                    
                                    switch (cat) {
                                        case "network":
                                            a.setAlertType("NET");
                                            break;
                                        case "file":
                                            a.setAlertType("FILE");
                                            break;
                                        case "filesystem":
                                            a.setAlertType("FILE");
                                            break;
                                        default:
                                            break;
                                    }
                                }

                                String c = a.getCategories() + ", " + cat;
                                a.setCategories(c);
                            }
                        }

                        if (project.getSemActive() > 0) {

                            if (project.getSemActive() == 2) {
                                searchResponse();
                            }
                            
                            if (!a.getStatus().equals("remove")) alertFacade.create(a);
                        }
                        
                        // send alert to log server
                        if (elasticFromPool != null) elasticFromPool.SendAlertToLog(a);
                        
                        // send alert to log server
                        if (graylogFromPool != null) graylogFromPool.SendAlertToLog(a);

                    } else {
                        
                        if (msg_type == 2) {
                            
                            String uuid = ((TextMessage) message).getStringProperty("alert_uuid");
                            
                            if (uuid != null && !uuid.isEmpty()) {
                            
                                a = alertFacade.findAlertByUuid(uuid);
                            
                                if (a != null) {
                                    
                                    if (project.getSemActive() == 2) {
                                    
                                        String oldStatus = a.getStatus();
                            
                                        searchResponse();
                                    
                                        if (a.equals("remove")) {
                                            alertFacade.remove(a);
                                        } else {
                                            if (!a.getStatus().equals("oldStatus")) alertFacade.edit(a);
                                        }
                                    }
                                    
                                    // send alert to log server
                                    if (elasticFromPool != null) elasticFromPool.SendAlertToLog(a);
                        
                                    // send alert to log server
                                    if (graylogFromPool != null) graylogFromPool.SendAlertToLog(a);
                                }
                            }
                        }
                    }
                }
            } 
        } catch (Exception e) {
            logger.error("alertflex_ctrl_exception", e);
        }
    }

    void searchResponse() {

        List<Response> rl = null;
        List<Response> responseList = new ArrayList<>();

        // check if field of action consists response profile for alert
        if (!a.getAction().equals("indef") && !a.getAction().isEmpty()) {
        
            Response r = responseFacade.findResponseForAction(a.getRefId(), a.getAlertSource(), a.getAction());

            if (r != null) {
                responseList.add(r);
            }
        }

        // check response for event
        rl = searchResponseForEvent(a);
        if (rl != null && !rl.isEmpty()) {
            responseList.addAll(rl);
        }

        // check response for cat
        if (!a.getCategories().isEmpty()) {

            String[] catArray = a.getCategories().split(", ");

            if (catArray != null && catArray.length > 0) {

                for (int i = 0; (i < catArray.length); i++) {

                    String cat = catArray[i];

                    rl = searchResponseForCat(cat);

                    if (rl != null && !rl.isEmpty()) {
                        responseList.addAll(rl);
                    }
                }
            }
        }

        if (responseList == null || responseList.isEmpty()) return;
       

        for (Response res : responseList) {
            if (res != null) {
                
                changeActionStatus(res);
                
                sendMqResponse(res);
            }
        }
    }
    
    public List<Response> searchResponseForEvent(Alert a) {

        List<Response> selectedResponse = new ArrayList<>();

        List<Response> resList = responseFacade.findResponseForEvent(a.getRefId(), a.getAlertSource(), a.getEventId());

        if (resList == null) {
            return null;
        }

        for (Response res : resList) {

            int severity = res.getAlertSeverity();
            if (severity != 0) {
                if (severity > a.getAlertSeverity()) {
                    continue;
                }
            }

            String parameter = res.getAlertAgent();
            if (!parameter.isEmpty() && !parameter.equals("indef")) {
                if (!parameter.equals(a.getAgentName())) {
                    continue;
                }
            }

            parameter = res.getAlertContainer();
            if (!parameter.isEmpty() && !parameter.equals("indef")) {
                if (!parameter.equals(a.getContainerName())) {
                    if (!parameter.equals(a.getContainerId())) {
                        continue;
                    }
                }
            }

            parameter = res.getAlertIp();
            if (!parameter.isEmpty() && !parameter.equals("indef")) {
                if (!parameter.equals(a.getSrcIp()) && !parameter.equals(a.getDstIp())) {
                    continue;
                }
            }

            parameter = res.getAlertUser();
            if (!parameter.isEmpty() && !parameter.equals("indef")) {
                if (!parameter.equals(a.getUserName())) {
                    continue;
                }
            }

            parameter = res.getAlertSensor();
            if (!parameter.isEmpty() && !parameter.equals("indef")) {
                if (!parameter.equals(a.getSensorId())) {
                    continue;
                }
            }

            parameter = res.getAlertFile();
            if (!parameter.isEmpty() && !parameter.equals("indef")) {
                if (!parameter.equals(a.getFileName())) {
                    continue;
                }
            }

            parameter = res.getAlertProcess();
            if (!parameter.isEmpty() && !parameter.equals("indef")) {
                if (!parameter.equals(a.getProcessName())) {
                    continue;
                }
            }

            parameter = res.getAlertRegex();
            if (!parameter.isEmpty() && !parameter.equals("indef")) {
                if (!a.getDescription().matches(parameter)) {
                    continue;
                }
            }

            int begin = res.getBeginHour();
            int end = res.getEndHour();
            if (begin == 0 && end == 0) {
                selectedResponse.add(res);
            } else {
                Calendar rightNow = Calendar.getInstance();
                int hour = rightNow.get(Calendar.HOUR_OF_DAY);
                if (begin <= hour && hour <= end) {
                    selectedResponse.add(res);
                }
            }
        }

        return selectedResponse;
    }

    public List<Response> searchResponseForCat(String c) {

        List<Response> selectedResponse = new ArrayList<>();

        List<Response> resList = responseFacade.findResponseForCat(a.getRefId(), a.getAlertSource(), c);

        if (resList == null) {
            return null;
        }

        for (Response res : resList) {

            int severity = res.getAlertSeverity();
            if (severity != 0) {
                if (severity > a.getAlertSeverity()) {
                    continue;
                }
            }

            String parameter = res.getAlertAgent();
            if (!parameter.isEmpty() && !parameter.equals("indef")) {
                if (!parameter.equals(a.getAgentName())) {
                    continue;
                }
            }

            parameter = res.getAlertContainer();
            if (!parameter.isEmpty() && !parameter.equals("indef")) {
                if (!parameter.equals(a.getContainerName())) {
                    if (!parameter.equals(a.getContainerId())) {
                        continue;
                    }
                }
            }

            parameter = res.getAlertIp();
            if (!parameter.isEmpty() && !parameter.equals("indef")) {
                if (!parameter.equals(a.getSrcIp()) && !parameter.equals(a.getDstIp())) {
                    continue;
                }
            }

            parameter = res.getAlertUser();
            if (!parameter.isEmpty() && !parameter.equals("indef")) {
                if (!parameter.equals(a.getUserName())) {
                    continue;
                }
            }

            parameter = res.getAlertSensor();
            if (!parameter.isEmpty() && !parameter.equals("indef")) {
                if (!parameter.equals(a.getSensorId())) {
                    continue;
                }
            }

            parameter = res.getAlertFile();
            if (!parameter.isEmpty() && !parameter.equals("indef")) {
                if (!parameter.equals(a.getFileName())) {
                    continue;
                }
            }

            parameter = res.getAlertProcess();
            if (!parameter.isEmpty() && !parameter.equals("indef")) {
                if (!parameter.equals(a.getProcessName())) {
                    continue;
                }
            }

            parameter = res.getAlertRegex();
            if (!parameter.isEmpty() && !parameter.equals("indef")) {
                if (!a.getDescription().matches(parameter)) {
                    continue;
                }
            }

            int begin = res.getBeginHour();
            int end = res.getEndHour();
            if (begin == 0 && end == 0) {
                selectedResponse.add(res);
            } else {
                Calendar rightNow = Calendar.getInstance();
                int hour = rightNow.get(Calendar.HOUR_OF_DAY);
                if (begin <= hour && hour <= end) {
                    selectedResponse.add(res);
                }
            }
        }

        return selectedResponse;
    }
    
        
    public void sendMqResponse(Response res) {
        
        PojoAlertLogic pal = new PojoAlertLogic(a);
        
        if ((!res.getPlaybook().isEmpty() && !res.getPlaybook().equals("indef")) 
            || (!res.getNotifyUsers().equals("indef") && !res.getNotifyUsers().isEmpty())
            || res.getAction().equals("thehive") || res.getSendSlack() != 0) {
                
            try {

                String strConnFactory = System.getProperty("AmqUrl", "");
                String user = System.getProperty("AmqUser", "");
                String pass = System.getProperty("AmqPwd", "");

                // Create a ConnectionFactory
                ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(strConnFactory);

                // Create a Connection
                Connection connectionResponse = connectionFactory.createConnection(user, pass);
                connectionResponse.start();

                // Create a Session
                Session sessionResponse = connectionResponse.createSession(false, Session.AUTO_ACKNOWLEDGE);

                // Create the destination (Topic or Queue)
                Destination destinationResponse = sessionResponse.createQueue("jms/alertflex/responses");

                // Create a MessageProducer from the Session to the Topic or Queue
                MessageProducer producer = sessionResponse.createProducer(destinationResponse);
                producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

                BytesMessage message = sessionResponse.createBytesMessage();

                message.setStringProperty("ref_id", pal.getRefId());
                message.setStringProperty("response_id", res.getResId());
            
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(bos);
                oos.writeObject(pal);
                
                oos.close();
                bos.close();
                
                message.writeBytes(bos.toByteArray());
                producer.send(message);

                // Clean up
                sessionResponse.close();
                connectionResponse.close();

            } catch (JMSException e) {
                logger.error("alertflex_ctrl_exception", e);
            } catch (IOException e) {
                logger.error("alertflex_ctrl_exception", e);
            }
        }
    }
    
    public void changeActionStatus(Response res) {

        if (res != null) {
            
            a.setAction(res.getResId());

            String status = res.getAction();
            
            // change status of alert
            if (status.equals("indef") || status.equals("thehive") || status.isEmpty()) {
                return;
            }

            switch (status) {

                case "confirm":
                    a.setStatus("confirmed");
                    break;

                case "incident":
                    a.setStatus("incident");
                    break;

                case "remove":
                    a.setStatus("remove");
                    break;

                default:
                    break;
            }
        }
    }
}
