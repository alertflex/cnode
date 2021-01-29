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
import org.alertflex.entity.AgentPackages;

@Stateless
public class AgentPackagesFacade extends AbstractFacade<AgentPackages> {

    @PersistenceContext(unitName = "alertflex_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AgentPackagesFacade() {
        super(AgentPackages.class);
    }

    public AgentPackages findPackage(String ref, String node, String agent, String name, String version) {

        AgentPackages ap;

        try {
            em.flush();

            Query qry = em.createQuery(
                    "SELECT a FROM AgentPackages a WHERE a.refId = :ref AND a.nodeId = :node AND a.agent = :agent AND a.name = :name AND a.version = :version")
                    .setParameter("ref", ref)
                    .setParameter("node", node)
                    .setParameter("agent", agent)
                    .setParameter("name", name)
                    .setParameter("version", version);
            qry.setMaxResults(1);
            // Enable forced database query
            qry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            ap = (AgentPackages) qry.getSingleResult();

        } catch (Exception e) {
            ap = null;
        }

        return ap;
    }
}
