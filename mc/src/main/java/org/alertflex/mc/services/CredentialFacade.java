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
import org.alertflex.mc.db.Credential;

/**
 *
 * @author root
 */
@Stateless
public class CredentialFacade extends AbstractFacade<Credential> {

    @PersistenceContext(unitName = "alertflex_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CredentialFacade() {
        super(Credential.class);
    }
    
    
    public Credential findCredential(String r, String n) {
        
        Credential c = null;
        
        try {
            em.flush();
            
            Query credentialQry = em.createQuery("SELECT c FROM Credential c WHERE c.refId = :ref AND c.name = :name").setParameter("ref", r).setParameter("name", n);
            
            
        // Enable forced database query
            credentialQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            c = (Credential) credentialQry.getSingleResult();

            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully Authenticated", ""));
        } catch (Exception e) {

            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid query to DB", ""));
        }
        
        return c;
    }
    
    public List<Credential> findCredentialByRef(String r) {
        
        List<Credential> listCredential = null;
        
        try {
            em.flush();
            
            Query listCredentialQry = em.createQuery("SELECT c FROM Credential c WHERE c.refId = :ref").setParameter("ref", r);
            
            
        // Enable forced database query
            listCredentialQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            listCredential =  listCredentialQry.getResultList();

            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully Authenticated", ""));
        } catch (Exception e) {

            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid query to DB", ""));
        }
        
        return listCredential;
    }
    
    public List<String> findCredentialNamesByRef(String r) {
        
        List<String> listCredentialName = null;
        
        try {
            em.flush();
            
            Query listCredentialQry = em.createQuery("SELECT c.name FROM Credential c WHERE c.refId = :ref").setParameter("ref", r);
            
            
        // Enable forced database query
            listCredentialQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            listCredentialName =  listCredentialQry.getResultList();

            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully Authenticated", ""));
        } catch (Exception e) {

            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid query to DB", ""));
        }
        
        return listCredentialName;
    }
    
    
}
