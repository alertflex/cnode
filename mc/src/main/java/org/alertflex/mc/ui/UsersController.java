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
import java.util.List;
import javax.ejb.EJB;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.alertflex.mc.db.Groups;
import org.alertflex.mc.db.Users;
import org.alertflex.mc.services.GroupsFacade;
import org.alertflex.mc.services.UsersFacade;
import org.alertflex.mc.supp.ActiveUser;
import org.alertflex.mc.supp.CiTools;



@ManagedBean
@ViewScoped
public class UsersController implements Serializable {
    
    @EJB
    private AuthenticationSingleton authSingleton;
    
    String session_tenant;
    String session_user;
    ActiveUser au;
    
    @EJB
    private UsersFacade users;
    
    @EJB
    private GroupsFacade groups;
        
    private List<Users> usersList = null;
            
    private Users selectedUser;
    
    String username = "";
    String email = "";
    String mobile = "";
    Boolean sendSms = false;
    String password = "";
    String oldPassword = "";
    
    int layoutMenu = 0;
    int dashboardRange = 0;
    
    public UsersController() {
    
        
    }   
    
    @PostConstruct
    public void init() {
                
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        HttpSession session = request.getSession(false);
        
        session_tenant = authSingleton.getTenantName(session);
        session_user = authSingleton.getUserName(session);
        au = authSingleton.getActiveUser(session);
        
        Users user = users.find(session_user);
        if (user != null) {
            email = user.getEmail();
            mobile = user.getMobile();
            sendSms = (user.getSendSms() != 0);
        }
    }
    
    public List<Users> getUsersList() {
        if (usersList == null)  usersList = users.findAll();
        return usersList;
    }

    public void setUsersList(List<Users> users_list) {
        this.usersList = users_list;
    }
    
        
    public Users getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(Users user) {
        this.selectedUser = user;
    }
    
    public String getUsername() {
       return username;
    } 
   
    public void setSession_user(String u) {
       this.session_user = u;
    }
    
    public String getSession_user() {
       return session_user;
    } 
    
    public void setSession_tenant(String t) {
       this.session_tenant = t;
    }
    
    public String getSession_tenant() {
       return session_tenant;
    } 
   
    public void setUsername(String u) {
       this.username = u;
    }
    
    public String getEmail() {
       return email;
    } 
   
    public void setEmail(String e) {
       this.email = e;
    }
    
    public String getMobile() {
       return mobile;
    } 
   
    public void setMobile(String m) {
       this.mobile = m;
    }
    
    
    public boolean getSendSms() {
       return sendSms;
    } 
   
    public void setSendSms(boolean ss) {
       this.sendSms = ss;
    }
    
    public String getPassword() {
       return this.password;
    } 
   
    public void setPassword(String p) {
       this.password = p;
    }
    
    public String getOldPassword() {
       return this.oldPassword;
    } 
   
    public void setOldPassword(String p) {
       this.oldPassword = p;
    }
    
    public int getDashboardRange() {
       return dashboardRange;
    } 
   
    public void setDashboardRange(int dr) {
       this.dashboardRange = dr;
    }
    
    public int getLayoutMenu() {
       return layoutMenu;
    } 
   
    public void setLayoutMenu(int lm) {
       this.layoutMenu = lm;
    }
    
    public List<Users> getUsers() {
       usersList = new ArrayList<>();
        
       usersList = users.findUsersByRefId(session_tenant);
        
       return usersList;
    }
    
    
    public void createUser () throws Exception {
        Users new_user;
        Groups new_group;
        FacesMessage message;
       
        new_user = users.find(username);
        
        if (new_user == null) {
            
            CiTools key = new CiTools("");
            String p = key.stringToDigest(password);
            
            new_user = new Users (username, p, session_tenant);
            new_user.setDashboardRange(dashboardRange);
            new_user.setLayoutMenu(layoutMenu);
            new_user.setEmail(email);
            new_user.setMobile(mobile);
            new_user.setSendSms((sendSms) ? 1 : 0);
            new_group = new Groups (username, "user");
            
            try { 
                users.create(new_user);
                groups.create(new_group);
            
                message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Action result: ","User profile was successfully created.");
            } catch (Exception ex) {
                message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error !", ex.toString());
            }
        
        } else {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Action result: ","User profile already exist.");
        }
        
        FacesContext.getCurrentInstance().addMessage(null,message);
          
    }
    
    public void changeUser () throws Exception {
        Users user;
        FacesMessage message;
        
        user = users.find(session_user);
        
        CiTools key = new CiTools("");
        String p = key.stringToDigest(oldPassword);
                
        if (user.getPassword().equals(p)) {
            
            if (dashboardRange != user.getDashboardRange()) {
                user.setDashboardRange(dashboardRange);
            }
            if (layoutMenu != user.getLayoutMenu()) {
                user.setLayoutMenu(layoutMenu);
            }
            if (!email.equals(user.getEmail())) user.setEmail(email);
            
            if (!mobile.equals(user.getMobile())) user.setMobile(mobile);
            
            int test = (sendSms) ? 1 : 0;
            if (test != user.getSendSms()) user.setSendSms(test);
            
            if (!password.equals("")) {
                p = key.stringToDigest(password);
                user.setPassword(p);
            }
            
            try { 
                users.edit(user);
                message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Action result: ","User settings were successfully changed.");        
            } catch (Exception ex) {
                message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error !", ex.toString());
            }
        } else message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Action result: ","Please, enter correct current password.");
        
        FacesContext.getCurrentInstance().addMessage(null,message);
    }
    
    public void deleteUser () {
        
        Users del_user;
        Groups del_group;
        FacesMessage message;
                                
        if (!username.equals("admin")) {
            
            del_user = users.find(username);
            
            if (del_user != null) {
                
                try { 
                    users.remove(del_user);
                    del_group = new Groups (username, "users");
                    groups.remove(del_group);
                
                    message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Action result: ","User profile was successfully deleted.");
                } catch (Exception ex) {
                    message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error !", ex.toString());
                }
        
            } else message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Action result: ","User profile does not exist.");
        }
        else message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Action result: ","You can't delete Admin user profile.");
            
        FacesContext.getCurrentInstance().addMessage(null,message);    
    }                
}
