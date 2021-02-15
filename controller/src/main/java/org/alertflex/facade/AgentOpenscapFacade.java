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
import org.alertflex.entity.AgentOpenscap;

@Stateless
public class AgentOpenscapFacade extends AbstractFacade<AgentOpenscap> {

    @PersistenceContext(unitName = "alertflex_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AgentOpenscapFacade() {
        super(AgentOpenscap.class);
    }

    public AgentOpenscap findOpenscap(String ref, String node, String agent, String profile, String check) {

        AgentOpenscap ao;

        try {
            em.flush();

            Query cQry = em.createQuery(
                    "SELECT a FROM AgentOpenscap a  WHERE a.refId = :ref AND a.nodeId = :node AND a.agent = :agent AND a.profileId = :profile AND a.checkId = :check")
                    .setParameter("ref", ref)
                    .setParameter("node", node)
                    .setParameter("agent", agent)
                    .setParameter("profile", profile)
                    .setParameter("check", check);
            cQry.setMaxResults(1);
            // Enable forced database query
            cQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            ao = (AgentOpenscap) cQry.getSingleResult();

        } catch (Exception e) {
            ao = null;
        }

        return ao;

    }
}
