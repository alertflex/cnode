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
import org.alertflex.entity.PostureInspector;
import org.alertflex.reports.Finding;

@Stateless
public class PostureInspectorFacade extends AbstractFacade<PostureInspector> {

    @PersistenceContext(unitName = "alertflex_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PostureInspectorFacade() {
        super(PostureInspector.class);
    }

    public PostureInspector findMisconfig(String ref, String template, String title, String ec2) {

        PostureInspector pi = null;

        try {
            em.flush();

            Query qry = em.createQuery(
                    "SELECT p FROM PostureInspector p WHERE p.refId = :ref AND p.arn = :template AND p.title = :title AND p.ec2Name = :ec2")
                    .setParameter("ref", ref)
                    .setParameter("template", template)
                    .setParameter("title", title)
                    .setParameter("ec2", ec2);
            
            // Enable forced database query
            qry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            pi = (PostureInspector) qry.getSingleResult();

        } catch (Exception e) {
            pi = null;
        }

        return pi;
    }
    
    public List<Object[]> getFindings(String ref) {

        List<Object[]> f = null;

        try {
            em.flush();

            Query qry = em.createQuery(
                    "SELECT p.severity, COUNT(p) FROM PostureInspector p WHERE p.refId = :ref GROUP BY p.severity", Finding.class)
                    .setParameter("ref", ref);
            // Enable forced database query
            qry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            
            f = (List<Object[]>) qry.getResultList();

        } catch (Exception e) {
            f = null;
        }

        return f;
    }
}
