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
@Table(name = "posture_appsecret")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PostureAppsecret.findAll", query = "SELECT p FROM PostureAppsecret p"),
    @NamedQuery(name = "PostureAppsecret.findByRecId", query = "SELECT p FROM PostureAppsecret p WHERE p.recId = :recId"),
    @NamedQuery(name = "PostureAppsecret.findByRefId", query = "SELECT p FROM PostureAppsecret p WHERE p.refId = :refId"),
    @NamedQuery(name = "PostureAppsecret.findByScanUuid", query = "SELECT p FROM PostureAppsecret p WHERE p.scanUuid = :scanUuid"),
    @NamedQuery(name = "PostureAppsecret.findByNode", query = "SELECT p FROM PostureAppsecret p WHERE p.node = :node"),
    @NamedQuery(name = "PostureAppsecret.findByProbe", query = "SELECT p FROM PostureAppsecret p WHERE p.probe = :probe"),
    @NamedQuery(name = "PostureAppsecret.findByArtifactName", query = "SELECT p FROM PostureAppsecret p WHERE p.artifactName = :artifactName"),
    @NamedQuery(name = "PostureAppsecret.findByArtifactType", query = "SELECT p FROM PostureAppsecret p WHERE p.artifactType = :artifactType"),
    @NamedQuery(name = "PostureAppsecret.findByTarget", query = "SELECT p FROM PostureAppsecret p WHERE p.target = :target"),
    @NamedQuery(name = "PostureAppsecret.findByTargetClass", query = "SELECT p FROM PostureAppsecret p WHERE p.targetClass = :targetClass"),
    @NamedQuery(name = "PostureAppsecret.findByRuleId", query = "SELECT p FROM PostureAppsecret p WHERE p.ruleId = :ruleId"),
    @NamedQuery(name = "PostureAppsecret.findByCategory", query = "SELECT p FROM PostureAppsecret p WHERE p.category = :category"),
    @NamedQuery(name = "PostureAppsecret.findByTitle", query = "SELECT p FROM PostureAppsecret p WHERE p.title = :title"),
    @NamedQuery(name = "PostureAppsecret.findByStartLine", query = "SELECT p FROM PostureAppsecret p WHERE p.startLine = :startLine"),
    @NamedQuery(name = "PostureAppsecret.findByEndLine", query = "SELECT p FROM PostureAppsecret p WHERE p.endLine = :endLine"),
    @NamedQuery(name = "PostureAppsecret.findBySeverity", query = "SELECT p FROM PostureAppsecret p WHERE p.severity = :severity"),
    @NamedQuery(name = "PostureAppsecret.findByReportAdded", query = "SELECT p FROM PostureAppsecret p WHERE p.reportAdded = :reportAdded"),
    @NamedQuery(name = "PostureAppsecret.findByReportUpdated", query = "SELECT p FROM PostureAppsecret p WHERE p.reportUpdated = :reportUpdated")})
public class PostureAppsecret implements Serializable {

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
    @Size(min = 1, max = 1024)
    @Column(name = "rule_id")
    private String ruleId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1024)
    @Column(name = "category")
    private String category;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1024)
    @Column(name = "title")
    private String title;
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
    @Size(min = 1, max = 128)
    @Column(name = "severity")
    private String severity;
    @Column(name = "report_added")
    @Temporal(TemporalType.TIMESTAMP)
    private Date reportAdded;
    @Column(name = "report_updated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date reportUpdated;

    public PostureAppsecret() {
    }

    public PostureAppsecret(Long recId) {
        this.recId = recId;
    }

    public PostureAppsecret(Long recId, String refId, String scanUuid, String node, String probe, String artifactName, String artifactType, String target, String targetClass, String ruleId, String category, String title, int startLine, int endLine, String severity) {
        this.recId = recId;
        this.refId = refId;
        this.scanUuid = scanUuid;
        this.node = node;
        this.probe = probe;
        this.artifactName = artifactName;
        this.artifactType = artifactType;
        this.target = target;
        this.targetClass = targetClass;
        this.ruleId = ruleId;
        this.category = category;
        this.title = title;
        this.startLine = startLine;
        this.endLine = endLine;
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

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
        if (!(object instanceof PostureAppsecret)) {
            return false;
        }
        PostureAppsecret other = (PostureAppsecret) object;
        if ((this.recId == null && other.recId != null) || (this.recId != null && !this.recId.equals(other.recId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.alertflex.entity.PostureAppsecret[ recId=" + recId + " ]";
    }
    
}
