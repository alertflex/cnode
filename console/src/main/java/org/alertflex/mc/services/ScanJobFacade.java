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
import org.alertflex.mc.db.ScanJob;

/**
 *
 * @author root
 */
@Stateless
public class ScanJobFacade extends AbstractFacade<ScanJob> {

    @PersistenceContext(unitName = "alertflex_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ScanJobFacade() {
        super(ScanJob.class);
    }
    
    public List<ScanJob> findJobs(String r) {

        List<ScanJob> listJobs = null;

        try {
            em.flush();
      
            Query listQry = em.createQuery("SELECT s FROM ScanJob s WHERE s.refId = :ref")
                    .setParameter("ref", r);
            
            
        // Enable forced database query
            listQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            listJobs =  listQry.getResultList();

        } catch (Exception e) {

        }
        
        return listJobs;
    }
    
    public List<String> findJobNames(String r) {

        List<String> listJobs = null;

        try {
            em.flush();
      
            Query listQry = em.createQuery("SELECT s.name FROM ScanJob s WHERE s.refId = :ref")
                    .setParameter("ref", r);
            
            
        // Enable forced database query
            listQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            listJobs =  listQry.getResultList();

        } catch (Exception e) {

        }
        
        return listJobs;
    }
    
    public ScanJob findJobByTypeValue(String ref, String type, String value) {
        
        ScanJob sj = null;
        
        try {
            em.flush();
            
            Query listQry = em.createQuery("SELECT s FROM ScanJob s WHERE s.refId = :ref AND s.scanType = :type AND s.value = :value")
                .setParameter("ref", ref)
                .setParameter("type", type)
                .setParameter("value", value);
        
            // Enable forced database query
            listQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            sj =  (ScanJob) listQry.getSingleResult();
            
        } catch (Exception e) {

            
        }
        
        return sj;
       
    }
    
    public ScanJob findJobByName(String ref, String name) {
        
        ScanJob sj = null;
        
        try {
            em.flush();
            
            Query listQry = em.createQuery(
                "SELECT s FROM ScanJob s WHERE s.refId = :ref AND s.name = :name").setParameter("ref", ref).setParameter("name", name);
        
            listQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            sj =  (ScanJob) listQry.getSingleResult();
            
        } catch (Exception e) {

            
        }
        
        return sj;
       
    }
}
