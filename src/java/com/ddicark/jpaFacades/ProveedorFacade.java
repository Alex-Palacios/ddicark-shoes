/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ddicark.jpaFacades;

import com.ddicark.auxiliares.funciones;
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
public class ProveedorFacade extends AbstractFacade<Proveedor> {
    @PersistenceContext(unitName = "ddicark-shoesPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ProveedorFacade() {
        super(Proveedor.class);
    }
    
    
    
    //FUNCIONES DE CONSULTAS
    
    /* Funcion que verifica si el proveedor ya existe
     *      - devuelve una lista vacia si no existe
     *      - 
     */
    public List<Proveedor> existeProveedor(String nombre){
        List<Proveedor> resultado =null;
        try{
            Query consulta = em.createNamedQuery("Proveedor.findByNombreProveedor"); //Especificar nombre de la consulta que se va a ejecutar
            //Se le pasan los parametros a la consulta
            consulta.setParameter("nombreProveedor",nombre);
            resultado = consulta.getResultList();
            return resultado;   
        }catch(Exception e){
            new funciones().setMsj(1,"ERROR: No se pudo realizar consultar existencia de proveedor a la Bases de Datos");
            return resultado;
        }
        
    }
    
    /*Funcion que devuelve los proveedores Nacionales o Extranjeros
     *         parametro nacionales: true - devuelve proveedores nacionales
     *         parametro nacionales: false - devuelve proveedores extranjeros 
     */
    public List<Proveedor> getProveedores(boolean nacionales){
        List<Proveedor> resultado =null;
        try{
            Query consulta;
            if(nacionales){
                consulta = em.createNamedQuery("Proveedor.nacionales");
            }else{
                consulta = em.createNamedQuery("Proveedor.extranjeros");
            }
            resultado = consulta.getResultList();
            return resultado;
        }catch (Exception e){
            new funciones().setMsj(1,"ERROR: No se pudo realizar la consulta de proveedores");
            return resultado;
        }
    }
    
    
    
    /*
     * FUNCION QUE DEVUELVE PROVEEDOR DEL CORTE DE INVENTARIO INICIAL
     */
     
    public Proveedor proveedorCorteInventarioInicial(){
        List<Proveedor> resultado =null;
        try{
            Query consulta = em.createNamedQuery("Proveedor.corteInventarioInicial"); 
            resultado = consulta.getResultList();
            if(resultado != null){
                return resultado.get(0);
            }else{
                return null;
            }   
        }catch(Exception e){
            new funciones().setMsj(4,"ERROR: No se pudo consultar AL PROVEEDOR DE CORTE DE INVENTARIO en la Bases de Datos");
            return null;
        }
        
    }
}
