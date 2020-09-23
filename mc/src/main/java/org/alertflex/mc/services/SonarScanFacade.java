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
import org.alertflex.mc.db.SonarScan;

/**
 *
 * @author root
 */
@Stateless
public class SonarScanFacade extends AbstractFacade<SonarScan> {

    @PersistenceContext(unitName = "alertflex_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SonarScanFacade() {
        super(SonarScan.class);
    }
    
    public List<String> findProjects(String ref) {
        
        List<String> lp = null;
        
        try {
            em.flush();
            
            Query vQry = em.createQuery(
                    "SELECT s.projectId FROM SonarScan s WHERE s.refId = :ref GROUP BY s.projectId")
                    .setParameter("ref", ref);
                    
            vQry.setMaxResults(1);        
        // Enable forced database query
            vQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            lp =  vQry.getResultList();
        
        } catch (Exception e) {
            lp = null;
        }
        
        return lp;
       
    }
    
    public List<SonarScan> findRecords(String ref, String project) {
        
        List<SonarScan> lss = null;
        
        try {
            em.flush();
            
            Query vQry = em.createQuery(
                    "SELECT s FROM SonarScan s WHERE s.refId = :ref AND s.projectId = :p")
                    .setParameter("ref", ref)
                    .setParameter("p", project);
                    
            vQry.setMaxResults(1);        
        // Enable forced database query
            vQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            lss =  vQry.getResultList();
        
        } catch (Exception e) {
            lss = null;
        }
        
        return lss;
       
    }
    
    public SonarScan findVulnerability(String ref, String project, String key) {
        
        SonarScan ss;
        
        try {
            em.flush();
            
            Query vQry = em.createQuery(
                    "SELECT s FROM SonarScan s WHERE s.refId = :ref AND s.projectId = :p AND s.issueKey = :k")
                    .setParameter("ref", ref)
                    .setParameter("p", project)
                    .setParameter("k", key);
            vQry.setMaxResults(1);        
        // Enable forced database query
            vQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            ss =  (SonarScan) vQry.getSingleResult();
        
        } catch (Exception e) {
            ss = null;
        }
        
        return ss;
       
    }
    
}
