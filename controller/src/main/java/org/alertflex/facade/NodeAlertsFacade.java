/*
 *   Copyright 2021 Oleg Zharkov
 *
 *   Licensed under the Apache License, Version 2.0 (the "License").
 *   You may not use this file except in compliance with the License.
 *   A copy of the License is located at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   or in the "license" file accompanying this file. This file is distributed
 *   on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 *   express or implied. See the License for the specific language governing
 *   permissions and limitations under the License.
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
import org.alertflex.entity.NodeAlerts;

@Stateless
public class NodeAlertsFacade extends AbstractFacade<NodeAlerts> {

    @PersistenceContext(unitName = "afevents_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public NodeAlertsFacade() {
        super(NodeAlerts.class);
    }

    public int delOldStat(String ref, Timestamp timerange) {

        int deletedCount = 0;

        try {
            em.flush();
            Query qry = em.createQuery("DELETE FROM NodeAlerts n  WHERE n.refId = :ref AND n.timeOfSurvey < :timerange")
                    .setParameter("ref", ref)
                    .setParameter("timerange", timerange);

            // Enable forced database query
            qry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            deletedCount = qry.executeUpdate();
        } catch (Exception e) {

        }

        return deletedCount;
    }

    public NodeAlerts getLastRecord(String r, String n, Timestamp start, Timestamp end) {

        NodeAlerts na = null;

        try {

            em.flush();

            Query qry = em.createQuery(
                    "SELECT n FROM NodeAlerts n WHERE n.refId = :ref AND n.nodeId = :node AND n.timeOfSurvey BETWEEN :start AND :end ORDER BY n.timeOfSurvey")
                    .setParameter("ref", r).setParameter("node", n).setParameter("start", start).setParameter("end", end);

            // Enable forced database query
            qry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            List<NodeAlerts> nal = qry.getResultList();

            if (nal != null && nal.size() > 0) {
                na = nal.get(nal.size() - 1);
            }

        } catch (Exception e) {

            return null;
        }

        return na;
    }
}
