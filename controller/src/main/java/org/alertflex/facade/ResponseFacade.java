/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alertflex.facade;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.CacheRetrieveMode;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.alertflex.entity.Response;

/**
 *
 * @author root
 */
@Stateless
public class ResponseFacade extends AbstractFacade<Response> {

    @PersistenceContext(unitName = "alertflex_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ResponseFacade() {
        super(Response.class);
    }

    public Response findResponseForAction(String ref, String source, String action) {

        Response r = null;

        try {
            em.flush();

            Query listQry = em.createQuery("SELECT r FROM Response r WHERE r.refId = :ref AND r.alertSource = :source AND r.status = :status AND r.resType = :type AND r.resCause = :action")
                    .setParameter("ref", ref)
                    .setParameter("status", 1)
                    .setParameter("source", source)
                    .setParameter("type", "action")
                    .setParameter("action", action);
            // Enable forced database query
            listQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            r = (Response) listQry.getSingleResult();

        } catch (Exception e) {

            r = null;
        }

        return r;
    }

    public List<Response> findResponseForEvent(String ref, String source, String event) {

        List<Response> l = new ArrayList();

        try {
            em.flush();

            Query listQry = em.createQuery("SELECT r FROM Response r WHERE r.refId = :ref AND r.alertSource = :source AND r.status = :status AND r.resType = :type AND r.resCause = :event")
                    .setParameter("ref", ref)
                    .setParameter("status", 1)
                    .setParameter("source", source)
                    .setParameter("type", "event")
                    .setParameter("event", event);
            // Enable forced database query
            listQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            l = listQry.getResultList();

        } catch (Exception e) {

            l = new ArrayList();
        }

        return l;
    }

    public List<Response> findResponseForCat(String ref, String source, String cat) {

        List<Response> l = new ArrayList();

        try {
            em.flush();

            Query listQry = em.createQuery("SELECT r FROM Response r WHERE r.refId = :ref AND r.alertSource = :source AND r.status = :status AND r.resType = :type AND r.resCause = :cat")
                    .setParameter("ref", ref)
                    .setParameter("status", 1)
                    .setParameter("source", source)
                    .setParameter("type", "cat")
                    .setParameter("cat", cat);
            // Enable forced database query
            listQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            l = listQry.getResultList();

        } catch (Exception e) {

            l = new ArrayList();
        }

        return l;
    }
}
