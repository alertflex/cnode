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
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import org.alertflex.mc.services.NodeFacade;
import java.util.ArrayList;
import java.util.Date;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.alertflex.mc.db.Agent;
import org.alertflex.mc.db.Project;
import org.alertflex.mc.services.AgentFacade;
import org.alertflex.mc.services.ProjectFacade;
import org.apache.activemq.ActiveMQConnectionFactory;


@ManagedBean
@ViewScoped
public class HidsAgentsController implements Serializable {
    
    @EJB
    private AuthenticationSingleton authSingleton;
    
    String session_tenant;
    String session_user;
    
    @EJB
    private ProjectFacade prjFacade;
    Project project = null;
        
    @EJB
    private AgentFacade agentFacade;
    
    private List<Agent> listAgents;
    
    @EJB
    private NodeFacade nodeFacade;
    
    private String selectedNodeId;
    private List<String> listNodeId;
    
    private Agent selectedAgent;
    
    String agentID = "000";
    String agentName = "indef";
    String agentIp = "indef";
    String agentVersion = "indef";
    String agentManager = "indef";
    String agentOsPlatform = "indef";
    String agentOsVersion = "indef";
    String agentOsName = "indef";
    String agentIpLinked = "indef";
    String agentHostLinked = "indef";
    String agentContainerLinked = "indef";
        
    @PostConstruct
    public void init() {
        
        selectedNodeId = "";
        
                        
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        HttpSession session = request.getSession(false);
        
        session_tenant = authSingleton.getTenantName(session);
        session_user = authSingleton.getUserName(session);
        
        project = prjFacade.find(session_tenant);
        
        listNodeId = nodeFacade.findAllNodeNames(session_tenant);
        
        listAgents = new ArrayList();
        
    }
    
    public String getSelectedNodeId() {
        return selectedNodeId;
    }
 
    public void setSelectedNodeId(String p) {
        this.selectedNodeId = p;
    }
    
    public List<String> getListNodeId() {
        return listNodeId;
    }
    
    public void updateListAgents() {
        getListAgents();
    }
    
    public List<Agent> getListAgents() {
        
        listAgents = agentFacade.findAgentsByNode(session_tenant, selectedNodeId);
        
        if (listAgents == null) listAgents = new ArrayList();
        
        return listAgents;
    }
    
      
    public String getAgentID() {
        return this.agentID;
    }
    
    public void setAgentID(String i) {
        this.agentID = i;
    }
    
    public String getAgentName() {
        return this.agentName;
    }
    
    public void setAgentName(String n) {
        this.agentName = n;
    }
    
    public String getAgentIp() {
        return this.agentIp;
    }
    
    public void setAgentIp(String i) {
        this.agentIp = i;
    }
    
    public String getAgentIpLinked() {
        return this.agentIpLinked;
    }
    
    public void setAgentIpLinked(String ip) {
        this.agentIpLinked = ip;
    }
    
    public String getAgentHostLinked() {
        return this.agentHostLinked;
    }
    
    public void setAgentHostLinked(String h) {
        this.agentHostLinked = h;
    }
    
    public String getAgentContainerLinked() {
        return this.agentContainerLinked;
    }
    
    public void setAgentContainerLinked(String c) {
        this.agentContainerLinked = c;
    }
    
    public String getAgentVersion() {
        return this.agentVersion;
    }
    
    public void setAgentVersion(String v) {
        this.agentVersion = v;
    }
    
    public String getAgentManager() {
        return this.agentManager;
    }
    
    public void setAgentManager(String m) {
        this.agentManager = m;
    }
    
    public String getAgentOsPlatform() {
        return this.agentOsPlatform;
    }
    
    public void setAgentOsPlatform(String op) {
        this.agentOsPlatform = op;
    }
    
    public String getAgentOsVersion() {
        return this.agentOsVersion;
    }
    
    public void setAgentOsVersion(String ov) {
        this.agentOsVersion = ov;
    }
    
    public String getAgentOsName() {
        return this.agentOsName;
    }
    
    public void setAgentOsName(String on) {
        this.agentOsName = on;
    }
    
    // Alias settings  
    public Agent getSelectedAgent() {
        return selectedAgent;
    }
    
    // Alias settings  
    public void setSelectedAgent(Agent a) {
        this.selectedAgent = a;
    }
    
    public void deleteSelectedAgent() {
        if (selectedAgent == null ) return;
        
        String id = selectedAgent.getAgentId();
        
        try {
            agentFacade.remove(selectedAgent);
            
            FacesMessage msg = new FacesMessage("Agent has been removed from list, id of agent: ", id);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        } catch (Exception ex) { 
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error !", ex.toString())); 
            FacesContext.getCurrentInstance().validationFailed(); 
        } 
        
        selectedAgent = null;
    }
    
    public void addAgent() {
        
        List<Agent> agl = agentFacade.findAgentByParameters(session_tenant, selectedNodeId, agentName);
        
        if(!agl.isEmpty()) return;
        
        Agent a = new Agent();
        
        a.setRefId(session_tenant);
        a.setNodeId(selectedNodeId);
        a.setAgentId(agentID);
        a.setName(agentName);
        a.setIp(agentIp);
        a.setStatus("indef");
        a.setIpLinked("indef");
        a.setHostLinked("indef");
        a.setManager("indef");
        a.setVersion("indef");
        a.setOsPlatform("indef");
        a.setOsName("indef");
        a.setOsVersion("indef");
        a.setAgentKey("indef");
        a.setContainerLinked("indef");
        
        Date date = new Date();
        a.setDateAdd(date.toString());
        a.setDateUpdate(date);
        
        try {
            agentFacade.create(a);
            
            FacesMessage msg = new FacesMessage("Agent has been edited, id of agent: ", selectedAgent.getAgentId());
            FacesContext.getCurrentInstance().addMessage(null, msg);
            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        } catch (Exception ex) { 
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error !", ex.toString())); 
            FacesContext.getCurrentInstance().validationFailed(); 
        } 
        
        createRemote();
        
        selectedAgent = null;
    }
    
    public void createRemote() {
        
        if (selectedNodeId == null && !selectedNodeId.isEmpty()) return;
        
        if (!agentName.isEmpty() && !agentName.equals("indef") && !agentIp.isEmpty() && !agentIp.equals("indef")) {
            
            try {
            
                String strConnFactory = System.getProperty("AmqUrl", "");
                String user = System.getProperty("AmqUser", "");
                String pass = System.getProperty("AmqPwd", "");
            
                // Create a ConnectionFactory
                ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(strConnFactory);
            
                // Create a Connection
                Connection connection = connectionFactory.createConnection(user,pass);
                connection.start();

                // Create a Session
                Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

                // Create the destination (Topic or Queue)
                Destination destination = session.createTopic("jms/altprobe/" + project.getRefId());

                // Create a MessageProducer from the Session to the Topic or Queue
                MessageProducer producer = session.createProducer(destination);
                producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
                
                String agentParams = "{\"name\":\"" + agentName + "\",\"ip\":\"" + agentIp + "\"}";
            
                TextMessage message = session.createTextMessage(agentParams);
            
                message.setStringProperty("node_id", selectedNodeId);
                message.setStringProperty("sensor", "master");
                message.setStringProperty("msg_type", "text");
                message.setStringProperty("content_type", "create_agent");
                message.setStringProperty("agent_name", agentName);
                
                producer.send(message);
                
                // Clean up
                session.close();
                connection.close();
                
            } catch ( JMSException e) {
            
                e.printStackTrace();
                // send alert to log
            } 
        }
    }
    
    public void updateSelectedAgent() {
        
        if (selectedAgent == null ) return;
        
        try {
            
            selectedAgent.setIpLinked(agentIpLinked);
            selectedAgent.setHostLinked(agentHostLinked);
            selectedAgent.setContainerLinked(agentContainerLinked);
                        
            agentFacade.edit(selectedAgent);
            
            FacesMessage msg = new FacesMessage("Agent has been edited, id of agent: ", selectedAgent.getAgentId());
            FacesContext.getCurrentInstance().addMessage(null, msg);
            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        } catch (Exception ex) { 
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error !", ex.toString())); 
            FacesContext.getCurrentInstance().validationFailed(); 
        } 
        
        selectedAgent = null;
    }
    
}