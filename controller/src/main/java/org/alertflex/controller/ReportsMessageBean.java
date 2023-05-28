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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.ActivationConfigProperty;
import javax.jms.Message;
import org.alertflex.facade.ProjectFacade;
import javax.ejb.MessageDriven;
import javax.ejb.Stateless;
import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import org.alertflex.entity.Alert;
import org.alertflex.entity.Project;
import org.alertflex.facade.AgentMisconfigFacade;
import org.alertflex.facade.AgentVulFacade;
import org.alertflex.facade.AlertFacade;
import org.alertflex.facade.PostureAppsecretFacade;
import org.alertflex.facade.PostureAppvulnFacade;
import org.alertflex.facade.PostureInspectorFacade;
import org.alertflex.facade.PostureCloudformationFacade;
import org.alertflex.facade.PostureDockerconfigFacade;
import org.alertflex.facade.PostureDockervulnFacade;
import org.alertflex.facade.PostureK8sconfigFacade;
import org.alertflex.facade.PostureK8svulnFacade;
import org.alertflex.facade.PostureKubehunterFacade;
import org.alertflex.facade.PostureNmapFacade;
import org.alertflex.facade.PostureNucleiFacade;
import org.alertflex.facade.PostureTerraformFacade;
import org.alertflex.facade.PostureZapFacade;
import org.alertflex.reports.AlertsBar;
import org.alertflex.reports.AlertsPie;
import org.alertflex.reports.Finding;
import org.alertflex.reports.JasperDataAlertsSeverity;
import org.alertflex.reports.JasperDataAlertsSource;
import org.alertflex.reports.JasperDataCloud;
import org.alertflex.reports.JasperDataMisconfig;
import org.alertflex.reports.JasperDataScanners;
import org.alertflex.reports.JasperDataVuln;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")
    ,
        @ActivationConfigProperty(propertyName = "destination", propertyValue = "jms/alertflex/reports"),})
@Stateless
public class ReportsMessageBean implements MessageListener {

    private static final Logger logger = LogManager.getLogger(ReportsMessageBean.class);

    @EJB
    private ProjectFacade projectFacade;
    Project p;
    String ref_id;
    
    @EJB
    private AlertFacade alertFacade;
    
    @EJB
    private PostureAppsecretFacade postureAppsecretFacade;
    
    @EJB
    private PostureAppvulnFacade postureAppvulnFacade;
    
    @EJB
    private PostureDockerconfigFacade postureDockerconfigFacade;
    
    @EJB
    private PostureDockervulnFacade postureDockervulnFacade;
    
    @EJB
    private PostureK8sconfigFacade postureK8sconfigFacade;
    
    @EJB
    private PostureK8svulnFacade postureK8svulnFacade;
    
    @EJB
    private AgentMisconfigFacade agentScaFacade;
    
    @EJB
    private AgentVulFacade agentVulFacade;
    
    @EJB
    private PostureZapFacade postureZapFacade;
    
    @EJB
    private PostureKubehunterFacade postureKubehunterFacade;
    
    @EJB
    private PostureCloudformationFacade postureCloudformationFacade;
    
    @EJB
    private PostureTerraformFacade postureTerraformFacade;
    
    @EJB
    private PostureInspectorFacade postureInspectorFacade;
    
    @EJB
    private PostureNmapFacade postureNmapFacade;
    
    @EJB
    private PostureNucleiFacade postureNucleiFacade;
    

    @Override
    public void onMessage(Message message) {
        
        
        try {
            
            String responseText = "Error";
            
            if (message instanceof TextMessage) {

                TextMessage textMessage = (TextMessage) message;
                
                ref_id = textMessage.getStringProperty("ref_id");
                p = projectFacade.findProjectByRef(ref_id);
                
                if (p != null) {
                    
                    int interval = textMessage.getIntProperty("interval");
                    String dir = textMessage.getStringProperty("report_dir");
                    String typeReport = textMessage.getText();
                    
                    switch (typeReport ) {
                        
                        case "Alerts" :
                            if (createAlertsReport(dir, interval)) responseText = "Ok";
                            break;
                            
                        case "Cloud" :
                            if (createCloudReport(dir, interval)) responseText = "Ok";
                            break;
                            
                        case "Scanners" :
                            if (createScannersReport(dir, interval)) responseText = "Ok";
                            break;
                            
                        case "Misconfig" :
                            if (createMisconfigReport(dir, interval)) responseText = "Ok";
                            break;
                            
                        case "Vuln" :
                            if (createVulnReport(dir, interval)) responseText = "Ok";
                            break;
                            
                        default :
                            break;
                    }
                } 
            } 
            
            /***** Send response *****/
            
            String strConnFactory = System.getProperty("AmqUrl", "");
            String user = System.getProperty("AmqUser", "");
            String pass = System.getProperty("AmqPwd", "");

            // Create a ConnectionFactory
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(strConnFactory);

            // Create a Connection
            Connection connectionResponse = connectionFactory.createConnection(user, pass);
            connectionResponse.start();

            // Create a Session
            Session sessionResponse = connectionResponse.createSession(false, Session.AUTO_ACKNOWLEDGE);
            
            //Setup a message producer to respond to messages from clients, we will get the destination
            //to send to from the JMSReplyTo header field from a Message
            MessageProducer replyProducer = sessionResponse.createProducer(null);
            replyProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
            
            TextMessage response = sessionResponse.createTextMessage();
            if (message instanceof TextMessage) {
                TextMessage txtMsg = (TextMessage) message;
                String messageText = txtMsg.getText();
                response.setText(responseText);
            }

            //Set the correlation ID from the received message to be the correlation id of the response message
            //this lets the client identify which message this is a response to if it has more than
            //one outstanding message to the server
            response.setJMSCorrelationID(message.getJMSCorrelationID());

            //Send the response to the Destination specified by the JMSReplyTo field of the received message,
            //this is presumably a temporary queue created by the client
            replyProducer.send(message.getJMSReplyTo(), response);

            
        } catch (JMSException e) {
            logger.error("alertflex_ctrl_exception", e);
        }
    }
    
    public Boolean createAlertsReport(String reportDir, int interval) {

        try {

            Date end = new Date();
            Date start = new Date(end.getTime() - 1000 * interval);
            
            List<Alert> alertsList = alertFacade.findIntervalsBetween(p.getRefId(), start, end, 10000);

            if ((alertsList != null) && (alertsList.size() > 0)) {

                JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(reportDir + "alerts_report.jasper");

                JasperDataAlertsSource jasperDataAlertsSource = new JasperDataAlertsSource(alertsList, start, end, 50);
                JasperDataAlertsSeverity jasperDataAlertsSeverity = new JasperDataAlertsSeverity(alertsList, start, end, 50);

                Map<String, Object> params = new HashMap<String, Object>();

                List<AlertsPie> alertsSourcePie = jasperDataAlertsSource.getBeanCollectionPie();
                params.put("datasourceAlertsSourcePie", alertsSourcePie);

                List<AlertsBar> alertsSourceBar = jasperDataAlertsSource.getBeanCollectionBar();
                params.put("datasourceAlertsSourceBar", alertsSourceBar);

                List<AlertsPie> alertsSeverityPie = jasperDataAlertsSeverity.getBeanCollectionPie();
                params.put("datasourceAlertsSeverityPie", alertsSeverityPie);

                List<AlertsBar> alertsSeverityBar = jasperDataAlertsSeverity.getBeanCollectionBar();
                params.put("datasourceAlertsSeverityBar", alertsSeverityBar);

                params.put("reportDir", reportDir);

                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JREmptyDataSource());

                JasperExportManager.exportReportToPdfFile(jasperPrint, reportDir + "alerts_report.pdf");
            }

        } catch (JRException e) {
            logger.error("alertflex_ctrl_exception", e);
            return false;
        }

        return true;
    }
    
    public Boolean createCloudReport(String reportDir, int interval) {

        try {
            
            Date end = new Date();
            Date start = new Date(end.getTime() - 1000 * interval);
            List<Alert> alertsList = alertFacade.findAlertsGuardDuty(p.getRefId(), start, end, 10000);

            List<Finding> inspectorFindings = new ArrayList();
            List<Object[]> inspectorObjects = postureInspectorFacade.getFindings(p.getRefId());
            if (inspectorObjects != null && inspectorObjects.size() > 0) {
                
                for (Object[] o: inspectorObjects) {
                    String f = (String) o[0];
                    Long c = (Long) o[1];
                    inspectorFindings.add(new Finding(f,c.intValue()));
                }
            } else {
                inspectorFindings.add(new Finding("indef",1));
            }
            
            List<Finding> cformationFindings = new ArrayList();
            List<Object[]> cformationObjects = postureCloudformationFacade.getFindings(p.getRefId());
            if (cformationObjects != null && cformationObjects.size() > 0) {
                
                for (Object[] o: cformationObjects) {
                    String f = (String) o[0];
                    Long c = (Long) o[1];
                    cformationFindings.add(new Finding(f,c.intValue()));
                }
            } else {
                cformationFindings.add(new Finding("indef",1));
            }
            
            List<Finding> terraformFindings = new ArrayList();
            List<Object[]> terraformObjects = postureTerraformFacade.getFindings(p.getRefId());
            if (terraformObjects != null && terraformObjects.size() > 0) {
                
                for (Object[] o: terraformObjects) {
                    String f = (String) o[0];
                    Long c = (Long) o[1];
                    terraformFindings.add(new Finding(f,c.intValue()));
                }
            } else {
                terraformFindings.add(new Finding("none",1));
            }
            
            JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(reportDir + "cloud_report.jasper");

            JasperDataCloud jasperDataCloud = new JasperDataCloud(inspectorFindings, cformationFindings, terraformFindings, alertsList, start, end, 50);
            
            Map<String, Object> params = new HashMap<String, Object>();

            List<AlertsPie> alertsSeverityPie = jasperDataCloud.getBeanCollectionPie();
            params.put("datasourceGuardduty", alertsSeverityPie);
            params.put("datasourceInspector", jasperDataCloud.getInspectorFindings());
            params.put("datasourceCformation", jasperDataCloud.getCformationFindings());
            params.put("datasourceTerraform", jasperDataCloud.getTerraformFindings());
            params.put("reportDir", reportDir);

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JREmptyDataSource());

            JasperExportManager.exportReportToPdfFile(jasperPrint, reportDir + "cloud_report.pdf");
        
        } catch (JRException e) {
            logger.error("alertflex_ctrl_exception", e);
            return false;
        }

        return true;
    }
    
    
    public Boolean createScannersReport(String reportDir, int interval) {

        try {
            
            List<Finding> zapFindings = new ArrayList();
            List<Finding> kubehunterFindings = new ArrayList();
            List<Finding> nmapFindings = new ArrayList();
            List<Finding> nucleiFindings = new ArrayList();
            
            List<Object[]> zapObjects = postureZapFacade.getFindings(p.getRefId());
            List<Object[]> kubehunterObjects = postureKubehunterFacade.getFindings(p.getRefId());
            List<Object[]> nmapObjects = postureNmapFacade.getFindings(p.getRefId());
            List<Object[]> nucleiObjects = postureNucleiFacade.getFindings(p.getRefId());
                        
            if (zapObjects != null && zapObjects.size() > 0) {
                
                for (Object[] o: zapObjects) {
                    String f = (String) o[0];
                    Long c = (Long) o[1];
                    zapFindings.add(new Finding(f,c.intValue()));
                }
            } else {
                zapFindings.add(new Finding("indef",1));
            }
            
            if (kubehunterObjects != null && kubehunterObjects.size() > 0) {
                
                for (Object[] o: kubehunterObjects) {
                    String f = (String) o[0];
                    Long c = (Long) o[1];
                    kubehunterFindings.add(new Finding(f,c.intValue()));
                }
            } else {
                kubehunterFindings.add(new Finding("indef",1));
            }
            
            if (nmapObjects != null && nmapObjects.size() > 0) {
                
                for (Object[] o: nmapObjects) {
                    String f = (String) o[0];
                    Long c = (Long) o[1];
                    nmapFindings.add(new Finding(f,c.intValue()));
                }
            } else {
                nmapFindings.add(new Finding("indef",1));
            }
            
            if (nucleiObjects != null && nucleiObjects.size() > 0) {
                
                for (Object[] o: nucleiObjects) {
                    String f = (String) o[0];
                    Long c = (Long) o[1];
                    nucleiFindings.add(new Finding(f,c.intValue()));
                }
            } else {
                nucleiFindings.add(new Finding("indef",1));
            }
            
            JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(reportDir + "scanners_report.jasper");
                
            JasperDataScanners jasperDataScanners = new JasperDataScanners(kubehunterFindings, zapFindings, nmapFindings, nucleiFindings);
            
            Map<String, Object> params = new HashMap<String, Object>();

            params.put("datasourceKubehunter", jasperDataScanners.getKubehunterFindings());
            params.put("datasourceZap", jasperDataScanners.getZapFindings());
            params.put("datasourceNmap", jasperDataScanners.getNmapFindings());
            params.put("datasourceNuclei", jasperDataScanners.getNucleiFindings());
            
            params.put("reportDir", reportDir);

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JREmptyDataSource());

            JasperExportManager.exportReportToPdfFile(jasperPrint, reportDir + "scanners_report.pdf");

        } catch (JRException e) {
            logger.error("alertflex_ctrl_exception", e);
            return false;
        }

        return true;
    }
    
    public Boolean createMisconfigReport(String reportDir, int interval) {

        try {
            
            List<Finding> appsecretFindings = new ArrayList();
            List<Object[]> appsecretObjects = postureAppsecretFacade.getFindings(p.getRefId());
            if (appsecretObjects != null && appsecretObjects.size() > 0) {
                
                for (Object[] o: appsecretObjects) {
                    String f = (String) o[0];
                    Long c = (Long) o[1];
                    appsecretFindings.add(new Finding(f,c.intValue()));
                }
            } else {
                appsecretFindings.add(new Finding("indef",1));
            }
            
            List<Finding> dockerconfigFindings = new ArrayList();
            List<Object[]> dockerconfigObjects = postureDockerconfigFacade.getFindings(p.getRefId());
            if (dockerconfigObjects != null && dockerconfigObjects.size() > 0) {
                
                for (Object[] o: dockerconfigObjects) {
                    String f = (String) o[0];
                    Long c = (Long) o[1];
                    dockerconfigFindings.add(new Finding(f,c.intValue()));
                }
            } else {
                dockerconfigFindings.add(new Finding("indef",1));
            }
            
            List<Finding> hostFindings = new ArrayList();
            List<Object[]> hostObjects = agentScaFacade.getFindings(p.getRefId());
            if (hostObjects != null && hostObjects.size() > 0) {
                
                for (Object[] o: hostObjects) {
                    String f = (String) o[0];
                    Long c = (Long) o[1];
                    hostFindings.add(new Finding(f,c.intValue()));
                }
            } else {
                hostFindings.add(new Finding("none",1));
            }
            
            List<Finding> k8sFindings = new ArrayList();
            List<Object[]> k8sObjects = postureK8sconfigFacade.getFindings(p.getRefId());
            if (k8sObjects != null && k8sObjects.size() > 0) {
                
                for (Object[] o: k8sObjects) {
                    String f = (String) o[0];
                    Long c = (Long) o[1];
                    k8sFindings.add(new Finding(f,c.intValue()));
                }
            } else {
                k8sFindings.add(new Finding("none",1));
            }
            
            JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(reportDir + "misconfig_report.jasper");

            JasperDataMisconfig jasperDataMisconfig = new JasperDataMisconfig(appsecretFindings, dockerconfigFindings, hostFindings, k8sFindings);
            
            Map<String, Object> params = new HashMap<String, Object>();

            params.put("datasourceAppsecrets", jasperDataMisconfig.getAppSecretsFindings());
            params.put("datasourceDockerfiles", jasperDataMisconfig.getDockerFilesFindings());
            params.put("datasourceHostconfig", jasperDataMisconfig.getHostFindings());
            params.put("datasourceK8sconfig", jasperDataMisconfig.getKubernetesFindings());
            params.put("reportDir", reportDir);

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JREmptyDataSource());

            JasperExportManager.exportReportToPdfFile(jasperPrint, reportDir + "misconfig_report.pdf");
        
        } catch (JRException e) {
            logger.error("alertflex_ctrl_exception", e);
            return false;
        }

        return true;
    }
    
    public Boolean createVulnReport(String reportDir, int interval) {

        try {
            
            List<Finding> appvulnFindings = new ArrayList();
            List<Object[]> appvulnObjects = postureAppvulnFacade.getFindings(p.getRefId());
            if (appvulnObjects != null && appvulnObjects.size() > 0) {
                
                for (Object[] o: appvulnObjects) {
                    String f = (String) o[0];
                    Long c = (Long) o[1];
                    appvulnFindings.add(new Finding(f,c.intValue()));
                }
            } else {
                appvulnFindings.add(new Finding("indef",1));
            }
            
            List<Finding> dockervulnFindings = new ArrayList();
            List<Object[]> dockervulnObjects = postureDockervulnFacade.getFindings(p.getRefId());
            if (dockervulnObjects != null && dockervulnObjects.size() > 0) {
                
                for (Object[] o: dockervulnObjects) {
                    String f = (String) o[0];
                    Long c = (Long) o[1];
                    dockervulnFindings.add(new Finding(f,c.intValue()));
                }
            } else {
                dockervulnFindings.add(new Finding("indef",1));
            }
            
            List<Finding> hostFindings = new ArrayList();
            List<Object[]> hostObjects = agentVulFacade.getFindings(p.getRefId());
            if (hostObjects != null && hostObjects.size() > 0) {
                
                for (Object[] o: hostObjects) {
                    String f = (String) o[0];
                    Long c = (Long) o[1];
                    hostFindings.add(new Finding(f,c.intValue()));
                }
            } else {
                hostFindings.add(new Finding("none",1));
            }
            
            List<Finding> k8sFindings = new ArrayList();
            List<Object[]> k8sObjects = postureK8svulnFacade.getFindings(p.getRefId());
            if (k8sObjects != null && k8sObjects.size() > 0) {
                
                for (Object[] o: k8sObjects) {
                    String f = (String) o[0];
                    Long c = (Long) o[1];
                    k8sFindings.add(new Finding(f,c.intValue()));
                }
            } else {
                k8sFindings.add(new Finding("none",1));
            }
            
            JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(reportDir + "vuln_report.jasper");

            JasperDataVuln jasperDataVuln = new JasperDataVuln(appvulnFindings, dockervulnFindings, hostFindings, k8sFindings);
            
            Map<String, Object> params = new HashMap<String, Object>();

            params.put("datasourceAppvuln", jasperDataVuln.getApplicationsFindings());
            params.put("datasourceDockervuln", jasperDataVuln.getDockerImagesFindings());
            params.put("datasourceHostvuln", jasperDataVuln.getHostFindings());
            params.put("datasourceK8svuln", jasperDataVuln.getKubernetesFindings());
            params.put("reportDir", reportDir);

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JREmptyDataSource());

            JasperExportManager.exportReportToPdfFile(jasperPrint, reportDir + "vuln_report.pdf");
        
        } catch (JRException e) {
            logger.error("alertflex_ctrl_exception", e);
            return false;
        }

        return true;
    }
}

