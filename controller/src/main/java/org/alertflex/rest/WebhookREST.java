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
import java.io.ObjectOutputStream;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jms.BytesMessage;
import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import org.alertflex.common.PojoAlertLogic;
import org.alertflex.entity.Playbook;
import org.alertflex.facade.PlaybookFacade;
import org.alertflex.facade.UsersFacade;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Stateless
@Path("webhook")
public class WebhookREST {

    private static final Logger logger = LogManager.getLogger(WebhookREST.class);

    @EJB
    private PlaybookFacade playbookFacade;

    public WebhookREST() {

    }

    @POST
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response push(@PathParam("id") String id, String jsonBody) {

        if (id != null && !id.isEmpty()) {

            Playbook p = playbookFacade.findPlaybookByWebhook(id);

            if (p != null) {
                
                if (jsonBody.isEmpty()) return Response.status(Response.Status.BAD_REQUEST).build();

                sendPlaybookToMQ(p);
                return Response.ok().build();
            }
        }

        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    public void sendPlaybookToMQ(Playbook p) {
    
        PojoAlertLogic a = new PojoAlertLogic();
        
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
            javax.jms.Session session = connection.createSession(false, javax.jms.Session.AUTO_ACKNOWLEDGE);

            // Create the destination (Topic or Queue)
            Destination destination = session.createQueue("jms/alertflex/playbooks");

            // Create a MessageProducer from the Session to the Topic or Queue
            MessageProducer producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
            
            BytesMessage message = session.createBytesMessage();

            message.setStringProperty("ref_id", p.getRefId());
            message.setStringProperty("playbook_id", p.getName());
            message.setStringProperty("report_uuid", "indef");
            
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
                        
            oos.writeObject(a);
            oos.close();
            bos.close();
            
            message.writeBytes(bos.toByteArray());
            producer.send(message);
            
            // Clean up
            session.close();
            connection.close();

        } catch (JMSException e) {
            logger.error("alertflex_ctrl_exception", e);
        } catch (IOException e) {
            logger.error("alertflex_ctrl_exception", e);
        }
    }
}
