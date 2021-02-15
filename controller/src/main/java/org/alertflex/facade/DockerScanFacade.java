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
import org.alertflex.entity.DockerScan;

@Stateless
public class DockerScanFacade extends AbstractFacade<DockerScan> {

    @PersistenceContext(unitName = "alertflex_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DockerScanFacade() {
        super(DockerScan.class);
    }

    public DockerScan findRecord(String ref, String node, String sensor, String resultId, String result) {

        DockerScan ds;

        try {
            em.flush();

            Query qry = em.createQuery(
                    "SELECT d FROM DockerScan d WHERE d.refId = :ref AND d.nodeId = :node AND d.sensor = :sensor AND d.resultId = :id AND d.result = :result")
                    .setParameter("ref", ref)
                    .setParameter("node", node)
                    .setParameter("sensor", sensor)
                    .setParameter("id", resultId)
                    .setParameter("result", result);
            qry.setMaxResults(1);
            // Enable forced database query
            qry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            ds = (DockerScan) qry.getSingleResult();

        } catch (Exception e) {
            ds = null;
        }

        return ds;
    }
}
