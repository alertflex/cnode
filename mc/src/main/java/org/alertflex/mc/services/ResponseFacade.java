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
import org.alertflex.mc.db.Response;

/**
 *
 * @author root
 */
@Stateless
public class ResponseFacade extends AbstractFacade<Response> {

    @PersistenceContext(unitName = "alertflex_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ResponseFacade() {
        super(Response.class);
    }
    
    public List<Response> findResponseByTenant(String r) {
        
        List<Response> responseList = null;
        
        try {
            em.flush();
            
            Query responseListQry = em.createQuery(
                    "SELECT r FROM Response r WHERE r.refId = :refId").setParameter("refId", r);
        // Enable forced database query
             // Enable forced database query
                responseListQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.USE);
                responseList =  responseListQry.getResultList();
            } catch (Exception e) {

            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid quety to DB", ""));
        }
        
        return responseList;
       
    }
    
    public Response findResponseByName( String r, String name) {
        
        Response res = null;
        
        try {
            em.flush();
            
            Query responseQry = em.createQuery(
                    "SELECT r FROM Response r WHERE r.rpId = :name AND r.refId = :refId").setParameter("refId", r).setParameter("name", name);
        // Enable forced database query
            responseQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.USE);
            res =  (Response) responseQry.getSingleResult();

            
        } catch (Exception e) {

            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid query to DB", ""));
        }
        
        return res;
       
    }
    
    public List findResponseByActive(String ref) {
        
        List l = new ArrayList();
        
        try {
            em.flush();
            
            Query listQry = em.createQuery("SELECT r FROM Response r WHERE r.status = :status AND r.refId = :refId").setParameter("status", 1).setParameter("refId", ref);
            // Enable forced database query
            listQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.USE);
            l =  listQry.getResultList();

            
        } catch (Exception e) {

            l = new ArrayList();
        }
        
        return l;
    }
    
    public List findResponseForEvent(String ref) {
        
        List l = new ArrayList();
        
        try {
            em.flush();
            
            Query listQry = em.createQuery("SELECT r FROM Response r WHERE r.refId = :ref AND r.status = :status AND r.resType = :type").setParameter("ref", ref).setParameter("status", 1).setParameter("type", 0);
            // Enable forced database query
            listQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            l =  listQry.getResultList();

            
        } catch (Exception e) {

            l = new ArrayList();
        }
        
        return l;
    }
    
    public List findResponseForCat(String ref) {
        
        List l = new ArrayList();
        
        try {
            em.flush();
            
            Query listQry = em.createQuery("SELECT r FROM Response r WHERE r.refId = :ref AND r.status = :status AND r.resType = :type").setParameter("ref", ref).setParameter("status", 1).setParameter("type", 1);
            // Enable forced database query
            listQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            l =  listQry.getResultList();

            
        } catch (Exception e) {

            l = new ArrayList();
        }
        
        return l;
    }
    
    public List<Response> findResponseByNode(String r, String n) {
        
        List<Response> responseList = null;
        
        try {
            em.flush();
            
            Query responseListQry = em.createQuery(
                    "SELECT r FROM Response r WHERE r.refId = :refId AND r.node = :node")
                    .setParameter("refId", r)
                    .setParameter("node", n);
            
            // Enable forced database query
            responseListQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.USE);
            responseList =  responseListQry.getResultList();
        
        } catch (Exception e) {

            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid quety to DB", ""));
        }
        
        return responseList;
       
    }
    
}