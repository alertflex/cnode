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
    , @NamedQuery(name = "AlertPriority.findBySeverityDefault", query = "SELECT a FROM AlertPriority a WHERE a.severityDefault = :severityDefault")
    , @NamedQuery(name = "AlertPriority.findBySeverityThreshold", query = "SELECT a FROM AlertPriority a WHERE a.severityThreshold = :severityThreshold")
    , @NamedQuery(name = "AlertPriority.findByMinorThreshold", query = "SELECT a FROM AlertPriority a WHERE a.minorThreshold = :minorThreshold")
    , @NamedQuery(name = "AlertPriority.findByMajorThreshold", query = "SELECT a FROM AlertPriority a WHERE a.majorThreshold = :majorThreshold")
    , @NamedQuery(name = "AlertPriority.findByCriticalThreshold", query = "SELECT a FROM AlertPriority a WHERE a.criticalThreshold = :criticalThreshold")
    , @NamedQuery(name = "AlertPriority.findByMlInternal", query = "SELECT a FROM AlertPriority a WHERE a.mlInternal = :mlInternal")
    , @NamedQuery(name = "AlertPriority.findByMlExternal", query = "SELECT a FROM AlertPriority a WHERE a.mlExternal = :mlExternal")
    , @NamedQuery(name = "AlertPriority.findByMlThreshold", query = "SELECT a FROM AlertPriority a WHERE a.mlThreshold = :mlThreshold")
    , @NamedQuery(name = "AlertPriority.findByMlResponse", query = "SELECT a FROM AlertPriority a WHERE a.mlResponse = :mlResponse")
    , @NamedQuery(name = "AlertPriority.findByText1", query = "SELECT a FROM AlertPriority a WHERE a.text1 = :text1")
    , @NamedQuery(name = "AlertPriority.findByText2", query = "SELECT a FROM AlertPriority a WHERE a.text2 = :text2")
    , @NamedQuery(name = "AlertPriority.findByText3", query = "SELECT a FROM AlertPriority a WHERE a.text3 = :text3")
    , @NamedQuery(name = "AlertPriority.findByText4", query = "SELECT a FROM AlertPriority a WHERE a.text4 = :text4")
    , @NamedQuery(name = "AlertPriority.findByText5", query = "SELECT a FROM AlertPriority a WHERE a.text5 = :text5")
    , @NamedQuery(name = "AlertPriority.findByValue1", query = "SELECT a FROM AlertPriority a WHERE a.value1 = :value1")
    , @NamedQuery(name = "AlertPriority.findByValue2", query = "SELECT a FROM AlertPriority a WHERE a.value2 = :value2")
    , @NamedQuery(name = "AlertPriority.findByValue3", query = "SELECT a FROM AlertPriority a WHERE a.value3 = :value3")
    , @NamedQuery(name = "AlertPriority.findByValue4", query = "SELECT a FROM AlertPriority a WHERE a.value4 = :value4")
    , @NamedQuery(name = "AlertPriority.findByValue5", query = "SELECT a FROM AlertPriority a WHERE a.value5 = :value5")})
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
    @Column(name = "severity_default")
    private int severityDefault;
    @Basic(optional = false)
    @NotNull
    @Column(name = "severity_threshold")
    private int severityThreshold;
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
    @Column(name = "ml_internal")
    private int mlInternal;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ml_external")
    private int mlExternal;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ml_threshold")
    private int mlThreshold;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "ml_response")
    private String mlResponse;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "text1")
    private String text1;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "text2")
    private String text2;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "text3")
    private String text3;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "text4")
    private String text4;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "text5")
    private String text5;
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

    public AlertPriority() {
    }

    public AlertPriority(Integer recId) {
        this.recId = recId;
    }

    public AlertPriority(Integer recId, String refId, String source, String description, int log, int severityDefault, int severityThreshold, int minorThreshold, int majorThreshold, int criticalThreshold, int mlInternal, int mlExternal, int mlThreshold, String mlResponse, String text1, String text2, String text3, String text4, String text5, int value1, int value2, int value3, int value4, int value5) {
        this.recId = recId;
        this.refId = refId;
        this.source = source;
        this.description = description;
        this.log = log;
        this.severityDefault = severityDefault;
        this.severityThreshold = severityThreshold;
        this.minorThreshold = minorThreshold;
        this.majorThreshold = majorThreshold;
        this.criticalThreshold = criticalThreshold;
        this.mlInternal = mlInternal;
        this.mlExternal = mlExternal;
        this.mlThreshold = mlThreshold;
        this.mlResponse = mlResponse;
        this.text1 = text1;
        this.text2 = text2;
        this.text3 = text3;
        this.text4 = text4;
        this.text5 = text5;
        this.value1 = value1;
        this.value2 = value2;
        this.value3 = value3;
        this.value4 = value4;
        this.value5 = value5;
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

    public int getSeverityDefault() {
        return severityDefault;
    }

    public void setSeverityDefault(int severityDefault) {
        this.severityDefault = severityDefault;
    }

    public int getSeverityThreshold() {
        return severityThreshold;
    }

    public void setSeverityThreshold(int severityThreshold) {
        this.severityThreshold = severityThreshold;
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

    public int getMlInternal() {
        return mlInternal;
    }

    public void setMlInternal(int mlInternal) {
        this.mlInternal = mlInternal;
    }

    public int getMlExternal() {
        return mlExternal;
    }

    public void setMlExternal(int mlExternal) {
        this.mlExternal = mlExternal;
    }

    public int getMlThreshold() {
        return mlThreshold;
    }

    public void setMlThreshold(int mlThreshold) {
        this.mlThreshold = mlThreshold;
    }

    public String getMlResponse() {
        return mlResponse;
    }

    public void setMlResponse(String mlResponse) {
        this.mlResponse = mlResponse;
    }

    public String getText1() {
        return text1;
    }

    public void setText1(String text1) {
        this.text1 = text1;
    }

    public String getText2() {
        return text2;
    }

    public void setText2(String text2) {
        this.text2 = text2;
    }

    public String getText3() {
        return text3;
    }

    public void setText3(String text3) {
        this.text3 = text3;
    }

    public String getText4() {
        return text4;
    }

    public void setText4(String text4) {
        this.text4 = text4;
    }

    public String getText5() {
        return text5;
    }

    public void setText5(String text5) {
        this.text5 = text5;
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
