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
import org.alertflex.mc.db.DockerScan;
import org.alertflex.mc.services.AgentFacade;
import org.alertflex.mc.services.ProjectFacade;
import org.alertflex.mc.services.DockerScanFacade;
import org.alertflex.mc.services.NodeFacade;



@ManagedBean (name="dockerBenchController")
@ViewScoped
public class DockerBenchController implements Serializable {
    
    @EJB
    private AuthenticationSingleton authSingleton;
    
    String session_tenant;
    String session_user;
    
    @EJB
    private ProjectFacade prjFacade;
    private Project project;
    
    @EJB
    private DockerScanFacade dockerScanFacade;
    
     @EJB
    private NodeFacade nodeFacade;
    
    private String selectedNodeId;
    private List<String> listNodeId;
    
    private String selectedSensorName;
    private List<String> listSensorNames;
    
    List<DockerScan> listVulnerability;
        
                   
    public DockerBenchController() {
        
    }
    
    @PostConstruct
    public void init() {
        
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        HttpSession session = request.getSession(false);
        
        session_tenant = authSingleton.getTenantName(session);
        session_user = authSingleton.getUserName(session);
        
        
        listNodeId = nodeFacade.findAllNodeNames(session_tenant);
        
        if (listNodeId == null) listNodeId = new ArrayList();
        
        listSensorNames = new ArrayList();
        
        listVulnerability = new ArrayList();
        
    }
    
    public String getSelectedSensorName() {
        return selectedSensorName;
    }
 
    public void setSelectedSensorName(String sn) {
        this.selectedSensorName = sn;
    }
    
    public void updateListSensorNames() {
        
        listSensorNames = null;
        
        if (selectedNodeId != null) listSensorNames = dockerScanFacade.findSensorNamesByNode(session_tenant, selectedNodeId);
            
        if (listSensorNames == null) listSensorNames = new ArrayList();
        
    }
    
    public List<String> getListSensorNames() {
        
        return listSensorNames;
    }
    
    public String getSelectedNodeId() {
        return selectedNodeId;
    }
 
    public void setSelectedNodeId(String p) {
        this.selectedNodeId = p;
    }
    
    public List<String> getListNodeId() {
        return listNodeId;
    }
    
    public List<DockerScan> getListVulnerability() {
        return listVulnerability;
    }
    
    public void setListVulnerability(List<DockerScan> dsl) {
        this.listVulnerability = dsl;
        
    }
    
       
    public void updateVulList() {
        
        if (!selectedNodeId.isEmpty()) {
            
            listVulnerability = dockerScanFacade.findRecord(session_tenant, selectedNodeId, selectedSensorName);
            
            if (listVulnerability == null) listVulnerability = new ArrayList();
                    
        }
        
    }
    
    public void deleteRecord(DockerScan ds) {
        
        if(ds != null) {
            
            try {
                
                String resultId = ds.getResultId();
                
                dockerScanFacade.remove(ds);
                
                updateVulList();
                
                FacesMessage msg = new FacesMessage("Item has been removed", resultId );
                FacesContext.getCurrentInstance().addMessage(null, msg);
                FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
            } catch (Exception ex) { 
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error !", ex.toString())); 
                FacesContext.getCurrentInstance().validationFailed(); 
            }
        }
        
    }
} 