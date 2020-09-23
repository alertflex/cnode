/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alertflex.mc.tasks;

import java.util.ArrayList;
import java.util.List;
import org.alertflex.mc.db.Project;

/**
 *
 * @author root
 */


public class JobsManager {
    
    List<JobValue> jobValueList;
    
    public JobsManager() {
        jobValueList = new ArrayList<>();
    }
    
    public JobValue getProjectValue(String project, String job, String type, Integer timer) {
        
        for (JobValue jv: jobValueList) {
            if (jv.checkValue(project, job, type)) {
                
                jv.updateTimerange(timer);
                return jv;
            }
        }
        
        JobValue newValues = new JobValue(project, job, type, timer);
        jobValueList.add(newValues);
        
        return newValues;
    }
    
    public boolean checkTimerCounter(String project, String job, String type, Integer timer) {
        
        JobValue lv = getProjectValue(project, job, type, timer);
       
        return lv.CheckTimerCounter();
    }
}

