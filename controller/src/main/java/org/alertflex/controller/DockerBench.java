/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alertflex.controller;


import java.util.Date;
import java.util.UUID;
import org.alertflex.entity.Alert;
import org.alertflex.entity.DockerScan;
import org.alertflex.entity.Project;
import org.alertflex.entity.Node;
import org.json.JSONArray;
import org.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author root
 */
public class DockerBench {
    
    private static final Logger logger = LoggerFactory.getLogger(DockerBench.class);
    
    private InfoMessageBean eventBean;
    Project project;
    Node node;
    
    public DockerBench (InfoMessageBean eb) {
        this.eventBean = eb;
        this.project = eventBean.getProject();
        
    }
    
    public void saveReport(String sensor, String report) {
        
        try {
            
            String n = eventBean.getNode();
            String r = eventBean.getRefId();
            Date date = new Date();
            
            node = eventBean.getNodeFacade().findByNodeName(r,n);
        
            if (node == null || sensor == null || report.isEmpty()) return;
            
            JSONObject obj = new JSONObject(report);
            JSONArray arr = obj.getJSONArray("tests");
            
            for (int i = 0; i < arr.length(); i++) {
                
                JSONObject test = arr.getJSONObject(i);
                String testDesc = test.getString("desc");
                JSONArray results = test.getJSONArray("results");
                
                for (int j = 0; j < results.length(); j++) {
                    
                    DockerScan ds = new DockerScan();
                    
                    ds.setRefId(r);
                    ds.setNodeId(n);
                    ds.setSensor(sensor);
                    ds.setTestDesc(testDesc);
                    ds.setReportAdded(date);
                    ds.setReportUpdated(date);
                    
                    ds.setResultId(results.getJSONObject(j).getString("id"));
                    ds.setResultDesc(results.getJSONObject(j).getString("desc"));
                    ds.setResult(results.getJSONObject(j).getString("result"));
                    
                    Integer severity = 0;
                    switch (ds.getResult()) {
                        case "PASS":
                            severity = 0;
                            break;
                        case "INFO":
                            severity = 1;
                            break;
                        case "NOTE":
                            severity = 2;
                            break;
                        case "WARN":
                            severity = 3;
                            break;
                        default:
                            break;
                    }
                    
                    ds.setSeverity(severity);
                                        
                    ds.setDetails("indef");
                    if (results.getJSONObject(j).has("details")) ds.setDetails(results.getJSONObject(j).getString("details"));
                    
                    DockerScan dsExisting = eventBean.getDockerScanFacade().findRecord(ds.getRefId(), 
                        ds.getNodeId(), 
                        ds.getSensor(), 
                        ds.getResultId(),
                        ds.getResult());
                    
                    if (dsExisting == null) {
                        
                        // create alert
                        eventBean.getDockerScanFacade().create(ds);
                        
                        createDockerScanAlert(ds);
                    
                    } else {
                        
                        dsExisting.setReportUpdated(date);
                        eventBean.getDockerScanFacade().edit(dsExisting);
                    }
                    
                }
            }
    
                            
        } catch (Exception e) {
            logger.error("alertflex_ctrl_exception", e);
        }
    }
    
    
public void createDockerScanAlert(DockerScan ds) {
    
       
        Alert a = new Alert();
            
        a.setNodeId(eventBean.getNode());
        a.setRefId(eventBean.getRefId());
        a.setAlertUuid(UUID.randomUUID().toString());
        
        a.setAlertSeverity(ds.getSeverity());
        a.setEventId("1");
        a.setCategories("docker_bench");
        a.setDescription(ds.getResultDesc());
        a.setAlertSource("DockerBench");
        a.setAlertType("MISC");
    
        a.setSensorId(ds.getSensor());
        a.setLocation("indef");
        a.setAction("indef");
        a.setStatus("processed");
        a.setFilter("");
        a.setInfo(ds.getTestDesc());
        a.setTimeEvent("indef");
        Date date = new Date(); 
        a.setTimeCollr(date);
        a.setTimeCntrl(date);
        a.setAgentName("indef");
        a.setUserName("indef");
            
        a.setSrcIp("0.0.0.0");
        a.setDstIp("0.0.0.0");
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
            
        eventBean.createAlert(a);
        
    }
}
