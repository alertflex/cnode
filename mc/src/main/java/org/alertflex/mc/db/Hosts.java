/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alertflex.mc.db;

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
@Table(name = "hosts")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Hosts.findAll", query = "SELECT h FROM Hosts h")
    , @NamedQuery(name = "Hosts.findByRecId", query = "SELECT h FROM Hosts h WHERE h.recId = :recId")
    , @NamedQuery(name = "Hosts.findByName", query = "SELECT h FROM Hosts h WHERE h.name = :name")
    , @NamedQuery(name = "Hosts.findByRefId", query = "SELECT h FROM Hosts h WHERE h.refId = :refId")
    , @NamedQuery(name = "Hosts.findByNode", query = "SELECT h FROM Hosts h WHERE h.node = :node")
    , @NamedQuery(name = "Hosts.findByCred", query = "SELECT h FROM Hosts h WHERE h.cred = :cred")
    , @NamedQuery(name = "Hosts.findBySensor", query = "SELECT h FROM Hosts h WHERE h.sensor = :sensor")
    , @NamedQuery(name = "Hosts.findByAgent", query = "SELECT h FROM Hosts h WHERE h.agent = :agent")
    , @NamedQuery(name = "Hosts.findByDescription", query = "SELECT h FROM Hosts h WHERE h.description = :description")
    , @NamedQuery(name = "Hosts.findByAddress", query = "SELECT h FROM Hosts h WHERE h.address = :address")
    , @NamedQuery(name = "Hosts.findByPort", query = "SELECT h FROM Hosts h WHERE h.port = :port")})
public class Hosts implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "rec_id")
    private Integer recId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "name")
    private String name;
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
    @Column(name = "cred")
    private String cred;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "sensor")
    private String sensor;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "agent")
    private String agent;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "address")
    private String address;
    @Basic(optional = false)
    @NotNull
    @Column(name = "port")
    private int port;

    public Hosts() {
    }

    public Hosts(Integer recId) {
        this.recId = recId;
    }

    public Hosts(Integer recId, String name, String refId, String node, String cred, String sensor, String agent, String description, String address, int port) {
        this.recId = recId;
        this.name = name;
        this.refId = refId;
        this.node = node;
        this.cred = cred;
        this.sensor = sensor;
        this.agent = agent;
        this.description = description;
        this.address = address;
        this.port = port;
    }

    public Integer getRecId() {
        return recId;
    }

    public void setRecId(Integer recId) {
        this.recId = recId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getCred() {
        return cred;
    }

    public void setCred(String cred) {
        this.cred = cred;
    }

    public String getSensor() {
        return sensor;
    }

    public void setSensor(String sensor) {
        this.sensor = sensor;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
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
        if (!(object instanceof Hosts)) {
            return false;
        }
        Hosts other = (Hosts) object;
        if ((this.recId == null && other.recId != null) || (this.recId != null && !this.recId.equals(other.recId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.alertflex.mc.db.Hosts[ recId=" + recId + " ]";
    }
    
}
