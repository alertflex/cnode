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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.zip.GZIPOutputStream;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jms.BytesMessage;
import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
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
import org.alertflex.entity.Hosts;
import org.alertflex.entity.Node;
import org.alertflex.entity.Probe;
import org.alertflex.entity.Project;
import org.alertflex.facade.HostsFacade;
import org.alertflex.facade.NodeFacade;
import org.alertflex.facade.ProbeFacade;
import org.alertflex.facade.ProjectFacade;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@Stateless
@Path("vrn")
public class VrnREST {

    private static final Logger logger = LogManager.getLogger(VrnREST.class);
    private static final long TIMEOUT = 10 * 1000;
    
    @EJB
    private ProjectFacade projectFacade;
    
    @EJB
    private NodeFacade nodeFacade;
    
    @EJB
    private ProbeFacade probeFacade;
    
    @EJB
    private HostsFacade hostsFacade;
    
    public VrnREST() {

    }
    
    @GET
    @Path("/status/{projectId}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response status(@PathParam("projectId") String id, @Context SecurityContext sc) {
        
        if (id == null || id.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        
        List<Probe> probeList = probeFacade.findProbesByRef(id);
        
        if (probeList == null || probeList.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        
        JSONArray array = new JSONArray();
        
        try {
        
            for (Probe p: probeList) {
            
                JSONObject obj = new JSONObject();
            
                obj.put("vrn", p.getProbePK().getNode());
                obj.put("probe", p.getProbePK().getName());
                obj.put("type", p.getProbeType());
                obj.put("host", p.getHostName());
                
                String status;
                switch(p.getStatus()) {
                    case 0 : status = "disable";
                        break;
                    case 1 : status = "active";
                        break;
                    case 2 : status = "inactive";
                        break;
                    default: status = "disable";
                        break;
                }
                
                obj.put("status", status);
                obj.put("update", p.getStatusUpdated());
            
                array.put(obj);
            }
        } catch (JSONException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        
        String status = array.toString();
            
        return Response
            .status(Response.Status.OK)
            .entity(status)
            .build();
    }
    
    @POST
    @Path("/filters/{vrnName}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response filters(@PathParam("vrnName") String vrnName, String postBody) {
        
        JSONObject filters = new JSONObject(postBody);
        String projectId = filters.getString("ref_id");
        String node = vrnName;
        
        Project project = projectFacade.findProjectByRef(projectId);
        
        if (project == null) return Response.status(404).entity("Error proectId").build();
        
        if (node == null || projectId == null || filters == null) {
            Response.status(404).entity("Error parameters").build();
        }
        
        if (node.isEmpty() || projectId.isEmpty() || filters.isEmpty()) {
            Response.status(404).entity("Error parameters").build();
        }
        
        Node n = nodeFacade.findByNodeName(projectId, node);
        
        if (n == null || n.getFiltersControl() == 0) {
            Response.status(404).entity("Error parameters").build();
        }
        
        List<Hosts> listHosts = hostsFacade.findHostsByNode(projectId, node);
        
        if (listHosts == null) return Response.status(404).entity("Error vrn").build();
        
        for (Hosts h: listHosts) {
            if (h.getHostType().equals("collector"))
            sendFilters(projectId, node, h.getName(), postBody);
        }
        
        return Response
            .status(Response.Status.OK)
            .build();
    }
    
    private final static Pattern UUID_REGEX_PATTERN =
        Pattern.compile("^[{]?[0-9a-fA-F]{8}-([0-9a-fA-F]{4}-){3}[0-9a-fA-F]{12}[}]?$");
 
    public static boolean isValidUUID(String str) {
        if (str == null) {
            return false;
        }
        return UUID_REGEX_PATTERN.matcher(str).matches();
    }
    
    public boolean sendFilters(String ref, String node, String probe, String filters) {
        
        try {

            String strConnFactory = System.getProperty("AmqUrl", "");
            String user = System.getProperty("AmqUser", "");
            String pass = System.getProperty("AmqPwd", "");

            // Create a ConnectionFactory
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(strConnFactory);

            // Create a Connection
            Connection connection = connectionFactory.createConnection(user, pass);
            connection.start();

            // Create a Session
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // Create the destination (Topic or Queue)
            Destination destination = session.createQueue("jms/altprobe/" + node + "/" + probe + "/sensors");

            // Create a MessageProducer from the Session to the Topic or Queue
            MessageProducer producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
            
            // create tmp query for response   
            Destination tempDest = session.createTemporaryQueue();
            MessageConsumer responseConsumer = session.createConsumer(tempDest);

            BytesMessage msg = session.createBytesMessage();
            
            msg.setStringProperty("ref_id", ref);
            msg.setStringProperty("content_type", "filters");
            
            // send a request..
            msg.setJMSReplyTo(tempDest);
            String correlationId = UUID.randomUUID().toString();
            msg.setJMSCorrelationID(correlationId);
            
            byte[] sompFilters = compress(filters);

            msg.writeBytes(sompFilters);
            producer.send(msg);
            
            // read response
            javax.jms.Message response = responseConsumer.receive(TIMEOUT);
                
            String res = "";

            if (response == null) {
                return false;
            }

            // Clean up
            session.close();
            connection.close();

        } catch (IOException | JMSException e) {
            return false;
        }
        
        return true;
    }
    
    public byte[] compress(String str) throws IOException {

        if ((str == null) || (str.length() == 0)) {
            return null;
        }

        ByteArrayOutputStream obj = new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(obj);
        gzip.write(str.getBytes("UTF-8"));
        gzip.flush();
        gzip.close();
        return obj.toByteArray();
    }
}


