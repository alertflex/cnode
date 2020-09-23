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
import org.alertflex.mc.db.SnykScan;

/**
 *
 * @author root
 */
@Stateless
public class SnykScanFacade extends AbstractFacade<SnykScan> {

    @PersistenceContext(unitName = "alertflex_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SnykScanFacade() {
        super(SnykScan.class);
    }
    
    public List<String> findProjects(String ref) {
        
        List<String> lp = null;
        
        try {
            em.flush();
            
            Query vQry = em.createQuery(
                "SELECT s.projectId FROM SnykScan s WHERE s.refId = :ref GROUP BY s.projectId")
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
    
    public List<SnykScan> findRecords(String ref, String project) {
        
        List<SnykScan> ssl;
        
        try {
            em.flush();
            
            Query qry = em.createQuery(
                "SELECT s FROM SnykScan s WHERE s.refId = :ref AND s.projectId = :project")
                .setParameter("ref", ref)
                .setParameter("project", project);
            
            // Enable forced database query
            qry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            ssl =  (List<SnykScan>) qry.getResultList();
            
        
        } catch (Exception e) {
            ssl = null;
        }
        
        return ssl;
    }
    
    public SnykScan findVulnerability(String ref, String project, String cve, String pkgs) {
        
        SnykScan ss;
        
        try {
            em.flush();
            
            Query vQry = em.createQuery("SELECT s FROM SnykScan s WHERE s.refId = :ref AND s.projectId = :project AND s.vulnerabilityId = :cve AND s.pkgName = :pkgs")
                .setParameter("ref", ref)
                .setParameter("project", project)
                .setParameter("cve", cve)
                .setParameter("pkgs", pkgs);
            vQry.setMaxResults(1); 
            // Enable forced database query
            vQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            ss =  (SnykScan) vQry.getSingleResult();
        
        } catch (Exception e) {
            ss = null;
        }
        
        return ss;
       
    }
    
}
