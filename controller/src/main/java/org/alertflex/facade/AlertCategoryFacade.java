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

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.CacheRetrieveMode;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.alertflex.entity.AlertCategory;

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
