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

import java.text.SimpleDateFormat;
import java.io.IOException;

import okhttp3.*;
import org.alertflex.common.PojoAlertLogic;
import org.alertflex.entity.Alert;
import org.alertflex.entity.Project;
import org.json.JSONObject;

public class GitlabIncident {

    String url = "";
    //  String user = "";
    String key = "";

    String postRequest;

    String desc;
    String type;
    String source;
    String sourceRef;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String d) {
        this.desc = d;
    }

    public String getType() {
        return type;
    }

    public void setType(String t) {
        this.type = t;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String s) {
        this.source = s;
    }

    public String getSourceRef() {
        return sourceRef;
    }

    public void setSourceRef(String s) {
        this.sourceRef = s;
    }

    public String createIncident(Project p, Alert a, String t, String s) {

        url = p.getGitlabUrl();
        key = p.getGitlabKey();

        if (url.isEmpty() || key.isEmpty()) {
            return null;
        }
        
        try {
            

            OkHttpClient client = new OkHttpClient();

            convertRequestToJson(a, t, s);

            Request request = new Request.Builder()
                    .header("Authorization", "Bearer " + key)
                    .url(url)
                    .post(RequestBody.create(MediaType.parse("application/json"), postRequest))
                    .build();

            Response response = client.newCall(request).execute();

            int res = response.code();

            if (res != 201) return null;

        } catch (IOException e) { return null;}
        
        return a.getAlertUuid();
    }

    public String createIncidentPal(Project p, PojoAlertLogic a, String t, String s) {

        url = p.getGitlabUrl();
        key = p.getGitlabKey();

        if (url.isEmpty() || key.isEmpty()) {
            return null;
        }
        
        try {
            
            OkHttpClient client = new OkHttpClient();
            
            convertRequestToJsonPal(a, t, s);
            
            Request request = new Request.Builder()
                    .header("Authorization", "Bearer " + key)
                    .url(url)
                    .post(RequestBody.create(MediaType.parse("application/json"), postRequest))
                    .build();

            Response response = client.newCall(request).execute();

            int res = response.code();

            if (res != 201) return null;

        } catch (IOException e) { return null;}
        
        return a.getAlertUuid();
    }

    void convertRequestToJson(Alert a, String t, String s) {
        String myDateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS";
        SimpleDateFormat customFormat = new SimpleDateFormat(myDateFormat);
        customFormat.setLenient(false);

        String[] category_array = a.getCategories().split(", ");

        String tags = "";

        for (int i = 0; (i < category_array.length); i++) {
            tags = tags + "\"" + category_array[i] + "\"";
            if (i != category_array.length - 1) {
                tags = tags + ",";
            }
        }
        
        String sev = "info";
        
        switch (a.getAlertSeverity()) {
            
            case 0 : sev = "info";
                break;
            case 1 : sev = "low";
                break;
            case 2 : sev = "medium";
                break;
            case 3 : sev = "critical";
                break;
        }
        
        
        postRequest = "{\"title\": \"" + a.getEventId() + "\"," +
            "\"description\": \"" + a.getDescription() + "\"," +
            "\"start_time\": \"" + a.getTimeCollr() + "\"," +
            "\"service\": \"" + a.getAlertType() + "\"," +
            "\"monitoring_tool\": \"" + a.getAlertSource() + "\"," +
            "\"hosts\": \"" + a.getSrcIp() + ", " + a.getDstIp() + "\"," +
            "\"severity\": \"" + sev + "\"," +
            "\"fingerprint\": \"" + a.getAlertUuid() + "\"," +
            "\"tags\":[\"alertflex\",\"node:" + a.getNode() + "\"," + tags + "]}";
        
    }
    
    void convertRequestToJsonPal(PojoAlertLogic a, String t, String s) {
        
        String myDateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS";
        SimpleDateFormat customFormat = new SimpleDateFormat(myDateFormat);
        customFormat.setLenient(false);

        String[] category_array = a.getCategories().split(", ");

        String tags = "";

        for (int i = 0; (i < category_array.length); i++) {
            tags = tags + "\"" + category_array[i] + "\"";
            if (i != category_array.length - 1) {
                tags = tags + ",";
            }
        }
        
        String sev = "info";
        
        switch (a.getAlertSeverity()) {
            
            case 0 : sev = "info";
                break;
            case 1 : sev = "low";
                break;
            case 2 : sev = "medium";
                break;
            case 3 : sev = "critical";
                break;
        }

        postRequest = "{\"title\": \"" + a.getEventId() + "\"," +
            "\"description\": \"" + a.getDescription() + "\"," +
            "\"start_time\": \"" + a.getTimeCollr() + "\"," +
            "\"service\": \"" + a.getAlertType() + "\"," +
            "\"monitoring_tool\": \"" + a.getAlertSource() + "\"," +
            "\"hosts\": \"" + a.getSrcIp() + ", " + a.getDstIp() + "\"," +
            "\"severity\": \"" + sev + "\"," +
            "\"fingerprint\": \"" + a.getAlertUuid() + "\"," +
            "\"tags\":[\"alertflex\",\"node:" + a.getNode() + "\"," + tags + "]}";

    }

    String convertResponseToObject(String res) {

        JSONObject obj = new JSONObject(res);

        String r = obj.getString("id");

        return r;
    }
}
