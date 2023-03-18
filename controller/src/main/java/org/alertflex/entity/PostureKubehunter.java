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
@Table(name = "posture_kubehunter")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PostureKubehunter.findAll", query = "SELECT p FROM PostureKubehunter p"),
    @NamedQuery(name = "PostureKubehunter.findByRecId", query = "SELECT p FROM PostureKubehunter p WHERE p.recId = :recId"),
    @NamedQuery(name = "PostureKubehunter.findByRefId", query = "SELECT p FROM PostureKubehunter p WHERE p.refId = :refId"),
    @NamedQuery(name = "PostureKubehunter.findByScanUuid", query = "SELECT p FROM PostureKubehunter p WHERE p.scanUuid = :scanUuid"),
    @NamedQuery(name = "PostureKubehunter.findByNode", query = "SELECT p FROM PostureKubehunter p WHERE p.node = :node"),
    @NamedQuery(name = "PostureKubehunter.findByProbe", query = "SELECT p FROM PostureKubehunter p WHERE p.probe = :probe"),
    @NamedQuery(name = "PostureKubehunter.findByTarget", query = "SELECT p FROM PostureKubehunter p WHERE p.target = :target"),
    @NamedQuery(name = "PostureKubehunter.findByKubeType", query = "SELECT p FROM PostureKubehunter p WHERE p.kubeType = :kubeType"),
    @NamedQuery(name = "PostureKubehunter.findByServices", query = "SELECT p FROM PostureKubehunter p WHERE p.services = :services"),
    @NamedQuery(name = "PostureKubehunter.findByLocation", query = "SELECT p FROM PostureKubehunter p WHERE p.location = :location"),
    @NamedQuery(name = "PostureKubehunter.findByVulnerabilityId", query = "SELECT p FROM PostureKubehunter p WHERE p.vulnerabilityId = :vulnerabilityId"),
    @NamedQuery(name = "PostureKubehunter.findByCategory", query = "SELECT p FROM PostureKubehunter p WHERE p.category = :category"),
    @NamedQuery(name = "PostureKubehunter.findByTitle", query = "SELECT p FROM PostureKubehunter p WHERE p.title = :title"),
    @NamedQuery(name = "PostureKubehunter.findByDescription", query = "SELECT p FROM PostureKubehunter p WHERE p.description = :description"),
    @NamedQuery(name = "PostureKubehunter.findByReference", query = "SELECT p FROM PostureKubehunter p WHERE p.reference = :reference"),
    @NamedQuery(name = "PostureKubehunter.findBySeverity", query = "SELECT p FROM PostureKubehunter p WHERE p.severity = :severity"),
    @NamedQuery(name = "PostureKubehunter.findByReportAdded", query = "SELECT p FROM PostureKubehunter p WHERE p.reportAdded = :reportAdded"),
    @NamedQuery(name = "PostureKubehunter.findByReportUpdated", query = "SELECT p FROM PostureKubehunter p WHERE p.reportUpdated = :reportUpdated")})
public class PostureKubehunter implements Serializable {

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
    @Size(min = 1, max = 512)
    @Column(name = "kube_type")
    private String kubeType;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1024)
    @Column(name = "services")
    private String services;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "location")
    private String location;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "vulnerability_id")
    private String vulnerabilityId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1024)
    @Column(name = "category")
    private String category;
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
    @Column(name = "reference")
    private String reference;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1024)
    @Column(name = "severity")
    private String severity;
    @Column(name = "report_added")
    @Temporal(TemporalType.TIMESTAMP)
    private Date reportAdded;
    @Column(name = "report_updated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date reportUpdated;

    public PostureKubehunter() {
    }

    public PostureKubehunter(Long recId) {
        this.recId = recId;
    }

    public PostureKubehunter(Long recId, String refId, String scanUuid, String node, String probe, String target, String kubeType, String services, String location, String vulnerabilityId, String category, String title, String description, String reference, String severity) {
        this.recId = recId;
        this.refId = refId;
        this.scanUuid = scanUuid;
        this.node = node;
        this.probe = probe;
        this.target = target;
        this.kubeType = kubeType;
        this.services = services;
        this.location = location;
        this.vulnerabilityId = vulnerabilityId;
        this.category = category;
        this.title = title;
        this.description = description;
        this.reference = reference;
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

    public String getScanUuid() {
        return scanUuid;
    }

    public void setScanUuid(String scanUuid) {
        this.scanUuid = scanUuid;
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

    public String getKubeType() {
        return kubeType;
    }

    public void setKubeType(String kubeType) {
        this.kubeType = kubeType;
    }

    public String getServices() {
        return services;
    }

    public void setServices(String services) {
        this.services = services;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getVulnerabilityId() {
        return vulnerabilityId;
    }

    public void setVulnerabilityId(String vulnerabilityId) {
        this.vulnerabilityId = vulnerabilityId;
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

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
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
        if (!(object instanceof PostureKubehunter)) {
            return false;
        }
        PostureKubehunter other = (PostureKubehunter) object;
        if ((this.recId == null && other.recId != null) || (this.recId != null && !this.recId.equals(other.recId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.alertflex.entity.PostureKubehunter[ recId=" + recId + " ]";
    }
    
}
