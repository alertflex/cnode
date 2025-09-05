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
import org.alertflex.entity.Tools;

@Stateless
public class ToolsFacade extends AbstractFacade<Tools> {

    @PersistenceContext(unitName = "alertflex_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ToolsFacade() {
        super(Tools.class);
    }

    public Tools findToolsByProjectId(String id) {

        Tools tools = null;

        try {
            em.flush();

            Query toolsQry = em.createQuery("SELECT t FROM Tools t WHERE t.projectId = :id").setParameter("id", id);

            // Enable forced database query
            toolsQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            tools = (Tools) toolsQry.getSingleResult();
        } catch (Exception e) { }

        return tools;
    }
}
