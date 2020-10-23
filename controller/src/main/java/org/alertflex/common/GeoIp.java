/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alertflex.common;

/**
 *
 * @author root
 */
public class GeoIp {
    
    String ip;
    String cc;
    float lat;
    float lon;
    
    public GeoIp (String ip) {
        this.ip = ip;
        this.cc = "indef";
        this.lat = 0;
        this.lon = 0;
    }
    
    public GeoIp (String ip, String cc, float lat, float lon) {
        this.ip = ip;
        this.cc = cc;
        this.lat = lat;
        this.lon = lon;
    }
    
    public String getIp() {
        return ip;
    }
    
    public void setIp(String ip) {
        this.ip = ip;
    }
    
    public String getCc() {
        return cc;
    }
    
    public void setCc(String cc) {
        this.cc = cc;
    }
    
    public Float getLat() {
        return lat;
    }
    
    public void setLat(Float lat) {
        this.lat = lat;
    }
    
    public Float getLon() {
        return lon;
    }
    
    public void setLon(Float lon) {
        this.lon = lon;
    }
    
    public String getSrcIp() {
        String srcip = "{ \"ip\":\""
            + ip
            + "\",\"country_code\":\""
            + cc
            + "\",\"latitude\":"
            + lat
            + ",\"longitude\":"
            + lon
            + "}";
        
        return srcip;
    }
    
    public String getDstIp() {
        String dstip = "{ \"ip\":\""
            + ip
            + "\",\"country_code\":\""
            + cc
            + "\",\"latitude\":"
            + lat
            + ",\"longitude\":"
            + lon
            + "}";
        
        return dstip;
    }
}
