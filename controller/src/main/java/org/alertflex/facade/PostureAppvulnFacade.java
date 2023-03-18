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
import org.alertflex.entity.PostureAppvuln;

@Stateless
public class PostureAppvulnFacade extends AbstractFacade<PostureAppvuln> {

    @PersistenceContext(unitName = "alertflex_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PostureAppvulnFacade() {
        super(PostureAppvuln.class);
    }

    public PostureAppvuln findVulnerability(String ref, String node, String probe, String artifactName, String target, String vuln, String pkgName, String pkgVer) {

        PostureAppvuln pa;

        try {
            em.flush();

            Query vQry = em.createQuery("SELECT p FROM PostureAppvuln p WHERE p.refId = :ref AND p.node = :node AND p.probe = :probe AND p.artifactName = :artifact AND p.target = :target AND p.vulnerabilityId = :vuln AND p.pkgName = :pkgName AND p.pkgVersion = :pkgVer")
                    .setParameter("ref", ref)
                    .setParameter("node", node)
                    .setParameter("probe", probe)
                    .setParameter("artifact", artifactName)
                    .setParameter("target", target)
                    .setParameter("vuln", vuln)
                    .setParameter("pkgName", pkgName)
                    .setParameter("pkgVer", pkgVer);
            vQry.setMaxResults(1);
            // Enable forced database query
            vQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            pa = (PostureAppvuln) vQry.getSingleResult();

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
                    "SELECT p.severity, COUNT(p) FROM PostureAppvuln p WHERE p.refId = :ref GROUP BY p.severity")
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
