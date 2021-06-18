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
@Table(name = "bwlist_packages")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "BwlistPackages.findAll", query = "SELECT b FROM BwlistPackages b"),
    @NamedQuery(name = "BwlistPackages.findByRecId", query = "SELECT b FROM BwlistPackages b WHERE b.recId = :recId"),
    @NamedQuery(name = "BwlistPackages.findByNodeId", query = "SELECT b FROM BwlistPackages b WHERE b.nodeId = :nodeId"),
    @NamedQuery(name = "BwlistPackages.findByRefId", query = "SELECT b FROM BwlistPackages b WHERE b.refId = :refId"),
    @NamedQuery(name = "BwlistPackages.findByAgent", query = "SELECT b FROM BwlistPackages b WHERE b.agent = :agent"),
    @NamedQuery(name = "BwlistPackages.findByPackageName", query = "SELECT b FROM BwlistPackages b WHERE b.packageName = :packageName"),
    @NamedQuery(name = "BwlistPackages.findByListType", query = "SELECT b FROM BwlistPackages b WHERE b.listType = :listType"),
    @NamedQuery(name = "BwlistPackages.findByDateAdd", query = "SELECT b FROM BwlistPackages b WHERE b.dateAdd = :dateAdd")})
public class BwlistPackages implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "rec_id")
    private Long recId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "node_id")
    private String nodeId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "ref_id")
    private String refId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "agent")
    private String agent;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "package_name")
    private String packageName;
    @Basic(optional = false)
    @NotNull
    @Column(name = "list_type")
    private int listType;
    @Column(name = "date_add")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateAdd;

    public BwlistPackages() {
    }

    public BwlistPackages(Long recId) {
        this.recId = recId;
    }

    public BwlistPackages(Long recId, String nodeId, String refId, String agent, String packageName, int listType) {
        this.recId = recId;
        this.nodeId = nodeId;
        this.refId = refId;
        this.agent = agent;
        this.packageName = packageName;
        this.listType = listType;
    }

    public Long getRecId() {
        return recId;
    }

    public void setRecId(Long recId) {
        this.recId = recId;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public int getListType() {
        return listType;
    }

    public void setListType(int listType) {
        this.listType = listType;
    }

    public Date getDateAdd() {
        return dateAdd;
    }

    public void setDateAdd(Date dateAdd) {
        this.dateAdd = dateAdd;
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
        if (!(object instanceof BwlistPackages)) {
            return false;
        }
        BwlistPackages other = (BwlistPackages) object;
        if ((this.recId == null && other.recId != null) || (this.recId != null && !this.recId.equals(other.recId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.alertflex.entity.BwlistPackages[ recId=" + recId + " ]";
    }
    
}
