/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alertflex.mc.services;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.CacheRetrieveMode;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.alertflex.mc.db.AgentSca;

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
    
    public List<AgentSca> findSca(String ref, String node, String agent) {
        
        List<AgentSca> l = null;
        
        try {
            em.flush();
            
            Query listQry = em.createQuery(
                "SELECT a FROM AgentSca a WHERE a.refId = :ref AND a.nodeId = :node AND a.agent = :agent").setParameter("ref", ref).setParameter("node", node).setParameter("agent", agent);
        
            // Enable forced database query
            listQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            l =  (List<AgentSca>) listQry.getResultList();
            
            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully Authenticated", ""));
        } catch (Exception e) {
            l = null;
        }
        
        return l;
       
    }
    
}
