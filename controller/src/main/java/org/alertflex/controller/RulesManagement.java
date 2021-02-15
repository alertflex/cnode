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

import org.alertflex.common.ProjectRepository;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import org.alertflex.common.SensorRepository;
import org.alertflex.entity.Project;
import org.alertflex.entity.Node;
import org.alertflex.entity.NodePK;
import org.alertflex.entity.Sensor;
import org.alertflex.entity.SensorPK;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RulesManagement {

    private static final Logger logger = LoggerFactory.getLogger(RulesManagement.class);

    private InfoMessageBean eventBean;
    Project project;
    Node node;

    ProjectRepository pr;

    public RulesManagement(InfoMessageBean eb) {
        this.eventBean = eb;
        this.project = eventBean.getProject();
        this.pr = new ProjectRepository(project);
    }

    public void saveRule(int type, String rule, String data) {
        
        String sensorType = "";
        String sensorName;

        try {

            String ref = project.getRefId();
            String nodeName = eventBean.getNode();
            String probe = eventBean.getProbe();
            
            Node node = eventBean.getNodeFacade().findByNodeName(ref, nodeName);
            pr.initNode(nodeName);
            
            if (node == null) {

                NodePK nodePK = new NodePK(ref, nodeName);
                node = new Node();
                node.setNodePK(nodePK);
                node.setUnit("change unit");
                node.setDescription("change desc");
                node.setOpenC2(1);
                node.setFiltersControl(0);
                eventBean.getNodeFacade().create(node);
            }
            
            switch (type) {
                case 0:
                    sensorName = probe + ".crs";
                    sensorType = "Falco";
                    break;
                    
                case 1:
                    sensorName = probe + ".hids";
                    sensorType = "Wazuh";
                    break;
                        
                case 2:
                    sensorName = probe + ".nids";
                    sensorType = "Suricata";
                    break;
                
                default:
                    return;
            }

            Sensor s = eventBean.getSensorFacade().findSensorByName(ref, nodeName, sensorName);

            if (s == null) {

                SensorPK sensorPK = new SensorPK(ref, nodeName, sensorName);
                s = new Sensor(sensorPK);
                s.setDescription(sensorType + " sensor");
                s.setType(sensorType);
                s.setProbe(probe);
                s.setHost("indef");
                s.setIprepUpdate(0);
                s.setRulesUpdate(0);
                eventBean.getSensorFacade().create(s);
            }

            SensorRepository sr = new SensorRepository(pr.getNodeDir(), sensorName, sensorType);
            boolean ready = sr.getStatus();

            if (data.isEmpty() || !ready) {
                return;
            }

            String rulePath = sr.getRemRulesDir() + rule;

            Path p = Paths.get(rulePath);

            Files.write(p, data.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

        } catch (IOException e) {
            logger.error("alertflex_ctrl_exception", e);
        }
    }
}
