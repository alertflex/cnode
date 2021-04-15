/*
 * Alertflex Management Console
 *
 * Copyright (C) 2021 Oleg Zharkov
 *
 */

package org.alertflex.common;

import org.alertflex.common.NmapScanReport;
import org.alertflex.common.NmapScanParser;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.SAXParser;
import org.xml.sax.SAXException;

public class NmapScanJson {

    public List<NmapScanReport> getResult(InputStream xmlData) {

        SAXParserFactory factory = SAXParserFactory.newInstance();

        factory.setValidating(true);
        factory.setNamespaceAware(false);

        try {

            SAXParser parser = factory.newSAXParser();
            NmapScanParser ns = new NmapScanParser();

            parser.parse(xmlData, ns);

            xmlData.close();

            return ns.getResult();

        } catch (ParserConfigurationException | SAXException | IOException e) {

            return null;
        }

    }
}
