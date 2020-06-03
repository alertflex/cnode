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
import org.alertflex.entity.AgentPackages;

/**
 *
 * @author root
 */
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
            ap =  (AgentPackages) qry.getSingleResult();
        
        } catch (Exception e) {
            ap = null;
        }
        
        return ap;
    }
}
