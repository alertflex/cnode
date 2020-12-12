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
public class IdsPolicy {

    public Severity severity;
    public boolean log;

    public List<GrayList> gl;

    public IdsPolicy() {
        severity = new Severity();
        log = true;
        gl = new ArrayList<>();
    }

    public List<GrayList> getGl() {
        return gl;
    }

    public void setGl(List<GrayList> l) {
        this.gl = l;
    }

    public Severity getSeverity() {
        return severity;
    }

    public void setSeverity(Severity s) {
        this.severity = s;
    }

    public void setLog(boolean l) {
        this.log = l;
    }

    public boolean getLog() {
        return log;
    }
}
