/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alertflex.facade;

import java.sql.Timestamp;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.CacheRetrieveMode;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.alertflex.entity.NetStat;

/**
 *
 * @author root
 */
@Stateless
public class NetStatFacade extends AbstractFacade<NetStat> {
    @PersistenceContext(unitName = "afevents_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public NetStatFacade() {
        super(NetStat.class);
    }
    
    public int delOldStat(String ref, Timestamp timerange) {
        
        int deletedCount = 0;
        
        try {
            em.flush();
            Query qry = em.createQuery("DELETE FROM NetStat n  WHERE n.refId = :ref AND n.timeOfSurvey < :timerange")
                .setParameter("ref", ref)
                .setParameter("timerange", timerange);
            
            // Enable forced database query
            qry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            deletedCount =  qry.executeUpdate();
        } catch (Exception e) {

        }
        
        return deletedCount;
    }
    
    public NetStat getLastRecord(String r, String n, String i) {
        
        NetStat ns = null;
        
        try {
            em.flush();
            
            Query qry = em.createQuery(
                "SELECT n FROM NetStat n WHERE n.refId = :ref AND n.nodeId = :node AND n.ids = :ids ORDER BY n.timeOfSurvey DESC")
                    .setParameter("ref", r).setParameter("node", n).setParameter("ids", i).setMaxResults(1);
                            
        // Enable forced database query
            qry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            ns =  (NetStat) qry.getSingleResult();
            
        } catch (Exception e) {

            return null;
        }
        
        return ns;
    }
}
