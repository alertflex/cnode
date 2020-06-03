/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alertflex.filters;

/**
 *
 * @author root
 */

public class Aggregate {
    int reproduced = 0;
    int inPeriod = 0;
               
    public void setReproduced (int r) {
        this.reproduced = r;
    }
    
    public int getReproduced () {
        return reproduced;
    }
    
    public void setInPeriod (int p) {
        this.inPeriod = p;
    }
    
    public int getInPeriod () {
        return inPeriod;
    }
    
}
