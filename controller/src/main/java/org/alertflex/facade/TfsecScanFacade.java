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
import org.alertflex.entity.TfsecScan;

@Stateless
public class TfsecScanFacade extends AbstractFacade<TfsecScan> {

    @PersistenceContext(unitName = "alertflex_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TfsecScanFacade() {
        super(TfsecScan.class);
    }

    public TfsecScan findVulnerability(String ref, String node, String probe, String prj, String rule, String filename) {

        TfsecScan ts;

        try {
            em.flush();

            Query vQry = em.createQuery("SELECT t FROM TfsecScan t WHERE t.refId = :ref AND t.node = :node AND t.probe = :probe AND t.projectId = :prj AND t.ruleId = :rule AND t.filename = :file")
                    .setParameter("ref", ref)
                    .setParameter("node", node)
                    .setParameter("probe", probe)
                    .setParameter("prj", prj)
                    .setParameter("rule", rule)
                    .setParameter("file", filename);
            vQry.setMaxResults(1);
            // Enable forced database query
            vQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            ts = (TfsecScan) vQry.getSingleResult();

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
                    "SELECT t.severity, COUNT(t) FROM TfsecScan t WHERE t.refId = :ref GROUP BY t.severity")
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

