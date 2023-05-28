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
import org.alertflex.entity.PostureCloudsploit;

@Stateless
public class PostureCloudsploitFacade extends AbstractFacade<PostureCloudsploit> {

    @PersistenceContext(unitName = "alertflex_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PostureCloudsploitFacade() {
        super(PostureCloudsploit.class);
    }

    public PostureCloudsploit findVulnerability(String ref, String cloud, String region, String resources, String plugin, String title) {

        PostureCloudsploit pc = null;

        try {
            em.flush();

            Query qry = em.createQuery(
                    "SELECT p FROM PostureCloudsploit p WHERE p.refId = :ref AND p.cloudType = :cloud AND p.resources = :resources AND p.region = :region AND p.plugin = :plugin AND p.title = :title")
                    .setParameter("ref", ref)
                    .setParameter("cloud", cloud)
                    .setParameter("region", region)
                    .setParameter("resources", resources)
                    .setParameter("plugin", plugin)
                    .setParameter("title", title);
            // Enable forced database query
            qry.setMaxResults(1);
            
            qry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            pc = (PostureCloudsploit) qry.getSingleResult();

        } catch (Exception e) {
            pc = null;
        }

        return pc;
    }
    
    public List<Object[]> getFindings(String ref) {

        List<Object[]> f = null;

        try {
            em.flush();
            
            Query qry = em.createQuery(
                    "SELECT p.severity, COUNT(p) FROM PostureCloudsploit p WHERE p.refId = :ref GROUP BY p.severity")
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
