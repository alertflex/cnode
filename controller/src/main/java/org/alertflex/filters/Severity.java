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
public class Severity {

    public int threshold;
    public int level0;
    public int level1;
    public int level2;

    public Severity() {

        threshold = 0;
        level0 = 0;
        level1 = 0;
        level2 = 0;
    }

    public void setThreshold(int t) {
        this.threshold = t;
    }

    public int getThreshold() {
        return threshold;
    }

    public void setLevel0(int l) {
        this.level0 = l;
    }

    public int getLevel0() {
        return level0;
    }

    public void setLevel1(int l) {
        this.level1 = l;
    }

    public int getLevel1() {
        return level1;
    }

    public void setLevel2(int l) {
        this.level2 = l;
    }

    public int getLevel2() {
        return level2;
    }
}
