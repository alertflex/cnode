/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alertflex.controller;


import org.alertflex.common.ProjectRepository;
import org.alertflex.entity.Agent;
import org.json.JSONException;
import org.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author root
 */
public class AgentsManagement {
    
    private static final Logger logger = LoggerFactory.getLogger(AgentsManagement.class);
    
    private InfoMessageBean eventBean;
    String ref;
    String node;
    
    ProjectRepository pr;
        
        
    public AgentsManagement (InfoMessageBean eb) {
        this.eventBean = eb;
        this.ref = eb.getRefId();
        this.node = eb.getNode();
    }
    
    public void saveAgentKey(String agent, String json) {
        
        try {
            
            JSONObject obj = new JSONObject(json);
            
            int error = obj.getInt("error");
            
            if (error != 0) return;
            
            JSONObject data = obj.getJSONObject("data");
            
            String id = data.getString("id");
            String key = data.getString("key");
            
            Agent a = eventBean.getAgentFacade().findAgentByName(ref, node, agent);
            
            if (a != null) {
                a.setAgentId(id);
                a.setAgentKey(key);
                eventBean.getAgentFacade().edit(a);
            }
            
        } catch (JSONException e) {
            logger.error(json);
            logger.error("alertflex_ctrl_exception", e);
        }
            
    }
        
}
