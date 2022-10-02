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

package org.alertflex.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.alertflex.common.NmapScanReport;
import org.alertflex.common.NmapScanParser;
import org.alertflex.entity.Alert;
import org.alertflex.entity.AlertPriority;
import org.alertflex.entity.NmapScan;
import org.alertflex.entity.Project;
import org.alertflex.entity.Node;
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

    public void saveReport(String report, String target) {

        try {

            String r = eventBean.getRefId();
            String n = eventBean.getNode();
            String p = eventBean.getProbe();
            Date date = new Date();

            node = eventBean.getNodeFacade().findByNodeName(r, n);

            if (node == null || p == null || report.isEmpty()) {
                return;
            }

            InputStream is = new ByteArrayInputStream(report.getBytes(StandardCharsets.UTF_8));
            
            List<NmapScanReport> listNmapScanReport = getResult(is);
            
            for (NmapScanReport nsr: listNmapScanReport) {
                
                NmapScan ns = new NmapScan();
                
                String name = nsr.getName();
                int port = Integer.parseInt(nsr.getPortid());
                String protocol = nsr.getProtocol();
                String state = nsr.getState();
                
                NmapScan nsExisting = eventBean.getNmapScanFacade().findRecord(r,n,p,target,port,state);
                
                if (nsExisting == null) {
                    
                    ns.setHost(target);
                    ns.setNode(n);
                    ns.setRefId(r);
                    ns.setProbe(p);
                    ns.setPortId(port);
                    ns.setProtocol(protocol);
                    ns.setState(state);
                    ns.setName(name);
                    ns.setReportAdded(date);
                    ns.setReportUpdated(date);
                    
                    eventBean.getNmapScanFacade().create(ns);
                    
                    createNmapScanAlert(ns);
                    
                } else {
                    nsExisting.setReportUpdated(date);
                    eventBean.getNmapScanFacade().edit(nsExisting);
                }
            }
           
            
        } catch (Exception e) {
            logger.error("alertflex_ctrl_exception", e);
        }
    }
    
    public void createNmapScanAlert(NmapScan ns) {

        Alert a = new Alert();

        a.setNodeId(eventBean.getNode());
        a.setRefId(eventBean.getRefId());
        a.setAlertUuid(UUID.randomUUID().toString());

        AlertPriority ap = eventBean.getAlertPriorityFacade().findPriorityBySource(ns.getRefId(), "ZAP");
        
        int sev = ap.getSeverityDefault();
        if (sev < ap.getSeverityThreshold()) return;
        
        a.setEventSeverity(Integer.toString(sev));
        a.setAlertSeverity(sev);
        a.setEventId("1");
        
        a.setCategories("nmap");
        a.setAlertSource("Nmap");
        a.setAlertType("MISC");

        a.setSensorId(ns.getProbe());
        a.setLocation(ns.getHost());
        a.setAction("indef");
        a.setStatus("processed");
        a.setFilter("");
        
        a.setDescription("Port: " + ns.getName() + " is " + ns.getState());
        
        a.setInfo("indef");
        a.setTimeEvent("indef");
        Date date = new Date();
        a.setTimeCollr(date);
        a.setTimeCntrl(date);
        a.setAgentName("indef");
        a.setUserName("indef");

        a.setSrcIp("0.0.0.0");
        a.setDstIp("0.0.0.0");
        a.setDstPort(ns.getPortId());
        a.setSrcPort(ns.getPortId());
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

    }

    public List<NmapScanReport> getResult(InputStream xmlData) {

        SAXParserFactory factory = SAXParserFactory.newInstance();

        factory.setValidating(true);
        factory.setNamespaceAware(false);

        try {

            SAXParser parser = factory.newSAXParser();
            NmapScanParser ns = new NmapScanParser();

            parser.parse(xmlData, ns);

            xmlData.close();

            return ns.getResult();

        } catch (ParserConfigurationException | SAXException | IOException e) {

            return null;
        }
    }
}

