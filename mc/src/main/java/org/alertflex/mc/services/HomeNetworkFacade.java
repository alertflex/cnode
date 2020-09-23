/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alertflex.mc.services;

import java.util.List;
import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.persistence.CacheRetrieveMode;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.alertflex.mc.db.HomeNetwork;

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
    
    public List<String> findAllNetworksByRef(String r) {
        
        List<String> listHnet = null;
        
        try {
            em.flush();
            
            Query listNodesQry = em.createQuery("SELECT h.network FROM HomeNetwork h WHERE h.refId = :ref").setParameter("ref", r);
            
            
        // Enable forced database query
            listNodesQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            listHnet =  listNodesQry.getResultList();

            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully Authenticated", ""));
        } catch (Exception e) {

            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid query to DB", ""));
        }
        
        return listHnet;
    }
    
    public HomeNetwork findByNetwork(String r, String n) {
        HomeNetwork hnet = null;
        
        try {
            em.flush();
            
            Query listProbesQry = em.createQuery("SELECT h FROM HomeNetwork h WHERE h.refId = :ref AND h.network = :network").setParameter("ref", r).setParameter("network", n);
            
            
        // Enable forced database query
            listProbesQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            hnet =  (HomeNetwork) listProbesQry.getSingleResult();

            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully Authenticated", ""));
        } catch (Exception e) {
            return null;
        }
        
        return hnet;
    }
    
    public List<HomeNetwork> findNetworksByNode(String r, String n) {
        
        List<HomeNetwork> listNetworks = null;
        
        try {
            em.flush();
            
            Query listNetworksQry = em.createQuery("SELECT h FROM HomeNetwork h WHERE h.refId = :ref AND h.nodeId = :node").setParameter("ref", r).setParameter("node", n);
            
            
            // Enable forced database query
            listNetworksQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            listNetworks =  listNetworksQry.getResultList();

            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully Authenticated", ""));
        } catch (Exception e) {

            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid query to DB", ""));
        }
        
        return listNetworks;
    }
    
}
