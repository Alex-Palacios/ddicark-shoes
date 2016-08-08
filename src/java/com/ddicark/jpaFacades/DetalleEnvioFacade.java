/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ddicark.jpaFacades;

import com.ddicark.auxiliares.VentasMes;
import com.ddicark.auxiliares.funciones;
import com.ddicark.entidades.DetalleEnvio;
import com.ddicark.entidades.DetalleFactura;
import com.ddicark.entidades.Envio;
import com.ddicark.entidades.Factura;
import com.ddicark.entidades.Inventario;
import com.ddicark.entidades.Pedido;
import java.math.BigDecimal;
import java.util.ArrayList;
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
public class DetalleEnvioFacade extends AbstractFacade<DetalleEnvio> {
    @PersistenceContext(unitName = "ddicark-shoesPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DetalleEnvioFacade() {
        super(DetalleEnvio.class);
    }
    
    
    /*
     * FUNCION QUE DEVUELVE EL DETALLE DE UNA ORDEN DE ENVIO ESPECIFICA
     */
     public List<DetalleEnvio> detalleOrdenEnvio(Envio orden){
        List<DetalleEnvio> resultado =null;
        try{
            Query consulta = em.createNamedQuery("DetalleEnvio.findByNumenvio"); //Especificar nombre de la consulta que se va a ejecutar
            //Se le pasan los parametros a la consulta
            consulta.setParameter("numenvio",orden.getNumenvio());
            resultado = consulta.getResultList();
            return resultado;   
        }catch(Exception e){
            new funciones().setMsj(4,"ERROR: No se pudo consultar DETALLE DE LA ORDEN DE ENVIO N°" + orden.getNumenvio());
            return resultado;
        }
        
    }
     
     
     
    /*
     * FUNCION QUE DEVUELVE LAS LINEAS DE VENTA DE UNA ORDEN DE ENVIO
     * PARA LA FACTURA NUEVA
     */ 
    public List<Object> getLineasVentaFCF(Envio envio){
        List<Object> consulta = null;
        try{
            String sql =        "SELECT i.ESTILO ,d.PRECIO_FACTURAR,COUNT(*) " 
                            +   "FROM detalle_envio d ,inventario i " 
                            +   "WHERE NUMENVIO = "+envio.getNumenvio()+" AND d.CODIGO_PRODUCTO = i.CODIGO AND d.LINEA_VENTA is null " 
                            +   "GROUP BY i.ESTILO,d.PRECIO_FACTURAR " ;
                            //+   "LIMIT 14";
            
            Query query = em.createNativeQuery(sql);
            consulta = query.getResultList();
            return consulta;   
        }catch(Exception e){
            new funciones().setMsj(4,"ERROR: No se pudo realizar la consulta de Lineas de venta del envio #"+ envio.getNumenvio()+" a la BD");
            return consulta;
        }
    }
    
    public List<Object> getLineasVenta(Envio envio){
        List<Object> consulta = null;
        try{
            String sql =        "SELECT i.ESTILO ,d.PRECIO_FACTURAR,COUNT(*) " 
                            +   "FROM detalle_envio d ,inventario i " 
                            +   "WHERE NUMENVIO = "+envio.getNumenvio()+" AND d.CODIGO_PRODUCTO = i.CODIGO AND d.LINEA_VENTA is null " 
                            +   "GROUP BY i.ESTILO,d.PRECIO_FACTURAR ";
                            //+   "LIMIT 5";
            
            Query query = em.createNativeQuery(sql);
            consulta = query.getResultList();
            return consulta;   
        }catch(Exception e){
            new funciones().setMsj(4,"ERROR: No se pudo realizar la consulta de Lineas de venta del envio #"+ envio.getNumenvio()+" a la BD");
            return consulta;
        }
    }
    
    
    /*
     * FUNCION QUE DEVUELVE LAS LINEAS DE VENTA DE UNA FACTURA
     * PARA REFACTURAR
     */ 
    public List<Object> getLineasVenta(Factura factura){
        List<Object> consulta = null;
        try{
            String sql =        "SELECT i.ESTILO ,d.PRECIO_FACTURAR,COUNT(*) " 
                            +   "FROM detalle_envio d ,inventario i " 
                            +   "WHERE d.CODIGO_PRODUCTO = i.CODIGO "
                            +   "AND d.NOTA = 'REFACTURAR' "
                            +   "AND d.LINEA_VENTA IN (SELECT ID FROM detalle_factura WHERE NUMFACTURA = '"+factura.getFacturaPK().getNumfactura()+"' AND FECHA_FACTURA = '"+new funciones().setFechaFormateada(factura.getFacturaPK().getFechaFactura(),2)+"' ) "
                            +   "GROUP BY i.ESTILO,d.PRECIO_FACTURAR " 
                            +   "LIMIT 5";
            
            Query query = em.createNativeQuery(sql);
            consulta = query.getResultList();
            return consulta;   
        }catch(Exception e){
            new funciones().setMsj(4,"ERROR: No se pudo obtener el detalle de la factura para REFACTURAR de la BD");
            return consulta;
        }
    }
    
    /*
     * FUNCION QUE DEVUELVE EL DETALLE DE UNA ORDEN DE ENVIO DE UNA LINEA DE VENTA (DETALLE_FACTURA)
     */
     public List<DetalleEnvio> detalleLineaVenta(DetalleFactura DF){
        List<DetalleEnvio> resultado =null;
        try{
            Query consulta = em.createNamedQuery("DetalleEnvio.findByLineaVenta"); //Especificar nombre de la consulta que se va a ejecutar
            //Se le pasan los parametros a la consulta
            consulta.setParameter("lineaVenta",DF);
            resultado = consulta.getResultList();
            return resultado;   
        }catch(Exception e){
            new funciones().setMsj(4,"ERROR: No se pudo consultar DETALLE DE LA LINEA DE VENTA ");
            return resultado;
        }
        
    }
     
     
     
     /*
     * FUNCION QUE ACTUALIZA EL CODIGO DE PRODUCTO Y DE CAMBIO EN LA TABLA DETALLE ENVIO
     */ 
    public void updateCodigosCambio(DetalleEnvio de){
        try{
            String sql =        "UPDATE detalle_envio "
                            +   "SET CAMBIO_PRODUCTO = '"+de.getInventario().getCodigo()+"' , "
                            +   "    CODIGO_PRODUCTO = '"+de.getCambioProducto().getCodigo()+"'  "
                            +   "WHERE NUMENVIO = "+de.getDetalleEnvioPK().getNumenvio()+" AND CODIGO_PRODUCTO = '"+de.getDetalleEnvioPK().getCodigoProducto()+"'  ";
            
            Query query = em.createNativeQuery(sql);
            query.executeUpdate();
        }catch(Exception e){
            new funciones().setMsj(4,"ERROR: No se pudo ACTUALIZAR CODIGOS -- CAMBIO DEL PRODUCTO");
            
        }
    }
    
    
    
    /*
     * FUNCION QUE DEVUELVE LOS PRODUCTOS DEVUELTOS PARA REINGRESO
     */
     public List<DetalleEnvio> reingresosInventarioDevoluciones(){
        List<DetalleEnvio> resultado =null;
        try{
            Query consulta = em.createNamedQuery("DetalleEnvio.reingresosPorDevolucion"); 
            resultado = consulta.getResultList();
            return resultado;   
        }catch(Exception e){
            new funciones().setMsj(4,"ERROR: No se pudo consultar REINGRESOS DE PRODUCTOS POR DEVOLUCIONES");
            return resultado;
        }
        
    }
     
     
     
     
     /*
     * FUNCION QUE DEVUELVE LOS PRODUCTOS ANULADOS CUANDO SE ANULA UNA FACTURA
     */
     public List<DetalleEnvio> productosAnulados(Factura factura){
        List<DetalleEnvio> resultado =null;
        try{
            Query consulta = em.createNamedQuery("DetalleEnvio.reingresoPorAnulacion"); 
            consulta.setParameter("factura",factura);
            resultado = consulta.getResultList();
            return resultado;   
        }catch(Exception e){
            new funciones().setMsj(4,"ERROR: No se pudo consultar PRODUCTOS DE LA FACTURA ANULADA");
            return resultado;
        }
        
    }
     
     
     
     /*
     * FUNCION QUE DEVUELVE EL DETALLE DEL ENVIO
     * PARA IMPRIMIR ORDEN DE ENVIO
     */ 
    public List<Object> getDetalleCurvaEnvio(Envio envio){
        List<Object> consulta = null;
        try{
            String sql =        "SELECT i.ESTILO,i.COLOR,i.TALLA,COUNT(*) as CANTIDAD " 
                            +   "FROM detalle_envio d ,inventario i " 
                            +   "WHERE NUMENVIO = "+envio.getNumenvio()+" AND d.CODIGO_PRODUCTO = i.CODIGO " 
                            +   "GROUP BY 1,2,3 ";
            
            Query query = em.createNativeQuery(sql);
            consulta = query.getResultList();
            return consulta;   
        }catch(Exception e){
            new funciones().setMsj(4,"ERROR: No se pudo realizar la consulta de detalle del envio #"+ envio.getNumenvio()+" a la BD");
            return consulta;
        }
    }
    
    public boolean isDetalleNI(int envio){
        boolean NI = false;
        try{
            Query consulta = em.createNamedQuery("DetalleEnvio.unidadesNI"); 
            consulta.setParameter("numenvio", envio);
            Object r = consulta.getSingleResult();
            int resultado = 0;
            /* Objetivo: almacena el siguiente id valido
             */
            if (r != null) {
                resultado = Integer.parseInt(r.toString());
            }
            if(resultado > 0 ){
                NI = true;
            }
            return NI;   
        }catch(Exception e){
            new funciones().setMsj(4,"ERROR: No se pudo consultar unidades NI Despachadas");
            return NI;
        }
    }
    
    /*
     * FUNCION QUE DEVUELVE LA CURVA DE UN ESTILO DE UN ENVIO ESPECIFICO
     */
     public List<String> curvaEstiloEnvio(Envio envio , String estilo){
        List<String> resultado =null;
        try{
            Query consulta = em.createNamedQuery("DetalleEnvio.curvaEstilo"); 
            consulta.setParameter("numenvio", envio.getNumenvio());
            consulta.setParameter("estilo", estilo);
            resultado = consulta.getResultList();
            return resultado;   
        }catch(Exception e){
            new funciones().setMsj(4,"ERROR: No se pudo consultar la curva de un estilo del la orden de envio");
            return resultado;
        }
        
    }
     
     
     /*
     * FUNCION QUE DEVUELVE EL PRECIO UNITARIO A FACTURAR DE UN ESTILO Y COLOR ESPECIFICO
     */
     public BigDecimal precioUnitario(Envio envio , String estilo, String color){
        BigDecimal resultado =BigDecimal.ZERO;
        try{
            Query consulta = em.createNamedQuery("DetalleEnvio.precioUnitarioEstilo"); 
            consulta.setParameter("numenvio", envio.getNumenvio());
            consulta.setParameter("estilo", estilo);
            consulta.setParameter("color", color);
            List<BigDecimal> r = consulta.getResultList();
            if(r != null){
                resultado = r.get(0);
            }
            return resultado;   
        }catch(Exception e){
            new funciones().setMsj(4,"ERROR: No se pudo consultar la curva de un estilo del la orden de envio");
            return resultado;
        }
        
    }
     
     
     
     
      /*
     * FUNCION QUE CUENTA LOS PRODUCTOS EMPACADOS DE UN PEDIDO
     */
     public int unidadesDespachadas(Pedido pedido){
        try{
            Query consulta = em.createNamedQuery("DetalleEnvio.unidadesDespachadasByPedido"); 
            consulta.setParameter("pedido", pedido);
            Object r = consulta.getSingleResult();
            int resultado = 0;
            /* Objetivo: almacena el siguiente id valido
             */
            if (r != null) {
                resultado = Integer.parseInt(r.toString());
            }
            return resultado;   
        }catch(Exception e){
            new funciones().setMsj(4,"ERROR: No se pudo consultar unidades Despachadas");
            return 0;
        }
        
    }
     
     
     
     
     
     /*
     * FUNCION QUE DEVUELVE EL DETALLE DE UNA ORDEN DE ENVIO DE UNA LINEA DE VENTA (DETALLE_FACTURA)
     */
     public List<DetalleEnvio> getDetalleEnvio(Envio envio){
        List<DetalleEnvio> resultado =null;
        try{
            Query consulta = em.createNamedQuery("DetalleEnvio.findByNumenvio"); //Especificar nombre de la consulta que se va a ejecutar
            //Se le pasan los parametros a la consulta
            consulta.setParameter("numenvio",envio.getNumenvio());
            resultado = consulta.getResultList();
            return resultado;   
        }catch(Exception e){
            new funciones().setMsj(4,"ERROR: No se pudo consultar DETALLE DE ENVIO ");
            return resultado;
        }
        
    }
     
     
     
     
     
     
     
     /*
     * DETALLE VENTAS DEL MES
     */
    public List<VentasMes> getVentasMes(int mes, int anio){
        List<Object[]> consulta = null;
        List<VentasMes> ventas = new ArrayList<VentasMes>();
        String sql = "";
        try{
            sql = "call SP_GET_VENTAS_YEAR_MONTH("+anio+","+mes +");";
            
            Query query = em.createNativeQuery(sql);
            consulta = query.getResultList();
            if(consulta != null){
                for(Object[] actual : consulta){
                    VentasMes nuevo = new VentasMes(actual);
                    ventas.add(nuevo);
                }
            }
            return ventas;   
        }catch(Exception e){
            new funciones().setMsj(4,"ERROR: No se pudo realizar la CONSULTA DE LAS VENTAS DEL MES");
            return ventas;
        }
    }
    
}
