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
import org.alertflex.entity.PostureK8svuln;
import org.alertflex.entity.PostureTask;
import org.alertflex.supp.ProjectRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class K8sVuln {

    private static final Logger logger = LogManager.getLogger(K8sVuln.class);

    private InfoMessageBean eventBean;
    Project project;
    Node node;

    public K8sVuln(InfoMessageBean eb) {
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
            pt.setPostureType("K8sVuln");
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
            
            JSONArray arrVuln = body.getJSONArray("Vulnerabilities");
            
            for (int k = 0; k < arrVuln.length(); k++) {
            
                String namespace = arrVuln.getJSONObject(k).getString("Namespace");
            
                JSONArray arrResults = arrVuln.getJSONObject(k).optJSONArray("Results");

                for (int i = 0; i < arrResults.length(); i++) {

                    JSONObject result = arrResults.getJSONObject(i);

                    String resultTarget = result.getString("Target");
                    String resultClass = result.getString("Class");
                    String resultType = result.getString("Type");
                    
                    if (result.has("Vulnerabilities")) {

                        JSONArray arrVulnerabilities = result.getJSONArray("Vulnerabilities");

                        for (int j = 0; j < arrVulnerabilities.length(); j++) {

                            PostureK8svuln pv = new PostureK8svuln();

                            pv.setScanUuid(uuid);
                            pv.setRefId(r);
                            pv.setNode(n);
                            pv.setProbe(p);
                            pv.setClusterName(clusterName);
                            pv.setNamespace(namespace);
                            pv.setTarget(resultTarget);
                            pv.setTargetClass(resultClass);
                            pv.setTargetType(resultType);
                                        
                            String vulnId = arrVulnerabilities.getJSONObject(j).getString("VulnerabilityID");
                            pv.setVulnerabilityId(vulnId);

                            String pkgName = arrVulnerabilities.getJSONObject(j).getString("PkgName");
                            pv.setPkgName(pkgName);
                    
                            String pkgVersion = arrVulnerabilities.getJSONObject(j).getString("InstalledVersion");
                            pv.setPkgVersion(pkgVersion);
                    
                    
                            JSONArray arrCweid = arrVulnerabilities.getJSONObject(j).getJSONArray("CweIDs");
                            String cweid = arrCweid.toString();
                            pv.setCweid(cweid);
                    
                            String title = "indef";
                            if (arrVulnerabilities.getJSONObject(j).has("Title")) {
                                title = arrVulnerabilities.getJSONObject(j).getString("Title");
                            }
                            pv.setTitle(title);
                    
                            String desc = "indef";
                            if (arrVulnerabilities.getJSONObject(j).has("Description")) {
                                desc = arrVulnerabilities.getJSONObject(j).getString("Description");
                            }
                            if (desc.length() >= 2048) {
                                String substrDesc = desc.substring(0, 2046);
                                pv.setDescription(substrDesc);
                            } else {
                                pv.setDescription(desc);
                            }
                    
                            if (arrVulnerabilities.getJSONObject(j).has("PrimaryURL")) {
                        
                                String reference = arrVulnerabilities.getJSONObject(j).getString("PrimaryURL");
                                if (reference.length() >= 2048) {
                                    String substrReference = reference.substring(0, 2046);
                                    pv.setReference(substrReference);
                                } else {
                                    pv.setReference(reference);
                                }
                            } else {
                                pv.setReference("indef");
                            }
                    
                            String sev =  arrVulnerabilities.getJSONObject(j).getString("Severity");
                            pv.setSeverity(sev);
                    
                            pv.setReportAdded(date);
                            pv.setReportUpdated(date);
                            pv.setStatus("processed");
                            
                            String alertUuid = "";
                    
                            PostureK8svuln pvExisting = eventBean.getPostureK8svulnFacade()
                                .findVulnerability(r, n, p, pv.getClusterName(), pv.getNamespace(), pv.getTarget(), pv.getVulnerabilityId(), pv.getPkgName(), pv.getPkgVersion());

                            if (pvExisting == null) {
                                alertUuid = createPostureK8svulnAlert(pv);
                                if (alertUuid != null) pv.setAlertUuid(alertUuid);
                                else pv.setAlertUuid("indef");
                                eventBean.getPostureK8svulnFacade().create(pv);
                
                            } else {
                                switch (alertType) {
                    
                                    case 1: // all-existing
                        
                                        alertUuid = createPostureK8svulnAlert(pvExisting);
                                        if (alertUuid != null) pvExisting.setAlertUuid(alertUuid);
                                        else pvExisting.setAlertUuid("indef");
                            
                                        pvExisting.setReportUpdated(date);
                                        eventBean.getPostureK8svulnFacade().edit(pvExisting);
                        
                                        break;
                        
                                    case 2: // non confirmed 
                        
                                        if (!pvExisting.getStatus().equals("confirmed")) {
                                            alertUuid = createPostureK8svulnAlert(pvExisting);
                                            if (alertUuid != null) pvExisting.setAlertUuid(alertUuid);
                                            else pvExisting.setAlertUuid("indef");
                                        }
                            
                                        pvExisting.setReportUpdated(date);
                                        eventBean.getPostureK8svulnFacade().edit(pvExisting);
                            
                                        break;
                        
                                    case 3: // new
                        
                                        pvExisting.setReportUpdated(date);
                                        eventBean.getPostureK8svulnFacade().edit(pvExisting);
                            
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

    public String createPostureK8svulnAlert(PostureK8svuln pv) {

        Alert a = new Alert();

        a.setNode(eventBean.getNode());
        a.setRefId(eventBean.getRefId());
        a.setAlertUuid(UUID.randomUUID().toString());

        AlertPriority ap = eventBean.getAlertPriorityFacade().findPriorityBySource(pv.getRefId(), "Trivy");
        
        int sev = ap.getSeverityDefault();
        if (pv.getSeverity().equals(ap.getText1())) sev = ap.getValue1();
        if (pv.getSeverity().equals(ap.getText2())) sev = ap.getValue2();
        if (pv.getSeverity().equals(ap.getText3())) sev = ap.getValue3();
        if (pv.getSeverity().equals(ap.getText4())) sev = ap.getValue4();
        if (pv.getSeverity().equals(ap.getText5())) sev = ap.getValue5();
        if (sev < ap.getSeverityThreshold()) return null;
        
        a.setEventSeverity(pv.getSeverity());
        a.setAlertSeverity(sev);
        a.setEventId("1");
        
        a.setCategories(pv.getVulnerabilityId());
        a.setAlertSource("K8sVuln");
        a.setAlertType("MISC");

        a.setProbe(pv.getProbe());
        a.setLocation(pv.getClusterName());
        a.setAction("indef");
        a.setStatus("processed");
        a.setFilter("");
        
        String desc = pv.getTitle();
        if (desc.length() >= 1024) {
            String substrDesc = desc.substring(0, 1022);
            a.setDescription(substrDesc);
        } else {
            a.setDescription(desc);
        }
        
        String info = pv.getDescription();
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
        a.setProcessName(pv.getScanUuid());
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
