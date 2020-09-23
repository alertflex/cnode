/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alertflex.mc.db;

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
    @NamedQuery(name = "AgentSca.findAll", query = "SELECT a FROM AgentSca a")
    , @NamedQuery(name = "AgentSca.findByRecId", query = "SELECT a FROM AgentSca a WHERE a.recId = :recId")
    , @NamedQuery(name = "AgentSca.findByNodeId", query = "SELECT a FROM AgentSca a WHERE a.nodeId = :nodeId")
    , @NamedQuery(name = "AgentSca.findByRefId", query = "SELECT a FROM AgentSca a WHERE a.refId = :refId")
    , @NamedQuery(name = "AgentSca.findByAgent", query = "SELECT a FROM AgentSca a WHERE a.agent = :agent")
    , @NamedQuery(name = "AgentSca.findByName", query = "SELECT a FROM AgentSca a WHERE a.name = :name")
    , @NamedQuery(name = "AgentSca.findBySeverity", query = "SELECT a FROM AgentSca a WHERE a.severity = :severity")
    , @NamedQuery(name = "AgentSca.findByInvalid", query = "SELECT a FROM AgentSca a WHERE a.invalid = :invalid")
    , @NamedQuery(name = "AgentSca.findByFail", query = "SELECT a FROM AgentSca a WHERE a.fail = :fail")
    , @NamedQuery(name = "AgentSca.findByTotalChecks", query = "SELECT a FROM AgentSca a WHERE a.totalChecks = :totalChecks")
    , @NamedQuery(name = "AgentSca.findByPass", query = "SELECT a FROM AgentSca a WHERE a.pass = :pass")
    , @NamedQuery(name = "AgentSca.findByScore", query = "SELECT a FROM AgentSca a WHERE a.score = :score")
    , @NamedQuery(name = "AgentSca.findByDescription", query = "SELECT a FROM AgentSca a WHERE a.description = :description")
    , @NamedQuery(name = "AgentSca.findByRefUrl", query = "SELECT a FROM AgentSca a WHERE a.refUrl = :refUrl")
    , @NamedQuery(name = "AgentSca.findByPolicyId", query = "SELECT a FROM AgentSca a WHERE a.policyId = :policyId")
    , @NamedQuery(name = "AgentSca.findByStartScan", query = "SELECT a FROM AgentSca a WHERE a.startScan = :startScan")
    , @NamedQuery(name = "AgentSca.findByEndScan", query = "SELECT a FROM AgentSca a WHERE a.endScan = :endScan")
    , @NamedQuery(name = "AgentSca.findByReportAdded", query = "SELECT a FROM AgentSca a WHERE a.reportAdded = :reportAdded")
    , @NamedQuery(name = "AgentSca.findByReportUpdated", query = "SELECT a FROM AgentSca a WHERE a.reportUpdated = :reportUpdated")})
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
    @Column(name = "node_id")
    private String nodeId;
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
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @NotNull
    @Column(name = "severity")
    private int severity;
    @Basic(optional = false)
    @NotNull
    @Column(name = "invalid")
    private int invalid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fail")
    private int fail;
    @Basic(optional = false)
    @NotNull
    @Column(name = "total_checks")
    private int totalChecks;
    @Basic(optional = false)
    @NotNull
    @Column(name = "pass")
    private int pass;
    @Basic(optional = false)
    @NotNull
    @Column(name = "score")
    private int score;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2056)
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "ref_url")
    private String refUrl;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "policy_id")
    private String policyId;
    @Column(name = "start_scan")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startScan;
    @Column(name = "end_scan")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endScan;
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

    public AgentSca(Long recId, String nodeId, String refId, String agent, String name, int severity, int invalid, int fail, int totalChecks, int pass, int score, String description, String refUrl, String policyId) {
        this.recId = recId;
        this.nodeId = nodeId;
        this.refId = refId;
        this.agent = agent;
        this.name = name;
        this.severity = severity;
        this.invalid = invalid;
        this.fail = fail;
        this.totalChecks = totalChecks;
        this.pass = pass;
        this.score = score;
        this.description = description;
        this.refUrl = refUrl;
        this.policyId = policyId;
    }

    public Long getRecId() {
        return recId;
    }

    public void setRecId(Long recId) {
        this.recId = recId;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSeverity() {
        return severity;
    }

    public void setSeverity(int severity) {
        this.severity = severity;
    }
    
    public String getColorSeverity() {
        if (severity == 3) return "font-size: 200%; color: red; font-weight: 600; ";
        else {
            if (severity == 2) return "font-size: 200%; color: orange; font-weight: 600; ";
            else {
                if (severity == 1) return "font-size: 200%; color: green; font-weight: 600; ";
            }
        }
        return "font-size: 200%; color: gray; font-weight: 600; ";
     
    }
    
    public String getDigitSeverity() {
        if (severity == 1) return "\u2460";
        else {
            if (severity == 2) return "\u2461";
            else {
                if (severity== 3) return "\u2462";
            }
        }
        return "\u24ea";
     
    }

    public int getInvalid() {
        return invalid;
    }

    public void setInvalid(int invalid) {
        this.invalid = invalid;
    }

    public int getFail() {
        return fail;
    }

    public void setFail(int fail) {
        this.fail = fail;
    }

    public int getTotalChecks() {
        return totalChecks;
    }

    public void setTotalChecks(int totalChecks) {
        this.totalChecks = totalChecks;
    }

    public int getPass() {
        return pass;
    }

    public void setPass(int pass) {
        this.pass = pass;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRefUrl() {
        return refUrl;
    }

    public void setRefUrl(String refUrl) {
        this.refUrl = refUrl;
    }

    public String getPolicyId() {
        return policyId;
    }

    public void setPolicyId(String policyId) {
        this.policyId = policyId;
    }

    public Date getStartScan() {
        return startScan;
    }

    public void setStartScan(Date startScan) {
        this.startScan = startScan;
    }

    public Date getEndScan() {
        return endScan;
    }

    public void setEndScan(Date endScan) {
        this.endScan = endScan;
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
        return "org.alertflex.mc.db.AgentSca[ recId=" + recId + " ]";
    }
    
}
