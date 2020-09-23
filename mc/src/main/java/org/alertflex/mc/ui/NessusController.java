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
import org.alertflex.mc.db.NessusScan;
import org.alertflex.mc.db.Project;
import org.alertflex.mc.services.NessusScanFacade;
import org.alertflex.mc.services.ProjectFacade;
import org.alertflex.mc.services.SonarScanFacade;



@ManagedBean (name="nessusController")
@ViewScoped
public class NessusController implements Serializable {
    
    @EJB
    private AuthenticationSingleton authSingleton;
    
    String session_tenant;
    String session_user;
    
    @EJB
    private ProjectFacade prjFacade;
    private Project project;
    
    @EJB
    private NessusScanFacade nessusScanFacade;
    
    
    private String selectedScan = "";
    private List<String> listScans;
    
    List<NessusScan> listVulnerability;
        
                   
    public NessusController() {
        
    }
    
    @PostConstruct
    public void init() {
        
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        HttpSession session = request.getSession(false);
        
        session_tenant = authSingleton.getTenantName(session);
        session_user = authSingleton.getUserName(session);
        
        listScans = nessusScanFacade.findScanNames(session_tenant);
        if(listScans == null) listScans = new ArrayList();
        
        listVulnerability = new ArrayList();
        
    }
    
    public String getSelectedScan() {
        
        return selectedScan;
    }
 
    public void setSelectedScan(String s) {
        
        this.selectedScan = s;
        
    }
    
    public List<String> getListScans() {
        return listScans;
    }
    
    public void setListScans(List<String> ls) {
        this.listScans = ls;
        
    }
    
    public List<NessusScan> getListVulnerability() {
        return listVulnerability;
    }
    
    public void setListVulnerability(List<NessusScan> lv) {
        this.listVulnerability = lv;
        
    }
    
       
    public void updateVulList() {
        
        if (!selectedScan.isEmpty()) {
            
            listVulnerability = nessusScanFacade.findRecordsByScan(session_tenant, selectedScan);
            
            if (listVulnerability == null) listVulnerability = new ArrayList();
                    
        }
        
    }
    
    
    public void deleteRecord(NessusScan ns) {
        
        if(ns != null) {
            
            try {
                
                String plugName = ns.getPluginName();
                
                nessusScanFacade.remove(ns);
                
                updateVulList();
                
                FacesMessage msg = new FacesMessage("Item has been removed", plugName );
                FacesContext.getCurrentInstance().addMessage(null, msg);
                FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
            } catch (Exception ex) { 
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error !", ex.toString())); 
                FacesContext.getCurrentInstance().validationFailed(); 
            }
        }
        
    }
} 