/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alertflex.mc.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.CacheRetrieveMode;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.alertflex.mc.db.Alert;

/**
 *
 * @author root
 */
@Stateless
public class AlertFacade extends AbstractFacade<Alert> {
    @PersistenceContext(unitName = "afevents_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AlertFacade() {
        super(Alert.class);
    }
    
    public Alert findAlert(String r, Long alertId) {
        
        Alert al;
        
        try {
            em.flush();
            
            Query alertsListQry = em.createQuery(
                    "SELECT a FROM Alert a WHERE a.refId = :ref AND a.alertId = :alertId").setParameter("ref", r).setParameter("alertId", alertId);
            
            
        // Enable forced database query
            alertsListQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            al =  (Alert) alertsListQry.getSingleResult();

            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully Authenticated", ""));
        } catch (Exception e) {

            al = null;
        }
        
        return al;
       
    }
    
    public Alert findAlertsByUuid(String r, String uuid) {
        
        Alert al;
        
        try {
            em.flush();
            
            Query alertsListQry = em.createQuery(
                    "SELECT a FROM Alert a WHERE a.refId = :ref AND a.alertUuid = :uuid").setParameter("ref", r).setParameter("uuid", uuid);
            
            
        // Enable forced database query
            alertsListQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            al =  (Alert) alertsListQry.getSingleResult();

            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully Authenticated", ""));
        } catch (Exception e) {

            al = null;
        }
        
        return al;
       
    }
    
       
    public Long findStatus(String r, String s) {
        
        Long result = 0L;
        
        try {
            em.flush();
            
            Query alertsListQry = em.createQuery("SELECT count(a.status) FROM Alert a WHERE a.refId = :ref AND a.status = :status").setParameter("ref", r).setParameter("status", s);
            // Enable forced database query
            alertsListQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            result =  (Long) alertsListQry.getSingleResult();

            
        } catch (Exception e) {

            result = 0L;
        }
        
        return result;
       
    }
    
    public List findSeveritiesByNodes(String r, Date start, Date end) {
        
        List l = new ArrayList();
        
        try {
            em.flush();
            
            Query listQry = em.createQuery("SELECT a.nodeId, a.alertSeverity, count(a.nodeId) as Counter FROM Alert a WHERE a.refId = :ref AND a.timeCollr BETWEEN :start AND :end GROUP BY a.nodeId, a.alertSeverity ORDER BY a.nodeId, Counter DESC").setParameter("ref", r).setParameter("start", start).setParameter("end", end);
            // Enable forced database query
            listQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            l =  listQry.getResultList();

            
        } catch (Exception e) {

            l = new ArrayList();
        }
        
        return l;
       
    }
    
    public List findTypeByNodes(String r, Date start, Date end) {
        
        List l = new ArrayList();
        
        try {
            em.flush();
            // SELECT probe_id, priority, count(probe_id) as Sum FROM alerts group by probe_id, priority order by Sum;
            Query listQry = em.createQuery("SELECT a.nodeId, a.alertType, count(a.nodeId) as Counter FROM Alert a WHERE a.refId = :ref AND a.timeCollr BETWEEN :start AND :end GROUP BY a.nodeId, a.alertType ORDER BY a.nodeId, Counter DESC").setParameter("ref", r).setParameter("start", start).setParameter("end", end);
            // Enable forced database query
            listQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            l =  listQry.getResultList();

            
        } catch (Exception e) {

            l = new ArrayList();
        }
        
        return l;
       
    }
    
    
    public List<Alert> findIntervalsBetween(String r, Date start, Date end) {
        
        List<Alert> al = null;
        
        try {
            em.flush();
            
            Query alertsListQry = em.createQuery(
                    "SELECT a FROM Alert a WHERE a.refId = :ref AND a.timeCollr BETWEEN :start AND :end")
                    .setParameter("ref", r).setParameter("start", start).setParameter("end", end);
            
            
        // Enable forced database query
            alertsListQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            al =  alertsListQry.getResultList();

        } catch (Exception e) {

        }
        
        return al;
       
    }
    
    public List<Alert> findSeverityIntervalsBetween(String r, int sev, Date start, Date end) {
        
        List<Alert> al = null;
        
        try {
            em.flush();
            
            Query alertsListQry = em.createQuery(
                    "SELECT a FROM Alert a WHERE a.refId = :ref AND a.alertSeverity = :sev AND a.timeCollr BETWEEN :start AND :end")
                    .setParameter("ref", r).setParameter("sev", sev).setParameter("start", start).setParameter("end", end);
            
            
        // Enable forced database query
            alertsListQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            al =  alertsListQry.getResultList();

        } catch (Exception e) {

        }
        
        return al;
       
    }
    
    public List<Alert> findTypeIntervalsBetween(String r, String type, Date start, Date end) {
        
        List<Alert> al = null;
        
        try {
            em.flush();
            
            Query alertsListQry = em.createQuery(
                    "SELECT a FROM Alert a WHERE a.refId = :ref AND a.alertType = :type AND a.timeCollr BETWEEN :start AND :end")
                    .setParameter("ref", r).setParameter("type", type).setParameter("start", start).setParameter("end", end);
            
            
        // Enable forced database query
            alertsListQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            al =  alertsListQry.getResultList();

        } catch (Exception e) {

        }
        
        return al;
       
    }
    
    public List<Alert> findAlertsByStatus(String r, String[] statusArray, int limit) {
        
        List<String> statusList = Arrays.asList(statusArray);  
        List<Alert> al = null;
        
        try {
            em.flush();
            
            Query alertsListQry = em.createQuery(
                    "SELECT a FROM Alert a WHERE a.refId = :ref AND a.status IN ?1").setParameter("ref", r);
            alertsListQry.setParameter(1, statusList);
            // Enable forced database query
            //alertsListQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            
            if (limit != 0) al =  alertsListQry.setMaxResults(limit).getResultList();
            else al = alertsListQry.getResultList();
            
        } catch (Exception e) {

            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid query to DB", ""));
        }
        
        return al;
       
    }
    
    public void confirmAllAlerts(String r, String[] statusArray) {
        
        List<String> statusList = Arrays.asList(statusArray);  
        
        try {
            em.flush();
            Query alertsUpdate = em.createQuery(
                    "UPDATE Alert a SET a.status = :new_status WHERE a.refId = :ref AND a.status IN ?1")
                        .setParameter("ref", r)
                        .setParameter("new_status", "confirmed");
            alertsUpdate.setParameter(1, statusList);
            
                        
        // Enable forced database query
            int i = alertsUpdate.executeUpdate();

        } catch (Exception e) {

        }
        
    }
    
    public void confirmAllCriticalAlerts(String r, String[] statusArray) {
        
        List<String> statusList = Arrays.asList(statusArray);  
        
        try {
            em.flush();
            Query alertsUpdate = em.createQuery(
                    "UPDATE Alert a SET a.status = :new_status WHERE a.refId = :ref AND a.alertSeverity = :sev AND a.status IN ?1")
                        .setParameter("ref", r)
                        .setParameter("sev", 3)
                        .setParameter("new_status", "confirmed");
            alertsUpdate.setParameter(1, statusList);
            
                        
        // Enable forced database query
            int i = alertsUpdate.executeUpdate();

        } catch (Exception e) {

        }
        
    }
    
    public void confirmAllMajorAlerts(String r, String[] statusArray) {
        
        List<String> statusList = Arrays.asList(statusArray);  
        
        try {
            em.flush();
            Query alertsUpdate = em.createQuery(
                    "UPDATE Alert a SET a.status = :new_status WHERE a.refId = :ref AND a.alertSeverity = :sev AND a.status IN ?1")
                        .setParameter("ref", r)
                        .setParameter("sev", 2)
                        .setParameter("new_status", "confirmed");
            alertsUpdate.setParameter(1, statusList);
            
                        
        // Enable forced database query
            int i = alertsUpdate.executeUpdate();

        } catch (Exception e) {

        }
        
    }
    
    public void confirmAllMinorAlerts(String r, String[] statusArray) {
        
        List<String> statusList = Arrays.asList(statusArray);  
        
        try {
            em.flush();
            Query alertsUpdate = em.createQuery(
                    "UPDATE Alert a SET a.status = :new_status WHERE a.refId = :ref AND a.alertSeverity = :sev AND a.status IN ?1")
                        .setParameter("ref", r)
                        .setParameter("sev", 1)
                        .setParameter("new_status", "confirmed");
            alertsUpdate.setParameter(1, statusList);
            
                        
        // Enable forced database query
            int i = alertsUpdate.executeUpdate();

        } catch (Exception e) {

        }
        
    }
    
    public void confirmAllNormalAlerts(String r, String[] statusArray) {
        
        List<String> statusList = Arrays.asList(statusArray);  
        
        try {
            em.flush();
            Query alertsUpdate = em.createQuery(
                    "UPDATE Alert a SET a.status = :new_status WHERE a.refId = :ref AND a.alertSeverity = :sev AND a.status IN ?1")
                        .setParameter("ref", r)
                        .setParameter("sev", 0)
                        .setParameter("new_status", "confirmed");
            alertsUpdate.setParameter(1, statusList);
            
                        
        // Enable forced database query
            int i = alertsUpdate.executeUpdate();

        } catch (Exception e) {

        }
        
    }
    
    public Long findNumAlertsByStatus(String r, String[] statusArray) {
        
        List<String> statusList = Arrays.asList(statusArray);  
        Long res;
        
        try {
            em.flush();
            
            Query alertsNumQry = em.createQuery(
                    "SELECT COUNT(a.status) FROM Alert a WHERE a.refId = :ref AND a.status IN ?1").setParameter("ref", r);
            alertsNumQry.setParameter(1, statusList);
            // Enable forced database query
            //alertsNumQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            
            res = (Long) alertsNumQry.getSingleResult();
            
        } catch (Exception e) {

            res = 0L;
        }
        
        return res;
       
    }
    
    public List<Alert> findIntervalsByStatusAlerts(String r, String[] statusArray, Date start, Date end) {
        
        List<Alert> alerts = null;
        
        List<String> statusList = Arrays.asList(statusArray);  
                
        if (!statusList.isEmpty()) {
        
            try {
                em.flush();
                
                Query alertsListQry = em.createQuery("SELECT a FROM Alert a WHERE a.status IN ?1 AND a.timeCollr BETWEEN :start AND :end AND a.refId = :ref").setParameter("start", start).setParameter("end", end).setParameter("ref", r);
                alertsListQry.setParameter(1, statusList);
            
            
                // Enable forced database query
                alertsListQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
                alerts =  alertsListQry.getResultList();
            } catch (Exception e) {

            }
        }
        return alerts;
       
    }
    
    
    
    public List<Alert> findIntervalsByProbesArray(String r, String[] nodesArray, Date start, Date end) {
        
        List<Alert> alerts = null;
        
        List<String> nodes = Arrays.asList(nodesArray);  
                
        if (!nodes.isEmpty()) {
        
            try {
                em.flush();
                
                Query alertsListQry = em.createQuery("SELECT a FROM Alert a WHERE a.nodeId IN ?1 AND a.timeCollr BETWEEN :start AND :end AND a.refId = :ref").setParameter("start", start).setParameter("end", end).setParameter("ref", r);
                alertsListQry.setParameter(1, nodes);
            
            
                // Enable forced database query
                alertsListQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
                alerts =  alertsListQry.getResultList();
            } catch (Exception e) {

            }
        }
        return alerts;
       
    }
    
    public List<Alert> findAlertsByTypeStatus(String r, String t, String[] statusArray) {
        
        List<Alert> al = null;
        List<String> statusList = Arrays.asList(statusArray);  
        
        try {
            em.flush();
            
            Query alertsListQry = em.createQuery(
                    "SELECT a FROM Alert a WHERE a.refId = :ref AND a.type = :type AND a.status IN ?1").setParameter("ref", r).setParameter("type", t);
            alertsListQry.setParameter(1, statusList);
            // Enable forced database query
            alertsListQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            al =  alertsListQry.getResultList();

            
        } catch (Exception e) {

        }
        
        return al;
       
    }
    
    public List<Alert> findAlertsBySeverityStatus(String r, Integer s, String[] statusArray) {
        
        List<Alert> al = null;
        List<String> statusList = Arrays.asList(statusArray);  
        
        try {
            em.flush();
            
            Query alertsListQry = em.createQuery(
                    "SELECT a FROM Alert a WHERE a.refId = :ref AND a.alertSeverity = :sev AND a.status IN ?1")
                    .setParameter("ref", r)
                    .setParameter("sev", s);
            alertsListQry.setParameter(1, statusList);
            // Enable forced database query
            alertsListQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            al =  alertsListQry.getResultList();

            
        } catch (Exception e) {
         
        }
        
        return al;
       
    }
    
    public Long counterAlertsBySeverityStatus(String r, Integer s, String[] statusArray) {
        
        Long result = 0L;
        List<String> statusList = Arrays.asList(statusArray);  
        
        try {
            em.flush();
            
            Query alertsListQry = em.createQuery(
                    "SELECT count(a.status) FROM Alert a WHERE a.refId = :ref AND a.alertSeverity = :sev AND a.status IN ?1")
                    .setParameter("ref", r)
                    .setParameter("sev", s);
            alertsListQry.setParameter(1, statusList);
            // Enable forced database query
            alertsListQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            result = (Long) alertsListQry.getSingleResult();

            
        } catch (Exception e) {
            return 0L;
        }
        
        return result;
       
    }
    
    public List<Alert> findAlertsForHids(String r, String n, String a, Date start, Date end) {
        
        List<Alert> alerts = null;
        
        try {
            em.flush();
        
            if (n.equals("*")) {
                Query alertsListQry = em.createQuery("SELECT a FROM Alert a WHERE a.refId = :ref AND a.alertType = :type AND a.alertSource = :source AND a.timeCollr BETWEEN :start AND :end")
                    .setParameter("ref", r)
                    .setParameter("type", "HOST")
                    .setParameter("source", "Wazuh")
                    .setParameter("start", start)
                    .setParameter("end", end);
                    
                // Enable forced database query
                alertsListQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
                alerts =  alertsListQry.getResultList();
                return alerts;
            
            } else {
                
                if (a.equals("*")) {
                    
                    Query alertsListQry = em.createQuery("SELECT a FROM Alert a WHERE a.refId = :ref AND a.nodeId = :node AND a.alertType = :type AND a.alertSource = :source AND a.timeCollr BETWEEN :start AND :end")
                        .setParameter("ref", r)
                        .setParameter("node", n)
                        .setParameter("type", "HOST")
                        .setParameter("source", "Wazuh")
                        .setParameter("start", start)
                        .setParameter("end", end);
                    
                    // Enable forced database query
                    alertsListQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
                    alerts =  alertsListQry.getResultList();
                    return alerts;
                
                }
            } 
            
            Query alertsListQry = em.createQuery("SELECT a FROM Alert a WHERE a.refId = :ref AND a.nodeId = :node AND a.dstHostname = :agent AND a.alertType = :type AND a.alertSource = :source AND a.timeCollr BETWEEN :start AND :end")
                .setParameter("ref", r)
                .setParameter("node", n)
                .setParameter("agent", a)
                .setParameter("type", "HOST")
                .setParameter("source", "Wazuh")
                .setParameter("start", start)
                .setParameter("end", end);
                    
            // Enable forced database query
            alertsListQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            alerts =  alertsListQry.getResultList();
                   
            
        } catch (Exception e) {

        }
        
        return alerts;
       
    }
    
    public List<Alert> findAlertsForNids(String r, String n, String a, Date start, Date end) {
        
        List<Alert> alerts = null;
        
        try {
            em.flush();
        
            if (n.equals("*")) {
                Query alertsListQry = em.createQuery("SELECT a FROM Alert a WHERE a.refId = :ref AND a.alertType = :type AND a.alertSource = :source AND a.timeCollr BETWEEN :start AND :end")
                    .setParameter("ref", r)
                    .setParameter("type", "NET")
                    .setParameter("source", "Suricata")
                    .setParameter("start", start)
                    .setParameter("end", end);
                    
                // Enable forced database query
                alertsListQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
                alerts =  alertsListQry.getResultList();
                return alerts;
            
            } else {
                
                if (a.equals("*")) {
                    
                    Query alertsListQry = em.createQuery("SELECT a FROM Alert a WHERE a.refId = :ref AND a.nodeId = :node AND a.alertType = :type AND a.alertSource = :source AND a.timeCollr BETWEEN :start AND :end")
                        .setParameter("ref", r)
                        .setParameter("node", n)
                        .setParameter("type", "NET")
                        .setParameter("source", "Suricata")
                        .setParameter("start", start)
                        .setParameter("end", end);
                    
                    // Enable forced database query
                    alertsListQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
                    alerts =  alertsListQry.getResultList();
                    return alerts;
                
                }
            } 
            
            Query alertsListQry = em.createQuery("SELECT a FROM Alert a WHERE a.refId = :ref AND a.nodeId = :node AND a.dstHostname = :agent AND a.alertType = :type AND a.alertSource = :source AND a.timeCollr BETWEEN :start AND :end")
                .setParameter("ref", r)
                .setParameter("node", n)
                .setParameter("agent", a)
                .setParameter("type", "NET")
                .setParameter("source", "Suricata")
                .setParameter("start", start)
                .setParameter("end", end);
                    
            // Enable forced database query
            alertsListQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            alerts =  alertsListQry.getResultList();
                   
            
        } catch (Exception e) {

        }
        
        return alerts;
       
    }
    
    public List<Alert> findAlertsForFim(String r, String n, String a, Date start, Date end) {
        
        List<Alert> alerts = null;
        
        try {
            em.flush();
        
            if (n.equals("*")) {
                Query alertsListQry = em.createQuery("SELECT a FROM Alert a WHERE a.refId = :ref AND a.alertType = :type AND a.alertSource = :source AND a.timeCollr BETWEEN :start AND :end")
                    .setParameter("ref", r)
                    .setParameter("type", "FILE")
                    .setParameter("source", "Wazuh")
                    .setParameter("start", start)
                    .setParameter("end", end);
                    
                // Enable forced database query
                alertsListQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
                alerts =  alertsListQry.getResultList();
                return alerts;
            
            } else {
                
                if (a.equals("*")) {
                    
                    Query alertsListQry = em.createQuery("SELECT a FROM Alert a WHERE a.refId = :ref AND a.nodeId = :node AND a.alertType = :type AND a.alertSource = :source AND a.timeCollr BETWEEN :start AND :end")
                        .setParameter("ref", r)
                        .setParameter("node", n)
                        .setParameter("type", "FILE")
                        .setParameter("source", "Wazuh")
                        .setParameter("start", start)
                        .setParameter("end", end);
                    
                    // Enable forced database query
                    alertsListQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
                    alerts =  alertsListQry.getResultList();
                    return alerts;
                
                }
            } 
            
            Query alertsListQry = em.createQuery("SELECT a FROM Alert a WHERE a.refId = :ref AND a.nodeId = :node AND a.dstHostname = :agent AND a.alertType = :type AND a.alertSource = :source AND a.timeCollr BETWEEN :start AND :end")
                .setParameter("ref", r)
                .setParameter("node", n)
                .setParameter("agent", a)
                .setParameter("type", "FILE")
                .setParameter("source", "Wazuh")
                .setParameter("start", start)
                .setParameter("end", end);
                    
            // Enable forced database query
            alertsListQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            alerts =  alertsListQry.getResultList();
                   
            
        } catch (Exception e) {

        }
        
        return alerts;
       
    }
    
    public List<Alert> findAlertsForWaf(String r, String n, String a, Date start, Date end) {
        
        List<Alert> alerts = null;
        
        try {
            em.flush();
        
            if (n.equals("*")) {
                Query alertsListQry = em.createQuery("SELECT a FROM Alert a WHERE a.refId = :ref AND a.alertType = :type AND a.alertSource = :source AND a.timeCollr BETWEEN :start AND :end")
                    .setParameter("ref", r)
                    .setParameter("type", "NET")
                    .setParameter("source", "Modsecurity")
                    .setParameter("start", start)
                    .setParameter("end", end);
                    
                // Enable forced database query
                alertsListQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
                alerts =  alertsListQry.getResultList();
                return alerts;
            
            } else {
                
                if (a.equals("*")) {
                    
                    Query alertsListQry = em.createQuery("SELECT a FROM Alert a WHERE a.refId = :ref AND a.nodeId = :node AND a.alertType = :type AND a.alertSource = :source AND a.timeCollr BETWEEN :start AND :end")
                        .setParameter("ref", r)
                        .setParameter("node", n)
                        .setParameter("type", "NET")
                        .setParameter("source", "Modsecurity")
                        .setParameter("start", start)
                        .setParameter("end", end);
                    
                    // Enable forced database query
                    alertsListQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
                    alerts =  alertsListQry.getResultList();
                    return alerts;
                
                }
            } 
            
            Query alertsListQry = em.createQuery("SELECT a FROM Alert a WHERE a.refId = :ref AND a.nodeId = :node AND a.dstHostname = :agent AND a.alertType = :type AND a.alertSource = :source AND a.timeCollr BETWEEN :start AND :end")
                .setParameter("ref", r)
                .setParameter("node", n)
                .setParameter("agent", a)
                .setParameter("type", "NET")
                .setParameter("source", "Modsecurity")
                .setParameter("start", start)
                .setParameter("end", end);
                    
            // Enable forced database query
            alertsListQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            alerts =  alertsListQry.getResultList();
                   
            
        } catch (Exception e) {

        }
        
        return alerts;
       
    }
    
    public List<Alert> findAlertsForUsers(String r, String n, String a, Date start, Date end) {
        
        List<Alert> alerts = null;
        
        try {
            em.flush();
        
            if (n.equals("*")) {
                Query alertsListQry = em.createQuery("SELECT a FROM Alert a WHERE a.refId = :ref AND a.userName != '' AND a.userName is not null AND a.timeCollr BETWEEN :start AND :end")
                    .setParameter("ref", r)
                    .setParameter("start", start)
                    .setParameter("end", end);
                    
                // Enable forced database query
                alertsListQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
                alerts =  alertsListQry.getResultList();
                return alerts;
            
            } else {
                
                if (a.equals("*")) {
                    
                    Query alertsListQry = em.createQuery("SELECT a FROM Alert a WHERE a.refId = :ref AND a.nodeId = :node AND a.userName != '' AND a.userName is not null AND a.timeCollr BETWEEN :start AND :end")
                        .setParameter("ref", r)
                        .setParameter("node", n)
                        .setParameter("start", start)
                        .setParameter("end", end);
                    
                    // Enable forced database query
                    alertsListQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
                    alerts =  alertsListQry.getResultList();
                    return alerts;
                
                }
            } 
            
            Query alertsListQry = em.createQuery("SELECT a FROM Alert a WHERE a.refId = :ref AND a.nodeId = :node AND a.dstHostname = :agent AND a.userName != '' AND a.userName is not null AND a.timeCollr BETWEEN :start AND :end")
                .setParameter("ref", r)
                .setParameter("node", n)
                .setParameter("agent", a)
                .setParameter("start", start)
                .setParameter("end", end);
                    
            // Enable forced database query
            alertsListQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            alerts =  alertsListQry.getResultList();
                   
            
        } catch (Exception e) {

        }
        
        return alerts;
       
    }
    
    public List<Alert> findAlertsForPcidss(String r, String n, String a, Date start, Date end) {
        
        List<Alert> alerts = null;
        
        try {
            em.flush();
        
            if (n.equals("*")) {
                Query alertsListQry = em.createQuery("SELECT a FROM Alert a WHERE a.refId = :ref AND a.categories LIKE :cat AND a.timeCollr BETWEEN :start AND :end")
                    .setParameter("ref", r)
                    .setParameter("cat", "%pci_dss_%")
                    .setParameter("start", start)
                    .setParameter("end", end);
                    
                // Enable forced database query
                alertsListQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
                alerts =  alertsListQry.getResultList();
                return alerts;
            
            } else {
                
                if (a.equals("*")) {
                    
                    Query alertsListQry = em.createQuery("SELECT a FROM Alert a WHERE a.refId = :ref AND a.nodeId = :node AND a.categories LIKE :cat AND a.timeCollr BETWEEN :start AND :end")
                        .setParameter("ref", r)
                        .setParameter("node", n)
                        .setParameter("cat", "%pci_dss_%")
                        .setParameter("start", start)
                        .setParameter("end", end);
                    
                    // Enable forced database query
                    alertsListQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
                    alerts =  alertsListQry.getResultList();
                    return alerts;
                
                }
            } 
            
            Query alertsListQry = em.createQuery("SELECT a FROM Alert a WHERE a.refId = :ref AND a.nodeId = :node AND a.dstHostname = :agent AND a.categories LIKE :cat AND a.timeCollr BETWEEN :start AND :end")
                .setParameter("ref", r)
                .setParameter("node", n)
                .setParameter("agent", a)
                .setParameter("cat", "%pci_dss_%")
                .setParameter("start", start)
                .setParameter("end", end);
                    
            // Enable forced database query
            alertsListQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            alerts =  alertsListQry.getResultList();
                   
            
        } catch (Exception e) {

        }
        
        return alerts;
    }
    
    public List<Alert> findAlertsForGdpr(String r, String n, String a, Date start, Date end) {
        
        List<Alert> alerts = null;
        
        try {
            em.flush();
        
            if (n.equals("*")) {
                Query alertsListQry = em.createQuery("SELECT a FROM Alert a WHERE a.refId = :ref AND a.categories LIKE :cat AND a.timeCollr BETWEEN :start AND :end")
                    .setParameter("ref", r)
                    .setParameter("cat", "%gdpr_%")
                    .setParameter("start", start)
                    .setParameter("end", end);
                    
                // Enable forced database query
                alertsListQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
                alerts =  alertsListQry.getResultList();
                return alerts;
            
            } else {
                
                if (a.equals("*")) {
                    
                    Query alertsListQry = em.createQuery("SELECT a FROM Alert a WHERE a.refId = :ref AND a.nodeId = :node AND a.categories LIKE :cat AND a.timeCollr BETWEEN :start AND :end")
                        .setParameter("ref", r)
                        .setParameter("node", n)
                        .setParameter("cat", "%gdpr_%")
                        .setParameter("start", start)
                        .setParameter("end", end);
                    
                    // Enable forced database query
                    alertsListQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
                    alerts =  alertsListQry.getResultList();
                    return alerts;
                
                }
            } 
            
            Query alertsListQry = em.createQuery("SELECT a FROM Alert a WHERE a.refId = :ref AND a.nodeId = :node AND a.dstHostname = :agent AND a.categories LIKE :cat AND a.timeCollr BETWEEN :start AND :end")
                .setParameter("ref", r)
                .setParameter("node", n)
                .setParameter("agent", a)
                .setParameter("cat", "%gdpr_%")
                .setParameter("start", start)
                .setParameter("end", end);
                    
            // Enable forced database query
            alertsListQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            alerts =  alertsListQry.getResultList();
                   
            
        } catch (Exception e) {

                //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid query to DB", ""));
        }
        
        return alerts;
    }
    
    public List<Alert> findAlertsForHipaa(String r, String n, String a, Date start, Date end) {
        
        List<Alert> alerts = null;
        
        try {
            em.flush();
        
            if (n.equals("*")) {
                Query alertsListQry = em.createQuery("SELECT a FROM Alert a WHERE a.refId = :ref AND a.categories LIKE :cat AND a.timeCollr BETWEEN :start AND :end")
                    .setParameter("ref", r)
                    .setParameter("cat", "%hipaa_%")
                    .setParameter("start", start)
                    .setParameter("end", end);
                    
                // Enable forced database query
                alertsListQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
                alerts =  alertsListQry.getResultList();
                return alerts;
            
            } else {
                
                if (a.equals("*")) {
                    
                    Query alertsListQry = em.createQuery("SELECT a FROM Alert a WHERE a.refId = :ref AND a.nodeId = :node AND a.categories LIKE :cat AND a.timeCollr BETWEEN :start AND :end")
                        .setParameter("ref", r)
                        .setParameter("node", n)
                        .setParameter("cat", "%hipaa_%")
                        .setParameter("start", start)
                        .setParameter("end", end);
                    
                    // Enable forced database query
                    alertsListQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
                    alerts =  alertsListQry.getResultList();
                    return alerts;
                
                }
            } 
            
            Query alertsListQry = em.createQuery("SELECT a FROM Alert a WHERE a.refId = :ref AND a.nodeId = :node AND a.dstHostname = :agent AND a.categories LIKE :cat AND a.timeCollr BETWEEN :start AND :end")
                .setParameter("ref", r)
                .setParameter("node", n)
                .setParameter("agent", a)
                .setParameter("cat", "%hipaa_%")
                .setParameter("start", start)
                .setParameter("end", end);
                    
            // Enable forced database query
            alertsListQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            alerts =  alertsListQry.getResultList();
                   
            
        } catch (Exception e) {

        }
        
        return alerts;
    }
    
    public List<Alert> findAlertsForNist(String r, String n, String a, Date start, Date end) {
        
        List<Alert> alerts = null;
        
        try {
            em.flush();
        
            if (n.equals("*")) {
                Query alertsListQry = em.createQuery("SELECT a FROM Alert a WHERE a.refId = :ref AND a.categories LIKE :cat AND a.timeCollr BETWEEN :start AND :end")
                    .setParameter("ref", r)
                    .setParameter("cat", "%nist_%")
                    .setParameter("start", start)
                    .setParameter("end", end);
                    
                // Enable forced database query
                alertsListQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
                alerts =  alertsListQry.getResultList();
                return alerts;
            
            } else {
                
                if (a.equals("*")) {
                    
                    Query alertsListQry = em.createQuery("SELECT a FROM Alert a WHERE a.refId = :ref AND a.nodeId = :node AND a.categories LIKE :cat AND a.timeCollr BETWEEN :start AND :end")
                        .setParameter("ref", r)
                        .setParameter("node", n)
                        .setParameter("cat", "%nist_%")
                        .setParameter("start", start)
                        .setParameter("end", end);
                    
                    // Enable forced database query
                    alertsListQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
                    alerts =  alertsListQry.getResultList();
                    return alerts;
                
                }
            } 
            
            Query alertsListQry = em.createQuery("SELECT a FROM Alert a WHERE a.refId = :ref AND a.nodeId = :node AND a.dstHostname = :agent AND a.categories LIKE :cat AND a.timeCollr BETWEEN :start AND :end")
                .setParameter("ref", r)
                .setParameter("node", n)
                .setParameter("agent", a)
                .setParameter("cat", "%nist_%")
                .setParameter("start", start)
                .setParameter("end", end);
                    
            // Enable forced database query
            alertsListQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            alerts =  alertsListQry.getResultList();
                   
            
        } catch (Exception e) {

        }
        
        return alerts;
    }
    
    public Alert findAlertByUuid(String uuid) {
        
        Alert al;
        
        try {
            em.flush();
            
            Query alertsListQry = em.createQuery(
                    "SELECT a FROM Alert a WHERE a.alertUuid = :uuid").setParameter("uuid", uuid);
            
            
        // Enable forced database query
            alertsListQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            al =  (Alert) alertsListQry.getSingleResult();

        } catch (Exception e) {

            al = null;
        }
        
        return al;
       
    }
}