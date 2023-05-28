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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import org.alertflex.controller.InfoMessageBean;
import org.alertflex.entity.Alert;
import org.alertflex.entity.AlertPriority;
import org.alertflex.entity.Project;
import org.alertflex.entity.Node;
import org.alertflex.entity.PostureTask;
import org.alertflex.entity.PostureSonarqube;
import org.alertflex.supp.ProjectRepository;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;

public class Sonarqube {

    private static final Logger logger = LogManager.getLogger(Sonarqube.class);

    private InfoMessageBean eventBean;
    Project project;
    Node node;

    public Sonarqube(InfoMessageBean eb) {
        this.eventBean = eb;
        this.project = eventBean.getProject();

    }

    public void saveReport(String results, String target, String uuid, int alertType) {
        
        String user = project.getSonarUser();
        String pwd = project.getSonarPass();
        String url = project.getSonarUrl() + "/api/issues/search?types=BUG";
        
        if (user.isEmpty() || pwd.isEmpty() || url.isEmpty()) return;
        
        String authString = user + ":" + pwd;
        byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
        String authStringEnc = new String(authEncBytes);
        
        try {
            
            HttpClient client = HttpClientBuilder.create().disableContentCompression().build();
            HttpGet get = new HttpGet(url);
            get.setHeader("accept", "application/json");
            get.setHeader("Authorization", "Basic " + authStringEnc);
            HttpResponse response = client.execute(get);

            if (response != null) {

                String json = EntityUtils.toString(response.getEntity(), "UTF-8");
                
                
                String r = eventBean.getRefId();
                String n = eventBean.getNode();
                String p = eventBean.getHost() + ".sonarqube";
                Date date = new Date();
                
                ProjectRepository pr = new ProjectRepository(project);
                String posturePath = pr.getCtrlPostureDir() + uuid + ".json";
                Path pp = Paths.get(posturePath);
                Files.write(pp, json.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
                
                PostureTask pt = new PostureTask();
                pt.setRefId(r);
                pt.setNode(n);
                pt.setProbe(p);
                pt.setPostureType("Sonarqube");
                pt.setTarget(target);
                pt.setTaskUuid(uuid);
                pt.setReportAdded(date);
                eventBean.getPostureTaskFacade().create(pt);
                

                JSONObject obj = new JSONObject(json);

                JSONArray issues = obj.getJSONArray("issues");

                for (int i = 0; i < issues.length(); i++) {
                    
                    PostureSonarqube ps = new PostureSonarqube();
                    
                    ps.setRefId(r);
                    ps.setNode(n);
                    ps.setProbe(p);
                    ps.setScanUuid(uuid);
                    ps.setReportCreated(date);
                    ps.setReportUpdated(date);
                    ps.setScanUuid(uuid);
                    ps.setStatus("processed");

                    String project = issues.getJSONObject(i).getString("project");
                    ps.setTarget(project);
                    
                    String component = issues.getJSONObject(i).getString("component");
                    ps.setComponent(component);
                    
                    String severity = issues.getJSONObject(i).getString("severity");
                    ps.setSeverity(severity);
                    
                    String message = issues.getJSONObject(i).getString("message");
                    ps.setMessage(message);
                    
                    String issueStatus = issues.getJSONObject(i).getString("status");
                    ps.setIssueStatus(issueStatus);
                   
                    String rule = issues.getJSONObject(i).getString("rule");
                    ps.setRuleId(rule);
                    
                    JSONArray tagsArray = issues.getJSONObject(i).getJSONArray("tags");
                    String categories = "";
                    for (int j = 0; j < tagsArray.length(); j++) {
                        categories = categories + tagsArray.getString(j);
                        if (j < tagsArray.length() - 1) categories = categories + ", ";
                    }
                    ps.setCategories(categories);

                    JSONObject textRange = issues.getJSONObject(i).getJSONObject("textRange");
                    int startLine = textRange.getInt("startLine");
                    ps.setStartLine(startLine);
                    int endLine = textRange.getInt("endLine");
                    ps.setEndLine(endLine);
                    
                    String creationDate = issues.getJSONObject(i).getString("creationDate");
                    Date issueCreationDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(creationDate);
                    ps.setIssueCreated(issueCreationDate);
                    
                    String updateDate = issues.getJSONObject(i).getString("updateDate");
                    Date issueUpdateDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(updateDate);
                    ps.setIssueUpdated(issueUpdateDate);
                    
                    String alertUuid = "";
                    
                    PostureSonarqube psExisting = eventBean.getPostureSonarqubeFacade()
                            .findVulnerability(r, n, p, target, component, rule);
                    
                    if (psExisting == null) {
                        alertUuid = createPostureSonarqubeAlert(ps);
                        if (alertUuid != null) ps.setAlertUuid(alertUuid);
                        else ps.setAlertUuid("indef");
                        eventBean.getPostureSonarqubeFacade().create(ps);
                
                    } else {
                        switch (alertType) {
                    
                            case 1: // all-existing
                        
                                alertUuid = createPostureSonarqubeAlert(psExisting);
                                if (alertUuid != null) psExisting.setAlertUuid(alertUuid);
                                else psExisting.setAlertUuid("indef");
                            
                                psExisting.setReportUpdated(date);
                                eventBean.getPostureSonarqubeFacade().edit(psExisting);
                        
                                break;
                        
                            case 2: // non confirmed 
                        
                                if (!psExisting.getStatus().equals("confirmed")) {
                                    alertUuid = createPostureSonarqubeAlert(psExisting);
                                    if (alertUuid != null) psExisting.setAlertUuid(alertUuid);
                                    else psExisting.setAlertUuid("indef");
                                }
                            
                                psExisting.setReportUpdated(date);
                                eventBean.getPostureSonarqubeFacade().edit(psExisting);
                            
                                break;
                        
                            case 3: // new
                        
                                psExisting.setReportUpdated(date);
                                eventBean.getPostureSonarqubeFacade().edit(psExisting);
                            
                                break;
                        }
                    }
                }
            } 
        } catch (IOException | ParseException e) {
           return;
        }
    }

    public String createPostureSonarqubeAlert(PostureSonarqube ps) {

        Alert a = new Alert();

        a.setNode(eventBean.getNode());
        a.setRefId(eventBean.getRefId());
        a.setAlertUuid(UUID.randomUUID().toString());

        AlertPriority ap = eventBean.getAlertPriorityFacade().findPriorityBySource(ps.getRefId(), "SonarQube");
        
        int sev = ap.getSeverityDefault();
        if (ps.getSeverity().equals(ap.getText1())) sev = ap.getValue1();
        if (ps.getSeverity().equals(ap.getText2())) sev = ap.getValue2();
        if (ps.getSeverity().equals(ap.getText3())) sev = ap.getValue3();
        if (ps.getSeverity().equals(ap.getText4())) sev = ap.getValue4();
        if (ps.getSeverity().equals(ap.getText5())) sev = ap.getValue5();
        if (sev < ap.getSeverityThreshold()) return null;
        
        a.setEventSeverity(ps.getSeverity());
        a.setAlertSeverity(sev);
        a.setEventId(ps.getRuleId());
        
        a.setCategories(ps.getCategories());
        a.setAlertSource("SonarQube");
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
        
        String info = ps.getComponent();
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
