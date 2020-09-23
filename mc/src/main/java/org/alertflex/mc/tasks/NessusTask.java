/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alertflex.mc.tasks;

import java.util.Date;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.alertflex.mc.db.Alert;
import org.alertflex.mc.db.NessusScan;
import org.alertflex.mc.db.Project;
import org.alertflex.mc.db.ScanJob;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.security.cert.CertificateException;
import javax.net.ssl.*;
import java.util.List;
import org.alertflex.mc.db.AlertPriority;
import org.json.*;


/**
 *
 * @author root
 */

public class NessusTask {
    
    private static final Logger logger = LoggerFactory.getLogger(NessusTask.class);
    
    private ScanJobs scanJobs;
    
    private Project project = null;
        
    List<NessusScan> nsList = null;
   
    public NessusTask(ScanJobs sj) {
        
        this.scanJobs = sj;
        
    }
    
    public Boolean run(Project p, ScanJob sj) throws Exception {
        
        project = p;
        
        String target = sj.getValue1();
                                    
        if (!target.isEmpty() && !target.equals("indef")) {
            
            getNessusScan(target);
            
            if (nsList != null) {
                
                saveNessusScan(target);
                return true;
            }
        }
        
        return false;
    }
    
    
    public void getNessusScan(String target) {
        
        try {

            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager)trustAllCerts[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            OkHttpClient client = builder.build();
            
            //https://192.168.1.37:8834
            String urlNessus = project.getNessusUrl();
            
            if (urlNessus.isEmpty()) return;
            
            String accessKey = project.getNessusAccesskey();
            String secretKey = project.getNessusSecretkey();
            
            if (accessKey.isEmpty() || secretKey.isEmpty()) return;
            
            //"accessKey=0533145804...;secretKey=b4faca9e316075112..."
            String keys = "accessKey=" + accessKey + ";" + secretKey;
            
            Request request = new Request.Builder()
                .url(urlNessus + "/scans")
                .get()
                .addHeader("accept", "application/json")
                .addHeader("X-ApiKeys", keys)
                .build();

            Response response = client.newCall(request).execute();

            if (response.message().equals("OK")) {

                JSONObject obj = new JSONObject(response.body().string());
                JSONArray scans = obj.getJSONArray("scans");

                Integer scanId = 0;

                for (int i = 0; i < scans.length(); i++) {
                    String name = scans.getJSONObject(i).getString("name");
                    if (name.equals(target)) {
                        scanId =  scans.getJSONObject(i).getInt("id");
                        break;
                    }
                }

                if (scanId == 0) {
                    return;
                }

                String getUrlScanId = urlNessus + "/" + Integer.toString(scanId);

                request = new Request.Builder()
                        .url(urlNessus)
                        .get()
                        .addHeader("accept", "application/json")
                        .addHeader("X-ApiKeys", keys)
                        .build();

                response = client.newCall(request).execute();

                if (response.message().equals("OK")) {

                    obj = new JSONObject(response.body().string());
                    scans = obj.getJSONArray("vulnerabilities");
                    Date date = new Date();

                    for (int i = 0; i < scans.length(); i++) {
                        
                        NessusScan ns = new NessusScan();
                        
                        ns.setRefId(project.getRefId());
                        ns.setScanName(target);
                        ns.setCount(scans.getJSONObject(i).getInt("count"));
                        ns.setVulnIndex(scans.getJSONObject(i).getInt("vuln_index"));
                        ns.setPluginName(scans.getJSONObject(i).getString("plugin_name"));
                        ns.setSeverity(scans.getJSONObject(i).getInt("severity"));
                        ns.setPluginId(scans.getJSONObject(i).getInt("plugin_id"));
                        ns.setSeverityIndex(scans.getJSONObject(i).getInt("severity_index"));
                        ns.setPluginFamily(scans.getJSONObject(i).getString("plugin_family"));
                        
                        // ad cpe?
                        
                        ns.setReportAdded(date);
                        ns.setReportUpdated(date);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    
    public void saveNessusScan(String t) {
        
        if (nsList == null) return;
        
        for (NessusScan ns : nsList) {
            
            //findRecord(String ref, String scanName, int vulnIndex, String pluginName) {
            
            NessusScan existingRecord = scanJobs.getNessusScanFacade().findRecord(project.getRefId(), t, ns.getVulnIndex(), ns.getPluginName());
            
            if (existingRecord == null) {
                
                scanJobs.getNessusScanFacade().create(ns);
                
                AlertPriority ap = scanJobs.getAlertPriorityFacade().findPriorityBySource(project.getRefId(), "Nessus");
                if (ap != null) {
                            
                    int sev = ap.getSeverityDefault();
                    int score = ns.getSeverity();
                    int thMinor = ap.getMinorThreshold();
                    int thMajor = ap.getMajorThreshold();
                    int thCritical = ap.getCriticalThreshold();
                        
                    if (score > thMinor && thMinor != 0) sev = 1;
                        
                    if (score > thMajor && thMajor != 0) sev = 2;
                        
                    if (score > thCritical && thCritical != 0) sev = 3;
                            
                    if (sev >= ap.getSeverityThreshold()) {
                    
                                
                        Alert a = new Alert();
                
                        a.setNodeId("controller");
                        a.setRefId(project.getRefId());
                        a.setAlertUuid(UUID.randomUUID().toString());
                        a.setAlertSource("Nessus");
                        a.setAlertType("MISC");
                        a.setSensorId("controller");
                        a.setAlertSeverity(sev);
                        a.setDescription("Vulnerabilitie: " + ns.getPluginName());
                        a.setEventId("1");
                        a.setEventSeverity(Integer.toString(score));
                        a.setLocation(t);
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
                        a.setCategories("new Nessus scan alert");
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
                scanJobs.getNessusScanFacade().edit(existingRecord);
            }
        }
    }
}