/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alertflex.reports;

import java.util.Collection;
import java.util.List;

/**
 *
 * @author root
 */
public class JasperDataScanners {
    
    List<Finding> zapFindings;
    List<Finding> inspectorFindings;
    
    public JasperDataScanners(List<Finding> lzf, List<Finding> lif) {
        
        zapFindings = lzf;
        
        inspectorFindings = lif;
    }
    
    /**
     *
     */
    public Collection<Finding> getZapFindings() {
        return zapFindings;
    }

    public Collection<Finding> getInspectorFindings() {
        return inspectorFindings;
    }
    
}
