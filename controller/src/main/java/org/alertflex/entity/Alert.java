/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alertflex.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author root
 */
@Entity
@Table(name = "alert")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Alert.findAll", query = "SELECT a FROM Alert a")
    , @NamedQuery(name = "Alert.findByAlertId", query = "SELECT a FROM Alert a WHERE a.alertId = :alertId")
    , @NamedQuery(name = "Alert.findByAlertUuid", query = "SELECT a FROM Alert a WHERE a.alertUuid = :alertUuid")
    , @NamedQuery(name = "Alert.findByRefId", query = "SELECT a FROM Alert a WHERE a.refId = :refId")
    , @NamedQuery(name = "Alert.findByNodeId", query = "SELECT a FROM Alert a WHERE a.nodeId = :nodeId")
    , @NamedQuery(name = "Alert.findBySensorId", query = "SELECT a FROM Alert a WHERE a.sensorId = :sensorId")
    , @NamedQuery(name = "Alert.findByCategories", query = "SELECT a FROM Alert a WHERE a.categories = :categories")
    , @NamedQuery(name = "Alert.findByDescription", query = "SELECT a FROM Alert a WHERE a.description = :description")
    , @NamedQuery(name = "Alert.findByAlertSeverity", query = "SELECT a FROM Alert a WHERE a.alertSeverity = :alertSeverity")
    , @NamedQuery(name = "Alert.findByAlertSource", query = "SELECT a FROM Alert a WHERE a.alertSource = :alertSource")
    , @NamedQuery(name = "Alert.findByAlertType", query = "SELECT a FROM Alert a WHERE a.alertType = :alertType")
    , @NamedQuery(name = "Alert.findByEventId", query = "SELECT a FROM Alert a WHERE a.eventId = :eventId")
    , @NamedQuery(name = "Alert.findByEventSeverity", query = "SELECT a FROM Alert a WHERE a.eventSeverity = :eventSeverity")
    , @NamedQuery(name = "Alert.findBySrcIp", query = "SELECT a FROM Alert a WHERE a.srcIp = :srcIp")
    , @NamedQuery(name = "Alert.findByDstIp", query = "SELECT a FROM Alert a WHERE a.dstIp = :dstIp")
    , @NamedQuery(name = "Alert.findBySrcHostname", query = "SELECT a FROM Alert a WHERE a.srcHostname = :srcHostname")
    , @NamedQuery(name = "Alert.findByDstHostname", query = "SELECT a FROM Alert a WHERE a.dstHostname = :dstHostname")
    , @NamedQuery(name = "Alert.findBySrcPort", query = "SELECT a FROM Alert a WHERE a.srcPort = :srcPort")
    , @NamedQuery(name = "Alert.findByDstPort", query = "SELECT a FROM Alert a WHERE a.dstPort = :dstPort")
    , @NamedQuery(name = "Alert.findByFileName", query = "SELECT a FROM Alert a WHERE a.fileName = :fileName")
    , @NamedQuery(name = "Alert.findByFilePath", query = "SELECT a FROM Alert a WHERE a.filePath = :filePath")
    , @NamedQuery(name = "Alert.findByHashMd5", query = "SELECT a FROM Alert a WHERE a.hashMd5 = :hashMd5")
    , @NamedQuery(name = "Alert.findByHashSha1", query = "SELECT a FROM Alert a WHERE a.hashSha1 = :hashSha1")
    , @NamedQuery(name = "Alert.findByHashSha256", query = "SELECT a FROM Alert a WHERE a.hashSha256 = :hashSha256")
    , @NamedQuery(name = "Alert.findByProcessId", query = "SELECT a FROM Alert a WHERE a.processId = :processId")
    , @NamedQuery(name = "Alert.findByProcessName", query = "SELECT a FROM Alert a WHERE a.processName = :processName")
    , @NamedQuery(name = "Alert.findByProcessCmdline", query = "SELECT a FROM Alert a WHERE a.processCmdline = :processCmdline")
    , @NamedQuery(name = "Alert.findByProcessPath", query = "SELECT a FROM Alert a WHERE a.processPath = :processPath")
    , @NamedQuery(name = "Alert.findByUrlHostname", query = "SELECT a FROM Alert a WHERE a.urlHostname = :urlHostname")
    , @NamedQuery(name = "Alert.findByUrlPath", query = "SELECT a FROM Alert a WHERE a.urlPath = :urlPath")
    , @NamedQuery(name = "Alert.findByUserName", query = "SELECT a FROM Alert a WHERE a.userName = :userName")
    , @NamedQuery(name = "Alert.findByAgentName", query = "SELECT a FROM Alert a WHERE a.agentName = :agentName")
    , @NamedQuery(name = "Alert.findByContainerId", query = "SELECT a FROM Alert a WHERE a.containerId = :containerId")
    , @NamedQuery(name = "Alert.findByContainerName", query = "SELECT a FROM Alert a WHERE a.containerName = :containerName")
    , @NamedQuery(name = "Alert.findByLocation", query = "SELECT a FROM Alert a WHERE a.location = :location")
    , @NamedQuery(name = "Alert.findByStatus", query = "SELECT a FROM Alert a WHERE a.status = :status")
    , @NamedQuery(name = "Alert.findByAction", query = "SELECT a FROM Alert a WHERE a.action = :action")
    , @NamedQuery(name = "Alert.findByFilter", query = "SELECT a FROM Alert a WHERE a.filter = :filter")
    , @NamedQuery(name = "Alert.findByInfo", query = "SELECT a FROM Alert a WHERE a.info = :info")
    , @NamedQuery(name = "Alert.findByTimeEvent", query = "SELECT a FROM Alert a WHERE a.timeEvent = :timeEvent")
    , @NamedQuery(name = "Alert.findByTimeCollr", query = "SELECT a FROM Alert a WHERE a.timeCollr = :timeCollr")
    , @NamedQuery(name = "Alert.findByTimeCntrl", query = "SELECT a FROM Alert a WHERE a.timeCntrl = :timeCntrl")})
public class Alert implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "alert_id")
    private Long alertId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 37)
    @Column(name = "alert_uuid")
    private String alertUuid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "ref_id")
    private String refId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "node_id")
    private String nodeId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "sensor_id")
    private String sensorId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1024)
    @Column(name = "categories")
    private String categories;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1024)
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @NotNull
    @Column(name = "alert_severity")
    private int alertSeverity;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 32)
    @Column(name = "alert_source")
    private String alertSource;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 32)
    @Column(name = "alert_type")
    private String alertType;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "event_id")
    private String eventId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "event_severity")
    private String eventSeverity;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "src_ip")
    private String srcIp;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "dst_ip")
    private String dstIp;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "src_hostname")
    private String srcHostname;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "dst_hostname")
    private String dstHostname;
    @Basic(optional = false)
    @NotNull
    @Column(name = "src_port")
    private int srcPort;
    @Basic(optional = false)
    @NotNull
    @Column(name = "dst_port")
    private int dstPort;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "file_name")
    private String fileName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1024)
    @Column(name = "file_path")
    private String filePath;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "hash_md5")
    private String hashMd5;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "hash_sha1")
    private String hashSha1;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "hash_sha256")
    private String hashSha256;
    @Column(name = "process_id")
    private Integer processId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "process_name")
    private String processName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "process_cmdline")
    private String processCmdline;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1024)
    @Column(name = "process_path")
    private String processPath;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "url_hostname")
    private String urlHostname;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1024)
    @Column(name = "url_path")
    private String urlPath;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "user_name")
    private String userName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "agent_name")
    private String agentName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "container_id")
    private String containerId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "container_name")
    private String containerName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1024)
    @Column(name = "location")
    private String location;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 32)
    @Column(name = "status")
    private String status;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 256)
    @Column(name = "action")
    private String action;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "filter")
    private String filter;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1024)
    @Column(name = "info")
    private String info;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "time_event")
    private String timeEvent;
    @Column(name = "time_collr")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeCollr;
    @Column(name = "time_cntrl")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeCntrl;
    @Lob
    @Size(max = 65535)
    @Column(name = "json_event")
    private String jsonEvent;

    public Alert() {
    }

    public Alert(Long alertId) {
        this.alertId = alertId;
    }

    public Alert(Long alertId, String alertUuid, String refId, String nodeId, String sensorId, String categories, String description, int alertSeverity, String alertSource, String alertType, String eventId, String eventSeverity, String srcIp, String dstIp, String srcHostname, String dstHostname, int srcPort, int dstPort, String fileName, String filePath, String hashMd5, String hashSha1, String hashSha256, String processName, String processCmdline, String processPath, String urlHostname, String urlPath, String userName, String agentName, String containerId, String containerName, String location, String status, String action, String filter, String info, String timeEvent) {
        this.alertId = alertId;
        this.alertUuid = alertUuid;
        this.refId = refId;
        this.nodeId = nodeId;
        this.sensorId = sensorId;
        this.categories = categories;
        this.description = description;
        this.alertSeverity = alertSeverity;
        this.alertSource = alertSource;
        this.alertType = alertType;
        this.eventId = eventId;
        this.eventSeverity = eventSeverity;
        this.srcIp = srcIp;
        this.dstIp = dstIp;
        this.srcHostname = srcHostname;
        this.dstHostname = dstHostname;
        this.srcPort = srcPort;
        this.dstPort = dstPort;
        this.fileName = fileName;
        this.filePath = filePath;
        this.hashMd5 = hashMd5;
        this.hashSha1 = hashSha1;
        this.hashSha256 = hashSha256;
        this.processName = processName;
        this.processCmdline = processCmdline;
        this.processPath = processPath;
        this.urlHostname = urlHostname;
        this.urlPath = urlPath;
        this.userName = userName;
        this.agentName = agentName;
        this.containerId = containerId;
        this.containerName = containerName;
        this.location = location;
        this.status = status;
        this.action = action;
        this.filter = filter;
        this.info = info;
        this.timeEvent = timeEvent;
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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
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

    public String getJsonEvent() {
        return jsonEvent;
    }

    public void setJsonEvent(String jsonEvent) {
        this.jsonEvent = jsonEvent;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (alertId != null ? alertId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Alert)) {
            return false;
        }
        Alert other = (Alert) object;
        if ((this.alertId == null && other.alertId != null) || (this.alertId != null && !this.alertId.equals(other.alertId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.alertflex.entity.Alert[ alertId=" + alertId + " ]";
    }

}
