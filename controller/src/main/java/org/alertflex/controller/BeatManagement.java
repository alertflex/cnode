/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alertflex.controller;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import static java.nio.charset.StandardCharsets.UTF_8;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.EJB;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerService;
import javax.annotation.PreDestroy;
import static javax.ejb.ConcurrencyManagementType.CONTAINER;
import javax.jms.BytesMessage;
import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.alertflex.entity.Project;
import org.alertflex.entity.Alert;
import org.alertflex.facade.AlertFacade;
import org.alertflex.facade.ProjectFacade;
import org.alertflex.common.Netflow;
import org.alertflex.entity.AlertPriority;
import org.alertflex.entity.Attributes;
import org.alertflex.entity.Events;
import org.alertflex.facade.AgentFacade;
import org.alertflex.facade.AlertPriorityFacade;
import org.alertflex.facade.AttributesFacade;
import org.alertflex.facade.EventsFacade;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;


/**
 * @author Oleg Zharkov
 */
@Singleton (name="beatManagement")
@ConcurrencyManagement(CONTAINER)
@Startup
public class BeatManagement  {
    
    @Resource
    private TimerService timerService;
    
    @EJB
    private ProjectFacade projectFacade;
    List<Project> projectList;
    Project prj = null;
    
    @EJB
    private AlertFacade alertFacade;
    
    @EJB
    private AlertPriorityFacade alertPriorityFacade;
    
    ActiveMQConnectionFactory connectionFactory = null;
    Connection connection = null;
    Session session = null;
    Destination destination = null;
    MessageConsumer consumer = null;
    String strConnFactory = "";
    String user = "";
    String pass = "";
    
    @EJB
    private AgentFacade agentFacade;
        
    @EJB
    private EventsFacade eventsFacade;
    
    @EJB
    private AttributesFacade attributesFacade;
    
    static final String misp_ipsrc = "ip-src"; 
    Map<String, Attributes> ipsrcMap = new HashMap();
    Map<String, Attributes> processIpsrcMap = new HashMap();
    
    static final String misp_ipdst = "ip-dst"; 
    Map<String, Attributes> ipdstMap = new HashMap();
    Map<String, Attributes> processIpdstMap = new HashMap();
    
    static final String misp_dns = "domain";
    Map<String, Attributes> dnsMap = new HashMap();
    
    static final String misp_md5 = "md5";
    static final String misp_filename_md5 = "filename|md5";
    static final String misp_sha1 = "sha1";
    static final String misp_filename_sha1 = "filename|sha1";
    
    static final long INTERVAL_WAIT_PACKETS = 100;
    static final int INTERVAL_CHECK_PACKETS = 1000;
    static final int SIZE_NETFLOW_ARRAY = 100;
    static final int COUNTER_TICS_THRESHOLD = 100;
    static final int MAPS_STORE_THRESHOLD = 1000;
    
    int counterTics = 0;
    int counterMaps = 0;
        
    List<Netflow> netflowList = null;
        
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(BeatManagement.class);
    
    @PostConstruct
    public void init() {
        
        netflowList = new ArrayList<Netflow>();
        
        try {
            
            strConnFactory = System.getProperty("AmqUrl", "");
            user = System.getProperty("AmqUser", "");
            pass = System.getProperty("AmqPwd", "");
            
            // Create a ConnectionFactory
            connectionFactory = new ActiveMQConnectionFactory(strConnFactory);
            
            // Create a Connection
            connection = connectionFactory.createConnection(user,pass);
            connection.start();

            // Create a Session
            session = connection.createSession(false, javax.jms.Session.AUTO_ACKNOWLEDGE);

            // Create the destination (Topic or Queue)
            Destination destination = session.createQueue("jms/logstash");
            
            // MessageConsumer is used for receiving (consuming) messages
            consumer = session.createConsumer(destination);
            
            timerService.createTimer(1, INTERVAL_CHECK_PACKETS, "beatManagement");
            
        } catch ( JMSException e) {
            logger.error("alertflex_ctrl_exception", e);
        } 
        
    }
    
    
    @PreDestroy
    public void destroy() {
        // Clean up
        try {
            session.close();
            connection.close();
        } catch ( JMSException e) {
            logger.error("alertflex_ctrl_exception", e);
        } 
    }
    
    @Lock(LockType.WRITE)
    @Timeout
    public void runBeatManagement (Timer timer) {
        
        Message message = null;
        Alert a = null;
        
        try {
            
            if(netflowList.size() > 0 && counterTics > COUNTER_TICS_THRESHOLD) {
                checkIOC();
                counterTics = 0;
            } else counterTics++;
            
            if(counterMaps > MAPS_STORE_THRESHOLD) {
                ipsrcMap = new HashMap();
                processIpsrcMap = new HashMap();
                ipdstMap = new HashMap();
                processIpdstMap = new HashMap();
                dnsMap = new HashMap();
                counterMaps = 0;
                
            } else counterMaps++;
            
            do {
                // read alerts
                message = consumer.receive(INTERVAL_WAIT_PACKETS);
 
                if (message != null && message instanceof BytesMessage) {
                    
                    BytesMessage bytesMessage = (BytesMessage) message;
            
                    byte[] buffer = new byte[(int)bytesMessage.getBodyLength()];
            
                    bytesMessage.readBytes(buffer);
            
                    String text = new String(buffer, UTF_8);
            
                    handleMessage(text);
                }
                
            } while (message != null); 
            
            if (netflowList.size() > SIZE_NETFLOW_ARRAY) {
                checkIOC();
                netflowList = new ArrayList<Netflow>();
            }
        
        } catch (Exception e) {
            logger.error("alertflex_ctrl_exception", e);
        }
    }  
    
    
    void handleMessage(String record) {
        
        JSONObject obj = new JSONObject(record);
        
        String filebeat = "";
        String packetbeat = "";
        String eventCategory = "";
        String eventAction = "";
        String typeNet = "";
        
        if (obj.has("input")) {
            JSONObject input = obj.getJSONObject("input");
            if(input.has("type")) filebeat = input.getString("type");
        }
        
        if (obj.has("type")) packetbeat = obj.getString("type");
        
        // if syslog, get event and translate to alert
        if (filebeat.equals("syslog")) {
            
            JSONObject project = obj.getJSONObject("project");
            JSONObject agent = obj.getJSONObject("agent");
            JSONObject log = obj.getJSONObject("log");
            JSONObject syslog = obj.getJSONObject("syslog");
                        
            String refId = project.getString("ref_id");
            String node = project.getString("node");
            String hostname = agent.getString("hostname");
            String timestamp = obj.getString("@timestamp");
            String message = obj.getString("message");
            
            // source of syslog
            JSONObject source = log.getJSONObject("source");
            String address = "indef";
            if(source.has("address")) address = source.getString("address");
            
            //score, severity, process
            
            Integer score = syslog.getInt("priority");
            String severity = syslog.getString("severity_label");
            String facility = syslog.getString("facility_label");
            
            // save alert
            prj = projectFacade.findProjectByRef(refId);
        
            if (prj != null ) {
                
                AlertPriority ap = alertPriorityFacade.findPriorityBySource(refId, "Syslog");
            
                int sev = ap.getSeverityDefault();
            
                if (!severity.isEmpty() && ap != null) {
            
                    if(ap.getText1().equals(severity)) {
                        sev = ap.getValue1();
                    } else if(ap.getText2().equals(severity)) {
                        sev = ap.getValue2();
                    } else if(ap.getText3().equals(severity)) {
                        sev = ap.getValue3();
                    } else if(ap.getText4().equals(severity)) {
                        sev = ap.getValue4();
                    } else if(ap.getText5().equals(severity)) {
                        sev = ap.getValue5();
                    } 
                
                    if (sev >= ap.getSeverityThreshold()) { 
                        
                        Alert a = new Alert();
                
                        a.setRefId(refId);
                        a.setNodeId(node);
                        a.setAlertUuid(UUID.randomUUID().toString());
                        a.setAlertSource("Syslog");
                        a.setAlertType("MISC");
                        a.setSensorId("filebeat");
                
                        a.setAlertSeverity(sev);
                        a.setEventSeverity(severity);
                
                        a.setDescription(message);
                        a.setEventId("1");
                        a.setLocation(address);
                        a.setAction("indef");
                        a.setStatus("processed");
                        a.setFilter("");
                        a.setInfo(severity);
                        a.setTimeEvent(timestamp);
                        Date date = new Date(); 
                        a.setTimeCollr(date);
                        a.setTimeCntrl(date);
                        a.setAgentName(hostname);
                        a.setUserName("indef");
            
                        a.setCategories(facility);
                        a.setSrcIp("indef");
                        a.setDstIp("indef");
                        a.setDstPort(0);
                        a.setSrcPort(0);
                        a.setSrcHostname("");
                        a.setDstHostname("");
                        a.setFileName("indef");
                        a.setFilePath("indef");
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
                        a.setJsonEvent("indef");
                
                        // save alert in MySQL 
                        if (prj.getSemActive() > 0) {
                    
                            alertFacade.create(a);
                    
                            // send alert for response processing to Worker
                            if (prj.getSemActive() >= 2) {
                                sendAlertToMQ(a);
                            }
                        }
                    }
                }
            }  
            
            return;
        }
        
        if (!filebeat.equals("netflow") && packetbeat.equals("flow")) return;
        
        if (obj.has("event")) {
        
            JSONObject event = obj.getJSONObject("event");
            
        
            if (event.has("category")) eventCategory = event.getString("category");
        
            if (event.has("action")) eventAction = event.getString("action");
        
        }
        
        if (eventAction.equals("network_flow") && eventCategory.equals("network_traffic")) {
            
            JSONObject project = obj.getJSONObject("project");
            JSONObject agent = obj.getJSONObject("agent");
            JSONObject source = obj.getJSONObject("source");
            JSONObject dest = obj.getJSONObject("destination");
            JSONObject network = obj.getJSONObject("network");
            
            String refId = project.getString("ref_id");
            String node = project.getString("node");
            String hostName = agent.getString("hostname");
            String protocol = network.getString("transport");
            String timestamp = obj.getString("@timestamp");
            
            String dstIp = dest.getString("ip");
            int dstPort = dest.getInt("port");
            
            String srcIp = source.getString("ip");
            int srcPort = source.getInt("port");
            
            String process = "indef";
            int pid = 0;
            String path = "indef";
            if (obj.has("process")) {
                
                JSONObject proc = obj.getJSONObject("process");
                
                process = proc.getString("name");
                pid = proc.getInt("pid");
                // path = proc.getString("executable");
            }
            
            int packets = network.getInt("packets");
            int bytes = network.getInt("bytes");
            
            // send netflow to log server
            if (prj == null || !prj.getRefId().equals(refId)) {
                prj = projectFacade.findProjectByRef(refId);
            }
            
            String dstAgent = getAgent(refId, node, dstIp);
            String srcAgent = getAgent(refId, node, srcIp);
            
            Netflow nf = new Netflow(refId, node, hostName, timestamp, protocol, process, pid, 
                path, dstIp, dstAgent, dstPort, srcIp, srcAgent, srcPort, packets, bytes);
            
            netflowList.add(nf);
            
            // save alert in MySQL and send to Redis   
            if (prj.getSendNetflow() > 0) {
                    
                // send netflow to log server
                LogServer ls = new LogServer(prj);
                ls.SendNetflowToLog(nf);
                ls.close();
            }
            
        }
    }
    
    
    void checkIOC() {
        
        Attributes attr;
        String artifacts;
        int alertType;
        
        for (Netflow nf: netflowList) {
            
            String dstip = nf.getDstIp();
                    
            if (!ipdstMap.containsKey(dstip) && isPublicIp(dstip)) {
                
                attr = attributesFacade.findByValueAndType(dstip,misp_ipdst);
                ipdstMap.put(dstip, attr);
                        
                if (attr != null) {
                    
                    alertType = 25;
                            
                    artifacts = "{\"artifacts\": [{\"dataType\": \"ip\",\"data\":\""
                        + dstip
                        + "\",\"message\": \"destination ip\" }]}";
                            
                    createIocAlert(alertType, nf, attr, artifacts);
                }
            } 
            
            String srcip = nf.getSrcIp();
                    
            if (!ipsrcMap.containsKey(srcip) && isPublicIp(srcip)) {
                
                attr = attributesFacade.findByValueAndType(srcip,misp_ipsrc);
                ipsrcMap.put(srcip, attr);
                        
                if (attr != null) {
                    
                    alertType = 26;
                            
                    artifacts = "{\"artifacts\": [{\"dataType\": \"ip\",\"data\":\""
                        + srcip
                        + "\",\"message\": \"source ip\" }]}";
                            
                    createIocAlert(alertType, nf, attr, artifacts);
                }
            }
        }
    }
    
    
    public void createIocAlert (int alertType, Netflow nf, Attributes attr, String artifacts) {
        
        // send alert to log server
        if (prj == null || !prj.getRefId().equals(nf.getRefId())) {
            prj = projectFacade.findProjectByRef(nf.getRefId());
        }
        
        if (prj != null ) {
                
            Alert a = new Alert();
            
            a.setRefId(nf.getRefId());
            a.setNodeId(nf.getNode());
            a.setAlertUuid(UUID.randomUUID().toString());
        
            Events event = eventsFacade.findById(attr.getEventId());
        
            int srv = 0;
            if(event != null) {
                
                srv = event.getThreatLevelId();
                
                a.setEventSeverity(Integer.toString(srv));
                
                switch(srv) {
                    case 1:
                        srv = 3;
                        break;
                    case 2: 
                        srv = 2;
                        break;
                    case 3:
                        srv = 1;
                        break;
                    default:
                        srv = 0;
                        break;
                }
            
                a.setAlertSeverity(srv);
            } else {
                a.setAlertSeverity(4);
            }
        
            switch (alertType) {
            
                case 25:
                    a.setEventId("22");
                    a.setCategories("ipdst, " + attr.getCategory());
                    if (event != null) a.setDescription("Alertflex netflow event, suspicious dst ip - " + attr.getValue1() + ". " + event.getInfo());
                    else a.setDescription("Alertflex netflow event, suspicious dst ip - " + attr.getValue1() + ". ");
                    a.setAlertSource("MISP");
                    a.setAlertType("NET");
                
                    break;
                case 26:
                    a.setEventId("26");
                    a.setCategories("ipsrc, " + attr.getCategory());
                    if (event != null) a.setDescription("Alertflex netflow event, suspicious src ip - " + attr.getValue1() + ". " + event.getInfo());
                    else a.setDescription("Alertflex netflow event, suspicious src ip - " + attr.getValue1() + ". ");
                    a.setAlertSource("MISP");
                    a.setAlertType("NET");
                
                    break; 
            }
            
            a.setSensorId(nf.getHostname());
            a.setLocation("indef");
            a.setAction("indef");
            a.setStatus("processed");
            a.setFilter("");
            a.setInfo(artifacts);
            a.setTimeEvent(nf.getTimestamp());
            Date date = new Date(); 
            a.setTimeCollr(date);
            a.setTimeCntrl(date);
            a.setAgentName(nf.getHostname());
            a.setUserName("indef");
            
            a.setSrcIp(nf.getSrcIp());
            a.setDstIp(nf.getDstIp());
            a.setDstPort(nf.getDstPort());
            a.setSrcPort(nf.getSrcPort());
            a.setSrcHostname(nf.getSrcHostname());
            a.setDstHostname(nf.getDstHostname());
            a.setFileName("indef");
            a.setFilePath("indef");
            a.setHashMd5("indef");
            a.setHashSha1("indef");
            a.setHashSha256("indef");
            a.setProcessId(nf.getProcessId());
            a.setProcessName(nf.getProcessName());
            a.setProcessCmdline("indef");
            a.setProcessPath(nf.getProcessPath());
            a.setUrlHostname("indef");
            a.setUrlPath("indef");
            a.setContainerId("indef");
            a.setContainerName("indef");
            a.setJsonEvent("indef");
                    
            // save alert in MySQL and send to Redis   
            if (prj.getSemActive() > 0) {
                    
                alertFacade.create(a);
                    
                // send alert for response processing to Worker
                if (prj.getSemActive() >= 2) {
                    sendAlertToMQ(a);
                }
            }
        }
    }
    
    public void sendAlertToMQ(Alert a) {
        
        try {
            
            strConnFactory = System.getProperty("AmqUrl", "");
            user = System.getProperty("AmqUser", "");
            pass = System.getProperty("AmqPwd", "");
            
            // Create a ConnectionFactory
            ActiveMQConnectionFactory connectionFactoryProducer = new ActiveMQConnectionFactory(strConnFactory);
            
            // Create a Connection
            Connection connectionProducer = connectionFactoryProducer.createConnection(user,pass);
            connectionProducer.start();

            // Create a Session
            Session sessionProducer = connectionProducer.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // Create the destination (Topic or Queue)
            Destination destinationProducer = sessionProducer.createQueue("jms/alertflex/alerts");

            // Create a MessageProducer from the Session to the Topic or Queue
            MessageProducer producer = sessionProducer.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
            
            TextMessage message = session.createTextMessage();
            
            message.setIntProperty("msg_type", 2);
            message.setStringProperty("ref_id", a.getRefId());
            message.setStringProperty("alert_uuid", a.getAlertUuid());
            message.setText("empty");
            producer.send(message);
            
            // Clean up
            sessionProducer.close();
            connectionProducer.close();
            
                
        } catch ( JMSException e) {
            e.printStackTrace();
        } 
    }
    
    private String getAgent(String ref, String node, String ip) {
        
        if (!isPublicIp(ip)) {
            String a = agentFacade.findAgentByIP(ref, node, ip);
            if (!a.isEmpty()) return a;
        }
        
        return "indef";
    }
    
    private boolean isPublicIp(String ip) {
        
        Inet4Address address;
        
        try {
            address = (Inet4Address) InetAddress.getByName(ip);
        } catch (UnknownHostException exception) {
            return false; // assuming no logging, exception handling required
        }
        
        return !(address.isSiteLocalAddress() || 
             address.isAnyLocalAddress()  || 
             address.isLinkLocalAddress() || 
             address.isLoopbackAddress() || 
             address.isMulticastAddress());
    }
}

