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
@Table(name = "net_stat")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NetStat.findAll", query = "SELECT n FROM NetStat n")
    , @NamedQuery(name = "NetStat.findByRecId", query = "SELECT n FROM NetStat n WHERE n.recId = :recId")
    , @NamedQuery(name = "NetStat.findByNodeId", query = "SELECT n FROM NetStat n WHERE n.nodeId = :nodeId")
    , @NamedQuery(name = "NetStat.findByRefId", query = "SELECT n FROM NetStat n WHERE n.refId = :refId")
    , @NamedQuery(name = "NetStat.findByIds", query = "SELECT n FROM NetStat n WHERE n.ids = :ids")
    , @NamedQuery(name = "NetStat.findByInvalid", query = "SELECT n FROM NetStat n WHERE n.invalid = :invalid")
    , @NamedQuery(name = "NetStat.findByPkts", query = "SELECT n FROM NetStat n WHERE n.pkts = :pkts")
    , @NamedQuery(name = "NetStat.findByBytes", query = "SELECT n FROM NetStat n WHERE n.bytes = :bytes")
    , @NamedQuery(name = "NetStat.findByEthernet", query = "SELECT n FROM NetStat n WHERE n.ethernet = :ethernet")
    , @NamedQuery(name = "NetStat.findByPpp", query = "SELECT n FROM NetStat n WHERE n.ppp = :ppp")
    , @NamedQuery(name = "NetStat.findByPppoe", query = "SELECT n FROM NetStat n WHERE n.pppoe = :pppoe")
    , @NamedQuery(name = "NetStat.findByGre", query = "SELECT n FROM NetStat n WHERE n.gre = :gre")
    , @NamedQuery(name = "NetStat.findByVlan", query = "SELECT n FROM NetStat n WHERE n.vlan = :vlan")
    , @NamedQuery(name = "NetStat.findByVlanQinq", query = "SELECT n FROM NetStat n WHERE n.vlanQinq = :vlanQinq")
    , @NamedQuery(name = "NetStat.findByMpls", query = "SELECT n FROM NetStat n WHERE n.mpls = :mpls")
    , @NamedQuery(name = "NetStat.findByIpv4", query = "SELECT n FROM NetStat n WHERE n.ipv4 = :ipv4")
    , @NamedQuery(name = "NetStat.findByIpv6", query = "SELECT n FROM NetStat n WHERE n.ipv6 = :ipv6")
    , @NamedQuery(name = "NetStat.findByTcp", query = "SELECT n FROM NetStat n WHERE n.tcp = :tcp")
    , @NamedQuery(name = "NetStat.findByUdp", query = "SELECT n FROM NetStat n WHERE n.udp = :udp")
    , @NamedQuery(name = "NetStat.findBySctp", query = "SELECT n FROM NetStat n WHERE n.sctp = :sctp")
    , @NamedQuery(name = "NetStat.findByIcmpv4", query = "SELECT n FROM NetStat n WHERE n.icmpv4 = :icmpv4")
    , @NamedQuery(name = "NetStat.findByIcmpv6", query = "SELECT n FROM NetStat n WHERE n.icmpv6 = :icmpv6")
    , @NamedQuery(name = "NetStat.findByTeredo", query = "SELECT n FROM NetStat n WHERE n.teredo = :teredo")
    , @NamedQuery(name = "NetStat.findByIpv4InIpv6", query = "SELECT n FROM NetStat n WHERE n.ipv4InIpv6 = :ipv4InIpv6")
    , @NamedQuery(name = "NetStat.findByIpv6InIpv6", query = "SELECT n FROM NetStat n WHERE n.ipv6InIpv6 = :ipv6InIpv6")
    , @NamedQuery(name = "NetStat.findByTimeOfSurvey", query = "SELECT n FROM NetStat n WHERE n.timeOfSurvey = :timeOfSurvey")})
public class NetStat implements Serializable {

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
    @Size(min = 1, max = 255)
    @Column(name = "ids")
    private String ids;
    @Basic(optional = false)
    @NotNull
    @Column(name = "invalid")
    private long invalid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "pkts")
    private long pkts;
    @Basic(optional = false)
    @NotNull
    @Column(name = "bytes")
    private long bytes;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ethernet")
    private long ethernet;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ppp")
    private long ppp;
    @Basic(optional = false)
    @NotNull
    @Column(name = "pppoe")
    private long pppoe;
    @Basic(optional = false)
    @NotNull
    @Column(name = "gre")
    private long gre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "vlan")
    private long vlan;
    @Basic(optional = false)
    @NotNull
    @Column(name = "vlan_qinq")
    private long vlanQinq;
    @Basic(optional = false)
    @NotNull
    @Column(name = "mpls")
    private long mpls;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ipv4")
    private long ipv4;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ipv6")
    private long ipv6;
    @Basic(optional = false)
    @NotNull
    @Column(name = "tcp")
    private long tcp;
    @Basic(optional = false)
    @NotNull
    @Column(name = "udp")
    private long udp;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sctp")
    private long sctp;
    @Basic(optional = false)
    @NotNull
    @Column(name = "icmpv4")
    private long icmpv4;
    @Basic(optional = false)
    @NotNull
    @Column(name = "icmpv6")
    private long icmpv6;
    @Basic(optional = false)
    @NotNull
    @Column(name = "teredo")
    private long teredo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ipv4_in_ipv6")
    private long ipv4InIpv6;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ipv6_in_ipv6")
    private long ipv6InIpv6;
    @Column(name = "time_of_survey")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeOfSurvey;

    public NetStat() {
    }

    public NetStat(Long recId) {
        this.recId = recId;
    }

    public NetStat(Long recId, String nodeId, String refId, String ids, long invalid, long pkts, long bytes, long ethernet, long ppp, long pppoe, long gre, long vlan, long vlanQinq, long mpls, long ipv4, long ipv6, long tcp, long udp, long sctp, long icmpv4, long icmpv6, long teredo, long ipv4InIpv6, long ipv6InIpv6) {
        this.recId = recId;
        this.nodeId = nodeId;
        this.refId = refId;
        this.ids = ids;
        this.invalid = invalid;
        this.pkts = pkts;
        this.bytes = bytes;
        this.ethernet = ethernet;
        this.ppp = ppp;
        this.pppoe = pppoe;
        this.gre = gre;
        this.vlan = vlan;
        this.vlanQinq = vlanQinq;
        this.mpls = mpls;
        this.ipv4 = ipv4;
        this.ipv6 = ipv6;
        this.tcp = tcp;
        this.udp = udp;
        this.sctp = sctp;
        this.icmpv4 = icmpv4;
        this.icmpv6 = icmpv6;
        this.teredo = teredo;
        this.ipv4InIpv6 = ipv4InIpv6;
        this.ipv6InIpv6 = ipv6InIpv6;
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

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public long getInvalid() {
        return invalid;
    }

    public void setInvalid(long invalid) {
        this.invalid = invalid;
    }

    public long getPkts() {
        return pkts;
    }

    public void setPkts(long pkts) {
        this.pkts = pkts;
    }

    public long getBytes() {
        return bytes;
    }

    public void setBytes(long bytes) {
        this.bytes = bytes;
    }

    public long getEthernet() {
        return ethernet;
    }

    public void setEthernet(long ethernet) {
        this.ethernet = ethernet;
    }

    public long getPpp() {
        return ppp;
    }

    public void setPpp(long ppp) {
        this.ppp = ppp;
    }

    public long getPppoe() {
        return pppoe;
    }

    public void setPppoe(long pppoe) {
        this.pppoe = pppoe;
    }

    public long getGre() {
        return gre;
    }

    public void setGre(long gre) {
        this.gre = gre;
    }

    public long getVlan() {
        return vlan;
    }

    public void setVlan(long vlan) {
        this.vlan = vlan;
    }

    public long getVlanQinq() {
        return vlanQinq;
    }

    public void setVlanQinq(long vlanQinq) {
        this.vlanQinq = vlanQinq;
    }

    public long getMpls() {
        return mpls;
    }

    public void setMpls(long mpls) {
        this.mpls = mpls;
    }

    public long getIpv4() {
        return ipv4;
    }

    public void setIpv4(long ipv4) {
        this.ipv4 = ipv4;
    }

    public long getIpv6() {
        return ipv6;
    }

    public void setIpv6(long ipv6) {
        this.ipv6 = ipv6;
    }

    public long getTcp() {
        return tcp;
    }

    public void setTcp(long tcp) {
        this.tcp = tcp;
    }

    public long getUdp() {
        return udp;
    }

    public void setUdp(long udp) {
        this.udp = udp;
    }

    public long getSctp() {
        return sctp;
    }

    public void setSctp(long sctp) {
        this.sctp = sctp;
    }

    public long getIcmpv4() {
        return icmpv4;
    }

    public void setIcmpv4(long icmpv4) {
        this.icmpv4 = icmpv4;
    }

    public long getIcmpv6() {
        return icmpv6;
    }

    public void setIcmpv6(long icmpv6) {
        this.icmpv6 = icmpv6;
    }

    public long getTeredo() {
        return teredo;
    }

    public void setTeredo(long teredo) {
        this.teredo = teredo;
    }

    public long getIpv4InIpv6() {
        return ipv4InIpv6;
    }

    public void setIpv4InIpv6(long ipv4InIpv6) {
        this.ipv4InIpv6 = ipv4InIpv6;
    }

    public long getIpv6InIpv6() {
        return ipv6InIpv6;
    }

    public void setIpv6InIpv6(long ipv6InIpv6) {
        this.ipv6InIpv6 = ipv6InIpv6;
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
        if (!(object instanceof NetStat)) {
            return false;
        }
        NetStat other = (NetStat) object;
        if ((this.recId == null && other.recId != null) || (this.recId != null && !this.recId.equals(other.recId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.alertflex.entity.NetStat[ recId=" + recId + " ]";
    }

}
