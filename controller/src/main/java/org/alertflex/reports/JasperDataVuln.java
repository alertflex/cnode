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
public class JasperDataVuln {
    
    List<Finding> applicationsFindings;   
    List<Finding> dockerImagesFindings;
    List<Finding> hostsFindings;
    List<Finding> kubernetesFindings;
    
        
    public JasperDataVuln(List<Finding> laf, List<Finding> ldf, List<Finding> lhf, List<Finding> lkf) {
        
        applicationsFindings = laf;
        dockerImagesFindings = ldf;
        hostsFindings = lhf;
        kubernetesFindings = lkf;
        
    }
    
    
    public Collection<Finding> getApplicationsFindings() {
        return applicationsFindings;
    }
    
    public Collection<Finding> getDockerImagesFindings() {
        return dockerImagesFindings;
    }
    
    public Collection<Finding> getHostFindings() {
        return hostsFindings;
    }
    
    public Collection<Finding> getKubernetesFindings() {
        return kubernetesFindings;
    }

}
