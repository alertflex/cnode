/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alertflex.mc.ui;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.alertflex.mc.db.AlertPriority;
import org.alertflex.mc.supp.AuthenticationSingleton;
import org.alertflex.mc.services.AlertPriorityFacade;


/**
 *
 * @author root
 */


@ManagedBean
@ViewScoped
public class AlertsPriorityController {
    
    @EJB
    private AuthenticationSingleton authSingleton;
    
    String session_tenant;
    String session_user;
    
    @EJB
    private AlertPriorityFacade alertPriorityFacade;
    
    AlertPriority ap = null;
    
    String desc;
    Boolean log = false;
    Integer severityDefault = 0;
    Integer severityThreshold = 0;
    Integer minorThreshold = 0;
    Integer majorThreshold = 0;
    Integer criticalThreshold = 0;
    
    String text1 = "indef";
    String text2 = "indef";
    String text3 = "indef";
    String text4 = "indef";
    String text5 = "indef";
        
    Integer value1 = 0;
    Integer value2 = 0;
    Integer value3 = 0;
    Integer value4 = 0;
    Integer value5 = 0;
        
    List<String> listSources;
    String selectedSource = "";
        
    @PostConstruct
    public void init() {
                
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        HttpSession session = request.getSession(false);
        
        session_tenant = authSingleton.getTenantName(session);
        session_user = authSingleton.getUserName(session);  
        
        listSources = alertPriorityFacade.findSourcesNameByRef(session_tenant);
        
        if (listSources == null) listSources  = new ArrayList();
        
    }
    
    public List<String> getListSources() {
        return listSources;
    }
    
    public void setListSources(List<String> ls) {
        this.listSources = ls;
    }
    
    public String getSelectedSource() {
        return selectedSource;
    }

    public void setSelectedSource(String s) {
        this.selectedSource = s;
    }
    
    public String getDesc() {
        return desc;
    }

    public void setDesc(String d) {
        this.desc = d;
    }
    
    public Integer getSeverityDefault() {
       return severityDefault;
    } 
   
    public void setSeverityDefault(Integer s) {
       this.severityDefault = s;
    }
    
    public Integer getSeverityThreshold() {
       return severityThreshold;
    } 
   
    public void setSeverityThreshold(Integer th) {
       this.severityThreshold = th;
    }
    
    public Boolean getLog() {
       return log;
    } 
    
    public void setLog(Boolean l) {
       this.log = l;
    } 
    
    public Integer getMinorThreshold() {
       return minorThreshold;
    } 
   
    public void setMinorThreshold(Integer m) {
       this.minorThreshold = m;
    }
    
    public Integer getMajorThreshold() {
       return majorThreshold;
    } 
   
    public void setMajorThreshold(Integer m) {
       this.majorThreshold = m;
    }
    
    public Integer getCriticalThreshold() {
       return criticalThreshold;
    } 
   
    public void setCriticalThreshold(Integer c) {
       this.criticalThreshold = c;
    }
    
    public String getText1() {
        return text1;
    }

    public void setText1(String p) {
        this.text1 = p;
    }
    
    public String getText2() {
        return text2;
    }

    public void setText2(String p) {
        this.text2 = p;
    }
    
    public String getText3() {
        return text3;
    }

    public void setText3(String p) {
        this.text3 = p;
    }
    
    public String getText4() {
        return text4;
    }

    public void setText4(String p) {
        this.text4 = p;
    }
    
    public String getText5() {
        return text5;
    }

    public void setText5(String p) {
        this.text5 = p;
    }
    
    public Integer getValue1() {
       return value1;
    } 
   
    public void setValue1(Integer s) {
       this.value1 = s;
    }
    
    public Integer getValue2() {
       return value2;
    } 
   
    public void setValue2(Integer s) {
       this.value2 = s;
    }
    
    public Integer getValue3() {
       return value3;
    } 
   
    public void setValue3(Integer s) {
       this.value3 = s;
    }
    
    public Integer getValue4() {
       return value4;
    } 
   
    public void setValue4(Integer s) {
       this.value4 = s;
    }
    
    public Integer getValue5() {
       return value5;
    } 
   
    public void setValue5(Integer s) {
       this.value5 = s;
    }
    
    public void loadParameters() {
        
       if (!selectedSource.isEmpty()) {
            
            ap = alertPriorityFacade.findPriorityBySource(session_tenant, selectedSource);
            
            if (ap != null ) {
            
                desc = ap.getDescription();
                severityDefault = ap.getSeverityDefault();
                severityThreshold = ap.getSeverityThreshold();
                log = (ap.getLog() > 0);
                minorThreshold = ap.getMinorThreshold();
                majorThreshold = ap.getMajorThreshold();
                criticalThreshold = ap.getCriticalThreshold();
                
                text1 = ap.getText1();
                text2 = ap.getText2();
                text3 = ap.getText3();
                text4 = ap.getText4();
                text5 = ap.getText5();
                
                value1 = ap.getValue1();
                value2 = ap.getValue2();
                value3 = ap.getValue3();
                value4 = ap.getValue4();
                value5 = ap.getValue5();
                
            }
        }
    }
    
    public void createPriority() {
        
        FacesMessage message;
        
        ap = alertPriorityFacade.findPriorityBySource(session_tenant, selectedSource);
        
        if (ap == null) {
            
            ap = new AlertPriority();
            
            ap.setSource(selectedSource);
            ap.setDescription(desc);
            ap.setSeverityDefault(severityDefault);
            ap.setSeverityThreshold(severityThreshold);
            ap.setLog((log) ? 1 : 0);
            ap.setMinorThreshold(minorThreshold);
            ap.setMajorThreshold(majorThreshold);
            ap.setCriticalThreshold(criticalThreshold);
            
            ap.setText1(text1);
            ap.setText2(text2);
            ap.setText3(text3);
            ap.setText4(text4);
            ap.setText5(text5);
            
            ap.setValue1(value1);
            ap.setValue2(value2);
            ap.setValue3(value3);
            ap.setValue4(value4);
            ap.setValue5(value5);
                        
            try { 
                
                alertPriorityFacade.create(ap);
                            
                message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Action result: ","Alert priority has been created.");
            } catch (Exception ex) {
                message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error !", ex.toString());
            }
        
            FacesContext.getCurrentInstance().addMessage(null, message);
            
        }
    }
    
    public void editPriority() {
        
        FacesMessage message;
        
        if (ap != null) {
            
            ap.setSource(selectedSource);
            ap.setDescription(desc);
            ap.setSeverityDefault(severityDefault);
            ap.setSeverityThreshold(severityThreshold);
            ap.setLog((log) ? 1 : 0);
            ap.setMinorThreshold(minorThreshold);
            ap.setMajorThreshold(majorThreshold);
            ap.setCriticalThreshold(criticalThreshold);
            
            ap.setText1(text1);
            ap.setText2(text2);
            ap.setText3(text3);
            ap.setText4(text4);
            ap.setText5(text5);
            
            ap.setValue1(value1);
            ap.setValue2(value2);
            ap.setValue3(value3);
            ap.setValue4(value4);
            ap.setValue5(value5);
            
            try { 
                
                alertPriorityFacade.edit(ap);
                            
                message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Action result: ","Alert priority has been changed.");
            } catch (Exception ex) {
                message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error !", ex.toString());
            }
        
            FacesContext.getCurrentInstance().addMessage(null, message);
            
        }
    }
    
    public void deleteSource() {
        
        FacesMessage message;
        
        ap = alertPriorityFacade.findPriorityBySource(session_tenant, selectedSource);
        
        if (ap != null) {
            
            try { 
                
                alertPriorityFacade.remove(ap);
                            
                message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Action result: ","Alert priority has been changed.");
            } catch (Exception ex) {
                message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error !", ex.toString());
            }
        
            FacesContext.getCurrentInstance().addMessage(null, message);
            
        }
    }
}
