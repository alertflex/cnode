/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alertflex.mc.tasks;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import hidden.org.elasticsearch.action.search.SearchRequest;
import hidden.org.elasticsearch.action.search.SearchResponse;
import hidden.org.elasticsearch.client.RequestOptions;
import hidden.org.elasticsearch.client.RestClient;
import hidden.org.elasticsearch.client.RestClientBuilder;
import hidden.org.elasticsearch.client.RestHighLevelClient;
import hidden.org.elasticsearch.index.query.*;
import hidden.org.elasticsearch.search.SearchHit;
import hidden.org.elasticsearch.search.SearchHits;
import hidden.org.elasticsearch.search.builder.SearchSourceBuilder;
import javax.net.ssl.SSLContext;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.CertificateException;
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
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.TrustAllStrategy;


/**
 *
 * @author root
 */
public class ElasticTask {
    
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
        
        RestHighLevelClient client;
        
        String rangeFrom = "now-" + Integer.toString(tr) + "s";
        
        try {
            
            String host = project.getElkHost();
            
            if (host.isEmpty()) return false;
            
            String user = project.getElkUser();
            
            String password = project.getElkPass();
            
            String keyStorePass = project.getElkStorepass();
            
            if (!user.isEmpty() && !password.isEmpty()) {
                
                if (!keyStorePass.isEmpty() && !keyStorePass.equals("indef")) {
                    
                    Path keyStorePath = Paths.get(project.getElkKeystore());

                    Path trustStorePath = Paths.get(project.getElkTruststore());
                
                    KeyStore truststore = KeyStore.getInstance("jks");
                    try (InputStream is = Files.newInputStream(trustStorePath)) {
                        truststore.load(is, keyStorePass.toCharArray());
                    } catch (CertificateException e) {
                        logger.error(e.getMessage());
                        return false;
                    } catch (NoSuchAlgorithmException e) {
                        logger.error(e.getMessage());
                        return false;
                    }

                    KeyStore keystore = KeyStore.getInstance("jks");
                    try (InputStream is = Files.newInputStream(keyStorePath)) {
                        keystore.load(is, keyStorePass.toCharArray());
                    } catch (CertificateException e) {
                        logger.error(e.getMessage());
                        return false;
                    } catch (NoSuchAlgorithmException e) {
                        logger.error(e.getMessage());
                        return false;
                    }
                
                    SSLContextBuilder sslBuilder = SSLContexts.custom()
                        .loadTrustMaterial(truststore, null)
                        .loadKeyMaterial(keystore,keyStorePass.toCharArray());

                    final SSLContext sslContext = sslBuilder.build();
                
                    final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
                    credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(user, password));

                    client = new RestHighLevelClient(
                        RestClient.builder(new HttpHost(host, 9200, "https"))
                            .setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
                            @Override
                            public HttpAsyncClientBuilder customizeHttpClient(
                                HttpAsyncClientBuilder httpClientBuilder) {
                                    return httpClientBuilder.setSSLContext(sslContext).setDefaultCredentialsProvider(credentialsProvider);
                                }
                            }
                        )
                    );
                
                } else {
                    
                    final SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, TrustAllStrategy.INSTANCE).build();
                    
                    final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
                    credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(user, password));

                    client = new RestHighLevelClient(RestClient.builder(new HttpHost(host, 9200, "https"))
                        .setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
                            @Override
                            public HttpAsyncClientBuilder customizeHttpClient(
                                HttpAsyncClientBuilder httpClientBuilder) {
                                    return httpClientBuilder.setSSLContext(sslContext)
                                        .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                                        .setDefaultCredentialsProvider(credentialsProvider);
                                }
                    }));
                }
                
            } else {
                
                client = new RestHighLevelClient(
                    RestClient.builder(
                        new HttpHost(host, 9200, "http"),
                        new HttpHost(host, 9201, "http")
                    )
                );
                
            }
            
            SearchRequest searchRequest;
            SearchSourceBuilder searchSourceBuilder;
            SearchResponse searchResponse;
            RangeQueryBuilder rangeQuery;
            TermsQueryBuilder termsQuery;
            ExistsQueryBuilder existsQuery;
            BoolQueryBuilder boolsQurey;
            
            
            switch(typeLog) {
                
                case "netflow":
                    
                    searchRequest = new SearchRequest(index);
                    searchSourceBuilder = new SearchSourceBuilder();

                    rangeQuery = QueryBuilders
                        .rangeQuery("@timestamp")
                        //.from("now-36000s")
                        .from(rangeFrom)
                        .to("now");
                    
                    termsQuery = QueryBuilders.termsQuery("event.category","network_traffic");

                    boolsQurey = QueryBuilders
                        .boolQuery()
                        .must(rangeQuery)
                        .must(termsQuery);
                    
                    break;
                
                case "packetbeat":
                    
                    searchRequest = new SearchRequest(index);
                    searchSourceBuilder = new SearchSourceBuilder();

                    rangeQuery = QueryBuilders
                        .rangeQuery("@timestamp")
                        //.from("now-36000s")
                        .from(rangeFrom)
                        .to("now");

                    termsQuery = QueryBuilders.termsQuery("event.category","network_traffic");

                    boolsQurey = QueryBuilders
                        .boolQuery()
                        .must(rangeQuery)
                        .must(termsQuery);
                    
                    break;
                        
                default:
                    return false;
            }
            
            searchSourceBuilder.query(boolsQurey);
            searchSourceBuilder.from(0);
            searchSourceBuilder.size(flowSize);
            searchRequest.source(searchSourceBuilder);

            searchResponse = client.search(searchRequest, RequestOptions.DEFAULT); 
            
            SearchHits hits = searchResponse.getHits();
            
            String json = null;

            switch(typeLog) {
                
                case "netflow":
                        
                    json = processJsonNetflow(hits);
                    break;
                
                case "packetbeat":
                    
                    json = processJsonPacketbeat(hits);
                    break;
            }
            
            client.close();
            
            if (json != null && !json.isEmpty()) sendJsonData(json);
            
            return true;
            
        } catch (Exception e) {
           logger.error("alertflex_wrk_exception", e);
        }
        
        return false;
    }
    
    public String processJsonNetflow(SearchHits hits) {
        
        String json = "{\"logs\": [";
        
        
        int i = 0;
        for (SearchHit hit : hits.getHits()) {
            // add {"@timestamp":"2019-12-05T11:40:47.005Z", ... }
            json = json + "{\"short_message\":\"netflow-elastic\",\"message\":";
            
            String sourceAsString = hit.getSourceAsString();
            
            if(i++ != hits.getHits().length - 1) json = json + sourceAsString + "},";
            else json = json + sourceAsString + "}";
        }
        
        json = json + "]}";
        
        return json;
    }
    
    public String processJsonPacketbeat(SearchHits hits) {
        
        String json = "{\"logs\": [";
        
        
        int i = 0;
        for (SearchHit hit : hits.getHits()) {
            // add {"@timestamp":"2019-12-05T11:40:47.005Z", ... }
            json = json + "{\"short_message\":\"packet-elastic\",\"message\":";
            
            String sourceAsString = hit.getSourceAsString();
            
            if(i++ != hits.getHits().length - 1) json = json + sourceAsString + "},";
            else json = json + sourceAsString + "}";
        }
        
        json = json + "]}";
        
        return json;
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
