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
@Table(name = "nessus_scan")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NessusScan.findAll", query = "SELECT n FROM NessusScan n")
    , @NamedQuery(name = "NessusScan.findByRecId", query = "SELECT n FROM NessusScan n WHERE n.recId = :recId")
    , @NamedQuery(name = "NessusScan.findByRefId", query = "SELECT n FROM NessusScan n WHERE n.refId = :refId")
    , @NamedQuery(name = "NessusScan.findByScanName", query = "SELECT n FROM NessusScan n WHERE n.scanName = :scanName")
    , @NamedQuery(name = "NessusScan.findByCount", query = "SELECT n FROM NessusScan n WHERE n.count = :count")
    , @NamedQuery(name = "NessusScan.findByVulnIndex", query = "SELECT n FROM NessusScan n WHERE n.vulnIndex = :vulnIndex")
    , @NamedQuery(name = "NessusScan.findBySeverity", query = "SELECT n FROM NessusScan n WHERE n.severity = :severity")
    , @NamedQuery(name = "NessusScan.findBySeverityIndex", query = "SELECT n FROM NessusScan n WHERE n.severityIndex = :severityIndex")
    , @NamedQuery(name = "NessusScan.findByPluginId", query = "SELECT n FROM NessusScan n WHERE n.pluginId = :pluginId")
    , @NamedQuery(name = "NessusScan.findByPluginName", query = "SELECT n FROM NessusScan n WHERE n.pluginName = :pluginName")
    , @NamedQuery(name = "NessusScan.findByPluginFamily", query = "SELECT n FROM NessusScan n WHERE n.pluginFamily = :pluginFamily")
    , @NamedQuery(name = "NessusScan.findByReportAdded", query = "SELECT n FROM NessusScan n WHERE n.reportAdded = :reportAdded")
    , @NamedQuery(name = "NessusScan.findByReportUpdated", query = "SELECT n FROM NessusScan n WHERE n.reportUpdated = :reportUpdated")})
public class NessusScan implements Serializable {

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
    @Size(min = 1, max = 512)
    @Column(name = "scan_name")
    private String scanName;
    @Basic(optional = false)
    @NotNull
    @Column(name = "count")
    private int count;
    @Basic(optional = false)
    @NotNull
    @Column(name = "vuln_index")
    private int vulnIndex;
    @Basic(optional = false)
    @NotNull
    @Column(name = "severity")
    private int severity;
    @Basic(optional = false)
    @NotNull
    @Column(name = "severity_index")
    private int severityIndex;
    @Basic(optional = false)
    @NotNull
    @Column(name = "plugin_id")
    private int pluginId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1024)
    @Column(name = "plugin_name")
    private String pluginName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "plugin_family")
    private String pluginFamily;
    @Column(name = "report_added")
    @Temporal(TemporalType.TIMESTAMP)
    private Date reportAdded;
    @Column(name = "report_updated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date reportUpdated;

    public NessusScan() {
    }

    public NessusScan(Long recId) {
        this.recId = recId;
    }

    public NessusScan(Long recId, String refId, String scanName, int count, int vulnIndex, int severity, int severityIndex, int pluginId, String pluginName, String pluginFamily) {
        this.recId = recId;
        this.refId = refId;
        this.scanName = scanName;
        this.count = count;
        this.vulnIndex = vulnIndex;
        this.severity = severity;
        this.severityIndex = severityIndex;
        this.pluginId = pluginId;
        this.pluginName = pluginName;
        this.pluginFamily = pluginFamily;
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

    public String getScanName() {
        return scanName;
    }

    public void setScanName(String scanName) {
        this.scanName = scanName;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getVulnIndex() {
        return vulnIndex;
    }

    public void setVulnIndex(int vulnIndex) {
        this.vulnIndex = vulnIndex;
    }

    public int getSeverity() {
        return severity;
    }

    public void setSeverity(int severity) {
        this.severity = severity;
    }

    public int getSeverityIndex() {
        return severityIndex;
    }

    public void setSeverityIndex(int severityIndex) {
        this.severityIndex = severityIndex;
    }

    public int getPluginId() {
        return pluginId;
    }
    
    public String getPluginIdUrl() {
        return "https://www.tenable.com/plugins/nessus/" + Integer.toString(pluginId);
    }

    public void setPluginId(int pluginId) {
        this.pluginId = pluginId;
    }

    public String getPluginName() {
        return pluginName;
    }

    public void setPluginName(String pluginName) {
        this.pluginName = pluginName;
    }

    public String getPluginFamily() {
        return pluginFamily;
    }

    public void setPluginFamily(String pluginFamily) {
        this.pluginFamily = pluginFamily;
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
        if (!(object instanceof NessusScan)) {
            return false;
        }
        NessusScan other = (NessusScan) object;
        if ((this.recId == null && other.recId != null) || (this.recId != null && !this.recId.equals(other.recId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.alertflex.mc.db.NessusScan[ recId=" + recId + " ]";
    }
    
}
