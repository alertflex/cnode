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
import org.alertflex.entity.TrivyScan;

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
