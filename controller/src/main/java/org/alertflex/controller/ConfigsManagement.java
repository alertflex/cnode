/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alertflex.controller;


import org.alertflex.common.ProjectRepository;
import org.alertflex.filters.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import org.alertflex.entity.Project;
import org.alertflex.entity.Node;
import org.alertflex.entity.NodePK;
import org.alertflex.entity.Sensor;
import org.alertflex.entity.SensorPK;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author root
 */
public class ConfigsManagement {
    
    private static final Logger logger = LoggerFactory.getLogger(ConfigsManagement.class);
    
    private InfoMessageBean eventBean;
    Project project;
    Node node;
    
    ProjectRepository lr;
    
        
    public ConfigsManagement (InfoMessageBean eb) {
        this.eventBean = eb;
        this.project = eventBean.getProject();
        
    }
    
    public void saveConfig(String sensor, int typeSensor, String data) {
        
        try {
            
            String n = eventBean.getNode();
            String r = project.getRefId();
            
            node = eventBean.getNodeFacade().findByNodeName(r,n);
        
            if (node == null) {
                
                NodePK nodePK = new NodePK(r,n);
                
                node = new Node();
                
                node.setNodePK(nodePK);
        
                node.setUnit("change unit");
            
                node.setDescription("change desc");
                
                eventBean.getNodeFacade().create(node);
                
                SensorPK sensorPK = new SensorPK(r,n,"master-crs");
                Sensor s = new Sensor(sensorPK);
                
                s.setAgent("master");
                s.setDescription("Master falco");
                s.setSensorType("falco");
                s.setIprepUpdate(0);
                s.setRulesUpdate(0);
            
                eventBean.getSensorFacade().create(s);
            
                sensorPK = new SensorPK(r,n,"master-hids");
                s = new Sensor(sensorPK);
                
                s.setAgent("master");
                s.setDescription("Wazuh manager");
                s.setSensorType("wazuh");
                s.setIprepUpdate(0);
                s.setRulesUpdate(0);
            
                eventBean.getSensorFacade().create(s);
                    
                sensorPK = new SensorPK(r,n,"master-nids");
                s = new Sensor(sensorPK);
                
                s.setAgent("master");              
                s.setDescription("Master suricata");
                s.setSensorType("suricata");
                s.setIprepUpdate(0);
                s.setRulesUpdate(0);
            
                eventBean.getSensorFacade().create(s);
                
                sensorPK = new SensorPK(r,n,"master-waf");
                s = new Sensor(sensorPK);
                
                s.setAgent("master");              
                s.setDescription("Master modsec");
                s.setSensorType("modsec");
                s.setIprepUpdate(0);
                s.setRulesUpdate(0);
            
                eventBean.getSensorFacade().create(s);
            } else {
                
                Sensor s = eventBean.getSensorFacade().findSensorByName(r,n,sensor);
                
                if (s == null) {
                    
                    s = new Sensor();
                
                    s.setAgent("indef");              
                    s.setDescription("change me");
                    s.setIprepUpdate(0);
                    s.setRulesUpdate(0);
                    
                    String sensorName;
                    
                    switch (typeSensor) {
                        
                        case 0 :
                            sensorName = sensor + "-crs";
                            s.setSensorType("falco");
                            break;
                        case 1:
                            sensorName = sensor + "-hids";
                            s.setSensorType("wazuh");
                            break;
                        case 2 :
                            sensorName = sensor + "-nids";
                            s.setSensorType("suricata");
                            break;
                        case 3:
                            sensorName = sensor + "-waf";
                            s.setSensorType("modsec");
                            break;
                        default:
                            return;
                    }
                    
                    SensorPK sensorPK = new SensorPK(r,n,sensorName);
                    s.setSensorPK(sensorPK);
                
                    eventBean.getSensorFacade().create(s);
                }
            }
            
            lr = new ProjectRepository(project);
            
            boolean ready = lr.initSensor(node, sensor);
            
            if (data.isEmpty() || !ready) return;
            
            String configPath = "dummy.conf";
            
            switch (typeSensor) {
                
                case 0 :
                    configPath = lr.getSensorDir(sensor) + "falco.yaml";
                    break;
                case 1 :
                    configPath = lr.getSensorDir(sensor) + "ossec.conf";
                    break;
                case 2 :
                    configPath = lr.getSensorDir(sensor) + "suricata.yaml";
                    break;
                case 3 :
                    configPath = lr.getSensorDir(sensor) + "main.conf";
                    break;
                default:
                    break;
                    
            }
            
            Path p = Paths.get(configPath);
            
            Files.write(p, data.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    
                            
        } catch (IOException e) {
            logger.error("alertflex_ctrl_exception", e);
        }
    }
}
