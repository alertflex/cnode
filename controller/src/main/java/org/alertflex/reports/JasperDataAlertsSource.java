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

package org.alertflex.reports;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.alertflex.entity.Alert;
import org.joda.time.DateTime;

public class JasperDataAlertsSource {

    DateTime start;
    DateTime end;
    int numIntervals;

    List<Alert> alertsList;

    private List<AlertsPie> dataPie;
    private List<AlertsBar> dataBar;

    public JasperDataAlertsSource(List<Alert> al, Date s, Date e, int n) {

        start = new DateTime(s);
        end = new DateTime(e);
        numIntervals = n;
        alertsList = al;
    }

    public void createDataPie() {

        dataPie = new ArrayList();

        CounterInterval ciFiles = new CounterInterval("FILE");
        CounterInterval ciHosts = new CounterInterval("HOST");
        CounterInterval ciNet = new CounterInterval("NET");
        CounterInterval ciMisc = new CounterInterval("MISC");

        for (Alert a : alertsList) {

            switch (a.getAlertType()) {
                case "FILE":
                    ciFiles.setCounter();
                    break;
                case "HOST":
                    ciHosts.setCounter();
                    break;
                case "NET":
                    ciNet.setCounter();
                    break;
                case "MISC":
                    ciMisc.setCounter();
                    break;
            }
        }

        dataPie.add(new AlertsPie("FILE", ciFiles.getCounter()));
        dataPie.add(new AlertsPie("HOST", ciHosts.getCounter()));
        dataPie.add(new AlertsPie("NET", ciNet.getCounter()));
        dataPie.add(new AlertsPie("MISC", ciMisc.getCounter()));
    }

    public void createDataBar() {

        dataBar = new ArrayList();

        long sd = start.getMillis();
        long singlePart = (end.getMillis() - sd) / numIntervals;

        List<CounterInterval> intervalsListFiles = new ArrayList<CounterInterval>();
        List<CounterInterval> intervalsListHosts = new ArrayList<CounterInterval>();
        List<CounterInterval> intervalsListNet = new ArrayList<CounterInterval>();
        List<CounterInterval> intervalsListMisc = new ArrayList<CounterInterval>();

        if (singlePart > 0) {

            for (int i = 0; i < numIntervals; i++) {

                DateTime startDT = new DateTime(sd + singlePart * i);
                DateTime endDT = new DateTime(sd + singlePart * (i + 1));

                intervalsListFiles.add(new CounterInterval("FILE", startDT, endDT));
                intervalsListHosts.add(new CounterInterval("HOST", startDT, endDT));
                intervalsListNet.add(new CounterInterval("NET", startDT, endDT));
                intervalsListMisc.add(new CounterInterval("MISC", startDT, endDT));
            }

            for (Alert a : alertsList) {

                DateTime date = new DateTime(a.getTimeCollr());
                Long m = date.getMillis();

                switch (a.getAlertType()) {
                    case "FILE":
                        for (CounterInterval ci : intervalsListFiles) {
                            if ((ci.getStartMillis() <= m) && (m <= ci.getEndMillis())) {
                                ci.setCounter();
                                break;
                            }
                        }
                        break;
                    case "HOST":
                        for (CounterInterval ci : intervalsListHosts) {
                            if ((ci.getStartMillis() <= m) && (m <= ci.getEndMillis())) {
                                ci.setCounter();
                                break;
                            }
                        }
                        break;
                    case "NET":
                        for (CounterInterval ci : intervalsListNet) {
                            if ((ci.getStartMillis() <= m) && (m <= ci.getEndMillis())) {
                                ci.setCounter();
                                break;
                            }
                        }
                        break;
                    case "MISC":
                        for (CounterInterval ci : intervalsListMisc) {
                            if ((ci.getStartMillis() <= m) && (m <= ci.getEndMillis())) {
                                ci.setCounter();
                                break;
                            }
                        }
                        break;
                }
            }

            for (CounterInterval ci : intervalsListFiles) {
                dataBar.add(new AlertsBar("FILE", ci.getCounter(), ci.getStart().toDate()));
            }
            for (CounterInterval ci : intervalsListHosts) {
                dataBar.add(new AlertsBar("HOST", ci.getCounter(), ci.getStart().toDate()));
            }
            for (CounterInterval ci : intervalsListNet) {
                dataBar.add(new AlertsBar("NET", ci.getCounter(), ci.getStart().toDate()));
            }
            for (CounterInterval ci : intervalsListMisc) {
                dataBar.add(new AlertsBar("MISC", ci.getCounter(), ci.getStart().toDate()));
            }
        }
    }

    public List<AlertsPie> getBeanCollectionPie() {
        createDataPie();
        return dataPie;
    }

    public List<AlertsBar> getBeanCollectionBar() {
        createDataBar();
        return dataBar;
    }
}
