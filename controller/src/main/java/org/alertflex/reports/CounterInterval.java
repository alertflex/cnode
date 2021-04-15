/*
 * Alertflex Management Console
 *
 * Copyright (C) 2021 Oleg Zharkov
 *
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
