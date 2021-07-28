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
import org.alertflex.entity.KubeScan;
import org.alertflex.entity.Project;
import org.alertflex.entity.Node;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KubeBench {

    private static final Logger logger = LoggerFactory.getLogger(KubeBench.class);

    private InfoMessageBean eventBean;
    Project project;
    Node node;

    public KubeBench(InfoMessageBean eb) {
        this.eventBean = eb;
        this.project = eventBean.getProject();

    }

    public void saveReport(String report) {

        try {

            String r = eventBean.getRefId();
            String n = eventBean.getNode();
            String p = eventBean.getProbe();
            Date date = new Date();

            node = eventBean.getNodeFacade().findByNodeName(r, n);

            if (node == null || p == null || report.isEmpty()) {
                return;
            }

            JSONArray arr = new JSONArray(report);
            
            for (int i = 0; i < arr.length(); i++) {
                
                JSONObject typeConf = arr.getJSONObject(i);
                String id = typeConf.getString("id");
                String name = typeConf.getString("text");
                String type = typeConf.getString("node_type");
                
                JSONArray arrTests = typeConf.getJSONArray("tests");
                
                for (int j = 0; j < arrTests.length(); j++) {
                    
                    JSONObject testSection = arrTests.getJSONObject(j);
                    String section = testSection.getString("section");
                    
                    String sectionDesc = testSection.getString("desc");
                    
                    JSONArray arrResults = testSection.getJSONArray("results");
                    
                    for (int k = 0; k < arrResults.length(); k++) {
                        
                        KubeScan ks = new KubeScan();
                        
                        JSONObject testResult = arrResults.getJSONObject(k);
                        
                        String testNumber = testResult.getString("test_number");
                        
                        String testDesc = testResult.getString("test_desc");
                        if (testDesc.length() >= 1024) {
                            String substrTestDesc = testDesc.substring(0, 1022);
                            ks.setResultDesc(substrTestDesc);
                        } else {
                            ks.setResultDesc(testDesc);
                        }
                        
                        String remediation = testResult.getString("remediation");
                        if (remediation.length() >= 1024) {
                            String substrRemediation = remediation.substring(0, 1022);
                            ks.setResultRemediation(substrRemediation);
                        } else {
                            ks.setResultRemediation(remediation);
                        }
                        
                        String status = testResult.getString("status");
                       
                        String actualValue = testResult.getString("actual_value");
                        if (actualValue.length() >= 512) {
                            String substrActualValue = actualValue.substring(0, 510);
                            ks.setActualValue(substrActualValue);
                        } else {
                            if (actualValue.isEmpty()) actualValue = "indef";
                            ks.setActualValue(actualValue);
                        }
                        
                        String expectedResult = testResult.getString("expected_result");
                        if (expectedResult.length() >= 512) {
                            String substrExpectedResult = expectedResult.substring(0, 510);
                            ks.setExpectedResult(substrExpectedResult);
                        } else {
                            if (expectedResult.isEmpty()) expectedResult = "indef";
                            ks.setExpectedResult(expectedResult);
                        }
                        
                        KubeScan ksExisting = eventBean.getKubeScanFacade().findScan(r,n,p,id,section,testNumber);
                        
                        if (ksExisting == null) {
                            
                            ks.setRefId(r);
                            ks.setNodeId(n);
                            ks.setProbe(p);
                            ks.setTestId(id);
                            ks.setTestType(type);
                            ks.setTestName(name);
                            ks.setSectionNumber(section);
                            ks.setSectionDesc(sectionDesc);
                            ks.setResultNumber(testNumber);
                            ks.setResultStatus(status);
                            
                            ks.setReportAdded(date);
                            ks.setReportUpdated(date);
                            
                            eventBean.getKubeScanFacade().create(ks);
                            
                            createKubeScanAlert(ks);
                            
                        } else {
                            ksExisting.setReportUpdated(date);
                            eventBean.getKubeScanFacade().edit(ksExisting);
                        }
                        
                    }
                    
                }
                
            }
            
        } catch (Exception e) {
            logger.error("alertflex_ctrl_exception", e);
        }
    }

    public void createKubeScanAlert(KubeScan ks) {

        Alert a = new Alert();

        a.setNodeId(eventBean.getNode());
        a.setRefId(eventBean.getRefId());
        a.setAlertUuid(UUID.randomUUID().toString());

        AlertPriority ap = eventBean.getAlertPriorityFacade().findPriorityBySource(ks.getRefId(), "KubeBench");
        
        int sev = ap.getSeverityDefault();
        if (ks.getResultStatus().equals(ap.getText1())) sev = ap.getValue1();
        if (ks.getResultStatus().equals(ap.getText2())) sev = ap.getValue2();
        if (ks.getResultStatus().equals(ap.getText3())) sev = ap.getValue3();
        if (ks.getResultStatus().equals(ap.getText4())) sev = ap.getValue4();
        if (ks.getResultStatus().equals(ap.getText5())) sev = ap.getValue5();
        if (sev < ap.getSeverityThreshold()) return;
        
        a.setAlertSeverity(sev);
        a.setEventSeverity(ks.getResultStatus());
        a.setEventId("1");
        a.setCategories("kube-bench");
        a.setDescription(ks.getResultDesc());
        a.setAlertSource("KubeBench");
        a.setAlertType("MISC");

        a.setSensorId(ks.getProbe());
        a.setLocation(ks.getTestName());
        a.setAction("indef");
        a.setStatus("processed");
        a.setFilter("");
        a.setInfo(ks.getResultRemediation());
        a.setTimeEvent("indef");
        Date date = new Date();
        a.setTimeCollr(ks.getReportAdded());
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
        a.setProcessName("indef");
        a.setProcessCmdline("indef");
        a.setProcessPath("indef");
        a.setUrlHostname("indef");
        a.setUrlPath("indef");
        a.setContainerId("indef");
        a.setContainerName("indef");
        a.setCloudInstance("indef");

        eventBean.createAlert(a);

    }
}
