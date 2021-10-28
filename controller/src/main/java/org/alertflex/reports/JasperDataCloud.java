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
import java.util.Collection;
import java.util.Date;
import java.util.List;
import org.alertflex.entity.Alert;
import org.joda.time.DateTime;

/**
 *
 * @author root
 */
public class JasperDataCloud {
    
    DateTime start;
    DateTime end;
    int numIntervals;
    
    List<Alert> alertsList;

    private List<AlertsPie> dataPie;
    
    List<Finding> inspectorFindings;
    
        
    public JasperDataCloud(List<Finding> lsnykf, List<Alert> al, Date s, Date e, int n) {
        
        inspectorFindings = lsnykf;
        
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
        
        if ((alertsList != null) && (alertsList.size() > 0)) {

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
        
        } else {
            dataPie.add(new AlertsPie("none", 1));
        }
    }
    
    public List<AlertsPie> getBeanCollectionPie() {
        createDataPie();
        return dataPie;
    }
    
    public Collection<Finding> getInspectorFindings() {
        return inspectorFindings;
    }

}
