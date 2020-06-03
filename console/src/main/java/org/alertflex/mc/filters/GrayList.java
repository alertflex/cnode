/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alertflex.mc.filters;

/**
 *
 * @author root
 */

public class GrayList {
    
    public int id;
    
    public String event;
    public String host;
    public String match;
        
    public Aggregate agr;
    Response rsp;
       
    public GrayList () {
        
        host = "indef";
        match = "indef";
        
        agr = new Aggregate();
        rsp = new Response();
    }
    
    public void setId(int i) {
        this.id = i;
    }
    
    public int getId () {
        return id;
    }
    
    public void setEvent(String e) {
        this.event = e;
    }
    
    public String getEvent () {
        return event;
    }
    
    public void setHost(String h) {
        this.host = h;
    }
    
    public String getHost() {
        return host;
    }
    
    public void setMatch(String m) {
        this.match = m;
    }
    
    public String getMatch () {
        return match;
    }
    
    public void setAgr (Aggregate a) {
        this.agr = a;
    }
    
    public Aggregate getAgr () {
        return agr;
    }
    
    public void setRsp (Response r) {
        this.rsp = r;
    }
    
    public Response getRsp () {
        return rsp;
    }
}
