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


public class LogrepProjects {
    
    List<LogrepValue> projectValueList;
    
    public LogrepProjects() {
        projectValueList = new ArrayList<>();
    }
    
    public LogrepValue getProjectValue(Project p) {
        
        for (LogrepValue lv: projectValueList) {
            if (lv.project.equals(p.getRefId())) {
                
                lv.updateTimerange(p.getLogrepTimerange());
                return lv;
            }
        }
        
        LogrepValue newValues = new LogrepValue(p);
        projectValueList.add(newValues);
        
        return newValues;
    }
    
    public boolean checkTimerCounter(Project p) {
        
        LogrepValue lv = getProjectValue(p);
       
        return lv.CheckTimerCounter();
    }
}

