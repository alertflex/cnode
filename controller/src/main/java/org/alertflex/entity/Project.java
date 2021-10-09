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
    @NamedQuery(name = "Project.findByAlertTimerange", query = "SELECT p FROM Project p WHERE p.alertTimerange = :alertTimerange"),
    @NamedQuery(name = "Project.findByStatTimerange", query = "SELECT p FROM Project p WHERE p.statTimerange = :statTimerange"),
    @NamedQuery(name = "Project.findByTaskTimerange", query = "SELECT p FROM Project p WHERE p.taskTimerange = :taskTimerange"),
    @NamedQuery(name = "Project.findByBlockIprange", query = "SELECT p FROM Project p WHERE p.blockIprange = :blockIprange"),
    @NamedQuery(name = "Project.findByIocCheck", query = "SELECT p FROM Project p WHERE p.iocCheck = :iocCheck"),
    @NamedQuery(name = "Project.findByIocEvent", query = "SELECT p FROM Project p WHERE p.iocEvent = :iocEvent"),
    @NamedQuery(name = "Project.findByPrometheusStat", query = "SELECT p FROM Project p WHERE p.prometheusStat = :prometheusStat"),
    @NamedQuery(name = "Project.findBySendNetflow", query = "SELECT p FROM Project p WHERE p.sendNetflow = :sendNetflow"),
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
    @NamedQuery(name = "Project.findByJiraUrl", query = "SELECT p FROM Project p WHERE p.jiraUrl = :jiraUrl"),
    @NamedQuery(name = "Project.findByJiraUser", query = "SELECT p FROM Project p WHERE p.jiraUser = :jiraUser"),
    @NamedQuery(name = "Project.findByJiraPass", query = "SELECT p FROM Project p WHERE p.jiraPass = :jiraPass"),
    @NamedQuery(name = "Project.findByJiraProject", query = "SELECT p FROM Project p WHERE p.jiraProject = :jiraProject"),
    @NamedQuery(name = "Project.findByJiraType", query = "SELECT p FROM Project p WHERE p.jiraType = :jiraType"),
    @NamedQuery(name = "Project.findByVtKey", query = "SELECT p FROM Project p WHERE p.vtKey = :vtKey"),
    @NamedQuery(name = "Project.findByTwiliosmsAccount", query = "SELECT p FROM Project p WHERE p.twiliosmsAccount = :twiliosmsAccount"),
    @NamedQuery(name = "Project.findByTwiliosmsToken", query = "SELECT p FROM Project p WHERE p.twiliosmsToken = :twiliosmsToken"),
    @NamedQuery(name = "Project.findByTwiliosmsFrom", query = "SELECT p FROM Project p WHERE p.twiliosmsFrom = :twiliosmsFrom"),
    @NamedQuery(name = "Project.findByTwiliomailKey", query = "SELECT p FROM Project p WHERE p.twiliomailKey = :twiliomailKey"),
    @NamedQuery(name = "Project.findByTwiliomailFrom", query = "SELECT p FROM Project p WHERE p.twiliomailFrom = :twiliomailFrom"),
    @NamedQuery(name = "Project.findBySlackHook", query = "SELECT p FROM Project p WHERE p.slackHook = :slackHook"),
    @NamedQuery(name = "Project.findByCuckooHost", query = "SELECT p FROM Project p WHERE p.cuckooHost = :cuckooHost"),
    @NamedQuery(name = "Project.findByCuckooPort", query = "SELECT p FROM Project p WHERE p.cuckooPort = :cuckooPort"),
    @NamedQuery(name = "Project.findByFalconUrl", query = "SELECT p FROM Project p WHERE p.falconUrl = :falconUrl"),
    @NamedQuery(name = "Project.findByFalconKey", query = "SELECT p FROM Project p WHERE p.falconKey = :falconKey"),
    @NamedQuery(name = "Project.findByVmrayUrl", query = "SELECT p FROM Project p WHERE p.vmrayUrl = :vmrayUrl"),
    @NamedQuery(name = "Project.findByVmrayKey", query = "SELECT p FROM Project p WHERE p.vmrayKey = :vmrayKey"),
    @NamedQuery(name = "Project.findBySonarUrl", query = "SELECT p FROM Project p WHERE p.sonarUrl = :sonarUrl"),
    @NamedQuery(name = "Project.findBySonarUser", query = "SELECT p FROM Project p WHERE p.sonarUser = :sonarUser"),
    @NamedQuery(name = "Project.findBySonarPass", query = "SELECT p FROM Project p WHERE p.sonarPass = :sonarPass"),
    @NamedQuery(name = "Project.findByZapHost", query = "SELECT p FROM Project p WHERE p.zapHost = :zapHost"),
    @NamedQuery(name = "Project.findByZapPort", query = "SELECT p FROM Project p WHERE p.zapPort = :zapPort"),
    @NamedQuery(name = "Project.findByZapKey", query = "SELECT p FROM Project p WHERE p.zapKey = :zapKey"),
    @NamedQuery(name = "Project.findByXforceKey", query = "SELECT p FROM Project p WHERE p.xforceKey = :xforceKey"),
    @NamedQuery(name = "Project.findByXforcePass", query = "SELECT p FROM Project p WHERE p.xforcePass = :xforcePass"),
    @NamedQuery(name = "Project.findByAwsRegion", query = "SELECT p FROM Project p WHERE p.awsRegion = :awsRegion"),
    @NamedQuery(name = "Project.findByAwsIpinsights", query = "SELECT p FROM Project p WHERE p.awsIpinsights = :awsIpinsights"),
    @NamedQuery(name = "Project.findBySqsCloudtrail", query = "SELECT p FROM Project p WHERE p.sqsCloudtrail = :sqsCloudtrail")})
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
    @Column(name = "alert_timerange")
    private int alertTimerange;
    @Basic(optional = false)
    @NotNull
    @Column(name = "stat_timerange")
    private int statTimerange;
    @Basic(optional = false)
    @NotNull
    @Column(name = "task_timerange")
    private int taskTimerange;
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
    @Column(name = "jira_url")
    private String jiraUrl;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "jira_user")
    private String jiraUser;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "jira_pass")
    private String jiraPass;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "jira_project")
    private String jiraProject;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "jira_type")
    private String jiraType;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "vt_key")
    private String vtKey;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "twiliosms_account")
    private String twiliosmsAccount;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "twiliosms_token")
    private String twiliosmsToken;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "twiliosms_from")
    private String twiliosmsFrom;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "twiliomail_key")
    private String twiliomailKey;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "twiliomail_from")
    private String twiliomailFrom;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "slack_hook")
    private String slackHook;
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
    @Column(name = "vmray_url")
    private String vmrayUrl;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "vmray_key")
    private String vmrayKey;
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
    @Column(name = "zap_host")
    private String zapHost;
    @Basic(optional = false)
    @NotNull
    @Column(name = "zap_port")
    private int zapPort;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "zap_key")
    private String zapKey;
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
    @Size(min = 1, max = 256)
    @Column(name = "sqs_cloudtrail")
    private String sqsCloudtrail;

    public Project() {
    }

    public Project(String refId) {
        this.refId = refId;
    }

    public Project(String refId, String name, String projectPath, int semActive, int alertLimit, int alertTimerange, int statTimerange, int taskTimerange, int blockIprange, int iocCheck, int iocEvent, int prometheusStat, int sendNetflow, String graylogHost, int graylogPort, String elkHost, int elkPort, String elkUser, String elkPass, String elkStorepass, String elkKeystore, String elkTruststore, String hiveUrl, String hiveKey, String mispUrl, String mispKey, String jiraUrl, String jiraUser, String jiraPass, String jiraProject, String jiraType, String vtKey, String twiliosmsAccount, String twiliosmsToken, String twiliosmsFrom, String twiliomailKey, String twiliomailFrom, String slackHook, String cuckooHost, int cuckooPort, String falconUrl, String falconKey, String vmrayUrl, String vmrayKey, String sonarUrl, String sonarUser, String sonarPass, String zapHost, int zapPort, String zapKey, String xforceKey, String xforcePass, String awsRegion, String awsIpinsights, String sqsCloudtrail) {
        this.refId = refId;
        this.name = name;
        this.projectPath = projectPath;
        this.semActive = semActive;
        this.alertLimit = alertLimit;
        this.alertTimerange = alertTimerange;
        this.statTimerange = statTimerange;
        this.taskTimerange = taskTimerange;
        this.blockIprange = blockIprange;
        this.iocCheck = iocCheck;
        this.iocEvent = iocEvent;
        this.prometheusStat = prometheusStat;
        this.sendNetflow = sendNetflow;
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
        this.jiraUrl = jiraUrl;
        this.jiraUser = jiraUser;
        this.jiraPass = jiraPass;
        this.jiraProject = jiraProject;
        this.jiraType = jiraType;
        this.vtKey = vtKey;
        this.twiliosmsAccount = twiliosmsAccount;
        this.twiliosmsToken = twiliosmsToken;
        this.twiliosmsFrom = twiliosmsFrom;
        this.twiliomailKey = twiliomailKey;
        this.twiliomailFrom = twiliomailFrom;
        this.slackHook = slackHook;
        this.cuckooHost = cuckooHost;
        this.cuckooPort = cuckooPort;
        this.falconUrl = falconUrl;
        this.falconKey = falconKey;
        this.vmrayUrl = vmrayUrl;
        this.vmrayKey = vmrayKey;
        this.sonarUrl = sonarUrl;
        this.sonarUser = sonarUser;
        this.sonarPass = sonarPass;
        this.zapHost = zapHost;
        this.zapPort = zapPort;
        this.zapKey = zapKey;
        this.xforceKey = xforceKey;
        this.xforcePass = xforcePass;
        this.awsRegion = awsRegion;
        this.awsIpinsights = awsIpinsights;
        this.sqsCloudtrail = sqsCloudtrail;
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

    public int getAlertTimerange() {
        return alertTimerange;
    }

    public void setAlertTimerange(int alertTimerange) {
        this.alertTimerange = alertTimerange;
    }

    public int getStatTimerange() {
        return statTimerange;
    }

    public void setStatTimerange(int statTimerange) {
        this.statTimerange = statTimerange;
    }

    public int getTaskTimerange() {
        return taskTimerange;
    }

    public void setTaskTimerange(int taskTimerange) {
        this.taskTimerange = taskTimerange;
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

    public String getJiraUrl() {
        return jiraUrl;
    }

    public void setJiraUrl(String jiraUrl) {
        this.jiraUrl = jiraUrl;
    }

    public String getJiraUser() {
        return jiraUser;
    }

    public void setJiraUser(String jiraUser) {
        this.jiraUser = jiraUser;
    }

    public String getJiraPass() {
        return jiraPass;
    }

    public void setJiraPass(String jiraPass) {
        this.jiraPass = jiraPass;
    }

    public String getJiraProject() {
        return jiraProject;
    }

    public void setJiraProject(String jiraProject) {
        this.jiraProject = jiraProject;
    }

    public String getJiraType() {
        return jiraType;
    }

    public void setJiraType(String jiraType) {
        this.jiraType = jiraType;
    }

    public String getVtKey() {
        return vtKey;
    }

    public void setVtKey(String vtKey) {
        this.vtKey = vtKey;
    }

    public String getTwiliosmsAccount() {
        return twiliosmsAccount;
    }

    public void setTwiliosmsAccount(String twiliosmsAccount) {
        this.twiliosmsAccount = twiliosmsAccount;
    }

    public String getTwiliosmsToken() {
        return twiliosmsToken;
    }

    public void setTwiliosmsToken(String twiliosmsToken) {
        this.twiliosmsToken = twiliosmsToken;
    }

    public String getTwiliosmsFrom() {
        return twiliosmsFrom;
    }

    public void setTwiliosmsFrom(String twiliosmsFrom) {
        this.twiliosmsFrom = twiliosmsFrom;
    }

    public String getTwiliomailKey() {
        return twiliomailKey;
    }

    public void setTwiliomailKey(String twiliomailKey) {
        this.twiliomailKey = twiliomailKey;
    }

    public String getTwiliomailFrom() {
        return twiliomailFrom;
    }

    public void setTwiliomailFrom(String twiliomailFrom) {
        this.twiliomailFrom = twiliomailFrom;
    }

    public String getSlackHook() {
        return slackHook;
    }

    public void setSlackHook(String slackHook) {
        this.slackHook = slackHook;
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

    public String getVmrayUrl() {
        return vmrayUrl;
    }

    public void setVmrayUrl(String vmrayUrl) {
        this.vmrayUrl = vmrayUrl;
    }

    public String getVmrayKey() {
        return vmrayKey;
    }

    public void setVmrayKey(String vmrayKey) {
        this.vmrayKey = vmrayKey;
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

    public String getZapHost() {
        return zapHost;
    }

    public void setZapHost(String zapHost) {
        this.zapHost = zapHost;
    }

    public int getZapPort() {
        return zapPort;
    }

    public void setZapPort(int zapPort) {
        this.zapPort = zapPort;
    }

    public String getZapKey() {
        return zapKey;
    }

    public void setZapKey(String zapKey) {
        this.zapKey = zapKey;
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

    public String getSqsCloudtrail() {
        return sqsCloudtrail;
    }

    public void setSqsCloudtrail(String sqsCloudtrail) {
        this.sqsCloudtrail = sqsCloudtrail;
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
