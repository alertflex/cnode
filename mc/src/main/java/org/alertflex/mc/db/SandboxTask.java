/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alertflex.mc.db;

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
@Table(name = "sandbox_task")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SandboxTask.findAll", query = "SELECT s FROM SandboxTask s")
    , @NamedQuery(name = "SandboxTask.findByRecId", query = "SELECT s FROM SandboxTask s WHERE s.recId = :recId")
    , @NamedQuery(name = "SandboxTask.findByRefId", query = "SELECT s FROM SandboxTask s WHERE s.refId = :refId")
    , @NamedQuery(name = "SandboxTask.findBySandboxJob", query = "SELECT s FROM SandboxTask s WHERE s.sandboxJob = :sandboxJob")
    , @NamedQuery(name = "SandboxTask.findBySandboxType", query = "SELECT s FROM SandboxTask s WHERE s.sandboxType = :sandboxType")
    , @NamedQuery(name = "SandboxTask.findBySandboxId", query = "SELECT s FROM SandboxTask s WHERE s.sandboxId = :sandboxId")
    , @NamedQuery(name = "SandboxTask.findBySeverity", query = "SELECT s FROM SandboxTask s WHERE s.severity = :severity")
    , @NamedQuery(name = "SandboxTask.findByHostName", query = "SELECT s FROM SandboxTask s WHERE s.hostName = :hostName")
    , @NamedQuery(name = "SandboxTask.findByFileName", query = "SELECT s FROM SandboxTask s WHERE s.fileName = :fileName")
    , @NamedQuery(name = "SandboxTask.findByTimeOfCreation", query = "SELECT s FROM SandboxTask s WHERE s.timeOfCreation = :timeOfCreation")
    , @NamedQuery(name = "SandboxTask.findByTimeOfAction", query = "SELECT s FROM SandboxTask s WHERE s.timeOfAction = :timeOfAction")
    , @NamedQuery(name = "SandboxTask.findByStatus", query = "SELECT s FROM SandboxTask s WHERE s.status = :status")
    , @NamedQuery(name = "SandboxTask.findByInfo", query = "SELECT s FROM SandboxTask s WHERE s.info = :info")})
public class SandboxTask implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "rec_id")
    private Long recId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 256)
    @Column(name = "ref_id")
    private String refId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "sandbox_job")
    private String sandboxJob;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "sandbox_type")
    private String sandboxType;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "sandbox_id")
    private String sandboxId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "severity")
    private int severity;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "host_name")
    private String hostName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "file_name")
    private String fileName;
    @Column(name = "time_of_creation")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeOfCreation;
    @Column(name = "time_of_action")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeOfAction;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "status")
    private String status;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2048)
    @Column(name = "info")
    private String info;

    public SandboxTask() {
    }

    public SandboxTask(Long recId) {
        this.recId = recId;
    }

    public SandboxTask(Long recId, String refId, String sandboxJob, String sandboxType, String sandboxId, int severity, String hostName, String fileName, String status, String info) {
        this.recId = recId;
        this.refId = refId;
        this.sandboxJob = sandboxJob;
        this.sandboxType = sandboxType;
        this.sandboxId = sandboxId;
        this.severity = severity;
        this.hostName = hostName;
        this.fileName = fileName;
        this.status = status;
        this.info = info;
    }

    public Long getRecId() {
        return recId;
    }

    public void setRecId(Long recId) {
        this.recId = recId;
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public String getSandboxJob() {
        return sandboxJob;
    }

    public void setSandboxJob(String sandboxJob) {
        this.sandboxJob = sandboxJob;
    }

    public String getSandboxType() {
        return sandboxType;
    }

    public void setSandboxType(String sandboxType) {
        this.sandboxType = sandboxType;
    }

    public String getSandboxId() {
        return sandboxId;
    }

    public void setSandboxId(String sandboxId) {
        this.sandboxId = sandboxId;
    }

    public int getSeverity() {
        return severity;
    }

    public void setSeverity(int severity) {
        this.severity = severity;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Date getTimeOfCreation() {
        return timeOfCreation;
    }

    public void setTimeOfCreation(Date timeOfCreation) {
        this.timeOfCreation = timeOfCreation;
    }

    public Date getTimeOfAction() {
        return timeOfAction;
    }

    public void setTimeOfAction(Date timeOfAction) {
        this.timeOfAction = timeOfAction;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
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
        if (!(object instanceof SandboxTask)) {
            return false;
        }
        SandboxTask other = (SandboxTask) object;
        if ((this.recId == null && other.recId != null) || (this.recId != null && !this.recId.equals(other.recId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.alertflex.mc.db.SandboxTask[ recId=" + recId + " ]";
    }
    
}
