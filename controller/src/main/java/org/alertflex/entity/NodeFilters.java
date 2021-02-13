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
@Table(name = "node_filters")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NodeFilters.findAll", query = "SELECT n FROM NodeFilters n")
    , @NamedQuery(name = "NodeFilters.findByRecId", query = "SELECT n FROM NodeFilters n WHERE n.recId = :recId")
    , @NamedQuery(name = "NodeFilters.findByRefId", query = "SELECT n FROM NodeFilters n WHERE n.refId = :refId")
    , @NamedQuery(name = "NodeFilters.findByNode", query = "SELECT n FROM NodeFilters n WHERE n.node = :node")
    , @NamedQuery(name = "NodeFilters.findByProbe", query = "SELECT n FROM NodeFilters n WHERE n.probe = :probe")
    , @NamedQuery(name = "NodeFilters.findByAgentList", query = "SELECT n FROM NodeFilters n WHERE n.agentList = :agentList")
    , @NamedQuery(name = "NodeFilters.findByHnetList", query = "SELECT n FROM NodeFilters n WHERE n.hnetList = :hnetList")
    , @NamedQuery(name = "NodeFilters.findByHidsFilters", query = "SELECT n FROM NodeFilters n WHERE n.hidsFilters = :hidsFilters")
    , @NamedQuery(name = "NodeFilters.findByNidsFilters", query = "SELECT n FROM NodeFilters n WHERE n.nidsFilters = :nidsFilters")
    , @NamedQuery(name = "NodeFilters.findByCrsFilters", query = "SELECT n FROM NodeFilters n WHERE n.crsFilters = :crsFilters")
    , @NamedQuery(name = "NodeFilters.findByTimeOfSurvey", query = "SELECT n FROM NodeFilters n WHERE n.timeOfSurvey = :timeOfSurvey")})
public class NodeFilters implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "rec_id")
    private Long recId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "ref_id")
    private String refId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "node")
    private String node;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "probe")
    private String probe;
    @Basic(optional = false)
    @NotNull
    @Column(name = "agent_list")
    private long agentList;
    @Basic(optional = false)
    @NotNull
    @Column(name = "hnet_list")
    private long hnetList;
    @Basic(optional = false)
    @NotNull
    @Column(name = "hids_filters")
    private long hidsFilters;
    @Basic(optional = false)
    @NotNull
    @Column(name = "nids_filters")
    private long nidsFilters;
    @Basic(optional = false)
    @NotNull
    @Column(name = "crs_filters")
    private long crsFilters;
    @Column(name = "time_of_survey")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeOfSurvey;

    public NodeFilters() {
    }

    public NodeFilters(Long recId) {
        this.recId = recId;
    }

    public NodeFilters(Long recId, String refId, String node, String probe, long agentList, long hnetList, long hidsFilters, long nidsFilters, long crsFilters) {
        this.recId = recId;
        this.refId = refId;
        this.node = node;
        this.probe = probe;
        this.agentList = agentList;
        this.hnetList = hnetList;
        this.hidsFilters = hidsFilters;
        this.nidsFilters = nidsFilters;
        this.crsFilters = crsFilters;
    }

    public Long getRecId() {
        return recId;
    }

    public void setRecId(Long recId) {
        this.recId = recId;
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public String getProbe() {
        return probe;
    }

    public void setProbe(String probe) {
        this.probe = probe;
    }

    public long getAgentList() {
        return agentList;
    }

    public void setAgentList(long agentList) {
        this.agentList = agentList;
    }

    public long getHnetList() {
        return hnetList;
    }

    public void setHnetList(long hnetList) {
        this.hnetList = hnetList;
    }

    public long getHidsFilters() {
        return hidsFilters;
    }

    public void setHidsFilters(long hidsFilters) {
        this.hidsFilters = hidsFilters;
    }

    public long getNidsFilters() {
        return nidsFilters;
    }

    public void setNidsFilters(long nidsFilters) {
        this.nidsFilters = nidsFilters;
    }

    public long getCrsFilters() {
        return crsFilters;
    }

    public void setCrsFilters(long crsFilters) {
        this.crsFilters = crsFilters;
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
        if (!(object instanceof NodeFilters)) {
            return false;
        }
        NodeFilters other = (NodeFilters) object;
        if ((this.recId == null && other.recId != null) || (this.recId != null && !this.recId.equals(other.recId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.alertflex.entity.NodeFilters[ recId=" + recId + " ]";
    }
    
}
