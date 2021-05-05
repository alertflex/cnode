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
@Table(name = "sonar_scan")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SonarScan.findAll", query = "SELECT s FROM SonarScan s"),
    @NamedQuery(name = "SonarScan.findByRecId", query = "SELECT s FROM SonarScan s WHERE s.recId = :recId"),
    @NamedQuery(name = "SonarScan.findByRefId", query = "SELECT s FROM SonarScan s WHERE s.refId = :refId"),
    @NamedQuery(name = "SonarScan.findByNodeId", query = "SELECT s FROM SonarScan s WHERE s.nodeId = :nodeId"),
    @NamedQuery(name = "SonarScan.findByProbe", query = "SELECT s FROM SonarScan s WHERE s.probe = :probe"),
    @NamedQuery(name = "SonarScan.findByProjectId", query = "SELECT s FROM SonarScan s WHERE s.projectId = :projectId"),
    @NamedQuery(name = "SonarScan.findByProjectPath", query = "SELECT s FROM SonarScan s WHERE s.projectPath = :projectPath"),
    @NamedQuery(name = "SonarScan.findByIssueKey", query = "SELECT s FROM SonarScan s WHERE s.issueKey = :issueKey"),
    @NamedQuery(name = "SonarScan.findByIssueSeverity", query = "SELECT s FROM SonarScan s WHERE s.issueSeverity = :issueSeverity"),
    @NamedQuery(name = "SonarScan.findBySeverity", query = "SELECT s FROM SonarScan s WHERE s.severity = :severity"),
    @NamedQuery(name = "SonarScan.findByRule", query = "SELECT s FROM SonarScan s WHERE s.rule = :rule"),
    @NamedQuery(name = "SonarScan.findByComponent", query = "SELECT s FROM SonarScan s WHERE s.component = :component"),
    @NamedQuery(name = "SonarScan.findByMessage", query = "SELECT s FROM SonarScan s WHERE s.message = :message"),
    @NamedQuery(name = "SonarScan.findByStatus", query = "SELECT s FROM SonarScan s WHERE s.status = :status"),
    @NamedQuery(name = "SonarScan.findByTags", query = "SELECT s FROM SonarScan s WHERE s.tags = :tags"),
    @NamedQuery(name = "SonarScan.findByLine", query = "SELECT s FROM SonarScan s WHERE s.line = :line"),
    @NamedQuery(name = "SonarScan.findByReportCreated", query = "SELECT s FROM SonarScan s WHERE s.reportCreated = :reportCreated"),
    @NamedQuery(name = "SonarScan.findByReportUpdated", query = "SELECT s FROM SonarScan s WHERE s.reportUpdated = :reportUpdated")})
public class SonarScan implements Serializable {

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
    @Size(min = 1, max = 128)
    @Column(name = "node_id")
    private String nodeId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "probe")
    private String probe;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "project_id")
    private String projectId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1024)
    @Column(name = "project_path")
    private String projectPath;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "issue_key")
    private String issueKey;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "issue_severity")
    private String issueSeverity;
    @Basic(optional = false)
    @NotNull
    @Column(name = "severity")
    private int severity;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 256)
    @Column(name = "rule")
    private String rule;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2048)
    @Column(name = "component")
    private String component;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2048)
    @Column(name = "message")
    private String message;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "status")
    private String status;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1024)
    @Column(name = "tags")
    private String tags;
    @Basic(optional = false)
    @NotNull
    @Column(name = "line")
    private int line;
    @Column(name = "report_created")
    @Temporal(TemporalType.TIMESTAMP)
    private Date reportCreated;
    @Column(name = "report_updated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date reportUpdated;

    public SonarScan() {
    }

    public SonarScan(Long recId) {
        this.recId = recId;
    }

    public SonarScan(Long recId, String refId, String nodeId, String probe, String projectId, String projectPath, String issueKey, String issueSeverity, int severity, String rule, String component, String message, String status, String tags, int line) {
        this.recId = recId;
        this.refId = refId;
        this.nodeId = nodeId;
        this.probe = probe;
        this.projectId = projectId;
        this.projectPath = projectPath;
        this.issueKey = issueKey;
        this.issueSeverity = issueSeverity;
        this.severity = severity;
        this.rule = rule;
        this.component = component;
        this.message = message;
        this.status = status;
        this.tags = tags;
        this.line = line;
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

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getProbe() {
        return probe;
    }

    public void setProbe(String probe) {
        this.probe = probe;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectPath() {
        return projectPath;
    }

    public void setProjectPath(String projectPath) {
        this.projectPath = projectPath;
    }

    public String getIssueKey() {
        return issueKey;
    }

    public void setIssueKey(String issueKey) {
        this.issueKey = issueKey;
    }

    public String getIssueSeverity() {
        return issueSeverity;
    }

    public void setIssueSeverity(String issueSeverity) {
        this.issueSeverity = issueSeverity;
    }

    public int getSeverity() {
        return severity;
    }

    public void setSeverity(int severity) {
        this.severity = severity;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public Date getReportCreated() {
        return reportCreated;
    }

    public void setReportCreated(Date reportCreated) {
        this.reportCreated = reportCreated;
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
        if (!(object instanceof SonarScan)) {
            return false;
        }
        SonarScan other = (SonarScan) object;
        if ((this.recId == null && other.recId != null) || (this.recId != null && !this.recId.equals(other.recId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.alertflex.entity.SonarScan[ recId=" + recId + " ]";
    }
    
}
