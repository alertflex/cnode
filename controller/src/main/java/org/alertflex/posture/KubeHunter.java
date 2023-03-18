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

package org.alertflex.posture;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Date;
import java.util.UUID;
import org.alertflex.controller.InfoMessageBean;
import org.alertflex.entity.Alert;
import org.alertflex.entity.AlertPriority;
import org.alertflex.entity.PostureKubehunter;
import org.alertflex.entity.Project;
import org.alertflex.entity.Node;
import org.alertflex.entity.PostureTask;
import org.alertflex.supp.ProjectRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class KubeHunter {

    private static final Logger logger = LogManager.getLogger(AppSecret.class);

    private InfoMessageBean eventBean;
    Project project;
    Node node;

    public KubeHunter(InfoMessageBean eb) {
        this.eventBean = eb;
        this.project = eventBean.getProject();

    }

    public void saveReport(String results, String target, String uuid) {
        
        String r = eventBean.getRefId();
        String n = eventBean.getNode();
        String p = eventBean.getHost() + ".kube-hunter";
        Date date = new Date();

        try {

            ProjectRepository pr = new ProjectRepository(project);
            String posturePath = pr.getCtrlPostureDir() + uuid + ".json";
            Path pp = Paths.get(posturePath);
            Files.write(pp, results.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            
        } catch (Exception e) {
                
            return;
        }
        
        try {
                
            PostureTask pt = new PostureTask ();
                            
            pt.setRefId(r);
            pt.setNode(n);
            pt.setProbe(p);
            pt.setPostureType("KubeHunter");
            pt.setTarget(target);
            pt.setTaskUuid(uuid);
            pt.setReportAdded(date);
                
            eventBean.getPostureTaskFacade().create(pt);
                 
        } catch (Exception e) {
            
            return;
        }
        
        try {
    
            node = eventBean.getNodeFacade().findByNodeName(r, n);

            if (node == null || p == null || results.isEmpty()) {
                return;
            }
            
            JSONObject body = new JSONObject(results);

            node = eventBean.getNodeFacade().findByNodeName(r, n);

            if (node == null || p == null || results.isEmpty()) {
                return;
            }
            
            JSONObject reportJson = new JSONObject(results);
            
            PostureKubehunter pk = new PostureKubehunter();
            pk.setScanUuid(uuid);
            pk.setRefId(r);
            pk.setNode(n);
            pk.setProbe(p);
            
            JSONArray arr = reportJson.getJSONArray("nodes");
            
            String typeNodes = "";
            
            for (int i = 0; i < arr.length(); i++) {
                
                String type = arr.getJSONObject(i).getString("type");
                
                if (i == arr.length() - 1) typeNodes = typeNodes + type;
                else typeNodes = typeNodes + type + ", ";
                
            }  
            
            pk.setKubeType(typeNodes);
            
            arr = reportJson.getJSONArray("services");
            
            String services = "";
            
            for (int i = 0; i < arr.length(); i++) {
                
                String service = arr.getJSONObject(i).getString("service");
                
                if (i == arr.length() - 1) services = services + service;
                else services = services + service + ", ";
                
            }  
            
            pk.setServices(services);
            
            arr = reportJson.getJSONArray("vulnerabilities");   
            
            for (int i = 0; i < arr.length(); i++) {
                
                String loc = arr.getJSONObject(i).getString("location");
                
                String vid = arr.getJSONObject(i).getString("vid");
                
                String cat = arr.getJSONObject(i).getString("category");
                
                String severity = arr.getJSONObject(i).getString("severity");
                
                String vuln = arr.getJSONObject(i).getString("vulnerability");
                
                String desc = arr.getJSONObject(i).getString("description");
                if (desc.length() >= 2048) {
                    String substrDesc = desc.substring(0, 2046);
                    pk.setDescription(substrDesc);
                } else {
                    pk.setDescription(desc);
                }
                
                String evidence = arr.getJSONObject(i).getString("evidence");
                
                String avdRef = arr.getJSONObject(i).getString("avd_reference");
                
                String hunter = arr.getJSONObject(i).getString("hunter");
                        
                PostureKubehunter pkExisting = eventBean.getPostureKubehunterFacade().findVulnerability(r, n, p, target, loc, vid, cat);
                        
                if (pkExisting == null) {
                    
                    pk.setLocation(loc);
                    pk.setVulnerabilityId(vid);
                    pk.setCategory(cat);
                    pk.setSeverity(severity);
                    pk.setTitle(vuln);
                    pk.setReference(avdRef);
                    pk.setTarget(target);
                    pk.setDescription(desc);
                    pk.setReportAdded(date);
                    pk.setReportUpdated(date);
                    eventBean.getPostureKubehunterFacade().create(pk);
                    
                    createHunterScanAlert(pk);
                    
                } else {
                    pkExisting.setReportUpdated(date);
                    eventBean.getPostureKubehunterFacade().edit(pkExisting);
                }
                        
            }
            
        } catch (Exception e) {
            logger.error("alertflex_ctrl_exception", e);
        }
    }

    public void createHunterScanAlert(PostureKubehunter pk) {

        Alert a = new Alert();

        a.setNode(eventBean.getNode());
        a.setRefId(eventBean.getRefId());
        a.setAlertUuid(UUID.randomUUID().toString());

        a.setEventSeverity(pk.getSeverity());
        
        AlertPriority ap = eventBean.getAlertPriorityFacade().findPriorityBySource(pk.getRefId(), "KubeHunter");
        
        int sev = ap.getSeverityDefault();
        if (pk.getSeverity().equals(ap.getText1())) sev = ap.getValue1();
        if (pk.getSeverity().equals(ap.getText2())) sev = ap.getValue2();
        if (pk.getSeverity().equals(ap.getText3())) sev = ap.getValue3();
        if (pk.getSeverity().equals(ap.getText4())) sev = ap.getValue4();
        if (pk.getSeverity().equals(ap.getText5())) sev = ap.getValue5();
        if (sev < ap.getSeverityThreshold()) return;
        
        a.setAlertSeverity(sev);
        
        a.setEventId("1");
        a.setCategories(pk.getCategory());
        
        String desc = pk.getDescription();
        if (desc.length() >= 1024) {
            String substrDesc = desc.substring(0, 1022);
            a.setDescription(substrDesc);
        } else {
            a.setDescription(desc);
        }
        
        a.setAlertSource("KubeHunter");
        a.setAlertType("MISC");

        a.setProbe(pk.getProbe());
        a.setLocation(pk.getLocation());
        a.setAction("indef");
        a.setStatus("processed");
        a.setFilter("");
        a.setInfo(pk.getReference());
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
        a.setFileName("indef");
        a.setHashMd5("indef");
        a.setHashSha1("indef");
        a.setHashSha256("indef");
        a.setProcessId(0);
        a.setProcessName(pk.getScanUuid());
        a.setProcessCmdline("indef");
        a.setProcessPath("indef");
        a.setUrlHostname("indef");
        a.setUrlPath("indef");
        a.setContainerId("indef");
        a.setContainerName("indef");
        a.setCloudInstance("indef");
        a.setIncidentExt("indef");

        eventBean.createAlert(a);

    }
}
