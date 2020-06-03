/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alertflex.common;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.alertflex.entity.Project;

/**
 *
 * @author root
 */


public class IprepValue {
    
    String project = "";
    
    Integer IprepTimerange = 0;
    
    Long timerCounter = 0L;
    Long ipCounter = 0L;
    Integer projectTimer = 0;
    
    boolean isDirOpen = false;
    String projectListsDir;
    Path projectListsPath;
    
    public IprepValue(Project p) {
        
        this.project = p.getRefId();
        
        this.IprepTimerange = p.getIprepTimerange();
        
        openDir(p);
        
    }
    
    public void updateTimerange(Integer t) {
        this.IprepTimerange = t;
    }
    
    public Long getIpCounter() {
        return ipCounter;
    }
    
    public void setIpCounter(Long c) {
        this.ipCounter = c;
    }
    
    public boolean CheckTimerCounter() {
        
        if (!isDirOpen) return false; 
        
        if (projectTimer == 0) return false;
        
        if (timerCounter < projectTimer) {
            timerCounter++;
            return false;
        }
        
        timerCounter = 0L;
        projectTimer = IprepTimerange;
        return true;
    }
    
    boolean openDir(Project p) {
        
        if (isDirOpen) return true;
        
        String projectDir = p.getProjectPath();
        Path projectPath = Paths.get(projectDir);
        
        if (Files.notExists(projectPath)) {
            try { Files.createDirectory(projectPath); }
            catch (Exception e ) {  
                return false;
            }
        }
        
        projectDir = projectDir + "controller/";
        projectPath = Paths.get(projectDir);
        
        if (Files.notExists(projectPath)) {
            try { Files.createDirectory(projectPath); }
            catch (Exception e ) {  
                return false;
            }
        }
        
        projectListsDir = projectDir + "lists/";
        projectListsPath = Paths.get(projectDir);
        
        if (Files.notExists(projectListsPath)) {
            try { Files.createDirectory(projectListsPath); }
            catch (Exception e ) {  
                return false;
            }
        }
        
        isDirOpen = true;
        
        return true;
    }
    
    public boolean dirStatus() {
        return isDirOpen;
    }
    
    public String getProjectListsDir () {
        return projectListsDir;
    }

}

