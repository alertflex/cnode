/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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

/**
 *
 * @author root
 */
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

    public List getOldAlerts(Timestamp timerange) {

        List l = new ArrayList();

        try {
            em.flush();
            Query listQry = em.createQuery("SELECT a FROM Alert a WHERE a.timeOfEvent < :timerange AND a.status != :status").setParameter("timerange", timerange).setParameter("status", "incident");
            // Enable forced database query
            listQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            l = listQry.getResultList();
        } catch (Exception e) {

            l = new ArrayList();
        }

        return l;
    }

    public List<Alert> findAlertsByNewStatus() {

        List<Alert> l = new ArrayList();

        try {
            em.flush();

            Query listQry = em.createQuery("SELECT a FROM Alert a WHERE a.status IN (:st1, :st2, :st3)").setParameter("st1", "processed_new").setParameter("st2", "modified_new").setParameter("st3", "aggregated_new");
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
