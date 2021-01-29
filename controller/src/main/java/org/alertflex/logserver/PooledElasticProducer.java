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
 
package org.alertflex.logserver;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import hidden.org.elasticsearch.client.RestClient;
import hidden.org.elasticsearch.client.RestClientBuilder;
import hidden.org.elasticsearch.client.RestHighLevelClient;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.ejb.Startup;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import org.alertflex.entity.Project;
import org.alertflex.facade.ProjectFacade;

@ApplicationScoped
@Startup
public class PooledElasticProducer {

    @EJB
    private ProjectFacade projectFacade;
    List<Project> projectList;
    private Project prj = null;

    private ElasticSearch es = null;

    @PostConstruct
    public void initPool() {

        try {

            projectList = projectFacade.findAll();

            if (projectList == null || projectList.isEmpty()) {
                return;
            }

            prj = projectList.get(0);

            if (prj == null) {
                return;
            }

            String host = prj.getElkHost();

            String user = prj.getElkUser();

            String password = prj.getElkPass();

            int port = prj.getElkPort();

            if (!host.isEmpty() && !user.isEmpty() && !password.isEmpty()) {

                String keyStorePass = prj.getElkStorepass();
                String keystorePath = prj.getElkKeystore();
                String truststorePath = prj.getElkTruststore();

                if (!keyStorePass.isEmpty() && !keyStorePass.equals("indef")) {

                    Path keyStorePath = Paths.get(keystorePath);

                    Path trustStorePath = Paths.get(truststorePath);

                    KeyStore truststore = KeyStore.getInstance("jks");
                    try (InputStream is = Files.newInputStream(trustStorePath)) {
                        truststore.load(is, keyStorePass.toCharArray());
                    } catch (CertificateException e) {
                        return;
                    } catch (NoSuchAlgorithmException e) {
                        return;
                    }

                    KeyStore keystore = KeyStore.getInstance("jks");
                    try (InputStream is = Files.newInputStream(keyStorePath)) {
                        keystore.load(is, keyStorePass.toCharArray());
                    } catch (CertificateException e) {
                        return;
                    } catch (NoSuchAlgorithmException e) {
                        return;
                    }

                    SSLContextBuilder sslBuilder = SSLContexts.custom()
                            .loadTrustMaterial(truststore, null)
                            .loadKeyMaterial(keystore, keyStorePass.toCharArray());

                    final SSLContext sslContext = sslBuilder.build();

                    final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
                    credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(user, password));

                    RestHighLevelClient client = new RestHighLevelClient(
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

                    es = new ElasticSearch(client);

                } else {

                    final SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, TrustAllStrategy.INSTANCE).build();

                    final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
                    credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(user, password));

                    RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(new HttpHost(host, port, "https"))
                            .setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
                                @Override
                                public HttpAsyncClientBuilder customizeHttpClient(
                                        HttpAsyncClientBuilder httpClientBuilder) {
                                    return httpClientBuilder.setSSLContext(sslContext)
                                            .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                                            .setDefaultCredentialsProvider(credentialsProvider);
                                }
                            }));

                    es = new ElasticSearch(client);
                }

            } else {

                if (!host.isEmpty()) {
                    RestHighLevelClient client = new RestHighLevelClient(
                            RestClient.builder(
                                    new HttpHost(host, 9200, "http"),
                                    new HttpHost(host, 9201, "http")
                            )
                    );

                    es = new ElasticSearch(client);
                }
            }

        } catch (Exception e) {
            return;
        }

    }

    @Produces
    @FromElasticPool
    public ElasticSearch get() {

        if (es != null) {

            return es;
        }

        return null;
    }

    @PreDestroy
    public void close() {

        if (es != null) {

            try {

                es.close();

            } catch (IOException e) {
                es = null;
                return;
            }
        }
    }
}
