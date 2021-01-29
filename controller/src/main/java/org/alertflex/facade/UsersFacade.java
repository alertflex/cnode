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
import org.alertflex.entity.Users;

@Stateless
public class UsersFacade extends AbstractFacade<Users> {

    @PersistenceContext(unitName = "alertflex_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UsersFacade() {
        super(Users.class);
    }

    public List<Users> findUsersByRefId(String r) {

        List<Users> usersList = null;

        try {
            em.flush();

            Query usersListQry = em.createQuery("SELECT u FROM Users u WHERE u.refId = :ref").setParameter("ref", r);

            // Enable forced database query
            usersListQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            usersList = usersListQry.getResultList();
        } catch (Exception e) {

            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid quety to DB", ""));
        }

        return usersList;
    }

    public Users findUserById(String tenant, String id) {

        Users user = null;

        try {
            em.flush();

            Query usersListQry = em.createQuery("SELECT u FROM Users u WHERE u.refId = :tenant AND u.userid = :user").setParameter("tenant", tenant).setParameter("user", id);

            // Enable forced database query
            usersListQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            user = (Users) usersListQry.getSingleResult();
        } catch (Exception e) {

            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid quety to DB", ""));
        }

        return user;

    }

    public Users findUserByName(String name) {

        Users user = null;

        try {
            em.flush();

            Query usersListQry = em.createQuery("SELECT u FROM Users u WHERE u.userid = :user").setParameter("user", name);

            // Enable forced database query
            usersListQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            user = (Users) usersListQry.getSingleResult();
        } catch (Exception e) {

            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid quety to DB", ""));
        }

        return user;

    }

}
