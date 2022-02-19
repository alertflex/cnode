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
import org.alertflex.entity.DockerScan;
import org.alertflex.entity.Project;
import org.alertflex.entity.Node;
import org.json.JSONArray;
import org.json.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class DockerBench {

    private static final Logger logger = LogManager.getLogger(DockerBench.class);
    
    private InfoMessageBean eventBean;
    Project project;
    Node node;

    public DockerBench(InfoMessageBean eb) {
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

            JSONObject obj = new JSONObject(report);
            JSONArray arr = obj.getJSONArray("tests");

            for (int i = 0; i < arr.length(); i++) {

                JSONObject test = arr.getJSONObject(i);
                String testDesc = test.getString("desc");
                JSONArray results = test.getJSONArray("results");

                for (int j = 0; j < results.length(); j++) {

                    DockerScan ds = new DockerScan();

                    ds.setRefId(r);
                    ds.setNodeId(n);
                    ds.setProbe(p);
                    ds.setTestDesc(testDesc);
                    ds.setReportAdded(date);
                    ds.setReportUpdated(date);

                    ds.setResultId(results.getJSONObject(j).getString("id"));
                    ds.setResultDesc(results.getJSONObject(j).getString("desc"));
                    ds.setResult(results.getJSONObject(j).getString("result"));

                    ds.setDetails("indef");
                    if (results.getJSONObject(j).has("details")) {
                        ds.setDetails(results.getJSONObject(j).getString("details"));
                    }

                    DockerScan dsExisting = eventBean.getDockerScanFacade().findScan(
                        ds.getRefId(),
                        ds.getNodeId(),
                        ds.getProbe(),
                        ds.getResultId());

                    if (dsExisting == null) {

                        eventBean.getDockerScanFacade().create(ds);
                        
                        createDockerScanAlert(ds);
                        
                    } else {

                        dsExisting.setReportUpdated(date);
                        eventBean.getDockerScanFacade().edit(dsExisting);
                    }
                }
            }

        } catch (Exception e) {
            logger.error("alertflex_ctrl_exception", e);
        }
    }

    public void createDockerScanAlert(DockerScan ds) {

        Alert a = new Alert();

        a.setNodeId(eventBean.getNode());
        a.setRefId(eventBean.getRefId());
        a.setAlertUuid(UUID.randomUUID().toString());

        AlertPriority ap = eventBean.getAlertPriorityFacade().findPriorityBySource(ds.getRefId(), "DockerBench");
        
        int sev = ap.getSeverityDefault();
        if (ds.getResult().equals(ap.getText1())) sev = ap.getValue1();
        if (ds.getResult().equals(ap.getText2())) sev = ap.getValue2();
        if (ds.getResult().equals(ap.getText3())) sev = ap.getValue3();
        if (ds.getResult().equals(ap.getText4())) sev = ap.getValue4();
        if (ds.getResult().equals(ap.getText5())) sev = ap.getValue5();
        if (sev < ap.getSeverityThreshold()) return;
        
        a.setAlertSeverity(sev);
        a.setEventSeverity(ds.getResult());
        a.setEventId(ds.getResultId());
        a.setCategories("docker-bench");
        a.setDescription(ds.getResultDesc());
        a.setAlertSource("DockerBench");
        a.setAlertType("MISC");

        a.setSensorId(ds.getProbe());
        a.setLocation(ds.getTestDesc());
        a.setAction("indef");
        a.setStatus("processed");
        a.setFilter("");
        a.setInfo(ds.getTestDesc());
        a.setTimeEvent("indef");
        Date date = new Date();
        a.setTimeCollr(ds.getReportAdded());
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
        a.setIncidentExt("indef");

        eventBean.createAlert(a);

    }
}
