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
import org.alertflex.mc.db.AgentProcesses;
import org.alertflex.mc.services.AgentFacade;
import org.alertflex.mc.services.AgentProcessesFacade;


@ManagedBean
@ViewScoped
public class AgentProcessesController implements Serializable {
    
    @EJB
    private AuthenticationSingleton authSingleton;
    
    String session_tenant;
    String session_user;
    
    @EJB
    private AgentFacade agentFacade;
    
    @EJB
    private AgentProcessesFacade agentProcessesFacade;
    
    private List<AgentProcesses> listAgentProcesses;
    
    @EJB
    private NodeFacade nodeFacade;
    
    private String selectedNodeId;
    private List<String> listNodeId;
    
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
        
        listAgentProcesses = new ArrayList();
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
        
        listAgentProcesses = agentProcessesFacade.findProcesses(session_tenant, selectedNodeId, agentId);
        
        if (listAgentProcesses == null) listAgentProcesses = new ArrayList();
        
    }

    public List<AgentProcesses> getListAgentProcesses() {
        
        return listAgentProcesses;
    }
    
    public void getListAgentProcesses(List<AgentProcesses> lp) {
        
        this.listAgentProcesses = lp;
    }
    
    public void deleteRecord(AgentProcesses p) {
        
        if(p != null) {
            
            try {
                
                String processId = p.getPid();
                
                agentProcessesFacade.remove(p);
                
                updateList();
            
                FacesMessage msg = new FacesMessage("Item has been removed", processId );
                FacesContext.getCurrentInstance().addMessage(null, msg);
                FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
            } catch (Exception ex) { 
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error !", ex.toString())); 
                FacesContext.getCurrentInstance().validationFailed(); 
            }
        }
    }
} 