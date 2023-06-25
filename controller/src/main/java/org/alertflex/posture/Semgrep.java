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
import org.alertflex.entity.PostureSemgrep;
import org.alertflex.supp.ProjectRepository;
import org.json.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;

public class Semgrep {

    private static final Logger logger = LogManager.getLogger(Semgrep.class);

    private InfoMessageBean eventBean;
    Project project;
    Node node;

    public Semgrep(InfoMessageBean eb) {
        this.eventBean = eb;
        this.project = eventBean.getProject();

    }

    public void saveReport(String results, String target, String uuid, String alertCorr, int alertType) {
        
        String r = eventBean.getRefId();
        String n = eventBean.getNode();
        String p = eventBean.getHost() + ".semgrep";
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
            pt.setPostureType("Semgrep");
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
            
            JSONObject res = new JSONObject(results);
            
            JSONArray findings = res.getJSONArray("results");
            
            for (int i = 0; i < findings.length(); i++) {
                
                PostureSemgrep ps = new PostureSemgrep();
                
                int startLine = 0;
                int endLine = 0;
                String message = "indef";
                String lines = "indef";
                String severity = "indef";
                String category = "indef";
                String cwe = "indef";
                String reference = "indef";
                String ruleId = "indef";
                String ruleOrigin = "indef";
                String path = "indef";
                
                if (findings.getJSONObject(i).has("path")) path = findings.getJSONObject(i).getString("path");
                
                if (findings.getJSONObject(i).has("start")) {
                    JSONObject start = findings.getJSONObject(i).getJSONObject("start");
                    if (start.has("line")) startLine = start.getInt("line");
                } 
                
                if (findings.getJSONObject(i).has("end")) {
                    JSONObject end = findings.getJSONObject(i).getJSONObject("end");
                    if (end.has("line")) endLine = end.getInt("line");
                } 
                
                if (findings.getJSONObject(i).has("extra")) {
                    JSONObject extra = findings.getJSONObject(i).getJSONObject("extra");
                    
                    if(extra.has("message")) message = extra.getString("message");
                    if(extra.has("lines")) lines = extra.getString("lines");
                    if(extra.has("severity")) severity = extra.getString("severity");
                    
                    if (extra.has("metadata")) {
                        JSONObject metadata = extra.getJSONObject("metadata");
                        
                        if(metadata.has("category")) category = metadata.getString("category");
                        
                        if(metadata.has("cwe")) {
                            JSONArray arrCwe = metadata.getJSONArray("cwe");
                            cwe = arrCwe.toString();
                        }
                        
                        if(metadata.has("references")) {
                            JSONArray arrReference = metadata.getJSONArray("references");
                            reference = arrReference.toString();
                        }
                        
                        if(metadata.has("semgrep.dev")) {
                            JSONObject semgrepDev = metadata.getJSONObject("semgrep.dev");
                            
                            if(semgrepDev.has("rule")) {
                                
                                JSONObject rule = semgrepDev.getJSONObject("rule");   
                                
                                if(rule.has("rule_id")) ruleId = rule.getString("rule_id");
                                if(rule.has("origin")) ruleOrigin = rule.getString("origin");
                            }
                        }
                    }
                } 
                
                Date dt = new Date();
                ps.setCategories(category);
                ps.setComponent(path);
                ps.setMessage(message);
                ps.setRuleId(ruleId);
                ps.setRuleOrigin(ruleOrigin);
                ps.setReference(reference);
                ps.setCwe(cwe);
                ps.setVulnLine(lines);
                ps.setStartLine(startLine);
                ps.setEndLine(endLine);
                ps.setTarget(target);
                ps.setSeverity(severity);
                ps.setNode(n);
                ps.setProbe(p);
                ps.setScanUuid(uuid);
                ps.setRefId(project.getRefId());
                ps.setReportAdded(dt);
                ps.setReportUpdated(dt);
                ps.setStatus("processed");
                
                String alertUuid = "";
                
                PostureSemgrep psExisting =  eventBean.getPostureSemgrepFacade()
                    .findVulnerability(r, n, target, path, message, startLine, endLine);
                
                if (psExisting == null) {
                    alertUuid = createPostureSemgrepAlert(ps);
                    if (alertUuid != null) ps.setAlertUuid(alertUuid);
                    else ps.setAlertUuid("indef");
                    eventBean.getPostureSemgrepFacade().create(ps);
                
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
                        
                            alertUuid = createPostureSemgrepAlert(ps);
                            if (alertUuid != null) psExisting.setAlertUuid(alertUuid);
                                                        
                            psExisting.setReportUpdated(date);
                            eventBean.getPostureSemgrepFacade().edit(psExisting);
                        
                            break;
                        
                        case 2: // non confirmed 
                        
                            if (!psExisting.getStatus().equals("confirmed")) {
                                alertUuid = createPostureSemgrepAlert(ps);
                                if (alertUuid != null) psExisting.setAlertUuid(alertUuid);
                            }
                            
                            psExisting.setReportUpdated(date);
                            eventBean.getPostureSemgrepFacade().edit(psExisting);
                            
                            break;
                        
                        case 3: // new
                        
                            psExisting.setReportUpdated(date);
                            eventBean.getPostureSemgrepFacade().edit(psExisting);
                            
                            break;
                    }
                }
            }

        } catch (Exception e) {
            logger.error("alertflex_ctrl_exception", e);
        }
    }

    public String createPostureSemgrepAlert(PostureSemgrep ps) {

        Alert a = new Alert();

        a.setNode(eventBean.getNode());
        a.setRefId(eventBean.getRefId());
        a.setAlertUuid(UUID.randomUUID().toString());

        AlertPriority ap = eventBean.getAlertPriorityFacade().findPriorityBySource(ps.getRefId(), "Semgrep");
        
        int sev = ap.getSeverityDefault();
        if (ps.getSeverity().equals(ap.getText1())) sev = ap.getValue1();
        if (ps.getSeverity().equals(ap.getText2())) sev = ap.getValue2();
        if (ps.getSeverity().equals(ap.getText3())) sev = ap.getValue3();
        if (sev < ap.getSeverityThreshold()) return null;
        
        a.setEventSeverity(ps.getSeverity());
        a.setAlertSeverity(sev);
        a.setEventId(ps.getCwe());
        
        a.setCategories(ps.getCategories());
        a.setAlertSource("Semgrep");
        a.setAlertType("MISC");

        a.setProbe(ps.getProbe());
        a.setLocation(ps.getTarget());
        a.setAction("indef");
        a.setStatus("processed");
        a.setFilter("");
        
        String desc = ps.getMessage();
        if (desc.length() >= 1024) {
            String substrDesc = desc.substring(0, 1022);
            a.setDescription(substrDesc);
        } else {
            a.setDescription(desc);
        }
        
        String info = ps.getVulnLine();
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
        a.setProcessName(ps.getScanUuid());
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
