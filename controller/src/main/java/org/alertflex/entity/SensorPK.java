/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alertflex.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author root
 */
@Embeddable
public class SensorPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "ref_id")
    private String refId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "master_node")
    private String masterNode;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "name")
    private String name;

    public SensorPK() {
    }

    public SensorPK(String refId, String masterNode, String name) {
        this.refId = refId;
        this.masterNode = masterNode;
        this.name = name;
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public String getMasterNode() {
        return masterNode;
    }

    public void setMasterNode(String masterNode) {
        this.masterNode = masterNode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (refId != null ? refId.hashCode() : 0);
        hash += (masterNode != null ? masterNode.hashCode() : 0);
        hash += (name != null ? name.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SensorPK)) {
            return false;
        }
        SensorPK other = (SensorPK) object;
        if ((this.refId == null && other.refId != null) || (this.refId != null && !this.refId.equals(other.refId))) {
            return false;
        }
        if ((this.masterNode == null && other.masterNode != null) || (this.masterNode != null && !this.masterNode.equals(other.masterNode))) {
            return false;
        }
        if ((this.name == null && other.name != null) || (this.name != null && !this.name.equals(other.name))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.alertflex.entity.SensorPK[ refId=" + refId + ", masterNode=" + masterNode + ", name=" + name + " ]";
    }
    
}
