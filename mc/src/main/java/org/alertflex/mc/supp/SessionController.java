/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alertflex.mc.supp;

import java.io.IOException;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.persistence.NoResultException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.context.ExternalContext;
import javax.servlet.http.HttpSession;
import org.alertflex.mc.db.Users;
import org.alertflex.mc.db.Project;
import org.alertflex.mc.services.UsersFacade;
import org.alertflex.mc.services.ProjectFacade;


/**
 *
 * @author oleg
 */
@ManagedBean(name = "sessionController")
@SessionScoped
public class SessionController implements Serializable {
    
    @EJB
    private UsersFacade usersFacade;
    
    @EJB
    private ProjectFacade projectFacade;
    
    @EJB
    private AuthenticationSingleton authSingleton;
    
    ActiveUser au;
    
    String password;
    
    Project p;
    
    /**
     * Creates a new instance of SessionController
     */
    public SessionController() {
        
    }
    
    @PostConstruct
    public void init() {
    
        au = new ActiveUser();
    }
    
    public String getUsername() {
        return au.username;
    }

    public void setUsername(String u) {
        au.username = u;
    }
    
    public String getPassword() {
        return password;
    }

    public void setPassword(String p) {
        password = p;
    }
    
    public void setAuthenticated(boolean a) {
        au.authenticated = a;
    }
    
    public boolean getAuthenticated() {
        return au.authenticated;
    }
    
    public HttpSession getSession() {
        return au.session;
    }
    
    public List<ActiveUser> getActiveUsersList() {
        
        return authSingleton.getActiveUsersList();
    }
    
    public String login() throws IOException {
        
        HttpServletRequest request = null;
        HttpServletResponse response = null;
        
        au.timeOfLogin = new Date();
        
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            request = (HttpServletRequest) context.getExternalContext().getRequest();
            request.login(au.username, password);
            
            response = (HttpServletResponse) context.getExternalContext().getResponse();
            String redirectURL = request.getContextPath();
            response.sendRedirect(redirectURL + "/user/alerts.xhtml");  
                        
            au.session = request.getSession(false);
            Users u = usersFacade.find(au.username);
            p = projectFacade.findProjectById(u.getRefId());
            
            au.setUser(u, p);
                            
            if (au.getUser() != null) {
                
                au.authenticated = true;  
                Date date = new Date();            
                au.setTimeOfLogin(date);
                authSingleton.addActiveUser(au);
                
                au.session.setMaxInactiveInterval(1800);
            }
            else {
                NoResultException e = new NoResultException();
                throw e;
            }
        } catch (NoResultException | ServletException ex) {
            
            if(request != null){
                try {
                    request.logout();
                } catch (ServletException ex1) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "AuthBean#login Error: " + ex, ""));
                }
            }
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid username/password", ""));
            return null;
        }  
        
        password = "";
        
        return "login";
    }
    
    public String logout() throws IOException {
        
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        context.invalidateSession();
        au.authenticated = false;
        return "logout";
    }
    
    @PreDestroy
    public void exit() {
        authSingleton.deleteActiveUser(au);
    }
    
}
