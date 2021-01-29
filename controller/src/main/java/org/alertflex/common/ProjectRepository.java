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
 
package org.alertflex.common;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.alertflex.entity.Node;
import org.alertflex.entity.Project;

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
