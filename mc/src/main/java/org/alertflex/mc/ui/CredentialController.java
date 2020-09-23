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

import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.alertflex.mc.db.Credential;
import org.alertflex.mc.db.Hosts;
import org.alertflex.mc.services.CredentialFacade;
import org.alertflex.mc.services.HostsFacade;
import java.io.IOException;
import java.io.InputStream;
import org.alertflex.mc.supp.AuthenticationSingleton;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import javax.ejb.EJB;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.alertflex.mc.db.Project;
import org.alertflex.mc.services.ProjectFacade;
import org.apache.commons.io.IOUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;



@ManagedBean
@ViewScoped
public class CredentialController implements Serializable {
    
    @EJB
    private AuthenticationSingleton authSingleton;
    
    String session_tenant;
    String session_user;
    
    @EJB
    private ProjectFacade prjFacade;
    private Project project;
            
    @EJB
    private CredentialFacade credentialFacade;
    
    @EJB
    private HostsFacade hostsFacade;
    
    private List<Credential> listCredentials;
    private List<String> listCredentialsName;
    private String selectedCredentialName;
    private Credential selectedCredential;
    
    String credential = "";
    String user = "";
    String password = "";
    String key = "";
    String desc = "";
    
    
    public CredentialController() {
    
        
    }   
    
    @PostConstruct
    public void init() {
                
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        HttpSession session = request.getSession(false);
        
        session_tenant = authSingleton.getTenantName(session);
        session_user = authSingleton.getUserName(session);
        
        project = prjFacade.find(session_tenant);
                
        listCredentials = credentialFacade.findCredentialByRef(session_tenant);
        listCredentialsName = credentialFacade.findCredentialNamesByRef(session_tenant);
        
        if (listCredentials == null) listCredentials = new ArrayList();
        if (listCredentialsName == null) listCredentialsName = new ArrayList();
        
    }
    
    public List<Credential> getListCredentials() {
        return listCredentials;
    }
    
    public List<String> getListCredentialsName() {
        return listCredentialsName;
    }
    
    public String getSelectedCredentialName() {
        return selectedCredentialName;
    }

    public void setSelectedCredentialName(String n) {
        this.selectedCredentialName = n;
    }
    
    public String getCredential() {
        return credential;
    }
 
    public void setCredential(String a) {
        this.credential = a;
        
    }
    
    public String getUser() {
        return user;
    }
 
    public void setUser(String u) {
        this.user = u;
        
    }
    
    public String getKey() {
        return key;
    }
 
    public void setKey(String k) {
        this.user = k;
        
    }
    
    public String getPassword() {
        return password;
    }
 
    public void setPassword(String p) {
        if(!p.isEmpty()) this.password = p;
        
    }
    
    public String getDesc() {
        return desc;
    }
 
    public void setDesc(String d) {
        this.desc = d;
    }
    
    public void addCredential() {
        FacesMessage message;
        
        Credential c = new Credential();
        
        c.setRefId(session_tenant);
        c.setName(credential);
        c.setDescription(desc);
        c.setUsername(user);
        c.setPass(password);
        c.setSslKey(key);
        
        try { 
            credentialFacade.create(c);
                            
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Action result: ","Credential has been created.");
        } catch (Exception ex) {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error !", ex.toString());
        }
        
        listCredentials = credentialFacade.findCredentialByRef(session_tenant);
        
        if (listCredentials == null) listCredentials = new ArrayList();
        
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
    
    public void getParameters() {
        
        FacesMessage message;
        
        selectedCredential = credentialFacade.findCredential(session_tenant, selectedCredentialName);
        
        if (selectedCredential != null) {
            desc = selectedCredential.getDescription();
            user = selectedCredential.getUsername();
            password = selectedCredential.getPass();
            key = selectedCredential.getSslKey();
            
        }
    }
    
    public void editCredential() {
        
        FacesMessage message;
        
        if (selectedCredential != null) {
            
            if (!selectedCredential.getUsername().equals(user)) selectedCredential.setUsername(user);
            
            if (selectedCredential.getPass() != null) {
                if (!selectedCredential.getPass().equals(password)) selectedCredential.setPass(password);
            } else {
                if (password != null) selectedCredential.setPass(password);
            }
            
            if (!key.isEmpty()) {
                selectedCredential.setSslKey(key);
            }
            
            
            if (!selectedCredential.getDescription().equals(desc)) selectedCredential.setDescription(desc);
            
            try { 
                credentialFacade.edit(selectedCredential);
                            
                message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Action result: ","Credential has been changed.");
            } catch (Exception ex) {
                message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error !", ex.toString());
            }
            
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }
    
    public void deleteCredential() {
        FacesMessage message;
        
        selectedCredential = credentialFacade.findCredential(session_tenant, selectedCredentialName);
        
        if (selectedCredential == null) {
            
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error !", "The credential does not exist");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return;
        }
        
        List<Hosts> hosts = hostsFacade.findHostsByCredential(session_tenant, selectedCredentialName);
        
        if (hosts != null) {
        
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error !", "Host object with such credential name exist");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return;
        }
        
        try { 
            
            credentialFacade.remove(selectedCredential);
                            
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Action result: ","The credential has been deleted.");
            
        } catch (Exception ex) {
            
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error !", ex.toString());
        }
        
        FacesContext.getCurrentInstance().addMessage(null, message);
        
        listCredentials = credentialFacade.findCredentialByRef(session_tenant);
        
        if (listCredentials == null) listCredentials = new ArrayList();
            
    }
    
     public void keyUpload(FileUploadEvent event) throws IOException {
    
        UploadedFile uploadedFile = event.getFile();
        String fileName = uploadedFile.getFileName();
        
        if (!fileName.isEmpty()) {
            
            //if (!project.getVaultUrl().isEmpty()) uploadToValue(uploadedFile);
            //else uploadToDirectory(uploadedFile);
            
            InputStream input = uploadedFile.getInputstream();
            
            key = IOUtils.toString(input, StandardCharsets.UTF_8.name());
            
            FacesMessage message = new FacesMessage("Succesful", fileName + "- key has been uploaded");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }  
        
    }
    
}
