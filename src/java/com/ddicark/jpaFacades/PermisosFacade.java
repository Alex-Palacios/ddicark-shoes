/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ddicark.jpaFacades;

import com.ddicark.auxiliares.funciones;
import com.ddicark.entidades.Empleado;
import com.ddicark.entidades.Permisos;
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
public class PermisosFacade extends AbstractFacade<Permisos> {
    @PersistenceContext(unitName = "ddicark-shoesPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PermisosFacade() {
        super(Permisos.class);
    }
    
    
    /*
     * FUNCION QUE EL SIGUIENTE CLAVE DE PERMISOS
     */
    public int getNextIdPermiso() {
        Query query = em.createNamedQuery("Permisos.maxID");
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
     * LISTA DE PERMISOS DE UN EMPLEADO 
     */
    public List<Permisos> getPermisosEmpleado(Empleado usuario){
        List<Permisos> resultado =null;
        try{
            Query consulta = em.createNamedQuery("Permisos.empleado");
            consulta.setParameter("usuario", usuario);
            resultado = consulta.getResultList();
            return resultado;   
        }catch(Exception e){
            new funciones().setMsj(4,"ERROR DE CONEXION A LA BASE AL CONSULTAR PERMISOS DEL EMPLEADO");
            return resultado;
        }
        
    }
    
    
    
     /*
     * Devuelve el listado de los diferentes MENUS
     */
    public List<String> getMenuList(){
        List<String> resultado = null;
        try{
            Query consulta = em.createNamedQuery("Permisos.listaMenus"); 
            resultado = consulta.getResultList();
            return resultado;   
        }catch(Exception e){
            new funciones().setMsj(4,"ERROR: No se pudo realizar la consulta de la lista de MENUS");
            return resultado;
        }
    }
    
    
    /*
     * Devuelve el listado de los diferentes SUBMENUS DE UN MENU
     */
    public List<String> getSubmenuList(){
        List<String> resultado = null;
        try{
            Query consulta = em.createNamedQuery("Permisos.listaSubmenus"); 
            resultado = consulta.getResultList();
            return resultado;   
        }catch(Exception e){
            new funciones().setMsj(4,"ERROR: No se pudo realizar la consulta de la lista de MENUS");
            return resultado;
        }
    }
    
    
    /*
     * LISTA DE PERMISOS DE LOS USUARIOS
     */
    public List<Permisos> getPermisosUsuarios(){
        List<Permisos> resultado =null;
        try{
            Query consulta = em.createNamedQuery("Permisos.usuarios");
            resultado = consulta.getResultList();
            return resultado;   
        }catch(Exception e){
            new funciones().setMsj(4,"ERROR DE CONEXION A LA BASE AL CONSULTAR PERMISOS DE LOS USUARIOS DEL SISTEMA");
            return resultado;
        }
        
    }
    
    
}
