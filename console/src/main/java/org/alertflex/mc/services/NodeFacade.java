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
import org.alertflex.mc.db.Node;

/**
 *
 * @author root
 */
@Stateless
public class NodeFacade extends AbstractFacade<Node> {

    @PersistenceContext(unitName = "alertflex_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public NodeFacade() {
        super(Node.class);
    }
    
    public List<String> findAllNodeNames(String r) {
        
        List<String> listNodes = null;
        
        try {
            em.flush();
            
            Query listNodesQry = em.createQuery("SELECT n.nodePK.name FROM Node n WHERE n.nodePK.refId = :ref").setParameter("ref", r);
            
            
        // Enable forced database query
            listNodesQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            listNodes =  listNodesQry.getResultList();

            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully Authenticated", ""));
        } catch (Exception e) {

            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid query to DB", ""));
            return null;
        }
        
        return listNodes;
    }
    
    public List<Node> findAllNodes(String r) {
        
        List<Node> listNodes = null;
        
        try {
            em.flush();
            
            Query listNodesQry = em.createQuery("SELECT n FROM Node n WHERE n.nodePK.refId = :ref").setParameter("ref", r);
            
            
        // Enable forced database query
            listNodesQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            listNodes =  listNodesQry.getResultList();

            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully Authenticated", ""));
        } catch (Exception e) {

            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid query to DB", ""));
        }
        
        return listNodes;
    }
    
    public Node findByNodeName(String r, String n) {
        Node node = null;
        
        try {
            em.flush();
            
            Query listProbesQry = em.createQuery("SELECT n FROM Node n WHERE n.nodePK.refId = :ref AND n.nodePK.name = :node").setParameter("ref", r).setParameter("node", n);
            
            
        // Enable forced database query
            listProbesQry.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            node =  (Node) listProbesQry.getSingleResult();

            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully Authenticated", ""));
        } catch (Exception e) {
            return null;
        }
        
        return node;
    }
    
}
