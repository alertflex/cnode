/*
 *   Copyright 2021 Oleg Zharkov
 *
 *   Licensed under the Apache License, Version 2.0 (the "License").
 *   You may not use this file except in compliance with the License.
 *   A copy of the License is located at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   or in the "license" file accompanying this file. This file is distributed
 *   on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 *   express or implied. See the License for the specific language governing
 *   permissions and limitations under the License.
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

@Entity
@Table(name = "scanreport_task")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ScanreportTask.findAll", query = "SELECT s FROM ScanreportTask s"),
    @NamedQuery(name = "ScanreportTask.findByRecId", query = "SELECT s FROM ScanreportTask s WHERE s.recId = :recId"),
    @NamedQuery(name = "ScanreportTask.findByReportUuid", query = "SELECT s FROM ScanreportTask s WHERE s.reportUuid = :reportUuid"),
    @NamedQuery(name = "ScanreportTask.findByRefId", query = "SELECT s FROM ScanreportTask s WHERE s.refId = :refId"),
    @NamedQuery(name = "ScanreportTask.findBySource", query = "SELECT s FROM ScanreportTask s WHERE s.source = :source"),
    @NamedQuery(name = "ScanreportTask.findBySeverity", query = "SELECT s FROM ScanreportTask s WHERE s.severity = :severity"),
    @NamedQuery(name = "ScanreportTask.findByNum", query = "SELECT s FROM ScanreportTask s WHERE s.num = :num"),
    @NamedQuery(name = "ScanreportTask.findByStatus", query = "SELECT s FROM ScanreportTask s WHERE s.status = :status"),
    @NamedQuery(name = "ScanreportTask.findByReportType", query = "SELECT s FROM ScanreportTask s WHERE s.reportType = :reportType"),
    @NamedQuery(name = "ScanreportTask.findByReportAdded", query = "SELECT s FROM ScanreportTask s WHERE s.reportAdded = :reportAdded")})
public class ScanreportTask implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "rec_id")
    private Integer recId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "report_uuid")
    private String reportUuid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "ref_id")
    private String refId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "source")
    private String source;
    @Basic(optional = false)
    @NotNull
    @Column(name = "severity")
    private int severity;
    @Basic(optional = false)
    @NotNull
    @Column(name = "num")
    private int num;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 32)
    @Column(name = "status")
    private String status;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 32)
    @Column(name = "report_type")
    private String reportType;
    @Column(name = "report_added")
    @Temporal(TemporalType.TIMESTAMP)
    private Date reportAdded;

    public ScanreportTask() {
    }

    public ScanreportTask(Integer recId) {
        this.recId = recId;
    }

    public ScanreportTask(Integer recId, String reportUuid, String refId, String source, int severity, int num, String status, String reportType) {
        this.recId = recId;
        this.reportUuid = reportUuid;
        this.refId = refId;
        this.source = source;
        this.severity = severity;
        this.num = num;
        this.status = status;
        this.reportType = reportType;
    }

    public Integer getRecId() {
        return recId;
    }

    public void setRecId(Integer recId) {
        this.recId = recId;
    }

    public String getReportUuid() {
        return reportUuid;
    }

    public void setReportUuid(String reportUuid) {
        this.reportUuid = reportUuid;
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getSeverity() {
        return severity;
    }

    public void setSeverity(int severity) {
        this.severity = severity;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
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
        if (!(object instanceof ScanreportTask)) {
            return false;
        }
        ScanreportTask other = (ScanreportTask) object;
        if ((this.recId == null && other.recId != null) || (this.recId != null && !this.recId.equals(other.recId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.alertflex.entity.ScanreportTask[ recId=" + recId + " ]";
    }
    
}
