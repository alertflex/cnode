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
@Table(name = "posture_task")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PostureTask.findAll", query = "SELECT p FROM PostureTask p"),
    @NamedQuery(name = "PostureTask.findByRecId", query = "SELECT p FROM PostureTask p WHERE p.recId = :recId"),
    @NamedQuery(name = "PostureTask.findByRefId", query = "SELECT p FROM PostureTask p WHERE p.refId = :refId"),
    @NamedQuery(name = "PostureTask.findByTaskUuid", query = "SELECT p FROM PostureTask p WHERE p.taskUuid = :taskUuid"),
    @NamedQuery(name = "PostureTask.findByNode", query = "SELECT p FROM PostureTask p WHERE p.node = :node"),
    @NamedQuery(name = "PostureTask.findByProbe", query = "SELECT p FROM PostureTask p WHERE p.probe = :probe"),
    @NamedQuery(name = "PostureTask.findByPostureType", query = "SELECT p FROM PostureTask p WHERE p.postureType = :postureType"),
    @NamedQuery(name = "PostureTask.findByTarget", query = "SELECT p FROM PostureTask p WHERE p.target = :target"),
    @NamedQuery(name = "PostureTask.findByReportAdded", query = "SELECT p FROM PostureTask p WHERE p.reportAdded = :reportAdded")})
public class PostureTask implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "rec_id")
    private Integer recId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "ref_id")
    private String refId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "task_uuid")
    private String taskUuid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "node")
    private String node;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "probe")
    private String probe;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "posture_type")
    private String postureType;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "target")
    private String target;
    @Column(name = "report_added")
    @Temporal(TemporalType.TIMESTAMP)
    private Date reportAdded;

    public PostureTask() {
    }

    public PostureTask(Integer recId) {
        this.recId = recId;
    }

    public PostureTask(Integer recId, String refId, String taskUuid, String node, String probe, String postureType, String target) {
        this.recId = recId;
        this.refId = refId;
        this.taskUuid = taskUuid;
        this.node = node;
        this.probe = probe;
        this.postureType = postureType;
        this.target = target;
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

    public String getTaskUuid() {
        return taskUuid;
    }

    public void setTaskUuid(String taskUuid) {
        this.taskUuid = taskUuid;
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

    public String getPostureType() {
        return postureType;
    }

    public void setPostureType(String postureType) {
        this.postureType = postureType;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public Date getReportAdded() {
        return reportAdded;
    }

    public void setReportAdded(Date reportAdded) {
        this.reportAdded = reportAdded;
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
        if (!(object instanceof PostureTask)) {
            return false;
        }
        PostureTask other = (PostureTask) object;
        if ((this.recId == null && other.recId != null) || (this.recId != null && !this.recId.equals(other.recId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.alertflex.facade.PostureTask[ recId=" + recId + " ]";
    }
    
}
