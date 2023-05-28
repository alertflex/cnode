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
import org.alertflex.entity.PostureAppsecret;
import org.alertflex.entity.Project;
import org.alertflex.entity.Node;
import org.alertflex.entity.PostureTask;
import org.alertflex.supp.ProjectRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AppSecret {

    private static final Logger logger = LogManager.getLogger(AppSecret.class);

    private InfoMessageBean eventBean;
    Project project;
    Node node;

    public AppSecret(InfoMessageBean eb) {
        this.eventBean = eb;
        this.project = eventBean.getProject();

    }

    public void saveReport(String results, String target, String uuid, int alertType) {
        
        String r = eventBean.getRefId();
        String n = eventBean.getNode();
        String p = eventBean.getHost() + ".trivy";
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
            pt.setPostureType("AppSecret");
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
            
            String artifactName = body.getString("ArtifactName");
            String artifactType = body.getString("ArtifactType");

            JSONArray arrResults = body.optJSONArray("Results");

            for (int i = 0; i < arrResults.length(); i++) {

                JSONObject result = arrResults.getJSONObject(i);

                String resultTarget = result.getString("Target");
                String resultClass = result.getString("Class");

                JSONArray arrSecrets = result.getJSONArray("Secrets");

                for (int j = 0; j < arrSecrets.length(); j++) {

                    PostureAppsecret pa = new PostureAppsecret();

                    pa.setScanUuid(uuid);
                    pa.setRefId(r);
                    pa.setNode(n);
                    pa.setProbe(p);
                    pa.setArtifactName(artifactName);
                    pa.setArtifactType(artifactType);
                    pa.setTarget(resultTarget);
                    pa.setTargetClass(resultClass);
                    
                    String ruleId = arrSecrets.getJSONObject(j).getString("RuleID");
                    pa.setRuleId(ruleId);

                    String cat = arrSecrets.getJSONObject(j).getString("Category");
                    pa.setCategory(cat);
                    
                    String title = "indef";
                    if (arrSecrets.getJSONObject(j).has("Title")) {
                        title = arrSecrets.getJSONObject(j).getString("Title");
                    }
                    pa.setTitle(title);
                    
                    String sev =  arrSecrets.getJSONObject(j).getString("Severity");
                    pa.setSeverity(sev);
                    
                    int startLine = arrSecrets.getJSONObject(j).getInt("StartLine");
                    pa.setStartLine(startLine);
                    
                    int endLine = arrSecrets.getJSONObject(j).getInt("EndLine");
                    pa.setEndLine(endLine);
                    
                    pa.setReportAdded(date);
                    pa.setReportUpdated(date);
                    pa.setStatus("processed");
                    
                    String alertUuid = "";
                    
                    PostureAppsecret paExisting = eventBean.getPostureAppsecretFacade()
                        .findSecret(r, n, p, pa.getArtifactName(), pa.getTarget(), pa.getRuleId(), pa.getStartLine(), pa.getEndLine());
                    
                    if (paExisting == null) {
                        alertUuid = createPostureAppsecretAlert(pa);
                        if (alertUuid != null) pa.setAlertUuid(alertUuid);
                        else pa.setAlertUuid("indef");
                        eventBean.getPostureAppsecretFacade().create(pa);
                    
                    } else {
                        switch (alertType) {
                        
                            case 1: // all-existing
                            
                                alertUuid = createPostureAppsecretAlert(paExisting);
                                if (alertUuid != null) paExisting.setAlertUuid(alertUuid);
                                else paExisting.setAlertUuid("indef");
                                
                                paExisting.setReportUpdated(date);
                                eventBean.getPostureAppsecretFacade().edit(paExisting);
                            
                                break;
                            
                            case 2: // non confirmed 
                            
                                if (!paExisting.getStatus().equals("confirmed")) {
                                    alertUuid = createPostureAppsecretAlert(paExisting);
                                    if (alertUuid != null) paExisting.setAlertUuid(alertUuid);
                                    else paExisting.setAlertUuid("indef");
                                }
                                
                                paExisting.setReportUpdated(date);
                                eventBean.getPostureAppsecretFacade().edit(paExisting);
                                
                                break;
                            
                            case 3: // new
                            
                                paExisting.setReportUpdated(date);
                                eventBean.getPostureAppsecretFacade().edit(paExisting);
                                
                                break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("alertflex_ctrl_exception", e);
        }
    }

    public String createPostureAppsecretAlert(PostureAppsecret pa) {

        Alert a = new Alert();

        a.setNode(eventBean.getNode());
        a.setRefId(eventBean.getRefId());
        a.setAlertUuid(UUID.randomUUID().toString());

        AlertPriority ap = eventBean.getAlertPriorityFacade().findPriorityBySource(pa.getRefId(), "Trivy");
        
        int sev = ap.getSeverityDefault();
        if (pa.getSeverity().equals(ap.getText1())) sev = ap.getValue1();
        if (pa.getSeverity().equals(ap.getText2())) sev = ap.getValue2();
        if (pa.getSeverity().equals(ap.getText3())) sev = ap.getValue3();
        if (pa.getSeverity().equals(ap.getText4())) sev = ap.getValue4();
        if (pa.getSeverity().equals(ap.getText5())) sev = ap.getValue5();
        if (sev < ap.getSeverityThreshold()) return null;
        
        a.setEventSeverity(pa.getSeverity());
        a.setAlertSeverity(sev);
        a.setEventId("1");
        
        a.setCategories(pa.getRuleId());
        a.setAlertSource("AppSecret");
        a.setAlertType("MISC");

        a.setProbe(pa.getProbe());
        a.setLocation(pa.getArtifactName());
        a.setAction("indef");
        a.setStatus("processed");
        a.setFilter("");
        
        String desc = pa.getTitle();
        if (desc.length() >= 1024) {
            String substrDesc = desc.substring(0, 1022);
            a.setDescription(substrDesc);
        } else {
            a.setDescription(desc);
        }
        
        String info = "StartLine: " + Integer.toString(pa.getStartLine()) + ", EndLine: " + Integer.toString(pa.getEndLine());
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
        a.setProcessName(pa.getScanUuid());
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
