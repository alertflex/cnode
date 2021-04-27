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
