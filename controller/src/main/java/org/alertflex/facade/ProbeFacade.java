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
import org.alertflex.entity.Probe;

@Stateless
public class ProbeFacade extends AbstractFacade<Probe> {

    @PersistenceContext(unitName = "alertflex_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ProbeFacade() {
        super(Probe.class);
    }

    public Probe findProbeByName(String ref, String node, String name) {

        Probe p = null;

        try {
            em.flush();

            Query listQry = em.createQuery(
                    "SELECT p FROM Probe p WHERE p.probePK.node = :node AND p.probePK.refId = :ref AND p.probePK.name = :name")
                    .setParameter("ref", ref).setParameter("node", node).setParameter("name", name);

            // Enable forced database query
            listQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            p = (Probe) listQry.getSingleResult();

            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully Authenticated", ""));
        } catch (Exception e) {
            return null;
        }

        return p;

    }
    
    public List<Probe> findProbesByRef(String ref) {

        List<Probe> l = null;

        try {
            em.flush();

            Query listQry = em.createQuery(
                    "SELECT p FROM Probe p WHERE p.probePK.refId = :ref").setParameter("ref", ref);

            // Enable forced database query
            listQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            l = (List<Probe>) listQry.getResultList();

            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully Authenticated", ""));
        } catch (Exception e) {
            return null;
        }

        return l;

    }

    public List<Probe> findProbesByType(String ref, String node, String type) {

        List<Probe> l = null;

        try {
            em.flush();

            Query listQry = em.createQuery(
                    "SELECT p FROM Probe p WHERE p.probePK.node = :node AND p.probePK.refId = :ref AND p.probeType = :type")
                    .setParameter("ref", ref).setParameter("node", node).setParameter("type", type);

            // Enable forced database query
            listQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            l = (List<Probe>) listQry.getResultList();

            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully Authenticated", ""));
        } catch (Exception e) {
            return null;
        }

        return l;

    }
    
    public List<String> findProbeNamesByNode(String ref, String node) {

        List<String> l = null;

        try {
            em.flush();

            Query listQry = em.createQuery(
                "SELECT p.probePK.name FROM Probe p WHERE p.probePK.node = :node AND p.probePK.refId = :ref = :ref")
                    .setParameter("ref", ref).setParameter("node", node);

            // Enable forced database query
            listQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            l = listQry.getResultList();

        } catch (Exception e) {
            return null;
        }

        return l;
    }
    
    public List<Probe> findProbesByNode(String ref, String node) {

        List<Probe> l = null;

        try {
            em.flush();

            Query listQry = em.createQuery(
                "SELECT p FROM Probe p WHERE p.probePK.node = :node AND p.probePK.refId = :ref")
                    .setParameter("ref", ref).setParameter("node", node);

            // Enable forced database query
            listQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            l = listQry.getResultList();

        } catch (Exception e) {
            return null;
        }

        return l;
    }

}
