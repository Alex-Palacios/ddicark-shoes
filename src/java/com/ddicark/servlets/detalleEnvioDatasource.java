/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ddicark.servlets;

import com.ddicark.auxiliares.detalleNotaEnvio;
import java.util.ArrayList;
import java.util.List;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

/**
 *
 * @author DDICARK
 */
public class detalleEnvioDatasource implements JRDataSource {

    private List<detalleNotaEnvio> listaDetalle = new ArrayList<detalleNotaEnvio>();
    private int indiceDetalleActual = -1;
    

    public List<detalleNotaEnvio> getListaDetalle() {
        return listaDetalle;
    }
    
    
    
    @Override
    public boolean next() throws JRException {
        return ++indiceDetalleActual < listaDetalle.size();
    }

    @Override
    public Object getFieldValue(JRField jrf) throws JRException {
        Object valor = null; 
        if("estilo".equals(jrf.getName())){ 
            valor = listaDetalle.get(indiceDetalleActual).getEstilo();
        }else if("color".equals(jrf.getName())){ 
            valor = listaDetalle.get(indiceDetalleActual).getColor();
        }else if("T1".equals(jrf.getName())){ 
            valor = listaDetalle.get(indiceDetalleActual).getT1();
        }else if("T2".equals(jrf.getName())){ 
            valor = listaDetalle.get(indiceDetalleActual).getT2();
        }else if("T3".equals(jrf.getName())){ 
            valor = listaDetalle.get(indiceDetalleActual).getT3();
        }else if("T4".equals(jrf.getName())){ 
            valor = listaDetalle.get(indiceDetalleActual).getT4();
        }else if("T5".equals(jrf.getName())){ 
            valor = listaDetalle.get(indiceDetalleActual).getT5();
        }else if("T6".equals(jrf.getName())){ 
            valor = listaDetalle.get(indiceDetalleActual).getT6();
        }else if("T7".equals(jrf.getName())){ 
            valor = listaDetalle.get(indiceDetalleActual).getT7();
        }else if("T8".equals(jrf.getName())){ 
            valor = listaDetalle.get(indiceDetalleActual).getT8();
        }else if("T9".equals(jrf.getName())){ 
            valor = listaDetalle.get(indiceDetalleActual).getT9();
        }else if("T10".equals(jrf.getName())){ 
            valor = listaDetalle.get(indiceDetalleActual).getT10();
        }else if("unidades".equals(jrf.getName())){ 
            valor = listaDetalle.get(indiceDetalleActual).getUnidades();
        }else if("precio".equals(jrf.getName())){ 
            valor = listaDetalle.get(indiceDetalleActual).getPrecio();
        }else if("venta".equals(jrf.getName())){ 
            valor = listaDetalle.get(indiceDetalleActual).getVenta();
        }

        return valor; 
    }
    
    
    //AGREGA UN REGISTRO A LA LISTA
    public void addLista(detalleNotaEnvio detalle){
        this.listaDetalle.add(detalle);
    }
    
}
