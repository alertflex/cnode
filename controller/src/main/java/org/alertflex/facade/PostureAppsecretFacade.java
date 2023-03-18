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
import org.alertflex.entity.PostureAppsecret;

@Stateless
public class PostureAppsecretFacade extends AbstractFacade<PostureAppsecret> {

    @PersistenceContext(unitName = "alertflex_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PostureAppsecretFacade() {
        super(PostureAppsecret.class);
    }

    public PostureAppsecret findSecret(String ref, String node, String probe, String artifactName, String target, String rule, int start, int end) {

        PostureAppsecret pa;

        try {
            em.flush();

            Query vQry = em.createQuery("SELECT p FROM PostureAppsecret p WHERE p.refId = :ref AND p.node = :node AND p.probe = :probe AND p.artifactName = :artifact AND p.target = :target AND p.ruleId = :rule AND p.startLine = :start AND p.endLine = :end")
                    .setParameter("ref", ref)
                    .setParameter("node", node)
                    .setParameter("probe", probe)
                    .setParameter("artifact", artifactName)
                    .setParameter("target", target)
                    .setParameter("rule", rule)
                    .setParameter("start", start)
                    .setParameter("end", end);
            vQry.setMaxResults(1);
            // Enable forced database query
            vQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            pa = (PostureAppsecret) vQry.getSingleResult();

        } catch (Exception e) {
            pa = null;
        }

        return pa;

    }
    
    public List<Object[]> getFindings(String ref) {

        List<Object[]> f = null;

        try {
            em.flush();
            
            Query qry = em.createQuery(
                    "SELECT p.severity, COUNT(p) FROM PostureAppsecret p WHERE p.refId = :ref GROUP BY p.severity")
                    .setParameter("ref", ref);
            // Enable forced database query
            qry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            
            f =  (List<Object[]>) qry.getResultList();

        } catch (Exception e) {
            f = null;
        }

        return f;
    }

}
