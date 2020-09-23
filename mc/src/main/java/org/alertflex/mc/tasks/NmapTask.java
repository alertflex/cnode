/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alertflex.mc.tasks;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.alertflex.mc.db.Alert;
import org.alertflex.mc.db.AlertPriority;
import org.alertflex.mc.db.NmapScan;
import org.alertflex.mc.db.Project;
import org.alertflex.mc.db.ScanJob;
import org.alertflex.mc.supp.NmapScanJson;
import org.alertflex.mc.supp.NmapScanReport;


/**
 *
 * @author root
 */

public class NmapTask {
    
    private static final Logger logger = LoggerFactory.getLogger(NmapTask.class);
    
    private ScanJobs scanJobs;
    
    private Project project = null;
        
    List<NmapScan> nsList = null;
   
    public NmapTask(ScanJobs sj) {
        
        this.scanJobs = sj;
        
    }
    
    public Boolean run(Project p, ScanJob sj) throws Exception {
        
        project = p;
        
        String target = sj.getValue1();
        
        String arg = sj.getValue2();
                                    
        if (!target.isEmpty() && !target.equals("indef")) {
            
            runNmapScan(target, arg);
            
            if (nsList != null && !nsList.isEmpty()) {
                
                saveNmapScan(target);
                return true;
            }
        }
        
        return false;
    }
    
    
    public void runNmapScan(String t, String a) throws InterruptedException,IOException {
        
        String arg = "-oX";
        
        if (!a.isEmpty() && !a.equals("indef")) arg = a;
        
        ProcessBuilder pb = new ProcessBuilder("nmap",arg, "-", t);
        pb.redirectErrorStream(true);
        
        try {
            Process shell = pb.start();
            
            // To capture output from the shell
            InputStream shellIn = shell.getInputStream();
 
            Thread.sleep(1000);
            
            // Wait for the shell to finish and get the return code
            int shellExitStatus = shell.waitFor();
            
            NmapScanJson nsj = new NmapScanJson();
            
            List<NmapScanReport> nsrList = nsj.getResult(shellIn);
            
            if (nsrList != null && nsrList.size() > 0) {
                
                nsList = new ArrayList();
                
                for (NmapScanReport nsr : nsrList) {
                    
                    NmapScan ns = new NmapScan();
                    
                    ns.setRefId(project.getRefId());
                    ns.setHost(t);
                    ns.setProtocol(nsr.getProtocol());
                    ns.setPortId( Integer.parseInt(nsr.getPortid()));
                    ns.setState(nsr.getState());
                    ns.setName(nsr.getName());
                    Date date = new Date();
                    ns.setReportAdded(date);
                    ns.setReportUpdated(date);
                    
                    nsList.add(ns);
                            
                }
                
            }
            
        } catch (InterruptedException | IOException e) {
            
            nsList = new ArrayList();
        }
    }
    
    
    public void saveNmapScan(String t) {
        
        for (NmapScan ns : nsList) {
            
            NmapScan existingRecord = scanJobs.getNmapScanFacade().findRecord(project.getRefId(), t, ns.getPortId(), ns.getState());
            
            if (existingRecord == null) {
                
                scanJobs.getNmapScanFacade().create(ns);
                
                AlertPriority ap = scanJobs.getAlertPriorityFacade().findPriorityBySource(project.getRefId(), "Nmap");
                if (ap != null) {
                    
                    int sev = ap.getSeverityDefault();
                    
                    if (sev >= ap.getSeverityThreshold()) {
                    
                        Alert a = new Alert();
                
                        a.setNodeId("controller");
                        a.setRefId(project.getRefId());
                        a.setAlertUuid(UUID.randomUUID().toString());
                        a.setAlertSource("Nmap");
                        a.setAlertType("MISC");
                        a.setSensorId("controller");
                        a.setAlertSeverity(sev);
                        a.setDescription("New port - " + new Integer(ns.getPortId()).toString() + " or new state - " + ns.getState());
                        a.setEventId("1");
                        a.setEventSeverity(Integer.toString(sev));
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
                        a.setCategories("Nmap scan alert");
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
                scanJobs.getNmapScanFacade().edit(existingRecord);
            }
        }
    }
}