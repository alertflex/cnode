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
@Table(name = "cat_profile")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CatProfile.findAll", query = "SELECT c FROM CatProfile c")
    , @NamedQuery(name = "CatProfile.findByCpId", query = "SELECT c FROM CatProfile c WHERE c.cpId = :cpId")
    , @NamedQuery(name = "CatProfile.findByRefId", query = "SELECT c FROM CatProfile c WHERE c.refId = :refId")
    , @NamedQuery(name = "CatProfile.findByCpName", query = "SELECT c FROM CatProfile c WHERE c.cpName = :cpName")
    , @NamedQuery(name = "CatProfile.findByCpSource", query = "SELECT c FROM CatProfile c WHERE c.cpSource = :cpSource")
    , @NamedQuery(name = "CatProfile.findByCatName", query = "SELECT c FROM CatProfile c WHERE c.catName = :catName")})
public class CatProfile implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "cp_desc")
    private String cpDesc;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "cat_names")
    private String catNames;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cp_id")
    private Integer cpId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "ref_id")
    private String refId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "cp_name")
    private String cpName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "cp_source")
    private String cpSource;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "cat_name")
    private String catName;

    public CatProfile() {
    }

    public CatProfile(Integer cpId) {
        this.cpId = cpId;
    }

    public CatProfile(Integer cpId, String refId, String cpName, String cpSource, String catName) {
        this.cpId = cpId;
        this.refId = refId;
        this.cpName = cpName;
        this.cpSource = cpSource;
        this.catName = catName;
    }

    public Integer getCpId() {
        return cpId;
    }

    public void setCpId(Integer cpId) {
        this.cpId = cpId;
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public String getCpName() {
        return cpName;
    }

    public void setCpName(String cpName) {
        this.cpName = cpName;
    }

    public String getCpSource() {
        return cpSource;
    }

    public void setCpSource(String cpSource) {
        this.cpSource = cpSource;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cpId != null ? cpId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CatProfile)) {
            return false;
        }
        CatProfile other = (CatProfile) object;
        if ((this.cpId == null && other.cpId != null) || (this.cpId != null && !this.cpId.equals(other.cpId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.alertflex.entity.CatProfile[ cpId=" + cpId + " ]";
    }

    public String getCpDesc() {
        return cpDesc;
    }

    public void setCpDesc(String cpDesc) {
        this.cpDesc = cpDesc;
    }

    public String getCatNames() {
        return catNames;
    }

    public void setCatNames(String catNames) {
        this.catNames = catNames;
    }
    
}
