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
import org.alertflex.mc.db.ZapScan;

/**
 *
 * @author root
 */
@Stateless
public class ZapScanFacade extends AbstractFacade<ZapScan> {

    @PersistenceContext(unitName = "alertflex_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ZapScanFacade() {
        super(ZapScan.class);
    }
    
    public ZapScan findRecord(String ref, String url, int plugin, int cwe, int source, int wasc) {
        
        ZapScan zs;
        
        try {
            em.flush();
            
            Query vQry = em.createQuery(
                    "SELECT z FROM ZapScan z WHERE z.refId = :ref AND z.url = :url AND z.pluginid = :p AND z.cweid = :c AND z.sourceid = :s AND z.wascid = :w")
                    .setParameter("ref", ref)
                    .setParameter("url", url)
                    .setParameter("p", plugin)
                    .setParameter("c", cwe)
                    .setParameter("s", source)
                    .setParameter("w", wasc);
            vQry.setMaxResults(1);        
        // Enable forced database query
            vQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            zs =  (ZapScan) vQry.getSingleResult();
        
        } catch (Exception e) {
            zs = null;
        }
        
        return zs;
       
    }
    
    public List<ZapScan> findRecordsByUrl(String ref, String url) {
        
        List<ZapScan> zsList = null;
        
        try {
            em.flush();
            
            Query listNodesQry = em.createQuery("SELECT z FROM ZapScan z WHERE z.refId = :ref AND z.url = :url")
                    .setParameter("ref", ref)
                    .setParameter("url", url);
            
            
        // Enable forced database query
            listNodesQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            zsList =  listNodesQry.getResultList();

            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully Authenticated", ""));
        } catch (Exception e) {

            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid query to DB", ""));
            return null;
        }
        
        return zsList;
    }
}
