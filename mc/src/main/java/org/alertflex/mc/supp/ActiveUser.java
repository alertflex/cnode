/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alertflex.mc.supp;

import java.util.Date;
import javax.servlet.http.HttpSession;
import org.alertflex.mc.db.Users;
import org.alertflex.mc.db.Project;

/**
 *
 * @author root
 */
public class ActiveUser {
    
    public String username;
    public String tenantname;
    private Users user;
    private Project project;
    public boolean authenticated = false;
    public HttpSession session = null;
    public Date timeOfLogin;
    
    public ActiveUser() {
        authenticated = false;
        HttpSession session = null;
    }
    
    public Users getUser() {
        return user;
    }
    
    public void setUser(Users u, Project p) {
        this.user = u;
        this.project = p;
        this.tenantname = u.getRefId();
    }
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String u) {
        this.username = u;
    }
    
    public void setTenantname(String t) {
        this.tenantname = t;
    }
    
    public String getTenantname() {
        return tenantname;
    }

    public void setSession(HttpSession s) {
        this.session = s;
    }
    
    public HttpSession getSession() {
        return session;
    }
    
    public void setAuthenticated(boolean a) {
        this.authenticated = a;
    }
    
    public boolean getAuthenticated() {
        return authenticated;
    }
    
    public void setTimeOfLogin(Date t) {
        this.timeOfLogin = t;
    }
    
    public Date getTimeOfLogin() {
        return timeOfLogin;
    }
}
