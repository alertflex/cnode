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
public class JasperDataEndpoints {
    
    List<Finding> misconfigFindings;
    List<Finding> vulnFindings;
        
    public JasperDataEndpoints(List<Finding> lmf, List<Finding> lvf) {
        
        misconfigFindings = lmf;
        
        vulnFindings = lvf;
    }
    
    /**
     *
     */
    public Collection<Finding> getMisconfigFindings() {
        return misconfigFindings;
    }
    
    public Collection<Finding> getVulnFindings() {
        return vulnFindings;
    }

}
