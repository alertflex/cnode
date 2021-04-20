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
import org.alertflex.entity.SnykScan;

@Stateless
public class SnykScanFacade extends AbstractFacade<SnykScan> {

    @PersistenceContext(unitName = "alertflex_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SnykScanFacade() {
        super(SnykScan.class);
    }

    public SnykScan findVulnerability(String ref, String node, String probe, String prj, String id, String pkg) {

        SnykScan ss;

        try {
            em.flush();

            Query vQry = em.createQuery("SELECT s FROM SnykScan s WHERE s.refId = :ref AND s.nodeId = :node AND s.probe = :probe AND s.projectId = :prj AND s.vulnId = :id AND s.packageName = :pkg")
                    .setParameter("ref", ref)
                    .setParameter("node", node)
                    .setParameter("probe", probe)
                    .setParameter("prj", prj)
                    .setParameter("id", id)
                    .setParameter("pkg", pkg);
            vQry.setMaxResults(1);
            // Enable forced database query
            vQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            ss = (SnykScan) vQry.getSingleResult();

        } catch (Exception e) {
            ss = null;
        }

        return ss;

    }
    
    public List<Object[]> getFindings(String ref) {

        List<Object[]> f = null;

        try {
            em.flush();
            
            Query qry = em.createQuery(
                    "SELECT s.severity, COUNT(s) FROM SnykScan s WHERE s.refId = :ref GROUP BY s.severity")
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
