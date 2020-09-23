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
import org.alertflex.mc.db.ScriptJob;
import org.alertflex.mc.services.ProjectFacade;
import org.alertflex.mc.services.HostsFacade;
import org.alertflex.mc.services.ScriptJobFacade;



@ManagedBean (name="scriptJobController")
@ViewScoped
public class ScriptJobController implements Serializable {
    
    @EJB
    private AuthenticationSingleton authSingleton;
    
    String session_tenant;
    String session_user;
    
    @EJB
    private ProjectFacade prjFacade;
    private Project project;
       
    @EJB
    private ScriptJobFacade scriptJobFacade; 
    private String selectedScriptJob = "";
    private List<String> listScriptJob;
    
    @EJB
    private HostsFacade hostsFacade;
    private String selectedHostname = "";
    private List<String> listHostName;
    
    String desc = "";
    String scriptName = "";
    String scriptLocal = "";
    String scriptParams = "";
    
    Integer waitResult = 0;
    
    int timerange = 0;
    
    public ScriptJobController() {
        
    }
    
    @PostConstruct
    public void init() {
        
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        HttpSession session = request.getSession(false);
        
        session_tenant = authSingleton.getTenantName(session);
        session_user = authSingleton.getUserName(session);
        
        project = prjFacade.find(session_tenant);
        
        listHostName = hostsFacade.findHostsNamesByRef(session_tenant);
        if(listHostName == null) listHostName = new ArrayList();
        
        listHostName.add("indef");
        
        listScriptJob = scriptJobFacade.findJobNamesByRef(session_tenant);
        if(listScriptJob == null) listScriptJob = new ArrayList();
        
        String paramJob = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("job");
        
        if (paramJob != null && !paramJob.isEmpty()) {
            
            selectedScriptJob = paramJob;
            updateParameters();
        }
        
    }
    
    public String getSelectedHostname() {
        return selectedHostname;
    }
 
    public void setSelectedHostname(String h) {
        
        if (h != null)
            this.selectedHostname = h;
        
    }
    
    public List<String> getListHostName() {
        return listHostName;
    }
    
    public void setListHostName(List<String> lh) {
        
        this.listHostName = lh;
    }
    
    public String getSelectedScriptJob() {
        return selectedScriptJob;
    }
 
    public void setSelectedScriptJob(String sj) {
        this.selectedScriptJob = sj;
        
    }
    
    public List<String> getListScriptJob() {
        return listScriptJob;
    }
    
    public void setListScriptJob(List<String> lsj) {
        this.listScriptJob = lsj;
        
    }
    
    public String getDesc() {
        return desc;
    }
     
    public void setDesc(String d) {
        this.desc = d;
    }
    
    public void setScriptParams(String cp) {
       this.scriptParams = cp;
    }
    
    public String getScriptParams () {
       return scriptParams;
    }  
    
    public String getScriptName() {
        return scriptName;
    }
 
    public void setScriptName(String s) {
        this.scriptName = s;
    }
    
    public String getScriptLocal() {
        return scriptLocal;
    }
 
    public void setScriptLocal(String s) {
        this.scriptLocal = s;
    }
    
       
    public Integer getTimerange() {
        return timerange;
    }
 
    public void setTimerange(Integer t) {
        this.timerange = t;
        
    }
    
    public Integer getWaitResult() {
        return waitResult;
    }
 
    public void setWaitResult(Integer wr) {
        this.waitResult = wr;
        
    }
    
    public void updateParameters() {
        
        if (!selectedScriptJob.isEmpty()) {
            
            ScriptJob sj = scriptJobFacade.findJobByName(session_tenant, selectedScriptJob);
            
            if (sj != null ) {
            
                selectedHostname = sj.getHost();
                                
                desc= sj.getDescription();
               
                scriptParams = sj.getScriptParams();
                
                scriptName = sj.getScriptName();
                scriptLocal = sj.getScript();
                
                timerange = sj.getTimerange();
                
                waitResult = sj.getWaitResult();
                
            }
        }
    }
    
    public void deleteJob() {
        FacesMessage message;
        
        if(selectedScriptJob == null || selectedScriptJob.isEmpty()) return;
        
        ScriptJob sj = scriptJobFacade.findJobByName(session_tenant, selectedScriptJob);
        
        if (sj != null ) {
            
            try { 
                
                scriptJobFacade.remove(sj);
                                            
                message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Action result: ","Script job has been deleted.");
            } catch (Exception ex) {
                message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error !", ex.toString());
            }
                
        }
        else message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error !", "problem with name of Script job");
            
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
    
    public void editJob() {
        
        FacesMessage message;
        
        if(selectedScriptJob != null && !selectedScriptJob.isEmpty()) {
        
            ScriptJob sj = scriptJobFacade.findJobByName(session_tenant, selectedScriptJob);
            
            if (sj != null ) {
                
                if (!selectedScriptJob.isEmpty()) {
            
                    sj.setName(selectedScriptJob);
                    sj.setRefId(session_tenant);
                    sj.setDescription(desc);
                
                    sj.setHost(selectedHostname);
                
                    sj.setScriptParams(scriptParams);
                
                    sj.setScriptName(scriptName);
                    sj.setScript(scriptLocal);
                    
                    sj.setTimerange(timerange);
                    sj.setWaitResult(waitResult);
                    
                } else {
                
                    message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error !", "problem with parameters of Script job");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                    return;
                }  
                
                try { 
                    
                    scriptJobFacade.edit(sj);
                                                
                    message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Action result: ","Script job has been updated.");
                } catch (Exception ex) {
                    message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error !", ex.toString());
                }
                    
                FacesContext.getCurrentInstance().addMessage(null, message);
                return;
            }
        }
        
        message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error !", "problem with parameters for Script job");
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
    
    public void createJob() {
        
        FacesMessage message = null;
        
        ScriptJob sj = scriptJobFacade.findJobByName(session_tenant, selectedScriptJob);
        
        if (sj == null) {
            
            sj = new ScriptJob();
            
            if (!selectedScriptJob.isEmpty()) {
            
                sj.setName(selectedScriptJob);
                sj.setRefId(session_tenant);
                sj.setDescription(desc);
                sj.setPlaybook("indef");
                sj.setHost(selectedHostname);
                
                sj.setScriptParams(scriptParams);
                
                sj.setScriptName(scriptName);
                sj.setScript(scriptLocal);
                sj.setTimerange(timerange);
                sj.setWaitResult(waitResult);
                    
            } else {
                
                message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error !", "problem with parameters of Script job");
                FacesContext.getCurrentInstance().addMessage(null, message);
                return;
            }  
                
            try { 
                    
                scriptJobFacade.create(sj);
                                            
                message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Action result: ","Script job has been created.");
                    
            } catch (Exception ex) {
                message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error !", ex.toString());
            }
                
            FacesContext.getCurrentInstance().addMessage(null, message);
            return;
        } 
        
        message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error !", "problem with parameters of Script job");
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
} 