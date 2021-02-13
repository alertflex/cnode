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
import org.alertflex.entity.Container;

/**
 *
 * @author root
 */
@Stateless
public class ContainerFacade extends AbstractFacade<Container> {

    @PersistenceContext(unitName = "alertflex_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ContainerFacade() {
        super(Container.class);
    }

    public List<Container> findBySensor(String ref, String node, String sensor) {

        List<Container> lc = null;

        try {
            em.flush();

            Query listQry = em.createQuery(
                "SELECT c FROM Container c WHERE c.nodeId = :node AND c.refId = :ref AND c.sensorName = :sensor")
                    .setParameter("ref", ref).setParameter("node", node).setParameter("sensor", sensor);

            // Enable forced database query
            listQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            lc = listQry.getResultList();

        } catch (Exception e) {
            lc = null;
        }

        return lc;
    }
    
    public Container findByName(String ref, String node, String sensor, String id) {

        Container c = null;

        try {
            em.flush();

            Query listQry = em.createQuery(
                "SELECT c FROM Container c WHERE c.nodeId = :node AND c.refId = :ref AND c.sensorName = :sensor AND c.containerId = :id")
                    .setParameter("ref", ref).setParameter("node", node).setParameter("sensor", sensor).setParameter("id", id);

            // Enable forced database query
            listQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            c = (Container) listQry.getSingleResult();

        } catch (Exception e) {
            c = null;
        }

        return c;

    }
}
