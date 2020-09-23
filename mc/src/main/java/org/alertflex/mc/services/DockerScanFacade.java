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
import org.alertflex.mc.db.DockerScan;

/**
 *
 * @author root
 */
@Stateless
public class DockerScanFacade extends AbstractFacade<DockerScan> {

    @PersistenceContext(unitName = "alertflex_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DockerScanFacade() {
        super(DockerScan.class);
    }
    
    public List<DockerScan> findRecord(String ref, String node, String sensor) {
        
        List<DockerScan> dsl;
        
        try {
            em.flush();
            
            Query qry = em.createQuery(
                "SELECT d FROM DockerScan d WHERE d.refId = :ref AND d.nodeId = :node AND d.sensor = :sensor")
                .setParameter("ref", ref)
                .setParameter("node", node)
                .setParameter("sensor", sensor);
            
            // Enable forced database query
            qry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            dsl =  (List<DockerScan>) qry.getResultList();
            
        
        } catch (Exception e) {
            dsl = null;
        }
        
        return dsl;
    }
    
    public List<String> findSensorNamesByNode(String ref, String node) {
        
        List<String> snl;
        
        try {
            em.flush();
            
            Query qry = em.createQuery(
                "SELECT d.sensor FROM DockerScan d WHERE d.refId = :ref AND d.nodeId = :node GROUP BY d.sensor")
                .setParameter("ref", ref)
                .setParameter("node", node);
            
            // Enable forced database query
            qry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            snl =  (List<String>) qry.getResultList();
            
        
        } catch (Exception e) {
            snl = null;
        }
        
        return snl;
    }
}
