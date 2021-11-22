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
@Table(name = "cloudsploit_scan")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CloudsploitScan.findAll", query = "SELECT c FROM CloudsploitScan c"),
    @NamedQuery(name = "CloudsploitScan.findByRecId", query = "SELECT c FROM CloudsploitScan c WHERE c.recId = :recId"),
    @NamedQuery(name = "CloudsploitScan.findByRefId", query = "SELECT c FROM CloudsploitScan c WHERE c.refId = :refId"),
    @NamedQuery(name = "CloudsploitScan.findByCloudType", query = "SELECT c FROM CloudsploitScan c WHERE c.cloudType = :cloudType"),
    @NamedQuery(name = "CloudsploitScan.findByPlugin", query = "SELECT c FROM CloudsploitScan c WHERE c.plugin = :plugin"),
    @NamedQuery(name = "CloudsploitScan.findByCategory", query = "SELECT c FROM CloudsploitScan c WHERE c.category = :category"),
    @NamedQuery(name = "CloudsploitScan.findByTitle", query = "SELECT c FROM CloudsploitScan c WHERE c.title = :title"),
    @NamedQuery(name = "CloudsploitScan.findByDescription", query = "SELECT c FROM CloudsploitScan c WHERE c.description = :description"),
    @NamedQuery(name = "CloudsploitScan.findByResource", query = "SELECT c FROM CloudsploitScan c WHERE c.resource = :resource"),
    @NamedQuery(name = "CloudsploitScan.findByRegion", query = "SELECT c FROM CloudsploitScan c WHERE c.region = :region"),
    @NamedQuery(name = "CloudsploitScan.findByStatus", query = "SELECT c FROM CloudsploitScan c WHERE c.status = :status"),
    @NamedQuery(name = "CloudsploitScan.findByMessage", query = "SELECT c FROM CloudsploitScan c WHERE c.message = :message"),
    @NamedQuery(name = "CloudsploitScan.findByReportAdded", query = "SELECT c FROM CloudsploitScan c WHERE c.reportAdded = :reportAdded"),
    @NamedQuery(name = "CloudsploitScan.findByReportUpdated", query = "SELECT c FROM CloudsploitScan c WHERE c.reportUpdated = :reportUpdated")})
public class CloudsploitScan implements Serializable {

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
    @Column(name = "cloud_type")
    private String cloudType;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "plugin")
    private String plugin;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "category")
    private String category;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "title")
    private String title;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1024)
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "resource")
    private String resource;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "region")
    private String region;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "status")
    private String status;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1024)
    @Column(name = "message")
    private String message;
    @Column(name = "report_added")
    @Temporal(TemporalType.TIMESTAMP)
    private Date reportAdded;
    @Column(name = "report_updated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date reportUpdated;

    public CloudsploitScan() {
    }

    public CloudsploitScan(Long recId) {
        this.recId = recId;
    }

    public CloudsploitScan(Long recId, String refId, String cloudType, String plugin, String category, String title, String description, String resource, String region, String status, String message) {
        this.recId = recId;
        this.refId = refId;
        this.cloudType = cloudType;
        this.plugin = plugin;
        this.category = category;
        this.title = title;
        this.description = description;
        this.resource = resource;
        this.region = region;
        this.status = status;
        this.message = message;
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

    public String getCloudType() {
        return cloudType;
    }

    public void setCloudType(String cloudType) {
        this.cloudType = cloudType;
    }

    public String getPlugin() {
        return plugin;
    }

    public void setPlugin(String plugin) {
        this.plugin = plugin;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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
        if (!(object instanceof CloudsploitScan)) {
            return false;
        }
        CloudsploitScan other = (CloudsploitScan) object;
        if ((this.recId == null && other.recId != null) || (this.recId != null && !this.recId.equals(other.recId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.alertflex.entity.CloudsploitScan[ recId=" + recId + " ]";
    }
    
}
