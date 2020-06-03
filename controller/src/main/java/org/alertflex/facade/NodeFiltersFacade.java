/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alertflex.facade;

import java.sql.Timestamp;
import javax.ejb.Stateless;
import javax.persistence.CacheRetrieveMode;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.alertflex.entity.NodeFilters;

/**
 *
 * @author root
 */
@Stateless
public class NodeFiltersFacade extends AbstractFacade<NodeFilters> {
    @PersistenceContext(unitName = "afevents_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public NodeFiltersFacade() {
        super(NodeFilters.class);
    }
    
    public int delOldStat(String ref, Timestamp timerange) {
        
        int deletedCount = 0;
        
        try {
            em.flush();
            Query qry = em.createQuery("DELETE FROM NodeFilters n  WHERE n.refId = :ref AND n.timeOfSurvey < :timerange")
                .setParameter("ref", ref)
                .setParameter("timerange", timerange);
            
            // Enable forced database query
            qry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            deletedCount =  qry.executeUpdate();
        } catch (Exception e) {

        }
        
        return deletedCount;
    }
    
        
    public NodeFilters getLastRecord(String r, String n) {
        
        NodeFilters nf = null;
        
        try {
            em.flush();
            
            Query qry = em.createQuery(
                "SELECT n FROM NodeFilters n WHERE n.refId = :ref AND n.nodeId = :node ORDER BY n.timeOfSurvey DESC")
                    .setParameter("ref", r).setParameter("node", n).setMaxResults(1);
                            
        // Enable forced database query
            qry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            nf =  (NodeFilters) qry.getSingleResult();
            
        } catch (Exception e) {

            return null;
        }
        
        return nf;
       
    }
   
}
