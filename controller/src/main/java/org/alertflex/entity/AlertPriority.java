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
    , @NamedQuery(name = "AlertPriority.findByLog", query = "SELECT a FROM AlertPriority a WHERE a.log = :log")
    , @NamedQuery(name = "AlertPriority.findByAlertThreshold", query = "SELECT a FROM AlertPriority a WHERE a.alertThreshold = :alertThreshold")
    , @NamedQuery(name = "AlertPriority.findByMinorThreshold", query = "SELECT a FROM AlertPriority a WHERE a.minorThreshold = :minorThreshold")
    , @NamedQuery(name = "AlertPriority.findByMajorThreshold", query = "SELECT a FROM AlertPriority a WHERE a.majorThreshold = :majorThreshold")
    , @NamedQuery(name = "AlertPriority.findByCriticalThreshold", query = "SELECT a FROM AlertPriority a WHERE a.criticalThreshold = :criticalThreshold")
    , @NamedQuery(name = "AlertPriority.findBySeverity1", query = "SELECT a FROM AlertPriority a WHERE a.severity1 = :severity1")
    , @NamedQuery(name = "AlertPriority.findBySeverity2", query = "SELECT a FROM AlertPriority a WHERE a.severity2 = :severity2")
    , @NamedQuery(name = "AlertPriority.findBySeverity3", query = "SELECT a FROM AlertPriority a WHERE a.severity3 = :severity3")
    , @NamedQuery(name = "AlertPriority.findBySeverity4", query = "SELECT a FROM AlertPriority a WHERE a.severity4 = :severity4")
    , @NamedQuery(name = "AlertPriority.findBySeverity5", query = "SELECT a FROM AlertPriority a WHERE a.severity5 = :severity5")
    , @NamedQuery(name = "AlertPriority.findBySeverity6", query = "SELECT a FROM AlertPriority a WHERE a.severity6 = :severity6")
    , @NamedQuery(name = "AlertPriority.findBySeverity7", query = "SELECT a FROM AlertPriority a WHERE a.severity7 = :severity7")
    , @NamedQuery(name = "AlertPriority.findByValue1", query = "SELECT a FROM AlertPriority a WHERE a.value1 = :value1")
    , @NamedQuery(name = "AlertPriority.findByValue2", query = "SELECT a FROM AlertPriority a WHERE a.value2 = :value2")
    , @NamedQuery(name = "AlertPriority.findByValue3", query = "SELECT a FROM AlertPriority a WHERE a.value3 = :value3")
    , @NamedQuery(name = "AlertPriority.findByValue4", query = "SELECT a FROM AlertPriority a WHERE a.value4 = :value4")
    , @NamedQuery(name = "AlertPriority.findByValue5", query = "SELECT a FROM AlertPriority a WHERE a.value5 = :value5")
    , @NamedQuery(name = "AlertPriority.findByValue6", query = "SELECT a FROM AlertPriority a WHERE a.value6 = :value6")
    , @NamedQuery(name = "AlertPriority.findByValue7", query = "SELECT a FROM AlertPriority a WHERE a.value7 = :value7")})
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
    @Column(name = "log")
    private int log;
    @Basic(optional = false)
    @NotNull
    @Column(name = "alert_threshold")
    private int alertThreshold;
    @Basic(optional = false)
    @NotNull
    @Column(name = "minor_threshold")
    private int minorThreshold;
    @Basic(optional = false)
    @NotNull
    @Column(name = "major_threshold")
    private int majorThreshold;
    @Basic(optional = false)
    @NotNull
    @Column(name = "critical_threshold")
    private int criticalThreshold;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "severity1")
    private String severity1;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "severity2")
    private String severity2;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "severity3")
    private String severity3;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "severity4")
    private String severity4;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "severity5")
    private String severity5;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "severity6")
    private String severity6;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "severity7")
    private String severity7;
    @Basic(optional = false)
    @NotNull
    @Column(name = "value1")
    private int value1;
    @Basic(optional = false)
    @NotNull
    @Column(name = "value2")
    private int value2;
    @Basic(optional = false)
    @NotNull
    @Column(name = "value3")
    private int value3;
    @Basic(optional = false)
    @NotNull
    @Column(name = "value4")
    private int value4;
    @Basic(optional = false)
    @NotNull
    @Column(name = "value5")
    private int value5;
    @Basic(optional = false)
    @NotNull
    @Column(name = "value6")
    private int value6;
    @Basic(optional = false)
    @NotNull
    @Column(name = "value7")
    private int value7;

    public AlertPriority() {
    }

    public AlertPriority(Integer recId) {
        this.recId = recId;
    }

    public AlertPriority(Integer recId, String refId, String source, String description, int log, int alertThreshold, int minorThreshold, int majorThreshold, int criticalThreshold, String severity1, String severity2, String severity3, String severity4, String severity5, String severity6, String severity7, int value1, int value2, int value3, int value4, int value5, int value6, int value7) {
        this.recId = recId;
        this.refId = refId;
        this.source = source;
        this.description = description;
        this.log = log;
        this.alertThreshold = alertThreshold;
        this.minorThreshold = minorThreshold;
        this.majorThreshold = majorThreshold;
        this.criticalThreshold = criticalThreshold;
        this.severity1 = severity1;
        this.severity2 = severity2;
        this.severity3 = severity3;
        this.severity4 = severity4;
        this.severity5 = severity5;
        this.severity6 = severity6;
        this.severity7 = severity7;
        this.value1 = value1;
        this.value2 = value2;
        this.value3 = value3;
        this.value4 = value4;
        this.value5 = value5;
        this.value6 = value6;
        this.value7 = value7;
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

    public int getLog() {
        return log;
    }

    public void setLog(int log) {
        this.log = log;
    }

    public int getAlertThreshold() {
        return alertThreshold;
    }

    public void setAlertThreshold(int alertThreshold) {
        this.alertThreshold = alertThreshold;
    }

    public int getMinorThreshold() {
        return minorThreshold;
    }

    public void setMinorThreshold(int minorThreshold) {
        this.minorThreshold = minorThreshold;
    }

    public int getMajorThreshold() {
        return majorThreshold;
    }

    public void setMajorThreshold(int majorThreshold) {
        this.majorThreshold = majorThreshold;
    }

    public int getCriticalThreshold() {
        return criticalThreshold;
    }

    public void setCriticalThreshold(int criticalThreshold) {
        this.criticalThreshold = criticalThreshold;
    }

    public String getSeverity1() {
        return severity1;
    }

    public void setSeverity1(String severity1) {
        this.severity1 = severity1;
    }

    public String getSeverity2() {
        return severity2;
    }

    public void setSeverity2(String severity2) {
        this.severity2 = severity2;
    }

    public String getSeverity3() {
        return severity3;
    }

    public void setSeverity3(String severity3) {
        this.severity3 = severity3;
    }

    public String getSeverity4() {
        return severity4;
    }

    public void setSeverity4(String severity4) {
        this.severity4 = severity4;
    }

    public String getSeverity5() {
        return severity5;
    }

    public void setSeverity5(String severity5) {
        this.severity5 = severity5;
    }

    public String getSeverity6() {
        return severity6;
    }

    public void setSeverity6(String severity6) {
        this.severity6 = severity6;
    }

    public String getSeverity7() {
        return severity7;
    }

    public void setSeverity7(String severity7) {
        this.severity7 = severity7;
    }

    public int getValue1() {
        return value1;
    }

    public void setValue1(int value1) {
        this.value1 = value1;
    }

    public int getValue2() {
        return value2;
    }

    public void setValue2(int value2) {
        this.value2 = value2;
    }

    public int getValue3() {
        return value3;
    }

    public void setValue3(int value3) {
        this.value3 = value3;
    }

    public int getValue4() {
        return value4;
    }

    public void setValue4(int value4) {
        this.value4 = value4;
    }

    public int getValue5() {
        return value5;
    }

    public void setValue5(int value5) {
        this.value5 = value5;
    }

    public int getValue6() {
        return value6;
    }

    public void setValue6(int value6) {
        this.value6 = value6;
    }

    public int getValue7() {
        return value7;
    }

    public void setValue7(int value7) {
        this.value7 = value7;
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
