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
import org.alertflex.entity.AgentOpenscap;

/**
 *
 * @author root
 */
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
            ao =  (AgentOpenscap) cQry.getSingleResult();
        
        } catch (Exception e) {
            ao = null;
        }
        
        return ao;
       
    }
}
