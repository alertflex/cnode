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
@Table(name = "tfsec_scan")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TfsecScan.findAll", query = "SELECT t FROM TfsecScan t"),
    @NamedQuery(name = "TfsecScan.findByRecId", query = "SELECT t FROM TfsecScan t WHERE t.recId = :recId"),
    @NamedQuery(name = "TfsecScan.findByRefId", query = "SELECT t FROM TfsecScan t WHERE t.refId = :refId"),
    @NamedQuery(name = "TfsecScan.findByNode", query = "SELECT t FROM TfsecScan t WHERE t.node = :node"),
    @NamedQuery(name = "TfsecScan.findByProbe", query = "SELECT t FROM TfsecScan t WHERE t.probe = :probe"),
    @NamedQuery(name = "TfsecScan.findByProjectId", query = "SELECT t FROM TfsecScan t WHERE t.projectId = :projectId"),
    @NamedQuery(name = "TfsecScan.findByRuleId", query = "SELECT t FROM TfsecScan t WHERE t.ruleId = :ruleId"),
    @NamedQuery(name = "TfsecScan.findByLongId", query = "SELECT t FROM TfsecScan t WHERE t.longId = :longId"),
    @NamedQuery(name = "TfsecScan.findByRuleDescription", query = "SELECT t FROM TfsecScan t WHERE t.ruleDescription = :ruleDescription"),
    @NamedQuery(name = "TfsecScan.findByRuleProvider", query = "SELECT t FROM TfsecScan t WHERE t.ruleProvider = :ruleProvider"),
    @NamedQuery(name = "TfsecScan.findByRuleService", query = "SELECT t FROM TfsecScan t WHERE t.ruleService = :ruleService"),
    @NamedQuery(name = "TfsecScan.findByImpact", query = "SELECT t FROM TfsecScan t WHERE t.impact = :impact"),
    @NamedQuery(name = "TfsecScan.findByResolution", query = "SELECT t FROM TfsecScan t WHERE t.resolution = :resolution"),
    @NamedQuery(name = "TfsecScan.findByLinks", query = "SELECT t FROM TfsecScan t WHERE t.links = :links"),
    @NamedQuery(name = "TfsecScan.findByDescription", query = "SELECT t FROM TfsecScan t WHERE t.description = :description"),
    @NamedQuery(name = "TfsecScan.findBySeverity", query = "SELECT t FROM TfsecScan t WHERE t.severity = :severity"),
    @NamedQuery(name = "TfsecScan.findByStatus", query = "SELECT t FROM TfsecScan t WHERE t.status = :status"),
    @NamedQuery(name = "TfsecScan.findByResource", query = "SELECT t FROM TfsecScan t WHERE t.resource = :resource"),
    @NamedQuery(name = "TfsecScan.findByFilename", query = "SELECT t FROM TfsecScan t WHERE t.filename = :filename"),
    @NamedQuery(name = "TfsecScan.findByStartLine", query = "SELECT t FROM TfsecScan t WHERE t.startLine = :startLine"),
    @NamedQuery(name = "TfsecScan.findByEndLine", query = "SELECT t FROM TfsecScan t WHERE t.endLine = :endLine"),
    @NamedQuery(name = "TfsecScan.findByReportAdded", query = "SELECT t FROM TfsecScan t WHERE t.reportAdded = :reportAdded"),
    @NamedQuery(name = "TfsecScan.findByReportUpdated", query = "SELECT t FROM TfsecScan t WHERE t.reportUpdated = :reportUpdated")})
public class TfsecScan implements Serializable {

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
    @Column(name = "node")
    private String node;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "probe")
    private String probe;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1024)
    @Column(name = "project_id")
    private String projectId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "rule_id")
    private String ruleId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "long_id")
    private String longId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1024)
    @Column(name = "rule_description")
    private String ruleDescription;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "rule_provider")
    private String ruleProvider;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "rule_service")
    private String ruleService;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1024)
    @Column(name = "impact")
    private String impact;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1024)
    @Column(name = "resolution")
    private String resolution;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2048)
    @Column(name = "links")
    private String links;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1024)
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "severity")
    private String severity;
    @Basic(optional = false)
    @NotNull
    @Column(name = "status")
    private int status;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "resource")
    private String resource;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1024)
    @Column(name = "filename")
    private String filename;
    @Basic(optional = false)
    @NotNull
    @Column(name = "start_line")
    private int startLine;
    @Basic(optional = false)
    @NotNull
    @Column(name = "end_line")
    private int endLine;
    @Column(name = "report_added")
    @Temporal(TemporalType.TIMESTAMP)
    private Date reportAdded;
    @Column(name = "report_updated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date reportUpdated;

    public TfsecScan() {
    }

    public TfsecScan(Long recId) {
        this.recId = recId;
    }

    public TfsecScan(Long recId, String refId, String node, String probe, String projectId, String ruleId, String longId, String ruleDescription, String ruleProvider, String ruleService, String impact, String resolution, String links, String description, String severity, int status, String resource, String filename, int startLine, int endLine) {
        this.recId = recId;
        this.refId = refId;
        this.node = node;
        this.probe = probe;
        this.projectId = projectId;
        this.ruleId = ruleId;
        this.longId = longId;
        this.ruleDescription = ruleDescription;
        this.ruleProvider = ruleProvider;
        this.ruleService = ruleService;
        this.impact = impact;
        this.resolution = resolution;
        this.links = links;
        this.description = description;
        this.severity = severity;
        this.status = status;
        this.resource = resource;
        this.filename = filename;
        this.startLine = startLine;
        this.endLine = endLine;
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

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public String getLongId() {
        return longId;
    }

    public void setLongId(String longId) {
        this.longId = longId;
    }

    public String getRuleDescription() {
        return ruleDescription;
    }

    public void setRuleDescription(String ruleDescription) {
        this.ruleDescription = ruleDescription;
    }

    public String getRuleProvider() {
        return ruleProvider;
    }

    public void setRuleProvider(String ruleProvider) {
        this.ruleProvider = ruleProvider;
    }

    public String getRuleService() {
        return ruleService;
    }

    public void setRuleService(String ruleService) {
        this.ruleService = ruleService;
    }

    public String getImpact() {
        return impact;
    }

    public void setImpact(String impact) {
        this.impact = impact;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getLinks() {
        return links;
    }

    public void setLinks(String links) {
        this.links = links;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
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
        if (!(object instanceof TfsecScan)) {
            return false;
        }
        TfsecScan other = (TfsecScan) object;
        if ((this.recId == null && other.recId != null) || (this.recId != null && !this.recId.equals(other.recId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.alertflex.entity.TfsecScan[ recId=" + recId + " ]";
    }
    
}
