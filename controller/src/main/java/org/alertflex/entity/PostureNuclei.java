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
@Table(name = "posture_nuclei")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PostureNuclei.findAll", query = "SELECT p FROM PostureNuclei p"),
    @NamedQuery(name = "PostureNuclei.findByRecId", query = "SELECT p FROM PostureNuclei p WHERE p.recId = :recId"),
    @NamedQuery(name = "PostureNuclei.findByRefId", query = "SELECT p FROM PostureNuclei p WHERE p.refId = :refId"),
    @NamedQuery(name = "PostureNuclei.findByScanUuid", query = "SELECT p FROM PostureNuclei p WHERE p.scanUuid = :scanUuid"),
    @NamedQuery(name = "PostureNuclei.findByAlertUuid", query = "SELECT p FROM PostureNuclei p WHERE p.alertUuid = :alertUuid"),
    @NamedQuery(name = "PostureNuclei.findByNode", query = "SELECT p FROM PostureNuclei p WHERE p.node = :node"),
    @NamedQuery(name = "PostureNuclei.findByProbe", query = "SELECT p FROM PostureNuclei p WHERE p.probe = :probe"),
    @NamedQuery(name = "PostureNuclei.findByTarget", query = "SELECT p FROM PostureNuclei p WHERE p.target = :target"),
    @NamedQuery(name = "PostureNuclei.findByAlertRef", query = "SELECT p FROM PostureNuclei p WHERE p.alertRef = :alertRef"),
    @NamedQuery(name = "PostureNuclei.findByAlertName", query = "SELECT p FROM PostureNuclei p WHERE p.alertName = :alertName"),
    @NamedQuery(name = "PostureNuclei.findByAlertId", query = "SELECT p FROM PostureNuclei p WHERE p.alertId = :alertId"),
    @NamedQuery(name = "PostureNuclei.findByDescription", query = "SELECT p FROM PostureNuclei p WHERE p.description = :description"),
    @NamedQuery(name = "PostureNuclei.findByTargetHost", query = "SELECT p FROM PostureNuclei p WHERE p.targetHost = :targetHost"),
    @NamedQuery(name = "PostureNuclei.findByTargetIp", query = "SELECT p FROM PostureNuclei p WHERE p.targetIp = :targetIp"),
    @NamedQuery(name = "PostureNuclei.findByTargetType", query = "SELECT p FROM PostureNuclei p WHERE p.targetType = :targetType"),
    @NamedQuery(name = "PostureNuclei.findBySeverity", query = "SELECT p FROM PostureNuclei p WHERE p.severity = :severity"),
    @NamedQuery(name = "PostureNuclei.findByStatus", query = "SELECT p FROM PostureNuclei p WHERE p.status = :status"),
    @NamedQuery(name = "PostureNuclei.findByReportAdded", query = "SELECT p FROM PostureNuclei p WHERE p.reportAdded = :reportAdded"),
    @NamedQuery(name = "PostureNuclei.findByReportUpdated", query = "SELECT p FROM PostureNuclei p WHERE p.reportUpdated = :reportUpdated")})
public class PostureNuclei implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "rec_id")
    private Long recId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "ref_id")
    private String refId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "scan_uuid")
    private String scanUuid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "alert_uuid")
    private String alertUuid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "node")
    private String node;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "probe")
    private String probe;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "target")
    private String target;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1024)
    @Column(name = "alert_ref")
    private String alertRef;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "alert_name")
    private String alertName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "alert_id")
    private String alertId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2048)
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "target_host")
    private String targetHost;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "target_ip")
    private String targetIp;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "target_type")
    private String targetType;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "severity")
    private String severity;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 32)
    @Column(name = "status")
    private String status;
    @Column(name = "report_added")
    @Temporal(TemporalType.TIMESTAMP)
    private Date reportAdded;
    @Column(name = "report_updated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date reportUpdated;

    public PostureNuclei() {
    }

    public PostureNuclei(Long recId) {
        this.recId = recId;
    }

    public PostureNuclei(Long recId, String refId, String scanUuid, String alertUuid, String node, String probe, String target, String alertRef, String alertName, String alertId, String description, String targetHost, String targetIp, String targetType, String severity, String status) {
        this.recId = recId;
        this.refId = refId;
        this.scanUuid = scanUuid;
        this.alertUuid = alertUuid;
        this.node = node;
        this.probe = probe;
        this.target = target;
        this.alertRef = alertRef;
        this.alertName = alertName;
        this.alertId = alertId;
        this.description = description;
        this.targetHost = targetHost;
        this.targetIp = targetIp;
        this.targetType = targetType;
        this.severity = severity;
        this.status = status;
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

    public String getScanUuid() {
        return scanUuid;
    }

    public void setScanUuid(String scanUuid) {
        this.scanUuid = scanUuid;
    }

    public String getAlertUuid() {
        return alertUuid;
    }

    public void setAlertUuid(String alertUuid) {
        this.alertUuid = alertUuid;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public String getProbe() {
        return probe;
    }

    public void setProbe(String probe) {
        this.probe = probe;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getAlertRef() {
        return alertRef;
    }

    public void setAlertRef(String alertRef) {
        this.alertRef = alertRef;
    }

    public String getAlertName() {
        return alertName;
    }

    public void setAlertName(String alertName) {
        this.alertName = alertName;
    }

    public String getAlertId() {
        return alertId;
    }

    public void setAlertId(String alertId) {
        this.alertId = alertId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTargetHost() {
        return targetHost;
    }

    public void setTargetHost(String targetHost) {
        this.targetHost = targetHost;
    }

    public String getTargetIp() {
        return targetIp;
    }

    public void setTargetIp(String targetIp) {
        this.targetIp = targetIp;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getReportAdded() {
        return reportAdded;
    }

    public void setReportAdded(Date reportAdded) {
        this.reportAdded = reportAdded;
    }

    public Date getReportUpdated() {
        return reportUpdated;
    }

    public void setReportUpdated(Date reportUpdated) {
        this.reportUpdated = reportUpdated;
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
        if (!(object instanceof PostureNuclei)) {
            return false;
        }
        PostureNuclei other = (PostureNuclei) object;
        if ((this.recId == null && other.recId != null) || (this.recId != null && !this.recId.equals(other.recId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.alertflex.entity.PostureNuclei[ recId=" + recId + " ]";
    }
    
}
