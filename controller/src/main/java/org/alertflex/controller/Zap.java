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
import org.alertflex.entity.ZapScan;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Zap {

    private static final Logger logger = LoggerFactory.getLogger(Zap.class);

    private InfoMessageBean eventBean;
    Project project;
    Node node;

    public Zap(InfoMessageBean eb) {
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
            JSONArray arr = obj.getJSONArray("site");
            
            for (int i = 0; i < arr.length(); i++) {
                
                JSONArray arrayAlerts = arr.getJSONObject(i).getJSONArray("alerts");

                for (int j = 0; j < arrayAlerts.length(); j++) {

                    ZapScan zs = new ZapScan();

                    zs.setAlertRef(arrayAlerts.getJSONObject(j).getString("alertRef"));
                    zs.setAlertName(arrayAlerts.getJSONObject(j).getString("alert"));

                    String riskcode = arrayAlerts.getJSONObject(j).getString("riskcode");
                    zs.setRiskcode(Integer.parseInt(riskcode));

                    String confidence = arrayAlerts.getJSONObject(j).getString("confidence");
                    zs.setConfidence(Integer.parseInt(confidence));

                    zs.setRiskdesc(arrayAlerts.getJSONObject(j).getString("riskdesc"));
                    
                    if (arrayAlerts.getJSONObject(i).has("desc")) {
                        
                        String desc = arrayAlerts.getJSONObject(j).getString("desc");
                        if (desc.length() >= 2048) {
                            String substrDesc = desc.substring(0, 2046);
                            zs.setDescription(substrDesc);
                        } else {
                            zs.setDescription(desc);
                        }
                    } else {
                        zs.setDescription("indef");
                    }
                    
                    if (arrayAlerts.getJSONObject(j).has("solution")) {
                        
                        String solution = arrayAlerts.getJSONObject(j).getString("solution");
                        if (solution.length() >= 2048) {
                            String substrSolution = solution.substring(0, 2046);
                            zs.setSolution(substrSolution);
                        } else {
                            zs.setSolution(solution);
                        }
                    } else {
                        zs.setSolution("indef");
                    }
                    
                    if (arrayAlerts.getJSONObject(j).has("reference")) {
                        
                        String reference = arrayAlerts.getJSONObject(j).getString("reference");
                        if (reference.length() >= 2048) {
                            String substrReference = reference.substring(0, 2046);
                            zs.setReference(substrReference);
                        } else {
                            zs.setReference(reference);
                        }
                    } else {
                        zs.setReference("indef");
                    }
                    
                    if (arrayAlerts.getJSONObject(j).has("otherinfo")) {
                        
                        String otherinfo = arrayAlerts.getJSONObject(j).getString("otherinfo");
                        if (otherinfo.length() >= 2048) {
                            String substrOtherinfo = otherinfo.substring(0, 2046);
                            zs.setOtherinfo(substrOtherinfo);
                        } else {
                            zs.setOtherinfo(otherinfo);
                        }
                    } else {
                        zs.setOtherinfo("indef");
                    }
                    
                    JSONArray instances = arrayAlerts.getJSONObject(j).getJSONArray("instances");
                    
                    if (instances != null) {
                        
                        String uriMethods = "";
                        
                        for (int k = 0; k < instances.length(); k++) {
                            
                            String methods = instances.getJSONObject(k).getString("method");
                            String uri = instances.getJSONObject(k).getString("uri");
                            uriMethods =  uriMethods + methods + ":" + uri + " ";
                        }
                        
                        if (uriMethods.isEmpty()) zs.setUriMethods("indef");
                        else {
                            if (uriMethods.length() >= 2048) {
                                String substrUriMethods = uriMethods.substring(0, 2046);
                                zs.setUriMethods(substrUriMethods);
                            } else {
                                zs.setUriMethods(uriMethods);
                            }
                        }
                        
                    } else {
                            
                        zs.setUriMethods("indef");
                    }

                    if (arrayAlerts.getJSONObject(j).has("cweid")) {
                        String cweid = arrayAlerts.getJSONObject(j).getString("cweid");
                        zs.setCweid(Integer.parseInt(cweid));
                    } else {
                        zs.setCweid(0);
                    }

                    if (arrayAlerts.getJSONObject(j).has("wascid")) {
                        String wascid = arrayAlerts.getJSONObject(j).getString("wascid");
                        zs.setWascid(Integer.parseInt(wascid));
                    } else {
                        zs.setWascid(0);
                    }

                    zs.setNodeId(n);
                    zs.setProbe(p);
                    zs.setRefId(r);
                    zs.setTarget(target);
                    zs.setReportAdded(date);
                    zs.setReportUpdated(date);
                    
                    ZapScan zsExisting = eventBean.getZapScanFacade().findScan(r,n,p,target, zs.getAlertRef());
                        
                    if (zsExisting == null) {
                        
                        eventBean.getZapScanFacade().create(zs);
                        
                        createZapScanAlert(zs);
                        
                    } else {
                        zsExisting.setReportUpdated(date);
                        eventBean.getZapScanFacade().edit(zsExisting);
                    }
                }
            }
            
        } catch (Exception e) {
            logger.error("alertflex_ctrl_exception", e);
        }
    }

    public void createZapScanAlert(ZapScan zs) {

        Alert a = new Alert();

        a.setNodeId(eventBean.getNode());
        a.setRefId(eventBean.getRefId());
        a.setAlertUuid(UUID.randomUUID().toString());

        AlertPriority ap = eventBean.getAlertPriorityFacade().findPriorityBySource(zs.getRefId(), "ZAP");
        
        if (zs.getRiskcode() < ap.getSeverityThreshold()) return;
        
        a.setEventSeverity(zs.getRiskdesc());
        a.setAlertSeverity(zs.getRiskcode());
        a.setEventId("1");
        
        a.setCategories("zap");
        a.setAlertSource("ZAP");
        a.setAlertType("MISC");

        a.setSensorId(zs.getProbe());
        a.setLocation(zs.getTarget());
        a.setAction("indef");
        a.setStatus("processed");
        a.setFilter("");
        
        String desc = zs.getDescription();
        if (desc.length() >= 1024) {
            String substrDesc = desc.substring(0, 1022);
            a.setDescription(substrDesc);
        } else {
            a.setDescription(desc);
        }
        
        String info = zs.getAlertRef();
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
