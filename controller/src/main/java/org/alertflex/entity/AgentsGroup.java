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
@Table(name = "agents_group")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AgentsGroup.findAll", query = "SELECT a FROM AgentsGroup a"),
    @NamedQuery(name = "AgentsGroup.findByRecId", query = "SELECT a FROM AgentsGroup a WHERE a.recId = :recId"),
    @NamedQuery(name = "AgentsGroup.findByRefId", query = "SELECT a FROM AgentsGroup a WHERE a.refId = :refId"),
    @NamedQuery(name = "AgentsGroup.findByNode", query = "SELECT a FROM AgentsGroup a WHERE a.node = :node"),
    @NamedQuery(name = "AgentsGroup.findByGroupName", query = "SELECT a FROM AgentsGroup a WHERE a.groupName = :groupName"),
    @NamedQuery(name = "AgentsGroup.findByGroupRef", query = "SELECT a FROM AgentsGroup a WHERE a.groupRef = :groupRef"),
    @NamedQuery(name = "AgentsGroup.findByAgentsCount", query = "SELECT a FROM AgentsGroup a WHERE a.agentsCount = :agentsCount"),
    @NamedQuery(name = "AgentsGroup.findByDateAdd", query = "SELECT a FROM AgentsGroup a WHERE a.dateAdd = :dateAdd"),
    @NamedQuery(name = "AgentsGroup.findByDateUpdate", query = "SELECT a FROM AgentsGroup a WHERE a.dateUpdate = :dateUpdate")})
public class AgentsGroup implements Serializable {

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
    @Column(name = "group_name")
    private String groupName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "group_ref")
    private String groupRef;
    @Basic(optional = false)
    @NotNull
    @Column(name = "agents_count")
    private int agentsCount;
    @Column(name = "date_add")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateAdd;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;

    public AgentsGroup() {
    }

    public AgentsGroup(Integer recId) {
        this.recId = recId;
    }

    public AgentsGroup(Integer recId, String refId, String node, String groupName, String groupRef, int agentsCount) {
        this.recId = recId;
        this.refId = refId;
        this.node = node;
        this.groupName = groupName;
        this.groupRef = groupRef;
        this.agentsCount = agentsCount;
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

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupRef() {
        return groupRef;
    }

    public void setGroupRef(String groupRef) {
        this.groupRef = groupRef;
    }

    public int getAgentsCount() {
        return agentsCount;
    }

    public void setAgentsCount(int agentsCount) {
        this.agentsCount = agentsCount;
    }

    public Date getDateAdd() {
        return dateAdd;
    }

    public void setDateAdd(Date dateAdd) {
        this.dateAdd = dateAdd;
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
        if (!(object instanceof AgentsGroup)) {
            return false;
        }
        AgentsGroup other = (AgentsGroup) object;
        if ((this.recId == null && other.recId != null) || (this.recId != null && !this.recId.equals(other.recId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.alertflex.entity.AgentsGroup[ recId=" + recId + " ]";
    }
    
}
