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
@Table(name = "agent_vul")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AgentVul.findAll", query = "SELECT a FROM AgentVul a"),
    @NamedQuery(name = "AgentVul.findByRecId", query = "SELECT a FROM AgentVul a WHERE a.recId = :recId"),
    @NamedQuery(name = "AgentVul.findByRefId", query = "SELECT a FROM AgentVul a WHERE a.refId = :refId"),
    @NamedQuery(name = "AgentVul.findByNodeId", query = "SELECT a FROM AgentVul a WHERE a.nodeId = :nodeId"),
    @NamedQuery(name = "AgentVul.findByAgent", query = "SELECT a FROM AgentVul a WHERE a.agent = :agent"),
    @NamedQuery(name = "AgentVul.findByPkgName", query = "SELECT a FROM AgentVul a WHERE a.pkgName = :pkgName"),
    @NamedQuery(name = "AgentVul.findByPkgVersion", query = "SELECT a FROM AgentVul a WHERE a.pkgVersion = :pkgVersion"),
    @NamedQuery(name = "AgentVul.findByVulnerability", query = "SELECT a FROM AgentVul a WHERE a.vulnerability = :vulnerability"),
    @NamedQuery(name = "AgentVul.findByTitle", query = "SELECT a FROM AgentVul a WHERE a.title = :title"),
    @NamedQuery(name = "AgentVul.findByDescription", query = "SELECT a FROM AgentVul a WHERE a.description = :description"),
    @NamedQuery(name = "AgentVul.findByVulnRef", query = "SELECT a FROM AgentVul a WHERE a.vulnRef = :vulnRef"),
    @NamedQuery(name = "AgentVul.findBySeverity", query = "SELECT a FROM AgentVul a WHERE a.severity = :severity"),
    @NamedQuery(name = "AgentVul.findByReportAdded", query = "SELECT a FROM AgentVul a WHERE a.reportAdded = :reportAdded"),
    @NamedQuery(name = "AgentVul.findByReportUpdated", query = "SELECT a FROM AgentVul a WHERE a.reportUpdated = :reportUpdated")})
public class AgentVul implements Serializable {

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
    @Column(name = "agent")
    private String agent;
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
    @Size(min = 1, max = 512)
    @Column(name = "vulnerability")
    private String vulnerability;
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
    @Column(name = "vuln_ref")
    private String vulnRef;
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

    public AgentVul() {
    }

    public AgentVul(Long recId) {
        this.recId = recId;
    }

    public AgentVul(Long recId, String refId, String nodeId, String agent, String pkgName, String pkgVersion, String vulnerability, String title, String description, String vulnRef, String severity) {
        this.recId = recId;
        this.refId = refId;
        this.nodeId = nodeId;
        this.agent = agent;
        this.pkgName = pkgName;
        this.pkgVersion = pkgVersion;
        this.vulnerability = vulnerability;
        this.title = title;
        this.description = description;
        this.vulnRef = vulnRef;
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

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
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

    public String getVulnerability() {
        return vulnerability;
    }

    public void setVulnerability(String vulnerability) {
        this.vulnerability = vulnerability;
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

    public String getVulnRef() {
        return vulnRef;
    }

    public void setVulnRef(String vulnRef) {
        this.vulnRef = vulnRef;
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
        if (!(object instanceof AgentVul)) {
            return false;
        }
        AgentVul other = (AgentVul) object;
        if ((this.recId == null && other.recId != null) || (this.recId != null && !this.recId.equals(other.recId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.alertflex.entity.AgentVul[ recId=" + recId + " ]";
    }
    
}
