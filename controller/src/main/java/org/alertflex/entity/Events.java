/*
 *   Copyright 2021 Oleg Zharkov
 *
 *   Licensed under the Apache License, Version 2.0 (the "License").
 *   You may not use this file except in compliance with the License.
 *   A copy of the License is located at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   or in the "license" file accompanying this file. This file is distributed
 *   on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 *   express or implied. See the License for the specific language governing
 *   permissions and limitations under the License.
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
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "events")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Events.findAll", query = "SELECT e FROM Events e")
    , @NamedQuery(name = "Events.findById", query = "SELECT e FROM Events e WHERE e.id = :id")
    , @NamedQuery(name = "Events.findByOrgId", query = "SELECT e FROM Events e WHERE e.orgId = :orgId")
    , @NamedQuery(name = "Events.findByDate", query = "SELECT e FROM Events e WHERE e.date = :date")
    , @NamedQuery(name = "Events.findByUserId", query = "SELECT e FROM Events e WHERE e.userId = :userId")
    , @NamedQuery(name = "Events.findByUuid", query = "SELECT e FROM Events e WHERE e.uuid = :uuid")
    , @NamedQuery(name = "Events.findByPublished", query = "SELECT e FROM Events e WHERE e.published = :published")
    , @NamedQuery(name = "Events.findByAnalysis", query = "SELECT e FROM Events e WHERE e.analysis = :analysis")
    , @NamedQuery(name = "Events.findByAttributeCount", query = "SELECT e FROM Events e WHERE e.attributeCount = :attributeCount")
    , @NamedQuery(name = "Events.findByOrgcId", query = "SELECT e FROM Events e WHERE e.orgcId = :orgcId")
    , @NamedQuery(name = "Events.findByTimestamp", query = "SELECT e FROM Events e WHERE e.timestamp = :timestamp")
    , @NamedQuery(name = "Events.findByDistribution", query = "SELECT e FROM Events e WHERE e.distribution = :distribution")
    , @NamedQuery(name = "Events.findBySharingGroupId", query = "SELECT e FROM Events e WHERE e.sharingGroupId = :sharingGroupId")
    , @NamedQuery(name = "Events.findByProposalEmailLock", query = "SELECT e FROM Events e WHERE e.proposalEmailLock = :proposalEmailLock")
    , @NamedQuery(name = "Events.findByLocked", query = "SELECT e FROM Events e WHERE e.locked = :locked")
    , @NamedQuery(name = "Events.findByThreatLevelId", query = "SELECT e FROM Events e WHERE e.threatLevelId = :threatLevelId")
    , @NamedQuery(name = "Events.findByPublishTimestamp", query = "SELECT e FROM Events e WHERE e.publishTimestamp = :publishTimestamp")
    , @NamedQuery(name = "Events.findBySightingTimestamp", query = "SELECT e FROM Events e WHERE e.sightingTimestamp = :sightingTimestamp")
    , @NamedQuery(name = "Events.findByDisableCorrelation", query = "SELECT e FROM Events e WHERE e.disableCorrelation = :disableCorrelation")
    , @NamedQuery(name = "Events.findByExtendsUuid", query = "SELECT e FROM Events e WHERE e.extendsUuid = :extendsUuid")})
public class Events implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "org_id")
    private int orgId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "date")
    @Temporal(TemporalType.DATE)
    private Date date;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "info")
    private String info;
    @Basic(optional = false)
    @NotNull
    @Column(name = "user_id")
    private int userId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "uuid")
    private String uuid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "published")
    private boolean published;
    @Basic(optional = false)
    @NotNull
    @Column(name = "analysis")
    private short analysis;
    @Column(name = "attribute_count")
    private Integer attributeCount;
    @Basic(optional = false)
    @NotNull
    @Column(name = "orgc_id")
    private int orgcId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "timestamp")
    private int timestamp;
    @Basic(optional = false)
    @NotNull
    @Column(name = "distribution")
    private short distribution;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sharing_group_id")
    private int sharingGroupId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "proposal_email_lock")
    private boolean proposalEmailLock;
    @Basic(optional = false)
    @NotNull
    @Column(name = "locked")
    private boolean locked;
    @Basic(optional = false)
    @NotNull
    @Column(name = "threat_level_id")
    private int threatLevelId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "publish_timestamp")
    private int publishTimestamp;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sighting_timestamp")
    private int sightingTimestamp;
    @Basic(optional = false)
    @NotNull
    @Column(name = "disable_correlation")
    private boolean disableCorrelation;
    @Size(max = 40)
    @Column(name = "extends_uuid")
    private String extendsUuid;

    public Events() {
    }

    public Events(Integer id) {
        this.id = id;
    }

    public Events(Integer id, int orgId, Date date, String info, int userId, String uuid, boolean published, short analysis, int orgcId, int timestamp, short distribution, int sharingGroupId, boolean proposalEmailLock, boolean locked, int threatLevelId, int publishTimestamp, int sightingTimestamp, boolean disableCorrelation) {
        this.id = id;
        this.orgId = orgId;
        this.date = date;
        this.info = info;
        this.userId = userId;
        this.uuid = uuid;
        this.published = published;
        this.analysis = analysis;
        this.orgcId = orgcId;
        this.timestamp = timestamp;
        this.distribution = distribution;
        this.sharingGroupId = sharingGroupId;
        this.proposalEmailLock = proposalEmailLock;
        this.locked = locked;
        this.threatLevelId = threatLevelId;
        this.publishTimestamp = publishTimestamp;
        this.sightingTimestamp = sightingTimestamp;
        this.disableCorrelation = disableCorrelation;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getOrgId() {
        return orgId;
    }

    public void setOrgId(int orgId) {
        this.orgId = orgId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public boolean getPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public short getAnalysis() {
        return analysis;
    }

    public void setAnalysis(short analysis) {
        this.analysis = analysis;
    }

    public Integer getAttributeCount() {
        return attributeCount;
    }

    public void setAttributeCount(Integer attributeCount) {
        this.attributeCount = attributeCount;
    }

    public int getOrgcId() {
        return orgcId;
    }

    public void setOrgcId(int orgcId) {
        this.orgcId = orgcId;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public short getDistribution() {
        return distribution;
    }

    public void setDistribution(short distribution) {
        this.distribution = distribution;
    }

    public int getSharingGroupId() {
        return sharingGroupId;
    }

    public void setSharingGroupId(int sharingGroupId) {
        this.sharingGroupId = sharingGroupId;
    }

    public boolean getProposalEmailLock() {
        return proposalEmailLock;
    }

    public void setProposalEmailLock(boolean proposalEmailLock) {
        this.proposalEmailLock = proposalEmailLock;
    }

    public boolean getLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public int getThreatLevelId() {
        return threatLevelId;
    }

    public void setThreatLevelId(int threatLevelId) {
        this.threatLevelId = threatLevelId;
    }

    public int getPublishTimestamp() {
        return publishTimestamp;
    }

    public void setPublishTimestamp(int publishTimestamp) {
        this.publishTimestamp = publishTimestamp;
    }

    public int getSightingTimestamp() {
        return sightingTimestamp;
    }

    public void setSightingTimestamp(int sightingTimestamp) {
        this.sightingTimestamp = sightingTimestamp;
    }

    public boolean getDisableCorrelation() {
        return disableCorrelation;
    }

    public void setDisableCorrelation(boolean disableCorrelation) {
        this.disableCorrelation = disableCorrelation;
    }

    public String getExtendsUuid() {
        return extendsUuid;
    }

    public void setExtendsUuid(String extendsUuid) {
        this.extendsUuid = extendsUuid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Events)) {
            return false;
        }
        Events other = (Events) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.alertflex.entity.Events[ id=" + id + " ]";
    }

}
