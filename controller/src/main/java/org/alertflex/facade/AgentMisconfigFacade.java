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
import org.alertflex.entity.AgentMisconfig;
import org.alertflex.reports.Finding;

@Stateless
public class AgentMisconfigFacade extends AbstractFacade<AgentMisconfig> {

    @PersistenceContext(unitName = "alertflex_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AgentMisconfigFacade() {
        super(AgentMisconfig.class);
    }

    public AgentMisconfig findMisconfigurations(String ref, String node, String agent, int id, String policy) {

        AgentMisconfig am;

        try {
            em.flush();

            Query qry = em.createQuery(
                    "SELECT a FROM AgentMisconfig a WHERE a.refId = :ref AND a.node = :node AND a.agent = :agent AND a.scaId = :id AND a.policyId = :policy")
                    .setParameter("ref", ref)
                    .setParameter("node", node)
                    .setParameter("agent", agent)
                    .setParameter("id", id)
                    .setParameter("policy", policy);
            qry.setMaxResults(1);
            // Enable forced database query
            qry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            am = (AgentMisconfig) qry.getSingleResult();

        } catch (Exception e) {
            am = null;
        }

        return am;
    }
    
    public List<Object[]> getFindings(String ref) {

        List<Object[]> f = null;

        try {
            em.flush();

            Query qry = em.createQuery(
                    "SELECT a.agent, COUNT(a) FROM AgentMisconfig a WHERE a.refId = :ref GROUP BY a.agent", Finding.class)
                    .setParameter("ref", ref);
            // Enable forced database query
            qry.setMaxResults(10);
            qry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            
            f = (List<Object[]>) qry.getResultList();

        } catch (Exception e) {
            f = null;
        }

        return f;
    }
}
