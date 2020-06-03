/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alertflex.mc.db;

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
@Table(name = "groups")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Groups.findAll", query = "SELECT g FROM Groups g")
    , @NamedQuery(name = "Groups.findByGroupid", query = "SELECT g FROM Groups g WHERE g.groupid = :groupid")
    , @NamedQuery(name = "Groups.findByUserid", query = "SELECT g FROM Groups g WHERE g.userid = :userid")})
public class Groups implements Serializable {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "groupid")
    private String groupid;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "userid")
    private String userid;

    public Groups() {
    }

    public Groups(String userid) {
        this.userid = userid;
    }

    public Groups(String userid, String groupid) {
        this.userid = userid;
        this.groupid = groupid;
    }

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
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
        if (!(object instanceof Groups)) {
            return false;
        }
        Groups other = (Groups) object;
        if ((this.userid == null && other.userid != null) || (this.userid != null && !this.userid.equals(other.userid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.alertflex.ids.db.Groups[ userid=" + userid + " ]";
    }
    
}
