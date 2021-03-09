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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DockerBench {

    private static final Logger logger = LoggerFactory.getLogger(DockerBench.class);

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

        a.setAlertSeverity(0);
        a.setEventSeverity(ds.getResult());
        a.setEventId("1");
        a.setCategories("docker_bench");
        a.setDescription(ds.getResultDesc());
        a.setAlertSource("DockerBench");
        a.setAlertType("MISC");

        a.setSensorId(ds.getProbe());
        a.setLocation("indef");
        a.setAction("indef");
        a.setStatus("processed");
        a.setFilter("");
        a.setInfo(ds.getTestDesc());
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
