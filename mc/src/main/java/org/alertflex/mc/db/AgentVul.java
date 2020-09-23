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
@Table(name = "agent_vul")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AgentVul.findAll", query = "SELECT a FROM AgentVul a")
    , @NamedQuery(name = "AgentVul.findByRecId", query = "SELECT a FROM AgentVul a WHERE a.recId = :recId")
    , @NamedQuery(name = "AgentVul.findByNodeId", query = "SELECT a FROM AgentVul a WHERE a.nodeId = :nodeId")
    , @NamedQuery(name = "AgentVul.findByRefId", query = "SELECT a FROM AgentVul a WHERE a.refId = :refId")
    , @NamedQuery(name = "AgentVul.findByAgent", query = "SELECT a FROM AgentVul a WHERE a.agent = :agent")
    , @NamedQuery(name = "AgentVul.findByEvent", query = "SELECT a FROM AgentVul a WHERE a.event = :event")
    , @NamedQuery(name = "AgentVul.findBySeverity", query = "SELECT a FROM AgentVul a WHERE a.severity = :severity")
    , @NamedQuery(name = "AgentVul.findByDescription", query = "SELECT a FROM AgentVul a WHERE a.description = :description")
    , @NamedQuery(name = "AgentVul.findByCve", query = "SELECT a FROM AgentVul a WHERE a.cve = :cve")
    , @NamedQuery(name = "AgentVul.findByCveState", query = "SELECT a FROM AgentVul a WHERE a.cveState = :cveState")
    , @NamedQuery(name = "AgentVul.findByCveSeverity", query = "SELECT a FROM AgentVul a WHERE a.cveSeverity = :cveSeverity")
    , @NamedQuery(name = "AgentVul.findByReference", query = "SELECT a FROM AgentVul a WHERE a.reference = :reference")
    , @NamedQuery(name = "AgentVul.findByPackageName", query = "SELECT a FROM AgentVul a WHERE a.packageName = :packageName")
    , @NamedQuery(name = "AgentVul.findByPackageVersion", query = "SELECT a FROM AgentVul a WHERE a.packageVersion = :packageVersion")
    , @NamedQuery(name = "AgentVul.findByPackageCondition", query = "SELECT a FROM AgentVul a WHERE a.packageCondition = :packageCondition")
    , @NamedQuery(name = "AgentVul.findByCvePublished", query = "SELECT a FROM AgentVul a WHERE a.cvePublished = :cvePublished")
    , @NamedQuery(name = "AgentVul.findByCveUpdated", query = "SELECT a FROM AgentVul a WHERE a.cveUpdated = :cveUpdated")
    , @NamedQuery(name = "AgentVul.findByReportAdded", query = "SELECT a FROM AgentVul a WHERE a.reportAdded = :reportAdded")
    , @NamedQuery(name = "AgentVul.findByReportUpdated", query = "SELECT a FROM AgentVul a WHERE a.reportUpdated = :reportUpdated")})
public class AgentVul implements Serializable {

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
    @Size(min = 1, max = 1024)
    @Column(name = "cve")
    private String cve;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "cve_state")
    private String cveState;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "cve_severity")
    private String cveSeverity;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1024)
    @Column(name = "reference")
    private String reference;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "package_name")
    private String packageName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "package_version")
    private String packageVersion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "package_condition")
    private String packageCondition;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "cve_published")
    private String cvePublished;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "cve_updated")
    private String cveUpdated;
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

    public AgentVul(Long recId, String nodeId, String refId, String agent, int event, int severity, String description, String cve, String cveState, String cveSeverity, String reference, String packageName, String packageVersion, String packageCondition, String cvePublished, String cveUpdated) {
        this.recId = recId;
        this.nodeId = nodeId;
        this.refId = refId;
        this.agent = agent;
        this.event = event;
        this.severity = severity;
        this.description = description;
        this.cve = cve;
        this.cveState = cveState;
        this.cveSeverity = cveSeverity;
        this.reference = reference;
        this.packageName = packageName;
        this.packageVersion = packageVersion;
        this.packageCondition = packageCondition;
        this.cvePublished = cvePublished;
        this.cveUpdated = cveUpdated;
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

    public String getCve() {
        return cve;
    }

    public void setCve(String cve) {
        this.cve = cve;
    }

    public String getCveState() {
        return cveState;
    }

    public void setCveState(String cveState) {
        this.cveState = cveState;
    }

    public String getCveSeverity() {
        return cveSeverity;
    }

    public void setCveSeverity(String cveSeverity) {
        this.cveSeverity = cveSeverity;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getPackageVersion() {
        return packageVersion;
    }

    public void setPackageVersion(String packageVersion) {
        this.packageVersion = packageVersion;
    }

    public String getPackageCondition() {
        return packageCondition;
    }

    public void setPackageCondition(String packageCondition) {
        this.packageCondition = packageCondition;
    }

    public String getCvePublished() {
        return cvePublished;
    }

    public void setCvePublished(String cvePublished) {
        this.cvePublished = cvePublished;
    }

    public String getCveUpdated() {
        return cveUpdated;
    }

    public void setCveUpdated(String cveUpdated) {
        this.cveUpdated = cveUpdated;
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
        return "org.alertflex.mc.db.AgentVul[ recId=" + recId + " ]";
    }
    
}
