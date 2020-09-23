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


public class JobValue {
    
    String projectRef = "";
    
    String jobName = "";
    
    String jobType = "";
    
    Integer timerange = 0;
    
    Long timerCounter = 0L;
        
    public JobValue(String project, String job, String type, Integer timer) {
        
        this.projectRef = project;
        this.jobName = job;
        this.jobType = type;
        this.timerange = timer;
    }
    
    public Boolean checkValue(String project, String job, String type) {
        
        if (!this.projectRef.equals(project)) return false;
        
        if (!this.jobName.equals(job)) return false;
        
        if (!this.jobType.equals(type)) return false;
        
        return true;
    }
    
    public void updateTimerange(Integer t) {
        this.timerange = t;
    }
    
    public boolean CheckTimerCounter() {
        
        if (timerange == 0) return false;
        
        if (timerCounter < timerange) {
            timerCounter++;
            return false;
        }
        
        timerCounter = 0L;
        return true;
    }
}

