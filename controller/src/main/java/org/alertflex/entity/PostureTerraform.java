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
@Table(name = "posture_terraform")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PostureTerraform.findAll", query = "SELECT p FROM PostureTerraform p"),
    @NamedQuery(name = "PostureTerraform.findByRecId", query = "SELECT p FROM PostureTerraform p WHERE p.recId = :recId"),
    @NamedQuery(name = "PostureTerraform.findByRefId", query = "SELECT p FROM PostureTerraform p WHERE p.refId = :refId"),
    @NamedQuery(name = "PostureTerraform.findByScanUuid", query = "SELECT p FROM PostureTerraform p WHERE p.scanUuid = :scanUuid"),
    @NamedQuery(name = "PostureTerraform.findByAlertUuid", query = "SELECT p FROM PostureTerraform p WHERE p.alertUuid = :alertUuid"),
    @NamedQuery(name = "PostureTerraform.findByNode", query = "SELECT p FROM PostureTerraform p WHERE p.node = :node"),
    @NamedQuery(name = "PostureTerraform.findByProbe", query = "SELECT p FROM PostureTerraform p WHERE p.probe = :probe"),
    @NamedQuery(name = "PostureTerraform.findByProvider", query = "SELECT p FROM PostureTerraform p WHERE p.provider = :provider"),
    @NamedQuery(name = "PostureTerraform.findByService", query = "SELECT p FROM PostureTerraform p WHERE p.service = :service"),
    @NamedQuery(name = "PostureTerraform.findByArtifactName", query = "SELECT p FROM PostureTerraform p WHERE p.artifactName = :artifactName"),
    @NamedQuery(name = "PostureTerraform.findByArtifactType", query = "SELECT p FROM PostureTerraform p WHERE p.artifactType = :artifactType"),
    @NamedQuery(name = "PostureTerraform.findByTarget", query = "SELECT p FROM PostureTerraform p WHERE p.target = :target"),
    @NamedQuery(name = "PostureTerraform.findByTargetClass", query = "SELECT p FROM PostureTerraform p WHERE p.targetClass = :targetClass"),
    @NamedQuery(name = "PostureTerraform.findByTargetType", query = "SELECT p FROM PostureTerraform p WHERE p.targetType = :targetType"),
    @NamedQuery(name = "PostureTerraform.findByMisconfigType", query = "SELECT p FROM PostureTerraform p WHERE p.misconfigType = :misconfigType"),
    @NamedQuery(name = "PostureTerraform.findByMisconfigAvdid", query = "SELECT p FROM PostureTerraform p WHERE p.misconfigAvdid = :misconfigAvdid"),
    @NamedQuery(name = "PostureTerraform.findByTitle", query = "SELECT p FROM PostureTerraform p WHERE p.title = :title"),
    @NamedQuery(name = "PostureTerraform.findByDescription", query = "SELECT p FROM PostureTerraform p WHERE p.description = :description"),
    @NamedQuery(name = "PostureTerraform.findByRemediation", query = "SELECT p FROM PostureTerraform p WHERE p.remediation = :remediation"),
    @NamedQuery(name = "PostureTerraform.findByStartLine", query = "SELECT p FROM PostureTerraform p WHERE p.startLine = :startLine"),
    @NamedQuery(name = "PostureTerraform.findByEndLine", query = "SELECT p FROM PostureTerraform p WHERE p.endLine = :endLine"),
    @NamedQuery(name = "PostureTerraform.findByReference", query = "SELECT p FROM PostureTerraform p WHERE p.reference = :reference"),
    @NamedQuery(name = "PostureTerraform.findBySeverity", query = "SELECT p FROM PostureTerraform p WHERE p.severity = :severity"),
    @NamedQuery(name = "PostureTerraform.findByStatus", query = "SELECT p FROM PostureTerraform p WHERE p.status = :status"),
    @NamedQuery(name = "PostureTerraform.findByReportAdded", query = "SELECT p FROM PostureTerraform p WHERE p.reportAdded = :reportAdded"),
    @NamedQuery(name = "PostureTerraform.findByReportUpdated", query = "SELECT p FROM PostureTerraform p WHERE p.reportUpdated = :reportUpdated")})
public class PostureTerraform implements Serializable {

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
    @Column(name = "provider")
    private String provider;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "service")
    private String service;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "artifact_name")
    private String artifactName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "artifact_type")
    private String artifactType;
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
    @Column(name = "start_line")
    private int startLine;
    @Basic(optional = false)
    @NotNull
    @Column(name = "end_line")
    private int endLine;
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

    public PostureTerraform() {
    }

    public PostureTerraform(Long recId) {
        this.recId = recId;
    }

    public PostureTerraform(Long recId, String refId, String scanUuid, String alertUuid, String node, String probe, String provider, String service, String artifactName, String artifactType, String target, String targetClass, String targetType, String misconfigType, String misconfigAvdid, String title, String description, String remediation, int startLine, int endLine, String reference, String severity, String status) {
        this.recId = recId;
        this.refId = refId;
        this.scanUuid = scanUuid;
        this.alertUuid = alertUuid;
        this.node = node;
        this.probe = probe;
        this.provider = provider;
        this.service = service;
        this.artifactName = artifactName;
        this.artifactType = artifactType;
        this.target = target;
        this.targetClass = targetClass;
        this.targetType = targetType;
        this.misconfigType = misconfigType;
        this.misconfigAvdid = misconfigAvdid;
        this.title = title;
        this.description = description;
        this.remediation = remediation;
        this.startLine = startLine;
        this.endLine = endLine;
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

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getArtifactName() {
        return artifactName;
    }

    public void setArtifactName(String artifactName) {
        this.artifactName = artifactName;
    }

    public String getArtifactType() {
        return artifactType;
    }

    public void setArtifactType(String artifactType) {
        this.artifactType = artifactType;
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

    public int getStartLine() {
        return startLine;
    }

    public void setStartLine(int startLine) {
        this.startLine = startLine;
    }

    public int getEndLine() {
        return endLine;
    }

    public void setEndLine(int endLine) {
        this.endLine = endLine;
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
        if (!(object instanceof PostureTerraform)) {
            return false;
        }
        PostureTerraform other = (PostureTerraform) object;
        if ((this.recId == null && other.recId != null) || (this.recId != null && !this.recId.equals(other.recId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.alertflex.entity.PostureTerraform[ recId=" + recId + " ]";
    }
    
}
