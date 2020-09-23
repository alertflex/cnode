/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alertflex.mc.supp;

import org.alertflex.mc.db.SandboxJob;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author root
 */
public class SandboxFile {
    
    SandboxJob sandboxJob;
    String fileName = "";
    String jsonData = "";
    String state = "";   
    String src_ip = "indef";
    String dest_ip = "indef";
    int src_port = 0;
    int dest_port = 0;
    
    
    public SandboxFile(SandboxJob s, String f, String jd) {
        
        sandboxJob = s;
        
        fileName = f;
        
        jsonData = jd;
       
    }
    
    public SandboxJob getSandboxJob() {
        return sandboxJob;
    }
    
    public void setSandboxJob(SandboxJob s) {
        this.sandboxJob = s;
    }
    
    public String getFileName () {
        return fileName;
    }
    
    public void setFileName(String fn) {
        this.fileName = fn;
    }
    
    public String getJsonData() {
        return jsonData;
    }
    
    public String getSrcIp() {
        return src_ip;
    }
    
    public String getDestIp() {
        return dest_ip;
    }
    
    public Integer getSrcPort() {
        return src_port;
    }
    
    public Integer getDestPort() {
        return dest_port;
    }
    
    public Boolean getState() {
        
        if (jsonData.isEmpty()) return false;
        
        try {
            
            JSONObject obj = new JSONObject(jsonData);
            
            src_ip = obj.getString("src_ip");
            
            dest_ip = obj.getString("dest_ip");
            
            src_port = obj.getInt("src_port");
            
            dest_port = obj.getInt("dest_port");
        
            JSONObject fileInfo = obj.getJSONObject("fileinfo");
        
            String sha256 = fileInfo.getString("sha256");
            
            state = fileInfo.getString("state");
            
        } catch ( JSONException e) {
            
            return false;
        } 
        
        switch (state) {
            case "TRUNCATED":
                return true;
            case "CLOSED" :
                return true;
            default:
                break;
        }
        
        return false;
    }
}
