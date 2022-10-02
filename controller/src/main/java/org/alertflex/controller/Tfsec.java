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
import org.alertflex.entity.TfsecScan;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Tfsec {

    private static final Logger logger = LogManager.getLogger(Tfsec.class);

    private InfoMessageBean eventBean;
    Project project;
    Node node;

    public Tfsec(InfoMessageBean eb) {
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
            JSONArray arr = obj.getJSONArray("results");
            
            for (int i = 0; i < arr.length(); i++) {
                
                JSONObject res = arr.getJSONObject(i);
                
                TfsecScan ts = new TfsecScan();
                
                ts.setRefId(r);
                ts.setNode(n);
                ts.setProbe(p);
                
                ts.setProjectId(target);

                String ruleId = res.getString("rule_id");
                ts.setRuleId(ruleId);
                
                String longId = res.getString("long_id");
                ts.setLongId(longId);
                
                String ruleDescription = res.getString("rule_description");
                ts.setRuleDescription(ruleDescription);
                
                String ruleProvider = res.getString("rule_provider");
                ts.setRuleProvider(ruleProvider);
                
                String ruleService = res.getString("rule_service");
                ts.setRuleService(ruleService);
                
                String impact = res.getString("impact");
                ts.setImpact(impact);
                
                String resolution = res.getString("resolution");
                ts.setResolution(resolution);
                
                String links = res.getJSONArray("links").toString();
                if (links.length() >= 2048) {
                    String substrLinks = links.substring(0, 2046);
                    ts.setLinks(substrLinks);
                } else {
                    ts.setLinks(links);
                }
                
                String description = res.getString("description");
                ts.setDescription(description);
                
                String severity = res.getString("severity");
                ts.setSeverity(severity);
                
                String resource = res.getString("resource");
                ts.setResource(resource);
                
                int status = res.getInt("status");
                ts.setStatus(status);
                
                JSONObject location = res.getJSONObject("location");

                String filename = location.getString("filename");
                ts.setFilename(filename);
                
                int startLine = location.getInt("start_line");
                ts.setStartLine(startLine);
                
                int endLine = location.getInt("end_line");
                ts.setEndLine(endLine);
                
                ts.setReportAdded(date);
                ts.setReportUpdated(date);
                
                TfsecScan tsExisting = eventBean.getTfsecScanFacade().findVulnerability(r, n, p, target, ruleId, filename);               
                if (tsExisting == null) {

                    eventBean.getTfsecScanFacade().create(ts);
                    
                    createTfsecScanAlert(ts);
                    
                } else {

                    tsExisting.setReportUpdated(date);
                    eventBean.getTfsecScanFacade().edit(tsExisting);
                }
                
            }
            
        } catch (Exception e) {
            logger.error("alertflex_ctrl_exception", e);
        }
    }
    
    public void createTfsecScanAlert(TfsecScan ts) {

        Alert a = new Alert();

        a.setNodeId(eventBean.getNode());
        a.setRefId(eventBean.getRefId());
        a.setAlertUuid(UUID.randomUUID().toString());
        
        AlertPriority ap = eventBean.getAlertPriorityFacade().findPriorityBySource(ts.getRefId(), "Tfsec");
        
        int sev = 0;
        
        if (ap != null) {
            sev = ap.getSeverityDefault();
            if (ts.getSeverity().equals(ap.getText1())) sev = ap.getValue1();
            if (ts.getSeverity().equals(ap.getText2())) sev = ap.getValue2();
            if (ts.getSeverity().equals(ap.getText3())) sev = ap.getValue3();
            if (ts.getSeverity().equals(ap.getText4())) sev = ap.getValue4();
            if (ts.getSeverity().equals(ap.getText5())) sev = ap.getValue5();
            if (sev < ap.getSeverityThreshold()) return;
        }

        a.setAlertSeverity(sev);
        a.setEventSeverity(ts.getSeverity());
        
        a.setEventId("1");
        a.setCategories("tfsec");
        
        String desc = ts.getDescription();
        if (desc.length() >= 1024) {
            String substrDesc = desc.substring(0, 1022);
            a.setDescription(substrDesc);
        } else {
            a.setDescription(desc);
        }
        
        a.setAlertSource("Tfsec");
        a.setAlertType("MISC");

        a.setSensorId(ts.getProbe());
        a.setLocation(ts.getFilename());
        a.setAction("indef");
        a.setStatus("processed");
        a.setFilter("");
        
        String info = ts.getLinks();
        if (info.length() >= 1024) {
            String substrInfo = info.substring(0, 1022);
            a.setInfo(substrInfo);
        } else {
            a.setInfo(info);
        }
        a.setInfo(ts.getRefId());
        
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
        a.setRegValue("indef");
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
        a.setCloudInstance("indef");
        a.setIncidentExt("indef");

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

