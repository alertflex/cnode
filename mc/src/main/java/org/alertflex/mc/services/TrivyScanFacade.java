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
import org.alertflex.mc.db.TrivyScan;

/**
 *
 * @author root
 */
@Stateless
public class TrivyScanFacade extends AbstractFacade<TrivyScan> {

    @PersistenceContext(unitName = "alertflex_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TrivyScanFacade() {
        super(TrivyScan.class);
    }
    
    public List<TrivyScan> findRecords(String ref, String node, String sensor) {
        
        List<TrivyScan> tsl;
        
        try {
            em.flush();
            
            Query qry = em.createQuery(
                "SELECT t FROM TrivyScan t WHERE t.refId = :ref AND t.nodeId = :node AND t.sensor = :sensor")
                .setParameter("ref", ref)
                .setParameter("node", node)
                .setParameter("sensor", sensor);
            
            // Enable forced database query
            qry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            tsl =  (List<TrivyScan>) qry.getResultList();
            
        
        } catch (Exception e) {
            tsl = null;
        }
        
        return tsl;
    }
    
    public List<String> findSensorNamesByNode(String ref, String node) {
        
        List<String> snl;
        
        try {
            em.flush();
            
            Query qry = em.createQuery(
                "SELECT t.sensor FROM TrivyScan t WHERE t.refId = :ref AND t.nodeId = :node GROUP BY t.sensor")
                .setParameter("ref", ref)
                .setParameter("node", node);
            
            // Enable forced database query
            qry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            snl =  (List<String>) qry.getResultList();
            
        
        } catch (Exception e) {
            snl = null;
        }
        
        return snl;
    }
    
    public TrivyScan findVulnerability(String ref, String node, String sensor, String target, String cve, String pkgs) {
        
        TrivyScan ts;
        
        try {
            em.flush();
            
            Query vQry = em.createQuery("SELECT t FROM TrivyScan t WHERE t.refId = :ref AND t.nodeId = :node AND t.sensor = :sensor AND t.target = :target AND t.vulnerabilityId = :cve AND t.pkgName = :pkgs")
                .setParameter("ref", ref)
                .setParameter("node", node)
                .setParameter("sensor", sensor)
                .setParameter("target", target)
                .setParameter("cve", cve)
                .setParameter("pkgs", pkgs);
            vQry.setMaxResults(1); 
            // Enable forced database query
            vQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            ts =  (TrivyScan) vQry.getSingleResult();
        
        } catch (Exception e) {
            ts = null;
        }
        
        return ts;
       
    }
    
}
