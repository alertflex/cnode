/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alertflex.facade;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.CacheRetrieveMode;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.alertflex.entity.NodeMonitor;

/**
 *
 * @author root
 */
@Stateless
public class NodeMonitorFacade extends AbstractFacade<NodeMonitor> {
    @PersistenceContext(unitName = "afevents_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public NodeMonitorFacade() {
        super(NodeMonitor.class);
    }
    
     public int delOldStat(String ref, Timestamp timerange) {
        
        int deletedCount = 0;
        
        try {
            em.flush();
            Query qry = em.createQuery("DELETE FROM NodeMonitor n  WHERE n.refId = :ref AND n.timeOfSurvey < :timerange")
                .setParameter("ref", ref)
                .setParameter("timerange", timerange);
            
            // Enable forced database query
            qry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            deletedCount =  qry.executeUpdate();
        } catch (Exception e) {

        }
        
        return deletedCount;
    }
     
    public NodeMonitor getLastRecord(String r, String n) {
        
        NodeMonitor nm = null;
        
        try {
            em.flush();
            
            Query qry = em.createQuery(
                "SELECT n FROM NodeMonitor n WHERE n.refId = :ref AND n.nodeId = :node ORDER BY n.timeOfSurvey DESC")
                    .setParameter("ref", r).setParameter("node", n).setMaxResults(1);
                            
        // Enable forced database query
            qry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            nm =  (NodeMonitor) qry.getSingleResult();
            
            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully Authenticated", ""));
        } catch (Exception e) {

            return null;
        }
        
        return nm;
       
    }
}
