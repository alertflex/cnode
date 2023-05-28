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
@Table(name = "posture_cloudsploit")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PostureCloudsploit.findAll", query = "SELECT p FROM PostureCloudsploit p"),
    @NamedQuery(name = "PostureCloudsploit.findByRecId", query = "SELECT p FROM PostureCloudsploit p WHERE p.recId = :recId"),
    @NamedQuery(name = "PostureCloudsploit.findByRefId", query = "SELECT p FROM PostureCloudsploit p WHERE p.refId = :refId"),
    @NamedQuery(name = "PostureCloudsploit.findByScanUuid", query = "SELECT p FROM PostureCloudsploit p WHERE p.scanUuid = :scanUuid"),
    @NamedQuery(name = "PostureCloudsploit.findByAlertUuid", query = "SELECT p FROM PostureCloudsploit p WHERE p.alertUuid = :alertUuid"),
    @NamedQuery(name = "PostureCloudsploit.findByNode", query = "SELECT p FROM PostureCloudsploit p WHERE p.node = :node"),
    @NamedQuery(name = "PostureCloudsploit.findByProbe", query = "SELECT p FROM PostureCloudsploit p WHERE p.probe = :probe"),
    @NamedQuery(name = "PostureCloudsploit.findByCloudType", query = "SELECT p FROM PostureCloudsploit p WHERE p.cloudType = :cloudType"),
    @NamedQuery(name = "PostureCloudsploit.findByPlugin", query = "SELECT p FROM PostureCloudsploit p WHERE p.plugin = :plugin"),
    @NamedQuery(name = "PostureCloudsploit.findByCategory", query = "SELECT p FROM PostureCloudsploit p WHERE p.category = :category"),
    @NamedQuery(name = "PostureCloudsploit.findByTitle", query = "SELECT p FROM PostureCloudsploit p WHERE p.title = :title"),
    @NamedQuery(name = "PostureCloudsploit.findByDescription", query = "SELECT p FROM PostureCloudsploit p WHERE p.description = :description"),
    @NamedQuery(name = "PostureCloudsploit.findByResources", query = "SELECT p FROM PostureCloudsploit p WHERE p.resources = :resources"),
    @NamedQuery(name = "PostureCloudsploit.findByRegion", query = "SELECT p FROM PostureCloudsploit p WHERE p.region = :region"),
    @NamedQuery(name = "PostureCloudsploit.findByMessage", query = "SELECT p FROM PostureCloudsploit p WHERE p.message = :message"),
    @NamedQuery(name = "PostureCloudsploit.findBySeverity", query = "SELECT p FROM PostureCloudsploit p WHERE p.severity = :severity"),
    @NamedQuery(name = "PostureCloudsploit.findByStatus", query = "SELECT p FROM PostureCloudsploit p WHERE p.status = :status"),
    @NamedQuery(name = "PostureCloudsploit.findByReportAdded", query = "SELECT p FROM PostureCloudsploit p WHERE p.reportAdded = :reportAdded"),
    @NamedQuery(name = "PostureCloudsploit.findByReportUpdated", query = "SELECT p FROM PostureCloudsploit p WHERE p.reportUpdated = :reportUpdated")})
public class PostureCloudsploit implements Serializable {

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
    @Column(name = "scan_uuid")
    private String scanUuid;
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
    @Column(name = "cloud_type")
    private String cloudType;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "plugin")
    private String plugin;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "category")
    private String category;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "title")
    private String title;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1024)
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "resources")
    private String resources;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "region")
    private String region;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1024)
    @Column(name = "message")
    private String message;
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

    public PostureCloudsploit() {
    }

    public PostureCloudsploit(Long recId) {
        this.recId = recId;
    }

    public PostureCloudsploit(Long recId, String refId, String scanUuid, String alertUuid, String node, String probe, String cloudType, String plugin, String category, String title, String description, String resources, String region, String message, String severity, String status) {
        this.recId = recId;
        this.refId = refId;
        this.scanUuid = scanUuid;
        this.alertUuid = alertUuid;
        this.node = node;
        this.probe = probe;
        this.cloudType = cloudType;
        this.plugin = plugin;
        this.category = category;
        this.title = title;
        this.description = description;
        this.resources = resources;
        this.region = region;
        this.message = message;
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

    public String getScanUuid() {
        return scanUuid;
    }

    public void setScanUuid(String scanUuid) {
        this.scanUuid = scanUuid;
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

    public String getCloudType() {
        return cloudType;
    }

    public void setCloudType(String cloudType) {
        this.cloudType = cloudType;
    }

    public String getPlugin() {
        return plugin;
    }

    public void setPlugin(String plugin) {
        this.plugin = plugin;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public String getResources() {
        return resources;
    }

    public void setResources(String resources) {
        this.resources = resources;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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
        if (!(object instanceof PostureCloudsploit)) {
            return false;
        }
        PostureCloudsploit other = (PostureCloudsploit) object;
        if ((this.recId == null && other.recId != null) || (this.recId != null && !this.recId.equals(other.recId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.alertflex.entity.PostureCloudsploit[ recId=" + recId + " ]";
    }
    
}
