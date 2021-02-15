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
import org.alertflex.entity.Container;

@Stateless
public class ContainerFacade extends AbstractFacade<Container> {

    @PersistenceContext(unitName = "alertflex_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ContainerFacade() {
        super(Container.class);
    }

    public List<Container> findBySensor(String ref, String node, String sensor) {

        List<Container> lc = null;

        try {
            em.flush();

            Query listQry = em.createQuery(
                "SELECT c FROM Container c WHERE c.nodeId = :node AND c.refId = :ref AND c.sensorName = :sensor")
                    .setParameter("ref", ref).setParameter("node", node).setParameter("sensor", sensor);

            // Enable forced database query
            listQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            lc = listQry.getResultList();

        } catch (Exception e) {
            lc = null;
        }

        return lc;
    }
    
    public Container findByName(String ref, String node, String sensor, String id) {

        Container c = null;

        try {
            em.flush();

            Query listQry = em.createQuery(
                "SELECT c FROM Container c WHERE c.nodeId = :node AND c.refId = :ref AND c.sensorName = :sensor AND c.containerId = :id")
                    .setParameter("ref", ref).setParameter("node", node).setParameter("sensor", sensor).setParameter("id", id);

            // Enable forced database query
            listQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            c = (Container) listQry.getSingleResult();

        } catch (Exception e) {
            c = null;
        }

        return c;

    }
}
