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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "attributes")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Attributes.findAll", query = "SELECT a FROM Attributes a")
    , @NamedQuery(name = "Attributes.findById", query = "SELECT a FROM Attributes a WHERE a.id = :id")
    , @NamedQuery(name = "Attributes.findByEventId", query = "SELECT a FROM Attributes a WHERE a.eventId = :eventId")
    , @NamedQuery(name = "Attributes.findByObjectId", query = "SELECT a FROM Attributes a WHERE a.objectId = :objectId")
    , @NamedQuery(name = "Attributes.findByObjectRelation", query = "SELECT a FROM Attributes a WHERE a.objectRelation = :objectRelation")
    , @NamedQuery(name = "Attributes.findByCategory", query = "SELECT a FROM Attributes a WHERE a.category = :category")
    , @NamedQuery(name = "Attributes.findByType", query = "SELECT a FROM Attributes a WHERE a.type = :type")
    , @NamedQuery(name = "Attributes.findByToIds", query = "SELECT a FROM Attributes a WHERE a.toIds = :toIds")
    , @NamedQuery(name = "Attributes.findByUuid", query = "SELECT a FROM Attributes a WHERE a.uuid = :uuid")
    , @NamedQuery(name = "Attributes.findByTimestamp", query = "SELECT a FROM Attributes a WHERE a.timestamp = :timestamp")
    , @NamedQuery(name = "Attributes.findByDistribution", query = "SELECT a FROM Attributes a WHERE a.distribution = :distribution")
    , @NamedQuery(name = "Attributes.findBySharingGroupId", query = "SELECT a FROM Attributes a WHERE a.sharingGroupId = :sharingGroupId")
    , @NamedQuery(name = "Attributes.findByDeleted", query = "SELECT a FROM Attributes a WHERE a.deleted = :deleted")
    , @NamedQuery(name = "Attributes.findByDisableCorrelation", query = "SELECT a FROM Attributes a WHERE a.disableCorrelation = :disableCorrelation")})
public class Attributes implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "event_id")
    private int eventId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "object_id")
    private int objectId;
    @Size(max = 255)
    @Column(name = "object_relation")
    private String objectRelation;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "category")
    private String category;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "type")
    private String type;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "value1")
    private String value1;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "value2")
    private String value2;
    @Basic(optional = false)
    @NotNull
    @Column(name = "to_ids")
    private boolean toIds;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "uuid")
    private String uuid;
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
    @Lob
    @Size(max = 65535)
    @Column(name = "comment")
    private String comment;
    @Basic(optional = false)
    @NotNull
    @Column(name = "deleted")
    private boolean deleted;
    @Basic(optional = false)
    @NotNull
    @Column(name = "disable_correlation")
    private boolean disableCorrelation;

    public Attributes() {
    }

    public Attributes(Integer id) {
        this.id = id;
    }

    public Attributes(Integer id, int eventId, int objectId, String category, String type, String value1, String value2, boolean toIds, String uuid, int timestamp, short distribution, int sharingGroupId, boolean deleted, boolean disableCorrelation) {
        this.id = id;
        this.eventId = eventId;
        this.objectId = objectId;
        this.category = category;
        this.type = type;
        this.value1 = value1;
        this.value2 = value2;
        this.toIds = toIds;
        this.uuid = uuid;
        this.timestamp = timestamp;
        this.distribution = distribution;
        this.sharingGroupId = sharingGroupId;
        this.deleted = deleted;
        this.disableCorrelation = disableCorrelation;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public int getObjectId() {
        return objectId;
    }

    public void setObjectId(int objectId) {
        this.objectId = objectId;
    }

    public String getObjectRelation() {
        return objectRelation;
    }

    public void setObjectRelation(String objectRelation) {
        this.objectRelation = objectRelation;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue1() {
        return value1;
    }

    public void setValue1(String value1) {
        this.value1 = value1;
    }

    public String getValue2() {
        return value2;
    }

    public void setValue2(String value2) {
        this.value2 = value2;
    }

    public boolean getToIds() {
        return toIds;
    }

    public void setToIds(boolean toIds) {
        this.toIds = toIds;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public boolean getDisableCorrelation() {
        return disableCorrelation;
    }

    public void setDisableCorrelation(boolean disableCorrelation) {
        this.disableCorrelation = disableCorrelation;
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
        if (!(object instanceof Attributes)) {
            return false;
        }
        Attributes other = (Attributes) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.alertflex.entity.Attributes[ id=" + id + " ]";
    }

}
