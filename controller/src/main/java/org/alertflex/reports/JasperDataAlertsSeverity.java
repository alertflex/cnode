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

public class JasperDataAlertsSeverity {

    DateTime start;
    DateTime end;
    int numIntervals;

    List<Alert> alertsList;

    private List<AlertsPie> dataPie;
    private List<AlertsBar> dataBar;

    public JasperDataAlertsSeverity(List<Alert> al, Date s, Date e, int n) {

        start = new DateTime(s);
        end = new DateTime(e);
        numIntervals = n;
        alertsList = al;
    }

    public void createDataPie() {

        dataPie = new ArrayList();

        CounterInterval ciSev0 = new CounterInterval("Sev0");
        CounterInterval ciSev1 = new CounterInterval("Sev1");
        CounterInterval ciSev2 = new CounterInterval("Sev2");
        CounterInterval ciSev3 = new CounterInterval("Sev3");

        for (Alert a : alertsList) {

            switch (a.getAlertSeverity()) {
                case 0:
                    ciSev0.setCounter();
                    break;
                case 1:
                    ciSev1.setCounter();
                    break;
                case 2:
                    ciSev2.setCounter();
                    break;
                case 3:
                    ciSev3.setCounter();
                    break;
            }
        }

        dataPie.add(new AlertsPie("Sev0", ciSev0.getCounter()));
        dataPie.add(new AlertsPie("Sev1", ciSev1.getCounter()));
        dataPie.add(new AlertsPie("Sev2", ciSev2.getCounter()));
        dataPie.add(new AlertsPie("Sev3", ciSev3.getCounter()));
    }

    public void createDataBar() {

        dataBar = new ArrayList();

        long sd = start.getMillis();
        long singlePart = (end.getMillis() - sd) / numIntervals;

        List<CounterInterval> intervalsListSev0 = new ArrayList<CounterInterval>();
        List<CounterInterval> intervalsListSev1 = new ArrayList<CounterInterval>();
        List<CounterInterval> intervalsListSev2 = new ArrayList<CounterInterval>();
        List<CounterInterval> intervalsListSev3 = new ArrayList<CounterInterval>();

        if (singlePart > 0) {

            for (int i = 0; i < numIntervals; i++) {

                DateTime startDT = new DateTime(sd + singlePart * i);
                DateTime endDT = new DateTime(sd + singlePart * (i + 1));

                intervalsListSev0.add(new CounterInterval("Sev0", startDT, endDT));
                intervalsListSev1.add(new CounterInterval("Sev1", startDT, endDT));
                intervalsListSev2.add(new CounterInterval("Sev2", startDT, endDT));
                intervalsListSev3.add(new CounterInterval("Sev3", startDT, endDT));
            }

            for (Alert a : alertsList) {

                DateTime date = new DateTime(a.getTimeCollr());
                Long m = date.getMillis();

                switch (a.getAlertSeverity()) {
                    case 0:
                        for (CounterInterval ci : intervalsListSev0) {
                            if ((ci.getStartMillis() <= m) && (m <= ci.getEndMillis())) {
                                ci.setCounter();
                                break;
                            }
                        }
                        break;
                    case 1:
                        for (CounterInterval ci : intervalsListSev1) {
                            if ((ci.getStartMillis() <= m) && (m <= ci.getEndMillis())) {
                                ci.setCounter();
                                break;
                            }
                        }
                        break;
                    case 2:
                        for (CounterInterval ci : intervalsListSev2) {
                            if ((ci.getStartMillis() <= m) && (m <= ci.getEndMillis())) {
                                ci.setCounter();
                                break;
                            }
                        }
                        break;
                    case 3:
                        for (CounterInterval ci : intervalsListSev3) {
                            if ((ci.getStartMillis() <= m) && (m <= ci.getEndMillis())) {
                                ci.setCounter();
                                break;
                            }
                        }
                        break;
                }
            }

            for (CounterInterval ci : intervalsListSev0) {
                dataBar.add(new AlertsBar("Sev0", ci.getCounter(), ci.getStart().toDate()));
            }
            for (CounterInterval ci : intervalsListSev1) {
                dataBar.add(new AlertsBar("Sev1", ci.getCounter(), ci.getStart().toDate()));
            }
            for (CounterInterval ci : intervalsListSev2) {
                dataBar.add(new AlertsBar("Sev2", ci.getCounter(), ci.getStart().toDate()));
            }
            for (CounterInterval ci : intervalsListSev3) {
                dataBar.add(new AlertsBar("Sev3", ci.getCounter(), ci.getStart().toDate()));
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
