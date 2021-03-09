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
@Table(name = "docker_scan")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DockerScan.findAll", query = "SELECT d FROM DockerScan d"),
    @NamedQuery(name = "DockerScan.findByRecId", query = "SELECT d FROM DockerScan d WHERE d.recId = :recId"),
    @NamedQuery(name = "DockerScan.findByNodeId", query = "SELECT d FROM DockerScan d WHERE d.nodeId = :nodeId"),
    @NamedQuery(name = "DockerScan.findByRefId", query = "SELECT d FROM DockerScan d WHERE d.refId = :refId"),
    @NamedQuery(name = "DockerScan.findByProbe", query = "SELECT d FROM DockerScan d WHERE d.probe = :probe"),
    @NamedQuery(name = "DockerScan.findByTestDesc", query = "SELECT d FROM DockerScan d WHERE d.testDesc = :testDesc"),
    @NamedQuery(name = "DockerScan.findByResultId", query = "SELECT d FROM DockerScan d WHERE d.resultId = :resultId"),
    @NamedQuery(name = "DockerScan.findByResultDesc", query = "SELECT d FROM DockerScan d WHERE d.resultDesc = :resultDesc"),
    @NamedQuery(name = "DockerScan.findByResult", query = "SELECT d FROM DockerScan d WHERE d.result = :result"),
    @NamedQuery(name = "DockerScan.findByDetails", query = "SELECT d FROM DockerScan d WHERE d.details = :details"),
    @NamedQuery(name = "DockerScan.findByReportAdded", query = "SELECT d FROM DockerScan d WHERE d.reportAdded = :reportAdded"),
    @NamedQuery(name = "DockerScan.findByReportUpdated", query = "SELECT d FROM DockerScan d WHERE d.reportUpdated = :reportUpdated")})
public class DockerScan implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "rec_id")
    private Long recId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "node_id")
    private String nodeId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "ref_id")
    private String refId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "probe")
    private String probe;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "test_desc")
    private String testDesc;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "result_id")
    private String resultId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1024)
    @Column(name = "result_desc")
    private String resultDesc;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "result")
    private String result;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1024)
    @Column(name = "details")
    private String details;
    @Column(name = "report_added")
    @Temporal(TemporalType.TIMESTAMP)
    private Date reportAdded;
    @Column(name = "report_updated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date reportUpdated;

    public DockerScan() {
    }

    public DockerScan(Long recId) {
        this.recId = recId;
    }

    public DockerScan(Long recId, String nodeId, String refId, String probe, String testDesc, String resultId, String resultDesc, String result, String details) {
        this.recId = recId;
        this.nodeId = nodeId;
        this.refId = refId;
        this.probe = probe;
        this.testDesc = testDesc;
        this.resultId = resultId;
        this.resultDesc = resultDesc;
        this.result = result;
        this.details = details;
    }

    public Long getRecId() {
        return recId;
    }

    public void setRecId(Long recId) {
        this.recId = recId;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public String getProbe() {
        return probe;
    }

    public void setProbe(String probe) {
        this.probe = probe;
    }

    public String getTestDesc() {
        return testDesc;
    }

    public void setTestDesc(String testDesc) {
        this.testDesc = testDesc;
    }

    public String getResultId() {
        return resultId;
    }

    public void setResultId(String resultId) {
        this.resultId = resultId;
    }

    public String getResultDesc() {
        return resultDesc;
    }

    public void setResultDesc(String resultDesc) {
        this.resultDesc = resultDesc;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
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
        if (!(object instanceof DockerScan)) {
            return false;
        }
        DockerScan other = (DockerScan) object;
        if ((this.recId == null && other.recId != null) || (this.recId != null && !this.recId.equals(other.recId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.alertflex.entity.DockerScan[ recId=" + recId + " ]";
    }
    
}
