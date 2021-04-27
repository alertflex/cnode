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

package org.alertflex.controller;

import java.util.Date;
import java.util.UUID;
import org.alertflex.entity.Alert;
import org.alertflex.entity.AlertPriority;
import org.alertflex.entity.TrivyScan;
import org.alertflex.entity.Project;
import org.alertflex.entity.Node;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Trivy {

    private static final Logger logger = LoggerFactory.getLogger(Trivy.class);

    private InfoMessageBean eventBean;
    Project project;
    Node node;

    public Trivy(InfoMessageBean eb) {
        this.eventBean = eb;
        this.project = eventBean.getProject();

    }

    public void saveReport(String results, String target) {

        try {

            String r = eventBean.getRefId();
            String n = eventBean.getNode();
            String p = eventBean.getProbe();
            Date date = new Date();

            node = eventBean.getNodeFacade().findByNodeName(r, n);

            if (node == null || p == null || results.isEmpty()) {
                return;
            }

            JSONArray arrResults = new JSONArray(results);

            for (int i = 0; i < arrResults.length(); i++) {

                JSONObject result = arrResults.getJSONObject(i);

                String imageName = result.getString("Target");
                String imageType = result.getString("Type");

                JSONArray arrVuln = result.getJSONArray("Vulnerabilities");

                for (int j = 0; j < arrVuln.length(); j++) {

                    TrivyScan ts = new TrivyScan();

                    ts.setRefId(r);
                    ts.setNodeId(n);
                    ts.setProbe(p);
                    ts.setImageName(imageName);
                    ts.setImageType(imageType);

                    String vuln = arrVuln.getJSONObject(j).getString("VulnerabilityID");
                    ts.setVulnerability(vuln);

                    String pkgName = arrVuln.getJSONObject(j).getString("PkgName");
                    ts.setPkgName(pkgName);

                    String pkgVersion = arrVuln.getJSONObject(j).getString("InstalledVersion");
                    ts.setPkgVersion(pkgVersion);

                    String severity = arrVuln.getJSONObject(j).getString("Severity");
                    ts.setSeverity(severity);
                    
                    String title = "indef";
                    if (arrVuln.getJSONObject(j).has("Title")) {
                        title = arrVuln.getJSONObject(j).getString("Title");
                    }
                    ts.setTitle(title);
                    
                    String desc = "indef";
                    if (arrVuln.getJSONObject(j).has("Description")) {
                        desc = arrVuln.getJSONObject(j).getString("Description");
                    }
                    if (desc.length() >= 2048) {
                        String substrDesc = desc.substring(0, 2046);
                        ts.setDescription(substrDesc);
                    } else {
                        ts.setDescription(desc);
                    }
                    
                    String ref = "indef";
                    if (arrVuln.getJSONObject(j).has("PrimaryURL")) {
                        ref = arrVuln.getJSONObject(j).getString("PrimaryURL");
                    }
                    if (ref.length() >= 1024) {
                        String substrRef = ref.substring(0, 1022);
                        ts.setVulnRef(substrRef);
                    } else {
                        ts.setVulnRef(ref);
                    }
                    
                    JSONObject layer = arrVuln.getJSONObject(j).getJSONObject("Layer");
                    
                    String imageId = layer.getString("DiffID");
                    
                    ts.setImageId(imageId);

                    ts.setReportAdded(date);
                    ts.setReportUpdated(date);
                    
                    TrivyScan tsExisting = eventBean.getTrivyScanFacade().findVulnerability(r, n, p, imageName, vuln, pkgName);

                    if (tsExisting == null) {

                        eventBean.getTrivyScanFacade().create(ts);
                        
                        createTrivyScanAlert(ts);
                    
                    } else {

                        tsExisting.setReportUpdated(date);
                        eventBean.getTrivyScanFacade().edit(tsExisting);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("alertflex_ctrl_exception", e);
        }
    }

    public void createTrivyScanAlert(TrivyScan ts) {

        Alert a = new Alert();

        a.setNodeId(eventBean.getNode());
        a.setRefId(eventBean.getRefId());
        a.setAlertUuid(UUID.randomUUID().toString());

        AlertPriority ap = eventBean.getAlertPriorityFacade().findPriorityBySource(ts.getRefId(), "Trivy");
        
        int sev = ap.getSeverityDefault();
        if (ts.getSeverity().equals(ap.getText1())) sev = ap.getValue1();
        if (ts.getSeverity().equals(ap.getText2())) sev = ap.getValue2();
        if (ts.getSeverity().equals(ap.getText3())) sev = ap.getValue3();
        if (ts.getSeverity().equals(ap.getText4())) sev = ap.getValue4();
        if (ts.getSeverity().equals(ap.getText5())) sev = ap.getValue5();
        if (sev < ap.getSeverityThreshold()) return;
        
        a.setEventSeverity(ts.getSeverity());
        a.setAlertSeverity(sev);
        a.setEventId("1");
        
        a.setCategories("trivy");
        a.setDescription(ts.getTitle());
        a.setAlertSource("Trivy");
        a.setAlertType("MISC");

        a.setSensorId(ts.getProbe());
        a.setLocation(ts.getImageName());
        a.setAction("indef");
        a.setStatus("processed");
        a.setFilter("");
        
        String desc = ts.getTitle();
        if (desc.length() >= 1024) {
            String substrDesc = desc.substring(0, 1022);
            a.setDescription(substrDesc);
        } else {
            a.setDescription(desc);
        }
        
        String info = ts.getVulnRef();
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
        a.setFileName("indef");
        a.setFilePath("indef");
        a.setHashMd5("indef");
        a.setHashSha1("indef");
        a.setHashSha256("indef");
        a.setProcessId(0);
        a.setProcessName("indef");
        a.setProcessCmdline("indef");
        a.setProcessPath("indef");
        a.setUrlHostname("indef");
        a.setUrlPath("indef");
        a.setContainerId("indef");
        a.setContainerName("indef");
        a.setJsonEvent("indef");

        eventBean.createAlert(a);

    }
}
