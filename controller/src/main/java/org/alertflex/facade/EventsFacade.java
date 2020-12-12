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
import org.alertflex.entity.Events;

/**
 *
 * @author root
 */
@Stateless
public class EventsFacade extends AbstractFacade<Events> {

    @PersistenceContext(unitName = "misp_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EventsFacade() {
        super(Events.class);
    }

    public Events findById(Integer id) {

        Events event = null;

        try {
            em.flush();

            Query eventQry = em.createQuery("SELECT e FROM Events e WHERE e.id = :id").setParameter("id", id);

            // Enable forced database query
            eventQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            event = (Events) eventQry.getSingleResult();

        } catch (Exception e) {

        }

        return event;

    }

}
