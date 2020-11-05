/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alertflex.common;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.alertflex.entity.Node;
import org.alertflex.entity.Project;

/**
 *
 * @author root
 */
public class ProjectRepository {

    Project project;
    Node node;
    List<String> listSensorNames;
    Boolean status = true;

    String projectDir;
    Path projectPath;

    String ctrlDir;
    String listsCtrlDir;

    Path ctrlPath;
    Path listsCtrlPath;

    String nodeDir;
    Path nodePath;

    List<SensorRepository> listSensorRepository;

    public ProjectRepository(Project p) {

        this.project = p;
        if (p == null) {
            status = false;
            return;
        }

        projectDir = project.getProjectPath();
        projectPath = Paths.get(projectDir);

        if (Files.notExists(projectPath)) {
            try {
                Files.createDirectory(projectPath);
            } catch (Exception e) {
                status = false;
                return;
            }
        }

        try {
            ctrlDir = projectDir + "controller/";
            ctrlPath = Paths.get(ctrlDir);

            if (Files.notExists(ctrlPath)) {
                Files.createDirectory(ctrlPath);
            }

            listsCtrlDir = ctrlDir + "lists/";
            listsCtrlPath = Paths.get(listsCtrlDir);

            if (Files.notExists(listsCtrlPath)) {
                Files.createDirectory(listsCtrlPath);
            }

        } catch (Exception e) {
            status = false;
            return;
        }
    }

    public Boolean getStatus() {
        return status;
    }

    public String getProjectDir() {
        return projectDir;
    }

    public Path getProjectPath() {
        return projectPath;
    }

    public String getNodeDir() {
        return nodeDir;
    }

    public Path getNodePath() {
        return nodePath;
    }

    public String getSensorDir(String sensorName) {

        for (SensorRepository sr : listSensorRepository) {
            if (sr.getName().equals(sensorName)) {
                return sr.getDir();
            }
        }

        return null;
    }

    public Path getSensorPath(String sensorName) {

        for (SensorRepository sr : listSensorRepository) {
            if (sr.getName().equals(sensorName)) {
                return sr.getPath();
            }
        }

        return null;
    }

    public String getLocRulesDir(String sensorName) {

        for (SensorRepository sr : listSensorRepository) {
            if (sr.getName().equals(sensorName)) {
                return sr.getLocRulesDir();
            }
        }

        return null;
    }

    public Path getLocRulesPath(String sensorName) {

        for (SensorRepository sr : listSensorRepository) {
            if (sr.getName().equals(sensorName)) {
                return sr.getLocRulesPath();
            }
        }

        return null;
    }

    public String getRemRulesDir(String sensorName) {

        for (SensorRepository sr : listSensorRepository) {
            if (sr.getName().equals(sensorName)) {
                return sr.getRemRulesDir();
            }
        }

        return null;
    }

    public Path getRemRulesPath(String sensorName) {

        for (SensorRepository sr : listSensorRepository) {
            if (sr.getName().equals(sensorName)) {
                return sr.getRemRulesPath();
            }
        }

        return null;
    }

    public String getHidsRulesDir(String sensorName) {

        for (SensorRepository sr : listSensorRepository) {
            if (sr.getName().equals(sensorName)) {
                return sr.getHidsRulesDir();
            }
        }

        return null;
    }

    public Path getHidsRulesPath(String sensorName) {

        for (SensorRepository sr : listSensorRepository) {
            if (sr.getName().equals(sensorName)) {
                return sr.getHidsRulesPath();
            }
        }

        return null;
    }

    public String getHidsDecodersDir(String sensorName) {

        for (SensorRepository sr : listSensorRepository) {
            if (sr.getName().equals(sensorName)) {
                return sr.getHidsDecodersDir();
            }
        }

        return null;
    }

    public Path getHidsDecodersPath(String sensorName) {

        for (SensorRepository sr : listSensorRepository) {
            if (sr.getName().equals(sensorName)) {
                return sr.getHidsDecodersPath();
            }
        }

        return null;
    }

    public int getSensorType(String sensorName) {

        for (SensorRepository sr : listSensorRepository) {
            if (sr.getName().equals(sensorName)) {
                return sr.getSensorType();
            }
        }

        return 3;
    }

    public Boolean initSensor(Node n, String s) {

        this.node = n;

        nodeDir = project.getProjectPath() + node.getNodePK().getName() + "/";

        nodePath = Paths.get(nodeDir);
        if (Files.notExists(nodePath)) {
            try {
                Files.createDirectory(nodePath);
            } catch (Exception e) {
                status = false;
                return status;
            }
        }

        if (s != null) {

            SensorRepository sr = new SensorRepository();
            status = sr.init(nodeDir, s);
            if (!status) {
                return status;
            }

            this.listSensorRepository = new ArrayList();
            listSensorRepository.add(sr);
        }

        return status;
    }

    public Boolean initSensors(Node n, List<String> lsn) {

        this.node = n;

        nodeDir = project.getProjectPath() + node.getNodePK().getName() + "/";

        nodePath = Paths.get(nodeDir);
        if (Files.notExists(nodePath)) {
            try {
                Files.createDirectory(nodePath);
            } catch (Exception e) {
                status = false;
                return status;
            }
        }

        if (lsn != null) {
            this.listSensorNames = lsn;

        } else {
            listSensorNames = new ArrayList();
            listSensorNames.add("master-crs");
            listSensorNames.add("master-hids");
            listSensorNames.add("master-nids");
            listSensorNames.add("master-waf");
        }

        this.listSensorRepository = new ArrayList();

        for (String sensorName : listSensorNames) {

            SensorRepository sr = new SensorRepository();
            status = sr.init(nodeDir, sensorName);
            if (!status) {
                return status;
            }

            listSensorRepository.add(sr);
        }

        return status;
    }
}
