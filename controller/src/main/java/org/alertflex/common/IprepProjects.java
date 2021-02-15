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

package org.alertflex.common;

import java.util.ArrayList;
import java.util.List;
import org.alertflex.entity.Project;

public class IprepProjects {

    List<IprepValue> projectValueList;

    public IprepProjects() {
        projectValueList = new ArrayList<>();
    }

    public IprepValue getProjectValue(Project p) {

        for (IprepValue pc : projectValueList) {
            if (pc.project.equals(p.getName())) {

                pc.updateTimerange(p.getIprepTimerange());
                return pc;
            }
        }

        IprepValue newValues = new IprepValue(p);
        projectValueList.add(newValues);

        return newValues;
    }

    public boolean checkTimerCounter(Project p) {

        IprepValue pv = getProjectValue(p);

        return pv.CheckTimerCounter();
    }

    public boolean checkIpCounter(Project p, Long c) {

        if (c == 0) {
            return false;
        }

        for (IprepValue pc : projectValueList) {
            if (pc.project.equals(p.getName())) {

                if (c != pc.getIpCounter()) {
                    return true;
                } else {
                    return false;
                }
            }
        }

        return false;
    }

    public void setIpCounter(Project p, Long c) {

        for (IprepValue pc : projectValueList) {
            if (pc.project.equals(p.getName())) {

                pc.setIpCounter(c);
            }
        }
    }

    public boolean dirStatus(Project p) {

        for (IprepValue pc : projectValueList) {
            if (pc.project.equals(p.getName())) {

                return pc.dirStatus();
            }
        }

        return false;
    }

    public String getProjectListsDir(Project p) {

        for (IprepValue pc : projectValueList) {
            if (pc.project.equals(p.getName())) {

                return pc.getProjectListsDir();
            }
        }

        return "";
    }

}
