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

package org.alertflex.posture;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Date;
import org.alertflex.controller.InfoMessageBean;
import org.alertflex.entity.Project;
import org.alertflex.entity.Node;
import org.alertflex.entity.PostureTask;
import org.alertflex.supp.ProjectRepository;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import java.util.UUID;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class DockerSbom {

    private static final Logger logger = LogManager.getLogger(DockerSbom.class);

    private InfoMessageBean eventBean;
    Project project;
    Node node;

    public DockerSbom(InfoMessageBean eb) {
        this.eventBean = eb;
        this.project = eventBean.getProject();

    }

    public void saveReport(String results, String target, String uuid, int alertType) {
        
        String r = eventBean.getRefId();
        String n = eventBean.getNode();
        String p = eventBean.getHost() + ".trivy";
        Date date = new Date();

        try {

            ProjectRepository pr = new ProjectRepository(project);
            String posturePath = pr.getCtrlPostureDir() + uuid + ".json";
            Path pp = Paths.get(posturePath);
            Files.write(pp, results.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            
            PostureTask pt = new PostureTask ();
                            
            pt.setRefId(r);
            pt.setNode(n);
            pt.setProbe(p);
            pt.setPostureType("DockerSbom");
            pt.setTarget(target);
            pt.setTaskUuid(uuid);
            pt.setReportAdded(date);
                
            eventBean.getPostureTaskFacade().create(pt);
            
            String trackUrl = project.getTrackUrl();
            String trackKey = project.getTrackKey();
            String trackProject = project.getTrackProject();
            String trackVersion = project.getTrackVersion();
            
            HttpClient client = HttpClientBuilder.create().disableContentCompression().build();
            HttpPost post = new HttpPost(trackUrl);
            String boundary = "---------------"+UUID.randomUUID().toString();
            
            post.setHeader("accept", "application/json");
            post.setHeader("X-Api-Key", trackKey);
            post.setHeader("Content-Type", org.apache.http.entity.ContentType.MULTIPART_FORM_DATA.getMimeType()+"; boundary="+boundary);

            MultipartEntityBuilder builder = MultipartEntityBuilder.create();

            builder.setBoundary(boundary);

            builder.addTextBody("projectName", trackProject, org.apache.http.entity.ContentType.TEXT_PLAIN);
            builder.addTextBody("projectVersion", trackVersion, org.apache.http.entity.ContentType.TEXT_PLAIN);
            builder.addTextBody("autoCreate", "false", org.apache.http.entity.ContentType.TEXT_PLAIN);
            
            File file = new File(posturePath);
            FileBody fileBody = new FileBody(file, org.apache.http.entity.ContentType.APPLICATION_OCTET_STREAM);
            builder.addPart("bom", fileBody);

            final HttpEntity entity = builder.build();

            post.setEntity(entity);
            
            HttpResponse response = client.execute(post);

            if (response != null) {

                String json = EntityUtils.toString(response.getEntity(), "UTF-8");

            } 
        } catch (Exception e) {
            logger.error("alertflex_ctrl_exception", e);
        }
    }
}
