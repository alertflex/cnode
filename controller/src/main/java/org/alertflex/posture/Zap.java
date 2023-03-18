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
import org.alertflex.entity.PostureZap;
import org.alertflex.supp.ProjectRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Zap {

    private static final Logger logger = LogManager.getLogger(AppSecret.class);

    private InfoMessageBean eventBean;
    Project project;
    Node node;

    public Zap(InfoMessageBean eb) {
        this.eventBean = eb;
        this.project = eventBean.getProject();

    }

    public void saveReport(String results, String target, String uuid) {
        
        String r = eventBean.getRefId();
        String n = eventBean.getNode();
        String p = eventBean.getHost() + ".zap";
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
            pt.setPostureType("ZAP");
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

            JSONObject obj = new JSONObject(results);
            JSONArray arr = obj.getJSONArray("site");
            
            for (int i = 0; i < arr.length(); i++) {
                
                JSONArray arrayAlerts = arr.getJSONObject(i).getJSONArray("alerts");

                for (int j = 0; j < arrayAlerts.length(); j++) {

                    PostureZap pz = new PostureZap();

                    pz.setAlertRef(arrayAlerts.getJSONObject(j).getString("alertRef"));
                    pz.setAlertName(arrayAlerts.getJSONObject(j).getString("alert"));

                    String[] severityArray = arrayAlerts.getJSONObject(j).getString("riskdesc").split(" ");
                    pz.setSeverity(severityArray[0]);
                    
                    if (arrayAlerts.getJSONObject(i).has("desc")) {
                        
                        String desc = arrayAlerts.getJSONObject(j).getString("desc");
                        if (desc.length() >= 2048) {
                            String substrDesc = desc.substring(0, 2046);
                            pz.setDescription(substrDesc);
                        } else {
                            pz.setDescription(desc);
                        }
                    } else {
                        pz.setDescription("indef");
                    }
                    
                    if (arrayAlerts.getJSONObject(j).has("solution")) {
                        
                        String solution = arrayAlerts.getJSONObject(j).getString("solution");
                        if (solution.length() >= 2048) {
                            String substrSolution = solution.substring(0, 2046);
                            pz.setRemediation(substrSolution);
                        } else {
                            pz.setRemediation(solution);
                        }
                    } else {
                        pz.setRemediation("indef");
                    }
                    
                    if (arrayAlerts.getJSONObject(j).has("reference")) {
                        
                        String reference = arrayAlerts.getJSONObject(j).getString("reference");
                        if (reference.length() >= 2048) {
                            String substrReference = reference.substring(0, 2046);
                            pz.setReference(substrReference);
                        } else {
                            pz.setReference(reference);
                        }
                    } else {
                        pz.setReference("indef");
                    }
                    
                    if (arrayAlerts.getJSONObject(j).has("cweid")) {
                        String cweid = arrayAlerts.getJSONObject(j).getString("cweid");
                        pz.setCweid(Integer.parseInt(cweid));
                    } else {
                        pz.setCweid(0);
                    }

                    pz.setNode(n);
                    pz.setProbe(p);
                    pz.setRefId(r);
                    pz.setScanUuid(uuid);
                    pz.setTarget(target);
                    pz.setReportAdded(date);
                    pz.setReportUpdated(date);
                    
                    PostureZap pzExisting = eventBean.getPostureZapFacade().findVulnerability(r, n, p, target, pz.getAlertRef());
                        
                    if (pzExisting == null) {
                        
                        eventBean.getPostureZapFacade().create(pz);
                        
                        createZapScanAlert(pz);
                        
                    } else {
                        pzExisting.setReportUpdated(date);
                        eventBean.getPostureZapFacade().edit(pzExisting);
                    }
                }
            }
            
        } catch (Exception e) {
            logger.error("alertflex_ctrl_exception", e);
        }
    }

    public void createZapScanAlert(PostureZap pz) {

        Alert a = new Alert();

        a.setNode(eventBean.getNode());
        a.setRefId(eventBean.getRefId());
        a.setAlertUuid(UUID.randomUUID().toString());

        AlertPriority ap = eventBean.getAlertPriorityFacade().findPriorityBySource(pz.getRefId(), "ZAP");
        
        int sev = ap.getSeverityDefault();
        if (pz.getSeverity().equals(ap.getText1())) sev = ap.getValue1();
        if (pz.getSeverity().equals(ap.getText2())) sev = ap.getValue2();
        if (pz.getSeverity().equals(ap.getText3())) sev = ap.getValue3();
        if (pz.getSeverity().equals(ap.getText4())) sev = ap.getValue4();
        if (pz.getSeverity().equals(ap.getText5())) sev = ap.getValue5();
        if (sev < ap.getSeverityThreshold()) return;
        
        a.setEventSeverity(pz.getSeverity());
        a.setAlertSeverity(sev);
        a.setEventId(pz.getAlertRef());
        
        a.setCategories(pz.getAlertName());
        a.setAlertSource("ZAP");
        a.setAlertType("MISC");

        a.setProbe(pz.getProbe());
        a.setLocation(pz.getTarget());
        a.setAction("indef");
        a.setStatus("processed");
        a.setFilter("");
        
        String desc = pz.getDescription();
        if (desc.length() >= 1024) {
            String substrDesc = desc.substring(0, 1022);
            a.setDescription(substrDesc);
        } else {
            a.setDescription(desc);
        }
        
        String info = pz.getAlertRef();
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
        a.setProcessName(pz.getScanUuid());
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