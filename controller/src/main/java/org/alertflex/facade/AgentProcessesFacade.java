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

import javax.ejb.Stateless;
import javax.persistence.CacheRetrieveMode;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.alertflex.entity.AgentProcesses;

@Stateless
public class AgentProcessesFacade extends AbstractFacade<AgentProcesses> {

    @PersistenceContext(unitName = "alertflex_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AgentProcessesFacade() {
        super(AgentProcesses.class);
    }

    public AgentProcesses findProcess(String ref, String node, String agent, String name, String pid) {

        AgentProcesses ap;

        try {
            em.flush();

            Query qry = em.createQuery(
                    "SELECT a FROM AgentProcesses a WHERE a.refId = :ref AND a.nodeId = :node AND a.agent = :agent AND a.name = :name AND a.pid = :pid")
                    .setParameter("ref", ref)
                    .setParameter("node", node)
                    .setParameter("agent", agent)
                    .setParameter("name", name)
                    .setParameter("pid", pid);
            qry.setMaxResults(1);
            // Enable forced database query
            qry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            ap = (AgentProcesses) qry.getSingleResult();

        } catch (Exception e) {
            ap = null;
        }

        return ap;
    }

}
