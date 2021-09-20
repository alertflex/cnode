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
@Table(name = "net_topsessions")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NetTopsessions.findAll", query = "SELECT n FROM NetTopsessions n"),
    @NamedQuery(name = "NetTopsessions.findByRecId", query = "SELECT n FROM NetTopsessions n WHERE n.recId = :recId"),
    @NamedQuery(name = "NetTopsessions.findByRefId", query = "SELECT n FROM NetTopsessions n WHERE n.refId = :refId"),
    @NamedQuery(name = "NetTopsessions.findByNode", query = "SELECT n FROM NetTopsessions n WHERE n.node = :node"),
    @NamedQuery(name = "NetTopsessions.findByProbe", query = "SELECT n FROM NetTopsessions n WHERE n.probe = :probe"),
    @NamedQuery(name = "NetTopsessions.findBySensorName", query = "SELECT n FROM NetTopsessions n WHERE n.sensorName = :sensorName"),
    @NamedQuery(name = "NetTopsessions.findBySensorType", query = "SELECT n FROM NetTopsessions n WHERE n.sensorType = :sensorType"),
    @NamedQuery(name = "NetTopsessions.findBySrcIp", query = "SELECT n FROM NetTopsessions n WHERE n.srcIp = :srcIp"),
    @NamedQuery(name = "NetTopsessions.findBySrcCountry", query = "SELECT n FROM NetTopsessions n WHERE n.srcCountry = :srcCountry"),
    @NamedQuery(name = "NetTopsessions.findByDstIp", query = "SELECT n FROM NetTopsessions n WHERE n.dstIp = :dstIp"),
    @NamedQuery(name = "NetTopsessions.findByDstCountry", query = "SELECT n FROM NetTopsessions n WHERE n.dstCountry = :dstCountry"),
    @NamedQuery(name = "NetTopsessions.findBySrcHostname", query = "SELECT n FROM NetTopsessions n WHERE n.srcHostname = :srcHostname"),
    @NamedQuery(name = "NetTopsessions.findByDstHostname", query = "SELECT n FROM NetTopsessions n WHERE n.dstHostname = :dstHostname"),
    @NamedQuery(name = "NetTopsessions.findBySessions", query = "SELECT n FROM NetTopsessions n WHERE n.sessions = :sessions"),
    @NamedQuery(name = "NetTopsessions.findByTokenExist", query = "SELECT n FROM NetTopsessions n WHERE n.tokenExist = :tokenExist"),
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
    @Column(name = "sensor_name")
    private String sensorName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "sensor_type")
    private String sensorType;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "src_ip")
    private String srcIp;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "src_country")
    private String srcCountry;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "dst_ip")
    private String dstIp;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "dst_country")
    private String dstCountry;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "src_hostname")
    private String srcHostname;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "dst_hostname")
    private String dstHostname;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sessions")
    private long sessions;
    @Basic(optional = false)
    @NotNull
    @Column(name = "token_exist")
    private int tokenExist;
    @Column(name = "time_of_survey")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeOfSurvey;

    public NetTopsessions() {
    }

    public NetTopsessions(Integer recId) {
        this.recId = recId;
    }

    public NetTopsessions(Integer recId, String refId, String node, String probe, String sensorName, String sensorType, String srcIp, String srcCountry, String dstIp, String dstCountry, String srcHostname, String dstHostname, long sessions, int tokenExist) {
        this.recId = recId;
        this.refId = refId;
        this.node = node;
        this.probe = probe;
        this.sensorName = sensorName;
        this.sensorType = sensorType;
        this.srcIp = srcIp;
        this.srcCountry = srcCountry;
        this.dstIp = dstIp;
        this.dstCountry = dstCountry;
        this.srcHostname = srcHostname;
        this.dstHostname = dstHostname;
        this.sessions = sessions;
        this.tokenExist = tokenExist;
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

    public String getSensorName() {
        return sensorName;
    }

    public void setSensorName(String sensorName) {
        this.sensorName = sensorName;
    }

    public String getSensorType() {
        return sensorType;
    }

    public void setSensorType(String sensorType) {
        this.sensorType = sensorType;
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

    public String getSrcHostname() {
        return srcHostname;
    }

    public void setSrcHostname(String srcHostname) {
        this.srcHostname = srcHostname;
    }

    public String getDstHostname() {
        return dstHostname;
    }

    public void setDstHostname(String dstHostname) {
        this.dstHostname = dstHostname;
    }

    public long getSessions() {
        return sessions;
    }

    public void setSessions(long sessions) {
        this.sessions = sessions;
    }

    public int getTokenExist() {
        return tokenExist;
    }

    public void setTokenExist(int tokenExist) {
        this.tokenExist = tokenExist;
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
