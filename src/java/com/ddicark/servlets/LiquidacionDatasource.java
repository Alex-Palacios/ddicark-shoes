/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ddicark.servlets;

import java.util.ArrayList;
import java.util.List;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

/**
 *
 * @author ALEX
 */
public class LiquidacionDatasource implements JRDataSource {

    private List<Liquidacion> lista = new ArrayList<Liquidacion>();
    private int index = -1;
        
    
    @Override
    public boolean next() throws JRException {
        return ++index < lista.size();
    }

    @Override
    public Object getFieldValue(JRField jrf) throws JRException {
        Object valor = null; 
        if("fecha".equals(jrf.getName())){ 
            valor = lista.get(index).getFECHA();
        }else if("tipo".equals(jrf.getName())){ 
            valor = lista.get(index).getTIPO();
        }else if("cliente".equals(jrf.getName())){ 
            valor = lista.get(index).getCLIENTE();
        }else if("factura".equals(jrf.getName())){ 
            valor = lista.get(index).getFACTURA();
        }else if("municipio".equals(jrf.getName())){ 
            valor = lista.get(index).getMUNICIPO();
        }else if("monto".equals(jrf.getName())){ 
            valor = lista.get(index).getMONTO();
        }else if("saldo".equals(jrf.getName())){ 
            valor = lista.get(index).getSALDO();
        }else if("limite".equals(jrf.getName())){ 
            valor = lista.get(index).getLIMITE();
        }else if("dias".equals(jrf.getName())){ 
            valor = lista.get(index).getDIAS();
        }
        return valor; 
    }
    
    

    public List<Liquidacion> getLista() {
        return lista;
    }
    
    //AGREGA UN REGISTRO A LA LISTA
    public void addLista(Liquidacion cuenta){
        this.lista.add(cuenta);
    }
    
    
}
