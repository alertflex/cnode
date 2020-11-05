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
@Table(name = "node_alerts")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NodeAlerts.findAll", query = "SELECT n FROM NodeAlerts n")
    , @NamedQuery(name = "NodeAlerts.findByRecId", query = "SELECT n FROM NodeAlerts n WHERE n.recId = :recId")
    , @NamedQuery(name = "NodeAlerts.findByNodeId", query = "SELECT n FROM NodeAlerts n WHERE n.nodeId = :nodeId")
    , @NamedQuery(name = "NodeAlerts.findByRefId", query = "SELECT n FROM NodeAlerts n WHERE n.refId = :refId")
    , @NamedQuery(name = "NodeAlerts.findByCrsAgg", query = "SELECT n FROM NodeAlerts n WHERE n.crsAgg = :crsAgg")
    , @NamedQuery(name = "NodeAlerts.findByCrsFilter", query = "SELECT n FROM NodeAlerts n WHERE n.crsFilter = :crsFilter")
    , @NamedQuery(name = "NodeAlerts.findByCrsS0", query = "SELECT n FROM NodeAlerts n WHERE n.crsS0 = :crsS0")
    , @NamedQuery(name = "NodeAlerts.findByCrsS1", query = "SELECT n FROM NodeAlerts n WHERE n.crsS1 = :crsS1")
    , @NamedQuery(name = "NodeAlerts.findByCrsS2", query = "SELECT n FROM NodeAlerts n WHERE n.crsS2 = :crsS2")
    , @NamedQuery(name = "NodeAlerts.findByCrsS3", query = "SELECT n FROM NodeAlerts n WHERE n.crsS3 = :crsS3")
    , @NamedQuery(name = "NodeAlerts.findByHidsAgg", query = "SELECT n FROM NodeAlerts n WHERE n.hidsAgg = :hidsAgg")
    , @NamedQuery(name = "NodeAlerts.findByHidsFilter", query = "SELECT n FROM NodeAlerts n WHERE n.hidsFilter = :hidsFilter")
    , @NamedQuery(name = "NodeAlerts.findByHidsS0", query = "SELECT n FROM NodeAlerts n WHERE n.hidsS0 = :hidsS0")
    , @NamedQuery(name = "NodeAlerts.findByHidsS1", query = "SELECT n FROM NodeAlerts n WHERE n.hidsS1 = :hidsS1")
    , @NamedQuery(name = "NodeAlerts.findByHidsS2", query = "SELECT n FROM NodeAlerts n WHERE n.hidsS2 = :hidsS2")
    , @NamedQuery(name = "NodeAlerts.findByHidsS3", query = "SELECT n FROM NodeAlerts n WHERE n.hidsS3 = :hidsS3")
    , @NamedQuery(name = "NodeAlerts.findByNidsAgg", query = "SELECT n FROM NodeAlerts n WHERE n.nidsAgg = :nidsAgg")
    , @NamedQuery(name = "NodeAlerts.findByNidsFilter", query = "SELECT n FROM NodeAlerts n WHERE n.nidsFilter = :nidsFilter")
    , @NamedQuery(name = "NodeAlerts.findByNidsS0", query = "SELECT n FROM NodeAlerts n WHERE n.nidsS0 = :nidsS0")
    , @NamedQuery(name = "NodeAlerts.findByNidsS1", query = "SELECT n FROM NodeAlerts n WHERE n.nidsS1 = :nidsS1")
    , @NamedQuery(name = "NodeAlerts.findByNidsS2", query = "SELECT n FROM NodeAlerts n WHERE n.nidsS2 = :nidsS2")
    , @NamedQuery(name = "NodeAlerts.findByNidsS3", query = "SELECT n FROM NodeAlerts n WHERE n.nidsS3 = :nidsS3")
    , @NamedQuery(name = "NodeAlerts.findByWafAgg", query = "SELECT n FROM NodeAlerts n WHERE n.wafAgg = :wafAgg")
    , @NamedQuery(name = "NodeAlerts.findByWafFilter", query = "SELECT n FROM NodeAlerts n WHERE n.wafFilter = :wafFilter")
    , @NamedQuery(name = "NodeAlerts.findByWafS0", query = "SELECT n FROM NodeAlerts n WHERE n.wafS0 = :wafS0")
    , @NamedQuery(name = "NodeAlerts.findByWafS1", query = "SELECT n FROM NodeAlerts n WHERE n.wafS1 = :wafS1")
    , @NamedQuery(name = "NodeAlerts.findByWafS2", query = "SELECT n FROM NodeAlerts n WHERE n.wafS2 = :wafS2")
    , @NamedQuery(name = "NodeAlerts.findByWafS3", query = "SELECT n FROM NodeAlerts n WHERE n.wafS3 = :wafS3")
    , @NamedQuery(name = "NodeAlerts.findByTimeOfSurvey", query = "SELECT n FROM NodeAlerts n WHERE n.timeOfSurvey = :timeOfSurvey")})
public class NodeAlerts implements Serializable {

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
    @Column(name = "crs_agg")
    private long crsAgg;
    @Basic(optional = false)
    @NotNull
    @Column(name = "crs_filter")
    private long crsFilter;
    @Basic(optional = false)
    @NotNull
    @Column(name = "crs_s0")
    private long crsS0;
    @Basic(optional = false)
    @NotNull
    @Column(name = "crs_s1")
    private long crsS1;
    @Basic(optional = false)
    @NotNull
    @Column(name = "crs_s2")
    private long crsS2;
    @Basic(optional = false)
    @NotNull
    @Column(name = "crs_s3")
    private long crsS3;
    @Basic(optional = false)
    @NotNull
    @Column(name = "hids_agg")
    private long hidsAgg;
    @Basic(optional = false)
    @NotNull
    @Column(name = "hids_filter")
    private long hidsFilter;
    @Basic(optional = false)
    @NotNull
    @Column(name = "hids_s0")
    private long hidsS0;
    @Basic(optional = false)
    @NotNull
    @Column(name = "hids_s1")
    private long hidsS1;
    @Basic(optional = false)
    @NotNull
    @Column(name = "hids_s2")
    private long hidsS2;
    @Basic(optional = false)
    @NotNull
    @Column(name = "hids_s3")
    private long hidsS3;
    @Basic(optional = false)
    @NotNull
    @Column(name = "nids_agg")
    private long nidsAgg;
    @Basic(optional = false)
    @NotNull
    @Column(name = "nids_filter")
    private long nidsFilter;
    @Basic(optional = false)
    @NotNull
    @Column(name = "nids_s0")
    private long nidsS0;
    @Basic(optional = false)
    @NotNull
    @Column(name = "nids_s1")
    private long nidsS1;
    @Basic(optional = false)
    @NotNull
    @Column(name = "nids_s2")
    private long nidsS2;
    @Basic(optional = false)
    @NotNull
    @Column(name = "nids_s3")
    private long nidsS3;
    @Basic(optional = false)
    @NotNull
    @Column(name = "waf_agg")
    private long wafAgg;
    @Basic(optional = false)
    @NotNull
    @Column(name = "waf_filter")
    private long wafFilter;
    @Basic(optional = false)
    @NotNull
    @Column(name = "waf_s0")
    private long wafS0;
    @Basic(optional = false)
    @NotNull
    @Column(name = "waf_s1")
    private long wafS1;
    @Basic(optional = false)
    @NotNull
    @Column(name = "waf_s2")
    private long wafS2;
    @Basic(optional = false)
    @NotNull
    @Column(name = "waf_s3")
    private long wafS3;
    @Column(name = "time_of_survey")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeOfSurvey;

    public NodeAlerts() {
    }

    public NodeAlerts(Long recId) {
        this.recId = recId;
    }

    public NodeAlerts(Long recId, String nodeId, String refId, long crsAgg, long crsFilter, long crsS0, long crsS1, long crsS2, long crsS3, long hidsAgg, long hidsFilter, long hidsS0, long hidsS1, long hidsS2, long hidsS3, long nidsAgg, long nidsFilter, long nidsS0, long nidsS1, long nidsS2, long nidsS3, long wafAgg, long wafFilter, long wafS0, long wafS1, long wafS2, long wafS3) {
        this.recId = recId;
        this.nodeId = nodeId;
        this.refId = refId;
        this.crsAgg = crsAgg;
        this.crsFilter = crsFilter;
        this.crsS0 = crsS0;
        this.crsS1 = crsS1;
        this.crsS2 = crsS2;
        this.crsS3 = crsS3;
        this.hidsAgg = hidsAgg;
        this.hidsFilter = hidsFilter;
        this.hidsS0 = hidsS0;
        this.hidsS1 = hidsS1;
        this.hidsS2 = hidsS2;
        this.hidsS3 = hidsS3;
        this.nidsAgg = nidsAgg;
        this.nidsFilter = nidsFilter;
        this.nidsS0 = nidsS0;
        this.nidsS1 = nidsS1;
        this.nidsS2 = nidsS2;
        this.nidsS3 = nidsS3;
        this.wafAgg = wafAgg;
        this.wafFilter = wafFilter;
        this.wafS0 = wafS0;
        this.wafS1 = wafS1;
        this.wafS2 = wafS2;
        this.wafS3 = wafS3;
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

    public long getCrsAgg() {
        return crsAgg;
    }

    public void setCrsAgg(long crsAgg) {
        this.crsAgg = crsAgg;
    }

    public long getCrsFilter() {
        return crsFilter;
    }

    public void setCrsFilter(long crsFilter) {
        this.crsFilter = crsFilter;
    }

    public long getCrsS0() {
        return crsS0;
    }

    public void setCrsS0(long crsS0) {
        this.crsS0 = crsS0;
    }

    public long getCrsS1() {
        return crsS1;
    }

    public void setCrsS1(long crsS1) {
        this.crsS1 = crsS1;
    }

    public long getCrsS2() {
        return crsS2;
    }

    public void setCrsS2(long crsS2) {
        this.crsS2 = crsS2;
    }

    public long getCrsS3() {
        return crsS3;
    }

    public void setCrsS3(long crsS3) {
        this.crsS3 = crsS3;
    }

    public long getHidsAgg() {
        return hidsAgg;
    }

    public void setHidsAgg(long hidsAgg) {
        this.hidsAgg = hidsAgg;
    }

    public long getHidsFilter() {
        return hidsFilter;
    }

    public void setHidsFilter(long hidsFilter) {
        this.hidsFilter = hidsFilter;
    }

    public long getHidsS0() {
        return hidsS0;
    }

    public void setHidsS0(long hidsS0) {
        this.hidsS0 = hidsS0;
    }

    public long getHidsS1() {
        return hidsS1;
    }

    public void setHidsS1(long hidsS1) {
        this.hidsS1 = hidsS1;
    }

    public long getHidsS2() {
        return hidsS2;
    }

    public void setHidsS2(long hidsS2) {
        this.hidsS2 = hidsS2;
    }

    public long getHidsS3() {
        return hidsS3;
    }

    public void setHidsS3(long hidsS3) {
        this.hidsS3 = hidsS3;
    }

    public long getNidsAgg() {
        return nidsAgg;
    }

    public void setNidsAgg(long nidsAgg) {
        this.nidsAgg = nidsAgg;
    }

    public long getNidsFilter() {
        return nidsFilter;
    }

    public void setNidsFilter(long nidsFilter) {
        this.nidsFilter = nidsFilter;
    }

    public long getNidsS0() {
        return nidsS0;
    }

    public void setNidsS0(long nidsS0) {
        this.nidsS0 = nidsS0;
    }

    public long getNidsS1() {
        return nidsS1;
    }

    public void setNidsS1(long nidsS1) {
        this.nidsS1 = nidsS1;
    }

    public long getNidsS2() {
        return nidsS2;
    }

    public void setNidsS2(long nidsS2) {
        this.nidsS2 = nidsS2;
    }

    public long getNidsS3() {
        return nidsS3;
    }

    public void setNidsS3(long nidsS3) {
        this.nidsS3 = nidsS3;
    }

    public long getWafAgg() {
        return wafAgg;
    }

    public void setWafAgg(long wafAgg) {
        this.wafAgg = wafAgg;
    }

    public long getWafFilter() {
        return wafFilter;
    }

    public void setWafFilter(long wafFilter) {
        this.wafFilter = wafFilter;
    }

    public long getWafS0() {
        return wafS0;
    }

    public void setWafS0(long wafS0) {
        this.wafS0 = wafS0;
    }

    public long getWafS1() {
        return wafS1;
    }

    public void setWafS1(long wafS1) {
        this.wafS1 = wafS1;
    }

    public long getWafS2() {
        return wafS2;
    }

    public void setWafS2(long wafS2) {
        this.wafS2 = wafS2;
    }

    public long getWafS3() {
        return wafS3;
    }

    public void setWafS3(long wafS3) {
        this.wafS3 = wafS3;
    }

    public Date getTimeOfSurvey() {
        return timeOfSurvey;
    }

    public void setTimeOfSurvey(Date timeOfSurvey) {
        this.timeOfSurvey = timeOfSurvey;
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
        if (!(object instanceof NodeAlerts)) {
            return false;
        }
        NodeAlerts other = (NodeAlerts) object;
        if ((this.recId == null && other.recId != null) || (this.recId != null && !this.recId.equals(other.recId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.alertflex.entity.NodeAlerts[ recId=" + recId + " ]";
    }

}
