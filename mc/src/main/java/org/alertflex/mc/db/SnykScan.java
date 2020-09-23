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
@Table(name = "snyk_scan")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SnykScan.findAll", query = "SELECT s FROM SnykScan s")
    , @NamedQuery(name = "SnykScan.findByRecId", query = "SELECT s FROM SnykScan s WHERE s.recId = :recId")
    , @NamedQuery(name = "SnykScan.findByRefId", query = "SELECT s FROM SnykScan s WHERE s.refId = :refId")
    , @NamedQuery(name = "SnykScan.findByProjectId", query = "SELECT s FROM SnykScan s WHERE s.projectId = :projectId")
    , @NamedQuery(name = "SnykScan.findByVulnerabilityId", query = "SELECT s FROM SnykScan s WHERE s.vulnerabilityId = :vulnerabilityId")
    , @NamedQuery(name = "SnykScan.findByPkgName", query = "SELECT s FROM SnykScan s WHERE s.pkgName = :pkgName")
    , @NamedQuery(name = "SnykScan.findByIssueType", query = "SELECT s FROM SnykScan s WHERE s.issueType = :issueType")
    , @NamedQuery(name = "SnykScan.findByPkgVersions", query = "SELECT s FROM SnykScan s WHERE s.pkgVersions = :pkgVersions")
    , @NamedQuery(name = "SnykScan.findByPriorityScore", query = "SELECT s FROM SnykScan s WHERE s.priorityScore = :priorityScore")
    , @NamedQuery(name = "SnykScan.findByIssueTitle", query = "SELECT s FROM SnykScan s WHERE s.issueTitle = :issueTitle")
    , @NamedQuery(name = "SnykScan.findByIssueSeverity", query = "SELECT s FROM SnykScan s WHERE s.issueSeverity = :issueSeverity")
    , @NamedQuery(name = "SnykScan.findBySeverity", query = "SELECT s FROM SnykScan s WHERE s.severity = :severity")
    , @NamedQuery(name = "SnykScan.findByIssueUrl", query = "SELECT s FROM SnykScan s WHERE s.issueUrl = :issueUrl")
    , @NamedQuery(name = "SnykScan.findByIssueCve", query = "SELECT s FROM SnykScan s WHERE s.issueCve = :issueCve")
    , @NamedQuery(name = "SnykScan.findByPublicationTime", query = "SELECT s FROM SnykScan s WHERE s.publicationTime = :publicationTime")
    , @NamedQuery(name = "SnykScan.findByDisclosureTime", query = "SELECT s FROM SnykScan s WHERE s.disclosureTime = :disclosureTime")
    , @NamedQuery(name = "SnykScan.findByIssueCvssv3", query = "SELECT s FROM SnykScan s WHERE s.issueCvssv3 = :issueCvssv3")
    , @NamedQuery(name = "SnykScan.findByIssueLanguage", query = "SELECT s FROM SnykScan s WHERE s.issueLanguage = :issueLanguage")
    , @NamedQuery(name = "SnykScan.findByReportAdded", query = "SELECT s FROM SnykScan s WHERE s.reportAdded = :reportAdded")
    , @NamedQuery(name = "SnykScan.findByReportUpdated", query = "SELECT s FROM SnykScan s WHERE s.reportUpdated = :reportUpdated")})
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
    @Column(name = "project_id")
    private String projectId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "vulnerability_id")
    private String vulnerabilityId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "pkg_name")
    private String pkgName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "issue_type")
    private String issueType;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "pkg_versions")
    private String pkgVersions;
    @Basic(optional = false)
    @NotNull
    @Column(name = "priority_score")
    private int priorityScore;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "issue_title")
    private String issueTitle;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "issue_severity")
    private String issueSeverity;
    @Basic(optional = false)
    @NotNull
    @Column(name = "severity")
    private int severity;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1024)
    @Column(name = "issue_url")
    private String issueUrl;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "issue_cve")
    private String issueCve;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "publication_time")
    private String publicationTime;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "disclosure_time")
    private String disclosureTime;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "issue_cvssv3")
    private String issueCvssv3;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "issue_language")
    private String issueLanguage;
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

    public SnykScan(Long recId, String refId, String projectId, String vulnerabilityId, String pkgName, String issueType, String pkgVersions, int priorityScore, String issueTitle, String issueSeverity, int severity, String issueUrl, String issueCve, String publicationTime, String disclosureTime, String issueCvssv3, String issueLanguage) {
        this.recId = recId;
        this.refId = refId;
        this.projectId = projectId;
        this.vulnerabilityId = vulnerabilityId;
        this.pkgName = pkgName;
        this.issueType = issueType;
        this.pkgVersions = pkgVersions;
        this.priorityScore = priorityScore;
        this.issueTitle = issueTitle;
        this.issueSeverity = issueSeverity;
        this.severity = severity;
        this.issueUrl = issueUrl;
        this.issueCve = issueCve;
        this.publicationTime = publicationTime;
        this.disclosureTime = disclosureTime;
        this.issueCvssv3 = issueCvssv3;
        this.issueLanguage = issueLanguage;
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

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getVulnerabilityId() {
        return vulnerabilityId;
    }

    public void setVulnerabilityId(String vulnerabilityId) {
        this.vulnerabilityId = vulnerabilityId;
    }

    public String getPkgName() {
        return pkgName;
    }

    public void setPkgName(String pkgName) {
        this.pkgName = pkgName;
    }

    public String getIssueType() {
        return issueType;
    }

    public void setIssueType(String issueType) {
        this.issueType = issueType;
    }

    public String getPkgVersions() {
        return pkgVersions;
    }

    public void setPkgVersions(String pkgVersions) {
        this.pkgVersions = pkgVersions;
    }

    public int getPriorityScore() {
        return priorityScore;
    }

    public void setPriorityScore(int priorityScore) {
        this.priorityScore = priorityScore;
    }

    public String getIssueTitle() {
        return issueTitle;
    }

    public void setIssueTitle(String issueTitle) {
        this.issueTitle = issueTitle;
    }

    public String getIssueSeverity() {
        return issueSeverity;
    }

    public void setIssueSeverity(String issueSeverity) {
        this.issueSeverity = issueSeverity;
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
                if (severity == 3) return "\u2462";
            }
        }
        return "\u24ea";
     
    }

    public String getIssueUrl() {
        return issueUrl;
    }

    public void setIssueUrl(String issueUrl) {
        this.issueUrl = issueUrl;
    }

    public String getIssueCve() {
        return issueCve;
    }

    public void setIssueCve(String issueCve) {
        this.issueCve = issueCve;
    }

    public String getPublicationTime() {
        return publicationTime;
    }

    public void setPublicationTime(String publicationTime) {
        this.publicationTime = publicationTime;
    }

    public String getDisclosureTime() {
        return disclosureTime;
    }

    public void setDisclosureTime(String disclosureTime) {
        this.disclosureTime = disclosureTime;
    }

    public String getIssueCvssv3() {
        return issueCvssv3;
    }

    public void setIssueCvssv3(String issueCvssv3) {
        this.issueCvssv3 = issueCvssv3;
    }

    public String getIssueLanguage() {
        return issueLanguage;
    }

    public void setIssueLanguage(String issueLanguage) {
        this.issueLanguage = issueLanguage;
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
        return "org.alertflex.mc.db.SnykScan[ recId=" + recId + " ]";
    }
    
}
