/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alertflex.mc.ui;

/**
 *
 * @author root
 */

import org.alertflex.mc.supp.AuthenticationSingleton;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.alertflex.mc.db.Alert;
import org.alertflex.mc.tasks.Monitor;
import org.alertflex.mc.services.AlertFacade;
import org.alertflex.mc.services.NodeFacade;
import org.primefaces.event.RowEditEvent;


@ManagedBean
@ViewScoped
public class AlertsNewController implements Serializable {
    
    @EJB
    public AlertFacade alertsFacade;
    
    @EJB
    private AuthenticationSingleton authSingleton;
    
    @EJB
    private Monitor monitorController;
    
    String session_tenant;
    String session_user;
    
    String[] statusArray = {"processed", "modified", "aggregated"};
        
    List<Alert> criticalAlertsList;
    
    List<Alert> majorAlertsList;
    
    List<Alert> minorAlertsList;
    
    List<Alert> normalAlertsList;
    
    /**
     * Creates a new instance of OssecController
     */
    public AlertsNewController() {
       
    }
    
    @PostConstruct
    public void init() {
        
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        HttpSession session = request.getSession(false);
        
        session_tenant = authSingleton.getTenantName(session);
        session_user = authSingleton.getUserName(session);
        
        criticalAlertsList = alertsFacade.findAlertsBySeverityStatus(session_tenant, 3, statusArray);
        if (criticalAlertsList == null) criticalAlertsList = new ArrayList<>();
        
        majorAlertsList = alertsFacade.findAlertsBySeverityStatus(session_tenant, 2, statusArray);
        if (majorAlertsList == null) majorAlertsList = new ArrayList<>();
        
        minorAlertsList = alertsFacade.findAlertsBySeverityStatus(session_tenant, 1, statusArray);
        if (minorAlertsList == null) minorAlertsList = new ArrayList<>();
        
        normalAlertsList = alertsFacade.findAlertsBySeverityStatus(session_tenant, 0, statusArray);
        if (normalAlertsList == null) normalAlertsList = new ArrayList<>();
        
        
    }
    
    public String getCriticalAlerts () {
        
        Long i = monitorController.getNumNewCriticalAlerts(session_tenant);
        
        return i.toString();
        
    }
    
    public String getMajorAlerts () {
        
        Long i = monitorController.getNumNewMajorAlerts(session_tenant);
        
        return i.toString();
        
    }
    
    public String getMinorAlerts () {
        
        Long i = monitorController.getNumNewMinorAlerts(session_tenant);
        
        return i.toString();
        
    }
    
    public String getNormalAlerts () {
        
        Long i = monitorController.getNumNewNormalAlerts(session_tenant);
        
        return i.toString();
        
    }
    
    public List<Alert> getCriticalAlertsList () {
        
        return criticalAlertsList;
    }
    
    public void  setCriticalAlertsList (List<Alert> al) {
        this.criticalAlertsList = al;
    }
    
    public List<Alert> getMajorAlertsList () {
        
        return majorAlertsList;
    }
    
    public void  setMajorAlertsList (List<Alert> al) {
        this.majorAlertsList =al;
    }
    
    public int  getCounterMajorAlerts () {
        int counter = 0;
        if (majorAlertsList != null) counter = majorAlertsList.size();
        return counter;
    }
    
    public List<Alert> getMinorAlertsList () {
        
        return minorAlertsList;
    }
    
    public void  setMinorAlertsList (List<Alert> al) {
        this.minorAlertsList =al;
    }
    
    public List<Alert> getNormalAlertsList () {
        
        return normalAlertsList;
    }
    
    public void  setNormalAlertsList (List<Alert> al) {
        this.normalAlertsList =al;
    }
    
    public List<String> changeStatus() {
        
        List<String> statusChangeList = new ArrayList<>();
        
        statusChangeList.add("confirmed");
        statusChangeList.add("incident"); 
        
        return statusChangeList;
    }
    
    public void onRowEdit(RowEditEvent event) {
               
        Alert a = null; 
        
        try { 
            a = (Alert) event.getObject();
            alertsFacade.edit(a);
            FacesMessage msg = new FacesMessage("Alert Edited", a.getAlertId().toString());
            FacesContext.getCurrentInstance().addMessage(null, msg);
            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest origRequest = (HttpServletRequest) context.getExternalContext().getRequest();
            String contextPath = origRequest.getContextPath();
            switch (a.getAlertSeverity()) {
                case 3 :
                    FacesContext.getCurrentInstance().getExternalContext().redirect(contextPath + "/user/alerts-critical.xhtml");
                    break;
                case 2 :
                    FacesContext.getCurrentInstance().getExternalContext().redirect(contextPath + "/user/alerts-major.xhtml");
                    break;
                case 1 :
                    FacesContext.getCurrentInstance().getExternalContext().redirect(contextPath + "/user/alerts-minor.xhtml");
                    break;
                case 0 :
                    FacesContext.getCurrentInstance().getExternalContext().redirect(contextPath + "/user/alerts-normal.xhtml");
                    break;
            }
            
        
        } catch (Exception ex) { 
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error !", ex.toString())); 
            FacesContext.getCurrentInstance().validationFailed(); 
        } 
    }
    
    public void confirmAllCriticalAlerts() {
       
        try { 
                        
            alertsFacade.confirmAllCriticalAlerts(session_tenant, statusArray);
                        
            FacesMessage msg = new FacesMessage("Alert Edited", "All alerts were confirmed");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest origRequest = (HttpServletRequest) context.getExternalContext().getRequest();
            String contextPath = origRequest.getContextPath();
            FacesContext.getCurrentInstance().getExternalContext().redirect(contextPath + "/user/alerts-critical.xhtml");
            
            
        } catch (Exception ex) { 
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error !", ex.toString())); 
            FacesContext.getCurrentInstance().validationFailed(); 
        } 
        
    }
    
    public void confirmAllMajorAlerts() {
       
        try { 
                        
            alertsFacade.confirmAllMajorAlerts(session_tenant, statusArray);
                        
            FacesMessage msg = new FacesMessage("Alert Edited", "All alerts were confirmed");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest origRequest = (HttpServletRequest) context.getExternalContext().getRequest();
            String contextPath = origRequest.getContextPath();
            FacesContext.getCurrentInstance().getExternalContext().redirect(contextPath + "/user/alerts-major.xhtml");
            
            
        } catch (Exception ex) { 
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error !", ex.toString())); 
            FacesContext.getCurrentInstance().validationFailed(); 
        } 
        
    }
    
    public void confirmAllMinorAlerts() {
       
        try { 
                        
            alertsFacade.confirmAllMinorAlerts(session_tenant, statusArray);
                        
            FacesMessage msg = new FacesMessage("Alert Edited", "All alerts were confirmed");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest origRequest = (HttpServletRequest) context.getExternalContext().getRequest();
            String contextPath = origRequest.getContextPath();
            FacesContext.getCurrentInstance().getExternalContext().redirect(contextPath + "/user/alerts-minor.xhtml");
            
            
        } catch (Exception ex) { 
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error !", ex.toString())); 
            FacesContext.getCurrentInstance().validationFailed(); 
        } 
        
    }
    
    public void confirmAllNormalAlerts() {
       
        try { 
                        
            alertsFacade.confirmAllNormalAlerts(session_tenant, statusArray);
                        
            FacesMessage msg = new FacesMessage("Alert Edited", "All alerts were confirmed");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest origRequest = (HttpServletRequest) context.getExternalContext().getRequest();
            String contextPath = origRequest.getContextPath();
            FacesContext.getCurrentInstance().getExternalContext().redirect(contextPath + "/user/alerts-normal.xhtml");
            
            
        } catch (Exception ex) { 
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error !", ex.toString())); 
            FacesContext.getCurrentInstance().validationFailed(); 
        } 
        
    }
    
    public void confirmShownCriticalAlerts() {
        
        try { 
                        
            for (Alert a: criticalAlertsList) {
                a.setStatus("confirmed");
                alertsFacade.edit(a);
            }
            
            FacesMessage msg = new FacesMessage("Alert Edited", "All shown alerts were confirmed");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest origRequest = (HttpServletRequest) context.getExternalContext().getRequest();
            String contextPath = origRequest.getContextPath();
            FacesContext.getCurrentInstance().getExternalContext().redirect(contextPath + "/user/new-alerts.xhtml");
            
            
        } catch (Exception ex) { 
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error !", ex.toString())); 
            FacesContext.getCurrentInstance().validationFailed(); 
        } 
        
    }
    
    public void confirmShownMajorAlerts() {
        
        try { 
                        
            for (Alert a: majorAlertsList) {
                a.setStatus("confirmed");
                alertsFacade.edit(a);
            }
            
            FacesMessage msg = new FacesMessage("Alert Edited", "All shown alerts were confirmed");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest origRequest = (HttpServletRequest) context.getExternalContext().getRequest();
            String contextPath = origRequest.getContextPath();
            FacesContext.getCurrentInstance().getExternalContext().redirect(contextPath + "/user/new-alerts.xhtml");
            
            
        } catch (Exception ex) { 
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error !", ex.toString())); 
            FacesContext.getCurrentInstance().validationFailed(); 
        } 
        
    }
    
    public void confirmShownMinorAlerts() {
        
        try { 
                        
            for (Alert a: minorAlertsList) {
                a.setStatus("confirmed");
                alertsFacade.edit(a);
            }
            
            FacesMessage msg = new FacesMessage("Alert Edited", "All shown alerts were confirmed");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest origRequest = (HttpServletRequest) context.getExternalContext().getRequest();
            String contextPath = origRequest.getContextPath();
            FacesContext.getCurrentInstance().getExternalContext().redirect(contextPath + "/user/new-alerts.xhtml");
            
            
        } catch (Exception ex) { 
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error !", ex.toString())); 
            FacesContext.getCurrentInstance().validationFailed(); 
        } 
        
    }
    
    public void confirmShownNormalAlerts() {
        
        try { 
                        
            for (Alert a: normalAlertsList) {
                a.setStatus("confirmed");
                alertsFacade.edit(a);
            }
            
            FacesMessage msg = new FacesMessage("Alert Edited", "All shown alerts were confirmed");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest origRequest = (HttpServletRequest) context.getExternalContext().getRequest();
            String contextPath = origRequest.getContextPath();
            FacesContext.getCurrentInstance().getExternalContext().redirect(contextPath + "/user/new-alerts.xhtml");
            
            
        } catch (Exception ex) { 
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error !", ex.toString())); 
            FacesContext.getCurrentInstance().validationFailed(); 
        } 
        
    }
}
