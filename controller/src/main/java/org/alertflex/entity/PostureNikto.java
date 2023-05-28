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
@Table(name = "posture_nikto")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PostureNikto.findAll", query = "SELECT p FROM PostureNikto p"),
    @NamedQuery(name = "PostureNikto.findByRecId", query = "SELECT p FROM PostureNikto p WHERE p.recId = :recId"),
    @NamedQuery(name = "PostureNikto.findByRefId", query = "SELECT p FROM PostureNikto p WHERE p.refId = :refId"),
    @NamedQuery(name = "PostureNikto.findByScanUuid", query = "SELECT p FROM PostureNikto p WHERE p.scanUuid = :scanUuid"),
    @NamedQuery(name = "PostureNikto.findByAlertUuid", query = "SELECT p FROM PostureNikto p WHERE p.alertUuid = :alertUuid"),
    @NamedQuery(name = "PostureNikto.findByNode", query = "SELECT p FROM PostureNikto p WHERE p.node = :node"),
    @NamedQuery(name = "PostureNikto.findByProbe", query = "SELECT p FROM PostureNikto p WHERE p.probe = :probe"),
    @NamedQuery(name = "PostureNikto.findByTarget", query = "SELECT p FROM PostureNikto p WHERE p.target = :target"),
    @NamedQuery(name = "PostureNikto.findByIp", query = "SELECT p FROM PostureNikto p WHERE p.ip = :ip"),
    @NamedQuery(name = "PostureNikto.findByPort", query = "SELECT p FROM PostureNikto p WHERE p.port = :port"),
    @NamedQuery(name = "PostureNikto.findByBanner", query = "SELECT p FROM PostureNikto p WHERE p.banner = :banner"),
    @NamedQuery(name = "PostureNikto.findByVulnId", query = "SELECT p FROM PostureNikto p WHERE p.vulnId = :vulnId"),
    @NamedQuery(name = "PostureNikto.findByVulnOsvdb", query = "SELECT p FROM PostureNikto p WHERE p.vulnOsvdb = :vulnOsvdb"),
    @NamedQuery(name = "PostureNikto.findByVulnMethod", query = "SELECT p FROM PostureNikto p WHERE p.vulnMethod = :vulnMethod"),
    @NamedQuery(name = "PostureNikto.findByVulnUrl", query = "SELECT p FROM PostureNikto p WHERE p.vulnUrl = :vulnUrl"),
    @NamedQuery(name = "PostureNikto.findByVulnMsg", query = "SELECT p FROM PostureNikto p WHERE p.vulnMsg = :vulnMsg"),
    @NamedQuery(name = "PostureNikto.findBySeverity", query = "SELECT p FROM PostureNikto p WHERE p.severity = :severity"),
    @NamedQuery(name = "PostureNikto.findByStatus", query = "SELECT p FROM PostureNikto p WHERE p.status = :status"),
    @NamedQuery(name = "PostureNikto.findByReportAdded", query = "SELECT p FROM PostureNikto p WHERE p.reportAdded = :reportAdded"),
    @NamedQuery(name = "PostureNikto.findByReportUpdated", query = "SELECT p FROM PostureNikto p WHERE p.reportUpdated = :reportUpdated")})
public class PostureNikto implements Serializable {

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
    @Size(min = 1, max = 512)
    @Column(name = "target")
    private String target;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "ip")
    private String ip;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "port")
    private String port;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "banner")
    private String banner;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "vuln_id")
    private String vulnId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "vuln_osvdb")
    private String vulnOsvdb;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "vuln_method")
    private String vulnMethod;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1024)
    @Column(name = "vuln_url")
    private String vulnUrl;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2048)
    @Column(name = "vuln_msg")
    private String vulnMsg;
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

    public PostureNikto() {
    }

    public PostureNikto(Long recId) {
        this.recId = recId;
    }

    public PostureNikto(Long recId, String refId, String scanUuid, String alertUuid, String node, String probe, String target, String ip, String port, String banner, String vulnId, String vulnOsvdb, String vulnMethod, String vulnUrl, String vulnMsg, String severity, String status) {
        this.recId = recId;
        this.refId = refId;
        this.scanUuid = scanUuid;
        this.alertUuid = alertUuid;
        this.node = node;
        this.probe = probe;
        this.target = target;
        this.ip = ip;
        this.port = port;
        this.banner = banner;
        this.vulnId = vulnId;
        this.vulnOsvdb = vulnOsvdb;
        this.vulnMethod = vulnMethod;
        this.vulnUrl = vulnUrl;
        this.vulnMsg = vulnMsg;
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

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getVulnId() {
        return vulnId;
    }

    public void setVulnId(String vulnId) {
        this.vulnId = vulnId;
    }

    public String getVulnOsvdb() {
        return vulnOsvdb;
    }

    public void setVulnOsvdb(String vulnOsvdb) {
        this.vulnOsvdb = vulnOsvdb;
    }

    public String getVulnMethod() {
        return vulnMethod;
    }

    public void setVulnMethod(String vulnMethod) {
        this.vulnMethod = vulnMethod;
    }

    public String getVulnUrl() {
        return vulnUrl;
    }

    public void setVulnUrl(String vulnUrl) {
        this.vulnUrl = vulnUrl;
    }

    public String getVulnMsg() {
        return vulnMsg;
    }

    public void setVulnMsg(String vulnMsg) {
        this.vulnMsg = vulnMsg;
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
        if (!(object instanceof PostureNikto)) {
            return false;
        }
        PostureNikto other = (PostureNikto) object;
        if ((this.recId == null && other.recId != null) || (this.recId != null && !this.recId.equals(other.recId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.alertflex.entity.PostureNikto[ recId=" + recId + " ]";
    }
    
}
