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
public class estadoCuentaDatasource implements JRDataSource {

    private List<ItemCuenta> listaDetalle = new ArrayList<ItemCuenta>();
    private int index = -1;
        
    
    @Override
    public boolean next() throws JRException {
        return ++index < listaDetalle.size();
    }

    @Override
    public Object getFieldValue(JRField jrf) throws JRException {
        Object valor = null; 
        if("FECHA".equals(jrf.getName())){ 
            valor = listaDetalle.get(index).getFECHA();
        }else if("TRANSACCION".equals(jrf.getName())){ 
            valor = listaDetalle.get(index).getTRANSACCION();
        }else if("DOCUMENTO".equals(jrf.getName())){ 
            valor = listaDetalle.get(index).getDOCUMENTO();
        }else if("DEBITO".equals(jrf.getName())){ 
            valor = listaDetalle.get(index).getDEBITO();
        }else if("CREDITO".equals(jrf.getName())){ 
            valor = listaDetalle.get(index).getCREDITO();
        }else if("SALDO".equals(jrf.getName())){ 
            valor = listaDetalle.get(index).getSALDO();
        }

        return valor; 
    }
    
    

    public List<ItemCuenta> getListaDetalle() {
        return listaDetalle;
    }
    
    //AGREGA UN REGISTRO A LA LISTA
    public void addLista(ItemCuenta detalle){
        this.listaDetalle.add(detalle);
    }
    
    
}
