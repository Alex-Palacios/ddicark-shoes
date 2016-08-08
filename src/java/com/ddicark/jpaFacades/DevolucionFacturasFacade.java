/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ddicark.jpaFacades;

import com.ddicark.auxiliares.funciones;
import com.ddicark.entidades.Devolucion;
import com.ddicark.entidades.DevolucionFacturas;
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
public class DevolucionFacturasFacade extends AbstractFacade<DevolucionFacturas> {
    @PersistenceContext(unitName = "ddicark-shoesPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DevolucionFacturasFacade() {
        super(DevolucionFacturas.class);
    }
    
    
    
    /*
     * FUNCIONES
     */
    
    /*
     * FUNCION QUE EL SIGUIENTE ID DE LA LISTA
     */
    public int getNextIdDevolucionFacturas() {
        Query query = em.createNamedQuery("DevolucionFacturas.maxID");
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
     * LISTA DE FACTURAS EN LA QUE HA SIDO APLICADA UNA DEVOLUCION
     */
    public List<DevolucionFacturas> devolucionAplicada(Devolucion devolucion){
        List<DevolucionFacturas> resultado =null;
        try{
            Query consulta = em.createNamedQuery("DevolucionFacturas.findByDevolucion");
            consulta.setParameter("devolucion", devolucion);
            resultado = consulta.getResultList();
            return resultado;   
        }catch(Exception e){
            new funciones().setMsj(4,"ERROR: No se pudo realizar la consulta de FACTURAS APLICADAS DE DEVOLUCION en la BD");
            return resultado;
        }
    
    }
}
