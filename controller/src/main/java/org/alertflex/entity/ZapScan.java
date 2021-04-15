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
@Table(name = "zap_scan")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ZapScan.findAll", query = "SELECT z FROM ZapScan z"),
    @NamedQuery(name = "ZapScan.findByRecId", query = "SELECT z FROM ZapScan z WHERE z.recId = :recId"),
    @NamedQuery(name = "ZapScan.findByRefId", query = "SELECT z FROM ZapScan z WHERE z.refId = :refId"),
    @NamedQuery(name = "ZapScan.findByNodeId", query = "SELECT z FROM ZapScan z WHERE z.nodeId = :nodeId"),
    @NamedQuery(name = "ZapScan.findByProbe", query = "SELECT z FROM ZapScan z WHERE z.probe = :probe"),
    @NamedQuery(name = "ZapScan.findByTarget", query = "SELECT z FROM ZapScan z WHERE z.target = :target"),
    @NamedQuery(name = "ZapScan.findByAlertRef", query = "SELECT z FROM ZapScan z WHERE z.alertRef = :alertRef"),
    @NamedQuery(name = "ZapScan.findByAlertName", query = "SELECT z FROM ZapScan z WHERE z.alertName = :alertName"),
    @NamedQuery(name = "ZapScan.findByRiskcode", query = "SELECT z FROM ZapScan z WHERE z.riskcode = :riskcode"),
    @NamedQuery(name = "ZapScan.findByConfidence", query = "SELECT z FROM ZapScan z WHERE z.confidence = :confidence"),
    @NamedQuery(name = "ZapScan.findByRiskdesc", query = "SELECT z FROM ZapScan z WHERE z.riskdesc = :riskdesc"),
    @NamedQuery(name = "ZapScan.findByCweid", query = "SELECT z FROM ZapScan z WHERE z.cweid = :cweid"),
    @NamedQuery(name = "ZapScan.findByWascid", query = "SELECT z FROM ZapScan z WHERE z.wascid = :wascid"),
    @NamedQuery(name = "ZapScan.findByUriMethods", query = "SELECT z FROM ZapScan z WHERE z.uriMethods = :uriMethods"),
    @NamedQuery(name = "ZapScan.findByDescription", query = "SELECT z FROM ZapScan z WHERE z.description = :description"),
    @NamedQuery(name = "ZapScan.findBySolution", query = "SELECT z FROM ZapScan z WHERE z.solution = :solution"),
    @NamedQuery(name = "ZapScan.findByOtherinfo", query = "SELECT z FROM ZapScan z WHERE z.otherinfo = :otherinfo"),
    @NamedQuery(name = "ZapScan.findByReference", query = "SELECT z FROM ZapScan z WHERE z.reference = :reference"),
    @NamedQuery(name = "ZapScan.findByReportAdded", query = "SELECT z FROM ZapScan z WHERE z.reportAdded = :reportAdded"),
    @NamedQuery(name = "ZapScan.findByReportUpdated", query = "SELECT z FROM ZapScan z WHERE z.reportUpdated = :reportUpdated")})
public class ZapScan implements Serializable {

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
    @Column(name = "target")
    private String target;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "alert_ref")
    private String alertRef;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1024)
    @Column(name = "alert_name")
    private String alertName;
    @Basic(optional = false)
    @NotNull
    @Column(name = "riskcode")
    private int riskcode;
    @Basic(optional = false)
    @NotNull
    @Column(name = "confidence")
    private int confidence;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "riskdesc")
    private String riskdesc;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cweid")
    private int cweid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "wascid")
    private int wascid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2048)
    @Column(name = "uri_methods")
    private String uriMethods;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2048)
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2048)
    @Column(name = "solution")
    private String solution;
    @Size(max = 2048)
    @Column(name = "otherinfo")
    private String otherinfo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2048)
    @Column(name = "reference")
    private String reference;
    @Column(name = "report_added")
    @Temporal(TemporalType.TIMESTAMP)
    private Date reportAdded;
    @Column(name = "report_updated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date reportUpdated;

    public ZapScan() {
    }

    public ZapScan(Long recId) {
        this.recId = recId;
    }

    public ZapScan(Long recId, String refId, String nodeId, String probe, String target, String alertRef, String alertName, int riskcode, int confidence, String riskdesc, int cweid, int wascid, String uriMethods, String description, String solution, String reference) {
        this.recId = recId;
        this.refId = refId;
        this.nodeId = nodeId;
        this.probe = probe;
        this.target = target;
        this.alertRef = alertRef;
        this.alertName = alertName;
        this.riskcode = riskcode;
        this.confidence = confidence;
        this.riskdesc = riskdesc;
        this.cweid = cweid;
        this.wascid = wascid;
        this.uriMethods = uriMethods;
        this.description = description;
        this.solution = solution;
        this.reference = reference;
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

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getAlertRef() {
        return alertRef;
    }

    public void setAlertRef(String alertRef) {
        this.alertRef = alertRef;
    }

    public String getAlertName() {
        return alertName;
    }

    public void setAlertName(String alertName) {
        this.alertName = alertName;
    }

    public int getRiskcode() {
        return riskcode;
    }

    public void setRiskcode(int riskcode) {
        this.riskcode = riskcode;
    }

    public int getConfidence() {
        return confidence;
    }

    public void setConfidence(int confidence) {
        this.confidence = confidence;
    }

    public String getRiskdesc() {
        return riskdesc;
    }

    public void setRiskdesc(String riskdesc) {
        this.riskdesc = riskdesc;
    }

    public int getCweid() {
        return cweid;
    }

    public void setCweid(int cweid) {
        this.cweid = cweid;
    }

    public int getWascid() {
        return wascid;
    }

    public void setWascid(int wascid) {
        this.wascid = wascid;
    }

    public String getUriMethods() {
        return uriMethods;
    }

    public void setUriMethods(String uriMethods) {
        this.uriMethods = uriMethods;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public String getOtherinfo() {
        return otherinfo;
    }

    public void setOtherinfo(String otherinfo) {
        this.otherinfo = otherinfo;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
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
        if (!(object instanceof ZapScan)) {
            return false;
        }
        ZapScan other = (ZapScan) object;
        if ((this.recId == null && other.recId != null) || (this.recId != null && !this.recId.equals(other.recId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.alertflex.entity.ZapScan[ recId=" + recId + " ]";
    }
    
}
