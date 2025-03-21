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
import org.alertflex.entity.PostureDockervuln;
import org.alertflex.entity.PostureTask;
import org.alertflex.supp.ProjectRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DockerVuln {

    private static final Logger logger = LogManager.getLogger(DockerVuln.class);

    private InfoMessageBean eventBean;
    Project project;
    Node node;

    public DockerVuln(InfoMessageBean eb) {
        this.eventBean = eb;
        this.project = eventBean.getProject();

    }

    public void saveReport(String results, String target, String uuid, String alertCorr, int alertType) {
        
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
            pt.setPostureType("DockerVuln");
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
                String resultType = result.getString("Type");
                
                if (result.has("Vulnerabilities")) {

                    JSONArray arrVulnerabilities = result.getJSONArray("Vulnerabilities");

                    for (int j = 0; j < arrVulnerabilities.length(); j++) {

                        PostureDockervuln pd = new PostureDockervuln();

                        pd.setScanUuid(uuid);
                        pd.setRefId(r);
                        pd.setNode(n);
                        pd.setProbe(p);
                        pd.setArtifactName(artifactName);
                        pd.setArtifactType(artifactType);
                        pd.setTarget(resultTarget);
                        pd.setTargetClass(resultClass);
                        pd.setTargetType(resultType);
                                        
                        String vulnId = arrVulnerabilities.getJSONObject(j).getString("VulnerabilityID");
                        pd.setVulnerabilityId(vulnId);

                        String pkgName = arrVulnerabilities.getJSONObject(j).getString("PkgName");
                        pd.setPkgName(pkgName);
                    
                        String pkgVersion = arrVulnerabilities.getJSONObject(j).getString("InstalledVersion");
                        pd.setPkgVersion(pkgVersion);
                    
                    
                        JSONArray arrCweid = arrVulnerabilities.getJSONObject(j).getJSONArray("CweIDs");
                        String cweid = arrCweid.toString();
                        pd.setCweid(cweid);
                    
                        String title = "indef";
                        if (arrVulnerabilities.getJSONObject(j).has("Title")) {
                            title = arrVulnerabilities.getJSONObject(j).getString("Title");
                        }
                        pd.setTitle(title);
                    
                        String desc = "indef";
                        if (arrVulnerabilities.getJSONObject(j).has("Description")) {
                            desc = arrVulnerabilities.getJSONObject(j).getString("Description");
                        }
                        if (desc.length() >= 2048) {
                            String substrDesc = desc.substring(0, 2046);
                            pd.setDescription(substrDesc);
                        } else {
                            pd.setDescription(desc);
                        }
                    
                        if (arrVulnerabilities.getJSONObject(j).has("PrimaryURL")) {
                        
                            String reference = arrVulnerabilities.getJSONObject(j).getString("PrimaryURL");
                            if (reference.length() >= 2048) {
                                String substrReference = reference.substring(0, 2046);
                                pd.setReference(substrReference);
                            } else {
                                pd.setReference(reference);
                            }
                        } else {
                            pd.setReference("indef");
                        }
                    
                        String sev =  arrVulnerabilities.getJSONObject(j).getString("Severity");
                        pd.setSeverity(sev);
                    
                        pd.setReportAdded(date);
                        pd.setReportUpdated(date);
                        pd.setStatus("processed");
                        
                        String alertUuid = "";
                    
                        PostureDockervuln pdExisting = eventBean.getPostureDockervulnFacade()
                            .findVulnerability(r, n, p, pd.getArtifactName(), pd.getTarget(), pd.getVulnerabilityId(),pd.getPkgName(), pd.getPkgVersion());

                        if (pdExisting == null) {
                            alertUuid = createPostureDockervulnAlert(pd);
                            if (alertUuid != null) pd.setAlertUuid(alertUuid);
                            else pd.setAlertUuid("indef");
                            eventBean.getPostureDockervulnFacade().create(pd);
                
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
                        
                                    alertUuid = createPostureDockervulnAlert(pd);
                                    if (alertUuid != null) pdExisting.setAlertUuid(alertUuid);
                                                                
                                    pdExisting.setReportUpdated(date);
                                    eventBean.getPostureDockervulnFacade().edit(pdExisting);
                        
                                    break;
                        
                                case 2: // non confirmed 
                        
                                    if (!pdExisting.getStatus().equals("confirmed")) {
                                        alertUuid = createPostureDockervulnAlert(pd);
                                        if (alertUuid != null) pdExisting.setAlertUuid(alertUuid);
                                    }
                            
                                    pdExisting.setReportUpdated(date);
                                    eventBean.getPostureDockervulnFacade().edit(pdExisting);
                            
                                    break;
                        
                                case 3: // new
                        
                                    pdExisting.setReportUpdated(date);
                                    eventBean.getPostureDockervulnFacade().edit(pdExisting);
                            
                                    break;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("alertflex_ctrl_exception", e);
        }
    }

    public String createPostureDockervulnAlert(PostureDockervuln pd) {

        Alert a = new Alert();

        a.setNode(eventBean.getNode());
        a.setRefId(eventBean.getRefId());
        a.setAlertUuid(UUID.randomUUID().toString());

        AlertPriority ap = eventBean.getAlertPriorityFacade().findPriorityBySource(pd.getRefId(), "Trivy");
        
        int sev = ap.getSeverityDefault();
        if (pd.getSeverity().equals(ap.getText1())) sev = ap.getValue1();
        if (pd.getSeverity().equals(ap.getText2())) sev = ap.getValue2();
        if (pd.getSeverity().equals(ap.getText3())) sev = ap.getValue3();
        if (pd.getSeverity().equals(ap.getText4())) sev = ap.getValue4();
        if (pd.getSeverity().equals(ap.getText5())) sev = ap.getValue5();
        if (sev < ap.getSeverityThreshold()) return null;
        
        a.setEventSeverity(pd.getSeverity());
        a.setAlertSeverity(sev);
        a.setEventId("1");
        
        a.setCategories(pd.getVulnerabilityId());
        a.setAlertSource("DockerVuln");
        a.setAlertType("MISC");

        a.setProbe(pd.getProbe());
        a.setLocation(pd.getArtifactName());
        a.setAction("indef");
        a.setStatus("processed");
        a.setFilter("");
        
        String desc = pd.getTitle();
        if (desc.length() >= 1024) {
            String substrDesc = desc.substring(0, 1022);
            a.setDescription(substrDesc);
        } else {
            a.setDescription(desc);
        }
        
        String info = pd.getDescription();
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
        a.setProcessName(pd.getScanUuid());
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
