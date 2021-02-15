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

package org.alertflex.controller;

import com.opendxl.client.DxlClient;
import com.opendxl.client.DxlClientConfig;
import com.opendxl.client.ServiceRegistrationInfo;
import com.opendxl.client.callback.RequestCallback;
import com.opendxl.client.exception.DxlException;
import com.opendxl.client.message.Message;
import com.opendxl.client.message.Request;
import com.opendxl.client.message.Response;
import java.util.UUID;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.alertflex.common.DxlCommon;
import org.alertflex.facade.SensorFacade;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton(name = "openDxlAltprobe")
@Startup
public class OpenDxlAltprobe implements RequestCallback {

    private static final Logger logger = LoggerFactory.getLogger(OpenDxlAltprobe.class);
    
    @EJB
    private SensorFacade sensorFacade;

    /**
     * The amount of time to wait for operations to complete
     */
    private static final long TIMEOUT = 10 * 1000;
    /* 10 seconds */
    
    private static final int RECONNECT = 3;

    DxlClient client = null;
    ServiceRegistrationInfo info = null;

    public OpenDxlAltprobe() {
        
        String dxlConfigDir = System.getProperty("OpenDxl", "");

        if (!dxlConfigDir.isEmpty()) {

            try {
                // Create DXL configuration from file, for example C:\\\\Temp\\\\dxlconfig\\\\dxlclient.config
                DxlClientConfig config = DxlCommon.getClientConfig(dxlConfigDir);
                
                config.setConnectRetries(RECONNECT);

                // Create the client
                client = new DxlClient(config);
                
                // Connect to the fabric
                client.connect();

                // Create service registration object
                info = new ServiceRegistrationInfo(client, "alertflex");

                // Add a topic for the service to respond to
                info.addTopic("/altprobe", this);

                // Register the service with the fabric (wait up to 10 seconds for registration to complete)
                client.registerServiceSync(info, TIMEOUT);
                
                String servid = "";
                if (client.isConnected()) {
                    servid = info.getServiceId();
                }
            } catch (DxlException e) {
                destroy();
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

                if (client.isConnected()) {
                    client.disconnect();
                }

            }
        } catch (Exception e) {
            logger.error("alertflex_ctrl_exception", e);
        }
    }

    @Override
    public void onRequest(Request request) {
        try {
            
            String req = new String(request.getPayload());
            
            JSONObject obj = new JSONObject(req);
            
            JSONObject actuator = obj.getJSONObject("actuator");
            
            if (!actuator.isNull("x-alertflex")) {
                JSONObject xAlertflex = actuator.getJSONObject("x-alertflex");
                String tenant = xAlertflex.getString("tenant");
                String node = xAlertflex.getString("node");
                String probe = xAlertflex.getString("probe");
                
                //Sensor s = sensorFacade.findSensorByProbe(tenant, node, probe);
                
                //if (s != null) {
                    String msgBody = sendCommand(node, probe, req);
                    final Response res = new Response(request);
                    res.setPayload(msgBody.getBytes(Message.CHARSET_UTF8));
                    client.sendResponse(res);
                    return;
                //}
            }

            final Response res = new Response(request);
            res.setPayload("Request Error".getBytes(Message.CHARSET_UTF8));
            client.sendResponse(res);
            return;
            
        } catch (Exception e) {
            logger.error("alertflex_ctrl_exception", e);
        }
    }

    public String sendCommand(String node, String probe, String msgBody) {

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
            Destination commandDest = session.createQueue("jms/altprobe/" + node + "/" + probe);
            MessageProducer commandProducer = session.createProducer(commandDest);

            // create tmp query for response   
            Destination tempDest = session.createTemporaryQueue();
            MessageConsumer responseConsumer = session.createConsumer(tempDest);
            
            TextMessage message = session.createTextMessage(msgBody);
            
            // send a request..
            message.setJMSReplyTo(tempDest);
            String correlationId = UUID.randomUUID().toString();
            message.setJMSCorrelationID(correlationId);

            commandProducer.send(message);

            // read response
            javax.jms.Message response = responseConsumer.receive(TIMEOUT);

            if (response != null && response instanceof TextMessage) {
                String res = ((TextMessage) response).getText();
                session.close();
                connection.close();
                return res;
            } else {
                session.close();
                connection.close();
                return "Request Error";
            }
            
        } catch (JMSException e) {

            logger.error("alertflex_ctrl_exception", e);
            return "Request Error";
        }
    }
}
