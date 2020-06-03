/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alertflex.mc.services;

import javax.ejb.Stateless;
import javax.persistence.CacheRetrieveMode;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.alertflex.mc.db.Users;

/**
 *
 * @author root
 */
@Stateless
public class UsersFacade extends AbstractFacade<Users> {

    @PersistenceContext(unitName = "alertflex_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UsersFacade() {
        super(Users.class);
    }
    
    public Users findUserById(String id) {
        
        Users user = null;
        
        try {
            em.flush();
                
            Query usersListQry = em.createQuery("SELECT u FROM Users u WHERE u.userid = :user").setParameter("user", id);
            
            
                // Enable forced database query
                usersListQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
                user =  (Users) usersListQry.getSingleResult();
            } catch (Exception e) {

            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid quety to DB", ""));
        }
        
        return user;
       
    }
    
}
