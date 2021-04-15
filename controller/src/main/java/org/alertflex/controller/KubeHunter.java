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
import org.alertflex.entity.HunterScan;
import org.alertflex.entity.Project;
import org.alertflex.entity.Node;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KubeHunter {

    private static final Logger logger = LoggerFactory.getLogger(KubeHunter.class);

    private InfoMessageBean eventBean;
    Project project;
    Node node;

    public KubeHunter(InfoMessageBean eb) {
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
            
            HunterScan hs = new HunterScan();
            hs.setRefId(r);
            hs.setNodeId(n);
            hs.setProbe(p);
            
            JSONArray arr = reportJson.getJSONArray("nodes");
            
            String typeNodes = "";
            
            for (int i = 0; i < arr.length(); i++) {
                
                String type = arr.getJSONObject(i).getString("type");
                
                if (i == arr.length() - 1) typeNodes = typeNodes + type;
                else typeNodes = typeNodes + type + ", ";
                
            }  
            
            hs.setKubeType(typeNodes);
            
            arr = reportJson.getJSONArray("services");
            
            String services = "";
            
            for (int i = 0; i < arr.length(); i++) {
                
                String service = arr.getJSONObject(i).getString("service");
                
                if (i == arr.length() - 1) services = services + service;
                else services = services + service + ", ";
                
            }  
            
            hs.setServices(services);
            
            arr = reportJson.getJSONArray("vulnerabilities");   
            
            for (int i = 0; i < arr.length(); i++) {
                
                String loc = arr.getJSONObject(i).getString("location");
                
                String vid = arr.getJSONObject(i).getString("vid");
                
                String cat = arr.getJSONObject(i).getString("category");
                
                String severity = arr.getJSONObject(i).getString("severity");
                
                String vuln = arr.getJSONObject(i).getString("vulnerability");
                
                String desc = arr.getJSONObject(i).getString("description");
                
                String evidence = arr.getJSONObject(i).getString("evidence");
                
                String avdRef = arr.getJSONObject(i).getString("avd_reference");
                
                String hunter = arr.getJSONObject(i).getString("hunter");
                        
                HunterScan hsExisting = eventBean.getHunterScanFacade().findScan(r,n,p,target, loc,vid,cat);
                        
                if (hsExisting == null) {
                    hs.setLocation(loc);
                    hs.setVid(vid);
                    hs.setCat(cat);
                    hs.setSeverity(severity);
                    hs.setVulnerability(vuln);
                    hs.setDescription(desc);
                    hs.setEvidence(evidence);
                    hs.setAvdReference(avdRef);
                    hs.setHunter(hunter);
                    hs.setTarget(target);
                    hs.setReportAdded(date);
                    hs.setReportUpdated(date);
                    eventBean.getHunterScanFacade().create(hs);
                } else {
                    hsExisting.setReportUpdated(date);
                    eventBean.getHunterScanFacade().edit(hsExisting);
                }
                        
            }
            
        } catch (Exception e) {
            logger.error("alertflex_ctrl_exception", e);
        }
    }

    public void createHunterScanAlert(HunterScan hs) {

        Alert a = new Alert();

        a.setNodeId(eventBean.getNode());
        a.setRefId(eventBean.getRefId());
        a.setAlertUuid(UUID.randomUUID().toString());

        a.setAlertSeverity(0);
        a.setEventSeverity(hs.getSeverity());
        a.setEventId("1");
        a.setCategories(hs.getCat());
        a.setDescription(hs.getDescription());
        a.setAlertSource("KubeHunter");
        a.setAlertType("MISC");

        a.setSensorId(hs.getProbe());
        a.setLocation(hs.getLocation());
        a.setAction("indef");
        a.setStatus("processed");
        a.setFilter("");
        a.setInfo(hs.getAvdReference());
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
