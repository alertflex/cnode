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

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.alertflex.entity.Node;
import org.alertflex.entity.NodeAlerts;
import org.alertflex.entity.NodeMonitor;
import org.alertflex.entity.Project;
import org.alertflex.facade.NodeFacade;
import org.alertflex.facade.ProbeFacade;
import org.alertflex.facade.NodeAlertsFacade;
import org.alertflex.facade.NodeMonitorFacade;
import org.alertflex.facade.ProjectFacade;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Stateless
@Path("metrics")
@Produces(MediaType.TEXT_PLAIN)
public class MetricsREST {

    private static final Logger logger = LogManager.getLogger(MetricsREST.class);

    @EJB
    private ProjectFacade projectFacade;

    @EJB
    private NodeFacade nodeFacade;

    @EJB
    private ProbeFacade probeFacade;

    @EJB
    private NodeMonitorFacade nodeMonitorFacade;

    @EJB
    private NodeAlertsFacade nodeAlertsFacade;

    StringBuilder sb;

    public MetricsREST() {

    }

    @GET
    public String metrics() {

        List<Project> projectList = projectFacade.findAll();

        if (projectList == null || projectList.isEmpty()) {
            return "";
        }

        if (projectList.get(0).getPrometheusStat() == 0) {
            return "";
        }

        String prj = projectList.get(0).getRefId();

        List<Node> nodeList = nodeFacade.findByRef(prj);

        if (nodeList == null || nodeList.isEmpty()) {
            return sb.toString();
        }

        sb = new StringBuilder("# HELP Alertflex metrics\n");

        for (Node node : nodeList) {

            String nodeId = node.getNodePK().getName();

            if (nodeId != null || !nodeId.isEmpty()) {

                Date endDate = new Date();
                long millis = endDate.getTime() - 300 * 1000;
                Date startDate = new Date(millis);
                Timestamp end = new Timestamp(endDate.getTime());
                Timestamp start = new Timestamp(startDate.getTime());

                NodeAlerts nal = nodeAlertsFacade.getLastRecord(prj, nodeId, start, end);
                if (nal != null) {
                    metricsNodeAlerts(nodeId, nal);
                }

                NodeMonitor nmon = nodeMonitorFacade.getLastRecord(prj, nodeId, start, end);
                if (nmon != null) {
                    metricsNodeMonitor(nodeId, nmon);
                }
}
        }

        return sb.toString();
    }

    private void metricsNodeMonitor(String nodeId, NodeMonitor nm) {

        StringBuilder template = new StringBuilder("alertflex_node_monitor{node=\"");
        template.append(nodeId);
        template.append("\",type=\"");

        sb.append(template.toString());
        sb.append("crs\"} ");
        sb.append(Long.toString(nm.getEventsCrs()));
        sb.append("\n");

        sb.append(template.toString());
        sb.append("hids\"} ");
        sb.append(Long.toString(nm.getEventsHids()));
        sb.append("\n");

        sb.append(template.toString());
        sb.append("nids\"} ");
        sb.append(Long.toString(nm.getEventsNids()));
        sb.append("\n");

        sb.append(template.toString());
        sb.append("waf\"} ");
        sb.append(Long.toString(nm.getEventsWaf()));
        sb.append("\n");

        sb.append(template.toString());
        sb.append("log_counter\"} ");
        sb.append(Long.toString(nm.getLogCounter()));
        sb.append("\n");

        sb.append(template.toString());
        sb.append("log_volume\"} ");
        sb.append(Long.toString(nm.getLogVolume()));
        sb.append("\n");

        sb.append(template.toString());
        sb.append("stat_counter\"} ");
        sb.append(Long.toString(nm.getStatCounter()));
        sb.append("\n");

        sb.append(template.toString());
        sb.append("stat_volume\"} ");
        sb.append(Long.toString(nm.getStatVolume()));
        sb.append("\n");

        sb.append("\n");
    }

    private void metricsNodeAlerts(String nodeId, NodeAlerts na) {

        StringBuilder template = new StringBuilder("alertflex_node_alerts{node=\"");
        template.append(nodeId);
        template.append("\",type=\"");

        sb.append(template.toString());
        sb.append("crs_agg\"} ");
        sb.append(Long.toString(na.getCrsAgg()));
        sb.append("\n");

        sb.append(template.toString());
        sb.append("crs_filter\"} ");
        sb.append(Long.toString(na.getCrsFilter()));
        sb.append("\n");

        sb.append(template.toString());
        sb.append("crs_s0\"} ");
        sb.append(Long.toString(na.getCrsS0()));
        sb.append("\n");

        sb.append(template.toString());
        sb.append("crs_s1\"} ");
        sb.append(Long.toString(na.getCrsS1()));
        sb.append("\n");

        sb.append(template.toString());
        sb.append("crs_s2\"} ");
        sb.append(Long.toString(na.getCrsS2()));
        sb.append("\n");

        sb.append(template.toString());
        sb.append("crs_s3\"} ");
        sb.append(Long.toString(na.getCrsS3()));
        sb.append("\n");

        sb.append(template.toString());
        sb.append("hids_agg\"} ");
        sb.append(Long.toString(na.getHidsAgg()));
        sb.append("\n");

        sb.append(template.toString());
        sb.append("hids_filter\"} ");
        sb.append(Long.toString(na.getHidsFilter()));
        sb.append("\n");

        sb.append(template.toString());
        sb.append("hids_s0\"} ");
        sb.append(Long.toString(na.getHidsS0()));
        sb.append("\n");

        sb.append(template.toString());
        sb.append("hids_s1\"} ");
        sb.append(Long.toString(na.getHidsS1()));
        sb.append("\n");

        sb.append(template.toString());
        sb.append("hids_s2\"} ");
        sb.append(Long.toString(na.getHidsS2()));
        sb.append("\n");

        sb.append(template.toString());
        sb.append("hids_s3\"} ");
        sb.append(Long.toString(na.getHidsS3()));
        sb.append("\n");

        sb.append(template.toString());
        sb.append("nids_agg\"} ");
        sb.append(Long.toString(na.getNidsAgg()));
        sb.append("\n");

        sb.append(template.toString());
        sb.append("nids_filter\"} ");
        sb.append(Long.toString(na.getNidsFilter()));
        sb.append("\n");

        sb.append(template.toString());
        sb.append("nids_s0\"} ");
        sb.append(Long.toString(na.getNidsS0()));
        sb.append("\n");

        sb.append(template.toString());
        sb.append("nids_s1\"} ");
        sb.append(Long.toString(na.getNidsS1()));
        sb.append("\n");

        sb.append(template.toString());
        sb.append("nids_s2\"} ");
        sb.append(Long.toString(na.getNidsS2()));
        sb.append("\n");

        sb.append(template.toString());
        sb.append("nids_s3\"} ");
        sb.append(Long.toString(na.getNidsS3()));
        sb.append("\n");
        
        sb.append(template.toString());
        sb.append("waf_agg\"} ");
        sb.append(Long.toString(na.getWafAgg()));
        sb.append("\n");

        sb.append(template.toString());
        sb.append("waf_filter\"} ");
        sb.append(Long.toString(na.getWafFilter()));
        sb.append("\n");

        sb.append(template.toString());
        sb.append("waf_s0\"} ");
        sb.append(Long.toString(na.getWafS0()));
        sb.append("\n");

        sb.append(template.toString());
        sb.append("waf_s1\"} ");
        sb.append(Long.toString(na.getWafS1()));
        sb.append("\n");

        sb.append(template.toString());
        sb.append("waf_s2\"} ");
        sb.append(Long.toString(na.getWafS2()));
        sb.append("\n");

        sb.append(template.toString());
        sb.append("waf_s3\"} ");
        sb.append(Long.toString(na.getWafS3()));
        sb.append("\n");

        sb.append("\n");
    }
}
