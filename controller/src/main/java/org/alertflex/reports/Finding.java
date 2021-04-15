/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alertflex.reports;

/**
 *
 * @author root
 */
public class Finding {
    /**
     *
     */
    private String finding;
    private Integer count;

    /**
     *
     */
    public Finding(
            String f,
            Integer c
    ) {
        finding = f;
        count = c;
    }


    /**
     *
     */
    public Finding getMe() {
        return this;
    }


    /**
     *
     */
    public String getFinding() {
        return finding;
    }


    /**
     *
     */
    public Integer getCount() {
        return count;
    }
}
