/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alertflex.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "project")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Project.findAll", query = "SELECT p FROM Project p"),
    @NamedQuery(name = "Project.findByRefId", query = "SELECT p FROM Project p WHERE p.refId = :refId"),
    @NamedQuery(name = "Project.findByName", query = "SELECT p FROM Project p WHERE p.name = :name"),
    @NamedQuery(name = "Project.findByProjectPath", query = "SELECT p FROM Project p WHERE p.projectPath = :projectPath"),
    @NamedQuery(name = "Project.findBySemActive", query = "SELECT p FROM Project p WHERE p.semActive = :semActive"),
    @NamedQuery(name = "Project.findByAlertLimit", query = "SELECT p FROM Project p WHERE p.alertLimit = :alertLimit"),
    @NamedQuery(name = "Project.findByAlertType", query = "SELECT p FROM Project p WHERE p.alertType = :alertType"),
    @NamedQuery(name = "Project.findByAlertTimerange", query = "SELECT p FROM Project p WHERE p.alertTimerange = :alertTimerange"),
    @NamedQuery(name = "Project.findByNodeTimerange", query = "SELECT p FROM Project p WHERE p.nodeTimerange = :nodeTimerange"),
    @NamedQuery(name = "Project.findByTaskTimerange", query = "SELECT p FROM Project p WHERE p.taskTimerange = :taskTimerange"),
    @NamedQuery(name = "Project.findByPostureTimerange", query = "SELECT p FROM Project p WHERE p.postureTimerange = :postureTimerange"),
    @NamedQuery(name = "Project.findBySensorTimerange", query = "SELECT p FROM Project p WHERE p.sensorTimerange = :sensorTimerange"),
    @NamedQuery(name = "Project.findByBlockIprange", query = "SELECT p FROM Project p WHERE p.blockIprange = :blockIprange"),
    @NamedQuery(name = "Project.findByIocCheck", query = "SELECT p FROM Project p WHERE p.iocCheck = :iocCheck"),
    @NamedQuery(name = "Project.findByIocEvent", query = "SELECT p FROM Project p WHERE p.iocEvent = :iocEvent"),
    @NamedQuery(name = "Project.findByPrometheusStat", query = "SELECT p FROM Project p WHERE p.prometheusStat = :prometheusStat"),
    @NamedQuery(name = "Project.findBySendNetflow", query = "SELECT p FROM Project p WHERE p.sendNetflow = :sendNetflow"),
    @NamedQuery(name = "Project.findBySendIncident", query = "SELECT p FROM Project p WHERE p.sendIncident = :sendIncident"),
    @NamedQuery(name = "Project.findByGraylogHost", query = "SELECT p FROM Project p WHERE p.graylogHost = :graylogHost"),
    @NamedQuery(name = "Project.findByGraylogPort", query = "SELECT p FROM Project p WHERE p.graylogPort = :graylogPort"),
    @NamedQuery(name = "Project.findByElkHost", query = "SELECT p FROM Project p WHERE p.elkHost = :elkHost"),
    @NamedQuery(name = "Project.findByElkPort", query = "SELECT p FROM Project p WHERE p.elkPort = :elkPort"),
    @NamedQuery(name = "Project.findByElkUser", query = "SELECT p FROM Project p WHERE p.elkUser = :elkUser"),
    @NamedQuery(name = "Project.findByElkPass", query = "SELECT p FROM Project p WHERE p.elkPass = :elkPass"),
    @NamedQuery(name = "Project.findByElkStorepass", query = "SELECT p FROM Project p WHERE p.elkStorepass = :elkStorepass"),
    @NamedQuery(name = "Project.findByElkKeystore", query = "SELECT p FROM Project p WHERE p.elkKeystore = :elkKeystore"),
    @NamedQuery(name = "Project.findByElkTruststore", query = "SELECT p FROM Project p WHERE p.elkTruststore = :elkTruststore"),
    @NamedQuery(name = "Project.findByHiveUrl", query = "SELECT p FROM Project p WHERE p.hiveUrl = :hiveUrl"),
    @NamedQuery(name = "Project.findByHiveKey", query = "SELECT p FROM Project p WHERE p.hiveKey = :hiveKey"),
    @NamedQuery(name = "Project.findByMispUrl", query = "SELECT p FROM Project p WHERE p.mispUrl = :mispUrl"),
    @NamedQuery(name = "Project.findByMispKey", query = "SELECT p FROM Project p WHERE p.mispKey = :mispKey"),
    @NamedQuery(name = "Project.findByGitlabUrl", query = "SELECT p FROM Project p WHERE p.gitlabUrl = :gitlabUrl"),
    @NamedQuery(name = "Project.findByGitlabKey", query = "SELECT p FROM Project p WHERE p.gitlabKey = :gitlabKey"),
    @NamedQuery(name = "Project.findByVtKey", query = "SELECT p FROM Project p WHERE p.vtKey = :vtKey"),
    @NamedQuery(name = "Project.findByCuckooHost", query = "SELECT p FROM Project p WHERE p.cuckooHost = :cuckooHost"),
    @NamedQuery(name = "Project.findByCuckooPort", query = "SELECT p FROM Project p WHERE p.cuckooPort = :cuckooPort"),
    @NamedQuery(name = "Project.findByFalconUrl", query = "SELECT p FROM Project p WHERE p.falconUrl = :falconUrl"),
    @NamedQuery(name = "Project.findByFalconKey", query = "SELECT p FROM Project p WHERE p.falconKey = :falconKey"),
    @NamedQuery(name = "Project.findByMobsfUrl", query = "SELECT p FROM Project p WHERE p.mobsfUrl = :mobsfUrl"),
    @NamedQuery(name = "Project.findByMobsfKey", query = "SELECT p FROM Project p WHERE p.mobsfKey = :mobsfKey"),
    @NamedQuery(name = "Project.findByXforceKey", query = "SELECT p FROM Project p WHERE p.xforceKey = :xforceKey"),
    @NamedQuery(name = "Project.findByXforcePass", query = "SELECT p FROM Project p WHERE p.xforcePass = :xforcePass"),
    @NamedQuery(name = "Project.findBySonarUrl", query = "SELECT p FROM Project p WHERE p.sonarUrl = :sonarUrl"),
    @NamedQuery(name = "Project.findBySonarUser", query = "SELECT p FROM Project p WHERE p.sonarUser = :sonarUser"),
    @NamedQuery(name = "Project.findBySonarPass", query = "SELECT p FROM Project p WHERE p.sonarPass = :sonarPass"),
    @NamedQuery(name = "Project.findByTrackUrl", query = "SELECT p FROM Project p WHERE p.trackUrl = :trackUrl"),
    @NamedQuery(name = "Project.findByTrackKey", query = "SELECT p FROM Project p WHERE p.trackKey = :trackKey"),
    @NamedQuery(name = "Project.findByTrackProject", query = "SELECT p FROM Project p WHERE p.trackProject = :trackProject"),
    @NamedQuery(name = "Project.findByTrackVersion", query = "SELECT p FROM Project p WHERE p.trackVersion = :trackVersion"),
    @NamedQuery(name = "Project.findByAwsRegion", query = "SELECT p FROM Project p WHERE p.awsRegion = :awsRegion"),
    @NamedQuery(name = "Project.findByAwsIpinsights", query = "SELECT p FROM Project p WHERE p.awsIpinsights = :awsIpinsights"),
    @NamedQuery(name = "Project.findByChatgptUrl", query = "SELECT p FROM Project p WHERE p.chatgptUrl = :chatgptUrl"),
    @NamedQuery(name = "Project.findByChatgptKey", query = "SELECT p FROM Project p WHERE p.chatgptKey = :chatgptKey"),
    @NamedQuery(name = "Project.findByChatgptTemperature", query = "SELECT p FROM Project p WHERE p.chatgptTemperature = :chatgptTemperature")})
public class Project implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "ref_id")
    private String refId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "project_path")
    private String projectPath;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sem_active")
    private int semActive;
    @Basic(optional = false)
    @NotNull
    @Column(name = "alert_limit")
    private int alertLimit;
    @Basic(optional = false)
    @NotNull
    @Column(name = "alert_type")
    private int alertType;
    @Basic(optional = false)
    @NotNull
    @Column(name = "alert_timerange")
    private int alertTimerange;
    @Basic(optional = false)
    @NotNull
    @Column(name = "node_timerange")
    private int nodeTimerange;
    @Basic(optional = false)
    @NotNull
    @Column(name = "task_timerange")
    private int taskTimerange;
    @Basic(optional = false)
    @NotNull
    @Column(name = "posture_timerange")
    private int postureTimerange;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sensor_timerange")
    private int sensorTimerange;
    @Basic(optional = false)
    @NotNull
    @Column(name = "block_iprange")
    private int blockIprange;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ioc_check")
    private int iocCheck;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ioc_event")
    private int iocEvent;
    @Basic(optional = false)
    @NotNull
    @Column(name = "prometheus_stat")
    private int prometheusStat;
    @Basic(optional = false)
    @NotNull
    @Column(name = "send_netflow")
    private int sendNetflow;
    @Basic(optional = false)
    @NotNull
    @Column(name = "send_incident")
    private int sendIncident;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "graylog_host")
    private String graylogHost;
    @Basic(optional = false)
    @NotNull
    @Column(name = "graylog_port")
    private int graylogPort;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "elk_host")
    private String elkHost;
    @Basic(optional = false)
    @NotNull
    @Column(name = "elk_port")
    private int elkPort;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "elk_user")
    private String elkUser;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "elk_pass")
    private String elkPass;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "elk_storepass")
    private String elkStorepass;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "elk_keystore")
    private String elkKeystore;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "elk_truststore")
    private String elkTruststore;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "hive_url")
    private String hiveUrl;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "hive_key")
    private String hiveKey;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "misp_url")
    private String mispUrl;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "misp_key")
    private String mispKey;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "gitlab_url")
    private String gitlabUrl;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "gitlab_key")
    private String gitlabKey;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "vt_key")
    private String vtKey;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "cuckoo_host")
    private String cuckooHost;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cuckoo_port")
    private int cuckooPort;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "falcon_url")
    private String falconUrl;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "falcon_key")
    private String falconKey;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "mobsf_url")
    private String mobsfUrl;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "mobsf_key")
    private String mobsfKey;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "xforce_key")
    private String xforceKey;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "xforce_pass")
    private String xforcePass;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "sonar_url")
    private String sonarUrl;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "sonar_user")
    private String sonarUser;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "sonar_pass")
    private String sonarPass;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "track_url")
    private String trackUrl;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "track_key")
    private String trackKey;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "track_project")
    private String trackProject;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "track_version")
    private String trackVersion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "aws_region")
    private String awsRegion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 256)
    @Column(name = "aws_ipinsights")
    private String awsIpinsights;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "chatgpt_url")
    private String chatgptUrl;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "chatgpt_key")
    private String chatgptKey;
    @Basic(optional = false)
    @NotNull
    @Column(name = "chatgpt_temperature")
    private float chatgptTemperature;

    public Project() {
    }

    public Project(String refId) {
        this.refId = refId;
    }

    public Project(String refId, String name, String projectPath, int semActive, int alertLimit, int alertType, int alertTimerange, int nodeTimerange, int taskTimerange, int postureTimerange, int sensorTimerange, int blockIprange, int iocCheck, int iocEvent, int prometheusStat, int sendNetflow, int sendIncident, String graylogHost, int graylogPort, String elkHost, int elkPort, String elkUser, String elkPass, String elkStorepass, String elkKeystore, String elkTruststore, String hiveUrl, String hiveKey, String mispUrl, String mispKey, String gitlabUrl, String gitlabKey, String vtKey, String cuckooHost, int cuckooPort, String falconUrl, String falconKey, String mobsfUrl, String mobsfKey, String xforceKey, String xforcePass, String sonarUrl, String sonarUser, String sonarPass, String trackUrl, String trackKey, String trackProject, String trackVersion, String awsRegion, String awsIpinsights, String chatgptUrl, String chatgptKey, float chatgptTemperature) {
        this.refId = refId;
        this.name = name;
        this.projectPath = projectPath;
        this.semActive = semActive;
        this.alertLimit = alertLimit;
        this.alertType = alertType;
        this.alertTimerange = alertTimerange;
        this.nodeTimerange = nodeTimerange;
        this.taskTimerange = taskTimerange;
        this.postureTimerange = postureTimerange;
        this.sensorTimerange = sensorTimerange;
        this.blockIprange = blockIprange;
        this.iocCheck = iocCheck;
        this.iocEvent = iocEvent;
        this.prometheusStat = prometheusStat;
        this.sendNetflow = sendNetflow;
        this.sendIncident = sendIncident;
        this.graylogHost = graylogHost;
        this.graylogPort = graylogPort;
        this.elkHost = elkHost;
        this.elkPort = elkPort;
        this.elkUser = elkUser;
        this.elkPass = elkPass;
        this.elkStorepass = elkStorepass;
        this.elkKeystore = elkKeystore;
        this.elkTruststore = elkTruststore;
        this.hiveUrl = hiveUrl;
        this.hiveKey = hiveKey;
        this.mispUrl = mispUrl;
        this.mispKey = mispKey;
        this.gitlabUrl = gitlabUrl;
        this.gitlabKey = gitlabKey;
        this.vtKey = vtKey;
        this.cuckooHost = cuckooHost;
        this.cuckooPort = cuckooPort;
        this.falconUrl = falconUrl;
        this.falconKey = falconKey;
        this.mobsfUrl = mobsfUrl;
        this.mobsfKey = mobsfKey;
        this.xforceKey = xforceKey;
        this.xforcePass = xforcePass;
        this.sonarUrl = sonarUrl;
        this.sonarUser = sonarUser;
        this.sonarPass = sonarPass;
        this.trackUrl = trackUrl;
        this.trackKey = trackKey;
        this.trackProject = trackProject;
        this.trackVersion = trackVersion;
        this.awsRegion = awsRegion;
        this.awsIpinsights = awsIpinsights;
        this.chatgptUrl = chatgptUrl;
        this.chatgptKey = chatgptKey;
        this.chatgptTemperature = chatgptTemperature;
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProjectPath() {
        return projectPath;
    }

    public void setProjectPath(String projectPath) {
        this.projectPath = projectPath;
    }

    public int getSemActive() {
        return semActive;
    }

    public void setSemActive(int semActive) {
        this.semActive = semActive;
    }

    public int getAlertLimit() {
        return alertLimit;
    }

    public void setAlertLimit(int alertLimit) {
        this.alertLimit = alertLimit;
    }

    public int getAlertType() {
        return alertType;
    }

    public void setAlertType(int alertType) {
        this.alertType = alertType;
    }

    public int getAlertTimerange() {
        return alertTimerange;
    }

    public void setAlertTimerange(int alertTimerange) {
        this.alertTimerange = alertTimerange;
    }

    public int getNodeTimerange() {
        return nodeTimerange;
    }

    public void setNodeTimerange(int nodeTimerange) {
        this.nodeTimerange = nodeTimerange;
    }

    public int getTaskTimerange() {
        return taskTimerange;
    }

    public void setTaskTimerange(int taskTimerange) {
        this.taskTimerange = taskTimerange;
    }

    public int getPostureTimerange() {
        return postureTimerange;
    }

    public void setPostureTimerange(int postureTimerange) {
        this.postureTimerange = postureTimerange;
    }

    public int getSensorTimerange() {
        return sensorTimerange;
    }

    public void setSensorTimerange(int sensorTimerange) {
        this.sensorTimerange = sensorTimerange;
    }

    public int getBlockIprange() {
        return blockIprange;
    }

    public void setBlockIprange(int blockIprange) {
        this.blockIprange = blockIprange;
    }

    public int getIocCheck() {
        return iocCheck;
    }

    public void setIocCheck(int iocCheck) {
        this.iocCheck = iocCheck;
    }

    public int getIocEvent() {
        return iocEvent;
    }

    public void setIocEvent(int iocEvent) {
        this.iocEvent = iocEvent;
    }

    public int getPrometheusStat() {
        return prometheusStat;
    }

    public void setPrometheusStat(int prometheusStat) {
        this.prometheusStat = prometheusStat;
    }

    public int getSendNetflow() {
        return sendNetflow;
    }

    public void setSendNetflow(int sendNetflow) {
        this.sendNetflow = sendNetflow;
    }

    public int getSendIncident() {
        return sendIncident;
    }

    public void setSendIncident(int sendIncident) {
        this.sendIncident = sendIncident;
    }

    public String getGraylogHost() {
        return graylogHost;
    }

    public void setGraylogHost(String graylogHost) {
        this.graylogHost = graylogHost;
    }

    public int getGraylogPort() {
        return graylogPort;
    }

    public void setGraylogPort(int graylogPort) {
        this.graylogPort = graylogPort;
    }

    public String getElkHost() {
        return elkHost;
    }

    public void setElkHost(String elkHost) {
        this.elkHost = elkHost;
    }

    public int getElkPort() {
        return elkPort;
    }

    public void setElkPort(int elkPort) {
        this.elkPort = elkPort;
    }

    public String getElkUser() {
        return elkUser;
    }

    public void setElkUser(String elkUser) {
        this.elkUser = elkUser;
    }

    public String getElkPass() {
        return elkPass;
    }

    public void setElkPass(String elkPass) {
        this.elkPass = elkPass;
    }

    public String getElkStorepass() {
        return elkStorepass;
    }

    public void setElkStorepass(String elkStorepass) {
        this.elkStorepass = elkStorepass;
    }

    public String getElkKeystore() {
        return elkKeystore;
    }

    public void setElkKeystore(String elkKeystore) {
        this.elkKeystore = elkKeystore;
    }

    public String getElkTruststore() {
        return elkTruststore;
    }

    public void setElkTruststore(String elkTruststore) {
        this.elkTruststore = elkTruststore;
    }

    public String getHiveUrl() {
        return hiveUrl;
    }

    public void setHiveUrl(String hiveUrl) {
        this.hiveUrl = hiveUrl;
    }

    public String getHiveKey() {
        return hiveKey;
    }

    public void setHiveKey(String hiveKey) {
        this.hiveKey = hiveKey;
    }

    public String getMispUrl() {
        return mispUrl;
    }

    public void setMispUrl(String mispUrl) {
        this.mispUrl = mispUrl;
    }

    public String getMispKey() {
        return mispKey;
    }

    public void setMispKey(String mispKey) {
        this.mispKey = mispKey;
    }

    public String getGitlabUrl() {
        return gitlabUrl;
    }

    public void setGitlabUrl(String gitlabUrl) {
        this.gitlabUrl = gitlabUrl;
    }

    public String getGitlabKey() {
        return gitlabKey;
    }

    public void setGitlabKey(String gitlabKey) {
        this.gitlabKey = gitlabKey;
    }

    public String getVtKey() {
        return vtKey;
    }

    public void setVtKey(String vtKey) {
        this.vtKey = vtKey;
    }

    public String getCuckooHost() {
        return cuckooHost;
    }

    public void setCuckooHost(String cuckooHost) {
        this.cuckooHost = cuckooHost;
    }

    public int getCuckooPort() {
        return cuckooPort;
    }

    public void setCuckooPort(int cuckooPort) {
        this.cuckooPort = cuckooPort;
    }

    public String getFalconUrl() {
        return falconUrl;
    }

    public void setFalconUrl(String falconUrl) {
        this.falconUrl = falconUrl;
    }

    public String getFalconKey() {
        return falconKey;
    }

    public void setFalconKey(String falconKey) {
        this.falconKey = falconKey;
    }

    public String getMobsfUrl() {
        return mobsfUrl;
    }

    public void setMobsfUrl(String mobsfUrl) {
        this.mobsfUrl = mobsfUrl;
    }

    public String getMobsfKey() {
        return mobsfKey;
    }

    public void setMobsfKey(String mobsfKey) {
        this.mobsfKey = mobsfKey;
    }

    public String getXforceKey() {
        return xforceKey;
    }

    public void setXforceKey(String xforceKey) {
        this.xforceKey = xforceKey;
    }

    public String getXforcePass() {
        return xforcePass;
    }

    public void setXforcePass(String xforcePass) {
        this.xforcePass = xforcePass;
    }

    public String getSonarUrl() {
        return sonarUrl;
    }

    public void setSonarUrl(String sonarUrl) {
        this.sonarUrl = sonarUrl;
    }

    public String getSonarUser() {
        return sonarUser;
    }

    public void setSonarUser(String sonarUser) {
        this.sonarUser = sonarUser;
    }

    public String getSonarPass() {
        return sonarPass;
    }

    public void setSonarPass(String sonarPass) {
        this.sonarPass = sonarPass;
    }

    public String getTrackUrl() {
        return trackUrl;
    }

    public void setTrackUrl(String trackUrl) {
        this.trackUrl = trackUrl;
    }

    public String getTrackKey() {
        return trackKey;
    }

    public void setTrackKey(String trackKey) {
        this.trackKey = trackKey;
    }

    public String getTrackProject() {
        return trackProject;
    }

    public void setTrackProject(String trackProject) {
        this.trackProject = trackProject;
    }

    public String getTrackVersion() {
        return trackVersion;
    }

    public void setTrackVersion(String trackVersion) {
        this.trackVersion = trackVersion;
    }

    public String getAwsRegion() {
        return awsRegion;
    }

    public void setAwsRegion(String awsRegion) {
        this.awsRegion = awsRegion;
    }

    public String getAwsIpinsights() {
        return awsIpinsights;
    }

    public void setAwsIpinsights(String awsIpinsights) {
        this.awsIpinsights = awsIpinsights;
    }

    public String getChatgptUrl() {
        return chatgptUrl;
    }

    public void setChatgptUrl(String chatgptUrl) {
        this.chatgptUrl = chatgptUrl;
    }

    public String getChatgptKey() {
        return chatgptKey;
    }

    public void setChatgptKey(String chatgptKey) {
        this.chatgptKey = chatgptKey;
    }

    public float getChatgptTemperature() {
        return chatgptTemperature;
    }

    public void setChatgptTemperature(float chatgptTemperature) {
        this.chatgptTemperature = chatgptTemperature;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (refId != null ? refId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Project)) {
            return false;
        }
        Project other = (Project) object;
        if ((this.refId == null && other.refId != null) || (this.refId != null && !this.refId.equals(other.refId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.alertflex.entity.Project[ refId=" + refId + " ]";
    }
    
}
