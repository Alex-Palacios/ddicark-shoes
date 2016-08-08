/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ddicark.jpaFacades;

import com.ddicark.auxiliares.funciones;
import com.ddicark.entidades.FacturaIngreso;
import com.ddicark.entidades.FacturaIngresoPK;
import com.ddicark.entidades.Proveedor;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author DDICARK
 */
@Stateless
public class FacturaIngresoFacade extends AbstractFacade<FacturaIngreso> {
    @PersistenceContext(unitName = "ddicark-shoesPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public FacturaIngresoFacade() {
        super(FacturaIngreso.class);
    }
    
    //FUNCIONES Y CONSULTAS
    /*
     * FUNCION QUE VERIFICA SI LA FACTURA HA INGRESAR YA EXISTE EN EL SISTEMA
     */
    public boolean existeFacturaIngreso(FacturaIngresoPK clave, String tipo){
        boolean existe;
        Query query = em.createNamedQuery("FacturaIngreso.existeFactura");
        //Se le pasan los parametros a la consulta
        query.setParameter("facturaIngresoPK",clave);
        query.setParameter("tipoFactura",tipo);
        if (!query.getResultList().isEmpty()) {
            existe = true;
        }else{
            existe = false;
        }
        return existe;
    }
    
    /*
     * FUNCION QUE DEVUELVE EL LISTADO DE LAS FACTURAS CON ESTADO = "PRE INGRESO"
     */
     
    public List<FacturaIngreso> facturasPreIngreso(){
        List<FacturaIngreso> resultado =null;
        try{
            Query consulta = em.createNamedQuery("FacturaIngreso.findPREINGRESOS"); //Especificar nombre de la consulta que se va a ejecutar
            //Se le pasan los parametros a la consulta
            //consulta.setParameter("estadoFactura","PRE INGRESO");
            
            resultado = consulta.getResultList();
            return resultado;   
        }catch(Exception e){
            new funciones().setMsj(4,"ERROR: No se pudo consultar Facturas de PRE INGRESO a la Bases de Datos");
            return resultado;
        }
        
    }
    
    /*
     * FUNCION QUE DEVUELVE EL LISTADO DE LAS FACTURAS CON ESTADO = "PROCESANDO"
     */
     
    public List<FacturaIngreso> facturasProcesando(){
        List<FacturaIngreso> resultado =null;
        try{
            Query consulta = em.createNamedQuery("FacturaIngreso.findByEstadoFactura"); //Especificar nombre de la consulta que se va a ejecutar
            //Se le pasan los parametros a la consulta
            consulta.setParameter("estadoFactura","PROCESANDO");
            resultado = consulta.getResultList();
            return resultado;   
        }catch(Exception e){
            new funciones().setMsj(4,"ERROR: No se pudo consultar Facturas PROCESADAS en la Bases de Datos");
            return resultado;
        }
        
    }
    
    
    /*
     * FUNCION QUE DEVUELVE EL LISTADO DE LAS FACTURAS ANTERIORES DE UN PROVEEDOR
     */
     
    public List<FacturaIngreso> facturasAnteriores(Proveedor proveedor){
        List<FacturaIngreso> resultado =null;
        try{
            Query consulta = em.createNamedQuery("FacturaIngreso.facturaByProveedor"); //Especificar nombre de la consulta que se va a ejecutar
            //Se le pasan los parametros a la consulta
            consulta.setParameter("proveedor",proveedor);
            resultado = consulta.getResultList();
            return resultado;   
        }catch(Exception e){
            new funciones().setMsj(4,"ERROR: No se pudo consultar Facturas PROCESADAS en la Bases de Datos");
            return resultado;
        }
        
    }
    
    /*
     * FUNCION QUE DEVUELVE EL LISTADO DE LAS FACTURAS ANTERIORES DE UN PROVEEDOR
     */
     
    public List<FacturaIngreso> facturasCreditosActivos(){
        List<FacturaIngreso> resultado =null;
        try{
            Query consulta = em.createNamedQuery("FacturaIngreso.creditosActivos"); 
            resultado = consulta.getResultList();
            return resultado;   
        }catch(Exception e){
            new funciones().setMsj(4,"ERROR: No se pudo consultar los CREDITO ACTIVOS en la Bases de Datos");
            return resultado;
        }
        
    }
    
    
    /*
     * FUNCION QUE DEVUELVE LA FACTURA DEL CORTE DE INVENTARIO INICIAL
     */
     
    public FacturaIngreso facturaCorteInventarioInicial(){
        List<FacturaIngreso> resultado =null;
        try{
            Query consulta = em.createNamedQuery("FacturaIngreso.corteInventarioInicial"); 
            resultado = consulta.getResultList();
            if(resultado != null){
                return resultado.get(0);
            }else{
                return null;
            }   
        }catch(Exception e){
            new funciones().setMsj(4,"ERROR: No se pudo consultar los FACTURA DE CORTE DE INVENTARIO en la Bases de Datos");
            return null;
        }
        
    }
    
    
     /*
     * FUNCION QUE DEVUELVE EL LISTADO DE LAS FACTURAS CON ESTADO = "PRE INGRESO"
     */
     
    public List<FacturaIngreso> facturasCompra(){
        List<FacturaIngreso> resultado =null;
        try{
            Query consulta = em.createNamedQuery("FacturaIngreso.compras"); 
            resultado = consulta.getResultList();
            return resultado;   
        }catch(Exception e){
            new funciones().setMsj(4,"ERROR: No se pudo consultar COMPRAS a la Bases de Datos");
            return resultado;
        }
        
    }
}
