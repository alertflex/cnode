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
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.alertflex.mc.db.Project;
import org.alertflex.mc.db.ScanJob;
import org.alertflex.mc.services.ProjectFacade;
import org.alertflex.mc.services.ScanJobFacade;


@ManagedBean (name="scannerJobController")
@ViewScoped
public class ScannerJobController implements Serializable {
    
    @EJB
    private AuthenticationSingleton authSingleton;
    
    String session_tenant;
    String session_user;
    
    @EJB
    private ProjectFacade prjFacade;
    private Project project;
       
   @EJB
    private ScanJobFacade scanJobFacade; 
    private String selectedScanJob = "";
    private List<String> listScanJob;
    
    String desc;
        
    List<String> listScanType = null;
    private String selectedScanType = "";
    
    String value1 = "indef";
    String value2 = "indef";
    Integer value3 = 1;
    
    int timerange = 0;
    
    public ScannerJobController() {
        
    }
    
    @PostConstruct
    public void init() {
        
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        HttpSession session = request.getSession(false);
        
        session_tenant = authSingleton.getTenantName(session);
        session_user = authSingleton.getUserName(session);
        
        project = prjFacade.find(session_tenant);
        
        listScanJob = scanJobFacade.findJobNamesByRef(session_tenant);
        if(listScanJob == null) listScanJob = new ArrayList();
        
        listScanType = new ArrayList();
        listScanType.add("Nessus");
        listScanType.add("Nmap");
        listScanType.add("SonarQube");
        listScanType.add("Snyk");
        listScanType.add("ZAP");
        
        String param = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("job");
        
        if (param != null && !param.isEmpty()) {
            
            selectedScanJob = param;
            updateParameters();
        }
    }
    
    public String getSelectedScanJob() {
        return selectedScanJob;
    }
 
    public void setSelectedScanJob(String sj) {
        this.selectedScanJob = sj;
        
    }
    
    public List<String> getListScanJob() {
        return listScanJob;
    }
    
    public void setListScanJob(List<String> lsj) {
        this.listScanJob = lsj;
        
    }
    
    public String getDesc() {
        return desc;
    }
     
    public void setDesc(String d) {
        this.desc = d;
    }
    
    public String getSelectedScanType() {
        return selectedScanType;
    }
 
    public void setSelectedScanType(String st) {
        this.selectedScanType = st;
        
    }
    
    public List<String> getListScanType() {
        return listScanType;
    }
    
    public void setListScanType(List<String> lst) {
        this.listScanType = lst;
        
    }
    
    public String getValue1() {
        return value1;
    }
 
    public void setValue1(String v) {
        this.value1 = v;
    }
    
    public String getValue2() {
        return value2;
    }
 
    public void setValue2(String v) {
        this.value2 = v;
    }
    
    public Integer getValue3() {
        return value3;
    }
 
    public void setValue3(Integer v) {
        this.value3 = v;
    }
    
    public Integer getTimerange() {
        return timerange;
    }
 
    public void setTimerange(Integer t) {
        this.timerange = t;
        
    }
    
    public void updateParameters() {
        
        if (!selectedScanJob.isEmpty()) {
            
            ScanJob sj = scanJobFacade.findJobByName(session_tenant, selectedScanJob);
            
            if (sj != null ) {
            
                desc= sj.getDescription();
                selectedScanType = sj.getScanType();
                value1 = sj.getValue1();
                value2 = sj.getValue2();
                value3 = sj.getValue3();
                timerange = sj.getTimerange();
            }
        }
    }
    
    public void deleteJob() {
        FacesMessage message;
        
        if(selectedScanJob == null || selectedScanJob.isEmpty()) return;
        
        ScanJob sj = scanJobFacade.findJobByName(session_tenant, selectedScanJob);
        
        if (sj != null ) {
            
            try { 
                
                scanJobFacade.remove(sj);
               
                message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Action result: ","Scan job has been deleted.");
            } catch (Exception ex) {
                message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error !", ex.toString());
            }
        }
        else message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error !", "problem with name of Scan job");
            
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
    
    public void editJob() {
        
        FacesMessage message;
        
        if(selectedScanJob != null && !selectedScanJob.isEmpty()) {
        
            ScanJob sj = scanJobFacade.findJobByName(session_tenant, selectedScanJob);
            
            
            if (sj != null ) {
            
                sj.setDescription(desc);
                sj.setScanType(selectedScanType);
                sj.setValue1(value1);
                sj.setValue2(value2);
                sj.setValue3(value3);
                sj.setTimerange(timerange);
                                
                try { 
                    
                    scanJobFacade.edit(sj);
                                       
                    message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Action result: ","Scan job has been updated.");
                } catch (Exception ex) {
                    message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error !", ex.toString());
                }
            }
            else message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error !", "problem with name of Scan job");
            
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }
    
    public void createJob() {
        
        FacesMessage message = null;
        
        if (selectedScanType != null && !selectedScanType.isEmpty() && !value1.isEmpty() && !value1.equals("indef")) {
            
            ScanJob sj = scanJobFacade.findJobByTypeValue(session_tenant, selectedScanType, value1);
        
            if (sj == null) {
            
                sj = new ScanJob();
            
                sj.setName(selectedScanJob);
                sj.setRefId(session_tenant);
                sj.setDescription(desc);
                sj.setPlaybook("indef");
                sj.setScanType(selectedScanType);
                sj.setValue1(value1);
                sj.setValue2(value2);
                sj.setValue3(value3);
                sj.setTimerange(timerange);
                                                
                try { 
                    
                    scanJobFacade.create(sj);
                                              
                    message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Action result: ","Scan job has been created.");
                } catch (Exception ex) {
                    message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error !", ex.toString());
                }
            
            }
        }
        else message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error !", "problem with name of Scan job");
            
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
} 