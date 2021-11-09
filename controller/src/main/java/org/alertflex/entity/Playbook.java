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
@Table(name = "playbook")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Playbook.findAll", query = "SELECT p FROM Playbook p"),
    @NamedQuery(name = "Playbook.findByRecId", query = "SELECT p FROM Playbook p WHERE p.recId = :recId"),
    @NamedQuery(name = "Playbook.findByName", query = "SELECT p FROM Playbook p WHERE p.name = :name"),
    @NamedQuery(name = "Playbook.findByRefId", query = "SELECT p FROM Playbook p WHERE p.refId = :refId"),
    @NamedQuery(name = "Playbook.findByDescription", query = "SELECT p FROM Playbook p WHERE p.description = :description"),
    @NamedQuery(name = "Playbook.findByWebhook", query = "SELECT p FROM Playbook p WHERE p.webhook = :webhook"),
    @NamedQuery(name = "Playbook.findByTimerange", query = "SELECT p FROM Playbook p WHERE p.timerange = :timerange"),
    @NamedQuery(name = "Playbook.findByNotifyUsers", query = "SELECT p FROM Playbook p WHERE p.notifyUsers = :notifyUsers"),
    @NamedQuery(name = "Playbook.findByMsgSuccess", query = "SELECT p FROM Playbook p WHERE p.msgSuccess = :msgSuccess"),
    @NamedQuery(name = "Playbook.findByMsgError", query = "SELECT p FROM Playbook p WHERE p.msgError = :msgError"),
    @NamedQuery(name = "Playbook.findBySlackChannel", query = "SELECT p FROM Playbook p WHERE p.slackChannel = :slackChannel"),
    @NamedQuery(name = "Playbook.findByEnable", query = "SELECT p FROM Playbook p WHERE p.enable = :enable"),
    @NamedQuery(name = "Playbook.findByNumRuns", query = "SELECT p FROM Playbook p WHERE p.numRuns = :numRuns"),
    @NamedQuery(name = "Playbook.findByNumErrors", query = "SELECT p FROM Playbook p WHERE p.numErrors = :numErrors"),
    @NamedQuery(name = "Playbook.findByJobError", query = "SELECT p FROM Playbook p WHERE p.jobError = :jobError"),
    @NamedQuery(name = "Playbook.findByTimeOfRun", query = "SELECT p FROM Playbook p WHERE p.timeOfRun = :timeOfRun"),
    @NamedQuery(name = "Playbook.findByCpNames", query = "SELECT p FROM Playbook p WHERE p.cpNames = :cpNames")})
public class Playbook implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "rec_id")
    private Integer recId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "ref_id")
    private String refId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "webhook")
    private String webhook;
    @Basic(optional = false)
    @NotNull
    @Column(name = "timerange")
    private int timerange;
    @Size(max = 1024)
    @Column(name = "notify_users")
    private String notifyUsers;
    @Size(max = 512)
    @Column(name = "msg_success")
    private String msgSuccess;
    @Size(max = 512)
    @Column(name = "msg_error")
    private String msgError;
    @Size(max = 512)
    @Column(name = "slack_channel")
    private String slackChannel;
    @Basic(optional = false)
    @NotNull
    @Column(name = "enable")
    private int enable;
    @Basic(optional = false)
    @NotNull
    @Column(name = "num_runs")
    private int numRuns;
    @Basic(optional = false)
    @NotNull
    @Column(name = "num_errors")
    private int numErrors;
    @Basic(optional = false)
    @NotNull
    @Column(name = "job_error")
    private int jobError;
    @Column(name = "time_of_run")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeOfRun;
    @Size(max = 255)
    @Column(name = "cp_names")
    private String cpNames;

    public Playbook() {
    }

    public Playbook(Integer recId) {
        this.recId = recId;
    }

    public Playbook(Integer recId, String name, String refId, String description, String webhook, int timerange, int enable, int numRuns, int numErrors, int jobError) {
        this.recId = recId;
        this.name = name;
        this.refId = refId;
        this.description = description;
        this.webhook = webhook;
        this.timerange = timerange;
        this.enable = enable;
        this.numRuns = numRuns;
        this.numErrors = numErrors;
        this.jobError = jobError;
    }

    public Integer getRecId() {
        return recId;
    }

    public void setRecId(Integer recId) {
        this.recId = recId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWebhook() {
        return webhook;
    }

    public void setWebhook(String webhook) {
        this.webhook = webhook;
    }

    public int getTimerange() {
        return timerange;
    }

    public void setTimerange(int timerange) {
        this.timerange = timerange;
    }

    public String getNotifyUsers() {
        return notifyUsers;
    }

    public void setNotifyUsers(String notifyUsers) {
        this.notifyUsers = notifyUsers;
    }

    public String getMsgSuccess() {
        return msgSuccess;
    }

    public void setMsgSuccess(String msgSuccess) {
        this.msgSuccess = msgSuccess;
    }

    public String getMsgError() {
        return msgError;
    }

    public void setMsgError(String msgError) {
        this.msgError = msgError;
    }

    public String getSlackChannel() {
        return slackChannel;
    }

    public void setSlackChannel(String slackChannel) {
        this.slackChannel = slackChannel;
    }

    public int getEnable() {
        return enable;
    }

    public void setEnable(int enable) {
        this.enable = enable;
    }

    public int getNumRuns() {
        return numRuns;
    }

    public void setNumRuns(int numRuns) {
        this.numRuns = numRuns;
    }

    public int getNumErrors() {
        return numErrors;
    }

    public void setNumErrors(int numErrors) {
        this.numErrors = numErrors;
    }

    public int getJobError() {
        return jobError;
    }

    public void setJobError(int jobError) {
        this.jobError = jobError;
    }

    public Date getTimeOfRun() {
        return timeOfRun;
    }

    public void setTimeOfRun(Date timeOfRun) {
        this.timeOfRun = timeOfRun;
    }

    public String getCpNames() {
        return cpNames;
    }

    public void setCpNames(String cpNames) {
        this.cpNames = cpNames;
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
        if (!(object instanceof Playbook)) {
            return false;
        }
        Playbook other = (Playbook) object;
        if ((this.recId == null && other.recId != null) || (this.recId != null && !this.recId.equals(other.recId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.alertflex.entity.Playbook[ recId=" + recId + " ]";
    }
    
}
