/*
 * Alertflex Management Console
 *
 * Copyright (C) 2021 Oleg Zharkov
 *
 */

package org.alertflex.common;

public class NmapScanReport {

    String protocol;
    String portid;
    String state;
    String name;

    public void setProtocol(String p) {
        this.protocol = p;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setPortid(String p) {
        this.portid = p;
    }

    public String getPortid() {
        return portid;
    }

    public void setState(String s) {
        this.state = s;
    }

    public String getState() {
        return state;
    }

    public void setName(String n) {
        this.name = n;
    }

    public String getName() {
        return name;
    }
}
