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
@Table(name = "agent_sca")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AgentSca.findAll", query = "SELECT a FROM AgentSca a"),
    @NamedQuery(name = "AgentSca.findByRecId", query = "SELECT a FROM AgentSca a WHERE a.recId = :recId"),
    @NamedQuery(name = "AgentSca.findByNode", query = "SELECT a FROM AgentSca a WHERE a.node = :node"),
    @NamedQuery(name = "AgentSca.findByRefId", query = "SELECT a FROM AgentSca a WHERE a.refId = :refId"),
    @NamedQuery(name = "AgentSca.findByAgent", query = "SELECT a FROM AgentSca a WHERE a.agent = :agent"),
    @NamedQuery(name = "AgentSca.findByPolicyId", query = "SELECT a FROM AgentSca a WHERE a.policyId = :policyId"),
    @NamedQuery(name = "AgentSca.findByScaId", query = "SELECT a FROM AgentSca a WHERE a.scaId = :scaId"),
    @NamedQuery(name = "AgentSca.findByDescription", query = "SELECT a FROM AgentSca a WHERE a.description = :description"),
    @NamedQuery(name = "AgentSca.findByTitle", query = "SELECT a FROM AgentSca a WHERE a.title = :title"),
    @NamedQuery(name = "AgentSca.findByRationale", query = "SELECT a FROM AgentSca a WHERE a.rationale = :rationale"),
    @NamedQuery(name = "AgentSca.findByRemediation", query = "SELECT a FROM AgentSca a WHERE a.remediation = :remediation"),
    @NamedQuery(name = "AgentSca.findByReportAdded", query = "SELECT a FROM AgentSca a WHERE a.reportAdded = :reportAdded"),
    @NamedQuery(name = "AgentSca.findByReportUpdated", query = "SELECT a FROM AgentSca a WHERE a.reportUpdated = :reportUpdated")})
public class AgentSca implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "rec_id")
    private Long recId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "node")
    private String node;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "ref_id")
    private String refId;
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
    @Column(name = "report_added")
    @Temporal(TemporalType.TIMESTAMP)
    private Date reportAdded;
    @Column(name = "report_updated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date reportUpdated;

    public AgentSca() {
    }

    public AgentSca(Long recId) {
        this.recId = recId;
    }

    public AgentSca(Long recId, String node, String refId, String agent, String policyId, int scaId, String description, String title, String rationale, String remediation) {
        this.recId = recId;
        this.node = node;
        this.refId = refId;
        this.agent = agent;
        this.policyId = policyId;
        this.scaId = scaId;
        this.description = description;
        this.title = title;
        this.rationale = rationale;
        this.remediation = remediation;
    }

    public Long getRecId() {
        return recId;
    }

    public void setRecId(Long recId) {
        this.recId = recId;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
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
        if (!(object instanceof AgentSca)) {
            return false;
        }
        AgentSca other = (AgentSca) object;
        if ((this.recId == null && other.recId != null) || (this.recId != null && !this.recId.equals(other.recId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.alertflex.entity.AgentSca[ recId=" + recId + " ]";
    }
    
}
