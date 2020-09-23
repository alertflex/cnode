/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alertflex.mc.tasks;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.alertflex.mc.db.Alert;
import org.alertflex.mc.db.SnykScan;
import org.alertflex.mc.db.Project;
import org.alertflex.mc.db.ScanJob;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;
import java.util.List;
import org.alertflex.mc.db.AlertPriority;
import org.json.*;


/**
 *
 * @author root
 */

public class SnykTask {
    
    private static final Logger logger = LoggerFactory.getLogger(SnykTask.class);
    
    private ScanJobs scanJobs;
    
    private Project project = null;
        
    List<SnykScan> ssList = null;
   
    public SnykTask(ScanJobs sj) {
        
        this.scanJobs = sj;
        
    }
    
    public Boolean run(Project p, ScanJob sj) throws Exception {
        
        project = p;
        
        String target = sj.getValue1();
                                    
        if (!target.isEmpty() && !target.equals("indef")) {
            
            getSnykScan(target);
            
            if (ssList != null && !ssList.isEmpty()) {
                
                saveSnykScan(target);
                
                return true;
            }
        }
        
        return false;
    }
    
    
    public void getSnykScan(String target) {
        
        String snykOrgid = project.getSnykOrgid();
        
        String urlSnyk = "https://snyk.io/api/v1/org/" + snykOrgid + "/project/" + target + "/aggregated-issues";
        
        try {
            
            Client client = ClientBuilder.newClient();
            Entity payload = Entity.json("{  \"includeDescription\": false,  \"filters\": {    \"severities\": [      \"high\",      \"medium\",      \"low\"    ],    \"exploitMaturity\": [      \"mature\",      \"proof-of-concept\",      \"no-known-exploit\",      \"no-data\"    ],    \"types\": [      \"vuln\",      \"license\"    ],    \"ignored\": false,    \"patched\": false,    \"priority\": {      \"score\": {        \"min\": 0,        \"max\": 1000      }    }  }}");
            Response response = client.target(urlSnyk)
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .header("Authorization", "token " + project.getSnykKey())
                    .post(payload);
            
            int status = response.getStatus();
            
            if (status >= 200 && status <= 203) {
                
                Date date = new Date();
               
                String report = response.readEntity(String.class);
                
                if (!report.isEmpty()) {
                    
                    ssList = new ArrayList();
                    
                    JSONObject body = new JSONObject(report);
                    
                    JSONArray issues = body.getJSONArray("issues");
                    
                    for (int i = 0; i < issues.length(); i++) {
                        
                        SnykScan ss = new SnykScan();
                        
                        ss.setRefId(project.getRefId());
                        ss.setProjectId(target);
                        
                        JSONObject issue = issues.getJSONObject(i);
                        
                        String id = issue.getString("id");
                        ss.setVulnerabilityId(id);
                        
                        String pkgName = issue.getString("pkgName");
                        ss.setPkgName(pkgName);
                        
                        String issueType = issue.getString("issueType");
                        ss.setIssueType(issueType);
                        
                        String[] pkgArr = toStringArray(issue.getJSONArray("pkgVersions"));
                        String pkgVersions = String.join(", ", pkgArr);
                        ss.setPkgVersions(pkgVersions);
  
                        int priorityScore = issue.getInt("priorityScore");
                        ss.setPriorityScore(priorityScore);
                        
                        JSONObject issueData = issue.getJSONObject("issueData");
                        
                        String title = issueData.getString("title");
                        ss.setIssueTitle(title);
                        
                        String severity = issueData.getString("severity");
                        ss.setIssueSeverity(severity);
                        
                        String url = issueData.getString("url");
                        ss.setIssueUrl(url);
  
                        JSONObject identifiers = issueData.getJSONObject("identifiers");
                        
                        String[] cveArr = toStringArray(identifiers.getJSONArray("CVE"));
                        String issueCve = String.join(", ", cveArr);
                        ss.setIssueCve(issueCve);
  
                        String publicationTime = issueData.getString("publicationTime");
                        ss.setPublicationTime(publicationTime);
                        
                        String disclosureTime = issueData.getString("disclosureTime");
                        ss.setDisclosureTime(disclosureTime);
  
                        String CVSSv3 = issueData.getString("CVSSv3");
                        ss.setIssueCvssv3(CVSSv3);
 
                        String language = issueData.getString("language");
                        ss.setIssueLanguage(language);
                        
                        ss.setReportAdded(date);
                        ss.setReportUpdated(date);
                    
                        ssList.add(ss);
   
                    }
                    
                }
                
            }
            
            response.close();
            
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    private String[] toStringArray(JSONArray jsonArray) throws JSONException {
        
        String[] array = new String[jsonArray.length()];
        for (int i = 0; i < array.length; i++)
        array[i] = jsonArray.getString(i);
        return array;
    }
    
    
    public void saveSnykScan(String t) {
        
        for (SnykScan ss : ssList) {
            
            SnykScan ssExisting = scanJobs.getSnykScanFacade()
                .findVulnerability(project.getRefId(), t, ss.getVulnerabilityId(), ss.getPkgName());
            
            if (ssExisting == null) {
                
                AlertPriority ap = scanJobs.getAlertPriorityFacade().findPriorityBySource(project.getRefId(), "Snyk");
                int sev = 0;
                String issueSev = ss.getIssueSeverity();
                
                if (!issueSev.isEmpty() && ap != null) {
                            
                    sev = ap.getSeverityDefault();
                            
                    if(ap.getText1().equals(issueSev)) {
                        sev = ap.getValue1();
                    } else if(ap.getText2().equals(issueSev)) {
                        sev = ap.getValue2();
                    } else if(ap.getText3().equals(issueSev)) {
                        sev = ap.getValue3();
                    } else if(ap.getText4().equals(issueSev)) {
                        sev = ap.getValue4();
                    } 
                    
                    ss.setSeverity(sev);
                    scanJobs.getSnykScanFacade().create(ss);
                            
                    if (sev >= ap.getSeverityThreshold()) { 
                
                        Alert a = new Alert();
                
                        a.setNodeId("controller");
                        a.setRefId(project.getRefId());
                        a.setAlertUuid(UUID.randomUUID().toString());
                        a.setAlertSource("Snyk");
                        a.setAlertType("MISC");
                        a.setSensorId("controller");
                        a.setAlertSeverity(sev);
                        a.setDescription(ss.getIssueTitle());
                        a.setEventId("1");
                        a.setEventSeverity(issueSev);
                        a.setLocation(ss.getProjectId());
                        a.setAction("indef");
                        a.setStatus("processed");
                        a.setInfo(ss.getVulnerabilityId());
                        a.setFilter("indef");
                        a.setTimeEvent("");
                        Date d = new Date(); 
                        a.setTimeCollr(d);
                        a.setTimeCntrl(d);
                        a.setAgentName("indef");
                        a.setUserName("indef");
                        a.setCategories("snyk, " + ss.getIssueCve());
                        a.setSrcIp("indef");
                        a.setDstIp("indef");
                        a.setDstPort(0);
                        a.setSrcPort(0);
                        a.setSrcHostname("indef");
                        a.setDstHostname("indef");
                        a.setFileName(ss.getPkgName());
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
                ssExisting.setReportUpdated(d);
                scanJobs.getSnykScanFacade().edit(ssExisting);
            }
        }
    }
}