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

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.BytesMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import org.alertflex.entity.Tools;
import org.alertflex.facade.ToolsFacade;
import org.alertflex.supp.LogServer;
import org.json.JSONArray;
import org.json.JSONObject;
import org.alertflex.entity.Alert;
import org.alertflex.facade.AlertFacade;

@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
    @ActivationConfigProperty(propertyName = "destination", propertyValue = "jms/alertflex/log"),
    @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Client-acknowledge")})
public class LogMessageBean implements MessageListener {
    
    @EJB
    private ToolsFacade toolsFacade;
    
    @EJB
    private AlertFacade alertFacade;
    
    public LogMessageBean() {
    }

    @Override
    public void onMessage(Message message) {
        try {
            
            message.acknowledge();

            if (message instanceof BytesMessage) {

                BytesMessage bytesMessage = (BytesMessage) message;

                int msgType = bytesMessage.getIntProperty("msg_type");
                if (msgType != 2) return;
                
                String projectId = bytesMessage.getStringProperty("project_id");
                if (projectId.isEmpty()) return;
                
                byte[] buffer = new byte[(int) bytesMessage.getBodyLength()];
                bytesMessage.readBytes(buffer);

                String data = decompress(buffer);
                
                if (data.isEmpty()) return;
                
                JSONObject obj = new JSONObject(data);
                JSONArray arr = obj.getJSONArray("logs");
                
                Tools tools = toolsFacade.findToolsByProjectId(projectId);
                
                if (tools != null) {
                    
                    LogServer ls = new LogServer(tools);
                    
                    if (ls.getGenericClient() == null) return;

                    for (int i = 0; i < arr.length(); i++) { 
                        
                        String categoryName = arr.getJSONObject(i).getString("category_name");
                        
                        if (categoryName.equals("request_headers")) {
                            Long flowId = arr.getJSONObject(i).getLong("flow_id");
                            Alert a = alertFacade.findAlertsByFlowId(projectId, Long.toString(flowId));
                            if (a != null) {
                                String requestHeaders = arr.getJSONObject(i).getJSONArray("headers").toString();
                                a.setHttpRequestHeaders(requestHeaders);
                                alertFacade.edit(a);
                            }
                        } else {
                            
                            int classUid = arr.getJSONObject(i).getInt("class_uid");
                            
                            switch (classUid) {
                                case 2004 :
                                    ls.SendOcsfToLog(arr.getJSONObject(i).toString(), "ocsf-1.1.0-2004-detection_finding");
                                    break;
                                case 4002 :
                                    ls.SendOcsfToLog(arr.getJSONObject(i).toString(), "ocsf-1.1.0-4002-http_activity");
                                    break;
                                default:
                                    ls.SendOcsfToLog(arr.getJSONObject(i).toString(), "alertflex-");
                                    break;
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            String errorStr = e.toString();
            String errorMsg = e.getMessage();
        }
    }

    public String decompress(byte[] compressed) throws IOException {
        String outStr;
        
        if ((compressed == null) || (compressed.length == 0)) {
            return "";
        }
        if (isCompressed(compressed)) {
            GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(compressed));
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(gis));

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
                sb.append('\n');
            }
            outStr = sb.toString();
        } else {
            outStr = new String(compressed);
        }
        return outStr;
    }

    public boolean isCompressed(byte[] compressed) {
        return (compressed[0] == (byte) (GZIPInputStream.GZIP_MAGIC)) 
            && (compressed[1] == (byte) (GZIPInputStream.GZIP_MAGIC >> 8));
    }
}
