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
import org.alertflex.entity.AgentsGroup;

@Stateless
public class AgentsGroupFacade extends AbstractFacade<AgentsGroup> {

    @PersistenceContext(unitName = "alertflex_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AgentsGroupFacade() {
        super(AgentsGroup.class);
    }
    
    public List<AgentsGroup> findAgentsByNode(String ref, String node) {

        List<AgentsGroup> agl = null;

        try {
            em.flush();

            Query agentsProcessQry = em.createQuery(
                    "SELECT a FROM AgentsGroup a WHERE a.refId = :ref AND a.node = :node")
                    .setParameter("ref", ref).setParameter("node", node);
            agentsProcessQry.setMaxResults(1);
            // Enable forced database query
            agentsProcessQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);

            agl = (List<AgentsGroup>) agentsProcessQry.getResultList();

        } catch (Exception e) {
            agl = null; 
        }

        return agl;
    }

    public AgentsGroup findAgentsGroupByName(String ref, String node, String name) {

        AgentsGroup ag = null;

        try {
            em.flush();

            Query agentsProcessQry = em.createQuery(
                    "SELECT a FROM AgentsGroup a WHERE a.refId = :ref AND a.node = :node AND a.groupName = :name")
                    .setParameter("ref", ref).setParameter("node", node).setParameter("name", name);
            agentsProcessQry.setMaxResults(1);
            // Enable forced database query
            agentsProcessQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);

            ag = (AgentsGroup) agentsProcessQry.getSingleResult();

        } catch (Exception e) {

        }

        return ag;
    }
}
