/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ddicark.jpaFacades;

import com.ddicark.auxiliares.funciones;
import com.ddicark.entidades.Envio;
import com.ddicark.entidades.Pedido;
import com.ddicark.entidades.PedidoPK;
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
public class PedidoFacade extends AbstractFacade<Pedido> {
    @PersistenceContext(unitName = "ddicark-shoesPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PedidoFacade() {
        super(Pedido.class);
    }
    
    /*
     * FUNCION QUE VERIFICA SI EL PEDIDO HA INGRESAR YA EXISTE EN EL SISTEMA
     */
    public boolean existePedido(PedidoPK clave){
        boolean existe;
        Query query = em.createNamedQuery("Pedido.findPedido");
        //Se le pasan los parametros a la consulta
        query.setParameter("pedidoPK",clave);
        if (!query.getResultList().isEmpty()) {
            existe = true;
        }else{
            existe = false;
        }
        return existe;
    }
    
    /*Lista de pedidos por tipo(al contado o al credito)
     */
    public List<Pedido> getPedidosByTipo(String tipo){
        List<Pedido> resultado =null;
        try{
            Query consulta = em.createNamedQuery("Pedido.findByTipoPago");
            //Se le pasan los parametros a la consulta
            consulta.setParameter("tipoPago",tipo);
            resultado = consulta.getResultList();
            return resultado;   
        }catch(Exception e){
            new funciones().setMsj(1,"ERROR: No se pudo realizar la consulta de pedidos " + tipo + " de la BD");
            return resultado;
        }
    
    }
    
    /*Lista de pedidos Por Estado(Aprobado, solicitado)
     */
    public List<Pedido> getPedidosByEstado(String estado){
        List<Pedido> resultado =null;
        try{
            Query consulta = em.createNamedQuery("Pedido.findByEstadoPedido");
            //Se le pasan los parametros a la consulta
            consulta.setParameter("estadoPedido",estado);
            resultado = consulta.getResultList();
            return resultado;   
        }catch(Exception e){
            new funciones().setMsj(1,"ERROR: No se pudo realizar la consulta de pedidos " + estado + " de la BD");
            return resultado;
        }
    
    }
    
    /*Lista de pedidos Por Estado(Aprobado, solicitado)
     */
    public List<Pedido> PedidosEnCola(){
        List<Pedido> resultado =null;
        try{
            Query consulta = em.createNamedQuery("Pedido.enCola");
            //Se le pasan los parametros a la consulta
            resultado = consulta.getResultList();
            return resultado;   
        }catch(Exception e){
            new funciones().setMsj(1,"ERROR: No se pudo realizar la consulta de pedidos en COLA");
            return resultado;
        }
    
    }
    
    /*
     * Lista de SOLICITUDES DE CREDITO
     */
    public List<Pedido> getSolicitudesCredito(){
        List<Pedido> resultado =null;
        try{
            Query consulta = em.createNamedQuery("Pedido.solicitudCreditos");
            resultado = consulta.getResultList();
            return resultado;   
        }catch(Exception e){
            new funciones().setMsj(1,"ERROR: No se pudo realizar la consultar las Solicitudes de Credito de la BD");
            return resultado;
        }
    
    }
    
    
    
    /*
     * CONSULTA ENVIOS POR MES Y AÑO
     */
    public List<Pedido> consultarPedidos(int month, int year){
        List<Pedido> resultado =null;
        try{
            Query consulta = em.createNamedQuery("Pedido.byMonthYear"); 
            consulta.setParameter("month", month);
            consulta.setParameter("year", year);
            resultado = consulta.getResultList();
            return resultado;   
        }catch(Exception e){
            new funciones().setMsj(4,"ERROR: No se pudo consultar DE LOS PEDIDOS en la Bases de Datos");
            return resultado;
        }
        
    }
    
}
