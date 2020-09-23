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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.ejb.EJB;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.alertflex.mc.db.Alert;
import org.alertflex.mc.db.Response;
import org.alertflex.mc.db.Users;
import org.alertflex.mc.services.AlertFacade;
import org.alertflex.mc.services.ResponseFacade;
import org.alertflex.mc.services.UsersFacade;



@ManagedBean
@ViewScoped
public class ResponseController implements Serializable {
    
    @EJB
    private AuthenticationSingleton authSingleton;
    String session_tenant;
    String session_user;
    
    @EJB
    private AlertFacade alertsFacade;
    Alert alert;
    
    @EJB
    private ResponseFacade responseFacade;
    
    @EJB
    private UsersFacade userFacade;
    
    List<Response> responseList;
    private String selectedProfile = null;  
    private List<String> profiles = null;
    
    List<String> usersList = null;
    List<String>  selectedUsers = null;
    String notifyUsers = "";
    String notifyText = "";
    
    private String selectedCatNew = ""; 
    
    private String selectedAction = "indef";
    List<String> actionsList;
    
    private String selectedSource = "Select One";
    private List<String> listSources = null;
    
    int alertSeverity = 3;
    
    String alertAgent = "";
    String alertContainer = "";
    String alertIp = "";
    String alertUser = "";
    String alertSensor = "";
    String alertFile = "";
    String alertProcess = "";
    String alertRegex = "";
    
    int aggrReproduced = 0;
    int aggrInperiod = 0;
    
    int beginHour = 0;
    int endHour = 0;
    
    String eventId = "indef";
    
    boolean sendSlack = false;
    
    boolean profileStatus = false;
    
    public ResponseController() {
    
        
    }   
    
    @PostConstruct
    public void init() {
                
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        HttpSession session = request.getSession(false);
        
        session_tenant = authSingleton.getTenantName(session);
        session_user = authSingleton.getUserName(session);
        
        responseList = responseFacade.findResponseByTenant(session_tenant);
        if (responseList != null) {
            profiles = new ArrayList();
            for (Response r : responseList) profiles.add(r.getResId());
        }
        
        
        List<Users> listUsers = userFacade.findUsersByRefId(session_tenant);
        if (listUsers != null) {
            usersList = new ArrayList();
            for (Users u : listUsers) {
                usersList.add(u.getUserid());
            }
        }
        
        actionsList = new ArrayList<>();
        actionsList.add("indef");
        actionsList.add("confirm");
        actionsList.add("incident"); 
        actionsList.add("remove");
        
        
        listSources = new ArrayList<>();
        listSources.add("Alertflex");
        listSources.add("Cuckoo");
        listSources.add("HybridAnalysis");
        listSources.add("Falco");
        listSources.add("Nmap");
        listSources.add("Misc");
        listSources.add("MISP");
        listSources.add("ModSecurity");
        listSources.add("RITA");
        listSources.add("SonarQube");
        listSources.add("Suricata");
        listSources.add("Syslog");
        listSources.add("Vmray");
        listSources.add("Wazuh");
        listSources.add("ZAP");
        
        Long alertId = 0L;
        
        try {
            alertId = Long.parseLong(context.getExternalContext().getRequestParameterMap().get("alertId"));
        } catch (Exception e) {
            alertId = 0L;
        }   
        
        loadAlert(alertId);
    }
    
    public void loadAlert(Long alertId) {
        
        if (alertId != 0L) {
            
            alert = alertsFacade.findAlert(session_tenant, alertId);
            
            if (alert != null) {
                
                selectedProfile = "Dummy";
                
                if (!alert.getAlertSource().isEmpty()) selectedSource = alert.getAlertSource();
                if (!alert.getEventId().isEmpty()) eventId = alert.getEventId();
                
                if (!alert.getAgentName().isEmpty()) alertAgent = alert.getAgentName();
                if (!alert.getContainerName().isEmpty()) alertContainer = alert.getContainerName();
                if (!alert.getSrcIp().isEmpty()) alertIp = alert.getSrcIp();
                if (!alert.getUserName().isEmpty()) alertUser = alert.getUserName();
                
                if (!alert.getSensorId().isEmpty()) alertSensor = alert.getSensorId();
                if (!alert.getFileName().isEmpty()) alertFile = alert.getFileName();
                if (!alert.getProcessName().isEmpty()) alertProcess = alert.getProcessName();
            }
        } 
    }
    
    public String getSelectedSource() {
        
        return selectedSource;
    }
    
    public void setSelectedSource(String s) {
        
        if(s != null && !s.isEmpty()) {
        
            this.selectedSource = s;
            
        } 
    }
    
    public List<String> getListSources() {
        return listSources;
    }
    
    public void setListSources(List<String> l) {
        this.listSources = l;
    }
    
    Response getCurrentProfile() {
        
        if (selectedProfile != null) {
        
            for (Response r : responseList) {
                if (r.getResId().equals(selectedProfile)) return r;
            }
        }
        
        return null;
    }
    
    public String getSelectedProfile() {
        
        return selectedProfile;
    }
    
    public void setSelectedProfile(String p) {
        
        this.selectedProfile = p;
        
    }
    
    public List<String> getProfiles() {
        return profiles;
    }
    
    public void setProfiles(List<String> p) {
        this.profiles = p;
    }
    
    
    public String getSelectedCatNew() {
        
        return selectedCatNew;
    }
    
    public void setSelectedCatNew(String cn) {
        
        this.selectedCatNew = cn;
        
    }
    
    public String getSelectedAction() {
        
        return selectedAction;
    }
    
    public void setSelectedAction(String ss) {
        
        this.selectedAction = ss;
        
    }
    
    public List<String> getActionsList() {
        return actionsList;
    }
    
    public void setActionsList(List<String> l) {
        this.actionsList = l;
    }
    
    public List<String> getUsersList() {
        return usersList;
    }

    public void setUsersList(List<String> lu) {
        this.usersList = lu;
    }
    
    public List<String> convertUsers(String u) {
        
        if (u.isEmpty()) return new ArrayList();
        
        String[] parts = u.split(",");
        return Arrays.asList(parts);
            
    }
    
    public List<String> getSelectedUsers() {
        return selectedUsers;
    }

    public void setSelectedUsers(List<String> lu) {
        
        selectedUsers = lu;
        notifyUsers = "";
        
        for (String u : selectedUsers) { 
            notifyUsers = notifyUsers + u + ",";
        }
        
        
    }
    
    public void setProfileStatus (boolean s) {
        this.profileStatus = s;
    }
    
    public boolean getProfileStatus () {
        return profileStatus;
    }
    
    public String getStatusText() {
        
        if (!profileStatus) return "not active";
        else return "active";
    }
    
    public void setSendSlack (boolean s) {
        this.sendSlack = s;
    }
    
    public boolean getSendSlack () {
        return sendSlack;
    }
    
    public String getSendSlackText() {
        
        if (sendSlack) return "send msg";
        else return "don't send msg";
    }
    
    public void setAlertAgent(String a) {
       this.alertAgent = a;
    }
    
    public String getAlertAgent () {
       return alertAgent;
    }
    
    public void setAlertContainer(String a) {
       this.alertContainer = a;
    }
    
    public String getAlertContainer () {
       return alertContainer;
    }
    
    public void setAlertIp(String i) {
       this.alertIp = i;
    }
    
    public String getAlertIp () {
       return alertIp;
    }
    
    public void setAlertUser(String u) {
       this.alertUser = u;
    }
    
    public String getAlertUser () {
       return alertUser;
    }
    
    public void setAlertSensor(String s) {
       this.alertSensor = s;
    }
    
    public String getAlertSensor () {
       return alertSensor;
    } 
    
    public void setAlertFile(String f) {
       this.alertFile = f;
    }
    
    public String getAlertFile () {
       return alertFile;
    } 
    
    public void setAlertProcess(String p) {
       this.alertProcess = p;
    }
    
    public String getAlertProcess () {
       return alertProcess;
    } 
    
    public void setAlertRegex(String r) {
       this.alertRegex = r;
    }
    
    public String getAlertRegex () {
       return alertRegex;
    } 
    
    public void setAlertSeverity(int as) {
       this.alertSeverity = as;
    }
    
    public int getAlertSeverity() {
       return alertSeverity;
    } 
    
    public String getColorSeverity() {
        if (alertSeverity == 3) return "font-size: 200%; color: orangered; font-weight: 400; ";
        else {
            if (alertSeverity == 2) return "font-size: 200%; color: orange; font-weight: 400; ";
            else {
                if (alertSeverity == 1) return "font-size: 200%; color: #1f7ed0; font-weight: 400; ";
            }
        }
        return "font-size: 200%; color: olivedrab; font-weight: 400; ";
     
    }
    
    public String getDigitSeverity() {
        if (alertSeverity == 1) return "\u2460";
        else {
            if (alertSeverity == 2) return "\u2461";
            else {
                if (alertSeverity == 3) return "\u2462";
            }
        }
        return "\u24ea";
     
    }
    
    public void setAggrReproduced(int ar) {
       this.aggrReproduced = ar;
    }
    
    public int getAggrReproduced () {
       return aggrReproduced;
    } 
    
    public void setAggrInperiod(int ai) {
       this.aggrInperiod = ai;
    }
    
    public int getAggrInperiod () {
       return aggrInperiod;
    } 
    
    public void setBeginHour(int b) {
       this.beginHour = b;
    }
    
    public int getBeginHour () {
       return beginHour;
    } 
    
    public void setEndHour(int b) {
       this.endHour = b;
    }
    
    public int getEndHour () {
       return endHour;
    } 
    
    public void setEventId(String ei) {
       this.eventId = ei;
    }
    
    public String getEventId () {
       return eventId;
    } 
    
    public void setNotifyUsers(String u) {
       this.notifyUsers = u;
    }
    
    public String getNotifyUsers () {
       return notifyUsers;
    }    
        
    public void setNotifyText(String t) {
       this.notifyText = t;
    }
    
    public String getNotifyText () {
       return notifyText;
    }
    
    public void createProfile () {
        FacesMessage message;
        
        if (selectedProfile != null) {
       
            Response new_profile = getCurrentProfile();
        
            if (new_profile == null) {
                
                new_profile = new Response();
                new_profile.setRefId(session_tenant);
                new_profile.setResId(selectedProfile);
                
                new_profile.setResType(0);
                new_profile.setNode("indef");
                new_profile.setCatProfile("indef");
                new_profile.setPlaybook("indef");
                
                new_profile.setUserid(session_user);
                
                new_profile.setAlertSource(selectedSource);
                
                new_profile.setAlertSeverity(alertSeverity);
                new_profile.setAlertAgent(alertAgent);
                new_profile.setAlertContainer(alertContainer);
                new_profile.setAlertIp(alertIp);
                new_profile.setAlertUser(alertUser);
                new_profile.setAlertSensor(alertSensor);
                new_profile.setAlertFile(alertFile);
                new_profile.setAlertProcess(alertProcess);
                new_profile.setAlertRegex(alertRegex);
                
                new_profile.setAggrReproduced(aggrReproduced);
                new_profile.setAggrInperiod(aggrInperiod);
                
                new_profile.setBeginHour(beginHour);
                new_profile.setEndHour(endHour);
                                
                new_profile.setAction(selectedAction);
                new_profile.setCatNew(selectedCatNew);
                
                new_profile.setEventId(eventId);
                                
                new_profile.setNotifyUsers(notifyUsers);
                new_profile.setNotifyMsg(notifyText);
                
                int ss = sendSlack ? 1 : 0;
                new_profile.setSendSlack(ss);
                
                int ps = profileStatus ? 1 : 0;
                new_profile.setStatus(ps);
                
                try { 
                    responseFacade.create(new_profile);
                            
                    message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Action result: ","Profile was successfully created.");
                } catch (Exception ex) {
                    message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error !", ex.toString());
                }
        
            } else {
                message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Action result: ","Profile already exist.");
            }
        
            FacesContext.getCurrentInstance().addMessage(null,message);
        }
    }
    
    public void getParameters() {
        FacesMessage message;
        
        if (selectedProfile != null) {
       
            Response new_profile = getCurrentProfile();
        
            if (new_profile != null) {
                
                selectedSource = new_profile.getAlertSource();
                
                alertSeverity = new_profile.getAlertSeverity();
                alertAgent = new_profile.getAlertAgent();
                alertContainer = new_profile.getAlertContainer();
                alertIp = new_profile.getAlertIp();
                alertUser = new_profile.getAlertUser();
                alertRegex = new_profile.getAlertRegex();
                alertSensor = new_profile.getAlertSensor();
                alertFile = new_profile.getAlertFile();
                alertProcess = new_profile.getAlertProcess();
                
                aggrReproduced = new_profile.getAggrReproduced();
                aggrInperiod = new_profile.getAggrInperiod();
                
                beginHour = new_profile.getBeginHour();
                endHour = new_profile.getEndHour();
                
                selectedAction = new_profile.getAction();
                selectedCatNew = new_profile.getCatNew();
                
                eventId = new_profile.getEventId();
                                 
                 
                notifyUsers = new_profile.getNotifyUsers();
                selectedUsers = convertUsers(notifyUsers);
                notifyText = new_profile.getNotifyMsg();
                
                sendSlack = (new_profile.getSendSlack() > 0);
                
                profileStatus = (new_profile.getStatus() > 0);
                
                                
            } else {
                message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Action result: ","Profile does not exist.");
                FacesContext.getCurrentInstance().addMessage(null,message);
            }
        }
    }
    
    public void updateProfiles () {
        //session_tenant = authSingleton.getTenantName(session);
        responseList = responseFacade.findResponseByTenant(session_tenant);
        
        if (responseList != null) {
            profiles = new ArrayList();
            for (Response r : responseList) profiles.add(r.getResId());
        }
    }
    
    public void editProfile () {
        FacesMessage message;
        
        if (selectedProfile != null) {
       
            Response new_profile = getCurrentProfile();
        
            if (new_profile != null) {
                
                new_profile.setResType(0);
                new_profile.setNode("indef");
                new_profile.setCatProfile("indef");
                new_profile.setPlaybook("indef");
                
                if (!new_profile.getAlertSource().equals(selectedSource)) new_profile.setAlertSource(selectedSource);
                
                if (new_profile.getAlertSeverity() != alertSeverity) new_profile.setAlertSeverity(alertSeverity);
                
                if (!new_profile.getAlertAgent().equals(alertAgent)) new_profile.setAlertAgent(alertAgent);
                if (!new_profile.getAlertContainer().equals(alertContainer)) new_profile.setAlertContainer(alertContainer);
                if (!new_profile.getAlertIp().equals(alertIp)) new_profile.setAlertIp(alertIp);
                if (!new_profile.getAlertUser().equals(alertUser)) new_profile.setAlertUser(alertUser);
                if (!new_profile.getAlertSensor().equals(alertSensor)) new_profile.setAlertSensor(alertSensor);
                if (!new_profile.getAlertProcess().equals(alertProcess)) new_profile.setAlertProcess(alertProcess);
                if (!new_profile.getAlertFile().equals(alertFile)) new_profile.setAlertFile(alertFile);
                if (!new_profile.getAlertRegex().equals(alertRegex)) new_profile.setAlertRegex(alertRegex);
                
                if (new_profile.getAggrReproduced() != aggrReproduced) new_profile.setAggrReproduced(aggrReproduced);
                if (new_profile.getAggrInperiod() != aggrInperiod) new_profile.setAggrInperiod(aggrInperiod);
                
                if (new_profile.getBeginHour() !=  beginHour) new_profile.setBeginHour(beginHour);
                if (new_profile.getEndHour() !=  endHour) new_profile.setEndHour(endHour);
                
                if (!new_profile.getCatNew().equals(selectedCatNew)) new_profile.setCatNew(selectedCatNew);
                if (!new_profile.getAction().equals(selectedAction)) new_profile.setAction(selectedAction);
                
                if (new_profile.getEventId() != eventId) new_profile.setEventId(eventId);
                
                if (!new_profile.getNotifyUsers().equals(notifyUsers)) new_profile.setNotifyUsers(notifyUsers);
                if (!new_profile.getNotifyMsg().equals(notifyText)) new_profile.setNotifyMsg(notifyText);
                
                int ss = sendSlack ? 1:0;
                if (new_profile.getSendSlack() !=  ss) new_profile.setSendSlack(ss);
                
                int ps = profileStatus ? 1:0;
                if (new_profile.getStatus() !=  ps) new_profile.setStatus(ps);
                
                try { 
                    responseFacade.edit(new_profile);
                    message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Action result: ","Profile was successfully changed.");        
                } catch (Exception ex) {
                    message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error !", ex.toString());
                }
            } else message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Action result: ","Please, enter correct profile name.");
        
            FacesContext.getCurrentInstance().addMessage(null,message);
        }
    }
    
    public void deleteProfile () {
        
        FacesMessage message;
       
        Response new_profile = getCurrentProfile();
                                
        if (new_profile != null) {
            
            try { 
                responseFacade.remove(new_profile);
                message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Action result: ","Profile was successfully deleted.");
            } catch (Exception ex) {
                message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error !", ex.toString());
            }
        
        } else message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Action result: ","Profile does not exist.");
        
        FacesContext.getCurrentInstance().addMessage(null,message);    
    }   
    
}
