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
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import org.alertflex.common.DxlCommon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author root
 */
@Singleton(name = "openDxlResponse")
@Startup
public class OpenDxlResponse implements RequestCallback {

    private static final Logger logger = LoggerFactory.getLogger(OpenDxlResponse.class);

    /**
     * The topic for the service to respond to
     */
    private static final String SERVICE_TOPIC = "/response";
    /**
     * The amount of time to wait for operations to complete
     */
    private static final long TIMEOUT = 10 * 1000;
    /* 10 seconds */

    DxlClient client = null;
    ServiceRegistrationInfo info = null;

    public OpenDxlResponse() {

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

            String test = new String(request.getPayload());

            final Response res = new Response(request);
            res.setPayload("response OK".getBytes(Message.CHARSET_UTF8));

            client.sendResponse(res);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
