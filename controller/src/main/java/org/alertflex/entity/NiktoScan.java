/*
 *   Copyright 2021 Oleg Zharkov
 *
 *   Licensed under the Apache License, Version 2.0 (the "License").
 *   You may not use this file except in compliance with the License.
 *   A copy of the License is located at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   or in the "license" file accompanying this file. This file is distributed
 *   on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 *   express or implied. See the License for the specific language governing
 *   permissions and limitations under the License.
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

@Entity
@Table(name = "nikto_scan")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NiktoScan.findAll", query = "SELECT n FROM NiktoScan n"),
    @NamedQuery(name = "NiktoScan.findByRecId", query = "SELECT n FROM NiktoScan n WHERE n.recId = :recId"),
    @NamedQuery(name = "NiktoScan.findByRefId", query = "SELECT n FROM NiktoScan n WHERE n.refId = :refId"),
    @NamedQuery(name = "NiktoScan.findByNodeId", query = "SELECT n FROM NiktoScan n WHERE n.nodeId = :nodeId"),
    @NamedQuery(name = "NiktoScan.findByProbe", query = "SELECT n FROM NiktoScan n WHERE n.probe = :probe"),
    @NamedQuery(name = "NiktoScan.findByHost", query = "SELECT n FROM NiktoScan n WHERE n.host = :host"),
    @NamedQuery(name = "NiktoScan.findByIp", query = "SELECT n FROM NiktoScan n WHERE n.ip = :ip"),
    @NamedQuery(name = "NiktoScan.findByPort", query = "SELECT n FROM NiktoScan n WHERE n.port = :port"),
    @NamedQuery(name = "NiktoScan.findByBanner", query = "SELECT n FROM NiktoScan n WHERE n.banner = :banner"),
    @NamedQuery(name = "NiktoScan.findByVulnId", query = "SELECT n FROM NiktoScan n WHERE n.vulnId = :vulnId"),
    @NamedQuery(name = "NiktoScan.findByVulnOSVDB", query = "SELECT n FROM NiktoScan n WHERE n.vulnOSVDB = :vulnOSVDB"),
    @NamedQuery(name = "NiktoScan.findByVulnMethod", query = "SELECT n FROM NiktoScan n WHERE n.vulnMethod = :vulnMethod"),
    @NamedQuery(name = "NiktoScan.findByVulnUrl", query = "SELECT n FROM NiktoScan n WHERE n.vulnUrl = :vulnUrl"),
    @NamedQuery(name = "NiktoScan.findByVulnMsg", query = "SELECT n FROM NiktoScan n WHERE n.vulnMsg = :vulnMsg"),
    @NamedQuery(name = "NiktoScan.findByReportAdded", query = "SELECT n FROM NiktoScan n WHERE n.reportAdded = :reportAdded"),
    @NamedQuery(name = "NiktoScan.findByReportUpdated", query = "SELECT n FROM NiktoScan n WHERE n.reportUpdated = :reportUpdated")})
public class NiktoScan implements Serializable {

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
    @Column(name = "host")
    private String host;
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
    @Column(name = "vuln_OSVDB")
    private String vulnOSVDB;
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
    @Column(name = "report_added")
    @Temporal(TemporalType.TIMESTAMP)
    private Date reportAdded;
    @Column(name = "report_updated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date reportUpdated;

    public NiktoScan() {
    }

    public NiktoScan(Long recId) {
        this.recId = recId;
    }

    public NiktoScan(Long recId, String refId, String nodeId, String probe, String host, String ip, String port, String banner, String vulnId, String vulnOSVDB, String vulnMethod, String vulnUrl, String vulnMsg) {
        this.recId = recId;
        this.refId = refId;
        this.nodeId = nodeId;
        this.probe = probe;
        this.host = host;
        this.ip = ip;
        this.port = port;
        this.banner = banner;
        this.vulnId = vulnId;
        this.vulnOSVDB = vulnOSVDB;
        this.vulnMethod = vulnMethod;
        this.vulnUrl = vulnUrl;
        this.vulnMsg = vulnMsg;
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

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
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

    public String getVulnOSVDB() {
        return vulnOSVDB;
    }

    public void setVulnOSVDB(String vulnOSVDB) {
        this.vulnOSVDB = vulnOSVDB;
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
        if (!(object instanceof NiktoScan)) {
            return false;
        }
        NiktoScan other = (NiktoScan) object;
        if ((this.recId == null && other.recId != null) || (this.recId != null && !this.recId.equals(other.recId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.alertflex.entity.NiktoScan[ recId=" + recId + " ]";
    }
    
}
