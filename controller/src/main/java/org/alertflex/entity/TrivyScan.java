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
@Table(name = "trivy_scan")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TrivyScan.findAll", query = "SELECT t FROM TrivyScan t"),
    @NamedQuery(name = "TrivyScan.findByRecId", query = "SELECT t FROM TrivyScan t WHERE t.recId = :recId"),
    @NamedQuery(name = "TrivyScan.findByRefId", query = "SELECT t FROM TrivyScan t WHERE t.refId = :refId"),
    @NamedQuery(name = "TrivyScan.findByNode", query = "SELECT t FROM TrivyScan t WHERE t.node = :node"),
    @NamedQuery(name = "TrivyScan.findByProbe", query = "SELECT t FROM TrivyScan t WHERE t.probe = :probe"),
    @NamedQuery(name = "TrivyScan.findByImageName", query = "SELECT t FROM TrivyScan t WHERE t.imageName = :imageName"),
    @NamedQuery(name = "TrivyScan.findByImageType", query = "SELECT t FROM TrivyScan t WHERE t.imageType = :imageType"),
    @NamedQuery(name = "TrivyScan.findByImageId", query = "SELECT t FROM TrivyScan t WHERE t.imageId = :imageId"),
    @NamedQuery(name = "TrivyScan.findByPkgName", query = "SELECT t FROM TrivyScan t WHERE t.pkgName = :pkgName"),
    @NamedQuery(name = "TrivyScan.findByPkgVersion", query = "SELECT t FROM TrivyScan t WHERE t.pkgVersion = :pkgVersion"),
    @NamedQuery(name = "TrivyScan.findByVulnerability", query = "SELECT t FROM TrivyScan t WHERE t.vulnerability = :vulnerability"),
    @NamedQuery(name = "TrivyScan.findByTitle", query = "SELECT t FROM TrivyScan t WHERE t.title = :title"),
    @NamedQuery(name = "TrivyScan.findByDescription", query = "SELECT t FROM TrivyScan t WHERE t.description = :description"),
    @NamedQuery(name = "TrivyScan.findByVulnRef", query = "SELECT t FROM TrivyScan t WHERE t.vulnRef = :vulnRef"),
    @NamedQuery(name = "TrivyScan.findBySeverity", query = "SELECT t FROM TrivyScan t WHERE t.severity = :severity"),
    @NamedQuery(name = "TrivyScan.findByReportAdded", query = "SELECT t FROM TrivyScan t WHERE t.reportAdded = :reportAdded"),
    @NamedQuery(name = "TrivyScan.findByReportUpdated", query = "SELECT t FROM TrivyScan t WHERE t.reportUpdated = :reportUpdated")})
public class TrivyScan implements Serializable {

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
    @Column(name = "node")
    private String node;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "probe")
    private String probe;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1024)
    @Column(name = "image_name")
    private String imageName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1024)
    @Column(name = "image_type")
    private String imageType;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1024)
    @Column(name = "image_id")
    private String imageId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "pkg_name")
    private String pkgName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "pkg_version")
    private String pkgVersion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "vulnerability")
    private String vulnerability;
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
    @Size(min = 1, max = 1024)
    @Column(name = "vuln_ref")
    private String vulnRef;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "severity")
    private String severity;
    @Column(name = "report_added")
    @Temporal(TemporalType.TIMESTAMP)
    private Date reportAdded;
    @Column(name = "report_updated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date reportUpdated;

    public TrivyScan() {
    }

    public TrivyScan(Long recId) {
        this.recId = recId;
    }

    public TrivyScan(Long recId, String refId, String node, String probe, String imageName, String imageType, String imageId, String pkgName, String pkgVersion, String vulnerability, String title, String description, String vulnRef, String severity) {
        this.recId = recId;
        this.refId = refId;
        this.node = node;
        this.probe = probe;
        this.imageName = imageName;
        this.imageType = imageType;
        this.imageId = imageId;
        this.pkgName = pkgName;
        this.pkgVersion = pkgVersion;
        this.vulnerability = vulnerability;
        this.title = title;
        this.description = description;
        this.vulnRef = vulnRef;
        this.severity = severity;
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

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getPkgName() {
        return pkgName;
    }

    public void setPkgName(String pkgName) {
        this.pkgName = pkgName;
    }

    public String getPkgVersion() {
        return pkgVersion;
    }

    public void setPkgVersion(String pkgVersion) {
        this.pkgVersion = pkgVersion;
    }

    public String getVulnerability() {
        return vulnerability;
    }

    public void setVulnerability(String vulnerability) {
        this.vulnerability = vulnerability;
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

    public String getVulnRef() {
        return vulnRef;
    }

    public void setVulnRef(String vulnRef) {
        this.vulnRef = vulnRef;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
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
        if (!(object instanceof TrivyScan)) {
            return false;
        }
        TrivyScan other = (TrivyScan) object;
        if ((this.recId == null && other.recId != null) || (this.recId != null && !this.recId.equals(other.recId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.alertflex.entity.TrivyScan[ recId=" + recId + " ]";
    }
    
}
