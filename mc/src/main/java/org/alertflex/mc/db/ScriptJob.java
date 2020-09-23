/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alertflex.mc.db;

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

/**
 *
 * @author root
 */
@Entity
@Table(name = "script_job")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ScriptJob.findAll", query = "SELECT s FROM ScriptJob s")
    , @NamedQuery(name = "ScriptJob.findByRecId", query = "SELECT s FROM ScriptJob s WHERE s.recId = :recId")
    , @NamedQuery(name = "ScriptJob.findByName", query = "SELECT s FROM ScriptJob s WHERE s.name = :name")
    , @NamedQuery(name = "ScriptJob.findByPlaybook", query = "SELECT s FROM ScriptJob s WHERE s.playbook = :playbook")
    , @NamedQuery(name = "ScriptJob.findByPlayId", query = "SELECT s FROM ScriptJob s WHERE s.playId = :playId")
    , @NamedQuery(name = "ScriptJob.findByRefId", query = "SELECT s FROM ScriptJob s WHERE s.refId = :refId")
    , @NamedQuery(name = "ScriptJob.findByTimerange", query = "SELECT s FROM ScriptJob s WHERE s.timerange = :timerange")
    , @NamedQuery(name = "ScriptJob.findByDescription", query = "SELECT s FROM ScriptJob s WHERE s.description = :description")
    , @NamedQuery(name = "ScriptJob.findByHost", query = "SELECT s FROM ScriptJob s WHERE s.host = :host")
    , @NamedQuery(name = "ScriptJob.findByScriptParams", query = "SELECT s FROM ScriptJob s WHERE s.scriptParams = :scriptParams")
    , @NamedQuery(name = "ScriptJob.findByScriptName", query = "SELECT s FROM ScriptJob s WHERE s.scriptName = :scriptName")
    , @NamedQuery(name = "ScriptJob.findByWaitResult", query = "SELECT s FROM ScriptJob s WHERE s.waitResult = :waitResult")
    , @NamedQuery(name = "ScriptJob.findByErrorExit", query = "SELECT s FROM ScriptJob s WHERE s.errorExit = :errorExit")})
public class ScriptJob implements Serializable {

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
    @Column(name = "playbook")
    private String playbook;
    @Basic(optional = false)
    @NotNull
    @Column(name = "play_id")
    private int playId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "ref_id")
    private String refId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "timerange")
    private int timerange;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "host")
    private String host;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1024)
    @Column(name = "script_params")
    private String scriptParams;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "script_name")
    private String scriptName;
    @Lob
    @Size(max = 65535)
    @Column(name = "script")
    private String script;
    @Basic(optional = false)
    @NotNull
    @Column(name = "wait_result")
    private int waitResult;
    @Basic(optional = false)
    @NotNull
    @Column(name = "error_exit")
    private int errorExit;

    public ScriptJob() {
    }

    public ScriptJob(Integer recId) {
        this.recId = recId;
    }

    public ScriptJob(Integer recId, String name, String playbook, int playId, String refId, int timerange, String description, String host, String scriptParams, String scriptName, int waitResult, int errorExit) {
        this.recId = recId;
        this.name = name;
        this.playbook = playbook;
        this.playId = playId;
        this.refId = refId;
        this.timerange = timerange;
        this.description = description;
        this.host = host;
        this.scriptParams = scriptParams;
        this.scriptName = scriptName;
        this.waitResult = waitResult;
        this.errorExit = errorExit;
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

    public String getPlaybook() {
        return playbook;
    }

    public void setPlaybook(String playbook) {
        this.playbook = playbook;
    }

    public int getPlayId() {
        return playId;
    }

    public void setPlayId(int playId) {
        this.playId = playId;
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public int getTimerange() {
        return timerange;
    }

    public void setTimerange(int timerange) {
        this.timerange = timerange;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getScriptParams() {
        return scriptParams;
    }

    public void setScriptParams(String scriptParams) {
        this.scriptParams = scriptParams;
    }

    public String getScriptName() {
        return scriptName;
    }

    public void setScriptName(String scriptName) {
        this.scriptName = scriptName;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public int getWaitResult() {
        return waitResult;
    }

    public void setWaitResult(int waitResult) {
        this.waitResult = waitResult;
    }

    public int getErrorExit() {
        return errorExit;
    }

    public void setErrorExit(int errorExit) {
        this.errorExit = errorExit;
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
        if (!(object instanceof ScriptJob)) {
            return false;
        }
        ScriptJob other = (ScriptJob) object;
        if ((this.recId == null && other.recId != null) || (this.recId != null && !this.recId.equals(other.recId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.alertflex.mc.db.ScriptJob[ recId=" + recId + " ]";
    }
    
}
