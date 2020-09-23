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
import org.alertflex.mc.db.SandboxJob;

/**
 *
 * @author root
 */
@Stateless
public class SandboxJobFacade extends AbstractFacade<SandboxJob> {

    @PersistenceContext(unitName = "alertflex_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SandboxJobFacade() {
        super(SandboxJob.class);
    }
    
    public SandboxJob findJobByHostName(String ref, String name) {
        
        SandboxJob s = null;
        
        try {
            em.flush();
            
            Query listQry = em.createQuery(
                "SELECT s FROM SandboxJob s WHERE s.refId = :ref AND s.hostName = :name").setParameter("ref", ref).setParameter("name", name);
        
            // Enable forced database query
            listQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            s =  (SandboxJob) listQry.getSingleResult();
            
            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully Authenticated", ""));
        } catch (Exception e) {

            
        }
        
        return s;
       
    }
    
    public SandboxJob findJobByName(String ref, String name) {
        
        SandboxJob s = null;
        
        try {
            em.flush();
            
            Query listQry = em.createQuery(
                "SELECT s FROM SandboxJob s WHERE s.refId = :ref AND s.name = :name").setParameter("ref", ref).setParameter("name", name);
        
            // Enable forced database query
            listQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            s =  (SandboxJob) listQry.getSingleResult();
            
            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully Authenticated", ""));
        } catch (Exception e) {

            
        }
        
        return s;
       
    }
    
    public List<String> findJobNamesByRef(String r) {

        List<String> listTasks = null;

        try {
            em.flush();
      
            Query listQry = em.createQuery("SELECT s.name FROM SandboxJob s WHERE s.refId = :ref").setParameter("ref", r);
            
            
        // Enable forced database query
            listQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            listTasks =  listQry.getResultList();

            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully Authenticated", ""));
        } catch (Exception e) {

            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid query to DB", ""));
        }
        
        return listTasks;
    }
    
    public List<SandboxJob> findJobsByRef(String r) {

        List<SandboxJob> listTasks = null;

        try {
            em.flush();
      
            Query listQry = em.createQuery("SELECT s FROM SandboxJob s WHERE s.refId = :ref").setParameter("ref", r);
            
            
        // Enable forced database query
            listQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            listTasks =  listQry.getResultList();

            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully Authenticated", ""));
        } catch (Exception e) {

            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid query to DB", ""));
        }
        
        return listTasks;
    }
    
    public List<SandboxJob> findJobsByPlaybook(String r, String p) {

        List<SandboxJob> listJobs = null;

        try {
            em.flush();
      
            Query listQry = em.createQuery("SELECT s FROM SandboxJob s WHERE s.refId = :ref AND s.playbook = :playbook")
                    .setParameter("ref", r)
                    .setParameter("playbook", p);
            
            
        // Enable forced database query
            listQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            listJobs =  listQry.getResultList();

            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully Authenticated", ""));
        } catch (Exception e) {

            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid query to DB", ""));
        }
        
        return listJobs;
    }
    
}
