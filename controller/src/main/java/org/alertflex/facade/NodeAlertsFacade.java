/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alertflex.facade;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.CacheRetrieveMode;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.alertflex.entity.NodeAlerts;

/**
 *
 * @author root
 */
@Stateless
public class NodeAlertsFacade extends AbstractFacade<NodeAlerts> {
    @PersistenceContext(unitName = "afevents_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public NodeAlertsFacade() {
        super(NodeAlerts.class);
    }
    
    public int delOldStat(String ref, Timestamp timerange) {
        
        int deletedCount = 0;
        
        try {
            em.flush();
            Query qry = em.createQuery("DELETE FROM NodeAlerts n  WHERE n.refId = :ref AND n.timeOfSurvey < :timerange")
                .setParameter("ref", ref)
                .setParameter("timerange", timerange);
            
            // Enable forced database query
            qry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            deletedCount =  qry.executeUpdate();
        } catch (Exception e) {

        }
        
        return deletedCount;
    }
    
    public NodeAlerts getLastRecord(String r, String n, Timestamp start, Timestamp end) {
        
        NodeAlerts na = null;
        
        try {
            
            em.flush();
            
            Query qry = em.createQuery(
                "SELECT n FROM NodeAlerts n WHERE n.refId = :ref AND n.nodeId = :node AND n.timeOfSurvey BETWEEN :start AND :end ORDER BY n.timeOfSurvey")
                    .setParameter("ref", r).setParameter("node", n).setParameter("start", start).setParameter("end", end);
                            
        // Enable forced database query
            qry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            List<NodeAlerts> nal =  qry.getResultList();
            
            if (nal != null && nal.size() > 0) na = nal.get(nal.size() - 1);
                        
        } catch (Exception e) {

            return null;
        }
        
        return na;
    }
}
