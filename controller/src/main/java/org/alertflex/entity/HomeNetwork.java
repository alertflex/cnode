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
@Table(name = "home_network")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "HomeNetwork.findAll", query = "SELECT h FROM HomeNetwork h")
    , @NamedQuery(name = "HomeNetwork.findByRecId", query = "SELECT h FROM HomeNetwork h WHERE h.recId = :recId")
    , @NamedQuery(name = "HomeNetwork.findByRefId", query = "SELECT h FROM HomeNetwork h WHERE h.refId = :refId")
    , @NamedQuery(name = "HomeNetwork.findByNodeId", query = "SELECT h FROM HomeNetwork h WHERE h.nodeId = :nodeId")
    , @NamedQuery(name = "HomeNetwork.findByDescription", query = "SELECT h FROM HomeNetwork h WHERE h.description = :description")
    , @NamedQuery(name = "HomeNetwork.findByNetwork", query = "SELECT h FROM HomeNetwork h WHERE h.network = :network")
    , @NamedQuery(name = "HomeNetwork.findByNetmask", query = "SELECT h FROM HomeNetwork h WHERE h.netmask = :netmask")
    , @NamedQuery(name = "HomeNetwork.findByAlertSuppress", query = "SELECT h FROM HomeNetwork h WHERE h.alertSuppress = :alertSuppress")})
public class HomeNetwork implements Serializable {

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
    @Column(name = "node_id")
    private String nodeId;
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
    @Column(name = "alert_suppress")
    private int alertSuppress;

    public HomeNetwork() {
    }

    public HomeNetwork(Integer recId) {
        this.recId = recId;
    }

    public HomeNetwork(Integer recId, String refId, String nodeId, String description, String network, String netmask, int alertSuppress) {
        this.recId = recId;
        this.refId = refId;
        this.nodeId = nodeId;
        this.description = description;
        this.network = network;
        this.netmask = netmask;
        this.alertSuppress = alertSuppress;
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

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
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

    public int getAlertSuppress() {
        return alertSuppress;
    }

    public void setAlertSuppress(int alertSuppress) {
        this.alertSuppress = alertSuppress;
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
        if (!(object instanceof HomeNetwork)) {
            return false;
        }
        HomeNetwork other = (HomeNetwork) object;
        if ((this.recId == null && other.recId != null) || (this.recId != null && !this.recId.equals(other.recId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.alertflex.entity.HomeNetwork[ recId=" + recId + " ]";
    }
    
}
