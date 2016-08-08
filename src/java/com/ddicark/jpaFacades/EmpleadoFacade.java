/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ddicark.jpaFacades;

import com.ddicark.auxiliares.JsfUtil;
import com.ddicark.auxiliares.funciones;
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
public class EmpleadoFacade extends AbstractFacade<Empleado> {
    @PersistenceContext(unitName = "ddicark-shoesPU")
    private EntityManager em; //Gestiona las consultas a la BD

    @Override
    protected EntityManager getEntityManager() {
        return em; //Devuelve el gestor de la conexion a la BD 
    }
    
    //CONSTRUCTOR
    public EmpleadoFacade() {
        super(Empleado.class);
    }
    
    //FUNCIONES DE CONSULTAS
    
    /* Funcion que verifica las credenciales del usuario que hace login
     *      - devuelve una lista vacia si no existe el usuario con el password digitados
     *      - 
     */
    public List<Empleado> existeUser(String usr,String pass){
        List<Empleado> resultado =null;
        try{
            Query consulta = em.createNamedQuery("Empleado.findByUsuario"); //Especificar nombre de la consulta que se va a ejecutar
            //Se le pasan los parametros a la consulta
            pass = new JsfUtil().getMD5(pass);
            consulta.setParameter("username", usr);
            consulta.setParameter("password", pass);
            resultado = consulta.getResultList();
            return resultado;   
        }catch(Exception e){
            new funciones().setMsj(4,"ERROR DE CONEXION A LA BASE DE DATOS\n DETALLES: " + e.getMessage());
            return resultado;
        }
        
    }
    
    
    
    //funcion QUE VERIFICA SI EXISTE EL CODIGOEMPLEADO
    public boolean existeCodigo(String codigo){
        List<Empleado> resultado =null;
        try{
            Query consulta = em.createNamedQuery("Empleado.findByCodempleado"); //Especificar nombre de la consulta que se va a ejecutar
            //Se le pasan los parametros a la consulta
            consulta.setParameter("codempleado", codigo);
            resultado = consulta.getResultList();
            if(resultado.isEmpty()){
                return false;  
            }else{
                return true;
            } 
        }catch(Exception e){
            new funciones().setMsj(4,"ERROR AL CONSULTAR CODIGO EMPLEADO DE LA BD");
            return false;
        }
        
    }
    
    
    
    //funcion QUE VERIFICA SI EL USERNAME ESTA DISPONIBLE
    public boolean existeUsername(String username){
        List<Empleado> resultado =null;
        try{
            Query consulta = em.createNamedQuery("Empleado.findByUsername"); //Especificar nombre de la consulta que se va a ejecutar
            //Se le pasan los parametros a la consulta
            consulta.setParameter("username", username);
            resultado = consulta.getResultList();
            if(resultado.isEmpty()){
                return false;  
            }else{
                return true;
            } 
        }catch(Exception e){
            new funciones().setMsj(4,"ERROR AL VERIFICAR DISPONIBILIDAD DE USERNAME EN LA BD");
            return false;
        }
        
    }
    
    /*
     * DEVUELVE EL LISTADO DE LOS VENDEDORES
     */
    public List<Empleado> listaVendedores(){
        List<Empleado> resultado =null;
        try{
            Query consulta = em.createNamedQuery("Empleado.vendedores");
            resultado = consulta.getResultList();
            return resultado;   
        }catch(Exception e){
            new funciones().setMsj(4,"ERROR DE CONEXION A LA BASE AL CONSULTAR VENDEDORES");
            return resultado;
        }
        
    }
    
    
    /*
     * DEVUELVE EL LISTADO DE LOS USUARIOS
     */
    public List<Empleado> listaUsuarios(){
        List<Empleado> resultado =null;
        try{
            Query consulta = em.createNamedQuery("Empleado.usuarios");
            resultado = consulta.getResultList();
            return resultado;   
        }catch(Exception e){
            new funciones().setMsj(4,"ERROR DE CONEXION A LA BASE AL CONSULTAR Usuarios");
            return resultado;
        }
        
    }
    
    
    
}
