/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alertflex.mc.services;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.CacheRetrieveMode;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.alertflex.mc.db.SandboxTask;

/**
 *
 * @author root
 */
@Stateless
public class SandboxTaskFacade extends AbstractFacade<SandboxTask> {

    @PersistenceContext(unitName = "alertflex_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SandboxTaskFacade() {
        super(SandboxTask.class);
    }
    
    public List<SandboxTask> findTaskByType(String ref, String type, Date start, Date end) {
        
        List<SandboxTask> l = null;
        
        try {
            em.flush();
            
            Query listQry = em.createQuery(
                "SELECT s FROM SandboxTask s WHERE s.refId = :ref AND s.sandboxType = :type AND  BETWEEN :start AND :end")
                    .setParameter("ref", ref)
                    .setParameter("type", type)
                    .setParameter("start", start)
                    .setParameter("end", end);
                            
        // Enable forced database query
            listQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            l =  (List<SandboxTask>) listQry.getResultList();
            
        } catch (Exception e) {

        }
        
        return l;
    }
    
    public List<SandboxTask> findTaskByDate(String ref, Date start, Date end) {
        
        List<SandboxTask> l = null;
        
        try {
            em.flush();
            
            Query listQry = em.createQuery(
                "SELECT s FROM SandboxTask s WHERE s.refId = :ref AND s.timeOfCreation BETWEEN :start AND :end")
                    .setParameter("ref", ref)
                    .setParameter("start", start)
                    .setParameter("end", end);
                            
        // Enable forced database query
            listQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            l =  (List<SandboxTask>) listQry.getResultList();
            
        } catch (Exception e) {

        }
        
        return l;
    }
    
    public int delOldTaskes(String ref, Timestamp timerange) {
        
        int deletedCount = 0;
        
        try {
            em.flush();
            Query qry = em.createQuery("DELETE FROM SandboxTask s WHERE s.refId = :ref AND s.status = :status AND s.timeOfCreation < :timerange")
                .setParameter("ref", ref).setParameter("status", "reported").setParameter("timerange", timerange);
            // Enable forced database query
            qry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            deletedCount =  qry.executeUpdate();
        } catch (Exception e) {

        }
        
        return deletedCount;
    }
    
    public List<SandboxTask> findActiveJobs(String ref) {
        
        List<SandboxTask> listTask = null;
        
        try {
            em.flush();
            
            Query listNodesQry = em.createQuery("SELECT s FROM SandboxTask s WHERE s.refId = :ref AND s.status = :status")
                .setParameter("ref", ref).setParameter("status", "progress");
                           
            
            // Enable forced database query
            listNodesQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            listTask =  listNodesQry.getResultList();

            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully Authenticated", ""));
        } catch (Exception e) {

            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid query to DB", ""));
            return null;
        }
        
        return listTask;
    }
    
}
