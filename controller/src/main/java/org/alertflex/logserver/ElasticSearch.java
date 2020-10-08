/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alertflex.logserver;

import hidden.org.elasticsearch.action.index.IndexRequest;
import hidden.org.elasticsearch.action.index.IndexResponse;
import hidden.org.elasticsearch.client.RequestOptions;
import hidden.org.elasticsearch.client.RestHighLevelClient;
import hidden.org.elasticsearch.common.xcontent.XContentType;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Date;
import org.alertflex.common.Netflow;
import org.alertflex.entity.Alert;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author root
 */
public class ElasticSearch {
    
    private static final Logger logger = LoggerFactory.getLogger(ElasticSearch.class);
    
    private RestHighLevelClient client = null;
    
    String dateIndex = "";
    
    IndexRequest requestEvents = null;
    
    IndexRequest requestNetflow = null;
    
    IndexRequest requestSuricata = null;
    
    public ElasticSearch(RestHighLevelClient c) {
        this.client = c;
        
        dateIndex = getDateIndex();
    
        requestEvents = new IndexRequest("alertflex-events-" + dateIndex);
    
        requestNetflow = new IndexRequest("alertflex-netflow-" + dateIndex);
    
        requestSuricata = new IndexRequest("alertflex-suricata-" + dateIndex);
    }
    
    String getDateIndex() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
        Date dt = new Date();
        return dateFormat.format(dt).toString();
    }
    
    void checkDateIndex() {
        
        String di = getDateIndex();
        
        if (!dateIndex.equals(di)) {
            
            dateIndex = di;
            
            requestEvents = new IndexRequest("alertflex-events-" + dateIndex);
    
            requestNetflow = new IndexRequest("alertflex-netflow-" + dateIndex);
    
            requestSuricata = new IndexRequest("alertflex-suricata-" + dateIndex);
            
        }
    }
    
    public void SendSuricataToLog(String log) {
        
        String elasticJson ="";
        
        if (client != null) {
            
            try {
                
                JSONObject obj = new JSONObject(log);
        
                String message = obj.getString("short_message");
            
                switch(message) {
            
                    case "dns-nids":
                        elasticJson = ConvertDnsToLog(obj);
                        break;
                    case "http-nids":
                        elasticJson = ConvertHttpToLog(obj);
                        break;
                    case "netflow-nids":
                        elasticJson = ConvertNetflowToLog(obj);
                        break;
                    case "file-nids":
                        elasticJson = ConvertFileToLog(obj);
                        break;
                                       
                    default:
                        return;
                }
                
                if (!elasticJson.isEmpty()) {
                    checkDateIndex();
                    requestSuricata.source(elasticJson, XContentType.JSON);
                    IndexResponse indexResponse = client.index(requestSuricata, RequestOptions.DEFAULT);
                }
            
            } catch (Exception e) {
                logger.error("alertflex_ctrl_exception", e);
            } 
        }
    }
    
    public String ConvertDnsToLog(JSONObject obj) {
        
        return "";
    }
    
    public String ConvertHttpToLog(JSONObject obj) {
        
        return "";
    }
    
    public String ConvertNetflowToLog(JSONObject obj) {
        
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        String time = formatter.format(date);
                
        String report = "{\"version\": \"1.1\",\"host\":\""
            + obj.getString("host")
            + "\",\"short_message\":\"netflow-nids\",\"full_message\":\"Netflow event from Suricata NIDS\",\"level\":"
            + obj.getInt("level")
            + ",\"source_type\":\"NET\",\"source_name\":\"Suricata\",\"project_id\":\""
            + obj.getString("_project_id")
            + "\",\"sensor\":\""
            + obj.getString("_sensor")
            + "\",\"event_time\":\""
            + obj.getString("_event_time")
            + "\",\"controller_time\":\""
            + time
            + "\",\"protocol\":\""
            + obj.getString("_protocol")
            + "\",\"process\":\""
            + obj.getString("_process")
            + "\",\"srcip\":\""
            + obj.getString("_srcip")
            + "\",\"srcagent\":\""
            + obj.getString("_srcagent")
            + "\",\"srcport\":"
            + obj.getInt("_srcport")
            + ",\"dstip\":\""
            + obj.getString("_dstip")
            + "\",\"dstagent\":\""
            + obj.getString("_dstagent")
            + "\",\"dstport\":"
            + obj.getInt("_dstport")
            + ",\"bytes\":"
            + obj.getInt("_bytes")
            + ",\"packets\":"
            + obj.getInt("_packets")
            + "}";
        
        return report;
    }
    
    public String ConvertFileToLog(JSONObject obj) {
        
        return "";
    }
    
    public void SendAlertToLog(Alert a) {
        
        if (client != null) {
            
            try {
                
                Integer level;
                Integer severity = a.getAlertSeverity();
            
                switch (severity) {
                    case 0 :
                        level = 5; 
                        break;
                    case 1 :
                        level = 4; 
                        break;
                    case 2 :
                        level = 3; 
                        break;
                    case 3 :
                        level = 1; 
                        break;
                    default :
                        level = 6; 
                        break;
                }
                
                Date date = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
                String time = formatter.format(date);
                
                String alert = "{\"version\": \"1.1\",\"host\":\""
                +  a.getNodeId()
                + "\",\"short_message\":\"alert-flex\""
                + ",\"full_message\":\"Alert from Alertflex collector/controller\""
                + ",\"level\":"
                + level.toString()
                + ",\"source_type\":\""
                + a.getAlertType()
                + "\",\"source_name\":\""
                + a.getAlertSource()
		+ "\",\"project_id\":\""
                + a.getRefId()
                + "\",\"alert uuid\":\""
                + a.getAlertUuid()
                + "\",\"severity\":"
                + a.getAlertSeverity()
                + ",\"event\":\""
                + a.getEventId()
                + "\",\"categories\":\""
                + a.getCategories()
                + "\",\"description\":\""
                + a.getDescription()
                + "\",\"src_ip\":\""
                + a.getSrcIp()
                + "\",\"src_hostname\":\""
                + a.getSrcHostname()
                + "\",\"src_port\":"
                + a.getSrcPort()
                + ",\"dst_ip\":\""
                + a.getDstIp()
                + "\",\"dst_hostname\":\""
                + a.getDstHostname()
                + "\",\"dst_port\":"
                + a.getDstPort()
                + ",\"sensor\":\""
                + a.getSensorId()
                + "\",\"process\":\""
                + a.getProcessName()
                + "\",\"container\":\""
                + a.getContainerName()
                + "\",\"agent\":\""
                + a.getAgentName()
                + "\",\"user\":\""
                + a.getUserName()
                + "\",\"file\":\""
                + a.getFileName()
                + "\",\"location\":\""
                + a.getLocation()
                + "\",\"action\":\""
                + a.getAction()
                + "\",\"status\":\""
                + a.getStatus()
                + "\",\"filter\":\""
                + a.getFilter()
                + "\",\"collector_time\":\""
                + a.getTimeEvent()
                + "\",\"controller_time\":\""
                + time
                + "\"}";
                                
                requestEvents.source(alert, XContentType.JSON);
                IndexResponse indexResponse = client.index(requestEvents, RequestOptions.DEFAULT);
            
            } catch (Exception e) {
                logger.error("alertflex_ctrl_exception", e);
            } 
        }
    }
    
    public void SendNetflowToLog(Netflow n) {
        
        if (client != null) {
            try {
                
                Date date = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
                String time = formatter.format(date);
                
                String netflow = "{\"version\": \"1.1\",\"host\":\""
                +  n.getNode()
                + "\",\"short_message\":\"netflow-beat\""
                + ",\"full_message\":\"Netflow event from Beats\",\"level\": 7,\"source_type\":\"NET\",\"source_name\":\"Alertflex\",\"project_id\":\""
                + n.getRefId()
                + "\",\"sensor\":\""
                + n.getHostname()
                + "\",\"event_time\":\""
                + n.getTimestamp()
                + "\",\"collected_time\":\""
                + time
                + "\",\"protocol\":\""
                + n.getProtocol()
                + "\",\"process\":\""
                + n.getProcessName()
                + "\",\"pid\":"
                + n.getProcessId()
                + ",\"path\":\""
                + n.getProcessPath()
                + "\",\"src_ip\":\""
                + n.getSrcIp()
                + "\",\"src_hostname\":\""
                + n.getSrcHostname()
                + "\",\"srcport\":"
                + n.getSrcPort()
                + ",\"dstip\":\""
                + n.getDstIp()
                + "\",\"dstagent\":\""
                + n.getSrcHostname()
                + "\",\"dstport\":"
                + n.getSrcPort()
                + ",\"packets\":"
                + n.getPackets()
                + ",\"bytes\":"
                + n.getBytes()
                + "}";
                                
                requestNetflow.source(netflow, XContentType.JSON);
                IndexResponse indexResponse = client.index(requestNetflow, RequestOptions.DEFAULT);
            
            } catch (Exception e) {
                logger.error("alertflex_ctrl_exception", e);
            } 
        }
    }
    
    public void close() throws IOException {
        client.close();
    }
}
