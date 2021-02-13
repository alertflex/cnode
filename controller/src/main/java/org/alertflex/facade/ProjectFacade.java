/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alertflex.facade;

import javax.ejb.Stateless;
import javax.persistence.CacheRetrieveMode;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.alertflex.entity.Project;

/**
 *
 * @author root
 */
@Stateless
public class ProjectFacade extends AbstractFacade<Project> {

    @PersistenceContext(unitName = "alertflex_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ProjectFacade() {
        super(Project.class);
    }

    public Project findProjectByName(String n) {

        Project p = null;

        try {
            em.flush();

            Query projectQry = em.createQuery(
                    "SELECT p FROM Project p WHERE p.name = :name").setParameter("name", n);

            projectQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            p = (Project) projectQry.getSingleResult();

        } catch (Exception e) {

        }

        return p;
    }

    public Project findProjectByRef(String r) {

        Project p = null;

        try {
            em.flush();

            Query projectQry = em.createQuery(
                    "SELECT p FROM Project p WHERE p.refId = :ref").setParameter("ref", r);

            projectQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            p = (Project) projectQry.getSingleResult();

        } catch (Exception e) {

        }

        return p;
    }

}
