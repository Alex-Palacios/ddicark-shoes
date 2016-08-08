/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ddicark.auxiliares;

/**
 *
 * @author ALEX
 */
public class InventarioConsulta {
    
    private Object[] columna;

   

    public InventarioConsulta() {
    }

    public InventarioConsulta(Object[] columna) {
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
