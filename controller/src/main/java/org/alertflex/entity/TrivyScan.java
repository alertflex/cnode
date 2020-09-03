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
@Table(name = "trivy_scan")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TrivyScan.findAll", query = "SELECT t FROM TrivyScan t")
    , @NamedQuery(name = "TrivyScan.findByRecId", query = "SELECT t FROM TrivyScan t WHERE t.recId = :recId")
    , @NamedQuery(name = "TrivyScan.findByNodeId", query = "SELECT t FROM TrivyScan t WHERE t.nodeId = :nodeId")
    , @NamedQuery(name = "TrivyScan.findByRefId", query = "SELECT t FROM TrivyScan t WHERE t.refId = :refId")
    , @NamedQuery(name = "TrivyScan.findBySensor", query = "SELECT t FROM TrivyScan t WHERE t.sensor = :sensor")
    , @NamedQuery(name = "TrivyScan.findByTarget", query = "SELECT t FROM TrivyScan t WHERE t.target = :target")
    , @NamedQuery(name = "TrivyScan.findByTargetType", query = "SELECT t FROM TrivyScan t WHERE t.targetType = :targetType")
    , @NamedQuery(name = "TrivyScan.findByVulnerabilityId", query = "SELECT t FROM TrivyScan t WHERE t.vulnerabilityId = :vulnerabilityId")
    , @NamedQuery(name = "TrivyScan.findByPkgName", query = "SELECT t FROM TrivyScan t WHERE t.pkgName = :pkgName")
    , @NamedQuery(name = "TrivyScan.findByInstalledVersion", query = "SELECT t FROM TrivyScan t WHERE t.installedVersion = :installedVersion")
    , @NamedQuery(name = "TrivyScan.findByFixedVersion", query = "SELECT t FROM TrivyScan t WHERE t.fixedVersion = :fixedVersion")
    , @NamedQuery(name = "TrivyScan.findBySeveritySource", query = "SELECT t FROM TrivyScan t WHERE t.severitySource = :severitySource")
    , @NamedQuery(name = "TrivyScan.findByTitle", query = "SELECT t FROM TrivyScan t WHERE t.title = :title")
    , @NamedQuery(name = "TrivyScan.findByDescription", query = "SELECT t FROM TrivyScan t WHERE t.description = :description")
    , @NamedQuery(name = "TrivyScan.findBySeverity", query = "SELECT t FROM TrivyScan t WHERE t.severity = :severity")
    , @NamedQuery(name = "TrivyScan.findByReportAdded", query = "SELECT t FROM TrivyScan t WHERE t.reportAdded = :reportAdded")
    , @NamedQuery(name = "TrivyScan.findByReportUpdated", query = "SELECT t FROM TrivyScan t WHERE t.reportUpdated = :reportUpdated")})
public class TrivyScan implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "rec_id")
    private Long recId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "node_id")
    private String nodeId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "ref_id")
    private String refId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "sensor")
    private String sensor;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "target")
    private String target;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "target_type")
    private String targetType;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "vulnerability_id")
    private String vulnerabilityId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "pkg_name")
    private String pkgName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "installed_version")
    private String installedVersion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "fixed_version")
    private String fixedVersion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 256)
    @Column(name = "severity_source")
    private String severitySource;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "title")
    private String title;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1024)
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @NotNull
    @Column(name = "severity")
    private int severity;
    @Column(name = "report_added")
    @Temporal(TemporalType.TIMESTAMP)
    private Date reportAdded;
    @Column(name = "report_updated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date reportUpdated;

    public TrivyScan() {
    }

    public TrivyScan(Long recId) {
        this.recId = recId;
    }

    public TrivyScan(Long recId, String nodeId, String refId, String sensor, String target, String targetType, String vulnerabilityId, String pkgName, String installedVersion, String fixedVersion, String severitySource, String title, String description, int severity) {
        this.recId = recId;
        this.nodeId = nodeId;
        this.refId = refId;
        this.sensor = sensor;
        this.target = target;
        this.targetType = targetType;
        this.vulnerabilityId = vulnerabilityId;
        this.pkgName = pkgName;
        this.installedVersion = installedVersion;
        this.fixedVersion = fixedVersion;
        this.severitySource = severitySource;
        this.title = title;
        this.description = description;
        this.severity = severity;
    }

    public Long getRecId() {
        return recId;
    }

    public void setRecId(Long recId) {
        this.recId = recId;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public String getSensor() {
        return sensor;
    }

    public void setSensor(String sensor) {
        this.sensor = sensor;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public String getVulnerabilityId() {
        return vulnerabilityId;
    }

    public void setVulnerabilityId(String vulnerabilityId) {
        this.vulnerabilityId = vulnerabilityId;
    }

    public String getPkgName() {
        return pkgName;
    }

    public void setPkgName(String pkgName) {
        this.pkgName = pkgName;
    }

    public String getInstalledVersion() {
        return installedVersion;
    }

    public void setInstalledVersion(String installedVersion) {
        this.installedVersion = installedVersion;
    }

    public String getFixedVersion() {
        return fixedVersion;
    }

    public void setFixedVersion(String fixedVersion) {
        this.fixedVersion = fixedVersion;
    }

    public String getSeveritySource() {
        return severitySource;
    }

    public void setSeveritySource(String severitySource) {
        this.severitySource = severitySource;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSeverity() {
        return severity;
    }

    public void setSeverity(int severity) {
        this.severity = severity;
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
        if (!(object instanceof TrivyScan)) {
            return false;
        }
        TrivyScan other = (TrivyScan) object;
        if ((this.recId == null && other.recId != null) || (this.recId != null && !this.recId.equals(other.recId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.alertflex.entity.TrivyScan[ recId=" + recId + " ]";
    }
    
}
