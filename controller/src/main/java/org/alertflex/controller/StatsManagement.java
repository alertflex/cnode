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
import org.alertflex.supp.ProjectRepository;
import org.alertflex.entity.Agent;
import org.alertflex.entity.Alert;
import org.alertflex.entity.NodeAlerts;
import org.alertflex.entity.NodeMonitor;
import org.alertflex.entity.AgentMisconfig;
import org.alertflex.entity.AgentVul;
import org.alertflex.entity.AlertPriority;
import org.alertflex.entity.Container;
import org.alertflex.entity.Pod;
import org.alertflex.entity.Project;
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
                            a.setProbe(managerHost);
                            a.setOsPlatform(osPlatform);
                            a.setOsVersion(osVersion);
                            a.setOsName(osName);
                            
                            eventBean.getAgentFacade().create(a);

                            createNewAgentAlert(a);

                        } else {

                            agExisting.setDateUpdate(date);
                            agExisting.setStatus(status);
                            agExisting.setDateAdd(dateAdd);
                            agExisting.setVersion(version);
                            agExisting.setProbe(managerHost);
                            agExisting.setOsPlatform(osPlatform);
                            agExisting.setOsVersion(osVersion);
                            agExisting.setOsName(osName);
                            
                            eventBean.getAgentFacade().edit(agExisting);
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
                            p.setNode(nodeName);
                            p.setName(name);
                            p.setNamespace(nameSpace);
                            p.setCreationTimestamp(creationTimestamp);
                            p.setUid(uid);
                            p.setHostIp(hostIp);
                            p.setPodIp(podIp);
                            p.setPhase(phase);
                            p.setK8sNode(nodeK8s);
                            p.setReportAdded(new Date());
                            p.setReportUpdated(new Date());

                            eventBean.getPodFacade().create(p);

                        } else {
                            podExisting.setReportUpdated(new Date());
                            eventBean.getPodFacade().edit(podExisting);
                        }
                    }

                    break;
                
                case "misconfig":
                    
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
                        
                        AgentMisconfig am = new AgentMisconfig();
                        am.setRefId(ref);
                        am.setNode(nodeName);
                        String probeName = eventBean.getHost() + ".hids";
                        am.setProbe(probeName);
                        am.setAgent(agent);
                        am.setScaId(id);
                        am.setPolicyId(policyId);
                        am.setDescription(description);
                        am.setRationale(rationale);
                        am.setRemediation(remediation);
                        am.setTitle(title);
                        am.setReportAdded(date);
                        am.setReportUpdated(date);
                        am.setStatus("processed");
                        
                        AlertPriority ap = eventBean.getAlertPriorityFacade().findPriorityBySource(am.getRefId(), "WazuhMisconfig");
                        int alertSeverity = ap.getSeverityDefault();
                        String severity = "low";
            
                        switch (alertSeverity) {
                            case 0 : severity = "info";
                                break;
                            case 1 : severity = "low";
                                break;
                            case 2 : severity = "medium";
                                break;
                            case 3 : severity = "high";
                                break;
                        }
                        
                        am.setSeverity(severity);
                        String alertUuid = "";
                        
                        AgentMisconfig amExisting = eventBean.getAgentMisconfigFacade().findMisconfigurations(ref, nodeName, agent, id, policyId);

                        if (amExisting == null) {
                            alertUuid = createAgentMisconfigAlert(am, alertSeverity);
                            if (alertUuid != null) am.setAlertUuid(alertUuid);
                            else am.setAlertUuid("indef");
                            eventBean.getAgentMisconfigFacade().create(am);
                
                        } else {
                            switch (project.getAlertType()) {
                    
                                case 1: // all-existing
                        
                                    alertUuid = createAgentMisconfigAlert(am, alertSeverity);
                                    if (alertUuid != null) amExisting.setAlertUuid(alertUuid);
                                    else amExisting.setAlertUuid("indef");
                            
                                    amExisting.setReportUpdated(date);
                                    eventBean.getAgentMisconfigFacade().edit(amExisting);
                        
                                    break;
                        
                                case 2: // non confirmed 
                        
                                    if (!amExisting.getStatus().equals("confirmed")) {
                                        alertUuid = createAgentMisconfigAlert(am, alertSeverity);
                                        if (alertUuid != null) amExisting.setAlertUuid(alertUuid);
                                        else amExisting.setAlertUuid("indef");
                                    }
                            
                                    amExisting.setReportUpdated(date);
                                    eventBean.getAgentMisconfigFacade().edit(amExisting);
                            
                                    break;
                        
                                case 3: // new
                        
                                    amExisting.setReportUpdated(date);
                                    eventBean.getAgentMisconfigFacade().edit(amExisting);
                            
                                    break;
                            }
                        }
                    }

                    break;

                case "vulnerability":

                    JSONObject jv = obj.getJSONObject("data");
                    
                    formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                    AgentVul av = new AgentVul();

                    av.setRefId(eventBean.getRefId());
                    av.setNode(eventBean.getNode());
                    String probeName = eventBean.getHost() + ".hids";
                    av.setProbe(probeName);
                    av.setAgent(jv.getString("agent"));
                    av.setVulnerability(jv.getString("cve"));
                    av.setSeverity(jv.getString("severity"));
                    
                    String vulnRef = "indef";
                    if (jv.has("reference")) {
                        vulnRef = jv.getString("reference");
                    }
                    if (vulnRef.length() >= 1024) {
                        String substrReferences = vulnRef.substring(0, 1022);
                        av.setVulnRef(substrReferences);
                    } else {
                        av.setVulnRef(vulnRef);
                    }
                    
                    String desc = "indef";
                    if (jv.has("description")) {
                        desc = jv.getString("description");
                    }
                    av.setDescription(desc);
                    
                    String title = "indef";
                    if (jv.has("title")) {
                        title = jv.getString("title");
                    }
                    av.setTitle(title);
                    
                    av.setPkgName(jv.getString("pkg_name"));
                    av.setPkgVersion(jv.getString("pkg_version"));
                    
                    date = formatter.parse(jv.getString("time_of_survey"));

                    av.setReportAdded(date);
                    av.setReportUpdated(date);
                    av.setStatus("processed");
                    
                    String alertUuid = "";
                    
                    AgentVul avExisting = eventBean.getAgentVulFacade().findVulnerability(av.getRefId(),
                            av.getNode(),
                            av.getAgent(),
                            av.getVulnerability(),
                            av.getPkgName());

                    if (avExisting == null) {
                            alertUuid = createAgentVulnAlert(av);
                            if (alertUuid != null) av.setAlertUuid(alertUuid);
                            else av.setAlertUuid("indef");
                            eventBean.getAgentVulFacade().create(av);
                
                        } else {
                            switch (project.getAlertType()) {
                    
                                case 1: // all-existing
                        
                                    alertUuid = createAgentVulnAlert(av);
                                    if (alertUuid != null) avExisting.setAlertUuid(alertUuid);
                                    else avExisting.setAlertUuid("indef");
                            
                                    avExisting.setReportUpdated(date);
                                    eventBean.getAgentVulFacade().edit(avExisting);
                        
                                    break;
                        
                                case 2: // non confirmed 
                        
                                    if (!avExisting.getStatus().equals("confirmed")) {
                                        alertUuid = createAgentVulnAlert(av);
                                        if (alertUuid != null) avExisting.setAlertUuid(alertUuid);
                                        else avExisting.setAlertUuid("indef");
                                    }
                            
                                    avExisting.setReportUpdated(date);
                                    eventBean.getAgentVulFacade().edit(avExisting);
                            
                                    break;
                        
                                case 3: // new
                        
                                    avExisting.setReportUpdated(date);
                                    eventBean.getAgentVulFacade().edit(avExisting);
                            
                                    break;
                            }
                        }

                    break;

                case "node_alerts":
                    
                    if (project.getNodeTimerange() == 0) return;

                    JSONObject na = obj.getJSONObject("data");
                    
                    formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                    NodeAlerts node_alerts = new NodeAlerts();

                    node_alerts.setRefId(eventBean.getRefId());
                    node_alerts.setNode(eventBean.getNode());
                    node_alerts.setHost(eventBean.getHost());

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
                    
                    JSONObject nm = obj.getJSONObject("data");
                    
                    formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                    NodeMonitor node_monitor = new NodeMonitor();
                    
                    long eventsCRS = nm.getLong("crs");
                    long eventsWaf = nm.getLong("waf");
                    long eventsNids = nm.getLong("nids");
                    long eventsHids = nm.getLong("hids");
                    
                    node_monitor.setRefId(ref);
                    node_monitor.setNode(nodeName);
                    node_monitor.setHost(eventBean.getHost());
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
                
                default:
                    break;
                    
            }
        } catch (JSONException | ParseException | PersistenceException e) {
            logger.error("alertflex_ctrl_exception", e);
            logger.error(stat);
        }
    }
    
    public void createNewAgentAlert(Agent ag) {

        Alert a = new Alert();

        a.setRefId(ag.getRefId());
        a.setNode(ag.getNode());
        a.setAlertUuid(UUID.randomUUID().toString());

        AlertPriority ap = eventBean.getAlertPriorityFacade().findPriorityBySource(ag.getRefId(), "Alertflex");
        int sev = ap.getSeverityDefault();
        a.setAlertSeverity(sev);
        a.setEventSeverity(Integer.toString(sev));

        a.setAlertSource("Alertflex");
        a.setAlertType("HOST");
        a.setProbe(ag.getProbe());

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
        a.setNode(c.getNode());
        a.setAlertUuid(UUID.randomUUID().toString());

        AlertPriority ap = eventBean.getAlertPriorityFacade().findPriorityBySource(c.getRefId(), "Alertflex");
        int sev = ap.getSeverityDefault();
        a.setAlertSeverity(sev);
        a.setEventSeverity(Integer.toString(sev));

        a.setAlertSource("Alertflex");
        a.setAlertType("HOST");
        a.setProbe(c.getProbe());

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
    
    public String createAgentMisconfigAlert(AgentMisconfig am, int sev) {

        Alert a = new Alert();

        a.setRefId(am.getRefId());
        a.setNode(am.getNode());
        a.setAlertUuid(UUID.randomUUID().toString());

        a.setAlertSeverity(sev);
        a.setEventSeverity(am.getSeverity());

        a.setAlertSource("Wazuh");
        a.setAlertType("HOST");
        a.setProbe(am.getProbe());

        a.setDescription(am.getTitle());
        a.setEventId("3");
        a.setLocation("indef");
        a.setAction("indef");
        a.setStatus("processed");
        a.setFilter("indef");
        a.setInfo("");

        a.setTimeEvent("");
        Date date = new Date();
        a.setTimeCollr(am.getReportUpdated());
        a.setTimeCntrl(date);

        a.setAgentName(am.getAgent());
        a.setUserName("indef");
        a.setCategories("misconfig");
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
        
        return a.getAlertUuid();
    }
    
    public String createAgentVulnAlert(AgentVul av) {

        Alert a = new Alert();

        a.setRefId(av.getRefId());
        a.setNode(av.getNode());
        a.setAlertUuid(UUID.randomUUID().toString());

        AlertPriority ap = eventBean.getAlertPriorityFacade().findPriorityBySource(av.getRefId(), "Alertflex");
        int sev = ap.getSeverityDefault();
        a.setAlertSeverity(sev);
        a.setEventSeverity(av.getSeverity());

        a.setAlertSource("Wazuh");
        a.setAlertType("HOST");
        a.setProbe(av.getProbe());

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
        
        return a.getAlertUuid();
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
