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
@Table(name = "agent_packages")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AgentPackages.findAll", query = "SELECT a FROM AgentPackages a")
    , @NamedQuery(name = "AgentPackages.findByRecId", query = "SELECT a FROM AgentPackages a WHERE a.recId = :recId")
    , @NamedQuery(name = "AgentPackages.findByNodeId", query = "SELECT a FROM AgentPackages a WHERE a.nodeId = :nodeId")
    , @NamedQuery(name = "AgentPackages.findByRefId", query = "SELECT a FROM AgentPackages a WHERE a.refId = :refId")
    , @NamedQuery(name = "AgentPackages.findByAgent", query = "SELECT a FROM AgentPackages a WHERE a.agent = :agent")
    , @NamedQuery(name = "AgentPackages.findByName", query = "SELECT a FROM AgentPackages a WHERE a.name = :name")
    , @NamedQuery(name = "AgentPackages.findByPackageSize", query = "SELECT a FROM AgentPackages a WHERE a.packageSize = :packageSize")
    , @NamedQuery(name = "AgentPackages.findByArchitecture", query = "SELECT a FROM AgentPackages a WHERE a.architecture = :architecture")
    , @NamedQuery(name = "AgentPackages.findByPriority", query = "SELECT a FROM AgentPackages a WHERE a.priority = :priority")
    , @NamedQuery(name = "AgentPackages.findByVersion", query = "SELECT a FROM AgentPackages a WHERE a.version = :version")
    , @NamedQuery(name = "AgentPackages.findByVendor", query = "SELECT a FROM AgentPackages a WHERE a.vendor = :vendor")
    , @NamedQuery(name = "AgentPackages.findByPackageFormat", query = "SELECT a FROM AgentPackages a WHERE a.packageFormat = :packageFormat")
    , @NamedQuery(name = "AgentPackages.findByPackageSection", query = "SELECT a FROM AgentPackages a WHERE a.packageSection = :packageSection")
    , @NamedQuery(name = "AgentPackages.findByDescription", query = "SELECT a FROM AgentPackages a WHERE a.description = :description")
    , @NamedQuery(name = "AgentPackages.findByTimeScan", query = "SELECT a FROM AgentPackages a WHERE a.timeScan = :timeScan")
    , @NamedQuery(name = "AgentPackages.findByDateAdd", query = "SELECT a FROM AgentPackages a WHERE a.dateAdd = :dateAdd")
    , @NamedQuery(name = "AgentPackages.findByDateUpdate", query = "SELECT a FROM AgentPackages a WHERE a.dateUpdate = :dateUpdate")})
public class AgentPackages implements Serializable {

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
    @Column(name = "agent")
    private String agent;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @NotNull
    @Column(name = "package_size")
    private long packageSize;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "architecture")
    private String architecture;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "priority")
    private String priority;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 254)
    @Column(name = "version")
    private String version;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "vendor")
    private String vendor;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "package_format")
    private String packageFormat;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 254)
    @Column(name = "package_section")
    private String packageSection;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1024)
    @Column(name = "description")
    private String description;
    @Column(name = "time_scan")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeScan;
    @Column(name = "date_add")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateAdd;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;

    public AgentPackages() {
    }

    public AgentPackages(Long recId) {
        this.recId = recId;
    }

    public AgentPackages(Long recId, String nodeId, String refId, String agent, String name, long packageSize, String architecture, String priority, String version, String vendor, String packageFormat, String packageSection, String description) {
        this.recId = recId;
        this.nodeId = nodeId;
        this.refId = refId;
        this.agent = agent;
        this.name = name;
        this.packageSize = packageSize;
        this.architecture = architecture;
        this.priority = priority;
        this.version = version;
        this.vendor = vendor;
        this.packageFormat = packageFormat;
        this.packageSection = packageSection;
        this.description = description;
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

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPackageSize() {
        return packageSize;
    }

    public void setPackageSize(long packageSize) {
        this.packageSize = packageSize;
    }

    public String getArchitecture() {
        return architecture;
    }

    public void setArchitecture(String architecture) {
        this.architecture = architecture;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getPackageFormat() {
        return packageFormat;
    }

    public void setPackageFormat(String packageFormat) {
        this.packageFormat = packageFormat;
    }

    public String getPackageSection() {
        return packageSection;
    }

    public void setPackageSection(String packageSection) {
        this.packageSection = packageSection;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getTimeScan() {
        return timeScan;
    }

    public void setTimeScan(Date timeScan) {
        this.timeScan = timeScan;
    }

    public Date getDateAdd() {
        return dateAdd;
    }

    public void setDateAdd(Date dateAdd) {
        this.dateAdd = dateAdd;
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
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
        if (!(object instanceof AgentPackages)) {
            return false;
        }
        AgentPackages other = (AgentPackages) object;
        if ((this.recId == null && other.recId != null) || (this.recId != null && !this.recId.equals(other.recId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.alertflex.entity.AgentPackages[ recId=" + recId + " ]";
    }

}
