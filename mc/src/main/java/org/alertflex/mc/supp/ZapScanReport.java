/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alertflex.mc.supp;

import java.util.Date;

/**
 *
 * @author root
 */
public class ZapScanReport {
    
    String agent;
    Date timeScan;
    String addressScan;
    Integer pluginid;
    String alert;
    String name;
    Integer riskcode;
    Integer confidence;
    String riskdesc;
    String desc;
    String instances;
    Integer count;
    String solution;
    String otherinfo;
    String reference;
    Integer cweid;
    Integer wascid;       
    Integer sourceid;
    
    public void setAgent(String a) {
        this.agent = a;
    }
    
    public String getAgent() {
        return agent;
    }
    
    public void setTimeScan(Date ts) {
        this.timeScan = ts;
    }
    
    public Date getTimeScan() {
        return timeScan;
    }
    
    public void setAddressScan(String a) {
        this.addressScan = a;
    }
    
    public String getAddressScan() {
        return addressScan;
    }
    
    public void setPluginid(Integer p) {
        this.pluginid = p;
    }
    
    public Integer getPluginid() {
        return pluginid;
    }
    
    public void setAlert(String a) {
        this.alert = a;
    }
    
    public String getAlert() {
        return alert;
    }
    
    public void setName(String n) {
        this.name = n;
    }
    
    public String getName() {
        return name;
    }
    
    public void setRiskcode(Integer r) {
        this.riskcode = r;
    }
    
    public Integer getRiskcode() {
        return riskcode;
    }
    
    public void setConfidence(Integer c) {
        this.confidence = c;
    }
    
    public Integer getConfidence() {
        return confidence;
    }
    
    public void setRiskdesc(String r) {
        this.riskdesc = r;
    }
    
    public String getRiskdesc() {
        return riskdesc;
    }
    
    public void setDesc(String d) {
        this.desc = d;
    }
    
    public String getDesc() {
        return desc;
    }
    
    public void setInstances(String i) {
        this.instances = i;
    }
    
    public String getInstances() {
        return instances;
    }
    
    public void setCount(Integer c) {
        this.count = c;
    }
    
    public Integer getCount() {
        return count;
    }
    
    public void setSolution(String s) {
        this.solution = s;
    }
    
    public String getSolution() {
        return solution;
    }
    
    public void setOtherinfo(String o) {
        this.otherinfo = o;
    }
    
    public String getOtherinfo() {
        return otherinfo;
    }
    
    public void setReference(String r) {
        this.reference = r;
    }
    
    public String getReference() {
        return reference;
    }
    
    public void setCweid(Integer c) {
        this.cweid = c;
    }
    
    public Integer getCweid() {
        return cweid;
    }
    
    public void setWascid(Integer w) {
        this.wascid = w;
    }
    
    public Integer getWascid() {
        return wascid;
    }
    
    public void setSourceid(Integer s) {
        this.sourceid = s;
    }
    
    public Integer getSourceid() {
        return sourceid;
    }
}
