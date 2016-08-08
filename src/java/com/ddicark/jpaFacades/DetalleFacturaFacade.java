/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ddicark.jpaFacades;

import com.ddicark.auxiliares.funciones;
import com.ddicark.entidades.DetalleFactura;
import com.ddicark.entidades.Factura;
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
public class DetalleFacturaFacade extends AbstractFacade<DetalleFactura> {
    @PersistenceContext(unitName = "ddicark-shoesPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DetalleFacturaFacade() {
        super(DetalleFactura.class);
    }
    
    
    /*
     * FUNCION QUE EL SIGUIENTE ID DE DETALLE FACTURA
     */
    public int getNextIdDetalleFactura() {
        Query query = em.createNamedQuery("DetalleFactura.maxID");
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
     * FUNCION QUE DEVUELVE EL DETALLE DE UNA FACTURA
     */
     public List<DetalleFactura> detalleFactura(Factura factura){
        List<DetalleFactura> resultado =null;
        try{
            Query consulta = em.createNamedQuery("DetalleFactura.findByFactura"); 
            consulta.setParameter("factura",factura);
            resultado = consulta.getResultList();
            return resultado;   
        }catch(Exception e){
            new funciones().setMsj(4,"ERROR: No se pudo consultar PRODUCTOS DE LA FACTURA ANULADA");
            return resultado;
        }
        
    }
     
     
     
     
    
}
