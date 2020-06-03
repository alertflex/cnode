/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alertflex.mc.services;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.CacheRetrieveMode;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.alertflex.mc.db.Agent;

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
    
    public Agent findAgentByName(String ref, String name) {
        
        Agent a;
        
        try {
            em.flush();
            
            Query listQry = em.createQuery(
                "SELECT a FROM Agent a WHERE a.refId = :ref AND a.name = :name").setParameter("ref", ref).setParameter("name", name);
        
            // Enable forced database query
            listQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            a =  (Agent) listQry.getSingleResult();
            
            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully Authenticated", ""));
        } catch (Exception e) {

            return null;
        }
        
        return a;
       
    }
    
    public String findIdByName(String ref ,String node, String name) {
        
        String id = "";
        
        try {
            em.flush();
            
            Query listQry = em.createQuery(
                "SELECT a.agentId FROM Agent a WHERE a.refId = :ref AND a.nodeId = :node AND a.name = :name").setParameter("ref", ref).setParameter("node", node).setParameter("name", name);
        
            // Enable forced database query
            listQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            id =  (String) listQry.getSingleResult();
            
            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully Authenticated", ""));
        } catch (Exception e) {

            return "";
        }
        
        return id;
       
    }
    
    public List<Agent> findAgentsByNode(String ref, String node) {
        
        List<Agent> l = null;
        
        try {
            em.flush();
            
            Query listQry = em.createQuery(
                "SELECT a FROM Agent a WHERE a.refId = :ref AND a.nodeId = :node").setParameter("ref", ref).setParameter("node", node);
        
            // Enable forced database query
            listQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            l =  (List<Agent>) listQry.getResultList();
            
            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully Authenticated", ""));
        } catch (Exception e) {

            
        }
        
        return l;
       
    }
    
    public List<String> findAgentNamesByNode(String ref, String node) {
        
        List<String> l = null;
        
        try {
            em.flush();
            
            Query listQry = em.createQuery(
                "SELECT a.name FROM Agent a WHERE a.refId = :ref AND a.nodeId = :node").setParameter("ref", ref).setParameter("node", node);
        
            // Enable forced database query
            listQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            l =  (List<String>) listQry.getResultList();
            
            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully Authenticated", ""));
        } catch (Exception e) {

            
        }
        
        return l;
       
    }
    
    public List<Agent> findAliases(String ref, String node) {
        
        List<Agent> l = null;
        
        try {
            em.flush();
            
            Query listQry = em.createQuery(
                "SELECT a FROM Agent a WHERE a.refId = :ref AND a.nodeId = :node AND (a.ipLinked != :indef OR a.hostLinked != :indef OR a.containerLinked != :indef)").setParameter("ref", ref).setParameter("node", node).setParameter("indef", "indef");
        
            // Enable forced database query
            listQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            l =  (List<Agent>) listQry.getResultList();
            
            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully Authenticated", ""));
        } catch (Exception e) {
            
            l = new ArrayList();
            
        }
        
        return l;
       
    }
    
    public List<Agent> findAgentByParameters(String ref, String node, String name) {
        
        List<Agent> l = null;
        
        try {
            em.flush();
            
            Query listQry = em.createQuery(
                "SELECT a FROM Agent a WHERE a.refId = :ref AND a.nodeId = :node AND a.name = :name").setParameter("ref", ref).setParameter("node", node).setParameter("name", name);
        
            // Enable forced database query
            listQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            l =  (List<Agent>) listQry.getResultList();
            
            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully Authenticated", ""));
        } catch (Exception e) {
            
            l = new ArrayList();
            
        }
        
        return l;
       
    }
    
    public List<Agent> findAliasesParameters(String ref, String node, String ip, String host) {
        
        List<Agent> l = null;
        
        try {
            em.flush();
            
            Query listQry = em.createQuery(
                "SELECT a FROM Agent a WHERE a.refId = :ref AND a.nodeId = :node AND ((a.ipLinked = :ip AND a.ipLinked != :indef) OR (a.hostLinked = :hostname AND a.hostLinked != :indef))").setParameter("ref", ref).setParameter("node", node).setParameter("ip", ip).setParameter("hostname", host).setParameter("indef", "indef");
        
            // Enable forced database query
            listQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            l =  (List<Agent>) listQry.getResultList();
            
            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully Authenticated", ""));
        } catch (Exception e) {
            
            l = new ArrayList();
            
        }
        
        return l;
       
    }
    
}
