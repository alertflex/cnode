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
@Table(name = "event_category")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EventCategory.findAll", query = "SELECT e FROM EventCategory e")
    , @NamedQuery(name = "EventCategory.findById", query = "SELECT e FROM EventCategory e WHERE e.id = :id")
    , @NamedQuery(name = "EventCategory.findByRefId", query = "SELECT e FROM EventCategory e WHERE e.refId = :refId")
    , @NamedQuery(name = "EventCategory.findByEvent", query = "SELECT e FROM EventCategory e WHERE e.event = :event")
    , @NamedQuery(name = "EventCategory.findByCats", query = "SELECT e FROM EventCategory e WHERE e.cats = :cats")
    , @NamedQuery(name = "EventCategory.findBySource", query = "SELECT e FROM EventCategory e WHERE e.source = :source")})
public class EventCategory implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "ref_id")
    private String refId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "event")
    private int event;
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

    public EventCategory() {
    }

    public EventCategory(Integer id) {
        this.id = id;
    }

    public EventCategory(Integer id, String refId, int event, String cats, String source) {
        this.id = id;
        this.refId = refId;
        this.event = event;
        this.cats = cats;
        this.source = source;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public int getEvent() {
        return event;
    }

    public void setEvent(int event) {
        this.event = event;
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
        if (!(object instanceof EventCategory)) {
            return false;
        }
        EventCategory other = (EventCategory) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.alertflex.entity.EventCategory[ id=" + id + " ]";
    }
    
}
