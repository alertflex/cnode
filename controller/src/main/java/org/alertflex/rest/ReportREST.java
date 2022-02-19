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

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import org.alertflex.facade.ScanreportTaskFacade;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.List;
import org.alertflex.entity.ScanreportTask;
import org.json.JSONArray;
import org.json.JSONObject;

@Stateless
@Path("report")
public class ReportREST {

    private static final Logger logger = LogManager.getLogger(ReportREST.class);

    @EJB
    private ScanreportTaskFacade scanreportTaskFacade;

    public ReportREST() {

    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response push(@PathParam("id") String id, @Context SecurityContext sc) {

        if (id != null && !id.isEmpty()) {
            
            List<ScanreportTask> lr = scanreportTaskFacade.findReportsByUuid(id);
            
            String report = "wait";
            
            if (lr != null && !lr.isEmpty()) {
                
                String reportType = lr.get(0).getReportType();
                
                JSONObject reportJson = new JSONObject(new String("{ }"));
                
                reportJson.put("report_type", reportType);
                
                JSONArray alertsList = new JSONArray();
                
                for (ScanreportTask r : lr) {
                    
                    JSONObject alert = new JSONObject();
                    
                    alert.put("source", r.getSource());
                    alert.put("status", r.getStatus());
                    alert.put("severity", r.getSeverity());
                    alert.put("num", r.getNum());
                    
                    alertsList.put(alert);
                    
                    scanreportTaskFacade.remove(r);
                }
                
                reportJson.put("report",alertsList);
                
                report = reportJson.toString();
            }

            return Response
                    .status(Response.Status.OK)
                    .entity(report)
                    .build();
            
        }

        return Response.status(Response.Status.BAD_REQUEST).build();
    }
}
