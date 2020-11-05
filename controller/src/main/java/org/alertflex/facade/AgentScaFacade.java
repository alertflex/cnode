/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alertflex.facade;

import javax.ejb.Stateless;
import javax.persistence.CacheRetrieveMode;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.alertflex.entity.AgentSca;

/**
 *
 * @author root
 */
@Stateless
public class AgentScaFacade extends AbstractFacade<AgentSca> {

    @PersistenceContext(unitName = "alertflex_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AgentScaFacade() {
        super(AgentSca.class);
    }

    public AgentSca findSca(String ref, String node, String agent, String name, String policy) {

        AgentSca as;

        try {
            em.flush();

            Query qry = em.createQuery(
                    "SELECT a FROM AgentSca a WHERE a.refId = :ref AND a.nodeId = :node AND a.agent = :agent AND a.name = :name AND a.policyId = :policy")
                    .setParameter("ref", ref)
                    .setParameter("node", node)
                    .setParameter("agent", agent)
                    .setParameter("name", name)
                    .setParameter("policy", policy);
            qry.setMaxResults(1);
            // Enable forced database query
            qry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            as = (AgentSca) qry.getSingleResult();

        } catch (Exception e) {
            as = null;
        }

        return as;
    }
}
