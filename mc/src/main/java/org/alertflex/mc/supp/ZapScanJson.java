/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alertflex.mc.supp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;

import org.json.JSONObject;

/**
 *
 * @author root
 */
public class ZapScanJson {
    
    public List<ZapScanReport> getResult(String agent, String report) {
        
        JSONObject obj = new JSONObject(report);
        
        List<ZapScanReport> zsrList = new ArrayList<>();
        
        try {
        
            JSONArray array_site = obj.getJSONArray("site");
            
            Date date = new Date();
            
            for (int j = 0; j < array_site.length(); j++) {
                
                String addressScan = array_site.getJSONObject(j).getString("@name");
        
                JSONArray array_alerts = array_site.getJSONObject(j).getJSONArray("alerts");
        
                for (int i = 0; i < array_alerts.length(); i++) {
        
                    ZapScanReport zsr = new ZapScanReport();
            
                    zsr.setAgent(agent);
                    zsr.setTimeScan(date);
                    zsr.setAddressScan(addressScan);
                
                    String pluginid = array_alerts.getJSONObject(i).getString("pluginid");
                    zsr.setPluginid(Integer.parseInt(pluginid));
                
                    zsr.setAlert(array_alerts.getJSONObject(i).getString("alert"));
                    zsr.setName(array_alerts.getJSONObject(i).getString("name"));
                
                    String riskcode = array_alerts.getJSONObject(i).getString("riskcode");
                    zsr.setRiskcode(Integer.parseInt(riskcode));
                
                    String confidence = array_alerts.getJSONObject(i).getString("confidence");
                    zsr.setConfidence(Integer.parseInt(confidence));
                
                    zsr.setRiskdesc(array_alerts.getJSONObject(i).getString("riskdesc"));
                    zsr.setDesc(array_alerts.getJSONObject(i).getString("desc"));
                    zsr.setInstances(array_alerts.getJSONObject(i).getJSONArray("instances").toString());
                
                    String count = array_alerts.getJSONObject(i).getString("count");
                    zsr.setCount(Integer.parseInt(count));
                
                    zsr.setSolution(array_alerts.getJSONObject(i).getString("solution"));
                    zsr.setReference(array_alerts.getJSONObject(i).getString("reference"));
                
                    String cweid = array_alerts.getJSONObject(i).getString("cweid");
                    zsr.setCweid(Integer.parseInt(cweid));
                
                    String wascid = array_alerts.getJSONObject(i).getString("wascid");
                    zsr.setWascid(Integer.parseInt(wascid));
                
                    String sourceid = array_alerts.getJSONObject(i).getString("sourceid");
                    zsr.setSourceid(Integer.parseInt(sourceid));
                
                    zsrList.add(zsr);
                }
            }
            
        } catch ( JSONException e) {
            
            return null;
        } 
        
        return zsrList;
    }
}
