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

package org.alertflex.supp;

import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.transport.OpenSearchTransport;
import org.opensearch.client.transport.httpclient5.ApacheHttpClient5TransportBuilder;
import org.opensearch.client.opensearch.generic.OpenSearchGenericClient;
import org.opensearch.client.opensearch.generic.Requests;
import org.opensearch.client.opensearch.generic.Response;
import org.apache.hc.client5.http.auth.AuthScope;
import org.apache.hc.client5.http.auth.UsernamePasswordCredentials;
import org.apache.hc.client5.http.impl.auth.BasicCredentialsProvider;
import org.apache.hc.client5.http.impl.nio.PoolingAsyncClientConnectionManager;
import org.apache.hc.client5.http.impl.nio.PoolingAsyncClientConnectionManagerBuilder;
import org.apache.hc.client5.http.ssl.ClientTlsStrategyBuilder;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.nio.ssl.TlsStrategy;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import javax.net.ssl.SSLContext;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Date;
import org.alertflex.entity.Tools;


public class LogServer {

    OpenSearchClient client = null;
    OpenSearchGenericClient genericClient = null;
    
        
    public LogServer(Tools tools) {
            
        initOpenSearchClient(tools);
        
        if (client != null) {
            genericClient =  client.generic().
                withClientOptions(OpenSearchGenericClient.ClientOptions.throwOnHttpErrors());
        } else {
            genericClient = null;
        }
    }
    
    public OpenSearchGenericClient getGenericClient() {
        return genericClient;
    }
    
    void initOpenSearchClient(Tools tools) {
        try {
            
            if (tools == null) {
                return;
            }

            String url = tools.getOpensearchUrl();
                        
            String[] words = url.split("://");
            
            if (words.length != 2) return;
            
            String urlType = words[0];
            String host = words[1];
            
            if ( host.isEmpty() || host.equals("indef")) return;
            
            String user = tools.getOpensearchUser();
            String password = tools.getOpensearchPwd();
            
            if (user.isEmpty() || password.isEmpty()) return;

            int port = tools.getOpensearchPort();

            HttpHost httpHost = new HttpHost(urlType, host, port);
            final BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(new AuthScope(httpHost), 
                    new UsernamePasswordCredentials(user, password.toCharArray()));

            final SSLContext sslContext = SSLContextBuilder
                .create()
                .loadTrustMaterial(null, (chains, authType) -> true)
                .build();

            final ApacheHttpClient5TransportBuilder builder = ApacheHttpClient5TransportBuilder.builder(httpHost);

            builder.setHttpClientConfigCallback(httpClientBuilder -> {

                final TlsStrategy tlsStrategy = ClientTlsStrategyBuilder.create()
                    .setSslContext(sslContext)
                    .setHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                    .build();
                final PoolingAsyncClientConnectionManager connectionManager = PoolingAsyncClientConnectionManagerBuilder
                    .create()
                    .setTlsStrategy(tlsStrategy)
                    .build();

                return httpClientBuilder
                    .setDefaultCredentialsProvider(credentialsProvider)
                    .setConnectionManager(connectionManager);
            });

            OpenSearchTransport transport = builder.build();
            client = new OpenSearchClient(transport);    

        } catch (Exception e) {
            client = null;
        }
    }

    String getDateIndex(String index) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date dt = new Date();
        return index + dateFormat.format(dt).toString();
    }

    public void SendOcsfToLog(String ocsf, String index) {
        String body;
        if (genericClient != null) {
            
            String dateIndex = index;
            
            if (index.equals("alertflex-")) dateIndex = getDateIndex(index);
            
            try {
                Response response = genericClient.execute (
                        Requests.builder()
                            .endpoint(dateIndex + "/_doc/")
                            .method("POST")
                            .json(ocsf)
                            .build());
                body = response.getBody().get().bodyAsString();
                
            } catch (Exception e) {
                body = null;
            }
        }
    }
}
