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
import java.util.UUID;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
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
import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.alertflex.entity.AlertCategory;
import org.alertflex.entity.CatProfile;
import org.alertflex.entity.Project;
import org.alertflex.entity.Alert;
import org.alertflex.facade.AlertFacade;
import org.alertflex.facade.AlertCategoryFacade;
import org.alertflex.facade.CatProfileFacade;
import org.alertflex.facade.ProjectFacade;
import org.alertflex.facade.ResponseFacade;
import org.alertflex.entity.Response;
import org.alertflex.facade.EventCategoryFacade;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.LoggerFactory;
import static javax.ejb.ConcurrencyManagementType.CONTAINER;
import javax.inject.Inject;


/**
 * @author Oleg Zharkov
 */
@Singleton (name="alertsManagement")
@ConcurrencyManagement(CONTAINER)
@Startup
public class AlertsManagement  {
    
    @Resource
    private TimerService timerService;
    
    @EJB
    private ProjectFacade projectFacade;
    List<Project> projectList;
    
    @EJB
    private AlertFacade alertFacade;
    
    @EJB
    private EventCategoryFacade eventCategoryFacade;
    
    @EJB
    private CatProfileFacade catProfileFacade;
    
    Map<String,List<CatProfile>> catProfileAlertflexMap;
    Map<String,List<CatProfile>> catProfileCuckooMap;
    Map<String,List<CatProfile>> catProfileFalcoMap;
    Map<String,List<CatProfile>> catProfileHybridAnalysisMap;
    Map<String,List<CatProfile>> catProfileNmapMap;
    Map<String,List<CatProfile>> catProfileMiscMap;
    Map<String,List<CatProfile>> catProfileMISPMap;
    Map<String,List<CatProfile>> catProfileModSecurityMap;
    Map<String,List<CatProfile>> catProfileRITAMap;
    Map<String,List<CatProfile>> catProfileSuricataMap;
    Map<String,List<CatProfile>> catProfileSonarQubeMap;
    Map<String,List<CatProfile>> catProfileSyslogMap;
    Map<String,List<CatProfile>> catProfileVmrayMap;
    Map<String,List<CatProfile>> catProfileWazuhMap;
    Map<String,List<CatProfile>> catProfileZAPMap;
    
    @EJB
    private AlertCategoryFacade alertsCategoryFacade;
    
    Map<String,List<String>> catAlertflexMap;
    Map<String,List<String>> catCuckooMap;
    Map<String,List<String>> catFalcoMap;
    Map<String,List<String>> catHybridAnalysisMap;
    Map<String,List<String>> catNmapMap;
    Map<String,List<String>> catMiscMap;
    Map<String,List<String>> catMISPMap;
    Map<String,List<String>> catModSecurityMap;
    Map<String,List<String>> catRITAMap;
    Map<String,List<String>> catSonarQubeMap;
    Map<String,List<String>> catSuricataMap;
    Map<String,List<String>> catSyslogMap;
    Map<String,List<String>> catVmrayMap;
    Map<String,List<String>> catWazuhMap;
    Map<String,List<String>> catZAPMap;
            
    String[] catArray;
    
    @EJB
    private ResponseFacade responseFacade;
    
    Map<String,List<Response>> responseForEventMap;
    Map<String,List<Response>> responseForCatMap;
    
    Response defaultResponse;
    
    static final long INTERVAL_WAIT_ALERTS = 10;
    static final int INTERVAL_CHECK_ALERTS = 100;
    static final int INTERVAL_FRESH_CAT_PROFILE = 600; //2 min or 120 response
    int counterFreshCatProfiles = 0;
    
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(AlertsManagement.class);
    
    public AlertsManagement () {
        
        catProfileAlertflexMap = new HashMap<String,List<CatProfile>>();
        catProfileCuckooMap = new HashMap<String,List<CatProfile>>();
        catProfileFalcoMap = new HashMap<String,List<CatProfile>>();
        catProfileHybridAnalysisMap = new HashMap<String,List<CatProfile>>();
        catProfileMiscMap = new HashMap<String,List<CatProfile>>();
        catProfileMISPMap = new HashMap<String,List<CatProfile>>();
        catProfileModSecurityMap = new HashMap<String,List<CatProfile>>();
        catProfileRITAMap = new HashMap<String,List<CatProfile>>();
        catProfileSonarQubeMap = new HashMap<String,List<CatProfile>>();
        catProfileSuricataMap = new HashMap<String,List<CatProfile>>();
        catProfileSyslogMap = new HashMap<String,List<CatProfile>>();
        catProfileVmrayMap = new HashMap<String,List<CatProfile>>();
        catProfileWazuhMap = new HashMap<String,List<CatProfile>>();
        catProfileZAPMap = new HashMap<String,List<CatProfile>>();
        
        catAlertflexMap = new HashMap<String,List<String>>();
        catCuckooMap = new HashMap<String,List<String>>();
        catFalcoMap = new HashMap<String,List<String>>();
        catHybridAnalysisMap = new HashMap<String,List<String>>();
        catMiscMap = new HashMap<String,List<String>>();
        catMISPMap = new HashMap<String,List<String>>();
        catModSecurityMap = new HashMap<String,List<String>>();
        catRITAMap = new HashMap<String,List<String>>();
        catSonarQubeMap = new HashMap<String,List<String>>();
        catSuricataMap = new HashMap<String,List<String>>();
        catSyslogMap = new HashMap<String,List<String>>();
        catVmrayMap = new HashMap<String,List<String>>();
        catWazuhMap = new HashMap<String,List<String>>();
        catZAPMap = new HashMap<String,List<String>>();
        
        responseForEventMap = new HashMap<String,List<Response>>();
        responseForCatMap = new HashMap<String,List<Response>>();
        
        
    }
    
    String user = "";
    String pass = "";
    
    ActiveMQConnectionFactory connectionFactory = null;
    Connection connection = null;
    Session session = null;
    Destination destination = null;
    MessageConsumer consumer = null;
    
    @PostConstruct
    public void init() {
        
        try {
            
            String strConnFactory = System.getProperty("AmqUrl", "");
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
            Destination destination = session.createQueue("jms/alertflex/alerts");
            
            // MessageConsumer is used for receiving (consuming) messages
            consumer = session.createConsumer(destination);
            
            timerService.createTimer(1, INTERVAL_CHECK_ALERTS, "alertsManagement");
            
            projectList = projectFacade.findAll();
            
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
    public void runAlertsManagement (Timer timer) {
        
        Message message = null;
        int msg_type = 0;
        Alert a = null;
        
        try {
            
            checkCounter();
            
            do {
                // read alerts
                message = consumer.receive(INTERVAL_WAIT_ALERTS);
                
                if (message != null && message instanceof TextMessage) {
                    
                    msg_type = ((TextMessage) message).getIntProperty("msg_type");
            
                    if (msg_type != 1 && msg_type != 2) continue;
            
                    String ref_id = ((TextMessage) message).getStringProperty("ref_id");
                    Project project = findProjectByRef(ref_id);
                
                    if (project != null ) {
                        
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
                            a.setEventSeverity(((TextMessage) message).getIntProperty("event_severity"));
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
                            if (project.getIncJson() == 1) a.setJsonEvent(((TextMessage) message).getText());
                            else a.setJsonEvent("");
                
                            // enrich alert by new cat
                            List<String> newCats = eventCategoryFacade.findCatsByEvent(a.getAlertSource(), a.getEventId());
                            if (newCats != null && !newCats.isEmpty()) {
                                    
                                for (String cat: newCats) {
                                    
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
                            
                            // save alert in MySQL and check is Response required   
                            if (!a.getAction().equals("log-mgmt") && project.getSemActive() > 0) {
                            
                                alertFacade.create(a);
                                
                                // send alert to log server
                                LogServer ls = new LogServer(project);
                                ls.SendAlertToLog(a);
                                ls.close();
                    
                                if (project.getSemActive() >= 2) searchResponse(a);
                            }
                            
                            
                        }
                        
                        if (msg_type == 2) {
                            
                            String a_uuid = ((TextMessage) message).getStringProperty("alert_uuid");
                            a = alertFacade.findAlertByUuid(a_uuid);
                            
                            if(a != null && project.getSemActive() >= 2) searchResponse(a);
                        }
                    }
                }
                
            } while (message != null); 
        
        } catch (Exception e) {
            logger.error("alertflex_ctrl_exception", e);
        }
    }  
    
    
    void checkCounter() {
    
        // periodicaly update profiles info
        if (counterFreshCatProfiles == 0) {
                
            projectList = projectFacade.findAll();
        
            if (projectList == null || projectList.isEmpty()) return;
            
            catProfileAlertflexMap = new HashMap<String,List<CatProfile>>();
            catProfileCuckooMap = new HashMap<String,List<CatProfile>>();
            catProfileFalcoMap = new HashMap<String,List<CatProfile>>();
            catProfileHybridAnalysisMap = new HashMap<String,List<CatProfile>>();
            catProfileNmapMap = new HashMap<String,List<CatProfile>>();
            catProfileMiscMap = new HashMap<String,List<CatProfile>>();
            catProfileMISPMap = new HashMap<String,List<CatProfile>>();
            catProfileModSecurityMap = new HashMap<String,List<CatProfile>>();
            catProfileRITAMap = new HashMap<String,List<CatProfile>>();
            catProfileSonarQubeMap = new HashMap<String,List<CatProfile>>();
            catProfileSuricataMap = new HashMap<String,List<CatProfile>>();
            catProfileSyslogMap = new HashMap<String,List<CatProfile>>();
            catProfileVmrayMap = new HashMap<String,List<CatProfile>>();
            catProfileWazuhMap = new HashMap<String,List<CatProfile>>();
            catProfileZAPMap = new HashMap<String,List<CatProfile>>();
            
            catAlertflexMap = new HashMap<String,List<String>>();
            catCuckooMap = new HashMap<String,List<String>>();
            catFalcoMap = new HashMap<String,List<String>>();
            catHybridAnalysisMap = new HashMap<String,List<String>>();
            catNmapMap = new HashMap<String,List<String>>();
            catMiscMap = new HashMap<String,List<String>>();
            catMISPMap = new HashMap<String,List<String>>();
            catModSecurityMap = new HashMap<String,List<String>>();
            catRITAMap = new HashMap<String,List<String>>();
            catSonarQubeMap = new HashMap<String,List<String>>();
            catSuricataMap = new HashMap<String,List<String>>();
            catSyslogMap = new HashMap<String,List<String>>();
            catVmrayMap = new HashMap<String,List<String>>();
            catWazuhMap = new HashMap<String,List<String>>();
            catZAPMap = new HashMap<String,List<String>>();
        
            responseForEventMap = new HashMap<String,List<Response>>();
            responseForCatMap = new HashMap<String,List<Response>>();
        
            for (Project project: projectList) {
                
                List<String> catList = alertsCategoryFacade.findCatNames(project.getRefId(),"Alertflex");
                if (catList != null) catAlertflexMap.put(project.getRefId(),catList);
                catList = alertsCategoryFacade.findCatNames(project.getRefId(),"Cuckoo");
                if (catList != null) catCuckooMap.put(project.getRefId(),catList);
                catList = alertsCategoryFacade.findCatNames(project.getRefId(),"Falco");
                if (catList != null) catFalcoMap.put(project.getRefId(),catList);
                catList = alertsCategoryFacade.findCatNames(project.getRefId(),"HybridAnalysis");
                if (catList != null) catHybridAnalysisMap.put(project.getRefId(),catList);
                catList = alertsCategoryFacade.findCatNames(project.getRefId(),"Nmap");
                if (catList != null) catNmapMap.put(project.getRefId(),catList);
                catList = alertsCategoryFacade.findCatNames(project.getRefId(),"Misc");
                if (catList != null) catMiscMap.put(project.getRefId(),catList);
                catList = alertsCategoryFacade.findCatNames(project.getRefId(),"MISP");
                if (catList != null) catMISPMap.put(project.getRefId(),catList);
                catList = alertsCategoryFacade.findCatNames(project.getRefId(),"ModSecurity");
                if (catList != null) catModSecurityMap.put(project.getRefId(),catList);
                catList = alertsCategoryFacade.findCatNames(project.getRefId(),"RITA");
                if (catList != null) catRITAMap.put(project.getRefId(),catList);
                catList = alertsCategoryFacade.findCatNames(project.getRefId(),"SonarQube");
                if (catList != null) catSonarQubeMap.put(project.getRefId(),catList);
                catList = alertsCategoryFacade.findCatNames(project.getRefId(),"Suricata");
                if (catList != null) catSuricataMap.put(project.getRefId(),catList);
                catList = alertsCategoryFacade.findCatNames(project.getRefId(),"Syslog");
                if (catList != null) catSyslogMap.put(project.getRefId(),catList);
                catList = alertsCategoryFacade.findCatNames(project.getRefId(),"Vmray");
                if (catList != null) catVmrayMap.put(project.getRefId(),catList);
                catList = alertsCategoryFacade.findCatNames(project.getRefId(),"Wazuh");
                if (catList != null) catWazuhMap.put(project.getRefId(),catList);
                catList = alertsCategoryFacade.findCatNames(project.getRefId(),"ZAP");
                if (catList != null) catZAPMap.put(project.getRefId(),catList);
                
                List<CatProfile> catProfileList = catProfileFacade.findProfileBySource(project.getRefId(), "Alertflex");
                if (catProfileList != null) catProfileAlertflexMap.put(project.getRefId(),catProfileList);
                catProfileList = catProfileFacade.findProfileBySource(project.getRefId(), "Cuckoo");
                if (catProfileList != null) catProfileCuckooMap.put(project.getRefId(),catProfileList);
                catProfileList = catProfileFacade.findProfileBySource(project.getRefId(), "Falco");
                if (catProfileList != null) catProfileFalcoMap.put(project.getRefId(),catProfileList);
                catProfileList = catProfileFacade.findProfileBySource(project.getRefId(), "HybridAnalysis");
                if (catProfileList != null) catProfileHybridAnalysisMap.put(project.getRefId(),catProfileList);
                catProfileList = catProfileFacade.findProfileBySource(project.getRefId(), "Nmap");
                if (catProfileList != null) catProfileNmapMap.put(project.getRefId(),catProfileList);
                catProfileList = catProfileFacade.findProfileBySource(project.getRefId(), "Misc");
                if (catProfileList != null) catProfileMiscMap.put(project.getRefId(),catProfileList);
                catProfileList = catProfileFacade.findProfileBySource(project.getRefId(), "MISP");
                if (catProfileList != null) catProfileMISPMap.put(project.getRefId(),catProfileList);
                catProfileList = catProfileFacade.findProfileBySource(project.getRefId(), "ModSecurity");
                if (catProfileList != null) catProfileModSecurityMap.put(project.getRefId(),catProfileList);
                catProfileList = catProfileFacade.findProfileBySource(project.getRefId(), "RITA");
                if (catProfileList != null) catProfileRITAMap.put(project.getRefId(),catProfileList);
                catProfileList = catProfileFacade.findProfileBySource(project.getRefId(), "SonarQube");
                if (catProfileList != null) catProfileSonarQubeMap.put(project.getRefId(),catProfileList);
                catProfileList = catProfileFacade.findProfileBySource(project.getRefId(), "Suricata");
                if (catProfileList != null) catProfileSuricataMap.put(project.getRefId(),catProfileList);
                catProfileList = catProfileFacade.findProfileBySource(project.getRefId(), "Syslog");
                if (catProfileList != null) catProfileSyslogMap.put(project.getRefId(),catProfileList);
                catProfileList = catProfileFacade.findProfileBySource(project.getRefId(), "Vmray");
                if (catProfileList != null) catProfileVmrayMap.put(project.getRefId(),catProfileList);
                catProfileList = catProfileFacade.findProfileBySource(project.getRefId(), "Wazuh");
                if (catProfileList != null) catProfileWazuhMap.put(project.getRefId(),catProfileList);
                catProfileList = catProfileFacade.findProfileBySource(project.getRefId(), "ZAP");
                if (catProfileList != null) catProfileZAPMap.put(project.getRefId(),catProfileList);
                
            
                List<Response> responseList = responseFacade.findResponseForEvent(project.getRefId());
                if (responseList != null) responseForEventMap.put(project.getRefId(),responseList);
                responseList = responseFacade.findResponseForCat(project.getRefId());
                if (responseList != null) responseForCatMap.put(project.getRefId(),responseList);
            }
        } 
        
        if (counterFreshCatProfiles >= INTERVAL_FRESH_CAT_PROFILE) counterFreshCatProfiles = 0;
        else counterFreshCatProfiles++;
    }
    
    Project findProjectByRef(String ref) {
        
        if(projectList.isEmpty()) return null;
        
        for (Project p: projectList) {
            if (p.getRefId().equals(ref)) return p;
        }
        
        return null;
    }
    
    
    void searchResponse(Alert a) {
        
        // check if cats of alert exist
        checkCategory(a);
                
        List<Response> responseList = null;
        
        // check if field of action consists response profile for alert
        if (!a.getAction().equals("indef")) responseList = getResponseForAction(a);
        else responseList = getResponseForEvent(a);
        
        if (responseList == null || responseList.isEmpty()) responseList = getResponseForCat(a);
        
        if (responseList == null || responseList.isEmpty()) return;
                        
        for (Response res: responseList) {
            if (res != null) CreateResponse(res, a);
        }
    }
    
    
    void checkCategory(Alert alert) {
        
        // 
        catArray = alert.getCategories().split(", ");
        
        List<String> catList = null;
        
        switch(alert.getAlertSource()) {
            case "Alertflex":
                catList = catAlertflexMap.get(alert.getRefId());
                break;
            case "Cuckoo":
                catList = catCuckooMap.get(alert.getRefId());
                break;
            case "Falco":
                catList = catFalcoMap.get(alert.getRefId());
                break;
            case "HybridAnalysis":
                catList = catHybridAnalysisMap.get(alert.getRefId());
                break;
            case "Nmap":
                catList = catNmapMap.get(alert.getRefId());
                break;
            case "Misc":
                catList = catMiscMap.get(alert.getRefId());
                break;
            case "MISP":
                catList = catMISPMap.get(alert.getRefId());
                break;
            case "ModSecurity":
                catList = catModSecurityMap.get(alert.getRefId());
                break;
            case "RITA":
                catList = catRITAMap.get(alert.getRefId());
                break;
            case "SonarQube":
                catList = catSonarQubeMap.get(alert.getRefId());
                break;
            case "Suricata":
                catList = catSuricataMap.get(alert.getRefId());
                break;
            case "Syslog":
                catList = catSyslogMap.get(alert.getRefId());
                break;
            case "Vmray":
                catList = catVmrayMap.get(alert.getRefId());
                break;
            case "Wazuh":
                catList = catWazuhMap.get(alert.getRefId());
                break;
            case "ZAP":
                catList = catZAPMap.get(alert.getRefId());
                break;
            default:
                break;
        }
        
        if (catList != null) {
               
            for (int i=0; (i < catArray.length);  i++) {
                
                String cat = catArray[i];
            
                if (!isKnown(cat, catList)) unknownCat (alert, cat);
            
            }
        }
    }
    
    
    Boolean isKnown( String cat, List<String> catList) {
        
        for (String c : catList) {
                if (c.equals(cat)) return true;
        }
        
        return false;
    }
    
        
    public void unknownCat (Alert alert, String c) {
        
        
        String source = alert.getAlertSource();
        
        switch(source) {
            case "Alertflex":
                createCat(c, "Alertflex", alert.getRefId());
                catAlertflexMap.get(alert.getRefId()).add(c);
                break;
            case "Cuckoo":
                createCat(c, "Cuckoo", alert.getRefId());
                catCuckooMap.get(alert.getRefId()).add(c);
                break;
            case "Falco":
                createCat(c, "Falco", alert.getRefId());
                catFalcoMap.get(alert.getRefId()).add(c);
                break;
            case "HybridAnalysis":
                createCat(c, "HybridAnalysis", alert.getRefId());
                catHybridAnalysisMap.get(alert.getRefId()).add(c);
                break;
            case "Nmap":
                createCat(c, "Nmap", alert.getRefId());
                catNmapMap.get(alert.getRefId()).add(c);
                break;
            case "Misc":
                createCat(c, "Misc", alert.getRefId());
                catMiscMap.get(alert.getRefId()).add(c);
                break;
            case "MISP":
                createCat(c, "MISP", alert.getRefId());
                catMISPMap.get(alert.getRefId()).add(c);
                break;
            case "ModSecurity":
                createCat(c, "ModSecurity", alert.getRefId());
                catModSecurityMap.get(alert.getRefId()).add(c);
                break;
            case "RITA":
                createCat(c, "RITA", alert.getRefId());
                catRITAMap.get(alert.getRefId()).add(c);
                break;
            case "SonarQube":
                createCat(c, "SonarQube", alert.getRefId());
                catSonarQubeMap.get(alert.getRefId()).add(c);
                break;
            case "Suricata":
                createCat(c, "Suricata", alert.getRefId());
                catSuricataMap.get(alert.getRefId()).add(c);
                break;
            case "Syslog":
                createCat(c, "Syslog", alert.getRefId());
                catSyslogMap.get(alert.getRefId()).add(c);
                break;
            case "Vmray":
                createCat(c, "Vmray", alert.getRefId());
                catVmrayMap.get(alert.getRefId()).add(c);
                break;
            case "Wazuh":
                createCat(c, "Wazuh", alert.getRefId());
                catWazuhMap.get(alert.getRefId()).add(c);
                break;
            case "ZAP":
                createCat(c, "ZAP", alert.getRefId());
                catZAPMap.get(alert.getRefId()).add(c);
                break;
            default:
                break;
        }
        
              
        Alert a = new Alert();
                
            a.setRefId(alert.getRefId());
            a.setNodeId(alert.getNodeId());
            a.setAlertUuid(UUID.randomUUID().toString());
            a.setAlertSource("Alertflex");
            a.setAlertType("MISC");
            a.setSensorId(alert.getSensorId());
            a.setAlertSeverity(1);
            a.setDescription("Unknown category: " + c);
            a.setEventId("4");
            a.setEventSeverity(1);
            a.setLocation("Response from controller");
            a.setAction("indef");
            a.setStatus("processed");
            a.setFilter("indef");
            a.setInfo("Source: " + alert.getAlertSource() + ", type: " + alert.getAlertType() + ", event desc: " + alert.getDescription());
            Date date = new Date();
            a.setTimeEvent(date.toString());
            a.setTimeCollr(date);
            a.setTimeCntrl(date);
            a.setAgentName(alert.getAgentName());
            a.setUserName("indef");
            a.setCategories("unknown category");
            a.setSrcIp("indef");
            a.setDstIp("indef");
            a.setDstPort(0);
            a.setSrcPort(0);
            a.setSrcHostname("indef");
            a.setDstHostname("indef");
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
            
            alertFacade.create(a);
    }
    
    
    public void createCat (String cat, String type, String ref) {
        
       AlertCategory new_catAlerts = new AlertCategory (0, ref, cat, type, "cat was created automatically", "indef", "indef", "indef", "indef");
            
       alertsCategoryFacade.create(new_catAlerts);
    }
    
    
    public List<Response> getResponseForAction(Alert a)   {
        
        List<Response> selectedResponse = new ArrayList<>();
        
        List<Response> resList = responseForEventMap.get(a.getRefId());
        
        if (resList == null) return null;
        
        for (Response res: resList) {
            
            if (res.getResId().equals(a.getAction())) {
                
                String parameter = res.getAlertAgent();
                if (!parameter.isEmpty() && !parameter.equals("indef")) {
                    if (!parameter.equals(a.getAgentName())) continue;
                }
                
                parameter = res.getAlertContainer();
                if (!parameter.isEmpty() && !parameter.equals("indef")) {
                    if (!parameter.equals(a.getContainerId())) {
                        if (!parameter.equals(a.getContainerName())) continue;
                    }
                }
                
                parameter = res.getAlertIp();
                if (!parameter.isEmpty() && !parameter.equals("indef")) {
                    if (!parameter.equals(a.getSrcIp()) && !parameter.equals(a.getDstIp())) continue;
                }
                
                parameter = res.getAlertUser();
                if (!parameter.isEmpty() && !parameter.equals("indef")) {
                    if (!parameter.equals(a.getUserName())) continue;
                }
                
                parameter = res.getAlertSensor();
                if (!parameter.isEmpty() && !parameter.equals("indef")) {
                    if (!parameter.equals(a.getSensorId())) continue;
                }
                
                parameter = res.getAlertFile();
                if (!parameter.isEmpty() && !parameter.equals("indef")) {
                    if (!parameter.equals(a.getFileName())) continue;
                }
                
                parameter = res.getAlertProcess();
                if (!parameter.isEmpty() && !parameter.equals("indef")) {
                    if (!parameter.equals(a.getProcessName())) continue;
                }
                
                parameter = res.getAlertRegex();
                if (!parameter.isEmpty() && !parameter.equals("indef")) {
                    if (!a.getDescription().matches(parameter)) continue;
                }
                
                int begin = res.getBeginHour();
                int end = res.getEndHour();
                if (begin == 0 && end == 0) selectedResponse.add(res);
                else {
                    Calendar rightNow = Calendar.getInstance();
                    int hour = rightNow.get(Calendar.HOUR_OF_DAY);
                    if (begin <= hour && hour <= end)  selectedResponse.add(res);
                }
            }
        }
        
        return selectedResponse;
    } 
    
    public List<Response> getResponseForCat(Alert a)   {
        
        List<CatProfile> catProfileList = new ArrayList<>();
        List<String> selectedCatProfileNames = new ArrayList<>();
        List<Response> selectedResponse = new ArrayList<>();
        
        switch(a.getAlertSource()) {
            
            case "Alertflex":
                catProfileList = catProfileAlertflexMap.get(a.getRefId());
                break;
                
            case "Cuckoo":
                catProfileList = catProfileCuckooMap.get(a.getRefId());
                break;
            
            case "Falco":
                catProfileList = catProfileFalcoMap.get(a.getRefId());
                break;
                
            case "HybridAnalysis":
                catProfileList = catProfileHybridAnalysisMap.get(a.getRefId());
                break;
                
            case "Nmap":
                catProfileList = catProfileNmapMap.get(a.getRefId());
                break;
                
            case "Misc":
                catProfileList = catProfileMiscMap.get(a.getRefId());
                break;
            
            case "MISP":
                catProfileList = catProfileMISPMap.get(a.getRefId());
                break;
            
            case "ModSecurity":
                catProfileList = catProfileModSecurityMap.get(a.getRefId());
                break;
                
            case "RITA":
                catProfileList = catProfileRITAMap.get(a.getRefId());
                break;
                
            case "SonarQube":
                catProfileList = catProfileSonarQubeMap.get(a.getRefId());
                break;
            
            case "Suricata":
                catProfileList = catProfileSuricataMap.get(a.getRefId());    
                break;
                
            case "Syslog":
                catProfileList = catProfileSyslogMap.get(a.getRefId());
                break;
                
            case "Vmray":
                catProfileList = catProfileVmrayMap.get(a.getRefId());
                break;
            
            case "Wazuh":
                catProfileList = catProfileWazuhMap.get(a.getRefId());
                break;
                
            case "ZAP":
                catProfileList = catProfileZAPMap.get(a.getRefId());
                break;
                                
            default:
                break;
        }
        
        if (catProfileList == null) return null;
        
        for (CatProfile cp : catProfileList) {
            
            if (cp.getCatName().equals("*")) selectedCatProfileNames.add(cp.getCpName());
            else {
                for (int i=0; (i < catArray.length);  i++) {
                    if (cp.getCatName().equals(catArray[i])) {
                        selectedCatProfileNames.add(cp.getCpName());
                        break;
                    };
                }
            }
        }
        
        if (!selectedCatProfileNames.isEmpty()) {
            
            List<Response> resList = responseForCatMap.get(a.getRefId());
        
            if (resList == null) return null;
        
            for (Response res: resList) {
            
                for (String cpn: selectedCatProfileNames) {
                
                    if (res.getCatProfile().equals(cpn)) {
                        
                        String node = res.getNode();
            
                        if (a.getNodeId().equals(node) || node.equals("*")) {
                            
                            Integer sev = res.getAlertSeverity();
                            if (sev < 4) {
                                if (sev != a.getAlertSeverity()) continue;
                            }
                        
                            String parameter = res.getAlertAgent();
                            if (!parameter.isEmpty() && !parameter.equals("indef")) {
                                if (!parameter.equals(a.getAgentName())) continue;
                            }
                            
                            parameter = res.getAlertContainer();
                            if (!parameter.isEmpty() && !parameter.equals("indef")) {
                                if (!parameter.equals(a.getContainerName())) {
                                    if (!parameter.equals(a.getContainerId())) continue;
                                }
                            }
                
                            parameter = res.getAlertIp();
                            if (!parameter.isEmpty() && !parameter.equals("indef")) {
                                if (!parameter.equals(a.getSrcIp()) && !parameter.equals(a.getDstIp())) continue;
                            }
                
                            parameter = res.getAlertUser();
                            if (!parameter.isEmpty() && !parameter.equals("indef")) {
                                if (!parameter.equals(a.getUserName())) continue;
                            }
                
                            parameter = res.getAlertSensor();
                            if (!parameter.isEmpty() && !parameter.equals("indef")) {
                                if (!parameter.equals(a.getSensorId())) continue;
                            }
                
                            parameter = res.getAlertFile();
                            if (!parameter.isEmpty() && !parameter.equals("indef")) {
                                if (!parameter.equals(a.getFileName())) continue;
                            }
                
                            parameter = res.getAlertProcess();
                            if (!parameter.isEmpty() && !parameter.equals("indef")) {
                                if (!parameter.equals(a.getProcessName())) continue;
                            }
                
                            parameter = res.getAlertRegex();
                            if (!parameter.isEmpty() && !parameter.equals("indef")) {
                                if (!a.getDescription().matches(parameter)) continue;
                            }
                
                            int begin = res.getBeginHour();
                            int end = res.getEndHour();
                            if (begin == 0 && end == 0) selectedResponse.add(res);
                            else {
                                Calendar rightNow = Calendar.getInstance();
                                int hour = rightNow.get(Calendar.HOUR_OF_DAY);
                                if (begin <= hour && hour <= end)  selectedResponse.add(res);
                            }
                        }    
                    }
                }
            }
        }
            
        return selectedResponse;
    } 
    
    public List<Response> getResponseForEvent(Alert a)   {
        
        List<Response> selectedResponse = new ArrayList<>();
        
        List<Response> resList = responseForEventMap.get(a.getRefId());
        
        if (resList == null) return null;
        
        for (Response res: resList) {
            
            if (res.getEventId().equals(a.getEventId())) {
                
                String parameter = res.getAlertAgent();
                if (!parameter.isEmpty() && !parameter.equals("indef")) {
                    if (!parameter.equals(a.getAgentName())) continue;
                }
                
                parameter = res.getAlertContainer();
                if (!parameter.isEmpty() && !parameter.equals("indef")) {
                    if (!parameter.equals(a.getContainerName())) {
                        if (!parameter.equals(a.getContainerId())) continue;
                    }
                }
                
                parameter = res.getAlertIp();
                if (!parameter.isEmpty() && !parameter.equals("indef")) {
                    if (!parameter.equals(a.getSrcIp()) && !parameter.equals(a.getDstIp())) continue;
                }
                
                parameter = res.getAlertUser();
                if (!parameter.isEmpty() && !parameter.equals("indef")) {
                    if (!parameter.equals(a.getUserName())) continue;
                }
                
                parameter = res.getAlertSensor();
                if (!parameter.isEmpty() && !parameter.equals("indef")) {
                    if (!parameter.equals(a.getSensorId())) continue;
                }
                
                parameter = res.getAlertFile();
                if (!parameter.isEmpty() && !parameter.equals("indef")) {
                    if (!parameter.equals(a.getFileName())) continue;
                }
                
                parameter = res.getAlertProcess();
                if (!parameter.isEmpty() && !parameter.equals("indef")) {
                    if (!parameter.equals(a.getProcessName())) continue;
                }
                
                parameter = res.getAlertRegex();
                if (!parameter.isEmpty() && !parameter.equals("indef")) {
                    if (!a.getDescription().matches(parameter)) continue;
                }
                
                int begin = res.getBeginHour();
                int end = res.getEndHour();
                if (begin == 0 && end == 0) selectedResponse.add(res);
                else {
                    Calendar rightNow = Calendar.getInstance();
                    int hour = rightNow.get(Calendar.HOUR_OF_DAY);
                    if (begin <= hour && hour <= end)  selectedResponse.add(res);
                }
            }
        }
        
        return selectedResponse;
    } 
    
    
    public void CreateResponse(Response res, Alert a) {
        
        try {
            
            // Create a Connection
            Connection connectionResponse = connectionFactory.createConnection(user,pass);
            connection.start();

            // Create a Session
            Session sessionResponse = connectionResponse.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // Create the destination (Topic or Queue)
            Destination destinationResponse = sessionResponse.createQueue("jms/alertflex/responses");

            // Create a MessageProducer from the Session to the Topic or Queue
            MessageProducer producer = sessionResponse.createProducer(destinationResponse);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
            
            TextMessage message = session.createTextMessage();
            
            message.setStringProperty("ref_id", a.getRefId());
            message.setStringProperty("response_id", res.getResId());
            message.setStringProperty("alert_uuid", a.getAlertUuid());
            message.setText("empty");
            producer.send(message);
                        
            // Clean up
            sessionResponse.close();
            connectionResponse.close();            
                
        } catch ( JMSException e) {
            logger.error("alertflex_ctrl_exception", e);
        } 
    }
}

