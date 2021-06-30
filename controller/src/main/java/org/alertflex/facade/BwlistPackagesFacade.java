/*
 * Alertflex Management Console
 *
 * Copyright (C) 2021 Oleg Zharkov
 *
 */

package org.alertflex.facade;

import javax.ejb.Stateless;
import javax.persistence.CacheRetrieveMode;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.alertflex.entity.BwlistPackages;

@Stateless
public class BwlistPackagesFacade extends AbstractFacade<BwlistPackages> {

    @PersistenceContext(unitName = "alertflex_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public BwlistPackagesFacade() {
        super(BwlistPackages.class);
    }
    
    public BwlistPackages findByName(String ref, String node, String name) {

        BwlistPackages blp = null;

        try {
            em.flush();

            Query listQry = em.createQuery(
                "SELECT b FROM BwlistPackages b WHERE b.nodeId = :node AND b.refId = :ref AND b.packageName = :name")
                    .setParameter("ref", ref)
                    .setParameter("node", node)
                    .setParameter("name", name);

            // Enable forced database query
            listQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            blp = (BwlistPackages) listQry.getSingleResult();

        } catch (Exception e) {
            blp = null;
        }

        return blp;

    }

}
