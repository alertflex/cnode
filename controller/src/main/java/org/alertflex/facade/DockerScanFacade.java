/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alertflex.facade;

import javax.ejb.Stateless;
import javax.persistence.CacheRetrieveMode;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.alertflex.entity.DockerScan;

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
    
    public DockerScan findRecord(String ref, String node, String sensor, String resultId, String result) {
        
        DockerScan ds;
        
        try {
            em.flush();
            
            Query qry = em.createQuery(
                    "SELECT d FROM DockerScan d WHERE d.refId = :ref AND d.nodeId = :node AND d.sensor = :sensor AND d.resultId = :id AND d.result = :result")
                    .setParameter("ref", ref)
                    .setParameter("node", node)
                    .setParameter("sensor", sensor)
                    .setParameter("id", resultId)
                    .setParameter("result", result);
            qry.setMaxResults(1);        
            // Enable forced database query
            qry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            ds =  (DockerScan) qry.getSingleResult();
        
        } catch (Exception e) {
            ds = null;
        }
        
        return ds;
    }
}
