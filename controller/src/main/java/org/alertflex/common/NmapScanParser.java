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
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class NmapScanParser extends DefaultHandler {

    Boolean inPorts = false;
    Boolean inPort = false;
    Boolean inState = false;
    Boolean inService = false;

    List<NmapScanReport> nsrList = new ArrayList<>();

    NmapScanReport nsr;

    String result = null;

    public List<NmapScanReport> getResult() {
        return nsrList;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {

        switch (qName) {
            case "ports":
                inPorts = true;
                break;

            case "port":
                if (inPorts) {

                    inPort = true;

                    nsr = new NmapScanReport();

                    if (attributes.getQName(0).equals("protocol")) {
                        nsr.setProtocol(attributes.getValue(0));
                    }

                    if (attributes.getQName(1).equals("portid")) {
                        nsr.setPortid(attributes.getValue(1));
                    }
                }
                break;

            case "state":
                if (inPort) {

                    inState = true;

                    if (attributes.getQName(0).equals("state")) {
                        nsr.setState(attributes.getValue(0));
                    }

                }
                break;

            case "service":
                if (inState) {

                    inService = true;

                    if (attributes.getQName(0).equals("name")) {
                        nsr.setName(attributes.getValue(0));
                        nsrList.add(nsr);
                    }

                }
                break;

            default:
                break;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        switch (localName) {
            case "ports":
                inPort = false;
                break;

            case "port":
                inPorts = false;
                break;

            case "state":
                inState = false;
                break;

            case "service":
                inService = false;
                break;

            default:
                break;
        }
    }

}
