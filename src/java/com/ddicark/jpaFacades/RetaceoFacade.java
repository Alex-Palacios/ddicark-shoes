/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ddicark.jpaFacades;

import com.ddicark.auxiliares.funciones;
import com.ddicark.entidades.Retaceo;
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
public class RetaceoFacade extends AbstractFacade<Retaceo> {
    @PersistenceContext(unitName = "ddicark-shoesPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public RetaceoFacade() {
        super(Retaceo.class);
    }
    
    /**
     * *****************Inicio de modulo******************* 
     *      Nombre del módulo:
     *          - getNextIdRetaceoIMP 
     *      Objetivo: 
     *          - realiza una consulta a la base de datos para obtener el id maximo
     *            de las Importaciones IMP-id le suma uno para generar uno nuevo
     */
    public String getNextIdRetaceoIMP() {
        Query query = em.createNamedQuery("Retaceo.countNumRetaceo");
        /* Objetivo: Realiza una consulta a la base de datos
         */
        //Se le pasan los parametros a la consulta
        query.setParameter("busqueda","IMP-%");
        Object consulta = query.getSingleResult();
        /* Objetivo: Obtiene un unico resutado de la consulta realizada
         */
        int resultado = 0;
        /* Objetivo: almacena el siguiente id valido
         */
        if (consulta != null) {
            resultado = Integer.parseInt(consulta.toString());
        };
        String r;
        resultado++;
        r="IMP-"+resultado;
        return r;
    }
    
    
    /**
     * *****************Inicio de modulo******************* 
     *      Nombre del módulo:
     *          - getNextIdRetaceoNAC 
     *      Objetivo: 
     *          - realiza una consulta a la base de datos para obtener el id maximo
     *            de las Compras Nacionales COMP-id le suma uno para generar uno nuevo
     */
    public String getNextIdRetaceoNAC() {
        Query query = em.createNamedQuery("Retaceo.countNumRetaceo");
        /* Objetivo: Realiza una consulta a la base de datos
         */
        //Se le pasan los parametros a la consulta
        query.setParameter("busqueda","NAC-%");
        Object consulta = query.getSingleResult();
        /* Objetivo: Obtiene un unico resutado de la consulta realizada
         */
        int resultado = 0;
        /* Objetivo: almacena el siguiente id valido
         */
        if (consulta != null) {
            resultado = Integer.parseInt(consulta.toString());
        };
        String r;
        resultado++;
        r="NAC-"+resultado;
        return r;
    }
    
    
    /*Lista de los receteos por Importacion
     * 
     */
    public List<Retaceo> getItemsImportacion(boolean aprobado){
        List<Retaceo> resultado =null;
        try{
            Query consulta = em.createNamedQuery("Retaceo.findByImportaciones"); //Especificar nombre de la consulta que se va a ejecutar
            //Se le pasan los parametros a la consulta
            consulta.setParameter("aprobado",aprobado);
            resultado = consulta.getResultList();
            return resultado;   
        }catch(Exception e){
            new funciones().setMsj(1,"ERROR: No se pudo realizar consultar existencia de retaceos por Importaciones a la Bases de Datos");
            return resultado;
        }
    
    }
    
    /*Lista de los receteos por Compras Nacionales
     * 
     */
    public List<Retaceo> getItemsNacionales(boolean aprobado){
         List<Retaceo> resultado =null;
        try{
            Query consulta = em.createNamedQuery("Retaceo.findByNacionales"); //Especificar nombre de la consulta que se va a ejecutar
            //Se le pasan los parametros a la consulta
            consulta.setParameter("aprobado",aprobado);
            resultado = consulta.getResultList();
            return resultado;   
        }catch(Exception e){
            new funciones().setMsj(1,"ERROR: No se pudo realizar consultar existencia de retaceos por Importaciones a la Bases de Datos");
            return resultado;
        }
    
    }
    
    
}

