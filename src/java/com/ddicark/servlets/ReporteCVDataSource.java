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
 * @author Alex
 */
public class ReporteCVDataSource implements JRDataSource {

    private List<ReporteCV> lista = new ArrayList<ReporteCV>();
    private int index = -1;
        
    
    @Override
    public boolean next() throws JRException {
        return ++index < lista.size();
    }

    @Override
    public Object getFieldValue(JRField jrf) throws JRException {
        Object valor = null; 
        if("municipio".equals(jrf.getName())){ 
            valor = lista.get(index).getMunicipio();
        }else if("cliente".equals(jrf.getName())){ 
            valor = lista.get(index).getCliente();
        }else if("direccion".equals(jrf.getName())){ 
            valor = lista.get(index).getDireccion();
        }else if("telefono".equals(jrf.getName())){ 
            valor = lista.get(index).getTelefono();
        }else if("negocio".equals(jrf.getName())){ 
            valor = lista.get(index).getNegocio();
        }else if("saldo".equals(jrf.getName())){ 
            valor = lista.get(index).getSaldo();
        }
        return valor; 
    }
    
    

    public List<ReporteCV> getLista() {
        return lista;
    }
    
    //AGREGA UN REGISTRO A LA LISTA
    public void addLista(ReporteCV cuenta){
        this.lista.add(cuenta);
    }
    
    
    
}
