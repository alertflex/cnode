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
    
    
    List<Finding> kubehunterFindings;
    List<Finding> zapFindings;
    
    public JasperDataScanners(List<Finding> lkf, List<Finding> lzf) {
        
        kubehunterFindings = lkf;
        
        zapFindings = lzf;
    }
    
    /**
     *
     */
    public Collection<Finding> getKubehunterFindings() {
        return kubehunterFindings;
    }

    public Collection<Finding> getZapFindings() {
        return zapFindings;
    }
}
