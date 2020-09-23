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
import org.alertflex.mc.db.NmapScan;

/**
 *
 * @author root
 */
@Stateless
public class NmapScanFacade extends AbstractFacade<NmapScan> {

    @PersistenceContext(unitName = "alertflex_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public NmapScanFacade() {
        super(NmapScan.class);
    }
    
    public NmapScan findRecord(String ref, String host, int port, String state) {
        
        NmapScan ns;
        
        try {
            em.flush();
            
            Query vQry = em.createQuery(
                    "SELECT n FROM NmapScan n WHERE n.refId = :ref AND n.host = :host AND n.portId = :p AND n.state = :s")
                    .setParameter("ref", ref)
                    .setParameter("host", host)
                    .setParameter("p", port)
                    .setParameter("s", state);
            vQry.setMaxResults(1);        
        // Enable forced database query
            vQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            ns =  (NmapScan) vQry.getSingleResult();
        
        } catch (Exception e) {
            ns = null;
        }
        
        return ns;
    }
    
    public List<NmapScan> findRecordsByHost(String ref, String host) {
        
        List<NmapScan> nsList = null;
        
        try {
            em.flush();
            
            Query listNodesQry = em.createQuery("SELECT n FROM NmapScan n WHERE n.refId = :ref AND n.host = :host")
                    .setParameter("ref", ref)
                    .setParameter("host", host);
            
            
        // Enable forced database query
            listNodesQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            nsList =  listNodesQry.getResultList();

            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully Authenticated", ""));
        } catch (Exception e) {

            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid query to DB", ""));
            return null;
        }
        
        return nsList;
    }
    
}
