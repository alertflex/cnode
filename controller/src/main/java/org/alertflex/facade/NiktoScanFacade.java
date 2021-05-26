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
import org.alertflex.entity.NiktoScan;

@Stateless
public class NiktoScanFacade extends AbstractFacade<NiktoScan> {

    @PersistenceContext(unitName = "alertflex_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public NiktoScanFacade() {
        super(NiktoScan.class);
    }

    public NiktoScan findVulnerability(String ref, String node, String probe, String host, String ip, String port, String id) {

        NiktoScan ns;

        try {
            em.flush();

            Query vQry = em.createQuery("SELECT n FROM NiktoScan n WHERE n.refId = :ref AND n.nodeId = :node AND n.probe = :probe AND n.host = :host AND n.ip = :ip AND n.port = :port AND n.vulnId = :id")
                    .setParameter("ref", ref)
                    .setParameter("node", node)
                    .setParameter("probe", probe)
                    .setParameter("host", host)
                    .setParameter("ip", ip)
                    .setParameter("port", port)
                    .setParameter("id", id);
            vQry.setMaxResults(1);
            // Enable forced database query
            vQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            ns = (NiktoScan) vQry.getSingleResult();

        } catch (Exception e) {
            ns = null;
        }

        return ns;

    }
    
}
