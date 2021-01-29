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
 
package org.alertflex.facade;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.CacheRetrieveMode;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.alertflex.entity.Node;

@Stateless
public class NodeFacade extends AbstractFacade<Node> {

    @PersistenceContext(unitName = "alertflex_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public NodeFacade() {
        super(Node.class);
    }

    public List<Node> findByRef(String r) {

        List<Node> nodeList = null;

        try {
            em.flush();

            Query listProbesQry = em.createQuery("SELECT n FROM Node n WHERE n.nodePK.refId = :ref").setParameter("ref", r);

            listProbesQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            nodeList = (List<Node>) listProbesQry.getResultList();

            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully Authenticated", ""));
        } catch (Exception e) {
            return null;
        }

        return nodeList;
    }

    public Node findByNodeName(String r, String n) {
        Node node = null;

        try {
            em.flush();

            Query listProbesQry = em.createQuery("SELECT n FROM Node n WHERE n.nodePK.refId = :ref AND n.nodePK.name = :node").setParameter("ref", r).setParameter("node", n);

            listProbesQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            node = (Node) listProbesQry.getSingleResult();

            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully Authenticated", ""));
        } catch (Exception e) {
            return null;
        }

        return node;
    }
}
