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
import org.alertflex.entity.Project;
import org.alertflex.entity.Node;
import org.alertflex.entity.SnykScan;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Snyk {

    private static final Logger logger = LoggerFactory.getLogger(Snyk.class);

    private InfoMessageBean eventBean;
    Project project;
    Node node;

    public Snyk(InfoMessageBean eb) {
        this.eventBean = eb;
        this.project = eventBean.getProject();

    }

    public void saveReport(String report, String target) {

        try {

            String r = eventBean.getRefId();
            String n = eventBean.getNode();
            String p = eventBean.getProbe();
            Date date = new Date();

            node = eventBean.getNodeFacade().findByNodeName(r, n);

            if (node == null || p == null || report.isEmpty()) {
                return;
            }

            JSONObject obj = new JSONObject(report);
            JSONArray arr = obj.getJSONArray("vulnerabilities");
            
            for (int i = 0; i < arr.length(); i++) {
                
                JSONObject vuln = arr.getJSONObject(i);
                
                SnykScan ss = new SnykScan();
                
                ss.setRefId(r);
                ss.setNodeId(n);
                ss.setProbe(p);
                
                ss.setProjectId(target);

                String id = vuln.getString("id");
                ss.setVulnId(id);
                
                String packageName = vuln.getString("name");
                ss.setPackageName(packageName);
                
                String packageManager = vuln.getString("packageManager");
                ss.setPackageManager(packageManager);
                
                String severity = vuln.getString("severity");
                ss.setSeverity(severity);
                
                String language = vuln.getString("language");
                ss.setLanguage(language);
                
                String title = vuln.getString("title");
                ss.setTitle(title);
                
                String desc = vuln.getString("description"); // 2048
                if (desc.length() >= 2048) {
                    String substrDesc = desc.substring(0, 2046);
                    ss.setDescription(substrDesc);
                } else {
                    ss.setDescription(desc);
                }
                
                String version = vuln.getString("version");
                ss.setVulnVersion(version);
                
                String publicationTime = vuln.getString("publicationTime");
                ss.setPublicationTime(publicationTime);
                
                JSONArray ref = vuln.getJSONArray("references");
                String references = "";
                for (int j = 0; j < ref.length(); j++) {
                    JSONObject url = ref.getJSONObject(j);
                    references = references + " " + url.getString("url"); //2048
                }
                
                if (references.length() >= 2048) {
                    String substrReferences = references.substring(0, 2046);
                    ss.setVulnRef(substrReferences);
                } else {
                    ss.setVulnRef(references);
                }
                
                JSONObject identifiers = vuln.getJSONObject("identifiers");

                String[] cveArr = toStringArray(identifiers.getJSONArray("CVE"));
                String CVE = String.join(", ", cveArr);
                ss.setVulnCve(CVE);
                
                String[] cweArr = toStringArray(identifiers.getJSONArray("CWE"));
                String CWE = String.join(", ", cweArr);
                ss.setVulnCwe(CWE);
                
                ss.setReportAdded(date);
                ss.setReportUpdated(date);
                
                SnykScan ssExisting = eventBean.getSnykScanFacade().findVulnerability(r, n, p, target, id, packageName);
                
                if (ssExisting == null) {

                    eventBean.getSnykScanFacade().create(ss);
                    
                    createSnykScanAlert(ss);
                    
                } else {

                    ssExisting.setReportUpdated(date);
                    eventBean.getSnykScanFacade().edit(ssExisting);
                }
                
            }
            
        } catch (Exception e) {
            logger.error("alertflex_ctrl_exception", e);
        }
    }
    
    public void createSnykScanAlert(SnykScan ss) {

        Alert a = new Alert();

        a.setNodeId(eventBean.getNode());
        a.setRefId(eventBean.getRefId());
        a.setAlertUuid(UUID.randomUUID().toString());
        
        AlertPriority ap = eventBean.getAlertPriorityFacade().findPriorityBySource(ss.getRefId(), "Snyk");
        
        int sev = ap.getSeverityDefault();
        if (ss.getSeverity().equals(ap.getText1())) sev = ap.getValue1();
        if (ss.getSeverity().equals(ap.getText2())) sev = ap.getValue2();
        if (ss.getSeverity().equals(ap.getText3())) sev = ap.getValue3();
        if (ss.getSeverity().equals(ap.getText4())) sev = ap.getValue4();
        if (ss.getSeverity().equals(ap.getText5())) sev = ap.getValue5();
        if (sev < ap.getSeverityThreshold()) return;

        a.setAlertSeverity(sev);
        a.setEventSeverity(ss.getSeverity());
        
        a.setEventId("1");
        a.setCategories("snyk");
        
        String desc = ss.getDescription();
        if (desc.length() >= 1024) {
            String substrDesc = desc.substring(0, 1022);
            a.setDescription(substrDesc);
        } else {
            a.setDescription(desc);
        }
        
        a.setAlertSource("Snyk");
        a.setAlertType("MISC");

        a.setSensorId(ss.getProbe());
        a.setLocation(ss.getPackageName());
        a.setAction("indef");
        a.setStatus("processed");
        a.setFilter("");
        
        String info = ss.getVulnRef();
        if (info.length() >= 1024) {
            String substrInfo = info.substring(0, 1022);
            a.setInfo(substrInfo);
        } else {
            a.setInfo(info);
        }
        a.setInfo(ss.getRefId());
        
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
    
    private String[] toStringArray(JSONArray jsonArray) throws JSONException {

        String[] array = new String[jsonArray.length()];
        for (int i = 0; i < array.length; i++) {
            array[i] = jsonArray.getString(i);
        }
        return array;
    }

    
}
