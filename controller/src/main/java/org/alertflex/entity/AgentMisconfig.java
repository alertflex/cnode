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
@Table(name = "agent_misconfig")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AgentMisconfig.findAll", query = "SELECT a FROM AgentMisconfig a"),
    @NamedQuery(name = "AgentMisconfig.findByRecId", query = "SELECT a FROM AgentMisconfig a WHERE a.recId = :recId"),
    @NamedQuery(name = "AgentMisconfig.findByRefId", query = "SELECT a FROM AgentMisconfig a WHERE a.refId = :refId"),
    @NamedQuery(name = "AgentMisconfig.findByAlertUuid", query = "SELECT a FROM AgentMisconfig a WHERE a.alertUuid = :alertUuid"),
    @NamedQuery(name = "AgentMisconfig.findByNode", query = "SELECT a FROM AgentMisconfig a WHERE a.node = :node"),
    @NamedQuery(name = "AgentMisconfig.findByProbe", query = "SELECT a FROM AgentMisconfig a WHERE a.probe = :probe"),
    @NamedQuery(name = "AgentMisconfig.findByAgent", query = "SELECT a FROM AgentMisconfig a WHERE a.agent = :agent"),
    @NamedQuery(name = "AgentMisconfig.findByPolicyId", query = "SELECT a FROM AgentMisconfig a WHERE a.policyId = :policyId"),
    @NamedQuery(name = "AgentMisconfig.findByScaId", query = "SELECT a FROM AgentMisconfig a WHERE a.scaId = :scaId"),
    @NamedQuery(name = "AgentMisconfig.findByDescription", query = "SELECT a FROM AgentMisconfig a WHERE a.description = :description"),
    @NamedQuery(name = "AgentMisconfig.findByTitle", query = "SELECT a FROM AgentMisconfig a WHERE a.title = :title"),
    @NamedQuery(name = "AgentMisconfig.findByRationale", query = "SELECT a FROM AgentMisconfig a WHERE a.rationale = :rationale"),
    @NamedQuery(name = "AgentMisconfig.findByRemediation", query = "SELECT a FROM AgentMisconfig a WHERE a.remediation = :remediation"),
    @NamedQuery(name = "AgentMisconfig.findBySeverity", query = "SELECT a FROM AgentMisconfig a WHERE a.severity = :severity"),
    @NamedQuery(name = "AgentMisconfig.findByStatus", query = "SELECT a FROM AgentMisconfig a WHERE a.status = :status"),
    @NamedQuery(name = "AgentMisconfig.findByReportAdded", query = "SELECT a FROM AgentMisconfig a WHERE a.reportAdded = :reportAdded"),
    @NamedQuery(name = "AgentMisconfig.findByReportUpdated", query = "SELECT a FROM AgentMisconfig a WHERE a.reportUpdated = :reportUpdated")})
public class AgentMisconfig implements Serializable {

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
    @Column(name = "agent")
    private String agent;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "policy_id")
    private String policyId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sca_id")
    private int scaId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1024)
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1024)
    @Column(name = "title")
    private String title;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2048)
    @Column(name = "rationale")
    private String rationale;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2048)
    @Column(name = "remediation")
    private String remediation;
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

    public AgentMisconfig() {
    }

    public AgentMisconfig(Long recId) {
        this.recId = recId;
    }

    public AgentMisconfig(Long recId, String refId, String alertUuid, String node, String probe, String agent, String policyId, int scaId, String description, String title, String rationale, String remediation, String severity, String status) {
        this.recId = recId;
        this.refId = refId;
        this.alertUuid = alertUuid;
        this.node = node;
        this.probe = probe;
        this.agent = agent;
        this.policyId = policyId;
        this.scaId = scaId;
        this.description = description;
        this.title = title;
        this.rationale = rationale;
        this.remediation = remediation;
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

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public String getPolicyId() {
        return policyId;
    }

    public void setPolicyId(String policyId) {
        this.policyId = policyId;
    }

    public int getScaId() {
        return scaId;
    }

    public void setScaId(int scaId) {
        this.scaId = scaId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRationale() {
        return rationale;
    }

    public void setRationale(String rationale) {
        this.rationale = rationale;
    }

    public String getRemediation() {
        return remediation;
    }

    public void setRemediation(String remediation) {
        this.remediation = remediation;
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
        if (!(object instanceof AgentMisconfig)) {
            return false;
        }
        AgentMisconfig other = (AgentMisconfig) object;
        if ((this.recId == null && other.recId != null) || (this.recId != null && !this.recId.equals(other.recId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.alertflex.entity.AgentMisconfig[ recId=" + recId + " ]";
    }
    
}
