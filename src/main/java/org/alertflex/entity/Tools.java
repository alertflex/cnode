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
@Table(name = "tools")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Tools.findAll", query = "SELECT t FROM Tools t"),
    @NamedQuery(name = "Tools.findByProjectId", query = "SELECT t FROM Tools t WHERE t.projectId = :projectId"),
    @NamedQuery(name = "Tools.findByServiceUrl", query = "SELECT t FROM Tools t WHERE t.serviceUrl = :serviceUrl"),
    @NamedQuery(name = "Tools.findByServiceKey", query = "SELECT t FROM Tools t WHERE t.serviceKey = :serviceKey"),
    @NamedQuery(name = "Tools.findByOpensearchUrl", query = "SELECT t FROM Tools t WHERE t.opensearchUrl = :opensearchUrl"),
    @NamedQuery(name = "Tools.findByOpensearchPort", query = "SELECT t FROM Tools t WHERE t.opensearchPort = :opensearchPort"),
    @NamedQuery(name = "Tools.findByOpensearchUser", query = "SELECT t FROM Tools t WHERE t.opensearchUser = :opensearchUser"),
    @NamedQuery(name = "Tools.findByOpensearchPwd", query = "SELECT t FROM Tools t WHERE t.opensearchPwd = :opensearchPwd"),
    @NamedQuery(name = "Tools.findByDiscordHook", query = "SELECT t FROM Tools t WHERE t.discordHook = :discordHook"),
    @NamedQuery(name = "Tools.findByTelegramChatid", query = "SELECT t FROM Tools t WHERE t.telegramChatid = :telegramChatid"),
    @NamedQuery(name = "Tools.findByTelegramKey", query = "SELECT t FROM Tools t WHERE t.telegramKey = :telegramKey")})
public class Tools implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "project_id")
    private String projectId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "service_url")
    private String serviceUrl;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "service_key")
    private String serviceKey;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "opensearch_url")
    private String opensearchUrl;
    @Basic(optional = false)
    @NotNull
    @Column(name = "opensearch_port")
    private int opensearchPort;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "opensearch_user")
    private String opensearchUser;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "opensearch_pwd")
    private String opensearchPwd;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "discord_hook")
    private String discordHook;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "telegram_chatid")
    private String telegramChatid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "telegram_key")
    private String telegramKey;

    public Tools() {
    }

    public Tools(String projectId) {
        this.projectId = projectId;
    }

    public Tools(String projectId, String serviceUrl, String serviceKey, String opensearchUrl, int opensearchPort, String opensearchUser, String opensearchPwd, String discordHook, String telegramChatid, String telegramKey) {
        this.projectId = projectId;
        this.serviceUrl = serviceUrl;
        this.serviceKey = serviceKey;
        this.opensearchUrl = opensearchUrl;
        this.opensearchPort = opensearchPort;
        this.opensearchUser = opensearchUser;
        this.opensearchPwd = opensearchPwd;
        this.discordHook = discordHook;
        this.telegramChatid = telegramChatid;
        this.telegramKey = telegramKey;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getServiceUrl() {
        return serviceUrl;
    }

    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    public String getServiceKey() {
        return serviceKey;
    }

    public void setServiceKey(String serviceKey) {
        this.serviceKey = serviceKey;
    }

    public String getOpensearchUrl() {
        return opensearchUrl;
    }

    public void setOpensearchUrl(String opensearchUrl) {
        this.opensearchUrl = opensearchUrl;
    }

    public int getOpensearchPort() {
        return opensearchPort;
    }

    public void setOpensearchPort(int opensearchPort) {
        this.opensearchPort = opensearchPort;
    }

    public String getOpensearchUser() {
        return opensearchUser;
    }

    public void setOpensearchUser(String opensearchUser) {
        this.opensearchUser = opensearchUser;
    }

    public String getOpensearchPwd() {
        return opensearchPwd;
    }

    public void setOpensearchPwd(String opensearchPwd) {
        this.opensearchPwd = opensearchPwd;
    }

    public String getDiscordHook() {
        return discordHook;
    }

    public void setDiscordHook(String discordHook) {
        this.discordHook = discordHook;
    }

    public String getTelegramChatid() {
        return telegramChatid;
    }

    public void setTelegramChatid(String telegramChatid) {
        this.telegramChatid = telegramChatid;
    }

    public String getTelegramKey() {
        return telegramKey;
    }

    public void setTelegramKey(String telegramKey) {
        this.telegramKey = telegramKey;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (projectId != null ? projectId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tools)) {
            return false;
        }
        Tools other = (Tools) object;
        if ((this.projectId == null && other.projectId != null) || (this.projectId != null && !this.projectId.equals(other.projectId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.alertflex.entity.Tools[ projectId=" + projectId + " ]";
    }
    
}
