/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alertflex.facade;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.CacheRetrieveMode;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.alertflex.entity.AlertsCategory;

/**
 *
 * @author root
 */
@Stateless
public class AlertsCategoryFacade extends AbstractFacade<AlertsCategory> {

    @PersistenceContext(unitName = "alertflex_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AlertsCategoryFacade() {
        super(AlertsCategory.class);
    }
    
    public List<AlertsCategory> findCatsBySourceAndRef(String r, String source) {
        
        List<AlertsCategory> lac = null;
        
        try {
            em.flush();
            
            Query alertsCatListQry = em.createQuery("SELECT a FROM AlertsCategory a WHERE a.catSource = :source AND a.refId = :ref")
                .setParameter("source", source)
                .setParameter("ref", r);
        // Enable forced database query
            alertsCatListQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            lac =  alertsCatListQry.getResultList();

            
        } catch (Exception e) {

            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid query to DB", ""));
        }
        
        return lac;
    }
    
        
    public List<AlertsCategory> findCatsBySource(String r, String source) {
        
        List<AlertsCategory> lac = null;
        
        try {
            em.flush();
            
            Query alertsCatListQry = em.createQuery(
                    "SELECT a FROM AlertsCategory a WHERE a.catSource = :source AND (a.refId = :ref OR a.refId = :empty)")
                    .setParameter("source", source)
                    .setParameter("ref", r)
                    .setParameter("empty", "");
            // Enable forced database query
            alertsCatListQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            lac =  alertsCatListQry.getResultList();

            
        } catch (Exception e) {

            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid query to DB", ""));
        }
        
        return lac;
    }
    
    public List<String> findCatNames(String r, String source) {
        
        List<String> lcn = null;
        
        try {
            em.flush();
            
            Query alertsCatListQry = em.createQuery(
                    "SELECT a.catName FROM AlertsCategory a WHERE a.catSource = :source AND (a.refId = :ref OR a.refId = :empty)")
                    .setParameter("source", source)
                    .setParameter("ref", r)
                    .setParameter("empty", "");
            // Enable forced database query
            alertsCatListQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            lcn =  alertsCatListQry.getResultList();

            
        } catch (Exception e) {

            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid query to DB", ""));
        }
        
        return lcn;
    }
}
