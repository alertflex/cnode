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
@Table(name = "dependency_scan")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DependencyScan.findAll", query = "SELECT d FROM DependencyScan d"),
    @NamedQuery(name = "DependencyScan.findByRecId", query = "SELECT d FROM DependencyScan d WHERE d.recId = :recId"),
    @NamedQuery(name = "DependencyScan.findByRefId", query = "SELECT d FROM DependencyScan d WHERE d.refId = :refId"),
    @NamedQuery(name = "DependencyScan.findByNode", query = "SELECT d FROM DependencyScan d WHERE d.node = :node"),
    @NamedQuery(name = "DependencyScan.findByProbe", query = "SELECT d FROM DependencyScan d WHERE d.probe = :probe"),
    @NamedQuery(name = "DependencyScan.findByProjectId", query = "SELECT d FROM DependencyScan d WHERE d.projectId = :projectId"),
    @NamedQuery(name = "DependencyScan.findByFileName", query = "SELECT d FROM DependencyScan d WHERE d.fileName = :fileName"),
    @NamedQuery(name = "DependencyScan.findByFilePath", query = "SELECT d FROM DependencyScan d WHERE d.filePath = :filePath"),
    @NamedQuery(name = "DependencyScan.findBySeverity", query = "SELECT d FROM DependencyScan d WHERE d.severity = :severity"),
    @NamedQuery(name = "DependencyScan.findByVulnName", query = "SELECT d FROM DependencyScan d WHERE d.vulnName = :vulnName"),
    @NamedQuery(name = "DependencyScan.findByVulnRef", query = "SELECT d FROM DependencyScan d WHERE d.vulnRef = :vulnRef"),
    @NamedQuery(name = "DependencyScan.findByDescription", query = "SELECT d FROM DependencyScan d WHERE d.description = :description"),
    @NamedQuery(name = "DependencyScan.findByReportCreated", query = "SELECT d FROM DependencyScan d WHERE d.reportCreated = :reportCreated"),
    @NamedQuery(name = "DependencyScan.findByReportUpdated", query = "SELECT d FROM DependencyScan d WHERE d.reportUpdated = :reportUpdated")})
public class DependencyScan implements Serializable {

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
    @Size(min = 1, max = 512)
    @Column(name = "project_id")
    private String projectId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1024)
    @Column(name = "file_name")
    private String fileName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1024)
    @Column(name = "file_path")
    private String filePath;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "severity")
    private String severity;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "vuln_name")
    private String vulnName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2048)
    @Column(name = "vuln_ref")
    private String vulnRef;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2048)
    @Column(name = "description")
    private String description;
    @Column(name = "report_created")
    @Temporal(TemporalType.TIMESTAMP)
    private Date reportCreated;
    @Column(name = "report_updated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date reportUpdated;

    public DependencyScan() {
    }

    public DependencyScan(Long recId) {
        this.recId = recId;
    }

    public DependencyScan(Long recId, String refId, String node, String probe, String projectId, String fileName, String filePath, String severity, String vulnName, String vulnRef, String description) {
        this.recId = recId;
        this.refId = refId;
        this.node = node;
        this.probe = probe;
        this.projectId = projectId;
        this.fileName = fileName;
        this.filePath = filePath;
        this.severity = severity;
        this.vulnName = vulnName;
        this.vulnRef = vulnRef;
        this.description = description;
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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getVulnName() {
        return vulnName;
    }

    public void setVulnName(String vulnName) {
        this.vulnName = vulnName;
    }

    public String getVulnRef() {
        return vulnRef;
    }

    public void setVulnRef(String vulnRef) {
        this.vulnRef = vulnRef;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
        if (!(object instanceof DependencyScan)) {
            return false;
        }
        DependencyScan other = (DependencyScan) object;
        if ((this.recId == null && other.recId != null) || (this.recId != null && !this.recId.equals(other.recId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.alertflex.entity.DependencyScan[ recId=" + recId + " ]";
    }
    
}
