/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alertflex.mc.ui;

import org.alertflex.mc.supp.AuthenticationSingleton;
import javax.ejb.EJB;
import java.io.Serializable;  
import java.util.*;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.alertflex.mc.db.SandboxTask;
import org.alertflex.mc.services.SandboxTaskFacade;

/**
 *
 * @author root
 */
@ViewScoped
@ManagedBean
public class SandboxTasksController  implements Serializable {
    
    @EJB
    private AuthenticationSingleton authSingleton;
    
    String session_tenant;
    String session_user;
    
    @EJB
    private SandboxTaskFacade taskFacade;

        
    private Date start= new Date();  
    private Date end = new Date();  
      
    
    List<SandboxTask> taskList;
 
            
    @PostConstruct  
    public void init() { 
        
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        HttpSession session = request.getSession(false);
        
        session_tenant = authSingleton.getTenantName(session);
        session_user = authSingleton.getUserName(session);
        
        taskList = new ArrayList();
    } 
    
    public Date getStart() {
       return start;
    } 
   
    public void setStart(Date date) {
       this.start = date;
    }
    
    public Date getEnd() {
       return end;
    } 
   
    public void setEnd(Date date) {
       this.end = date;
    }
    
    public void setTaskList(List<SandboxTask> st) {
        
        taskList = st;
    }
    
    public List<SandboxTask> getTaskList() {
        
        taskList = taskFacade.findTaskByDate(session_tenant, start, end);
        
        if (taskList == null) taskList = new ArrayList();
        
        return taskList;
    }
    
    public List<SandboxTask> getTaskListCuckoo() {
        
        taskList = taskFacade.findTaskByType(session_tenant, "Cuckoo", start, end);
        
        if (taskList == null) taskList = new ArrayList();
        
        return taskList;
    }
    
    public List<SandboxTask> getTaskListHybridAnalysis() {
        
        taskList = taskFacade.findTaskByType(session_tenant, "HybridAnalysis", start, end);
        
        if (taskList == null) taskList = new ArrayList();
        
        return taskList;
    }
    
    public List<SandboxTask> getTaskListVMRay() {
        
        taskList = taskFacade.findTaskByType(session_tenant, "VMRay", start, end);
        
        if (taskList == null) taskList = new ArrayList();
        
        return taskList;
    }
}
