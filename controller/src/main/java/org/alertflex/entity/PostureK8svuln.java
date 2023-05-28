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
@Table(name = "posture_k8svuln")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PostureK8svuln.findAll", query = "SELECT p FROM PostureK8svuln p"),
    @NamedQuery(name = "PostureK8svuln.findByRecId", query = "SELECT p FROM PostureK8svuln p WHERE p.recId = :recId"),
    @NamedQuery(name = "PostureK8svuln.findByRefId", query = "SELECT p FROM PostureK8svuln p WHERE p.refId = :refId"),
    @NamedQuery(name = "PostureK8svuln.findByScanUuid", query = "SELECT p FROM PostureK8svuln p WHERE p.scanUuid = :scanUuid"),
    @NamedQuery(name = "PostureK8svuln.findByAlertUuid", query = "SELECT p FROM PostureK8svuln p WHERE p.alertUuid = :alertUuid"),
    @NamedQuery(name = "PostureK8svuln.findByNode", query = "SELECT p FROM PostureK8svuln p WHERE p.node = :node"),
    @NamedQuery(name = "PostureK8svuln.findByProbe", query = "SELECT p FROM PostureK8svuln p WHERE p.probe = :probe"),
    @NamedQuery(name = "PostureK8svuln.findByClusterName", query = "SELECT p FROM PostureK8svuln p WHERE p.clusterName = :clusterName"),
    @NamedQuery(name = "PostureK8svuln.findByNamespace", query = "SELECT p FROM PostureK8svuln p WHERE p.namespace = :namespace"),
    @NamedQuery(name = "PostureK8svuln.findByTarget", query = "SELECT p FROM PostureK8svuln p WHERE p.target = :target"),
    @NamedQuery(name = "PostureK8svuln.findByTargetClass", query = "SELECT p FROM PostureK8svuln p WHERE p.targetClass = :targetClass"),
    @NamedQuery(name = "PostureK8svuln.findByTargetType", query = "SELECT p FROM PostureK8svuln p WHERE p.targetType = :targetType"),
    @NamedQuery(name = "PostureK8svuln.findByVulnerabilityId", query = "SELECT p FROM PostureK8svuln p WHERE p.vulnerabilityId = :vulnerabilityId"),
    @NamedQuery(name = "PostureK8svuln.findByPkgName", query = "SELECT p FROM PostureK8svuln p WHERE p.pkgName = :pkgName"),
    @NamedQuery(name = "PostureK8svuln.findByPkgVersion", query = "SELECT p FROM PostureK8svuln p WHERE p.pkgVersion = :pkgVersion"),
    @NamedQuery(name = "PostureK8svuln.findByCweid", query = "SELECT p FROM PostureK8svuln p WHERE p.cweid = :cweid"),
    @NamedQuery(name = "PostureK8svuln.findByTitle", query = "SELECT p FROM PostureK8svuln p WHERE p.title = :title"),
    @NamedQuery(name = "PostureK8svuln.findByDescription", query = "SELECT p FROM PostureK8svuln p WHERE p.description = :description"),
    @NamedQuery(name = "PostureK8svuln.findByReference", query = "SELECT p FROM PostureK8svuln p WHERE p.reference = :reference"),
    @NamedQuery(name = "PostureK8svuln.findBySeverity", query = "SELECT p FROM PostureK8svuln p WHERE p.severity = :severity"),
    @NamedQuery(name = "PostureK8svuln.findByStatus", query = "SELECT p FROM PostureK8svuln p WHERE p.status = :status"),
    @NamedQuery(name = "PostureK8svuln.findByReportAdded", query = "SELECT p FROM PostureK8svuln p WHERE p.reportAdded = :reportAdded"),
    @NamedQuery(name = "PostureK8svuln.findByReportUpdated", query = "SELECT p FROM PostureK8svuln p WHERE p.reportUpdated = :reportUpdated")})
public class PostureK8svuln implements Serializable {

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
    @Column(name = "cluster_name")
    private String clusterName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "namespace")
    private String namespace;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "target")
    private String target;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "target_class")
    private String targetClass;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "target_type")
    private String targetType;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
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
    @Column(name = "pkg_version")
    private String pkgVersion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1024)
    @Column(name = "cweid")
    private String cweid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1024)
    @Column(name = "title")
    private String title;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2048)
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1024)
    @Column(name = "reference")
    private String reference;
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

    public PostureK8svuln() {
    }

    public PostureK8svuln(Long recId) {
        this.recId = recId;
    }

    public PostureK8svuln(Long recId, String refId, String scanUuid, String alertUuid, String node, String probe, String clusterName, String namespace, String target, String targetClass, String targetType, String vulnerabilityId, String pkgName, String pkgVersion, String cweid, String title, String description, String reference, String severity, String status) {
        this.recId = recId;
        this.refId = refId;
        this.scanUuid = scanUuid;
        this.alertUuid = alertUuid;
        this.node = node;
        this.probe = probe;
        this.clusterName = clusterName;
        this.namespace = namespace;
        this.target = target;
        this.targetClass = targetClass;
        this.targetType = targetType;
        this.vulnerabilityId = vulnerabilityId;
        this.pkgName = pkgName;
        this.pkgVersion = pkgVersion;
        this.cweid = cweid;
        this.title = title;
        this.description = description;
        this.reference = reference;
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

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(String targetClass) {
        this.targetClass = targetClass;
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

    public String getPkgVersion() {
        return pkgVersion;
    }

    public void setPkgVersion(String pkgVersion) {
        this.pkgVersion = pkgVersion;
    }

    public String getCweid() {
        return cweid;
    }

    public void setCweid(String cweid) {
        this.cweid = cweid;
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

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
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
        if (!(object instanceof PostureK8svuln)) {
            return false;
        }
        PostureK8svuln other = (PostureK8svuln) object;
        if ((this.recId == null && other.recId != null) || (this.recId != null && !this.recId.equals(other.recId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.alertflex.entity.PostureK8svuln[ recId=" + recId + " ]";
    }
    
}
