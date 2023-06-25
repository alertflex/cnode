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

package org.alertflex.supp;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author root
 */
@XmlRootElement
public class PosturePostBody {

    @XmlElement public Integer delay;
    @XmlElement public String alertCorr;
    @XmlElement public String postureType;
    @XmlElement public String target;
    @XmlElement public String host;
    @XmlElement public String vrn;
    @XmlElement public String project;
    
    public Integer getDelay() {
        return delay;
    }
    
    public void setDelay(Integer delay) {
        this.delay = delay;
    }
    
    public String getAlertCorr() {
        return alertCorr;
    }
    
    public void setAlertCorr(String alertCorr) {
        this.alertCorr = alertCorr;
    }
    
    public String getPostureType() {
        return postureType;
    }
    
    public void setPostureType(String postureType) {
        this.postureType = postureType;
    }
    
    public String getTarget() {
        return target;
    }
    
    public void setTarget(String target) {
        this.target = target;
    }
    
    public String getHost() {
        return host;
    }
    
    public void setHost(String host) {
        this.host = host;
    }
    
    public String getVrn() {
        return vrn;
    }
    
    public void setVrn(String vrn) {
        this.vrn = vrn;
    }
    
    public String getProject() {
        return project;
    }
    
    public void setProject(String project) {
        this.project = project;
    }
}
