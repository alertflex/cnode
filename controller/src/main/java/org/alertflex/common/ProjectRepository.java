/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alertflex.common;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.alertflex.entity.Node;
import org.alertflex.entity.Project;

/**
 *
 * @author root
 */
public class ProjectRepository {

    Project project;
    Boolean status = true;

    String projectDir;
    Path projectPath;

    String ctrlDir;
    Path ctrlPath;
    
    String ctrlListsDir;
    Path ctrlListsPath;

    Node node;
    String nodeDir;
    Path nodePath;

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

            ctrlListsDir = ctrlDir + "lists/";
            ctrlListsPath = Paths.get(ctrlListsDir);

            if (Files.notExists(ctrlListsPath)) {
                Files.createDirectory(ctrlListsPath);
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
    
    public String getCtrlDir() {
        return ctrlDir;
    }

    public Path getCtrlPath() {
        return ctrlPath;
    }
    
    public String getCtrlListsDir() {
        return ctrlListsDir;
    }

    public Path getCtrlListsPath() {
        return ctrlListsPath;
    }

    public String getNodeDir() {
        return nodeDir;
    }

    public Path getNodePath() {
        return nodePath;
    }

    public Boolean initNode(String nodeName) {

        nodeDir = project.getProjectPath() + nodeName + "/";

        nodePath = Paths.get(nodeDir);
        if (Files.notExists(nodePath)) {
            try {
                Files.createDirectory(nodePath);
            } catch (Exception e) {
                status = false;
                return status;
            }
        }

        return status;
    }
}
