package org.alertflex.controller;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.xml.bind.DatatypeConverter;
import org.alertflex.entity.Alert;
import org.alertflex.entity.Attributes;
import org.alertflex.entity.Events;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Oleg Zharkov
 */

public class LogsManagement {
    
    private static final Logger logger = LoggerFactory.getLogger(LogsManagement.class);
    
    private InfoMessageBean eventBean;

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
    static final String misp_sha256 = "sha256";
    static final String misp_filename_sha256 = "filename|sha256";
    
    static final String misp_url = "url";
    static final String misp_hostname = "hostname";
    static final String misp_link = "link";
    
    DatagramSocket socket = null ;
    
    boolean doCheckIOC = false;
    boolean doSendLogs = false;
    
    public LogsManagement (InfoMessageBean eb) {
        this.eventBean = eb;
    }
    
    public void EvaluateLogs(String logs) {
        
        String log_record = "log record empty";
                
        try {
            
            String logHost = eventBean.getProject().getLogHost();
            int logPort = eventBean.getProject().getLogPort();
            int sendNetflow = eventBean.getProject().getSendNetflow();
            InetAddress iaHost = null; 
            
            if (!logHost.isEmpty() && logPort != 0 && sendNetflow > 0) {
                
                iaHost = InetAddress.getByName(logHost);
                socket = new DatagramSocket() ;
                doSendLogs =  true;
            }
            
            doCheckIOC = (eventBean.getProject().getIocCheck() != 0);
            
            JSONObject obj = new JSONObject(logs);
            JSONArray arr = obj.getJSONArray("logs");
            
            
            for (int i = 0; i < arr.length(); i++) {
                
                log_record = arr.getJSONObject(i).toString();
                                
                boolean isAlert = false;
                
                // Check IOC
                if (doCheckIOC) isAlert = CheckIoc(log_record);
                else isAlert = CheckAlert(log_record); 
                
                // Send to Log Management
                if (doSendLogs && !isAlert)  {
                    byte [] data = log_record.getBytes() ;
                    DatagramPacket packet = new DatagramPacket( data, data.length, iaHost, logPort ) ;
                    socket.send( packet) ;
                }    
            }
             
        } catch ( IOException | JSONException e) {
            logger.error("alertflex_ctrl_exception", e);
            logger.error(logs);
        } finally {
            if( socket != null ) socket.close() ;
        }
    }
    
    public boolean CheckAlert (String r) throws UnknownHostException {
        
        try {
        
            JSONObject obj = new JSONObject(r);
        
            String message = obj.getString("short_message");
            
            switch(message) {
            
                case "alert-crs":
                    return true;
                case "alert-fim":
                    return true;
                case "alert-hids":
                    return true;
                case "alert-nids":
                    return true;
                case "alert-waf":
                    return true;
                    
                default:
                    break;
            }
            
        } catch (JSONException e) {
            logger.error(r);
            logger.error(e.getMessage());
        }
        
        return false;
                
    }
    
    public boolean CheckIoc (String r) throws UnknownHostException {
        
        String artifacts = "";
        String dstip = "indef";
        String srcip = "indef";
        String md5 = "";
        String sha1 = "";
        String sha256 = "";
        String dns = "";
        String agent = "";
        String hostname = "";
        String nodename = "";
        String filename = "indef";
        String process = "indef";
        String eventCategory = "indef";
        String eventAction = "indef";
        
        try {
        
            JSONObject obj = new JSONObject(r);
            JSONObject msg;
        
            String message = obj.getString("short_message");
        
            Attributes attr;
        
            int alert_type;
        
            switch(message) {
            
                case "alert-fim":
                    
                    nodename = obj.getString("host");
                    md5 = obj.getString("_md5");
                    sha1 = obj.getString("_sha1");
                    sha256 = obj.getString("_sha256");
                    filename = obj.getString("_filename");
                    agent = obj.getString("_agent");
                    hostname = obj.getString("_manager");
                    
                    attr = eventBean.getAttributesFacade().findByValueAndType(md5,misp_md5);
                
                    if (attr == null) attr = eventBean.getAttributesFacade().findByValueAndType(md5,misp_filename_md5);
                
                    if (attr != null) {
                        alert_type = 1;
                        
                        artifacts = "{\"artifacts\": [{ \"dataType\": \"md5\",\"data\":\""
                        + md5
                        + "\",\"message\": \"message digest MD5\" }]}";
                        
                        createIocAlert(r, attr, alert_type, artifacts, nodename, dstip, srcip, agent, hostname, filename, process);
                    }
                
                    attr = eventBean.getAttributesFacade().findByValueAndType(sha1,misp_sha1);
                
                    if (attr == null) attr = eventBean.getAttributesFacade().findByValueAndType(sha1,misp_filename_sha1);
                
                    if (attr != null) {
                        alert_type = 2;
                        
                        artifacts = "{\"artifacts\": [{ \"dataType\": \"sha1\",\"data\":\""
                        + sha1
                        + "\",\"message\": \"secure hash SHA1\" }]}";
                        
                        createIocAlert(r, attr, alert_type, artifacts, nodename, dstip, srcip, agent, hostname, filename, process);
                    }
                
                    attr = eventBean.getAttributesFacade().findByValueAndType(sha256,misp_sha256);
                
                    if (attr == null) attr = eventBean.getAttributesFacade().findByValueAndType(sha256,misp_filename_sha256);
                
                    if (attr != null) {
                        alert_type = 3;
                        
                        artifacts = "{\"artifacts\": [{ \"dataType\": \"sha256\",\"data\":\""
                        + sha256
                        + "\",\"message\": \"secure hash SHA256\" }]}";
                        
                        createIocAlert(r, attr, alert_type, artifacts, nodename, dstip, srcip, agent, hostname, filename, process);
                    }
                
                    return true;
                    
                case "alert-hids":
                    nodename = obj.getString("host");
                    dstip = obj.getString("_dstip");
                    srcip = obj.getString("_srcip");
                    agent = obj.getString("_agent");
                    hostname = obj.getString("_manager");
                                        
                    if (!ipdstMap.containsKey(dstip)) {
                
                        attr = eventBean.getAttributesFacade().findByValueAndType(dstip,misp_ipdst);
                        ipdstMap.put(dstip, attr);
                        
                        if (attr != null) {
                            alert_type = 4;
                            
                            artifacts = "{\"artifacts\": [{\"dataType\": \"ip\",\"data\":\""
                            + dstip
                            + "\",\"message\": \"destination ip\" }]}";
                            
                            createIocAlert(r, attr, alert_type, artifacts, nodename, dstip, srcip, agent, hostname, filename, process);
                        }
                    } 
                    
                    if (!ipsrcMap.containsKey(srcip)) {
                
                        attr = eventBean.getAttributesFacade().findByValueAndType(srcip,misp_ipsrc);
                        ipsrcMap.put(srcip, attr);
                        
                        if (attr != null) {
                            alert_type = 5;
                            
                            artifacts = "{\"artifacts\": [{\"dataType\": \"ip\",\"data\":\""
                            + srcip
                            + "\",\"message\": \"source ip\" }]}";
                            
                            createIocAlert(r, attr, alert_type, artifacts, nodename, dstip, srcip, agent, hostname, filename, process);
                        }
                    }
                    
                    return true;
                    
                case "alert-nids":
                    nodename = obj.getString("host");
                    dstip = obj.getString("_dstip");
                    srcip = obj.getString("_srcip");
                    hostname = obj.getString("_sensor");
                                                            
                    if (!ipdstMap.containsKey(dstip)) {
                
                        attr = eventBean.getAttributesFacade().findByValueAndType(dstip,misp_ipdst);
                        ipdstMap.put(dstip, attr);
                        
                        if (attr != null) {
                            alert_type = 6;
                            
                            artifacts = "{\"artifacts\": [{\"dataType\": \"ip\",\"data\":\""
                            + dstip
                            + "\",\"message\": \"destination ip\" }]}";
                            
                            createIocAlert(r, attr, alert_type, artifacts, nodename, dstip, srcip, agent, hostname, filename, process);
                        }
                    } 
                    
                    if (!ipsrcMap.containsKey(srcip)) {
                
                        attr = eventBean.getAttributesFacade().findByValueAndType(srcip,misp_ipsrc);
                        ipsrcMap.put(srcip, attr);
                        
                        if (attr != null) {
                            alert_type = 7;
                            
                            artifacts = "{\"artifacts\": [{\"dataType\": \"ip\",\"data\":\""
                            + srcip
                            + "\",\"message\": \"source ip\" }]}";
                            
                            createIocAlert(r, attr, alert_type, artifacts, nodename, dstip, srcip, agent, hostname, filename, process);
                        }
                    }
                    
                    return true;
                    
                case "dns-nids":
                    nodename = obj.getString("host");
                    dstip = obj.getString("_dstip");
                    srcip = obj.getString("_srcip");
                    hostname = obj.getString("_sensor");
                    dns = obj.getString("_rrname");
                
                    if (!ipdstMap.containsKey(dstip)) {
                
                        attr = eventBean.getAttributesFacade().findByValueAndType(dstip,misp_ipdst);
                        ipdstMap.put(dstip, attr);
                        
                        if (attr != null) {
                            alert_type = 8;
                            
                            artifacts = "{\"artifacts\": [{\"dataType\": \"ip\",\"data\":\""
                            + dstip
                            + "\",\"message\": \"destination ip\" }]}";
                            
                            createIocAlert(r, attr, alert_type, artifacts, nodename, dstip, srcip, agent, hostname, filename, process);
                        }
                    }
                    
                
                    if (!ipsrcMap.containsKey(srcip)) {
                
                        attr = eventBean.getAttributesFacade().findByValueAndType(srcip,misp_ipsrc);
                        ipsrcMap.put(srcip, attr);
                        
                        if (attr != null) {
                            alert_type = 9;
                            
                            artifacts = "{\"artifacts\": [{\"dataType\": \"ip\",\"data\":\""
                            + srcip
                            + "\",\"message\": \"source ip\" }]}";
                            
                            createIocAlert(r, attr, alert_type, artifacts, nodename, dstip, srcip, agent, hostname, filename, process);
                        }
                    }
                                    
                    
                    if (!dnsMap.containsKey(dns)) {
                
                        attr = eventBean.getAttributesFacade().findByValueAndType(dns,misp_dns);
                        dnsMap.put(dns, attr);
                        
                        if (attr != null) {
                            alert_type = 10;
                            
                            artifacts = "{\"artifacts\": [{\"dataType\": \"domain\",\"data\":\""
                            + dns
                            + "\",\"message\": \"domain\" }]}";
                            
                            createIocAlert(r, attr, alert_type, artifacts, nodename, dstip, srcip, agent, hostname, filename, process);
                        }
                    }
                    
                
                    break;
                    
                case "netflow-nids":
                    
                    nodename = obj.getString("host");
                    dstip = obj.getString("_dstip");
                    srcip = obj.getString("_srcip");
                    hostname = obj.getString("_sensor");
                    
                    if (!ipdstMap.containsKey(dstip)) {
                
                        attr = eventBean.getAttributesFacade().findByValueAndType(dstip,misp_ipdst);
                        ipdstMap.put(dstip, attr);
                        
                        if (attr != null) {
                            alert_type = 11;
                            
                            artifacts = "{\"artifacts\": [{\"dataType\": \"ip\",\"data\":\""
                            + dstip
                            + "\",\"message\":\" destination ip\" }]}";
                            
                            createIocAlert(r, attr, alert_type, artifacts, nodename, dstip, srcip, agent, hostname, filename, process);
                        }
                    }
                    
                                    
                    if (!ipsrcMap.containsKey(srcip)) {
                
                        attr = eventBean.getAttributesFacade().findByValueAndType(srcip,misp_ipsrc);
                        ipsrcMap.put(srcip, attr);
                        
                        if (attr != null) {
                            alert_type = 12;
                            
                            artifacts = "{\"artifacts\": [{\"dataType\": \"ip\",\"data\":\""
                            + srcip
                            + "\",\"message\":\" source ip\" }]}";
                            
                            createIocAlert(r, attr, alert_type, artifacts, nodename, dstip, srcip, agent, hostname, filename, process);
                        }
                    }
                    
                    break;
                    
                case "http-nids":
                    
                    nodename = obj.getString("host");
                    dstip = obj.getString("_dstip");
                    srcip = obj.getString("_srcip");
                    hostname = obj.getString("_url_hostname");
                    
                    if (!ipdstMap.containsKey(dstip)) {
                
                        attr = eventBean.getAttributesFacade().findByValueAndType(hostname,misp_hostname);
                        ipdstMap.put(dstip, attr);
                        
                        if (attr != null) {
                            alert_type = 13;
                            
                            artifacts = "{\"artifacts\": [{\"dataType\": \"url_hostname\",\"data\":\""
                            + hostname
                            + "\",\"message\":\" url host name\" }]}";
                            
                            createIocAlert(r, attr, alert_type, artifacts, nodename, dstip, srcip, agent, hostname, filename, process);
                        }
                    }
                    
                    break;
                    
                case "file-nids":
                    nodename = obj.getString("host");
                    dstip = obj.getString("_dstip");
                    srcip = obj.getString("_srcip");
                    hostname = obj.getString("_sensor");
                    filename = obj.getString("_filename");
                    md5 = obj.getString("_md5");
                    
                    attr = eventBean.getAttributesFacade().findByValueAndType(md5,misp_md5);
                
                    if (attr == null) attr = eventBean.getAttributesFacade().findByValueAndType(md5,misp_filename_md5);
                
                    if (attr != null) {
                        alert_type = 14;
                        
                        artifacts = "{\"artifacts\": [{ \"dataType\": \"md5\",\"data\":\""
                        + md5
                        + "\",\"message\": \"message digest before\" }]}";
                        
                        createIocAlert(r, attr, alert_type, artifacts, nodename, dstip, srcip, agent, hostname, filename, process);
                    }
                    
                    break;
                    
                case "alert-waf":
                    
                    nodename = obj.getString("host");
                    dstip = obj.getString("_dstip");
                    srcip = obj.getString("_srcip");
                    hostname = obj.getString("_target");
                                                            
                    if (!ipdstMap.containsKey(dstip)) {
                
                        attr = eventBean.getAttributesFacade().findByValueAndType(dstip,misp_ipdst);
                        ipdstMap.put(dstip, attr);
                        
                        if (attr != null) {
                            alert_type = 15;
                            
                            artifacts = "{\"artifacts\": [{\"dataType\": \"ip\",\"data\":\""
                            + dstip
                            + "\",\"message\": \"destination ip\" }]}";
                            
                            createIocAlert(r, attr, alert_type, artifacts, nodename, dstip, srcip, agent, hostname, filename, process);
                        }
                    } 
                    
                    if (!ipsrcMap.containsKey(srcip)) {
                
                        attr = eventBean.getAttributesFacade().findByValueAndType(srcip,misp_ipsrc);
                        ipsrcMap.put(srcip, attr);
                        
                        if (attr != null) {
                            alert_type = 16;
                            
                            artifacts = "{\"artifacts\": [{\"dataType\": \"ip\",\"data\":\""
                            + srcip
                            + "\",\"message\": \"source ip\" }]}";
                            
                            createIocAlert(r, attr, alert_type, artifacts, nodename, dstip, srcip, agent, hostname, filename, process);
                        }
                    }
                    
                    return true;
                    
                case "netflow-elastic":
                    
                    msg = obj.getJSONObject("message");
                    
                    if (msg.has("event")) {
        
                        JSONObject event = msg.getJSONObject("event");
            
        
                        if (event.has("category")) eventCategory = event.getString("category");
        
                        if (event.has("action")) eventAction = event.getString("action");
        
                    }
        
                    if (eventAction.equals("network_flow") && eventCategory.equals("network_traffic")) {
                        
                        JSONObject dest = msg.getJSONObject("destination");
                        JSONObject source = msg.getJSONObject("source");
                        JSONObject host = msg.getJSONObject("agent");
                        
                        nodename = "controller";
                        dstip = dest.getString("ip");
                        srcip = source.getString("ip");
                        hostname = host.getString("hostname");
                        agent = hostname;
                        
                        if (!ipdstMap.containsKey(dstip)) {
                
                            attr = eventBean.getAttributesFacade().findByValueAndType(dstip,misp_ipdst);
                            ipdstMap.put(dstip, attr);
                        
                            if (attr != null) {
                                alert_type = 17;
                            
                                artifacts = "{\"artifacts\": [{\"dataType\": \"ip\",\"data\":\""
                                    + dstip
                                    + "\",\"message\": \"destination ip\" }]}";
                            
                                createIocAlert(r, attr, alert_type, artifacts, nodename, dstip, srcip, agent, hostname, filename, process);
                            }
                        } 
                    
                        if (!ipsrcMap.containsKey(srcip)) {
                
                            attr = eventBean.getAttributesFacade().findByValueAndType(srcip,misp_ipsrc);
                            ipsrcMap.put(srcip, attr);
                        
                            if (attr != null) {
                                alert_type = 18;
                            
                                artifacts = "{\"artifacts\": [{\"dataType\": \"ip\",\"data\":\""
                                    + srcip
                                    + "\",\"message\": \"source ip\" }]}";
                            
                                createIocAlert(r, attr, alert_type, artifacts, nodename, dstip, srcip, agent, hostname, filename, process);
                            }
                        }
                    }
                    
                    return true;
                                        
                case "packet-elastic":
                    
                    msg = obj.getJSONObject("message");
                    
                    if (msg.has("event")) {
        
                        JSONObject event = msg.getJSONObject("event");
            
        
                        if (event.has("category")) eventCategory = event.getString("category");
        
                        if (event.has("action")) eventAction = event.getString("action");
        
                    }
        
                    if (eventAction.equals("network_flow") && eventCategory.equals("network_traffic")) {
                        
                        JSONObject dest = msg.getJSONObject("destination");
                        JSONObject source = msg.getJSONObject("source");
                        JSONObject host = msg.getJSONObject("agent");
                        
                        nodename = "controller";
                        dstip = dest.getString("ip");
                        srcip = source.getString("ip");
                        hostname = host.getString("hostname");
                        agent = hostname;
                        
                        if (msg.has("process")) {
                
                            JSONObject proc = obj.getJSONObject("process");
                            process = proc.getString("name");
                        } 
                        
                        if (!ipdstMap.containsKey(dstip)) {
                
                            attr = eventBean.getAttributesFacade().findByValueAndType(dstip,misp_ipdst);
                            ipdstMap.put(dstip, attr);
                        
                            if (attr != null) {
                                alert_type = 19;
                            
                                artifacts = "{\"artifacts\": [{\"dataType\": \"dstip\",\"data\":\""
                                    + dstip
                                    + "\",\"message\":\" process: "
                                    + process 
                                    + "\" }]}";
                                                                
                                createIocAlert(r, attr, alert_type, artifacts, nodename, dstip, srcip, agent, hostname, filename, process);
                            }
                        } 
                    
                        if (!ipsrcMap.containsKey(srcip)) {
                
                            attr = eventBean.getAttributesFacade().findByValueAndType(srcip,misp_ipsrc);
                            ipsrcMap.put(srcip, attr);
                        
                            if (attr != null) {
                                alert_type = 20;
                            
                                artifacts = "{\"artifacts\": [{\"dataType\": \"srcip\",\"data\":\""
                                    + srcip
                                    + "\",\"message\":\" process: "
                                    + process 
                                    + "\" }]}";
                            
                                createIocAlert(r, attr, alert_type, artifacts, nodename, dstip, srcip, agent, hostname, filename, process);
                            }
                        }
                    }
                    
                    return true;
                    
                case "netflow-graylog":
                    
                    msg = obj.getJSONObject("message");
                    
                    if (msg.has("filebeat_event_category")) eventCategory = msg.getString("filebeat_event_category");
        
                    if (msg.has("filebeat_event_action")) eventAction = msg.getString("filebeat_event_action");
        
                    if (eventAction.equals("network_flow") && eventCategory.equals("network_traffic")) {
                        
                        nodename = "controller";
                        if (msg.has("filebeat_netflow_destination_ipv4_address")) 
                            dstip = msg.getString("filebeat_netflow_destination_ipv4_address");
                        if (msg.has("filebeat_netflow_source_ipv4_address")) 
                            srcip = msg.getString("filebeat_netflow_source_ipv4_address");
                        hostname = msg.getString("filebeat_host_hostname");
                        agent = hostname;
                    
                        if (!ipdstMap.containsKey(dstip)) {
                
                            attr = eventBean.getAttributesFacade().findByValueAndType(dstip,misp_ipdst);
                            ipdstMap.put(dstip, attr);
                        
                            if (attr != null) {
                                alert_type = 21;
                            
                                artifacts = "{\"artifacts\": [{\"dataType\": \"ip\",\"data\":\""
                                    + dstip
                                    + "\",\"message\":\" destination ip\" }]}";
                            
                                createIocAlert(r, attr, alert_type, artifacts, nodename, dstip, srcip, agent, hostname, filename, process);
                            }
                        }
                    
                                    
                        if (!ipsrcMap.containsKey(srcip)) {
                
                            attr = eventBean.getAttributesFacade().findByValueAndType(srcip,misp_ipsrc);
                            ipsrcMap.put(srcip, attr);
                        
                            if (attr != null) {
                                alert_type = 22;
                            
                                artifacts = "{\"artifacts\": [{\"dataType\": \"ip\",\"data\":\""
                                    + srcip
                                    + "\",\"message\":\" source ip\" }]}";
                            
                                createIocAlert(r, attr, alert_type, artifacts, nodename, dstip, srcip, agent, hostname, filename, process);
                            }
                        }
                    }
                    
                    break;
                    
                case "packet-graylog":
                    
                    msg = obj.getJSONObject("message");
                    
                    if (msg.has("packetbeat_event_category")) eventCategory = msg.getString("packetbeat_event_category");
        
                    if (msg.has("packetbeat_event_action")) eventAction = msg.getString("packetbeat_event_action");
        
                    if (eventAction.equals("network_flow") && eventCategory.equals("network_traffic")) {
                        
                        nodename = "controller";
                        
                        if (msg.getString("packetbeat_network_type").equals("ipv4")) {
                        
                            dstip = msg.getString("packetbeat_destination_ip");
                            srcip = msg.getString("packetbeat_source_ip");
                        }
                            
                        hostname = msg.getString("packetbeat_host_name");
                        agent = hostname;
                        
                        if (msg.has("packetbeat_process_name")) {
                
                            process = msg.getString("packetbeat_process_name");
                        } 
                    
                        if (!ipdstMap.containsKey(dstip)) {
                
                            attr = eventBean.getAttributesFacade().findByValueAndType(dstip,misp_ipdst);
                            ipdstMap.put(dstip, attr);
                        
                            if (attr != null) {
                                alert_type = 23;
                            
                                artifacts = "{\"artifacts\": [{\"dataType\": \"dstip\",\"data\":\""
                                    + dstip
                                    + "\",\"message\":\" process: "
                                    + process 
                                    + "\" }]}";
                            
                                createIocAlert(r, attr, alert_type, artifacts, nodename, dstip, srcip, agent, hostname, filename, process);
                            }
                        }
                    
                                    
                        if (!ipsrcMap.containsKey(srcip)) {
                
                            attr = eventBean.getAttributesFacade().findByValueAndType(srcip,misp_ipsrc);
                            ipsrcMap.put(srcip, attr);
                        
                            if (attr != null) {
                                alert_type = 24;
                            
                                artifacts = "{\"artifacts\": [{\"dataType\": \"srcip\",\"data\":\""
                                    + srcip
                                    + "\",\"message\":\" process: "
                                    + process 
                                    + "\" }]}";
                            
                                createIocAlert(r, attr, alert_type, artifacts, nodename, dstip, srcip, agent, hostname, filename, process);
                            }
                        }
                    }
                    
                    break;
                    
                default:
                    break;
            }
            
        } catch (JSONException e) {
            logger.error("alertflex_ctrl_exception", e);
            logger.error(r);
        }
        
        return false;
    }
    
    
    public void createIocAlert(String r, Attributes attr, int type, String artifacts, String node, 
        String dstip, String srcip, String agent, String hostname, String filename, String process) {
        
        int srv = 0;
                
        Alert a = new Alert();
            
        a.setNodeId(eventBean.getNode());
        a.setRefId(eventBean.getRefId());
        a.setAlertUuid(UUID.randomUUID().toString());
        
        Events event = eventBean.getEventsFacade().findById(attr.getEventId());
        
        if(event != null) {
            srv = event.getThreatLevelId();
            a.setEventSeverity(srv);
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
        
        switch (type) {
            case 1:
                a.setEventId("1");
                a.setCategories("md5, " + attr.getCategory());
                if (event != null) a.setDescription("FIM event, suspicious md5 - " + attr.getValue1() + ". " + event.getInfo());
                else a.setDescription("FIM event, suspicious md5 - " + attr.getValue1() + ". ");
                a.setAlertSource("MISP");
                a.setAlertType("FILE");
                break;
            case 2:
                a.setEventId("2");
                a.setCategories("sha1, " + attr.getCategory());
                if (event != null) a.setDescription("FIM event, suspicious sha1 - " + attr.getValue1() + ". " + event.getInfo());
                else a.setDescription("FIM event, suspicious sha1 - " + attr.getValue1() + ". ");
                a.setAlertSource("MISP");
                a.setAlertType("FILE");
                break;
            case 3:
                a.setEventId("3");
                a.setCategories("sha256, " + attr.getCategory());
                if (event != null) a.setDescription("FIM event, suspicious sha256 - " + attr.getValue1() + ". " + event.getInfo());
                else a.setDescription("FIM event, suspicious sha256 - " + attr.getValue1() + ". ");
                a.setAlertSource("MISP");
                a.setAlertType("FILE");
                break;
            case 4:
                a.setEventId("4");
                a.setCategories("ipdst, " + attr.getCategory());
                if (event != null) a.setDescription("HIDS event, suspicious dst ip - " + attr.getValue1() + ". " + event.getInfo());
                else a.setDescription("HIDS event, suspicious dst ip - " + attr.getValue1() + ". ");
                a.setAlertSource("MISP");
                a.setAlertType("HOST");
                
                break;
            case 5:
                a.setEventId("5");
                a.setCategories("ipsrc, " + attr.getCategory());
                if (event != null) a.setDescription("HIDS event, suspicious src ip." + event.getInfo());
                else a.setDescription("HIDS event, suspicious src ip - " + attr.getValue1() + ". ");
                a.setAlertSource("MISP");
                a.setAlertType("HOST");
                
                break;
            case 6:
                a.setEventId("6");
                a.setCategories("ipdst, " + attr.getCategory());
                if (event != null) a.setDescription("NIDS event, suspicious dst ip - " + attr.getValue1() + ". " + event.getInfo());
                else a.setDescription("NIDS event, suspicious dst ip - " + attr.getValue1() + ". ");
                a.setAlertSource("MISP");
                a.setAlertType("NET");
                
                break;
            case 7:
                a.setEventId("7");
                a.setCategories("ipsrc, " + attr.getCategory());
                if (event != null) a.setDescription("NIDS event, suspicious src ip - " + attr.getValue1() + ". " + event.getInfo());
                else a.setDescription("NIDS event, suspicious src ip - " + attr.getValue1() + ". ");
                a.setAlertSource("MISP");
                a.setAlertType("NET");
                
                break;    
            case 8:
                a.setEventId("8");
                a.setCategories("ipdst, " + attr.getCategory());
                if (event != null) a.setDescription("DNS event, suspicious dst ip - " + attr.getValue1() + ". " + event.getInfo());
                else a.setDescription("DNS event, suspicious dst ip - " + attr.getValue1() + ". ");
                a.setAlertSource("MISP");
                a.setAlertType("NET");
                
                break;
            case 9:
                a.setEventId("9");
                a.setCategories("ipsrc, " + attr.getCategory());
                if (event != null) a.setDescription("DNS event, suspicious src ip - " + attr.getValue1() + ". " + event.getInfo());
                else a.setDescription("DNS event, suspicious src ip - " + attr.getValue1() + ". ");
                a.setAlertSource("MISP");
                a.setAlertType("NET");
                
                break;
            case 10:
                a.setEventId("10");
                a.setCategories("dns, " + attr.getCategory());
                if (event != null) a.setDescription("DNS event, suspicious domain name - " + attr.getValue1() + ". " + event.getInfo());
                else a.setDescription("DNS event, suspicious domain name - " + attr.getValue1() + ". ");
                a.setAlertSource("MISP");
                a.setAlertType("NET");
                
                break;
            case 11:
                a.setEventId("11");
                a.setCategories("ipdst, " + attr.getCategory());
                if (event != null) a.setDescription("Netflow event, suspicious dst ip - " + attr.getValue1() + ". " + event.getInfo());
                else a.setDescription("Netflow event, suspicious dst ip - " + attr.getValue1() + ". ");
                a.setAlertSource("MISP");
                a.setAlertType("NET");
                
                break;
            case 12:
                a.setEventId("12");
                a.setCategories("ipsrc, " + attr.getCategory());
                if (event != null) a.setDescription("Netflow event, suspicious src ip - " + attr.getValue1() + ". " + event.getInfo());
                else a.setDescription("Netflow event, suspicious src ip - " + attr.getValue1() + ". ");
                a.setAlertSource("MISP");
                a.setAlertType("NET");
                
                break; 
            case 13:
                a.setEventId("13");
                a.setCategories("url_hostname, " + attr.getCategory());
                if (event != null) a.setDescription("HTTP event, suspicious url hostname - " + attr.getValue1() + ". " + event.getInfo());
                else a.setDescription("HTTP event, suspicious url hostname - " + attr.getValue1() + ". ");
                a.setAlertSource("MISP");
                a.setAlertType("NET");
                
                break; 
            case 14:
                a.setEventId("14");
                a.setCategories("md5, " + attr.getCategory());
                if (event != null) a.setDescription("NIDS file event, suspicious md5 - " + attr.getValue1() + ". " + event.getInfo());
                else a.setDescription("NIDS file event, suspicious md5 - " + attr.getValue1() + ". ");
                a.setAlertSource("MISP");
                a.setAlertType("NET");
                
                break; 
            
            case 15:
                a.setEventId("15");
                a.setCategories("ipdst, " + attr.getCategory());
                if (event != null) a.setDescription("WAF event, suspicious dst ip - " + attr.getValue1() + ". " + event.getInfo());
                else a.setDescription("WAF event, suspicious dst ip - " + attr.getValue1() + ". ");
                a.setAlertSource("MISP");
                a.setAlertType("NET");
                
                break;
            case 16:
                a.setEventId("16");
                a.setCategories("ipsrc, " + attr.getCategory());
                if (event != null) a.setDescription("WAF event, suspicious src ip - " + attr.getValue1() + ". " + event.getInfo());
                else a.setDescription("WAF event, suspicious src ip - " + attr.getValue1() + ". ");
                a.setAlertSource("MISP");
                a.setAlertType("NET");
                
                break;
            case 17:
                a.setEventId("17");
                a.setCategories("ipdst, " + attr.getCategory());
                if (event != null) a.setDescription("Elastic netflow event, suspicious dst ip - " + attr.getValue1() + ". " + event.getInfo());
                else a.setDescription("Elastic netflow event, suspicious dst ip - " + attr.getValue1() + ". ");
                a.setAlertSource("MISP");
                a.setAlertType("NET");
                
                break;
            case 18:
                a.setEventId("18");
                a.setCategories("ipsrc, " + attr.getCategory());
                if (event != null) a.setDescription("Elastic netflow event, suspicious src ip - " + attr.getValue1() + ". " + event.getInfo());
                else a.setDescription("Elastic netflow event, suspicious src ip - " + attr.getValue1() + ". ");
                a.setAlertSource("MISP");
                a.setAlertType("NET");
                
                break; 
            case 19:
                a.setEventId("19");
                a.setCategories("ipdst, " + attr.getCategory());
                if (event != null) a.setDescription("Elastic packetbeat event, suspicious dst ip - " + attr.getValue1() + ". " + event.getInfo());
                else a.setDescription("Elastic packetbeat event, suspicious dst ip - " + attr.getValue1() + ". ");
                a.setAlertSource("MISP");
                a.setAlertType("NET");
                
                break;
            case 20:
                a.setEventId("20");
                a.setCategories("ipsrc, " + attr.getCategory());
                if (event != null) a.setDescription("Elastic packetbeat event, suspicious src ip - " + attr.getValue1() + ". " + event.getInfo());
                else a.setDescription("Elastic packetbeat event, suspicious src ip - " + attr.getValue1() + ". ");
                a.setAlertSource("MISP");
                a.setAlertType("NET");
                
                break;
            case 21:
                a.setEventId("21");
                a.setCategories("ipdst, " + attr.getCategory());
                if (event != null) a.setDescription("Graylog netflow event, suspicious dst ip - " + attr.getValue1() + ". " + event.getInfo());
                else a.setDescription("Graylog netflow event, suspicious dst ip - " + attr.getValue1() + ". ");
                a.setAlertSource("MISP");
                a.setAlertType("NET");
                
                break;
            case 22:
                a.setEventId("22");
                a.setCategories("ipsrc, " + attr.getCategory());
                if (event != null) a.setDescription("Graylog netflow event, suspicious src ip - " + attr.getValue1() + ". " + event.getInfo());
                else a.setDescription("Graylog netflow event, suspicious src ip - " + attr.getValue1() + ". ");
                a.setAlertSource("MISP");
                a.setAlertType("NET");
                
                break; 
            case 23:
                a.setEventId("23");
                a.setCategories("ipdst, " + attr.getCategory());
                if (event != null) a.setDescription("Graylog packetbeat event, suspicious dst ip - " + attr.getValue1() + ". " + event.getInfo());
                else a.setDescription("Graylog packetbeat event, suspicious dst ip - " + attr.getValue1() + ". ");
                a.setAlertSource("MISP");
                a.setAlertType("NET");
                
                break;
            case 24:
                a.setEventId("24");
                a.setCategories("ipsrc, " + attr.getCategory());
                if (event != null) a.setDescription("Graylog packetbeat event, suspicious src ip - " + attr.getValue1() + ". " + event.getInfo());
                else a.setDescription("Graylog packetbeat event, suspicious src ip - " + attr.getValue1() + ". ");
                a.setAlertSource("MISP");
                a.setAlertType("NET");
                
                break; 
            default:
                break;
            
        }
        
        a.setSensorId("indef");
        a.setLocation("indef");
        a.setAction("indef");
        a.setStatus("processed");
        a.setFilter("");
        a.setInfo(artifacts);
        a.setTimeEvent("indef");
        Date date = new Date(); 
        a.setTimeCollr(date);
        a.setTimeCntrl(date);
        a.setAgentName(agent);
        a.setUserName("indef");
            
        a.setSrcIp(srcip);
        a.setDstIp(dstip);
        a.setDstPort(0);
        a.setSrcPort(0);
        a.setSrcHostname("indef");
        a.setDstHostname("indef");
        a.setFileName(filename);
        a.setFilePath("indef");
        a.setHashMd5("indef");
        a.setHashSha1("indef");
        a.setHashSha256("indef");
        a.setProcessId(0);
        a.setProcessName(process);
        a.setProcessCmdline("indef");
        a.setProcessPath("indef");
        a.setUrlHostname("indef");
        a.setUrlPath("indef");
        a.setContainerId("indef");
        a.setContainerName("indef");
        a.setJsonEvent("indef");
            
        eventBean.createAlert(a);
    }
}

