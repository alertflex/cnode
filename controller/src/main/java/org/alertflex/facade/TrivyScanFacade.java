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
import org.alertflex.entity.TrivyScan;

@Stateless
public class TrivyScanFacade extends AbstractFacade<TrivyScan> {

    @PersistenceContext(unitName = "alertflex_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TrivyScanFacade() {
        super(TrivyScan.class);
    }

    public TrivyScan findVulnerability(String ref, String node, String probe, String image, String vuln, String pkg) {

        TrivyScan ts;

        try {
            em.flush();

            Query vQry = em.createQuery("SELECT t FROM TrivyScan t WHERE t.refId = :ref AND t.nodeId = :node AND t.probe = :probe AND t.imageName = :image AND t.vulnerability = :vuln AND t.pkgName = :pkg")
                    .setParameter("ref", ref)
                    .setParameter("node", node)
                    .setParameter("probe", probe)
                    .setParameter("image", image)
                    .setParameter("vuln", vuln)
                    .setParameter("pkg", pkg);
            vQry.setMaxResults(1);
            // Enable forced database query
            vQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            ts = (TrivyScan) vQry.getSingleResult();

        } catch (Exception e) {
            ts = null;
        }

        return ts;

    }
    
    public List<Object[]> getFindings(String ref) {

        List<Object[]> f = null;

        try {
            em.flush();
            
            Query qry = em.createQuery(
                    "SELECT t.severity, COUNT(t) FROM TrivyScan t WHERE t.refId = :ref GROUP BY t.severity")
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
