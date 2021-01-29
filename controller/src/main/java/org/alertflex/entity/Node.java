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

@Entity
@Table(name = "node")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Node.findAll", query = "SELECT n FROM Node n")
    , @NamedQuery(name = "Node.findByRefId", query = "SELECT n FROM Node n WHERE n.nodePK.refId = :refId")
    , @NamedQuery(name = "Node.findByName", query = "SELECT n FROM Node n WHERE n.nodePK.name = :name")
    , @NamedQuery(name = "Node.findByDescription", query = "SELECT n FROM Node n WHERE n.description = :description")
    , @NamedQuery(name = "Node.findByUnit", query = "SELECT n FROM Node n WHERE n.unit = :unit")
    , @NamedQuery(name = "Node.findByOpenC2", query = "SELECT n FROM Node n WHERE n.openC2 = :openC2")
    , @NamedQuery(name = "Node.findByFiltersControl", query = "SELECT n FROM Node n WHERE n.filtersControl = :filtersControl")})
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
    @Size(min = 1, max = 512)
    @Column(name = "unit")
    private String unit;
    @Basic(optional = false)
    @NotNull
    @Column(name = "open_c2")
    private int openC2;
    @Basic(optional = false)
    @NotNull
    @Column(name = "filters_control")
    private int filtersControl;

    public Node() {
    }

    public Node(NodePK nodePK) {
        this.nodePK = nodePK;
    }

    public Node(NodePK nodePK, String description, String unit, int openC2, int filtersControl) {
        this.nodePK = nodePK;
        this.description = description;
        this.unit = unit;
        this.openC2 = openC2;
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

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getOpenC2() {
        return openC2;
    }

    public void setOpenC2(int openC2) {
        this.openC2 = openC2;
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
