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
@Table(name = "alerts_category")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AlertsCategory.findAll", query = "SELECT a FROM AlertsCategory a")
    , @NamedQuery(name = "AlertsCategory.findByRecId", query = "SELECT a FROM AlertsCategory a WHERE a.recId = :recId")
    , @NamedQuery(name = "AlertsCategory.findByRefId", query = "SELECT a FROM AlertsCategory a WHERE a.refId = :refId")
    , @NamedQuery(name = "AlertsCategory.findByCatName", query = "SELECT a FROM AlertsCategory a WHERE a.catName = :catName")
    , @NamedQuery(name = "AlertsCategory.findByCatSource", query = "SELECT a FROM AlertsCategory a WHERE a.catSource = :catSource")
    , @NamedQuery(name = "AlertsCategory.findByCatDesc", query = "SELECT a FROM AlertsCategory a WHERE a.catDesc = :catDesc")
    , @NamedQuery(name = "AlertsCategory.findByTaxonomy", query = "SELECT a FROM AlertsCategory a WHERE a.taxonomy = :taxonomy")
    , @NamedQuery(name = "AlertsCategory.findByGalaxy", query = "SELECT a FROM AlertsCategory a WHERE a.galaxy = :galaxy")})
public class AlertsCategory implements Serializable {

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
    @Size(min = 1, max = 512)
    @Column(name = "cat_name")
    private String catName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "cat_source")
    private String catSource;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1024)
    @Column(name = "cat_desc")
    private String catDesc;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1024)
    @Column(name = "taxonomy")
    private String taxonomy;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1024)
    @Column(name = "galaxy")
    private String galaxy;

    public AlertsCategory() {
    }

    public AlertsCategory(Integer recId) {
        this.recId = recId;
    }

    public AlertsCategory(Integer recId, String refId, String catName, String catSource, String catDesc, String taxonomy, String galaxy) {
        this.recId = recId;
        this.refId = refId;
        this.catName = catName;
        this.catSource = catSource;
        this.catDesc = catDesc;
        this.taxonomy = taxonomy;
        this.galaxy = galaxy;
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

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getCatSource() {
        return catSource;
    }

    public void setCatSource(String catSource) {
        this.catSource = catSource;
    }

    public String getCatDesc() {
        return catDesc;
    }

    public void setCatDesc(String catDesc) {
        this.catDesc = catDesc;
    }

    public String getTaxonomy() {
        return taxonomy;
    }

    public void setTaxonomy(String taxonomy) {
        this.taxonomy = taxonomy;
    }

    public String getGalaxy() {
        return galaxy;
    }

    public void setGalaxy(String galaxy) {
        this.galaxy = galaxy;
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
        if (!(object instanceof AlertsCategory)) {
            return false;
        }
        AlertsCategory other = (AlertsCategory) object;
        if ((this.recId == null && other.recId != null) || (this.recId != null && !this.recId.equals(other.recId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.alertflex.entity.AlertsCategory[ recId=" + recId + " ]";
    }
    
}
