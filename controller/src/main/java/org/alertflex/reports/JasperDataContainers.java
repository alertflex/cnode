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
public class JasperDataContainers {
    
    List<Finding> dockerBenchFindings;
    List<Finding> kubeBenchFindings;
    List<Finding> kubeHunterFindings;
    List<Finding> trivyFindings;
        
    public JasperDataContainers(List<Finding> ldf, List<Finding> lkf, List<Finding> lhf, List<Finding> ltf) {
        
        dockerBenchFindings = ldf;
        
        kubeBenchFindings = lkf;
        
        kubeHunterFindings = lhf;
        
        trivyFindings = ltf;
    }
    
    /**
     *
     */
    public Collection<Finding> getDockerBenchFindings() {
        return dockerBenchFindings;
    }
    
    public Collection<Finding> getKubeBenchFindings() {
        return kubeBenchFindings;
    }
    
    public Collection<Finding> getKubeHunterFindings() {
        return kubeHunterFindings;
    }
    
    public Collection<Finding> getTrivyFindings() {
        return trivyFindings;
    }

}
