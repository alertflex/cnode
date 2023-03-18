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
@Table(name = "posture_k8sconfig")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PostureK8sconfig.findAll", query = "SELECT p FROM PostureK8sconfig p"),
    @NamedQuery(name = "PostureK8sconfig.findByRecId", query = "SELECT p FROM PostureK8sconfig p WHERE p.recId = :recId"),
    @NamedQuery(name = "PostureK8sconfig.findByRefId", query = "SELECT p FROM PostureK8sconfig p WHERE p.refId = :refId"),
    @NamedQuery(name = "PostureK8sconfig.findByScanUuid", query = "SELECT p FROM PostureK8sconfig p WHERE p.scanUuid = :scanUuid"),
    @NamedQuery(name = "PostureK8sconfig.findByNode", query = "SELECT p FROM PostureK8sconfig p WHERE p.node = :node"),
    @NamedQuery(name = "PostureK8sconfig.findByProbe", query = "SELECT p FROM PostureK8sconfig p WHERE p.probe = :probe"),
    @NamedQuery(name = "PostureK8sconfig.findByClusterName", query = "SELECT p FROM PostureK8sconfig p WHERE p.clusterName = :clusterName"),
    @NamedQuery(name = "PostureK8sconfig.findByNamespace", query = "SELECT p FROM PostureK8sconfig p WHERE p.namespace = :namespace"),
    @NamedQuery(name = "PostureK8sconfig.findByTarget", query = "SELECT p FROM PostureK8sconfig p WHERE p.target = :target"),
    @NamedQuery(name = "PostureK8sconfig.findByTargetClass", query = "SELECT p FROM PostureK8sconfig p WHERE p.targetClass = :targetClass"),
    @NamedQuery(name = "PostureK8sconfig.findByTargetType", query = "SELECT p FROM PostureK8sconfig p WHERE p.targetType = :targetType"),
    @NamedQuery(name = "PostureK8sconfig.findByMisconfigType", query = "SELECT p FROM PostureK8sconfig p WHERE p.misconfigType = :misconfigType"),
    @NamedQuery(name = "PostureK8sconfig.findByMisconfigAvdid", query = "SELECT p FROM PostureK8sconfig p WHERE p.misconfigAvdid = :misconfigAvdid"),
    @NamedQuery(name = "PostureK8sconfig.findByTitle", query = "SELECT p FROM PostureK8sconfig p WHERE p.title = :title"),
    @NamedQuery(name = "PostureK8sconfig.findByDescription", query = "SELECT p FROM PostureK8sconfig p WHERE p.description = :description"),
    @NamedQuery(name = "PostureK8sconfig.findByRemediation", query = "SELECT p FROM PostureK8sconfig p WHERE p.remediation = :remediation"),
    @NamedQuery(name = "PostureK8sconfig.findByReference", query = "SELECT p FROM PostureK8sconfig p WHERE p.reference = :reference"),
    @NamedQuery(name = "PostureK8sconfig.findBySeverity", query = "SELECT p FROM PostureK8sconfig p WHERE p.severity = :severity"),
    @NamedQuery(name = "PostureK8sconfig.findByReportAdded", query = "SELECT p FROM PostureK8sconfig p WHERE p.reportAdded = :reportAdded"),
    @NamedQuery(name = "PostureK8sconfig.findByReportUpdated", query = "SELECT p FROM PostureK8sconfig p WHERE p.reportUpdated = :reportUpdated")})
public class PostureK8sconfig implements Serializable {

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
    @Size(min = 1, max = 512)
    @Column(name = "misconfig_type")
    private String misconfigType;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "misconfig_avdid")
    private String misconfigAvdid;
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
    @Size(min = 1, max = 2048)
    @Column(name = "remediation")
    private String remediation;
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
    @Column(name = "report_added")
    @Temporal(TemporalType.TIMESTAMP)
    private Date reportAdded;
    @Column(name = "report_updated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date reportUpdated;

    public PostureK8sconfig() {
    }

    public PostureK8sconfig(Long recId) {
        this.recId = recId;
    }

    public PostureK8sconfig(Long recId, String refId, String scanUuid, String node, String probe, String clusterName, String namespace, String target, String targetClass, String targetType, String misconfigType, String misconfigAvdid, String title, String description, String remediation, String reference, String severity) {
        this.recId = recId;
        this.refId = refId;
        this.scanUuid = scanUuid;
        this.node = node;
        this.probe = probe;
        this.clusterName = clusterName;
        this.namespace = namespace;
        this.target = target;
        this.targetClass = targetClass;
        this.targetType = targetType;
        this.misconfigType = misconfigType;
        this.misconfigAvdid = misconfigAvdid;
        this.title = title;
        this.description = description;
        this.remediation = remediation;
        this.reference = reference;
        this.severity = severity;
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

    public String getMisconfigType() {
        return misconfigType;
    }

    public void setMisconfigType(String misconfigType) {
        this.misconfigType = misconfigType;
    }

    public String getMisconfigAvdid() {
        return misconfigAvdid;
    }

    public void setMisconfigAvdid(String misconfigAvdid) {
        this.misconfigAvdid = misconfigAvdid;
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

    public String getRemediation() {
        return remediation;
    }

    public void setRemediation(String remediation) {
        this.remediation = remediation;
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
        if (!(object instanceof PostureK8sconfig)) {
            return false;
        }
        PostureK8sconfig other = (PostureK8sconfig) object;
        if ((this.recId == null && other.recId != null) || (this.recId != null && !this.recId.equals(other.recId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.alertflex.entity.PostureK8sconfig[ recId=" + recId + " ]";
    }
    
}
