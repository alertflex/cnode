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
import org.alertflex.mc.db.SandboxJob;
import org.alertflex.mc.db.Project;
import org.alertflex.mc.services.ProjectFacade;
import org.alertflex.mc.services.HostsFacade;
import org.alertflex.mc.services.SandboxJobFacade;


@ManagedBean (name="sandboxJobController")
@ViewScoped
public class SandboxJobController implements Serializable {
    
    @EJB
    private AuthenticationSingleton authSingleton;
    
    String session_tenant;
    String session_user;
    
    @EJB
    private ProjectFacade prjFacade;
    private Project project;
        
    List<String> listSandboxType = null;
    private String selectedSandboxType = "Cuckoo";
    
    List<String> listSensorType = null;
    private String selectedSensorType = "suricata";
        
    @EJB
    private HostsFacade hostsFacade;
    private String selectedHostName = "";
    private List<String> listHostName;
    
    @EJB
    private SandboxJobFacade sandboxJobFacade; 
    private String selectedSandboxJob = "";
    private List<String> listSandboxJob;
    
    String desc;
        
    String path = "";
    String ext = ".*";
    
    int filesLimit = 0;
    
    int timerange = 0;
    
    Boolean delFile = false;
    Boolean delInfect = false;
    
               
    public SandboxJobController() {
        
    }
    
    @PostConstruct
    public void init() {
        
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        HttpSession session = request.getSession(false);
        
        session_tenant = authSingleton.getTenantName(session);
        session_user = authSingleton.getUserName(session);
        
        project = prjFacade.find(session_tenant);
        
        listSandboxJob = sandboxJobFacade.findJobNamesByRef(session_tenant);
        if(listSandboxJob == null) listSandboxJob = new ArrayList();
        
        listSandboxType = new ArrayList();
        listSandboxType.add("Cuckoo");
        listSandboxType.add("HybridAnalysis");
        listSandboxType.add("VMRay");
        
        listSensorType = new ArrayList();
        listSensorType.add("misc");
        listSensorType.add("suricata");
        
        listHostName = hostsFacade.findHostsNamesByRef(session_tenant);
        if(listHostName == null) listHostName = new ArrayList<>();
        
        String param = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("job");
        
        if (param != null && !param.isEmpty()) {
            
            selectedSandboxJob = param;
            updateParameters();
        }
        
    }
    
    
    public void updateListName() {
        
        listHostName = hostsFacade.findHostsNamesByRef(session_tenant);
        if(listHostName == null) listHostName = new ArrayList();
        
    }
    
    public String getSelectedHostName() {
        return selectedHostName;
    }
 
    public void setSelectedHostName(String h) {
        this.selectedHostName = h;
        
    }
    
    public List<String> getListHostName() {
        return listHostName;
    }
    
    public void setListHostName(List<String> lh) {
        
        this.listHostName = lh;
    }
    
    public String getSelectedSensorType() {
        
        return selectedSensorType;
    }
 
    public void setSelectedSensorType(String st) {
        
        this.selectedSensorType = st;
        
    }
    
    public List<String> getListSensorType() {
        return listSensorType;
    }
    
    public void setListSensorType(List<String> lst) {
        this.listSensorType = lst;
        
    }
    
    public String getSelectedSandboxJob() {
        return selectedSandboxJob;
    }
 
    public void setSelectedSandboxJob(String st) {
        this.selectedSandboxJob = st;
        
    }
    
    public List<String> getListSandboxJob() {
        return listSandboxJob;
    }
    
    public void setListSandboxJob(List<String> lst) {
        this.listSandboxJob = lst;
        
    }
    
    public String getSelectedSandboxType() {
        return selectedSandboxType;
    }
 
    public void setSelectedSandboxType(String st) {
        this.selectedSandboxType = st;
        
    }
    
    public List<String> getListSandboxType() {
        return listSandboxType;
    }
    
    public void setListSandboxType(List<String> lst) {
        this.listSandboxType = lst;
        
    }
    
    public String getDesc() {
        return desc;
    }
     
    public void setDesc(String d) {
        this.desc = d;
    }
    
    public String getPath() {
        return path;
    }
 
    public void setPath(String p) {
        this.path = p;
    }
    
    public String getExt() {
        return ext;
    }
 
    public void setExt(String e) {
        this.ext = e;
    }
    
    public Integer getFilesLimit() {
        return filesLimit;
    }
 
    public void setFilesLimit(Integer l) {
        this.filesLimit = l;
        
    }
    
    public Integer getTimerange() {
        return timerange;
    }
 
    public void setTimerange(Integer t) {
        this.timerange = t;
        
    }
    
    public Boolean getDelFile() {
        return delFile;
    }
 
    public void setDelFile(Boolean d) {
        this.delFile = d;
        
    }
    
    public Boolean getDelInfect() {
        return delInfect;
    }
 
    public void setDelInfect(Boolean d) {
        this.delInfect = d;
        
    }
    
        
    public void updateParameters() {
        
       if (!selectedSandboxJob.isEmpty()) {
            
            SandboxJob sj = sandboxJobFacade.findJobByName(session_tenant, selectedSandboxJob);
            
            if (sj != null ) {
            
                selectedHostName = sj.getHostName();
                selectedSensorType = sj.getSensorType();
                selectedSandboxType = sj.getSandboxType();
                                
                desc= sj.getDescription();
                path = sj.getFilePath();
                ext = sj.getFileExt();
                filesLimit = sj.getFilesLimit();
                timerange = sj.getTimerange();
                delFile = (sj.getDelFile() > 0);
                delInfect = (sj.getDelInfect() > 0);
                                
                updateListName();
            }
        }
    }
    
    public void deleteJob() {
        FacesMessage message;
        
        if(selectedSandboxJob == null || selectedSandboxJob.isEmpty()) return;
        
        SandboxJob sj = sandboxJobFacade.findJobByName(session_tenant, selectedSandboxJob);
        
        if (sj != null ) {
            
            try { 
                
                updateListName();
                
                sandboxJobFacade.remove(sj);
                                
                message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Action result: ","Sandbox job has been deleted.");
            } catch (Exception ex) {
                message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error !", ex.toString());
            }
                
            updateListName();
        }
        else message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error !", "problem with name of Sandbox job");
            
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
    
    public void editJob() {
        
        FacesMessage message;
        
        if(selectedSandboxJob != null && !selectedSandboxJob.isEmpty()) {
        
            SandboxJob sj = sandboxJobFacade.findJobByName(session_tenant, selectedSandboxJob);
            
            
            if (sj != null ) {
            
                sj.setSandboxType(selectedSandboxType);
                sj.setHostName(selectedHostName);
                sj.setSensorType(selectedSensorType);
                sj.setDescription(desc);
                sj.setFilePath(path);
                sj.setFileExt(ext);
                sj.setFilesLimit(filesLimit);
                sj.setTimerange(timerange);
                sj.setDelFile((delFile) ? 1 : 0);
                sj.setDelInfect((delInfect) ? 1 : 0);
                                
                try { 
                    sandboxJobFacade.edit(sj);
                    
                    message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Action result: ","Sandbox job has been updated.");
                } catch (Exception ex) {
                    message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error !", ex.toString());
                }
            }
            else message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error !", "problem with name of Sandbox job");
            
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }
    
    public void createJob() {
        
        FacesMessage message = null;
        
        if (selectedHostName != null) {
            
            SandboxJob sj = sandboxJobFacade.findJobByHostName(session_tenant, selectedHostName);
        
            if (sj == null) {
            
                sj = new SandboxJob();
            
                sj.setName(selectedSandboxJob);
                sj.setPlaybook("indef");
                sj.setRefId(session_tenant);
                sj.setHostName(selectedHostName);
                sj.setSensorType(selectedSensorType);
                sj.setSandboxType(selectedSandboxType);
                sj.setDescription(desc);
                sj.setFilePath(path);
                sj.setFileExt(ext);
                sj.setFilesLimit(filesLimit);
                sj.setTimerange(timerange);
                sj.setDelFile((delFile) ? 1 : 0);
                sj.setDelInfect((delInfect) ? 1 : 0);
               
                try { 
                    sandboxJobFacade.create(sj);
                    
                    message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Action result: ","Sandbox job has been created.");
                } catch (Exception ex) {
                    message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error !", ex.toString());
                }
            
            }
        }
        else message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error !", "problem with name of Sandbox job");
            
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
} 