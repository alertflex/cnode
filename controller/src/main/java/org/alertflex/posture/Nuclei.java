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

import java.io.BufferedReader;
import java.io.StringReader;
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
import org.alertflex.entity.PostureNuclei;
import org.alertflex.supp.ProjectRepository;
import org.json.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;

public class Nuclei {

    private static final Logger logger = LogManager.getLogger(Nuclei.class);

    private InfoMessageBean eventBean;
    Project project;
    Node node;

    public Nuclei(InfoMessageBean eb) {
        this.eventBean = eb;
        this.project = eventBean.getProject();

    }

    public void saveReport(String results, String target, String uuid, String alertCorr, int alertType) {
        
        String r = eventBean.getRefId();
        String n = eventBean.getNode();
        String p = eventBean.getHost() + ".nuclei";
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
            pt.setPostureType("Nuclei");
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
            
            StringReader reader = new StringReader(results);
            BufferedReader br = new BufferedReader(reader);
            
            String line;
            while((line=br.readLine())!=null)   {
                
                try {
        
                    PostureNuclei pn = new PostureNuclei();
                
                    pn.setRefId(r);
                    pn.setNode(n);
                    pn.setProbe(p);
                    pn.setScanUuid(uuid);
                    pn.setReportAdded(date);
                    pn.setReportUpdated(date);
                    pn.setTarget(target);
                
                    JSONObject obj = new JSONObject(line);
                
                    String alertId = obj.getString("template-id");
                    pn.setAlertId(alertId);
                
                    String alertRef = obj.getString("template-url");
                    pn.setAlertRef(alertRef);
                
                    String targetHost = obj.getString("host");
                    pn.setTargetHost(targetHost);
                
                    String targetIp = obj.getString("ip");
                    pn.setTargetIp(targetIp);
                
                    String targetType = obj.getString("type");
                    pn.setTargetType(targetType);
                
                    JSONObject info = obj.getJSONObject("info");
                
                    String alertName = info.getString("name");
                    pn.setAlertName(alertName);
                
                    String desc = "indef";
                    if (info.has("description")) desc = info.getString("description");
                    pn.setDescription(desc);
                
                    String severity = info.optString("severity");
                    pn.setSeverity(severity);
                    
                    pn.setStatus("processed");
                    
                    String alertUuid = "";
                
                    PostureNuclei pnExisting = eventBean.getPostureNucleiFacade().findVulnerability(r, n, p, target, alertName);
                
                    if (pnExisting == null) {
                        alertUuid = createPostureNucleiAlert(pn);
                        if (alertUuid != null) pn.setAlertUuid(alertUuid);
                        else pn.setAlertUuid("indef");
                    eventBean.getPostureNucleiFacade().create(pn);
                
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
                        
                            alertUuid = createPostureNucleiAlert(pn);
                            if (alertUuid != null) pnExisting.setAlertUuid(alertUuid);
                                                        
                            pnExisting.setReportUpdated(date);
                            eventBean.getPostureNucleiFacade().edit(pnExisting);
                        
                            break;
                        
                        case 2: // non confirmed 
                        
                            if (!pnExisting.getStatus().equals("confirmed")) {
                                alertUuid = createPostureNucleiAlert(pn);
                                if (alertUuid != null) pnExisting.setAlertUuid(alertUuid);
                            }
                            
                            pnExisting.setReportUpdated(date);
                            eventBean.getPostureNucleiFacade().edit(pnExisting);
                            
                            break;
                        
                        case 3: // new
                        
                            pnExisting.setReportUpdated(date);
                            eventBean.getPostureNucleiFacade().edit(pnExisting);
                            
                            break;
                    }
                }
                
                } catch (JSONException e) {
                    logger.error("alertflex_ctrl_exception", e);
                    logger.error(r);
                }
            }

        } catch (Exception e) {
            logger.error("alertflex_ctrl_exception", e);
        }
    }

    public String createPostureNucleiAlert(PostureNuclei pn) {

        Alert a = new Alert();

        a.setNode(eventBean.getNode());
        a.setRefId(eventBean.getRefId());
        a.setAlertUuid(UUID.randomUUID().toString());

        AlertPriority ap = eventBean.getAlertPriorityFacade().findPriorityBySource(pn.getRefId(), "Nuclei");
        
        int sev = ap.getSeverityDefault();
        if (pn.getSeverity().equals(ap.getText1())) sev = ap.getValue1();
        if (pn.getSeverity().equals(ap.getText2())) sev = ap.getValue2();
        if (pn.getSeverity().equals(ap.getText3())) sev = ap.getValue3();
        if (pn.getSeverity().equals(ap.getText4())) sev = ap.getValue4();
        if (pn.getSeverity().equals(ap.getText5())) sev = ap.getValue5();
        if (sev < ap.getSeverityThreshold()) return null;
        
        a.setEventSeverity(pn.getSeverity());
        a.setAlertSeverity(sev);
        a.setEventId(pn.getAlertId());
        
        a.setCategories("nuclei");
        a.setAlertSource("Nuclei");
        a.setAlertType("MISC");

        a.setProbe(pn.getProbe());
        a.setLocation(pn.getTargetHost());
        a.setAction("indef");
        a.setStatus("processed");
        a.setFilter("");
        
        String desc = pn.getDescription();
        if (desc.length() >= 1024) {
            String substrDesc = desc.substring(0, 1022);
            a.setDescription(substrDesc);
        } else {
            a.setDescription(desc);
        }
        
        String info = pn.getAlertName();
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
        a.setDstIp(pn.getTargetIp());
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
        a.setUrlHostname(pn.getAlertRef());
        a.setUrlPath("indef");
        a.setContainerId("indef");
        a.setContainerName("indef");
        a.setCloudInstance("indef");
        a.setIncidentExt("indef");

        eventBean.createAlert(a);
        
        return a.getAlertUuid();
    }
}
