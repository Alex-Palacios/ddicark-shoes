/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ddicark.auxiliares;



/**
 *
 * @author DDICARK
 */
public class tallaCant {
    private String color;
    private int T1;

    public tallaCant() {
    }
    
    public tallaCant(String color, int T1) {
        this.color = color;
        this.T1 = T1;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getT1() {
        return T1;
    }

    public void setT1(int T1) {
        this.T1 = T1;
    }
    
    public void setear(){
        this.color = "";
        this.T1 = 0;
    }
    
    
}
