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
import org.alertflex.entity.Project;

@Stateless
public class ProjectFacade extends AbstractFacade<Project> {

    @PersistenceContext(unitName = "alertflex_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ProjectFacade() {
        super(Project.class);
    }

    public Project findProjectByName(String n) {

        Project p = null;

        try {
            em.flush();

            Query projectQry = em.createQuery(
                    "SELECT p FROM Project p WHERE p.name = :name").setParameter("name", n);

            projectQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            p = (Project) projectQry.getSingleResult();

        } catch (Exception e) {

        }

        return p;
    }

    public Project findProjectByRef(String r) {

        Project p = null;

        try {
            em.flush();

            Query projectQry = em.createQuery(
                    "SELECT p FROM Project p WHERE p.refId = :ref").setParameter("ref", r);

            projectQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            p = (Project) projectQry.getSingleResult();

        } catch (Exception e) {

        }

        return p;
    }

}
