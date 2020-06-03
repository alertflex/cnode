/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alertflex.mc.tasks;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.codec.binary.Base64;
import java.io.InputStream;
import java.util.TimeZone;
import java.util.zip.GZIPOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.jms.BytesMessage;
import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import org.alertflex.mc.db.Project;
import org.alertflex.mc.db.ScanJob;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.json.JSONArray;
import org.json.JSONObject;


/**
 *
 * @author root
 */
public class GraylogTask {
    
    private static final Logger logger = LoggerFactory.getLogger(ElasticTask.class);
    
    Project project;
    
    public Boolean run(Project p, ScanJob sj) throws Exception {
        
        project = p;
        
        String typeLog = sj.getValue1();
        String index = sj.getValue2();
        Integer size = sj.getValue3();
        
        return getScans(typeLog, index, size, p.getLogrepTimerange());
        
    }
    
    public boolean getScans(String typeLog, String index, int flowSize, int tr) {
        
        
        Date end = new Date();
        Date start = new Date (end.getTime() - tr*1000);
        
        String myDateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
        SimpleDateFormat customFormat = new SimpleDateFormat(myDateFormat );
        customFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        // customFormat.setLenient(false);
        
        String host = "";
        Integer port = 0;
        String user = "";
        String password = "";
        
        try {
            
            
            host = project.getGraylogHost();
            
            port = project.getGraylogPort();
            
            if (host.isEmpty()) return false;
            
            user = project.getGraylogUser();
            
            password = project.getGraylogPass();
            
            if (!user.isEmpty() && !password.isEmpty()) {
            
                
                String json = "";
                String webPage = "";
                            
                switch(typeLog) {
                
                    case "netflow":
                        
                        webPage = "http://"
                        + host
                        + ":"
                        + Integer.toString(port)
                        + "/api/search/universal/absolute?query=filebeat_input_type:netflow%20%26%26%20filebeat_project_ref_id:"
                        + project.getRefId()
                        + "&from="
                        +  customFormat.format(start)
                        + "&to="
                        +  customFormat.format(end)
                        + "&limit=" 
                        + Integer.toString(flowSize)
                        + "&sort=bytes:desc&decorate=false";
                    
                        break;
                
                    case "packetbeat":
                        
                        webPage = "http://"
                        + host
                        + ":"
                        + Integer.toString(port)
                        + "/api/search/universal/absolute?query=packetbeat_event_action:network_flow%20%26%26%20packetbeat_project_ref_id:"
                        + project.getRefId()
                        + "&from="
                        +  customFormat.format(start)
                        + "&to="
                        +  customFormat.format(end)
                        + "&limit=" 
                        + Integer.toString(flowSize)
                        + "&sort=bytes:desc&decorate=false";
                    
                        break;
                        
                    default:
                        return false;
                }
                
                try {
                
                    String authString = user + ":" + password;
                    byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
                    String authStringEnc = new String(authEncBytes);
            
                    URL url = new URL(webPage);
                    URLConnection urlConnection = url.openConnection();
                    urlConnection.setRequestProperty("Authorization", "Basic " + authStringEnc);
                    urlConnection.setRequestProperty("Content-Type", "application/json");
                    urlConnection.setRequestProperty("Accept", "application/json");
                    InputStream is = urlConnection.getInputStream();
                    InputStreamReader isr = new InputStreamReader(is);

                    int numCharsRead;
                    char[] charArray = new char[1024];
                    StringBuffer sb = new StringBuffer();
			
                    while ((numCharsRead = isr.read(charArray)) > 0) {
                        sb.append(charArray, 0, numCharsRead);
                    }
            
                    json = sb.toString();
            
                } catch (MalformedURLException e) {
                    logger.error("alertflex_wrk_exception", e);
                    return false;
                } catch (IOException e) {
                    logger.error("alertflex_wrk_exception", e);
                    return false;
                }
            
                if (json == null || json.isEmpty()) return false;
                
                return reassemblingJson(typeLog, json);
            }
        } catch (Exception e) {
           logger.error("alertflex_wrk_exception", e);
        }
        
        return false;
    }
    
    public boolean reassemblingJson(String typeLog, String json) {
        
        JSONObject obj = new JSONObject(json);
        JSONArray arr = obj.getJSONArray("messages");
        
        JSONObject res = new JSONObject();
        JSONArray logs = new JSONArray();
        
        for (int i = 0; i < arr.length(); i++) {
            
            JSONObject message = new JSONObject();
            
            switch(typeLog) {
                
                    case "netflow":
                        message.put("short_message", "netflow_graylog");
                        break;
                
                    case "packetbeat":
                        message.put("short_message", "packet_graylog");
                        break;
                        
                    default:
                        return false;
            }
            
            message.put("message", arr.getJSONObject(i).getJSONObject("message"));
            
            logs.put(message);
            
        }
        
        res.putOpt("logs", logs);    
        
        return sendJsonData(res.toString());
    }
    
    public boolean sendJsonData(String json) {
        
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
            Destination destination = session.createQueue("jms/alertflex/info");

            // Create a MessageProducer from the Session to the Topic or Queue
            MessageProducer producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
            
            BytesMessage message = session.createBytesMessage();
            
            message.setIntProperty("msg_type", 2);
            message.setStringProperty("node_id", "controller");
            message.setStringProperty("ref_id", project.getRefId());
            
            byte[] jsonLogs = compress(json);
                       
            message.writeBytes(jsonLogs);
            producer.send(message);                   
            
            // Clean up
            session.close();
            connection.close();
            
            return true;
                
        } catch ( IOException | JMSException e) {
            logger.error("alertflex_wrk_exception", e);
        }
        
        return false;
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
