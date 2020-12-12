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
public class Response {

    String profile;
    String newEvent;
    int newSeverity;
    String newCategory;
    String newDesc;
    String newType;
    String newSource;

    public Response() {
        profile = "indef";
        newCategory = "indef";
        newDesc = "indef";
        newType = "indef";
        newSource = "indef";
        newSeverity = 0;
    }

    public void setProfile(String p) {
        this.profile = p;
    }

    public String getProfile() {
        return profile;
    }

    public void setNewEvent(String e) {
        this.newEvent = e;
    }

    public String getNewEvent() {
        return newEvent;
    }

    public void setNewSeverity(int p) {
        this.newSeverity = p;
    }

    public int getNewSeverity() {
        return newSeverity;
    }

    public void setNewCategory(String c) {
        this.newCategory = c;
    }

    public String getNewCategory() {
        return newCategory;
    }

    public void setNewDesc(String d) {
        this.newDesc = d;
    }

    public String getNewDesc() {
        return newDesc;
    }

    public void setNewType(String t) {
        this.newType = t;
    }

    public String getNewType() {
        return newType;
    }

    public void setNewSource(String s) {
        this.newSource = s;
    }

    public String getNewSource() {
        return newSource;
    }
}
