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
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import org.alertflex.mc.services.NodeFacade;
import java.util.ArrayList;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.alertflex.mc.db.AgentPackages;
import org.alertflex.mc.services.AgentFacade;
import org.alertflex.mc.services.AgentPackagesFacade;


@ManagedBean
@ViewScoped
public class AgentPackagesController implements Serializable {
    
    @EJB
    private AuthenticationSingleton authSingleton;
    
    String session_tenant;
    String session_user;
    
    @EJB
    private AgentPackagesFacade agentPackagesFacade;
    
    private List<AgentPackages> listAgentPackages;
    
    @EJB
    private NodeFacade nodeFacade;
    
    private String selectedNodeId;
    private List<String> listNodeId;
    
    @EJB
    private AgentFacade agentFacade;
    
    private String selectedAgentName;
    private List<String> listAgentNames;
    
    
    @PostConstruct
    public void init() {
        
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        HttpSession session = request.getSession(false);
        
        session_tenant = authSingleton.getTenantName(session);
        session_user = authSingleton.getUserName(session);
        
        listNodeId = nodeFacade.findAllNodeNames(session_tenant);
        
        if (listNodeId == null) listNodeId = new ArrayList();
        
        listAgentNames = new ArrayList();
        
        listAgentPackages = new ArrayList();
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
    
    public String getSelectedAgentName() {
        return selectedAgentName;
    }
 
    public void setSelectedAgentName(String an) {
        this.selectedAgentName = an;
    }
    
    public void updateListAgentNames() {
        
        listAgentNames = null;
        
        if (selectedNodeId != null) listAgentNames = agentFacade.findAgentNamesByNode(session_tenant, selectedNodeId);
            
        if (listAgentNames == null) listAgentNames = new ArrayList();
        
    }
    
    public List<String> getListAgentNames() {
        
        return listAgentNames;
    }
    
    public void updateList() {
        
         String agentId = agentFacade.findIdByName(session_tenant, selectedNodeId, selectedAgentName);
        
        if (agentId.isEmpty()) return;
        
        listAgentPackages = agentPackagesFacade.findPackages(session_tenant, selectedNodeId, agentId);
        
        if (listAgentPackages == null) listAgentPackages = new ArrayList();
        
    }

    public List<AgentPackages> getListAgentPackages() {
        
        return listAgentPackages;
    }
    
    public void getListAgentPackages(List<AgentPackages> lp) {
        
        this.listAgentPackages = lp;
    }
    
    public void deleteRecord(AgentPackages p) {
        
        if(p != null) {
            
            try {
                
                String packageId = p.getName();
                
                agentPackagesFacade.remove(p);
                
                updateList();
            
                FacesMessage msg = new FacesMessage("Item has been removed", packageId );
                FacesContext.getCurrentInstance().addMessage(null, msg);
                FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
            } catch (Exception ex) { 
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error !", ex.toString())); 
                FacesContext.getCurrentInstance().validationFailed(); 
            }
        }
    }
} 