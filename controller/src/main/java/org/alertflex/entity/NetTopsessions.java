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
@Table(name = "net_topsessions")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NetTopsessions.findAll", query = "SELECT n FROM NetTopsessions n"),
    @NamedQuery(name = "NetTopsessions.findByRecId", query = "SELECT n FROM NetTopsessions n WHERE n.recId = :recId"),
    @NamedQuery(name = "NetTopsessions.findByRefId", query = "SELECT n FROM NetTopsessions n WHERE n.refId = :refId"),
    @NamedQuery(name = "NetTopsessions.findByNode", query = "SELECT n FROM NetTopsessions n WHERE n.node = :node"),
    @NamedQuery(name = "NetTopsessions.findByProbe", query = "SELECT n FROM NetTopsessions n WHERE n.probe = :probe"),
    @NamedQuery(name = "NetTopsessions.findBySensor", query = "SELECT n FROM NetTopsessions n WHERE n.sensor = :sensor"),
    @NamedQuery(name = "NetTopsessions.findBySrcIp", query = "SELECT n FROM NetTopsessions n WHERE n.srcIp = :srcIp"),
    @NamedQuery(name = "NetTopsessions.findBySrcCountry", query = "SELECT n FROM NetTopsessions n WHERE n.srcCountry = :srcCountry"),
    @NamedQuery(name = "NetTopsessions.findByDstIp", query = "SELECT n FROM NetTopsessions n WHERE n.dstIp = :dstIp"),
    @NamedQuery(name = "NetTopsessions.findByDstCountry", query = "SELECT n FROM NetTopsessions n WHERE n.dstCountry = :dstCountry"),
    @NamedQuery(name = "NetTopsessions.findBySessions", query = "SELECT n FROM NetTopsessions n WHERE n.sessions = :sessions"),
    @NamedQuery(name = "NetTopsessions.findByTimeOfSurvey", query = "SELECT n FROM NetTopsessions n WHERE n.timeOfSurvey = :timeOfSurvey")})
public class NetTopsessions implements Serializable {

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
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "src_ip")
    private String srcIp;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "src_country")
    private String srcCountry;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "dst_ip")
    private String dstIp;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "dst_country")
    private String dstCountry;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sessions")
    private long sessions;
    @Column(name = "time_of_survey")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeOfSurvey;

    public NetTopsessions() {
    }

    public NetTopsessions(Integer recId) {
        this.recId = recId;
    }

    public NetTopsessions(Integer recId, String refId, String node, String probe, String sensor, String srcIp, String srcCountry, String dstIp, String dstCountry, long sessions) {
        this.recId = recId;
        this.refId = refId;
        this.node = node;
        this.probe = probe;
        this.sensor = sensor;
        this.srcIp = srcIp;
        this.srcCountry = srcCountry;
        this.dstIp = dstIp;
        this.dstCountry = dstCountry;
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

    public String getSrcIp() {
        return srcIp;
    }

    public void setSrcIp(String srcIp) {
        this.srcIp = srcIp;
    }

    public String getSrcCountry() {
        return srcCountry;
    }

    public void setSrcCountry(String srcCountry) {
        this.srcCountry = srcCountry;
    }

    public String getDstIp() {
        return dstIp;
    }

    public void setDstIp(String dstIp) {
        this.dstIp = dstIp;
    }

    public String getDstCountry() {
        return dstCountry;
    }

    public void setDstCountry(String dstCountry) {
        this.dstCountry = dstCountry;
    }

    public long getSessions() {
        return sessions;
    }

    public void setSessions(long sessions) {
        this.sessions = sessions;
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
        if (!(object instanceof NetTopsessions)) {
            return false;
        }
        NetTopsessions other = (NetTopsessions) object;
        if ((this.recId == null && other.recId != null) || (this.recId != null && !this.recId.equals(other.recId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.alertflex.entity.NetTopsessions[ recId=" + recId + " ]";
    }
    
}
