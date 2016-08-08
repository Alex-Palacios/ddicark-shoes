/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ddicark.auxiliares;

/**
 *
 * @author DDICARK
 */

public class curvaColor {
    private String color;
    private int T1;
    private int T2;
    private int T3;
    private int T4;
    private int T5;
    private int T6;
    private int T7;
    private int T8;
    private int T9;
    private int T10;
    
   
    public curvaColor() {
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

    public int getT2() {
        return T2;
    }

    public void setT2(int T2) {
        this.T2 = T2;
    }

    public int getT3() {
        return T3;
    }

    public void setT3(int T3) {
        this.T3 = T3;
    }

    public int getT4() {
        return T4;
    }

    public void setT4(int T4) {
        this.T4 = T4;
    }

    public int getT5() {
        return T5;
    }

    public void setT5(int T5) {
        this.T5 = T5;
    }

    public int getT6() {
        return T6;
    }

    public void setT6(int T6) {
        this.T6 = T6;
    }

    public int getT7() {
        return T7;
    }

    public void setT7(int T7) {
        this.T7 = T7;
    }

    public int getT8() {
        return T8;
    }

    public void setT8(int T8) {
        this.T8 = T8;
    }

    public int getT9() {
        return T9;
    }

    public void setT9(int T9) {
        this.T9 = T9;
    }

    public int getT10() {
        return T10;
    }

    public void setT10(int T10) {
        this.T10 = T10;
    }

   public int getCantidad(int posicion){
       switch(posicion){
           case 0: return this.T1;
           case 1: return this.T2;
           case 2: return this.T3;
           case 3: return this.T4;
           case 4: return this.T5;
           case 5: return this.T6;
           case 6: return this.T7;
           case 7: return this.T8;
           case 8: return this.T9;
           case 9: return this.T10;
       }
       return 0;
   }

   
    public void resetear(){
        this.color = "";
        this.T1 = 0;
        this.T2 = 0;
        this.T3 = 0;
        this.T4 = 0;
        this.T5 = 0;
        this.T6 = 0;
        this.T7 = 0;
        this.T8 = 0;
        this.T9 = 0;
        this.T10 = 0;
        
    }

    
}
