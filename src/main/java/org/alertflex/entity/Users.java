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
@Table(name = "users")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Users.findAll", query = "SELECT u FROM Users u"),
    @NamedQuery(name = "Users.findByProjectId", query = "SELECT u FROM Users u WHERE u.projectId = :projectId"),
    @NamedQuery(name = "Users.findByUserid", query = "SELECT u FROM Users u WHERE u.userid = :userid"),
    @NamedQuery(name = "Users.findByPassword", query = "SELECT u FROM Users u WHERE u.password = :password"),
    @NamedQuery(name = "Users.findByEmail", query = "SELECT u FROM Users u WHERE u.email = :email"),
    @NamedQuery(name = "Users.findByDashboardTimerange", query = "SELECT u FROM Users u WHERE u.dashboardTimerange = :dashboardTimerange"),
    @NamedQuery(name = "Users.findByColorTheme", query = "SELECT u FROM Users u WHERE u.colorTheme = :colorTheme"),
    @NamedQuery(name = "Users.findByLayoutMenu", query = "SELECT u FROM Users u WHERE u.layoutMenu = :layoutMenu"),
    @NamedQuery(name = "Users.findByFontSize", query = "SELECT u FROM Users u WHERE u.fontSize = :fontSize")})
public class Users implements Serializable {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "project_id")
    private String projectId;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "userid")
    private String userid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "password")
    private String password;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "email")
    private String email;
    @Basic(optional = false)
    @NotNull
    @Column(name = "dashboard_timerange")
    private int dashboardTimerange;
    @Basic(optional = false)
    @NotNull
    @Column(name = "color_theme")
    private int colorTheme;
    @Basic(optional = false)
    @NotNull
    @Column(name = "layout_menu")
    private int layoutMenu;
    @Basic(optional = false)
    @NotNull
    @Column(name = "font_size")
    private int fontSize;

    public Users() {
    }

    public Users(String userid) {
        this.userid = userid;
    }

    public Users(String userid, String projectId, String password, String email, int dashboardTimerange, int colorTheme, int layoutMenu, int fontSize) {
        this.userid = userid;
        this.projectId = projectId;
        this.password = password;
        this.email = email;
        this.dashboardTimerange = dashboardTimerange;
        this.colorTheme = colorTheme;
        this.layoutMenu = layoutMenu;
        this.fontSize = fontSize;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getDashboardTimerange() {
        return dashboardTimerange;
    }

    public void setDashboardTimerange(int dashboardTimerange) {
        this.dashboardTimerange = dashboardTimerange;
    }

    public int getColorTheme() {
        return colorTheme;
    }

    public void setColorTheme(int colorTheme) {
        this.colorTheme = colorTheme;
    }

    public int getLayoutMenu() {
        return layoutMenu;
    }

    public void setLayoutMenu(int layoutMenu) {
        this.layoutMenu = layoutMenu;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userid != null ? userid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Users)) {
            return false;
        }
        Users other = (Users) object;
        if ((this.userid == null && other.userid != null) || (this.userid != null && !this.userid.equals(other.userid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.alertflex.entity.Users[ userid=" + userid + " ]";
    }
    
}
