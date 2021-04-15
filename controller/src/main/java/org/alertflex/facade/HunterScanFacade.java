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

import javax.ejb.Stateless;
import javax.persistence.CacheRetrieveMode;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.alertflex.entity.HunterScan;

@Stateless
public class HunterScanFacade extends AbstractFacade<HunterScan> {

    @PersistenceContext(unitName = "alertflex_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public HunterScanFacade() {
        super(HunterScan.class);
    }

    public HunterScan findScan(String ref, String node, String probe, String target, String loc, String vid, String cat) {

        HunterScan hs = null;

        try {
            em.flush();

            Query qry = em.createQuery(
                    "SELECT h FROM HunterScan h WHERE h.refId = :ref AND h.nodeId = :node AND h.probe = :probe AND h.target = :target AND h.location = :loc AND h.vid = :vid AND h.cat = :cat")
                    .setParameter("ref", ref)
                    .setParameter("node", node)
                    .setParameter("probe", probe)
                    .setParameter("target", target)
                    .setParameter("loc", loc)
                    .setParameter("vid", vid)
                    .setParameter("cat", cat);
            qry.setMaxResults(1);
            // Enable forced database query
            qry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            hs = (HunterScan) qry.getSingleResult();

        } catch (Exception e) {
            hs = null;
        }

        return hs;
    }
}
