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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "net_countries")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NetCountries.findAll", query = "SELECT n FROM NetCountries n"),
    @NamedQuery(name = "NetCountries.findByRecId", query = "SELECT n FROM NetCountries n WHERE n.recId = :recId"),
    @NamedQuery(name = "NetCountries.findByRefId", query = "SELECT n FROM NetCountries n WHERE n.refId = :refId"),
    @NamedQuery(name = "NetCountries.findByNode", query = "SELECT n FROM NetCountries n WHERE n.node = :node"),
    @NamedQuery(name = "NetCountries.findByProbe", query = "SELECT n FROM NetCountries n WHERE n.probe = :probe"),
    @NamedQuery(name = "NetCountries.findBySensor", query = "SELECT n FROM NetCountries n WHERE n.sensor = :sensor"),
    @NamedQuery(name = "NetCountries.findByTimeOfSurvey", query = "SELECT n FROM NetCountries n WHERE n.timeOfSurvey = :timeOfSurvey"),
    @NamedQuery(name = "NetCountries.findByCountry", query = "SELECT n FROM NetCountries n WHERE n.country = :country"),
    @NamedQuery(name = "NetCountries.findByBytes", query = "SELECT n FROM NetCountries n WHERE n.bytes = :bytes"),
    @NamedQuery(name = "NetCountries.findBySessions", query = "SELECT n FROM NetCountries n WHERE n.sessions = :sessions")})
public class NetCountries implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "rec_id")
    private Integer recId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "ref_id")
    private String refId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "node")
    private String node;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "probe")
    private String probe;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "sensor")
    private String sensor;
    @Column(name = "time_of_survey")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeOfSurvey;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "country")
    private String country;
    @Basic(optional = false)
    @NotNull
    @Column(name = "bytes")
    private long bytes;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sessions")
    private long sessions;

    public NetCountries() {
    }

    public NetCountries(Integer recId) {
        this.recId = recId;
    }

    public NetCountries(Integer recId, String refId, String node, String probe, String sensor, String country, long bytes, long sessions) {
        this.recId = recId;
        this.refId = refId;
        this.node = node;
        this.probe = probe;
        this.sensor = sensor;
        this.country = country;
        this.bytes = bytes;
        this.sessions = sessions;
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

    public String getSensor() {
        return sensor;
    }

    public void setSensor(String sensor) {
        this.sensor = sensor;
    }

    public Date getTimeOfSurvey() {
        return timeOfSurvey;
    }

    public void setTimeOfSurvey(Date timeOfSurvey) {
        this.timeOfSurvey = timeOfSurvey;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public long getBytes() {
        return bytes;
    }

    public void setBytes(long bytes) {
        this.bytes = bytes;
    }

    public long getSessions() {
        return sessions;
    }

    public void setSessions(long sessions) {
        this.sessions = sessions;
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
        if (!(object instanceof NetCountries)) {
            return false;
        }
        NetCountries other = (NetCountries) object;
        if ((this.recId == null && other.recId != null) || (this.recId != null && !this.recId.equals(other.recId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.alertflex.entity.NetCountries[ recId=" + recId + " ]";
    }
    
}
