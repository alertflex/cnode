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
import org.alertflex.entity.AlertCategory;

/**
 *
 * @author root
 */
@Stateless
public class AlertCategoryFacade extends AbstractFacade<AlertCategory> {

    @PersistenceContext(unitName = "alertflex_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AlertCategoryFacade() {
        super(AlertCategory.class);
    }

    public List<String> findCatsByEvent(String source, String event) {

        List<String> cats = null;

        try {
            em.flush();

            Query catsQry = em.createQuery(
                    "SELECT a.cats FROM AlertCategory a WHERE a.source = :source AND a.eventId = :event GROUP BY a.cats")
                    .setParameter("source", source)
                    .setParameter("event", event);
            cats = (List<String>) catsQry.getResultList();

        } catch (Exception e) {

            return null;
        }

        return cats;
    }
}
