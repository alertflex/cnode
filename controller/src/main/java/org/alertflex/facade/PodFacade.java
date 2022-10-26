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
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.alertflex.entity.Pod;

@Stateless
public class PodFacade extends AbstractFacade<Pod> {

    @PersistenceContext(unitName = "alertflex_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PodFacade() {
        super(Pod.class);
    }

    public List<Pod> findByNode(String ref, String node) {

        List<Pod> lp = null;

        try {
            em.flush();

            Query listQry = em.createQuery(
                "SELECT p FROM Pod p WHERE p.nodeProbe = :node AND p.refId = :ref")
                    .setParameter("ref", ref).setParameter("node", node);

            // Enable forced database query
            listQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            lp = listQry.getResultList();

        } catch (Exception e) {
            lp = null;
        }

        return lp;
    }
    
    public Pod findByName(String ref, String node, String name) {

        Pod p = null;

        try {
            em.flush();

            Query listQry = em.createQuery(
                "SELECT p FROM Pod p WHERE p.nodeProbe = :node AND p.refId = :ref AND p.name = :name")
                    .setParameter("ref", ref).setParameter("node", node).setParameter("name", name);

            // Enable forced database query
            listQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            p = (Pod) listQry.getSingleResult();

        } catch (Exception e) {
            p = null;
        }

        return p;

    }
}
