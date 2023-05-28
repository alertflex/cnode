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
import org.alertflex.entity.PostureNikto;

@Stateless
public class PostureNiktoFacade extends AbstractFacade<PostureNikto> {

    @PersistenceContext(unitName = "alertflex_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PostureNiktoFacade() {
        super(PostureNikto.class);
    }

    public PostureNikto findVulnerability(String ref, String node, String probe, String target, String port, String vulnId) {

        PostureNikto pn = null;

        try {
            em.flush();

            Query qry = em.createQuery(
                    "SELECT p FROM PostureNikto p WHERE p.refId = :ref AND p.node = :node AND p.probe = :probe AND p.target = :target AND p.port = :port AND p.vulnId = :vulnId")
                    .setParameter("ref", ref)
                    .setParameter("node", node)
                    .setParameter("probe", probe)
                    .setParameter("target", target)
                    .setParameter("port", port)
                    .setParameter("vulnId", vulnId);
            // Enable forced database query
            qry.setMaxResults(1);
            
            qry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            pn = (PostureNikto) qry.getSingleResult();

        } catch (Exception e) {
            pn = null;
        }

        return pn;
    }
    
    public List<Object[]> getFindings(String ref) {

        List<Object[]> f = null;

        try {
            em.flush();
            
            Query qry = em.createQuery(
                    "SELECT p.severity, COUNT(p) FROM PostureNikto p WHERE p.refId = :ref GROUP BY p.severity")
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
