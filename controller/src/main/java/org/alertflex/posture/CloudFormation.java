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
import org.alertflex.entity.PostureCloudformation;
import org.alertflex.entity.PostureTask;
import org.alertflex.supp.ProjectRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CloudFormation {

    private static final Logger logger = LogManager.getLogger(CloudFormation.class);

    private InfoMessageBean eventBean;
    Project project;
    Node node;

    public CloudFormation(InfoMessageBean eb) {
        this.eventBean = eb;
        this.project = eventBean.getProject();

    }

    public void saveReport(String results, String target, String uuid) {
        
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
            pt.setPostureType("CloudFormation");
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
                
                if (result.has("Misconfigurations")) {

                    JSONArray arrMisconfigurations = result.getJSONArray("Misconfigurations");

                    for (int j = 0; j < arrMisconfigurations.length(); j++) {

                        PostureCloudformation pc = new PostureCloudformation();

                        pc.setScanUuid(uuid);
                        pc.setRefId(r);
                        pc.setNode(n);
                        pc.setProbe(p);
                        pc.setArtifactName(artifactName);
                        pc.setArtifactType(artifactType);
                        pc.setTarget(resultTarget);
                        pc.setTargetClass(resultClass);
                        pc.setTargetType(resultType);
                                        
                        String misconfigType = arrMisconfigurations.getJSONObject(j).getString("Type");
                        pc.setMisconfigType(misconfigType);

                        String misconfigAVDID = arrMisconfigurations.getJSONObject(j).getString("AVDID");
                        pc.setMisconfigAvdid(misconfigAVDID);
                    
                        String title = "indef";
                        if (arrMisconfigurations.getJSONObject(j).has("Title")) {
                            title = arrMisconfigurations.getJSONObject(j).getString("Title");
                        }
                        pc.setTitle(title);
                    
                        String desc = "indef";
                        if (arrMisconfigurations.getJSONObject(j).has("Description")) {
                            desc = arrMisconfigurations.getJSONObject(j).getString("Description");
                        }
                        if (desc.length() >= 2048) {
                            String substrDesc = desc.substring(0, 2046);
                            pc.setDescription(substrDesc);
                        } else {
                            pc.setDescription(desc);
                        }
                    
                        if (arrMisconfigurations.getJSONObject(j).has("Resolution")) {
                        
                            String solution = arrMisconfigurations.getJSONObject(j).getString("Resolution");
                            if (solution.length() >= 2048) {
                                String substrSolution = solution.substring(0, 2046);
                                pc.setRemediation(substrSolution);
                            } else {
                                pc.setRemediation(solution);
                            }
                        } else {
                            pc.setRemediation("indef");
                        }
                    
                        if (arrMisconfigurations.getJSONObject(j).has("PrimaryURL")) {
                        
                            String reference = arrMisconfigurations.getJSONObject(j).getString("PrimaryURL");
                            if (reference.length() >= 2048) {
                                String substrReference = reference.substring(0, 2046);
                                pc.setReference(substrReference);
                            } else {
                                pc.setReference(reference);
                            }
                        } else {
                            pc.setReference("indef");
                        }
                    
                        String sev =  arrMisconfigurations.getJSONObject(j).getString("Severity");
                        pc.setSeverity(sev);
                    
                        pc.setReportAdded(date);
                        pc.setReportUpdated(date);
                    
                        PostureCloudformation pcExisting = eventBean.getPostureCloudformationFacade()
                            .findMisconfig(r, n, p, pc.getArtifactName(), pc.getTarget(), pc.getMisconfigAvdid());

                        if (pcExisting == null) {

                            eventBean.getPostureCloudformationFacade().create(pc);
                        
                            createPostureCloudformationAlert(pc);
                    
                        } else {

                            pcExisting.setReportUpdated(date);
                            eventBean.getPostureCloudformationFacade().edit(pcExisting);
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("alertflex_ctrl_exception", e);
        }
    }

    public void createPostureCloudformationAlert(PostureCloudformation pc) {

        Alert a = new Alert();

        a.setNode(eventBean.getNode());
        a.setRefId(eventBean.getRefId());
        a.setAlertUuid(UUID.randomUUID().toString());

        AlertPriority ap = eventBean.getAlertPriorityFacade().findPriorityBySource(pc.getRefId(), "Trivy");
        
        int sev = ap.getSeverityDefault();
        if (pc.getSeverity().equals(ap.getText1())) sev = ap.getValue1();
        if (pc.getSeverity().equals(ap.getText2())) sev = ap.getValue2();
        if (pc.getSeverity().equals(ap.getText3())) sev = ap.getValue3();
        if (pc.getSeverity().equals(ap.getText4())) sev = ap.getValue4();
        if (pc.getSeverity().equals(ap.getText5())) sev = ap.getValue5();
        if (sev < ap.getSeverityThreshold()) return;
        
        a.setEventSeverity(pc.getSeverity());
        a.setAlertSeverity(sev);
        a.setEventId("1");
        
        a.setCategories(pc.getMisconfigAvdid());
        a.setAlertSource("CloudFormation");
        a.setAlertType("MISC");

        a.setProbe(pc.getProbe());
        a.setLocation(pc.getArtifactName());
        a.setAction("indef");
        a.setStatus("processed");
        a.setFilter("");
        
        String desc = pc.getTitle();
        if (desc.length() >= 1024) {
            String substrDesc = desc.substring(0, 1022);
            a.setDescription(substrDesc);
        } else {
            a.setDescription(desc);
        }
        
        String info = pc.getDescription();
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

    }
}
