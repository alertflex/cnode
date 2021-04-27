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
