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
@Table(name = "agent_openscap")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AgentOpenscap.findAll", query = "SELECT a FROM AgentOpenscap a")
    , @NamedQuery(name = "AgentOpenscap.findByRecId", query = "SELECT a FROM AgentOpenscap a WHERE a.recId = :recId")
    , @NamedQuery(name = "AgentOpenscap.findByNodeId", query = "SELECT a FROM AgentOpenscap a WHERE a.nodeId = :nodeId")
    , @NamedQuery(name = "AgentOpenscap.findByRefId", query = "SELECT a FROM AgentOpenscap a WHERE a.refId = :refId")
    , @NamedQuery(name = "AgentOpenscap.findByAgent", query = "SELECT a FROM AgentOpenscap a WHERE a.agent = :agent")
    , @NamedQuery(name = "AgentOpenscap.findByEvent", query = "SELECT a FROM AgentOpenscap a WHERE a.event = :event")
    , @NamedQuery(name = "AgentOpenscap.findBySeverity", query = "SELECT a FROM AgentOpenscap a WHERE a.severity = :severity")
    , @NamedQuery(name = "AgentOpenscap.findByDescription", query = "SELECT a FROM AgentOpenscap a WHERE a.description = :description")
    , @NamedQuery(name = "AgentOpenscap.findByBenchmark", query = "SELECT a FROM AgentOpenscap a WHERE a.benchmark = :benchmark")
    , @NamedQuery(name = "AgentOpenscap.findByProfileId", query = "SELECT a FROM AgentOpenscap a WHERE a.profileId = :profileId")
    , @NamedQuery(name = "AgentOpenscap.findByProfileTitle", query = "SELECT a FROM AgentOpenscap a WHERE a.profileTitle = :profileTitle")
    , @NamedQuery(name = "AgentOpenscap.findByCheckId", query = "SELECT a FROM AgentOpenscap a WHERE a.checkId = :checkId")
    , @NamedQuery(name = "AgentOpenscap.findByCheckTitle", query = "SELECT a FROM AgentOpenscap a WHERE a.checkTitle = :checkTitle")
    , @NamedQuery(name = "AgentOpenscap.findByCheckResult", query = "SELECT a FROM AgentOpenscap a WHERE a.checkResult = :checkResult")
    , @NamedQuery(name = "AgentOpenscap.findByCheckSeverity", query = "SELECT a FROM AgentOpenscap a WHERE a.checkSeverity = :checkSeverity")
    , @NamedQuery(name = "AgentOpenscap.findByCheckDescription", query = "SELECT a FROM AgentOpenscap a WHERE a.checkDescription = :checkDescription")
    , @NamedQuery(name = "AgentOpenscap.findByCheckRationale", query = "SELECT a FROM AgentOpenscap a WHERE a.checkRationale = :checkRationale")
    , @NamedQuery(name = "AgentOpenscap.findByCheckReferences", query = "SELECT a FROM AgentOpenscap a WHERE a.checkReferences = :checkReferences")
    , @NamedQuery(name = "AgentOpenscap.findByCheckIdentifiers", query = "SELECT a FROM AgentOpenscap a WHERE a.checkIdentifiers = :checkIdentifiers")
    , @NamedQuery(name = "AgentOpenscap.findByReportAdded", query = "SELECT a FROM AgentOpenscap a WHERE a.reportAdded = :reportAdded")
    , @NamedQuery(name = "AgentOpenscap.findByReportUpdated", query = "SELECT a FROM AgentOpenscap a WHERE a.reportUpdated = :reportUpdated")})
public class AgentOpenscap implements Serializable {

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
    @Column(name = "event")
    private int event;
    @Basic(optional = false)
    @NotNull
    @Column(name = "severity")
    private int severity;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1024)
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "benchmark")
    private String benchmark;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "profile_id")
    private String profileId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "profile_title")
    private String profileTitle;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "check_id")
    private String checkId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "check_title")
    private String checkTitle;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "check_result")
    private String checkResult;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "check_severity")
    private String checkSeverity;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1024)
    @Column(name = "check_description")
    private String checkDescription;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1024)
    @Column(name = "check_rationale")
    private String checkRationale;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1024)
    @Column(name = "check_references")
    private String checkReferences;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "check_identifiers")
    private String checkIdentifiers;
    @Column(name = "report_added")
    @Temporal(TemporalType.TIMESTAMP)
    private Date reportAdded;
    @Column(name = "report_updated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date reportUpdated;

    public AgentOpenscap() {
    }

    public AgentOpenscap(Long recId) {
        this.recId = recId;
    }

    public AgentOpenscap(Long recId, String nodeId, String refId, String agent, int event, int severity, String description, String benchmark, String profileId, String profileTitle, String checkId, String checkTitle, String checkResult, String checkSeverity, String checkDescription, String checkRationale, String checkReferences, String checkIdentifiers) {
        this.recId = recId;
        this.nodeId = nodeId;
        this.refId = refId;
        this.agent = agent;
        this.event = event;
        this.severity = severity;
        this.description = description;
        this.benchmark = benchmark;
        this.profileId = profileId;
        this.profileTitle = profileTitle;
        this.checkId = checkId;
        this.checkTitle = checkTitle;
        this.checkResult = checkResult;
        this.checkSeverity = checkSeverity;
        this.checkDescription = checkDescription;
        this.checkRationale = checkRationale;
        this.checkReferences = checkReferences;
        this.checkIdentifiers = checkIdentifiers;
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

    public int getEvent() {
        return event;
    }

    public void setEvent(int event) {
        this.event = event;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBenchmark() {
        return benchmark;
    }

    public void setBenchmark(String benchmark) {
        this.benchmark = benchmark;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public String getProfileTitle() {
        return profileTitle;
    }

    public void setProfileTitle(String profileTitle) {
        this.profileTitle = profileTitle;
    }

    public String getCheckId() {
        return checkId;
    }

    public void setCheckId(String checkId) {
        this.checkId = checkId;
    }

    public String getCheckTitle() {
        return checkTitle;
    }

    public void setCheckTitle(String checkTitle) {
        this.checkTitle = checkTitle;
    }

    public String getCheckResult() {
        return checkResult;
    }

    public void setCheckResult(String checkResult) {
        this.checkResult = checkResult;
    }

    public String getCheckSeverity() {
        return checkSeverity;
    }

    public void setCheckSeverity(String checkSeverity) {
        this.checkSeverity = checkSeverity;
    }

    public String getCheckDescription() {
        return checkDescription;
    }

    public void setCheckDescription(String checkDescription) {
        this.checkDescription = checkDescription;
    }

    public String getCheckRationale() {
        return checkRationale;
    }

    public void setCheckRationale(String checkRationale) {
        this.checkRationale = checkRationale;
    }

    public String getCheckReferences() {
        return checkReferences;
    }

    public void setCheckReferences(String checkReferences) {
        this.checkReferences = checkReferences;
    }

    public String getCheckIdentifiers() {
        return checkIdentifiers;
    }

    public void setCheckIdentifiers(String checkIdentifiers) {
        this.checkIdentifiers = checkIdentifiers;
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
        if (!(object instanceof AgentOpenscap)) {
            return false;
        }
        AgentOpenscap other = (AgentOpenscap) object;
        if ((this.recId == null && other.recId != null) || (this.recId != null && !this.recId.equals(other.recId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.alertflex.mc.db.AgentOpenscap[ recId=" + recId + " ]";
    }
    
}
