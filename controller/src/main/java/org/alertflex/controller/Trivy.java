package org.alertflex.controller;


import java.util.Date;
import java.util.UUID;
import org.alertflex.entity.Alert;
import org.alertflex.entity.TrivyScan;
import org.alertflex.entity.Project;
import org.alertflex.entity.Node;
import org.json.JSONArray;
import org.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author root
 */
public class Trivy {
    
    private static final Logger logger = LoggerFactory.getLogger(Trivy.class);
    
    private InfoMessageBean eventBean;
    Project project;
    Node node;
    
    public Trivy (InfoMessageBean eb) {
        this.eventBean = eb;
        this.project = eventBean.getProject();
        
    }
    
    public void saveReport(String sensor, String results) {
        
        try {
            
            String n = eventBean.getNode();
            String r = eventBean.getRefId();
            Date date = new Date();
            
            node = eventBean.getNodeFacade().findByNodeName(r,n);
        
            if (node == null || sensor == null || results.isEmpty()) return;
            
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
                    ts.setSensor(sensor);
                    ts.setTarget(target);
                    ts.setTargetType(targetType);
                    
                    String vulnerabilityID  = arrVuln.getJSONObject(j).getString("VulnerabilityID");
                    ts.setVulnerabilityId(vulnerabilityID);
                    
                    String pkgName  = arrVuln.getJSONObject(j).getString("PkgName");
                    ts.setPkgName(pkgName);
                    
                    String installedVersion  = arrVuln.getJSONObject(j).getString("InstalledVersion");
                    ts.setInstalledVersion(installedVersion);
                    
                    String fixedVersion  = arrVuln.getJSONObject(j).getString("FixedVersion");
                    ts.setFixedVersion(fixedVersion);
                    
                    String severitySource = arrVuln.getJSONObject(j).getString("SeveritySource");
                    ts.setSeveritySource(severitySource);
                    
                    String title = arrVuln.getJSONObject(j).getString("Title");
                    ts.setTitle(title);
                    
                    String description = arrVuln.getJSONObject(j).getString("Description");
                    ts.setDescription(description);
                    
                    String severityTrivy = arrVuln.getJSONObject(j).getString("Severity");
                                        
                    int severity = 0;
                    
                    switch (severityTrivy) {
                        case "INFO":
                            severity = 0;
                            break;
                        case "LOW":
                            severity = 1;
                            break;
                        case "MEDIUM":
                            severity = 2;
                            break;
                        case "HIGH":
                            severity = 3;
                            break;
                        case "CRITICAL":
                            severity = 3;
                            break;
                        default:
                            break;
                    }
                    
                    ts.setSeverity(severity);
                    
                    ts.setReportAdded(date);
                    ts.setReportUpdated(date);
                    
                    TrivyScan tsExisting = eventBean.getTrivyScanFacade()
                        .findVulnerability(r, n, sensor, target, vulnerabilityID, pkgName);
                    
                    if (tsExisting == null) {
                        
                        eventBean.getTrivyScanFacade().create(ts);
                        
                        createTrivyScanAlert(ts);
                    
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
    
    
public void createTrivyScanAlert(TrivyScan ts) {
    
       
        Alert a = new Alert();
            
        a.setNodeId(eventBean.getNode());
        a.setRefId(eventBean.getRefId());
        a.setAlertUuid(UUID.randomUUID().toString());
        
        a.setAlertSeverity(ts.getSeverity());
        a.setEventId("1");
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
