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
@Table(name = "agent")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Agent.findAll", query = "SELECT a FROM Agent a"),
    @NamedQuery(name = "Agent.findByRecId", query = "SELECT a FROM Agent a WHERE a.recId = :recId"),
    @NamedQuery(name = "Agent.findByRefId", query = "SELECT a FROM Agent a WHERE a.refId = :refId"),
    @NamedQuery(name = "Agent.findByNode", query = "SELECT a FROM Agent a WHERE a.node = :node"),
    @NamedQuery(name = "Agent.findByProbe", query = "SELECT a FROM Agent a WHERE a.probe = :probe"),
    @NamedQuery(name = "Agent.findByAgentId", query = "SELECT a FROM Agent a WHERE a.agentId = :agentId"),
    @NamedQuery(name = "Agent.findByAgentKey", query = "SELECT a FROM Agent a WHERE a.agentKey = :agentKey"),
    @NamedQuery(name = "Agent.findByIp", query = "SELECT a FROM Agent a WHERE a.ip = :ip"),
    @NamedQuery(name = "Agent.findByName", query = "SELECT a FROM Agent a WHERE a.name = :name"),
    @NamedQuery(name = "Agent.findByStatus", query = "SELECT a FROM Agent a WHERE a.status = :status"),
    @NamedQuery(name = "Agent.findByDateAdd", query = "SELECT a FROM Agent a WHERE a.dateAdd = :dateAdd"),
    @NamedQuery(name = "Agent.findByVersion", query = "SELECT a FROM Agent a WHERE a.version = :version"),
    @NamedQuery(name = "Agent.findByOsPlatform", query = "SELECT a FROM Agent a WHERE a.osPlatform = :osPlatform"),
    @NamedQuery(name = "Agent.findByOsVersion", query = "SELECT a FROM Agent a WHERE a.osVersion = :osVersion"),
    @NamedQuery(name = "Agent.findByOsName", query = "SELECT a FROM Agent a WHERE a.osName = :osName"),
    @NamedQuery(name = "Agent.findByDateUpdate", query = "SELECT a FROM Agent a WHERE a.dateUpdate = :dateUpdate")})
public class Agent implements Serializable {

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
    @Size(min = 1, max = 32)
    @Column(name = "agent_id")
    private String agentId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "agent_key")
    private String agentKey;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "ip")
    private String ip;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "status")
    private String status;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "date_add")
    private String dateAdd;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "version")
    private String version;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "os_platform")
    private String osPlatform;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "os_version")
    private String osVersion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "os_name")
    private String osName;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;

    public Agent() {
    }

    public Agent(Integer recId) {
        this.recId = recId;
    }

    public Agent(Integer recId, String refId, String node, String probe, String agentId, String agentKey, String ip, String name, String status, String dateAdd, String version, String osPlatform, String osVersion, String osName) {
        this.recId = recId;
        this.refId = refId;
        this.node = node;
        this.probe = probe;
        this.agentId = agentId;
        this.agentKey = agentKey;
        this.ip = ip;
        this.name = name;
        this.status = status;
        this.dateAdd = dateAdd;
        this.version = version;
        this.osPlatform = osPlatform;
        this.osVersion = osVersion;
        this.osName = osName;
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

    public String getProbe() {
        return probe;
    }

    public void setProbe(String probe) {
        this.probe = probe;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getAgentKey() {
        return agentKey;
    }

    public void setAgentKey(String agentKey) {
        this.agentKey = agentKey;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDateAdd() {
        return dateAdd;
    }

    public void setDateAdd(String dateAdd) {
        this.dateAdd = dateAdd;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getOsPlatform() {
        return osPlatform;
    }

    public void setOsPlatform(String osPlatform) {
        this.osPlatform = osPlatform;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getOsName() {
        return osName;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
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
        if (!(object instanceof Agent)) {
            return false;
        }
        Agent other = (Agent) object;
        if ((this.recId == null && other.recId != null) || (this.recId != null && !this.recId.equals(other.recId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.alertflex.entity.Agent[ recId=" + recId + " ]";
    }
    
}
