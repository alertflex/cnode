/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alertflex.common;

import java.util.ArrayList;
import java.util.List;
import org.alertflex.entity.Project;

/**
 *
 * @author root
 */
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
