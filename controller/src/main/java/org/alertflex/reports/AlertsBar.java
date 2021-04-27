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
