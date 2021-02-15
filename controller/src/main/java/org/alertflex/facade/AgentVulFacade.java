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
import org.alertflex.entity.AgentVul;

@Stateless
public class AgentVulFacade extends AbstractFacade<AgentVul> {

    @PersistenceContext(unitName = "alertflex_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AgentVulFacade() {
        super(AgentVul.class);
    }

    public AgentVul findVulnerability(String ref, String node, String agent, String cve, String pkgs) {

        AgentVul v;

        try {
            em.flush();

            Query vQry = em.createQuery(
                    "SELECT a FROM AgentVul a WHERE .refId = :ref AND a.nodeId = :node AND a.agent = :agent AND a.cve = :cve AND a.packageName = :pkgs")
                    .setParameter("ref", ref)
                    .setParameter("node", node)
                    .setParameter("agent", agent)
                    .setParameter("cve", cve)
                    .setParameter("pkgs", pkgs);
            vQry.setMaxResults(1);
            // Enable forced database query
            vQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            v = (AgentVul) vQry.getSingleResult();

        } catch (Exception e) {
            v = null;
        }

        return v;

    }

}
