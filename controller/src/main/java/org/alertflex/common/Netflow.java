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
public class Netflow {
    
    String refId;
    String node;
    String hostname;
    String timestamp;
    String protocol;
    String processName;
    int processId;
    String processPath;
    String dstIp;
    String dstHostname;
    int dstPort;
    String srcIp;
    String srcHostname;
    int srcPort;
    int packets;
    int bytes;
    
    
    public Netflow (String ref, String nod, String host, String time, String prot, String proc, int pi, 
            String ph, String di, String dh, int dp, String si, String sh, int sp, int pack, int byt) {
        this.refId = ref;
        this.node = nod;
        this.hostname = host;
        this.timestamp = time;
        this.protocol = prot;
        this.processName = proc;
        this.processId = pi;
        this.processPath = ph;
        this.dstIp = di;
        this.dstHostname= dh;
        this.dstPort = dp;
        this.srcIp = si;
        this.srcHostname = sh;
        this.dstPort = sp;
        this.packets = pack;
        this.bytes = byt;
    }
    
    public String getRefId() {
        return refId;
    }
    
    public void setRefId(String r) {
        this.refId = r;
    }
    
    public String getNode() {
        return node;
    }
    
    public void setNode(String n) {
        this.node = n;
    }
    
    public String getHostname() {
        return hostname;
    }
    
    public void setHostname(String h) {
        this.hostname = h;
    }
    
    public String getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(String t) {
        this.timestamp = t;
    }
    
    public String getProtocol() {
        return protocol;
    }
    
    public void setProtocol(String p) {
        this.protocol = p;
    }
    
    public String getProcessName() {
        return processName;
    }
    
    public void setProcessName(String p) {
        this.processName = p;
    }
    
    public int getProcessId() {
        return processId;
    }
    
    public void setProcessId(int p) {
        this.processId = p;
    }
    
    public String getProcessPath() {
        return processPath;
    }
    
    public void setProcessPath(String p) {
        this.processPath = p;
    }
    
    public String getDstIp() {
        return dstIp;
    }
    
    public void setDstIp(String ip) {
        this.dstIp = ip;
    }
    
    public String getDstHostname() {
        return dstHostname;
    }
    
    public void setDstHostname(String h) {
        this.dstHostname = h;
    }
    
    public int getDstPort() {
        return dstPort;
    }
    
    public void setDstPort(int p) {
        this.dstPort = p;
    }
    
    public String getSrcIp() {
        return srcIp;
    }
    
    public void setSrcIp(String ip) {
        this.srcIp = ip;
    }
    
    public String getSrcHostname() {
        return srcHostname;
    }
    
    public void setSrcHostname(String h) {
        this.srcHostname = h;
    }
    
    public int getSrcPort() {
        return srcPort;
    }
    
    public void setSrcPort(int p) {
        this.srcPort = p;
    }
    
    public int getPackets() {
        return packets;
    }
    
    public void setPackets(int p) {
        this.packets = p;
    }
    
    public int getBytes() {
        return bytes;
    }
    
    public void setBytes(int b) {
        this.bytes = b;
    }
}
