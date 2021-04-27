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

/**
 *
 * @author root
 */
@Entity
@Table(name = "snyk_scan")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SnykScan.findAll", query = "SELECT s FROM SnykScan s"),
    @NamedQuery(name = "SnykScan.findByRecId", query = "SELECT s FROM SnykScan s WHERE s.recId = :recId"),
    @NamedQuery(name = "SnykScan.findByRefId", query = "SELECT s FROM SnykScan s WHERE s.refId = :refId"),
    @NamedQuery(name = "SnykScan.findByNodeId", query = "SELECT s FROM SnykScan s WHERE s.nodeId = :nodeId"),
    @NamedQuery(name = "SnykScan.findByProbe", query = "SELECT s FROM SnykScan s WHERE s.probe = :probe"),
    @NamedQuery(name = "SnykScan.findByProjectId", query = "SELECT s FROM SnykScan s WHERE s.projectId = :projectId"),
    @NamedQuery(name = "SnykScan.findByVulnId", query = "SELECT s FROM SnykScan s WHERE s.vulnId = :vulnId"),
    @NamedQuery(name = "SnykScan.findByPackageName", query = "SELECT s FROM SnykScan s WHERE s.packageName = :packageName"),
    @NamedQuery(name = "SnykScan.findByPackageManager", query = "SELECT s FROM SnykScan s WHERE s.packageManager = :packageManager"),
    @NamedQuery(name = "SnykScan.findBySeverity", query = "SELECT s FROM SnykScan s WHERE s.severity = :severity"),
    @NamedQuery(name = "SnykScan.findByLanguage", query = "SELECT s FROM SnykScan s WHERE s.language = :language"),
    @NamedQuery(name = "SnykScan.findByTitle", query = "SELECT s FROM SnykScan s WHERE s.title = :title"),
    @NamedQuery(name = "SnykScan.findByDescription", query = "SELECT s FROM SnykScan s WHERE s.description = :description"),
    @NamedQuery(name = "SnykScan.findByVulnVersion", query = "SELECT s FROM SnykScan s WHERE s.vulnVersion = :vulnVersion"),
    @NamedQuery(name = "SnykScan.findByVulnRef", query = "SELECT s FROM SnykScan s WHERE s.vulnRef = :vulnRef"),
    @NamedQuery(name = "SnykScan.findByVulnCve", query = "SELECT s FROM SnykScan s WHERE s.vulnCve = :vulnCve"),
    @NamedQuery(name = "SnykScan.findByVulnCwe", query = "SELECT s FROM SnykScan s WHERE s.vulnCwe = :vulnCwe"),
    @NamedQuery(name = "SnykScan.findByPublicationTime", query = "SELECT s FROM SnykScan s WHERE s.publicationTime = :publicationTime"),
    @NamedQuery(name = "SnykScan.findByReportAdded", query = "SELECT s FROM SnykScan s WHERE s.reportAdded = :reportAdded"),
    @NamedQuery(name = "SnykScan.findByReportUpdated", query = "SELECT s FROM SnykScan s WHERE s.reportUpdated = :reportUpdated")})
public class SnykScan implements Serializable {

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
    @Size(min = 1, max = 1024)
    @Column(name = "project_id")
    private String projectId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1024)
    @Column(name = "vuln_id")
    private String vulnId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1024)
    @Column(name = "package_name")
    private String packageName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "package_manager")
    private String packageManager;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "severity")
    private String severity;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "language")
    private String language;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1024)
    @Column(name = "title")
    private String title;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2048)
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "vuln_version")
    private String vulnVersion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2048)
    @Column(name = "vuln_ref")
    private String vulnRef;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "vuln_cve")
    private String vulnCve;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "vuln_cwe")
    private String vulnCwe;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "publication_time")
    private String publicationTime;
    @Column(name = "report_added")
    @Temporal(TemporalType.TIMESTAMP)
    private Date reportAdded;
    @Column(name = "report_updated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date reportUpdated;

    public SnykScan() {
    }

    public SnykScan(Long recId) {
        this.recId = recId;
    }

    public SnykScan(Long recId, String refId, String nodeId, String probe, String projectId, String vulnId, String packageName, String packageManager, String severity, String language, String title, String description, String vulnVersion, String vulnRef, String vulnCve, String vulnCwe, String publicationTime) {
        this.recId = recId;
        this.refId = refId;
        this.nodeId = nodeId;
        this.probe = probe;
        this.projectId = projectId;
        this.vulnId = vulnId;
        this.packageName = packageName;
        this.packageManager = packageManager;
        this.severity = severity;
        this.language = language;
        this.title = title;
        this.description = description;
        this.vulnVersion = vulnVersion;
        this.vulnRef = vulnRef;
        this.vulnCve = vulnCve;
        this.vulnCwe = vulnCwe;
        this.publicationTime = publicationTime;
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

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getVulnId() {
        return vulnId;
    }

    public void setVulnId(String vulnId) {
        this.vulnId = vulnId;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getPackageManager() {
        return packageManager;
    }

    public void setPackageManager(String packageManager) {
        this.packageManager = packageManager;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
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

    public String getVulnVersion() {
        return vulnVersion;
    }

    public void setVulnVersion(String vulnVersion) {
        this.vulnVersion = vulnVersion;
    }

    public String getVulnRef() {
        return vulnRef;
    }

    public void setVulnRef(String vulnRef) {
        this.vulnRef = vulnRef;
    }

    public String getVulnCve() {
        return vulnCve;
    }

    public void setVulnCve(String vulnCve) {
        this.vulnCve = vulnCve;
    }

    public String getVulnCwe() {
        return vulnCwe;
    }

    public void setVulnCwe(String vulnCwe) {
        this.vulnCwe = vulnCwe;
    }

    public String getPublicationTime() {
        return publicationTime;
    }

    public void setPublicationTime(String publicationTime) {
        this.publicationTime = publicationTime;
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
        if (!(object instanceof SnykScan)) {
            return false;
        }
        SnykScan other = (SnykScan) object;
        if ((this.recId == null && other.recId != null) || (this.recId != null && !this.recId.equals(other.recId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.alertflex.entity.SnykScan[ recId=" + recId + " ]";
    }
    
}
