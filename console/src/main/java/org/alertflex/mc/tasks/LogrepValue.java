/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alertflex.mc.tasks;

import org.alertflex.mc.db.Project;

/**
 *
 * @author root
 */


public class LogrepValue {
    
    String project = "";
    
    Integer logrepTimerange = 0;
    
    Long timerCounter = 0L;
    Integer projectTimer = 0;
    
    public LogrepValue(Project p) {
        
        this.project = p.getRefId();
        this.logrepTimerange = p.getIprepTimerange();
        
    }
    
    public void updateTimerange(Integer t) {
        this.logrepTimerange = t;
    }
    
    public boolean CheckTimerCounter() {
        
        if (projectTimer == 0) return false;
        
        if (timerCounter < projectTimer) {
            timerCounter++;
            return false;
        }
        
        timerCounter = 0L;
        projectTimer = logrepTimerange;
        return true;
    }
}

