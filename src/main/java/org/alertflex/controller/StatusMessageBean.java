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

package org.alertflex.controller;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import java.util.zip.GZIPInputStream;
import javax.jms.BytesMessage;
import org.alertflex.entity.Probes;
import org.json.JSONObject;
import org.alertflex.entity.Project;
import org.alertflex.facade.ProbesFacade;
import org.alertflex.facade.ProjectFacade;


@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
    @ActivationConfigProperty(propertyName = "destination", propertyValue = "jms/alertflex/status"),
    @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Client-acknowledge")})
public class StatusMessageBean implements MessageListener {

    @EJB
    private ProjectFacade projectFacade;
    
    @EJB
    private ProbesFacade probesFacade;
    
    Project project;
    String projectId;
    String probeName;
    String sensorsGroup;
    String pipelineName;
    String pipelineTask;
    
    String probesStatus;
    
    @Override
    public void onMessage(Message message) {
        int msgType;

        try {
            
            message.acknowledge();

            if (message instanceof BytesMessage) {

                BytesMessage bytesMessage = (BytesMessage) message;

                msgType = bytesMessage.getIntProperty("msg_type");
                if (msgType != 4) return;
                
                projectId = bytesMessage.getStringProperty("project_id");
                project = projectFacade.findProjectById(projectId);
                
                if (project != null) {
                    
                    byte[] buffer = new byte[(int) bytesMessage.getBodyLength()];
                    bytesMessage.readBytes(buffer);

                    probesStatus = "";

                    probesStatus = decompress(buffer);
                    
                    if (probesStatus.isEmpty()) {
                        return;
                    }
                    
                    probeName = bytesMessage.getStringProperty("probe_name");
                    sensorsGroup = bytesMessage.getStringProperty("sensors_group");
                    pipelineName = bytesMessage.getStringProperty("pipeline_name");
                    pipelineTask = bytesMessage.getStringProperty("pipeline_task");
                    
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = new Date();
                    
                    JSONObject jsonProbeStatus = new JSONObject(probesStatus);
                    int reportInterval = jsonProbeStatus.getInt("report_interval");
                    int enableAlerts = jsonProbeStatus.getInt("enable_alerts");
                    int enableLogs = jsonProbeStatus.getInt("enable_logs");
                    String runMode = jsonProbeStatus.getString("run_mode");
                    String pathWorkspace = jsonProbeStatus.getString("path_workspace");
                    String pathOutput = jsonProbeStatus.getString("path_output");
                    int appLogStatus = jsonProbeStatus.getInt("app_log_status");
                    int appRedisStatus = jsonProbeStatus.getInt("app_redis_status");
                    int falcoLogStatus = jsonProbeStatus.getInt("falco_log_status");
                    int falcoRedisStatus = jsonProbeStatus.getInt("falco_redis_status");
                    int suricataLogStatus = jsonProbeStatus.getInt("suricata_log_status");
                    int suricataRedisStatus = jsonProbeStatus.getInt("suricata_redis_status");
                    String lastTestTime = jsonProbeStatus.getString("last_test_time");
                    
                    Probes probe = probesFacade.findProbeByProjectIdAndProbeName(projectId, probeName);
                    
                    if (probe == null) {
                        probe = new Probes();
                        probe.setDescription("");
                        probe.setProjectId(projectId);
                        probe.setProbeName(probeName);
                        probe.setSensorsGroup(sensorsGroup);
                        probe.setPipelineName(pipelineName);
                        probe.setPipelineTask(pipelineTask);
                        probe.setReportInterval(reportInterval);
                        probe.setEnableAlerts(enableAlerts);
                        probe.setEnableLogs(enableLogs);
                        probe.setRunMode(runMode);
                        probe.setPathWorkspace(pathWorkspace);
                        probe.setPathOutput(pathOutput);
                        probe.setAppLogStatus(appLogStatus);
                        probe.setAppRedisStatus(appRedisStatus);
                        probe.setFalcoLogStatus(falcoLogStatus);
                        probe.setFalcoRedisStatus(falcoRedisStatus);
                        probe.setSuricataLogStatus(suricataLogStatus);
                        probe.setSuricataRedisStatus(suricataRedisStatus);
                        probe.setFirstStatusTime(date);
                        probe.setLastStatusTime(date);
                        probe.setLastTestTime(lastTestTime);
                        probesFacade.create(probe);
                    } else {
                        probe.setSensorsGroup(sensorsGroup);
                        probe.setPipelineName(pipelineName);
                        probe.setPipelineTask(pipelineTask);
                        probe.setReportInterval(reportInterval);
                        probe.setEnableAlerts(enableAlerts);
                        probe.setEnableLogs(enableLogs);
                        probe.setRunMode(runMode);
                        probe.setPathWorkspace(pathWorkspace);
                        probe.setPathOutput(pathOutput);
                        probe.setAppLogStatus(appLogStatus);
                        probe.setAppRedisStatus(appRedisStatus);
                        probe.setFalcoLogStatus(falcoLogStatus);
                        probe.setFalcoRedisStatus(falcoRedisStatus);
                        probe.setSuricataLogStatus(suricataLogStatus);
                        probe.setSuricataRedisStatus(suricataRedisStatus);
                        probe.setLastStatusTime(date);
                        probe.setLastTestTime(lastTestTime);
                        probesFacade.edit(probe);
                    }
                }            
            }
        } catch (IOException | JMSException  e) {
           
        }
    }
    
    public String decompress(byte[] compressed) throws IOException {
        String outStr;
        if ((compressed == null) || (compressed.length == 0)) {
            return "";
        }
        if (isCompressed(compressed)) {
            GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(compressed));
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(gis));

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
                sb.append('\n');
            }
            outStr = sb.toString();
        } else {
            outStr = new String(compressed);
        }
        return outStr;
    }

    public boolean isCompressed(byte[] compressed) {
        return (compressed[0] == (byte) (GZIPInputStream.GZIP_MAGIC)) && 
            (compressed[1] == (byte) (GZIPInputStream.GZIP_MAGIC >> 8));
    }
}