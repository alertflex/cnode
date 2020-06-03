package org.alertflex.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import javax.persistence.PersistenceException;
import org.alertflex.entity.Agent;
import org.alertflex.entity.AgentOpenscap;
import org.alertflex.entity.Alert;
import org.alertflex.entity.NodeFilters;
import org.alertflex.entity.NodeAlerts;
import org.alertflex.entity.NodeMonitor;
import org.alertflex.entity.NetStat;
import org.alertflex.entity.AgentProcesses;
import org.alertflex.entity.AgentPackages;
import org.alertflex.entity.AgentSca;
import org.alertflex.entity.AgentVul;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Oleg Zharkov
 */

public class StatsManagement {
    
    private static final Logger logger = LoggerFactory.getLogger(StatsManagement.class);
    
    private InfoMessageBean eventBean;
    
    public StatsManagement (InfoMessageBean eb) {
        this.eventBean = eb;
    }
    
    public void EvaluateStats( String stats) throws ParseException {
        
        try {
            
            JSONObject obj = new JSONObject(stats);
            JSONArray arr = obj.getJSONArray("stats");
            
            for (int i = 0; i < arr.length(); i++) {
                
                persistStats(arr.getJSONObject(i));
                
                //logger.error(arr.getJSONObject(i).toString());
            }
             
        } catch ( JSONException e) {
            
            logger.error("alertflex_ctrl_exception", e);
            logger.error(stats);
        } 
    }
    
    public void persistStats(JSONObject obj) throws ParseException {
        
        String stat = obj.toString();
        
        try {
            
            String stats_type = obj.getString("type");
            
            switch(stats_type) {
                
                case "agents_list": {
                    

                    boolean filtersFlag = false;
                    
                    JSONArray arr = obj.getJSONArray("data");
                        
                    for (int i = 0; i < arr.length(); i++) {
                            
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date = formatter.parse(arr.getJSONObject(i).getString("time_of_survey"));
                        
                        String ref = eventBean.getRefId();
                        String node = eventBean.getNode();
                        String agent = arr.getJSONObject(i).getString("name");
                        String id = arr.getJSONObject(i).getString("id");
                        String status = arr.getJSONObject(i).getString("status");
                        String ip = arr.getJSONObject(i).getString("ip");
                        String dateAdd = arr.getJSONObject(i).getString("date_add");
                        String version = arr.getJSONObject(i).getString("version");
                        String managerHost = arr.getJSONObject(i).getString("manager_host");
                        String osPlatform = arr.getJSONObject(i).getString("os_platform");
                        String osVersion = arr.getJSONObject(i).getString("os_version");
                        String osName = arr.getJSONObject(i).getString("os_name");
                        
                        Agent agExisting = eventBean.getAgentFacade().findAgentByName(ref, node, agent);
                        
                        if (agExisting == null) {
                            
                            Agent a = new Agent();
                            
                            a.setRefId(ref);
                            a.setNodeId(node);
                            a.setDateUpdate(date);
                            a.setStatus(status);
                            a.setAgentId(id);
                            a.setIpLinked("indef");
                            a.setHostLinked("indef");
                            a.setContainerLinked("indef");
                            a.setAgentKey("indef");
                            a.setName(agent);
                            a.setIp(ip);
                            a.setDateAdd(dateAdd);
                            a.setVersion(version);
                            a.setManager(managerHost);
                            a.setOsPlatform(osPlatform);
                            a.setOsVersion(osVersion);
                            a.setOsName(osName);
                            
                            eventBean.getAgentFacade().create(a);
                            
                            createNewAgentAlert(a);
                            
                            filtersFlag = true;
                            
                        } else {
                            
                            if (!ip.equals(agExisting.getIp())) filtersFlag = true;
                                
                            agExisting.setDateUpdate(date);
                            agExisting.setStatus(status);
                            agExisting.setAgentId(id);
                            agExisting.setIp(ip);
                            agExisting.setDateAdd(dateAdd);
                            agExisting.setVersion(version);
                            agExisting.setManager(managerHost);
                            agExisting.setOsPlatform(osPlatform);
                            agExisting.setOsVersion(osVersion);
                            agExisting.setOsName(osName);
                            
                            eventBean.getAgentFacade().edit(agExisting);
                        }
                    }
                    
                    if (filtersFlag) updateFilters();
                    
                    break;
                }    
                
                case "sca": {
                    
                    String ref = eventBean.getRefId();
                    String node = eventBean.getNode();
                    String agent = obj.getString("agent");
                    
                    Date date = new Date();
                    
                    JSONObject data = obj.getJSONObject("data");
                    
                    int totalItems = data.getInt("totalItems");
                    
                    if (totalItems == 0) break;
                    
                    JSONArray arr = data.getJSONArray("items");
                    
                    for (int i = 0; i < arr.length(); i++) {
                            
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date startScan = formatter.parse(arr.getJSONObject(i).getString("start_scan"));
                        Date endScan = formatter.parse(arr.getJSONObject(i).getString("end_scan"));
                        
                        int invalid = arr.getJSONObject(i).getInt("invalid");
                        int fail = arr.getJSONObject(i).getInt("fail");
                        int totalChecks = arr.getJSONObject(i).getInt("total_checks");
                        int pass = arr.getJSONObject(i).getInt("pass");
                        int score = arr.getJSONObject(i).getInt("score");
                        String description = arr.getJSONObject(i).getString("description");
                        String references = arr.getJSONObject(i).getString("references");
                        String policyId = arr.getJSONObject(i).getString("policy_id");
                        String name = arr.getJSONObject(i).getString("name");
                        
                        AgentSca scaExisting = eventBean.getAgentScaFacade().findSca(ref, node, agent, name, policyId);
                        
                        if (scaExisting == null) {
                            
                            AgentSca a = new AgentSca();
                            
                            a.setRefId(ref);
                            a.setNodeId(node);
                            a.setAgent(agent);
                            a.setInvalid(invalid);
                            a.setFail(fail);
                            a.setTotalChecks(totalChecks);
                            a.setPass(pass);
                            a.setScore(score);
                            a.setDescription(description);
                            a.setName(name);
                            a.setRefUrl(references);
                            a.setPolicyId(policyId);
                            a.setStartScan(startScan);
                            a.setEndScan(endScan);
                            a.setDateAdd(date);
                            a.setDateUpdate(date);
                            
                            eventBean.getAgentScaFacade().create(a);
                            
                        } else {
                            
                            scaExisting.setInvalid(invalid);
                            scaExisting.setFail(fail);
                            scaExisting.setTotalChecks(totalChecks);
                            scaExisting.setPass(pass);
                            scaExisting.setScore(score);
                            scaExisting.setStartScan(startScan);
                            scaExisting.setEndScan(endScan);
                            scaExisting.setDateUpdate(date);
                            
                            eventBean.getAgentScaFacade().edit(scaExisting);
                        }
                    }
                    
                    break;
                } 
                
                case "packages": {
                    
                    String ref = eventBean.getRefId();
                    String node = eventBean.getNode();
                    String agent = obj.getString("agent");
                    
                    Date date = new Date();
                    
                    JSONObject data = obj.getJSONObject("data");
                    
                    JSONArray arr = data.getJSONArray("items");
                    
                    for (int i = 0; i < arr.length(); i++) {
                        
                        JSONObject scan = arr.getJSONObject(i).getJSONObject("scan");
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        Date time = formatter.parse(scan.getString("time"));
                        
                        long size = 0;
                        if (arr.getJSONObject(i).has("size")) size = arr.getJSONObject(i).getLong("size");
                        
                        String architecture = "";
                        if (arr.getJSONObject(i).has("architecture")) architecture = arr.getJSONObject(i).getString("architecture");
                        
                        String priority = "";
                        if (arr.getJSONObject(i).has("priority")) priority = arr.getJSONObject(i).getString("priority");
                        
                        String version = "";
                        if (arr.getJSONObject(i).has("version")) version = arr.getJSONObject(i).getString("version");
                        
                        String vendor = "";
                        if (arr.getJSONObject(i).has("vendor"))  vendor = arr.getJSONObject(i).getString("vendor");
                        
                        String format = "";
                        if (arr.getJSONObject(i).has("format"))  format = arr.getJSONObject(i).getString("format");
                        
                        String section = "";
                        if (arr.getJSONObject(i).has("section"))  section = arr.getJSONObject(i).getString("section");
                        
                        String name = "";
                        if (arr.getJSONObject(i).has("name"))  name = arr.getJSONObject(i).getString("name");
                        
                        String description = "";
                        if (arr.getJSONObject(i).has("description"))  description = arr.getJSONObject(i).getString("description");
                        
                        AgentPackages pExisting = eventBean.getAgentPackagesFacade().findPackage(ref, node, agent, name, version);
                        
                        if (pExisting == null) {
                            
                            AgentPackages p = new AgentPackages();
                            
                            p.setRefId(ref);
                            p.setNodeId(node);
                            p.setAgent(agent);
                            p.setPackageSize(size);
                            p.setArchitecture(architecture);
                            p.setPriority(priority);
                            p.setVersion(version);
                            p.setVendor(vendor);
                            p.setPackageFormat(format);
                            p.setPackageSection(section);
                            p.setName(name);
                            p.setDescription(description);
                            p.setTimeScan(time);
                            p.setDateAdd(date);
                            p.setDateUpdate(date);
                            
                            eventBean.getAgentPackagesFacade().create(p);
                            
                        } else {
                            
                            pExisting.setPackageSize(size);
                            pExisting.setArchitecture(architecture);
                            pExisting.setPriority(priority);
                            pExisting.setVersion(version);
                            pExisting.setVendor(vendor);
                            pExisting.setPackageFormat(format);
                            pExisting.setPackageSection(section);
                            pExisting.setName(name);
                            pExisting.setTimeScan(time);
                            pExisting.setDateUpdate(date);
                            
                            eventBean.getAgentPackagesFacade().edit(pExisting);
                        }
                    }
                    
                    break;
                }    
                
                case "processes": {
                    
                    String ref = eventBean.getRefId();
                    String node = eventBean.getNode();
                    String agent = obj.getString("agent");
                    
                    Date date = new Date();
                    
                    JSONObject data = obj.getJSONObject("data");
                    
                    JSONArray arr = data.getJSONArray("items");
                    
                    for (int i = 0; i < arr.length(); i++) {
                        
                        JSONObject scan = arr.getJSONObject(i).getJSONObject("scan");
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        Date time = formatter.parse(scan.getString("time"));
                        
                        int utime = 0;
                        if (arr.getJSONObject(i).has("utime")) utime = arr.getJSONObject(i).getInt("utime");
                        
                        String state = "";
                        if (arr.getJSONObject(i).has("state")) state = arr.getJSONObject(i).getString("state");
                        
                        int priority = 0;
                        if (arr.getJSONObject(i).has("priority")) priority = arr.getJSONObject(i).getInt("priority");
                        
                        String name = "";
                        if (arr.getJSONObject(i).has("name")) name = arr.getJSONObject(i).getString("name");
                        
                        int share = 0;
                        if (arr.getJSONObject(i).has("share")) share = arr.getJSONObject(i).getInt("share");
                        
                        String suser = "";
                        if (arr.getJSONObject(i).has("suser")) suser = arr.getJSONObject(i).getString("suser");
                        
                        String egroup = "";
                        if (arr.getJSONObject(i).has("egroup")) egroup = arr.getJSONObject(i).getString("egroup");
                        
                        int nlwp = 0;
                        if (arr.getJSONObject(i).has("nlwp")) nlwp = arr.getJSONObject(i).getInt("nlwp");
                        
                        int nice = 0;
                        if (arr.getJSONObject(i).has("nice")) nice = arr.getJSONObject(i).getInt("nice");
                        
                        String sgroup = "";
                        if (arr.getJSONObject(i).has("sgroup")) sgroup = arr.getJSONObject(i).getString("sgroup");
                        
                        int ppid = 0;
                        if (arr.getJSONObject(i).has("ppid")) ppid = arr.getJSONObject(i).getInt("ppid");
                        
                        int processor = 0;
                        if (arr.getJSONObject(i).has("processor")) processor = arr.getJSONObject(i).getInt("processor");
                        
                        String pid = "";
                        if (arr.getJSONObject(i).has("pid")) pid = arr.getJSONObject(i).getString("pid");
                        
                        String euser = "";
                        if (arr.getJSONObject(i).has("euser")) euser = arr.getJSONObject(i).getString("euser");
                        
                        String ruser = "";
                        if (arr.getJSONObject(i).has("ruser")) ruser = arr.getJSONObject(i).getString("ruser");
                        
                        int session = 0;
                        if (arr.getJSONObject(i).has("session")) session = arr.getJSONObject(i).getInt("session");
                        
                        int pgrp = 0;
                        if (arr.getJSONObject(i).has("pgrp")) pgrp = arr.getJSONObject(i).getInt("pgrp");
                        
                        int stime = 0;
                        if (arr.getJSONObject(i).has("stime")) stime = arr.getJSONObject(i).getInt("stime");
                        
                        long vm_size = 0;
                        if (arr.getJSONObject(i).has("vm_size")) vm_size = arr.getJSONObject(i).getLong("vm_size");
                        
                        int tgid = 0;
                        if (arr.getJSONObject(i).has("tgid")) tgid = arr.getJSONObject(i).getInt("tgid");
                        
                        int tty = 0;
                        if (arr.getJSONObject(i).has("tty")) tty = arr.getJSONObject(i).getInt("tty");
                        
                        String rgroup = "";
                        if (arr.getJSONObject(i).has("rgroup")) rgroup = arr.getJSONObject(i).getString("rgroup");
                        
                        long size = 0;
                        if (arr.getJSONObject(i).has("size")) size = arr.getJSONObject(i).getLong("size");
                        
                        int resident = 0;
                        if (arr.getJSONObject(i).has("resident")) resident = arr.getJSONObject(i).getInt("resident");
                        
                        String fgroup = "";
                        if (arr.getJSONObject(i).has("fgroup")) fgroup = arr.getJSONObject(i).getString("fgroup");
                        
                        int startTime = 0;
                        if (arr.getJSONObject(i).has("start_time")) startTime = arr.getJSONObject(i).getInt("start_time");
                        
                        String cmd = "";
                        if (arr.getJSONObject(i).has("cmd")) cmd = arr.getJSONObject(i).getString("cmd");
                        
                        AgentProcesses pExisting = eventBean.getAgentProcessesFacade().findProcess(ref, node, agent, name, pid);
                        
                        if (pExisting == null) {
                            
                            AgentProcesses p = new AgentProcesses();
                            
                            p.setRefId(ref);
                            p.setNodeId(node);
                            p.setAgent(agent);
                            p.setUtime(utime);
                            p.setProcessState(state);
                            p.setPriority(priority);
                            p.setName(name);
                            p.setProcessShare(share);
                            p.setSuser(suser);
                            p.setEgroup(egroup);
                            p.setNlwp(nlwp);
                            p.setNice(nice);
                            p.setSgroup(sgroup);
                            p.setPpid(ppid);
                            p.setProcessor(processor);
                            p.setPid(pid);
                            p.setEuser(euser);
                            p.setRuser(ruser);
                            p.setProcessSession(session);
                            p.setPgrp(pgrp);
                            p.setStime(stime);
                            p.setVmSize(vm_size);
                            p.setTgid(tgid);
                            p.setTty(tty);
                            p.setRgroup(rgroup);
                            p.setResident(resident);
                            p.setFgroup(fgroup);
                            p.setStartTime(startTime);
                            p.setCmd(cmd);
                            
                            p.setTimeScan(time);
                            p.setDateAdd(date);
                            p.setDateUpdate(date);
                            
                            eventBean.getAgentProcessesFacade().create(p);
                            
                        } else {
                            
                            pExisting.setProcessState(state);
                            pExisting.setPriority(priority);
                            pExisting.setProcessShare(share);
                            pExisting.setSuser(suser);
                            pExisting.setEgroup(egroup);
                            pExisting.setNlwp(nlwp);
                            pExisting.setNice(nice);
                            pExisting.setSgroup(sgroup);
                            pExisting.setPpid(ppid);
                            pExisting.setProcessor(processor);
                            pExisting.setEuser(euser);
                            pExisting.setRuser(ruser);
                            pExisting.setProcessSession(session);
                            pExisting.setPgrp(pgrp);
                            pExisting.setStime(stime);
                            pExisting.setVmSize(vm_size);
                            pExisting.setTgid(tgid);
                            pExisting.setTty(tty);
                            pExisting.setRgroup(rgroup);
                            pExisting.setResident(resident);
                            pExisting.setFgroup(fgroup);
                            pExisting.setStartTime(startTime);
                            pExisting.setCmd(cmd);
                            
                            pExisting.setTimeScan(time);
                            pExisting.setDateUpdate(date);
                            
                            eventBean.getAgentProcessesFacade().edit(pExisting);
                        }
                        
                    }
                    
                    break;
                }    
               
                case "vulnerability": {
                    
                    JSONObject jv = obj.getJSONObject("data");
                        
                    AgentVul v = new AgentVul();
                        
                    v.setRefId(eventBean.getRefId());
                    v.setNodeId(eventBean.getNode());
                    v.setAgent(jv.getString("agent"));
                    v.setEvent(jv.getInt("event_id"));
                    v.setSeverity(jv.getInt("severity"));
                    v.setDescription(jv.getString("description"));
                    
                    v.setCve(jv.getString("cve"));
                    v.setCveState(jv.getString("cve_state"));
                    v.setCveSeverity(jv.getString("cve_severity"));
                    v.setReference(jv.getString("reference"));
                    v.setPackageName(jv.getString("package_name"));
                    v.setPackageVersion(jv.getString("package_version"));
                    v.setPackageCondition(jv.getString("package_condition"));
                    v.setCvePublished(jv.getString("cve_published"));
                    v.setCveUpdated(jv.getString("cve_updated"));
                                        
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = formatter.parse(jv.getString("time_of_survey"));
                    
                    v.setReportCreated(date);
                    v.setReportUpdated(date);
                    
                    AgentVul vExisting = eventBean.getAgentVulFacade().findVulnerability(v.getRefId(), 
                        v.getNodeId(), 
                        v.getAgent(), 
                        v.getCve(),
                        v.getPackageName());
                    
                    if (vExisting == null) {
                        
                        // create alert
                        eventBean.getAgentVulFacade().create(v);
                    
                    } else {
                        
                        vExisting.setReportUpdated(date);
                        eventBean.getAgentVulFacade().edit(vExisting);
                    }
                    
                    break;
                }
                
                case "open-scap": {
                    
                    JSONObject jc = obj.getJSONObject("data");
                        
                    AgentOpenscap ao = new AgentOpenscap();
                        
                    ao.setRefId(eventBean.getRefId());
                    ao.setNodeId(eventBean.getNode());
                    ao.setAgent(jc.getString("agent"));
                    ao.setEvent(jc.getInt("event_id"));
                    ao.setSeverity(jc.getInt("severity"));
                    ao.setDescription(jc.getString("description"));
                    
                    ao.setBenchmark(jc.getString("benchmark"));
                    ao.setProfileId(jc.getString("profile_id"));
                    ao.setProfileTitle(jc.getString("profile_title"));
                    ao.setCheckId(jc.getString("check_id"));
                    ao.setCheckTitle(jc.getString("check_title"));
                    ao.setCheckResult(jc.getString("check_result"));
                    ao.setCheckSeverity(jc.getString("check_severity"));
                    ao.setCheckDescription(jc.getString("check_description"));
                    ao.setCheckRationale(jc.getString("check_rationale"));
                    ao.setCheckReferences(jc.getString("check_references"));
                    ao.setCheckIdentifiers(jc.getString("check_identifiers"));
                                        
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = formatter.parse(jc.getString("time_of_survey"));
                    
                    ao.setReportAdded(date);
                    ao.setReportUpdated(date);
                    
                    AgentOpenscap aoExisting = eventBean.getAgentOpenscapFacade().findOpenscap(ao.getRefId(), 
                        ao.getNodeId(), 
                        ao.getAgent(), 
                        ao.getProfileId(),
                        ao.getCheckId());
                    
                    if (aoExisting == null) {
                        // create alert
                        eventBean.getAgentOpenscapFacade().create(ao);
                    
                    } else {
                        
                        aoExisting.setReportUpdated(date);
                        eventBean.getAgentOpenscapFacade().edit(aoExisting);
                    }
                    
                    break;
                    
                }
                
                case "node_alerts": {
                    
                    JSONObject na = obj.getJSONObject("data");
                        
                    NodeAlerts node_alerts = new NodeAlerts();
                    
                    node_alerts.setRefId(eventBean.getRefId());
                    node_alerts.setNodeId(eventBean.getNode());
                    
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
                        
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = formatter.parse(na.getString("time_of_survey"));
                    node_alerts.setTimeOfSurvey(date);
                        
                    eventBean.getNodeAlertsFacade().create(node_alerts);
                        
                    break;
                }
                
                case "node_monitor": {
                    
                    JSONObject nm = obj.getJSONObject("data");
                    
                    NodeMonitor node_monitor = new NodeMonitor();
                    
                    node_monitor.setRefId(eventBean.getRefId());
                    node_monitor.setNodeId(eventBean.getNode());
                    
                    node_monitor.setEventsHids(nm.getLong("hids"));
                    node_monitor.setEventsNids(nm.getLong("nids"));
                    node_monitor.setEventsWaf(nm.getLong("waf"));
                    node_monitor.setEventsMisc(nm.getLong("misc"));
                    node_monitor.setEventsCrs(nm.getLong("crs"));
                    node_monitor.setLogCounter(nm.getLong("log_counter"));
                    node_monitor.setLogVolume(nm.getLong("log_volume"));
                    node_monitor.setStatCounter(nm.getLong("stat_counter"));
                    node_monitor.setStatVolume(nm.getLong("stat_volume"));
                                        
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = formatter.parse(nm.getString("time_of_survey"));
                    node_monitor.setTimeOfSurvey(date);
                        
                    eventBean.getNodeMonitorFacade().create(node_monitor);
                        
                    break;
                }
                    
                case "node_filters": {
                    
                    JSONObject nf = obj.getJSONObject("data");
                    
                    NodeFilters node_filters = new NodeFilters();
                    
                    node_filters.setRefId(eventBean.getRefId());
                    node_filters.setNodeId(eventBean.getNode());
                    
                    node_filters.setAgentList(nf.getLong("agent_list"));
                    node_filters.setHnetList(nf.getLong("hnet_list"));
                    node_filters.setHidsFilters(nf.getLong("hids_filters"));
                    node_filters.setNidsFilters(nf.getLong("nids_filters"));
                    node_filters.setCrsFilters(nf.getLong("crs_filters"));
                    node_filters.setWafFilters(nf.getLong("waf_filters"));
                        
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = formatter.parse(nf.getString("time_of_survey"));
                    node_filters.setTimeOfSurvey(date);
                        
                    eventBean.getNodeFiltersFacade().create(node_filters);
                        
                    break;
                }
                
                case "net_stat": {
                    
                    JSONArray arr = obj.getJSONArray("data");
                        
                    for (int i = 0; i < arr.length(); i++) {
                    
                        NetStat net_stat = new NetStat();
                        
                        net_stat.setRefId(eventBean.getRefId());
                        net_stat.setNodeId(eventBean.getNode());
                    
                        net_stat.setIds(arr.getJSONObject(i).getString("ids"));
                    
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
        
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date = formatter.parse(arr.getJSONObject(i).getString("time_of_survey"));
                        net_stat.setTimeOfSurvey(date);
                        
                        eventBean.getNetStatFacade().create(net_stat);
                    } 
                    
                    break;
                }
            }
        } catch ( JSONException | ParseException | PersistenceException e) {
            logger.error("alertflex_ctrl_exception", e);
            logger.error(stat);
        }     
    }
    
    public void createNewAgentAlert(Agent ag) {
        
        Alert a = new Alert();
            
        a.setRefId(ag.getRefId());
        a.setNodeId(ag.getNodeId());
        a.setAlertUuid(UUID.randomUUID().toString());
        
        a.setAlertSource("Alertflex");
        a.setAlertType("HOST");
        a.setSensorId(ag.getManager());
        a.setAlertSeverity(2);
        a.setDescription("new agent in the system with id: " + ag.getAgentId() + " and name: " + ag.getName());
        a.setEventId("1");
        a.setEventSeverity(0);
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
        a.setFileName("indef");
        a.setFilePath("indef");
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
        a.setJsonEvent("indef");
                                        
        eventBean.createAlert(a);
    }
    
    void updateFilters() {
        
        FiltersManagement fm = new FiltersManagement(eventBean);
        
        if (fm.readFilters()) fm.uploadFilters();
    }
}

