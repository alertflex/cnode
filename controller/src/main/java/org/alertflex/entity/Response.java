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
@Table(name = "response")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Response.findAll", query = "SELECT r FROM Response r")
    , @NamedQuery(name = "Response.findByRecId", query = "SELECT r FROM Response r WHERE r.recId = :recId")
    , @NamedQuery(name = "Response.findByResId", query = "SELECT r FROM Response r WHERE r.resId = :resId")
    , @NamedQuery(name = "Response.findByRefId", query = "SELECT r FROM Response r WHERE r.refId = :refId")
    , @NamedQuery(name = "Response.findByStatus", query = "SELECT r FROM Response r WHERE r.status = :status")
    , @NamedQuery(name = "Response.findByNode", query = "SELECT r FROM Response r WHERE r.node = :node")
    , @NamedQuery(name = "Response.findByUserid", query = "SELECT r FROM Response r WHERE r.userid = :userid")
    , @NamedQuery(name = "Response.findByResType", query = "SELECT r FROM Response r WHERE r.resType = :resType")
    , @NamedQuery(name = "Response.findByResCause", query = "SELECT r FROM Response r WHERE r.resCause = :resCause")
    , @NamedQuery(name = "Response.findByAlertSource", query = "SELECT r FROM Response r WHERE r.alertSource = :alertSource")
    , @NamedQuery(name = "Response.findByAlertSeverity", query = "SELECT r FROM Response r WHERE r.alertSeverity = :alertSeverity")
    , @NamedQuery(name = "Response.findByAlertUser", query = "SELECT r FROM Response r WHERE r.alertUser = :alertUser")
    , @NamedQuery(name = "Response.findByAlertAgent", query = "SELECT r FROM Response r WHERE r.alertAgent = :alertAgent")
    , @NamedQuery(name = "Response.findByAlertContainer", query = "SELECT r FROM Response r WHERE r.alertContainer = :alertContainer")
    , @NamedQuery(name = "Response.findByAlertIp", query = "SELECT r FROM Response r WHERE r.alertIp = :alertIp")
    , @NamedQuery(name = "Response.findByAlertSensor", query = "SELECT r FROM Response r WHERE r.alertSensor = :alertSensor")
    , @NamedQuery(name = "Response.findByAlertFile", query = "SELECT r FROM Response r WHERE r.alertFile = :alertFile")
    , @NamedQuery(name = "Response.findByAlertProcess", query = "SELECT r FROM Response r WHERE r.alertProcess = :alertProcess")
    , @NamedQuery(name = "Response.findByAlertRegex", query = "SELECT r FROM Response r WHERE r.alertRegex = :alertRegex")
    , @NamedQuery(name = "Response.findByBeginHour", query = "SELECT r FROM Response r WHERE r.beginHour = :beginHour")
    , @NamedQuery(name = "Response.findByEndHour", query = "SELECT r FROM Response r WHERE r.endHour = :endHour")
    , @NamedQuery(name = "Response.findByCorrelation", query = "SELECT r FROM Response r WHERE r.correlation = :correlation")
    , @NamedQuery(name = "Response.findByAction", query = "SELECT r FROM Response r WHERE r.action = :action")
    , @NamedQuery(name = "Response.findByNotifyUsers", query = "SELECT r FROM Response r WHERE r.notifyUsers = :notifyUsers")
    , @NamedQuery(name = "Response.findByNotifyMsg", query = "SELECT r FROM Response r WHERE r.notifyMsg = :notifyMsg")
    , @NamedQuery(name = "Response.findBySendSlack", query = "SELECT r FROM Response r WHERE r.sendSlack = :sendSlack")
    , @NamedQuery(name = "Response.findByPlaybook", query = "SELECT r FROM Response r WHERE r.playbook = :playbook")})
public class Response implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "rec_id")
    private Integer recId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "res_id")
    private String resId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "ref_id")
    private String refId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "status")
    private int status;
    @Size(max = 255)
    @Column(name = "node")
    private String node;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "userid")
    private String userid;
    @Size(max = 128)
    @Column(name = "res_type")
    private String resType;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "res_cause")
    private String resCause;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "alert_source")
    private String alertSource;
    @Basic(optional = false)
    @NotNull
    @Column(name = "alert_severity")
    private int alertSeverity;
    @Size(max = 512)
    @Column(name = "alert_user")
    private String alertUser;
    @Size(max = 128)
    @Column(name = "alert_agent")
    private String alertAgent;
    @Size(max = 512)
    @Column(name = "alert_container")
    private String alertContainer;
    @Size(max = 128)
    @Column(name = "alert_ip")
    private String alertIp;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "alert_sensor")
    private String alertSensor;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "alert_file")
    private String alertFile;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1024)
    @Column(name = "alert_process")
    private String alertProcess;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1024)
    @Column(name = "alert_regex")
    private String alertRegex;
    @Basic(optional = false)
    @NotNull
    @Column(name = "begin_hour")
    private int beginHour;
    @Basic(optional = false)
    @NotNull
    @Column(name = "end_hour")
    private int endHour;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "correlation")
    private String correlation;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "action")
    private String action;
    @Size(max = 1024)
    @Column(name = "notify_users")
    private String notifyUsers;
    @Size(max = 512)
    @Column(name = "notify_msg")
    private String notifyMsg;
    @Basic(optional = false)
    @NotNull
    @Column(name = "send_slack")
    private int sendSlack;
    @Size(max = 255)
    @Column(name = "playbook")
    private String playbook;

    public Response() {
    }

    public Response(Integer recId) {
        this.recId = recId;
    }

    public Response(Integer recId, String resId, String refId, int status, String userid, String resCause, String alertSource, int alertSeverity, String alertSensor, String alertFile, String alertProcess, String alertRegex, int beginHour, int endHour, String correlation, String action, int sendSlack) {
        this.recId = recId;
        this.resId = resId;
        this.refId = refId;
        this.status = status;
        this.userid = userid;
        this.resCause = resCause;
        this.alertSource = alertSource;
        this.alertSeverity = alertSeverity;
        this.alertSensor = alertSensor;
        this.alertFile = alertFile;
        this.alertProcess = alertProcess;
        this.alertRegex = alertRegex;
        this.beginHour = beginHour;
        this.endHour = endHour;
        this.correlation = correlation;
        this.action = action;
        this.sendSlack = sendSlack;
    }

    public Integer getRecId() {
        return recId;
    }

    public void setRecId(Integer recId) {
        this.recId = recId;
    }

    public String getResId() {
        return resId;
    }

    public void setResId(String resId) {
        this.resId = resId;
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getResType() {
        return resType;
    }

    public void setResType(String resType) {
        this.resType = resType;
    }

    public String getResCause() {
        return resCause;
    }

    public void setResCause(String resCause) {
        this.resCause = resCause;
    }

    public String getAlertSource() {
        return alertSource;
    }

    public void setAlertSource(String alertSource) {
        this.alertSource = alertSource;
    }

    public int getAlertSeverity() {
        return alertSeverity;
    }

    public void setAlertSeverity(int alertSeverity) {
        this.alertSeverity = alertSeverity;
    }

    public String getAlertUser() {
        return alertUser;
    }

    public void setAlertUser(String alertUser) {
        this.alertUser = alertUser;
    }

    public String getAlertAgent() {
        return alertAgent;
    }

    public void setAlertAgent(String alertAgent) {
        this.alertAgent = alertAgent;
    }

    public String getAlertContainer() {
        return alertContainer;
    }

    public void setAlertContainer(String alertContainer) {
        this.alertContainer = alertContainer;
    }

    public String getAlertIp() {
        return alertIp;
    }

    public void setAlertIp(String alertIp) {
        this.alertIp = alertIp;
    }

    public String getAlertSensor() {
        return alertSensor;
    }

    public void setAlertSensor(String alertSensor) {
        this.alertSensor = alertSensor;
    }

    public String getAlertFile() {
        return alertFile;
    }

    public void setAlertFile(String alertFile) {
        this.alertFile = alertFile;
    }

    public String getAlertProcess() {
        return alertProcess;
    }

    public void setAlertProcess(String alertProcess) {
        this.alertProcess = alertProcess;
    }

    public String getAlertRegex() {
        return alertRegex;
    }

    public void setAlertRegex(String alertRegex) {
        this.alertRegex = alertRegex;
    }

    public int getBeginHour() {
        return beginHour;
    }

    public void setBeginHour(int beginHour) {
        this.beginHour = beginHour;
    }

    public int getEndHour() {
        return endHour;
    }

    public void setEndHour(int endHour) {
        this.endHour = endHour;
    }

    public String getCorrelation() {
        return correlation;
    }

    public void setCorrelation(String correlation) {
        this.correlation = correlation;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getNotifyUsers() {
        return notifyUsers;
    }

    public void setNotifyUsers(String notifyUsers) {
        this.notifyUsers = notifyUsers;
    }

    public String getNotifyMsg() {
        return notifyMsg;
    }

    public void setNotifyMsg(String notifyMsg) {
        this.notifyMsg = notifyMsg;
    }

    public int getSendSlack() {
        return sendSlack;
    }

    public void setSendSlack(int sendSlack) {
        this.sendSlack = sendSlack;
    }

    public String getPlaybook() {
        return playbook;
    }

    public void setPlaybook(String playbook) {
        this.playbook = playbook;
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
        if (!(object instanceof Response)) {
            return false;
        }
        Response other = (Response) object;
        if ((this.recId == null && other.recId != null) || (this.recId != null && !this.recId.equals(other.recId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.alertflex.entity.Response[ recId=" + recId + " ]";
    }
    
}
