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

    public void saveReport(String results) {

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

                String target = result.getString("Target");
                String targetType = result.getString("Type");

                JSONArray arrVuln = result.getJSONArray("Vulnerabilities");

                for (int j = 0; j < arrVuln.length(); j++) {

                    TrivyScan ts = new TrivyScan();

                    ts.setRefId(r);
                    ts.setNodeId(n);
                    ts.setSensor(p);
                    ts.setTarget(target);
                    ts.setTargetType(targetType);

                    String vulnerabilityID = arrVuln.getJSONObject(j).getString("VulnerabilityID");
                    ts.setVulnerabilityId(vulnerabilityID);

                    String pkgName = arrVuln.getJSONObject(j).getString("PkgName");
                    ts.setPkgName(pkgName);

                    String installedVersion = arrVuln.getJSONObject(j).getString("InstalledVersion");
                    ts.setInstalledVersion(installedVersion);

                    String fixedVersion = arrVuln.getJSONObject(j).getString("FixedVersion");
                    ts.setFixedVersion(fixedVersion);

                    String severitySource = arrVuln.getJSONObject(j).getString("SeveritySource");
                    ts.setSeveritySource(severitySource);

                    String title = arrVuln.getJSONObject(j).getString("Title");
                    ts.setTitle(title);

                    String description = arrVuln.getJSONObject(j).getString("Description");
                    ts.setDescription(description);

                    ts.setReportAdded(date);
                    ts.setReportUpdated(date);

                    TrivyScan tsExisting = eventBean.getTrivyScanFacade()
                            .findVulnerability(r, n, p, target, vulnerabilityID, pkgName);

                    if (tsExisting == null) {

                        AlertPriority ap = eventBean.getAlertPriorityFacade().findPriorityBySource(project.getRefId(), "Snyk");
                        int sev = 0;
                        String severityTrivy = arrVuln.getJSONObject(j).getString("Severity");

                        if (!severityTrivy.isEmpty() && ap != null) {

                            sev = ap.getSeverityDefault();

                            if (ap.getText1().equals(severityTrivy)) {
                                sev = ap.getValue1();
                            } else if (ap.getText2().equals(severityTrivy)) {
                                sev = ap.getValue2();
                            } else if (ap.getText3().equals(severityTrivy)) {
                                sev = ap.getValue3();
                            } else if (ap.getText4().equals(severityTrivy)) {
                                sev = ap.getValue4();
                            } else if (ap.getText5().equals(severityTrivy)) {
                                sev = ap.getValue5();
                            }

                            ts.setSeverity(sev);
                            eventBean.getTrivyScanFacade().create(ts);

                            if (sev >= ap.getSeverityThreshold()) {
                                createTrivyScanAlert(ts, severityTrivy);
                            }
                        }
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

    public void createTrivyScanAlert(TrivyScan ts, String sev) {

        Alert a = new Alert();

        a.setNodeId(eventBean.getNode());
        a.setRefId(eventBean.getRefId());
        a.setAlertUuid(UUID.randomUUID().toString());

        a.setAlertSeverity(ts.getSeverity());
        a.setEventId("1");
        a.setEventSeverity(sev);
        a.setCategories("trivy");
        a.setDescription(ts.getTitle());
        a.setAlertSource("Trivy");
        a.setAlertType("MISC");

        a.setSensorId(ts.getSensor());
        a.setLocation("indef");
        a.setAction("indef");
        a.setStatus("processed");
        a.setFilter("");
        a.setInfo(ts.getDescription());
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
