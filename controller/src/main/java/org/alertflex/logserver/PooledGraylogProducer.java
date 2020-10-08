/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alertflex.logserver;

/**
 *
 * @author root
 */

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.ejb.Startup;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import org.alertflex.entity.Project;
import org.alertflex.facade.ProjectFacade;

@ApplicationScoped
@Startup
public class PooledGraylogProducer {
    
    @EJB
    private ProjectFacade projectFacade;
    List<Project> projectList;
    private Project prj = null;
    
    DatagramSocket socket = null;
    String logHost = null;
    InetAddress iaHost = null;
    int logPort = 0;
    
    private GrayLog grayLog = null;

    @PostConstruct
    public void initPool()  {
        
        try {
            
            projectList = projectFacade.findAll();
            
            if(projectList == null || projectList.isEmpty()) return;
            
            prj = projectList.get(0);
            
            if(prj == null) return;
            
            logHost = prj.getLogHost();
            logPort = prj.getLogPort();

            if (logHost != null && !logHost.isEmpty() && !logHost.equals("indef")) {

                iaHost = InetAddress.getByName(logHost);
                socket = new DatagramSocket();
                                
                grayLog = new GrayLog(socket, iaHost, logPort);
            }
        
        } catch (UnknownHostException | SocketException e) {
            
            
        }
        
    }
    
    
    @Produces
    @FromGraylogPool
    public  GrayLog get() {
        
        if (grayLog != null) {
            return grayLog;
        }
        
        return null;
    }

    @PreDestroy
    public void close() {
        if (grayLog != null) {
            try {
                socket.close();
            } catch (Exception e) {
                grayLog = null;
            }
        }
    }
}

