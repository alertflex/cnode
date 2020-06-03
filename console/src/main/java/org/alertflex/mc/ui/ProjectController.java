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

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import org.alertflex.mc.supp.AuthenticationSingleton;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.primefaces.context.RequestContext;
import org.alertflex.mc.db.Project;
import org.alertflex.mc.services.ProjectFacade;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;



@ManagedBean
@ViewScoped
public class ProjectController implements Serializable {
    
    @EJB
    private AuthenticationSingleton authSingleton;
    String session_tenant;
    String session_user;
    
    @EJB
    private ProjectFacade projectFacade;
    Project project;
    
    String projectPath = "";
    
    int alertTimerange = 0;
    int statTimerange = 0;
    int logrepTimerange = 0;
    int iprepTimerange = 0;
    
    Boolean incJson = false;
    Boolean statRest = false;
    Boolean semActive = false;
    Boolean enableResponse = false;
    Boolean iocCheck = false;
    
    int iprepCat = 0;
    int iocEvent = 0;
    
    String logHost = "";
    Integer logPort = 0;
    Boolean sendNetflow = false;
    
    String graylogUsername = "";
    String graylogPassword = "";
    String graylogHost = "";
    Integer graylogPort = 0;
    
    String elkHost = "";
    Integer elkPort = 0;
    String elkUsername = "";
    String elkPassword = "";
    String elkStorepass = "";
    String elkKeystore = "";
    String elkTruststore = "";
    
    public ProjectController() {
    
    }   
    
    @PostConstruct
    public void init() {
                
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        HttpSession session = request.getSession(false);
        
        session_tenant = authSingleton.getTenantName(session);
        session_user = authSingleton.getUserName(session);        
        
        project = projectFacade.find(session_tenant);
        
        alertTimerange = project.getAlertTimerange();
        statTimerange = project.getStatTimerange();
        logrepTimerange = project.getLogrepTimerange();
        iprepTimerange = project.getIprepTimerange();
        
        projectPath = project.getProjectPath();
                
        switch (project.getSemActive()) {
            
            case 1:
                semActive = true;
                enableResponse = false;
                break;
            case 2:
                semActive = true;
                enableResponse = true;
                break;
            default:
                semActive = false;
                enableResponse = false;
                break;
        }
        
        iocCheck = (project.getIocCheck() > 0);
        incJson = (project.getIncJson() > 0);
        statRest = (project.getStatRest() > 0);
        
        iocEvent = project.getIocEvent();
        iprepCat = project.getIprepCat();
        
        logHost = project.getLogHost();
        logPort = project.getLogPort();
        sendNetflow = (project.getSendNetflow() > 0);
        
        graylogHost = project.getGraylogHost();
        graylogUsername = project.getGraylogUser();
        graylogPassword = project.getGraylogPass();
        graylogPort = project.getGraylogPort();
        
        elkHost = project.getElkHost();
        elkPort = project.getElkPort();
        elkUsername = project.getElkUser();
        elkPassword = project.getElkPass();
        elkStorepass = project.getElkStorepass();
        elkKeystore = project.getElkKeystore();
        elkTruststore = project.getElkTruststore();
        
    }
    
    public int getAlertTimerange() {
       return alertTimerange;
    } 
   
    public void setAlertTimerange(int at) {
       this.alertTimerange = at;
    }
    
    public int getStatTimerange() {
       return statTimerange;
    } 
   
    public void setStatTimerange(int st) {
       this.statTimerange = st;
    }
    
    public int getLogrepTimerange() {
       return logrepTimerange;
    } 
   
    public void setLogrepTimerange(int lt) {
       this.logrepTimerange = lt;
    }
    
    public String getProjectPath() {
       return projectPath;
    } 
   
    public String getProjectRef() {
       return project.getRefId();
    } 
   
    public void setProjectPath(String p) {
       this.projectPath = p;
    }
    
    public Boolean getIncJson() {
       return incJson;
    } 
    
    public void setIncJson(Boolean ij) {
       this.incJson = ij;
    } 
    
    public Boolean getStatRest() {
       return statRest;
    } 
    
    public void setStatRest(Boolean sr) {
       this.statRest = sr;
    } 
    
    public Boolean getSemActive() {
       return semActive;
    } 
    
    public void setSemActive(Boolean sa) {
       this.semActive = sa;
    } 
    
    public Boolean getEnableResponse() {
       return enableResponse;
    } 
    
    public void setEnableResponse(Boolean er) {
       this.enableResponse = er;
    } 
    
    public Boolean getIocCheck() {
       return iocCheck;
    } 
    
    public void setIocCheck(Boolean ch) {
       this.iocCheck = ch;
    } 
    
    public int getIocEvent() {
       return iocEvent;
    } 
    
    public void setIocEvent(int e) {
       this.iocEvent = e;
    } 
    
    public int getIprepTimerange() {
       return iprepTimerange;
    } 
   
    public void setIprepTimerange(int it) {
       this.iprepTimerange = it;
    }
    
    public int getIprepCat() {
       return iprepCat;
    } 
   
    public void setIprepCat(int ic) {
       this.iprepCat = ic;
    }
    
    public String getLogHost() {
       return logHost;
    } 
   
    public void setLogHost(String h) {
       this.logHost = h;
    }
    
    public Integer getLogPort() {
       return logPort;
    } 
    
    public void setLogPort(Integer p) {
       this.logPort = p;
    } 
    
    public void setSendNetflow(Boolean sn) {
       this.sendNetflow = sn;
    } 
    
    public Boolean getSendNetflow() {
       return sendNetflow;
    } 
    
    public String getGraylogHost() {
       return graylogHost;
    } 
   
    public void setGraylogHost(String h) {
       this.graylogHost = h;
    }
    
    public Integer getGraylogPort() {
       return graylogPort;
    } 
    
    public void setGraylogPort(Integer p) {
       this.graylogPort = p;
    } 
    
    public String getGraylogUsername() {
       return graylogUsername;
    } 
   
    public void setGraylogUsername(String u) {
       this.graylogUsername = u;
    }
    
    
    public String getGraylogPassword() {
       return graylogPassword;
    } 
   
    public void setGraylogPassword(String p) {
       this.graylogPassword = p;
    }
    
    public String getElkHost() {
       return elkHost;
    } 
   
    public void setElkHost(String h) {
       this.elkHost = h;
    }
    
    public Integer getElkPort() {
       return elkPort;
    } 
    
    public void setElkPort(Integer p) {
       this.elkPort = p;
    } 
    
    public String getElkUsername() {
       return elkUsername;
    } 
   
    public void setElkUsername(String u) {
       this.elkUsername = u;
    }
    
    public String getElkPassword() {
       return elkPassword;
    } 
   
    public void setElkPassword(String p) {
       this.elkPassword = p;
    }
    
    public String getElkStorepass() {
       return elkStorepass;
    } 
   
    public void setElkStorepass(String p) {
       this.elkStorepass = p;
    }
    
    public void setElkKeystore(String k) {
       this.elkKeystore = k;
    }
    
    public String getElkKeystore() {
       return elkKeystore;
    } 
    
    public void setElkTruststore(String t) {
       this.elkTruststore = t;
    }
    
    public String getElkTruststore() {
       return elkTruststore;
    } 
    
    public void changeSetting () {
        FacesMessage message;
        
        try {
            project.setAlertTimerange(alertTimerange);
            project.setStatTimerange(statTimerange);
            project.setLogrepTimerange(logrepTimerange);
            
            project.setProjectPath(projectPath);
            
            project.setIncJson((incJson) ? 1 : 0);
            
            project.setSemActive((semActive) ? 1 : 0);
            
            if (semActive && enableResponse) project.setSemActive(2);
            
            project.setIocCheck((iocCheck) ? 1 : 0);
            
            project.setStatRest((statRest) ? 1 : 0);
            
            project.setIocEvent(iocEvent);
            
            project.setIprepCat(iprepCat);
            project.setIprepTimerange(iprepTimerange);
            
            project.setGraylogHost(graylogHost);
            project.setGraylogPort(graylogPort);
            project.setGraylogUser(graylogUsername);
            project.setGraylogPass(graylogPassword);
                        
            project.setElkHost(elkHost);
            project.setElkPort(elkPort);
            project.setElkUser(elkUsername);
            project.setElkPass(elkPassword);
            project.setElkStorepass(elkStorepass);
            project.setElkKeystore(elkKeystore);
            project.setElkTruststore(elkTruststore);
            
            projectFacade.edit(project);
            
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Action result: ","Server settings was successfully changed.");        
        } catch (Exception ex) {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error !", ex.toString());
        }
        
        RequestContext.getCurrentInstance().showMessageInDialog(message);
    }
    
    public StreamedContent getFileAltprobe() {
        try {
            
            String pemPath = project.getProjectPath();
            
            if (!pemPath.isEmpty()) {
                
                File initialFile = new File(pemPath + "Broker.pem");
                InputStream stream = new FileInputStream(initialFile);
 
                StreamedContent file = new DefaultStreamedContent(stream, "application/zip", "Broker.pem");
            
                return file;
            }
        } catch (Exception ex) {
            
        }
        
        return null;
    }
    
    public StreamedContent getFileBeats() {
        try {
            
            String pemPath = project.getProjectPath();
            
            if (!pemPath.isEmpty()) {
                
                File initialFile = new File(pemPath + "alertflex.crt");
                InputStream stream = new FileInputStream(initialFile);
 
                StreamedContent file = new DefaultStreamedContent(stream, "application/zip", "alertflex.crt");
            
                return file;
            }
        } catch (Exception ex) {
            
        }
        
        return null;
    }
    
}
