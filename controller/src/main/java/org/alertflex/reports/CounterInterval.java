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

import org.joda.time.DateTime;
import org.joda.time.Interval;

public class CounterInterval {

    String alertType;
    Integer counter;
    Interval interval;

    public CounterInterval(String t) {
        counter = 0;
        alertType = t;
    }

    public CounterInterval(String t, DateTime s, DateTime e) {
        counter = 0;
        alertType = t;
        interval = new Interval(s, e);
    }

    public String getAlertType() {
        return alertType;
    }

    public void setAlertType(String at) {
        alertType = at;
    }

    public DateTime getStart() {
        return interval.getStart();
    }

    public DateTime getEnd() {
        return interval.getEnd();
    }

    public Long getStartMillis() {
        return interval.getStartMillis();
    }

    public Long getEndMillis() {
        return interval.getEndMillis();
    }

    public Integer getCounter() {
        return counter;
    }

    public void setCounter() {
        counter++;
    }

    public void setCounter(Integer c) {
        counter = counter + c;
    }
}
