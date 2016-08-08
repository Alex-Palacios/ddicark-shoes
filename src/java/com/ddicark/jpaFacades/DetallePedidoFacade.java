/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ddicark.jpaFacades;

import com.ddicark.auxiliares.funciones;
import com.ddicark.entidades.DetallePedido;
import com.ddicark.entidades.PedidoPK;
import com.ddicark.entidades.Producto;
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
public class DetallePedidoFacade extends AbstractFacade<DetallePedido> {
    @PersistenceContext(unitName = "ddicark-shoesPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DetallePedidoFacade() {
        super(DetallePedido.class);
    }
    
    /*
     * FUNCION QUE EL SIGUIENTE CLAVE DE DETALLE PEDIDO
     */
    public int getNextIdDetallePedido() {
        Query query = em.createNamedQuery("DetallePedido.maxID");
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
    
    
    
     /*Lista de detalle pertenecientes a un pedido
     * especifico
     */
    public List<DetallePedido> getDetallePedido(PedidoPK pedidoID){
        List<DetallePedido> resultado =null;
        try{
            Query consulta = em.createNamedQuery("DetallePedido.detalleByPedido");
            //Se le pasan los parametros a la consulta
            consulta.setParameter("pedidoPK",pedidoID);
            resultado = consulta.getResultList();
            return resultado;   
        }catch(Exception e){
            new funciones().setMsj(1,"ERROR: No se pudo realizar la consulta de detalle del pedido N°:" + pedidoID.getNumpedido() + " de la BD");
            return resultado;
        }
    
    }
    
     /*
     * FUNCION QUE VERIFICA SI EL PRODUCTO INDICADO EXISTE EN UN PEDIDO
     */
    public boolean productoFuePedido(PedidoPK pedidoID, Producto estilo, String color, String talla ){
        boolean existe;
        Query query = em.createNamedQuery("DetallePedido.existeProductoPedido");
        //Se le pasan los parametros a la consulta
        query.setParameter("pedidoPK",pedidoID);
        query.setParameter("producto",estilo);
        query.setParameter("color",color);
        query.setParameter("talla",talla);
        if (!query.getResultList().isEmpty()) {
            existe = true;
        }else{
            existe = false;
        }
        return existe;
    }
    
}
