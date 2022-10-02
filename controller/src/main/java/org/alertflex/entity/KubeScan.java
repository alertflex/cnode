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
@Table(name = "kube_scan")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "KubeScan.findAll", query = "SELECT k FROM KubeScan k"),
    @NamedQuery(name = "KubeScan.findByRecId", query = "SELECT k FROM KubeScan k WHERE k.recId = :recId"),
    @NamedQuery(name = "KubeScan.findByRefId", query = "SELECT k FROM KubeScan k WHERE k.refId = :refId"),
    @NamedQuery(name = "KubeScan.findByNode", query = "SELECT k FROM KubeScan k WHERE k.node = :node"),
    @NamedQuery(name = "KubeScan.findByProbe", query = "SELECT k FROM KubeScan k WHERE k.probe = :probe"),
    @NamedQuery(name = "KubeScan.findByTestId", query = "SELECT k FROM KubeScan k WHERE k.testId = :testId"),
    @NamedQuery(name = "KubeScan.findByTestName", query = "SELECT k FROM KubeScan k WHERE k.testName = :testName"),
    @NamedQuery(name = "KubeScan.findByTestType", query = "SELECT k FROM KubeScan k WHERE k.testType = :testType"),
    @NamedQuery(name = "KubeScan.findBySectionNumber", query = "SELECT k FROM KubeScan k WHERE k.sectionNumber = :sectionNumber"),
    @NamedQuery(name = "KubeScan.findBySectionDesc", query = "SELECT k FROM KubeScan k WHERE k.sectionDesc = :sectionDesc"),
    @NamedQuery(name = "KubeScan.findByResultNumber", query = "SELECT k FROM KubeScan k WHERE k.resultNumber = :resultNumber"),
    @NamedQuery(name = "KubeScan.findByResultDesc", query = "SELECT k FROM KubeScan k WHERE k.resultDesc = :resultDesc"),
    @NamedQuery(name = "KubeScan.findByResultRemediation", query = "SELECT k FROM KubeScan k WHERE k.resultRemediation = :resultRemediation"),
    @NamedQuery(name = "KubeScan.findByResultStatus", query = "SELECT k FROM KubeScan k WHERE k.resultStatus = :resultStatus"),
    @NamedQuery(name = "KubeScan.findByActualValue", query = "SELECT k FROM KubeScan k WHERE k.actualValue = :actualValue"),
    @NamedQuery(name = "KubeScan.findByExpectedResult", query = "SELECT k FROM KubeScan k WHERE k.expectedResult = :expectedResult"),
    @NamedQuery(name = "KubeScan.findByReportAdded", query = "SELECT k FROM KubeScan k WHERE k.reportAdded = :reportAdded"),
    @NamedQuery(name = "KubeScan.findByReportUpdated", query = "SELECT k FROM KubeScan k WHERE k.reportUpdated = :reportUpdated")})
public class KubeScan implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "rec_id")
    private Long recId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "ref_id")
    private String refId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "node")
    private String node;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "probe")
    private String probe;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "test_id")
    private String testId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "test_name")
    private String testName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "test_type")
    private String testType;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "section_number")
    private String sectionNumber;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "section_desc")
    private String sectionDesc;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "result_number")
    private String resultNumber;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1024)
    @Column(name = "result_desc")
    private String resultDesc;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1024)
    @Column(name = "result_remediation")
    private String resultRemediation;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "result_status")
    private String resultStatus;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "actual_value")
    private String actualValue;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "expected_result")
    private String expectedResult;
    @Column(name = "report_added")
    @Temporal(TemporalType.TIMESTAMP)
    private Date reportAdded;
    @Column(name = "report_updated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date reportUpdated;

    public KubeScan() {
    }

    public KubeScan(Long recId) {
        this.recId = recId;
    }

    public KubeScan(Long recId, String refId, String node, String probe, String testId, String testName, String testType, String sectionNumber, String sectionDesc, String resultNumber, String resultDesc, String resultRemediation, String resultStatus, String actualValue, String expectedResult) {
        this.recId = recId;
        this.refId = refId;
        this.node = node;
        this.probe = probe;
        this.testId = testId;
        this.testName = testName;
        this.testType = testType;
        this.sectionNumber = sectionNumber;
        this.sectionDesc = sectionDesc;
        this.resultNumber = resultNumber;
        this.resultDesc = resultDesc;
        this.resultRemediation = resultRemediation;
        this.resultStatus = resultStatus;
        this.actualValue = actualValue;
        this.expectedResult = expectedResult;
    }

    public Long getRecId() {
        return recId;
    }

    public void setRecId(Long recId) {
        this.recId = recId;
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
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

    public String getTestId() {
        return testId;
    }

    public void setTestId(String testId) {
        this.testId = testId;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getTestType() {
        return testType;
    }

    public void setTestType(String testType) {
        this.testType = testType;
    }

    public String getSectionNumber() {
        return sectionNumber;
    }

    public void setSectionNumber(String sectionNumber) {
        this.sectionNumber = sectionNumber;
    }

    public String getSectionDesc() {
        return sectionDesc;
    }

    public void setSectionDesc(String sectionDesc) {
        this.sectionDesc = sectionDesc;
    }

    public String getResultNumber() {
        return resultNumber;
    }

    public void setResultNumber(String resultNumber) {
        this.resultNumber = resultNumber;
    }

    public String getResultDesc() {
        return resultDesc;
    }

    public void setResultDesc(String resultDesc) {
        this.resultDesc = resultDesc;
    }

    public String getResultRemediation() {
        return resultRemediation;
    }

    public void setResultRemediation(String resultRemediation) {
        this.resultRemediation = resultRemediation;
    }

    public String getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(String resultStatus) {
        this.resultStatus = resultStatus;
    }

    public String getActualValue() {
        return actualValue;
    }

    public void setActualValue(String actualValue) {
        this.actualValue = actualValue;
    }

    public String getExpectedResult() {
        return expectedResult;
    }

    public void setExpectedResult(String expectedResult) {
        this.expectedResult = expectedResult;
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
        if (!(object instanceof KubeScan)) {
            return false;
        }
        KubeScan other = (KubeScan) object;
        if ((this.recId == null && other.recId != null) || (this.recId != null && !this.recId.equals(other.recId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.alertflex.entity.KubeScan[ recId=" + recId + " ]";
    }
    
}
