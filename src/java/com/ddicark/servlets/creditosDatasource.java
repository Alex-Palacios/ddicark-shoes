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
public class creditosDatasource implements JRDataSource{
    private List<ItemCredito> listaDetalle = new ArrayList<ItemCredito>();
    private int index = -1;
        
    
    @Override
    public boolean next() throws JRException {
        return ++index < listaDetalle.size();
    }

    @Override
    public Object getFieldValue(JRField jrf) throws JRException {
        Object valor = null; 
        if("FACTURA".equals(jrf.getName())){ 
            valor = listaDetalle.get(index).getFACTURA();
        }else if("FECHA_FACTURA".equals(jrf.getName())){ 
            valor = listaDetalle.get(index).getFECHA();
        }else if("TOTAL".equals(jrf.getName())){ 
            valor = listaDetalle.get(index).getTOTAL();
        }else if("SALDO_ACTUAL".equals(jrf.getName())){ 
            valor = listaDetalle.get(index).getSALDO();
        }else if("DIAS".equals(jrf.getName())){ 
            valor = listaDetalle.get(index).getDIAS();
        }
        return valor; 
    }
    
    

    public List<ItemCredito> getListaDetalle() {
        return listaDetalle;
    }
    
    //AGREGA UN REGISTRO A LA LISTA
    public void addLista(ItemCredito detalle){
        this.listaDetalle.add(detalle);
    }
    
}
