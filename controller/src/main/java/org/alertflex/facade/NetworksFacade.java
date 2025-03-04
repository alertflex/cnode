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
import org.alertflex.entity.Networks;

@Stateless
public class NetworksFacade extends AbstractFacade<Networks> {

    @PersistenceContext(unitName = "alertflex_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public NetworksFacade() {
        super(Networks.class);
    }

    public List<Networks> findByRef(String ref) {

        List<Networks> ln = null;

        try {
            em.flush();

            Query listQry = em.createQuery(
                    "SELECT n FROM Networks n WHERE n.refId = :ref").setParameter("ref", ref);

            // Enable forced database query
            listQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            ln = (List<Networks>) listQry.getResultList();

        } catch (Exception e) {

        }

        return ln;

    }

}
