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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "alert_category")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AlertCategory.findAll", query = "SELECT a FROM AlertCategory a")
    , @NamedQuery(name = "AlertCategory.findById", query = "SELECT a FROM AlertCategory a WHERE a.id = :id")
    , @NamedQuery(name = "AlertCategory.findByEventId", query = "SELECT a FROM AlertCategory a WHERE a.eventId = :eventId")
    , @NamedQuery(name = "AlertCategory.findByCats", query = "SELECT a FROM AlertCategory a WHERE a.cats = :cats")
    , @NamedQuery(name = "AlertCategory.findBySource", query = "SELECT a FROM AlertCategory a WHERE a.source = :source")})
public class AlertCategory implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "event_id")
    private String eventId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1024)
    @Column(name = "cats")
    private String cats;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "source")
    private String source;

    public AlertCategory() {
    }

    public AlertCategory(Integer id) {
        this.id = id;
    }

    public AlertCategory(Integer id, String eventId, String cats, String source) {
        this.id = id;
        this.eventId = eventId;
        this.cats = cats;
        this.source = source;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getCats() {
        return cats;
    }

    public void setCats(String cats) {
        this.cats = cats;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
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
        if (!(object instanceof AlertCategory)) {
            return false;
        }
        AlertCategory other = (AlertCategory) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.alertflex.entity.AlertCategory[ id=" + id + " ]";
    }
    
}
