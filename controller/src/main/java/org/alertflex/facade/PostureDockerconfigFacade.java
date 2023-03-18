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
import org.alertflex.entity.PostureDockerconfig;

@Stateless
public class PostureDockerconfigFacade extends AbstractFacade<PostureDockerconfig> {

    @PersistenceContext(unitName = "alertflex_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PostureDockerconfigFacade() {
        super(PostureDockerconfig.class);
    }

    public PostureDockerconfig findMisconfig(String ref, String node, String probe, String artifactName, String target, String avdid) {

        PostureDockerconfig pd;

        try {
            em.flush();

            Query vQry = em.createQuery("SELECT p FROM PostureDockerconfig p WHERE p.refId = :ref AND p.node = :node AND p.probe = :probe AND p.artifactName = :artifact AND p.target = :target AND p.misconfigAvdid = :avdid")
                    .setParameter("ref", ref)
                    .setParameter("node", node)
                    .setParameter("probe", probe)
                    .setParameter("artifact", artifactName)
                    .setParameter("target", target)
                    .setParameter("avdid", avdid);
                    
            vQry.setMaxResults(1);
            // Enable forced database query
            vQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            pd = (PostureDockerconfig) vQry.getSingleResult();

        } catch (Exception e) {
            pd = null;
        }

        return pd;

    }
    
    public List<Object[]> getFindings(String ref) {

        List<Object[]> f = null;

        try {
            em.flush();
            
            Query qry = em.createQuery(
                    "SELECT p.severity, COUNT(p) FROM PostureDockerconfig p WHERE p.refId = :ref GROUP BY p.severity")
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
