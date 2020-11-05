/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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

/**
 *
 * @author root
 */
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
