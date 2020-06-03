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
@Table(name = "alerts_severity")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AlertsSeverity.findAll", query = "SELECT a FROM AlertsSeverity a")
    , @NamedQuery(name = "AlertsSeverity.findByRecId", query = "SELECT a FROM AlertsSeverity a WHERE a.recId = :recId")
    , @NamedQuery(name = "AlertsSeverity.findByRefId", query = "SELECT a FROM AlertsSeverity a WHERE a.refId = :refId")
    , @NamedQuery(name = "AlertsSeverity.findBySource", query = "SELECT a FROM AlertsSeverity a WHERE a.source = :source")
    , @NamedQuery(name = "AlertsSeverity.findByDescription", query = "SELECT a FROM AlertsSeverity a WHERE a.description = :description")
    , @NamedQuery(name = "AlertsSeverity.findByMinor", query = "SELECT a FROM AlertsSeverity a WHERE a.minor = :minor")
    , @NamedQuery(name = "AlertsSeverity.findByMajor", query = "SELECT a FROM AlertsSeverity a WHERE a.major = :major")
    , @NamedQuery(name = "AlertsSeverity.findByCritical", query = "SELECT a FROM AlertsSeverity a WHERE a.critical = :critical")})
public class AlertsSeverity implements Serializable {

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
    @Column(name = "source")
    private String source;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1024)
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @NotNull
    @Column(name = "minor")
    private int minor;
    @Basic(optional = false)
    @NotNull
    @Column(name = "major")
    private int major;
    @Basic(optional = false)
    @NotNull
    @Column(name = "critical")
    private int critical;

    public AlertsSeverity() {
    }

    public AlertsSeverity(Integer recId) {
        this.recId = recId;
    }

    public AlertsSeverity(Integer recId, String refId, String source, String description, int minor, int major, int critical) {
        this.recId = recId;
        this.refId = refId;
        this.source = source;
        this.description = description;
        this.minor = minor;
        this.major = major;
        this.critical = critical;
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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getMinor() {
        return minor;
    }

    public void setMinor(int minor) {
        this.minor = minor;
    }

    public int getMajor() {
        return major;
    }

    public void setMajor(int major) {
        this.major = major;
    }

    public int getCritical() {
        return critical;
    }

    public void setCritical(int critical) {
        this.critical = critical;
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
        if (!(object instanceof AlertsSeverity)) {
            return false;
        }
        AlertsSeverity other = (AlertsSeverity) object;
        if ((this.recId == null && other.recId != null) || (this.recId != null && !this.recId.equals(other.recId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.alertflex.entity.AlertsSeverity[ recId=" + recId + " ]";
    }
    
}
