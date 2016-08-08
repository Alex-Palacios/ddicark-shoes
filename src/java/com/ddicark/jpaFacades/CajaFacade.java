/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ddicark.jpaFacades;

import com.ddicark.auxiliares.funciones;
import com.ddicark.auxiliares.InventarioConsulta;
import com.ddicark.entidades.Caja;
import com.ddicark.entidades.Producto;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author DDICARK
 */
@Stateless
public class CajaFacade extends AbstractFacade<Caja> {
    @PersistenceContext(unitName = "ddicark-shoesPU")
    private EntityManager em;
    
    @EJB
    private InventarioFacade ejbFacadeInventario;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CajaFacade() {
        super(Caja.class);
    }
    
    
    /*
     * FUNCION QUE VERIFICA SI EL CODIGO DE LA CAJA EXISTE
     */
    public boolean existeCodigoCaja(String codigo){
        boolean existe;
        Query query = em.createNamedQuery("Caja.findByNumcaja");
        //Se le pasan los parametros a la consulta
        query.setParameter("numcaja",codigo);
        if (!query.getResultList().isEmpty()) {
            existe = true;
        }else{
            existe = false;
        }
        return existe;
    }
    
    
    /*
     * BUSCAR Y DEVOLVER ARTICULO
     */
    public Caja getCaja(String codigoBarra){
        List<Caja> r = null;
        try{
            Query query = em.createNamedQuery("Caja.findByNumcaja");
            //Se le pasan los parametros a la consulta
            query.setParameter("numcaja",codigoBarra);
            r = query.getResultList();
            if (r.size() == 1) {
                return r.get(0);
            }
        }catch(Exception e){
            new funciones().setMsj(4,"ERROR CON LA BASE DE DATOS AL BUSCAR CAJA");
        }
        return null;
    }
    
    
    /*
     * CONSULTA CAJA EN INVENTARIO FISICO
     */
    public List<InventarioConsulta> consultaCajasFisicas(){
        List<Object[]> consulta = null;
        List<InventarioConsulta> cajas = new ArrayList<InventarioConsulta>();
        String sql = "";
        try{
            sql =       "SELECT C.NUMCAJA, C.ESTILO, C.COLORES, C.DETALLE_CAJA, C.UNIDADES_CAJA, C.PRECIOVENTA_UNIDAD, C.UBICACION_CAJA, C.COMPLETA, " 
                    +   "( SELECT COUNT(*) FROM inventario i WHERE i.NUMCAJA = C.NUMCAJA AND i.ESTADOPRODUCTO ='EN EXISTENCIA') AS UNIDADES_DISPONIBLES " 
                    +   " FROM  caja C "
                    +   " WHERE C.NUMCAJA IN (SELECT DISTINCT(NUMCAJA) FROM inventario WHERE ESTADOPRODUCTO = 'EN EXISTENCIA' )";
                       
            Query query = em.createNativeQuery(sql);
            consulta = query.getResultList();
            if(consulta != null){
                for(Object[] actual : consulta){
                    InventarioConsulta nuevo = new InventarioConsulta(actual);
                    cajas.add(nuevo);
                }
            }
            return cajas;   
        }catch(Exception e){
            new funciones().setMsj(4,"ERROR: No se pudo realizar la CONSULTA  de INVENTARIO FISICO _ CAJAS a la BD");
            return cajas;
        }
    }
    
     /*
     * FUNCION que verifica si la caja esta completa o no y actualiza el estado de la caja
     */
    
    public void actualizarCajaCompleta(Caja caja){
        if(caja!=null){
            int diferencia = caja.getUnidadesCaja() - ejbFacadeInventario.countExistenciasCaja(caja, caja.getProducto().getProductoPK().getTipoProducto());
            if(diferencia <= 0){
                if(!caja.getCompleta()){
                    caja.setCompleta(true);
                    super.edit(caja);
                }
            }else{
                if(caja.getCompleta()){
                    caja.setCompleta(false);
                    super.edit(caja);
                }
            }
        }
       
    }
    
    
    /*
     * LISTA DE COLORES
     */
    public List<String> coloresByEstilo(Producto estilo){
        List<String> r = null;
        try{
            Query query = em.createNamedQuery("Caja.coloresByEstilo");
            //Se le pasan los parametros a la consulta
            query.setParameter("estilo",estilo);
            r = query.getResultList();
        }catch(Exception e){
            new funciones().setMsj(4,"ERROR AL OBTENER COLORES POR ESTILO");
        }
        return r;
    }
    
    
    /*
     * BUSCAR Y DEVOLVER ARTICULO
     */
    public Caja cajaEstiloColor(Producto estilo, String colores){
        List<Caja> r = null;
        try{
            Query query = em.createNamedQuery("Caja.byEstiloColor");
            //Se le pasan los parametros a la consulta
            query.setParameter("estilo",estilo);
            query.setParameter("colores",colores);
            r = query.getResultList();
            if (r.size() >= 1) {
                return r.get(0);
            }
        }catch(Exception e){
            new funciones().setMsj(4,"ERROR CON LA BASE DE DATOS AL BUSCAR CAJA");
        }
        return null;
    }
    
    
    /*
     * CONSULTA CAJA EN INVENTARIO FISICO
     */
    public List<Caja> inventarioFisico(){
        List<Caja> consulta = null;
        try{
            Query query = em.createNamedQuery("Caja.inventarioFisico");
            //Se le pasan los parametros a la consulta
            consulta = query.getResultList();
            return consulta;   
        }catch(Exception e){
            new funciones().setMsj(4,"ERROR: No se pudo realizar la CONSULTA  de INVENTARIO FISICO _ CAJAS a la BD");
            return consulta;
        }
    }
    
    
    
    /*
     * FUNCION QUE ACTUALIZA EL ESTADO DE LAS CAJAS A COMPLETAS
     * SI ESTAN INCOMPLETAS Y NO ES ASI
     */ 
    public void updateEstadoCajaCompleta(){
        try{
            String sql = "UPDATE caja c SET COMPLETA = 1 " 
                    +   "WHERE  c.COMPLETA = 0 "
                    +   "AND c.UNIDADES_CAJA <= (SELECT COUNT(*) FROM inventario i WHERE i.NUMCAJA=c.NUMCAJA AND ESTADOPRODUCTO = 'EN EXISTENCIA') ";
            
            Query query = em.createNativeQuery(sql);
            query.executeUpdate();
        }catch(Exception e){
            new funciones().setMsj(4,"ERROR: No se pudo ACTUALIZAR ESTADO -- DE CAJAS");
            
        }
    }
    
    
    
    
    
    
    
    public List<InventarioConsulta> consultaCajasFisicasAnteriores(){
        List<Object[]> consulta = null;
        List<InventarioConsulta> cajas = new ArrayList<InventarioConsulta>();
        String sql = "";
        try{
            sql =       "SELECT C.NUMCAJA, C.ESTILO, C.COLORES, C.DETALLE_CAJA, C.UNIDADES_CAJA, C.PRECIOVENTA_UNIDAD, C.UBICACION_CAJA, C.COMPLETA, " 
                    +   "( SELECT COUNT(*) FROM inventario i WHERE i.NUMCAJA = C.NUMCAJA AND i.ESTADOPRODUCTO LIKE 'X%') AS UNIDADES_DISPONIBLES " 
                    +   " FROM  caja C "
                    +   " WHERE C.NUMCAJA IN (SELECT DISTINCT(NUMCAJA) FROM inventario WHERE ESTADOPRODUCTO LIKE 'X%')";
                       
            Query query = em.createNativeQuery(sql);
            consulta = query.getResultList();
            if(consulta != null){
                for(Object[] actual : consulta){
                    InventarioConsulta nuevo = new InventarioConsulta(actual);
                    cajas.add(nuevo);
                }
            }
            return cajas;   
        }catch(Exception e){
            new funciones().setMsj(4,"ERROR: No se pudo realizar la CONSULTA  de INVENTARIO FISICO ANTERIOR A LAS CAJAS");
            return cajas;
        }
    }
    
    
}
