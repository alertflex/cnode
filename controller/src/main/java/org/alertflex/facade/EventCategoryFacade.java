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
import org.alertflex.entity.EventCategory;

/**
 *
 * @author root
 */
@Stateless
public class EventCategoryFacade extends AbstractFacade<EventCategory> {

    @PersistenceContext(unitName = "alertflex_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EventCategoryFacade() {
        super(EventCategory.class);
    }
    
    public List<String> findCatsByEvent(String source, String event) {
        
        List<String> cats = null;
        
        try {
            em.flush();
            
            Query catsQry = em.createQuery(
                "SELECT e.cats FROM EventCategory e WHERE e.source = :source AND e.event = :event GROUP BY e.cats")
                    .setParameter("source", source)
                    .setParameter("event", Integer.parseInt(event));
            cats = (List<String>) catsQry.getResultList();

            
        } catch (Exception e) {

            return null;
        }
        
        return cats;
    }
}
