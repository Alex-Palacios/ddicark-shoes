/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ddicark.controladores;

import com.ddicark.auxiliares.InventarioImport;
import com.ddicark.auxiliares.JsfUtil;
import com.ddicark.auxiliares.curvaColor;
import com.ddicark.auxiliares.funciones;
import com.ddicark.entidades.Caja;
import com.ddicark.entidades.Configuraciones;
import com.ddicark.entidades.FacturaIngreso;
import com.ddicark.entidades.Ingreso;
import com.ddicark.entidades.Inventario;
import com.ddicark.entidades.Producto;
import com.ddicark.entidades.Proveedor;
import com.ddicark.entidades.Transaccion;
import com.ddicark.jpaFacades.CajaFacade;
import com.ddicark.jpaFacades.ConfiguracionesFacade;
import com.ddicark.jpaFacades.FacturaIngresoFacade;
import com.ddicark.jpaFacades.IngresoFacade;
import com.ddicark.jpaFacades.InventarioFacade;
import com.ddicark.jpaFacades.ProductoFacade;
import com.ddicark.jpaFacades.ProveedorFacade;
import com.ddicark.jpaFacades.TransaccionFacade;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import org.apache.poi.POIXMLException;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author DDICARK
 */
@ManagedBean(name = "corteInventarioInicial")
@SessionScoped
public class CorteInventarioInicial implements Serializable{

    @EJB
    private TransaccionFacade ejbFacadeTransac;
    
    @EJB
    private ConfiguracionesFacade ejbFacadeConfig;
    
    @EJB
    private ProveedorFacade ejbFacadeProveedor;
    
    @EJB
    private FacturaIngresoFacade ejbFacadeFacturaCorte;
    
    @EJB
    private IngresoFacade ejbFacadeIngreso;
    
    @EJB
    private CajaFacade ejbFacadeCaja;
    
    @EJB
    private InventarioFacade ejbFacadeInventario;
    
    @EJB
    private ProductoFacade ejbFacadeEstilos;
    
    
    
    //VARIABLES GLOBALES
    private FacturaIngreso facturaCorte;
    private Proveedor proveedorCorte;
    private boolean ingresarPorCaja = true;
    private boolean ingresarCurva = false;
    private String codigoBarra;
    private String ubicacionCajaUltima;
    private String ubicacionUnidad;
    //Constructor
    
    public CorteInventarioInicial()  {
    }
    
    //VARIABLES DE CONTROL
    //General
    private Producto estilo;
    private String marca;
    private String material;
    private char genero;
    private char clasePersona;
    //Curva
    private int numcolores;
    private String[] tallas = new String[10];
    private curvaColor[] curva = {new curvaColor(),new curvaColor(),new curvaColor(),new curvaColor(),new curvaColor(),new curvaColor()};
    private int numcajas;
    private float costoReal;
    private float precioVenta;
    
    
    private int numTallas;
    private Caja caja;
    private List<Caja> cajas;
    private Inventario unidad;
    private List<Inventario> unidades;
    //GETTERS AND SETTERS
    public Producto getEstilo() {
        return estilo;
    }

    public void setEstilo(Producto estilo) {
        this.estilo = estilo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public char getGenero() {
        return genero;
    }

    public void setGenero(char genero) {
        this.genero = genero;
    }

    public char getClasePersona() {
        return clasePersona;
    }

    public void setClasePersona(char clasePersona) {
        this.clasePersona = clasePersona;
    }

    public int getNumcolores() {
        return numcolores;
    }

    public void setNumcolores(int numcolores) {
        this.numcolores = numcolores;
    }

    public String[] getTallas() {
        return tallas;
    }

    public void setTallas(String[] tallas) {
        this.tallas = tallas;
    }

    public curvaColor[] getCurva() {
        return curva;
    }

    public void setCurva(curvaColor[] curva) {
        this.curva = curva;
    }

    public int getNumcajas() {
        return numcajas;
    }

    public void setNumcajas(int numcajas) {
        this.numcajas = numcajas;
    }

    public float getCostoReal() {
        return costoReal;
    }

    public void setCostoReal(float costoReal) {
        this.costoReal = costoReal;
    }

    public float getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(float precioVenta) {
        this.precioVenta = precioVenta;
    }

    
    public boolean isIngresarPorCaja() {
        return ingresarPorCaja;
    }

    public void setIngresarPorCaja(boolean ingresarPorCaja) {
        this.ingresarPorCaja = ingresarPorCaja;
    }

    public void actualizarIngresarCaja(){
        boolean g=ingresarPorCaja;
    }

    public boolean isIngresarCurva() {
        return ingresarCurva;
    }

    public void setIngresarCurva(boolean ingresarCurva) {
        this.ingresarCurva = ingresarCurva;
    }

    public Caja getCaja() {
        return caja;
    }

    public void setCaja(Caja caja) {
        this.caja = caja;
    }

    public int getNumTallas() {
        return numTallas;
    }

    public void setNumTallas(int numTallas) {
        this.numTallas = numTallas;
    }

    public List<Caja> getCajas() {
        return cajas;
    }

    public void setCajas(List<Caja> cajas) {
        this.cajas = cajas;
    }

    public Inventario getUnidad() {
        return unidad;
    }

    public void setUnidad(Inventario unidad) {
        this.unidad = unidad;
    }

    public List<Inventario> getUnidades() {
        return unidades;
    }

    public void setUnidades(List<Inventario> unidades) {
        this.unidades = unidades;
    }

    public String getCodigoBarra() {
        return codigoBarra;
    }

    public void setCodigoBarra(String codigoBarra) {
        this.codigoBarra = codigoBarra;
    }

    public String getUbicacionUnidad() {
        return ubicacionUnidad;
    }

    public void setUbicacionUnidad(String ubicacionUnidad) {
        this.ubicacionUnidad = ubicacionUnidad;
    }
    
    
    
    
    /*
     * FUNCIONES CORTE DE INVENTARIO
     */
    
    public void prepararCorteInventarioInicial(){
        this.facturaCorte = ejbFacadeFacturaCorte.facturaCorteInventarioInicial();
        this.proveedorCorte = ejbFacadeProveedor.proveedorCorteInventarioInicial();
        this.caja = null;
        this.unidad = null;
        this.cajas = new ArrayList<Caja>();
        this.unidades = new ArrayList<Inventario>();
        this.numTallas = 6;
        resetearTodo();
    }
    
     //RESETEAR VALORES DE DETALLES
    public void resetearCurva(){
        for(int i=0; i < curva.length; i++){
            curva[i].resetear();
        }
        numcolores = 1;
        costoReal = (float) 0.00;
        precioVenta = (float) 0.00;
        numcajas = 0;
    }
    
      //RESETEAR VALORES DE DETALLES
    public void resetearCurvaTalla(){
        for(int i=0; i < curva.length; i++){
            curva[i].resetear();
        }
        for(int t=0; t < tallas.length; t++){
            tallas[t] = "";
        }
        numcolores = 1;
        costoReal = (float) 0.00;
        numcajas = 0;
    }
    
     //RESETEAR TODO
    public void resetear(){
        estilo=null;
//        marca=null;
//        material=null;
        ingresarCurva = false;
        ingresarPorCaja= true;
        resetearCurva();
    }
    
      //RESETEAR TODO
    public void resetearTodo(){
        estilo=null;
        marca=null;
        material=null;
        ingresarCurva = false;
        ingresarPorCaja= true;
        resetearCurva();
        resetearCurvaTalla();
    }
    
    
    public void definirCurva(){
        ingresarCurva = true;
        resetearCurva();
        marca = marca.toUpperCase();
        if(material != null){
            material = material.toUpperCase();
        }
    }
    
    /*
     * Funcion que valida la CURVA INGRESADA
     */
    
    public boolean validarCurva(){ 
        boolean valido = true;
        int numTalla = 0; //numero de tallas ingresadas
        //Recorrer arreglo de tallas
        for(int i=0 ; i < numTallas; i++){
            //Verificar que se haya ingresado una
            if((tallas[i] != null) && (!"".equals(tallas[i])) ){
                numTalla++;
            }
        }
        //Recorrer arreglo de colores
        int numColor = 0;
        for(int i=0 ; i < numcolores; i++){
            //Verificar que se haya ingresado una
            if((curva[i].getColor() != null) && (!"".equals(curva[i].getColor())) ){
                numColor++;
                curva[i].setColor(curva[i].getColor().toUpperCase());
            }
        }
        try{
            if(costoReal <= 0){
                valido = false;
                new funciones().setMsj(2,"Costo Unitario no puede ser menor o igual a CERO");
            }
            if(precioVenta <= 0){
                valido = false;
                new funciones().setMsj(2,"PRECIO VENTA no puede ser menor o igual a CERO");
            }
            if(numTalla != numTallas){
                valido = false;
                new funciones().setMsj(2,"Faltan tallas");
            }
            if(numColor != numcolores){
                valido = false;
                new funciones().setMsj(2,"Faltan un colores");
            }
            if(ingresarPorCaja){
                if(numcajas <=0){
                    valido = false;
                    new funciones().setMsj(2,"Ingrese el numero de cajas a ingresar con la misma curva");
                }
            }
            return valido;
        }catch(NullPointerException e){
            new funciones().setMsj(3,"HAY ALGUNOS VALORES NULOS");
            return false;
        }catch(Exception e){
            return false;
        }
    }
    
    
    //MUESTRA DIALOGOS PARA LEER CODIGOS
    public void leerCodigos(){
        caja = new Caja();
        unidad = new Inventario();
        cajas.clear();
        unidades.clear();
        codigoBarra=null;
        RequestContext context = RequestContext.getCurrentInstance(); 
        if(validarCurva()){
            if(ingresarPorCaja){
                //INGRESAR POR CAJAS
                String detalle=null;
                String colores=null;
                int numUnidadesCaja = 0;
                //LLenar caja
                for(int t=0; t < numTallas;t++){
                    //Detalle de Curva
                    if(t==0){
                        detalle = tallas[t];
                    }else{
                        detalle = detalle + "-" + tallas[t];
                    }
                }
                for(int c=0; c < numcolores;c++){
                    //Detalle Colores
                    if(c==0){
                        colores = curva[c].getColor();
                    }else{
                        colores = colores + "-" + curva[c].getColor();
                    }
                }
                for(int y=0; y < numcolores;y++){
                    //CANTIDADES
                    for(int x=0; x < numTallas; x++){
                        numUnidadesCaja = numUnidadesCaja + curva[y].getCantidad(x);
                    }                    
                }
                if(numUnidadesCaja > 0){
                    caja = new Caja();
                    caja.setProducto(estilo);
                    caja.setColoresCaja(colores);
                    caja.setDetalleCaja(detalle);
                    caja.setUnidadesCaja(numUnidadesCaja);
                    caja.setCostoUnitario(new BigDecimal(costoReal));
                    caja.setCostoUnitarioReal(new BigDecimal(costoReal));
                    caja.setCostoCaja(new BigDecimal(new funciones().redondearMas(costoReal*caja.getUnidadesCaja(), 2)));
                    caja.setPrecioventaUnidad(new BigDecimal(precioVenta));
                    caja.setCompleta(true);
                    caja.setUbicacionCaja(ubicacionCajaUltima);
                    for(int C=1; C <= numcajas; C++){
                        cajas.add(caja);
                    }
                    context.execute("codigoCajaCodigoDialog.show();");
                }else{
                    new funciones().setMsj(2, "CAJA VACIA");
                }
                
            }else{
                
                //INGRESAR POR UNIDAD
                for(int y=0; y < numcolores;y++){
                    //CANTIDADES
                    for(int x=0; x < numTallas; x++){
                        for(int u=0; u < curva[y].getCantidad(x);u++){
                            Inventario uni = new Inventario();
                            uni.setProducto(estilo);
                            uni.setColor(curva[y].getColor());
                            uni.setTalla(tallas[x]);
                            uni.setMarca(marca);
                            uni.setMaterial(material);
                            uni.setGenero(genero);
                            uni.setClasepersona(clasePersona);
                            uni.setProveedor(proveedorCorte);
                            uni.setNumcaja(null);
                            uni.setProcedencia("CORTE");
                            uni.setCostounitario(new BigDecimal(costoReal));
                            uni.setCostoreal(new BigDecimal(costoReal));
                            uni.setCostoContable(new BigDecimal(costoReal));
                            uni.setPreciomayoreo(new BigDecimal(precioVenta));
                            uni.setPreciounitario(calcularPrecioDetalle(uni.getPreciomayoreo()));
                            uni.setEstadoproducto("EN EXISTENCIA");
                            uni.setNotaProducto("CORTE DE INVENTARIO INICIAL");
                            unidades.add(uni);
                        }
                    }                    
                }
                if(unidades.isEmpty()){
                    new funciones().setMsj(3,"NO HA LISTADO NINGUNA UNIDAD");
                }else{
                    unidad = unidades.get(0);
                    context.execute("codigoUnidadDialog.show();"); 
                } 
            }
            
        }
    }
    
    
    /*
    * FUNCIONES QUE CALCULAN LOS PRECIOS DE VENTA RESPECTIVAMENTE
    */
   
   public BigDecimal calcularPrecioMayoreo(BigDecimal costoReal){
       BigDecimal precioMayoreo;
       float precio=0;
       float costo;
       float factor;
       //Obtenemos el porcentaje de Utilidad y se lo sumamos
       Configuraciones configUtilidad = ejbFacadeConfig.getConfig("PRECIO VENTA", "UTILIDAD");
       if(configUtilidad != null){
           factor = 1 + configUtilidad.getValorFloat();
           costo = new funciones().redondearMas(costoReal.floatValue(),2);
           precio = costo*factor;
           Configuraciones configComisiones = ejbFacadeConfig.getConfig("PRECIO VENTA", "COMISIONES Y COSTO DE DISTRIBUCION");
           if(configComisiones != null){
               factor = 1 + configComisiones.getValorFloat();
               precio = precio*factor;
               Configuraciones configDescuentos = ejbFacadeConfig.getConfig("PRECIO VENTA", "DESCUENTO");
               if(configDescuentos != null){
                    factor = 1 + configDescuentos.getValorFloat();
                    precio = precio*factor;
               }
           }
       }
       
       precio = new funciones().redondearMas(precio,2);
       precioMayoreo = new BigDecimal(precio);
       return precioMayoreo;
   }
   
   
   public BigDecimal calcularPrecioDetalle(BigDecimal precioMayoreo){
       BigDecimal precioDetalle;
       float precio=0;
       float costo;
       float factor;
       //Obtenemos el porcentaje de Utilidad y se lo sumamos
       Configuraciones configDetalle = ejbFacadeConfig.getConfig("PRECIO VENTA", "DETALLE");
       if(configDetalle != null){
           factor = 1 + configDetalle.getValorFloat();
           costo = new funciones().redondearMas(precioMayoreo.floatValue(),2);
           precio = costo*factor;
       }
       precio = new funciones().redondearMas(precio,2);
       precioDetalle = new BigDecimal(precio);
       return precioDetalle;
   }
   
   
   /*
    * FUNCION PARA INGRESAR CAJA AL INVENTARIO
    */
   
   public void ingresarCaja(){
       RequestContext context = RequestContext.getCurrentInstance(); 
       if(facturaCorte!= null && proveedorCorte !=null){
       if(codigoBarra != null && !"".equals(codigoBarra)){
           codigoBarra = codigoBarra.toUpperCase();
           if(codigoBarra.length() <= 20){
               if(!ejbFacadeCaja.existeCodigoCaja(codigoBarra)){
                   //INGRESAR CAJA
                   //TRANSACCION
                   Transaccion transac = new Transaccion();
                   transac.setTipoTransac(0);//CORTE DE INICIO DE INVENTARIO
                   transac.setFechaTransac(new funciones().getTime());
                   transac.setHoraTransac(new funciones().getTime());
                   transac.setResponsableTransac(new JsfUtil().getEmpleado());
                   transac.setIdtransac(ejbFacadeTransac.getNextIdTransac());
                   ejbFacadeTransac.create(transac);
                   //Crear Ingreso
                    Ingreso ingresoCaja = new Ingreso();
                    ingresoCaja.setFacturaIngreso(facturaCorte);
                    ingresoCaja.setFechaRegistro(new funciones().getTime());
                    ingresoCaja.setNotaIngreso("INGRESO CAJA");
                    ingresoCaja.setIdtransac(transac);
                    ingresoCaja.setNumingreso(ejbFacadeIngreso.getNextIdIngreso());
                    ejbFacadeIngreso.create(ingresoCaja);
                   caja.setNumcaja(codigoBarra);
                   if(caja.getProducto().getPrecioventaMayoreo().compareTo(caja.getPrecioventaUnidad()) < 0){
                       caja.getProducto().setPrecioventaMayoreo(caja.getPrecioventaUnidad());
                       caja.getProducto().setPrecioventaUnidad(calcularPrecioDetalle(caja.getProducto().getPrecioventaMayoreo()));
                       ejbFacadeEstilos.edit(caja.getProducto());
                   }
                   ejbFacadeCaja.create(caja);
                   ubicacionCajaUltima = caja.getUbicacionCaja();
                   //INGRESAR EL DETALLE
                   int contador=1;
                   for(int y=0; y < numcolores;y++){
                        //CANTIDADES
                        for(int x=0; x < numTallas; x++){
                            for(int u=0; u < curva[y].getCantidad(x);u++){
                                Inventario producto = new Inventario();
                                producto.setCodigo(caja.getNumcaja()+"-"+tallas[x]+"-"+contador++);
                                producto.setProducto(caja.getProducto());
                                producto.setColor(curva[y].getColor());
                                producto.setTalla(tallas[x]);
                                producto.setMarca(marca);
                                producto.setMaterial(material);
                                producto.setGenero(genero);
                                producto.setClasepersona(clasePersona);
                                producto.setProveedor(proveedorCorte);
                                producto.setNumcaja(caja);
                                producto.setProcedencia("CORTE");
                                producto.setCostounitario(caja.getCostoUnitario());
                                producto.setCostoreal(caja.getCostoUnitarioReal());
                                producto.setCostoContable(caja.getCostoUnitarioReal());
                                producto.setPreciomayoreo(caja.getPrecioventaUnidad());
                                producto.setPreciounitario(calcularPrecioDetalle(producto.getPreciomayoreo()));
                                producto.setEstadoproducto("EN EXISTENCIA");
                                producto.setNotaProducto("CORTE DE INVENTARIO INICIAL");
                                producto.setNumingreso(ingresoCaja);
                                producto.setFechaIngreso(ingresoCaja.getFechaRegistro());
                                ejbFacadeInventario.create(producto);
                            }
                        }                    
                    }
                   caja.setNumcaja(null);
                   cajas.remove(0);
                   new funciones().setMsj(1, "CAJA COD: " + codigoBarra +" INGRESADA AL INVENTARIO");
                   codigoBarra = null;
                   if(cajas.isEmpty()){
                       new funciones().setMsj(1, "CAJAS CODIFICADAS POR COMPLETO");
                       resetearCurva();
                       context.execute("codigoCajaCodigoDialog.hide();");
                   }else{
                       context.execute("codigoCajaCodigoDialog.hide();codigoCajaCodigoDialog.show();");
                   }
               }else{
                   new funciones().setMsj(3,"Codigo YA EXISTE EN INVENTARIO");
                   context.execute("codigoCajaCodigoDialog.hide();codigoCajaCodigoDialog.show();");
                   context.execute("jqDialog = jQuery('#'+codigoCajaCodigoDialog.id);jqDialog.effect('shake', { times:3 }, 100);");
                   codigoBarra = null;
               }
           }else{
               new funciones().setMsj(3,"Codigo Invalido");
                   context.execute("codigoCajaCodigoDialog.hide();codigoCajaCodigoDialog.show();");
               context.execute("jqDialog = jQuery('#'+codigoCajaCodigoDialog.id);jqDialog.effect('shake', { times:3 }, 100);");
               codigoBarra = null;
           }
       }else{
           new funciones().setMsj(3, "Ingrese Codigo");
                   context.execute("codigoCajaCodigoDialog.hide();codigoCajaCodigoDialog.show();");
           context.execute("jqDialog = jQuery('#'+codigoCajaCodigoDialog.id);jqDialog.effect('shake', { times:3 }, 100);");
           codigoBarra = null;
       }
       }else{
            new funciones().setMsj(3, "NO SE PUEDE INGRESAR PRODUCTO CONSULTE AL ADMINISTRADOR");
            context.execute("codigoCajaCodigoDialog.hide();codigoCajaCodigoDialog.show();");
            context.execute("jqDialog = jQuery('#'+codigoCajaCodigoDialog.id);jqDialog.effect('shake', { times:3 }, 100);");
            codigoBarra = null;
        } 
   }
   
   
   
   
   
   //INGRESA UNA UNIDAD A INVENTARIO
   public void ingresarUnitario(){
       RequestContext context = RequestContext.getCurrentInstance(); 
       if(facturaCorte!= null && proveedorCorte !=null){
           //INGRESAR A INVENTARIO
           //TRANSACCION
           Transaccion transac = new Transaccion();
           transac.setTipoTransac(0);//CORTE DE INICIO DE INVENTARIO
           transac.setFechaTransac(new funciones().getTime());
           transac.setHoraTransac(new funciones().getTime());
           transac.setResponsableTransac(new JsfUtil().getEmpleado());
           transac.setIdtransac(ejbFacadeTransac.getNextIdTransac());
           ejbFacadeTransac.create(transac);
           //Crear Ingreso
            Ingreso ingresoUni = new Ingreso();
            ingresoUni.setFacturaIngreso(facturaCorte);
            ingresoUni.setFechaRegistro(new funciones().getTime());
            ingresoUni.setNotaIngreso("INGRESO UNIDAD");
            ingresoUni.setIdtransac(transac);
            ingresoUni.setNumingreso(ejbFacadeIngreso.getNextIdIngreso());
            ejbFacadeIngreso.create(ingresoUni);
            boolean salir=false;
            int numUnitarioFactura = ejbFacadeInventario.countUnitario(facturaCorte);
            int numIngresadas = 0;
            while(!salir){
                numUnitarioFactura++;
                codigoBarra = facturaCorte.getFacturaIngresoPK().getDocumentoCompra() + "-" + numUnitarioFactura;
                if(!ejbFacadeInventario.existeCodigoUnidad(codigoBarra) && codigoBarra.length() <= 20){
                        unidades.get(0).setNumingreso(ingresoUni);
                        unidades.get(0).setFechaIngreso(ingresoUni.getFechaRegistro());
                        unidades.get(0).setCodigo(codigoBarra);
                        unidades.get(0).setUbicacionProducto(ubicacionUnidad);
                        if(unidades.get(0).getProducto().getPrecioventaMayoreo().compareTo(unidades.get(0).getPreciomayoreo()) < 0){
                            unidades.get(0).getProducto().setPrecioventaMayoreo(unidades.get(0).getPreciomayoreo());
                            unidades.get(0).getProducto().setPrecioventaUnidad(calcularPrecioDetalle(unidades.get(0).getProducto().getPrecioventaMayoreo()));
                            ejbFacadeEstilos.edit(unidades.get(0).getProducto());
                        }
                        ejbFacadeInventario.create(unidades.get(0));
                        unidades.remove(0);
                        numIngresadas++;
                   }
                if(unidades.isEmpty()){
                    salir = true;
                    resetearCurva();
                    new funciones().setMsj(1,numIngresadas + " UNIDADES INGRESADAS");
                    context.execute("codigoUnidadDialog.hide();");
                }
            }
       }else{
            new funciones().setMsj(3, "NO SE PUEDE INGRESAR PRODUCTO CONSULTE AL ADMINISTRADOR");
            context.execute("codigoUnidadDialog.hide();codigoUnidadDialog.show();");
            context.execute("jqDialog = jQuery('#'+codigoUnidadDialog.id);jqDialog.effect('shake', { times:3 }, 100);");
            codigoBarra = null;
        } 
   }
   
   
   
   
   
   /////////////////////////////////////////// INVENTARIO 2016 /////////////////////////////////////////////
   
   /* IMPORTAR EXCEL */
    private UploadedFile file;
    private List<InventarioImport> importacion;
    private StreamedContent filePlantilla;
   
    
    public StreamedContent getFilePlantilla() {
        InputStream stream = ((ServletContext)FacesContext.getCurrentInstance().getExternalContext().getContext()).getResourceAsStream("/resources/plantillas/PlantillaInventario.xlsx");
        filePlantilla = new DefaultStreamedContent(stream, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "PlantillaCalzado.xlsx");
        return filePlantilla;
    }
    
    public UploadedFile getFile() {
        return file;
    }

    public List<InventarioImport> getImportacion() {
        return importacion;
    }

    public void setImportacion(List<InventarioImport> importacion) {
        this.importacion = importacion;
    }
 
    
    public void setFile(UploadedFile file) {
        this.file = file;
    }
    
    public void handleFileUpload(FileUploadEvent event) {
        file = event.getFile();
        JsfUtil.addSuccessMessage(file.getFileName() + " archivo importado");
        cargarDatos();
    }
   
   
   public void cargarDatos(){
        if(file != null) {
            try {
                importacion =new  ArrayList<InventarioImport>();
                Iterator rowIterator = null;
                int filas=0;
                int importadas=0;
                try{
                    XSSFWorkbook workBook = new XSSFWorkbook(file.getInputstream());
                    XSSFSheet hssfSheet = workBook.getSheetAt(0);
                    rowIterator = hssfSheet.rowIterator();
                }catch (IOException ex) {
                    Logger.getLogger(CorteInventarioInicial.class.getName()).log(Level.SEVERE, null, ex);
                }catch(POIXMLException ex){
                    try {
                        POIFSFileSystem fsFileSystem = new POIFSFileSystem(file.getInputstream());
                        HSSFWorkbook workBook = new HSSFWorkbook(fsFileSystem);
                        HSSFSheet hssfSheet = workBook.getSheetAt(0);
                        rowIterator = hssfSheet.rowIterator();
                    } catch (IOException e) {
                        Logger.getLogger(CorteInventarioInicial.class.getName()).log(Level.SEVERE, null, e);
                    }
                }
                rowIterator.next();
                rowIterator.next();
                rowIterator.next();
                while(rowIterator.hasNext()){
                    InventarioImport row = new InventarioImport();
                    XSSFRow hssfRow = (XSSFRow) rowIterator.next();
                    filas++;
                    try{
                        if(hssfRow.getRowNum() > 3 &&  hssfRow.getCell(0).getStringCellValue()!= null && hssfRow.getCell(0).getStringCellValue() != "")
                        {
                            row.setNUMCAJA(hssfRow.getCell(0).getStringCellValue());
                            row.setMARCA(hssfRow.getCell(1).getStringCellValue());
                            row.setESTILO(hssfRow.getCell(2).getStringCellValue());
                            row.setCOLOR(hssfRow.getCell(3).getStringCellValue());
                            row.setCURVA(hssfRow.getCell(4).getStringCellValue());
                            row.setT17((int)hssfRow.getCell(5).getNumericCellValue());
                            row.setT18((int)hssfRow.getCell(6).getNumericCellValue());
                            row.setT19((int)hssfRow.getCell(7).getNumericCellValue());
                            row.setT20((int)hssfRow.getCell(8).getNumericCellValue());
                            row.setT21((int)hssfRow.getCell(9).getNumericCellValue());
                            row.setT22((int)hssfRow.getCell(10).getNumericCellValue());
                            row.setT23((int)hssfRow.getCell(11).getNumericCellValue());
                            row.setT24((int)hssfRow.getCell(12).getNumericCellValue());
                            row.setT25((int)hssfRow.getCell(13).getNumericCellValue());
                            row.setT26((int)hssfRow.getCell(14).getNumericCellValue());
                            row.setT27((int)hssfRow.getCell(15).getNumericCellValue());
                            row.setT28((int)hssfRow.getCell(16).getNumericCellValue());
                            row.setT29((int)hssfRow.getCell(17).getNumericCellValue());
                            row.setT30((int)hssfRow.getCell(18).getNumericCellValue());
                            row.setT31((int)hssfRow.getCell(19).getNumericCellValue());
                            row.setT32((int)hssfRow.getCell(20).getNumericCellValue());
                            row.setT33((int)hssfRow.getCell(21).getNumericCellValue());
                            row.setT34((int)hssfRow.getCell(22).getNumericCellValue());
                            row.setT35((int)hssfRow.getCell(23).getNumericCellValue());
                            row.setT36((int)hssfRow.getCell(24).getNumericCellValue());
                            row.setT37((int)hssfRow.getCell(25).getNumericCellValue());
                            row.setT38((int)hssfRow.getCell(26).getNumericCellValue());
                            row.setT39((int)hssfRow.getCell(27).getNumericCellValue());
                            row.setT40((int)hssfRow.getCell(28).getNumericCellValue());
                            row.setT41((int)hssfRow.getCell(29).getNumericCellValue());
                            row.setT42((int)hssfRow.getCell(30).getNumericCellValue());
                            row.setT43((int)hssfRow.getCell(31).getNumericCellValue());
                            row.setT44((int)hssfRow.getCell(32).getNumericCellValue());
                            row.setT45((int)hssfRow.getCell(33).getNumericCellValue());
                            row.setT46((int)hssfRow.getCell(34).getNumericCellValue());
                            row.setTOTAL((int)hssfRow.getCell(35).getNumericCellValue());
                            row.setCOSTO(hssfRow.getCell(36).getNumericCellValue());
                            row.setMONTO(hssfRow.getCell(37).getNumericCellValue());

                            importacion.add(row);
                            importadas++;
                        }
                    }catch(Exception exc){
                        //JsfUtil.addErrorMessage("Error al importar garantia # "+row.getNumClave());
                        break;
                    }
                }
                JsfUtil.addSuccessMessage(importadas +" IMPORTADAS");
            }catch(Exception ex){
                Logger.getLogger(CorteInventarioInicial.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else{
            JsfUtil.addErrorMessage(" Datos no cargados");
        }
    }
    
    
       
    public void limpiarCache(){
        file = null;
        if(importacion != null){
            importacion.clear();
        }
    }
    
    
    public void importarDatos(){
        //Comprobar Datos
        try{
            for(InventarioImport fila : this.getImportacion())
            {
                importarFila(fila);
            }
        }catch(Exception ex){
            Logger.getLogger(CorteInventarioInicial.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public void importarFila(InventarioImport row)
    {
        Caja box = new Caja();
        //CAJA
        if(!ejbFacadeCaja.existeCodigoCaja(row.getNUMCAJA()))
        {
            box.setNumcaja(row.getNUMCAJA());
            box.setProducto(ejbFacadeEstilos.find(row.getESTILO()));
            box.setColoresCaja(row.getCOLOR());
            box.setDetalleCaja(row.getCURVA());
            box.setUnidadesCaja(row.getTOTAL());
            box.setCostoCaja(new BigDecimal(row.getCOSTO()*row.getTOTAL()));
            box.setCostoUnitario(BigDecimal.ZERO);
            box.setCostoUnitarioReal(BigDecimal.ZERO);
            box.setPrecioventaUnidad(BigDecimal.ZERO);
            box.setUbicacionCaja("INV 2016");
            box.setCompleta(true);
            ejbFacadeCaja.create(box);
        }else{
            box = ejbFacadeCaja.getCaja(row.getNUMCAJA());
            box.setUnidadesCaja(box.getUnidadesCaja() + row.getTOTAL());
            box.setCostoCaja(new BigDecimal(box.getCostoCaja().doubleValue() + row.getCOSTO()*row.getTOTAL()));
            ejbFacadeCaja.edit(box);
        }
        //INVENTARIO
        if(row.getCURVA() == "A"){
            if(row.getT17() > 0){
                for(int i=1;i<=row.getT17(); i++){
                    Inventario item = new Inventario();
                    item.setCodigo(row.getNUMCAJA() + "-17-" +i);
                    item.setProducto(box.getProducto());
                    item.setColor(row.getCOLOR());
                    item.setTalla("17");
                    item.setMarca(row.getMARCA());
                    item.setMaterial("SINTETIC");
                    item.setGenero('M');
                    item.setClasepersona('A');
                    item.setProveedor(ejbFacadeProveedor.find(0));
                    item.setNumcaja(box);
                    item.setProcedencia("CORTE");
                    item.setCostounitario(new BigDecimal(row.getCOSTO()));
                    item.setCostoContable(new BigDecimal(row.getCOSTO()));
                    item.setCostoreal(new BigDecimal(row.getCOSTO()));
                    item.setPreciomayoreo(BigDecimal.ZERO);
                    item.setPreciounitario(BigDecimal.ZERO);
                    item.setEstadoproducto("EN EXISTENCIA");
                    item.setNumingreso(ejbFacadeIngreso.find(1449));
                    item.setFechaIngreso(new funciones().getTime());
                    item.setNotaProducto("CORTE 2016");
                    item.setUbicacionProducto("01");
                    
                }
            }
        }else if(row.getCURVA() == "B"){
            
        }else if(row.getCURVA() == "C"){
        
        }else if(row.getCURVA() == "D"){
            
        }else{
            
        }
       
        
    }
        
      
    
    
    
    
}