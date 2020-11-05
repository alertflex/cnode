/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alertflex.controller;

import org.alertflex.common.ProjectRepository;
import org.alertflex.filters.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import org.alertflex.entity.Project;
import org.alertflex.entity.Node;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author root
 */
public class RulesManagement {

    private static final Logger logger = LoggerFactory.getLogger(RulesManagement.class);

    private InfoMessageBean eventBean;
    Project project;
    Node node;

    ProjectRepository pr;

    public RulesManagement(InfoMessageBean eb) {
        this.eventBean = eb;
        this.project = eventBean.getProject();

    }

    public void saveRule(String sensor, String rule, String data) {

        try {

            String n = eventBean.getNode();
            String r = eventBean.getRefId();

            node = eventBean.getNodeFacade().findByNodeName(r, n);

            if (node == null) {
                return;
            }

            pr = new ProjectRepository(project);

            boolean ready = pr.initSensor(node, sensor);

            if (data.isEmpty() || !ready) {
                return;
            }

            String rulePath = pr.getRemRulesDir(sensor) + rule;

            Path p = Paths.get(rulePath);

            Files.write(p, data.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

        } catch (IOException e) {
            logger.error("alertflex_ctrl_exception", e);
        }
    }
}
