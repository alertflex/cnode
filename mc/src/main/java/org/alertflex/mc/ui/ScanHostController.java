/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alertflex.mc.ui;

/**
 *
 * @author root
 */


import java.io.IOException;
import java.io.InputStream;
import org.alertflex.mc.supp.AuthenticationSingleton;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable; 
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import java.util.ArrayList;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;
import javax.faces.application.FacesMessage;
import org.zaproxy.clientapi.core.ApiResponse;
import org.zaproxy.clientapi.core.ApiResponseElement;
import org.zaproxy.clientapi.core.ClientApi;
import org.alertflex.mc.db.Alert;
import org.alertflex.mc.db.AlertPriority;
import org.alertflex.mc.db.Project;
import org.alertflex.mc.services.ProjectFacade;
import org.alertflex.mc.db.NmapScan;
import org.alertflex.mc.supp.NmapScanJson;
import org.alertflex.mc.supp.NmapScanReport;
import org.alertflex.mc.db.ZapScan;
import org.alertflex.mc.services.AlertFacade;
import org.alertflex.mc.services.AlertPriorityFacade;
import org.alertflex.mc.services.NmapScanFacade;
import org.alertflex.mc.services.ZapScanFacade;
import org.alertflex.mc.supp.ZapScanJson;
import org.alertflex.mc.supp.ZapScanReport;


@ManagedBean
@ViewScoped
public class ScanHostController implements Serializable {
    
    @EJB
    private AuthenticationSingleton authSingleton;
    
    String session_tenant;
    String session_user;
    
    @EJB
    private ProjectFacade projectFacade;
    Project project;
    
    @EJB
    private AlertPriorityFacade alertPriorityFacade;
    
    @EJB
    private AlertFacade alertsFacade;
   
    @EJB
    private NmapScanFacade nmapScanFacade;
        
    @EJB
    private ZapScanFacade zapScanFacade;
    
    Integer progress = 0;
        
    List<NmapScan> nsList;
    List<ZapScan> zsList;
    
    String target = "";
    String value2 = "indef";
    Integer value3 = 3;
    
    
    @PostConstruct
    public void init() {
        
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        HttpSession session = request.getSession(false);
        
        session_tenant = authSingleton.getTenantName(session);
        session_user = authSingleton.getUserName(session);
        
        project = projectFacade.find(session_tenant);
        
        nsList = new ArrayList();
        zsList = new ArrayList();
    }
    
    public List<ZapScan> getZsList() {
        
        return zsList;
    }
    
    public List<NmapScan> getNsList() {
        
        return nsList;
    }
    
    public String getTarget() {
        
        return target;
    }
 
    public void setTarget(String t) {
        
        this.target = t;
        
    }
    
    public String getValue2() {
        return value2;
    }
 
    public void setValue2(String v) {
        this.value2 = v;
    }
    
    public Integer getValue3() {
        return value3;
    }
 
    public void setValue3(Integer v) {
        this.value3 = v;
    }
    
    public Integer getProgress() {
        
        if(progress > 100) progress = 100;
        return progress;
    }
 
    public void setProgress(Integer progress) {
        this.progress = progress;
    }
     
    public void stopScanHost() {
        progress = 100;
    }
    
    public void runZapScan() {
        
        String contextArgs[] = {};
        String context = null;
        String user = null;
        
        if (!value2.isEmpty() && !value2.equals("indef")) contextArgs = value2.split(",",2);
        
        if (contextArgs.length == 2) {
            context = contextArgs[0];
            user = contextArgs[1];
        } else if (contextArgs.length == 1) {
            context = contextArgs[0];
        }
        
        if (target == null || target.isEmpty()) return;
        
        String ZAP_ADDRESS = project.getZapHost();
        int ZAP_PORT = project.getZapPort();
        String ZAP_KEY = project.getZapKey();
        
        if (ZAP_KEY.isEmpty()) ZAP_KEY = null;
        
        ClientApi api = new ClientApi(ZAP_ADDRESS, ZAP_PORT, ZAP_KEY);

        try {
            
            api.core.deleteAllAlerts();
            
            api.spider.setOptionMaxDuration(value3);
            
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
                    
                    zs.setRefId(session_tenant);
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
            
        }
        
    }
    
    public void runNmapScan() throws InterruptedException,IOException {
        
        if (target == null || target.isEmpty()) return;
        
        String arg = "-oX";
        
        if (!value2.isEmpty() && !value2.equals("indef")) arg = value2;
        
        
        ProcessBuilder pb = new ProcessBuilder("nmap",arg, "-", target);
        pb.redirectErrorStream(true);
        
        try {
            Process shell = pb.start();
            
            // To capture output from the shell
            InputStream shellIn = shell.getInputStream();
 
            for (int i = 0; i < 60; i = i + 10) {
                Thread.sleep(500);
                 progress = i;               
            }
            
            // Wait for the shell to finish and get the return code
            int shellExitStatus = shell.waitFor();
            
            for (int i = 60; i < 110; i = i + 10) {
                Thread.sleep(100);
                 progress = i;               
            }
            
            NmapScanJson nsj = new NmapScanJson();
            
            List<NmapScanReport> nsrList = nsj.getResult(shellIn);
            
            if (nsrList != null && nsrList.size() > 0) {
                
                nsList = new ArrayList();
                
                for (NmapScanReport nsr : nsrList) {
                    
                    NmapScan ns = new NmapScan();
                    
                    ns.setRefId(session_tenant);
                    ns.setHost(target);
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
    
    
    public void saveNmapScan() {
        
        FacesMessage message;
        
        if (nsList == null) return;
        
        for (NmapScan ns : nsList) {
            
            NmapScan existingRecord = nmapScanFacade.findRecord(session_tenant, target, ns.getPortId(), ns.getState());
            
            if (existingRecord == null) {
                
                nmapScanFacade.create(ns);
                
                AlertPriority ap = alertPriorityFacade.findPriorityBySource(project.getRefId(), "Nmap");
                if (ap != null) {
                    
                    int sev = ap.getSeverityDefault();
                    
                    if (sev >= ap.getSeverityThreshold()) {
                                
                        Alert a = new Alert();
                
                        a.setNodeId("controller");
                        a.setRefId(session_tenant);
                        a.setAlertUuid(UUID.randomUUID().toString());
                        a.setAlertSource("Alertflex");
                        a.setAlertType("MISC");
                        a.setSensorId("controller");
                        a.setAlertSeverity(sev);
                        a.setDescription("New port - " + new Integer(ns.getPortId()).toString() + " or new state - " + ns.getState());
                        a.setEventId("1");
                        a.setEventSeverity(Integer.toString(sev));
                        a.setLocation(ns.getHost());
                        a.setAction("indef");
                        a.setStatus("processed_new");
                        a.setInfo("indef");
                        a.setFilter("indef");
                        a.setTimeEvent("");
                        Date d = new Date(); 
                        a.setTimeCollr(d);
                        a.setTimeCntrl(d);
                        a.setAgentName("indef");
                        a.setUserName("indef");
                        a.setCategories("new Nmap scan alert");
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
               
                        alertsFacade.create(a);
                    }
                }
                
            } else {
                Date d = new Date();
                existingRecord.setReportUpdated(d);
                nmapScanFacade.edit(existingRecord);
            }
            
        }
        
        message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Result: ","Scan has been saved.");
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
    
    public void viewNmapScan() {
    
        if (target != null) {
            
            nsList = nmapScanFacade.findRecordsByHost(session_tenant, target);
            
            if (nsList == null) nsList = new ArrayList();
        }
    }
    
    public void saveZapScan() {
        
        FacesMessage message;
        
        if (zsList == null) return;
        
        for (ZapScan zs : zsList) {
            
            ZapScan existingRecord = zapScanFacade.findRecord(session_tenant, target, zs.getPluginid(), zs.getCweid(), zs.getSourceid(), zs.getWascid());
            
            if ( existingRecord == null) {
                
                zapScanFacade.create(zs);
                
                AlertPriority ap = alertPriorityFacade.findPriorityBySource(project.getRefId(), "ZAP");
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
                        a.setRefId(session_tenant);
                        a.setAlertUuid(UUID.randomUUID().toString());
                        a.setAlertSource("Alertflex");
                        a.setAlertType("MISC");
                        a.setSensorId("controller");
                        a.setAlertSeverity(sev);
                        a.setDescription(zs.getDescription());
                        a.setEventId("1");
                        a.setEventSeverity(risk);
                        a.setLocation(zs.getUrl());
                        a.setAction("indef");
                        a.setStatus("processed_new");
                        a.setInfo("indef");
                        a.setFilter("indef");
                        a.setTimeEvent("");
                        Date d = new Date(); 
                        a.setTimeCollr(d);
                        a.setTimeCntrl(d);
                        a.setAgentName("indef");
                        a.setUserName("indef");
                        a.setCategories("new Zap scan alert");
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
                    
                        alertsFacade.create(a);
                    }
                }
            } else {
                Date d = new Date();
                existingRecord.setReportUpdated(d);
                zapScanFacade.edit(existingRecord);
            }
        }
        
         message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Result: ","Scan has been saved.");
         FacesContext.getCurrentInstance().addMessage(null, message);
    }
    
    public void viewZapScan() {
        
        if (target != null) {
            
            zsList = zapScanFacade.findRecordsByUrl(session_tenant, target);
            
            if (zsList == null) zsList = new ArrayList();
        }
        
    }
    
    public void deleteZsRecord(ZapScan zs) {
        
        if(zs != null) {
            
            try {
                
                zapScanFacade.remove(zs);
                
                viewZapScan();
                
                FacesMessage msg = new FacesMessage("Item has been removed" );
                FacesContext.getCurrentInstance().addMessage(null, msg);
                FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
            } catch (Exception ex) { 
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error !", ex.toString())); 
                FacesContext.getCurrentInstance().validationFailed(); 
            }
        }
        
    }
    
    public void deleteNsRecord(NmapScan ns) {
        
        if(ns != null) {
            
            try {
                
                nmapScanFacade.remove(ns);
                
                viewNmapScan();
                
                FacesMessage msg = new FacesMessage("Item has been removed" );
                FacesContext.getCurrentInstance().addMessage(null, msg);
                FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
            } catch (Exception ex) { 
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error !", ex.toString())); 
                FacesContext.getCurrentInstance().validationFailed(); 
            }
        }
        
    }
} 