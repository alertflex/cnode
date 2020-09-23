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
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable; 
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.alertflex.mc.db.Project;
import org.alertflex.mc.db.SonarScan;
import org.alertflex.mc.services.ProjectFacade;
import org.alertflex.mc.services.SonarScanFacade;



@ManagedBean (name="sonarQubeController")
@ViewScoped
public class SonarQubeController implements Serializable {
    
    @EJB
    private AuthenticationSingleton authSingleton;
    
    String session_tenant;
    String session_user;
    
    @EJB
    private ProjectFacade prjFacade;
    private Project project;
    
    @EJB
    private SonarScanFacade sonarScanFacade;
    
    
    private String selectedProjectKey = "";
    private List<String> listProjects;
    
    List<SonarScan> listVulnerability;
        
                   
    public SonarQubeController() {
        
    }
    
    @PostConstruct
    public void init() {
        
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        HttpSession session = request.getSession(false);
        
        session_tenant = authSingleton.getTenantName(session);
        session_user = authSingleton.getUserName(session);
        
        listProjects = sonarScanFacade.findProjects(session_tenant);
        if(listProjects == null) listProjects = new ArrayList();
        
        listVulnerability = new ArrayList();
        
    }
    
    public String getSelectedProjectKey() {
        
        return selectedProjectKey;
    }
 
    public void setSelectedProjectKey(String p) {
        
        this.selectedProjectKey = p;
        
    }
    
    public List<String> getListProjects() {
        return listProjects;
    }
    
    public void setListProjects(List<String> lp) {
        this.listProjects = lp;
        
    }
    
    public List<SonarScan> getListVulnerability() {
        return listVulnerability;
    }
    
    public void setListVulnerability(List<SonarScan> lv) {
        this.listVulnerability = lv;
        
    }
    
       
    public void updateVulList() {
        
        if (!selectedProjectKey.isEmpty()) {
            
            listVulnerability = sonarScanFacade.findRecords(session_tenant, selectedProjectKey);
            
            if (listVulnerability == null) listVulnerability = new ArrayList();
                    
        }
        
    }
    
    
    public void deleteRecord(SonarScan ss) {
        
        if(ss != null) {
            
            try {
                
                String issueKey = ss.getIssueKey();
                
                sonarScanFacade.remove(ss);
                
                updateVulList();
                
                FacesMessage msg = new FacesMessage("Item has been removed", issueKey);
                FacesContext.getCurrentInstance().addMessage(null, msg);
                FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
            } catch (Exception ex) { 
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error !", ex.toString())); 
                FacesContext.getCurrentInstance().validationFailed(); 
            }
        }
        
    }
} 