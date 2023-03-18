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
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
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
@Table(name = "probe")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Probe.findAll", query = "SELECT p FROM Probe p"),
    @NamedQuery(name = "Probe.findByRefId", query = "SELECT p FROM Probe p WHERE p.probePK.refId = :refId"),
    @NamedQuery(name = "Probe.findByNode", query = "SELECT p FROM Probe p WHERE p.probePK.node = :node"),
    @NamedQuery(name = "Probe.findByName", query = "SELECT p FROM Probe p WHERE p.probePK.name = :name"),
    @NamedQuery(name = "Probe.findByHostName", query = "SELECT p FROM Probe p WHERE p.hostName = :hostName"),
    @NamedQuery(name = "Probe.findByDescription", query = "SELECT p FROM Probe p WHERE p.description = :description"),
    @NamedQuery(name = "Probe.findByProbeType", query = "SELECT p FROM Probe p WHERE p.probeType = :probeType"),
    @NamedQuery(name = "Probe.findByStatus", query = "SELECT p FROM Probe p WHERE p.status = :status"),
    @NamedQuery(name = "Probe.findByStatusUpdated", query = "SELECT p FROM Probe p WHERE p.statusUpdated = :statusUpdated")})
public class Probe implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ProbePK probePK;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "host_name")
    private String hostName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "probe_type")
    private String probeType;
    @Basic(optional = false)
    @NotNull
    @Column(name = "status")
    private int status;
    @Column(name = "status_updated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date statusUpdated;

    public Probe() {
    }

    public Probe(ProbePK probePK) {
        this.probePK = probePK;
    }

    public Probe(ProbePK probePK, String hostName, String description, String probeType, int status) {
        this.probePK = probePK;
        this.hostName = hostName;
        this.description = description;
        this.probeType = probeType;
        this.status = status;
    }

    public Probe(String refId, String node, String name) {
        this.probePK = new ProbePK(refId, node, name);
    }

    public ProbePK getProbePK() {
        return probePK;
    }

    public void setProbePK(ProbePK probePK) {
        this.probePK = probePK;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProbeType() {
        return probeType;
    }

    public void setProbeType(String probeType) {
        this.probeType = probeType;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getStatusUpdated() {
        return statusUpdated;
    }

    public void setStatusUpdated(Date statusUpdated) {
        this.statusUpdated = statusUpdated;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (probePK != null ? probePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Probe)) {
            return false;
        }
        Probe other = (Probe) object;
        if ((this.probePK == null && other.probePK != null) || (this.probePK != null && !this.probePK.equals(other.probePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.alertflex.entity.Probe[ probePK=" + probePK + " ]";
    }
    
}
