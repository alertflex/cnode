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
import org.alertflex.mc.db.AgentVul;
import org.alertflex.mc.services.AgentFacade;
import org.alertflex.mc.services.AgentVulFacade;


@ManagedBean
@ViewScoped
public class AgentVulController implements Serializable {
    
    @EJB
    private AuthenticationSingleton authSingleton;
    
    String session_tenant;
    String session_user;
    
    @EJB
    private AgentVulFacade agentVulFacade;
    
    private List<AgentVul> listAgentVul;
    
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
        
        listAgentVul = new ArrayList();
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
        
        listAgentVul = agentVulFacade.findVulnerability(session_tenant, selectedNodeId, selectedAgentName);
        
        if (listAgentVul == null) listAgentVul = new ArrayList();
        
    }

    public List<AgentVul> getListAgentVul() {
        
        return listAgentVul;
    }
    
    public void getListAgentVul(List<AgentVul> lv) {
        
        this.listAgentVul = lv;
    }
    
    public void deleteRecord(AgentVul v) {
        
        if(v != null) {
            
            try {
                
                String cve = v.getCve();
                
                agentVulFacade.remove(v);
                
                updateList();
                
                FacesMessage msg = new FacesMessage("Item has been removed", cve );
                FacesContext.getCurrentInstance().addMessage(null, msg);
                FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
            } catch (Exception ex) { 
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error !", ex.toString())); 
                FacesContext.getCurrentInstance().validationFailed(); 
            }
        }
    }
} 