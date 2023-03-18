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
import org.alertflex.entity.PostureTask;

@Stateless
public class PostureTaskFacade extends AbstractFacade<PostureTask> {

    @PersistenceContext(unitName = "alertflex_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PostureTaskFacade() {
        super(PostureTask.class);
    }

    public PostureTask findPosture(String uuid) {

        PostureTask pt = null;

        try {
            em.flush();

            Query qry = em.createQuery(
                    "SELECT p FROM PostureTask p WHERE p.taskUuid = :uuid ")
                    .setParameter("uuid", uuid);
            qry.setMaxResults(1);
            // Enable forced database query
            qry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            pt = (PostureTask) qry.getSingleResult();

        } catch (Exception e) {
            pt = null;
        }

        return pt;
    }
}
