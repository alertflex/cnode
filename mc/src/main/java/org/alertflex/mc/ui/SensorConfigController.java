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
import org.alertflex.mc.db.Node;
import org.alertflex.mc.services.NodeFacade;
import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.zip.GZIPOutputStream;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.jms.BytesMessage;
import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.alertflex.mc.db.Project;
import org.alertflex.mc.db.Sensor;
import org.alertflex.mc.services.ProjectFacade;
import org.alertflex.mc.services.SensorFacade;
import org.alertflex.mc.supp.ProjectRepository;
import org.apache.activemq.ActiveMQConnectionFactory;


@ManagedBean
@ViewScoped
public class SensorConfigController implements Serializable {
    
    @EJB
    private AuthenticationSingleton authSingleton;
    
    String session_tenant;
    String session_user;
    
    @EJB
    private ProjectFacade prjFacade;
    Project project;
        
    @EJB
    private NodeFacade nodeFacade;
    
    @EJB
    private SensorFacade sensorFacade;
    
    private String selectedNodeId = "";
    private List<String> listNodeId;
    
    private String selectedSensor = "";
    private List<String> listSensor;
    
    ProjectRepository lr = null;
    
    String suricata_config = "suricata.yaml";
    String ossec_config = "ossec.conf";
    String modsec_config = "modsecurity.conf";
    String falco_config = "falco.yaml";
    String configPayload = "";
    Path configPath = null;
    
    
    public SensorConfigController() {
        
    }
    
    @PostConstruct
    public void init() {
        
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        HttpSession session = request.getSession(false);
        
        session_tenant = authSingleton.getTenantName(session);
        session_user = authSingleton.getUserName(session);
        
        project = prjFacade.find(session_tenant);
        
        listNodeId = nodeFacade.findAllNodeNames(session_tenant);
        
        listSensor = new ArrayList();
        
    }
    
    public String getSelectedNodeId() {
        return selectedNodeId;
    }
 
    public void setSelectedNodeId(String n) {
        
        this.selectedNodeId = n;
    }
    
    public void updateListSensorNames() {
        
        if (selectedNodeId.isEmpty()) return;
        
        List<Sensor> ls = sensorFacade.findSensorByNode(session_tenant, selectedNodeId);
        if (ls != null) {
            for (Sensor s: ls) {
                listSensor.add(s.getSensorPK().getName());
            }
        }        
    }
    
    public List<String> getListNodeId() {
        return listNodeId;
    }
    
    public List<String> getListSensor() {
        return listSensor;
    }
    
    public String getSelectedSensor() {
        return selectedSensor;
    }
 
    public void setSelectedSensor(String s) {
        this.selectedSensor = s;
    }
    
    public String getConfigPayload() {
        return configPayload;
    }
 
    public void setConfigPayload (String text) {
        
        this.configPayload = text;
    }
    
    public void openConfig() {
        
        FacesMessage message;
        
        Node node;
        
        if (selectedNodeId == null) return;
        
        if (!selectedNodeId.isEmpty() && !selectedNodeId.equals("controller")) {
            try {
                /* Create a connection instance */
                
                if (selectedSensor.isEmpty()) return;
                
                node = nodeFacade.findByNodeName(session_tenant,selectedNodeId);
                
                lr = new ProjectRepository(project);
                if (!lr.initSensors(node, listSensor)) {
                    
                    configPayload = "error";
                    return;
                }
                
                String dir = lr.getSensorDir(selectedSensor);
                if (dir == null) return;
                
                Sensor s = sensorFacade.findSensorByName(session_tenant, selectedNodeId, selectedSensor);
                
                if (s == null) return;
                
                switch (s.getSensorType()) {
                    
                    case "falco": {
                        
                        configPath = Paths.get(dir + falco_config);
                        break;
                    }  
                                    
                    case "wazuh": {
                        
                        configPath = Paths.get(dir + ossec_config);
                        break;
                    }    
                    
                    case "suricata": {
                        
                        configPath = Paths.get(dir + suricata_config);
                        break;
                    } 
                    
                    case "modsec": {
                        
                        configPath = Paths.get(dir + modsec_config);
                        break;
                    }
                                            
                    default:
                        break;
                }
                
                if (configPath != null) {
                    
                    List contents = Files.readAllLines(configPath);
                    configPayload = "";
                    for (Object content : contents) {
                        configPayload =  configPayload + content + "\n";
                    }

                }
                
                message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Result: ","Config has been loaded.");
                
                
            } catch (IOException ex) {
                
                message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error !", ex.toString());
                configPayload = "";
            }
            
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }
    
    
    public void saveConfig() {
        
        FacesMessage message;
        
        if (selectedNodeId == null) return;
        
        if (!selectedSensor.isEmpty() && !configPayload.isEmpty() && !configPayload.equals("error")) {
            try {
                /* Create a connection instance */
                
                Files.write(configPath, configPayload.getBytes());
                
                message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Result: ","Config has been saved.");
                
            } catch (IOException ex) {
                message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error !", ex.toString());
                configPayload = "";
            }
            
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }
    
    public void sendConfig() {
        
        FacesMessage message;
        
        Node node;
        
        String sensor = "";
        int sensorType = 0;
        
        if (selectedNodeId == null) return;
        
        if (!selectedSensor.isEmpty() && !configPayload.isEmpty() && !configPayload.equals("error")) {
            
            String[] parsedSensorName = selectedSensor.split("-");
                
            if(parsedSensorName.length != 2) return;
            
            sensor = parsedSensorName[0];
            
            switch (parsedSensorName[1]) {
                
                case "crs" : sensorType = 0;
                    break;
                    
                case "hids" : sensorType = 1;
                    break;
                
                case "nids" : sensorType = 2;
                    break;
                
                case "waf" : sensorType = 3;
                    break;
                    
                default:
                    return;
            }
            
        
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
            
                BytesMessage msg = session.createBytesMessage();
            
                msg.setStringProperty("node_id", selectedNodeId);
                msg.setStringProperty("msg_type", "byte");
                msg.setStringProperty("content_type", "config");
                
                msg.setStringProperty("sensor", sensor);
                msg.setIntProperty("sensor_type", sensorType);
            
            
                byte[] sompFilters = compress(configPayload);
                       
                msg.writeBytes(sompFilters);
                producer.send(msg);
                
                // Clean up
                session.close();
                connection.close();
                
                Files.write(configPath, configPayload.getBytes());
                
                message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Result: ","Config has been sent.");
                
            } catch ( IOException | JMSException e) {
            
                message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error !", e.toString());
                configPayload = "";
            } 
            
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }
    
    public byte[] compress(String str) throws IOException {
    
        if ((str == null) || (str.length() == 0)) {
            return null;
        }
    
        ByteArrayOutputStream obj = new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(obj);
        gzip.write(str.getBytes("UTF-8"));
        gzip.flush();
        gzip.close();
        return obj.toByteArray();
    }
    
} 