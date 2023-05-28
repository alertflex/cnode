package org.alertflex.posture.nmap;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.SAXParser;
import org.xml.sax.SAXException;

public class NmapJson {

    public List<NmapReport> getResult(InputStream xmlData) {

        SAXParserFactory factory = SAXParserFactory.newInstance();

        factory.setValidating(true);
        factory.setNamespaceAware(false);

        try {

            SAXParser parser = factory.newSAXParser();
            NmapParser ns = new NmapParser();

            parser.parse(xmlData, ns);

            xmlData.close();

            return ns.getResult();

        } catch (ParserConfigurationException | SAXException | IOException e) {

            return null;
        }

    }
}
