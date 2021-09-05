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

import java.sql.Timestamp;
import javax.ejb.Stateless;
import javax.persistence.CacheRetrieveMode;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.alertflex.entity.NetCountries;

@Stateless
public class NetCountriesFacade extends AbstractFacade<NetCountries> {

    @PersistenceContext(unitName = "afevents_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public NetCountriesFacade() {
        super(NetCountries.class);
    }

    public int delOldStat(String ref, Timestamp timerange) {

        int deletedCount = 0;

        try {
            em.flush();
            Query qry = em.createQuery("DELETE FROM NetCountries n  WHERE n.refId = :ref AND n.timeOfSurvey < :timerange")
                    .setParameter("ref", ref)
                    .setParameter("timerange", timerange);

            // Enable forced database query
            qry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            deletedCount = qry.executeUpdate();
        } catch (Exception e) {

        }

        return deletedCount;
    }

    public NetCountries getLastRecord(String r, String n, String s) {

        NetCountries nc = null;

        try {
            em.flush();

            Query qry = em.createQuery(
                    "SELECT n FROM NetCountries n WHERE n.refId = :ref AND n.node = :node AND n.sensor = :sensor ORDER BY n.timeOfSurvey DESC")
                    .setParameter("ref", r).setParameter("node", n).setParameter("sensor", s).setMaxResults(1);

            // Enable forced database query
            qry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            nc = (NetCountries) qry.getSingleResult();

        } catch (Exception e) {

            return null;
        }

        return nc;
    }
}
