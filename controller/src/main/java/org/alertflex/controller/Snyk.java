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
import org.alertflex.entity.Project;
import org.alertflex.entity.Node;
import org.alertflex.entity.SnykScan;
import org.alertflex.entity.ZapScan;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Snyk {

    private static final Logger logger = LoggerFactory.getLogger(Snyk.class);

    private InfoMessageBean eventBean;
    Project project;
    Node node;

    public Snyk(InfoMessageBean eb) {
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
            JSONArray arr = obj.getJSONArray("vulnerabilities");
            
            for (int i = 0; i < arr.length(); i++) {
                
                JSONObject vuln = arr.getJSONObject(i);
                
                SnykScan ss = new SnykScan();
                
                ss.setRefId(r);
                ss.setNodeId(n);
                ss.setProbe(p);
                
                ss.setProjectId(target);

                String id = vuln.getString("id");
                ss.setVulnId(id);
                
                String packageName = vuln.getString("name");
                ss.setPackageName(packageName);
                
                String packageManager = vuln.getString("packageManager");
                ss.setPackageManager(packageManager);
                
                String severity = vuln.getString("severity");
                ss.setSeverity(severity);
                
                String language = vuln.getString("language");
                ss.setLanguage(language);
                
                String title = vuln.getString("title");
                ss.setTitle(title);
                
                String desc = vuln.getString("description"); // 2048
                if (desc.length() >= 2048) {
                    String substrDesc = desc.substring(0, 2046);
                    ss.setDescription(substrDesc);
                } else {
                    ss.setDescription(desc);
                }
                
                String version = vuln.getString("version");
                ss.setVulnVersion(version);
                
                String publicationTime = vuln.getString("publicationTime");
                ss.setPublicationTime(publicationTime);
                
                JSONArray ref = vuln.getJSONArray("references");
                String references = "";
                for (int j = 0; j < ref.length(); j++) {
                    JSONObject url = ref.getJSONObject(j);
                    references = references + " " + url.getString("url"); //2048
                }
                
                if (references.length() >= 2048) {
                    String substrReferences = references.substring(0, 2046);
                    ss.setVulnRef(substrReferences);
                } else {
                    ss.setVulnRef(references);
                }
                
                JSONObject identifiers = vuln.getJSONObject("identifiers");

                String[] cveArr = toStringArray(identifiers.getJSONArray("CVE"));
                String CVE = String.join(", ", cveArr);
                ss.setVulnCve(CVE);
                
                String[] cweArr = toStringArray(identifiers.getJSONArray("CWE"));
                String CWE = String.join(", ", cweArr);
                ss.setVulnCwe(CWE);
                
                ss.setReportAdded(date);
                ss.setReportUpdated(date);
                
                SnykScan ssExisting = eventBean.getSnykScanFacade().findVulnerability(r, n, p, target, id, packageName);
                
                if (ssExisting == null) {

                    eventBean.getSnykScanFacade().create(ss);
                    
                } else {

                    ssExisting.setReportUpdated(date);
                    eventBean.getSnykScanFacade().edit(ssExisting);
                }
                
            }
            
        } catch (Exception e) {
            logger.error("alertflex_ctrl_exception", e);
        }
    }
    
    private String[] toStringArray(JSONArray jsonArray) throws JSONException {

        String[] array = new String[jsonArray.length()];
        for (int i = 0; i < array.length; i++) {
            array[i] = jsonArray.getString(i);
        }
        return array;
    }

    
}
