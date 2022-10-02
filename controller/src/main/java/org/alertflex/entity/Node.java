/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alertflex.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
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
@Table(name = "node")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Node.findAll", query = "SELECT n FROM Node n"),
    @NamedQuery(name = "Node.findByRefId", query = "SELECT n FROM Node n WHERE n.nodePK.refId = :refId"),
    @NamedQuery(name = "Node.findByName", query = "SELECT n FROM Node n WHERE n.nodePK.name = :name"),
    @NamedQuery(name = "Node.findByDescription", query = "SELECT n FROM Node n WHERE n.description = :description"),
    @NamedQuery(name = "Node.findByNodeType", query = "SELECT n FROM Node n WHERE n.nodeType = :nodeType"),
    @NamedQuery(name = "Node.findByVpc", query = "SELECT n FROM Node n WHERE n.vpc = :vpc"),
    @NamedQuery(name = "Node.findByCommandsControl", query = "SELECT n FROM Node n WHERE n.commandsControl = :commandsControl"),
    @NamedQuery(name = "Node.findByFiltersControl", query = "SELECT n FROM Node n WHERE n.filtersControl = :filtersControl")})
public class Node implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected NodePK nodePK;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "node_type")
    private String nodeType;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "vpc")
    private String vpc;
    @Basic(optional = false)
    @NotNull
    @Column(name = "commands_control")
    private int commandsControl;
    @Basic(optional = false)
    @NotNull
    @Column(name = "filters_control")
    private int filtersControl;

    public Node() {
    }

    public Node(NodePK nodePK) {
        this.nodePK = nodePK;
    }

    public Node(NodePK nodePK, String description, String nodeType, String vpc, int commandsControl, int filtersControl) {
        this.nodePK = nodePK;
        this.description = description;
        this.nodeType = nodeType;
        this.vpc = vpc;
        this.commandsControl = commandsControl;
        this.filtersControl = filtersControl;
    }

    public Node(String refId, String name) {
        this.nodePK = new NodePK(refId, name);
    }

    public NodePK getNodePK() {
        return nodePK;
    }

    public void setNodePK(NodePK nodePK) {
        this.nodePK = nodePK;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public String getVpc() {
        return vpc;
    }

    public void setVpc(String vpc) {
        this.vpc = vpc;
    }

    public int getCommandsControl() {
        return commandsControl;
    }

    public void setCommandsControl(int commandsControl) {
        this.commandsControl = commandsControl;
    }

    public int getFiltersControl() {
        return filtersControl;
    }

    public void setFiltersControl(int filtersControl) {
        this.filtersControl = filtersControl;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (nodePK != null ? nodePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Node)) {
            return false;
        }
        Node other = (Node) object;
        if ((this.nodePK == null && other.nodePK != null) || (this.nodePK != null && !this.nodePK.equals(other.nodePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.alertflex.entity.Node[ nodePK=" + nodePK + " ]";
    }
    
}
