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

package org.alertflex.posture;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.alertflex.controller.InfoMessageBean;
import org.alertflex.posture.nmap.NmapReport;
import org.alertflex.posture.nmap.NmapParser;
import org.alertflex.entity.Alert;
import org.alertflex.entity.AlertPriority;
import org.alertflex.entity.PostureNmap;
import org.alertflex.entity.Project;
import org.alertflex.entity.Node;
import org.alertflex.entity.PostureTask;
import org.alertflex.supp.ProjectRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.SAXException;

public class Nmap {

    private static final Logger logger = LogManager.getLogger(Nmap.class);

    private InfoMessageBean eventBean;
    Project project;
    Node node;

    public Nmap(InfoMessageBean eb) {
        this.eventBean = eb;
        this.project = eventBean.getProject();

    }

    public void saveReport(String results, String target, String uuid, String alertCorr, int alertType) {

        
            String r = eventBean.getRefId();
            String n = eventBean.getNode();
            String p = eventBean.getHost() + ".nmap";
            Date date = new Date();
            
        try {

            ProjectRepository pr = new ProjectRepository(project);
            String posturePath = pr.getCtrlPostureDir() + uuid + ".xml";
            Path pp = Paths.get(posturePath);
            Files.write(pp, results.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            
        } catch (Exception e) {
                
            return;
        }
        
        try {
            
            PostureTask pt = new PostureTask ();
                            
            pt.setRefId(r);
            pt.setNode(n);
            pt.setProbe(p);
            pt.setPostureType("Nmap");
            pt.setTarget(target);
            pt.setTaskUuid(uuid);
            pt.setReportAdded(date);
                
            eventBean.getPostureTaskFacade().create(pt);
                 
        } catch (Exception e) {
            
            return;
        }

            
        try {
    
            node = eventBean.getNodeFacade().findByNodeName(r, n);

            if (node == null || p == null || results.isEmpty()) {
                return;
            }
            
            AlertPriority ap = eventBean.getAlertPriorityFacade().findPriorityBySource(r, "Nmap");
            
            int alertSeverity = ap.getSeverityDefault();
            String severity = "low";
            
            switch (alertSeverity) {
                case 0 : severity = "info";
                    break;
                case 1 : severity = "low";
                    break;
                case 2 : severity = "medium";
                    break;
                case 3 : severity = "high";
                    break;
            }
            
            InputStream is = new ByteArrayInputStream(results.getBytes(StandardCharsets.UTF_8));
            
            List<NmapReport> listNmapReport = getResult(is);
            
            for (NmapReport nr: listNmapReport) {
                
                PostureNmap pn = new PostureNmap();
                
                int portId = Integer.parseInt(nr.getPortid());
                String portName = nr.getName();
                String protocol = nr.getProtocol();
                
                pn.setNode(n);
                pn.setRefId(r);
                pn.setProbe(p);
                pn.setScanUuid(uuid);
                pn.setTarget(target);
                pn.setPortId(portId);
                pn.setPortName(portName);
                pn.setProtocol(protocol);
                pn.setSeverity(severity);
                pn.setReportAdded(date);
                pn.setReportUpdated(date);
                pn.setStatus("processed");
                
                String alertUuid = "";
                                
                PostureNmap pnExisting = eventBean.getPostureNmapFacade().findVulnerability(r,n,p,target,portId);
                
                if (pnExisting == null) {
                    alertUuid = createPostureNmapAlert(pn, severity, alertSeverity);
                    if (alertUuid != null) pn.setAlertUuid(alertUuid);
                    else pn.setAlertUuid("indef");
                    eventBean.getPostureNmapFacade().create(pn);
                
                } else {
                    int corr = 0;
                        
                    switch (alertCorr) {
                        case "AllFindings": corr = 1;
                            break;
                        case "NonConfirmed": corr = 2;
                            break;
                        case "OnlyNew": corr = 3;
                            break;
                    }
                    
                    if (corr == 0) corr = alertType;
                    
                    switch (corr) {
                    
                        case 1: // all-existing
                        
                            alertUuid = createPostureNmapAlert(pn, severity, alertSeverity);
                            if (alertUuid != null) pnExisting.setAlertUuid(alertUuid);
                                                        
                            pnExisting.setReportUpdated(date);
                            eventBean.getPostureNmapFacade().edit(pnExisting);
                        
                            break;
                        
                        case 2: // non confirmed 
                        
                            if (!pnExisting.getStatus().equals("confirmed")) {
                                alertUuid = createPostureNmapAlert(pn, severity, alertSeverity);
                                if (alertUuid != null) pnExisting.setAlertUuid(alertUuid);
                            }
                            
                            pnExisting.setReportUpdated(date);
                            eventBean.getPostureNmapFacade().edit(pnExisting);
                            
                            break;
                        
                        case 3: // new
                        
                            pnExisting.setReportUpdated(date);
                            eventBean.getPostureNmapFacade().edit(pnExisting);
                            
                            break;
                    }
                }
            }
        } catch (Exception e) {
            logger.error("alertflex_ctrl_exception", e);
        }
    }
    
    public String createPostureNmapAlert(PostureNmap pn, String severity, int alertSeverity) {

        Alert a = new Alert();

        a.setNode(pn.getNode());
        a.setRefId(pn.getRefId());
        a.setAlertUuid(UUID.randomUUID().toString());

        a.setEventSeverity(severity);
        a.setAlertSeverity(alertSeverity);
        a.setEventId("1");
        
        a.setCategories("nmap");
        a.setAlertSource("Nmap");
        a.setAlertType("MISC");

        a.setProbe(pn.getProbe());
        a.setLocation(pn.getTarget());
        a.setAction("indef");
        a.setStatus("processed");
        a.setFilter("");
        
        a.setDescription("Nmap detected a problem with port: " + pn.getPortName() + " for host: " + pn.getTarget());
        
        a.setInfo("indef");
        a.setTimeEvent("indef");
        Date date = new Date();
        a.setTimeCollr(date);
        a.setTimeCntrl(date);
        a.setAgentName("indef");
        a.setUserName("indef");

        a.setSrcIp("0.0.0.0");
        a.setDstIp("0.0.0.0");
        a.setDstPort(pn.getPortId());
        a.setSrcPort(pn.getPortId());
        a.setSrcHostname("indef");
        a.setDstHostname("indef");
        a.setRegValue("indef");
        a.setFileName("indef");
        a.setHashMd5("indef");
        a.setHashSha1("indef");
        a.setHashSha256("indef");
        a.setProcessId(0);
        a.setProcessName("indef");
        a.setProcessCmdline("indef");
        a.setProcessPath("indef");
        a.setUrlHostname("indef");
        a.setUrlPath("indef");
        a.setContainerId("indef");
        a.setContainerName("indef");
        a.setCloudInstance("indef");
        a.setIncidentExt("indef");

        eventBean.createAlert(a);
        
        return a.getAlertUuid();
    }

    public List<NmapReport> getResult(InputStream xmlData) {

        SAXParserFactory factory = SAXParserFactory.newInstance();

        factory.setValidating(true);
        factory.setNamespaceAware(false);

        try {

            SAXParser parser = factory.newSAXParser();
            NmapParser ns = new NmapParser();

            parser.parse(xmlData, ns);

            xmlData.close();

            return ns.getResult();

        } catch (ParserConfigurationException | SAXException | IOException e) {

            return null;
        }
    }
}

