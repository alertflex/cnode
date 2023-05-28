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
import org.alertflex.entity.PostureK8sconfig;
import org.alertflex.entity.PostureTask;
import org.alertflex.supp.ProjectRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class K8sConfig {

    private static final Logger logger = LogManager.getLogger(K8sConfig.class);

    private InfoMessageBean eventBean;
    Project project;
    Node node;

    public K8sConfig(InfoMessageBean eb) {
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
            pt.setPostureType("K8sConfig");
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
            
            String clusterName = body.getString("ClusterName");
            
            JSONArray arrMisconf = body.getJSONArray("Misconfigurations");
            
            for (int k = 0; k < arrMisconf.length(); k++) {
            
                String namespace = arrMisconf.getJSONObject(k).getString("Namespace");
            
                JSONArray arrResults = arrMisconf.getJSONObject(k).optJSONArray("Results");

                for (int i = 0; i < arrResults.length(); i++) {

                    JSONObject result = arrResults.getJSONObject(i);

                    String resultTarget = result.getString("Target");
                    String resultClass = result.getString("Class");
                    String resultType = result.getString("Type");
                    
                    if (result.has("Misconfigurations")) {

                        JSONArray arrMisconfigurations = result.getJSONArray("Misconfigurations");

                        for (int j = 0; j < arrMisconfigurations.length(); j++) {

                            PostureK8sconfig pk = new PostureK8sconfig();

                            pk.setScanUuid(uuid);
                            pk.setRefId(r);
                            pk.setNode(n);
                            pk.setProbe(p);
                            pk.setClusterName(clusterName);
                            pk.setNamespace(namespace);
                            pk.setTarget(resultTarget);
                            pk.setTargetClass(resultClass);
                            pk.setTargetType(resultType);
                                        
                            String misconfigType = arrMisconfigurations.getJSONObject(j).getString("Type");
                            pk.setMisconfigType(misconfigType);

                            String misconfigAVDID = arrMisconfigurations.getJSONObject(j).getString("AVDID");
                            pk.setMisconfigAvdid(misconfigAVDID);
                    
                            String title = "indef";
                            if (arrMisconfigurations.getJSONObject(j).has("Title")) {
                                title = arrMisconfigurations.getJSONObject(j).getString("Title");
                            }
                            pk.setTitle(title);
                    
                            String desc = "indef";
                            if (arrMisconfigurations.getJSONObject(j).has("Description")) {
                                desc = arrMisconfigurations.getJSONObject(j).getString("Description");
                            }
                            if (desc.length() >= 2048) {
                                String substrDesc = desc.substring(0, 2046);
                                pk.setDescription(substrDesc);
                            } else {
                                pk.setDescription(desc);
                            }
                    
                            if (arrMisconfigurations.getJSONObject(j).has("Resolution")) {
                        
                                String solution = arrMisconfigurations.getJSONObject(j).getString("Resolution");
                                if (solution.length() >= 2048) {
                                    String substrSolution = solution.substring(0, 2046);
                                    pk.setRemediation(substrSolution);
                                } else {
                                    pk.setRemediation(solution);
                                }
                            } else {
                                pk.setRemediation("indef");
                            }
                    
                            if (arrMisconfigurations.getJSONObject(j).has("PrimaryURL")) {
                        
                                String reference = arrMisconfigurations.getJSONObject(j).getString("PrimaryURL");
                                if (reference.length() >= 2048) {
                                    String substrReference = reference.substring(0, 2046);
                                    pk.setReference(substrReference);
                                } else {
                                    pk.setReference(reference);
                                }
                            } else {
                                pk.setReference("indef");
                            }
                    
                            String sev =  arrMisconfigurations.getJSONObject(j).getString("Severity");
                            pk.setSeverity(sev);
                    
                            pk.setReportAdded(date);
                            pk.setReportUpdated(date);
                            pk.setStatus("processed");
                            
                            String alertUuid = "";
                    
                            PostureK8sconfig pkExisting = eventBean.getPostureK8sconfigFacade()
                                .findMisconfig(r, n, p, pk.getClusterName(), pk.getNamespace(), pk.getTarget(), pk.getMisconfigAvdid());

                            if (pkExisting == null) {
                                alertUuid = createPostureK8sconfigAlert(pk);
                                if (alertUuid != null) pk.setAlertUuid(alertUuid);
                                else pk.setAlertUuid("indef");
                                eventBean.getPostureK8sconfigFacade().create(pk);
                
                            } else {
                                switch (alertType) {
                    
                                    case 1: // all-existing
                        
                                        alertUuid = createPostureK8sconfigAlert(pkExisting);
                                        if (alertUuid != null) pkExisting.setAlertUuid(alertUuid);
                                        else pkExisting.setAlertUuid("indef");
                            
                                        pkExisting.setReportUpdated(date);
                                        eventBean.getPostureK8sconfigFacade().edit(pkExisting);
                        
                                        break;
                        
                                    case 2: // non confirmed 
                        
                                        if (!pkExisting.getStatus().equals("confirmed")) {
                                            alertUuid = createPostureK8sconfigAlert(pkExisting);
                                            if (alertUuid != null) pkExisting.setAlertUuid(alertUuid);
                                            else pkExisting.setAlertUuid("indef");
                                        }
                            
                                        pkExisting.setReportUpdated(date);
                                        eventBean.getPostureK8sconfigFacade().edit(pkExisting);
                            
                                        break;
                        
                                    case 3: // new
                        
                                        pkExisting.setReportUpdated(date);
                                        eventBean.getPostureK8sconfigFacade().edit(pkExisting);
                            
                                        break;
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("alertflex_ctrl_exception", e);
        }
    }

    public String createPostureK8sconfigAlert(PostureK8sconfig pk) {

        Alert a = new Alert();

        a.setNode(eventBean.getNode());
        a.setRefId(eventBean.getRefId());
        a.setAlertUuid(UUID.randomUUID().toString());

        AlertPriority ap = eventBean.getAlertPriorityFacade().findPriorityBySource(pk.getRefId(), "Trivy");
        
        int sev = ap.getSeverityDefault();
        if (pk.getSeverity().equals(ap.getText1())) sev = ap.getValue1();
        if (pk.getSeverity().equals(ap.getText2())) sev = ap.getValue2();
        if (pk.getSeverity().equals(ap.getText3())) sev = ap.getValue3();
        if (pk.getSeverity().equals(ap.getText4())) sev = ap.getValue4();
        if (pk.getSeverity().equals(ap.getText5())) sev = ap.getValue5();
        if (sev < ap.getSeverityThreshold()) return null;
        
        a.setEventSeverity(pk.getSeverity());
        a.setAlertSeverity(sev);
        a.setEventId("1");
        
        a.setCategories(pk.getMisconfigAvdid());
        a.setAlertSource("K8sConfig");
        a.setAlertType("MISC");

        a.setProbe(pk.getProbe());
        a.setLocation(pk.getClusterName());
        a.setAction("indef");
        a.setStatus("processed");
        a.setFilter("");
        
        String desc = pk.getTitle();
        if (desc.length() >= 1024) {
            String substrDesc = desc.substring(0, 1022);
            a.setDescription(substrDesc);
        } else {
            a.setDescription(desc);
        }
        
        String info = pk.getDescription();
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
        a.setProcessName(pk.getScanUuid());
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
