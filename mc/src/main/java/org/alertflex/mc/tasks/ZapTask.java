/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alertflex.mc.tasks;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.alertflex.mc.db.Alert;
import org.alertflex.mc.db.AlertPriority;
import org.alertflex.mc.db.Project;
import org.alertflex.mc.db.ScanJob;
import org.alertflex.mc.db.ZapScan;
import org.alertflex.mc.supp.ZapScanJson;
import org.alertflex.mc.supp.ZapScanReport;
import org.zaproxy.clientapi.core.ApiResponse;
import org.zaproxy.clientapi.core.ApiResponseElement;
import org.zaproxy.clientapi.core.ClientApi;


/**
 *
 * @author root
 */

public class ZapTask {
    
    private static final Logger logger = LoggerFactory.getLogger(ZapTask.class);
    
    private ScanJobs scanJobs;
    
    private Project project = null;
    
    List<ZapScan> zsList = null;
        
    public ZapTask(ScanJobs sj) {
        
        this.scanJobs = sj;
        
    }
    
    public Boolean run(Project p, ScanJob sj) throws Exception {
    
        project = p;
        
        String target = sj.getValue1();
        String args = sj.getValue2();
        int maxDuration = sj.getValue3();
        String contextArgs[] = {};
        String context = null;
        String user = null;
        
        if (!args.isEmpty() && !args.equals("indef")) contextArgs = args.split(",",2);
        
        if (contextArgs.length == 2) {
            context = contextArgs[0];
            user = contextArgs[1];
        } else if (contextArgs.length == 1) {
            context = contextArgs[0];
        }
        
        if (!target.isEmpty() && !target.equals("indef")) {
            
            runZapScan(target, context, user, maxDuration);
            
            if (zsList != null && !zsList.isEmpty()) {
                saveZapScan(target);
                return true;
            }
        }
        
        return false;
    }
    
    
    public void runZapScan(String target, String context, String user, int md) {
        
        String ZAP_ADDRESS = project.getZapHost();
        int ZAP_PORT = project.getZapPort();
        String ZAP_KEY = project.getZapKey();
        
        ClientApi api = new ClientApi(ZAP_ADDRESS, ZAP_PORT, ZAP_KEY);

        try {
            
            api.core.deleteAllAlerts();
                        
            api.spider.setOptionMaxDuration(md);
            
            ApiResponse resp;
            
            if (context == null && user == null) {
                resp = api.spider.scan(target, null, null, null, null);
            } else {
                if (user == null) {
                    resp = api.spider.scan(target, null, null, context, null);
                } else {
                    resp = api.spider.scanAsUser(context, user, target, null, null, null);
                }
            }
            
            // The scan now returns a scan id to support concurrent scanning
            String scanid = ((ApiResponseElement) resp).getValue();
            
            int progress = 0;
            
            // Poll the status until it completes
            while (true) {
                
                Thread.sleep(1000);
                
                progress = Integer.parseInt(((ApiResponseElement) api.spider.status(scanid)).getValue());
                
                if (progress >= 100) {
                    break;
                }
            }
            
            Thread.sleep(3000);
            /*
            progress = 0;
            
            api.ascan.setOptionMaxScanDurationInMins(value3);
            
            if (context == null && user == null) {
                resp = api.ascan.scan(target, "True", "False", null, null, null);
            } else {
                if (user == null) {
                    resp = api.ascan.scan(target, "True", "False", null, null, null,context);
                } else {
                    resp = api.ascan.scanAsUser(target, context, user, "True", "False", null, null, null);
                }
            }
            
            scanid = ((ApiResponseElement) resp).getValue();

            while (true) {
                
                Thread.sleep(5000);
                
                progress = Integer.parseInt(((ApiResponseElement) api.ascan.status(scanid)).getValue());
                
                if (progress >= 100) {
                    break;
                }
            }*/
            
            String scanReport = new String(api.core.jsonreport(), StandardCharsets.UTF_8);
            
            ZapScanJson zsj = new ZapScanJson();
            
            List<ZapScanReport> zsrList = zsj.getResult(target, scanReport);
            
            if (zsrList != null && zsrList.size() > 0) {
                
                zsList = new ArrayList();
                
                for (ZapScanReport zsr : zsrList) {
                    
                    ZapScan zs = new ZapScan();
                    
                    zs.setRefId(project.getRefId());
                    zs.setUrl(target);
                    zs.setPluginid(zsr.getPluginid());
                    zs.setAlert(zsr.getAlert());
                    zs.setName(zsr.getName());
                    zs.setRiskcode(zsr.getRiskcode());
                    zs.setConfidence(zsr.getConfidence());
                    zs.setRiskdesc(zsr.getRiskdesc());
                    zs.setDescription(zsr.getDesc());
                    zs.setCounter(zsr.getCount());
                    zs.setInstances(zsr.getInstances());
                    zs.setSolution(zsr.getSolution());
                    zs.setOtherinfo(zsr.getOtherinfo());
                    zs.setReference(zsr.getReference());
                    zs.setCweid(zsr.getCweid());
                    zs.setWascid(zsr.getWascid());
                    zs.setSourceid(zsr.getSourceid());
                    
                    Date date = new Date();
                    zs.setReportAdded(date);
                    zs.setReportUpdated(date);
                    
                    zsList.add(zs);
                            
                }
            } 
        } catch (Exception e) {
            zsList = new ArrayList();
            logger.error("alertflex_mc_exception", e);
        }
    }
    
    
    public void saveZapScan(String t) {
        
        for (ZapScan zs : zsList) {
            
            ZapScan existingRecord = scanJobs.getZapScanFacade().findRecord(project.getRefId(), t, zs.getPluginid(), zs.getCweid(), zs.getSourceid(), zs.getWascid());
            
            if ( existingRecord == null) {
                
                scanJobs.getZapScanFacade().create(zs);
                
                AlertPriority ap = scanJobs.getAlertPriorityFacade().findPriorityBySource(project.getRefId(), "ZAP");
                int sev = 0;
                String risk = zs.getRiskdesc();
                        
                if (!risk.isEmpty() && ap != null) {
                            
                    sev = ap.getSeverityDefault();
                            
                    if(ap.getText1().equals(risk)) {
                        sev = ap.getValue1();
                    } else if(ap.getText2().equals(risk)) {
                        sev = ap.getValue2();
                    } else if(ap.getText3().equals(risk)) {
                        sev = ap.getValue3();
                    } else if(ap.getText4().equals(risk)) {
                        sev = ap.getValue4();
                    } else if(ap.getText5().equals(risk)) {
                        sev = ap.getValue5();
                    } 
                            
                    if (sev >= ap.getSeverityThreshold()) { 
                        
                        Alert a = new Alert();
                
                        a.setNodeId("controller");
                        a.setRefId(project.getRefId());
                        a.setAlertUuid(UUID.randomUUID().toString());
                        a.setAlertSource("ZAP");
                        a.setAlertType("MISC");
                        a.setSensorId("controller");
                        a.setAlertSeverity(sev);
                        a.setDescription(zs.getDescription());
                        a.setEventId("1");
                        a.setEventSeverity(risk);
                        a.setLocation(t);
                        a.setAction("indef");
                        a.setStatus("processed");
                        a.setInfo("indef");
                        a.setFilter("indef");
                        a.setTimeEvent("");
                        Date d = new Date(); 
                        a.setTimeCollr(d);
                        a.setTimeCntrl(d);
                        a.setAgentName("indef");
                        a.setUserName("indef");
                        a.setCategories("Zap scan alert");
                        a.setSrcIp("indef");
                        a.setDstIp("indef");
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
                
                        scanJobs.getAlertFacade().create(a);
                    }
                }
            } else {
                Date d = new Date();
                existingRecord.setReportUpdated(d);
                scanJobs.getZapScanFacade().edit(existingRecord);
            }
        }
    }
}