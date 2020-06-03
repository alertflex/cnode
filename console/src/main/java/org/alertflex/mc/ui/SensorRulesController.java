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

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
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
import java.io.InputStream;
import java.io.InputStreamReader;

import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.zip.GZIPOutputStream;
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
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;


@ManagedBean
@ViewScoped
public class SensorRulesController implements Serializable {
    
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
    
    // Node params
    String rulePayload = "";
        
    String selectedRule = "";
    Path rulePath = null;
    List<String> listRules = null;
    
    String fileName = "";
    
    Boolean fileType = false;
    
    Node node;
    ProjectRepository pr = null;
    
    public SensorRulesController() {
       
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
    
    boolean initProjectRep() {
        node = nodeFacade.findByNodeName(session_tenant,selectedNodeId);
        if (node == null) return false;
        pr = new ProjectRepository(project);
        if (!pr.initSensor(node, selectedSensor)) return false;
        
        return true;
    }
    
    public void initListRules() {
        
        listRules = new ArrayList();
        
        initProjectRep();
                        
        int rulesType = pr.getSensorType(selectedSensor);
        
        if (rulesType == -1) return;
            
        Path dir = null;
            
        if (rulesType == 1) {
                    
            if (!fileType) dir = pr.getHidsRulesPath(selectedSensor);
            else dir = pr.getHidsDecodersPath(selectedSensor);
                    
        } else dir = pr.getLocRulesPath(selectedSensor);
        
        
        if (Files.isDirectory(dir)) {
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
                for (Path path : stream) {
                    if (!Files.isDirectory(path))
                        listRules.add(path.getFileName().toString());
                }
            } catch (IOException e) {
                
            }
        }
        
        return;
    }
    
    public boolean getFileType() {
        return fileType;
    }
 
    public void setFileType(boolean fileType) {
        this.fileType = fileType;
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
    
    public List<String> getListRules() {
        
        return listRules;
    }
    
    public void setListRules(List<String> r) {
        
        this.listRules = r;
    }
    
    public String getSelectedRule() {
        return selectedRule;
    }
    
    public void setSelectedRule(String r) {
        this.selectedRule = r;
    }
    
    public String getRulePayload() {
        return rulePayload;
    }
 
    public void setRulePayload (String text) {
        this.rulePayload = text;
    }
    
    public void openRule() {
        
        if (selectedNodeId == null) return;
        
        if (!selectedSensor.isEmpty() && !selectedRule.isEmpty()) {
            try {
                
                fileName = selectedRule;
                
                if (!initProjectRep()) return;
                
                /* Create a connection instance */
                String dir = null;
                
                if (pr.getSensorType(selectedSensor) == 1) {
                    
                    if (!fileType) dir = pr.getHidsRulesDir(selectedSensor);
                    else dir = pr.getHidsDecodersDir(selectedSensor);
                    
                } else {
                    dir = pr.getLocRulesDir(selectedSensor);
                }    
                    
                if (dir == null) return;
                
                rulePath = Paths.get(dir + selectedRule);
                
                if (rulePath != null) {
                    
                    List contents = Files.readAllLines(rulePath);
                    rulePayload = "";
                    for (Object content : contents) {
                        rulePayload =  rulePayload + content + "\n";
                    }
                    
                }
                
                
            } catch (IOException ex) {
                rulePayload = "error";
            }
            
            
            return;
        }
    }
    
    
    public void saveRule() {
        
        if (selectedNodeId == null) return;
        
        if (!selectedSensor.isEmpty() && !fileName.isEmpty() && !rulePayload.isEmpty() && !rulePayload.equals("error")) {
            
            if (!initProjectRep()) return;
                
            String dir = null;
                
            if (pr.getSensorType(selectedSensor) == 1) {
                    
                if (!fileType) dir = pr.getHidsRulesDir(selectedSensor);
                else dir = pr.getHidsDecodersDir(selectedSensor);
                    
            } else {
                dir = pr.getLocRulesDir(selectedSensor);
            }    
                
            rulePath = Paths.get(dir + fileName);
            
            try {
                /* Create a connection instance */
                
                Files.write(rulePath, rulePayload.getBytes());
                
            } catch (IOException ex) {
                rulePayload = "error";
            }
            
            return;
        }
        
    }
    
    public void sendRule() throws IOException {
        
        String sensor = "";
        int rulesType = 0;
        
        if (selectedNodeId == null) return;
        
        if (!selectedSensor.isEmpty() && !fileName.isEmpty() && !rulePayload.isEmpty() && !rulePayload.equals("error")) {
            
            if (!initProjectRep()) return;
            if (!pr.initSensor(node, selectedSensor)) return;
            
            rulesType = pr.getSensorType(selectedSensor);
            
            String[] parsedSensorName = selectedSensor.split("-");
                
            if(parsedSensorName.length != 2) return;
            
            sensor = parsedSensorName[0];
            
            String dir = null;
            
            if (rulesType == 1) {
                    
                if (!fileType) {
                    dir = pr.getHidsRulesDir(selectedSensor);
                }
                else {
                    dir = pr.getHidsDecodersDir(selectedSensor);
                    rulesType = 2;
                }
                    
            } else {
                if(rulesType == 2) rulesType = 3;
                if(rulesType == 3) rulesType = 4;
                dir = pr.getLocRulesDir(selectedSensor);
            }
                
            rulePath = Paths.get(dir + fileName);
            Files.write(rulePath, rulePayload.getBytes());
                   
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
            
                BytesMessage message = session.createBytesMessage();
                
                message.setStringProperty("node_id", selectedNodeId);
                message.setStringProperty("msg_type", "byte");
                message.setStringProperty("content_type", "rules");
                message.setStringProperty("rule_name", fileName);
                message.setIntProperty("rule_reload", 1);
                
                message.setStringProperty("sensor", sensor);
                message.setIntProperty("rules_type", rulesType);
                            
                byte[] sompFilters = compress(rulePayload);
                       
                message.writeBytes(sompFilters);
                producer.send(message);
                
                // Clean up
                session.close();
                connection.close();
                
            } catch ( IOException | JMSException e) {
            
                e.printStackTrace();
                rulePayload = "error";
            }
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
    
    public void uploadRule(FileUploadEvent event) throws IOException {
    
        UploadedFile uploadedFile = event.getFile();
        String uploadedFileName = uploadedFile.getFileName();
        
        if (!uploadedFileName.isEmpty()) {
            
            fileName = uploadedFileName;
            
            try {
                
                InputStream input = uploadedFile.getInputstream();
                
                rulePayload = new BufferedReader(new InputStreamReader(input)).lines().collect(Collectors.joining("\n"));
                
            } catch (IOException ex) {
                rulePayload = "error";
            }
        }  
        
    }
    
} 