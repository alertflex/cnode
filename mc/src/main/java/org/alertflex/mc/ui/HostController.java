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
import org.alertflex.mc.services.NodeFacade;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.alertflex.mc.db.Hosts;
import org.alertflex.mc.db.Project;
import org.alertflex.mc.services.CredentialFacade;
import org.alertflex.mc.services.ProjectFacade;
import org.alertflex.mc.services.HostsFacade;

@ManagedBean(name = "hostController")
@ViewScoped
public class HostController implements Serializable {

    @EJB
    private AuthenticationSingleton authSingleton;

    String session_tenant;
    String session_user;

    @EJB
    private ProjectFacade prjFacade;
    private Project project;

    @EJB
    private NodeFacade nodeFacade;
    private String selectedNodeName = "";
    private List<String> listNodeName;

    @EJB
    private CredentialFacade credentialFacade;
    private String selectedCredentialName = "";
    List<String> listCredentialName = null;

    @EJB
    private HostsFacade hostsFacade;
    private String selectedHostName = "";
    private List<String> listHostName;

    String name = "";
    String description = "";
    String agent = "";
    String sensor = "";
    String address = "127.0.0.1";
    int port = 0;

    public HostController() {

    }

    @PostConstruct
    public void init() {

        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        HttpSession session = request.getSession(false);

        session_tenant = authSingleton.getTenantName(session);
        session_user = authSingleton.getUserName(session);

        project = prjFacade.find(session_tenant);

        listNodeName = nodeFacade.findAllNodeNames(session_tenant);
        if (listNodeName == null) {
            listHostName = new ArrayList();
        }

        listCredentialName = credentialFacade.findCredentialNamesByRef(session_tenant);
        if (listCredentialName == null) {
            listCredentialName = new ArrayList();
        }

        updateListHostName();

    }

    public void updateListHostName() {
        listHostName = hostsFacade.findHostsNamesByRef(session_tenant);
        if (listHostName == null) {
            listHostName = new ArrayList();
        }
    }

    public List<String> getListHostName() {
        return listHostName;
    }

    public void setListHostName(List<String> lh) {

        this.listHostName = lh;
    }

    public String getSelectedHostName() {
        return selectedHostName;
    }

    public void setSelectedHostName(String h) {
        this.selectedHostName = h;

    }

    public String getSelectedNodeName() {

        return selectedNodeName;
    }

    public void setSelectedNodeName(String n) {

        this.selectedNodeName = n;

    }

    public List<String> getListNodeName() {
        return listNodeName;
    }

    public void setListNodeName(List<String> ln) {
        this.listNodeName = ln;

    }

    public String getSelectedCredentialName() {

        return selectedCredentialName;
    }

    public void setSelectedCredentialName(String c) {

        this.selectedCredentialName = c;

    }

    public List<String> getListCredentialName() {
        return listCredentialName;
    }

    public void setListCredentialName(List<String> lc) {
        this.listCredentialName = lc;

    }

    public String getName() {
        return name;
    }

    public void setName(String n) {
        this.name = n;

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String d) {
        this.description = d;

    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String a) {
        this.address = a;

    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String a) {
        this.agent = a;

    }

    public String getSensor() {
        return sensor;
    }

    public void setSensor(String s) {
        this.sensor = s;

    }

    public int getPort() {
        return port;
    }

    public void setPort(int p) {
        this.port = p;
    }

    public void updateParameters() {

        if (!selectedHostName.isEmpty()) {

            Hosts h = hostsFacade.findHost(session_tenant, selectedHostName);

            description = h.getDescription();
            agent = h.getAgent();
            sensor = h.getSensor();
            address = h.getAddress();
            port = h.getPort();
            selectedNodeName = h.getNode();
            selectedCredentialName = h.getCred();
        }
    }

    public void deleteHost() {
        FacesMessage message;

        if (selectedHostName == null) {
            return;
        }

        Hosts hostExist = hostsFacade.findHost(session_tenant, selectedHostName);

        if (hostExist == null) {

            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error !", "Host name does not exist");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return;
        }

        try {
            hostsFacade.remove(hostExist);

            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Action result: ", "Host profile has been deleted.");

        } catch (Exception ex) {

            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error !", ex.toString());
        }

        FacesContext.getCurrentInstance().addMessage(null, message);

        updateListHostName();
    }

    public void editHost() {

        FacesMessage message;

        if (selectedHostName != null) {

            Hosts h = hostsFacade.findHost(session_tenant, selectedHostName);

            h.setAgent(agent);
            h.setSensor(sensor);
            h.setDescription(description);
            h.setPort(port);
            h.setAddress(address);
            h.setCred(selectedCredentialName);
            h.setNode(selectedNodeName);

            try {
                hostsFacade.edit(h);

                message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Action result: ", "Host profile has been updated.");
            } catch (Exception ex) {
                message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error !", ex.toString());
            }
        } else {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error !", "problem with name of Host profile");
        }

        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void createHost() {

        FacesMessage message = null;

        if (name != null && !name.isEmpty()) {

            Hosts h = hostsFacade.findHost(session_tenant, name);

            if (h == null) {

                h = new Hosts();

                h.setName(name);
                h.setRefId(session_tenant);
                h.setAgent(agent);
                h.setSensor(sensor);
                h.setDescription(description);
                h.setPort(port);
                h.setAddress(address);
                h.setCred(selectedCredentialName);
                h.setNode(selectedNodeName);

                try {
                    hostsFacade.create(h);

                    message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Action result: ", "Host profile has been created.");
                } catch (Exception ex) {
                    message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error !", ex.toString());
                }

                updateListHostName();
            }
        } else {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error !", "problem with name of host profile");
        }

        FacesContext.getCurrentInstance().addMessage(null, message);
    }
}
