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
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.CacheRetrieveMode;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.alertflex.entity.Alert;

@Stateless
public class AlertFacade extends AbstractFacade<Alert> {

    @PersistenceContext(unitName = "afevents_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AlertFacade() {
        super(Alert.class);
    }
    
    public List<Alert> findAlertsBySQL(String r, String q) {

        List<Alert> l = new ArrayList();

        try {
            em.flush();
            Query listQry = em.createQuery(q);
            // Enable forced database query
            listQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            l = (List<Alert>) listQry.getResultList();
        } catch (Exception e) {

            l = new ArrayList();
        }

        return l;
    }

    public List getOldAlerts(String ref, Timestamp timerange) {

        List l = new ArrayList();

        try {
            em.flush();
            Query listQry = em.createQuery("SELECT a FROM Alert a WHERE  a.refId = :ref AND a.timeOfEvent < :timerange AND a.status NOT IN (:st1, :st2, :st3)")
                .setParameter("ref", ref)
                .setParameter("timerange", timerange)
                .setParameter("st1", "incident")
                .setParameter("st2", "ml_incident")
                .setParameter("st3", "ml_confirmed");
            // Enable forced database query
            listQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            l = listQry.setMaxResults(1000).getResultList();
        } catch (Exception e) {

            l = new ArrayList();
        }

        return l;
    }

    public List<Alert> findAlertsByNewStatus() {

        List<Alert> l = new ArrayList();

        try {
            em.flush();

            Query listQry = em.createQuery("SELECT a FROM Alert a WHERE a.status IN (:st1, :st2, :st3)")
                    .setParameter("st1", "processed_new")
                    .setParameter("st2", "modified_new")
                    .setParameter("st3", "aggregated_new");
            // Enable forced database query
            listQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            l = listQry.getResultList();

        } catch (Exception e) {

            l = new ArrayList();
        }

        return l;
    }

    public Alert findAlertByUuid(String uuid) {

        Alert al;

        try {
            em.flush();

            Query alertsListQry = em.createQuery(
                    "SELECT a FROM Alert a WHERE a.alertUuid = :uuid").setParameter("uuid", uuid);

            // Enable forced database query
            alertsListQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            al = (Alert) alertsListQry.getSingleResult();

            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully Authenticated", ""));
        } catch (Exception e) {

            al = null;
        }

        return al;

    }

    public int delOldAlerts(String ref, Timestamp timerange) {

        int deletedCount = 0;

        try {
            em.flush();
            Query qry = em.createQuery("DELETE FROM Alert a WHERE a.refId = :ref AND a.timeOfEvent < :timerange AND a.status NOT IN (:st1, :st2, :st3)")
                    .setParameter("ref", ref)
                    .setParameter("timerange", timerange)
                    .setParameter("st1", "incident")
                    .setParameter("st2", "ml_incident")
                    .setParameter("st3", "ml_confirmed");

            // Enable forced database query
            qry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            deletedCount = qry.executeUpdate();
        } catch (Exception e) {

        }

        return deletedCount;
    }

}
