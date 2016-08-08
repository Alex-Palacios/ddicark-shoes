/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ddicark.jpaFacades;

import com.ddicark.entidades.Envio;
import com.ddicark.entidades.Venta;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author DDICARK
 */
@Stateless
public class VentaFacade extends AbstractFacade<Venta> {
    @PersistenceContext(unitName = "ddicark-shoesPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public VentaFacade() {
        super(Venta.class);
    }
    
    
    //FUNCIONES
    
     /*
     * FUNCION QUE VERIFICA SI LA VENTA DE UNA ORDEN DE ENVIO YA EXISTE
     */
    public boolean existeVentaOrden(Envio orden){
        boolean existe;
        Query query = em.createNamedQuery("Venta.VentaByOrden");
        //Se le pasan los parametros a la consulta
        query.setParameter("ordenEnvio",orden);
        if (!query.getResultList().isEmpty()) {
            existe = true;
        }else{
            existe = false;
        }
        return existe;
    }
    
    /*
     * FUNCION QUE DEVUELVE UNA VENTA DE UNA ORDEN
     */
    public Venta getVentaOrden(Envio orden){
        Venta venta = new Venta();
        Query query = em.createNamedQuery("Venta.VentaByOrden");
        //Se le pasan los parametros a la consulta
        query.setParameter("ordenEnvio",orden);
        venta = (Venta) query.getSingleResult();
        return venta;
    }
    
    
    /*
     * DEVUELVE EL SIGUIENTE NUMERO DE VENTA
     */
    
    public int getNextIdVenta() {
        Query query = em.createNamedQuery("Venta.maxID");
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
    
}
