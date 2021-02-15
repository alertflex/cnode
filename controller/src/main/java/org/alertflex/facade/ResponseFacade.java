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

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.CacheRetrieveMode;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.alertflex.entity.Response;

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
