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
@Table(name = "probes")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Probes.findAll", query = "SELECT p FROM Probes p"),
    @NamedQuery(name = "Probes.findByRecId", query = "SELECT p FROM Probes p WHERE p.recId = :recId"),
    @NamedQuery(name = "Probes.findByProjectId", query = "SELECT p FROM Probes p WHERE p.projectId = :projectId"),
    @NamedQuery(name = "Probes.findByProbeName", query = "SELECT p FROM Probes p WHERE p.probeName = :probeName"),
    @NamedQuery(name = "Probes.findByDescription", query = "SELECT p FROM Probes p WHERE p.description = :description"),
    @NamedQuery(name = "Probes.findBySensorsGroup", query = "SELECT p FROM Probes p WHERE p.sensorsGroup = :sensorsGroup"),
    @NamedQuery(name = "Probes.findByReportInterval", query = "SELECT p FROM Probes p WHERE p.reportInterval = :reportInterval"),
    @NamedQuery(name = "Probes.findByEnableAlerts", query = "SELECT p FROM Probes p WHERE p.enableAlerts = :enableAlerts"),
    @NamedQuery(name = "Probes.findByEnableLogs", query = "SELECT p FROM Probes p WHERE p.enableLogs = :enableLogs"),
    @NamedQuery(name = "Probes.findByRunMode", query = "SELECT p FROM Probes p WHERE p.runMode = :runMode"),
    @NamedQuery(name = "Probes.findByPathWorkspace", query = "SELECT p FROM Probes p WHERE p.pathWorkspace = :pathWorkspace"),
    @NamedQuery(name = "Probes.findByPathOutput", query = "SELECT p FROM Probes p WHERE p.pathOutput = :pathOutput"),
    @NamedQuery(name = "Probes.findByAppLogStatus", query = "SELECT p FROM Probes p WHERE p.appLogStatus = :appLogStatus"),
    @NamedQuery(name = "Probes.findByAppRedisStatus", query = "SELECT p FROM Probes p WHERE p.appRedisStatus = :appRedisStatus"),
    @NamedQuery(name = "Probes.findByFalcoLogStatus", query = "SELECT p FROM Probes p WHERE p.falcoLogStatus = :falcoLogStatus"),
    @NamedQuery(name = "Probes.findByFalcoRedisStatus", query = "SELECT p FROM Probes p WHERE p.falcoRedisStatus = :falcoRedisStatus"),
    @NamedQuery(name = "Probes.findBySuricataLogStatus", query = "SELECT p FROM Probes p WHERE p.suricataLogStatus = :suricataLogStatus"),
    @NamedQuery(name = "Probes.findBySuricataRedisStatus", query = "SELECT p FROM Probes p WHERE p.suricataRedisStatus = :suricataRedisStatus"),
    @NamedQuery(name = "Probes.findByPipelineName", query = "SELECT p FROM Probes p WHERE p.pipelineName = :pipelineName"),
    @NamedQuery(name = "Probes.findByPipelineTask", query = "SELECT p FROM Probes p WHERE p.pipelineTask = :pipelineTask"),
    @NamedQuery(name = "Probes.findByLastTestTime", query = "SELECT p FROM Probes p WHERE p.lastTestTime = :lastTestTime"),
    @NamedQuery(name = "Probes.findByFirstStatusTime", query = "SELECT p FROM Probes p WHERE p.firstStatusTime = :firstStatusTime"),
    @NamedQuery(name = "Probes.findByLastStatusTime", query = "SELECT p FROM Probes p WHERE p.lastStatusTime = :lastStatusTime")})
public class Probes implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "rec_id")
    private Integer recId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "project_id")
    private String projectId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "probe_name")
    private String probeName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "sensors_group")
    private String sensorsGroup;
    @Basic(optional = false)
    @NotNull
    @Column(name = "report_interval")
    private int reportInterval;
    @Basic(optional = false)
    @NotNull
    @Column(name = "enable_alerts")
    private int enableAlerts;
    @Basic(optional = false)
    @NotNull
    @Column(name = "enable_logs")
    private int enableLogs;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "run_mode")
    private String runMode;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "path_workspace")
    private String pathWorkspace;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "path_output")
    private String pathOutput;
    @Basic(optional = false)
    @NotNull
    @Column(name = "app_log_status")
    private int appLogStatus;
    @Basic(optional = false)
    @NotNull
    @Column(name = "app_redis_status")
    private int appRedisStatus;
    @Basic(optional = false)
    @NotNull
    @Column(name = "falco_log_status")
    private int falcoLogStatus;
    @Basic(optional = false)
    @NotNull
    @Column(name = "falco_redis_status")
    private int falcoRedisStatus;
    @Basic(optional = false)
    @NotNull
    @Column(name = "suricata_log_status")
    private int suricataLogStatus;
    @Basic(optional = false)
    @NotNull
    @Column(name = "suricata_redis_status")
    private int suricataRedisStatus;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "pipeline_name")
    private String pipelineName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "pipeline_task")
    private String pipelineTask;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "last_test_time")
    private String lastTestTime;
    @Column(name = "first_status_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date firstStatusTime;
    @Column(name = "last_status_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastStatusTime;

    public Probes() {
    }

    public Probes(Integer recId) {
        this.recId = recId;
    }

    public Probes(Integer recId, String projectId, String probeName, String description, String sensorsGroup, int reportInterval, int enableAlerts, int enableLogs, String runMode, String pathWorkspace, String pathOutput, int appLogStatus, int appRedisStatus, int falcoLogStatus, int falcoRedisStatus, int suricataLogStatus, int suricataRedisStatus, String pipelineName, String pipelineTask, String lastTestTime) {
        this.recId = recId;
        this.projectId = projectId;
        this.probeName = probeName;
        this.description = description;
        this.sensorsGroup = sensorsGroup;
        this.reportInterval = reportInterval;
        this.enableAlerts = enableAlerts;
        this.enableLogs = enableLogs;
        this.runMode = runMode;
        this.pathWorkspace = pathWorkspace;
        this.pathOutput = pathOutput;
        this.appLogStatus = appLogStatus;
        this.appRedisStatus = appRedisStatus;
        this.falcoLogStatus = falcoLogStatus;
        this.falcoRedisStatus = falcoRedisStatus;
        this.suricataLogStatus = suricataLogStatus;
        this.suricataRedisStatus = suricataRedisStatus;
        this.pipelineName = pipelineName;
        this.pipelineTask = pipelineTask;
        this.lastTestTime = lastTestTime;
    }

    public Integer getRecId() {
        return recId;
    }

    public void setRecId(Integer recId) {
        this.recId = recId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProbeName() {
        return probeName;
    }

    public void setProbeName(String probeName) {
        this.probeName = probeName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSensorsGroup() {
        return sensorsGroup;
    }

    public void setSensorsGroup(String sensorsGroup) {
        this.sensorsGroup = sensorsGroup;
    }

    public int getReportInterval() {
        return reportInterval;
    }

    public void setReportInterval(int reportInterval) {
        this.reportInterval = reportInterval;
    }

    public int getEnableAlerts() {
        return enableAlerts;
    }

    public void setEnableAlerts(int enableAlerts) {
        this.enableAlerts = enableAlerts;
    }

    public int getEnableLogs() {
        return enableLogs;
    }

    public void setEnableLogs(int enableLogs) {
        this.enableLogs = enableLogs;
    }

    public String getRunMode() {
        return runMode;
    }

    public void setRunMode(String runMode) {
        this.runMode = runMode;
    }

    public String getPathWorkspace() {
        return pathWorkspace;
    }

    public void setPathWorkspace(String pathWorkspace) {
        this.pathWorkspace = pathWorkspace;
    }

    public String getPathOutput() {
        return pathOutput;
    }

    public void setPathOutput(String pathOutput) {
        this.pathOutput = pathOutput;
    }

    public int getAppLogStatus() {
        return appLogStatus;
    }

    public void setAppLogStatus(int appLogStatus) {
        this.appLogStatus = appLogStatus;
    }

    public int getAppRedisStatus() {
        return appRedisStatus;
    }

    public void setAppRedisStatus(int appRedisStatus) {
        this.appRedisStatus = appRedisStatus;
    }

    public int getFalcoLogStatus() {
        return falcoLogStatus;
    }

    public void setFalcoLogStatus(int falcoLogStatus) {
        this.falcoLogStatus = falcoLogStatus;
    }

    public int getFalcoRedisStatus() {
        return falcoRedisStatus;
    }

    public void setFalcoRedisStatus(int falcoRedisStatus) {
        this.falcoRedisStatus = falcoRedisStatus;
    }

    public int getSuricataLogStatus() {
        return suricataLogStatus;
    }

    public void setSuricataLogStatus(int suricataLogStatus) {
        this.suricataLogStatus = suricataLogStatus;
    }

    public int getSuricataRedisStatus() {
        return suricataRedisStatus;
    }

    public void setSuricataRedisStatus(int suricataRedisStatus) {
        this.suricataRedisStatus = suricataRedisStatus;
    }

    public String getPipelineName() {
        return pipelineName;
    }

    public void setPipelineName(String pipelineName) {
        this.pipelineName = pipelineName;
    }

    public String getPipelineTask() {
        return pipelineTask;
    }

    public void setPipelineTask(String pipelineTask) {
        this.pipelineTask = pipelineTask;
    }

    public String getLastTestTime() {
        return lastTestTime;
    }

    public void setLastTestTime(String lastTestTime) {
        this.lastTestTime = lastTestTime;
    }

    public Date getFirstStatusTime() {
        return firstStatusTime;
    }

    public void setFirstStatusTime(Date firstStatusTime) {
        this.firstStatusTime = firstStatusTime;
    }

    public Date getLastStatusTime() {
        return lastStatusTime;
    }

    public void setLastStatusTime(Date lastStatusTime) {
        this.lastStatusTime = lastStatusTime;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (recId != null ? recId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Probes)) {
            return false;
        }
        Probes other = (Probes) object;
        if ((this.recId == null && other.recId != null) || (this.recId != null && !this.recId.equals(other.recId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.alertflex.entity.Probes[ recId=" + recId + " ]";
    }
    
}
