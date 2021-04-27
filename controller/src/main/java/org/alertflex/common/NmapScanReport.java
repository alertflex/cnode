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

public class NmapScanReport {

    String protocol;
    String portid;
    String state;
    String name;

    public void setProtocol(String p) {
        this.protocol = p;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setPortid(String p) {
        this.portid = p;
    }

    public String getPortid() {
        return portid;
    }

    public void setState(String s) {
        this.state = s;
    }

    public String getState() {
        return state;
    }

    public void setName(String n) {
        this.name = n;
    }

    public String getName() {
        return name;
    }
}
