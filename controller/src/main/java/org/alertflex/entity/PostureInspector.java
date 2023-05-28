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
@Table(name = "posture_inspector")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PostureInspector.findAll", query = "SELECT p FROM PostureInspector p"),
    @NamedQuery(name = "PostureInspector.findByRecId", query = "SELECT p FROM PostureInspector p WHERE p.recId = :recId"),
    @NamedQuery(name = "PostureInspector.findByRefId", query = "SELECT p FROM PostureInspector p WHERE p.refId = :refId"),
    @NamedQuery(name = "PostureInspector.findByScanUuid", query = "SELECT p FROM PostureInspector p WHERE p.scanUuid = :scanUuid"),
    @NamedQuery(name = "PostureInspector.findByAlertUuid", query = "SELECT p FROM PostureInspector p WHERE p.alertUuid = :alertUuid"),
    @NamedQuery(name = "PostureInspector.findByEc2Name", query = "SELECT p FROM PostureInspector p WHERE p.ec2Name = :ec2Name"),
    @NamedQuery(name = "PostureInspector.findByArn", query = "SELECT p FROM PostureInspector p WHERE p.arn = :arn"),
    @NamedQuery(name = "PostureInspector.findByAssetType", query = "SELECT p FROM PostureInspector p WHERE p.assetType = :assetType"),
    @NamedQuery(name = "PostureInspector.findByFindingId", query = "SELECT p FROM PostureInspector p WHERE p.findingId = :findingId"),
    @NamedQuery(name = "PostureInspector.findByTitle", query = "SELECT p FROM PostureInspector p WHERE p.title = :title"),
    @NamedQuery(name = "PostureInspector.findByDescription", query = "SELECT p FROM PostureInspector p WHERE p.description = :description"),
    @NamedQuery(name = "PostureInspector.findByRemediation", query = "SELECT p FROM PostureInspector p WHERE p.remediation = :remediation"),
    @NamedQuery(name = "PostureInspector.findBySeverity", query = "SELECT p FROM PostureInspector p WHERE p.severity = :severity"),
    @NamedQuery(name = "PostureInspector.findByStatus", query = "SELECT p FROM PostureInspector p WHERE p.status = :status"),
    @NamedQuery(name = "PostureInspector.findByReportAdded", query = "SELECT p FROM PostureInspector p WHERE p.reportAdded = :reportAdded"),
    @NamedQuery(name = "PostureInspector.findByReportUpdated", query = "SELECT p FROM PostureInspector p WHERE p.reportUpdated = :reportUpdated")})
public class PostureInspector implements Serializable {

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
    @Size(min = 1, max = 256)
    @Column(name = "ec2_name")
    private String ec2Name;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "arn")
    private String arn;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "asset_type")
    private String assetType;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "finding_id")
    private String findingId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2048)
    @Column(name = "title")
    private String title;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2048)
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2048)
    @Column(name = "remediation")
    private String remediation;
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

    public PostureInspector() {
    }

    public PostureInspector(Long recId) {
        this.recId = recId;
    }

    public PostureInspector(Long recId, String refId, String scanUuid, String alertUuid, String ec2Name, String arn, String assetType, String findingId, String title, String description, String remediation, String severity, String status) {
        this.recId = recId;
        this.refId = refId;
        this.scanUuid = scanUuid;
        this.alertUuid = alertUuid;
        this.ec2Name = ec2Name;
        this.arn = arn;
        this.assetType = assetType;
        this.findingId = findingId;
        this.title = title;
        this.description = description;
        this.remediation = remediation;
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

    public String getEc2Name() {
        return ec2Name;
    }

    public void setEc2Name(String ec2Name) {
        this.ec2Name = ec2Name;
    }

    public String getArn() {
        return arn;
    }

    public void setArn(String arn) {
        this.arn = arn;
    }

    public String getAssetType() {
        return assetType;
    }

    public void setAssetType(String assetType) {
        this.assetType = assetType;
    }

    public String getFindingId() {
        return findingId;
    }

    public void setFindingId(String findingId) {
        this.findingId = findingId;
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

    public String getRemediation() {
        return remediation;
    }

    public void setRemediation(String remediation) {
        this.remediation = remediation;
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
        if (!(object instanceof PostureInspector)) {
            return false;
        }
        PostureInspector other = (PostureInspector) object;
        if ((this.recId == null && other.recId != null) || (this.recId != null && !this.recId.equals(other.recId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.alertflex.entity.PostureInspector[ recId=" + recId + " ]";
    }
    
}
