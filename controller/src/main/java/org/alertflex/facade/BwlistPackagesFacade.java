/*
 * Alertflex Management Console
 *
 * Copyright (C) 2021 Oleg Zharkov
 *
 */

package org.alertflex.facade;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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

}
