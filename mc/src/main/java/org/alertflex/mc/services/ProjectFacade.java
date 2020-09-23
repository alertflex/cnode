/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alertflex.mc.services;

import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.persistence.CacheRetrieveMode;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.alertflex.mc.db.Project;

/**
 *
 * @author root
 */
@Stateless
public class ProjectFacade extends AbstractFacade<Project> {
    @PersistenceContext(unitName = "alertflex_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ProjectFacade() {
        super(Project.class);
    }
    
    public Project findProjectByName(String n) {
        
        Project p = null;
        
        try {
            em.flush();
            
            Query projectQry = em.createQuery(
                    "SELECT p FROM Project p WHERE p.name = :name").setParameter("name", n);
        // Enable forced database query
            projectQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            p =  (Project) projectQry.getSingleResult();

            
        } catch (Exception e) {

        }
        
        return p;
    }
    
    public Project findProjectById(String r) {
        
        Project p = null;
        
        try {
            em.flush();
            
            Query projectQry = em.createQuery(
                    "SELECT p FROM Project p WHERE p.refId = :refId").setParameter("refId", r);
        // Enable forced database query
            projectQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            p =  (Project) projectQry.getSingleResult();

            
        } catch (Exception e) {

            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid query to DB", ""));
        }
        
        return p;
       
    }
    
}
