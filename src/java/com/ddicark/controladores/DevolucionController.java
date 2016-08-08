package com.ddicark.controladores;

import com.ddicark.auxiliares.JsfUtil;
import com.ddicark.auxiliares.funciones;
import com.ddicark.entidades.Caja;
import com.ddicark.entidades.Configuraciones;
import com.ddicark.entidades.DetalleEnvio;
import com.ddicark.entidades.DetalleEnvioPK;
import com.ddicark.entidades.DetalleFactura;
import com.ddicark.entidades.Devolucion;
import com.ddicark.entidades.DevolucionFacturas;
import com.ddicark.entidades.Factura;
import com.ddicark.entidades.FacturaPK;
import com.ddicark.entidades.Inventario;
import com.ddicark.entidades.PagoVenta;
import com.ddicark.entidades.Transaccion;
import com.ddicark.jpaFacades.CajaFacade;
import com.ddicark.jpaFacades.ConfiguracionesFacade;
import com.ddicark.jpaFacades.DetalleEnvioFacade;
import com.ddicark.jpaFacades.DetalleFacturaFacade;
import com.ddicark.jpaFacades.DevolucionFacade;
import com.ddicark.jpaFacades.DevolucionFacturasFacade;
import com.ddicark.jpaFacades.FacturaFacade;
import com.ddicark.jpaFacades.InventarioFacade;
import com.ddicark.jpaFacades.PagoVentaFacade;
import com.ddicark.jpaFacades.TransaccionFacade;
import com.ddicark.jpaFacades.VentaFacade;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FlowEvent;

@ManagedBean(name = "devolucionController")
@SessionScoped
public class DevolucionController extends AbstractController<Devolucion>  implements Serializable {

    @EJB
    private ConfiguracionesFacade ejbFacadeConfig;
    
    @EJB
    private DevolucionFacade ejbFacadeDevolucion;
    
    @EJB
    private DevolucionFacturasFacade ejbFacadeDevolucionFacturas;
    
    @EJB
    private FacturaFacade ejbFacadeFactura;
    
    @EJB
    private VentaFacade ejbFacadeVenta;
    
    @EJB
    private DetalleFacturaFacade ejbFacadeDetalleFactura;
    
    @EJB
    private InventarioFacade ejbFacadeInventario;
    
    @EJB
    private CajaFacade ejbFacadeCaja;
    
    @EJB
    private DetalleEnvioFacade ejbFacadeDetalleEnvio;
    
    @EJB
    private TransaccionFacade ejbFacadeTransac;
    
    @EJB
    private PagoVentaFacade ejbFacadePagoVenta;
    
    
     public DevolucionController() {
        super(Devolucion.class);
    }
    //Inicializa controlador de persistencia de proveedores
    @PostConstruct
    public void init() {
        super.setFacade(ejbFacadeDevolucion);
    }
    
    
    //VARIABLES DE CONTROL
    
    private List<Devolucion> registradas;
    private List<DevolucionFacturas> aplicadasAFacturas;
    private List<Devolucion> listaDevoluciones;
    
    private Factura factura;  //Factura a la que se le aplica el cambio o devolucion
    private String numFactura;
    private Date fechaFactura;
    private DetalleFactura df;
    private DetalleEnvio de;
    private List<DetalleEnvio> cambios = new ArrayList<DetalleEnvio>();
    private String codigoCambio;
    private Inventario cambioUnidad;
    private Caja cambioCaja;
    private boolean unidadCambioLista = false;
    private String nuevoEstado;
    
    private List<DetalleEnvio> devoluciones = new ArrayList<DetalleEnvio>();
    private Devolucion nuevaDevolucion;
    
    
    private Devolucion aplicarDevolucion;
    private Factura aplicarFactura;
    private float devolucionAplicar;
    
    
    private List<DetalleEnvio> selectAnulados = new ArrayList<DetalleEnvio>();
    private boolean agregar;
    private boolean selectAll;
    private String notaAnulacion;
    private Factura nuevaFactura;
    private List<DetalleFactura> detalleNuevaFactura = new ArrayList<DetalleFactura>();
    private Configuraciones IVA ;
    private boolean regresar = false;
    private float nuevoPrecio;
    
    
    public Factura imprimirFactura;
    
    //GETTERS AND SETTERS
    public Factura getFactura() {
        return factura;
    }

    public void setFactura(Factura factura) {
        this.factura = factura;
    }

    public String getNumFactura() {
        return numFactura;
    }

    public void setNumFactura(String numFactura) {
        this.numFactura = numFactura;
    }

    public Date getFechaFactura() {
        return fechaFactura;
    }

    public void setFechaFactura(Date fechaFactura) {
        this.fechaFactura = fechaFactura;
    }

    public DetalleFactura getDf() {
        return df;
    }

    public void setDf(DetalleFactura df) {
        this.df = df;
    }

    public List<DetalleEnvio> getCambios() {
        return cambios;
    }

    public void setCambios(List<DetalleEnvio> cambios) {
        this.cambios = cambios;
    }

    public DetalleEnvio getDe() {
        return de;
    }

    public void setDe(DetalleEnvio de) {
        this.de = de;
    }

    public String getCodigoCambio() {
        return codigoCambio;
    }

    public void setCodigoCambio(String codigoCambio) {
        this.codigoCambio = codigoCambio;
    }

    public Inventario getCambioUnidad() {
        return cambioUnidad;
    }

    public void setCambioUnidad(Inventario cambioUnidad) {
        this.cambioUnidad = cambioUnidad;
    }

    public Caja getCambioCaja() {
        return cambioCaja;
    }

    public void setCambioCaja(Caja cambioCaja) {
        this.cambioCaja = cambioCaja;
    }

    public boolean isUnidadCambioLista() {
        return unidadCambioLista;
    }

    public void setUnidadCambioLista(boolean unidadCambioLista) {
        this.unidadCambioLista = unidadCambioLista;
    }

    public String getNuevoEstado() {
        return nuevoEstado;
    }

    public void setNuevoEstado(String nuevoEstado) {
        this.nuevoEstado = nuevoEstado;
    }

    public List<DetalleEnvio> getDevoluciones() {
        return devoluciones;
    }

    public void setDevoluciones(List<DetalleEnvio> devoluciones) {
        this.devoluciones = devoluciones;
    }

    public Devolucion getNuevaDevolucion() {
        return nuevaDevolucion;
    }

    public void setNuevaDevolucion(Devolucion nuevaDevolucion) {
        this.nuevaDevolucion = nuevaDevolucion;
    }

    public Devolucion getAplicarDevolucion() {
        return aplicarDevolucion;
    }

    public void setAplicarDevolucion(Devolucion aplicarDevolucion) {
        this.aplicarDevolucion = aplicarDevolucion;
    }

    public Factura getAplicarFactura() {
        return aplicarFactura;
    }

    public void setAplicarFactura(Factura aplicarFactura) {
        this.aplicarFactura = aplicarFactura;
    }

    public List<Devolucion> getRegistradas() {
        registradas = ejbFacadeDevolucion.devolucionesRegistradas();
        return registradas;
    }

    public float getDevolucionAplicar() {
        return devolucionAplicar;
    }

    public void setDevolucionAplicar(float devolucionAplicar) {
        this.devolucionAplicar = devolucionAplicar;
    }

    public List<DetalleEnvio> getSelectAnulados() {
        return selectAnulados;
    }

    public void setSelectAnulados(List<DetalleEnvio> selectAnulados) {
        this.selectAnulados = selectAnulados;
    }

    public boolean isAgregar() {
        return agregar;
    }

    public void setAgregar(boolean agregar) {
        this.agregar = agregar;
    }

    public boolean isSelectAll() {
        return selectAll;
    }

    public void setSelectAll(boolean selectAll) {
        this.selectAll = selectAll;
    }

    public String getNotaAnulacion() {
        return notaAnulacion;
    }

    public void setNotaAnulacion(String notaAnulacion) {
        this.notaAnulacion = notaAnulacion;
    }

    public Factura getNuevaFactura() {
        return nuevaFactura;
    }

    public void setNuevaFactura(Factura nuevaFactura) {
        this.nuevaFactura = nuevaFactura;
    }
    
    public void getIVA() {
        IVA = ejbFacadeConfig.getConfig("GLOBAL", "IVA");
        if(IVA == null){
            IVA = new Configuraciones();
            IVA.setValorFloat((float)0.13);
        }
    }

    public List<DetalleFactura> getDetalleNuevaFactura() {
        return detalleNuevaFactura;
    }

    public void setDetalleNuevaFactura(List<DetalleFactura> detalleNuevaFactura) {
        this.detalleNuevaFactura = detalleNuevaFactura;
    }

    public float getNuevoPrecio() {
        return nuevoPrecio;
    }

    public void setNuevoPrecio(float nuevoPrecio) {
        this.nuevoPrecio = nuevoPrecio;
    }

    public List<DevolucionFacturas> getAplicadasAFacturas(Devolucion devolucion) {
        aplicadasAFacturas = ejbFacadeDevolucionFacturas.devolucionAplicada(devolucion);
        return aplicadasAFacturas;
    }

    public List<Devolucion> getListaDevoluciones() {
        listaDevoluciones = ejbFacadeDevolucion.findAll();
        return listaDevoluciones;
    }

    public Factura getImprimirFactura() {
        return imprimirFactura;
    }

    public void setImprimirFactura(Factura imprimirFactura) {
        this.imprimirFactura = imprimirFactura;
    }
    
    
    
    
    
    
    
    /*
     * FUNCIONES PARA CAMBIOS
     */
    
    //Funcion que prepara un CAMBIO
    
    public void prepararCambio(){
        numFactura = null;
        fechaFactura = null;
        factura = null;
        cambios = new ArrayList<DetalleEnvio>();
        unidadCambioLista = false;
        cambioCaja=null;
        cambioUnidad = null;
    }
    
    
    //FUNCION QUE BUSCA Y CARGA DETALLE Y DATOS DE LA FACTURA
    public void buscarFactura(){
        FacturaPK clave = new FacturaPK(numFactura,fechaFactura);
        factura = ejbFacadeFactura.find(clave);
        if(factura != null){
            if(factura.getEstado().equals("ANULADA")){
                new funciones().setMsj(3,"FACTURA ANULADA");
                cambios.clear();
                factura = null;
            }else{
               new funciones().setMsj(1,"FACTURA CARGADA");
                cambios.clear();
                numFactura = null;
                fechaFactura = null;
            }
        }else{
            new funciones().setMsj(3,"FACTURA NO ENCONTRADA");
            cambios.clear();
        }
    }
    
    //FUNCION QUE PREPARA UNA CAMBIO DE UN PRODUCTO
    public void cambioProducto(DetalleEnvio de){
        codigoCambio = null;
        unidadCambioLista = false;
        RequestContext context = RequestContext.getCurrentInstance();
        this.de = new DetalleEnvio(new DetalleEnvioPK());
        if(!deListado(de)){
            if(de.getNota() == null || de.getNota().equals("CAMBIO")  || de.getNota().equals("REFACTURADO")){
                this.de  = de;
                context.addCallbackParam("mostrar", true);//MUESTRA DIALOGO DE CAMBIO
            }else{
                new funciones().setMsj(3,"PRODUCTO YA DEVUELTO");
            }
        }else{
            new funciones().setMsj(3,"PRODUCTO YA ESTA EN LISTA DE CAMBIOS");
            context.addCallbackParam("mostrar", false); //NO MUESTRA DIALOGO DE CAMBIO
        }
    }
    
    //FUNCION QUE VERIFICA QUE EL PRODUCTO NO ESTE EN LISTA DE CAMBIOS
    public boolean deListado(DetalleEnvio de){
        boolean r = false;
        for(DetalleEnvio actual : cambios){
            if(actual.getDetalleEnvioPK().getCodigoProducto().equals(de.getDetalleEnvioPK().getCodigoProducto()) && (actual.getDetalleEnvioPK().getNumenvio() == de.getDetalleEnvioPK().getNumenvio())){
                r = true;
            }
        }
        return r;
    }
    
    
    //BUSCA CAMBIO EN INVENTARIO
    public void buscarCambio(){
        RequestContext context = RequestContext.getCurrentInstance();
        cambioCaja = null;
        cambioUnidad = null;
        if(codigoCambio != null && !"".equals(codigoCambio)){
            cambioUnidad = ejbFacadeInventario.find(codigoCambio);
            if(cambioUnidad != null){
                //Unidad encontrada
                context.addCallbackParam("validar", true);
                context.addCallbackParam("caja", false);
            }else{
                cambioCaja =  ejbFacadeCaja.find(codigoCambio);
                if(cambioCaja != null){
                    //caja encontrada mostrar detalle caja
                    context.addCallbackParam("validar", true);
                    context.addCallbackParam("caja", true);
                }else{
                    //No existe producto con el codigo ingresado
                    new funciones().setMsj(3, "NO EXISTE PRODUCTO CON EL CODIGO INGRESADO");
                    context.addCallbackParam("validar", false);
                }
            }
            codigoCambio = null;
        }
    }
    
    //VERIFICA QUE EL ARTICULO NO HAYA SIDO DADO DE CAMBIO EN LA OPERACION ACTUAL
    public boolean enListaCambios(){
        for(DetalleEnvio actual : cambios){
            if(actual.getCambioProducto() != null){
                if(actual.getCambioProducto().getCodigo().equals(cambioUnidad.getCodigo())){
                    return true;
                }
            }
        }
        return false;
    }
    
    //FUNCION QUE PREPARA EL CAMBIO
    public void cambiarUnidad(){
        RequestContext context = RequestContext.getCurrentInstance();
        if(cambioUnidad != null){
            if(cambioUnidad.getEstadoproducto().equals("EN EXISTENCIA")){
                if(enListaCambios()){
                    new funciones().setMsj(3,"ARTICULO YA FUE LISTADO EN UN CAMBIO");
                    context.addCallbackParam("validar", false);
                    unidadCambioLista = false;
                }else{
                    if(cambioUnidad.getProducto().equals(de.getInventario().getProducto())){
                        if(cambioUnidad.getPreciomayoreo().compareTo(de.getInventario().getPreciomayoreo()) == 0){
                            de.setCambioProducto(cambioUnidad);
                            new funciones().setMsj(1,"ARTICULO LISTO PARA CAMBIAR");
                            context.addCallbackParam("validar", true);
                            unidadCambioLista = true;
                        }else{
                            //Diferente precio
                            new funciones().setMsj(2,"EL CAMBIO DEBE SER POR UN PRODUCTO DEL MISMO PRECIO");
                            context.addCallbackParam("validar", false);
                            unidadCambioLista = false;
                        }
                    }else{
                        //Diferente estilo
                        new funciones().setMsj(2,"EL CAMBIO DEBE SER POR UN PRODUCTO DEL MISMO ESTILO");
                        context.addCallbackParam("validar", false);
                        unidadCambioLista = false;
                    }
                }
            }else{
                //Producto no disponible
                new funciones().setMsj(2,"NO DISPONIBLE ESTADO ARTICULO: " + cambioUnidad.getEstadoproducto());
                context.addCallbackParam("validar", false);
                unidadCambioLista = false;
            }
        }
    }
    
    //FUNCION QUE LISTA EL CAMBIO
    public void listarCambio(){
        RequestContext context = RequestContext.getCurrentInstance();
        if(de.getCambioProducto() != null){
            de.getInventario().setEstadoproducto(nuevoEstado);
            cambios.add(de);
            new funciones().setMsj(1, "CAMBIO AÑADIDO A LISTA");
            context.addCallbackParam("validar", true); 
        }else{
            new funciones().setMsj(3, "SELECCIONE PRODUCTO A ENVIAR");
            context.addCallbackParam("validar", false); 
        }
    }
    
    public void resetCambio(){
        de.setCambioProducto(null);
        unidadCambioLista =false;
    }

    //FUNCION QUE PREPARA EL CAMBIO
    public void cambiarUnidadCaja(Inventario cambio){
        cambioUnidad = cambio;
        RequestContext context = RequestContext.getCurrentInstance();
        if(cambioUnidad != null){
            if(cambioUnidad.getEstadoproducto().equals("EN EXISTENCIA")){
                if(enListaCambios()){
                    new funciones().setMsj(3,"ARTICULO YA FUE LISTADO EN UN CAMBIO");
                    context.addCallbackParam("validar", false);
                    unidadCambioLista = false;
                }else{
                    if(cambioUnidad.getProducto().equals(de.getInventario().getProducto())){
                        if(cambioUnidad.getPreciomayoreo().compareTo(de.getInventario().getPreciomayoreo()) == 0){
                            de.setCambioProducto(cambioUnidad);
                            new funciones().setMsj(1,"ARTICULO LISTO PARA CAMBIAR");
                            context.addCallbackParam("validar", true);
                            unidadCambioLista = true;
                        }else{
                            //Diferente precio
                            new funciones().setMsj(2,"EL CAMBIO DEBE SER POR UN PRODUCTO DEL MISMO PRECIO");
                            context.addCallbackParam("validar", false);
                            unidadCambioLista = false;
                        }
                    }else{
                        //Diferente estilo
                        new funciones().setMsj(2,"EL CAMBIO DEBE SER POR UN PRODUCTO DEL MISMO ESTILO");
                        context.addCallbackParam("validar", false);
                        unidadCambioLista = false;
                    }

                }
            }else{
                //Producto no disponible
                new funciones().setMsj(2,"NO DISPONIBLE ESTADO ARTICULO: " + cambioUnidad.getEstadoproducto());
                context.addCallbackParam("validar", false);
                unidadCambioLista = false;
            }
        }
    }
    
    
    //Preparar listar cambio
    public void prepararListarCambio(){
        RequestContext context = RequestContext.getCurrentInstance();
        nuevoEstado = "";
        if(de.getCambioProducto() != null){
            context.addCallbackParam("mostrar", true);
        }else{
            new funciones().setMsj(3, "SELECCIONE EL PRODUCTO A ENVIAR");
            context.addCallbackParam("mostrar", false);
        }
    }
    
    public void limpiarListaCambios(){
        cambios.clear();
        new funciones().setMsj(1, "Lista Vacia");
    }
    
    
    /*
     * GUARDA Y CONFIRMA LOS CAMBIOS DE LA FACTURA
     */
    public void guardarListaCambios(){
        try{
            if(cambios.isEmpty()){
                new funciones().setMsj(3, "NO HA LISTADO NINGUN CAMBIO");
            }else{
                for(DetalleEnvio actual: cambios){
                    //Actualizar INVENTARIO
                    actual.getInventario().setFechaReingreso(new funciones().getTime());
                    ejbFacadeInventario.edit(actual.getInventario());
                    actual.getCambioProducto().setEstadoproducto("VENDIDO");
                    actual.getCambioProducto().setNotaProducto("ENVIADO COMO CAMBIO");
                    ejbFacadeInventario.edit(actual.getCambioProducto());
                    if(actual.getCambioProducto().getNumcaja() != null){
                        if(actual.getCambioProducto().getNumcaja().getCompleta()){
                            actual.getCambioProducto().getNumcaja().setCompleta(false);
                            ejbFacadeCaja.edit(actual.getCambioProducto().getNumcaja());
                        }
                    }
                    //Actualizar Detalle Envio
                    actual.setNota("CAMBIO");
                    ejbFacadeDetalleEnvio.edit(actual);
                    ejbFacadeDetalleEnvio.updateCodigosCambio(actual);
                }
                cambios.clear();
                new funciones().setMsj(1,"CAMBIOS REGISTRADOS CORRECTAMENTE");
                prepararCambio();
            }
            
        }catch(Exception e){
            new funciones().setMsj(3,"ERROR AL GUARDAR CAMBIOS EN LA BD");
        }
           
    }
    
    
    
    
    /*
     * FUNCIONES PARA LAS DEVOLUCIONES
     */
    
    
    //Funcion que prepara una DEVOLUCION 
    
    public void prepararDevolucion(){
        numFactura = null;
        fechaFactura = null;
        factura = null;
        devoluciones = new ArrayList<DetalleEnvio>();
        nuevaDevolucion = new Devolucion();
    }
    
    
    //FUNCION QUE BUSCA Y CARGA DETALLE Y DATOS DE LA FACTURA
    public void buscarFacturaDevolucion(){
        FacturaPK clave = new FacturaPK(numFactura,fechaFactura);
        factura = ejbFacadeFactura.find(clave);
        if(factura != null){
            if(factura.getEstado().equals("ANULADA")){
                new funciones().setMsj(3,"FACTURA ANULADA");
                devoluciones.clear();
                nuevaDevolucion = new Devolucion();
                factura = null;
            }else{
                new funciones().setMsj(1,"FACTURA CARGADA");
                devoluciones.clear();
                numFactura = null;
                fechaFactura = null;
                nuevaDevolucion = new Devolucion();
            }
            
        }else{
            new funciones().setMsj(3,"FACTURA NO ENCONTRADA");
            devoluciones.clear();
            nuevaDevolucion = new Devolucion();
        }
    }
    
    //CALCULA EL MONTO DE DEVOLUCION DE UN PRODUCTO A PARTIR DEL PRECIO FACTURADO
    public BigDecimal calcularDevolucionProducto(DetalleEnvio de){
        BigDecimal devolucion = BigDecimal.ZERO;
        if(de != null && de.getLineaVenta() != null){
            Configuraciones IVA = ejbFacadeConfig.getConfig("GLOBAL", "IVA");
            float iva= (float)0.00;
            if(IVA == null){
                iva = (float)1.13;
            }else{
                iva = 1 + IVA.getValorFloat();
            }
            //MENOS DESCUENTO
            //float descuento = de.getLineaVenta().getFactura().getNumventa().getDescuentoVenta()/100;
            //descuento = de.getPrecioFacturar().floatValue()*descuento;
            devolucion = de.getPrecioFacturar();//.subtract(new BigDecimal(descuento));
            //PRECIO + IVA
            devolucion = new BigDecimal(devolucion.floatValue()*iva);
            devolucion = new BigDecimal(new funciones().redondearMas(devolucion.floatValue(), 2));
        }
        
        return devolucion;
    }
    
    //FUNCION QUE PREPARA UNA DEVOLUCION DE UN PRODUCTO
    public void devolucionProducto(DetalleEnvio de){
        RequestContext context = RequestContext.getCurrentInstance();
        this.de = new DetalleEnvio(new DetalleEnvioPK());
        if(!deDevolucionListado(de)){
            if(de.getNota()==null || de.getNota().equals("CAMBIO") || de.getNota().equals("REFACTURADO")){
                this.de  = de;
                context.addCallbackParam("mostrar", true);//MUESTRA DIALOGO DE DEVOLUCION
            }else{
                new funciones().setMsj(3,"PRODUCTO YA FUE DEVUELTO ANTERIORMENTE");
            context.addCallbackParam("mostrar", false); //NO MUESTRA DIALOGO DE DEVOLUCION
            }
        }else{
            new funciones().setMsj(3,"PRODUCTO YA ESTA EN LISTA DE CAMBIOS");
            context.addCallbackParam("mostrar", false); //NO MUESTRA DIALOGO DE DEVOLUCION
        }
    }
    
     //FUNCION QUE VERIFICA QUE EL PRODUCTO NO ESTE EN LISTA DE DEVOLUCIONES
    public boolean deDevolucionListado(DetalleEnvio de){
        boolean r = false;
        for(DetalleEnvio actual : devoluciones){
            if(actual.getDetalleEnvioPK().getCodigoProducto().equals(de.getDetalleEnvioPK().getCodigoProducto()) && (actual.getDetalleEnvioPK().getNumenvio() == de.getDetalleEnvioPK().getNumenvio())){
                r = true;
            }
        }
        return r;
    }
    
    //FUNCION QUE LISTA UN ARTICULO EN DEVOLUCIONES
    public void listarDevolucion(){
        RequestContext context = RequestContext.getCurrentInstance();
        if(de.getNota() == null || de.getNota().equals("CAMBIO")  || de.getNota().equals("REFACTURADO")){
            devoluciones.add(de);
            calcularTotalDevolucion();
            new funciones().setMsj(1, "PRODUCTO AGREGADO A LISTA DE DEVOLUCION");
            context.addCallbackParam("validar", true); 
        }else{
            new funciones().setMsj(3, "PRODUCTO YA FUE PROCESADO EN OTRA DEVOLUCION");
            context.addCallbackParam("validar", false); 
        }
    }
    
    //CALCULA EL TOTAL DE LA DEVOLUCION
    public void calcularTotalDevolucion(){
        nuevaDevolucion.setMontoDevolucion(BigDecimal.ZERO);
        //nuevaDevolucion.setDescuentoDevolucion(new BigDecimal(factura.getNumventa().getDescuentoVenta()));
        nuevaDevolucion.setDescuentoDevolucion(BigDecimal.ZERO);
        nuevaDevolucion.setTotalDevolucion(BigDecimal.ZERO);
        if(devoluciones != null){
            for(DetalleEnvio actual : devoluciones){
                nuevaDevolucion.setMontoDevolucion(nuevaDevolucion.getMontoDevolucion().add(actual.getPrecioFacturar()));
                nuevaDevolucion.setTotalDevolucion(nuevaDevolucion.getTotalDevolucion().add(calcularDevolucionProducto(actual)));
            }
            nuevaDevolucion.setUnidadesDevolucion(devoluciones.size());
            nuevaDevolucion.setMontoDevolucion(new BigDecimal(new funciones().redondearMas(nuevaDevolucion.getMontoDevolucion().floatValue(),2)));
            nuevaDevolucion.setTotalDevolucion(new BigDecimal(new funciones().redondearMas(nuevaDevolucion.getTotalDevolucion().floatValue(),2)));
        }
    }
    
    //Limpia lista devoluciones
    public void limpiarListaDevoluciones(){
        devoluciones.clear();
        new funciones().setMsj(1, "Lista Vacia");
        nuevaDevolucion = new Devolucion();
    }
    
    
    /*
     * GUARDA Y CONFIRMA DEVOLUCION DE LA FACTURA
     */
    public void registrarDevolucion(){
        try{
            if(devoluciones.isEmpty()){
                new funciones().setMsj(3, "NO HA LISTADO NINGUNA DEVOLUCION");
            }else if(nuevaDevolucion != null){
                //GUARDAR DEVOLUCION
                nuevaDevolucion.setFactura(factura);
                nuevaDevolucion.setFechaDevolucion(new funciones().getTime());
                nuevaDevolucion.setResponsable(new JsfUtil().getEmpleado());
                nuevaDevolucion.setSaldoDevolucion(nuevaDevolucion.getTotalDevolucion());
                nuevaDevolucion.setEstadoDevolucion("REGISTRADA");
                nuevaDevolucion.setNumdevolucion(ejbFacadeDevolucion.getNextIdDevolucion());
                ejbFacadeDevolucion.create(nuevaDevolucion);
                //Actualizar Articulos
                for(DetalleEnvio actual: devoluciones){
                    //Hacer cambio en productos
                    actual.setNota("REINGRESO");
                    ejbFacadeDetalleEnvio.edit(actual);
                }
                devoluciones.clear();
                new funciones().setMsj(1,"DEVOLUCION REGISTRADA CORRECTAMENTE");
                prepararDevolucion();
            }else{
                new funciones().setMsj(3,"ERROR NULLpointer AL PROCESAR NUEVA DEVOLUCION");
            }
            
        }catch(Exception e){
            new funciones().setMsj(3,"ERROR AL GUARDAR DEVOLUCION EN LA BD");
        }
           
    }
    
    
    
 
    
    
    
    
    /*
     * FUNCIONES PARA APLICAR DEVOLUCION A UNA FACTURA
     */
    
    //Prepara para aplicar otra devolucion
    public void prepararAplicarDevolucion(){
        aplicarDevolucion = null;
        aplicarFactura = null;
        devolucionAplicar = (float)0.00;
    }
    
    //Prepara para realizar el pago a una factura
    
    public void prepararPagoDevolucion(){
        devolucionAplicar = (float)0.00;
        if(aplicarFactura != null && aplicarDevolucion != null){
            
        }
    }
    
    
    public void validarMontoDevolucionAplicado(){
        RequestContext context = RequestContext.getCurrentInstance();
        if(devolucionAplicar > 0){
            if(devolucionAplicar <= aplicarDevolucion.getSaldoDevolucion().floatValue()){
                if(devolucionAplicar <= aplicarFactura.getSaldo().floatValue()){
                    context.addCallbackParam("mostrar", true);
                }else{
                    new funciones().setMsj(3,"Monto a Aplicar Mayor que el Saldo de la Factura");
                    context.addCallbackParam("mostrar", false);
                }
            }else{
                new funciones().setMsj(3,"Monto a Aplicar Mayor que el monto de la Devolucion");
                context.addCallbackParam("mostrar", false);
            }
        }else{
            new funciones().setMsj(3,"Monto a Aplicar Invalido debe ser > 0");
            context.addCallbackParam("mostrar", false);
        }
    }
    
    
    
        
    public void confirmarAplicarDevolucion(){
        if(aplicarFactura != null){
            //registrar Pago
           try{
               //NUEVA TRANSACCION
                Transaccion transac = new Transaccion();
                transac.setFechaTransac(new funciones().getTime());
                transac.setHoraTransac(new funciones().getTime());
                transac.setResponsableTransac(new JsfUtil().getEmpleado());
                transac.setTipoTransac(5); //REGISTRO DE DEVOLUCION A FACTURA
                transac.setIdtransac(ejbFacadeTransac.getNextIdTransac());
                ejbFacadeTransac.create(transac);
                //REGISTRAR PAGO
                PagoVenta pagoVenta = new PagoVenta();
                pagoVenta.setIdtransac(transac);
                pagoVenta.setFactura(aplicarFactura);
                pagoVenta.setTipoPago("DEVOLUCION");
                pagoVenta.setEstado("RECIBIDO");
                pagoVenta.setFechaPago(new funciones().getTime());
                pagoVenta.setInteres(BigDecimal.ZERO);
                pagoVenta.setMora(BigDecimal.ZERO);
                pagoVenta.setDescuento(BigDecimal.ZERO);
                pagoVenta.setAbono(new BigDecimal(devolucionAplicar));
                BigDecimal total = pagoVenta.getAbono().add(pagoVenta.getInteres()).add(pagoVenta.getMora());
                pagoVenta.setTotalPago(new BigDecimal(new funciones().redondearMas(total.floatValue(), 2)));
                pagoVenta.setDetallePago("DEVOLUCION DE "+aplicarDevolucion.getFactura().getTipoFactura()+"-"+ aplicarDevolucion.getFactura().getFacturaPK().getNumfactura());
                pagoVenta.setIdpago(ejbFacadePagoVenta.getNextIdPagoVenta());
                ejbFacadePagoVenta.create(pagoVenta); // Registrar Pago en la BD
                aplicarFactura.getPagoVentaCollection().add(pagoVenta); //Actualizar Contexto
                //Actualizar Factura
                BigDecimal saldo = aplicarFactura.getSaldo().subtract(pagoVenta.getAbono());
                aplicarFactura.setSaldo(new BigDecimal(new funciones().redondearMas(saldo.floatValue(), 2)));
                if(aplicarFactura.getSaldo().compareTo(BigDecimal.ZERO)==0){
                    aplicarFactura.setEstado("CANCELADA");
                    aplicarFactura.setFechaCancelado(transac.getFechaTransac());
                    new funciones().setMsj(1, "FACTURA CANCELADA COMPLETAMENTE");
                }
                ejbFacadeFactura.edit(aplicarFactura);
                new funciones().setMsj(1, "DEVOLUCION APLICADA CORRECTAMENTE A FACTURA " + aplicarFactura.getFacturaPK().getNumfactura());
                //ACTUALIZAR DEVOLUCION
                BigDecimal saldoDevolucion = aplicarDevolucion.getSaldoDevolucion().subtract(pagoVenta.getAbono());
                aplicarDevolucion.setSaldoDevolucion(new BigDecimal(new funciones().redondearMas(saldoDevolucion.floatValue(), 2)));
                if(aplicarDevolucion.getSaldoDevolucion().compareTo(BigDecimal.ZERO)==0){
                    aplicarDevolucion.setEstadoDevolucion("APLICADA");
                    new funciones().setMsj(1, "DEVOLUCION APLICADA POR COMPLETO");
                }
                ejbFacadeDevolucion.edit(aplicarDevolucion);
                //Actualizar lista de facturas en la que ha sido aplicada la devolucion
                DevolucionFacturas listaFacturas = new DevolucionFacturas();
                listaFacturas.setDevolucion(aplicarDevolucion);
                listaFacturas.setFactura(aplicarFactura);
                listaFacturas.setId(ejbFacadeDevolucionFacturas.getNextIdDevolucionFacturas());
                ejbFacadeDevolucionFacturas.create(listaFacturas);
                aplicarFactura.getDevolucionFacturasCollection().add(listaFacturas);
                aplicarDevolucion.getDevolucionFacturasCollection().add(listaFacturas);
                prepararAplicarDevolucion();
           }catch(Exception e){
               new funciones().setMsj(3, "ERROR AL APLICAR DEVOLUCION : "+ e.getMessage());
           }
        }
    }
    
    
    
    
    
    
    
    
    
    
    /*
     * FUNCIONES PARA ANULACIONES
     */
    public void prepararAnulacion(){
         //if(regresar){
         //   regresar = false;
       // }else{
            factura= null;
            nuevaFactura = null;
            detalleNuevaFactura= new ArrayList<DetalleFactura>();
            selectAnulados.clear();
       // }
    }
    
    
     //FUNCION QUE BUSCA Y CARGA DETALLE Y DATOS DE LA FACTURA
    public void buscarFacturaAnulacion(){
        FacturaPK clave = new FacturaPK(numFactura,fechaFactura);
        factura = ejbFacadeFactura.find(clave);
        if(factura != null){
            new funciones().setMsj(1,"FACTURA CARGADA");
            selectAnulados.clear();
            numFactura = null;
            fechaFactura = null;
            agregar = false;
            selectAll = false;
            selectAnulados.clear();
            new funciones().setMsj(1, "FACTURA " + factura.getEstado());
        }else{
            new funciones().setMsj(3,"FACTURA NO ENCONTRADA");
            selectAnulados.clear();
            agregar = false;
            selectAll = false;
            selectAnulados.clear();
        }
    }
    
    
    public void prepararAnular(){
        RequestContext context = RequestContext.getCurrentInstance();
        if(factura != null){
            if(!(new funciones().esAntes(factura.getFacturaPK().getFechaFactura(),new funciones().getTime())&& (new JsfUtil().getEmpleado().getPuestoEmpleado().equals("BODEGA") || new JsfUtil().getEmpleado().getPuestoEmpleado().equals("VENDEDOR")))){
                if(!factura.getEstado().equals("ANULADA")){
                    context.execute("cfdConfirmarAnulacion.show();");
                }else{
                    new funciones().setMsj(3, "FACTURA YA ESTA ANULADA"); 
                }
            }else{
                new funciones().setMsj(3, "NO PUEDE ANULAR ESTA FACTURA .... SI DESEA ANULARLA FAVOR DE PEDIRSELO A CONTABILIDAD");
               
            }
        }else{
            new funciones().setMsj(3, "NO HA SELECCIONADO FACTURA");
        }
    }
    
    
    
    //CONFIRMAR Y ANULAR FACTURA
    public void anularFactura(){
        agregar = false;
        selectAll = false;
        selectAnulados.clear();
        factura.setEstado("ANULADA");
        int productosDevueltos=0;
        ejbFacadeFactura.edit(factura);
        for(DetalleFactura df: factura.getDetalleFacturaCollection()){
            for(DetalleEnvio de:df.getDetalleEnvioCollection()){
                if(de.getNota() != null){
                    if(de.getNota().equals("DEVOLUCION") || de.getNota().equals("REINGRESO")){
                        productosDevueltos++;
                    }else{
                        de.setNota("ANULADO");
                        ejbFacadeDetalleEnvio.edit(de);
                    }
                }else{
                    de.setNota("ANULADO");
                    ejbFacadeDetalleEnvio.edit(de);
                }
            }
        }
        List<PagoVenta> pagos = ejbFacadePagoVenta.pagosFactura(factura);
        if(pagos != null){
            registrarDevolucionPorAnulacion(pagos);
        }
        new funciones().setMsj(1, "FACTURA ANULADA");
        if(productosDevueltos > 0){
            new funciones().setMsj(2, productosDevueltos + " PRODUCTOS de esta Factura ya fueron procesados como devolucion");
        }
    }
    
    /*
     * REGISTRA UNA DEVOLUCION POR ANULACION DE FACTURA
     */
    public void registrarDevolucionPorAnulacion(List<PagoVenta> pagos){
        nuevaDevolucion = null;
        try{
            if(pagos.isEmpty()){
                new funciones().setMsj(1, "FACTURA SIN PAGOS REGISTRADOS");
            }else{
                //GUARDAR DEVOLUCION
                BigDecimal totalDevolucion = BigDecimal.ZERO;
                for(PagoVenta actual: pagos){
                    totalDevolucion = totalDevolucion.add(actual.getTotalPago());
                }     
                nuevaDevolucion = new Devolucion();
                nuevaDevolucion.setFactura(factura);
                nuevaDevolucion.setFechaDevolucion(new funciones().getTime());
                nuevaDevolucion.setResponsable(new JsfUtil().getEmpleado());
                nuevaDevolucion.setMontoDevolucion(totalDevolucion);
                nuevaDevolucion.setDescuentoDevolucion(BigDecimal.ZERO);
                nuevaDevolucion.setTotalDevolucion(nuevaDevolucion.getMontoDevolucion());
                nuevaDevolucion.setSaldoDevolucion(nuevaDevolucion.getTotalDevolucion());
                nuevaDevolucion.setEstadoDevolucion("REGISTRADA");
                nuevaDevolucion.setNumdevolucion(ejbFacadeDevolucion.getNextIdDevolucion());
                nuevaDevolucion.setNotaDevolucion("POR ANULACION DE FACTURA");
                ejbFacadeDevolucion.create(nuevaDevolucion);
                //Actualizar Articulos
                for(DetalleEnvio actual: devoluciones){
                    //Hacer cambio en productos
                    actual.setNota("REINGRESO");
                    ejbFacadeDetalleEnvio.edit(actual);
                }
                new funciones().setMsj(1,"DEVOLUCION DE : " + new funciones().redondearMas(nuevaDevolucion.getTotalDevolucion().floatValue(),2) +" REGISTRADA CORRECTAMENTE");
                nuevaDevolucion = null;
            }
        }catch(Exception e){
            new funciones().setMsj(3,"ERROR AL REGISTRAR DEVOLUCION EN LA BD");
        }
    }
    
    
    //Agrega uno a la lista de seleccion
    public void agregarAlista(DetalleEnvio de){
        if(de != null){
            if(agregar){
                selectAnulados.add(de);
                new funciones().setMsj(1, selectAnulados.size() +" UNIDADES SELECCIONADAS");
            }else{
                selectAnulados.remove(de);
                new funciones().setMsj(1, selectAnulados.size() +" UNIDADES SELECCIONADAS");
            }
        }
    }
    
    
    //Seleccionar/Deseleccionar Todos
    public void seleccionarTodos(){
        if(selectAll){
            selectAnulados = ejbFacadeDetalleEnvio.productosAnulados(factura);
            agregar = true;
            new funciones().setMsj(1,selectAnulados.size() +" UNIDADES SELECCIONADAS");
        }else{
            selectAnulados.clear();
            agregar = false;
            new funciones().setMsj(1,selectAnulados.size() +" UNIDADES SELECCIONADAS");
        }
    }
    
    
    //REPORTAR PERDIDA
    public void prepararReportarPerdida(){
        RequestContext context = RequestContext.getCurrentInstance();
        notaAnulacion = null;
        if(!selectAnulados.isEmpty()){
            context.execute("cfdReportarPerdida.show();");
        }else{
            new funciones().setMsj(3, "NO HA SELECCIONADO PRODUCTOS");
        }
    }
    
    
    
    //REINGRESAR AL INVENTARIO
    public void prepararReingresarProducto(){
        RequestContext context = RequestContext.getCurrentInstance();
        if(!selectAnulados.isEmpty()){
            context.execute("cfdReingresarProducto.show();");
        }else{
            new funciones().setMsj(3, "NO HA SELECCIONADO PRODUCTOS");
        }
    }
    
    
     //VOLVER A FACTURAR
    public void prepararRefacturar(){
        RequestContext context = RequestContext.getCurrentInstance();
        nuevaFactura = new Factura(new FacturaPK());
        if(!selectAnulados.isEmpty()){
            nuevaFactura = new Factura(new FacturaPK());
            detalleNuevaFactura.clear();
            for(DetalleEnvio actual: selectAnulados){
                actual.setNota("REFACTURAR");
                ejbFacadeDetalleEnvio.edit(actual);
            }
            new funciones().irA("/faces/vistas/devoluciones/nuevaFactura.xhtml");
            regresar = true;
        }else{
            new funciones().setMsj(3, "NO HA SELECCIONADO PRODUCTOS");
        }
    }
    
    
    //CONFIRMAR Y REPORTAR PERDIDA
    public void reportarPerdida(){
        RequestContext context = RequestContext.getCurrentInstance();
        int productos = 0;
        try{
            for(DetalleEnvio actual : selectAnulados){
                actual.getInventario().setEstadoproducto("PERDIDA");
                actual.getInventario().setNotaProducto(notaAnulacion);
                ejbFacadeInventario.edit(actual.getInventario());
                actual.setNota("PERDIDA");
                ejbFacadeDetalleEnvio.edit(actual);
                productos++;
            }
            new funciones().setMsj(1,productos + " PRODUCTOS REPORTADOS");
            agregar = false;
            selectAnulados.clear();
            selectAll = false;
            context.addCallbackParam("validar",true);
        }catch(Exception e){
            new funciones().setMsj(4, "A OCURRIDO UN ERROR: " +e.getMessage());
            context.addCallbackParam("validar",false);
            agregar = false;
            selectAnulados.clear();
            selectAll = false;
        }
        
    }
    
    
    //CONFIRMAR Y REPORTAR PERDIDA
    public void reingresarProducto(){
        RequestContext context = RequestContext.getCurrentInstance();
        int productos = 0;
        try{
//            for(DetalleEnvio actual : selectAnulados){
//                actual.setNota("DEVOLUCION");
//                ejbFacadeDetalleEnvio.edit(actual);
//                actual.getInventario().setEstadoproducto("EN EXISTENCIA");
//                ejbFacadeInventario.edit(actual.getInventario());
//                productos++;
//            }
            for(DetalleEnvio actual : selectAnulados){
                actual.setNota("REINGRESO");
                ejbFacadeDetalleEnvio.edit(actual);
                productos++;
            }
            new funciones().setMsj(1,productos + " PRODUCTOS REINGRESADOS");
            //ejbFacadeCaja.updateEstadoCajaCompleta();
            agregar = false;
            selectAnulados.clear();
            selectAll = false;
            context.execute("cfdReingresarProducto.hide();");
        }catch(Exception e){
            new funciones().setMsj(4, "A OCURRIDO UN ERROR: " +e.getMessage());
            context.execute("var jqDialog = jQuery('#'+cfdReingresarProducto.id); jqDialog.effect('shake', { times:3 }, 100); ");
            agregar = false;
            selectAnulados.clear();
            selectAll = false;
        }
        
    }
    
    
    
    
    
    
    
    /*
     * FUNCION PARA PROCESAR VENTA DE ENVIO
     */
    public String generarNuevaFactura(FlowEvent event){
        if(event.getOldStep().equals("tbFactura")){
            nuevaFactura = new Factura(new FacturaPK());
            detalleNuevaFactura.clear();
            return "tbProductos";
        }else{
            //SI ES NEXT
            //if(nuevaFactura.getPorcentajeDescuento() < 0){
             //   new funciones().setMsj(3, "DESCUENTO DEBE SER >= 0");
            //    return "tbProductos";
            //}else 
            if(!selectAnulados.isEmpty()){
                detalleNuevaFactura.clear();
                nuevaFactura.setNumventa(factura.getNumventa());
                nuevaFactura.setTipoFactura(factura.getTipoFactura());
                nuevaFactura.setPorcentajeDescuento(BigDecimal.ZERO.floatValue());
                nuevaFactura.setDescuento(BigDecimal.ZERO);
                nuevaFactura.setSumas(BigDecimal.ZERO);
                nuevaFactura.setSubTotal(BigDecimal.ZERO);
                nuevaFactura.setIva(BigDecimal.ZERO);
                nuevaFactura.setTotal(BigDecimal.ZERO);
                getIVA();
                nuevaFactura.getFacturaPK().setFechaFactura(new funciones().getTime());
                nuevaFactura.setCliente(factura.getCliente());
                nuevaFactura.setCondicionPago(factura.getCondicionPago());
                if(nuevaFactura.getCondicionPago().equals("AL CREDITO")){
                    nuevaFactura.setFechaVencimiento(calcularFechaVencimientoCretido());
                }
                nuevaFactura.setEstado("ACTIVA");
                detallarFactura();
                calcularTotalesFactura();
                nuevaFactura.setPorcentajeDescuento(factura.getPorcentajeDescuento());
                return "tbFactura"; 
            }else{
                new funciones().setMsj(3, "NO HA SELECCIONADO PRODUCTO O YA FUERON RE-FACTURADOS");
                return "tbProductos";
            }
            
        }
    }
    
    
    /*
     * FUNCION QUE ACTUALIZA LA FACTURA AL CAMBIAR DE TIPO
     */
    public void cambioTipoFactura(){
        if(nuevaFactura.getFacturaPK().getFechaFactura() != null){
            nuevaFactura.setDescuento(BigDecimal.ZERO);
            nuevaFactura.setSumas(BigDecimal.ZERO);
            nuevaFactura.setSubTotal(BigDecimal.ZERO);
            nuevaFactura.setIva(BigDecimal.ZERO);
            nuevaFactura.setTotal(BigDecimal.ZERO);
            nuevaFactura.getFacturaPK().setFechaFactura(new funciones().getTime());
            if(nuevaFactura.getCondicionPago().equals("AL CREDITO")){
                nuevaFactura.setFechaVencimiento(calcularFechaVencimientoCretido());
            }
            nuevaFactura.setEstado("ACTIVA");
            detalleNuevaFactura.clear();
            detallarFactura();
            calcularTotalesFactura();
        }
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    //calcula fecha de vencimiento
    public Date calcularFechaVencimientoCretido(){
        Date resultado;
        int plazo = ejbFacadeConfig.getConfig("CREDITOS", "DIAS PLAZO").getValorInt(); //Dias Plazo de Credito
        resultado = new funciones().sumarDias(nuevaFactura.getFacturaPK().getFechaFactura(), plazo);
        return resultado;
    }
    
    
     /*
     * FUNCION QUE DETALLA LA FACTURA A PARTIR DE LOS PRODUCTOS SELECCIONADOS PARA REFACTURAR
     */
    
    public void detallarFactura(){
        List<Object> lineasVenta = ejbFacadeDetalleEnvio.getLineasVenta(factura);
        if(lineasVenta != null){
            for(Object linea : lineasVenta){
                Object[] actual = (Object[]) linea;
                DetalleFactura df = new DetalleFactura();
                df.setCantidad(Integer.parseInt(actual[2].toString()));
                df.setTipo(selectAnulados.get(0).getInventario().getProducto().getProductoPK().getTipoProducto());
                df.setEstilo((String)actual[0]);
                df.setPu((BigDecimal) actual[1]);
                df.setColores(coloresLineaVenta(df.getEstilo(),df.getPu()));
                //SI es FCF cambiar a precio con IVA incluido
                if(nuevaFactura.getTipoFactura().equals("FCF")){
        
                    BigDecimal precioIVA = new BigDecimal(df.getPu().doubleValue()*(1+IVA.getValorFloat()));
                    df.setPu(new BigDecimal(new funciones().redondearMas(precioIVA.floatValue(), 2)));
                }
                df.setMontoLinea(calcularMontoLineaVenta(df.getCantidad() , df.getPu()));
                detalleNuevaFactura.add(df);
            }
        }
    }
    
    
    /*
     * LISTA LOS COLORES DE LA LINEA DE VENTA
     */
    public String coloresLineaVenta(String estilo, BigDecimal pu){
        String colores = "";
        List<String> listaColores = new ArrayList<String>();
        for(DetalleEnvio actual: selectAnulados){
            if(actual.getInventario().getProducto().getProductoPK().getCodestilo().equals(estilo)){
                //if(actual.getPrecioFacturar().compareTo(pu) == 0){
                    if(!listaColores.contains(actual.getInventario().getColor())){
                        listaColores.add(actual.getInventario().getColor());
                    }
                //}
            }
        }
        for(int c=0; c < listaColores.size(); c++){
            if(c==0){
                colores = colores + listaColores.get(c);
            }else{
                colores = colores + ";" + listaColores.get(c);
            }
        }
        return colores;
    }
    
    
    /*
     * CALCULA EL MONTO DE LA LINEA DE VENTA
     */
    public BigDecimal calcularMontoLineaVenta(int cantidad, BigDecimal pu){
        BigDecimal montoLinea = BigDecimal.ZERO;
        BigDecimal monto = new BigDecimal(cantidad*pu.doubleValue());
        montoLinea = new BigDecimal(new funciones().redondearMas(monto.floatValue(),2));
        nuevaFactura.setSumas(montoLinea.add(nuevaFactura.getSumas()));
        return montoLinea;
    }
    
    /*
     * CALCULA EL MONTO TOTAL DE LA FACTURA
     */
    public void calcularTotalesFactura(){
        nuevaFactura.setSumas(new BigDecimal(new funciones().redondearMas(nuevaFactura.getSumas().floatValue(),2)));
            
        //Calculo de Subtotal
        BigDecimal subTotal = nuevaFactura.getSumas();//.subtract(nuevaFactura.getDescuento());
        nuevaFactura.setSubTotal(new BigDecimal(new funciones().redondearMas(subTotal.floatValue(), 2)));
        
        //Calculo de IVA
        if(nuevaFactura.getTipoFactura().equals("CCF")){
            BigDecimal iva = new BigDecimal(nuevaFactura.getSubTotal().doubleValue()*IVA.getValorFloat());
            nuevaFactura.setIva(new BigDecimal(new funciones().redondearMas(iva.floatValue(), 2)));
        }
        
        
        //Calculo del TOTAL DE LA FACTURA
        BigDecimal total = nuevaFactura.getSubTotal().add(nuevaFactura.getIva());
        nuevaFactura.setTotal(new BigDecimal(new funciones().redondearMas(total.floatValue(), 2)));
        
        
        //Calculo de DESCUENTO
        if(nuevaFactura.getPorcentajeDescuento() > 0){
            float porcentaje = (float) nuevaFactura.getPorcentajeDescuento().doubleValue() / 100;
            BigDecimal descuento = new BigDecimal(nuevaFactura.getTotal().doubleValue()*porcentaje);
            nuevaFactura.setDescuento(new BigDecimal(new funciones().redondearMas(descuento.floatValue(), 2)));
        }else{
            nuevaFactura.setDescuento(BigDecimal.ZERO);
        }
        
    }
    
    
    /*
     * CAMBIO DE PRECIOS
     */
    
    /*
     * FUNCIONES PARA CAMBIO DE PRECIO A FACTURAR
     */
    
    public void prepararCambioPrecio(DetalleEnvio detalle){
        RequestContext context = RequestContext.getCurrentInstance();
        de = detalle;
        nuevoPrecio = (float)0.00;
        context.addCallbackParam("mostrar", true);
    }
    
    
     public void validarCambioPrecio(){
        RequestContext context = RequestContext.getCurrentInstance();
        if(nuevoPrecio > 0){
            context.addCallbackParam("mostrar", true);
        }else{
            new funciones().setMsj(3, "Nuevo Precio INVALIDO debe ser > 0");
            context.addCallbackParam("mostrar", false);
        }
    }
    
    public void confirmarCambioPrecio(){
       int num = 0;
       String codigo = de.getInventario().getProducto().getProductoPK().getCodestilo();
       float precioActual = new funciones().redondearMas(de.getPrecioFacturar().floatValue(),2);
       if(selectAnulados != null){
           for(DetalleEnvio actual: selectAnulados){
               if(actual.getInventario().getProducto().getProductoPK().getCodestilo().equals(codigo)){
                   if(actual.getPrecioFacturar().floatValue() == precioActual){
                       actual.setPrecioFacturar(new BigDecimal(new funciones().redondearMas(nuevoPrecio, 2)));
                       ejbFacadeDetalleEnvio.edit(actual);
                       num++;
                   }
               }
           }
           new funciones().setMsj(2, "CODIGO: "+codigo);
           new funciones().setMsj(2, "NUEVO PRECIO: "+nuevoPrecio);
           new funciones().setMsj(1, "PRECIOS ACTUALIZADOS A " + num +" PRODUCTOS");
           
       }else{
           new funciones().setMsj(3, "ERROR LISTA DE DETALLE VACIA");
       }
    }
    
    
    
    /*
     * FUNCION PARA FACTURAR
     * guardar e imprimir
     */
    
    public void refacturar(){
        RequestContext context = RequestContext.getCurrentInstance();
        if(!detalleNuevaFactura.isEmpty()){
            if(!ejbFacadeFactura.existeFactura(nuevaFactura.getFacturaPK())){
                //NUEVA FACTURA
                if(nuevaFactura.getCondicionPago().equals("AL CREDITO")){
                    nuevaFactura.setSaldo(nuevaFactura.getTotal());
                    nuevaFactura.setTasaInteres((float) 0.00);//Tasa de interes no se ocupa todavia
                    nuevaFactura.setFechaProximoPago(null); //FECha de proximo pago no se ocupa aun
                    //nuevaFactura.setFechaVencimiento(calcularFechaVencimientoCretido());
                }else{
                    //AL CONTADO
                    nuevaFactura.setSaldo(nuevaFactura.getTotal());
                }
                //Calculo de DESCUENTO
                if(nuevaFactura.getPorcentajeDescuento() > 0){
                    float porcentaje = (float) nuevaFactura.getPorcentajeDescuento().doubleValue() / 100;
                    BigDecimal descuento = new BigDecimal(nuevaFactura.getTotal().doubleValue()*porcentaje);
                    nuevaFactura.setDescuento(new BigDecimal(new funciones().redondearMas(descuento.floatValue(), 2)));
                }                    
                //Crear Factura
                ejbFacadeFactura.create(nuevaFactura);
                ejbFacadeFactura.find(nuevaFactura.getFacturaPK());
                factura.getNumventa().getFacturaCollection().add(nuevaFactura); //Actualiza Listado
                //Guardar Detalle DE la Factura
                for(DetalleFactura df:detalleNuevaFactura){
                    df.setFactura(nuevaFactura);
                    df.setId(ejbFacadeDetalleFactura.getNextIdDetalleFactura());
                    ejbFacadeDetalleFactura.create(df);
                    nuevaFactura.getDetalleFacturaCollection().add(df);
                    //Vincular detalles de envio
                    vincularDetallesFacturaEnvio(df);
                }
                new funciones().setMsj(1, "FACTURA N°"+ nuevaFactura.getFacturaPK().getNumfactura() 
                                            + " con Fecha: "+new funciones().setFechaFormateada(nuevaFactura.getFacturaPK().getFechaFactura(),1) 
                                            + "\nPROCESADA CORRECTAMENTE");
                //Actualizar VENTA
                BigDecimal vn = factura.getNumventa().getVentaNeta().subtract(factura.getTotal());
                vn = vn.add(nuevaFactura.getTotal());
                factura.getNumventa().setVentaNeta(new BigDecimal(new funciones().redondearMas(vn.floatValue(),2)));  
                ejbFacadeVenta.edit(factura.getNumventa());
                
                //IMPRIMIR FACTURA
                context.addCallbackParam("validar", true);
                imprimirFactura = nuevaFactura;
                //Reset Factura
                resetFactura();
                context.execute("cfdImprimir.show();");
            }else{
                //YA EXISTE LA FACTURA EN EL SISTEMA
                new funciones().setMsj(3, "FACTURA YA EXISTE EN EL SISTEMA");
            }
        }else{
            //YA EXISTE LA FACTURA EN EL SISTEMA
                new funciones().setMsj(3, "FACTURA NO DETALLADA");
        }
        
    }
    
    
    
    
    public void vincularDetallesFacturaEnvio(DetalleFactura df){
        int count = 0;
        for(DetalleEnvio de: selectAnulados){
            if(de.getInventario().getProducto().getProductoPK().getCodestilo().equals(df.getEstilo())){
                de.setLineaVenta(df);
                de.setNota("REFACTURADO");
                //actualizar lista Linea Venta --> Detalle Envio
                df.getDetalleEnvioCollection().add(de);
                ejbFacadeDetalleEnvio.edit(de);
                count++; 
            }
        }
        if(count == df.getCantidad()){
            new funciones().setMsj(1, count + " unidades Facturadas");
        }else{
            new funciones().setMsj(3," NO se actualizaron todos los productos : " + count/df.getCantidad() );
        }
    }
    
    
    
    public void resetFactura(){
        nuevaFactura = new Factura(new FacturaPK());
        detalleNuevaFactura.clear();
    }
    
    
    
}
