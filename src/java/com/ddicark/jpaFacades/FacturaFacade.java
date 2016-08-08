/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ddicark.jpaFacades;

import com.ddicark.auxiliares.funciones;
import com.ddicark.entidades.Cliente;
import com.ddicark.entidades.Empleado;
import com.ddicark.entidades.Envio;
import com.ddicark.entidades.Factura;
import com.ddicark.entidades.FacturaPK;
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
public class FacturaFacade extends AbstractFacade<Factura> {
    @PersistenceContext(unitName = "ddicark-shoesPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public FacturaFacade() {
        super(Factura.class);
    }

    /*
     * FUNCION QUE VERIFICA SI LA FACTURA HA INGRESAR YA EXISTE EN EL SISTEMA
     */
    public boolean existeFactura(FacturaPK clave){
        boolean existe;
        FacturaPK consulta = new FacturaPK();
        consulta.setNumfactura(clave.getNumfactura());
        consulta.setFechaFactura(clave.getFechaFactura());
        Query query = em.createNamedQuery("Factura.existeFactura");
        //Se le pasan los parametros a la consulta
        query.setParameter("facturaPK",consulta);
        if (!query.getResultList().isEmpty()) {
            existe = true;
        }else{
            existe = false;
        }
        return existe;
    }
    
    /*
     * FUNCION QUE DEVUELVE EL LISTADO DE LAS FACTURAS AL CONTADO
     * ACTIVAS (NO CANCELADAS)
     */
     
    public List<Factura> facturasContadoActiva(){
        List<Factura> resultado =null;
        try{
            Query consulta = em.createNamedQuery("Factura.alContadoActivas"); 
            resultado = consulta.getResultList();
            return resultado;   
        }catch(Exception e){
            new funciones().setMsj(4,"ERROR: No se pudo consultar Facturas AL CONTADO ACTIVAS de la Bases de Datos");
            return resultado;
        }
        
    }
    
    
    
    public List<Factura> facturasContadoActivaVendedor(Empleado vendedor){
        List<Factura> resultado =null;
        try{
            Query consulta = em.createNamedQuery("Factura.alContadoActivasByVendedor"); 
            consulta.setParameter("vendedor", vendedor);
            resultado = consulta.getResultList();
            return resultado;   
        }catch(Exception e){
            new funciones().setMsj(4,"ERROR: No se pudo consultar Facturas AL CONTADO ACTIVAS de la Bases de Datos");
            return resultado;
        }
        
    }
    
    /*
     * FUNCION QUE DEVUELVE EL LISTADO DE LAS FACTURAS AL CREDITO
     * ACTIVAS
     */
     
    public List<Factura> facturasCreditoActiva(){
        List<Factura> resultado =null;
        try{
            Query consulta = em.createNamedQuery("Factura.alCreditoActivas"); 
            resultado = consulta.getResultList();
            return resultado;   
        }catch(Exception e){
            new funciones().setMsj(4,"ERROR: No se pudo consultar Facturas AL CREDITO ACTIVAS de la Bases de Datos");
            return resultado;
        }
        
    }
    
    
    public List<Factura> facturasCreditoActiva(Empleado vendedor){
        List<Factura> resultado =null;
        try{
            Query consulta = em.createNamedQuery("Factura.alCreditoActivasByVendedor"); 
            consulta.setParameter("vendedor", vendedor);
            resultado = consulta.getResultList();
            return resultado;   
        }catch(Exception e){
            new funciones().setMsj(4,"ERROR: No se pudo consultar Facturas AL CREDITO ACTIVAS de la Bases de Datos");
            return resultado;
        }
        
    }
    
    
      /*
     * FUNCION QUE DEVUELVE EL LISTADO DE LAS FACTURAS AL CREDITO
     * EN UN ESTADO ESPECIFICO Y DE UN CLIENTE ESPECIFICO
     */
     
    public List<Factura> facturasClienteCredito(Cliente cliente, String estadoCredito){
        List<Factura> resultado =null;
        try{
            Query consulta = em.createNamedQuery("Factura.ByClienteCreditosEstado"); //Especificar nombre de la consulta que se va a ejecutar
            //Se le pasan los parametros a la consulta
            consulta.setParameter("cliente",cliente);
            consulta.setParameter("estado",estadoCredito);
            resultado = consulta.getResultList();
            return resultado;   
        }catch(Exception e){
            new funciones().setMsj(4,"ERROR: No se pudo consultar Facturas AL CREDITO del Cliente a la Bases de Datos");
            return resultado;
        }
        
    }
    
    
    /*
     * FUNCION QUE DEVUELVE EL LISTADO DE LAS FACTURAS AL CONTADO
     * DE UN CLIENTE ESPECIFICO
     */
     
    public List<Factura> facturasClienteContado(Cliente cliente){
        List<Factura> resultado =null;
        try{
            Query consulta = em.createNamedQuery("Factura.ByClienteContado"); //Especificar nombre de la consulta que se va a ejecutar
            //Se le pasan los parametros a la consulta
            consulta.setParameter("cliente",cliente);
            resultado = consulta.getResultList();
            return resultado;   
        }catch(Exception e){
            new funciones().setMsj(4,"ERROR: No se pudo consultar Facturas AL CONTADO del Cliente a la Bases de Datos");
            return resultado;
        }
        
    }
    
    /*
     * FUNCION QUE DEVUELVE EL LISTADO DE LAS FACTURAS AL CONTADO
     * DE UN CLIENTE ESPECIFICO
     */
     
    public List<Factura> facturasActivasCliente(Cliente cliente){
        List<Factura> resultado =null;
        try{
            Query consulta = em.createNamedQuery("Factura.ActivasByCliente"); //Especificar nombre de la consulta que se va a ejecutar
            //Se le pasan los parametros a la consulta
            consulta.setParameter("cliente",cliente);
            resultado = consulta.getResultList();
            return resultado;   
        }catch(Exception e){
            new funciones().setMsj(4,"ERROR: No se pudo consultar Facturas ACTIVAS del Cliente a la Bases de Datos");
            return resultado;
        }
        
    }
    
    /*
     * FUNCION QUE DEVUELVE EL LISTADO DE LAS VENTAS REALIZADAS
     */
     
    public List<Factura> listaVenta(int month, int year){
        List<Factura> resultado =null;
        try{
            Query consulta = em.createNamedQuery("Factura.listaVentas");
            consulta.setParameter("month", month);
            consulta.setParameter("year", year);
            resultado = consulta.getResultList();
            return resultado;   
        }catch(Exception e){
            new funciones().setMsj(4,"ERROR: No se pudo consultar LAS VENTAS REALIZADAS en la Bases de Datos");
            return resultado;
        }
        
    }
    
    
    
    /*
     * CONSULTA FACTURAS EMITIDAS POR MES Y AÑO
     */
    public List<Factura> consultarFacturasEmitidas(int month, int year){
        List<Factura> resultado =null;
        try{
            Query consulta = em.createNamedQuery("Factura.byMonthYear"); 
            consulta.setParameter("month", month);
            consulta.setParameter("year", year);
            resultado = consulta.getResultList();
            return resultado;   
        }catch(Exception e){
            new funciones().setMsj(4,"ERROR: No se pudo consultar LAS VENTAS REALIZADAS en la Bases de Datos");
            return resultado;
        }
        
    }
    
    
    
    
    
     /*
     * FUNCION QUE DEVUELVE EL NUMERO DE CREDITOS ACTIVOS DE UN CLIENTE
     */
    public int getCreditosActivos(Cliente cliente) {
        Query query = em.createNamedQuery("Factura.numCreditosActivosByCliente");
        query.setParameter("cliente", cliente);
        Object consulta = query.getSingleResult();
        int resultado = 0;
        if (consulta != null) {
            resultado = Integer.parseInt(consulta.toString());
        }
        return resultado;
    }

    
    /*
     * FUNCION QUE DEVUELVE LA DEUDA TOTAL ACTUAL DE UN CLIENTE
     */
    public float getDeudaPorCreditos(Cliente cliente) {
        Query query = em.createNamedQuery("Factura.deudaActualByCliente");
        query.setParameter("cliente", cliente);
        Object consulta = query.getSingleResult();
        float resultado = (float) 0.00;
        if (consulta != null) {
            resultado = Float.parseFloat(consulta.toString());
        }
        return resultado;
    }
    
    
    public List<Factura> facturasCliente(Cliente cliente){
        List<Factura> resultado =null;
        try{
            Query consulta = em.createNamedQuery("Factura.ByCliente"); 
            consulta.setParameter("cliente",cliente);
            resultado = consulta.getResultList();
            return resultado;   
        }catch(Exception e){
            new funciones().setMsj(4,"ERROR: No se pudo consultar Facturas DEL CLIENTE");
            return resultado;
        }
        
    }
    
    
    
    
    
    public List<Factura> facturasPendientes(Cliente cliente){
        List<Factura> resultado =null;
        try{
            Query consulta = em.createNamedQuery("Factura.ByClienteCreditosEstado"); 
            consulta.setParameter("cliente",cliente);
            consulta.setParameter("estado","ACTIVA");
            resultado = consulta.getResultList();
            return resultado;   
        }catch(Exception e){
            new funciones().setMsj(4,"ERROR: No se pudo consultar Facturas PENDIENTES DEL CLIENTE");
            return resultado;
        }
        
    }
    
    
    public List<Factura> facturasByEnvio(Envio envio){
        List<Factura> resultado =null;
        try{
            Query consulta = em.createNamedQuery("Factura.findByEnvio"); 
            consulta.setParameter("envio",envio);
            resultado = consulta.getResultList();
            return resultado;   
        }catch(Exception e){
            new funciones().setMsj(4,"ERROR: No se pudo consultar Facturas del Envio #" + envio.getNumenvio());
            return resultado;
        }
        
    }
    
    
    public List<Factura> facturasByVendedor(Empleado vendedor){
        List<Factura> resultado =null;
        try{
            Query consulta = em.createNamedQuery("Factura.findByVendedor"); 
            consulta.setParameter("vendedor",vendedor);
            resultado = consulta.getResultList();
            return resultado;   
        }catch(Exception e){
            new funciones().setMsj(4,"ERROR: No se pudo consultar FACTURAS POR VENDEDOR");
            return resultado;
        }
        
    }
    
    public List<Factura> facturasActivas(){
        List<Factura> resultado =null;
        try{
            Query consulta = em.createNamedQuery("Factura.findActivas"); 
            resultado = consulta.getResultList();
            return resultado;   
        }catch(Exception e){
            new funciones().setMsj(4,"ERROR: No se pudo consultar FACTURAS ACTIVAS");
            return resultado;
        }
        
    }
    
    
}
