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
@Table(name = "container")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Container.findAll", query = "SELECT c FROM Container c")
    , @NamedQuery(name = "Container.findByRecId", query = "SELECT c FROM Container c WHERE c.recId = :recId")
    , @NamedQuery(name = "Container.findByRefId", query = "SELECT c FROM Container c WHERE c.refId = :refId")
    , @NamedQuery(name = "Container.findByNodeId", query = "SELECT c FROM Container c WHERE c.nodeId = :nodeId")
    , @NamedQuery(name = "Container.findByProbe", query = "SELECT c FROM Container c WHERE c.probe = :probe")
    , @NamedQuery(name = "Container.findByContainerId", query = "SELECT c FROM Container c WHERE c.containerId = :containerId")
    , @NamedQuery(name = "Container.findByImageName", query = "SELECT c FROM Container c WHERE c.imageName = :imageName")
    , @NamedQuery(name = "Container.findByImageId", query = "SELECT c FROM Container c WHERE c.imageId = :imageId")
    , @NamedQuery(name = "Container.findByCommand", query = "SELECT c FROM Container c WHERE c.command = :command")
    , @NamedQuery(name = "Container.findByState", query = "SELECT c FROM Container c WHERE c.state = :state")
    , @NamedQuery(name = "Container.findByStatus", query = "SELECT c FROM Container c WHERE c.status = :status")
    , @NamedQuery(name = "Container.findByReportAdded", query = "SELECT c FROM Container c WHERE c.reportAdded = :reportAdded")
    , @NamedQuery(name = "Container.findByReportUpdated", query = "SELECT c FROM Container c WHERE c.reportUpdated = :reportUpdated")})
public class Container implements Serializable {

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
    @Size(min = 1, max = 512)
    @Column(name = "node_id")
    private String nodeId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "probe")
    private String probe;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "container_id")
    private String containerId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "image_name")
    private String imageName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "image_id")
    private String imageId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1024)
    @Column(name = "command")
    private String command;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "state")
    private String state;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "status")
    private String status;
    @Column(name = "report_added")
    @Temporal(TemporalType.TIMESTAMP)
    private Date reportAdded;
    @Column(name = "report_updated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date reportUpdated;

    public Container() {
    }

    public Container(Integer recId) {
        this.recId = recId;
    }

    public Container(Integer recId, String refId, String nodeId, String probe, String containerId, String imageName, String imageId, String command, String state, String status) {
        this.recId = recId;
        this.refId = refId;
        this.nodeId = nodeId;
        this.probe = probe;
        this.containerId = containerId;
        this.imageName = imageName;
        this.imageId = imageId;
        this.command = command;
        this.state = state;
        this.status = status;
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

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getProbe() {
        return probe;
    }

    public void setProbe(String probe) {
        this.probe = probe;
    }

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getReportAdded() {
        return reportAdded;
    }

    public void setReportAdded(Date reportAdded) {
        this.reportAdded = reportAdded;
    }

    public Date getReportUpdated() {
        return reportUpdated;
    }

    public void setReportUpdated(Date reportUpdated) {
        this.reportUpdated = reportUpdated;
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
        if (!(object instanceof Container)) {
            return false;
        }
        Container other = (Container) object;
        if ((this.recId == null && other.recId != null) || (this.recId != null && !this.recId.equals(other.recId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.alertflex.entity.Container[ recId=" + recId + " ]";
    }
    
}
