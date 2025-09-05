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
import org.alertflex.entity.Probes;

@Stateless
public class ProbesFacade extends AbstractFacade<Probes> {

    @PersistenceContext(unitName = "alertflex_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ProbesFacade() {
        super(Probes.class);
    }
    
    public List<Probes> findProbesByProjectId(String id) {

        List<Probes> lp = null;

        try {
            em.flush();

            Query lpQry = em.createQuery("SELECT p FROM Probes p WHERE p.projectId = :id").setParameter("id", id);

            // Enable forced database query
            lpQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            lp = lpQry.getResultList();
        } catch (Exception e) { }

        return lp;
    }

    public Probes findProbeByProjectIdAndProbeName(String id, String name) {

        Probes probe = null;

        try {
            em.flush();

            Query probesQry = em.createQuery("SELECT p FROM Probes p WHERE p.projectId = :id AND p.probeName = :name")
                    .setParameter("id", id).setParameter("name", name);

            // Enable forced database query
            probesQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            probe = (Probes) probesQry.getSingleResult();
        } catch (Exception e) { }

        return probe;
    }
}
