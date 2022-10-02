/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alertflex.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author root
 */
@Entity
@Table(name = "networks")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Networks.findAll", query = "SELECT n FROM Networks n"),
    @NamedQuery(name = "Networks.findByRecId", query = "SELECT n FROM Networks n WHERE n.recId = :recId"),
    @NamedQuery(name = "Networks.findByRefId", query = "SELECT n FROM Networks n WHERE n.refId = :refId"),
    @NamedQuery(name = "Networks.findByNode", query = "SELECT n FROM Networks n WHERE n.node = :node"),
    @NamedQuery(name = "Networks.findByDescription", query = "SELECT n FROM Networks n WHERE n.description = :description"),
    @NamedQuery(name = "Networks.findByNetwork", query = "SELECT n FROM Networks n WHERE n.network = :network"),
    @NamedQuery(name = "Networks.findByNetmask", query = "SELECT n FROM Networks n WHERE n.netmask = :netmask"),
    @NamedQuery(name = "Networks.findByNetAcl", query = "SELECT n FROM Networks n WHERE n.netAcl = :netAcl"),
    @NamedQuery(name = "Networks.findByNetType", query = "SELECT n FROM Networks n WHERE n.netType = :netType")})
public class Networks implements Serializable {

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
    @Size(min = 1, max = 512)
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "network")
    private String network;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "netmask")
    private String netmask;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "net_acl")
    private String netAcl;
    @Basic(optional = false)
    @NotNull
    @Column(name = "net_type")
    private int netType;

    public Networks() {
    }

    public Networks(Integer recId) {
        this.recId = recId;
    }

    public Networks(Integer recId, String refId, String node, String description, String network, String netmask, String netAcl, int netType) {
        this.recId = recId;
        this.refId = refId;
        this.node = node;
        this.description = description;
        this.network = network;
        this.netmask = netmask;
        this.netAcl = netAcl;
        this.netType = netType;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public String getNetmask() {
        return netmask;
    }

    public void setNetmask(String netmask) {
        this.netmask = netmask;
    }

    public String getNetAcl() {
        return netAcl;
    }

    public void setNetAcl(String netAcl) {
        this.netAcl = netAcl;
    }

    public int getNetType() {
        return netType;
    }

    public void setNetType(int netType) {
        this.netType = netType;
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
        if (!(object instanceof Networks)) {
            return false;
        }
        Networks other = (Networks) object;
        if ((this.recId == null && other.recId != null) || (this.recId != null && !this.recId.equals(other.recId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.alertflex.entity.Networks[ recId=" + recId + " ]";
    }
    
}
