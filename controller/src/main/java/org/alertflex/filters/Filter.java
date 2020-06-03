/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alertflex.filters;

import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author root
 */
public class Filter {
    public String refId;
    public String desc;
    
    public IdsPolicy crs;
       
    public IdsPolicy nids;
    
    public IdsPolicy hids;
    
    public IdsPolicy waf;
    
    public Filter () {
        refId = "";
        desc = "";
        nids = new IdsPolicy();
        hids = new IdsPolicy();
        waf = new IdsPolicy();
    }
    
    public String getRefId() {
        return refId;
    }
    
    public void setRefId(String ri) {
        this.refId = ri;
    }
    
    public String getDesc() {
        return desc;
    }
    
    public void setDesc(String n) {
        this.desc = n;
    }
    
    public IdsPolicy getNids() {
        return nids;
    }
    
    public void setNids(IdsPolicy n) {
        this.nids = n;
    }
    
    public IdsPolicy getHids() {
        return hids;
    }
    
    public void setHids(IdsPolicy h) {
        this.hids = h;
    }
    
    public IdsPolicy getWaf() {
        return waf;
    }
    
    public void setWaf(IdsPolicy w) {
        this.waf = w;
    }
    
    public IdsPolicy getCrs() {
        return crs;
    }
    
    public void setCrs(IdsPolicy c) {
        this.crs = c;
    }
}
