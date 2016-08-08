/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ddicark.jpaFacades;

import com.ddicark.auxiliares.funciones;
import com.ddicark.entidades.Cliente;
import com.ddicark.entidades.Empleado;
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
public class ClienteFacade extends AbstractFacade<Cliente> {
    @PersistenceContext(unitName = "ddicark-shoesPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ClienteFacade() {
        super(Cliente.class);
    }
    
    /*
     * FUNCION QUE EL SIGUIENTE CLAVE DE CLIENTE
     */
    public int getNextIdCliente() {
        Query query = em.createNamedQuery("Cliente.maxID");
        /* Objetivo: Realiza una consulta a la base de datos
         */
        Object consulta = query.getSingleResult();
        /* Objetivo: Obtiene un unico resutado de la consulta realizada
         */
        int resultado = 0;
        /* Objetivo: almacena el siguiente id valido
         */
        if (consulta != null) {
            resultado = Integer.parseInt(consulta.toString());
        }
        resultado=resultado==0?1:++resultado;
        return resultado;
    }
    
    
    /*
     * FUNCION QUE DEVUELVE LA LISTA DE CLIENTES POR NATURALEZA
     */
    public List<Cliente> getClientesByNaturaleza(String naturaleza){
        List<Cliente> resultado =null;
        try{
            Query consulta = em.createNamedQuery("Cliente.clientesByNaturaleza"); //Especificar nombre de la consulta que se va a ejecutar
            //Se le pasan los parametros a la consulta
            consulta.setParameter("naturaleza",naturaleza);
            resultado = consulta.getResultList();
            return resultado;   
        }catch(Exception e){
            new funciones().setMsj(1,"ERROR: No se pudo realizar consulta clientes por naturaleza a la Bases de Datos");
            return resultado;
        }
    
    }
    
    
     public List<Cliente> getClientesByVendedor(Empleado vendedor){
        List<Cliente> resultado =null;
        try{
            Query consulta = em.createNamedQuery("Cliente.clientesByVendedor"); //Especificar nombre de la consulta que se va a ejecutar
            //Se le pasan los parametros a la consulta
            consulta.setParameter("vendedor",vendedor);
            resultado = consulta.getResultList();
            return resultado;   
        }catch(Exception e){
            new funciones().setMsj(1,"ERROR: No se pudo realizar consulta clientes por naturaleza a la Bases de Datos");
            return resultado;
        }
    
    }
    
    
     //funcion QUE VERIFICA SI EL CLIENTE YA EXISTE
    public boolean existeCliente(String nombre, String apellido){
        List<Cliente> resultado =null;
        try{
            Query consulta = em.createNamedQuery("Cliente.existeCliente"); //Especificar nombre de la consulta que se va a ejecutar
            //Se le pasan los parametros a la consulta
            consulta.setParameter("nombre", nombre);
            consulta.setParameter("apellido", apellido);
            resultado = consulta.getResultList();
            if(resultado.isEmpty()){
                return false;  
            }else{
                return true;
            } 
        }catch(Exception e){
            new funciones().setMsj(4,"ERROR AL VERIFICAR EXISTENCIA DE CLIENTE EN LA BD");
            return false;
        }
        
    }
    
    
    
    
}
