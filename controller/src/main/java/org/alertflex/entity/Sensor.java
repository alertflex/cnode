/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alertflex.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
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
@Table(name = "sensor")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Sensor.findAll", query = "SELECT s FROM Sensor s"),
    @NamedQuery(name = "Sensor.findByRefId", query = "SELECT s FROM Sensor s WHERE s.sensorPK.refId = :refId"),
    @NamedQuery(name = "Sensor.findByNode", query = "SELECT s FROM Sensor s WHERE s.sensorPK.node = :node"),
    @NamedQuery(name = "Sensor.findByProbe", query = "SELECT s FROM Sensor s WHERE s.probe = :probe"),
    @NamedQuery(name = "Sensor.findByName", query = "SELECT s FROM Sensor s WHERE s.sensorPK.name = :name"),
    @NamedQuery(name = "Sensor.findByDescription", query = "SELECT s FROM Sensor s WHERE s.description = :description"),
    @NamedQuery(name = "Sensor.findBySensorType", query = "SELECT s FROM Sensor s WHERE s.sensorType = :sensorType"),
    @NamedQuery(name = "Sensor.findByStatus", query = "SELECT s FROM Sensor s WHERE s.status = :status")})
public class Sensor implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected SensorPK sensorPK;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "probe")
    private String probe;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "sensor_type")
    private String sensorType;
    @Basic(optional = false)
    @NotNull
    @Column(name = "status")
    private int status;

    public Sensor() {
    }

    public Sensor(SensorPK sensorPK) {
        this.sensorPK = sensorPK;
    }

    public Sensor(SensorPK sensorPK, String probe, String description, String sensorType, int status) {
        this.sensorPK = sensorPK;
        this.probe = probe;
        this.description = description;
        this.sensorType = sensorType;
        this.status = status;
    }

    public Sensor(String refId, String node, String name) {
        this.sensorPK = new SensorPK(refId, node, name);
    }

    public SensorPK getSensorPK() {
        return sensorPK;
    }

    public void setSensorPK(SensorPK sensorPK) {
        this.sensorPK = sensorPK;
    }

    public String getProbe() {
        return probe;
    }

    public void setProbe(String probe) {
        this.probe = probe;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSensorType() {
        return sensorType;
    }

    public void setSensorType(String sensorType) {
        this.sensorType = sensorType;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (sensorPK != null ? sensorPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Sensor)) {
            return false;
        }
        Sensor other = (Sensor) object;
        if ((this.sensorPK == null && other.sensorPK != null) || (this.sensorPK != null && !this.sensorPK.equals(other.sensorPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.alertflex.entity.Sensor[ sensorPK=" + sensorPK + " ]";
    }
    
}
