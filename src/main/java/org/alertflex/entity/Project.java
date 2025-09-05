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
    @NamedQuery(name = "Project.findByProjectId", query = "SELECT p FROM Project p WHERE p.projectId = :projectId"),
    @NamedQuery(name = "Project.findByProjectName", query = "SELECT p FROM Project p WHERE p.projectName = :projectName"),
    @NamedQuery(name = "Project.findByProjectStatus", query = "SELECT p FROM Project p WHERE p.projectStatus = :projectStatus"),
    @NamedQuery(name = "Project.findByAlertsLimit", query = "SELECT p FROM Project p WHERE p.alertsLimit = :alertsLimit"),
    @NamedQuery(name = "Project.findByAlertsTtl", query = "SELECT p FROM Project p WHERE p.alertsTtl = :alertsTtl"),
    @NamedQuery(name = "Project.findByTasksTtl", query = "SELECT p FROM Project p WHERE p.tasksTtl = :tasksTtl"),
    @NamedQuery(name = "Project.findByApiKey", query = "SELECT p FROM Project p WHERE p.apiKey = :apiKey"),
    @NamedQuery(name = "Project.findByApiAuth", query = "SELECT p FROM Project p WHERE p.apiAuth = :apiAuth")})
public class Project implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "project_id")
    private String projectId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "project_name")
    private String projectName;
    @Basic(optional = false)
    @NotNull
    @Column(name = "project_status")
    private int projectStatus;
    @Basic(optional = false)
    @NotNull
    @Column(name = "alerts_limit")
    private int alertsLimit;
    @Basic(optional = false)
    @NotNull
    @Column(name = "alerts_ttl")
    private int alertsTtl;
    @Basic(optional = false)
    @NotNull
    @Column(name = "tasks_ttl")
    private int tasksTtl;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "api_key")
    private String apiKey;
    @Basic(optional = false)
    @NotNull
    @Column(name = "api_auth")
    private int apiAuth;

    public Project() {
    }

    public Project(String projectId) {
        this.projectId = projectId;
    }

    public Project(String projectId, String projectName, int projectStatus, int alertsLimit, int alertsTtl, int tasksTtl, String apiKey, int apiAuth) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.projectStatus = projectStatus;
        this.alertsLimit = alertsLimit;
        this.alertsTtl = alertsTtl;
        this.tasksTtl = tasksTtl;
        this.apiKey = apiKey;
        this.apiAuth = apiAuth;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public int getProjectStatus() {
        return projectStatus;
    }

    public void setProjectStatus(int projectStatus) {
        this.projectStatus = projectStatus;
    }

    public int getAlertsLimit() {
        return alertsLimit;
    }

    public void setAlertsLimit(int alertsLimit) {
        this.alertsLimit = alertsLimit;
    }

    public int getAlertsTtl() {
        return alertsTtl;
    }

    public void setAlertsTtl(int alertsTtl) {
        this.alertsTtl = alertsTtl;
    }

    public int getTasksTtl() {
        return tasksTtl;
    }

    public void setTasksTtl(int tasksTtl) {
        this.tasksTtl = tasksTtl;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public int getApiAuth() {
        return apiAuth;
    }

    public void setApiAuth(int apiAuth) {
        this.apiAuth = apiAuth;
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
        if (!(object instanceof Project)) {
            return false;
        }
        Project other = (Project) object;
        if ((this.projectId == null && other.projectId != null) || (this.projectId != null && !this.projectId.equals(other.projectId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.alertflex.entity.Project[ projectId=" + projectId + " ]";
    }
    
}
