/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alertflex.mc.supp;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.SAXParser;

// SAX
import org.xml.sax.SAXException;

/**
 *
 * @author root
 */
public class NmapScanJson {
    
    public List<NmapScanReport> getResult (InputStream xmlData) {
        
        SAXParserFactory factory = SAXParserFactory.newInstance();

        factory.setValidating(true);
        factory.setNamespaceAware(false);
        
        try {
            
            SAXParser parser = factory.newSAXParser();
            ParserNmapScan ns = new ParserNmapScan();
            
            parser.parse(xmlData, ns);
                
            xmlData.close();
                
            return ns.getResult();
                    
        } catch (ParserConfigurationException | SAXException | IOException e) {
            
            return null;
        } 
     
    }
}
