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
    @NamedQuery(name = "Sensor.findByName", query = "SELECT s FROM Sensor s WHERE s.sensorPK.name = :name"),
    @NamedQuery(name = "Sensor.findByNode", query = "SELECT s FROM Sensor s WHERE s.sensorPK.node = :node"),
    @NamedQuery(name = "Sensor.findByHostName", query = "SELECT s FROM Sensor s WHERE s.hostName = :hostName"),
    @NamedQuery(name = "Sensor.findByDescription", query = "SELECT s FROM Sensor s WHERE s.description = :description"),
    @NamedQuery(name = "Sensor.findBySensorType", query = "SELECT s FROM Sensor s WHERE s.sensorType = :sensorType"),
    @NamedQuery(name = "Sensor.findByAwsidsRulegroup", query = "SELECT s FROM Sensor s WHERE s.awsidsRulegroup = :awsidsRulegroup"),
    @NamedQuery(name = "Sensor.findByK8sPolicy", query = "SELECT s FROM Sensor s WHERE s.k8sPolicy = :k8sPolicy"),
    @NamedQuery(name = "Sensor.findBySuricataRule", query = "SELECT s FROM Sensor s WHERE s.suricataRule = :suricataRule"),
    @NamedQuery(name = "Sensor.findByStatus", query = "SELECT s FROM Sensor s WHERE s.status = :status")})
public class Sensor implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected SensorPK sensorPK;
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
    @Column(name = "sensor_type")
    private String sensorType;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "awsids_rulegroup")
    private String awsidsRulegroup;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "k8s_policy")
    private String k8sPolicy;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "suricata_rule")
    private String suricataRule;
    @Basic(optional = false)
    @NotNull
    @Column(name = "status")
    private int status;

    public Sensor() {
    }

    public Sensor(SensorPK sensorPK) {
        this.sensorPK = sensorPK;
    }

    public Sensor(SensorPK sensorPK, String hostName, String description, String sensorType, String awsidsRulegroup, String k8sPolicy, String suricataRule, int status) {
        this.sensorPK = sensorPK;
        this.hostName = hostName;
        this.description = description;
        this.sensorType = sensorType;
        this.awsidsRulegroup = awsidsRulegroup;
        this.k8sPolicy = k8sPolicy;
        this.suricataRule = suricataRule;
        this.status = status;
    }

    public Sensor(String refId, String name, String node) {
        this.sensorPK = new SensorPK(refId, name, node);
    }

    public SensorPK getSensorPK() {
        return sensorPK;
    }

    public void setSensorPK(SensorPK sensorPK) {
        this.sensorPK = sensorPK;
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

    public String getSensorType() {
        return sensorType;
    }

    public void setSensorType(String sensorType) {
        this.sensorType = sensorType;
    }

    public String getAwsidsRulegroup() {
        return awsidsRulegroup;
    }

    public void setAwsidsRulegroup(String awsidsRulegroup) {
        this.awsidsRulegroup = awsidsRulegroup;
    }

    public String getK8sPolicy() {
        return k8sPolicy;
    }

    public void setK8sPolicy(String k8sPolicy) {
        this.k8sPolicy = k8sPolicy;
    }

    public String getSuricataRule() {
        return suricataRule;
    }

    public void setSuricataRule(String suricataRule) {
        this.suricataRule = suricataRule;
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
