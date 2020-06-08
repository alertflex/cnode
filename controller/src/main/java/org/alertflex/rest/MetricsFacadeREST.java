/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alertflex.rest;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.alertflex.entity.Node;
import org.alertflex.entity.Sensor;
import org.alertflex.entity.NodeFilters;
import org.alertflex.entity.NodeAlerts;
import org.alertflex.entity.NodeMonitor;
import org.alertflex.entity.NetStat;
import org.alertflex.entity.Project;
import org.alertflex.facade.NodeFacade;
import org.alertflex.facade.SensorFacade;
import org.alertflex.facade.NodeFiltersFacade;
import org.alertflex.facade.NodeAlertsFacade;
import org.alertflex.facade.NodeMonitorFacade;
import org.alertflex.facade.NetStatFacade;
import org.alertflex.facade.ProjectFacade;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author root
 */
@Stateless
@Path("metrics")
@Produces(MediaType.TEXT_PLAIN)
public class MetricsFacadeREST {

    private static final Logger logger = LoggerFactory.getLogger(MetricsFacadeREST.class);
    
    @EJB
    private ProjectFacade projectFacade;
    
    @EJB
    private NodeFacade nodeFacade;
    
    @EJB
    private SensorFacade sensorFacade;
    
    @EJB
    private NodeMonitorFacade nodeMonitorFacade;
        
    @EJB
    private NodeFiltersFacade nodeFiltersFacade;
    
    @EJB
    private NodeAlertsFacade nodeAlertsFacade;
    
    @EJB
    private NetStatFacade netStatFacade;
    
    StringBuilder sb;
    
    public MetricsFacadeREST() {
        
    }
    
    @GET
    public String metrics() {
        
        List<Project> projectList = projectFacade.findAll();
        
        if (projectList == null || projectList.isEmpty()) return "";
        
        if (projectList.get(0).getStatRest() == 0) return "";
        
        String prj = projectList.get(0).getRefId();
        
        List<Node> nodeList = nodeFacade.findByRef(prj);
        
        if (nodeList == null || nodeList.isEmpty()) return sb.toString();
        
        sb = new StringBuilder("# HELP Alertflex metrics\n");
        
        for(Node node: nodeList) {
            
            String nodeId = node.getNodePK().getName();
               
            if (nodeId != null || !nodeId.isEmpty()) {
                
                Date endDate = new Date();
                long millis = endDate.getTime() - 300*1000;
                Date startDate = new Date(millis);
                Timestamp end = new Timestamp(endDate.getTime());
                Timestamp start = new Timestamp(startDate.getTime());
                
                NodeFilters nf  = nodeFiltersFacade.getLastRecord(prj, nodeId, start, end);
                if (nf != null) metricsNodeFilters(nodeId, nf);
                
                NodeAlerts nal  = nodeAlertsFacade.getLastRecord(prj, nodeId, start, end);
                if (nal != null) metricsNodeAlerts(nodeId, nal);
                
                NodeMonitor nmon  = nodeMonitorFacade.getLastRecord(prj, nodeId, start, end);
                if (nmon != null) metricsNodeMonitor(nodeId, nmon);
                
                try {
                    List<Sensor> sensorsList = sensorFacade.findSensorsByType(prj, nodeId, "suricata");
                
                if (sensorsList != null || !sensorsList.isEmpty()) {
                    for (Sensor sensor: sensorsList) {
                        
                        String nids = sensor.getSensorPK().getName();
                        
                        if (nids != null || !nids.isEmpty()) {
                            
                            NetStat ns = netStatFacade.getLastRecord(prj, nodeId, nids);
                            if (ns != null) statNetwork(nodeId, nids, ns);
                        }
                    }
                }
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
                
            }
        }
       
        return sb.toString();
    }
    
    private void metricsNodeMonitor(String nodeId, NodeMonitor nm) {
        
        StringBuilder template = new StringBuilder("alertflex_node_monitor{node=\"");
        template.append(nodeId);
        template.append("\",type=\"");
        
        sb.append(template.toString());
        sb.append("crs\"} ");
        sb.append(Long.toString(nm.getEventsCrs()));
        sb.append("\n");
        
        sb.append(template.toString());
        sb.append("hids\"} ");
        sb.append(Long.toString(nm.getEventsHids()));
        sb.append("\n");
        
        sb.append(template.toString());
        sb.append("nids\"} ");
        sb.append(Long.toString(nm.getEventsNids()));
        sb.append("\n");
        
        sb.append(template.toString());
        sb.append("waf\"} ");
        sb.append(Long.toString(nm.getEventsWaf()));
        sb.append("\n");
        
        sb.append(template.toString());
        sb.append("misc\"} ");
        sb.append(Long.toString(nm.getEventsMisc()));
        sb.append("\n");
        
        sb.append(template.toString());
        sb.append("log_counter\"} ");
        sb.append(Long.toString(nm.getLogCounter()));
        sb.append("\n");
        
        sb.append(template.toString());
        sb.append("log_volume\"} ");
        sb.append(Long.toString(nm.getLogVolume()));
        sb.append("\n");
        
        sb.append(template.toString());
        sb.append("stat_counter\"} ");
        sb.append(Long.toString(nm.getStatCounter()));
        sb.append("\n");
        
        sb.append(template.toString());
        sb.append("stat_volume\"} ");
        sb.append(Long.toString(nm.getStatVolume()));
        sb.append("\n");
        
        sb.append("\n");
    }
    
    private void metricsNodeAlerts(String nodeId, NodeAlerts na) {
        
        StringBuilder template = new StringBuilder("alertflex_node_alerts{node=\"");
        template.append(nodeId);
        template.append("\",type=\"");
        
        sb.append(template.toString());
        sb.append("crs_agg\"} ");
        sb.append(Long.toString(na.getCrsAgg()));
        sb.append("\n");
        
        sb.append(template.toString());
        sb.append("crs_filter\"} ");
        sb.append(Long.toString(na.getCrsFilter()));
        sb.append("\n");
        
        sb.append(template.toString());
        sb.append("crs_s0\"} ");
        sb.append(Long.toString(na.getCrsS0()));
        sb.append("\n");
        
        sb.append(template.toString());
        sb.append("crs_s1\"} ");
        sb.append(Long.toString(na.getCrsS1()));
        sb.append("\n");
        
        sb.append(template.toString());
        sb.append("crs_s2\"} ");
        sb.append(Long.toString(na.getCrsS2()));
        sb.append("\n");
        
        sb.append(template.toString());
        sb.append("crs_s3\"} ");
        sb.append(Long.toString(na.getCrsS3()));
        sb.append("\n");
        
        sb.append(template.toString());
        sb.append("hids_agg\"} ");
        sb.append(Long.toString(na.getHidsAgg()));
        sb.append("\n");
        
        sb.append(template.toString());
        sb.append("hids_filter\"} ");
        sb.append(Long.toString(na.getHidsFilter()));
        sb.append("\n");
        
        sb.append(template.toString());
        sb.append("hids_s0\"} ");
        sb.append(Long.toString(na.getHidsS0()));
        sb.append("\n");
        
        sb.append(template.toString());
        sb.append("hids_s1\"} ");
        sb.append(Long.toString(na.getHidsS1()));
        sb.append("\n");
        
        sb.append(template.toString());
        sb.append("hids_s2\"} ");
        sb.append(Long.toString(na.getHidsS2()));
        sb.append("\n");
        
        sb.append(template.toString());
        sb.append("hids_s3\"} ");
        sb.append(Long.toString(na.getHidsS3()));
        sb.append("\n");
        
        sb.append(template.toString());
        sb.append("nids_agg\"} ");
        sb.append(Long.toString(na.getNidsAgg()));
        sb.append("\n");
        
        sb.append(template.toString());
        sb.append("nids_filter\"} ");
        sb.append(Long.toString(na.getNidsFilter()));
        sb.append("\n");
        
        sb.append(template.toString());
        sb.append("nids_s0\"} ");
        sb.append(Long.toString(na.getNidsS0()));
        sb.append("\n");
        
        sb.append(template.toString());
        sb.append("nids_s1\"} ");
        sb.append(Long.toString(na.getNidsS1()));
        sb.append("\n");
        
        sb.append(template.toString());
        sb.append("nids_s2\"} ");
        sb.append(Long.toString(na.getNidsS2()));
        sb.append("\n");
        
        sb.append(template.toString());
        sb.append("nids_s3\"} ");
        sb.append(Long.toString(na.getNidsS3()));
        sb.append("\n");
        
        sb.append(template.toString());
        sb.append("waf_agg\"} ");
        sb.append(Long.toString(na.getWafAgg()));
        sb.append("\n");
        
        sb.append(template.toString());
        sb.append("waf_filter\"} ");
        sb.append(Long.toString(na.getWafFilter()));
        sb.append("\n");
        
        sb.append(template.toString());
        sb.append("waf_s0\"} ");
        sb.append(Long.toString(na.getWafS0()));
        sb.append("\n");
        
        sb.append(template.toString());
        sb.append("waf_s1\"} ");
        sb.append(Long.toString(na.getWafS1()));
        sb.append("\n");
        
        sb.append(template.toString());
        sb.append("waf_s2\"} ");
        sb.append(Long.toString(na.getWafS2()));
        sb.append("\n");
        
        sb.append(template.toString());
        sb.append("waf_s3\"} ");
        sb.append(Long.toString(na.getWafS3()));
        sb.append("\n");
        
        sb.append("\n");
    }
    
    private void metricsNodeFilters(String nodeId, NodeFilters nf) {
        
        StringBuilder template = new StringBuilder("alertflex_node_filters{node=\"");
        template.append(nodeId);
        template.append("\",type=\"");
        
        sb.append(template.toString());
        sb.append("agent_list\"} ");
        sb.append(Long.toString(nf.getAgentList()));
        sb.append("\n");
        
        sb.append(template.toString());
        sb.append("hnet_list\"} ");
        sb.append(Long.toString(nf.getHnetList()));
        sb.append("\n");
        
        sb.append(template.toString());
        sb.append("crs_filters\"} ");
        sb.append(Long.toString(nf.getCrsFilters()));
        sb.append("\n");
        
        sb.append(template.toString());
        sb.append("nids_filters\"} ");
        sb.append(Long.toString(nf.getHidsFilters()));
        sb.append("\n");
        
        sb.append(template.toString());
        sb.append("nids_filters\"} ");
        sb.append(Long.toString(nf.getNidsFilters()));
        sb.append("\n");
        
        sb.append(template.toString());
        sb.append("waf_filters\"} ");
        sb.append(Long.toString(nf.getWafFilters()));
        sb.append("\n");
        
        sb.append("\n");
    }
    
    private void statNetwork(String nodeId, String ids, NetStat ns) {
        
        StringBuilder template = new StringBuilder("alertflex_network_stat{node=\"");
        template.append(nodeId);
        template.append("\",ids=\"");
        template.append(ids);
        template.append("\",type=\"");
        
        sb.append(template.toString());
        sb.append("invalid\"} ");
        sb.append(Long.toString(ns.getInvalid()));
        sb.append("\n");
        
        sb.append(template.toString());
        sb.append("pkts\"} ");
        sb.append(Long.toString(ns.getPkts()));
        sb.append("\n");
        
        sb.append(template.toString());
        sb.append("bytes\"} ");
        sb.append(Long.toString(ns.getBytes()));
        sb.append("\n");
        
        sb.append(template.toString());
        sb.append("ethernet\"} ");
        sb.append(Long.toString(ns.getEthernet()));
        sb.append("\n");
        
        sb.append(template.toString());
        sb.append("ppp\"} ");
        sb.append(Long.toString(ns.getPpp()));
        sb.append("\n");
        
        sb.append(template.toString());
        sb.append("pppoe\"} ");
        sb.append(Long.toString(ns.getPppoe()));
        sb.append("\n");
        
        sb.append(template.toString());
        sb.append("gre\"} ");
        sb.append(Long.toString(ns.getGre()));
        sb.append("\n");
        
        sb.append(template.toString());
        sb.append("vlan\"} ");
        sb.append(Long.toString(ns.getVlan()));
        sb.append("\n");
        
        sb.append(template.toString());
        sb.append("vlan_qinq\"} ");
        sb.append(Long.toString(ns.getVlanQinq()));
        sb.append("\n");
        
        sb.append(template.toString());
        sb.append("mpls\"} ");
        sb.append(Long.toString(ns.getMpls()));
        sb.append("\n");
        
        sb.append(template.toString());
        sb.append("ipv4\"} ");
        sb.append(Long.toString(ns.getIpv4()));
        sb.append("\n");
        
        sb.append(template.toString());
        sb.append("ipv6\"} ");
        sb.append(Long.toString(ns.getIpv6()));
        sb.append("\n");
        
        sb.append(template.toString());
        sb.append("tcp\"} ");
        sb.append(Long.toString(ns.getTcp()));
        sb.append("\n");
        
        sb.append(template.toString());
        sb.append("udp\"} ");
        sb.append(Long.toString(ns.getUdp()));
        sb.append("\n");
        
        sb.append(template.toString());
        sb.append("sctp\"} ");
        sb.append(Long.toString(ns.getSctp()));
        sb.append("\n");
        
        sb.append(template.toString());
        sb.append("icmpv4\"} ");
        sb.append(Long.toString(ns.getIcmpv4()));
        sb.append("\n");
        
        sb.append(template.toString());
        sb.append("icmpv6\"} ");
        sb.append(Long.toString(ns.getIcmpv6()));
        sb.append("\n");
        
        sb.append(template.toString());
        sb.append("teredo\"} ");
        sb.append(Long.toString(ns.getTeredo()));
        sb.append("\n");
        
        sb.append(template.toString());
        sb.append("ipv4_in_ipv6\"} ");
        sb.append(Long.toString(ns.getIpv4InIpv6()));
        sb.append("\n");
        
        sb.append(template.toString());
        sb.append("ipv6_in_ipv6\"} ");
        sb.append(Long.toString(ns.getIpv6InIpv6()));
        sb.append("\n");
        
        sb.append("\n");
    }
}
