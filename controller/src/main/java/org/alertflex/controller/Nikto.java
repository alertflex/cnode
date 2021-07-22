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
import org.alertflex.entity.NiktoScan;
import org.alertflex.entity.Project;
import org.alertflex.entity.Node;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Nikto {

    private static final Logger logger = LoggerFactory.getLogger(Nikto.class);

    private InfoMessageBean eventBean;
    Project project;
    Node node;

    public Nikto(InfoMessageBean eb) {
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
            
            JSONObject resultsJson = new JSONObject(results);
            
            String host = resultsJson.getString("host");
            String ip = resultsJson.getString("ip");
            String port = resultsJson.getString("port");
            String banner = resultsJson.getString("banner");
            
            JSONArray arrResults = resultsJson.getJSONArray("vulnerabilities");

            for (int i = 0; i < arrResults.length(); i++) {

                JSONObject result = arrResults.getJSONObject(i);
                
                String id = result.getString("id");
                String OSVDB = result.getString("OSVDB");
                String url = result.getString("url");
                String method = result.getString("method");
                String msg = result.getString("msg"); 
                
                NiktoScan nsExisting = eventBean.getNiktoScanFacade().findVulnerability(r, n, p, host, ip, port, id);

                if (nsExisting == null) {
                    
                    NiktoScan ns = new NiktoScan();
                    
                    ns.setNodeId(n);
                    ns.setRefId(r);
                    ns.setProbe(p);
                    ns.setHost(host);
                    ns.setIp(ip);
                    ns.setPort(port);
                    ns.setBanner(banner);
                    ns.setVulnId(id);
                    ns.setVulnOSVDB(OSVDB);
                    ns.setVulnMethod(method);
                    ns.setVulnUrl(url);
                    ns.setVulnMsg(msg);
                    ns.setReportAdded(date);
                    ns.setReportUpdated(date);
                    

                    eventBean.getNiktoScanFacade().create(ns);
                    
                    createNiktoScanAlert(ns);
                
                } else {

                    nsExisting.setReportUpdated(date);
                    eventBean.getNiktoScanFacade().edit(nsExisting);
                }
                
            }
        } catch (Exception e) {
            logger.error("alertflex_ctrl_exception", e);
        }
    }

    public void createNiktoScanAlert(NiktoScan ns) {

        Alert a = new Alert();

        a.setNodeId(eventBean.getNode());
        a.setRefId(eventBean.getRefId());
        a.setAlertUuid(UUID.randomUUID().toString());

        AlertPriority ap = eventBean.getAlertPriorityFacade().findPriorityBySource(ns.getRefId(), "Nikto");
        
        int sev = ap.getSeverityDefault();
        
        if (sev < ap.getSeverityThreshold()) return;
        
        a.setEventSeverity(Integer.toString(sev));
        a.setAlertSeverity(sev);
        a.setEventId("1");
        
        a.setCategories("nikto");
        a.setAlertSource("Nikto");
        a.setAlertType("MISC");

        a.setSensorId(ns.getProbe());
        a.setLocation(ns.getVulnUrl());
        a.setAction("indef");
        a.setStatus("processed");
        a.setFilter("");
        
        String desc = ns.getVulnMsg();
        if (desc.length() >= 1024) {
            String substrDesc = desc.substring(0, 1022);
            a.setDescription(substrDesc);
        } else {
            a.setDescription(desc);
        }
        
        a.setInfo(ns.getVulnOSVDB());
                        
        a.setTimeEvent("indef");
        Date date = new Date();
        a.setTimeCollr(date);
        a.setTimeCntrl(date);
        a.setAgentName("indef");
        a.setUserName("indef");

        a.setSrcIp("0.0.0.0");
        a.setDstIp(ns.getIp());
        a.setDstPort(Integer.parseInt(ns.getPort()));
        a.setSrcPort(0);
        a.setSrcHostname("indef");
        a.setDstHostname(ns.getHost());
        a.setRegValue("indef");
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
        a.setCloudInstance("indef");

        eventBean.createAlert(a);

    }
}
