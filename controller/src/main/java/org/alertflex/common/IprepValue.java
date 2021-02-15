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
import org.alertflex.entity.Project;

public class IprepValue {

    String project = "";

    Integer IprepTimerange = 0;

    Long timerCounter = 0L;
    Long ipCounter = 0L;
    Integer projectTimer = 0;

    boolean isDirOpen = false;
    String projectListsDir;
    Path projectListsPath;

    public IprepValue(Project p) {

        this.project = p.getRefId();

        this.IprepTimerange = p.getIprepTimerange();

        openDir(p);

    }

    public void updateTimerange(Integer t) {
        this.IprepTimerange = t;
    }

    public Long getIpCounter() {
        return ipCounter;
    }

    public void setIpCounter(Long c) {
        this.ipCounter = c;
    }

    public boolean CheckTimerCounter() {

        if (!isDirOpen) {
            return false;
        }

        if (projectTimer == 0) {
            return false;
        }

        if (timerCounter < projectTimer) {
            timerCounter++;
            return false;
        }

        timerCounter = 0L;
        projectTimer = IprepTimerange;
        return true;
    }

    boolean openDir(Project p) {

        if (isDirOpen) {
            return true;
        }

        String projectDir = p.getProjectPath();
        Path projectPath = Paths.get(projectDir);

        if (Files.notExists(projectPath)) {
            try {
                Files.createDirectory(projectPath);
            } catch (Exception e) {
                return false;
            }
        }

        projectDir = projectDir + "controller/";
        projectPath = Paths.get(projectDir);

        if (Files.notExists(projectPath)) {
            try {
                Files.createDirectory(projectPath);
            } catch (Exception e) {
                return false;
            }
        }

        projectListsDir = projectDir + "lists/";
        projectListsPath = Paths.get(projectDir);

        if (Files.notExists(projectListsPath)) {
            try {
                Files.createDirectory(projectListsPath);
            } catch (Exception e) {
                return false;
            }
        }

        isDirOpen = true;

        return true;
    }

    public boolean dirStatus() {
        return isDirOpen;
    }

    public String getProjectListsDir() {
        return projectListsDir;
    }

}
