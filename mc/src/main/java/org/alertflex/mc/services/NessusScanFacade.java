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
import org.alertflex.mc.db.NessusScan;

/**
 *
 * @author root
 */
@Stateless
public class NessusScanFacade extends AbstractFacade<NessusScan> {

    @PersistenceContext(unitName = "alertflex_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public NessusScanFacade() {
        super(NessusScan.class);
    }
    
    public NessusScan findRecord(String ref, String scanName, int vulnIndex, String pluginName) {
        
        NessusScan ns;
        
        try {
            em.flush();
            
            Query vQry = em.createQuery(
                    "SELECT n FROM NessusScan n WHERE n.refId = :ref AND n.scanName = :sn AND n.pluginName = :pn AND n.vulnIndex = :vi")
                    .setParameter("ref", ref)
                    .setParameter("sn", scanName)
                    .setParameter("vi", vulnIndex)
                    .setParameter("pn", pluginName);
            
            vQry.setMaxResults(1);        
        // Enable forced database query
            vQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            ns =  (NessusScan) vQry.getSingleResult();
        
        } catch (Exception e) {
            ns = null;
        }
        
        return ns;
       
    }
    
    public List<NessusScan> findRecordsByScan(String ref, String scanName) {
        
        List<NessusScan> nsList = null;
        
        try {
            em.flush();
            
            Query listNodesQry = em.createQuery("SELECT n FROM NessusScan n WHERE n.refId = :ref AND n.scanName = :sn")
                    .setParameter("ref", ref)
                    .setParameter("usn", scanName);
            
            
        // Enable forced database query
            listNodesQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            nsList =  listNodesQry.getResultList();

        } catch (Exception e) {

            return null;
        }
        
        return nsList;
    }
    
    public List<String> findScanNames(String ref) {
        
        List<String> lsn = null;
        
        try {
            em.flush();
            
            Query vQry = em.createQuery(
                    "SELECT n.scanName FROM NessusScan n WHERE n.refId = :ref GROUP BY n.scanName")
                    .setParameter("ref", ref);
                    
            vQry.setMaxResults(1);        
        // Enable forced database query
            vQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            lsn =  vQry.getResultList();
        
        } catch (Exception e) {
            lsn = null;
        }
        
        return lsn;
       
    }
}
