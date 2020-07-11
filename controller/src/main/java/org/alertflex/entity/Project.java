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
    @NamedQuery(name = "Project.findAll", query = "SELECT p FROM Project p")
    , @NamedQuery(name = "Project.findByRefId", query = "SELECT p FROM Project p WHERE p.refId = :refId")
    , @NamedQuery(name = "Project.findByName", query = "SELECT p FROM Project p WHERE p.name = :name")
    , @NamedQuery(name = "Project.findByProjectPath", query = "SELECT p FROM Project p WHERE p.projectPath = :projectPath")
    , @NamedQuery(name = "Project.findByAlertTimerange", query = "SELECT p FROM Project p WHERE p.alertTimerange = :alertTimerange")
    , @NamedQuery(name = "Project.findByStatTimerange", query = "SELECT p FROM Project p WHERE p.statTimerange = :statTimerange")
    , @NamedQuery(name = "Project.findByTaskTimerange", query = "SELECT p FROM Project p WHERE p.taskTimerange = :taskTimerange")
    , @NamedQuery(name = "Project.findByIprepTimerange", query = "SELECT p FROM Project p WHERE p.iprepTimerange = :iprepTimerange")
    , @NamedQuery(name = "Project.findByIncJson", query = "SELECT p FROM Project p WHERE p.incJson = :incJson")
    , @NamedQuery(name = "Project.findByIocCheck", query = "SELECT p FROM Project p WHERE p.iocCheck = :iocCheck")
    , @NamedQuery(name = "Project.findByIocEvent", query = "SELECT p FROM Project p WHERE p.iocEvent = :iocEvent")
    , @NamedQuery(name = "Project.findByIprepCat", query = "SELECT p FROM Project p WHERE p.iprepCat = :iprepCat")
    , @NamedQuery(name = "Project.findByStatRest", query = "SELECT p FROM Project p WHERE p.statRest = :statRest")
    , @NamedQuery(name = "Project.findBySemActive", query = "SELECT p FROM Project p WHERE p.semActive = :semActive")
    , @NamedQuery(name = "Project.findByLogHost", query = "SELECT p FROM Project p WHERE p.logHost = :logHost")
    , @NamedQuery(name = "Project.findByLogPort", query = "SELECT p FROM Project p WHERE p.logPort = :logPort")
    , @NamedQuery(name = "Project.findBySendNetflow", query = "SELECT p FROM Project p WHERE p.sendNetflow = :sendNetflow")
    , @NamedQuery(name = "Project.findByGraylogHost", query = "SELECT p FROM Project p WHERE p.graylogHost = :graylogHost")
    , @NamedQuery(name = "Project.findByGraylogPort", query = "SELECT p FROM Project p WHERE p.graylogPort = :graylogPort")
    , @NamedQuery(name = "Project.findByGraylogUser", query = "SELECT p FROM Project p WHERE p.graylogUser = :graylogUser")
    , @NamedQuery(name = "Project.findByGraylogPass", query = "SELECT p FROM Project p WHERE p.graylogPass = :graylogPass")
    , @NamedQuery(name = "Project.findByElkHost", query = "SELECT p FROM Project p WHERE p.elkHost = :elkHost")
    , @NamedQuery(name = "Project.findByElkPort", query = "SELECT p FROM Project p WHERE p.elkPort = :elkPort")
    , @NamedQuery(name = "Project.findByElkUser", query = "SELECT p FROM Project p WHERE p.elkUser = :elkUser")
    , @NamedQuery(name = "Project.findByElkPass", query = "SELECT p FROM Project p WHERE p.elkPass = :elkPass")
    , @NamedQuery(name = "Project.findByElkStorepass", query = "SELECT p FROM Project p WHERE p.elkStorepass = :elkStorepass")
    , @NamedQuery(name = "Project.findByElkKeystore", query = "SELECT p FROM Project p WHERE p.elkKeystore = :elkKeystore")
    , @NamedQuery(name = "Project.findByElkTruststore", query = "SELECT p FROM Project p WHERE p.elkTruststore = :elkTruststore")
    , @NamedQuery(name = "Project.findByMongoUrl", query = "SELECT p FROM Project p WHERE p.mongoUrl = :mongoUrl")
    , @NamedQuery(name = "Project.findByHiveUrl", query = "SELECT p FROM Project p WHERE p.hiveUrl = :hiveUrl")
    , @NamedQuery(name = "Project.findByHiveKey", query = "SELECT p FROM Project p WHERE p.hiveKey = :hiveKey")
    , @NamedQuery(name = "Project.findByMispUrl", query = "SELECT p FROM Project p WHERE p.mispUrl = :mispUrl")
    , @NamedQuery(name = "Project.findByMispKey", query = "SELECT p FROM Project p WHERE p.mispKey = :mispKey")
    , @NamedQuery(name = "Project.findByJiraUrl", query = "SELECT p FROM Project p WHERE p.jiraUrl = :jiraUrl")
    , @NamedQuery(name = "Project.findByJiraUser", query = "SELECT p FROM Project p WHERE p.jiraUser = :jiraUser")
    , @NamedQuery(name = "Project.findByJiraPass", query = "SELECT p FROM Project p WHERE p.jiraPass = :jiraPass")
    , @NamedQuery(name = "Project.findByJiraProject", query = "SELECT p FROM Project p WHERE p.jiraProject = :jiraProject")
    , @NamedQuery(name = "Project.findByJiraType", query = "SELECT p FROM Project p WHERE p.jiraType = :jiraType")
    , @NamedQuery(name = "Project.findByVtKey", query = "SELECT p FROM Project p WHERE p.vtKey = :vtKey")
    , @NamedQuery(name = "Project.findBySmsAccount", query = "SELECT p FROM Project p WHERE p.smsAccount = :smsAccount")
    , @NamedQuery(name = "Project.findBySmsToken", query = "SELECT p FROM Project p WHERE p.smsToken = :smsToken")
    , @NamedQuery(name = "Project.findBySmsFrom", query = "SELECT p FROM Project p WHERE p.smsFrom = :smsFrom")
    , @NamedQuery(name = "Project.findBySlackHook", query = "SELECT p FROM Project p WHERE p.slackHook = :slackHook")
    , @NamedQuery(name = "Project.findByZapHost", query = "SELECT p FROM Project p WHERE p.zapHost = :zapHost")
    , @NamedQuery(name = "Project.findByZapPort", query = "SELECT p FROM Project p WHERE p.zapPort = :zapPort")
    , @NamedQuery(name = "Project.findByZapKey", query = "SELECT p FROM Project p WHERE p.zapKey = :zapKey")
    , @NamedQuery(name = "Project.findBySonarUrl", query = "SELECT p FROM Project p WHERE p.sonarUrl = :sonarUrl")
    , @NamedQuery(name = "Project.findBySonarUser", query = "SELECT p FROM Project p WHERE p.sonarUser = :sonarUser")
    , @NamedQuery(name = "Project.findBySonarPass", query = "SELECT p FROM Project p WHERE p.sonarPass = :sonarPass")
    , @NamedQuery(name = "Project.findByCuckooHost", query = "SELECT p FROM Project p WHERE p.cuckooHost = :cuckooHost")
    , @NamedQuery(name = "Project.findByCuckooPort", query = "SELECT p FROM Project p WHERE p.cuckooPort = :cuckooPort")
    , @NamedQuery(name = "Project.findByFalconUrl", query = "SELECT p FROM Project p WHERE p.falconUrl = :falconUrl")
    , @NamedQuery(name = "Project.findByFalconKey", query = "SELECT p FROM Project p WHERE p.falconKey = :falconKey")
    , @NamedQuery(name = "Project.findByVmrayUrl", query = "SELECT p FROM Project p WHERE p.vmrayUrl = :vmrayUrl")
    , @NamedQuery(name = "Project.findByVmrayKey", query = "SELECT p FROM Project p WHERE p.vmrayKey = :vmrayKey")
    , @NamedQuery(name = "Project.findByMailSmtp", query = "SELECT p FROM Project p WHERE p.mailSmtp = :mailSmtp")
    , @NamedQuery(name = "Project.findByMailPort", query = "SELECT p FROM Project p WHERE p.mailPort = :mailPort")
    , @NamedQuery(name = "Project.findByMailUser", query = "SELECT p FROM Project p WHERE p.mailUser = :mailUser")
    , @NamedQuery(name = "Project.findByMailPass", query = "SELECT p FROM Project p WHERE p.mailPass = :mailPass")
    , @NamedQuery(name = "Project.findByMailFrom", query = "SELECT p FROM Project p WHERE p.mailFrom = :mailFrom")})
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
    @Column(name = "iprep_timerange")
    private int iprepTimerange;
    @Basic(optional = false)
    @NotNull
    @Column(name = "inc_json")
    private int incJson;
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
    @Column(name = "iprep_cat")
    private int iprepCat;
    @Basic(optional = false)
    @NotNull
    @Column(name = "stat_rest")
    private int statRest;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sem_active")
    private int semActive;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "log_host")
    private String logHost;
    @Basic(optional = false)
    @NotNull
    @Column(name = "log_port")
    private int logPort;
    @Basic(optional = false)
    @NotNull
    @Column(name = "send_netflow")
    private int sendNetflow;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "graylog_host")
    private String graylogHost;
    @Basic(optional = false)
    @NotNull
    @Column(name = "graylog_port")
    private int graylogPort;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "graylog_user")
    private String graylogUser;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "graylog_pass")
    private String graylogPass;
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
    @Column(name = "mongo_url")
    private String mongoUrl;
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
    @Size(min = 1, max = 255)
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
    @Column(name = "sms_account")
    private String smsAccount;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "sms_token")
    private String smsToken;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "sms_from")
    private String smsFrom;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "slack_hook")
    private String slackHook;
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
    @Size(min = 1, max = 1024)
    @Column(name = "zap_key")
    private String zapKey;
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
    @Column(name = "mail_smtp")
    private String mailSmtp;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 32)
    @Column(name = "mail_port")
    private String mailPort;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "mail_user")
    private String mailUser;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "mail_pass")
    private String mailPass;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "mail_from")
    private String mailFrom;

    public Project() {
    }

    public Project(String refId) {
        this.refId = refId;
    }

    public Project(String refId, String name, String projectPath, int alertTimerange, int statTimerange, int taskTimerange, int iprepTimerange, int incJson, int iocCheck, int iocEvent, int iprepCat, int statRest, int semActive, String logHost, int logPort, int sendNetflow, String graylogHost, int graylogPort, String graylogUser, String graylogPass, String elkHost, int elkPort, String elkUser, String elkPass, String elkStorepass, String elkKeystore, String elkTruststore, String mongoUrl, String hiveUrl, String hiveKey, String mispUrl, String mispKey, String jiraUrl, String jiraUser, String jiraPass, String jiraProject, String jiraType, String vtKey, String smsAccount, String smsToken, String smsFrom, String slackHook, String zapHost, int zapPort, String zapKey, String sonarUrl, String sonarUser, String sonarPass, String cuckooHost, int cuckooPort, String falconUrl, String falconKey, String vmrayUrl, String vmrayKey, String mailSmtp, String mailPort, String mailUser, String mailPass, String mailFrom) {
        this.refId = refId;
        this.name = name;
        this.projectPath = projectPath;
        this.alertTimerange = alertTimerange;
        this.statTimerange = statTimerange;
        this.taskTimerange = taskTimerange;
        this.iprepTimerange = iprepTimerange;
        this.incJson = incJson;
        this.iocCheck = iocCheck;
        this.iocEvent = iocEvent;
        this.iprepCat = iprepCat;
        this.statRest = statRest;
        this.semActive = semActive;
        this.logHost = logHost;
        this.logPort = logPort;
        this.sendNetflow = sendNetflow;
        this.graylogHost = graylogHost;
        this.graylogPort = graylogPort;
        this.graylogUser = graylogUser;
        this.graylogPass = graylogPass;
        this.elkHost = elkHost;
        this.elkPort = elkPort;
        this.elkUser = elkUser;
        this.elkPass = elkPass;
        this.elkStorepass = elkStorepass;
        this.elkKeystore = elkKeystore;
        this.elkTruststore = elkTruststore;
        this.mongoUrl = mongoUrl;
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
        this.smsAccount = smsAccount;
        this.smsToken = smsToken;
        this.smsFrom = smsFrom;
        this.slackHook = slackHook;
        this.zapHost = zapHost;
        this.zapPort = zapPort;
        this.zapKey = zapKey;
        this.sonarUrl = sonarUrl;
        this.sonarUser = sonarUser;
        this.sonarPass = sonarPass;
        this.cuckooHost = cuckooHost;
        this.cuckooPort = cuckooPort;
        this.falconUrl = falconUrl;
        this.falconKey = falconKey;
        this.vmrayUrl = vmrayUrl;
        this.vmrayKey = vmrayKey;
        this.mailSmtp = mailSmtp;
        this.mailPort = mailPort;
        this.mailUser = mailUser;
        this.mailPass = mailPass;
        this.mailFrom = mailFrom;
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

    public int getIprepTimerange() {
        return iprepTimerange;
    }

    public void setIprepTimerange(int iprepTimerange) {
        this.iprepTimerange = iprepTimerange;
    }

    public int getIncJson() {
        return incJson;
    }

    public void setIncJson(int incJson) {
        this.incJson = incJson;
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

    public int getIprepCat() {
        return iprepCat;
    }

    public void setIprepCat(int iprepCat) {
        this.iprepCat = iprepCat;
    }

    public int getStatRest() {
        return statRest;
    }

    public void setStatRest(int statRest) {
        this.statRest = statRest;
    }

    public int getSemActive() {
        return semActive;
    }

    public void setSemActive(int semActive) {
        this.semActive = semActive;
    }

    public String getLogHost() {
        return logHost;
    }

    public void setLogHost(String logHost) {
        this.logHost = logHost;
    }

    public int getLogPort() {
        return logPort;
    }

    public void setLogPort(int logPort) {
        this.logPort = logPort;
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

    public String getGraylogUser() {
        return graylogUser;
    }

    public void setGraylogUser(String graylogUser) {
        this.graylogUser = graylogUser;
    }

    public String getGraylogPass() {
        return graylogPass;
    }

    public void setGraylogPass(String graylogPass) {
        this.graylogPass = graylogPass;
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

    public String getMongoUrl() {
        return mongoUrl;
    }

    public void setMongoUrl(String mongoUrl) {
        this.mongoUrl = mongoUrl;
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

    public String getSmsAccount() {
        return smsAccount;
    }

    public void setSmsAccount(String smsAccount) {
        this.smsAccount = smsAccount;
    }

    public String getSmsToken() {
        return smsToken;
    }

    public void setSmsToken(String smsToken) {
        this.smsToken = smsToken;
    }

    public String getSmsFrom() {
        return smsFrom;
    }

    public void setSmsFrom(String smsFrom) {
        this.smsFrom = smsFrom;
    }

    public String getSlackHook() {
        return slackHook;
    }

    public void setSlackHook(String slackHook) {
        this.slackHook = slackHook;
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

    public String getMailSmtp() {
        return mailSmtp;
    }

    public void setMailSmtp(String mailSmtp) {
        this.mailSmtp = mailSmtp;
    }

    public String getMailPort() {
        return mailPort;
    }

    public void setMailPort(String mailPort) {
        this.mailPort = mailPort;
    }

    public String getMailUser() {
        return mailUser;
    }

    public void setMailUser(String mailUser) {
        this.mailUser = mailUser;
    }

    public String getMailPass() {
        return mailPass;
    }

    public void setMailPass(String mailPass) {
        this.mailPass = mailPass;
    }

    public String getMailFrom() {
        return mailFrom;
    }

    public void setMailFrom(String mailFrom) {
        this.mailFrom = mailFrom;
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
