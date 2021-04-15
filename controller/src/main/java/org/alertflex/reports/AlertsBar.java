/*
 * Alertflex Management Console
 *
 * Copyright (C) 2021 Oleg Zharkov
 *
 */

package org.alertflex.reports;

import java.util.Date;

public class AlertsBar {

    private String type;
    private Integer count;
    private Date period;

    public AlertsBar(
            String t,
            Integer c,
            Date p
    ) {
        type = t;
        count = c;
        period = p;
    }

    public AlertsBar getMe() {
        return this;
    }

    public String getType() {
        return type;
    }

    public Integer getCount() {
        return count;
    }

    public Date getPeriod() {
        return period;
    }
}
