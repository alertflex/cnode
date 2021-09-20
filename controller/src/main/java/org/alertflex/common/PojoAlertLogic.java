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

package org.alertflex.common;

import java.io.Serializable;
import java.util.Date;
import org.alertflex.entity.Alert;

public class PojoAlertLogic implements Serializable {
    
    private static final long serialVersionUID = 1L;

    String generatedParams = "indef";
    Boolean runPayload = false; // means need to runPayload 

    private Long alertId = 0L;
    private String alertUuid = "indef";
    private String refId = "indef";
    private String nodeId = "indef";
    private String sensorId = "indef";
    private String categories = "indef";
    private String description = "indef";
    private int alertSeverity = 0;
    private String alertSource = "indef";
    private String alertType = "indef";
    private String eventId = "indef";
    private String eventSeverity = "indef";
    private String srcIp = "indef";
    private String dstIp = "indef";
    private String srcHostname = "indef";
    private String dstHostname = "indef";
    private int srcPort = 0;
    private int dstPort = 0;
    private String regValue = "indef";
    private String fileName = "indef";
    private String hashMd5 = "indef";
    private String hashSha1 = "indef";
    private String hashSha256 = "indef";
    private Integer processId = 0;
    private String processName = "indef";
    private String processCmdline = "indef";
    private String processPath = "indef";
    private String urlHostname = "indef";
    private String urlPath = "indef";
    private String userName = "indef";
    private String agentName = "indef";
    private String containerId = "indef";
    private String containerName = "indef";
    private String cloudInstance = "indef";
    private String location = "indef";
    private String status = "indef";
    private String action = "indef";
    private String filter = "indef";
    private String info = "indef";
    private String incidentExt = "indef";
    private String timeEvent = "indef";
    private Date timeCollr = new Date();
    private Date timeCntrl = new Date();
    
    public PojoAlertLogic() {

    }

    public PojoAlertLogic(Alert a) {

        alertId = a.getAlertId();
        alertUuid = a.getAlertUuid();
        refId = a.getRefId();
        nodeId = a.getNodeId();
        sensorId = a.getSensorId();
        categories = a.getCategories();
        description = a.getDescription();
        alertSeverity = a.getAlertSeverity();
        alertSource = a.getAlertSource();
        alertType = a.getAlertType();
        eventId = a.getEventId();
        eventSeverity = a.getEventSeverity();
        srcIp = a.getSrcIp();
        dstIp = a.getDstIp();
        srcHostname = a.getSrcHostname();
        dstHostname = a.getDstHostname();
        srcPort = a.getSrcPort();
        dstPort = a.getDstPort();
        regValue = a.getRegValue();
        fileName = a.getFileName();
        hashMd5 = a.getHashMd5();
        hashSha1 = a.getHashSha1();
        hashSha256 = a.getHashSha256();
        processId = a.getProcessId();
        processName = a.getProcessName();
        processCmdline = a.getProcessCmdline();
        processPath = a.getProcessPath();
        urlHostname = a.getUrlHostname();
        urlPath = a.getUrlPath();
        userName = a.getUserName();
        agentName = a.getAgentName();
        containerId = a.getContainerId();
        containerName = a.getContainerName();
        cloudInstance = a.getCloudInstance();
        location = a.getLocation();
        status = a.getStatus();
        action = a.getAction();
        filter = a.getFilter();
        info = a.getInfo();
        incidentExt = a.getIncidentExt();
        timeEvent = a.getTimeEvent();
        timeCollr = a.getTimeCollr();
        timeCntrl = a.getTimeCntrl();
    }

    public Boolean getRunPayload() {
        return runPayload;
    }

    public void setRunPayload(Boolean rp) {
        runPayload = rp;
    }

    public String getGeneratedParams() {
        return generatedParams;
    }

    public void setGeneratedParams(String p) {
        generatedParams = p;
    }

    public Long getAlertId() {
        return alertId;
    }

    public void setAlertId(Long alertId) {
        this.alertId = alertId;
    }

    public String getAlertUuid() {
        return alertUuid;
    }

    public void setAlertUuid(String alertUuid) {
        this.alertUuid = alertUuid;
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getSensorId() {
        return sensorId;
    }

    public void setSensorId(String sensorId) {
        this.sensorId = sensorId;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAlertSeverity() {
        return alertSeverity;
    }

    public void setAlertSeverity(int alertSeverity) {
        this.alertSeverity = alertSeverity;
    }

    public String getAlertSource() {
        return alertSource;
    }

    public void setAlertSource(String alertSource) {
        this.alertSource = alertSource;
    }

    public String getAlertType() {
        return alertType;
    }

    public void setAlertType(String alertType) {
        this.alertType = alertType;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventSeverity() {
        return eventSeverity;
    }

    public void setEventSeverity(String eventSeverity) {
        this.eventSeverity = eventSeverity;
    }

    public String getSrcIp() {
        return srcIp;
    }

    public void setSrcIp(String srcIp) {
        this.srcIp = srcIp;
    }

    public String getDstIp() {
        return dstIp;
    }

    public void setDstIp(String dstIp) {
        this.dstIp = dstIp;
    }

    public String getSrcHostname() {
        return srcHostname;
    }

    public void setSrcHostname(String srcHostname) {
        this.srcHostname = srcHostname;
    }

    public String getDstHostname() {
        return dstHostname;
    }

    public void setDstHostname(String dstHostname) {
        this.dstHostname = dstHostname;
    }

    public int getSrcPort() {
        return srcPort;
    }

    public void setSrcPort(int srcPort) {
        this.srcPort = srcPort;
    }

    public int getDstPort() {
        return dstPort;
    }

    public void setDstPort(int dstPort) {
        this.dstPort = dstPort;
    }

    public String getRegValue() {
        return regValue;
    }

    public void setRegValue(String regValue) {
        this.regValue = regValue;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getHashMd5() {
        return hashMd5;
    }

    public void setHashMd5(String hashMd5) {
        this.hashMd5 = hashMd5;
    }

    public String getHashSha1() {
        return hashSha1;
    }

    public void setHashSha1(String hashSha1) {
        this.hashSha1 = hashSha1;
    }

    public String getHashSha256() {
        return hashSha256;
    }

    public void setHashSha256(String hashSha256) {
        this.hashSha256 = hashSha256;
    }

    public Integer getProcessId() {
        return processId;
    }

    public void setProcessId(Integer processId) {
        this.processId = processId;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public String getProcessCmdline() {
        return processCmdline;
    }

    public void setProcessCmdline(String processCmdline) {
        this.processCmdline = processCmdline;
    }

    public String getProcessPath() {
        return processPath;
    }

    public void setProcessPath(String processPath) {
        this.processPath = processPath;
    }

    public String getUrlHostname() {
        return urlHostname;
    }

    public void setUrlHostname(String urlHostname) {
        this.urlHostname = urlHostname;
    }

    public String getUrlPath() {
        return urlPath;
    }

    public void setUrlPath(String urlPath) {
        this.urlPath = urlPath;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public String getContainerName() {
        return containerName;
    }

    public void setContainerName(String containerName) {
        this.containerName = containerName;
    }
    
    public String getCloudInstance() {
        return cloudInstance;
    }

    public void setCloudInstance(String cloudInstance) {
        this.cloudInstance = cloudInstance;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
    public String getIncidentExt() {
        return incidentExt;
    }

    public void setIncidentExt(String inc) {
        this.incidentExt = inc;
    }

    public String getTimeEvent() {
        return timeEvent;
    }

    public void setTimeEvent(String timeEvent) {
        this.timeEvent = timeEvent;
    }

    public Date getTimeCollr() {
        return timeCollr;
    }

    public void setTimeCollr(Date timeCollr) {
        this.timeCollr = timeCollr;
    }

    public Date getTimeCntrl() {
        return timeCntrl;
    }

    public void setTimeCntrl(Date timeCntrl) {
        this.timeCntrl = timeCntrl;
    }

}
