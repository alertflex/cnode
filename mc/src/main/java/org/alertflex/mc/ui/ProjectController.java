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
    int taskTimerange = 0;
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
    
    String cuckooHost = "";
    Integer cuckooPort = 0;
    
    String falconUrl = "";
    String falconKey = "";
    
    String vmrayUrl = "";
    String vmrayKey = "";
    
    String zapHost = "";
    Integer zapPort = 0;
    String zapKey = "";
    
    String nessusURL = "";
    String nessusAccesskey = "";
    String nessusSecretkey = "";
    
    String sonarURL = "";
    String sonarUser = "";
    String sonarPassword = "";
    
    String snykOrgid = "";
    String snykKey = "";
    
    String mailSmtp = "";
    String mailPort = "";
    String mailUser = "";
    String mailPass = "";
    String mailFrom = "";
    
    String smsAccount = "";
    String smsToken = "";
    String smsFrom = "";
    
    String slackHook = "";
    
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
        taskTimerange = project.getTaskTimerange();
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
        
        cuckooHost = project.getCuckooHost();
        cuckooPort = project.getCuckooPort();
        falconUrl = project.getFalconUrl();
        falconKey = project.getFalconKey();
        vmrayUrl = project.getVmrayUrl();
        vmrayKey = project.getVmrayKey();
        
        zapHost = project.getZapHost();
        zapPort = project.getZapPort();
        zapKey = project.getZapKey();
        
        sonarURL = project.getSonarUrl();
        sonarUser = project.getSonarUser();
        sonarPassword = project.getSonarPass();
        
        nessusURL = project.getNessusUrl();
        nessusAccesskey = project.getNessusAccesskey();
        nessusSecretkey = project.getNessusSecretkey();
        
        snykOrgid = project.getSnykOrgid();
        snykKey = project.getSnykKey();
        
        mailSmtp = project.getMailSmtp();
        mailPort = project.getMailPort();
        mailUser = project.getMailUser();
        mailPass = project.getMailPass();
        mailFrom = project.getMailFrom();
        
        smsAccount = project.getSmsAccount();
        smsToken = project.getSmsToken();
        smsFrom = project.getSmsFrom();
        
        slackHook = project.getSlackHook();
        
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
    
    public int getTaskTimerange() {
       return taskTimerange;
    } 
   
    public void setTaskTimerange(int tt) {
       this.taskTimerange = tt;
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
    
    public String getCuckooHost() {
       return cuckooHost;
    } 
   
    public void setCuckooHost(String h) {
       this.cuckooHost = h;
    }
    
    public Integer getCuckooPort() {
       return cuckooPort;
    } 
    
    public void setCuckooPort(Integer p) {
       this.cuckooPort = p;
    } 
    
    public String getFalconUrl() {
       return falconUrl;
    } 
   
    public void setFalconUrl(String u) {
       this.falconUrl = u;
    }
    
    public String getFalconKey() {
       return falconKey;
    } 
   
    public void setFalconKey(String k) {
       this.falconKey = k;
    }
    
    public String getVmrayUrl() {
       return vmrayUrl;
    } 
   
    public void setVmrayUrl(String u) {
       this.vmrayUrl = u;
    }
    
    public String getVmrayKey() {
       return vmrayKey;
    } 
   
    public void setVmrayKey(String k) {
       this.vmrayKey = k;
    }
    
    public String getZapHost() {
       return zapHost;
    } 
   
    public void setZapHost(String h) {
       this.zapHost = h;
    }
    
    public Integer getZapPort() {
       return zapPort;
    } 
    
    public void setZapPort(Integer p) {
       this.zapPort = p;
    }
    
    public String getZapKey() {
       return zapKey;
    } 
   
    public void setZapKey(String k) {
       this.zapKey = k;
    }
    
    public String getSonarURL() {
       return sonarURL;
    } 
   
    public void setSonarURL(String u) {
       this.sonarURL = u;
    }
    
    public String getSonarUser() {
       return sonarUser;
    } 
   
    public void setSonarUser(String u) {
       this.sonarUser = u;
    }
    
    public String getSonarPassword() {
       return sonarPassword;
    } 
   
    public void setSonarPassword(String p) {
       if(!p.isEmpty()) this.sonarPassword = p;
    }
    
    public String getNessusURL() {
       return nessusURL;
    } 
   
    public void setNessusURL(String n) {
       this.nessusURL = n;
    }
    
    public String getNessusAccesskey() {
       return nessusAccesskey;
    } 
   
    public void setNessusAccesskey(String k) {
       this.nessusAccesskey = k;
    }
    
    public String getNessusSecretkey() {
       return nessusSecretkey;
    } 
   
    public void setNessusSecretkey(String k) {
       if(!k.isEmpty()) this.nessusSecretkey = k;
    }
    
    public String getSnykKey() {
       return snykKey;
    } 
   
    public void setSnykKey(String k) {
       this.snykKey = k;
    }
    
    public String getSnykOrgid() {
       return snykOrgid;
    } 
   
    public void setSnykOrgid(String oi) {
       this.snykOrgid = oi;
    }
    
    public String getMailSmtp() {
       return mailSmtp;
    } 
   
    public void setMailSmtp(String s) {
       this.mailSmtp = s;
    }
    
    public String getMailPort() {
       return mailPort;
    } 
   
    public void setMailPort(String p) {
       this.mailPort = p;
    }
    
    public String getMailPass() {
       return mailPass;
    } 
   
    public void setMailPass(String p) {
       if(!p.isEmpty()) this.mailPass = p;
    }
    
    public String getMailUser() {
       return mailUser;
    } 
   
    public void setMailUser(String u) {
       this.mailUser = u;
    }
    
    public String getMailFrom() {
       return mailFrom;
    } 
   
    public void setMailFrom(String t) {
       this.mailFrom = t;
    }
    
    public String getSmsAccount() {
       return smsAccount;
    } 
   
    public void setSmsAccount(String a) {
       this.smsAccount = a;
    }
    
    public String getSmsToken() {
       return smsToken;
    } 
   
    public void setSmsToken(String t) {
       if(!t.isEmpty()) this.smsToken = t;
    }
    
    public String getSmsFrom() {
       return smsFrom;
    } 
   
    public void setSmsFrom(String f) {
       this.smsFrom = f;
    }
    
    public String getSlackHook() {
       return slackHook;
    } 
   
    public void setSlackHook(String h) {
       if(!h.isEmpty()) this.slackHook = h;
    }
    
    public void changeSetting () {
        FacesMessage message;
        
        try {
            project.setAlertTimerange(alertTimerange);
            project.setStatTimerange(statTimerange);
            project.setTaskTimerange(taskTimerange);
                        
            project.setProjectPath(projectPath);
            
            project.setIncJson((incJson) ? 1 : 0);
            
            project.setSemActive((semActive) ? 1 : 0);
            
            if (semActive && enableResponse) project.setSemActive(2);
            
            project.setIocCheck((iocCheck) ? 1 : 0);
            
            project.setStatRest((statRest) ? 1 : 0);
            
            project.setIocEvent(iocEvent);
            
            project.setIprepCat(iprepCat);
            project.setIprepTimerange(iprepTimerange);
            
            project.setLogHost(logHost);
            project.setLogPort(logPort);
            project.setSendNetflow((sendNetflow) ? 1 : 0);
            
            project.setCuckooHost(cuckooHost);
            project.setCuckooPort(cuckooPort);
            project.setFalconUrl(falconUrl);
            project.setFalconKey(falconKey);
            project.setVmrayUrl(vmrayUrl);
            project.setVmrayKey(vmrayKey);
            
            project.setZapHost(zapHost);
            project.setZapPort(zapPort);
            project.setZapKey(zapKey);
            
            project.setSonarUrl(sonarURL);
            project.setSonarUser(sonarUser);
            project.setSonarPass(sonarPassword);
            
            project.setNessusUrl(nessusURL);
            project.setNessusAccesskey(nessusAccesskey);
            project.setNessusSecretkey(nessusSecretkey);
            
            project.setSnykOrgid(snykOrgid);
            project.setSnykKey(snykKey);
            
            project.setMailSmtp(mailSmtp);
            project.setMailPort(mailPort);
            project.setMailUser(mailUser);
            project.setMailPass(mailPass);
            project.setMailFrom(mailFrom);
                        
            project.setSmsAccount(smsAccount);
            project.setSmsToken(smsToken);
            project.setSmsFrom(smsFrom);
            
            project.setSlackHook(slackHook);
            
            projectFacade.edit(project);
            
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Action result: ","Server settings was successfully changed.");        
        } catch (Exception ex) {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error !", ex.toString());
        }
        
        FacesContext.getCurrentInstance().addMessage(null, message);
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
}
