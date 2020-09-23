/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alertflex.mc.ui;

/**
 *
 * @author root
 */

import org.alertflex.mc.supp.AuthenticationSingleton;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.alertflex.mc.db.Alert;
import org.alertflex.mc.services.AlertFacade;
import org.alertflex.mc.supp.CounterInterval;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.DateAxis;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;
import org.joda.time.DateTime;
import org.primefaces.event.RowEditEvent;
import org.primefaces.model.chart.Axis;

@ManagedBean
@ViewScoped
public class AlertsSearchController {
    
    @EJB
    private AuthenticationSingleton authSingleton;
    
    String session_tenant;
    String session_user;
    
    @EJB
    private AlertFacade alertsFacade;
    
    private Date start= new Date();  
    private Date end = new Date(); 
    
    private List<Alert> alertsList = null;
    private List<Alert> filteredList = null;
    
    private Alert selectedAlert;
    
    private  boolean alertSeverityBox = true;
    private  boolean alertIdBox = false;
    private  boolean alertUuidBox = false;
    private  boolean nodeIdBox = false;
    private  boolean sensorIdBox = false;
    private  boolean alertSourceBox = true;
    private  boolean alertTypeBox = true;
    private  boolean eventSeverityBox = false;
    private  boolean eventIdBox = true;
    private  boolean categoriesBox = true;
    private  boolean descriptionBox = false;
    private  boolean locationBox = false;
    private  boolean statusBox = false;
    private  boolean actionBox = false;
    private  boolean infoBox = false;
    private  boolean filterBox = false;
    
    private  boolean agentNameBox = false;
    private  boolean userNameBox = false;
    
    private  boolean srcIpBox = false;
    private  boolean dstIpBox = false;
    private  boolean srcHostnameBox = false;
    private  boolean dstHostnameBox = false;
    private  boolean srcPortBox = false;
    private  boolean dstPortBox = false;
    
    private  boolean hashMd5Box = false;
    private  boolean hashSha1Box = false;
    private  boolean hashSha256Box = false;
    
    private  boolean processIdBox = false;
    private  boolean processNameBox = false;
    private  boolean processCmdlineBox = false;
    private  boolean processPathBox = false;
    
    private  boolean urlHostnameBox = false;
    private  boolean urlPathBox = false;
    
    private  boolean fileNameBox = false;
    private  boolean filePathBox = false;
    
    private  boolean containerIdBox = false;
    private  boolean containerNameBox = false;
    
    private  boolean timeStampBox = false;
    private  boolean timeCollrBox = false;
    private  boolean timeCtrlBox = true;
    
        
    final private static int INTERVALS = 100;
    
    /**
     * Creates a new instance of OssecController
     */
    public AlertsSearchController() {
       
    }
    
    @PostConstruct
    public void init() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        HttpSession session = request.getSession(false);
        
        session_tenant = authSingleton.getTenantName(session);
        session_user = authSingleton.getUserName(session);
        
        alertsList = new ArrayList<>();
        
    }
    
    public Alert getSelectedAlert () {
        return selectedAlert;
    }
    
    public void setSelectedAlert(Alert a) {
        this.selectedAlert = a;
    }
    
    public boolean getAlertSeverityBox() {
        return alertSeverityBox;
    }
 
    public void setAlertSeverityBox(boolean s) {
        this.alertSeverityBox = s;
    }
    
    public boolean getAlertIdBox() {
        return alertIdBox;
    }
 
    public void setAlertIdBox(boolean a) {
        this.alertIdBox = a;
    }
    
    public boolean getAlertUuidBox() {
        return alertUuidBox;
    }
 
    public void setAlertUuidBox(boolean a) {
        this.alertUuidBox = a;
    }
    
    public boolean getNodeIdBox() {
        return nodeIdBox;
    }
 
    public void setNodeIdBox(boolean n) {
        this.nodeIdBox = n;
    }
    
    public boolean getSensorIdBox() {
        return sensorIdBox;
    }
 
    public void setSensorIdBox(boolean s) {
        this.sensorIdBox = s;
    }
    
    public boolean getAlertSourceBox() {
        return alertSourceBox;
    }
 
    public void setAlertSourceBox(boolean s) {
        this.alertSourceBox = s;
    }
    
    public boolean getAlertTypeBox() {
        return alertTypeBox;
    }
 
    public void setAlertTypeBox(boolean s) {
        this.alertTypeBox = s;
    }
    
    public boolean getEventSeverityBox() {
        return eventSeverityBox;
    }
 
    public void setEventSeverityBox(boolean e) {
        this.eventSeverityBox = e;
    }
    
    public boolean getEventIdBox() {
        return eventIdBox;
    }
 
    public void setEventIdBox(boolean e) {
        this.eventIdBox = e;
    }
    
    public boolean getCategoriesBox() {
        return categoriesBox;
    }
 
    public void setCategoriesBox(boolean c) {
        this.categoriesBox = c;
    }
    
    public boolean getDescriptionBox() {
        return descriptionBox;
    }
 
    public void setDescriptionBox(boolean d) {
        this.descriptionBox = d;
    }
    
    public boolean getLocationBox() {
        return locationBox;
    }
 
    public void setLocationBox(boolean l) {
        this.locationBox = l;
    }
    
    public boolean getActionBox() {
        return actionBox;
    }
 
    public void setActionBox(boolean a) {
        this.actionBox = a;
    }
    
    public boolean getStatusBox() {
        return statusBox;
    }
 
    public void setStatusBox(boolean a) {
        this.statusBox = a;
    }
    
    public boolean getFilterBox() {
        return filterBox;
    }
 
    public void setFilterBox(boolean f) {
        this.filterBox = f;
    }
    
    public boolean getInfoBox() {
        return infoBox;
    }
 
    public void setInfoBox(boolean a) {
        this.infoBox = a;
    }
    
    public boolean getUserNameBox() {
        return userNameBox;
    }
 
    public void setUserNameBox(boolean u) {
        this.userNameBox = u;
    }
    
    public boolean getAgentNameBox() {
        return agentNameBox;
    }
 
    public void setAgentNameBox(boolean a) {
        this.agentNameBox = a;
    }
    
    public boolean getSrcIpBox() {
        return srcIpBox;
    }
 
    public void setSrcIpBox(boolean ip) {
        this.srcIpBox = ip;
    }
    
    public boolean getDstIpBox() {
        return dstIpBox;
    }
 
    public void setDstIpBox(boolean ip) {
        this.dstIpBox = ip;
    }
    
    public boolean getSrcHostnameBox() {
        return srcHostnameBox;
    }
 
    public void setSrcHostnameBox(boolean a) {
        this.srcHostnameBox = a;
    }
    
    public boolean getDstHostnameBox() {
        return dstHostnameBox;
    }
 
    public void setDstHostnameBox(boolean a) {
        this.dstHostnameBox = a;
    }
    
    public boolean getSrcPortBox() {
        return srcPortBox;
    }
 
    public void setSrcPortBox(boolean p) {
        this.srcPortBox = p;
    }
    
    public boolean getDstPortBox() {
        return dstPortBox;
    }
 
    public void setDstPortBox(boolean p) {
        this.dstPortBox = p;
    }
    
    public boolean getHashMd5Box() {
        return hashMd5Box;
    }
 
    public void setHashMd5Box(boolean a) {
        this.hashMd5Box = a;
    }
    
    public boolean getHashSha1Box() {
        return hashSha1Box;
    }
 
    public void setHashSha1Box(boolean a) {
        this.hashSha1Box = a;
    }
    
    public boolean getHashSha256Box() {
        return hashSha256Box;
    }
 
    public void setHashSha256Box(boolean a) {
        this.hashSha256Box = a;
    }
    
    
    public boolean getProcessIdBox() {
        return processIdBox;
    }
 
    public void setProcessIdBox(boolean a) {
        this.processIdBox = a;
    }
    
    public boolean getProcessNameBox() {
        return processNameBox;
    }
 
    public void setProcessNameBox(boolean a) {
        this.processNameBox = a;
    }
    
    public boolean getProcessCmdlineBox() {
        return processCmdlineBox;
    }
 
    public void setProcessCmdlineBox(boolean a) {
        this.processCmdlineBox = a;
    }
    
    public boolean getProcessPathBox() {
        return processPathBox;
    }
 
    public void setProcessPathBox(boolean a) {
        this.processPathBox = a;
    }
    
    public boolean getFileNameBox() {
        return fileNameBox;
    }
 
    public void setFileNameBox(boolean a) {
        this.fileNameBox = a;
    }
    
    public boolean getFilePathBox() {
        return filePathBox;
    }
 
    public void setFilePathBox(boolean a) {
        this.filePathBox = a;
    }
    
    public boolean getContainerIdBox() {
        return containerIdBox;
    }
 
    public void setContainerIdBox(boolean a) {
        this.containerIdBox = a;
    }
    
    public boolean getContainerNameBox() {
        return containerNameBox;
    }
 
    public void setContainerNameBox(boolean a) {
        this.containerNameBox = a;
    }
    
    public boolean getUrlHostnameBox() {
        return urlHostnameBox;
    }
 
    public void setUrlHostnameBox(boolean a) {
        this.urlHostnameBox = a;
    }
    
    public boolean getUrlPathBox() {
        return urlPathBox;
    }
 
    public void setUrlPathBox(boolean a) {
        this.urlPathBox = a;
    }
    
    public boolean getTimeStampBox() {
        return timeStampBox;
    }
 
    public void setTimeStampBox(boolean t) {
        this.timeStampBox = t;
    }
    
    public boolean getTimeCtrlBox() {
        return timeCtrlBox;
    }
 
    public void setTimeCtrlBox(boolean t) {
        this.timeCtrlBox = t;
    }
    
    public boolean getTimeCollrBox() {
        return timeCollrBox;
    }
 
    public void setTimeCollrBox(boolean t) {
        this.timeCollrBox = t;
    }
    
       
    public Date getStart() {
       return start;
    } 
   
    public void setStart(Date date) {
       this.start = date;
    }
    
    public Date getEnd() {
       return end;
    } 
   
    public void setEnd(Date date) {
       this.end = date;
    }
    
    public List<Alert> getFilteredList() {
        return filteredList;
    }

    public void setFilteredList(List<Alert> list) {
        this.filteredList = list;
    }
    
    public AlertFacade getAlertsFacade() {
        return alertsFacade;
    }
    
    public void setAlertsList(List<Alert> list) {
        this.alertsList = list;
    }
    
    public List<Alert> getAlertsList() {
        return alertsList;
    }
    
    public void loadAlerts() {
        
        alertsList = alertsFacade.findIntervalsBetween(session_tenant, start, end);
        
    }
    
      
    public int  getCounterAlerts () {
               
        return alertsList.size();
    }
    
    public LineChartModel getLineChart() { 
        
        LineChartModel lineChart = new LineChartModel();
        
        LineChartSeries lc = new LineChartSeries();
        
        
        DateTime startDate = new DateTime(start);
        DateTime endDate = new DateTime(end);
        
        long sd = startDate.getMillis();
        long singlePart = (endDate.getMillis() - sd) / INTERVALS;
        
        List<CounterInterval> intervalsList = new ArrayList<CounterInterval>();
        
        if (singlePart > 0) {
            
            for (int i = 0; i < INTERVALS; i++) {
                
                DateTime startDT = new DateTime(sd + singlePart*i);
                DateTime endDT = new DateTime(sd + singlePart * (i + 1));
            
                CounterInterval interval = new CounterInterval(startDT, endDT);
                intervalsList.add(interval); 
            }
            
            if ((alertsList != null) && (alertsList.size() > 0)) {
        
                for (Alert a : alertsList) {
                
                    DateTime date = new DateTime(a.getTimeCollr());
                    Long m = date.getMillis();
                    for (CounterInterval ci : intervalsList) {
                        if ((ci.getStartMillis() <= m) && (m <= ci.getEndMillis())) {
                            ci.setCounter();
                            break;
                        }
                    }
                }
            
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            
                for (CounterInterval ci : intervalsList) {
                    String date = df.format(ci.getStart().toDate());
                    lc.set(date, ci.getCounter());        
                }
                
                lc.setSmoothLine(true);
                lc.setFill(true);
                DateAxis axis = new DateAxis();
                Axis yAxis = lineChart.getAxis(AxisType.Y);
                yAxis.setMin(0);
                lineChart.getAxes().put(AxisType.X, axis);
                lineChart.setExtender("skinTimeLineChart");
                lineChart.addSeries(lc);
                return  lineChart;
            }
            
        } 
        
        lc.setSmoothLine(true);
        lc.setFill(true);
        lc.set("2016-01-01 00:00:00", 0);
        DateAxis axis = new DateAxis();
        Axis yAxis = lineChart.getAxis(AxisType.Y);
        yAxis.setMin(0);
        lineChart.getAxes().put(AxisType.X, axis);
        lineChart.setExtender("skinTimeLineChart");
        lineChart.addSeries(lc);
        return  lineChart;
    }
    
    public List<String> changeStatus() {
        
        List<String> statusChangeList = new ArrayList<>();
        
        statusChangeList.add("confirmed");
        statusChangeList.add("incident"); 
        
        return statusChangeList;
    }
    
    public void onRowEdit(RowEditEvent event) {
               
        Alert a = null; 
        
        try { 
            a = (Alert) event.getObject();
            alertsFacade.edit(a);
            FacesMessage msg = new FacesMessage("Status of alert has been edited.", a.getAlertId().toString());
            FacesContext.getCurrentInstance().addMessage(null, msg);
            
        } catch (Exception ex) { 
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error !", ex.toString())); 
        } 
    }  
    
    public void sendToJira() {
        
        
        
    }
}
