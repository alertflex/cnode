/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alertflex.facade;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.CacheRetrieveMode;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.alertflex.entity.Agent;

/**
 *
 * @author root
 */
@Stateless
public class AgentFacade extends AbstractFacade<Agent> {

    @PersistenceContext(unitName = "alertflex_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AgentFacade() {
        super(Agent.class);
    }

    public Agent findAgentByName(String ref, String node, String name) {

        Agent a = null;

        try {
            em.flush();

            Query agentProcessQry = em.createQuery(
                    "SELECT a FROM Agent a WHERE a.refId = :ref AND a.nodeId = :node AND a.name = :name")
                    .setParameter("ref", ref).setParameter("node", node).setParameter("name", name);
            agentProcessQry.setMaxResults(1);
            // Enable forced database query
            agentProcessQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);

            a = (Agent) agentProcessQry.getSingleResult();

        } catch (Exception e) {

        }

        return a;
    }

    public Agent findAgentById(String ref, String node, String id) {

        Agent a = null;

        try {
            em.flush();

            Query agentProcessQry = em.createQuery(
                    "SELECT a FROM Agent a WHERE a.refId = :ref AND a.nodeId = :nodeId AND a.agentId = :id").setParameter("ref", ref).setParameter("nodeId", node).setParameter("id", id);
            agentProcessQry.setMaxResults(1);
            // Enable forced database query
            agentProcessQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            a = (Agent) agentProcessQry.getSingleResult();

        } catch (Exception e) {

        }

        return a;
    }

    public List<Agent> findAliases(String ref, String node) {

        List<Agent> l = null;

        try {
            em.flush();

            Query listQry = em.createQuery(
                    "SELECT a FROM Agent a WHERE a.refId = :ref AND a.nodeId = :node AND (a.ipLinked != :indef OR a.hostLinked != :indef OR a.containerLinked != :indef)").setParameter("ref", ref).setParameter("node", node).setParameter("indef", "indef");

            // Enable forced database query
            listQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            l = (List<Agent>) listQry.getResultList();

            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully Authenticated", ""));
        } catch (Exception e) {

            l = new ArrayList();

        }

        return l;
    }

    public String findAgentByIP(String ref, String node, String ip) {

        String a = null;

        try {
            em.flush();

            Query agentProcessQry = em.createQuery(
                    "SELECT a.name FROM Agent a WHERE a.refId = :ref AND a.nodeId = :node AND (a.ipLinked = :ip OR a.ip = :ip)").setParameter("ref", ref).setParameter("node", node).setParameter("ip", ip);
            agentProcessQry.setMaxResults(1);
            // Enable forced database query
            agentProcessQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            a = (String) agentProcessQry.getSingleResult();

        } catch (Exception e) {
            return "";
        }

        return a;

    }

}
