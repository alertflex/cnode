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
@Table(name = "hunter_scan")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "HunterScan.findAll", query = "SELECT h FROM HunterScan h"),
    @NamedQuery(name = "HunterScan.findByRecId", query = "SELECT h FROM HunterScan h WHERE h.recId = :recId"),
    @NamedQuery(name = "HunterScan.findByRefId", query = "SELECT h FROM HunterScan h WHERE h.refId = :refId"),
    @NamedQuery(name = "HunterScan.findByNodeId", query = "SELECT h FROM HunterScan h WHERE h.nodeId = :nodeId"),
    @NamedQuery(name = "HunterScan.findByProbe", query = "SELECT h FROM HunterScan h WHERE h.probe = :probe"),
    @NamedQuery(name = "HunterScan.findByTarget", query = "SELECT h FROM HunterScan h WHERE h.target = :target"),
    @NamedQuery(name = "HunterScan.findByKubeType", query = "SELECT h FROM HunterScan h WHERE h.kubeType = :kubeType"),
    @NamedQuery(name = "HunterScan.findByServices", query = "SELECT h FROM HunterScan h WHERE h.services = :services"),
    @NamedQuery(name = "HunterScan.findByLocation", query = "SELECT h FROM HunterScan h WHERE h.location = :location"),
    @NamedQuery(name = "HunterScan.findByVid", query = "SELECT h FROM HunterScan h WHERE h.vid = :vid"),
    @NamedQuery(name = "HunterScan.findByCat", query = "SELECT h FROM HunterScan h WHERE h.cat = :cat"),
    @NamedQuery(name = "HunterScan.findBySeverity", query = "SELECT h FROM HunterScan h WHERE h.severity = :severity"),
    @NamedQuery(name = "HunterScan.findByVulnerability", query = "SELECT h FROM HunterScan h WHERE h.vulnerability = :vulnerability"),
    @NamedQuery(name = "HunterScan.findByDescription", query = "SELECT h FROM HunterScan h WHERE h.description = :description"),
    @NamedQuery(name = "HunterScan.findByEvidence", query = "SELECT h FROM HunterScan h WHERE h.evidence = :evidence"),
    @NamedQuery(name = "HunterScan.findByAvdReference", query = "SELECT h FROM HunterScan h WHERE h.avdReference = :avdReference"),
    @NamedQuery(name = "HunterScan.findByHunter", query = "SELECT h FROM HunterScan h WHERE h.hunter = :hunter"),
    @NamedQuery(name = "HunterScan.findByReportAdded", query = "SELECT h FROM HunterScan h WHERE h.reportAdded = :reportAdded"),
    @NamedQuery(name = "HunterScan.findByReportUpdated", query = "SELECT h FROM HunterScan h WHERE h.reportUpdated = :reportUpdated")})
public class HunterScan implements Serializable {

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
    @Column(name = "vid")
    private String vid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1024)
    @Column(name = "cat")
    private String cat;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1024)
    @Column(name = "severity")
    private String severity;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1024)
    @Column(name = "vulnerability")
    private String vulnerability;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2048)
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "evidence")
    private String evidence;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1024)
    @Column(name = "avd_reference")
    private String avdReference;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "hunter")
    private String hunter;
    @Column(name = "report_added")
    @Temporal(TemporalType.TIMESTAMP)
    private Date reportAdded;
    @Column(name = "report_updated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date reportUpdated;

    public HunterScan() {
    }

    public HunterScan(Long recId) {
        this.recId = recId;
    }

    public HunterScan(Long recId, String refId, String nodeId, String probe, String target, String kubeType, String services, String location, String vid, String cat, String severity, String vulnerability, String description, String evidence, String avdReference, String hunter) {
        this.recId = recId;
        this.refId = refId;
        this.nodeId = nodeId;
        this.probe = probe;
        this.target = target;
        this.kubeType = kubeType;
        this.services = services;
        this.location = location;
        this.vid = vid;
        this.cat = cat;
        this.severity = severity;
        this.vulnerability = vulnerability;
        this.description = description;
        this.evidence = evidence;
        this.avdReference = avdReference;
        this.hunter = hunter;
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

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getCat() {
        return cat;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getVulnerability() {
        return vulnerability;
    }

    public void setVulnerability(String vulnerability) {
        this.vulnerability = vulnerability;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEvidence() {
        return evidence;
    }

    public void setEvidence(String evidence) {
        this.evidence = evidence;
    }

    public String getAvdReference() {
        return avdReference;
    }

    public void setAvdReference(String avdReference) {
        this.avdReference = avdReference;
    }

    public String getHunter() {
        return hunter;
    }

    public void setHunter(String hunter) {
        this.hunter = hunter;
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
        if (!(object instanceof HunterScan)) {
            return false;
        }
        HunterScan other = (HunterScan) object;
        if ((this.recId == null && other.recId != null) || (this.recId != null && !this.recId.equals(other.recId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.alertflex.entity.HunterScan[ recId=" + recId + " ]";
    }
    
}
