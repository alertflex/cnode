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
import org.alertflex.entity.PostureCloudsploit;
import org.alertflex.supp.ProjectRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;

public class Cloudsploit {

    private static final Logger logger = LogManager.getLogger(Cloudsploit.class);

    private InfoMessageBean eventBean;
    Project project;
    Node node;

    public Cloudsploit(InfoMessageBean eb) {
        this.eventBean = eb;
        this.project = eventBean.getProject();

    }

    public void saveReport(String results, String target, String uuid, String alertCorr, int alertType) {
        
        String r = eventBean.getRefId();
        String n = eventBean.getNode();
        String p = eventBean.getHost() + ".cloudsploit";
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
            pt.setPostureType("Cloudsploit");
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
            
            String cloudType = node.getNodeType();
            
            JSONArray findings = new JSONArray(results);
            
            for (int i = 0; i < findings.length(); i++) {
            
                PostureCloudsploit pc = new PostureCloudsploit();
                
                String category = "indef";
                if (findings.getJSONObject(i).has("category")) {
                    category = findings.getJSONObject(i).getString("category");
                }
                
                String description = "indef";
                if (findings.getJSONObject(i).has("description")) {
                    description = findings.getJSONObject(i).getString("description");
                }
                
                if (description.length() >= 2048) {
                    String substrDescription = description.substring(0, 2046);
                    pc.setDescription(substrDescription);
                } else {
                    pc.setDescription(description);
                }
                
                String message = "indef";
                if (findings.getJSONObject(i).has("message")) {
                    message = findings.getJSONObject(i).getString("message");
                }
                
                String plugin = "indef";
                if (findings.getJSONObject(i).has("plugin")) {
                    plugin = findings.getJSONObject(i).getString("plugin");
                }
                
                String resource = "indef";
                if (findings.getJSONObject(i).has("resource")) {
                    resource = findings.getJSONObject(i).getString("resource");
                }
                
                String title = "indef";
                if (findings.getJSONObject(i).has("title")) {
                    title = findings.getJSONObject(i).getString("title");
                }
                
                String region = "indef";
                if (findings.getJSONObject(i).has("region")) {
                    region = findings.getJSONObject(i).getString("region");
                }
                
                String status = "indef";
                if (findings.getJSONObject(i).has("status")) {
                    status = findings.getJSONObject(i).getString("status");
                }
                                
                Date dt = new Date();
                pc.setCategory(category);
                pc.setMessage(message);
                pc.setPlugin(plugin);
                pc.setResources(resource);
                pc.setTitle(title);
                pc.setRegion(region);
                pc.setSeverity(status);
                pc.setNode(n);
                pc.setProbe(p);
                pc.setCloudType(target);
                pc.setRefId(project.getRefId());
                pc.setReportAdded(dt);
                pc.setReportUpdated(dt);
                pc.setStatus("processed");
                pc.setScanUuid(uuid);
                
                String alertUuid = "";
                
                PostureCloudsploit pcExisting =  eventBean.getPostureCloudsploitFacade()
                    .findVulnerability(project.getRefId(), cloudType, region, resource, plugin, title);
                
                if (pcExisting == null) {
                    alertUuid = createPostureCloudsploitAlert(pc);
                    if (alertUuid != null) pc.setAlertUuid(alertUuid);
                    else pc.setAlertUuid("indef");
                    eventBean.getPostureCloudsploitFacade().create(pc);
                
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
                        
                            alertUuid = createPostureCloudsploitAlert(pc);
                            if (alertUuid != null) pcExisting.setAlertUuid(alertUuid);
                                                        
                            pcExisting.setReportUpdated(date);
                            eventBean.getPostureCloudsploitFacade().edit(pcExisting);
                        
                            break;
                        
                        case 2: // non confirmed 
                        
                            if (!pcExisting.getStatus().equals("confirmed")) {
                                alertUuid = createPostureCloudsploitAlert(pc);
                                if (alertUuid != null) pcExisting.setAlertUuid(alertUuid);
                            }
                            
                            pcExisting.setReportUpdated(date);
                            eventBean.getPostureCloudsploitFacade().edit(pcExisting);
                            
                            break;
                        
                        case 3: // new
                        
                            pcExisting.setReportUpdated(date);
                            eventBean.getPostureCloudsploitFacade().edit(pcExisting);
                            
                            break;
                    }
                }
            }

        } catch (Exception e) {
            logger.error("alertflex_ctrl_exception", e);
        }
    }

    public String createPostureCloudsploitAlert(PostureCloudsploit pc) {

        Alert a = new Alert();

        a.setNode(eventBean.getNode());
        a.setRefId(eventBean.getRefId());
        a.setAlertUuid(UUID.randomUUID().toString());

        AlertPriority ap = eventBean.getAlertPriorityFacade().findPriorityBySource(pc.getRefId(), "CloudSploit");
        
        int sev = ap.getSeverityDefault();
        if (pc.getSeverity().equals(ap.getText1())) sev = ap.getValue1();
        if (pc.getSeverity().equals(ap.getText2())) sev = ap.getValue2();
        if (pc.getSeverity().equals(ap.getText3())) sev = ap.getValue3();
        if (pc.getSeverity().equals(ap.getText4())) sev = ap.getValue4();
        if (sev < ap.getSeverityThreshold()) return null;
        
        a.setEventSeverity(pc.getSeverity());
        a.setAlertSeverity(sev);
        a.setEventId("1");
        
        a.setCategories(pc.getCategory());
        a.setAlertSource("Cloudsploit");
        a.setAlertType("MISC");

        a.setProbe(pc.getProbe());
        a.setLocation(pc.getResources());
        a.setAction("indef");
        a.setStatus("processed");
        a.setFilter("");
        
        String desc = pc.getDescription();
        if (desc.length() >= 1024) {
            String substrDesc = desc.substring(0, 1022);
            a.setDescription(substrDesc);
        } else {
            a.setDescription(desc);
        }
        
        String info = pc.getMessage();
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
        a.setProcessName(pc.getScanUuid());
        a.setProcessCmdline("indef");
        a.setProcessPath("indef");
        a.setUrlHostname("indef");
        a.setUrlPath("indef");
        a.setContainerId("indef");
        a.setContainerName("indef");
        a.setCloudInstance("indef");
        a.setIncidentExt("indef");

        eventBean.createAlert(a);

        return a.getAlertUuid();
    }
}
