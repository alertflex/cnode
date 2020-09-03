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
@Table(name = "alert_priority")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AlertPriority.findAll", query = "SELECT a FROM AlertPriority a")
    , @NamedQuery(name = "AlertPriority.findByRecId", query = "SELECT a FROM AlertPriority a WHERE a.recId = :recId")
    , @NamedQuery(name = "AlertPriority.findByRefId", query = "SELECT a FROM AlertPriority a WHERE a.refId = :refId")
    , @NamedQuery(name = "AlertPriority.findBySource", query = "SELECT a FROM AlertPriority a WHERE a.source = :source")
    , @NamedQuery(name = "AlertPriority.findByDescription", query = "SELECT a FROM AlertPriority a WHERE a.description = :description")
    , @NamedQuery(name = "AlertPriority.findByMinor", query = "SELECT a FROM AlertPriority a WHERE a.minor = :minor")
    , @NamedQuery(name = "AlertPriority.findByMajor", query = "SELECT a FROM AlertPriority a WHERE a.major = :major")
    , @NamedQuery(name = "AlertPriority.findByCritical", query = "SELECT a FROM AlertPriority a WHERE a.critical = :critical")
    , @NamedQuery(name = "AlertPriority.findByPriority1", query = "SELECT a FROM AlertPriority a WHERE a.priority1 = :priority1")
    , @NamedQuery(name = "AlertPriority.findByPriority2", query = "SELECT a FROM AlertPriority a WHERE a.priority2 = :priority2")
    , @NamedQuery(name = "AlertPriority.findByPriority3", query = "SELECT a FROM AlertPriority a WHERE a.priority3 = :priority3")
    , @NamedQuery(name = "AlertPriority.findByPriority4", query = "SELECT a FROM AlertPriority a WHERE a.priority4 = :priority4")
    , @NamedQuery(name = "AlertPriority.findByPriority5", query = "SELECT a FROM AlertPriority a WHERE a.priority5 = :priority5")
    , @NamedQuery(name = "AlertPriority.findByPriority6", query = "SELECT a FROM AlertPriority a WHERE a.priority6 = :priority6")
    , @NamedQuery(name = "AlertPriority.findByPriority7", query = "SELECT a FROM AlertPriority a WHERE a.priority7 = :priority7")})
public class AlertPriority implements Serializable {

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
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "priority1")
    private String priority1;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "priority2")
    private String priority2;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "priority3")
    private String priority3;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "priority4")
    private String priority4;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "priority5")
    private String priority5;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "priority6")
    private String priority6;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "priority7")
    private String priority7;

    public AlertPriority() {
    }

    public AlertPriority(Integer recId) {
        this.recId = recId;
    }

    public AlertPriority(Integer recId, String refId, String source, String description, int minor, int major, int critical, String priority1, String priority2, String priority3, String priority4, String priority5, String priority6, String priority7) {
        this.recId = recId;
        this.refId = refId;
        this.source = source;
        this.description = description;
        this.minor = minor;
        this.major = major;
        this.critical = critical;
        this.priority1 = priority1;
        this.priority2 = priority2;
        this.priority3 = priority3;
        this.priority4 = priority4;
        this.priority5 = priority5;
        this.priority6 = priority6;
        this.priority7 = priority7;
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

    public String getPriority1() {
        return priority1;
    }

    public void setPriority1(String priority1) {
        this.priority1 = priority1;
    }

    public String getPriority2() {
        return priority2;
    }

    public void setPriority2(String priority2) {
        this.priority2 = priority2;
    }

    public String getPriority3() {
        return priority3;
    }

    public void setPriority3(String priority3) {
        this.priority3 = priority3;
    }

    public String getPriority4() {
        return priority4;
    }

    public void setPriority4(String priority4) {
        this.priority4 = priority4;
    }

    public String getPriority5() {
        return priority5;
    }

    public void setPriority5(String priority5) {
        this.priority5 = priority5;
    }

    public String getPriority6() {
        return priority6;
    }

    public void setPriority6(String priority6) {
        this.priority6 = priority6;
    }

    public String getPriority7() {
        return priority7;
    }

    public void setPriority7(String priority7) {
        this.priority7 = priority7;
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
        if (!(object instanceof AlertPriority)) {
            return false;
        }
        AlertPriority other = (AlertPriority) object;
        if ((this.recId == null && other.recId != null) || (this.recId != null && !this.recId.equals(other.recId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.alertflex.entity.AlertPriority[ recId=" + recId + " ]";
    }
    
}
