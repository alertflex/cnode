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
@Table(name = "nmap_scan")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NmapScan.findAll", query = "SELECT n FROM NmapScan n"),
    @NamedQuery(name = "NmapScan.findByRecId", query = "SELECT n FROM NmapScan n WHERE n.recId = :recId"),
    @NamedQuery(name = "NmapScan.findByRefId", query = "SELECT n FROM NmapScan n WHERE n.refId = :refId"),
    @NamedQuery(name = "NmapScan.findByNode", query = "SELECT n FROM NmapScan n WHERE n.node = :node"),
    @NamedQuery(name = "NmapScan.findByProbe", query = "SELECT n FROM NmapScan n WHERE n.probe = :probe"),
    @NamedQuery(name = "NmapScan.findByHost", query = "SELECT n FROM NmapScan n WHERE n.host = :host"),
    @NamedQuery(name = "NmapScan.findByProtocol", query = "SELECT n FROM NmapScan n WHERE n.protocol = :protocol"),
    @NamedQuery(name = "NmapScan.findByPortId", query = "SELECT n FROM NmapScan n WHERE n.portId = :portId"),
    @NamedQuery(name = "NmapScan.findByState", query = "SELECT n FROM NmapScan n WHERE n.state = :state"),
    @NamedQuery(name = "NmapScan.findByName", query = "SELECT n FROM NmapScan n WHERE n.name = :name"),
    @NamedQuery(name = "NmapScan.findByReportAdded", query = "SELECT n FROM NmapScan n WHERE n.reportAdded = :reportAdded"),
    @NamedQuery(name = "NmapScan.findByReportUpdated", query = "SELECT n FROM NmapScan n WHERE n.reportUpdated = :reportUpdated")})
public class NmapScan implements Serializable {

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
    @Size(min = 1, max = 128)
    @Column(name = "host")
    private String host;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "protocol")
    private String protocol;
    @Basic(optional = false)
    @NotNull
    @Column(name = "portId")
    private int portId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "state")
    private String state;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "name")
    private String name;
    @Column(name = "report_added")
    @Temporal(TemporalType.TIMESTAMP)
    private Date reportAdded;
    @Column(name = "report_updated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date reportUpdated;

    public NmapScan() {
    }

    public NmapScan(Long recId) {
        this.recId = recId;
    }

    public NmapScan(Long recId, String refId, String node, String probe, String host, String protocol, int portId, String state, String name) {
        this.recId = recId;
        this.refId = refId;
        this.node = node;
        this.probe = probe;
        this.host = host;
        this.protocol = protocol;
        this.portId = portId;
        this.state = state;
        this.name = name;
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

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public int getPortId() {
        return portId;
    }

    public void setPortId(int portId) {
        this.portId = portId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        if (!(object instanceof NmapScan)) {
            return false;
        }
        NmapScan other = (NmapScan) object;
        if ((this.recId == null && other.recId != null) || (this.recId != null && !this.recId.equals(other.recId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.alertflex.entity.NmapScan[ recId=" + recId + " ]";
    }
    
}
