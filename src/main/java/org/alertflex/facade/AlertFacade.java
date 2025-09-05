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
    
    public Alert findAlertsByFlowId(String projectId, String flowId) {

        Alert alert;

        try {
            em.flush();

            Query alertQry = em.createQuery(
                "SELECT a FROM Alert a WHERE a.projectId = :id AND a.httpFlowId = :flowid")
                    .setParameter("id", projectId).setParameter("flowid", flowId);
            // Enable forced database query
            alertQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            alert = (Alert) alertQry.getSingleResult();
        } catch (Exception e) {
            alert = null;
        }

        return alert;
    }
}
