/*
 * Copyright (C) 2021 Oleg Zharkov
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the Server Side Public License, version 1,
 * as published by MongoDB, Inc.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Server Side Public License for more details.
 *
 * You should have received a copy of the Server Side Public License
 * along with this program. If not, see
 * <http://www.mongodb.com/licensing/server-side-public-license>.
 */

package org.alertflex.facade;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.CacheRetrieveMode;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.alertflex.entity.Project;

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
                    "SELECT p FROM Project p WHERE p.projectName = :name").setParameter("name", n);
            // Enable forced database query
            projectQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            p = (Project) projectQry.getSingleResult();

        } catch (Exception e) {

        }

        return p;
    }

    public Project findProjectById(String id) {

        Project p = null;

        try {
            em.flush();

            Query projectQry = em.createQuery(
                    "SELECT p FROM Project p WHERE p.projectId = :id").setParameter("id", id);
            // Enable forced database query
            projectQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            p = (Project) projectQry.getSingleResult();

        } catch (Exception e) {

        }

        return p;
    }
    
    public List<String> findAllProjectNames() {

        List<String> listNames = null;

        try {
            em.flush();

            Query listQry = em.createQuery("SELECT p.projectName FROM Project p");

            // Enable forced database query
            listQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            listNames = listQry.getResultList();

        } catch (Exception e) {

        }

        return listNames;
    }
    
    public List<Project> findAllProjects() {

        List<Project> listProjects = null;

        try {
            em.flush();

            Query listQry = em.createQuery("SELECT p FROM Project p");

            // Enable forced database query
            listQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            listProjects = listQry.getResultList();

        } catch (Exception e) { }

        return listProjects;
    }
}
