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

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import org.alertflex.entity.Alert;
import org.alertflex.entity.Users;
import org.alertflex.facade.AlertFacade;
import org.alertflex.facade.UsersFacade;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Stateless
@Path("stix-alerts")
public class StixAlertsREST {

    private static final Logger logger = LoggerFactory.getLogger(StixAlertsREST.class);

    @EJB
    private UsersFacade usersFacade;

    @EJB
    private AlertFacade alertFacade;

    public StixAlertsREST() {

    }

    @GET
    @Path("/status")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response status(@Context SecurityContext sc) {

        String userName = sc.getUserPrincipal().getName();

        Users user = usersFacade.findUserByName(userName);

        if (user != null) {

            return Response.ok().build();
        }

        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @GET
    @Path("/search")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response search(@QueryParam("query") String q, @Context SecurityContext sc) {

        String userName = sc.getUserPrincipal().getName();

        Users user = usersFacade.findUserByName(userName);

        if (user != null) {

            List<Alert> listAlerts = alertFacade.findAlertsBySQL(user.getRefId(), q);

            String events = createEntity(listAlerts);

            return Response
                    .status(Response.Status.OK)
                    .entity(events)
                    .build();
        }

        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    String createEntity(List<Alert> alerts) {

        JSONArray array = new JSONArray();

        for (Alert a : alerts) {

            JSONObject obj = new JSONObject();

            String dummy;

            obj.put("event", a.getEventId());
            obj.put("source", a.getAlertSource());
            obj.put("type", a.getAlertType());
            obj.put("node", a.getNodeId());
            obj.put("user", a.getUserName());
            obj.put("severity", a.getAlertSeverity());
            obj.put("description", a.getDescription());
            obj.put("create_time", a.getTimeCollr().getTime());

            if (!a.getCategories().isEmpty()) {
                obj.put("category", a.getCategories());
            }

            if (!a.getSrcIp().isEmpty() && !a.getSrcIp().equals("indef")) {
                obj.put("srcip", a.getSrcIp());
                obj.put("srcport", a.getSrcPort());
            } else {
                obj.put("srcip", "0.0.0.0");
                obj.put("srcport", 0);
            }

            if (!a.getDstIp().isEmpty() && !a.getDstIp().equals("indef")) {
                obj.put("dstip", a.getDstIp());
                obj.put("dstport", a.getDstPort());
            } else {
                obj.put("dstip", "0.0.0.0");
                obj.put("dstport", 0);
            }

            if (!a.getDstIp().isEmpty() || !a.getSrcIp().isEmpty()) {
                obj.put("protocol", "ip");
            }

            if (!a.getAgentName().isEmpty()) {
                obj.put("agent", a.getAgentName());
            }
            if (!a.getProcessName().isEmpty() && !a.getProcessName().equals("indef")) {
                obj.put("process", a.getProcessName());
            }
            if (!a.getFileName().isEmpty() && !a.getFileName().equals("indef")) {
                obj.put("file", a.getFileName());
            }
            if (!a.getInfo().isEmpty()) {
                obj.put("info", a.getInfo());
            }

            if (a.getAlertType().equals("FILE")) {

                try {

                    if (a.getAlertSource().equals("Wazuh")) {

                        if (!a.getHashSha1().isEmpty()) {
                            obj.put("sha1", a.getHashSha1());
                        }

                        if (!a.getHashSha256().isEmpty()) {
                            obj.put("sha256", a.getHashSha256());
                        }

                        if (!a.getHashMd5().isEmpty()) {
                            obj.put("md5", a.getHashMd5());
                        }

                    } else {

                        if (a.getAlertSource().equals("MISP")) {

                            JSONObject artifacts = new JSONObject(a.getInfo());
                            JSONArray fileInfo = artifacts.getJSONArray("artifacts");

                            for (int i = 0; i < fileInfo.length(); i++) {

                                String dataType = fileInfo.getJSONObject(i).getString("dataType");

                                if (dataType.equals("md5")) {
                                    String md5 = fileInfo.getJSONObject(i).getString("data");
                                    obj.put("md5", md5);
                                }

                                if (dataType.equals("sha1")) {
                                    String sha1 = fileInfo.getJSONObject(i).getString("data");
                                    obj.put("sha1", sha1);
                                }
                            }
                        }
                    }

                } catch (JSONException e) {
                    logger.error(e.getMessage());
                }
            } else {

                if (a.getAlertSource().equals("MISP") && a.getAlertType().equals("NET")) {

                    if (a.getCategories().contains("dns, ")) {
                        try {

                            JSONObject artifacts = new JSONObject(a.getInfo());
                            JSONArray fileInfo = artifacts.getJSONArray("artifacts");

                            for (int i = 0; i < fileInfo.length(); i++) {

                                String dataType = fileInfo.getJSONObject(i).getString("dataType");

                                if (dataType.equals("domain")) {
                                    String domain = fileInfo.getJSONObject(i).getString("data");
                                    obj.put("domain-name", domain);
                                }

                            }
                        } catch (JSONException e) {
                            logger.error(e.getMessage());
                        }
                    }
                }
            }

            array.put(obj);

        }

        return array.toString();

    }

}
