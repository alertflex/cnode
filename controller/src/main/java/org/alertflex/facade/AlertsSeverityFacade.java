/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alertflex.facade;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.alertflex.entity.AlertsSeverity;

/**
 *
 * @author root
 */
@Stateless
public class AlertsSeverityFacade extends AbstractFacade<AlertsSeverity> {

    @PersistenceContext(unitName = "alertflex_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AlertsSeverityFacade() {
        super(AlertsSeverity.class);
    }
    
}
