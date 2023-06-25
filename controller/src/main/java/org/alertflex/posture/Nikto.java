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
import org.alertflex.entity.Project;
import org.alertflex.entity.Node;
import org.alertflex.entity.PostureTask;
import org.alertflex.entity.PostureNikto;
import org.alertflex.supp.ProjectRepository;
import org.json.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;

public class Nikto {

    private static final Logger logger = LogManager.getLogger(Nikto.class);

    private InfoMessageBean eventBean;
    Project project;
    Node node;

    public Nikto(InfoMessageBean eb) {
        this.eventBean = eb;
        this.project = eventBean.getProject();

    }

    public void saveReport(String results, String target, String uuid, String alertCorr, int alertType) {
        
        String r = eventBean.getRefId();
        String n = eventBean.getNode();
        String p = eventBean.getHost() + ".nikto";
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
            pt.setPostureType("Nikto");
            pt.setTarget(target);
            pt.setTaskUuid(uuid);
            pt.setReportAdded(date);
                
            eventBean.getPostureTaskFacade().create(pt);
                 
        } catch (Exception e) {
            
            return;
        }
        
        try {
    
            node = eventBean.getNodeFacade().findByNodeName(r, n);
            
            AlertPriority ap = eventBean.getAlertPriorityFacade().findPriorityBySource(r, "Nikto");
            
            int alertSeverity = ap.getSeverityDefault();
            String severity = "low";
            
            switch (alertSeverity) {
                case 0 : severity = "info";
                    break;
                case 1 : severity = "low";
                    break;
                case 2 : severity = "medium";
                    break;
                case 3 : severity = "high";
                    break;
            }

            if (node == null || p == null || results.isEmpty()) {
                return;
            }
            
            JSONObject resultsJson = new JSONObject(results);
            
            String host = resultsJson.getString("host");
            String ip = resultsJson.getString("ip");
            String port = resultsJson.getString("port");
            String banner = resultsJson.getString("banner");
            
            JSONArray arrResults = resultsJson.getJSONArray("vulnerabilities");

            for (int i = 0; i < arrResults.length(); i++) {

                JSONObject result = arrResults.getJSONObject(i);
                
                String id = result.getString("id");
                String OSVDB = result.getString("OSVDB");
                String url = result.getString("url");
                String method = result.getString("method");
                String msg = result.getString("msg"); 
                
                PostureNikto pn = new PostureNikto();
                    
                pn.setNode(n);
                pn.setRefId(r);
                pn.setProbe(p);
                pn.setScanUuid(uuid);
                pn.setTarget(host);
                pn.setIp(ip);
                pn.setPort(port);
                pn.setBanner(banner);
                pn.setSeverity(severity);
                pn.setVulnId(id);
                pn.setVulnOsvdb(OSVDB);
                pn.setVulnMethod(method);
                pn.setVulnUrl(url);
                pn.setVulnMsg(msg);
                pn.setReportAdded(date);
                pn.setReportUpdated(date);
                pn.setStatus("processed");
                
                String alertUuid = "indef";
                
                PostureNikto pnExisting = eventBean.getPostureNiktoFacade().findVulnerability(r, n, p, host, port, id);
                
                if (pnExisting == null) {
                    alertUuid = createPostureNiktoAlert(pn, severity, alertSeverity);
                    if (alertUuid != null) pn.setAlertUuid(alertUuid);
                    else pn.setAlertUuid("indef");
                    eventBean.getPostureNiktoFacade().create(pn);
                
                } else {
                    int corr = 0;
                        
                    switch (alertCorr) {
                        case "AllFindings": corr = 1;
                            break;
                        case "NonConfirmed": corr = 2;
                            break;
                        case "OnlyNew": corr = 3;
                            break;
                    }
                    
                    if (corr == 0) corr = alertType;
                    
                    switch (corr) {
                    
                        case 1: // all-existing
                        
                            alertUuid = createPostureNiktoAlert(pn, severity, alertSeverity);
                            if (alertUuid != null) pnExisting.setAlertUuid(alertUuid);
                                                        
                            pnExisting.setReportUpdated(date);
                            eventBean.getPostureNiktoFacade().edit(pnExisting);
                        
                            break;
                        
                        case 2: // non confirmed 
                        
                            if (!pnExisting.getStatus().equals("confirmed")) {
                                alertUuid = createPostureNiktoAlert(pn, severity, alertSeverity);
                                if (alertUuid != null) pnExisting.setAlertUuid(alertUuid);
                            }
                            
                            pnExisting.setReportUpdated(date);
                            eventBean.getPostureNiktoFacade().edit(pnExisting);
                            
                            break;
                        
                        case 3: // new
                        
                            pnExisting.setReportUpdated(date);
                            eventBean.getPostureNiktoFacade().edit(pnExisting);
                            
                            break;
                    }
                }
            }

        } catch (Exception e) {
            logger.error("alertflex_ctrl_exception", e);
        }
    }

    public String createPostureNiktoAlert(PostureNikto pn, String severity, int alertSeverity) {

        Alert a = new Alert();

        a.setNode(eventBean.getNode());
        a.setRefId(eventBean.getRefId());
        a.setAlertUuid(UUID.randomUUID().toString());

        a.setEventSeverity(severity);
        a.setAlertSeverity(alertSeverity);
        a.setEventId(pn.getVulnId());
        
        a.setCategories("nikto");
        a.setAlertSource("Nikto");
        a.setAlertType("MISC");

        a.setProbe(pn.getProbe());
        a.setLocation(pn.getTarget());
        a.setAction("indef");
        a.setStatus("processed");
        a.setFilter("");
        
        String desc = pn.getVulnMsg();
        if (desc.length() >= 1024) {
            String substrDesc = desc.substring(0, 1022);
            a.setDescription(substrDesc);
        } else {
            a.setDescription(desc);
        }
        
        String info = pn.getVulnOsvdb();
        if (info.length() >= 1024) {
            String substrInfo = info.substring(0, 1022);
            a.setInfo(substrInfo);
        } else {
            a.setInfo(info);
        }
        
        a.setTimeEvent("indef");
        Date date = new Date();
        a.setTimeCollr(date);
        a.setTimeCntrl(date);
        a.setAgentName("indef");
        a.setUserName("indef");

        a.setSrcIp("0.0.0.0");
        a.setDstIp(pn.getTarget());
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
        a.setProcessName(pn.getScanUuid());
        a.setProcessCmdline("indef");
        a.setProcessPath("indef");
        a.setUrlHostname(pn.getVulnUrl());
        a.setUrlPath("indef");
        a.setContainerId("indef");
        a.setContainerName("indef");
        a.setCloudInstance("indef");
        a.setIncidentExt("indef");

        eventBean.createAlert(a);
        
        return a.getAlertUuid();
    }
}
