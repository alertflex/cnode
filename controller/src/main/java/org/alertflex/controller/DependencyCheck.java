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

package org.alertflex.controller;

import java.util.Date;
import java.util.UUID;
import org.alertflex.entity.Alert;
import org.alertflex.entity.AlertPriority;
import org.alertflex.entity.DependencyScan;
import org.alertflex.entity.Project;
import org.alertflex.entity.Node;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DependencyCheck {

    private static final Logger logger = LoggerFactory.getLogger(DependencyCheck.class);

    private InfoMessageBean eventBean;
    Project project;
    Node node;

    public DependencyCheck(InfoMessageBean eb) {
        this.eventBean = eb;
        this.project = eventBean.getProject();

    }

    public void saveReport(String report, String target) {

        try {
            
            String r = eventBean.getRefId();
            String n = eventBean.getNode();
            String p = eventBean.getProbe();
            Date date = new Date();

            node = eventBean.getNodeFacade().findByNodeName(r, n);

            if (node == null || p == null || report.isEmpty()) {
                return;
            }
            
            JSONObject reportJson = new JSONObject(report);
            
            JSONArray arr = reportJson.getJSONArray("dependencies");
            
            for (int i = 0; i < arr.length(); i++) {
                
                if (arr.getJSONObject(i).has("vulnerabilities")) {
                
                    JSONArray vulns = arr.getJSONObject(i).getJSONArray("vulnerabilities");
                
                    String fileName = arr.getJSONObject(i).getString("fileName");
                
                    String filePath = arr.getJSONObject(i).getString("filePath");
                    
                    for (int j = 0; j < vulns.length(); j++) {
                    
                        DependencyScan ds = new DependencyScan();
                        ds.setRefId(r);
                        ds.setNodeId(n);
                        ds.setProbe(p);
                        ds.setProjectId(target);
                        ds.setFileName(fileName);
                        ds.setFilePath(filePath);
                
                        String name = vulns.getJSONObject(j).getString("name");
                        ds.setVulnName(name);
                    
                        String severity = vulns.getJSONObject(j).getString("severity");
                        ds.setSeverity(severity);
                    
                        String desc = vulns.getJSONObject(j).getString("description");
                    
                        if (desc.length() >= 2048) {
                            String substrDesc = desc.substring(0, 2046);
                            ds.setDescription(substrDesc);
                        } else {
                            ds.setDescription(desc);
                        }
                    
                        JSONArray ref = vulns.getJSONObject(j).getJSONArray("references");
                        String references = "";
                        for (int k = 0; k < ref.length(); k++) {
                            JSONObject url = ref.getJSONObject(k);
                            references = references + " " + url.getString("url"); //2048
                        }
                
                        if (references.length() >= 2048) {
                            String substrReferences = references.substring(0, 2046);
                            ds.setVulnRef(substrReferences);
                        } else {
                            ds.setVulnRef(references);
                        }
                    
                        ds.setReportCreated(date);
                        ds.setReportUpdated(date);
                
                        DependencyScan dsExisting = eventBean.getDependencyScanFacade().findVulnerability(r, n, p, target, name, fileName);
                
                        if (dsExisting == null) {

                            eventBean.getDependencyScanFacade().create(ds);
                    
                            createDependencyScanAlert(ds);
                    
                        } else {

                            dsExisting.setReportUpdated(date);
                            eventBean.getDependencyScanFacade().edit(dsExisting);
                        }
                    }    
                }
            }  
            
        } catch (Exception e) {
            logger.error("alertflex_ctrl_exception", e);
        }
    }

    public void createDependencyScanAlert(DependencyScan ds) {

        Alert a = new Alert();

        a.setNodeId(eventBean.getNode());
        a.setRefId(eventBean.getRefId());
        a.setAlertUuid(UUID.randomUUID().toString());

        a.setEventSeverity(ds.getSeverity());
        
        AlertPriority ap = eventBean.getAlertPriorityFacade().findPriorityBySource(ds.getRefId(), "DependencyCheck");
        
        int sev = ap.getSeverityDefault();
        if (ds.getSeverity().equals(ap.getText1())) sev = ap.getValue1();
        if (ds.getSeverity().equals(ap.getText2())) sev = ap.getValue2();
        if (ds.getSeverity().equals(ap.getText3())) sev = ap.getValue3();
        if (ds.getSeverity().equals(ap.getText4())) sev = ap.getValue4();
        if (ds.getSeverity().equals(ap.getText5())) sev = ap.getValue5();
        if (sev < ap.getSeverityThreshold()) return;
        
        a.setAlertSeverity(sev);
        
        a.setEventId(ds.getVulnName());
        a.setCategories("dependency-check");
        
        String desc = ds.getDescription();
        if (desc.length() >= 1024) {
            String substrDesc = desc.substring(0, 1022);
            a.setDescription(substrDesc);
        } else {
            a.setDescription(desc);
        }
        
        a.setAlertSource("DependencyCheck");
        a.setAlertType("MISC");

        a.setSensorId(ds.getProbe());
        a.setLocation(ds.getFilePath() + " " + ds.getFileName());
        a.setAction("indef");
        a.setStatus("processed");
        a.setFilter("");
        a.setInfo(ds.getRefId());
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
        a.setRegValue("indef");
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
        a.setCloudInstance("indef");

        eventBean.createAlert(a);

    }
}
