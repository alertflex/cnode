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
@Table(name = "alert")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Alert.findAll", query = "SELECT a FROM Alert a"),
    @NamedQuery(name = "Alert.findByAlertId", query = "SELECT a FROM Alert a WHERE a.alertId = :alertId"),
    @NamedQuery(name = "Alert.findByAlertUuid", query = "SELECT a FROM Alert a WHERE a.alertUuid = :alertUuid"),
    @NamedQuery(name = "Alert.findByProjectId", query = "SELECT a FROM Alert a WHERE a.projectId = :projectId"),
    @NamedQuery(name = "Alert.findByProbeName", query = "SELECT a FROM Alert a WHERE a.probeName = :probeName"),
    @NamedQuery(name = "Alert.findBySensorsGroup", query = "SELECT a FROM Alert a WHERE a.sensorsGroup = :sensorsGroup"),
    @NamedQuery(name = "Alert.findByAlertSeverity", query = "SELECT a FROM Alert a WHERE a.alertSeverity = :alertSeverity"),
    @NamedQuery(name = "Alert.findByAlertSource", query = "SELECT a FROM Alert a WHERE a.alertSource = :alertSource"),
    @NamedQuery(name = "Alert.findByAlertRule", query = "SELECT a FROM Alert a WHERE a.alertRule = :alertRule"),
    @NamedQuery(name = "Alert.findByAlertMessage", query = "SELECT a FROM Alert a WHERE a.alertMessage = :alertMessage"),
    @NamedQuery(name = "Alert.findBySrcIp", query = "SELECT a FROM Alert a WHERE a.srcIp = :srcIp"),
    @NamedQuery(name = "Alert.findByDstIp", query = "SELECT a FROM Alert a WHERE a.dstIp = :dstIp"),
    @NamedQuery(name = "Alert.findBySrcPort", query = "SELECT a FROM Alert a WHERE a.srcPort = :srcPort"),
    @NamedQuery(name = "Alert.findByDstPort", query = "SELECT a FROM Alert a WHERE a.dstPort = :dstPort"),
    @NamedQuery(name = "Alert.findByUserName", query = "SELECT a FROM Alert a WHERE a.userName = :userName"),
    @NamedQuery(name = "Alert.findByFileName", query = "SELECT a FROM Alert a WHERE a.fileName = :fileName"),
    @NamedQuery(name = "Alert.findByProcessId", query = "SELECT a FROM Alert a WHERE a.processId = :processId"),
    @NamedQuery(name = "Alert.findByProcessName", query = "SELECT a FROM Alert a WHERE a.processName = :processName"),
    @NamedQuery(name = "Alert.findByContainerId", query = "SELECT a FROM Alert a WHERE a.containerId = :containerId"),
    @NamedQuery(name = "Alert.findByContainerName", query = "SELECT a FROM Alert a WHERE a.containerName = :containerName"),
    @NamedQuery(name = "Alert.findByContainerImage", query = "SELECT a FROM Alert a WHERE a.containerImage = :containerImage"),
    @NamedQuery(name = "Alert.findByPodId", query = "SELECT a FROM Alert a WHERE a.podId = :podId"),
    @NamedQuery(name = "Alert.findByPodName", query = "SELECT a FROM Alert a WHERE a.podName = :podName"),
    @NamedQuery(name = "Alert.findByNameSpace", query = "SELECT a FROM Alert a WHERE a.nameSpace = :nameSpace"),
    @NamedQuery(name = "Alert.findByHttpHostname", query = "SELECT a FROM Alert a WHERE a.httpHostname = :httpHostname"),
    @NamedQuery(name = "Alert.findByHttpPort", query = "SELECT a FROM Alert a WHERE a.httpPort = :httpPort"),
    @NamedQuery(name = "Alert.findByHttpUrl", query = "SELECT a FROM Alert a WHERE a.httpUrl = :httpUrl"),
    @NamedQuery(name = "Alert.findByHttpMethod", query = "SELECT a FROM Alert a WHERE a.httpMethod = :httpMethod"),
    @NamedQuery(name = "Alert.findByHttpStatus", query = "SELECT a FROM Alert a WHERE a.httpStatus = :httpStatus"),
    @NamedQuery(name = "Alert.findByHttpFlowId", query = "SELECT a FROM Alert a WHERE a.httpFlowId = :httpFlowId"),
    @NamedQuery(name = "Alert.findByHttpContentType", query = "SELECT a FROM Alert a WHERE a.httpContentType = :httpContentType"),
    @NamedQuery(name = "Alert.findByHttpRequestHeaders", query = "SELECT a FROM Alert a WHERE a.httpRequestHeaders = :httpRequestHeaders"),
    @NamedQuery(name = "Alert.findByHttpRequestBody", query = "SELECT a FROM Alert a WHERE a.httpRequestBody = :httpRequestBody"),
    @NamedQuery(name = "Alert.findByPipelineName", query = "SELECT a FROM Alert a WHERE a.pipelineName = :pipelineName"),
    @NamedQuery(name = "Alert.findByPipelineTask", query = "SELECT a FROM Alert a WHERE a.pipelineTask = :pipelineTask"),
    @NamedQuery(name = "Alert.findByJobName", query = "SELECT a FROM Alert a WHERE a.jobName = :jobName"),
    @NamedQuery(name = "Alert.findByAssetName", query = "SELECT a FROM Alert a WHERE a.assetName = :assetName"),
    @NamedQuery(name = "Alert.findByAssetTarget", query = "SELECT a FROM Alert a WHERE a.assetTarget = :assetTarget"),
    @NamedQuery(name = "Alert.findByTestUuid", query = "SELECT a FROM Alert a WHERE a.testUuid = :testUuid"),
    @NamedQuery(name = "Alert.findByStatus", query = "SELECT a FROM Alert a WHERE a.status = :status"),
    @NamedQuery(name = "Alert.findByOriginalTime", query = "SELECT a FROM Alert a WHERE a.originalTime = :originalTime"),
    @NamedQuery(name = "Alert.findByLoggedTime", query = "SELECT a FROM Alert a WHERE a.loggedTime = :loggedTime")})
public class Alert implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "alert_id")
    private Long alertId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 37)
    @Column(name = "alert_uuid")
    private String alertUuid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "project_id")
    private String projectId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "probe_name")
    private String probeName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "sensors_group")
    private String sensorsGroup;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "alert_severity")
    private String alertSeverity;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 32)
    @Column(name = "alert_source")
    private String alertSource;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "alert_rule")
    private String alertRule;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1024)
    @Column(name = "alert_message")
    private String alertMessage;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "src_ip")
    private String srcIp;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "dst_ip")
    private String dstIp;
    @Basic(optional = false)
    @NotNull
    @Column(name = "src_port")
    private int srcPort;
    @Basic(optional = false)
    @NotNull
    @Column(name = "dst_port")
    private int dstPort;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "user_name")
    private String userName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1024)
    @Column(name = "file_name")
    private String fileName;
    @Column(name = "process_id")
    private Integer processId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "process_name")
    private String processName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "container_id")
    private String containerId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "container_name")
    private String containerName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "container_image")
    private String containerImage;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "pod_id")
    private String podId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "pod_name")
    private String podName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "name_space")
    private String nameSpace;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1024)
    @Column(name = "http_hostname")
    private String httpHostname;
    @Basic(optional = false)
    @NotNull
    @Column(name = "http_port")
    private int httpPort;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1024)
    @Column(name = "http_url")
    private String httpUrl;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "http_method")
    private String httpMethod;
    @Basic(optional = false)
    @NotNull
    @Column(name = "http_status")
    private int httpStatus;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "http_flow_id")
    private String httpFlowId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "http_content_type")
    private String httpContentType;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2048)
    @Column(name = "http_request_headers")
    private String httpRequestHeaders;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2048)
    @Column(name = "http_request_body")
    private String httpRequestBody;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "pipeline_name")
    private String pipelineName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "pipeline_task")
    private String pipelineTask;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "job_name")
    private String jobName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "asset_name")
    private String assetName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "asset_target")
    private String assetTarget;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 37)
    @Column(name = "test_uuid")
    private String testUuid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 32)
    @Column(name = "status")
    private String status;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "original_time")
    private String originalTime;
    @Column(name = "logged_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date loggedTime;

    public Alert() {
    }

    public Alert(Long alertId) {
        this.alertId = alertId;
    }

    public Alert(Long alertId, String alertUuid, String projectId, String probeName, String sensorsGroup, String alertSeverity, String alertSource, String alertRule, String alertMessage, String srcIp, String dstIp, int srcPort, int dstPort, String userName, String fileName, String processName, String containerId, String containerName, String containerImage, String podId, String podName, String nameSpace, String httpHostname, int httpPort, String httpUrl, String httpMethod, int httpStatus, String httpFlowId, String httpContentType, String httpRequestHeaders, String httpRequestBody, String pipelineName, String pipelineTask, String jobName, String assetName, String assetTarget, String testUuid, String status, String originalTime) {
        this.alertId = alertId;
        this.alertUuid = alertUuid;
        this.projectId = projectId;
        this.probeName = probeName;
        this.sensorsGroup = sensorsGroup;
        this.alertSeverity = alertSeverity;
        this.alertSource = alertSource;
        this.alertRule = alertRule;
        this.alertMessage = alertMessage;
        this.srcIp = srcIp;
        this.dstIp = dstIp;
        this.srcPort = srcPort;
        this.dstPort = dstPort;
        this.userName = userName;
        this.fileName = fileName;
        this.processName = processName;
        this.containerId = containerId;
        this.containerName = containerName;
        this.containerImage = containerImage;
        this.podId = podId;
        this.podName = podName;
        this.nameSpace = nameSpace;
        this.httpHostname = httpHostname;
        this.httpPort = httpPort;
        this.httpUrl = httpUrl;
        this.httpMethod = httpMethod;
        this.httpStatus = httpStatus;
        this.httpFlowId = httpFlowId;
        this.httpContentType = httpContentType;
        this.httpRequestHeaders = httpRequestHeaders;
        this.httpRequestBody = httpRequestBody;
        this.pipelineName = pipelineName;
        this.pipelineTask = pipelineTask;
        this.jobName = jobName;
        this.assetName = assetName;
        this.assetTarget = assetTarget;
        this.testUuid = testUuid;
        this.status = status;
        this.originalTime = originalTime;
    }

    public Long getAlertId() {
        return alertId;
    }

    public void setAlertId(Long alertId) {
        this.alertId = alertId;
    }

    public String getAlertUuid() {
        return alertUuid;
    }

    public void setAlertUuid(String alertUuid) {
        this.alertUuid = alertUuid;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProbeName() {
        return probeName;
    }

    public void setProbeName(String probeName) {
        this.probeName = probeName;
    }

    public String getSensorsGroup() {
        return sensorsGroup;
    }

    public void setSensorsGroup(String sensorsGroup) {
        this.sensorsGroup = sensorsGroup;
    }

    public String getAlertSeverity() {
        return alertSeverity;
    }

    public void setAlertSeverity(String alertSeverity) {
        this.alertSeverity = alertSeverity;
    }

    public String getAlertSource() {
        return alertSource;
    }

    public void setAlertSource(String alertSource) {
        this.alertSource = alertSource;
    }

    public String getAlertRule() {
        return alertRule;
    }

    public void setAlertRule(String alertRule) {
        this.alertRule = alertRule;
    }

    public String getAlertMessage() {
        return alertMessage;
    }

    public void setAlertMessage(String alertMessage) {
        this.alertMessage = alertMessage;
    }

    public String getSrcIp() {
        return srcIp;
    }

    public void setSrcIp(String srcIp) {
        this.srcIp = srcIp;
    }

    public String getDstIp() {
        return dstIp;
    }

    public void setDstIp(String dstIp) {
        this.dstIp = dstIp;
    }

    public int getSrcPort() {
        return srcPort;
    }

    public void setSrcPort(int srcPort) {
        this.srcPort = srcPort;
    }

    public int getDstPort() {
        return dstPort;
    }

    public void setDstPort(int dstPort) {
        this.dstPort = dstPort;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Integer getProcessId() {
        return processId;
    }

    public void setProcessId(Integer processId) {
        this.processId = processId;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public String getContainerName() {
        return containerName;
    }

    public void setContainerName(String containerName) {
        this.containerName = containerName;
    }

    public String getContainerImage() {
        return containerImage;
    }

    public void setContainerImage(String containerImage) {
        this.containerImage = containerImage;
    }

    public String getPodId() {
        return podId;
    }

    public void setPodId(String podId) {
        this.podId = podId;
    }

    public String getPodName() {
        return podName;
    }

    public void setPodName(String podName) {
        this.podName = podName;
    }

    public String getNameSpace() {
        return nameSpace;
    }

    public void setNameSpace(String nameSpace) {
        this.nameSpace = nameSpace;
    }

    public String getHttpHostname() {
        return httpHostname;
    }

    public void setHttpHostname(String httpHostname) {
        this.httpHostname = httpHostname;
    }

    public int getHttpPort() {
        return httpPort;
    }

    public void setHttpPort(int httpPort) {
        this.httpPort = httpPort;
    }

    public String getHttpUrl() {
        return httpUrl;
    }

    public void setHttpUrl(String httpUrl) {
        this.httpUrl = httpUrl;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(int httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String getHttpFlowId() {
        return httpFlowId;
    }

    public void setHttpFlowId(String httpFlowId) {
        this.httpFlowId = httpFlowId;
    }

    public String getHttpContentType() {
        return httpContentType;
    }

    public void setHttpContentType(String httpContentType) {
        this.httpContentType = httpContentType;
    }

    public String getHttpRequestHeaders() {
        return httpRequestHeaders;
    }

    public void setHttpRequestHeaders(String httpRequestHeaders) {
        this.httpRequestHeaders = httpRequestHeaders;
    }

    public String getHttpRequestBody() {
        return httpRequestBody;
    }

    public void setHttpRequestBody(String httpRequestBody) {
        this.httpRequestBody = httpRequestBody;
    }

    public String getPipelineName() {
        return pipelineName;
    }

    public void setPipelineName(String pipelineName) {
        this.pipelineName = pipelineName;
    }

    public String getPipelineTask() {
        return pipelineTask;
    }

    public void setPipelineTask(String pipelineTask) {
        this.pipelineTask = pipelineTask;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public String getAssetTarget() {
        return assetTarget;
    }

    public void setAssetTarget(String assetTarget) {
        this.assetTarget = assetTarget;
    }

    public String getTestUuid() {
        return testUuid;
    }

    public void setTestUuid(String testUuid) {
        this.testUuid = testUuid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOriginalTime() {
        return originalTime;
    }

    public void setOriginalTime(String originalTime) {
        this.originalTime = originalTime;
    }

    public Date getLoggedTime() {
        return loggedTime;
    }

    public void setLoggedTime(Date loggedTime) {
        this.loggedTime = loggedTime;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (alertId != null ? alertId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Alert)) {
            return false;
        }
        Alert other = (Alert) object;
        if ((this.alertId == null && other.alertId != null) || (this.alertId != null && !this.alertId.equals(other.alertId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.alertflex.entity.Alert[ alertId=" + alertId + " ]";
    }
    
}
