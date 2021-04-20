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
    List<Finding> snykFindings;
    List<Finding> inspectorFindings;
    
    public JasperDataScanners(List<Finding> lif, List<Finding> lsf, List<Finding> lzf) {
        
        zapFindings = lzf;
        
        snykFindings = lsf;
        
        inspectorFindings = lif;
    }
    
    /**
     *
     */
    public Collection<Finding> getZapFindings() {
        return zapFindings;
    }
    
    public Collection<Finding> getSnykFindings() {
        return snykFindings;
    }

    public Collection<Finding> getInspectorFindings() {
        return inspectorFindings;
    }
    
}
