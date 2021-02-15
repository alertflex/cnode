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

    public TrivyScan findVulnerability(String ref, String node, String sensor, String target, String cve, String pkgs) {

        TrivyScan ts;

        try {
            em.flush();

            Query vQry = em.createQuery("SELECT t FROM TrivyScan t WHERE t.refId = :ref AND t.nodeId = :node AND t.sensor = :sensor AND t.target = :target AND t.vulnerabilityId = :cve AND t.pkgName = :pkgs")
                    .setParameter("ref", ref)
                    .setParameter("node", node)
                    .setParameter("sensor", sensor)
                    .setParameter("target", target)
                    .setParameter("cve", cve)
                    .setParameter("pkgs", pkgs);
            vQry.setMaxResults(1);
            // Enable forced database query
            vQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            ts = (TrivyScan) vQry.getSingleResult();

        } catch (Exception e) {
            ts = null;
        }

        return ts;

    }

}
