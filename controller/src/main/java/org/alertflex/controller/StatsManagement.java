/*
 *   Copyright 2021 Oleg Zharkov
 *
 *   Licensed under the Apache License, Version 2.0 (the "License").
 *   You may not use this file except in compliance with the License.
 *   A copy of the License is located at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   or in the "license" file accompanying this file. This file is distributed
 *   on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 *   express or implied. See the License for the specific language governing
 *   permissions and limitations under the License.
 */

package org.alertflex.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.zip.GZIPOutputStream;
import javax.persistence.PersistenceException;
import org.alertflex.common.ProjectRepository;
import org.alertflex.entity.Agent;
import org.alertflex.entity.Alert;
import org.alertflex.entity.NodeAlerts;
import org.alertflex.entity.NodeMonitor;
import org.alertflex.entity.NetStat;
import org.alertflex.entity.AgentSca;
import org.alertflex.entity.AgentVul;
import org.alertflex.entity.AgentsGroup;
import org.alertflex.entity.AlertPriority;
import org.alertflex.entity.Container;
import org.alertflex.entity.Hosts;
import org.alertflex.entity.NetCountries;
import org.alertflex.entity.Node;
import org.alertflex.entity.NodePK;
import org.alertflex.entity.Pod;
import org.alertflex.entity.Project;
import org.alertflex.entity.Sensor;
import org.alertflex.entity.SensorPK;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class StatsManagement {
    
    private static final long TIMEOUT = 1 * 1000;

    private static final Logger logger = LogManager.getLogger(StatsManagement.class);

    private InfoMessageBean eventBean;
    
    Project project;
    ProjectRepository pr;

    public StatsManagement(InfoMessageBean eb) {
        this.eventBean = eb;
        this.project = eventBean.getProject();
        this.pr = new ProjectRepository(project);
    }

    public void EvaluateStats(String stats) throws ParseException {

        try {

            JSONObject obj = new JSONObject(stats);
            JSONArray arr = obj.getJSONArray("stats");

            for (int i = 0; i < arr.length(); i++) {

                persistStats(arr.getJSONObject(i));

                //logger.error(arr.getJSONObject(i).toString());
            }

        } catch (JSONException e) {

            logger.error("alertflex_ctrl_exception", e);
            logger.error(stats);
        }
    }

    public void persistStats(JSONObject obj) throws ParseException {
        
        String probe = "";

        String stat = obj.toString();

        try {
            
            JSONObject data;
            JSONArray arr;
            
            String ref = eventBean.getRefId();
            String nodeName = eventBean.getNode();
            
            String agent;
            
            SimpleDateFormat formatter;
            Date date = new Date();

            String stats_type = obj.getString("type");
            

            switch (stats_type) {

                case "agents_list": 

                    boolean filtersFlag = false;

                    arr = obj.getJSONArray("data");
                    
                    formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                    for (int i = 0; i < arr.length(); i++) {

                        date = formatter.parse(arr.getJSONObject(i).getString("time_of_survey"));

                        agent = arr.getJSONObject(i).getString("name");
                        String id = arr.getJSONObject(i).getString("id");
                        String status = arr.getJSONObject(i).getString("status");
                        String ip = arr.getJSONObject(i).getString("ip");
                        String dateAdd = arr.getJSONObject(i).getString("date_add");
                        String version = arr.getJSONObject(i).getString("version");
                        String managerHost = arr.getJSONObject(i).getString("manager_host");
                        String osPlatform = arr.getJSONObject(i).getString("os_platform");
                        String osVersion = arr.getJSONObject(i).getString("os_version");
                        String osName = arr.getJSONObject(i).getString("os_name");
                        String group = arr.getJSONObject(i).getString("group");

                        Agent agExisting = eventBean.getAgentFacade().findAgentByName(ref, nodeName, agent);
                        
                        Date wasCreatedBefore =new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(dateAdd); 

                        if (agExisting == null) {
                            
                            Agent a = new Agent();

                            a.setRefId(ref);
                            a.setNode(nodeName);
                            a.setDateUpdate(date);
                            a.setStatus(status);
                            a.setAgentId(id);
                            a.setAgentKey("indef");
                            a.setName(agent);
                            a.setIp(ip);
                            a.setDateAdd(dateAdd);
                            a.setVersion(version);
                            a.setManager(managerHost);
                            a.setOsPlatform(osPlatform);
                            a.setOsVersion(osVersion);
                            a.setOsName(osName);
                            a.setGroupName(group);

                            eventBean.getAgentFacade().create(a);

                            createNewAgentAlert(a);

                        } else {

                            agExisting.setDateUpdate(date);
                            agExisting.setStatus(status);
                            agExisting.setDateAdd(dateAdd);
                            agExisting.setVersion(version);
                            agExisting.setManager(managerHost);
                            agExisting.setOsPlatform(osPlatform);
                            agExisting.setOsVersion(osVersion);
                            agExisting.setOsName(osName);
                            agExisting.setGroupName(group);

                            eventBean.getAgentFacade().edit(agExisting);
                        }
                    }
                    
                    break;
                    
                case "groups_list": 

                    arr = obj.getJSONArray("data");
                    
                    formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                    for (int i = 0; i < arr.length(); i++) {

                        date = formatter.parse(arr.getJSONObject(i).getString("time_of_survey"));

                        String group = arr.getJSONObject(i).getString("name");
                        int count = arr.getJSONObject(i).getInt("count");
                        
                        AgentsGroup agExisting = eventBean.getAgentsGroupFacade().findAgentsGroupByName(ref, nodeName, group);
                        
                        if (agExisting == null) {
                            
                            AgentsGroup g = new AgentsGroup();

                            g.setRefId(ref);
                            g.setNode(nodeName);
                            g.setGroupRef("indef");
                            g.setGroupName(group);
                            g.setAgentsCount(count);
                            g.setDateAdd(date);
                            g.setDateUpdate(date);

                            eventBean.getAgentsGroupFacade().create(g);

                        } else {

                            agExisting.setDateUpdate(date);
                            agExisting.setAgentsCount(count);
                            
                            eventBean.getAgentsGroupFacade().edit(agExisting);
                        }
                    }
                    
                    break;
                
                case "containers_list": 

                    arr = obj.getJSONArray("data");
                    
                    probe = obj.getString("probe");
                    
                    formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                    for (int i = 0; i < arr.length(); i++) {
                        
                        JSONArray names = arr.getJSONObject(i).getJSONArray("Names");
                        String containerNameTmp = "";
                        for (int j = 0; j < names.length(); j++) {
                            containerNameTmp = containerNameTmp + names.get(j).toString();
                        }
                        String containerName = "";
                        if (containerNameTmp.length() >= 1024) {
                            containerName = containerNameTmp.substring(0, 1022);    
                        } else containerName = containerNameTmp;
                        
                        String containerId = arr.getJSONObject(i).getString("Id");
                        String imageName = arr.getJSONObject(i).getString("Image");
                        String imageId = arr.getJSONObject(i).getString("ImageID");
                        
                        String command = "indef";
                        String commandTmp = arr.getJSONObject(i).getString("Command");
                        if (commandTmp.length() >= 1024) {
                            command = commandTmp.substring(0, 1022);
                        } else {
                            if (!commandTmp.isEmpty()) command = commandTmp;
                        }
                        
                        Long created = arr.getJSONObject(i).getLong("Created");
                        String state = arr.getJSONObject(i).getString("State");
                        String status = arr.getJSONObject(i).getString("Status");
                        
                        Container contExisting = eventBean.getContainerFacade().findByName(ref, nodeName, probe, imageId);

                        if (contExisting == null) {

                            Container c = new Container();

                            c.setRefId(ref);
                            c.setNode(nodeName);
                            c.setProbe(probe);
                            c.setContainerName(containerName);
                            c.setContainerId(containerId);
                            c.setImageName(imageName);
                            c.setImageId(imageId);
                            c.setCommand(command);
                            c.setState(state);
                            c.setStatus(status);
                            c.setReportAdded(new Date(created));
                            c.setReportUpdated(new Date());

                            eventBean.getContainerFacade().create(c);

                            createNewContainerAlert(c);

                        } else {
                            contExisting.setReportUpdated(new Date());
                            eventBean.getContainerFacade().edit(contExisting);
                        }
                    }

                    break;
                    
                case "pods_list": 

                    arr = obj.getJSONArray("data");
                    
                    probe = obj.getString("probe");
                    
                    formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    
                    for (int i = 0; i < arr.length(); i++) {
                        
                        String name = arr.getJSONObject(i).getString("name");
                        String nameSpace = arr.getJSONObject(i).getString("name_space");
                        String creationTimestamp = arr.getJSONObject(i).getString("creation_timestamp");
                        String uid = arr.getJSONObject(i).getString("uid");
                        String hostIp = arr.getJSONObject(i).getString("host_ip");
                        String podIp = arr.getJSONObject(i).getString("pod_ip");
                        String phase = arr.getJSONObject(i).getString("phase");
                        String nodeK8s = arr.getJSONObject(i).getString("node_name");
                        
                        Pod podExisting = eventBean.getPodFacade().findByName(ref, nodeName, name);

                        if (podExisting == null) {

                            Pod p = new Pod();

                            p.setRefId(ref);
                            p.setNodeProbe(nodeName);
                            p.setName(name);
                            p.setNameSpace(nameSpace);
                            p.setCreationTimestamp(creationTimestamp);
                            p.setUid(uid);
                            p.setHostIp(hostIp);
                            p.setPodIp(podIp);
                            p.setPhase(phase);
                            p.setNodeName(nodeK8s);
                            p.setReportAdded(new Date());
                            p.setReportUpdated(new Date());

                            eventBean.getPodFacade().create(p);

                        } else {
                            podExisting.setReportUpdated(new Date());
                            eventBean.getPodFacade().edit(podExisting);
                        }
                    }

                    break;
                
                case "sca":
                    
                    agent = obj.getString("agent");

                    data = obj.getJSONObject("data");

                    arr = data.getJSONArray("affected_items");
                    
                    formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

                    for (int i = 0; i < arr.length(); i++) {

                        int id = arr.getJSONObject(i).getInt("id");
                        
                        String title = arr.getJSONObject(i).getString("title");
                        
                         String policyId = arr.getJSONObject(i).getString("policy_id");
                        
                        String descTmp = "";
                        if (arr.getJSONObject(i).has("description")) {
                            
                            descTmp = arr.getJSONObject(i).getString("description");
                        
                        } else {
                        
                            if (arr.getJSONObject(i).has("file")) {
                                
                                descTmp = arr.getJSONObject(i).getString("file");
                                
                            } else {
                                
                                if (arr.getJSONObject(i).has("registry")) {
                                    
                                    descTmp = arr.getJSONObject(i).getString("registry");
                                
                                }
                            }
                        }
                        
                        String description = "";
                        if (descTmp.length() >= 1024) {
                            description = descTmp.substring(0, 1022);    
                        } else description = descTmp;
                        
                        String rationaleTmp = "";
                        if (arr.getJSONObject(i).has("rationale")) {
                            rationaleTmp = arr.getJSONObject(i).getString("rationale");
                        }
                        String rationale = "";
                        if (rationaleTmp.length() >= 2048) {
                            rationale = rationaleTmp.substring(0, 2046);    
                        } else rationale = rationaleTmp;
                        
                        
                        String remediationTmp = "";
                        if (arr.getJSONObject(i).has("remediation")) {
                            remediationTmp = arr.getJSONObject(i).getString("remediation");
                        }
                        String remediation = "";
                        if (remediationTmp.length() >= 2048) {
                            remediation = remediationTmp.substring(0, 2046);    
                        } else remediation = remediationTmp;
                        
                        AgentSca scaExisting = eventBean.getAgentScaFacade().findSca(ref, nodeName, agent, id, policyId);

                        if (scaExisting == null) {

                            AgentSca a = new AgentSca();

                            a.setRefId(ref);
                            a.setNode(nodeName);
                            a.setAgent(agent);
                            a.setScaId(id);
                            a.setPolicyId(policyId);
                            a.setDescription(description);
                            
                            
                            a.setRationale(rationale);
                            a.setRemediation(remediation);
                            a.setTitle(title);
                            a.setReportAdded(date);
                            a.setReportUpdated(date);

                            eventBean.getAgentScaFacade().create(a);

                           // createNewScaAlert(a);
                            
                        } else {
                            scaExisting.setReportUpdated(date);
                            eventBean.getAgentScaFacade().edit(scaExisting);
                        }
                    }

                    break;

                case "vulnerability":

                    JSONObject jv = obj.getJSONObject("data");
                    
                    formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                    AgentVul v = new AgentVul();

                    v.setRefId(eventBean.getRefId());
                    v.setNode(eventBean.getNode());
                    v.setAgent(jv.getString("agent"));
                    v.setVulnerability(jv.getString("cve"));
                    v.setSeverity(jv.getString("severity"));
                    
                    String vulnRef = "indef";
                    if (jv.has("reference")) {
                        vulnRef = jv.getString("reference");
                    }
                    if (vulnRef.length() >= 1024) {
                        String substrReferences = vulnRef.substring(0, 1022);
                        v.setVulnRef(substrReferences);
                    } else {
                        v.setVulnRef(vulnRef);
                    }
                    
                    String desc = "indef";
                    if (jv.has("description")) {
                        desc = jv.getString("description");
                    }
                    v.setDescription(desc);
                    
                    String title = "indef";
                    if (jv.has("title")) {
                        title = jv.getString("title");
                    }
                    v.setTitle(title);
                    
                    v.setPkgName(jv.getString("pkg_name"));
                    v.setPkgVersion(jv.getString("pkg_version"));
                    
                    date = formatter.parse(jv.getString("time_of_survey"));

                    v.setReportAdded(date);
                    v.setReportUpdated(date);

                    AgentVul vExisting = eventBean.getAgentVulFacade().findVulnerability(v.getRefId(),
                            v.getNode(),
                            v.getAgent(),
                            v.getVulnerability(),
                            v.getPkgName());

                    if (vExisting == null) {

                        eventBean.getAgentVulFacade().create(v);

                        createNewVulnAlert(v);
                        
                    } else {

                        vExisting.setReportUpdated(date);
                        eventBean.getAgentVulFacade().edit(vExisting);
                    }

                    break;

                case "node_alerts":
                    
                    if (project.getNodeTimerange() == 0) return;

                    JSONObject na = obj.getJSONObject("data");
                    
                    formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                    NodeAlerts node_alerts = new NodeAlerts();

                    node_alerts.setRefId(eventBean.getRefId());
                    node_alerts.setNode(eventBean.getNode());
                    node_alerts.setProbe(eventBean.getProbe());

                    node_alerts.setCrsAgg(na.getLong("crs_agg"));
                    node_alerts.setCrsFilter(na.getLong("crs_filter"));
                    node_alerts.setCrsS0(na.getLong("crs_s0"));
                    node_alerts.setCrsS1(na.getLong("crs_s1"));
                    node_alerts.setCrsS2(na.getLong("crs_s2"));
                    node_alerts.setCrsS3(na.getLong("crs_s3"));

                    node_alerts.setHidsAgg(na.getLong("hids_agg"));
                    node_alerts.setHidsFilter(na.getLong("hids_filter"));
                    node_alerts.setHidsS0(na.getLong("hids_s0"));
                    node_alerts.setHidsS1(na.getLong("hids_s1"));
                    node_alerts.setHidsS2(na.getLong("hids_s2"));
                    node_alerts.setHidsS3(na.getLong("hids_s3"));

                    node_alerts.setNidsAgg(na.getLong("nids_agg"));
                    node_alerts.setNidsFilter(na.getLong("nids_filter"));
                    node_alerts.setNidsS0(na.getLong("nids_s0"));
                    node_alerts.setNidsS1(na.getLong("nids_s1"));
                    node_alerts.setNidsS2(na.getLong("nids_s2"));
                    node_alerts.setNidsS3(na.getLong("nids_s3"));
                    
                    node_alerts.setWafAgg(na.getLong("waf_agg"));
                    node_alerts.setWafFilter(na.getLong("waf_filter"));
                    node_alerts.setWafS0(na.getLong("waf_s0"));
                    node_alerts.setWafS1(na.getLong("waf_s1"));
                    node_alerts.setWafS2(na.getLong("waf_s2"));
                    node_alerts.setWafS3(na.getLong("waf_s3"));

                    date = formatter.parse(na.getString("time_of_survey"));
                    node_alerts.setTimeOfSurvey(date);

                    eventBean.getNodeAlertsFacade().create(node_alerts);

                    break;
                
                case "node_monitor":
                    
                    if (project.getNodeTimerange() == 0) return;
                    
                    Node node = eventBean.getNodeFacade().findByNodeName(ref, nodeName);
                    
                    if (node == null) {
                        
                        pr.initNode(nodeName);

                        NodePK nodePK = new NodePK(ref, nodeName);
                        node = new Node();
                        node.setNodePK(nodePK);
                        node.setDescription("change desc");
                        node.setNodeType("indef");
                        node.setVpc("indef");
                        node.setCommandsControl(0);
                        node.setFiltersControl(0);
                        eventBean.getNodeFacade().create(node);
                    }
                    
                    Hosts host = eventBean.getHostsFacade().findHost(ref, eventBean.getProbe());
            
                    if (host == null) {

                        host = new Hosts();
                        host.setName(eventBean.getProbe());
                        host.setNode(nodeName);
                        host.setRefId(ref);
                        host.setCred("dummmy");
                        host.setAddress("127.0.0.1");
                        host.setPort(22);
                        host.setDescription("change desc");
                        host.setHostType("collector");
                        host.setAgent("indef");
                        host.setCloudInstance("indef");
                        eventBean.getHostsFacade().create(host);
                    }
                    
                    JSONObject nm = obj.getJSONObject("data");
                    
                    formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                    NodeMonitor node_monitor = new NodeMonitor();
                    
                    long eventsCRS = nm.getLong("crs");
                    if (eventsCRS > 0) checkSensor(ref, nodeName, 0);
                    
                    long eventsWaf = nm.getLong("waf");
                    if (eventsWaf > 0) checkSensor(ref, nodeName, 1);
                    
                    long eventsNids = nm.getLong("nids");
                    if (eventsNids > 0) checkSensor(ref, nodeName, 2);
                    
                    long eventsHids = nm.getLong("hids");
                    if (eventsHids > 0) checkSensor(ref, nodeName, 3);
                    
                    node_monitor.setRefId(ref);
                    node_monitor.setNode(nodeName);
                    node_monitor.setProbe(eventBean.getProbe());
                    node_monitor.setEventsHids(eventsHids);
                    node_monitor.setEventsNids(eventsNids);
                    node_monitor.setEventsWaf(eventsWaf);
                    node_monitor.setEventsCrs(eventsCRS);
                    node_monitor.setLogCounter(nm.getLong("log_counter"));
                    node_monitor.setLogVolume(nm.getLong("log_volume"));
                    node_monitor.setStatCounter(nm.getLong("stat_counter"));
                    node_monitor.setStatVolume(nm.getLong("stat_volume"));

                    date = formatter.parse(nm.getString("time_of_survey"));
                    node_monitor.setTimeOfSurvey(date);

                    eventBean.getNodeMonitorFacade().create(node_monitor);
                    
                    break;
                
                case "net_stat": 
                    
                    if (project.getNetTimerange() == 0) return;

                    arr = obj.getJSONArray("data");
                    
                    formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                    for (int i = 0; i < arr.length(); i++) {

                        NetStat net_stat = new NetStat();

                        net_stat.setRefId(eventBean.getRefId());
                        net_stat.setNode(eventBean.getNode());
                        net_stat.setProbe(eventBean.getProbe());
                        
                        net_stat.setSensor(arr.getJSONObject(i).getString("ids"));

                        net_stat.setInvalid(arr.getJSONObject(i).getLong("invalid"));

                        net_stat.setPkts(arr.getJSONObject(i).getLong("pkts"));

                        net_stat.setBytes(arr.getJSONObject(i).getLong("bytes"));

                        net_stat.setEthernet(arr.getJSONObject(i).getLong("ethernet"));

                        net_stat.setPpp(arr.getJSONObject(i).getLong("ppp"));

                        net_stat.setPppoe(arr.getJSONObject(i).getLong("pppoe"));

                        net_stat.setGre(arr.getJSONObject(i).getLong("gre"));

                        net_stat.setVlan(arr.getJSONObject(i).getLong("vlan"));

                        net_stat.setVlanQinq(arr.getJSONObject(i).getLong("vlan_qinq"));

                        net_stat.setMpls(arr.getJSONObject(i).getLong("mpls"));

                        net_stat.setIpv4(arr.getJSONObject(i).getLong("ipv4"));

                        net_stat.setIpv6(arr.getJSONObject(i).getLong("ipv6"));

                        net_stat.setTcp(arr.getJSONObject(i).getLong("tcp"));

                        net_stat.setUdp(arr.getJSONObject(i).getLong("udp"));

                        net_stat.setSctp(arr.getJSONObject(i).getLong("sctp"));

                        net_stat.setIcmpv4(arr.getJSONObject(i).getLong("icmpv4"));

                        net_stat.setIcmpv6(arr.getJSONObject(i).getLong("icmpv6"));

                        net_stat.setTeredo(arr.getJSONObject(i).getLong("teredo"));

                        net_stat.setIpv4InIpv6(arr.getJSONObject(i).getLong("ipv4_in_ipv6"));

                        net_stat.setIpv6InIpv6(arr.getJSONObject(i).getLong("ipv6_in_ipv6"));

                        date = formatter.parse(arr.getJSONObject(i).getString("time_of_survey"));
                        net_stat.setTimeOfSurvey(date);

                        eventBean.getNetStatFacade().create(net_stat);
                    }

                    break;
                    
                case "net_countries": {
                    
                    if (project.getNetTimerange() == 0) return;
                    
                    arr = obj.getJSONArray("data");
                    
                    formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        
                    for (int i = 0; i < arr.length(); i++) {
                            
                        NetCountries net_country = new NetCountries();
                    
                        net_country.setRefId(eventBean.getRefId());
                        net_country.setNode(eventBean.getNode());
                        net_country.setProbe(eventBean.getProbe());
                        net_country.setSensor(arr.getJSONObject(i).getString("sensor"));
                        net_country.setCountry(arr.getJSONObject(i).getString("country"));
                        net_country.setBytes(arr.getJSONObject(i).getLong("bytes"));
                        net_country.setSessions(arr.getJSONObject(i).getLong("sessions"));
                        date = formatter.parse(arr.getJSONObject(i).getString("time_of_survey"));
                        net_country.setTimeOfSurvey(date);
                        
                        eventBean.getNetCountriesFacade().create(net_country);
                    }
                    break;
                }
                    
                default:
                    break;
                    
            }
        } catch (JSONException | ParseException | PersistenceException e) {
            logger.error("alertflex_ctrl_exception", e);
            logger.error(stat);
        }
    }
    
    public void checkSensor(String ref, String node, int type) {
        
        String sensorName;
        String sensorType;
        String probe = eventBean.getProbe();
                
        switch (type) {
            case 0:
                sensorName = probe + ".crs";
                sensorType = "Falco";
                break;
                
            case 1:
                sensorName = probe + ".waf";
                sensorType = "ModSecurity";
                break;
                    
            case 2:
                sensorName = probe + ".nids";
                sensorType = "Suricata";
                break;
                
            case 3:
                sensorName = probe + ".hids";
                sensorType = "Wazuh";
                break;
            
            default:
                return;
        }

        Sensor s = eventBean.getSensorFacade().findSensorByName(ref, node, sensorName);

        if (s == null) {

            SensorPK sensorPK = new SensorPK(ref, sensorName, node);
            s = new Sensor(sensorPK);
            s.setDescription(sensorType + " sensor");
            s.setSensorType(sensorType);
            s.setHostName(probe);
            s.setAwsidsRulegroup("indef");
            s.setK8sPolicy("indef");
            s.setSuricataRule("indef");
            s.setStatus(1);
            eventBean.getSensorFacade().create(s);
        
        } else {
            
            if (s.getStatus() == 0) {
                s.setStatus(1);
                eventBean.getSensorFacade().edit(s);
            }
        }
    }

    public void createNewAgentAlert(Agent ag) {

        Alert a = new Alert();

        a.setRefId(ag.getRefId());
        a.setNodeId(ag.getNode());
        a.setAlertUuid(UUID.randomUUID().toString());

        AlertPriority ap = eventBean.getAlertPriorityFacade().findPriorityBySource(ag.getRefId(), "Alertflex");
        int sev = ap.getSeverityDefault();
        a.setAlertSeverity(sev);
        a.setEventSeverity(Integer.toString(sev));

        a.setAlertSource("Alertflex");
        a.setAlertType("HOST");
        a.setSensorId(ag.getManager());

        a.setDescription("new agent in the system with id: " + ag.getAgentId() + " and name: " + ag.getName());
        a.setEventId("1");
        a.setLocation("Response from controller");
        a.setAction("indef");
        a.setStatus("processed");
        a.setFilter("indef");
        a.setInfo("");

        a.setTimeEvent("");
        Date date = new Date();
        a.setTimeCollr(ag.getDateUpdate());
        a.setTimeCntrl(date);

        a.setAgentName(ag.getName());
        a.setUserName("indef");
        a.setCategories("new agent");
        a.setSrcIp("indef");
        a.setDstIp("indef");
        a.setDstPort(0);
        a.setSrcPort(0);
        a.setSrcHostname("indef");
        a.setDstHostname("indef");
        a.setRegValue("indef");
        a.setFileName("indef");
        a.setHashMd5("indef");
        a.setHashSha1("indef");
        a.setHashSha256("indef");
        a.setProcessId(0);
        a.setProcessName("indef");
        a.setProcessCmdline("indef");
        a.setProcessPath("indef");
        a.setUrlHostname("indef");
        a.setUrlPath("indef");
        a.setContainerId("indef");
        a.setContainerName("indef");
        a.setCloudInstance("indef");
        a.setIncidentExt("indef");

        eventBean.createAlert(a);
    }
    
    public void createNewContainerAlert(Container c) {

        Alert a = new Alert();

        a.setRefId(c.getRefId());
        a.setNodeId(c.getNode());
        a.setAlertUuid(UUID.randomUUID().toString());

        AlertPriority ap = eventBean.getAlertPriorityFacade().findPriorityBySource(c.getRefId(), "Alertflex");
        int sev = ap.getSeverityDefault();
        a.setAlertSeverity(sev);
        a.setEventSeverity(Integer.toString(sev));

        a.setAlertSource("Alertflex");
        a.setAlertType("HOST");
        a.setSensorId(c.getProbe());

        a.setDescription("new container in the system with id: " + c.getContainerId());
        a.setEventId("2");
        a.setLocation("Response from controller");
        a.setAction("indef");
        a.setStatus("processed");
        a.setFilter("indef");
        a.setInfo("");

        a.setTimeEvent("");
        a.setTimeCollr(c.getReportAdded());
        a.setTimeCntrl(new Date());

        a.setAgentName("indef");
        a.setUserName("indef");
        a.setCategories("new container");
        a.setSrcIp("indef");
        a.setDstIp("indef");
        a.setDstPort(0);
        a.setSrcPort(0);
        a.setSrcHostname("indef");
        a.setDstHostname("indef");
        a.setRegValue("indef");
        a.setFileName("indef");
        a.setHashMd5("indef");
        a.setHashSha1("indef");
        a.setHashSha256("indef");
        a.setProcessId(0);
        a.setProcessName("indef");
        a.setProcessCmdline("indef");
        a.setProcessPath("indef");
        a.setUrlHostname("indef");
        a.setUrlPath("indef");
        a.setContainerId("indef");
        a.setContainerName("indef");
        a.setCloudInstance("indef");
        a.setIncidentExt("indef");

        eventBean.createAlert(a);
    }
    
    public void createNewScaAlert(AgentSca as) {

        Alert a = new Alert();

        a.setRefId(as.getRefId());
        a.setNodeId(as.getNode());
        a.setAlertUuid(UUID.randomUUID().toString());

        AlertPriority ap = eventBean.getAlertPriorityFacade().findPriorityBySource(as.getRefId(), "Alertflex");
        int sev = ap.getSeverityDefault();
        a.setAlertSeverity(sev);
        a.setEventSeverity(Integer.toString(sev));

        a.setAlertSource("Wazuh");
        a.setAlertType("HOST");
        a.setSensorId("indef");

        a.setDescription(as.getTitle());
        a.setEventId("3");
        a.setLocation("indef");
        a.setAction("indef");
        a.setStatus("processed");
        a.setFilter("indef");
        a.setInfo("");

        a.setTimeEvent("");
        Date date = new Date();
        a.setTimeCollr(as.getReportUpdated());
        a.setTimeCntrl(date);

        a.setAgentName(as.getAgent());
        a.setUserName("indef");
        a.setCategories("sca");
        a.setSrcIp("indef");
        a.setDstIp("indef");
        a.setDstPort(0);
        a.setSrcPort(0);
        a.setSrcHostname("indef");
        a.setDstHostname("indef");
        a.setRegValue("indef");
        a.setFileName("indef");
        a.setHashMd5("indef");
        a.setHashSha1("indef");
        a.setHashSha256("indef");
        a.setProcessId(0);
        a.setProcessName("indef");
        a.setProcessCmdline("indef");
        a.setProcessPath("indef");
        a.setUrlHostname("indef");
        a.setUrlPath("indef");
        a.setContainerId("indef");
        a.setContainerName("indef");
        a.setCloudInstance("indef");
        a.setIncidentExt("indef");

        eventBean.createAlert(a);
    }
    
    public void createNewVulnAlert(AgentVul av) {

        Alert a = new Alert();

        a.setRefId(av.getRefId());
        a.setNodeId(av.getNode());
        a.setAlertUuid(UUID.randomUUID().toString());

        AlertPriority ap = eventBean.getAlertPriorityFacade().findPriorityBySource(av.getRefId(), "Alertflex");
        int sev = ap.getSeverityDefault();
        a.setAlertSeverity(sev);
        a.setEventSeverity(Integer.toString(sev));

        a.setAlertSource("Wazuh");
        a.setAlertType("HOST");
        a.setSensorId("indef");

        a.setDescription(av.getTitle());
        a.setEventId("4");
        a.setLocation(av.getVulnRef());
        a.setAction("indef");
        a.setStatus("processed");
        a.setFilter("indef");
        a.setInfo("");

        a.setTimeEvent("");
        Date date = new Date();
        a.setTimeCollr(av.getReportUpdated());
        a.setTimeCntrl(date);

        a.setAgentName(av.getAgent());
        a.setUserName("indef");
        a.setCategories("vulnerability");
        a.setSrcIp("indef");
        a.setDstIp("indef");
        a.setDstPort(0);
        a.setSrcPort(0);
        a.setSrcHostname("indef");
        a.setDstHostname("indef");
        a.setRegValue("indef");
        a.setFileName(av.getPkgName());
        a.setHashMd5("indef");
        a.setHashSha1("indef");
        a.setHashSha256("indef");
        a.setProcessId(0);
        a.setProcessName("indef");
        a.setProcessCmdline("indef");
        a.setProcessPath("indef");
        a.setUrlHostname("indef");
        a.setUrlPath("indef");
        a.setContainerId("indef");
        a.setContainerName("indef");
        a.setCloudInstance("indef");
        a.setIncidentExt("indef");

        eventBean.createAlert(a);
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
