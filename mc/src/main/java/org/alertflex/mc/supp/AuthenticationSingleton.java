
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alertflex.mc.supp;

/**
 *
 * @author root
 */

import java.util.List;
import java.util.ArrayList;
import javax.annotation.PostConstruct;
import javax.ejb.AccessTimeout;
import javax.ejb.ConcurrencyManagement;
import static javax.ejb.ConcurrencyManagementType.CONTAINER;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.servlet.http.HttpSession;
import org.alertflex.mc.supp.ActiveUser;


/**
 *
 * @author 
 */

@Singleton (name="authenticationSingleton")
@ConcurrencyManagement(CONTAINER)
@Startup
public class AuthenticationSingleton  {

    List<ActiveUser> activeUsersList;
    
    /**
     * Creates a new instance of AuthenticationController
     */
    public AuthenticationSingleton() {
        
    }
    
    @PostConstruct
    public void init() {
        
        activeUsersList = new ArrayList<>();
    }
    
    @Lock(LockType.READ)
    public List<ActiveUser> getActiveUsersList() {
        
        return activeUsersList;
    }
    
    @Lock(LockType.WRITE)
    @AccessTimeout(value=500)
    public void addActiveUser(ActiveUser u) {
        activeUsersList.add(u);
    }
    
    @Lock(LockType.WRITE)
    @AccessTimeout(value=500)
    public void deleteActiveUser(ActiveUser u) {
        activeUsersList.remove(u);
    }
    
    @Lock(LockType.READ)
    public ActiveUser getActiveUser(HttpSession s) {
        
        for (ActiveUser au : activeUsersList) {
            if(au.getSession() == s) return au;   
        }
        return null;
    }
    
    @Lock(LockType.READ)
    public String getUserName(HttpSession s) {
        
        for (ActiveUser au : activeUsersList) {
            if(au.getSession() == s) return au.getUsername();   
        }
        return null;
    }
    
    @Lock(LockType.READ)
    public String getTenantName(HttpSession s) {
        
        for (ActiveUser au : activeUsersList) {
            if(au.getSession() == s) return au.getTenantname();   
        }
        return null;
    }
}