/*
 * Alertflex Management Console
 *
 * Copyright (C) 2021 Oleg Zharkov
 *
 */

package org.alertflex.reports;

public class AlertsPie {

    private String type;
    private Integer count;

    public AlertsPie(
            String t,
            Integer c
    ) {
        type = t;
        count = c;
    }

    public AlertsPie getMe() {
        return this;
    }

    public String getType() {
        return type;
    }

    public Integer getCount() {
        return count;
    }

}
