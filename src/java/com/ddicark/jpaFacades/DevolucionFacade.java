/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ddicark.jpaFacades;

import com.ddicark.auxiliares.funciones;
import com.ddicark.entidades.Devolucion;
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
public class DevolucionFacade extends AbstractFacade<Devolucion> {
    @PersistenceContext(unitName = "ddicark-shoesPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DevolucionFacade() {
        super(Devolucion.class);
    }
    
    //FUNCIONES
    
     /*
     * FUNCION QUE EL SIGUIENTE CLAVE DE DEVOLUCION
     */
    public int getNextIdDevolucion() {
        Query query = em.createNamedQuery("Devolucion.maxID");
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
     * FUNCION QUE DEVUELVE EL LISTADO DE LAS DEVOLUCIONES REGISTRADAS
     */
     
    public List<Devolucion> devolucionesRegistradas(){
        List<Devolucion> resultado =null;
        try{
            Query consulta = em.createNamedQuery("Devolucion.Registradas");
            resultado = consulta.getResultList();
            return resultado;   
        }catch(Exception e){
            new funciones().setMsj(4,"ERROR: No se pudo consultar DEVOLUCIONES REGISTRADAS a la Bases de Datos");
            return resultado;
        }
        
    }
    
}
