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
import org.alertflex.mc.db.ScriptJob;

/**
 *
 * @author root
 */
@Stateless
public class ScriptJobFacade extends AbstractFacade<ScriptJob> {

    @PersistenceContext(unitName = "alertflex_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ScriptJobFacade() {
        super(ScriptJob.class);
    }
    
    public List<ScriptJob> findJobByRef(String r) {

        List<ScriptJob> listJobs = null;

        try {
            em.flush();
      
            Query listQry = em.createQuery("SELECT s FROM ScriptJob s WHERE s.refId = :ref").setParameter("ref", r);
            
            
        // Enable forced database query
            listQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            listJobs =  listQry.getResultList();

        } catch (Exception e) {

        }
        
        return listJobs;
    }
    
    public List<String> findJobNamesByRef(String r) {

        List<String> listJobs = null;

        try {
            em.flush();
      
            Query listQry = em.createQuery("SELECT s.name FROM ScriptJob s WHERE s.refId = :ref").setParameter("ref", r);
            
            
        // Enable forced database query
            listQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            listJobs =  listQry.getResultList();

        } catch (Exception e) {

        }
        
        return listJobs;
    }
    
    public List<ScriptJob> findJobsByPlaybook(String r, String p) {

        List<ScriptJob> listJobs = null;

        try {
            em.flush();
      
            Query listQry = em.createQuery("SELECT s FROM ScriptJob s WHERE s.refId = :ref AND s.playbook = :playbook")
                    .setParameter("ref", r)
                    .setParameter("playbook", p);
            
            
        // Enable forced database query
            listQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            listJobs =  listQry.getResultList();

        } catch (Exception e) {

        }
        
        return listJobs;
    }
    
    public ScriptJob findJobByName(String ref, String name) {
        
        ScriptJob sj = null;
        
        try {
            em.flush();
            
            Query listQry = em.createQuery(
                "SELECT s FROM ScriptJob s WHERE s.refId = :ref AND s.name = :name").setParameter("ref", ref).setParameter("name", name);
        
            // Enable forced database query
            listQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            sj =  (ScriptJob) listQry.getSingleResult();
            
            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully Authenticated", ""));
        } catch (Exception e) {

            
        }
        
        return sj;
       
    }
    
    public ScriptJob findJobByPlaybookName(String ref, String playbook) {
        
        ScriptJob sj = null;
        
        try {
            em.flush();
            
            Query listQry = em.createQuery(
                "SELECT s FROM ScriptJob s WHERE s.refId = :ref AND s.playbook = :name").setParameter("ref", ref).setParameter("name", playbook);
        
            // Enable forced database query
            listQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            sj =  (ScriptJob) listQry.getSingleResult();
            
        } catch (Exception e) {

            
        }
        
        return sj;
       
    }
    
    public List<ScriptJob> findJobsByNode(String r, String n) {

        List<ScriptJob> listTasks = null;

        try {
            em.flush();
      
            Query listQry = em.createQuery("SELECT s FROM ScriptJob s WHERE s.refId = :ref AND s.node = :node")
                .setParameter("ref", r)
                .setParameter("node", n);
            
            
            // Enable forced database query
            listQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            listTasks =  listQry.getResultList();

        } catch (Exception e) {

        }
        
        return listTasks;
    }
    
    public ScriptJob findJobByHost(String ref, String host) {
        
        ScriptJob sj = null;
        
        try {
            em.flush();
            
            Query listQry = em.createQuery(
                "SELECT s FROM ScriptJob s WHERE s.refId = :ref AND s.hostname = :host")
                    .setParameter("ref", ref)
                    .setParameter("host", host);
        
            // Enable forced database query
            listQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            sj =  (ScriptJob) listQry.getSingleResult();
            
        } catch (Exception e) {

            
        }
        
        return sj;
       
    }
    
        
    public ScriptJob findJobBySensorName(String ref, String sensor) {
        
        ScriptJob sj = null;
        
        try {
            em.flush();
            
            Query listQry = em.createQuery(
                "SELECT s FROM ScriptJob s WHERE s.refId = :ref AND s.sensor = :name")
                    .setParameter("ref", ref)
                    .setParameter("name", sensor);
        
            // Enable forced database query
            listQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            sj =  (ScriptJob) listQry.getSingleResult();
            
        } catch (Exception e) {

            
        }
        
        return sj;
       
    }
    
}
