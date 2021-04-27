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
import org.alertflex.entity.KubeScan;

@Stateless
public class KubeScanFacade extends AbstractFacade<KubeScan> {

    @PersistenceContext(unitName = "alertflex_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public KubeScanFacade() {
        super(KubeScan.class);
    }

    public KubeScan findScan(String ref, String node, String probe, String id, String section, String number) {

        KubeScan ks = null;

        try {
            em.flush();

            Query qry = em.createQuery(
                    "SELECT k FROM KubeScan k WHERE k.refId = :ref AND k.nodeId = :node AND k.probe = :probe AND k.testId = :id AND k.sectionNumber = :section AND k.resultNumber = :number")
                    .setParameter("ref", ref)
                    .setParameter("node", node)
                    .setParameter("probe", probe)
                    .setParameter("id", id)
                    .setParameter("section", section)
                    .setParameter("number", number);
            qry.setMaxResults(1);
            // Enable forced database query
            qry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            ks = (KubeScan) qry.getSingleResult();

        } catch (Exception e) {
            ks = null;
        }

        return ks;
    }
    
    public List<Object[]> getFindings(String ref) {

        List<Object[]> f = null;

        try {
            em.flush();
            
            Query qry = em.createQuery(
                    "SELECT k.resultStatus, COUNT(k) FROM KubeScan k WHERE k.refId = :ref GROUP BY k.resultStatus")
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
