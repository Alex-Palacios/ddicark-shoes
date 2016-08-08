/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ddicark.jpaFacades;

import com.ddicark.auxiliares.funciones;
import com.ddicark.entidades.FacturaIngreso;
import com.ddicark.entidades.Preingreso;
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
public class PreingresoFacade extends AbstractFacade<Preingreso> {
    @PersistenceContext(unitName = "ddicark-shoesPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PreingresoFacade() {
        super(Preingreso.class);
    }
    
    /**
     * *****************Inicio de modulo******************* 
     *      OBTIENE EL SIGUIENTE ID DEL PREINGRESO
     */
    public int getNextIdPreIngreso() {
        Query query = em.createNamedQuery("Preingreso.maxIDPreingreso");
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
      * Lista de los preIngreso pertenecientes a una factura
     * Lista para codificar
     */
    public List<Preingreso> getItemsFactura(FacturaIngreso factura){
        List<Preingreso> resultado =null;
        try{
            Query consulta = em.createNamedQuery("Preingreso.facturaList");
            //Se le pasan los parametros a la consulta
            consulta.setParameter("facturaIngreso",factura);
            resultado = consulta.getResultList();
            return resultado;   
        }catch(Exception e){
            new funciones().setMsj(1,"ERROR: No se pudo realizar la consulta de preingreso de la factura N°:" + factura.getFacturaIngresoPK().getDocumentoCompra() + " de la BD");
            return resultado;
        }
    
    }
    
    /*Lista de los preIngreso pertenecientes a una factura
     * preingresada
     */
    public List<Preingreso> getPreingresoFactura(FacturaIngreso factura){
        List<Preingreso> resultado =null;
        try{
            Query consulta = em.createNamedQuery("Preingreso.preingresoList");
            //Se le pasan los parametros a la consulta
            consulta.setParameter("facturaIngreso",factura);
            resultado = consulta.getResultList();
            return resultado;   
        }catch(Exception e){
            new funciones().setMsj(1,"ERROR: No se pudo realizar la consulta de preingreso de la factura N°:" + factura.getFacturaIngresoPK().getDocumentoCompra() + " de la BD");
            return resultado;
        }
    
    }
    
    
    /*
     * Lista de los preIngreso pertenecientes a una factura
     * preingresada y a una caja especifica
     */
    public List<Preingreso> getPreingresoCaja(FacturaIngreso factura , int numcaja){
        List<Preingreso> resultado =null;
        try{
            Query consulta = em.createNamedQuery("Preingreso.cajaList");
            //Se le pasan los parametros a la consulta
            consulta.setParameter("facturaIngreso",factura);
            consulta.setParameter("numcaja",numcaja);
            resultado = consulta.getResultList();
            return resultado;   
        }catch(Exception e){
            new funciones().setMsj(1,"ERROR: No se pudo realizar la consulta de preingreso por caja de la BD");
            return resultado;
        }
    
    }
    
    
    /*dEVUELVE EL MAXIMO NUMERO DE cajas completas ingresadas en una factura
     */
    public int getMAXNumCajasFactura(FacturaIngreso factura){
        Query query = em.createNamedQuery("Preingreso.numCajaFactura");
        //Se le pasan los parametros a la consulta
        query.setParameter("facturaIngreso",factura);
        Object consulta = query.getSingleResult();
        /* Objetivo: Obtiene un unico resutado de la consulta realizada
         */
        int resultado = 0;
        /* Objetivo: almacena el siguiente id valido
         */
        if (consulta != null) {
            resultado = Integer.parseInt(consulta.toString());
        }
        return resultado;
    
    }
    
     /*Cuenta las cajas completas ingresadas en una factura
     */
    public int getNumCajasFactura(FacturaIngreso factura){
        try{
            
            String sql =        "SELECT COUNT(DISTINCT(NUMCAJA_PREINGRESO)) " 
                            +   "FROM preingreso " 
                            +   "WHERE DOCUMENTO_COMPRA = '"+ factura.getFacturaIngresoPK().getDocumentoCompra()+"'"
                            +   "   AND FECHA_DOCUMENTO = '"+ new funciones().setFechaFormateada(factura.getFacturaIngresoPK().getFechaDocumento(),2)+"'"
                            +   "   AND NUMCAJA_PREINGRESO > 0 AND NUMCAJA_PREINGRESO IS NOT NULL ";
        
            Query query = em.createNativeQuery(sql);
            
            Object consulta = query.getSingleResult();
            int resultado = 0;
            if (consulta != null) {
                resultado = Integer.parseInt(consulta.toString());
            }
            return resultado;
        }catch(Exception e){
            return 0;
        }
    }
    
    /*
     * Listado de las tallas ordenadas ascendentemente de una curva de una caja
     * de una factura especifica
     */
    public List<String> curvaTallaList(FacturaIngreso factura,int caja){
        List<String> resultado = null;
        try{
            Query consulta = em.createNamedQuery("Preingreso.consultaCurvaTallas");
            //Se le pasan los parametros a la consulta
            consulta.setParameter("facturaIngreso",factura);
            consulta.setParameter("numCaja", caja);
            resultado = consulta.getResultList();
            return resultado;   
        }catch(Exception e){
            new funciones().setMsj(1,"ERROR: No se pudo realizar la consulta de tallas de la factura: " + factura.getFacturaIngresoPK().getDocumentoCompra() + "y caja #"+caja+" de la BD");
            return resultado;
        }
    }
    
     /*
     * Listado de colores curva de una caja
     * de una factura especifica
     */
    public List<String> cajaColoresList(FacturaIngreso factura,int caja){
        List<String> resultado = null;
        try{
            Query consulta = em.createNamedQuery("Preingreso.consultaColoresCaja");
            //Se le pasan los parametros a la consulta
            consulta.setParameter("facturaIngreso",factura);
            consulta.setParameter("numCaja", caja);
            resultado = consulta.getResultList();
            return resultado;   
        }catch(Exception e){
            new funciones().setMsj(4,"ERROR: No se pudo realizar la consulta de colores de la factura: " + factura.getFacturaIngresoPK().getDocumentoCompra() + "y caja #"+caja+" de la BD");
            return resultado;
        }
    }
    
    
    /*
     * Curva de una caja de una factura determinada del preingreso
     */
    public List curvaList(FacturaIngreso factura,int caja){
        List resultado = null;
        try{
            Query consulta = em.createNamedQuery("Preingreso.consultaCurva");
            //Se le pasan los parametros a la consulta
            consulta.setParameter("facturaIngreso",factura);
            consulta.setParameter("numCaja", caja);
            resultado = consulta.getResultList();
            return resultado;   
        }catch(Exception e){
            new funciones().setMsj(1,"ERROR: No se pudo realizar la consulta de la CURVA de la factura: " + factura.getFacturaIngresoPK().getDocumentoCompra() + " y caja #"+caja+" de la BD");
            return resultado;
        }
    }
    
    /*
     * FUNCION QUE VERIFICA SI EL CODIGO DE LA CAJA EXISTE
     */
    public boolean existeCodigoCaja(String codigo){
        boolean existe;
        Query query = em.createNamedQuery("Preingreso.existeCaja");
        //Se le pasan los parametros a la consulta
        query.setParameter("codigoCaja",codigo);
        if (!query.getResultList().isEmpty()) {
            existe = true;
        }else{
            existe = false;
        }
        return existe;
    }
    
    /*
     * FUNCION QUE VERIFICA SI EL CODIGO DE LA UNIDAD EXISTE
     */
    public boolean existeCodigoUnidad(String codigo){
        boolean existe;
        Query query = em.createNamedQuery("Preingreso.existeUnidad");
        //Se le pasan los parametros a la consulta
        query.setParameter("codigoBarra",codigo);
        if (!query.getResultList().isEmpty()) {
            existe = true;
        }else{
            existe = false;
        }
        return existe;
    }
    
    /*
     * Retorna un preingreso con codigo de barra especificado
     */
    public Preingreso getPreingresoCodigo(String codigoBarra, String codigoCaja){
        Preingreso r = null;
        Query query = em.createNamedQuery("Preingreso.findByCodigos");
        //Se le pasan los parametros a la consulta
        query.setParameter("codigoBarra",codigoBarra);
        query.setParameter("codigoCaja",codigoCaja);
        Object consulta = query.getSingleResult();
        if (consulta != null) {
            r = (Preingreso) consulta;
        }
        
        return r;
    }
    
    /*
     * Retorna un preingreso con codigo de barra especificado
     */
    public Preingreso getPreingresoCodigo(String codigoBarra){
        Preingreso r = null;
        try{
            Query query = em.createNamedQuery("Preingreso.findByCodigoBarra");
            //Se le pasan los parametros a la consulta
            query.setParameter("codigoBarra",codigoBarra);
            Object consulta = query.getSingleResult();
            if (consulta != null) {
                r = (Preingreso) consulta;
            }
        }catch(Exception e){
            new funciones().setMsj(4,"ERROR CON LA BASE DE DATOS");
        }
        return r;
    }
    
    /*
     * FUNCION QUE CUENTA EL TOTAL DE PRODUCTOS EN PREINGRESOS DE UNA FACTURA
     */
        public int countFactura(FacturaIngreso factura) {
            Query query = em.createNamedQuery("Preingreso.contarPreFactura");
            query.setParameter("facturaIngreso",factura);
            Object consulta = query.getSingleResult();
            int resultado = 0;
            if (consulta != null) {
                resultado = Integer.parseInt(consulta.toString());
            }
            return resultado;
        }
        
        /*
     * FUNCION QUE CUENTA EL TOTAL DE PRODUCTOS INVENTARIADOS EN PREINGRESOS DE UNA FACTURA
     */
        public int countInvFactura(FacturaIngreso factura) {
            Query query = em.createNamedQuery("Preingreso.contarInvFactura");
            query.setParameter("facturaIngreso",factura);
            Object consulta = query.getSingleResult();
            int resultado = 0;
            if (consulta != null) {
                resultado = Integer.parseInt(consulta.toString());
            }
            return resultado;
        }
        
           /*
     * FUNCION QUE CUENTA EL TOTAL DE PRODUCTOS DE UNA CAJA EN PREINGRESO
     */
        public int countPreingresoCajaFactura(FacturaIngreso factura , int caja) {
            Query query = em.createNamedQuery("Preingreso.contarUnidadesCaja");
            query.setParameter("facturaIngreso",factura);
            query.setParameter("numcaja",caja);
            Object consulta = query.getSingleResult();
            int resultado = 0;
            if (consulta != null) {
                resultado = Integer.parseInt(consulta.toString());
            }
            return resultado;
        }
    
     /*
     * FUNCION QUE CUENTA EL TOTAL DE UNIDADES DE UNA CAJA PREINGRESADA LISTA PARA CODIFICARSE
     */
        public int countUnidadesCajaPreingresadasFactura(FacturaIngreso factura , int caja) {
            Query query = em.createNamedQuery("Preingreso.contarUnidadesCajaPreingresadas");
            query.setParameter("facturaIngreso",factura);
            query.setParameter("numcaja",caja);
            Object consulta = query.getSingleResult();
            int resultado = 0;
            if (consulta != null) {
                resultado = Integer.parseInt(consulta.toString());
            }
            return resultado;
        }
        
        
         /*
     * FUNCION QUE ACTUALIZA EL ESTADO DE PRODUCTO PREINGRESADO
     */ 
    public void updateEstadoPreingreso(FacturaIngreso factura, String estado){
        try{
            String sql =        "UPDATE preingreso "
                            +   "SET ESTADO_PREINGRESO = '"+estado+"' "
                            +   "WHERE DOCUMENTO_COMPRA = '"+factura.getFacturaIngresoPK().getDocumentoCompra()+"' AND FECHA_DOCUMENTO = '"+ new funciones().setFechaFormateada(factura.getFacturaIngresoPK().getFechaDocumento(),2)+"'  ";
            
            Query query = em.createNativeQuery(sql);
            query.executeUpdate();
        }catch(Exception e){
            new funciones().setMsj(4,"ERROR: No se pudo ACTUALIZAR ESTADO -- DE PRODUCTOS PREINGRESADOS");
            
        }
    }
    }

