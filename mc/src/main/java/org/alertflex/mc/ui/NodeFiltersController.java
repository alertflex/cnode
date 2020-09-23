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
import java.util.List;
import org.json.*;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable; 
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import org.alertflex.mc.db.Node;
import org.alertflex.mc.services.NodeFacade;
import java.io.IOException;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
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
import org.alertflex.mc.db.Response;
import org.alertflex.mc.db.Agent;
import org.alertflex.mc.db.Alert;
import org.alertflex.mc.db.HomeNetwork;
import org.alertflex.mc.services.AgentFacade;
import org.alertflex.mc.services.AlertFacade;
import org.alertflex.mc.services.HomeNetworkFacade;
import org.alertflex.mc.services.ProjectFacade;
import org.alertflex.mc.services.ResponseFacade;
import org.alertflex.mc.supp.ProjectRepository;
import org.alertflex.mc.filters.Filter;
import org.alertflex.mc.filters.GrayList;
import org.apache.activemq.ActiveMQConnectionFactory;


@ManagedBean
@ViewScoped
public class NodeFiltersController implements Serializable {
    
    @EJB
    private AuthenticationSingleton authSingleton;
    
    String session_tenant;
    String session_user;
    
    @EJB
    private ProjectFacade prjFacade;
    private Project project;
        
    @EJB
    private AlertFacade alertsFacade;
    Alert alert;
    
    @EJB
    private NodeFacade nodeFacade;
    
    @EJB
    private AgentFacade agentFacade;
    
    @EJB
    private HomeNetworkFacade hnetFacade;
    
    @EJB
    private ResponseFacade responseFacade;
    
    List<Response> responseList;
    private List<String> responseNames = null;
        
    private String selectedNodeId;
    private List<String> listNodeId;
    
    private String selectedFilterName;
    private List<String> listFiltersNames;
    
    private GrayList selectedCrsGl;
    private GrayList backupCrsGl;
    
    private GrayList selectedHidsGl;
    private GrayList backupHidsGl;
    
    private GrayList selectedNidsGl;
    private GrayList backupNidsGl;
    
    private GrayList selectedWafGl;
    private GrayList backupWafGl;
    
    String filter_file;
    boolean filterFlag = false;
    Filter filter;
    
    public NodeFiltersController() {
        
    }
    
    private String option;   
    
    public String getOption() {
        return option;
    }
 
    public void setOption(String option) {
        this.option = option;
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
            responseNames = new ArrayList();
            responseNames.add("log-mgmt");
            responseNames.add("suppress");
            for (Response r : responseList) responseNames.add(r.getResId());
        }
        
        filter_file = "";
        selectedNodeId = "";
        selectedFilterName = "";
        filterFiles = new ArrayList();
        filterFile = "";
                
        project = prjFacade.find(session_tenant);
        
        listNodeId = nodeFacade.findAllNodeNames(session_tenant);
        
        filter = new Filter();
        
        Long alertId = 0L;
        
        try {
            alertId = Long.parseLong(context.getExternalContext().getRequestParameterMap().get("alertId"));
        } catch (Exception e) {
            alertId = 0L;
        }   
        
        if (alertId != 0L) alert = alertsFacade.findAlert(session_tenant, alertId);
        else alert = null;
    }
    
    public List<String> getResponseNames() {
        return responseNames;
    }
    
    public void setResponseNames(List<String> rn) {
        this.responseNames = rn;
    }
    
    public String getSelectedNodeId() {
        return selectedNodeId;
    }
 
    public void setSelectedNodeId(String n) {
        this.selectedNodeId = n;
        
    }
    
    public List<String> getListNodeId() {
        return listNodeId;
    }
    
    public String getSelectedFilterName() {
        return selectedFilterName;
    }
 
    public void setSelectedFilterName(String fn) {
        this.selectedFilterName = fn;
        
    }
    
    public List<String> getListFiltersNames() {
        return listFiltersNames;
    }
    
    public String getFilterDesc() {
        return filter.getDesc();
    }
    
    public void setFilterDesc(String n) {
        filter.setDesc(n);
    }
    
    // Crs settings  
    public GrayList getSelectedCrsGl() {
        return selectedCrsGl;
    }
    
    public void setSelectedCrsGl(GrayList gl) {
        if (gl != null) {
            
            backupCrsGl = new GrayList();
            
            backupCrsGl.setId(gl.getId());
            
            backupCrsGl.setEvent(gl.getEvent());
            backupCrsGl.setHost(gl.getHost());
            backupCrsGl.setMatch(gl.getMatch());
            
            backupCrsGl.getAgr().setInPeriod(gl.getAgr().getInPeriod());
            backupCrsGl.getAgr().setReproduced(gl.getAgr().getReproduced());
            
            backupCrsGl.getRsp().setProfile(gl.getRsp().getProfile());
            backupCrsGl.getRsp().setNewCategory(gl.getRsp().getNewCategory());
            backupCrsGl.getRsp().setNewDesc(gl.getRsp().getNewDesc());
            backupCrsGl.getRsp().setNewEvent(gl.getRsp().getNewEvent());
            backupCrsGl.getRsp().setNewSeverity(gl.getRsp().getNewSeverity());
            backupCrsGl.getRsp().setNewType(gl.getRsp().getNewType());
            backupCrsGl.getRsp().setNewSource(gl.getRsp().getNewSource());
            
        }
        
        this.selectedCrsGl = gl;
    }
    
    public void deleteSelectedCrsGl() {
        filter.crs.gl.remove(selectedCrsGl);
        int i = 0;
        for (GrayList l: filter.crs.gl) {
            l.setId(i);
            i++;
        }
        selectedCrsGl = null;
    }
    
        
    public void createSelectedCrsGl() {
        GrayList l = new GrayList();
        
        if (alert != null && alert.getAlertSource().equals("Falco")) {
            l.event = alert.getEventId();
            l.host = alert.getContainerName();
        }
        
        l.setId(filter.crs.gl.size());
        filter.crs.gl.add(l);
        selectedCrsGl = null;
    }
    
    public void cancelSelectedCrsGl() {
        
        selectedCrsGl.setId(backupCrsGl.getId());
            
        selectedCrsGl.setEvent(backupCrsGl.getEvent());
        selectedCrsGl.setHost(backupCrsGl.getHost());
        selectedCrsGl.setMatch(backupCrsGl.getMatch());
        
        selectedCrsGl.getAgr().setInPeriod(backupCrsGl.getAgr().getInPeriod());
        selectedCrsGl.getAgr().setReproduced(backupCrsGl.getAgr().getReproduced());
        
        selectedCrsGl.getRsp().setProfile(backupCrsGl.getRsp().getProfile());
        selectedCrsGl.getRsp().setNewCategory(backupCrsGl.getRsp().getNewCategory());
        selectedCrsGl.getRsp().setNewDesc(backupCrsGl.getRsp().getNewDesc());
        selectedCrsGl.getRsp().setNewEvent(backupCrsGl.getRsp().getNewEvent());
        selectedCrsGl.getRsp().setNewSeverity(backupCrsGl.getRsp().getNewSeverity());
        selectedCrsGl.getRsp().setNewType(backupCrsGl.getRsp().getNewType());
        selectedCrsGl.getRsp().setNewSource(backupCrsGl.getRsp().getNewSource());
        
        selectedCrsGl = null;
    }
    
    public void updateSelectedCrsGl() {
        selectedCrsGl = null;
    }
    
    public Integer getCrsSeverityTh() {
        return filter.crs.severity.threshold;
    }
    
    public void setCrsSeverityTh(Integer t) {
        filter.crs.severity.threshold = t;
    }
    
    public Integer getCrsSeverityL0() {
        return filter.crs.severity.level0;
    }
    
    public void setCrsSeverityL0(Integer l) {
        filter.crs.severity.level0 = l;
    }
    
    public Integer getCrsSeverityL1() {
        return filter.crs.severity.level1;
    }
    
    public void setCrsSeverityL1(Integer l) {
        filter.crs.severity.level1 = l;
    }
    
    public Integer getCrsSeverityL2() {
        return filter.crs.severity.level2;
    }
    
    public void setCrsSeverityL2(Integer l) {
        filter.crs.severity.level2 = l;
    }
    
    public Boolean getCrsLog() {
        return filter.crs.getLog();
    }
    
    public void setCrsLog(Boolean log) {
        filter.crs.setLog(log);
    }
    
    public List<GrayList> getCrsGl() {
        return filter.crs.getGl();
    }
    
    public void setCrsGl(List<GrayList> l) {
        filter.crs.setGl(l);
    }
    
    // HIDS settings  
    public GrayList getSelectedHidsGl() {
        return selectedHidsGl;
    }
    
    public void setSelectedHidsGl(GrayList gl) {
        if (gl != null) {
            
            backupHidsGl = new GrayList();
            
            backupHidsGl.setId(gl.getId());
            
            backupHidsGl.setEvent(gl.getEvent());
            backupHidsGl.setHost(gl.getHost());
            backupHidsGl.setMatch(gl.getMatch());
            
            backupHidsGl.getAgr().setInPeriod(gl.getAgr().getInPeriod());
            backupHidsGl.getAgr().setReproduced(gl.getAgr().getReproduced());
            
            backupHidsGl.getRsp().setProfile(gl.getRsp().getProfile());
            backupHidsGl.getRsp().setNewCategory(gl.getRsp().getNewCategory());
            backupHidsGl.getRsp().setNewDesc(gl.getRsp().getNewDesc());
            backupHidsGl.getRsp().setNewEvent(gl.getRsp().getNewEvent());
            backupHidsGl.getRsp().setNewSeverity(gl.getRsp().getNewSeverity());
            backupHidsGl.getRsp().setNewType(gl.getRsp().getNewType());
            backupHidsGl.getRsp().setNewSource(gl.getRsp().getNewSource());
            
        }
        
        this.selectedHidsGl = gl;
    }
    
    public void deleteSelectedHidsGl() {
        filter.hids.gl.remove(selectedHidsGl);
        int i = 0;
        for (GrayList l: filter.hids.gl) {
            l.setId(i);
            i++;
        }
        selectedHidsGl = null;
    }
    
        
    public void createSelectedHidsGl() {
        GrayList l = new GrayList();
        
        if (alert != null && alert.getAlertSource().equals("Wazuh")) {
            l.event = alert.getEventId();
            l.host = alert.getAgentName();
        }
        
        l.setId(filter.hids.gl.size());
        filter.hids.gl.add(l);
        selectedHidsGl = null;
    }
    
    public void cancelSelectedHidsGl() {
        
        selectedHidsGl.setId(backupHidsGl.getId());
            
        selectedHidsGl.setEvent(backupHidsGl.getEvent());
        selectedHidsGl.setHost(backupHidsGl.getHost());
        selectedHidsGl.setMatch(backupHidsGl.getMatch());
        
        selectedHidsGl.getAgr().setInPeriod(backupHidsGl.getAgr().getInPeriod());
        selectedHidsGl.getAgr().setReproduced(backupHidsGl.getAgr().getReproduced());
        
        selectedHidsGl.getRsp().setProfile(backupHidsGl.getRsp().getProfile());
        selectedHidsGl.getRsp().setNewCategory(backupHidsGl.getRsp().getNewCategory());
        selectedHidsGl.getRsp().setNewDesc(backupHidsGl.getRsp().getNewDesc());
        selectedHidsGl.getRsp().setNewEvent(backupHidsGl.getRsp().getNewEvent());
        selectedHidsGl.getRsp().setNewSeverity(backupHidsGl.getRsp().getNewSeverity());
        selectedHidsGl.getRsp().setNewType(backupHidsGl.getRsp().getNewType());
        selectedHidsGl.getRsp().setNewSource(backupHidsGl.getRsp().getNewSource());
        
        selectedHidsGl = null;
    }
    
    public void updateSelectedHidsGl() {
        selectedHidsGl = null;
    }
    
    public Integer getHidsSeverityTh() {
        return filter.hids.severity.threshold;
    }
    
    public void setHidsSeverityTh(Integer t) {
        filter.hids.severity.threshold = t;
    }
    
    public Integer getHidsSeverityL0() {
        return filter.hids.severity.level0;
    }
    
    public void setHidsSeverityL0(Integer l) {
        filter.hids.severity.level0 = l;
    }
    
    public Integer getHidsSeverityL1() {
        return filter.hids.severity.level1;
    }
    
    public void setHidsSeverityL1(Integer l) {
        filter.hids.severity.level1 = l;
    }
    
    public Integer getHidsSeverityL2() {
        return filter.hids.severity.level2;
    }
    
    public void setHidsSeverityL2(Integer l) {
        filter.hids.severity.level2 = l;
    }
    
    public Boolean getHidsLog() {
        return filter.hids.getLog();
    }
    
    public void setHidsLog(Boolean log) {
        filter.hids.setLog(log);
    }
    
    public List<GrayList> getHidsGl() {
        return filter.hids.getGl();
    }
    
    public void setHidsGl(List<GrayList> l) {
        filter.hids.setGl(l);
    }
    
    // NIDS settings  
    public GrayList getSelectedNidsGl() {
        return selectedNidsGl;
    }
    
    public void setSelectedNidsGl(GrayList gl) {
        if (gl != null) {
            
            backupNidsGl = new GrayList();
            
            backupNidsGl.setId(gl.getId());
            
            backupNidsGl.setEvent(gl.getEvent());
            backupNidsGl.setHost(gl.getHost());
            backupNidsGl.setMatch(gl.getMatch());
            
            backupNidsGl.getAgr().setInPeriod(gl.getAgr().getInPeriod());
            backupNidsGl.getAgr().setReproduced(gl.getAgr().getReproduced());
            
            backupNidsGl.getRsp().setProfile(gl.getRsp().getProfile());
            backupNidsGl.getRsp().setNewCategory(gl.getRsp().getNewCategory());
            backupNidsGl.getRsp().setNewDesc(gl.getRsp().getNewDesc());
            backupNidsGl.getRsp().setNewEvent(gl.getRsp().getNewEvent());
            backupNidsGl.getRsp().setNewSeverity(gl.getRsp().getNewSeverity());
            backupNidsGl.getRsp().setNewType(gl.getRsp().getNewType());
            backupNidsGl.getRsp().setNewSource(gl.getRsp().getNewSource());
        }
        
        this.selectedNidsGl = gl;
    }
    
    public void deleteSelectedNidsGl() {
        filter.nids.gl.remove(selectedNidsGl);
        int i = 0;
        for (GrayList l: filter.nids.gl) {
            l.setId(i);
            i++;
        }
        selectedNidsGl = null;
    }
    
        
    public void createSelectedNidsGl() {
        GrayList l = new GrayList();
        
        if (alert != null && alert.getAlertSource().equals("Suricata")) {
            l.event = alert.getEventId();
            l.host = alert.getDstIp() + " " + alert.getSrcIp();
        }
        
        l.setId(filter.nids.gl.size());
        filter.nids.gl.add(l);
        selectedNidsGl = null;
    }
    
    public void cancelSelectedNidsGl() {
        
        selectedNidsGl.setId(backupNidsGl.getId());
            
        selectedNidsGl.setEvent(backupNidsGl.getEvent());
        selectedNidsGl.setHost(backupNidsGl.getHost());
        selectedNidsGl.setMatch(backupNidsGl.getMatch());
        
        selectedNidsGl.getAgr().setInPeriod(backupNidsGl.getAgr().getInPeriod());
        selectedNidsGl.getAgr().setReproduced(backupNidsGl.getAgr().getReproduced());
        
        selectedNidsGl.getRsp().setProfile(backupNidsGl.getRsp().getProfile());
        selectedNidsGl.getRsp().setNewCategory(backupNidsGl.getRsp().getNewCategory());
        selectedNidsGl.getRsp().setNewDesc(backupNidsGl.getRsp().getNewDesc());
        selectedNidsGl.getRsp().setNewEvent(backupNidsGl.getRsp().getNewEvent());
        selectedNidsGl.getRsp().setNewSeverity(backupNidsGl.getRsp().getNewSeverity());
        selectedNidsGl.getRsp().setNewType(backupNidsGl.getRsp().getNewType());
        selectedNidsGl.getRsp().setNewSource(backupNidsGl.getRsp().getNewSource());
    
        selectedNidsGl = null;
    }
    
    public void updateSelectedNidsGl() {
        selectedNidsGl = null;
    }
    
    public Integer getNidsSeverityTh() {
        return filter.nids.severity.threshold;
    }
    
    public void setNidsSeverityTh(Integer t) {
        filter.nids.severity.threshold = t;
    }
    
    public Integer getNidsSeverityL0() {
        return filter.nids.severity.level0;
    }
    
    public void setNidsSeverityL0(Integer l) {
        filter.nids.severity.level0 = l;
    }
    
    public Integer getNidsSeverityL1() {
        return filter.nids.severity.level1;
    }
    
    public void setNidsSeverityL1(Integer l) {
        filter.nids.severity.level1 = l;
    }
    
    public Integer getNidsSeverityL2() {
        return filter.nids.severity.level2;
    }
    
    public void setNidsSeverityL2(Integer l) {
        filter.nids.severity.level2 = l;
    }
    
    public Boolean getNidsLog() {
        return filter.nids.getLog();
    }
    
    public void setNidsLog(Boolean log) {
        filter.nids.setLog(log);
    }
    
    public List<GrayList> getNidsGl() {
        return filter.nids.getGl();
    }
    
    public void setNidsGl(List<GrayList> gl) {
        filter.nids.setGl(gl);
    }
    
    // WAF settings  
    public GrayList getSelectedWafGl() {
        return selectedWafGl;
    }
    
    public void setSelectedWafGl(GrayList gl) {
        if (gl != null) {
            
            backupWafGl = new GrayList();
            
            backupWafGl.setId(gl.getId());
            
            backupWafGl.setEvent(gl.getEvent());
            backupWafGl.setHost(gl.getHost());
            backupWafGl.setMatch(gl.getMatch());
            
            backupWafGl.getAgr().setInPeriod(gl.getAgr().getInPeriod());
            backupWafGl.getAgr().setReproduced(gl.getAgr().getReproduced());
            
            backupWafGl.getRsp().setProfile(gl.getRsp().getProfile());
            backupWafGl.getRsp().setNewCategory(gl.getRsp().getNewCategory());
            backupWafGl.getRsp().setNewDesc(gl.getRsp().getNewDesc());
            backupWafGl.getRsp().setNewEvent(gl.getRsp().getNewEvent());
            backupWafGl.getRsp().setNewSeverity(gl.getRsp().getNewSeverity());
            backupWafGl.getRsp().setNewType(gl.getRsp().getNewType());
            backupWafGl.getRsp().setNewSource(gl.getRsp().getNewSource());
            
        }
        
        this.selectedWafGl = gl;
    }
    
    public void deleteSelectedWafGl() {
        filter.waf.gl.remove(selectedWafGl);
        int i = 0;
        for (GrayList l: filter.waf.gl) {
            l.setId(i);
            i++;
        }
        selectedWafGl = null;
    }
    
        
    public void createSelectedWafGl() {
        GrayList l = new GrayList();
        
        if (alert != null && alert.getAlertSource().equals("Modsecurity")) {
            l.event = alert.getEventId();
            l.host = alert.getDstIp() + " " + alert.getSrcIp();
        }
        
        l.setId(filter.waf.gl.size());
        filter.waf.gl.add(l);
        selectedWafGl = null;
    }
    
    public void cancelSelectedWafGl() {
        
        selectedWafGl.setId(backupWafGl.getId());
            
        selectedWafGl.setEvent(backupWafGl.getEvent());
        selectedWafGl.setHost(backupWafGl.getHost());
        selectedWafGl.setMatch(backupWafGl.getMatch());
        
        selectedWafGl.getAgr().setInPeriod(backupWafGl.getAgr().getInPeriod());
        selectedWafGl.getAgr().setReproduced(backupWafGl.getAgr().getReproduced());
        
        selectedWafGl.getRsp().setProfile(backupWafGl.getRsp().getProfile());
        selectedWafGl.getRsp().setNewCategory(backupWafGl.getRsp().getNewCategory());
        selectedWafGl.getRsp().setNewDesc(backupWafGl.getRsp().getNewDesc());
        selectedWafGl.getRsp().setNewEvent(backupWafGl.getRsp().getNewEvent());
        selectedWafGl.getRsp().setNewSeverity(backupWafGl.getRsp().getNewSeverity());
        selectedWafGl.getRsp().setNewType(backupWafGl.getRsp().getNewType());
        selectedWafGl.getRsp().setNewSource(backupWafGl.getRsp().getNewSource());
    
        selectedWafGl = null;
    }
    
    public void updateSelectedWafGl() {
        selectedWafGl = null;
    }
    
    public Integer getWafSeverityTh() {
        return filter.waf.severity.threshold;
    }
    
    public void setWafSeverityTh(Integer t) {
        filter.waf.severity.threshold = t;
    }
    
    public Integer getWafSeverityL0() {
        return filter.waf.severity.level0;
    }
    
    public void setWafSeverityL0(Integer l) {
        filter.waf.severity.level0 = l;
    }
    
    public Integer getWafSeverityL1() {
        return filter.waf.severity.level1;
    }
    
    public void setWafSeverityL1(Integer l) {
        filter.waf.severity.level1 = l;
    }
    
    public Integer getWafSeverityL2() {
        return filter.waf.severity.level2;
    }
    
    public void setWafSeverityL2(Integer l) {
        filter.waf.severity.level2 = l;
    }
    
    public Boolean getWafLog() {
        return filter.waf.getLog();
    }
    
    public void setWafLog(Boolean log) {
        filter.waf.setLog(log);
    }
    
    public List<GrayList> getWafGl() {
        return filter.waf.getGl();
    }
    
    public void setWafGl(List<GrayList> gl) {
        filter.waf.setGl(gl);
    }
    
        
    private List<String> filterFiles;
    ProjectRepository lr;
    private Path dir;
    
    public void initFilterFiles() {
        
        filterFiles = new ArrayList();
        
        Node node = nodeFacade.findByNodeName(session_tenant,selectedNodeId);
        
        if (node == null) return;
        
        lr = new ProjectRepository(project);
        boolean ready = lr.initSensors(node, null);
                       
        if (!ready) return;
        
        dir = lr.getNodePath();
        
        if (Files.isDirectory(dir)) {
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
                for (Path path : stream) {
                    if (!Files.isDirectory(path))
                        filterFiles.add(path.getFileName().toString());
                }
            } catch (IOException e) {
                
            }
        }
        
        return;
    }
    
    public List<String> getFilterFiles() {
        
        return filterFiles;
    }
    
    public void setFilterFiles(List<String> f) {
        
        this.filterFiles = f;
    }
    
    private String filterFile = "";
    
    public String getFilterFile() {
        return filterFile;
    }
    
    public void setFilterFile(String f) {
        this.filterFile = f;
    }
    
    public void openFile() {
        
        FacesMessage message;
        
        filter = new Filter();
        filter_file = "";
        Path file = null;
        
        try {
        
            if (Files.isDirectory(dir)) {
                DirectoryStream<Path> stream = Files.newDirectoryStream(dir);
                for (Path path : stream) {
                    if (!Files.isDirectory(path)) {
                        if(filterFile.equals(path.getFileName().toString())) {
                            file = path;
                            break;
                        }
                    }
                }
            } 
            
            if (file == null) return;
        
            InputStream in = Files.newInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line = null;
            while ((line = reader.readLine()) != null) {
                filter_file = filter_file + line;
            }

            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Result: ","Filters have been loaded.");
                
        } catch (IOException ex) {
            
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error !", ex.toString());
        }
            
        FacesContext.getCurrentInstance().addMessage(null, message);
        
        convertFileToFilters();
    }
    
    
    void convertFileToFilters() {
        
        JSONObject obj = new JSONObject(filter_file);
        
        filter.refId = obj.getString("ref_id");
        filter.desc = obj.getString("filter_desc");
                
        JSONObject obj_sources = obj.getJSONObject("sources");
        
        // CRS
        JSONObject obj_crs = obj_sources.getJSONObject("crs");
        JSONObject obj_crs_severity = obj_crs.getJSONObject("severity");
        
        filter.crs.severity.threshold =  obj_crs_severity.getInt("threshold");
        filter.crs.severity.level0 =  obj_crs_severity.getInt("level0");
        filter.crs.severity.level1 =  obj_crs_severity.getInt("level1");
        filter.crs.severity.level2 =  obj_crs_severity.getInt("level2");
        
        filter.crs.log = obj_crs.getBoolean("log");
                
        JSONArray crs_gl = obj_crs.getJSONArray("gray_list");
        for (int i = 0; i < crs_gl.length(); i++) {
                        
            GrayList gl = new GrayList();
            
            gl.setId(i);
            
            gl.setEvent(crs_gl.getJSONObject(i).getString("event"));
            gl.setHost(crs_gl.getJSONObject(i).getString("container"));
            gl.setMatch(crs_gl.getJSONObject(i).getString("match"));
                        
            JSONObject crl = crs_gl.getJSONObject(i).getJSONObject("aggregate");
            
            gl.getAgr().setInPeriod(crl.getInt("in_period"));
            gl.getAgr().setReproduced(crl.getInt("reproduced"));
            
            crl = crs_gl.getJSONObject(i).getJSONObject("response");
            
            gl.getRsp().setProfile(crl.getString("profile"));
            gl.getRsp().setNewCategory(crl.getString("new_category"));
            gl.getRsp().setNewDesc(crl.getString("new_description"));
            gl.getRsp().setNewEvent(crl.getString("new_event"));
            gl.getRsp().setNewSeverity(crl.getInt("new_severity"));
            gl.getRsp().setNewType(crl.getString("new_type"));
            gl.getRsp().setNewSource(crl.getString("new_source"));
                        
            filter.crs.gl.add(gl);        
        }    
        
        // HIDS
        JSONObject obj_hids = obj_sources.getJSONObject("hids");
        JSONObject obj_hids_severity = obj_hids.getJSONObject("severity");
        
        filter.hids.severity.threshold =  obj_hids_severity.getInt("threshold");
        filter.hids.severity.level0 =  obj_hids_severity.getInt("level0");
        filter.hids.severity.level1 =  obj_hids_severity.getInt("level1");
        filter.hids.severity.level2 =  obj_hids_severity.getInt("level2");
        
        filter.hids.log = obj_hids.getBoolean("log");
                
        JSONArray hids_gl = obj_hids.getJSONArray("gray_list");
        for (int i = 0; i < hids_gl.length(); i++) {
                        
            GrayList gl = new GrayList();
            
            gl.setId(i);
            
            gl.setEvent(hids_gl.getJSONObject(i).getString("event"));
            gl.setHost(hids_gl.getJSONObject(i).getString("agent"));
            gl.setMatch(hids_gl.getJSONObject(i).getString("match"));
                        
            JSONObject crl = hids_gl.getJSONObject(i).getJSONObject("aggregate");
            
            gl.getAgr().setInPeriod(crl.getInt("in_period"));
            gl.getAgr().setReproduced(crl.getInt("reproduced"));
            
            crl = hids_gl.getJSONObject(i).getJSONObject("response");
            
            gl.getRsp().setProfile(crl.getString("profile"));
            gl.getRsp().setNewCategory(crl.getString("new_category"));
            gl.getRsp().setNewDesc(crl.getString("new_description"));
            gl.getRsp().setNewEvent(crl.getString("new_event"));
            gl.getRsp().setNewSeverity(crl.getInt("new_severity"));
            gl.getRsp().setNewType(crl.getString("new_type"));
            gl.getRsp().setNewSource(crl.getString("new_source"));
                        
            filter.hids.gl.add(gl);        
        }    
        
        // NIDS
        JSONObject obj_nids = obj_sources.getJSONObject("nids");
        JSONObject obj_nids_severity = obj_nids.getJSONObject("severity");
        
        filter.nids.severity.threshold =  obj_nids_severity.getInt("threshold");
        filter.nids.severity.level0 =  obj_nids_severity.getInt("level0");
        filter.nids.severity.level1 =  obj_nids_severity.getInt("level1");
        filter.nids.severity.level2 =  obj_nids_severity.getInt("level2");
        
        filter.nids.log = obj_nids.getBoolean("log");
                
        JSONArray nids_gl = obj_nids.getJSONArray("gray_list");
        for (int i = 0; i < nids_gl.length(); i++) {
                        
            GrayList gl = new GrayList();
            
            gl.setId(i);
            
            gl.setEvent(nids_gl.getJSONObject(i).getString("event"));
            gl.setHost(nids_gl.getJSONObject(i).getString("host"));
            gl.setMatch(nids_gl.getJSONObject(i).getString("match"));
            
            JSONObject crl = nids_gl.getJSONObject(i).getJSONObject("aggregate");
            
            gl.getAgr().setInPeriod(crl.getInt("in_period"));
            gl.getAgr().setReproduced(crl.getInt("reproduced"));
            
            crl = nids_gl.getJSONObject(i).getJSONObject("response");
            
            gl.getRsp().setProfile(crl.getString("profile"));
            gl.getRsp().setNewCategory(crl.getString("new_category"));
            gl.getRsp().setNewDesc(crl.getString("new_description"));
            gl.getRsp().setNewEvent(crl.getString("new_event"));
            gl.getRsp().setNewSeverity(crl.getInt("new_severity"));
            gl.getRsp().setNewType(crl.getString("new_type"));
            gl.getRsp().setNewSource(crl.getString("new_source"));
            
            filter.nids.gl.add(gl);        
        } 
        
        // WAF
        JSONObject obj_waf = obj_sources.getJSONObject("waf");
        JSONObject obj_waf_severity = obj_waf.getJSONObject("severity");
        
        filter.waf.severity.threshold =  obj_waf_severity.getInt("threshold");
        filter.waf.severity.level0 =  obj_waf_severity.getInt("level0");
        filter.waf.severity.level1 =  obj_waf_severity.getInt("level1");
        filter.waf.severity.level2 =  obj_waf_severity.getInt("level2");
        
        filter.waf.log = obj_waf.getBoolean("log");
                
        JSONArray waf_gl = obj_waf.getJSONArray("gray_list");
        for (int i = 0; i < waf_gl.length(); i++) {
                        
            GrayList gl = new GrayList();
            
            gl.setId(i);
            
            gl.setEvent(waf_gl.getJSONObject(i).getString("event"));
            gl.setHost(waf_gl.getJSONObject(i).getString("host"));
            gl.setMatch(waf_gl.getJSONObject(i).getString("match"));
                        
            JSONObject crl = waf_gl.getJSONObject(i).getJSONObject("aggregate");
            
            gl.getAgr().setInPeriod(crl.getInt("in_period"));
            gl.getAgr().setReproduced(crl.getInt("reproduced"));
            
            crl = waf_gl.getJSONObject(i).getJSONObject("response");
            
            gl.getRsp().setProfile(crl.getString("profile"));
            gl.getRsp().setNewCategory(crl.getString("new_category"));
            gl.getRsp().setNewDesc(crl.getString("new_description"));
            gl.getRsp().setNewEvent(crl.getString("new_event"));
            gl.getRsp().setNewSeverity(crl.getInt("new_severity"));
            gl.getRsp().setNewType(crl.getString("new_type"));
            gl.getRsp().setNewSource(crl.getString("new_source"));
            
            filter.waf.gl.add(gl);        
        }
    }
    
    private String newFile = "";
    
    public String getNewFile() {
        return newFile;
    }
    
    public void setNewFile(String f) {
        this.newFile = f;
    }
    
    public void saveFile() {
        
        FacesMessage message;
    
        try {
            
            convertFiltersToFile();
            
            if (filter_file.isEmpty() || lr == null) return;
            
            byte data[] = filter_file.getBytes();
            
            String colDir = lr.getNodeDir();
            
            Path p = Paths.get(colDir + newFile);
            
            Files.write(p, filter_file.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Result: ","Filters have been saved.");
                
        } catch (IOException ex) {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error !", ex.toString());
        }
            
        FacesContext.getCurrentInstance().addMessage(null, message);
        
    }
    
    public void uploadFile() {
        
        FacesMessage message;
        
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
            msg.setStringProperty("sensor", "all");
            msg.setStringProperty("msg_type", "byte");
            msg.setStringProperty("content_type", "filters");
            
            byte[] sompFilters = compress(filter_file);
                       
            msg.writeBytes(sompFilters);
            producer.send(msg);
            
            // Clean up
            session.close();
            connection.close();
            
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Result: ","Filters have been sent.");
            
                
        } catch ( IOException | JMSException e) {
            
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error !", e.toString());
        } 
        
        FacesContext.getCurrentInstance().addMessage(null, message);
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
    
    void convertFiltersToFile() {
        
        filter_file = new String("{ }");
        
        JSONObject obj = new JSONObject(filter_file);
        
        obj.put("ref_id", filter.refId);
        obj.put("filter_desc", filter.desc);
                
        JSONArray hnets = new JSONArray();
        
        List<HomeNetwork> listHnet = hnetFacade.findByRef(session_tenant);
        
        if (listHnet == null) listHnet = new ArrayList();
                
        for (int i = 0; i < listHnet.size(); i++) {
            
            JSONObject net = new JSONObject();
            
            net.put("network", listHnet.get(i).getNetwork());
            net.put("netmask", listHnet.get(i).getNetmask());
            net.put("node", listHnet.get(i).getNodeId());
            net.put("alert_suppress", (boolean) (listHnet.get(i).getAlertSuppress() > 0));
                                              
            hnets.put(net);
        } 
        
        obj.putOpt("home_net", hnets);
        
        JSONArray aliases = new JSONArray();
        
        List<Agent> listAliases = agentFacade.findAliases(session_tenant, selectedNodeId);
        
        if (listAliases == null) listAliases = new ArrayList();
                
        for (int i = 0; i < listAliases.size(); i++) {
            
            JSONObject al = new JSONObject();
            
            al.put("agent", listAliases.get(i).getName());
            al.put("host", listAliases.get(i).getHostLinked());
            al.put("container", listAliases.get(i).getContainerLinked());
            
            if (!listAliases.get(i).getIp().equals("indef")) al.put("ip", listAliases.get(i).getIp());
            else al.put("ip", listAliases.get(i).getIpLinked());
                                               
            aliases.put(al);
        } 
        
        obj.putOpt("alias", aliases);
        
        JSONObject sources = new JSONObject();
        obj.putOpt("sources", sources);
        
        // CRS
        JSONObject crs = new JSONObject();
        sources.putOpt("crs", crs);
        
        JSONObject crs_severity = new JSONObject();
        crs.putOpt("severity", crs_severity);
        
        crs_severity.put("threshold", filter.crs.severity.threshold);
        crs_severity.put("level0", filter.crs.severity.level0);
        crs_severity.put("level1", filter.crs.severity.level1);
        crs_severity.put("level2", filter.crs.severity.level2);
        
        crs.put("log", filter.crs.log);
        
        JSONArray crs_gl = new JSONArray();
                
        for (int i = 0; i < filter.crs.gl.size(); i++) {
            
            JSONObject gl = new JSONObject();
            
            gl.put("event", filter.crs.gl.get(i).getEvent());
            gl.put("container", filter.crs.gl.get(i).getHost());
            gl.put("match", filter.crs.gl.get(i).getMatch());
            
            JSONObject agr = new JSONObject();
            agr.put("in_period", filter.crs.gl.get(i).getAgr().getInPeriod());
            agr.put("reproduced", filter.crs.gl.get(i).getAgr().getReproduced());
            gl.putOpt("aggregate", agr);  
            
            JSONObject rsp = new JSONObject();
            rsp.put("profile", filter.crs.gl.get(i).getRsp().getProfile());
            rsp.put("new_category", filter.crs.gl.get(i).getRsp().getNewCategory());
            rsp.put("new_description", filter.crs.gl.get(i).getRsp().getNewDesc());
            rsp.put("new_event", filter.crs.gl.get(i).getRsp().getNewEvent());
            rsp.put("new_severity", filter.crs.gl.get(i).getRsp().getNewSeverity());
            rsp.put("new_type", filter.crs.gl.get(i).getRsp().getNewType());
            rsp.put("new_source", filter.crs.gl.get(i).getRsp().getNewSource());
            gl.putOpt("response", rsp);  
            
            crs_gl.put(gl);
        }  
        
        crs.putOpt("gray_list", crs_gl);
        
        // HIDS
        JSONObject hids = new JSONObject();
        sources.putOpt("hids", hids);
        
        JSONObject hids_severity = new JSONObject();
        hids.putOpt("severity", hids_severity);
        
        hids_severity.put("threshold", filter.hids.severity.threshold);
        hids_severity.put("level0", filter.hids.severity.level0);
        hids_severity.put("level1", filter.hids.severity.level1);
        hids_severity.put("level2", filter.hids.severity.level2);
        
        hids.put("log", filter.hids.log);
        
        JSONArray hids_gl = new JSONArray();
                
        for (int i = 0; i < filter.hids.gl.size(); i++) {
            
            JSONObject gl = new JSONObject();
            
            gl.put("event", filter.hids.gl.get(i).getEvent());
            gl.put("agent", filter.hids.gl.get(i).getHost());
            gl.put("match", filter.hids.gl.get(i).getMatch());
            
            JSONObject agr = new JSONObject();
            agr.put("in_period", filter.hids.gl.get(i).getAgr().getInPeriod());
            agr.put("reproduced", filter.hids.gl.get(i).getAgr().getReproduced());
            gl.putOpt("aggregate", agr);  
            
            JSONObject rsp = new JSONObject();
            rsp.put("profile", filter.hids.gl.get(i).getRsp().getProfile());
            rsp.put("new_category", filter.hids.gl.get(i).getRsp().getNewCategory());
            rsp.put("new_description", filter.hids.gl.get(i).getRsp().getNewDesc());
            rsp.put("new_event", filter.hids.gl.get(i).getRsp().getNewEvent());
            rsp.put("new_severity", filter.hids.gl.get(i).getRsp().getNewSeverity());
            rsp.put("new_type", filter.hids.gl.get(i).getRsp().getNewType());
            rsp.put("new_source", filter.hids.gl.get(i).getRsp().getNewSource());
            gl.putOpt("response", rsp);  
            
            hids_gl.put(gl);
        }  
        
        hids.putOpt("gray_list", hids_gl);
        
        
        // NIDS
        JSONObject nids = new JSONObject();
        sources.putOpt("nids", nids);
        
        JSONObject nids_severity = new JSONObject();
        nids.putOpt("severity", nids_severity);
        
        nids_severity.put("threshold", filter.nids.severity.threshold);
        nids_severity.put("level0", filter.nids.severity.level0);
        nids_severity.put("level1", filter.nids.severity.level1);
        nids_severity.put("level2", filter.nids.severity.level2);
        
        nids.put("log", filter.nids.log);
        
        JSONArray nids_gl = new JSONArray();
                
        for (int i = 0; i < filter.nids.gl.size(); i++) {
            
            JSONObject gl = new JSONObject();
            
            gl.put("event", filter.nids.gl.get(i).getEvent());
            gl.put("host", filter.nids.gl.get(i).getHost());
            gl.put("match", filter.nids.gl.get(i).getMatch());
            
            JSONObject agr = new JSONObject();
            agr.put("in_period", filter.nids.gl.get(i).getAgr().getInPeriod());
            agr.put("reproduced", filter.nids.gl.get(i).getAgr().getReproduced());
            gl.putOpt("aggregate", agr);  
            
            JSONObject rsp = new JSONObject();
            rsp.put("profile", filter.nids.gl.get(i).getRsp().getProfile());
            rsp.put("new_category", filter.nids.gl.get(i).getRsp().getNewCategory());
            rsp.put("new_description", filter.nids.gl.get(i).getRsp().getNewDesc());
            rsp.put("new_event", filter.nids.gl.get(i).getRsp().getNewEvent());
            rsp.put("new_severity", filter.nids.gl.get(i).getRsp().getNewSeverity());
            rsp.put("new_type", filter.nids.gl.get(i).getRsp().getNewType());
            rsp.put("new_source", filter.nids.gl.get(i).getRsp().getNewSource());
            gl.putOpt("response", rsp);  
            
            nids_gl.put(gl);
        }  
        
        nids.putOpt("gray_list", nids_gl);
        
        // WAF
        JSONObject waf = new JSONObject();
        sources.putOpt("waf", waf);
        
        JSONObject waf_severity = new JSONObject();
        waf.putOpt("severity", waf_severity);
        
        waf_severity.put("threshold", filter.waf.severity.threshold);
        waf_severity.put("level0", filter.waf.severity.level0);
        waf_severity.put("level1", filter.waf.severity.level1);
        waf_severity.put("level2", filter.waf.severity.level2);
        
        waf.put("log", filter.waf.log);
        
        JSONArray waf_gl = new JSONArray();
                
        for (int i = 0; i < filter.waf.gl.size(); i++) {
            
            JSONObject gl = new JSONObject();
            
            gl.put("event", filter.waf.gl.get(i).getEvent());
            gl.put("host", filter.waf.gl.get(i).getHost());
            gl.put("match", filter.waf.gl.get(i).getMatch());
            
            JSONObject agr = new JSONObject();
            agr.put("in_period", filter.waf.gl.get(i).getAgr().getInPeriod());
            agr.put("reproduced", filter.waf.gl.get(i).getAgr().getReproduced());
            gl.putOpt("aggregate", agr);  
            
            JSONObject rsp = new JSONObject();
            rsp.put("profile", filter.waf.gl.get(i).getRsp().getProfile());
            rsp.put("new_category", filter.waf.gl.get(i).getRsp().getNewCategory());
            rsp.put("new_description", filter.waf.gl.get(i).getRsp().getNewDesc());
            rsp.put("new_event", filter.waf.gl.get(i).getRsp().getNewEvent());
            rsp.put("new_severity", filter.waf.gl.get(i).getRsp().getNewSeverity());
            rsp.put("new_type", filter.waf.gl.get(i).getRsp().getNewType());
            rsp.put("new_source", filter.waf.gl.get(i).getRsp().getNewSource());
            gl.putOpt("response", rsp);  
            
            waf_gl.put(gl);
        }  
        
        waf.putOpt("gray_list", waf_gl);
        
        filter_file = obj.toString();
        
    }
    
} 