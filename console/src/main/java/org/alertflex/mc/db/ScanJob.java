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
@Table(name = "scan_job")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ScanJob.findAll", query = "SELECT s FROM ScanJob s")
    , @NamedQuery(name = "ScanJob.findByRecId", query = "SELECT s FROM ScanJob s WHERE s.recId = :recId")
    , @NamedQuery(name = "ScanJob.findByName", query = "SELECT s FROM ScanJob s WHERE s.name = :name")
    , @NamedQuery(name = "ScanJob.findByPlaybook", query = "SELECT s FROM ScanJob s WHERE s.playbook = :playbook")
    , @NamedQuery(name = "ScanJob.findByPlayId", query = "SELECT s FROM ScanJob s WHERE s.playId = :playId")
    , @NamedQuery(name = "ScanJob.findByRefId", query = "SELECT s FROM ScanJob s WHERE s.refId = :refId")
    , @NamedQuery(name = "ScanJob.findByScanType", query = "SELECT s FROM ScanJob s WHERE s.scanType = :scanType")
    , @NamedQuery(name = "ScanJob.findByValue1", query = "SELECT s FROM ScanJob s WHERE s.value1 = :value1")
    , @NamedQuery(name = "ScanJob.findByValue2", query = "SELECT s FROM ScanJob s WHERE s.value2 = :value2")
    , @NamedQuery(name = "ScanJob.findByValue3", query = "SELECT s FROM ScanJob s WHERE s.value3 = :value3")
    , @NamedQuery(name = "ScanJob.findByDescription", query = "SELECT s FROM ScanJob s WHERE s.description = :description")
    , @NamedQuery(name = "ScanJob.findByErrorExit", query = "SELECT s FROM ScanJob s WHERE s.errorExit = :errorExit")})
public class ScanJob implements Serializable {

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
    @Size(min = 1, max = 255)
    @Column(name = "scan_type")
    private String scanType;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "value1")
    private String value1;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "value2")
    private String value2;
    @Basic(optional = false)
    @NotNull
    @Column(name = "value3")
    private int value3;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @NotNull
    @Column(name = "error_exit")
    private int errorExit;

    public ScanJob() {
    }

    public ScanJob(Integer recId) {
        this.recId = recId;
    }

    public ScanJob(Integer recId, String name, String playbook, int playId, String refId, String scanType, String value1, String value2, int value3, String description, int errorExit) {
        this.recId = recId;
        this.name = name;
        this.playbook = playbook;
        this.playId = playId;
        this.refId = refId;
        this.scanType = scanType;
        this.value1 = value1;
        this.value2 = value2;
        this.value3 = value3;
        this.description = description;
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

    public String getScanType() {
        return scanType;
    }

    public void setScanType(String scanType) {
        this.scanType = scanType;
    }

    public String getValue1() {
        return value1;
    }

    public void setValue1(String value1) {
        this.value1 = value1;
    }

    public String getValue2() {
        return value2;
    }

    public void setValue2(String value2) {
        this.value2 = value2;
    }

    public int getValue3() {
        return value3;
    }

    public void setValue3(int value3) {
        this.value3 = value3;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
        if (!(object instanceof ScanJob)) {
            return false;
        }
        ScanJob other = (ScanJob) object;
        if ((this.recId == null && other.recId != null) || (this.recId != null && !this.recId.equals(other.recId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.alertflex.ids.db.ScanJob[ recId=" + recId + " ]";
    }
    
}
