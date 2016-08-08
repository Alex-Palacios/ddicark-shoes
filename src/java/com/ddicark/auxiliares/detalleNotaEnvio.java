/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ddicark.auxiliares;

import java.math.BigDecimal;

/**
 *
 * @author DDICARK
 */
public class detalleNotaEnvio {
    
    private String estilo;
    private String color;
    private String T1;
    private String T2;
    private String T3;
    private String T4;
    private String T5;
    private String T6;
    private String T7;
    private String T8;
    private String T9;
    private String T10;
    private Integer unidades;
    private BigDecimal precio;
    private BigDecimal venta;

    public detalleNotaEnvio() {
        
    }

    public detalleNotaEnvio(String estilo, String color, String T1, String T2, String T3, String T4, String T5, String T6, String T7, String T8, String T9, String T10, Integer unidades, BigDecimal precio, BigDecimal venta) {
        this.estilo = estilo;
        this.color = color;
        this.T1 = T1;
        this.T2 = T2;
        this.T3 = T3;
        this.T4 = T4;
        this.T5 = T5;
        this.T6 = T6;
        this.T7 = T7;
        this.T8 = T8;
        this.T9 = T9;
        this.T10 = T10;
        this.unidades = unidades;
        this.precio = precio;
        this.venta = venta;
    }
    
    

    public String getEstilo() {
        return estilo;
    }

    public void setEstilo(String estilo) {
        this.estilo = estilo;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getT1() {
        return T1;
    }

    public void setT1(String T1) {
        this.T1 = T1;
    }

    public String getT2() {
        return T2;
    }

    public void setT2(String T2) {
        this.T2 = T2;
    }

    public String getT3() {
        return T3;
    }

    public void setT3(String T3) {
        this.T3 = T3;
    }

    public String getT4() {
        return T4;
    }

    public void setT4(String T4) {
        this.T4 = T4;
    }

    public String getT5() {
        return T5;
    }

    public void setT5(String T5) {
        this.T5 = T5;
    }

    public String getT6() {
        return T6;
    }

    public void setT6(String T6) {
        this.T6 = T6;
    }

    public String getT7() {
        return T7;
    }

    public void setT7(String T7) {
        this.T7 = T7;
    }

    public String getT8() {
        return T8;
    }

    public void setT8(String T8) {
        this.T8 = T8;
    }

    public String getT9() {
        return T9;
    }

    public void setT9(String T9) {
        this.T9 = T9;
    }

    public String getT10() {
        return T10;
    }

    public void setT10(String T10) {
        this.T10 = T10;
    }
    

    public Integer getUnidades() {
        return unidades;
    }

    public void setUnidades(Integer unidades) {
        this.unidades = unidades;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public BigDecimal getVenta() {
        return venta;
    }

    public void setVenta(BigDecimal venta) {
        this.venta = venta;
    }

   
    
    
    public void setCurva(int talla,String valor){
        switch(talla){
            case 0: this.T1 = valor; break;
            case 1: this.T2 = valor; break;
            case 2: this.T3 = valor; break;
            case 3: this.T4 = valor; break;
            case 4: this.T5 = valor; break;
            case 5: this.T6 = valor; break;
            case 6: this.T7 = valor; break;
            case 7: this.T8 = valor; break;
            case 8: this.T9 = valor; break;
            case 9: this.T10 = valor; break;
            
        }
    }
    
    
    
}
