/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ddicark.auxiliares;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author INFORMATICA
 */
public class VentasMes {
    
    private Object[] columna;

   

    public VentasMes() {
    }

    public VentasMes(Object[] columna) {
        this.columna = columna;
    }

    public Object[] getColumna() {
        return columna;
    }

    public void setColumna(Object[] columna) {
        this.columna = columna;
    }

    
    public Object getItemColumna(int i){
        return columna[i];
    }
    
    
            
    
}
