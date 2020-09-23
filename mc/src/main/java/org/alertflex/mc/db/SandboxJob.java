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
@Table(name = "sandbox_job")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SandboxJob.findAll", query = "SELECT s FROM SandboxJob s")
    , @NamedQuery(name = "SandboxJob.findByRecId", query = "SELECT s FROM SandboxJob s WHERE s.recId = :recId")
    , @NamedQuery(name = "SandboxJob.findByName", query = "SELECT s FROM SandboxJob s WHERE s.name = :name")
    , @NamedQuery(name = "SandboxJob.findByPlaybook", query = "SELECT s FROM SandboxJob s WHERE s.playbook = :playbook")
    , @NamedQuery(name = "SandboxJob.findByPlayId", query = "SELECT s FROM SandboxJob s WHERE s.playId = :playId")
    , @NamedQuery(name = "SandboxJob.findByRefId", query = "SELECT s FROM SandboxJob s WHERE s.refId = :refId")
    , @NamedQuery(name = "SandboxJob.findBySensorType", query = "SELECT s FROM SandboxJob s WHERE s.sensorType = :sensorType")
    , @NamedQuery(name = "SandboxJob.findBySandboxType", query = "SELECT s FROM SandboxJob s WHERE s.sandboxType = :sandboxType")
    , @NamedQuery(name = "SandboxJob.findByHostName", query = "SELECT s FROM SandboxJob s WHERE s.hostName = :hostName")
    , @NamedQuery(name = "SandboxJob.findByTimerange", query = "SELECT s FROM SandboxJob s WHERE s.timerange = :timerange")
    , @NamedQuery(name = "SandboxJob.findByDescription", query = "SELECT s FROM SandboxJob s WHERE s.description = :description")
    , @NamedQuery(name = "SandboxJob.findByFilePath", query = "SELECT s FROM SandboxJob s WHERE s.filePath = :filePath")
    , @NamedQuery(name = "SandboxJob.findByFileExt", query = "SELECT s FROM SandboxJob s WHERE s.fileExt = :fileExt")
    , @NamedQuery(name = "SandboxJob.findByFilesLimit", query = "SELECT s FROM SandboxJob s WHERE s.filesLimit = :filesLimit")
    , @NamedQuery(name = "SandboxJob.findByDelFile", query = "SELECT s FROM SandboxJob s WHERE s.delFile = :delFile")
    , @NamedQuery(name = "SandboxJob.findByDelInfect", query = "SELECT s FROM SandboxJob s WHERE s.delInfect = :delInfect")
    , @NamedQuery(name = "SandboxJob.findByErrorExit", query = "SELECT s FROM SandboxJob s WHERE s.errorExit = :errorExit")})
public class SandboxJob implements Serializable {

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
    @Column(name = "sensor_type")
    private String sensorType;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "sandbox_type")
    private String sandboxType;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "host_name")
    private String hostName;
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
    @Size(min = 1, max = 512)
    @Column(name = "file_path")
    private String filePath;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1024)
    @Column(name = "file_ext")
    private String fileExt;
    @Basic(optional = false)
    @NotNull
    @Column(name = "files_limit")
    private int filesLimit;
    @Basic(optional = false)
    @NotNull
    @Column(name = "del_file")
    private int delFile;
    @Basic(optional = false)
    @NotNull
    @Column(name = "del_infect")
    private int delInfect;
    @Basic(optional = false)
    @NotNull
    @Column(name = "error_exit")
    private int errorExit;

    public SandboxJob() {
    }

    public SandboxJob(Integer recId) {
        this.recId = recId;
    }

    public SandboxJob(Integer recId, String name, String playbook, int playId, String refId, String sensorType, String sandboxType, String hostName, int timerange, String description, String filePath, String fileExt, int filesLimit, int delFile, int delInfect, int errorExit) {
        this.recId = recId;
        this.name = name;
        this.playbook = playbook;
        this.playId = playId;
        this.refId = refId;
        this.sensorType = sensorType;
        this.sandboxType = sandboxType;
        this.hostName = hostName;
        this.timerange = timerange;
        this.description = description;
        this.filePath = filePath;
        this.fileExt = fileExt;
        this.filesLimit = filesLimit;
        this.delFile = delFile;
        this.delInfect = delInfect;
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

    public String getSensorType() {
        return sensorType;
    }

    public void setSensorType(String sensorType) {
        this.sensorType = sensorType;
    }

    public String getSandboxType() {
        return sandboxType;
    }

    public void setSandboxType(String sandboxType) {
        this.sandboxType = sandboxType;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
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

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileExt() {
        return fileExt;
    }

    public void setFileExt(String fileExt) {
        this.fileExt = fileExt;
    }

    public int getFilesLimit() {
        return filesLimit;
    }

    public void setFilesLimit(int filesLimit) {
        this.filesLimit = filesLimit;
    }

    public int getDelFile() {
        return delFile;
    }

    public void setDelFile(int delFile) {
        this.delFile = delFile;
    }

    public int getDelInfect() {
        return delInfect;
    }

    public void setDelInfect(int delInfect) {
        this.delInfect = delInfect;
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
        if (!(object instanceof SandboxJob)) {
            return false;
        }
        SandboxJob other = (SandboxJob) object;
        if ((this.recId == null && other.recId != null) || (this.recId != null && !this.recId.equals(other.recId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.alertflex.mc.db.SandboxJob[ recId=" + recId + " ]";
    }
    
}
