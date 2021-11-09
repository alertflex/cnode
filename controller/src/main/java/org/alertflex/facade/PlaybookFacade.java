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

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.CacheRetrieveMode;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.alertflex.entity.Playbook;

@Stateless
public class PlaybookFacade extends AbstractFacade<Playbook> {

    @PersistenceContext(unitName = "alertflex_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PlaybookFacade() {
        super(Playbook.class);
    }

    public List<String> findAllPlaybookNames(String r) {

        List<String> listPlaybooks = null;

        try {
            em.flush();

            Query listQry = em.createQuery("SELECT p.name FROM Playbook p WHERE p.refId = :ref").setParameter("ref", r);

            // Enable forced database query
            listQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            listPlaybooks = listQry.getResultList();

            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully Authenticated", ""));
        } catch (Exception e) {

            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid query to DB", ""));
        }

        return listPlaybooks;
    }

    public List<Playbook> findAllPlaybooks(String r) {

        List<Playbook> listPlaybooks = null;

        try {
            em.flush();

            Query listQry = em.createQuery("SELECT p FROM Playbook p WHERE p.refId = :ref").setParameter("ref", r);

            // Enable forced database query
            listQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            listPlaybooks = listQry.getResultList();

            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully Authenticated", ""));
        } catch (Exception e) {

            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid query to DB", ""));
        }

        return listPlaybooks;
    }

    public Playbook findPlaybookByName(String r, String n) {

        Playbook playbook = null;

        try {
            em.flush();

            Query listQry = em.createQuery("SELECT p FROM Playbook p WHERE p.refId = :ref AND p.name = :name").setParameter("ref", r).setParameter("name", n);

            // Enable forced database query
            listQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            playbook = (Playbook) listQry.getSingleResult();

            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully Authenticated", ""));
        } catch (Exception e) {
            return null;
        }

        return playbook;
    }

    public Playbook findPlaybookByWebhook(String w) {

        Playbook playbook = null;

        try {
            em.flush();

            Query listQry = em.createQuery("SELECT p FROM Playbook p WHERE p.webhook = :webhook").setParameter("webhook", w);

            // Enable forced database query
            listQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            playbook = (Playbook) listQry.getSingleResult();

            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully Authenticated", ""));
        } catch (Exception e) {
            return null;
        }

        return playbook;
    }
    
    public List findTopPlaybooks(String r) {

        List l = new ArrayList();

        try {
            em.flush();

            Query listQry = em.createQuery(
                    "SELECT p.name, count(p) as Counter FROM Playbook p WHERE p.refId = :ref GROUP BY p.name ORDER BY Counter DESC")
                    .setParameter("ref", r).setMaxResults(10);

            // Enable forced database query
            listQry.setMaxResults(10).setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            l = listQry.getResultList();

        } catch (Exception e) {

            l = new ArrayList();
        }

        return l;

    }

}
