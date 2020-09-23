/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alertflex.mc.supp;

import org.joda.time.DateTime;
import org.joda.time.Interval;

/**
 *
 * @author root
 */
public class CounterInterval {
    long counter;
    Interval interval;
    
    public CounterInterval (DateTime s, DateTime e) {
        counter = 0;
        
        interval = new Interval(s, e);
    }
    
    public DateTime getStart() {
        return interval.getStart();
    }
    
    public DateTime getEnd() {
        return interval.getEnd();
    }
    
    public Long getStartMillis() {
        return interval.getStartMillis();
    }
    
    public Long getEndMillis() {
        return interval.getEndMillis();
    }
    
    public long getCounter() {
        return counter;
    }
    
    public void setCounter() {
        counter++;
    }
    
    public void setCounter(long c) {
        counter = counter + c;
    }
}
