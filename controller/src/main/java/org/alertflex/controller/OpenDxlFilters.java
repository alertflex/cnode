/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alertflex.controller;

import com.opendxl.client.DxlClient;
import com.opendxl.client.DxlClientConfig;
import com.opendxl.client.ServiceRegistrationInfo;
import com.opendxl.client.callback.RequestCallback;
import com.opendxl.client.exception.DxlException;
import com.opendxl.client.message.Message;
import com.opendxl.client.message.Request;
import com.opendxl.client.message.Response;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.jms.BytesMessage;
import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.alertflex.common.DxlCommon;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author root
 */
@Singleton (name="openDxlFilters")
@Startup
public class OpenDxlFilters implements RequestCallback {
    
    private static final Logger logger = LoggerFactory.getLogger(OpenDxlFilters.class);
    
     /** The topic for the service to respond to */
    private static final String SERVICE_TOPIC = "/filters";
    /** The amount of time to wait for operations to complete */
    private static final long TIMEOUT = 10 * 1000; /* 10 seconds */
    
    DxlClient client = null;
    ServiceRegistrationInfo info = null;
       

    public OpenDxlFilters() {
        
        String dxlConfigDir = System.getProperty("OpenDxl", "");
        
        if (!dxlConfigDir.isEmpty()) {

            try {
                // Create DXL configuration from file, for example C:\\\\Temp\\\\dxlconfig\\\\dxlclient.config
                DxlClientConfig config = DxlCommon.getClientConfig(dxlConfigDir);

                // Create the client
                client = new DxlClient(config);

                // Connect to the fabric
                client.connect();
                
                // Create service registration object
                info = new ServiceRegistrationInfo(client, "alertflex");

                // Add a topic for the service to respond to
                info.addTopic(SERVICE_TOPIC, this);
                
                // Register the service with the fabric (wait up to 10 seconds for registration to complete)
                client.registerServiceSync(info, TIMEOUT);
                
                if (client.isConnected()) {
                    String servid = info.getServiceId();
                    String servtype = info.getServiceType();
                }
            } catch (DxlException e) {
                
                logger.error("alertflex_ctrl_exception", e);
                
            } catch (Exception e) {
                
                logger.error("alertflex_ctrl_exception", e);
            }
            
        }
    }
    
    @PreDestroy
    public void destroy() {
        // Clean up
        try {
            if (client != null) {
                
                if (client.isConnected()) client.disconnect();
                
            }
        } catch ( Exception e) {
            logger.error("alertflex_ctrl_exception", e);
        } 
    }

    @Override
    public void onRequest(Request request) {
        try {
            
            String filtersJson = new String(request.getPayload());
            
            try {
            
                JSONObject obj = new JSONObject(filtersJson);
                JSONObject filterObj = obj.getJSONObject("filters");
            
                String ref = filterObj.getString("ref_id");
                String node = obj.getString("node_id");
                            
                if (!ref.isEmpty() && !node.isEmpty() && filterObj != null) {
                    if (uploadFilters(ref, node, filterObj.toString())) {
                        final Response res = new Response(request);
                        res.setPayload("filters OK".getBytes(Message.CHARSET_UTF8));
                        client.sendResponse(res);
                        return;
                    }
                }
                    
            } catch (JSONException e) {
                logger.error("alertflex_ctrl_exception", e);
            }
            
            final Response res = new Response(request);
            res.setPayload("filters error".getBytes(Message.CHARSET_UTF8));
            client.sendResponse(res);
            
        } catch (Exception e) {
            logger.error("alertflex_ctrl_exception", e);
        }
    }
    
    public boolean uploadFilters(String refId, String nodeId, String filters) {
        
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
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // Create the destination (Topic or Queue)
            Destination destination = session.createTopic("jms/altprobe/" + refId);

            // Create a MessageProducer from the Session to the Topic or Queue
            MessageProducer producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
            
            BytesMessage message = session.createBytesMessage();
            
            message.setStringProperty("node_id", nodeId);
            message.setStringProperty("sensor", "all");
            message.setStringProperty("msg_type", "byte");
            message.setStringProperty("content_type", "filters");
            
            byte[] sompFilters = compress(filters);
                       
            message.writeBytes(sompFilters);
            producer.send(message);
            
            // Clean up
            session.close();
            connection.close();
            
            return true;
            
        } catch ( IOException | JMSException e) {
            
            logger.error("alertflex_ctrl_exception", e);
            return false;
        } 
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
