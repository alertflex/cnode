/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alertflex.controller;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.alertflex.common.Netflow;
import org.alertflex.entity.Alert;
import org.alertflex.entity.Project;

/**
 *
 * @author root
 */
public class LogServer {
    
    Project p = null;
    DatagramSocket socket = null;
    InetAddress iaHost = null;
    int logPort = 0;
       
    
    public LogServer(Project p) {
        
        this.p = p;
        
        try {
            
            String logHost = p.getLogHost();
            logPort = p.getLogPort();
        
        
            if (!logHost.isEmpty() && logPort != 0) {
                iaHost = InetAddress.getByName(logHost);
                socket = new DatagramSocket();
            }
            
        } catch (Exception e)  {
            e.printStackTrace();
        } 
    }
    
    public Boolean open() {
        
        if( socket != null ) {
            
            try {
            
                String logHost = p.getLogHost();
                logPort = p.getLogPort();
        
        
                if (!logHost.isEmpty() && logPort != 0) {
                    iaHost = InetAddress.getByName(logHost);
                    socket = new DatagramSocket();
                }
            
            } catch (Exception e)  {
                return false;
            } 
        } else return false;
        
        return true;
    }
    
    void close() {
        
        if( socket != null ) socket.close();
    }
    
    
        
    public void SendAlertToLog(Alert a) {
        
        if (socket != null) {
            
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
                + ",\"_type\":\""
                + a.getAlertType()
                + "\",\"_source\":\""
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
    
    public void SendNetflowToLog(Netflow n) {
        
        if (socket != null) {
            try {
                
                Date date = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
                String time = formatter.format(date);
                
                String alert = "{\"version\": \"1.1\",\"host\":\""
                +  n.getNode()
                + "\",\"short_message\":\"netflow-beat\""
                + ",\"full_message\":\"Netflow event from Beats\",\"level\": 7,\"_type\":\"NET\",\"_source\":\"Alertflex\",\"_project_id\":\""
                + n.getRefId()
                + "\",\"_sensor\":\""
                + n.getHostname()
                + "\",\"_event_time\":\""
                + n.getTimestamp()
                + "\",\"_collected_time\":\""
                + time
                + "\",\"_protocol\":\""
                + n.getProtocol()
                + "\",\"_process\":\""
                + n.getProcessName()
                + "\",\"_pid\":"
                + n.getProcessId()
                + ",\"_path\":\""
                + n.getProcessPath()
                + "\",\"_src_ip\":\""
                + n.getSrcIp()
                + "\",\"_src_hostname\":\""
                + n.getSrcHostname()
                + "\",\"_srcport\":"
                + n.getSrcPort()
                + ",\"_dstip\":\""
                + n.getDstIp()
                + "\",\"_dstagent\":\""
                + n.getSrcHostname()
                + "\",\"_dstport\":"
                + n.getSrcPort()
                + ",\"_packets\":"
                + n.getPackets()
                + ",\"_bytes\":"
                + n.getBytes()
                + "}";
                                
                byte [] data = alert.getBytes(StandardCharsets.UTF_8);
                DatagramPacket packet = new DatagramPacket( data, data.length, iaHost, logPort ) ;
                socket.send( packet ) ;
            
            } catch (Exception e) {
                e.printStackTrace();
        
            }
        }
    }
}
