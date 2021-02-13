/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alertflex.common;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author root
 */
public class SensorRepository {

    String sensorName;
    String sensorType = "";
    Boolean status = false;

    String dir;
    String remRulesDir;
    String locRulesDir;
    String hidsRulesDir;
    String hidsDecodersDir;

    Path path;
    Path remRulesPath;
    Path locRulesPath;
    Path hidsRulesPath;
    Path hidsDecodersPath;

    public SensorRepository (String nodeDir, String name, String type) {
        
        this.sensorName = name;
        this.sensorType = type;

        try {
            dir = nodeDir + sensorName + "/";
            path = Paths.get(dir);
            if (Files.notExists(path)) {
                Files.createDirectory(path);
            }

            remRulesDir = dir + "remrules/";
            remRulesPath = Paths.get(remRulesDir);
            if (Files.notExists(remRulesPath)) {
                Files.createDirectory(remRulesPath);
            }

            locRulesDir = dir + "locrules/";
            locRulesPath = Paths.get(locRulesDir);
            if (Files.notExists(locRulesPath)) {
                Files.createDirectory(locRulesPath);
            }

            if (sensorType.equals("Wazuh")) {

                hidsRulesDir = locRulesDir + "rules/";
                hidsRulesPath = Paths.get(hidsRulesDir);
                if (Files.notExists(hidsRulesPath)) {
                    Files.createDirectory(hidsRulesPath);
                }

                hidsDecodersDir = locRulesDir + "decoders/";
                hidsDecodersPath = Paths.get(hidsDecodersDir);
                if (Files.notExists(hidsDecodersPath)) {
                    Files.createDirectory(hidsDecodersPath);
                }
            }

        } catch (Exception e) {
            return;
        }
        
        status = true;
    }
    
    public Boolean getStatus() {
        return status;
    }

    public String getSensorName() {
        return sensorName;
    }

    public String getDir() {
        return dir;
    }

    public Path getPath() {
        return path;
    }

    public String getLocRulesDir() {
        return locRulesDir;
    }

    public Path getLocRulesPath() {
        return locRulesPath;
    }

    public String getRemRulesDir() {
        return remRulesDir;
    }

    public Path getRemRulesPath() {
        return remRulesPath;
    }

    public String getHidsRulesDir() {
        return hidsRulesDir;
    }

    public Path getHidsRulesPath() {
        return hidsRulesPath;
    }

    public String getHidsDecodersDir() {
        return hidsDecodersDir;
    }

    public Path getHidsDecodersPath() {
        return hidsDecodersPath;
    }

    public String getSensorType() {
        return sensorType;
    }
}
