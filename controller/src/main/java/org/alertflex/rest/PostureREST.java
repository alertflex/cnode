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

package org.alertflex.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import org.alertflex.entity.PostureTask;
import org.alertflex.supp.PosturePostBody;
import org.alertflex.entity.Project;
import org.alertflex.facade.AlertFacade;
import org.alertflex.facade.PostureTaskFacade;
import org.alertflex.facade.ProjectFacade;
import org.alertflex.supp.ProjectRepository;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.json.JSONObject;

@Stateless
@Path("posture")
public class PostureREST {

    private static final Logger logger = LogManager.getLogger(PostureREST.class);
    
    @EJB
    private ProjectFacade projectFacade;
    
    @EJB
    private AlertFacade alertFacade;
    
    @EJB
    private PostureTaskFacade postureTaskFacade;

    public PostureREST() {

    }
    
    @GET
    @Path("/json/{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response json(@PathParam("id") String id, @Context SecurityContext sc) {
        
        String report = "";
        
        if (id != null && !id.isEmpty()) {
            
            PostureTask pt = postureTaskFacade.findPosture(id);
            
            if (pt == null) {
            
                report = "wait";
            

                return Response
                    .status(Response.Status.NO_CONTENT)
                    .entity(report)
                    .build();
            }
            
            Project p = projectFacade.findProjectByRef(pt.getRefId());
            
            if (p == null) return Response.status(Response.Status.BAD_REQUEST).build();
            
            
            try {
                ProjectRepository pr = new ProjectRepository(p);
                
                String posturePath = pr.getCtrlPostureDir() + id + ".json";
                if (pt.getPostureType().equals("Nmap")) posturePath = pr.getCtrlPostureDir() + id + ".xml";
                
                java.nio.file.Path fp = Paths.get(posturePath);
                InputStream in = Files.newInputStream(fp);
            
                StringBuilder textBuilder = new StringBuilder();
                try (Reader reader = new BufferedReader(new InputStreamReader(in, Charset.forName(StandardCharsets.UTF_8.name())))) {
                    int c = 0;
                    while ((c = reader.read()) != -1) {
                        textBuilder.append((char) c);
                    }
                }
                
                return Response
                    .status(Response.Status.OK)
                    .entity(textBuilder.toString())
                    .build();
           
            
            } catch (IOException ex) {
                
            }
            
        }

        return Response.status(Response.Status.BAD_REQUEST).build();
    }
    
       
    @GET
    @Path("/alerts/{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response alerts(@PathParam("id") String id, @Context SecurityContext sc) {
        
        String report = "";

        if (id == null || id.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
            
        PostureTask pt = postureTaskFacade.findPosture(id);
            
        if (pt == null) {
            
            report = "wait";
            

            return Response
                .status(Response.Status.NO_CONTENT)
                .entity(report)
                .build();
        }
            
        Project p = projectFacade.findProjectByRef(pt.getRefId());
            
        if (p == null) return Response.status(Response.Status.BAD_REQUEST).build();
        
        Long sum_sev0 = 0L;
        Long sum_sev1 = 0L;
        Long sum_sev2 = 0L;
        Long sum_sev3 = 0L;
        
        List<Object> result = alertFacade.findCountSeverities(p.getRefId(), id);
        
        if (result != null) {

            for (Iterator i = result.iterator(); i.hasNext(); ) {
                
                Object[] objArr = (Object[]) i.next();
                
                Object objSev = objArr[0];
                Integer sev = (Integer) objSev;
                
                Object objCounter = objArr[1];
                Long counter = (Long) objCounter;
                
                switch (sev) {
                    case 0: sum_sev0 = counter;
                        break;
                    case 1: sum_sev1 = counter;
                        break;
                    case 2: sum_sev2 = counter;
                        break;
                    case 3: sum_sev3 = counter;
                        break;
                }
            }
        }
        
        JSONObject obj = new JSONObject();
            
        obj.put("probe", pt.getProbe());
        obj.put("type", pt.getPostureType());
        obj.put("date", pt.getReportAdded());
        obj.put("alert_critical", Long.toString(sum_sev3));
        obj.put("alert_major", Long.toString(sum_sev2));
        obj.put("alert_minor", Long.toString(sum_sev1));
        obj.put("alert_info", Long.toString(sum_sev0));

        String alerts = obj.toString();
            
        return Response
            .status(Response.Status.OK)
            .entity(alerts)
            .build();
    }
    
    @POST
    @Consumes("application/json")
    public Response posture(PosturePostBody postureBody) {
        
        Integer delay = postureBody.getDelay();
        String postureType = postureBody.getPostureType();
        String alertCorr = postureBody.getAlertCorr();
        String target = postureBody.getTarget();
        String node = postureBody.getVrn();
        String host = postureBody.host;
        String projectId = postureBody.getProject();
        
        if (postureType.isEmpty() || target.isEmpty() || node.isEmpty() || host.isEmpty() || projectId.isEmpty()) {
            Response.status(404).entity("Error parameters").build();
        }
        
        Project project = projectFacade.findProjectByRef(projectId);
        
        if (project == null) return Response.status(404).entity("Error proectId").build();
        
        int postureParam = 0;
        switch (postureType) {
            case "AppSecret" : postureParam = 4;
                break;
            case "DockerConfig" : postureParam = 5;
                break;
            case "K8sConfig" : postureParam = 6;
                break;
            case "AppVuln" : postureParam = 7;
                break;
            case "DockerVuln" : postureParam = 8;
                break;
            case "K8sVuln" : postureParam = 9;
                break;    
            case "AppSbom" : postureParam = 10;
                break;
            case "DockerSbom" : postureParam = 11;
                break;
            case "CloudFormation" : postureParam = 12;
                break;
            case "Terraform" : postureParam = 13;
                break;
            case "KubeHunter" : postureParam = 14;
                break;
            case "ZAP" : postureParam = 15;
                break;
            case "Nmap" : postureParam = 16;
                break;
            case "Nuclei" : postureParam = 17;
                break;
            case "Nikto" : postureParam = 18;
                break;
            case "CloudSploit" : postureParam = 19;
                break;
            case "Semgrep" : postureParam = 20;
                break;
            case "SonarQube" : postureParam = 21;
                break;
            default:
                postureParam = 0;
        }
        
        if (postureParam == 0) return Response.status(404).entity("Error").build();
        
        String postureId = sendToScanner(project, postureBody, postureParam, alertCorr);
        String jsonRespons = "";
        
        if (isValidUUID(postureId)) {
            
            jsonRespons = "{ \"taskId\": \"" + postureId + "\"}";
            
            return Response.status(200).entity(jsonRespons).build();
        }
        else {
            
            jsonRespons = "{ \"error\": \"" + postureId + "\"}";
            
            return Response.status(404).entity(jsonRespons).build();
        }
    }
    
    private final static Pattern UUID_REGEX_PATTERN =
        Pattern.compile("^[{]?[0-9a-fA-F]{8}-([0-9a-fA-F]{4}-){3}[0-9a-fA-F]{12}[}]?$");
 
    public static boolean isValidUUID(String str) {
        if (str == null) {
            return false;
        }
        return UUID_REGEX_PATTERN.matcher(str).matches();
    }
    
    public String sendToScanner(Project p, PosturePostBody pb, int postureType, String alertCorr) {
        
        int wait = pb.getDelay();
        if (wait == 0) wait = 60; //1 min
        
        String msg = "";
        
        msg = "{\"action\": \"scan\", \"target\": {\"device\": {\"device_id\": \"" + 
            pb.getTarget() + "\" } }, \"actuator\": {\"x-alertflex\": {\"project\": \"" +
            pb.getProject() + "\"} }, \"args\": { \"delay\": " +
            Long.toString(wait) +  ",\"posture_type\": " +
            Integer.toString(postureType) + ",\"alert_corr\": \"" +
            alertCorr + "\"} }";
            
        
        
        return sendRequest(p.getRefId(), pb.getVrn(), pb.getHost(), msg, wait);
    }
    
    public String sendRequest(String ref, String node, String host, String msg, int wait) {
        
        String result = "error";

        try {

            String strConnFactory = System.getProperty("AmqUrl", "");
            String user = System.getProperty("AmqUser", "");
            String pass = System.getProperty("AmqPwd", "");

            // Create a ConnectionFactory
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(strConnFactory);

            // Create a Connection
            Connection connection = connectionFactory.createConnection(user,pass);
            connection.start();

            // Create a Session
            Session session = connection.createSession(false, javax.jms.Session.AUTO_ACKNOWLEDGE);
            
            // create query for request
            Destination commandDest = session.createQueue("jms/altprobe/" + node + "/" + host + "/scanners");
            MessageProducer commandProducer = session.createProducer(commandDest);

            // create query for response
            Destination tempDest = session.createTemporaryQueue();
            MessageConsumer responseConsumer = session.createConsumer(tempDest);
            
            TextMessage message = session.createTextMessage(msg);

            // send a request..
            message.setJMSReplyTo(tempDest);
            String correlationId = UUID.randomUUID().toString();
            message.setJMSCorrelationID(correlationId);

            commandProducer.send(message);

            // read response
            javax.jms.Message response = responseConsumer.receive(wait*1000);
            
            if (response != null && response instanceof TextMessage) {
                String rst = ((TextMessage) response).getText();
                
                JSONObject obj = new JSONObject(rst);
                Integer code = obj.getInt("status");
                result = obj.getString("request_id");
                
                if (code < 200 || code > 206) result  = obj.getString("status_text");
                
                if (!result.equals(correlationId)) result  = "result UUID mispatch";
                
            } else {
                result  = "wrong response";
            }
            
            session.close();
            connection.close();

        } catch ( JMSException e) {
            logger.error("alertflex_mc_exception", e);
            result  = "wrong request";
        }
        
        return result;
    }
}


