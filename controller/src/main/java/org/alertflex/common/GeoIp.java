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

public class GeoIp {

    String ip;
    String cc;
    float lat;
    float lon;

    public GeoIp(String ip) {
        this.ip = ip;
        this.cc = "indef";
        this.lat = 0;
        this.lon = 0;
    }

    public GeoIp(String ip, String cc, float lat, float lon) {
        this.ip = ip;
        this.cc = cc;
        this.lat = lat;
        this.lon = lon;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public Float getLat() {
        return lat;
    }

    public void setLat(Float lat) {
        this.lat = lat;
    }

    public Float getLon() {
        return lon;
    }

    public void setLon(Float lon) {
        this.lon = lon;
    }

    public String getSrcIp() {
        String srcip = "{ \"ip\":\""
                + ip
                + "\",\"country_code\":\""
                + cc
                + "\",\"latitude\":"
                + lat
                + ",\"longitude\":"
                + lon
                + "}";

        return srcip;
    }

    public String getDstIp() {
        String dstip = "{ \"ip\":\""
                + ip
                + "\",\"country_code\":\""
                + cc
                + "\",\"latitude\":"
                + lat
                + ",\"longitude\":"
                + lon
                + "}";

        return dstip;
    }
}
