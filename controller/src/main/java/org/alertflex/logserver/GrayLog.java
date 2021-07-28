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

package org.alertflex.logserver;

import com.maxmind.geoip.Location;
import com.maxmind.geoip.LookupService;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.alertflex.common.GeoIp;
import org.alertflex.entity.Alert;
import org.json.JSONObject;

public class GrayLog {
    
    DatagramSocket socket = null;
    InetAddress iaHost = null;
    int logPort = 0;
    
    public GrayLog(DatagramSocket ds, InetAddress ih, int lp) {
        socket = ds;
        iaHost = ih;
        logPort = lp;
    }
    
    public void SendStringToLog(String log) {
        
        if (socket != null &&  iaHost != null && logPort != 0) {
            
            try {
                
                byte [] data = log.getBytes(StandardCharsets.UTF_8);
                DatagramPacket packet = new DatagramPacket( data, data.length, iaHost, logPort ) ;
                socket.send( packet ) ;
            
            } catch (Exception e) {
                e.printStackTrace();
            } 
        }
    }
    
    public void SendAlertToLog(Alert a) {
        
        if (socket != null &&  iaHost != null && logPort != 0) {
            
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
                + ",\"_source_type\":\""
                + a.getAlertType()
                + "\",\"_source_name\":\""
                + a.getAlertSource()
		+ "\",\"_project_id\":\""
                + a.getRefId()
                + "\",\"_alert uuid\":\""
                + a.getAlertUuid()
                + "\",\"_severity\":"
                + a.getAlertSeverity()
                + ",\"_event\":\""
                + a.getEventId()
                + "\",\"_categories\":\""
                + a.getCategories()
                + "\",\"_description\":\""
                + a.getDescription()
                + "\",\"_src_ip\":\""
                + a.getSrcIp()
                + "\",\"_src_hostname\":\""
                + a.getSrcHostname()
                + "\",\"_src_port\":"
                + a.getSrcPort()
                + ",\"_dst_ip\":\""
                + a.getDstIp()
                + "\",\"_dst_hostname\":\""
                + a.getDstHostname()
                + "\",\"_dst_port\":"
                + a.getDstPort()
                + ",\"_sensor\":\""
                + a.getSensorId()
                + "\",\"_process\":\""
                + a.getProcessName()
                + "\",\"_container\":\""
                + a.getContainerName()
                + "\",\"_agent\":\""
                + a.getAgentName()
                + "\",\"_user\":\""
                + a.getUserName()
                + "\",\"_file\":\""
                + a.getFileName()
                + "\",\"_location\":\""
                + a.getLocation()
                + "\",\"_action\":\""
                + a.getAction()
                + "\",\"_status\":\""
                + a.getStatus()
                + "\",\"_filter\":\""
                + a.getFilter()
                + "\",\"_collector_time\":\""
                + a.getTimeEvent()
                + "\",\"_controller_time\":\""
                + time
                + "\"}";
                                
                byte [] data = alert.getBytes(StandardCharsets.UTF_8);
                DatagramPacket packet = new DatagramPacket( data, data.length, iaHost, logPort ) ;
                socket.send( packet ) ;
            
            } catch (Exception e) {
                e.printStackTrace();
        
            } 
        }
    }
    
    public void SendSuricataToLog(String log, LookupService ls) {
        
        String graylogJson ="";
        
        if (socket != null &&  iaHost != null && logPort != 0) {
            try {
                
                JSONObject obj = new JSONObject(log);
        
                String message = obj.getString("short_message");
                
                GeoIp dstIp;
                GeoIp srcIp;
                String srcip = "";
                String dstip = "";
                String cc = "";
                float lat = 0;
                float lon = 0;
                
                dstip = obj.getString("dstip");
                dstIp = new GeoIp(dstip);
                
                srcip = obj.getString("srcip");
                srcIp = new GeoIp(srcip);
                
                if (ls != null) {
                
                    try {
                        
                        InetAddress ia_src = InetAddress.getByName(srcip);
                    
                        if (!ia_src.isSiteLocalAddress()){
                            Location loc = ls.getLocation(srcip);
                            lat = loc.latitude;
                            lon = loc.longitude;
                            cc = loc.countryCode;
                            
                            srcIp = new GeoIp(srcip,cc,lat,lon);
                        } 
                        
                        InetAddress ia_dst = InetAddress.getByName(dstip);
                
                        if (!ia_dst.isSiteLocalAddress()) {
                            Location loc = ls.getLocation(dstip);
                            lat = loc.latitude;
                            lon = loc.longitude;
                            cc = loc.countryCode;
                            dstIp = new GeoIp(dstip,cc,lat,lon);
                        }
                    } catch (Exception e) {}
                }
            
                switch(message) {
            
                    case "dns-nids":
                        graylogJson = ConvertDnsToLog(obj,dstIp,srcIp);
                        break;
                    case "http-nids":
                        graylogJson = ConvertHttpToLog(obj,dstIp,srcIp);
                        break;
                    case "netflow-nids":
                        graylogJson = ConvertNetflowToLog(obj,dstIp,srcIp);
                        break;
                    case "file-nids":
                        graylogJson = ConvertFileToLog(obj,dstIp,srcIp);
                        break;
                                       
                    default:
                        return;
                }
                
                
                                
                byte [] data = graylogJson.getBytes(StandardCharsets.UTF_8);
                DatagramPacket packet = new DatagramPacket( data, data.length, iaHost, logPort ) ;
                socket.send( packet ) ;
            
            } catch (Exception e) {
                e.printStackTrace();
        
            }
        }
    }
    
    public String ConvertDnsToLog(JSONObject obj, GeoIp dstIp, GeoIp srcIp) {
        
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        String time = formatter.format(date);
        
        String report = "{\"version\": \"1.1\",\"host\":\""
            + obj.getString("node")
            + "\",\"short_message\":\"dns-nids\",\"full_message\":\"DNS event from Suricata NIDS\",\"level\":"
            + obj.getInt("level")
            + ",\"_source_type\":\"NET\",\"_source_name\":\"Suricata\",\"_project_id\":\""
            + obj.getString("project_id")
            + "\",\"_sensor\":\""
            + obj.getString("sensor")
            + "\",\"_event_time\":\""
            + obj.getString("event_time")
            + "\",\"_controller_time\":\""
            + time
            + "\",\"_dns_type\":\""
            + obj.getString("dns_type")
            + "\",\"_src_ip\":\""
            + srcIp.getIp()
            + "\",\"_src_ip_geo_country\":\""
            + srcIp.getCc()
            + "\",\"_src_ip_geo_location\":\""
            + srcIp.getLat() + "," + srcIp.getLon()
            + "\",\"_src_agent\":\""
            + obj.getString("srcagent")
            + "\",\"_src_port\":"
            + obj.getInt("srcport")
            + ",\"_dst_ip\":\""
            + dstIp.getIp()
            + "\",\"_dst_ip_geo_country\":\""
            + dstIp.getCc()
            + "\",\"_dst_ip_geo_location\":\""
            + dstIp.getLat() + "," + dstIp.getLon()
            + "\",\"_dst_agent\":\""
            + obj.getString("dstagent")
            + "\",\"_dst_port\":"
            + obj.getInt("dstport")
            + ",\"_rrname\":\""
            + obj.getString("rrname")
            + "\",\"_rrtype\":\""
            + obj.getString("rrtype")
            + "\"}";
        
        return report;
    }
    
    public String ConvertHttpToLog(JSONObject obj, GeoIp dstIp, GeoIp srcIp) {
        
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        String time = formatter.format(date);
        
        String report = "{\"version\": \"1.1\",\"host\":\""
            + obj.getString("node")
            + "\",\"short_message\":\"http-nids\",\"full_message\":\"HTTP event from Suricata NIDS\",\"level\":"
            + obj.getInt("level")
            + ",\"_source_type\":\"NET\",\"_source_name\":\"Suricata\",\"_project_id\":\""
            + obj.getString("project_id")
            + "\",\"_sensor\":\""
            + obj.getString("sensor")
            + "\",\"_event_time\":\""
            + obj.getString("event_time")
            + "\",\"_controller_time\":\""
            + time
            + "\",\"_src_ip\":\""
            + srcIp.getIp()
            + "\",\"_src_ip_geo_country\":\""
            + srcIp.getCc()
            + "\",\"_src_ip_geo_location\":\""
            + srcIp.getLat() + "," + srcIp.getLon()
            + "\",\"_src_agent\":\""
            + obj.getString("srcagent")
            + "\",\"_src_port\":"
            + obj.getInt("srcport")
            + ",\"_dst_ip\":\""
            + dstIp.getIp()
            + "\",\"_dst_ip_geo_country\":\""
            + dstIp.getCc()
            + "\",\"_dst_ip_geo_location\":\""
            + dstIp.getLat() + "," + dstIp.getLon()
            + "\",\"_dst_agent\":\""
            + obj.getString("dstagent")
            + "\",\"_dst_port\":"
            + obj.getInt("dstport")
            + ",\"_url_hostname\":\""
            + obj.getString("url_hostname")
            + "\",\"_url_path\":\""
            + obj.getString("url_path")
            + "\",\"_http_user_agent\":\""
            + obj.getString("http_user_agent")
            + "\",\"_http_content_type\":\""
            + obj.getString("http_content_type")
            + "\"}";
        
        return report;
    }
    
    public String ConvertNetflowToLog(JSONObject obj, GeoIp dstIp, GeoIp srcIp) {
        
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        String time = formatter.format(date);
                
        String report = "{\"version\": \"1.1\",\"host\":\""
            + obj.getString("node")
            + "\",\"short_message\":\"netflow-nids\",\"full_message\":\"Netflow event from Suricata NIDS\",\"level\":"
            + obj.getInt("level")
            + ",\"_source_type\":\"NET\",\"_source_name\":\"Suricata\",\"_project_id\":\""
            + obj.getString("project_id")
            + "\",\"_sensor\":\""
            + obj.getString("sensor")
            + "\",\"_event_time\":\""
            + obj.getString("event_time")
            + "\",\"_controller_time\":\""
            + time
            + "\",\"_protocol\":\""
            + obj.getString("protocol")
            + "\",\"_process\":\""
            + obj.getString("process")
            + "\",\"_src_ip\":\""
            + srcIp.getIp()
            + "\",\"_src_ip_geo_country\":\""
            + srcIp.getCc()
            + "\",\"_src_ip_geo_location\":\""
            + srcIp.getLat() + "," + srcIp.getLon()
            + "\",\"_src_agent\":\""
            + obj.getString("srcagent")
            + "\",\"_src_port\":"
            + obj.getInt("srcport")
            + ",\"_dst_ip\":\""
            + dstIp.getIp()
            + "\",\"_dst_ip_geo_country\":\""
            + dstIp.getCc()
            + "\",\"_dst_ip_geo_location\":\""
            + dstIp.getLat() + "," + dstIp.getLon()
            + "\",\"_dst_agent\":\""
            + obj.getString("dstagent")
            + "\",\"_dst_port\":"
            + obj.getInt("dstport")
            + ",\"_bytes\":"
            + obj.getInt("bytes")
            + ",\"_packets\":"
            + obj.getInt("packets")
            + "}";
        
        return report;
    }
    
    public String ConvertFileToLog(JSONObject obj, GeoIp dstIp, GeoIp srcIp) {
        
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        String time = formatter.format(date);
                
        String report = "{\"version\": \"1.1\",\"host\":\""
            + obj.getString("node")
            + "\",\"short_message\":\"file-nids\",\"full_message\":\"File event from Suricata NIDS\",\"level\":"
            + obj.getInt("level")
            + ",\"_source_type\":\"NET\",\"_source_name\":\"Suricata\",\"_project_id\":\""
            + obj.getString("project_id")
            + "\",\"_sensor\":\""
            + obj.getString("sensor")
            + "\",\"_event_time\":\""
            + obj.getString("event_time")
            + "\",\"_controller_time\":\""
            + time
            + "\",\"_protocol\":\""
            + obj.getString("protocol")
            + "\",\"_process\":\""
            + obj.getString("process")
            + "\",\"_src_ip\":\""
            + srcIp.getIp()
            + "\",\"_src_ip_geo_country\":\""
            + srcIp.getCc()
            + "\",\"_src_ip_geo_location\":\""
            + srcIp.getLat() + "," + srcIp.getLon()
            + "\",\"_src_agent\":\""
            + obj.getString("srcagent")
            + "\",\"_src_port\":"
            + obj.getInt("srcport")
            + ",\"_dst_ip\":\""
            + dstIp.getIp()
            + "\",\"_dst_ip_geo_country\":\""
            + dstIp.getCc()
            + "\",\"_dst_ip_geo_location\":\""
            + dstIp.getLat() + "," + dstIp.getLon()
            + "\",\"dst_agent\":\""
            + obj.getString("dstagent")
            + "\",\"_dst_port\":"
            + obj.getInt("dstport")
            + ",\"_size\":"
            + obj.getInt("size")
            + ",\"_filename\":\""
            + obj.getString("filename")
            + "\",\"_state\":\""
            + obj.getString("state")
            + "\",\"_md5\":\""
            + obj.getString("md5")
            + "\"}";
        
        return report;
    }
    
    public void SendAwsWafToLog(String log, LookupService ls) {
        
        String graylogJson ="";
        
        if (socket != null &&  iaHost != null && logPort != 0) {
            try {
                
                JSONObject obj = new JSONObject(log);
        
                GeoIp srcIp;
                String srcip = "";
                String cc = "";
                float lat = 0;
                float lon = 0;
                
                srcip = obj.getString("clientIp");
                srcIp = new GeoIp(srcip);
                
                if (ls != null) {
                
                    try {
                        
                        InetAddress ia_src = InetAddress.getByName(srcip);
                    
                        if (!ia_src.isSiteLocalAddress()){
                            Location loc = ls.getLocation(srcip);
                            lat = loc.latitude;
                            lon = loc.longitude;
                            cc = loc.countryCode;
                            
                            srcIp = new GeoIp(srcip,cc,lat,lon);
                        } 
                        
                    } catch (Exception e) {}
                }
            
                graylogJson = ConvertAwsWafEventToLog(obj, srcIp);
                
                byte [] data = graylogJson.getBytes(StandardCharsets.UTF_8);
                DatagramPacket packet = new DatagramPacket( data, data.length, iaHost, logPort ) ;
                socket.send( packet ) ;
            
            } catch (Exception e) {
                e.printStackTrace();
        
            }
        }
    }
    
    public String ConvertAwsWafEventToLog(JSONObject obj, GeoIp ip) {
        
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        String time = formatter.format(date);

        String report = "{\"version\": \"1.1\",\"host\":\""
                + obj.getString("node")
                + "\",\"short_message\":\"event-awswaf\",\"full_message\":\"Event from AWS WAF\",\"level\":"
                + obj.getInt("level")
                + ",\"_source_type\":\"NET\",\"_source_name\":\"AwsWaf\",\"_project_id\":\""
                + obj.getString("project_id")
                + "\",\"_sensor\":\""
                + obj.getString("sensor")
                + "\",\"_event_time\":\""
                + obj.getString("collected_time")
                + "\",\"_controller_time\":\""
                + time
                + "\",\"_description\":\""
                + obj.getString("description")
                + "\",\"_terminating_rule_id\":\""
                + obj.getString("terminatingRuleId")
                + "\",\"_terminating_rule_type\":\""
                + obj.getString("terminatingRuleType")
                + "\",\"_action\":\""
                + obj.getString("action")
                
                + "\",\"_clientIp\":\""
                + ip.getIp()
                + "\",\"_src_ip_geo_country\":\""
                + ip.getCc()
                + "\",\"_src_ip_geo_location\":\""
                + ip.getLat() + "," + ip.getLon()
                
                + "\",\"_uri\":\""
                + obj.getString("uri")
                + "\",\"_args\":\""
                + obj.getString("args")
                + "\",\"_http_method\":\""
                + obj.getString("httpMethod")
                + "\",\"_sever\":\""
                + obj.getString("server")
                + "\"}";

        return report;
    }
}
