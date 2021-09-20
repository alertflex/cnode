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
import org.slf4j.LoggerFactory;
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
import org.alertflex.facade.AgentScaFacade;
import org.alertflex.facade.AgentVulFacade;
import org.alertflex.facade.AlertFacade;
import org.alertflex.facade.DependencyScanFacade;
import org.alertflex.facade.DockerScanFacade;
import org.alertflex.facade.HunterScanFacade;
import org.alertflex.facade.InspectorScanFacade;
import org.alertflex.facade.KubeScanFacade;
import org.alertflex.facade.SnykScanFacade;
import org.alertflex.facade.SonarScanFacade;
import org.alertflex.facade.ZapScanFacade;
import org.alertflex.facade.TrivyScanFacade;
import org.alertflex.reports.AlertsBar;
import org.alertflex.reports.AlertsPie;
import org.alertflex.reports.Finding;
import org.alertflex.reports.JasperDataAlertsSeverity;
import org.alertflex.reports.JasperDataAlertsSource;
import org.alertflex.reports.JasperDataCloud;
import org.alertflex.reports.JasperDataContainers;
import org.alertflex.reports.JasperDataEndpoints;
import org.alertflex.reports.JasperDataScanners;
import org.apache.activemq.ActiveMQConnectionFactory;


@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")
    ,
        @ActivationConfigProperty(propertyName = "destination", propertyValue = "jms/alertflex/reports"),})
@Stateless
public class ReportsMessageBean implements MessageListener {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ReportsMessageBean.class);

    @EJB
    private ProjectFacade projectFacade;
    Project p;
    String ref_id;
    
    @EJB
    private AlertFacade alertFacade;
    
    @EJB
    private AgentScaFacade agentVulFacade;
    
    @EJB
    private AgentVulFacade agentScaFacade;
    
    @EJB
    private ZapScanFacade zapScanFacade;
    
    @EJB
    private SnykScanFacade snykScanFacade;
    
    @EJB
    private SonarScanFacade sonarScanFacade;
    
    @EJB
    private InspectorScanFacade inspectorScanFacade;
    
    @EJB
    private DockerScanFacade dockerScanFacade;
    
    @EJB
    private KubeScanFacade kubeScanFacade;
    
    @EJB
    private HunterScanFacade hunterScanFacade;
    
    @EJB
    private TrivyScanFacade trivyScanFacade;
    
    @EJB
    private DependencyScanFacade dependencyScanFacade;

        
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
                            
                        case "Containers" :
                            if (createContainersReport(dir, interval)) responseText = "Ok";
                            break;
                            
                        case "Endpoints" :
                            if (createEndpointsReport(dir, interval)) responseText = "Ok";
                            break;
                            
                        case "Scanners" :
                            if (createScannersReport(dir, interval)) responseText = "Ok";
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

            List<Object[]> inspectorObjects = inspectorScanFacade.getFindings(p.getRefId());
            
            if (inspectorObjects != null && inspectorObjects.size() > 0) {
                
                for (Object[] o: inspectorObjects) {
                    String f = (String) o[0];
                    Long c = (Long) o[1];
                    inspectorFindings.add(new Finding(f,c.intValue()));
                }
            }
            
            if ((alertsList != null) && (alertsList.size() > 0)) {

                JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(reportDir + "cloud_report.jasper");

                JasperDataCloud jasperDataCloud = new JasperDataCloud(inspectorFindings, alertsList, start, end, 50);
                
                Map<String, Object> params = new HashMap<String, Object>();

                List<AlertsPie> alertsSeverityPie = jasperDataCloud.getBeanCollectionPie();
                params.put("datasourceGuardduty", alertsSeverityPie);

                params.put("datasourceInspector", jasperDataCloud.getInspectorFindings());

                params.put("reportDir", reportDir);

                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JREmptyDataSource());

                JasperExportManager.exportReportToPdfFile(jasperPrint, reportDir + "cloud_report.pdf");
            }

        } catch (JRException e) {
            logger.error("alertflex_ctrl_exception", e);
            return false;
        }

        return true;
    }
    
    public Boolean createContainersReport(String reportDir, int interval) {

        try {
            
            List<Finding> dockerBenchFindings = new ArrayList();
            List<Finding> kubeBenchFindings = new ArrayList();
            List<Finding> kubeHunterFindings = new ArrayList();
            List<Finding> trivyFindings = new ArrayList();
            
            List<Object[]> dockerBenchObjects = dockerScanFacade.getFindings(p.getRefId());
            List<Object[]> kubeBenchObjects = kubeScanFacade.getFindings(p.getRefId());
            List<Object[]> kubeHunterObjects = hunterScanFacade.getFindings(p.getRefId());
            List<Object[]> trivyObjects = trivyScanFacade.getFindings(p.getRefId());
                        
            if (dockerBenchObjects != null && dockerBenchObjects.size() > 0) {
                
                for (Object[] o: dockerBenchObjects) {
                    String f = (String) o[0];
                    Long c = (Long) o[1];
                    dockerBenchFindings.add(new Finding(f,c.intValue()));
                }
            }
            
            if (kubeBenchObjects != null && kubeBenchObjects.size() > 0) {
                
                for (Object[] o: kubeBenchObjects) {
                    String f = (String) o[0];
                    Long c = (Long) o[1];
                    kubeBenchFindings.add(new Finding(f,c.intValue()));
                }
            }
            
            if (kubeHunterObjects != null && kubeHunterObjects.size() > 0) {
                
                for (Object[] o: kubeHunterObjects) {
                    String f = (String) o[0];
                    Long c = (Long) o[1];
                    kubeHunterFindings.add(new Finding(f,c.intValue()));
                }
            }
            
            if (trivyObjects != null && trivyObjects.size() > 0) {
                
                for (Object[] o: trivyObjects) {
                    String f = (String) o[0];
                    Long c = (Long) o[1];
                    trivyFindings.add(new Finding(f,c.intValue()));
                }
            }
            
            JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(reportDir + "containers_report.jasper");
                
            JasperDataContainers jasperDataContainers = new JasperDataContainers(dockerBenchFindings, kubeBenchFindings, kubeHunterFindings, trivyFindings);
            
            Map<String, Object> params = new HashMap<String, Object>();

            params.put("datasourceDockerBench", jasperDataContainers.getDockerBenchFindings());
            params.put("datasourceKubeBench", jasperDataContainers.getKubeBenchFindings());
            params.put("datasourceKubeHunter", jasperDataContainers.getKubeHunterFindings());
            params.put("datasourceTrivy", jasperDataContainers.getTrivyFindings());
            params.put("reportDir", reportDir);

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JREmptyDataSource());

            JasperExportManager.exportReportToPdfFile(jasperPrint, reportDir + "containers_report.pdf");

        } catch (JRException e) {
            logger.error("alertflex_ctrl_exception", e);
            return false;
        }

        return true;
    }
    
    public Boolean createEndpointsReport(String reportDir, int interval) {

        try {
            
            List<Finding> misconfigFindings = new ArrayList();
            List<Finding> vulnFindings = new ArrayList();
            
            List<Object[]> misconfigObjects = agentScaFacade.getFindings(p.getRefId());
            List<Object[]> vulnObjects = agentVulFacade.getFindings(p.getRefId());
                        
            if (misconfigObjects != null && misconfigObjects.size() > 0) {
                
                for (Object[] o: misconfigObjects) {
                    String f = (String) o[0];
                    Long c = (Long) o[1];
                    misconfigFindings.add(new Finding(f,c.intValue()));
                }
            }
            
            if (vulnObjects != null && vulnObjects.size() > 0) {
                
                for (Object[] o: vulnObjects) {
                    String f = (String) o[0];
                    Long c = (Long) o[1];
                    vulnFindings.add(new Finding(f,c.intValue()));
                }
            }
            
            JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(reportDir + "endpoints_report.jasper");
                
            JasperDataEndpoints jasperDataEndpoints = new JasperDataEndpoints(misconfigFindings, vulnFindings);
            
            Map<String, Object> params = new HashMap<String, Object>();

            params.put("datasourceMisconfig", jasperDataEndpoints.getMisconfigFindings());
            params.put("datasourceVuln", jasperDataEndpoints.getVulnFindings());
            params.put("reportDir", reportDir);

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JREmptyDataSource());

            JasperExportManager.exportReportToPdfFile(jasperPrint, reportDir + "endpoints_report.pdf");

        } catch (JRException e) {
            logger.error("alertflex_ctrl_exception", e);
            return false;
        }

        return true;
    }
    
    
    public Boolean createScannersReport(String reportDir, int interval) {

        try {
            
            List<Finding> zapFindings = new ArrayList();
            List<Finding> dependencyFindings = new ArrayList();
            List<Finding> sonarqubeFindings = new ArrayList();
            List<Finding> snykFindings = new ArrayList();

            List<Object[]> zapObjects = zapScanFacade.getFindings(p.getRefId());
            List<Object[]> dependencyObjects = dependencyScanFacade.getFindings(p.getRefId());
            List<Object[]> sonarqubeObjects = sonarScanFacade.getFindings(p.getRefId());
            List<Object[]> snykObjects = snykScanFacade.getFindings(p.getRefId());
            
            if (zapObjects != null && zapObjects.size() > 0) {
                
                for (Object[] o: zapObjects) {
                    String f = (String) o[0];
                    Long c = (Long) o[1];
                    zapFindings.add(new Finding(f,c.intValue()));
                }
            }
            
            if (dependencyObjects != null && dependencyObjects.size() > 0) {
                
                for (Object[] o: dependencyObjects) {
                    String f = (String) o[0];
                    Long c = (Long) o[1];
                    dependencyFindings.add(new Finding(f,c.intValue()));
                }
            }
            
            if (snykObjects != null && snykObjects.size() > 0) {
                
                for (Object[] o: snykObjects) {
                    String f = (String) o[0];
                    Long c = (Long) o[1];
                    snykFindings.add(new Finding(f,c.intValue()));
                }
            }
            
            if (sonarqubeObjects != null && sonarqubeObjects.size() > 0) {
                
                for (Object[] o: sonarqubeObjects) {
                    String f = (String) o[0];
                    Long c = (Long) o[1];
                    sonarqubeFindings.add(new Finding(f,c.intValue()));
                }
            }

            JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(reportDir + "scanners_report.jasper");
                
            JasperDataScanners jasperDataScanners = new JasperDataScanners(snykFindings, dependencyFindings, sonarqubeFindings, zapFindings);
            
            Map<String, Object> params = new HashMap<String, Object>();

            params.put("datasourceSnyk", jasperDataScanners.getSnykFindings());
            params.put("datasourceDependency", jasperDataScanners.getDependencyFindings());
            params.put("datasourceSonarqube", jasperDataScanners.getSonarqubeFindings());
            params.put("datasourceZap", jasperDataScanners.getZapFindings());
            params.put("reportDir", reportDir);

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JREmptyDataSource());

            JasperExportManager.exportReportToPdfFile(jasperPrint, reportDir + "scanners_report.pdf");

        } catch (JRException e) {
            logger.error("alertflex_ctrl_exception", e);
            return false;
        }

        return true;
    }
}
