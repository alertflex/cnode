/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alertflex.facade;

import java.util.List;
import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.persistence.CacheRetrieveMode;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.alertflex.entity.CatProfile;

/**
 *
 * @author root
 */
@Stateless
public class CatProfileFacade extends AbstractFacade<CatProfile> {
    @PersistenceContext(unitName = "alertflex_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CatProfileFacade() {
        super(CatProfile.class);
    }
    
    public List<CatProfile> findProfileByRefId(String r) {
        
        List<CatProfile> catProfileList = null;
        
        try {
            em.flush();
                
            Query catProfileListQry = em.createQuery("SELECT p FROM CatProfile p WHERE p.refId = :ref").setParameter("ref", r);
            
            
            // Enable forced database query
            catProfileListQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            catProfileList =  catProfileListQry.getResultList();
        
        } catch (Exception e) {

            // FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid quety to DB", ""));
        }
        
        return catProfileList;
       
    }
    
    public List<CatProfile> findProfileBySource(String ref, String source) {
        
        List<CatProfile> catProfileList = null;
        
        try {
            em.flush();
                
            Query catProfileListQry = em.createQuery("SELECT p FROM CatProfile p WHERE p.refId = :ref AND p.cpSource = :source")
                .setParameter("ref", ref).setParameter("source", source);
            
            
            // Enable forced database query
            catProfileListQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            catProfileList =  catProfileListQry.getResultList();
        
        } catch (Exception e) {

            // FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid quety to DB", ""));
        }
        
        return catProfileList;
       
    }
    
    public CatProfile findProfileByProfilename(String r, String profile_name) {
        
        CatProfile profile = null;
        
        try {
            em.flush();
                
            Query catProfileListQry = em.createQuery("SELECT p FROM CatProfile p WHERE p.profileName = :profile_name AND p.refId = :ref")
                .setParameter("profile_name", profile_name).setParameter("ref", r);
            
            // Enable forced database query
            catProfileListQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
                profile = (CatProfile) catProfileListQry.getSingleResult();
        } catch (Exception e) {

            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid quety to DB", ""));
            profile = null;
        }
        
        return profile;
       
    }
}
