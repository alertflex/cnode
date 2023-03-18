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
import org.alertflex.entity.PostureK8svuln;

@Stateless
public class PostureK8svulnFacade extends AbstractFacade<PostureK8svuln> {

    @PersistenceContext(unitName = "alertflex_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PostureK8svulnFacade() {
        super(PostureK8svuln.class);
    }

    public PostureK8svuln findVulnerability(String ref, String node, String probe, String clusterName, String namespace, String target, String vuln, String pkgName, String pkgVer) {

        PostureK8svuln pk;

        try {
            em.flush();

            Query vQry = em.createQuery("SELECT p FROM PostureK8svuln p WHERE p.refId = :ref AND p.node = :node AND p.probe = :probe AND p.clusterName = :cluster AND p.namespace = :namespace AND p.target = :target AND p.vulnerabilityId = :vuln AND p.pkgName = :pkgName AND p.pkgVersion = :pkgVer")
                    .setParameter("ref", ref)
                    .setParameter("node", node)
                    .setParameter("probe", probe)
                    .setParameter("cluster", clusterName)
                    .setParameter("namespace", namespace)
                    .setParameter("target", target)
                    .setParameter("vuln", vuln)
                    .setParameter("pkgName", pkgName)
                    .setParameter("pkgVer", pkgVer);
            vQry.setMaxResults(1);
            // Enable forced database query
            vQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            pk = (PostureK8svuln) vQry.getSingleResult();

        } catch (Exception e) {
            pk = null;
        }

        return pk;

    }
    
    public List<Object[]> getFindings(String ref) {

        List<Object[]> f = null;

        try {
            em.flush();
            
            Query qry = em.createQuery(
                    "SELECT p.severity, COUNT(p) FROM PostureK8svuln p WHERE p.refId = :ref GROUP BY p.severity")
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
