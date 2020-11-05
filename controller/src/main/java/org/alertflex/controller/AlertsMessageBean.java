/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alertflex.controller;

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
import org.alertflex.facade.EventCategoryFacade;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.LoggerFactory;
import javax.ejb.MessageDriven;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.MessageListener;
import org.alertflex.logserver.ElasticSearch;
import org.alertflex.logserver.FromElasticPool;
import org.alertflex.logserver.FromGraylogPool;
import org.alertflex.logserver.GrayLog;

/**
 * @author Oleg Zharkov
 */
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
    private EventCategoryFacade eventCategoryFacade;

    @EJB
    private ResponseFacade responseFacade;

    @Override
    public void onMessage(Message message) {

        int msg_type = 0;
        Alert a = null;

        try {

            if (message instanceof TextMessage) {

                TextMessage textMessage = (TextMessage) message;

                msg_type = ((TextMessage) message).getIntProperty("msg_type");

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
                            loc.substring(0, 1022);
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
                        List<String> newCats = eventCategoryFacade.findCatsByEvent(a.getAlertSource(), a.getEventId());
                        if (newCats != null && !newCats.isEmpty()) {

                            for (String cat : newCats) {

                                String c = a.getCategories() + ", " + cat;
                                a.setCategories(c);
                            }
                        }

                        switch (a.getStatus()) {
                            case "processed_new":
                                a.setStatus("processed");
                                break;
                            case "modified_new":
                                a.setStatus("modified");
                                break;
                            case "aggregated_new":
                                a.setStatus("aggregated");
                                break;
                            default:
                                break;
                        }

                        if (project.getSemActive() > 0) {

                            alertFacade.create(a);

                            if (project.getSemActive() == 2) {
                                searchResponse(a);
                            }

                            // send alert to log server
                            if (elasticFromPool != null) {
                                elasticFromPool.SendAlertToLog(a);
                            }

                            if (graylogFromPool != null) {
                                graylogFromPool.SendAlertToLog(a);
                            }
                        }

                    } else {

                        String a_uuid = ((TextMessage) message).getStringProperty("alert_uuid");
                        a = alertFacade.findAlertByUuid(a_uuid);

                        if (a != null && project.getSemActive() == 2) {
                            searchResponse(a);
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("alertflex_ctrl_exception", e);
        }
    }

    void searchResponse(Alert a) {

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
        rl = getResponseForEvent(a);
        if (rl != null && !rl.isEmpty()) {
            responseList.addAll(rl);
        }

        // check response for cat
        if (!a.getCategories().isEmpty()) {

            String[] catArray = a.getCategories().split(", ");

            if (catArray != null && catArray.length > 0) {

                for (int i = 0; (i < catArray.length); i++) {

                    String cat = catArray[i];

                    rl = getResponseForCat(a, cat);

                    if (rl != null && !rl.isEmpty()) {
                        responseList.addAll(rl);
                    }
                }
            }
        }

        if (responseList == null || responseList.isEmpty()) {
            return;
        }

        for (Response res : responseList) {
            if (res != null) {
                CreateResponse(res, a);
            }
        }
    }

    public List<Response> getResponseForEvent(Alert a) {

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

    public List<Response> getResponseForCat(Alert a, String c) {

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

    public void CreateResponse(Response res, Alert a) {

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

            TextMessage message = sessionResponse.createTextMessage();

            message.setStringProperty("ref_id", a.getRefId());
            message.setStringProperty("response_id", res.getResId());
            message.setStringProperty("alert_uuid", a.getAlertUuid());
            message.setText("empty");
            producer.send(message);

            // Clean up
            sessionResponse.close();
            connectionResponse.close();

        } catch (JMSException e) {
            logger.error("alertflex_ctrl_exception", e);
        }
    }
}
