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
@Table(name = "inspector_scan")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "InspectorScan.findAll", query = "SELECT i FROM InspectorScan i"),
    @NamedQuery(name = "InspectorScan.findByRecId", query = "SELECT i FROM InspectorScan i WHERE i.recId = :recId"),
    @NamedQuery(name = "InspectorScan.findByRefId", query = "SELECT i FROM InspectorScan i WHERE i.refId = :refId"),
    @NamedQuery(name = "InspectorScan.findByEc2Name", query = "SELECT i FROM InspectorScan i WHERE i.ec2Name = :ec2Name"),
    @NamedQuery(name = "InspectorScan.findByArn", query = "SELECT i FROM InspectorScan i WHERE i.arn = :arn"),
    @NamedQuery(name = "InspectorScan.findByAssetType", query = "SELECT i FROM InspectorScan i WHERE i.assetType = :assetType"),
    @NamedQuery(name = "InspectorScan.findBySeverity", query = "SELECT i FROM InspectorScan i WHERE i.severity = :severity"),
    @NamedQuery(name = "InspectorScan.findByFindingId", query = "SELECT i FROM InspectorScan i WHERE i.findingId = :findingId"),
    @NamedQuery(name = "InspectorScan.findByTitle", query = "SELECT i FROM InspectorScan i WHERE i.title = :title"),
    @NamedQuery(name = "InspectorScan.findByDescription", query = "SELECT i FROM InspectorScan i WHERE i.description = :description"),
    @NamedQuery(name = "InspectorScan.findByRecommendation", query = "SELECT i FROM InspectorScan i WHERE i.recommendation = :recommendation"),
    @NamedQuery(name = "InspectorScan.findByReportAdded", query = "SELECT i FROM InspectorScan i WHERE i.reportAdded = :reportAdded"),
    @NamedQuery(name = "InspectorScan.findByReportUpdated", query = "SELECT i FROM InspectorScan i WHERE i.reportUpdated = :reportUpdated")})
public class InspectorScan implements Serializable {

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
    @Column(name = "severity")
    private String severity;
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
    @Column(name = "recommendation")
    private String recommendation;
    @Column(name = "report_added")
    @Temporal(TemporalType.TIMESTAMP)
    private Date reportAdded;
    @Column(name = "report_updated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date reportUpdated;

    public InspectorScan() {
    }

    public InspectorScan(Long recId) {
        this.recId = recId;
    }

    public InspectorScan(Long recId, String refId, String ec2Name, String arn, String assetType, String severity, String findingId, String title, String description, String recommendation) {
        this.recId = recId;
        this.refId = refId;
        this.ec2Name = ec2Name;
        this.arn = arn;
        this.assetType = assetType;
        this.severity = severity;
        this.findingId = findingId;
        this.title = title;
        this.description = description;
        this.recommendation = recommendation;
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

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
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

    public String getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(String recommendation) {
        this.recommendation = recommendation;
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
        if (!(object instanceof InspectorScan)) {
            return false;
        }
        InspectorScan other = (InspectorScan) object;
        if ((this.recId == null && other.recId != null) || (this.recId != null && !this.recId.equals(other.recId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.alertflex.entity.InspectorScan[ recId=" + recId + " ]";
    }
    
}
