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
    @NamedQuery(name = "Sensor.findAll", query = "SELECT s FROM Sensor s")
    , @NamedQuery(name = "Sensor.findByRefId", query = "SELECT s FROM Sensor s WHERE s.sensorPK.refId = :refId")
    , @NamedQuery(name = "Sensor.findByNode", query = "SELECT s FROM Sensor s WHERE s.sensorPK.node = :node")
    , @NamedQuery(name = "Sensor.findByProbe", query = "SELECT s FROM Sensor s WHERE s.probe = :probe")
    , @NamedQuery(name = "Sensor.findByName", query = "SELECT s FROM Sensor s WHERE s.sensorPK.name = :name")
    , @NamedQuery(name = "Sensor.findByType", query = "SELECT s FROM Sensor s WHERE s.type = :type")
    , @NamedQuery(name = "Sensor.findByDescription", query = "SELECT s FROM Sensor s WHERE s.description = :description")
    , @NamedQuery(name = "Sensor.findByHost", query = "SELECT s FROM Sensor s WHERE s.host = :host")
    , @NamedQuery(name = "Sensor.findByRulesUpdate", query = "SELECT s FROM Sensor s WHERE s.rulesUpdate = :rulesUpdate")})
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
    @Size(min = 1, max = 64)
    @Column(name = "type")
    private String type;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "host")
    private String host;
    @Basic(optional = false)
    @NotNull
    @Column(name = "rules_update")
    private int rulesUpdate;

    public Sensor() {
    }

    public Sensor(SensorPK sensorPK) {
        this.sensorPK = sensorPK;
    }

    public Sensor(SensorPK sensorPK, String probe, String type, String description, String host, int rulesUpdate) {
        this.sensorPK = sensorPK;
        this.probe = probe;
        this.type = type;
        this.description = description;
        this.host = host;
        this.rulesUpdate = rulesUpdate;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public int getRulesUpdate() {
        return rulesUpdate;
    }

    public void setRulesUpdate(int rulesUpdate) {
        this.rulesUpdate = rulesUpdate;
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
