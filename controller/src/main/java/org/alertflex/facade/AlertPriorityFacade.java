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
import org.alertflex.entity.AlertPriority;

@Stateless
public class AlertPriorityFacade extends AbstractFacade<AlertPriority> {

    @PersistenceContext(unitName = "alertflex_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AlertPriorityFacade() {
        super(AlertPriority.class);
    }

    public List<String> findSourcesNameByRef(String r) {

        List<String> las = null;

        try {
            em.flush();

            Query alertsCatListQry = em.createQuery("SELECT a.source FROM AlertPriority a WHERE a.refId = :ref")
                    .setParameter("ref", r);
            // Enable forced database query
            alertsCatListQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            las = alertsCatListQry.getResultList();

        } catch (Exception e) {
            return null;
        }

        return las;
    }

    public AlertPriority findPriorityBySource(String ref, String source) {

        AlertPriority ap = null;

        try {
            em.flush();

            Query alertsServerityQry = em.createQuery("SELECT a FROM AlertPriority a WHERE a.refId = :ref AND a.source = :source")
                    .setParameter("source", source)
                    .setParameter("ref", ref);
            // Enable forced database query
            alertsServerityQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            ap = (AlertPriority) alertsServerityQry.getSingleResult();

        } catch (Exception e) {

            return null;
        }

        return ap;
    }

}
