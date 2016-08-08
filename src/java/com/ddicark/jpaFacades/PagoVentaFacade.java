    /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ddicark.jpaFacades;

import com.ddicark.auxiliares.funciones;
import com.ddicark.entidades.Empleado;
import com.ddicark.entidades.Factura;
import com.ddicark.entidades.PagoVenta;
import com.ddicark.entidades.Remesa;
import java.util.Date;
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
public class PagoVentaFacade extends AbstractFacade<PagoVenta> {
    @PersistenceContext(unitName = "ddicark-shoesPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PagoVentaFacade() {
        super(PagoVenta.class);
    }
    
    
    
    
    /*
     * FUNCION QUE EL SIGUIENTE CLAVE DE PAGOVENTA
     */
    public int getNextIdPagoVenta() {
        Query query = em.createNamedQuery("PagoVenta.maxID");
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
     * FUNCION QUE DEVUELVE LOS PAGOS REGISTRADOS A UNA FACTURA
     */
     
    public List<PagoVenta> pagosFactura(Factura factura){
        List<PagoVenta> resultado =null;
        try{
            Query consulta = em.createNamedQuery("PagoVenta.pagosFactura"); 
            consulta.setParameter("factura", factura);
            resultado = consulta.getResultList();
            return resultado;   
        }catch(Exception e){
            new funciones().setMsj(4,"ERROR: No se pudo consultar LOS PAGOS DE LA FACTURA: "+ factura.getFacturaPK().getNumfactura()+" de la Bases de Datos");
            return resultado;
        }
        
    }
    
    /*
     * CONSULTA FACTURAS EMITIDAS POR MES Y AÑO
     */
    public List<PagoVenta> consultarPagos(int month, int year){
        List<PagoVenta> resultado =null;
        try{
            Query consulta = em.createNamedQuery("PagoVenta.byMonthYear"); 
            consulta.setParameter("month", month);
            consulta.setParameter("year", year);
            resultado = consulta.getResultList();
            return resultado;   
        }catch(Exception e){
            new funciones().setMsj(4,"ERROR: No se pudo consultar LOS PAGOS REGISTRADOS en la Bases de Datos");
            return resultado;
        }
        
    }
    
    /*
     * CONSULTA FACTURAS EMITIDAS POR Rango de fechas
     */
    public List<PagoVenta> consultarPagosByRango(Date fechaInicio, Date FechaFin){
        List<PagoVenta> resultado =null;
        try{
            Query consulta = em.createNamedQuery("PagoVenta.byRangoFecha"); 
            consulta.setParameter("fechaInicio", fechaInicio);
            consulta.setParameter("fechaFin", FechaFin);
            resultado = consulta.getResultList();
            return resultado;   
        }catch(Exception e){
            new funciones().setMsj(4,"ERROR: No se pudo consultar LOS PAGOS REGISTRADOS en la Bases de Datos");
            return resultado;
        }
        
    }
    
    
    
    /*
     * CONSULTA FACTURAS EMITIDAS POR Rango de fechas
     */
    public List<PagoVenta> consultarPagosByRango(Date fechaInicio, Date FechaFin, Empleado vendedor){
        List<PagoVenta> resultado =null;
        try{
            Query consulta = em.createNamedQuery("PagoVenta.byRangoFechaByVendedor"); 
            consulta.setParameter("vendedor", vendedor);
            consulta.setParameter("fechaInicio", fechaInicio);
            consulta.setParameter("fechaFin", FechaFin);
            resultado = consulta.getResultList();
            return resultado;   
        }catch(Exception e){
            new funciones().setMsj(4,"ERROR: No se pudo consultar LOS PAGOS REGISTRADOS en la Bases de Datos");
            return resultado;
        }
        
    }
    /*
     * CONSULTA FACTURAS EMITIDAS POR MES Y AÑO
     */
    public List<PagoVenta> consultarPagos(Date fecha){
        List<PagoVenta> resultado =null;
        try{
            Query consulta = em.createNamedQuery("PagoVenta.fecha");
            consulta.setParameter("fecha", fecha);
            resultado = consulta.getResultList();
            return resultado;   
        }catch(Exception e){
            new funciones().setMsj(4,"ERROR: No se pudo consultar LOS PAGOS REGISTRADOS en la Bases de Datos");
            return resultado;
        }
        
    }
    
    
     /*
     * CONSULTA FACTURAS EMITIDAS POR MES Y AÑO
     */
    public List<PagoVenta> consultarPagosVendedor(Empleado vendedor){
        List<PagoVenta> resultado =null;
        try{
            Query consulta = em.createNamedQuery("PagoVenta.registradosByVendedor");
            consulta.setParameter("vendedor", vendedor);
            resultado = consulta.getResultList();
            return resultado;   
        }catch(Exception e){
            new funciones().setMsj(4,"ERROR: No se pudo consultar LOS PAGOS REGISTRADOS POR VENDEDOR en la Bases de Datos");
            return null;
        }
        
    }
    
    
    /*
     * CONSULTA PAGOS ASOCIADOS A UNA REMESA
     */
    public List<PagoVenta> consultarPagos(Remesa remesa){
        List<PagoVenta> resultado =null;
        try{
            Query consulta = em.createNamedQuery("PagoVenta.byRemesa");
            consulta.setParameter("remesa", remesa);
            resultado = consulta.getResultList();
            return resultado;   
        }catch(Exception e){
            new funciones().setMsj(4,"ERROR: No se pudo consultar LOS PAGOS ASOCIADOS A REMESA en la Bases de Datos");
            return resultado;
        }
        
    }
    
    
    /*
     * CONSULTA PAGOS QUE NO SE HAN REMESADO UNA REMESA
     */
    public List<PagoVenta> consultarPagosSinRemesar(){
        List<PagoVenta> resultado =null;
        try{
            Query consulta = em.createNamedQuery("PagoVenta.sinRemesar");
            resultado = consulta.getResultList();
            return resultado;   
        }catch(Exception e){
            new funciones().setMsj(4,"ERROR: No se pudo consultar LOS PAGOS SIN REMESAR en la Bases de Datos");
            return resultado;
        }
        
    }
    
    
    
     /*
     * CONSULTA PAGOS QUE NO SE HAN REMESADO UNA REMESA
     */
    public List<PagoVenta> consultarPagosRemesaSinVerificar(){
        List<PagoVenta> resultado =null;
        try{
            Query consulta = em.createNamedQuery("PagoVenta.remesaSinVerificar");
            resultado = consulta.getResultList();
            return resultado;   
        }catch(Exception e){
            new funciones().setMsj(4,"ERROR: No se pudo consultar LOS PAGOS SIN verificacion en la Bases de Datos");
            return resultado;
        }
        
    }
    
    
     /*
     * CONSULTA CHEQUES Y REMESAS REGISTRADAS
     */
    public List<PagoVenta> consultarChequesRemesasRegistradas(){
        List<PagoVenta> resultado =null;
        try{
            Query consulta = em.createNamedQuery("PagoVenta.chequesRemesasRegistradas");
            resultado = consulta.getResultList();
            return resultado;   
        }catch(Exception e){
            new funciones().setMsj(4,"ERROR: No se pudo consultar Los Cheque y Remesas Registradas");
            return resultado;
        }
        
    }
}
