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

import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Date;
import org.alertflex.entity.Alert;
import org.json.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ElasticSearch {

    private static final Logger logger = LogManager.getLogger(ElasticSearch.class);

    private RestHighLevelClient client = null;

    String dateIndex = "";

    IndexRequest requestEvents = null;

    IndexRequest requestSuricata = null;
    
    IndexRequest requestAwsWaf = null;

    public ElasticSearch(RestHighLevelClient c) {
        this.client = c;

        dateIndex = getDateIndex();

        requestEvents = new IndexRequest("alertflex-events-" + dateIndex);

        requestSuricata = new IndexRequest("alertflex-suricata-" + dateIndex);
        
        requestAwsWaf = new IndexRequest("alertflex-awswaf-" + dateIndex);
    }

    String getDateIndex() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
        Date dt = new Date();
        return dateFormat.format(dt).toString();
    }

    void checkDateIndex() {

        String di = getDateIndex();

        if (!dateIndex.equals(di)) {

            dateIndex = di;

            requestEvents = new IndexRequest("alertflex-events-" + dateIndex);

            requestSuricata = new IndexRequest("alertflex-suricata-" + dateIndex);
            
            requestAwsWaf = new IndexRequest("alertflex-awswaf-" + dateIndex);

        }
    }

    public void SendSuricataToLog(JSONObject obj) {

        String elasticJson = "";

        if (client != null) {

            try {
                
                Date date = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
                String time = formatter.format(date);
                
                obj.append("controller_time", time);

                elasticJson = obj.toString();

                if (!elasticJson.isEmpty()) {
                    checkDateIndex();
                    requestSuricata.source(elasticJson, XContentType.JSON);
                    IndexResponse indexResponse = client.index(requestSuricata, RequestOptions.DEFAULT);
                }

            } catch (Exception e) {
                logger.error("alertflex_ctrl_exception", e);
            }
        }
    }

    public void SendAlertToLog(Alert a) {

        if (client != null) {

            try {

                Integer level;
                Integer severity = a.getAlertSeverity();

                switch (severity) {
                    case 0:
                        level = 5;
                        break;
                    case 1:
                        level = 4;
                        break;
                    case 2:
                        level = 3;
                        break;
                    case 3:
                        level = 1;
                        break;
                    default:
                        level = 6;
                        break;
                }

                Date date = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
                String time = formatter.format(date);

                String alert = "{\"version\": \"1.1\",\"node\":\""
                        + a.getNodeId()
                        + "\",\"short_message\":\"alert-flex\""
                        + ",\"full_message\":\"Alert from Alertflex collector/controller\""
                        + ",\"level\":"
                        + level.toString()
                        + ",\"source_type\":\""
                        + a.getAlertType()
                        + "\",\"source_name\":\""
                        + a.getAlertSource()
                        + "\",\"project_id\":\""
                        + a.getRefId()
                        + "\",\"alert uuid\":\""
                        + a.getAlertUuid()
                        + "\",\"severity\":"
                        + a.getAlertSeverity()
                        + ",\"event\":\""
                        + a.getEventId()
                        + "\",\"categories\":\""
                        + a.getCategories()
                        + "\",\"description\":\""
                        + a.getDescription()
                        + "\",\"src_ip\":\""
                        + a.getSrcIp()
                        + "\",\"src_hostname\":\""
                        + a.getSrcHostname()
                        + "\",\"src_port\":"
                        + a.getSrcPort()
                        + ",\"dst_ip\":\""
                        + a.getDstIp()
                        + "\",\"dst_hostname\":\""
                        + a.getDstHostname()
                        + "\",\"dst_port\":"
                        + a.getDstPort()
                        + ",\"sensor\":\""
                        + a.getSensorId()
                        + "\",\"process\":\""
                        + a.getProcessName()
                        + "\",\"container\":\""
                        + a.getContainerName()
                        + "\",\"agent\":\""
                        + a.getAgentName()
                        + "\",\"user\":\""
                        + a.getUserName()
                        + "\",\"file\":\""
                        + a.getFileName()
                        + "\",\"location\":\""
                        + a.getLocation()
                        + "\",\"action\":\""
                        + a.getAction()
                        + "\",\"status\":\""
                        + a.getStatus()
                        + "\",\"filter\":\""
                        + a.getFilter()
                        + "\",\"collector_time\":\""
                        + a.getTimeEvent()
                        + "\",\"controller_time\":\""
                        + time
                        + "\"}";

                requestEvents.source(alert, XContentType.JSON);
                IndexResponse indexResponse = client.index(requestEvents, RequestOptions.DEFAULT);

            } catch (Exception e) {
                logger.error("alertflex_ctrl_exception", e);
            }
        }
    }
    
    public void SendAwsWafToLog(JSONObject obj) {
        
        String elasticJson = "";

        if (client != null) {

            try {
                
                Date date = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
                String time = formatter.format(date);
                
                obj.append("controller_time", time);

                elasticJson = obj.toString();
                

                if (!elasticJson.isEmpty()) {
                    checkDateIndex();
                    requestAwsWaf.source(elasticJson, XContentType.JSON);
                    IndexResponse indexResponse = client.index(requestAwsWaf, RequestOptions.DEFAULT);
                }

            } catch (Exception e) {
                logger.error("alertflex_ctrl_exception", e);
            }
        }

        
    }
    
    public void close() throws IOException {
        client.close();
    }
}
