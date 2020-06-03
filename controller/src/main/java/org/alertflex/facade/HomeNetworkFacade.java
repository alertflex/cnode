/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alertflex.facade;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.CacheRetrieveMode;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.alertflex.entity.HomeNetwork;

/**
 *
 * @author root
 */
@Stateless
public class HomeNetworkFacade extends AbstractFacade<HomeNetwork> {
    @PersistenceContext(unitName = "alertflex_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public HomeNetworkFacade() {
        super(HomeNetwork.class);
    }
    
    public List<HomeNetwork> findByRef(String ref) {
        
        List<HomeNetwork> hn = null;
        
        try {
            em.flush();
            
            Query listQry = em.createQuery(
                "SELECT h FROM HomeNetwork h WHERE h.refId = :ref").setParameter("ref", ref);
        
            // Enable forced database query
            listQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            hn =  (List<HomeNetwork>) listQry.getResultList();
            
            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully Authenticated", ""));
        } catch (Exception e) {

            
        }
        
        return hn;
       
    }
    
}
