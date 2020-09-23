/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alertflex.mc.services;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.CacheRetrieveMode;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.alertflex.mc.db.Sensor;

/**
 *
 * @author root
 */
@Stateless
public class SensorFacade extends AbstractFacade<Sensor> {

    @PersistenceContext(unitName = "alertflex_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SensorFacade() {
        super(Sensor.class);
    }
    
    public List<Sensor> findSensorByNode(String ref, String node) {
        
        List<Sensor> l = null;
        
        try {
            em.flush();
            
            Query listQry = em.createQuery(
                "SELECT s FROM Sensor s WHERE s.sensorPK.masterNode = :node AND s.sensorPK.refId = :ref").setParameter("ref", ref).setParameter("node", node);
        
            // Enable forced database query
            listQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            l =  (List<Sensor>) listQry.getResultList();
            
            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully Authenticated", ""));
        } catch (Exception e) {

            
        }
        
        return l;
       
    }
    
    public List<String> findSensorNameByNode(String ref, String node) {
        
        List<String> l = null;
        
        try {
            em.flush();
            
            Query listQry = em.createQuery(
                "SELECT s.sensorPK.name FROM Sensor s WHERE s.sensorPK.masterNode = :node AND s.sensorPK.refId = :ref").setParameter("ref", ref).setParameter("node", node);
        
            // Enable forced database query
            listQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            l =  (List<String>) listQry.getResultList();
            
            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully Authenticated", ""));
        } catch (Exception e) {

            
        }
        
        return l;
       
    }
    
    public List<String> findSuricataByNode(String ref, String node) {
        
        List<String> l = null;
        
        try {
            em.flush();
            
            Query listQry = em.createQuery(
                "SELECT s.sensorPK.name FROM Sensor s WHERE s.sensorPK.masterNode = :node AND s.sensorPK.refId = :ref AND s.sensorType = :type")
                    .setParameter("ref", ref)
                    .setParameter("node", node)
                    .setParameter("type", "suricata");
        
            // Enable forced database query
            listQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            l =  (List<String>) listQry.getResultList();
            
            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully Authenticated", ""));
        } catch (Exception e) {

            
        }
        
        return l;
       
    }
    
    public Sensor findSensorByName(String ref, String node, String name) {
        
        Sensor s = null;
        
        try {
            em.flush();
            
            Query listQry = em.createQuery(
                "SELECT s FROM Sensor s WHERE s.sensorPK.masterNode = :node AND s.sensorPK.refId = :ref AND s.sensorPK.name = :name").setParameter("ref", ref).setParameter("node", node).setParameter("name", name);
        
            // Enable forced database query
            listQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            s =  (Sensor) listQry.getSingleResult();
            
            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully Authenticated", ""));
        } catch (Exception e) {

            
        }
        
        return s;
       
    }
    
}
