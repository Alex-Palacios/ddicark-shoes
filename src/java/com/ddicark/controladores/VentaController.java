package com.ddicark.controladores;

import com.ddicark.auxiliares.JsfUtil;
import com.ddicark.auxiliares.funciones;
import com.ddicark.entidades.Cliente;
import com.ddicark.entidades.Configuraciones;
import com.ddicark.entidades.DetalleEnvio;
import com.ddicark.entidades.DetalleFactura;
import com.ddicark.entidades.Empleado;
import com.ddicark.entidades.Envio;
import com.ddicark.entidades.Factura;
import com.ddicark.entidades.FacturaPK;
import com.ddicark.entidades.Transaccion;
import com.ddicark.entidades.Venta;
import com.ddicark.jpaFacades.ConfiguracionesFacade;
import com.ddicark.jpaFacades.DetalleEnvioFacade;
import com.ddicark.jpaFacades.DetalleFacturaFacade;
import com.ddicark.jpaFacades.EnvioFacade;
import com.ddicark.jpaFacades.FacturaFacade;
import com.ddicark.jpaFacades.InventarioFacade;
import com.ddicark.jpaFacades.PedidoFacade;
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
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FlowEvent;

@ManagedBean(name = "ventasController")
@SessionScoped
public class VentaController extends AbstractController<Venta> implements Serializable {

    @EJB
    private ConfiguracionesFacade ejbFacadeConfig;
    
    @EJB
    private TransaccionFacade ejbFacadeTransaccion;
    
    @EJB
    private PedidoFacade ejbFacadePedido;
    
    @EJB
    private EnvioFacade ejbFacadeEnvio;
    
    @EJB
    private DetalleEnvioFacade ejbFacadeDetalleEnvio;
    
    @EJB
    private VentaFacade ejbFacadeVenta;
    
    @EJB
    private FacturaFacade ejbFacadeFactura;
    
    @EJB
    private InventarioFacade ejbFacadeInventario;
    
    @EJB
    private DetalleFacturaFacade ejbFacadeDetalleFactura;

    public VentaController() {
        super(Venta.class);
    }
     
    @PostConstruct
    public void init() {
        super.setFacade(ejbFacadeVenta);
    }
    
    
    
    //Variables de Control
    private DetalleEnvio de;
    private float nuevoPrecio;
    private float cambioPrecio;
    private Transaccion transaccion;
    private Envio ordenEnvio;    
    private Venta ventaEnvio;
    private Factura nuevaFactura;
    private List<DetalleFactura> detalleFactura;
    private List<DetalleEnvio> detalleEnvio;
    private float porcentajeFacturado;
    private Configuraciones IVA;
    private Empleado vendedor;
    private String observacionesVenta;
    
    public Factura imprimirFactura;
    
    //GETTERS AND SETTERS
    
    
    
    public float getNuevoPrecio() {
        return nuevoPrecio;
    }

    public void setNuevoPrecio(float nuevoPrecio) {
        this.nuevoPrecio = nuevoPrecio;
    }

    public float getCambioPrecio() {
        return cambioPrecio;
    }

    public void setCambioPrecio(float cambioPrecio) {
        this.cambioPrecio = cambioPrecio;
    }
    
    
    
    public DetalleEnvio getDe() {
        return de;
    }

    public void setDe(DetalleEnvio de) {
        this.de = de;
    }
    
    
    
    public Envio getOrdenEnvio() {
        return ordenEnvio;
    }

    public void setOrdenEnvio(Envio ordenEnvio) {
        this.ordenEnvio = ordenEnvio;
    }

    public Venta getVentaEnvio() {
        return ventaEnvio;
    }

    public void setVentaEnvio(Venta ventaEnvio) {
        this.ventaEnvio = ventaEnvio;
    }
    
    
    public Factura getNuevaFactura() {
        return nuevaFactura;
    }

    public void setNuevaFactura(Factura nuevaFactura) {
        this.nuevaFactura = nuevaFactura;
    }
    
    public List<DetalleFactura> getDetalleFactura() {
        return detalleFactura;
    }

    public void setDetalleFactura(List<DetalleFactura> detalleFactura) {
        this.detalleFactura = detalleFactura;
    }

    public List<DetalleEnvio> getDetalleEnvio() {
        return detalleEnvio;
    }

    public void setDetalleEnvio(List<DetalleEnvio> detalleEnvio) {
        this.detalleEnvio = detalleEnvio;
    }

    public float getPorcentajeFacturado() {
        porcentajeFacturado = (float)0.00;
        if(detalleEnvio != null){
            int facturado = 0;
            for(DetalleEnvio actual: detalleEnvio ){
                if(actual.getLineaVenta() != null){
                    facturado++;
                }
            }
            if(detalleEnvio.size() > 0){
                porcentajeFacturado = (facturado / detalleEnvio.size())*100;
                if(porcentajeFacturado == 100){
                    if(ordenEnvio.getEstado().equals("EMPACADO")){
                       ordenEnvio.setEstado("FACTURADO");
                       ejbFacadeEnvio.edit(ordenEnvio);
                    }
                }
            }
        }
        return porcentajeFacturado;
    }

    public void setPorcentajeFacturado(float porcentajeFacturado) {
        this.porcentajeFacturado = porcentajeFacturado;
    }

    public void getIVA() {
        IVA = ejbFacadeConfig.getConfig("GLOBAL", "IVA");
        if(IVA == null){
            IVA = new Configuraciones();
            IVA.setValorFloat((float)0.13);
        }
    }

    public Empleado getVendedor() {
        return vendedor;
    }

    public void setVendedor(Empleado vendedor) {
        this.vendedor = vendedor;
    }

    public String getObservacionesVenta() {
        return observacionesVenta;
    }

    public void setObservacionesVenta(String observacionesVenta) {
        this.observacionesVenta = observacionesVenta;
    }

    public Factura getImprimirFactura() {
        return imprimirFactura;
    }

    public void setImprimirFactura(Factura imprimirFactura) {
        this.imprimirFactura = imprimirFactura;
    }
    
    
    
    
    
    
    
    
    
    
    //FUNCIONES
   
    /*
     * FUNCION PARA PROCESAR VENTA DE ENVIO
     */
    public String procesarVenta(FlowEvent event){
        nuevaFactura = new Factura(new FacturaPK());
        detalleFactura = new ArrayList<DetalleFactura>();
        detalleEnvio = null;
        if(event.getOldStep().equals("tbVenta")){
            return event.getNewStep();
        }else{
            //Preguntar Si existe numero de venta asignada al envio
            if(ejbFacadeVenta.existeVentaOrden(ordenEnvio)){
                //YA EXISTE LA VENTA --> CARGAR DATOS
                ventaEnvio = ejbFacadeVenta.getVentaOrden(ordenEnvio);
                detalleEnvio = ejbFacadeDetalleEnvio.detalleOrdenEnvio(ordenEnvio);
                return event.getNewStep();
            }else{
                //NO EXISTE LA VENTA
                if(crearVenta()){
                    detalleEnvio = ejbFacadeDetalleEnvio.detalleOrdenEnvio(ordenEnvio);
                    return event.getNewStep();
                }else{
                    new funciones().setMsj(1, "No se puede CREAR VENTA");
                    return event.getOldStep();
                }
            }
        }
    }
    
    
    /*
     *FUNCION QUE CREA LA VENTA PARA LA ORDEN DE ENVIO
     */
    public boolean crearVenta(){
        boolean respuesta = false;
        try{
            //CREAR TRANSACCION
            transaccion = new Transaccion();
            transaccion.setTipoTransac(3);  // UNA VENTA
            transaccion.setResponsableTransac(new JsfUtil().getEmpleado());
            transaccion.setFechaTransac(new funciones().getTime());
            transaccion.setHoraTransac(new funciones().getTime());
            transaccion.setIdtransac(ejbFacadeTransaccion.getNextIdTransac());
            ejbFacadeTransaccion.create(transaccion);
            //CREAR VENTA
            ventaEnvio = new Venta();
            ventaEnvio.setIdtransac(transaccion);
            ventaEnvio.setDescuentoVenta((float) 0.00);
            ventaEnvio.setVentaNeta(BigDecimal.ZERO);
            ventaEnvio.setOrdenEnvio(ordenEnvio);
            ventaEnvio.setMontoVenta(ordenEnvio.getSubTotal());
            ventaEnvio.setTipoVenta(ordenEnvio.getPedido().getTipoPago());
            ventaEnvio.setEstadoVenta("FACTURANDO");
            ventaEnvio.setNumventa(ejbFacadeVenta.getNextIdVenta());
            ejbFacadeVenta.create(ventaEnvio);
            respuesta = true;
            return respuesta;
        }catch(Exception ex){
            return respuesta;
        }
        
    }
    
    /*
     * FUNCION QUE PREPARA UNA NUEVA FACTURA
     */
    
    public void prepararNuevaFactura(){
        
        //if(ventaEnvio.getDescuentoVenta() != null){
           /* if(ventaEnvio.getDescuentoVenta() < 0 || ventaEnvio.getDescuentoVenta() > 100){
                new funciones().setMsj(3, "EL PORCENTAJE DE DESCUENTO DEBE SER UNA VALOR ENTRE 0.00% y 100.00%");
            }else{*/
                BigDecimal deudaActual = totalDeudaCliente(ventaEnvio.getOrdenEnvio().getPedido().getClientePedido());
                BigDecimal deudaTotal = deudaActual.add(ventaEnvio.getOrdenEnvio().getSubTotal());
                if(ventaEnvio.getOrdenEnvio().getPedido().getTipoPago().equals("AL CONTADO") || ventaEnvio.getOrdenEnvio().getPedido().getClientePedido().getLimite() == null || ventaEnvio.getOrdenEnvio().getPedido().getClientePedido().getLimite().compareTo(BigDecimal.ZERO) == 0 || deudaTotal.compareTo(ventaEnvio.getOrdenEnvio().getPedido().getClientePedido().getLimite()) <=0){
                    //Preparar factura
                    nuevaFactura = new Factura(new FacturaPK());
                    nuevaFactura.setNumventa(ventaEnvio);
                    nuevaFactura.setTipoFactura("FCF");
                    nuevaFactura.setPorcentajeDescuento(BigDecimal.ZERO.floatValue());
                    nuevaFactura.setDescuento(BigDecimal.ZERO);
                    nuevaFactura.setSumas(BigDecimal.ZERO);
                    nuevaFactura.setSubTotal(BigDecimal.ZERO);
                    nuevaFactura.setIva(BigDecimal.ZERO);
                    nuevaFactura.setTotal(BigDecimal.ZERO);
                    detalleFactura = new ArrayList<DetalleFactura>();
                    getIVA();
                    nuevaFactura.getFacturaPK().setFechaFactura(new funciones().getTime());
                    nuevaFactura.setCliente(ventaEnvio.getOrdenEnvio().getPedido().getClientePedido());
                    nuevaFactura.setCondicionPago(ventaEnvio.getTipoVenta());
                    if(nuevaFactura.getCondicionPago().equals("AL CREDITO")){
                        nuevaFactura.setFechaVencimiento(calcularFechaVencimientoCretido());
                    }
                    nuevaFactura.setEstado("ACTIVA");
                    detallarFactura();
                    calcularTotalesFactura();
                    if(detalleFactura.isEmpty()){
                        resetFactura();
                        new funciones().setMsj(2, "Ya no hay productos que facturar");
                    }
                }else{
                    new funciones().setMsj(2,"NO SE PUEDE FACTURAR ENVIO PORQUE CLIENTE EXCEDE LIMITE DE CREDITO"); 
                }
           // }
        //}
    }
    
    public void actualizarFechaVencimiento(){
         nuevaFactura.setFechaVencimiento(calcularFechaVencimientoCretido());
    }
    /*
     * FUNCION QUE DETALLA LA FACTURA A PARTIR DE LOS PRODUCTOS DEL ENVIO QUE
     * NO HAN SIDO FACTURADOS
     */
    
    public void detallarFactura(){
        List<Object> lineasVenta;
        //if(nuevaFactura.getTipoFactura().equals("CCF")){
        lineasVenta = ejbFacadeDetalleEnvio.getLineasVenta(ordenEnvio);
        //}else{
        //    lineasVenta = ejbFacadeDetalleEnvio.getLineasVentaFCF(ordenEnvio);
        //}
        if(lineasVenta != null){
            for(Object linea : lineasVenta){
                Object[] actual = (Object[]) linea;
                DetalleFactura df = new DetalleFactura();
                df.setCantidad(Integer.parseInt(actual[2].toString()));
                df.setTipo(detalleEnvio.get(0).getInventario().getProducto().getProductoPK().getTipoProducto());
                df.setEstilo((String)actual[0]);
                df.setPu((BigDecimal) actual[1]);
                df.setColores(coloresLineaVenta(df.getEstilo(),df.getPu()));
                
                BigDecimal precioIVA = new BigDecimal(df.getPu().floatValue()*(1+IVA.getValorFloat()));
                precioIVA = new BigDecimal(new funciones().redondearMas(precioIVA.floatValue(), 2));
                //SI es FCF cambiar a precio con IVA incluido
                if(nuevaFactura.getTipoFactura().equals("FCF")){
                        df.setPu(precioIVA);
                }
                df.setMontoLinea(calcularMontoLineaVenta(df.getCantidad() , precioIVA));
                
                detalleFactura.add(df);
                
            }
        }
    }
    
    
    /*
     * FUNCION QUE ACTUALIZA LA FACTURA AL CAMBIAR DE TIPO
     */
    public void cambioTipoFactura(){
        if(nuevaFactura.getFacturaPK().getFechaFactura() != null){
            nuevaFactura.setNumventa(ventaEnvio);
            nuevaFactura.setDescuento(BigDecimal.ZERO);
            nuevaFactura.setSumas(BigDecimal.ZERO);
            nuevaFactura.setSubTotal(BigDecimal.ZERO);
            nuevaFactura.setIva(BigDecimal.ZERO);
            nuevaFactura.setTotal(BigDecimal.ZERO);
            nuevaFactura.getFacturaPK().setFechaFactura(new funciones().getTime());
            nuevaFactura.setCliente(ventaEnvio.getOrdenEnvio().getPedido().getClientePedido());
            nuevaFactura.setCondicionPago(ventaEnvio.getTipoVenta());
            if(nuevaFactura.getCondicionPago().equals("AL CREDITO")){
                nuevaFactura.setFechaVencimiento(calcularFechaVencimientoCretido());
            }
            nuevaFactura.setEstado("ACTIVA");
            detalleFactura = new ArrayList<DetalleFactura>();
            detallarFactura();
            calcularTotalesFactura();
        }
    }
    
    
    /*
     * CALCULA EL MONTO DE LA LINEA DE VENTA
     */
    public BigDecimal calcularMontoLineaVenta(int cantidad, BigDecimal pu){
        BigDecimal montoLinea = new BigDecimal(cantidad*pu.floatValue());
        montoLinea = new BigDecimal(new funciones().redondearMas(montoLinea.floatValue(),2));
        nuevaFactura.setTotal(nuevaFactura.getTotal().add(montoLinea));
        if(nuevaFactura.getTipoFactura().equals("CCF")){
            montoLinea = new BigDecimal(montoLinea.floatValue()/(1+IVA.getValorFloat()));
            montoLinea = new BigDecimal(new funciones().redondearMas(montoLinea.floatValue(),2));
        }
        nuevaFactura.setSumas(nuevaFactura.getSumas().add(montoLinea));
        return montoLinea;
    }
    
    /*
     * CALCULA EL MONTO TOTAL DE LA FACTURA
     */
    public void calcularTotalesFactura(){
        nuevaFactura.setSumas(new BigDecimal(new funciones().redondearMas(nuevaFactura.getSumas().floatValue(),2)));
        nuevaFactura.setTotal(new BigDecimal(new funciones().redondearMas(nuevaFactura.getTotal().floatValue(),2)));
        //Calculo de Subtotal
        BigDecimal subTotal = nuevaFactura.getSumas();//.subtract(nuevaFactura.getDescuento());
        nuevaFactura.setSubTotal(new BigDecimal(new funciones().redondearMas(subTotal.floatValue(), 2)));
        BigDecimal iva = nuevaFactura.getTotal().subtract(nuevaFactura.getSubTotal());//Calculo de IVA
        nuevaFactura.setIva(new BigDecimal(new funciones().redondearMas(iva.floatValue(), 2)));
        //if(nuevaFactura.getTipoFactura().equals("CCF")){
            //BigDecimal iva = new BigDecimal(nuevaFactura.getSubTotal().doubleValue()*IVA.getValorFloat());
            //nuevaFactura.setIva(new BigDecimal(new funciones().redondearMas(iva.floatValue(), 2)));
        //}
               
        //Calculo del TOTAL DE LA FACTURA
        //BigDecimal total = nuevaFactura.getSubTotal().add(nuevaFactura.getIva());
        //BigDecimal total = nuevaFactura.getSubTotal().add(nuevaFactura.getIva());
        //nuevaFactura.setTotal(new BigDecimal(new funciones().redondearMas(total.floatValue(), 2)));
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
     * LISTA LOS COLORES DE LA LINEA DE VENTA
     */
    public String coloresLineaVenta(String estilo, BigDecimal pu){
        String colores = "";
        List<String> listaColores = new ArrayList<String>();
        for(DetalleEnvio actual: detalleEnvio){
            if(actual.getLineaVenta() == null && actual.getInventario().getProducto().getProductoPK().getCodestilo().equals(estilo)){
               // actual.setPrecioFacturar(new BigDecimal(new funciones().redondearMas(pu.floatValue(),2)));
               // if(actual.getPrecioFacturar().compareTo(pu) == 0){
                    if(!listaColores.contains(actual.getInventario().getColor())){
                        listaColores.add(actual.getInventario().getColor());
                    }
               // }
            }
        }
        for(int c=0; c < listaColores.size(); c++){
            if(c==0){
                colores = listaColores.get(c);
            }else{
                colores = colores + "--" + listaColores.get(c);
            }
        }
        return colores;
    }
    
    
    public void resetFactura(){
        nuevaFactura = new Factura(new FacturaPK());
        detalleFactura = new ArrayList<DetalleFactura>();
    }
    
    
    /*
     * FUNCION PARA FACTURAR
     * guardar e imprimir
     */
    
    public void facturar(){
        RequestContext context = RequestContext.getCurrentInstance();
        if(validarDatosCompletosCliente()){
            if(!detalleFactura.isEmpty()){
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
                    ventaEnvio.getFacturaCollection().add(nuevaFactura); //Actualiza Listado
                    //Guardar Detalle DE la Factura
                    for(DetalleFactura df:detalleFactura){
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
                    BigDecimal vn = ventaEnvio.getVentaNeta().add(nuevaFactura.getTotal());
                    ventaEnvio.setVentaNeta(new BigDecimal(new funciones().redondearMas(vn.floatValue(),2)));  
                    ventaEnvio.setDescuentoVenta(new funciones().redondearMas(nuevaFactura.getPorcentajeDescuento().floatValue(),2));
                    ejbFacadeVenta.edit(ventaEnvio);

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
          
        }else{
            
        }
        
    }
    
    
    public Date calcularFechaVencimientoCretido(){
        Date resultado;
        int plazo = ejbFacadeConfig.getConfig("CREDITOS", "DIAS PLAZO").getValorInt(); //Dias Plazo de Credito
        resultado = new funciones().sumarDias(nuevaFactura.getFacturaPK().getFechaFactura(), plazo);
        return resultado;
    }
    
    
    
    public void vincularDetallesFacturaEnvio(DetalleFactura df){
        int count = 0;
        for(DetalleEnvio de: detalleEnvio){
            if(de.getInventario().getProducto().getProductoPK().getCodestilo().equals(df.getEstilo())){
                de.setLineaVenta(df);
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
    
    
     //prepara el despacho de la venta
   public void prepararDespacho(){
       vendedor = null;
       observacionesVenta = null;
   }
    
    /*
     * FUNCION PARA DESPACHAR Y COMPLETAR LA VENTA ACTUAL
     */
    public void completarVENTA(){
        if(ventaFacturada()){
           //PROCESAR VENTA
          try{
            //CAMBIAR ESTADO DE LOS PRODUCTOS DE LA VENTA A PRODUCTO VENDIDO y COLOCAR FECHA DE EGRESO Y PRECIO VENDIDO
             for(DetalleEnvio actual: detalleEnvio){
                actual.getInventario().setEstadoproducto("VENDIDO");
                actual.getInventario().setFechaEgreso(new funciones().getTime());
                actual.getInventario().setPreciovendido(actual.getPrecioFacturar());
                ejbFacadeInventario.edit(actual.getInventario());
             }
             try{
                //Cambiar estado de la venta y actualizarla
                ventaEnvio.setNotaVenta(observacionesVenta);
                ventaEnvio.setDespachadoA(vendedor);
                ventaEnvio.setEstadoVenta("COMPLETADA"); //Entregado a vendedor
                ejbFacadeVenta.edit(ventaEnvio);
                new funciones().setMsj(1,"VENTA COMPLETADA");
                //Actualizar estado de ORDEN DE ENVIO
                ventaEnvio.getOrdenEnvio().setEstado("DESPACHADO"); //Enviado al Cliente
                ejbFacadeEnvio.edit(ventaEnvio.getOrdenEnvio());
                resetVentaOrdenEnvio();
                new funciones().irA("faces/vistas/ventas/facturacion.xhtml"); 
            }catch(Exception e){
                new funciones().setMsj(4, "ERROR AL ACTUALIZAR VENTA");
            }
          }catch(Exception e){
              new funciones().setMsj(4, "ERROR AL DESPACHAR PRODUCTOS DE VENTA ");
          }
       }else{
          new funciones().setMsj(3,"NO SE HAN FACTURADO TODOS LOS PRODUCTOS DE LA VENTA");
          
       }
    }
    
    
    //FUNCION que verifica si la venta esta completamente factura y lista para despacharse
    
    public boolean ventaFacturada(){
        boolean despachar = true;
        if(detalleEnvio != null){
            for(DetalleEnvio actual: detalleEnvio){
                if(actual.getLineaVenta() == null){
                    despachar = false;
                    break;
                }
            }
        }else{
            despachar = false;
        }
        
        return despachar;
    }
    
    //Resetea todo para una nueva venta
    public void resetVentaOrdenEnvio(){
        nuevaFactura = new Factura(new FacturaPK());
        detalleFactura = new ArrayList<DetalleFactura>();
        detalleEnvio = null;
        ventaEnvio = null;
        ordenEnvio = new Envio();
    }
    
    
    
    
    
    
    
    
    
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
       BigDecimal totalVenta = BigDecimal.ZERO;
       if(detalleEnvio != null){
           for(DetalleEnvio actual: detalleEnvio){
               if(actual.getInventario().getProducto().getProductoPK().getCodestilo().equals(codigo)){
                   if(actual.getPrecioFacturar().floatValue() == precioActual){
                       actual.setPrecioFacturar(new BigDecimal(new funciones().redondearMas(nuevoPrecio, 2)));
                       ejbFacadeDetalleEnvio.edit(actual);
                       num++;
                   }
               }
               totalVenta = totalVenta.add(actual.getPrecioFacturar());
           }
           
           //ACTUALIZAR ENVIO Y VENTA
           ventaEnvio.setMontoVenta(totalVenta);
           ejbFacadeVenta.edit(ventaEnvio);
           ordenEnvio.setSubTotal(totalVenta);
           ejbFacadeEnvio.edit(ordenEnvio);
           //ventaEnvio = ordenEnvio.getVenta();
           new funciones().setMsj(2, "CODIGO: "+codigo);
           new funciones().setMsj(2, "NUEVO PRECIO: "+nuevoPrecio);
           new funciones().setMsj(1, "PRECIOS ACTUALIZADOS A " + num +" PRODUCTOS");
           
       }else{
           new funciones().setMsj(3, "ERROR LISTA DE DETALLE VACIA");
       }
    }
    
    
    /*
     * FUNCIO QUE IMPRIME LA FACTURA
     */
     public void reimprimirFactura(Factura factura) {
        if(factura != null){
            imprimirFactura = factura;
            new funciones().irA("faces/vistas/ventas/factura.xhtml"); 
        }
    }

     
     /*
      * VERIFICA QUE LOS DATOS DEL CLIENTE ESTEN COMPLETOS PARA FACTURAR
      */
     public boolean validarDatosCompletosCliente(){
         boolean validos = true;
         if(nuevaFactura.getTipoFactura().equals("FCF")){
           if((nuevaFactura.getCliente().getDuiCliente() == null || "".equals(nuevaFactura.getCliente().getDuiCliente())) && (nuevaFactura.getCliente().getNitCliente() == null  || "".equals(nuevaFactura.getCliente().getNitCliente()) )){
               validos = false;
               new funciones().setMsj(2, "SE NECESITA DUI o NIT DEL CLIENTE");
           }     
        }else{
            if(nuevaFactura.getCliente().getNitCliente() == null  || "".equals(nuevaFactura.getCliente().getNitCliente()) || nuevaFactura.getCliente().getNrcCliente() == null || "".equals(nuevaFactura.getCliente().getNrcCliente())){
               validos = false;
               new funciones().setMsj(2, "SE NECESITA NIT y NRC DEL CLIENTE");
            } 
        }
         return validos;
     }
    
     
     
     
     public BigDecimal totalDeudaCliente(Cliente cliente){
        BigDecimal deuda = BigDecimal.ZERO;
        if(cliente != null){
            deuda = new BigDecimal(ejbFacadeFactura.getDeudaPorCreditos(cliente));
        }
        return deuda;
    }
}

