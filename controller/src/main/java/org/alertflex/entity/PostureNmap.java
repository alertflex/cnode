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
@Table(name = "posture_nmap")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PostureNmap.findAll", query = "SELECT p FROM PostureNmap p"),
    @NamedQuery(name = "PostureNmap.findByRecId", query = "SELECT p FROM PostureNmap p WHERE p.recId = :recId"),
    @NamedQuery(name = "PostureNmap.findByRefId", query = "SELECT p FROM PostureNmap p WHERE p.refId = :refId"),
    @NamedQuery(name = "PostureNmap.findByScanUuid", query = "SELECT p FROM PostureNmap p WHERE p.scanUuid = :scanUuid"),
    @NamedQuery(name = "PostureNmap.findByAlertUuid", query = "SELECT p FROM PostureNmap p WHERE p.alertUuid = :alertUuid"),
    @NamedQuery(name = "PostureNmap.findByNode", query = "SELECT p FROM PostureNmap p WHERE p.node = :node"),
    @NamedQuery(name = "PostureNmap.findByProbe", query = "SELECT p FROM PostureNmap p WHERE p.probe = :probe"),
    @NamedQuery(name = "PostureNmap.findByTarget", query = "SELECT p FROM PostureNmap p WHERE p.target = :target"),
    @NamedQuery(name = "PostureNmap.findByProtocol", query = "SELECT p FROM PostureNmap p WHERE p.protocol = :protocol"),
    @NamedQuery(name = "PostureNmap.findByPortId", query = "SELECT p FROM PostureNmap p WHERE p.portId = :portId"),
    @NamedQuery(name = "PostureNmap.findByPortName", query = "SELECT p FROM PostureNmap p WHERE p.portName = :portName"),
    @NamedQuery(name = "PostureNmap.findBySeverity", query = "SELECT p FROM PostureNmap p WHERE p.severity = :severity"),
    @NamedQuery(name = "PostureNmap.findByStatus", query = "SELECT p FROM PostureNmap p WHERE p.status = :status"),
    @NamedQuery(name = "PostureNmap.findByReportAdded", query = "SELECT p FROM PostureNmap p WHERE p.reportAdded = :reportAdded"),
    @NamedQuery(name = "PostureNmap.findByReportUpdated", query = "SELECT p FROM PostureNmap p WHERE p.reportUpdated = :reportUpdated")})
public class PostureNmap implements Serializable {

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
    @Size(min = 1, max = 128)
    @Column(name = "target")
    private String target;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "protocol")
    private String protocol;
    @Basic(optional = false)
    @NotNull
    @Column(name = "port_id")
    private int portId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "port_name")
    private String portName;
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

    public PostureNmap() {
    }

    public PostureNmap(Long recId) {
        this.recId = recId;
    }

    public PostureNmap(Long recId, String refId, String scanUuid, String alertUuid, String node, String probe, String target, String protocol, int portId, String portName, String severity, String status) {
        this.recId = recId;
        this.refId = refId;
        this.scanUuid = scanUuid;
        this.alertUuid = alertUuid;
        this.node = node;
        this.probe = probe;
        this.target = target;
        this.protocol = protocol;
        this.portId = portId;
        this.portName = portName;
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

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public int getPortId() {
        return portId;
    }

    public void setPortId(int portId) {
        this.portId = portId;
    }

    public String getPortName() {
        return portName;
    }

    public void setPortName(String portName) {
        this.portName = portName;
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
        if (!(object instanceof PostureNmap)) {
            return false;
        }
        PostureNmap other = (PostureNmap) object;
        if ((this.recId == null && other.recId != null) || (this.recId != null && !this.recId.equals(other.recId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.alertflex.entity.PostureNmap[ recId=" + recId + " ]";
    }
    
}
