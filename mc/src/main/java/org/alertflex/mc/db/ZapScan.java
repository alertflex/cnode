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
import javax.persistence.Lob;
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
    @NamedQuery(name = "ZapScan.findAll", query = "SELECT z FROM ZapScan z")
    , @NamedQuery(name = "ZapScan.findByRecId", query = "SELECT z FROM ZapScan z WHERE z.recId = :recId")
    , @NamedQuery(name = "ZapScan.findByRefId", query = "SELECT z FROM ZapScan z WHERE z.refId = :refId")
    , @NamedQuery(name = "ZapScan.findByUrl", query = "SELECT z FROM ZapScan z WHERE z.url = :url")
    , @NamedQuery(name = "ZapScan.findByPluginid", query = "SELECT z FROM ZapScan z WHERE z.pluginid = :pluginid")
    , @NamedQuery(name = "ZapScan.findByAlert", query = "SELECT z FROM ZapScan z WHERE z.alert = :alert")
    , @NamedQuery(name = "ZapScan.findByName", query = "SELECT z FROM ZapScan z WHERE z.name = :name")
    , @NamedQuery(name = "ZapScan.findByRiskcode", query = "SELECT z FROM ZapScan z WHERE z.riskcode = :riskcode")
    , @NamedQuery(name = "ZapScan.findByConfidence", query = "SELECT z FROM ZapScan z WHERE z.confidence = :confidence")
    , @NamedQuery(name = "ZapScan.findByRiskdesc", query = "SELECT z FROM ZapScan z WHERE z.riskdesc = :riskdesc")
    , @NamedQuery(name = "ZapScan.findByDescription", query = "SELECT z FROM ZapScan z WHERE z.description = :description")
    , @NamedQuery(name = "ZapScan.findByCounter", query = "SELECT z FROM ZapScan z WHERE z.counter = :counter")
    , @NamedQuery(name = "ZapScan.findBySolution", query = "SELECT z FROM ZapScan z WHERE z.solution = :solution")
    , @NamedQuery(name = "ZapScan.findByOtherinfo", query = "SELECT z FROM ZapScan z WHERE z.otherinfo = :otherinfo")
    , @NamedQuery(name = "ZapScan.findByReference", query = "SELECT z FROM ZapScan z WHERE z.reference = :reference")
    , @NamedQuery(name = "ZapScan.findByCweid", query = "SELECT z FROM ZapScan z WHERE z.cweid = :cweid")
    , @NamedQuery(name = "ZapScan.findByWascid", query = "SELECT z FROM ZapScan z WHERE z.wascid = :wascid")
    , @NamedQuery(name = "ZapScan.findBySourceid", query = "SELECT z FROM ZapScan z WHERE z.sourceid = :sourceid")
    , @NamedQuery(name = "ZapScan.findByReportAdded", query = "SELECT z FROM ZapScan z WHERE z.reportAdded = :reportAdded")
    , @NamedQuery(name = "ZapScan.findByReportUpdated", query = "SELECT z FROM ZapScan z WHERE z.reportUpdated = :reportUpdated")})
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
    @Column(name = "url")
    private String url;
    @Basic(optional = false)
    @NotNull
    @Column(name = "pluginid")
    private int pluginid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "alert")
    private String alert;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "name")
    private String name;
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
    @Size(min = 1, max = 1024)
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @NotNull
    @Column(name = "counter")
    private int counter;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1024)
    @Column(name = "solution")
    private String solution;
    @Size(max = 1024)
    @Column(name = "otherinfo")
    private String otherinfo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1024)
    @Column(name = "reference")
    private String reference;
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
    @Column(name = "sourceid")
    private int sourceid;
    @Column(name = "report_added")
    @Temporal(TemporalType.TIMESTAMP)
    private Date reportAdded;
    @Column(name = "report_updated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date reportUpdated;
    @Lob
    @Size(max = 65535)
    @Column(name = "instances")
    private String instances;

    public ZapScan() {
    }

    public ZapScan(Long recId) {
        this.recId = recId;
    }

    public ZapScan(Long recId, String refId, String url, int pluginid, String alert, String name, int riskcode, int confidence, String riskdesc, String description, int counter, String solution, String reference, int cweid, int wascid, int sourceid) {
        this.recId = recId;
        this.refId = refId;
        this.url = url;
        this.pluginid = pluginid;
        this.alert = alert;
        this.name = name;
        this.riskcode = riskcode;
        this.confidence = confidence;
        this.riskdesc = riskdesc;
        this.description = description;
        this.counter = counter;
        this.solution = solution;
        this.reference = reference;
        this.cweid = cweid;
        this.wascid = wascid;
        this.sourceid = sourceid;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getPluginid() {
        return pluginid;
    }

    public void setPluginid(int pluginid) {
        this.pluginid = pluginid;
    }

    public String getAlert() {
        return alert;
    }

    public void setAlert(String alert) {
        this.alert = alert;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
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

    public int getSourceid() {
        return sourceid;
    }

    public void setSourceid(int sourceid) {
        this.sourceid = sourceid;
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

    public String getInstances() {
        return instances;
    }

    public void setInstances(String instances) {
        this.instances = instances;
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
        return "org.alertflex.mc.db.ZapScan[ recId=" + recId + " ]";
    }
    
}
