/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alertflex.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
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
import org.alertflex.facade.EventCategoryFacade;
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
import org.alertflex.redis.FromJedisPool;
import redis.clients.jedis.Jedis;

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
    
    @Inject
    @FromJedisPool
    Jedis jedisFromPool;

    @EJB
    private ProjectFacade projectFacade;

    @EJB
    private AlertFacade alertFacade;

    @EJB
    private EventCategoryFacade eventCategoryFacade;

    @EJB
    private ResponseFacade responseFacade;
    
    PojoAlertLogic pal = null;
    
    @Override
    public void onMessage(Message message) {
        
        Alert a = null;

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

                            if (project.getSemActive() == 2) {
                                
                                pal = new PojoAlertLogic(a);
                                searchResponse();
                                a.setStatus(pal.getStatus());
                                a.setCategories(pal.getCategories());
                            }
                            
                            if (!a.getStatus().equals("remove")) alertFacade.create(a);

                            // send alert to log server
                            if (elasticFromPool != null) {
                                elasticFromPool.SendAlertToLog(a);
                            }

                            if (graylogFromPool != null) {
                                graylogFromPool.SendAlertToLog(a);
                            }
                        }

                    } else {
                        
                        if (message instanceof BytesMessage) {

                            BytesMessage msg = (BytesMessage) message;
                            
                            byte[] bytes = new byte[(int) msg.getBodyLength()];
                            msg.readBytes(bytes);
                
                            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
                            ObjectInputStream ois = new ObjectInputStream(bis);
                            pal = (PojoAlertLogic) ois.readObject();
                
                            ois.close();
                            bis.close();

                            if (pal != null) {
                                searchResponse();
                                a = alertFacade.findAlertByUuid(pal.getAlertUuid());
                                if (a != null) {
                                    if (pal.getStatus().equals("remove")) {
                                        alertFacade.remove(a);
                                    } else {
                                        if (!a.getStatus().equals(pal.getStatus()) ||
                                            !a.getCategories().equals(pal.getCategories())) {
                                            
                                            a.setStatus(pal.getStatus());
                                            a.setCategories(pal.getCategories());
                                            alertFacade.edit(a);
                                        }
                                    }
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
        if (!pal.getAction().equals("indef") && !pal.getAction().isEmpty()) {

            Response r = responseFacade.findResponseForAction(pal.getRefId(), pal.getAlertSource(), pal.getAction());

            if (r != null) {
                responseList.add(r);
            }
        }

        // check response for event
        rl = searchResponseForEvent();
        if (rl != null && !rl.isEmpty()) {
            responseList.addAll(rl);
        }

        // check response for cat
        if (!pal.getCategories().isEmpty()) {

            String[] catArray = pal.getCategories().split(", ");

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

        if (responseList == null || responseList.isEmpty()) {
            return;
        }

        for (Response res : responseList) {
            if (res != null) {
                createResponse(res);
            }
        }
    }
    
    public List<Response> searchResponseForEvent() {

        List<Response> selectedResponse = new ArrayList<>();

        List<Response> resList = responseFacade.findResponseForEvent(pal.getRefId(), pal.getAlertSource(), pal.getEventId());

        if (resList == null) {
            return null;
        }

        for (Response res : resList) {

            int severity = res.getAlertSeverity();
            if (severity != 0) {
                if (severity > pal.getAlertSeverity()) {
                    continue;
                }
            }

            String parameter = res.getAlertAgent();
            if (!parameter.isEmpty() && !parameter.equals("indef")) {
                if (!parameter.equals(pal.getAgentName())) {
                    continue;
                }
            }

            parameter = res.getAlertContainer();
            if (!parameter.isEmpty() && !parameter.equals("indef")) {
                if (!parameter.equals(pal.getContainerName())) {
                    if (!parameter.equals(pal.getContainerId())) {
                        continue;
                    }
                }
            }

            parameter = res.getAlertIp();
            if (!parameter.isEmpty() && !parameter.equals("indef")) {
                if (!parameter.equals(pal.getSrcIp()) && !parameter.equals(pal.getDstIp())) {
                    continue;
                }
            }

            parameter = res.getAlertUser();
            if (!parameter.isEmpty() && !parameter.equals("indef")) {
                if (!parameter.equals(pal.getUserName())) {
                    continue;
                }
            }

            parameter = res.getAlertSensor();
            if (!parameter.isEmpty() && !parameter.equals("indef")) {
                if (!parameter.equals(pal.getSensorId())) {
                    continue;
                }
            }

            parameter = res.getAlertFile();
            if (!parameter.isEmpty() && !parameter.equals("indef")) {
                if (!parameter.equals(pal.getFileName())) {
                    continue;
                }
            }

            parameter = res.getAlertProcess();
            if (!parameter.isEmpty() && !parameter.equals("indef")) {
                if (!parameter.equals(pal.getProcessName())) {
                    continue;
                }
            }

            parameter = res.getAlertRegex();
            if (!parameter.isEmpty() && !parameter.equals("indef")) {
                if (!pal.getDescription().matches(parameter)) {
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

        List<Response> resList = responseFacade.findResponseForCat(pal.getRefId(), pal.getAlertSource(), c);

        if (resList == null) {
            return null;
        }

        for (Response res : resList) {

            int severity = res.getAlertSeverity();
            if (severity != 0) {
                if (severity > pal.getAlertSeverity()) {
                    continue;
                }
            }

            String parameter = res.getAlertAgent();
            if (!parameter.isEmpty() && !parameter.equals("indef")) {
                if (!parameter.equals(pal.getAgentName())) {
                    continue;
                }
            }

            parameter = res.getAlertContainer();
            if (!parameter.isEmpty() && !parameter.equals("indef")) {
                if (!parameter.equals(pal.getContainerName())) {
                    if (!parameter.equals(pal.getContainerId())) {
                        continue;
                    }
                }
            }

            parameter = res.getAlertIp();
            if (!parameter.isEmpty() && !parameter.equals("indef")) {
                if (!parameter.equals(pal.getSrcIp()) && !parameter.equals(pal.getDstIp())) {
                    continue;
                }
            }

            parameter = res.getAlertUser();
            if (!parameter.isEmpty() && !parameter.equals("indef")) {
                if (!parameter.equals(pal.getUserName())) {
                    continue;
                }
            }

            parameter = res.getAlertSensor();
            if (!parameter.isEmpty() && !parameter.equals("indef")) {
                if (!parameter.equals(pal.getSensorId())) {
                    continue;
                }
            }

            parameter = res.getAlertFile();
            if (!parameter.isEmpty() && !parameter.equals("indef")) {
                if (!parameter.equals(pal.getFileName())) {
                    continue;
                }
            }

            parameter = res.getAlertProcess();
            if (!parameter.isEmpty() && !parameter.equals("indef")) {
                if (!parameter.equals(pal.getProcessName())) {
                    continue;
                }
            }

            parameter = res.getAlertRegex();
            if (!parameter.isEmpty() && !parameter.equals("indef")) {
                if (!pal.getDescription().matches(parameter)) {
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
    
    public void createResponse(Response res) {
        
        if (res != null) {

            // check if response should aggregated
            if (res.getAggrReproduced() != 0 && res.getAggrInperiod() != 0) {

                if (aggregateResponse(res)) {

                    pal.setStatus("aggregated");

                    changeActionStatus(res);
                    sendMqResponse(res);

                } else {

                    if (res.getAction().equals("remove")) {
                        pal.setStatus("remove");
                    }

                }
            } else {
                
                changeActionStatus(res);
                sendMqResponse(res);

            }
        }
        
    }
    
    public boolean aggregateResponse(Response res) {

        // return true if aggregation is finished and alert should be arised
        if (jedisFromPool != null) {

            String key = res.getRefId();
            String fieldReproduced = "reproduced_" + res.getResId();
            String fieldPeriod = "period_" + res.getResId();
            long periodResponse = (long) res.getAggrInperiod();
            long reproducedResponse = (long) res.getAggrReproduced();

            long currentTime = (new Date()).getTime();
            long counterTime = 0;
            long counter = 0;

            boolean firstTime = true;

            if (jedisFromPool.hexists(key, fieldReproduced)) {
                firstTime = false;
            }

            if (jedisFromPool.hexists(key, fieldPeriod)) {
                firstTime = false;
            }

            if (!firstTime) {

                String value = jedisFromPool.hget(key, fieldReproduced);
                if (!value.equals("nil")) {
                    counter = Long.parseLong(value);
                }

                value = jedisFromPool.hget(key, fieldPeriod);
                if (!value.equals("nil")) {
                    counterTime = Long.parseLong(value);
                }
            }

            if (!firstTime && counter != 0 && counterTime != 0) {

                counter++;
                counterTime = counterTime + periodResponse * 1000;

                if (counter >= res.getAggrReproduced()) {

                    //clear hash
                    jedisFromPool.hdel(key, fieldReproduced);
                    jedisFromPool.hdel(key, fieldPeriod);

                    if (currentTime <= counterTime) {
                        return true;
                    }

                    jedisFromPool.hincrBy(key, fieldReproduced, 1);
                    jedisFromPool.hincrBy(key, fieldPeriod, currentTime);

                } else {

                    if (currentTime > counterTime) {
                        //clear hash
                        jedisFromPool.hdel(key, fieldReproduced);
                        jedisFromPool.hdel(key, fieldPeriod);
                        // create hash
                        jedisFromPool.hincrBy(key, fieldReproduced, 1);
                        jedisFromPool.hincrBy(key, fieldPeriod, currentTime);

                    } else {
                        // increase counter
                        jedisFromPool.hincrBy(key, fieldReproduced, 1);
                    }
                }

            } else {
                //clear hash
                jedisFromPool.hdel(key, fieldReproduced);
                jedisFromPool.hdel(key, fieldPeriod);
                // create hash
                jedisFromPool.hincrBy(key, fieldReproduced, 1);
                jedisFromPool.hincrBy(key, fieldPeriod, currentTime);
            }
        }

        return false;
    }

    public void sendMqResponse(Response res) {
        
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
            
            pal.setAction(res.getResId());

            String status = res.getAction();
            // change status of alert
            if (status.equals("indef") || status.equals("thehive") || status.isEmpty()) {
                return;
            }

            switch (status) {

                case "confirm":
                    pal.setStatus("confirmed");
                    break;

                case "incident":
                    pal.setStatus("incident");
                    break;

                case "remove":
                    pal.setStatus("remove");
                    break;

                default:
                    break;
            }
        }
    }
}
