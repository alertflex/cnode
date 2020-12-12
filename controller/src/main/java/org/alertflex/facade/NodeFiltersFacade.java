/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alertflex.facade;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.CacheRetrieveMode;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.alertflex.entity.NodeFilters;

/**
 *
 * @author root
 */
@Stateless
public class NodeFiltersFacade extends AbstractFacade<NodeFilters> {

    @PersistenceContext(unitName = "afevents_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public NodeFiltersFacade() {
        super(NodeFilters.class);
    }

    public int delOldStat(String ref, Timestamp timerange) {

        int deletedCount = 0;

        try {
            em.flush();
            Query qry = em.createQuery("DELETE FROM NodeFilters n  WHERE n.refId = :ref AND n.timeOfSurvey < :timerange")
                    .setParameter("ref", ref)
                    .setParameter("timerange", timerange);

            // Enable forced database query
            qry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            deletedCount = qry.executeUpdate();
        } catch (Exception e) {

        }

        return deletedCount;
    }

    public NodeFilters getLastRecord(String r, String n, Timestamp start, Timestamp end) {

        NodeFilters nf = null;

        try {

            em.flush();

            Query qry = em.createQuery(
                    "SELECT n FROM NodeFilters n WHERE n.refId = :ref AND n.nodeId = :node AND n.timeOfSurvey BETWEEN :start AND :end ORDER BY n.timeOfSurvey")
                    .setParameter("ref", r).setParameter("node", n).setParameter("start", start).setParameter("end", end);

            // Enable forced database query
            qry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            List<NodeFilters> nfl = qry.getResultList();

            if (nfl != null && nfl.size() > 0) {
                nf = nfl.get(nfl.size() - 1);
            }

        } catch (Exception e) {

            return null;
        }

        return nf;

    }

}
