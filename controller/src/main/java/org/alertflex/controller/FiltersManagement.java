/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alertflex.controller;


import org.alertflex.common.ProjectRepository;
import java.util.List;
import org.json.*;

import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.zip.GZIPOutputStream;
import javax.jms.BytesMessage;
import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import org.alertflex.entity.Project;
import org.alertflex.entity.Agent;
import org.alertflex.entity.HomeNetwork;
import org.alertflex.entity.Node;
import org.alertflex.filters.Filter;
import org.alertflex.filters.GrayList;
import org.apache.activemq.ActiveMQConnectionFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author root
 */
public class FiltersManagement {
    
    private static final Logger logger = LoggerFactory.getLogger(FiltersManagement.class);
    
    private InfoMessageBean eventBean;
    Project project;
    Node node;
    
    ProjectRepository lr;
    boolean filterFlag = false;
    String filterFile;
    Filter filter;

        
    public FiltersManagement (InfoMessageBean eb) {
        this.eventBean = eb;
        this.project = eventBean.getProject();
        this.filter = new Filter();
    }
    
    public Boolean readFilters() {
        
        node = eventBean.getNodeFacade().findByNodeName(eventBean.getRefId(),eventBean.getNode());
        
        if (node == null) return false;
        
        lr = new ProjectRepository(project);
        boolean ready = lr.initSensors(node, null);
                
        if (!ready) return false;
        
        String filterPath = lr.getNodeDir() + "filters.yaml";
        
        try {
        
           filterFile = new String(Files.readAllBytes(Paths.get(filterPath))); 
                
        } catch (IOException e) {
            logger.error("alertflex_ctrl_exception", e);
            return false;
        }
            
        JSONObject obj = new JSONObject(filterFile);
        
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
        
        return true;
    }
    
    public void saveFilters(String f) {
        
        try {
            
            String n = eventBean.getNode();
            String r = eventBean.getRefId();
            
            node = eventBean.getNodeFacade().findByNodeName(r,n);
        
            if (node == null) return;
            
            lr = new ProjectRepository(project);
            boolean ready = lr.initSensors(node, null);
            
            if (f.isEmpty() || !ready) return;
            
            String filterPath = lr.getNodeDir() + "filters.json";
            
            Path p = Paths.get(filterPath);
            
            Files.write(p, f.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    
                            
        } catch (IOException e) {
            logger.error("alertflex_ctrl_exception", e);
        }
            
    }
        
    
    public void uploadFilters() {
        
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
            
            byte[] sompFilters = compress(filterFile);
                       
            message.writeBytes(sompFilters);
            producer.send(message);
            
            // Clean up
            session.close();
            connection.close();
            
                
        } catch ( IOException | JMSException e) {
            logger.error("alertflex_ctrl_exception", e);
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
    
    void updateFilters() {
        
        filterFile = new String("{ }");
        
        JSONObject obj = new JSONObject(filterFile);
        
        obj.put("ref_id", filter.refId);
        obj.put("filter_desc", filter.desc);
                
        JSONArray hnets = new JSONArray();
        
        List<HomeNetwork> listHnet = eventBean.getHomeNetworkFacade().findByRef(eventBean.getRefId());
        
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
        
        List<Agent> listAliases = eventBean.getAgentFacade().findAliases(eventBean.getRefId(),eventBean.getNode());
        
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
        
        filterFile = obj.toString();
        
    }
    
}
