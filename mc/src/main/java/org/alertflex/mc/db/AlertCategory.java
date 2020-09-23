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
@Table(name = "alert_category")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AlertCategory.findAll", query = "SELECT a FROM AlertCategory a")
    , @NamedQuery(name = "AlertCategory.findByRecId", query = "SELECT a FROM AlertCategory a WHERE a.recId = :recId")
    , @NamedQuery(name = "AlertCategory.findByRefId", query = "SELECT a FROM AlertCategory a WHERE a.refId = :refId")
    , @NamedQuery(name = "AlertCategory.findByCatName", query = "SELECT a FROM AlertCategory a WHERE a.catName = :catName")
    , @NamedQuery(name = "AlertCategory.findByCatDesc", query = "SELECT a FROM AlertCategory a WHERE a.catDesc = :catDesc")
    , @NamedQuery(name = "AlertCategory.findByCatSource", query = "SELECT a FROM AlertCategory a WHERE a.catSource = :catSource")
    , @NamedQuery(name = "AlertCategory.findByMispGalaxy", query = "SELECT a FROM AlertCategory a WHERE a.mispGalaxy = :mispGalaxy")
    , @NamedQuery(name = "AlertCategory.findByMispTaxonomy", query = "SELECT a FROM AlertCategory a WHERE a.mispTaxonomy = :mispTaxonomy")
    , @NamedQuery(name = "AlertCategory.findByMitreTactics", query = "SELECT a FROM AlertCategory a WHERE a.mitreTactics = :mitreTactics")
    , @NamedQuery(name = "AlertCategory.findByMitreTechniques", query = "SELECT a FROM AlertCategory a WHERE a.mitreTechniques = :mitreTechniques")})
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
    @Size(min = 1, max = 1024)
    @Column(name = "cat_desc")
    private String catDesc;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "cat_source")
    private String catSource;
    @Size(max = 255)
    @Column(name = "misp_galaxy")
    private String mispGalaxy;
    @Size(max = 255)
    @Column(name = "misp_taxonomy")
    private String mispTaxonomy;
    @Size(max = 255)
    @Column(name = "mitre_tactics")
    private String mitreTactics;
    @Size(max = 255)
    @Column(name = "mitre_techniques")
    private String mitreTechniques;

    public AlertCategory() {
    }

    public AlertCategory(Integer recId) {
        this.recId = recId;
    }

    public AlertCategory(Integer recId, String refId, String catName, String catDesc, String catSource) {
        this.recId = recId;
        this.refId = refId;
        this.catName = catName;
        this.catDesc = catDesc;
        this.catSource = catSource;
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

    public String getCatDesc() {
        return catDesc;
    }

    public void setCatDesc(String catDesc) {
        this.catDesc = catDesc;
    }

    public String getCatSource() {
        return catSource;
    }

    public void setCatSource(String catSource) {
        this.catSource = catSource;
    }

    public String getMispGalaxy() {
        return mispGalaxy;
    }

    public void setMispGalaxy(String mispGalaxy) {
        this.mispGalaxy = mispGalaxy;
    }

    public String getMispTaxonomy() {
        return mispTaxonomy;
    }

    public void setMispTaxonomy(String mispTaxonomy) {
        this.mispTaxonomy = mispTaxonomy;
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
        return "org.alertflex.mc.db.AlertCategory[ recId=" + recId + " ]";
    }
    
}
