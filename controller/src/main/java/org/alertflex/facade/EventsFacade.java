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

import javax.ejb.Stateless;
import javax.persistence.CacheRetrieveMode;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.alertflex.entity.Events;

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
