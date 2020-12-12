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
@Table(name = "node_monitor")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NodeMonitor.findAll", query = "SELECT n FROM NodeMonitor n")
    , @NamedQuery(name = "NodeMonitor.findByRecId", query = "SELECT n FROM NodeMonitor n WHERE n.recId = :recId")
    , @NamedQuery(name = "NodeMonitor.findByNodeId", query = "SELECT n FROM NodeMonitor n WHERE n.nodeId = :nodeId")
    , @NamedQuery(name = "NodeMonitor.findByRefId", query = "SELECT n FROM NodeMonitor n WHERE n.refId = :refId")
    , @NamedQuery(name = "NodeMonitor.findByEventsNids", query = "SELECT n FROM NodeMonitor n WHERE n.eventsNids = :eventsNids")
    , @NamedQuery(name = "NodeMonitor.findByEventsHids", query = "SELECT n FROM NodeMonitor n WHERE n.eventsHids = :eventsHids")
    , @NamedQuery(name = "NodeMonitor.findByEventsWaf", query = "SELECT n FROM NodeMonitor n WHERE n.eventsWaf = :eventsWaf")
    , @NamedQuery(name = "NodeMonitor.findByEventsMisc", query = "SELECT n FROM NodeMonitor n WHERE n.eventsMisc = :eventsMisc")
    , @NamedQuery(name = "NodeMonitor.findByEventsCrs", query = "SELECT n FROM NodeMonitor n WHERE n.eventsCrs = :eventsCrs")
    , @NamedQuery(name = "NodeMonitor.findByLogCounter", query = "SELECT n FROM NodeMonitor n WHERE n.logCounter = :logCounter")
    , @NamedQuery(name = "NodeMonitor.findByLogVolume", query = "SELECT n FROM NodeMonitor n WHERE n.logVolume = :logVolume")
    , @NamedQuery(name = "NodeMonitor.findByStatCounter", query = "SELECT n FROM NodeMonitor n WHERE n.statCounter = :statCounter")
    , @NamedQuery(name = "NodeMonitor.findByStatVolume", query = "SELECT n FROM NodeMonitor n WHERE n.statVolume = :statVolume")
    , @NamedQuery(name = "NodeMonitor.findByTimeOfSurvey", query = "SELECT n FROM NodeMonitor n WHERE n.timeOfSurvey = :timeOfSurvey")})
public class NodeMonitor implements Serializable {

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
    @Column(name = "events_nids")
    private long eventsNids;
    @Basic(optional = false)
    @NotNull
    @Column(name = "events_hids")
    private long eventsHids;
    @Basic(optional = false)
    @NotNull
    @Column(name = "events_waf")
    private long eventsWaf;
    @Basic(optional = false)
    @NotNull
    @Column(name = "events_misc")
    private long eventsMisc;
    @Basic(optional = false)
    @NotNull
    @Column(name = "events_crs")
    private long eventsCrs;
    @Basic(optional = false)
    @NotNull
    @Column(name = "log_counter")
    private long logCounter;
    @Basic(optional = false)
    @NotNull
    @Column(name = "log_volume")
    private long logVolume;
    @Basic(optional = false)
    @NotNull
    @Column(name = "stat_counter")
    private long statCounter;
    @Basic(optional = false)
    @NotNull
    @Column(name = "stat_volume")
    private long statVolume;
    @Column(name = "time_of_survey")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeOfSurvey;

    public NodeMonitor() {
    }

    public NodeMonitor(Long recId) {
        this.recId = recId;
    }

    public NodeMonitor(Long recId, String nodeId, String refId, long eventsNids, long eventsHids, long eventsWaf, long eventsMisc, long eventsCrs, long logCounter, long logVolume, long statCounter, long statVolume) {
        this.recId = recId;
        this.nodeId = nodeId;
        this.refId = refId;
        this.eventsNids = eventsNids;
        this.eventsHids = eventsHids;
        this.eventsWaf = eventsWaf;
        this.eventsMisc = eventsMisc;
        this.eventsCrs = eventsCrs;
        this.logCounter = logCounter;
        this.logVolume = logVolume;
        this.statCounter = statCounter;
        this.statVolume = statVolume;
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

    public long getEventsNids() {
        return eventsNids;
    }

    public void setEventsNids(long eventsNids) {
        this.eventsNids = eventsNids;
    }

    public long getEventsHids() {
        return eventsHids;
    }

    public void setEventsHids(long eventsHids) {
        this.eventsHids = eventsHids;
    }

    public long getEventsWaf() {
        return eventsWaf;
    }

    public void setEventsWaf(long eventsWaf) {
        this.eventsWaf = eventsWaf;
    }

    public long getEventsMisc() {
        return eventsMisc;
    }

    public void setEventsMisc(long eventsMisc) {
        this.eventsMisc = eventsMisc;
    }

    public long getEventsCrs() {
        return eventsCrs;
    }

    public void setEventsCrs(long eventsCrs) {
        this.eventsCrs = eventsCrs;
    }

    public long getLogCounter() {
        return logCounter;
    }

    public void setLogCounter(long logCounter) {
        this.logCounter = logCounter;
    }

    public long getLogVolume() {
        return logVolume;
    }

    public void setLogVolume(long logVolume) {
        this.logVolume = logVolume;
    }

    public long getStatCounter() {
        return statCounter;
    }

    public void setStatCounter(long statCounter) {
        this.statCounter = statCounter;
    }

    public long getStatVolume() {
        return statVolume;
    }

    public void setStatVolume(long statVolume) {
        this.statVolume = statVolume;
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
        if (!(object instanceof NodeMonitor)) {
            return false;
        }
        NodeMonitor other = (NodeMonitor) object;
        if ((this.recId == null && other.recId != null) || (this.recId != null && !this.recId.equals(other.recId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.alertflex.entity.NodeMonitor[ recId=" + recId + " ]";
    }

}
