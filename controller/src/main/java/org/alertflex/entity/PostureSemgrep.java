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
@Table(name = "posture_semgrep")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PostureSemgrep.findAll", query = "SELECT p FROM PostureSemgrep p"),
    @NamedQuery(name = "PostureSemgrep.findByRecId", query = "SELECT p FROM PostureSemgrep p WHERE p.recId = :recId"),
    @NamedQuery(name = "PostureSemgrep.findByRefId", query = "SELECT p FROM PostureSemgrep p WHERE p.refId = :refId"),
    @NamedQuery(name = "PostureSemgrep.findByScanUuid", query = "SELECT p FROM PostureSemgrep p WHERE p.scanUuid = :scanUuid"),
    @NamedQuery(name = "PostureSemgrep.findByAlertUuid", query = "SELECT p FROM PostureSemgrep p WHERE p.alertUuid = :alertUuid"),
    @NamedQuery(name = "PostureSemgrep.findByNode", query = "SELECT p FROM PostureSemgrep p WHERE p.node = :node"),
    @NamedQuery(name = "PostureSemgrep.findByProbe", query = "SELECT p FROM PostureSemgrep p WHERE p.probe = :probe"),
    @NamedQuery(name = "PostureSemgrep.findByTarget", query = "SELECT p FROM PostureSemgrep p WHERE p.target = :target"),
    @NamedQuery(name = "PostureSemgrep.findByComponent", query = "SELECT p FROM PostureSemgrep p WHERE p.component = :component"),
    @NamedQuery(name = "PostureSemgrep.findByCategories", query = "SELECT p FROM PostureSemgrep p WHERE p.categories = :categories"),
    @NamedQuery(name = "PostureSemgrep.findByMessage", query = "SELECT p FROM PostureSemgrep p WHERE p.message = :message"),
    @NamedQuery(name = "PostureSemgrep.findByCwe", query = "SELECT p FROM PostureSemgrep p WHERE p.cwe = :cwe"),
    @NamedQuery(name = "PostureSemgrep.findByRuleId", query = "SELECT p FROM PostureSemgrep p WHERE p.ruleId = :ruleId"),
    @NamedQuery(name = "PostureSemgrep.findByRuleOrigin", query = "SELECT p FROM PostureSemgrep p WHERE p.ruleOrigin = :ruleOrigin"),
    @NamedQuery(name = "PostureSemgrep.findByVulnLine", query = "SELECT p FROM PostureSemgrep p WHERE p.vulnLine = :vulnLine"),
    @NamedQuery(name = "PostureSemgrep.findByStartLine", query = "SELECT p FROM PostureSemgrep p WHERE p.startLine = :startLine"),
    @NamedQuery(name = "PostureSemgrep.findByEndLine", query = "SELECT p FROM PostureSemgrep p WHERE p.endLine = :endLine"),
    @NamedQuery(name = "PostureSemgrep.findByReference", query = "SELECT p FROM PostureSemgrep p WHERE p.reference = :reference"),
    @NamedQuery(name = "PostureSemgrep.findBySeverity", query = "SELECT p FROM PostureSemgrep p WHERE p.severity = :severity"),
    @NamedQuery(name = "PostureSemgrep.findByStatus", query = "SELECT p FROM PostureSemgrep p WHERE p.status = :status"),
    @NamedQuery(name = "PostureSemgrep.findByReportAdded", query = "SELECT p FROM PostureSemgrep p WHERE p.reportAdded = :reportAdded"),
    @NamedQuery(name = "PostureSemgrep.findByReportUpdated", query = "SELECT p FROM PostureSemgrep p WHERE p.reportUpdated = :reportUpdated")})
public class PostureSemgrep implements Serializable {

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
    @Column(name = "component")
    private String component;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "categories")
    private String categories;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2048)
    @Column(name = "message")
    private String message;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1024)
    @Column(name = "cwe")
    private String cwe;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "rule_id")
    private String ruleId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "rule_origin")
    private String ruleOrigin;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1024)
    @Column(name = "vuln_line")
    private String vulnLine;
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
    @Size(min = 1, max = 2048)
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

    public PostureSemgrep() {
    }

    public PostureSemgrep(Long recId) {
        this.recId = recId;
    }

    public PostureSemgrep(Long recId, String refId, String scanUuid, String alertUuid, String node, String probe, String target, String component, String categories, String message, String cwe, String ruleId, String ruleOrigin, String vulnLine, int startLine, int endLine, String reference, String severity, String status) {
        this.recId = recId;
        this.refId = refId;
        this.scanUuid = scanUuid;
        this.alertUuid = alertUuid;
        this.node = node;
        this.probe = probe;
        this.target = target;
        this.component = component;
        this.categories = categories;
        this.message = message;
        this.cwe = cwe;
        this.ruleId = ruleId;
        this.ruleOrigin = ruleOrigin;
        this.vulnLine = vulnLine;
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

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCwe() {
        return cwe;
    }

    public void setCwe(String cwe) {
        this.cwe = cwe;
    }

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public String getRuleOrigin() {
        return ruleOrigin;
    }

    public void setRuleOrigin(String ruleOrigin) {
        this.ruleOrigin = ruleOrigin;
    }

    public String getVulnLine() {
        return vulnLine;
    }

    public void setVulnLine(String vulnLine) {
        this.vulnLine = vulnLine;
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
        if (!(object instanceof PostureSemgrep)) {
            return false;
        }
        PostureSemgrep other = (PostureSemgrep) object;
        if ((this.recId == null && other.recId != null) || (this.recId != null && !this.recId.equals(other.recId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.alertflex.entity.PostureSemgrep[ recId=" + recId + " ]";
    }
    
}
