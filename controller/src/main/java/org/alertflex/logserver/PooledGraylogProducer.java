/*
 *   Copyright 2021 Oleg Zharkov
 *
 *   Licensed under the Apache License, Version 2.0 (the "License").
 *   You may not use this file except in compliance with the License.
 *   A copy of the License is located at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   or in the "license" file accompanying this file. This file is distributed
 *   on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 *   express or implied. See the License for the specific language governing
 *   permissions and limitations under the License.
 */

package org.alertflex.logserver;

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
            
            logHost = prj.getGraylogHost();
            logPort = prj.getGraylogPort();

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
