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
@Table(name = "agent_processes")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AgentProcesses.findAll", query = "SELECT a FROM AgentProcesses a")
    , @NamedQuery(name = "AgentProcesses.findByRecId", query = "SELECT a FROM AgentProcesses a WHERE a.recId = :recId")
    , @NamedQuery(name = "AgentProcesses.findByNodeId", query = "SELECT a FROM AgentProcesses a WHERE a.nodeId = :nodeId")
    , @NamedQuery(name = "AgentProcesses.findByRefId", query = "SELECT a FROM AgentProcesses a WHERE a.refId = :refId")
    , @NamedQuery(name = "AgentProcesses.findByAgent", query = "SELECT a FROM AgentProcesses a WHERE a.agent = :agent")
    , @NamedQuery(name = "AgentProcesses.findByUtime", query = "SELECT a FROM AgentProcesses a WHERE a.utime = :utime")
    , @NamedQuery(name = "AgentProcesses.findByProcessState", query = "SELECT a FROM AgentProcesses a WHERE a.processState = :processState")
    , @NamedQuery(name = "AgentProcesses.findByPriority", query = "SELECT a FROM AgentProcesses a WHERE a.priority = :priority")
    , @NamedQuery(name = "AgentProcesses.findByName", query = "SELECT a FROM AgentProcesses a WHERE a.name = :name")
    , @NamedQuery(name = "AgentProcesses.findByProcessShare", query = "SELECT a FROM AgentProcesses a WHERE a.processShare = :processShare")
    , @NamedQuery(name = "AgentProcesses.findBySuser", query = "SELECT a FROM AgentProcesses a WHERE a.suser = :suser")
    , @NamedQuery(name = "AgentProcesses.findByEgroup", query = "SELECT a FROM AgentProcesses a WHERE a.egroup = :egroup")
    , @NamedQuery(name = "AgentProcesses.findByNlwp", query = "SELECT a FROM AgentProcesses a WHERE a.nlwp = :nlwp")
    , @NamedQuery(name = "AgentProcesses.findByNice", query = "SELECT a FROM AgentProcesses a WHERE a.nice = :nice")
    , @NamedQuery(name = "AgentProcesses.findBySgroup", query = "SELECT a FROM AgentProcesses a WHERE a.sgroup = :sgroup")
    , @NamedQuery(name = "AgentProcesses.findByPpid", query = "SELECT a FROM AgentProcesses a WHERE a.ppid = :ppid")
    , @NamedQuery(name = "AgentProcesses.findByProcessor", query = "SELECT a FROM AgentProcesses a WHERE a.processor = :processor")
    , @NamedQuery(name = "AgentProcesses.findByPid", query = "SELECT a FROM AgentProcesses a WHERE a.pid = :pid")
    , @NamedQuery(name = "AgentProcesses.findByEuser", query = "SELECT a FROM AgentProcesses a WHERE a.euser = :euser")
    , @NamedQuery(name = "AgentProcesses.findByRuser", query = "SELECT a FROM AgentProcesses a WHERE a.ruser = :ruser")
    , @NamedQuery(name = "AgentProcesses.findByProcessSession", query = "SELECT a FROM AgentProcesses a WHERE a.processSession = :processSession")
    , @NamedQuery(name = "AgentProcesses.findByPgrp", query = "SELECT a FROM AgentProcesses a WHERE a.pgrp = :pgrp")
    , @NamedQuery(name = "AgentProcesses.findByStime", query = "SELECT a FROM AgentProcesses a WHERE a.stime = :stime")
    , @NamedQuery(name = "AgentProcesses.findByVmSize", query = "SELECT a FROM AgentProcesses a WHERE a.vmSize = :vmSize")
    , @NamedQuery(name = "AgentProcesses.findByTgid", query = "SELECT a FROM AgentProcesses a WHERE a.tgid = :tgid")
    , @NamedQuery(name = "AgentProcesses.findByTty", query = "SELECT a FROM AgentProcesses a WHERE a.tty = :tty")
    , @NamedQuery(name = "AgentProcesses.findByRgroup", query = "SELECT a FROM AgentProcesses a WHERE a.rgroup = :rgroup")
    , @NamedQuery(name = "AgentProcesses.findByProcessSize", query = "SELECT a FROM AgentProcesses a WHERE a.processSize = :processSize")
    , @NamedQuery(name = "AgentProcesses.findByResident", query = "SELECT a FROM AgentProcesses a WHERE a.resident = :resident")
    , @NamedQuery(name = "AgentProcesses.findByFgroup", query = "SELECT a FROM AgentProcesses a WHERE a.fgroup = :fgroup")
    , @NamedQuery(name = "AgentProcesses.findByCmd", query = "SELECT a FROM AgentProcesses a WHERE a.cmd = :cmd")
    , @NamedQuery(name = "AgentProcesses.findByStartTime", query = "SELECT a FROM AgentProcesses a WHERE a.startTime = :startTime")
    , @NamedQuery(name = "AgentProcesses.findByTimeScan", query = "SELECT a FROM AgentProcesses a WHERE a.timeScan = :timeScan")
    , @NamedQuery(name = "AgentProcesses.findByDateAdd", query = "SELECT a FROM AgentProcesses a WHERE a.dateAdd = :dateAdd")
    , @NamedQuery(name = "AgentProcesses.findByDateUpdate", query = "SELECT a FROM AgentProcesses a WHERE a.dateUpdate = :dateUpdate")})
public class AgentProcesses implements Serializable {

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
    @Size(min = 1, max = 128)
    @Column(name = "agent")
    private String agent;
    @Basic(optional = false)
    @NotNull
    @Column(name = "utime")
    private int utime;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "process_state")
    private String processState;
    @Basic(optional = false)
    @NotNull
    @Column(name = "priority")
    private int priority;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1024)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @NotNull
    @Column(name = "process_share")
    private int processShare;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "suser")
    private String suser;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "egroup")
    private String egroup;
    @Basic(optional = false)
    @NotNull
    @Column(name = "nlwp")
    private int nlwp;
    @Basic(optional = false)
    @NotNull
    @Column(name = "nice")
    private int nice;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "sgroup")
    private String sgroup;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ppid")
    private int ppid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "processor")
    private int processor;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "pid")
    private String pid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "euser")
    private String euser;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "ruser")
    private String ruser;
    @Basic(optional = false)
    @NotNull
    @Column(name = "process_session")
    private int processSession;
    @Basic(optional = false)
    @NotNull
    @Column(name = "pgrp")
    private int pgrp;
    @Basic(optional = false)
    @NotNull
    @Column(name = "stime")
    private int stime;
    @Basic(optional = false)
    @NotNull
    @Column(name = "vm_size")
    private long vmSize;
    @Basic(optional = false)
    @NotNull
    @Column(name = "tgid")
    private int tgid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "tty")
    private int tty;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "rgroup")
    private String rgroup;
    @Basic(optional = false)
    @NotNull
    @Column(name = "process_size")
    private long processSize;
    @Basic(optional = false)
    @NotNull
    @Column(name = "resident")
    private int resident;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "fgroup")
    private String fgroup;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2056)
    @Column(name = "cmd")
    private String cmd;
    @Basic(optional = false)
    @NotNull
    @Column(name = "start_time")
    private int startTime;
    @Column(name = "time_scan")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeScan;
    @Column(name = "date_add")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateAdd;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;

    public AgentProcesses() {
    }

    public AgentProcesses(Long recId) {
        this.recId = recId;
    }

    public AgentProcesses(Long recId, String nodeId, String refId, String agent, int utime, String processState, int priority, String name, int processShare, String suser, String egroup, int nlwp, int nice, String sgroup, int ppid, int processor, String pid, String euser, String ruser, int processSession, int pgrp, int stime, long vmSize, int tgid, int tty, String rgroup, long processSize, int resident, String fgroup, String cmd, int startTime) {
        this.recId = recId;
        this.nodeId = nodeId;
        this.refId = refId;
        this.agent = agent;
        this.utime = utime;
        this.processState = processState;
        this.priority = priority;
        this.name = name;
        this.processShare = processShare;
        this.suser = suser;
        this.egroup = egroup;
        this.nlwp = nlwp;
        this.nice = nice;
        this.sgroup = sgroup;
        this.ppid = ppid;
        this.processor = processor;
        this.pid = pid;
        this.euser = euser;
        this.ruser = ruser;
        this.processSession = processSession;
        this.pgrp = pgrp;
        this.stime = stime;
        this.vmSize = vmSize;
        this.tgid = tgid;
        this.tty = tty;
        this.rgroup = rgroup;
        this.processSize = processSize;
        this.resident = resident;
        this.fgroup = fgroup;
        this.cmd = cmd;
        this.startTime = startTime;
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

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public int getUtime() {
        return utime;
    }

    public void setUtime(int utime) {
        this.utime = utime;
    }

    public String getProcessState() {
        return processState;
    }

    public void setProcessState(String processState) {
        this.processState = processState;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getProcessShare() {
        return processShare;
    }

    public void setProcessShare(int processShare) {
        this.processShare = processShare;
    }

    public String getSuser() {
        return suser;
    }

    public void setSuser(String suser) {
        this.suser = suser;
    }

    public String getEgroup() {
        return egroup;
    }

    public void setEgroup(String egroup) {
        this.egroup = egroup;
    }

    public int getNlwp() {
        return nlwp;
    }

    public void setNlwp(int nlwp) {
        this.nlwp = nlwp;
    }

    public int getNice() {
        return nice;
    }

    public void setNice(int nice) {
        this.nice = nice;
    }

    public String getSgroup() {
        return sgroup;
    }

    public void setSgroup(String sgroup) {
        this.sgroup = sgroup;
    }

    public int getPpid() {
        return ppid;
    }

    public void setPpid(int ppid) {
        this.ppid = ppid;
    }

    public int getProcessor() {
        return processor;
    }

    public void setProcessor(int processor) {
        this.processor = processor;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getEuser() {
        return euser;
    }

    public void setEuser(String euser) {
        this.euser = euser;
    }

    public String getRuser() {
        return ruser;
    }

    public void setRuser(String ruser) {
        this.ruser = ruser;
    }

    public int getProcessSession() {
        return processSession;
    }

    public void setProcessSession(int processSession) {
        this.processSession = processSession;
    }

    public int getPgrp() {
        return pgrp;
    }

    public void setPgrp(int pgrp) {
        this.pgrp = pgrp;
    }

    public int getStime() {
        return stime;
    }

    public void setStime(int stime) {
        this.stime = stime;
    }

    public long getVmSize() {
        return vmSize;
    }

    public void setVmSize(long vmSize) {
        this.vmSize = vmSize;
    }

    public int getTgid() {
        return tgid;
    }

    public void setTgid(int tgid) {
        this.tgid = tgid;
    }

    public int getTty() {
        return tty;
    }

    public void setTty(int tty) {
        this.tty = tty;
    }

    public String getRgroup() {
        return rgroup;
    }

    public void setRgroup(String rgroup) {
        this.rgroup = rgroup;
    }

    public long getProcessSize() {
        return processSize;
    }

    public void setProcessSize(long processSize) {
        this.processSize = processSize;
    }

    public int getResident() {
        return resident;
    }

    public void setResident(int resident) {
        this.resident = resident;
    }

    public String getFgroup() {
        return fgroup;
    }

    public void setFgroup(String fgroup) {
        this.fgroup = fgroup;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public Date getTimeScan() {
        return timeScan;
    }

    public void setTimeScan(Date timeScan) {
        this.timeScan = timeScan;
    }

    public Date getDateAdd() {
        return dateAdd;
    }

    public void setDateAdd(Date dateAdd) {
        this.dateAdd = dateAdd;
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
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
        if (!(object instanceof AgentProcesses)) {
            return false;
        }
        AgentProcesses other = (AgentProcesses) object;
        if ((this.recId == null && other.recId != null) || (this.recId != null && !this.recId.equals(other.recId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.alertflex.entity.AgentProcesses[ recId=" + recId + " ]";
    }
    
}
