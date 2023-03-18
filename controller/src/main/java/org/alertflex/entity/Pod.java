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
@Table(name = "pod")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Pod.findAll", query = "SELECT p FROM Pod p"),
    @NamedQuery(name = "Pod.findByRecId", query = "SELECT p FROM Pod p WHERE p.recId = :recId"),
    @NamedQuery(name = "Pod.findByRefId", query = "SELECT p FROM Pod p WHERE p.refId = :refId"),
    @NamedQuery(name = "Pod.findByNode", query = "SELECT p FROM Pod p WHERE p.node = :node"),
    @NamedQuery(name = "Pod.findByName", query = "SELECT p FROM Pod p WHERE p.name = :name"),
    @NamedQuery(name = "Pod.findByNamespace", query = "SELECT p FROM Pod p WHERE p.namespace = :namespace"),
    @NamedQuery(name = "Pod.findByUid", query = "SELECT p FROM Pod p WHERE p.uid = :uid"),
    @NamedQuery(name = "Pod.findByHostIp", query = "SELECT p FROM Pod p WHERE p.hostIp = :hostIp"),
    @NamedQuery(name = "Pod.findByPodIp", query = "SELECT p FROM Pod p WHERE p.podIp = :podIp"),
    @NamedQuery(name = "Pod.findByPhase", query = "SELECT p FROM Pod p WHERE p.phase = :phase"),
    @NamedQuery(name = "Pod.findByK8sNode", query = "SELECT p FROM Pod p WHERE p.k8sNode = :k8sNode"),
    @NamedQuery(name = "Pod.findByCreationTimestamp", query = "SELECT p FROM Pod p WHERE p.creationTimestamp = :creationTimestamp"),
    @NamedQuery(name = "Pod.findByReportAdded", query = "SELECT p FROM Pod p WHERE p.reportAdded = :reportAdded"),
    @NamedQuery(name = "Pod.findByReportUpdated", query = "SELECT p FROM Pod p WHERE p.reportUpdated = :reportUpdated")})
public class Pod implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "rec_id")
    private Integer recId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "ref_id")
    private String refId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "node")
    private String node;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "namespace")
    private String namespace;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "uid")
    private String uid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "host_ip")
    private String hostIp;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "pod_ip")
    private String podIp;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "phase")
    private String phase;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "k8s_node")
    private String k8sNode;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "creation_timestamp")
    private String creationTimestamp;
    @Column(name = "report_added")
    @Temporal(TemporalType.TIMESTAMP)
    private Date reportAdded;
    @Column(name = "report_updated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date reportUpdated;

    public Pod() {
    }

    public Pod(Integer recId) {
        this.recId = recId;
    }

    public Pod(Integer recId, String refId, String node, String name, String namespace, String uid, String hostIp, String podIp, String phase, String k8sNode, String creationTimestamp) {
        this.recId = recId;
        this.refId = refId;
        this.node = node;
        this.name = name;
        this.namespace = namespace;
        this.uid = uid;
        this.hostIp = hostIp;
        this.podIp = podIp;
        this.phase = phase;
        this.k8sNode = k8sNode;
        this.creationTimestamp = creationTimestamp;
    }

    public Integer getRecId() {
        return recId;
    }

    public void setRecId(Integer recId) {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getHostIp() {
        return hostIp;
    }

    public void setHostIp(String hostIp) {
        this.hostIp = hostIp;
    }

    public String getPodIp() {
        return podIp;
    }

    public void setPodIp(String podIp) {
        this.podIp = podIp;
    }

    public String getPhase() {
        return phase;
    }

    public void setPhase(String phase) {
        this.phase = phase;
    }

    public String getK8sNode() {
        return k8sNode;
    }

    public void setK8sNode(String k8sNode) {
        this.k8sNode = k8sNode;
    }

    public String getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(String creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
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
        if (!(object instanceof Pod)) {
            return false;
        }
        Pod other = (Pod) object;
        if ((this.recId == null && other.recId != null) || (this.recId != null && !this.recId.equals(other.recId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.alertflex.entity.Pod[ recId=" + recId + " ]";
    }
    
}
