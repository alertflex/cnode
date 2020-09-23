
package org.alertflex.mc.tasks;

import com.twilio.Twilio;
import com.twilio.type.PhoneNumber;
import java.util.List;
import javax.ejb.EJB;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Properties;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import org.alertflex.mc.db.Project;
import org.alertflex.mc.db.Users;
import org.alertflex.mc.db.Alert;
import org.alertflex.mc.services.AlertFacade;
import org.alertflex.mc.services.ProjectFacade;
import org.alertflex.mc.services.ResponseFacade;
import org.alertflex.mc.services.UsersFacade;
import org.alertflex.mc.supp.CiTools;
import org.alertflex.mc.db.Response;
import org.alertflex.mc.services.CredentialFacade;
import org.alertflex.mc.redis.FromJedisPool;
import redis.clients.jedis.Jedis;
import org.slf4j.LoggerFactory;


/**
 * @author Oleg Zharkov
 */
@MessageDriven(activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
        @ActivationConfigProperty(propertyName = "destination", propertyValue = "jms/alertflex/responses"),
})
@Stateless
public class ResponseMessageBean implements MessageListener {
    
    @Inject
    @FromJedisPool
    Jedis jedisFromPool;
    
    @EJB
    private ProjectFacade projectFacade;
    List<Project> projectList;
    
    @EJB
    private CredentialFacade credentialFacade;
    CiTools cipher;
    
    @EJB
    private UsersFacade usersFacade;
    
    @EJB
    private AlertFacade alertFacade;
    
    @EJB
    private ResponseFacade responseFacade;
    
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ResponseMessageBean.class);
    
    @Override
    public void onMessage(javax.jms.Message msg) {
        
        try {
            
            if (msg instanceof TextMessage) {
                
                TextMessage message = (TextMessage) msg;
            
                String refId = message.getStringProperty("ref_id");
                String responseId = message.getStringProperty("response_id");
                String alertUuid = message.getStringProperty("alert_uuid");
            
                Response response = responseFacade.findResponseByName(refId, responseId);
                Alert alert = alertFacade.findAlertByUuid(alertUuid);
                
                if(response != null && alert != null) {
                    
                    // check if response should aggregated
                    if (response.getAggrReproduced() != 0 && response.getAggrInperiod() != 0) {
                        
                        if (aggregateResponse(response)) {
                            
                            alert.setStatus("aggregated");
                            
                            if (response.getAction().equals("remove")) alert.setAction(response.getResId());
                            
                            createResponse(response, alert);
                            
                        } else {
                            
                            if (response.getAction().equals("remove")) alertFacade.remove(alert);
                       
                        }
                    } else {
                        
                        createResponse(response, alert);
                        
                    }
                }
            }
        } catch (Exception e) {
            logger.error("alertflex_mc_exception", e);
        }
    } 
    
    public boolean aggregateResponse(Response res) {
        
        // return true if aggregation is finished and alert should be arised
        
        if (jedisFromPool != null) {
                            
            String key = res.getRefId();
            String fieldReproduced = "reproduced_" + res.getResId();
            String fieldPeriod = "period_" + res.getResId();
            long periodResponse = (long) res.getAggrInperiod();
            long reproducedResponse = (long) res.getAggrReproduced();
            
            long currentTime = (new Date()).getTime();
            long counterTime = 0;
            long counter = 0;
            
            boolean firstTime = true;
            
            if (jedisFromPool.hexists(key, fieldReproduced)) {
                firstTime = false;
            }
            
            if (jedisFromPool.hexists(key,  fieldPeriod)) {
                firstTime = false;
            }
            
            if(!firstTime) {
            
                String value = jedisFromPool.hget(key, fieldReproduced);
                if (!value.equals("nil")) {
                    counter = Long.parseLong(value);
                } 
            
                value = jedisFromPool.hget(key, fieldPeriod);
                if (!value.equals("nil")) {
                    counterTime = Long.parseLong(value);
                } 
            }
            
            if (!firstTime && counter != 0 && counterTime != 0) {
                
                counter++;
                counterTime = counterTime + periodResponse*1000;
                
                if (counter >= res.getAggrReproduced()) {
                    
                    //clear hash
                    jedisFromPool.hdel(key, fieldReproduced);
                    jedisFromPool.hdel(key, fieldPeriod);
                    
                    if (currentTime <= counterTime) {
                        return true;
                    } 
                    
                    jedisFromPool.hincrBy(key, fieldReproduced, 1);
                    jedisFromPool.hincrBy(key, fieldPeriod, currentTime);
                    
                } else {
                    
                    if (currentTime > counterTime) {
                        //clear hash
                        jedisFromPool.hdel(key, fieldReproduced);
                        jedisFromPool.hdel(key, fieldPeriod);
                        // create hash
                        jedisFromPool.hincrBy(key, fieldReproduced, 1);
                        jedisFromPool.hincrBy(key, fieldPeriod, currentTime);
                        
                    } else {
                        // increase counter
                        jedisFromPool.hincrBy(key, fieldReproduced, 1);
                    }
                }
                
            } else {
                //clear hash
                jedisFromPool.hdel(key, fieldReproduced);
                jedisFromPool.hdel(key, fieldPeriod);
                // create hash
                jedisFromPool.hincrBy(key, fieldReproduced, 1);
                jedisFromPool.hincrBy(key, fieldPeriod, currentTime);
            }
        }
        
        return false;
    }
    
    
    public void createResponse(Response res, Alert a) {
        
        if (a != null && res != null) {
                
            // send notification
            sendNotification(res, a);
            
            String action = res.getAction();
            
            // action
            if(action.equals("indef") || action.isEmpty()) return;
            
            
            if(!res.getCatNew().isEmpty() && !res.getCatNew().equals("indef")) {
                
                String cat = a.getCategories() + ", " + res.getCatNew();
            }
            
            switch (action)  {
                
                case "confirm" : 
                    a.setStatus("confirmed");
                    break;
                    
                case "incident" : 
                    a.setStatus("incident");
                    break;
                        
                case "remove" : 
                    alertFacade.remove(a);
                    return;
                    
                default:
                    break;
            }
            
            a.setAction(res.getResId());
            alertFacade.edit(a);
        } 
    }
    
    public void sendNotification(Response r, Alert a) {
        
        try {
        
            Project project = projectFacade.find(a.getRefId());
            CiTools cipher = new CiTools(project.getName());
        
            String textMessage = r.getNotifyMsg();
        
            textMessage = textMessage + "\n\n" + 
                "Alert: " + a.getEventId() + "\n" +
                "Severity: " + a.getAlertSeverity() + "\n" +
                "Source: " + a.getAlertSource() + "\n" +
                "Category: " + a.getCategories() + "\n" +
                "Description: " + a.getDescription();
        
            if (r.getSendSlack() != 0) sendSlack(project, cipher, textMessage);
            
            sendMail(project, cipher, textMessage, r);
            
            sendSms(project, cipher, textMessage, r);
            
        } catch (Exception e) {
            logger.error("alertflex_mc_exception", e);
        }
    }
    
    
    public void sendSms(Project project, CiTools cipher, String textMessage, Response r) {
        
        String accountSid = project.getSmsAccount();
        String authToken = cipher.decrypt(project.getSmsToken());
        String from = project.getSmsFrom();
       
        if (accountSid.isEmpty() || authToken.isEmpty() || from.isEmpty()) return;
       
        Twilio.init(accountSid, authToken);
        
        String[] selectedUsers = r.getNotifyUsers().split(",");
        
        for (String u: selectedUsers) {
            
            Users user = usersFacade.findUserById(project.getRefId(), u);
            
            String to = user.getMobile();
            
            if (to != null && !to.isEmpty() && user.getSendSms() != 0) {
        
                com.twilio.rest.api.v2010.account.Message message = com.twilio.rest.api.v2010.account.Message
                    .creator(new PhoneNumber(to),  // to
                        new PhoneNumber(from),  // from
                        textMessage)
                    .create();
                
                String id = message.getSid();
            }
        }
    }
    
    public void sendMail(Project project, CiTools cipher, String textMessage, Response r) {
        
        try {
            
            final String username = project.getMailUser();
            final String password = cipher.decrypt(project.getMailPass());
            String from = project.getMailFrom();

            Properties prop = new Properties();
            prop.put("mail.smtp.host", project.getMailSmtp());
            prop.put("mail.smtp.port", project.getMailPort());
            prop.put("mail.smtp.auth", "true");
            prop.put("mail.smtp.socketFactory.port", project.getMailPort());
            prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            
            Session session = Session.getInstance(prop, new javax.mail.Authenticator() {
                @Override
		protected PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(username, password);
		}
            });
            
            String[] selectedUsers = r.getNotifyUsers().split(",");
            
            for (String u: selectedUsers) {
                
                String to = usersFacade.findUserById(project.getRefId(), u).getEmail();
                
                if (to != null && !to.isEmpty()) {
                    
                    Message message = new MimeMessage(session);
                    message.setFrom(new InternetAddress(from));
                    
                    message.setRecipients(
                        Message.RecipientType.TO,
                        InternetAddress.parse(to)
                    );
            
                    message.setSubject("Alertflex notification");
                    message.setText(textMessage);

                    Transport.send(message);
            
                }
            }
        } catch (MessagingException e) {
            logger.error("alertflex_mc_exception", e);
        }
    }
    
    public void sendSlack(Project project, CiTools cipher, String textMessage) {
        
        String slackHook = cipher.decrypt(project.getSlackHook());
       
        if (!slackHook.isEmpty()) {
                    
            try {

                URL slackUrl = new URL(slackHook);
                HttpURLConnection urlConnection = (HttpURLConnection) slackUrl.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestMethod("POST");
                OutputStream os = urlConnection.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
                osw.write("{\"text\":\"" + textMessage + "\"}");
                osw.flush();
                osw.close();
                os.close();
                urlConnection.getResponseCode();
                urlConnection.getResponseMessage();
                urlConnection.disconnect();

            } catch (IOException e) {
                logger.error("alertflex_mc_exception", e);
            }
        }
    }
}
