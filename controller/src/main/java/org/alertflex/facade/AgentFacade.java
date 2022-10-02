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
import org.alertflex.entity.Agent;

@Stateless
public class AgentFacade extends AbstractFacade<Agent> {

    @PersistenceContext(unitName = "alertflex_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AgentFacade() {
        super(Agent.class);
    }
    
    public List<Agent> findAgentsByNode(String ref, String node) {

        List<Agent> al = null;

        try {
            em.flush();

            Query agentProcessQry = em.createQuery(
                    "SELECT a FROM Agent a WHERE a.refId = :ref AND a.node = :node")
                    .setParameter("ref", ref).setParameter("node", node);
            agentProcessQry.setMaxResults(1);
            // Enable forced database query
            agentProcessQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);

            al = (List<Agent>) agentProcessQry.getResultList();

        } catch (Exception e) {
            al = null; 
        }

        return al;
    }

    public Agent findAgentByName(String ref, String node, String name) {

        Agent a = null;

        try {
            em.flush();

            Query agentProcessQry = em.createQuery(
                    "SELECT a FROM Agent a WHERE a.refId = :ref AND a.node = :node AND a.name = :name")
                    .setParameter("ref", ref).setParameter("node", node).setParameter("name", name);
            agentProcessQry.setMaxResults(1);
            // Enable forced database query
            agentProcessQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);

            a = (Agent) agentProcessQry.getSingleResult();

        } catch (Exception e) {

        }

        return a;
    }

    public Agent findAgentById(String ref, String node, String id) {

        Agent a = null;

        try {
            em.flush();

            Query agentProcessQry = em.createQuery(
                    "SELECT a FROM Agent a WHERE a.refId = :ref AND a.node = :node AND a.agentId = :id")
                    .setParameter("ref", ref).setParameter("node", node).setParameter("id", id);
            agentProcessQry.setMaxResults(1);
            // Enable forced database query
            agentProcessQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            a = (Agent) agentProcessQry.getSingleResult();

        } catch (Exception e) {

        }

        return a;
    }

    public String findAgentByIP(String ref, String node, String ip) {

        String a = null;

        try {
            em.flush();

            Query agentProcessQry = em.createQuery(
                    "SELECT a.name FROM Agent a WHERE a.refId = :ref AND a.node = :node AND a.ip = :ip").setParameter("ref", ref).setParameter("node", node).setParameter("ip", ip);
            agentProcessQry.setMaxResults(1);
            // Enable forced database query
            agentProcessQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            a = (String) agentProcessQry.getSingleResult();

        } catch (Exception e) {
            return "";
        }

        return a;
    }
}
