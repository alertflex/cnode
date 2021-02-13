/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alertflex.facade;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.CacheRetrieveMode;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.alertflex.entity.Hosts;

/**
 *
 * @author root
 */
@Stateless
public class HostsFacade extends AbstractFacade<Hosts> {

    @PersistenceContext(unitName = "alertflex_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public HostsFacade() {
        super(Hosts.class);
    }

    public Hosts findHost(String r, String n) {

        Hosts h = null;

        try {
            em.flush();

            Query hostQry = em.createQuery("SELECT h FROM Hosts h WHERE h.refId = :ref AND h.name = :name").setParameter("ref", r).setParameter("name", n);

            // Enable forced database query
            hostQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            h = (Hosts) hostQry.getSingleResult();

            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully Authenticated", ""));
        } catch (Exception e) {

            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid query to DB", ""));
        }

        return h;
    }

    public List<Hosts> findHostsByRef(String r) {

        List<Hosts> listHost = null;

        try {
            em.flush();

            Query listHostQry = em.createQuery("SELECT h FROM Hosts h WHERE h.refId = :ref").setParameter("ref", r);

            // Enable forced database query
            listHostQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            listHost = listHostQry.getResultList();

            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully Authenticated", ""));
        } catch (Exception e) {

            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid query to DB", ""));
        }

        return listHost;
    }

    public List<String> findHostsNamesByRef(String r) {

        List<String> listHostName = null;

        try {
            em.flush();

            Query listHostQry = em.createQuery("SELECT h.name FROM Hosts h WHERE h.refId = :ref").setParameter("ref", r);

            // Enable forced database query
            listHostQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            listHostName = listHostQry.getResultList();

            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully Authenticated", ""));
        } catch (Exception e) {

            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid query to DB", ""));
        }

        return listHostName;
    }

    public List<String> findHostsNamesByNode(String r, String n) {

        List<String> listHostName = null;

        try {
            em.flush();

            Query listHostQry = em.createQuery("SELECT h.name FROM Hosts h WHERE h.refId = :ref AND h.node = :node").setParameter("ref", r).setParameter("node", n);

            // Enable forced database query
            listHostQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            listHostName = listHostQry.getResultList();

            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully Authenticated", ""));
        } catch (Exception e) {

            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid query to DB", ""));
        }

        return listHostName;
    }

    public List<Hosts> findHostsByNode(String r, String n) {

        List<Hosts> listHosts = null;

        try {
            em.flush();

            Query listHostsQry = em.createQuery("SELECT h FROM Hosts h WHERE h.refId = :ref AND h.node = :node").setParameter("ref", r).setParameter("node", n);

            // Enable forced database query
            listHostsQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            listHosts = listHostsQry.getResultList();

            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully Authenticated", ""));
        } catch (Exception e) {

            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid query to DB", ""));
        }

        return listHosts;
    }

    public List<Hosts> findHostsByCredential(String r, String c) {

        List<Hosts> listHosts = null;

        try {
            em.flush();

            Query listHostsQry = em.createQuery("SELECT h FROM Hosts h WHERE h.refId = :ref AND h.cred = :cred")
                    .setParameter("ref", r)
                    .setParameter("cred", c);

            // Enable forced database query
            listHostsQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            listHosts = listHostsQry.getResultList();

            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully Authenticated", ""));
        } catch (Exception e) {

            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid query to DB", ""));
        }

        return listHosts;
    }

    public Hosts findHostByAgent(String r, String n, String a) {

        Hosts h = null;

        try {
            em.flush();

            Query hostQry = em.createQuery("SELECT h FROM Hosts h WHERE h.refId = :ref AND h.node = :node AND h.agent = :agent")
                    .setParameter("ref", r)
                    .setParameter("node", n)
                    .setParameter("agent", a);

            // Enable forced database query
            hostQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            h = (Hosts) hostQry.getSingleResult();

            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully Authenticated", ""));
        } catch (Exception e) {

            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid query to DB", ""));
        }

        return h;
    }
}
