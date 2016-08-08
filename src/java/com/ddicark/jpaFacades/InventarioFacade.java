/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ddicark.jpaFacades;

import com.ddicark.auxiliares.InventarioConsulta;
import com.ddicark.auxiliares.curvaColor;
import com.ddicark.auxiliares.funciones;
import com.ddicark.entidades.Caja;
import com.ddicark.entidades.FacturaIngreso;
import com.ddicark.entidades.Inventario;
import com.ddicark.entidades.Producto;
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
public class InventarioFacade extends AbstractFacade<Inventario> {
    @PersistenceContext(unitName = "ddicark-shoesPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public InventarioFacade() {
        super(Inventario.class);
    }
    
    /*
     * Deveulve el listado de los diferentes materiales existentes
     */
    public List<String> getMaterialList(String query){
        List<String> resultado = null;
        try{
            Query consulta = em.createNamedQuery("Inventario.listaMaterial"); //Especificar nombre de la consulta que se va a ejecutar
            String like = query + "%";
            consulta.setParameter("query", like);
            resultado = consulta.getResultList();
            return resultado;   
        }catch(Exception e){
            new funciones().setMsj(4,"ERROR: No se pudo realizar la consulta de materiales en la tabla inventario");
            return resultado;
        }
    }
    
    /*
     * Deveulve el listado de las diferentes marcas existentes
     */
    public List<String> getMarcaList(String query){
        List<String> resultado = null;
        try{
            Query consulta = em.createNamedQuery("Inventario.listaMarca"); //Especificar nombre de la consulta que se va a ejecutar
            String like = query + "%";
            consulta.setParameter("query", like);
            resultado = consulta.getResultList();
            return resultado;   
        }catch(Exception e){
            new funciones().setMsj(4,"ERROR: No se pudo realizar la consulta de materiales en la tabla inventario");
            return resultado;
        }
    }
    
    
    /*
     * Deveulve el listado de los diferentes colores existentes
     * que empiezan con query
     */
    public List<String> getColorList(String query){
        List<String> resultado = null;
        try{
            Query consulta = em.createNamedQuery("Inventario.listaColoresQuery"); //Especificar nombre de la consulta que se va a ejecutar
            String like = query + "%";
            consulta.setParameter("query", like);
            resultado = consulta.getResultList();
            return resultado;   
        }catch(Exception e){
            new funciones().setMsj(4,"ERROR: No se pudo realizar la consulta de colores con query en la tabla inventario");
            return resultado;
        }
    }
    
    /*
     * Devuelve el listado de los diferentes colores existentes
     * de un tipo de producto
     */
    public List<String> getColorList(int tipo){
        List<String> resultado = null;
        try{
            Query consulta = em.createNamedQuery("Inventario.listaColoresTipo"); 
            consulta.setParameter("tipoProducto", tipo);
            resultado = consulta.getResultList();
            return resultado;   
        }catch(Exception e){
            new funciones().setMsj(4,"ERROR: No se pudo realizar la consulta de todos los colores en la tabla inventario");
            return resultado;
        }
    }
    
    /*
     * Devuelve el listado de las diferentes marcas existentes
     * de un tipo de producto
     */
    public List<String> getMarcaList(int tipo){
        List<String> resultado = null;
        try{
            Query consulta = em.createNamedQuery("Inventario.listaMarcasTipo"); 
            consulta.setParameter("tipoProducto", tipo);
            resultado = consulta.getResultList();
            return resultado;   
        }catch(Exception e){
            new funciones().setMsj(4,"ERROR: No se pudo realizar la consulta de todos los colores en la tabla inventario");
            return resultado;
        }
    }
    
    /*
     * Devuelve el listado de los diferentes materiales existentes
     * de un tipo de producto
     */
    public List<String> getMaterialList(int tipo){
        List<String> resultado = null;
        try{
            Query consulta = em.createNamedQuery("Inventario.listaMaterialesTipo"); 
            consulta.setParameter("tipoProducto", tipo);
            resultado = consulta.getResultList();
            return resultado;   
        }catch(Exception e){
            new funciones().setMsj(4,"ERROR: No se pudo realizar la consulta de todos los colores en la tabla inventario");
            return resultado;
        }
    }
    
    /*
     * FUNCION QUE VERIFICA SI EL CODIGO DE LA UNIDAD EXISTE
     */
    public boolean existeCodigoUnidad(String codigo){
        boolean existe;
        Query query = em.createNamedQuery("Inventario.findByCodigo");
        //Se le pasan los parametros a la consulta
        query.setParameter("codigo",codigo);
        if (!query.getResultList().isEmpty()) {
            existe = true;
        }else{
            existe = false;
        }
        return existe;
    }
    
    
     /*
     * LISTADO DE LOS PRODUCTOS DE TIPO [tipo]
     * EN MUESTRA
     */
    public List<Inventario> muestraList(int tipo){
        List<Inventario> resultado = null;
        try{
            Query consulta = em.createNamedQuery("Inventario.listaMuestras"); 
            consulta.setParameter("tipoProducto", tipo);
            resultado = consulta.getResultList();
            return resultado;   
        }catch(Exception e){
            new funciones().setMsj(4,"ERROR: No se pudo realizar la consulta de MUESTRAS en la tabla inventario");
            return resultado;
        }
    }
    
    /*
     * BUSCAR Y DEVOLVER ARTICULO
     */
    public Inventario getArticulo(String codigoBarra){
        List<Inventario> r = null;
        try{
            Query query = em.createNamedQuery("Inventario.findByCodigo");
            //Se le pasan los parametros a la consulta
            query.setParameter("codigo",codigoBarra);
            r = query.getResultList();
            if (r.size() == 1) {
                return r.get(0);
            }
        }catch(Exception e){
            new funciones().setMsj(4,"ERROR CON LA BASE DE DATOS AL BUSCAR ARTICULO");
        }
        return null;
    }
    
    
    
    /*
     * CONSULTA PEDIDO vrs EXISTENCIAS
     */
    public List<Object[]> consultaPedidosVrsExistencias(int tipo){
        List<Object[]> consulta = null;
        try{
            String sql =        "SELECT d.CODESTILO, d.COLOR, d.TALLA, SUM(d.CANTIDAD) as Pedidos , "
                            +   "       (SELECT COUNT(*) "
                            +   "        FROM inventario i "
                            +   "        WHERE i.TIPO_PRODUCTO = 1 AND i.ESTADOPRODUCTO = 'EN EXISTENCIA' "
                            +   "        AND i.ESTILO = d.CODESTILO AND i.COLOR = d.COLOR AND i.TALLA=d.TALLA) as Existencias ,"
                            +   "       SUM(d.CANTIDAD) - (SELECT COUNT(*) "
                            +   "        FROM inventario i "
                            +   "        WHERE i.TIPO_PRODUCTO = 1 AND i.ESTADOPRODUCTO = 'EN EXISTENCIA' "
                            +   "        AND i.ESTILO = d.CODESTILO AND i.COLOR = d.COLOR AND i.TALLA=d.TALLA) as Diferencia "
                            +   "FROM detalle_pedido d "
                            +   "WHERE d.TIPO_PRODUCTO = 1 AND d.NUMPEDIDO in (SELECT numpedido FROM pedido WHERE estado_pedido = 'APROBADO') "
                            +   "GROUP BY d.CODESTILO,d.COLOR,d.TALLA ";
            switch(tipo){
                case 1:
                    //Consulta completa
                    break;
                case 2:
                    //Consultar solo los que se pueden cubrir con existencias
                    sql = sql + " HAVING Diferencia <= 0 ";
                    break;
                case 3:
                    //Consultar solo los que NO se pueden cubrir con existencias
                    sql = sql + " HAVING Diferencia > 0 ";
                    break;
            }
            Query query = em.createNativeQuery(sql);
            consulta = query.getResultList();
            return consulta;   
        }catch(Exception e){
            new funciones().setMsj(4,"ERROR: No se pudo realizar la consulta de Pedidos vrs Existencias a la BD");
            return consulta;
        }
    }
    
    
     /*
     * Devuelve el listado de las diferetes tallas
     * de la curva de una caja
     */
    public List<String> getTallasCurva(Caja caja , int tipo){
        List<String> resultado = null;
        try{
            Query consulta = em.createNamedQuery("Inventario.listaTallasCaja");
            consulta.setParameter("tipoProducto", tipo);
            consulta.setParameter("numcaja", caja.getNumcaja());
            resultado = consulta.getResultList();
            return resultado;   
        }catch(Exception e){
            new funciones().setMsj(4,"ERROR: No se pudo realizar la consulta de tallas de la curva");
            return resultado;
        }
    }
    
    
     /*
     * Devuelve la curva de una caja
     */
    public List<Object> getCurvaCaja(Caja caja , int tipo){
        List<Object> resultado = null;
        try{
            Query consulta = em.createNamedQuery("Inventario.curvaByCaja");
            consulta.setParameter("tipoProducto", tipo);
            consulta.setParameter("numcaja", caja.getNumcaja());
            resultado = consulta.getResultList();
            return resultado;   
        }catch(Exception e){
            new funciones().setMsj(4,"ERROR: No se pudo realizar la consulta de tallas de la curva");
            return resultado;
        }
    }
    
    /*
     * LISTADO DE PRODUCTOS DE UNA CAJA QUE PUEDEN LISTAR A ORDEN DE ENVIO
     */
     public List<Inventario> unidadesByCaja(Caja caja , int tipo){
        List<Inventario> resultado = null;
        try{
            Query consulta = em.createNamedQuery("Inventario.listarProductoCajaByEnvio");
            consulta.setParameter("tipoProducto", tipo);
            consulta.setParameter("numcaja", caja.getNumcaja());
            resultado = consulta.getResultList();
            return resultado;   
        }catch(Exception e){
            new funciones().setMsj(4,"ERROR: No se pudo realizar la consulta de tallas de la curva");
            return resultado;
        }
    }
     
     
    /*
     * LISTADO DE PRODUCTOS DE UNA CAJA
     */
     public List<Inventario> detalleDeCaja(Caja caja){
        List<Inventario> resultado = null;
        try{
            Query consulta = em.createNamedQuery("Inventario.detalleCaja");
            consulta.setParameter("caja", caja);
            resultado = consulta.getResultList();
            return resultado;   
        }catch(Exception e){
            new funciones().setMsj(4,"ERROR: No se pudo realizar la consulta de detalle de caja");
            return resultado;
        }
    } 
    
    
    /*
     * FUNCION QUE CUENTA EL NUMERO DE ARTICULOS
     * EN EXISTECIA DE UNA CAJA TOMANDO EN CUENTA LOS DADO COMO MUESTRA Y LOS DEFECTUOSOS
     */
    public int countArticulosCaja(Caja caja , int tipo) {
        Query query = em.createNamedQuery("Inventario.contarArticulosCaja");
        query.setParameter("tipoProducto", tipo);
        query.setParameter("numcaja", caja.getNumcaja());
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
        return resultado;
    }
    
    
    
    /*
     * FUNCION QUE CUENTA EL NUMERO DE ARTICULOS
     * EN EXISTECIA DE UNA CAJA 
     */
    public int countExistenciasCaja(Caja caja , int tipo) {
        Query query = em.createNamedQuery("Inventario.existenciasCaja");
        query.setParameter("tipoProducto", tipo);
        query.setParameter("caja", caja.getNumcaja());
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
        };
        return resultado;
    }
    
    
    
    /*
     * CONSULTA DE EXISTENCIAS , MUESTRAS y DEFECTUOSOS
     */
    public List<InventarioConsulta> consultaExistenciaUnidades(int tipoProducto, int caja , String marca , String material){
        List<Object[]> consulta = null;
        List<InventarioConsulta> unidades = new ArrayList<InventarioConsulta>();
        String sql = "";
        try{
            sql =        "SELECT i.ESTILO,i.COLOR,i.TALLA,i.PRECIOMAYOREO, COUNT(*),"
                +   "       (SELECT COUNT(*) FROM inventario m"
                +   "        WHERE m.TIPO_PRODUCTO = 1 AND m.ESTADOPRODUCTO = 'MUESTRA' "
                +   "        AND m.ESTILO = i.ESTILO AND m.COLOR = i.COLOR AND m.talla=i.TALLA) as MUESTRA,"
                +   "       (SELECT COUNT(*) FROM inventario d"
                +   "        WHERE d.TIPO_PRODUCTO = 1 AND d.ESTADOPRODUCTO = 'DEFECTUOSO' "
                +   "        AND d.ESTILO = i.ESTILO AND d.COLOR = i.COLOR AND d.talla=i.TALLA) as DEFECTUOSO "
                +   "FROM inventario i "
                +   "WHERE i.TIPO_PRODUCTO = "+tipoProducto +" AND i.ESTADOPRODUCTO ='EN EXISTENCIA' ";
            
            switch(caja){
                case 2:
                    sql = sql + " AND i.NUMCAJA IS NOT NULL ";
                    break;
                case 3:
                    sql = sql + " AND i.NUMCAJA IS NULL ";
                    break;
            }
            if(marca != null){
                if(!marca.equals("")){
                    sql = sql + " AND i.MARCA = '"+marca+"' ";
                }
            }
            if(material != null){
                if(!material.equals("")){
                    sql = sql + " AND i.MARCA = '"+marca+"' ";
                }
            }
            sql = sql +   "GROUP BY 1,2,3,4 ";
            
            Query query = em.createNativeQuery(sql);
            consulta = query.getResultList();
            if(consulta != null){
                for(Object[] actual : consulta){
                    InventarioConsulta nuevo = new InventarioConsulta(actual);
                    unidades.add(nuevo);
                }
            }
            return unidades;    
        }catch(Exception e){
            new funciones().setMsj(4,"ERROR: No se pudo realizar la CONSULTA 1 de EXISTENCIAS a la BD");
            return unidades;
        }
    }
    
    
    /*
     * CONSULTA CAJA EN EXISTENCIA
     * consultarCaja 1:TODAS 2:SOLO COMPLETAS y 3:INCOMPLETAS
     */
    public List<InventarioConsulta> consultaExistenciaCajas(int tipoProducto, int consultarCaja){
        List<Object[]> consulta = null;
        List<InventarioConsulta> cajas = new ArrayList<InventarioConsulta>();
        String sql = "";
        try{
            sql =       "SELECT C.ESTILO,C.COLORES,C.DETALLE_CAJA,C.PRECIOVENTA_UNIDAD, COUNT(*), C.COMPLETA "
                +       "FROM caja C " 
                +       "WHERE TIPO_PRODUCTO = " + tipoProducto 
                +       "  AND C.NUMCAJA IN ( "
                +       "SELECT DISTINCT(i.NUMCAJA) FROM inventario i WHERE i.TIPO_PRODUCTO = "+tipoProducto+" AND i.ESTADOPRODUCTO = 'EN EXISTENCIA' "
                +       ") " ;
                
            
            switch(consultarCaja){
                case 2:
                    sql = sql + " AND C.COMPLETA = TRUE ";
                    break;
                case 3:
                    sql = sql + " AND C.COMPLETA = FALSE ";
                    break;
            }
            sql = sql +   "GROUP BY 1,2,3,4";
            
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
            new funciones().setMsj(4,"ERROR: No se pudo realizar la CONSULTA 1 de EXISTENCIAS a la BD");
            return cajas;
        }
    }
    
    
    
    /*
     * INVENTARIO FISICO POR TIPO [tipo]
     * 
     */
    public List<Inventario> unidadesFisicas(int tipo , int consultarCaja){
        List<Inventario> resultado = null;
        Query consulta = null;
        try{
            switch(consultarCaja){
                case 0://Consultar Articulos sin caja
                    consulta = em.createNamedQuery("Inventario.unidadesFisicasByTipoSinCaja");
                    break;
                case 1: //Consultar los articulos en cajs
                    consulta = em.createNamedQuery("Inventario.unidadesFisicasByTipoEnCaja"); 
                    break;
                case 2: //Consultar todos los articulos
                    consulta = em.createNamedQuery("Inventario.unidadesFisicasByTipo"); 
                    break;
            }
            if(consulta != null){
                consulta.setParameter("tipoProducto", tipo);
                resultado = consulta.getResultList();
            }
            return resultado;   
        }catch(Exception e){
            new funciones().setMsj(4,"ERROR: No se pudo realizar la consulta de MUESTRAS en la tabla inventario");
            return resultado;
        }
    }
    
    
    
    /*
     * LISTA DE COLORES por estilo
     */
    public List<String> coloresByEstilo(Producto estilo){
        List<String> r = null;
        try{
            Query query = em.createNamedQuery("Inventario.coloresByEstilo");
            //Se le pasan los parametros a la consulta
            query.setParameter("estilo",estilo);
            r = query.getResultList();
        }catch(Exception e){
            new funciones().setMsj(4,"ERROR AL OBTENER COLORES POR ESTILO");
        }
        return r;
    }
    
    
    
    
    
    /*
     * Devuelve el listado de las diferetes tallas
     * de la curva de una caja
     */
    public List<String> tallasCaja(Caja caja){
        List<String> resultado = null;
        try{
            Query consulta = em.createNamedQuery("Inventario.tallasByCaja");
            consulta.setParameter("caja", caja);
            resultado = consulta.getResultList();
            return resultado;   
        }catch(Exception e){
            new funciones().setMsj(4,"ERROR: No se pudo realizar la consulta de tallas de la caja");
            return resultado;
        }
    }
    
    
    
    /*
     * Devuelve la curva de una caja
     */
    public List<Object> curvaByCaja(Caja caja){
        List<Object> resultado = null;
        try{
            Query consulta = em.createNamedQuery("Inventario.curvaCaja");
            consulta.setParameter("caja", caja);
            resultado = consulta.getResultList();
            return resultado;   
        }catch(Exception e){
            new funciones().setMsj(4,"ERROR: No se pudo realizar la consulta de la curva de una caja");
            return resultado;
        }
    }
    
    /*
     * Devuelve la curva de una caja
     */
    public BigDecimal precioVentaByEstilo(Producto estilo){
        List<BigDecimal> resultado;
        try{
            Query consulta = em.createNamedQuery("Inventario.precioByEstilo");
            consulta.setParameter("estilo", estilo);
            resultado = consulta.getResultList();
            if(resultado != null && !resultado.isEmpty()){
                return resultado.get(0);
            }
             
        }catch(Exception e){
            new funciones().setMsj(4,"ERROR: No se pudo realizar la consulta de precio del estilo");
        }
        return BigDecimal.ZERO;  
    }
    
    
    
    public int countUnitario(FacturaIngreso factura) {
        Query query = em.createNamedQuery("Inventario.contarUnitFactura");
        query.setParameter("factura", factura);
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
        return resultado;
    }
    
    
    
    
    /*
     * BUSCAR Y DEVOLVER ARTICULO
     */
    public List<Inventario> curvaEstiloUnitario(String estilo){
        List<Inventario> r = null;
        try{
            Query query = em.createNamedQuery("Inventario.unitarioByEstilo");
            //Se le pasan los parametros a la consulta
            query.setParameter("estilo",estilo);
            r = query.getResultList();
            return r;
        }catch(Exception e){
            new funciones().setMsj(4,"ERROR CON LA BASE DE DATOS AL BUSCAR ARTICULOS UNITARIO");
        }
        return null;
    }
    
    
    
    
    
    
    public List<String> getTallasCurvaAnterior(Caja caja , int tipo){
        List<String> resultado = null;
        try{
            Query consulta = em.createNamedQuery("Inventario.listaTallasCajaAnterior");
            consulta.setParameter("tipoProducto", tipo);
            consulta.setParameter("numcaja", caja.getNumcaja());
            resultado = consulta.getResultList();
            return resultado;   
        }catch(Exception e){
            new funciones().setMsj(4,"ERROR: No se pudo realizar la consulta de tallas de la curva");
            return resultado;
        }
    }
    
    public List<Object> getCurvaCajaAnterior(Caja caja , int tipo){
        List<Object> resultado = null;
        try{
            Query consulta = em.createNamedQuery("Inventario.curvaByCajaAnterior");
            consulta.setParameter("tipoProducto", tipo);
            consulta.setParameter("numcaja", caja.getNumcaja());
            resultado = consulta.getResultList();
            return resultado;   
        }catch(Exception e){
            new funciones().setMsj(4,"ERROR: No se pudo realizar la consulta de tallas de la curva");
            return resultado;
        }
    }
}
