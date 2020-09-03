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
@Table(name = "alert_category")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AlertCategory.findAll", query = "SELECT a FROM AlertCategory a")
    , @NamedQuery(name = "AlertCategory.findByRecId", query = "SELECT a FROM AlertCategory a WHERE a.recId = :recId")
    , @NamedQuery(name = "AlertCategory.findByRefId", query = "SELECT a FROM AlertCategory a WHERE a.refId = :refId")
    , @NamedQuery(name = "AlertCategory.findByCatName", query = "SELECT a FROM AlertCategory a WHERE a.catName = :catName")
    , @NamedQuery(name = "AlertCategory.findByCatSource", query = "SELECT a FROM AlertCategory a WHERE a.catSource = :catSource")
    , @NamedQuery(name = "AlertCategory.findByCatDesc", query = "SELECT a FROM AlertCategory a WHERE a.catDesc = :catDesc")
    , @NamedQuery(name = "AlertCategory.findByMitreTactics", query = "SELECT a FROM AlertCategory a WHERE a.mitreTactics = :mitreTactics")
    , @NamedQuery(name = "AlertCategory.findByMitreTechniques", query = "SELECT a FROM AlertCategory a WHERE a.mitreTechniques = :mitreTechniques")
    , @NamedQuery(name = "AlertCategory.findByMispTaxonomy", query = "SELECT a FROM AlertCategory a WHERE a.mispTaxonomy = :mispTaxonomy")
    , @NamedQuery(name = "AlertCategory.findByMispGalaxy", query = "SELECT a FROM AlertCategory a WHERE a.mispGalaxy = :mispGalaxy")})
public class AlertCategory implements Serializable {

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
    @Column(name = "mitre_tactics")
    private String mitreTactics;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1024)
    @Column(name = "mitre_techniques")
    private String mitreTechniques;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1024)
    @Column(name = "misp_taxonomy")
    private String mispTaxonomy;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1024)
    @Column(name = "misp_galaxy")
    private String mispGalaxy;

    public AlertCategory() {
    }

    public AlertCategory(Integer recId) {
        this.recId = recId;
    }

    public AlertCategory(Integer recId, String refId, String catName, String catSource, String catDesc, String mitreTactics, String mitreTechniques, String mispTaxonomy, String mispGalaxy) {
        this.recId = recId;
        this.refId = refId;
        this.catName = catName;
        this.catSource = catSource;
        this.catDesc = catDesc;
        this.mitreTactics = mitreTactics;
        this.mitreTechniques = mitreTechniques;
        this.mispTaxonomy = mispTaxonomy;
        this.mispGalaxy = mispGalaxy;
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

    public String getMitreTactics() {
        return mitreTactics;
    }

    public void setMitreTactics(String mitreTactics) {
        this.mitreTactics = mitreTactics;
    }

    public String getMitreTechniques() {
        return mitreTechniques;
    }

    public void setMitreTechniques(String mitreTechniques) {
        this.mitreTechniques = mitreTechniques;
    }

    public String getMispTaxonomy() {
        return mispTaxonomy;
    }

    public void setMispTaxonomy(String mispTaxonomy) {
        this.mispTaxonomy = mispTaxonomy;
    }

    public String getMispGalaxy() {
        return mispGalaxy;
    }

    public void setMispGalaxy(String mispGalaxy) {
        this.mispGalaxy = mispGalaxy;
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
        if (!(object instanceof AlertCategory)) {
            return false;
        }
        AlertCategory other = (AlertCategory) object;
        if ((this.recId == null && other.recId != null) || (this.recId != null && !this.recId.equals(other.recId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.alertflex.entity.AlertCategory[ recId=" + recId + " ]";
    }
    
}
