package com.ddicark.controladores;

import com.ddicark.auxiliares.JsfUtil;
import com.ddicark.auxiliares.funciones;
import com.ddicark.entidades.Cliente;
import com.ddicark.entidades.Empleado;
import com.ddicark.entidades.Factura;
import com.ddicark.entidades.FacturaIngreso;
import com.ddicark.entidades.FacturaPK;
import com.ddicark.entidades.PagoVenta;
import com.ddicark.entidades.Transaccion;
import com.ddicark.entidades.Venta;
import com.ddicark.jpaFacades.FacturaFacade;
import com.ddicark.jpaFacades.PagoVentaFacade;
import com.ddicark.jpaFacades.TransaccionFacade;
import java.io.InputStream;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@ManagedBean(name = "facturaController")
@SessionScoped
public class FacturaController extends AbstractController<Factura> implements Serializable {

    @EJB
    private FacturaFacade ejbFacadeFactura;
    
    @EJB
    private TransaccionFacade ejbFacadeTransac;
    
    @EJB
    private PagoVentaFacade ejbFacadePagoVenta;
    
    private List<String> condicionPagoItems;
    
    private Empleado vendedor;
    
     public FacturaController() {
       super(Factura.class); 
    }
    //Inicializa controlador de persistencia de proveedores
    @PostConstruct
    public void init() {
        super.setFacade(ejbFacadeFactura);
    }

    @Override
    protected void setEmbeddableKeys() {
        //Ingresa los valores de los campos que forman parte de una llave primaria y al mismo tiempo son foraneas
    }

    @Override
    protected void initializeEmbeddableKey() {
        this.getSelected().setFacturaPK(new com.ddicark.entidades.FacturaPK());
    }

    public List<String> getCondicionPagoItems() {
        condicionPagoItems = new ArrayList<String>();
        condicionPagoItems.add("Select");
        condicionPagoItems.add("AL CONTADO");
        condicionPagoItems.add("AL CREDITO");
        return condicionPagoItems;
    }


    
    //VARIABLES DE CONTROL
    private List<Factura> ventas;
    
    
    private Factura nuevaFactura;
    private Factura facturaContado;
    private Factura facturaCredito;
    private List<Factura> facturasContadoActivas;
    private List<Factura> facturasCreditoActivas;
    private float pago;
    private float nuevoSaldo;
    
    private float descuento;
    private String tipoPago;
    private String banco;
    private String identificador;
    private String notaPago;
    private String numrecibo;
    private boolean pagoValido = false;
    private SelectItem[] tipoOptions;
    private SelectItem[] condicionOptions;
    private SelectItem[] tipoPagoOptions;
    
    private List<Factura> facturasActivasCliente;
    
    private List<Factura> filtroFacturas;
    
    private Date fechaPago;
    
    
    private String pathServletFactura;
    
    //Getters And Setters
    public List<Factura> getFiltroFacturas() {
        return filtroFacturas;
    }

    public void setFiltroFacturas(List<Factura> filtroFacturas) {
        this.filtroFacturas = filtroFacturas;
    }
    
    
    public List<Factura> getVentas() {
        return ventas;
    }
    
    
    public Factura getFacturaContado() {
        return facturaContado;
    }

    public void setFacturaContado(Factura facturaContado) {
        this.facturaContado = facturaContado;
    }

    public List<Factura> getFacturasContadoActivas() {
        facturasContadoActivas = ejbFacadeFactura.facturasContadoActiva();
        return facturasContadoActivas;
    }

    public List<Factura> getFacturasCreditoActivas() {
        facturasCreditoActivas = ejbFacadeFactura.facturasCreditoActiva();
        return facturasCreditoActivas;
    }

    
    
    public PagoVentaFacade getEjbFacadePagoVenta() {
        return ejbFacadePagoVenta;
    }
    
    

    public Factura getFacturaCredito() {
        return facturaCredito;
    }

    public void setFacturaCredito(Factura facturaCredito) {
        this.facturaCredito = facturaCredito;
    }

    public float getPago() {
        return pago;
    }

    public Date getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(Date fechaPago) {
        this.fechaPago = fechaPago;
    }

    
    public void setPago(float pago) {
        this.pago = pago;
    }

    public float getNuevoSaldo() {
        return nuevoSaldo;
    }

    public void setNuevoSaldo(float nuevoSaldo) {
        this.nuevoSaldo = nuevoSaldo;
    }

    public float getDescuento() {
        return descuento;
    }

    public void setDescuento(float descuento) {
        this.descuento = descuento;
    }

    public String getTipoPago() {
        return tipoPago;
    }

    public void setTipoPago(String tipoPago) {
        this.tipoPago = tipoPago;
    }

    public String getBanco() {
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    
    public String getNotaPago() {
        return notaPago;
    }

    public void setNotaPago(String notaPago) {
        this.notaPago = notaPago;
    }

    public String getNumrecibo() {
        return numrecibo;
    }

    public void setNumrecibo(String numrecibo) {
        this.numrecibo = numrecibo;
    }
    
    

    public boolean isPagoValido() {
        return pagoValido;
    }

    public void setPagoValido(boolean pagoValido) {
        this.pagoValido = pagoValido;
    }

    public Empleado getVendedor() {
        return vendedor;
    }

    public void setVendedor(Empleado vendedor) {
        this.vendedor = vendedor;
    }
    
    
    
    public List<Factura> getFacturasActivasCliente(Cliente cliente) {
        facturasActivasCliente = null;
        if(cliente != null){
            facturasActivasCliente = ejbFacadeFactura.facturasActivasCliente(cliente);
        }
        return facturasActivasCliente;
    }

    public SelectItem[] getTipoOptions() {
        SelectItem[] options = new SelectItem[3]; 
        options[0] = new SelectItem("", "Select"); 
        options[1] = new SelectItem("FCF", "FCF");  
        options[2] = new SelectItem("CCF", "CCF");   
        tipoOptions = options;
        return tipoOptions;
    }

    public SelectItem[] getCondicionOptions() {
        SelectItem[] options = new SelectItem[3]; 
        options[0] = new SelectItem("", "Select"); 
        options[1] = new SelectItem("AL CONTADO", "AL CONTADO");  
        options[2] = new SelectItem("AL CREDITO", "AL CREDITO");   
        condicionOptions = options;
        return condicionOptions;
    }

    public SelectItem[] getTipoPagoOptions() {
        SelectItem[] options = new SelectItem[4]; 
        options[0] = new SelectItem("", "Select"); 
        options[1] = new SelectItem("EFECTIVO", "EFECTIVO");  
        options[2] = new SelectItem("CHEQUE", "CHEQUE");
        options[3] = new SelectItem("REMESA", "REMESA");
        tipoPagoOptions = options;
        return tipoPagoOptions;
    }
    
    
    
    
    //Funciones 

    public String getPathServletFactura() {
        pathServletFactura = new JsfUtil().getPathContext() + "/factura";
        String numFactura = null;
        Date fechaFactura =null;
        try{
            numFactura = new JsfUtil().getVentaController().getImprimirFactura().getFacturaPK().getNumfactura();
            fechaFactura = new JsfUtil().getVentaController().getImprimirFactura().getFacturaPK().getFechaFactura();
        }catch(Exception ex){
            new funciones().setMsj(3, "ERROR AL RECUPERAR FACTURA CONTROLADOR");
        }
        if(numFactura != null && fechaFactura != null){
            pathServletFactura = pathServletFactura+"?numFactura="+numFactura+"&fechaFactura="+fechaFactura;
        }
        return pathServletFactura;
    }

    public void setPathServletFactura(String pathServletFactura) {
        this.pathServletFactura = pathServletFactura;
    }

    
       
    
    public void prepararNuevaFactura(Venta numVenta){
        nuevaFactura = new Factura(new FacturaPK());
        
    }
    
    /*
     * Calcula el total de pagos realizados por el cliente de una factura
     */
    public float calcularTotalPagosFactura(Factura factura){
        float total = (float) 0.00;
        if(factura != null){
            List<PagoVenta> pagosFactura = ejbFacadePagoVenta.pagosFactura(factura);
            if(pagosFactura != null && !pagosFactura.isEmpty()){
                BigDecimal suma = BigDecimal.ZERO;
                for(PagoVenta actual : pagosFactura){
                    suma = suma.add(actual.getTotalPago());
                }
                total = new funciones().redondearMas(suma.floatValue(), 2);
            }
        }
        return total;
    }
    
    
    //Verifica que la cantidad ingresada como pago sea valido
   public void validarPagoContado(){
       RequestContext context = RequestContext.getCurrentInstance(); 
       pago = new funciones().redondearMas(pago,2);
       Date fechaActual = new funciones().getTime();
       if(pago < 0){
           pagoValido = false;
           new funciones().setMsj(3,"Pago no puede ser Negativo");
       }else if(pago == 0){
           new funciones().setMsj(3,"Pago debe ser mayor a 0.00");
           pagoValido = false;
       }else if(fechaPago.after(fechaActual) ){
           new funciones().setMsj(3,"Fecha de Pago Invalida");
           pagoValido = false;
       }else if(!verificarDescuentoAutorizado(true)){
           new funciones().setMsj(3,"Descuento excede el autorizado");
           pagoValido = false;
       }else{
           float saldoActual = new funciones().redondearMas(facturaContado.getSaldo().floatValue(),2);
           float cantidad = pago + descuento;
           if( cantidad <= saldoActual){
               //PAGO VALIDO
               pagoValido = true;
               //calcular nuevo Saldo
               nuevoSaldo = new funciones().redondearMas(saldoActual - pago - descuento,2);
           }else{
               //pago invalido
               pagoValido = false;
               new funciones().setMsj(2,"PAGO SE EXCEDE DEL SALDO ACTUAL");
           }
       }
       context.addCallbackParam("validar", pagoValido);
       
   }
   
   
   public boolean verificarDescuentoAutorizado(boolean contado){
       boolean OK = true;
       //Date fechaI = new Date("24/08/2014");
       if(contado){
           if(descuento > 0){
                BigDecimal descReg = BigDecimal.valueOf(descuento);
                for(PagoVenta p : ejbFacadePagoVenta.pagosFactura(facturaContado)){
                    descReg = descReg.add(p.getDescuento());
                }
                float total = new funciones().redondearMas(descReg.floatValue(), 2);
                if(total > new funciones().redondearMas(facturaContado.getDescuento().floatValue(), 2)){
                    OK = false;
                }
           }
       }else{
           if(descuento > 0){
                BigDecimal descReg = BigDecimal.valueOf(descuento);
                for(PagoVenta p : ejbFacadePagoVenta.pagosFactura(facturaCredito)){
                    descReg = descReg.add(p.getDescuento());
                }
                float total = new funciones().redondearMas(descReg.floatValue(), 2);
                if(total > new funciones().redondearMas(facturaCredito.getDescuento().floatValue(), 2)){
                    OK = false;
                }
           }
       }
       return OK;
   }
   
    /*
     * FUNCIONES PARA PAGOS
     */
   public void cancelarFacturaContado(){
       if(facturaContado != null){
           try{ 
                //Registrar pago
                //NUEVA TRANSACCION
                Transaccion transac = new Transaccion();
                transac.setFechaTransac(new funciones().getTime());
                transac.setHoraTransac(new funciones().getTime());
                transac.setResponsableTransac(new JsfUtil().getEmpleado());
                transac.setTipoTransac(5); //REGISTRO DE PAGO VENTA
                transac.setIdtransac(ejbFacadeTransac.getNextIdTransac());
                ejbFacadeTransac.create(transac);
                //REGISTRAR PAGO
                PagoVenta pagoVenta = new PagoVenta();
                pagoVenta.setIdtransac(transac);
                pagoVenta.setFechaPago(fechaPago);
                pagoVenta.setFactura(facturaContado);
                pagoVenta.setInteres(BigDecimal.ZERO);
                pagoVenta.setMora(BigDecimal.ZERO);
                pagoVenta.setAbono(new BigDecimal(pago));
                pagoVenta.setDescuento(new BigDecimal(descuento));
                BigDecimal total = pagoVenta.getAbono().add(pagoVenta.getInteres()).add(pagoVenta.getMora().add(pagoVenta.getDescuento()));
                pagoVenta.setTotalPago(new BigDecimal(new funciones().redondearMas(total.floatValue(), 2)));
                pagoVenta.setDetallePago("ABONO A FACTURA");
                pagoVenta.setSerieRECIBO(facturaContado.getCliente().getEmpleadoasignado().getSerieRECIBO());
                pagoVenta.setRecibo(numrecibo);
                pagoVenta.setResponsableCobro(facturaContado.getCliente().getEmpleadoasignado());
                pagoVenta.setTipoPago(tipoPago);
                if(!pagoVenta.getTipoPago().equals("EFECTIVO")){
                    pagoVenta.setBanco(banco);
                    pagoVenta.setIdentificador(identificador);
                }
               // if(pagoCorte){
               //     pagoVenta.setEstado("ANTIGUO");
               // }else{
                    pagoVenta.setEstado("REGISTRADO");
                //}
                pagoVenta.setIdpago(ejbFacadePagoVenta.getNextIdPagoVenta());
                pagoVenta.setNota(notaPago);
                ejbFacadePagoVenta.create(pagoVenta); // Registrar Pago en la BD
                facturaContado.getPagoVentaCollection().add(pagoVenta); //Actualizar Contexto
                //Actualizar Factura
                BigDecimal saldo = facturaContado.getSaldo().subtract(pagoVenta.getTotalPago());
                facturaContado.setSaldo(new BigDecimal(new funciones().redondearMas(saldo.floatValue(), 2)));
                if(facturaContado.getSaldo().floatValue() <= 0.02 ){
                    pagoVenta.setDetallePago("CANCELACION DE FACTURA");
                    ejbFacadePagoVenta.edit(pagoVenta);
                    facturaContado.setEstado("CANCELADA");
                    facturaContado.setFechaCancelado(pagoVenta.getFechaPago());
                }
                ejbFacadeFactura.edit(facturaContado);
                new funciones().setMsj(1, "PAGO REGISTRADO CORRECTAMENTE");
                if(facturaContado.getEstado().equals("CANCELADA")){
                    RequestContext context = RequestContext.getCurrentInstance();
                    context.execute("alert('FACTURA CANCELADA POR COMPLETO');");
                }

               
           }catch(Exception e){
               new funciones().setMsj(3, "ERROR AL REGISTRAR PAGO");
           }
       }
   }
   
   //Prepara pago de factura al credito
   public void prepararPago(){
       pagoCorte = true;
       vendedor = null;
       banco = null;
       numrecibo = null;
       identificador = null;
       pago = (float) 0.00;
       descuento = (float) 0.00;
       notaPago = null;
       nuevoSaldo = (float) 0.00;
       pagoValido = false;
       fechaPago = new funciones().getTime();
   }
   
    public void cambiarMontoPago(){
       pago = (float) 0.00;
       descuento = (float) 0.00;
       nuevoSaldo = (float) 0.00;
       pagoValido = false;
   }
   
   
   //Verifica que la cantidad ingresada como pago sea valido
   public void validarPago(){
       RequestContext context = RequestContext.getCurrentInstance(); 
       pago = new funciones().redondearMas(pago,2);
       Date fechaActual = new funciones().getTime();
       if(pago < 0){
           pagoValido = false;
           new funciones().setMsj(3,"Pago no puede ser Negativo");
       }else if(pago == 0){
           new funciones().setMsj(3,"Pago debe ser mayor a 0.00");
           pagoValido = false;
       }else if(fechaPago.after(fechaActual) ){
           new funciones().setMsj(3,"Fecha de Pago Invalida");
           pagoValido = false;
       }else if(!verificarDescuentoAutorizado(false)){
           new funciones().setMsj(3,"Descuento excede el autorizado");
           pagoValido = false;
       }else if(descuento > 0 && new funciones().difDias(facturaCredito.getFacturaPK().getFechaFactura(),fechaPago) > 70){
           new funciones().setMsj(3,"Factura tiene mas de 60 dias ... descuento no procede");
           pagoValido = false;
       }else{
           float saldoActual = new funciones().redondearMas(facturaCredito.getSaldo().floatValue(),2);
           float cantidad = pago + descuento;
           if( cantidad <= saldoActual){
               //PAGO VALIDO
               pagoValido = true;
               //calcular nuevo Saldo
               nuevoSaldo = new funciones().redondearMas(saldoActual - pago - descuento,2);
           }else{
               //pago invalido
               pagoValido = false;
               new funciones().setMsj(2,"PAGO SE EXCEDE DEL SALDO ACTUAL");
           }
       }
       context.addCallbackParam("validar", pagoValido);
       
   }
   /*
    * FUNCION QUE REGISTRA PAGO DE FACTURAS AL CREDITO
    */
    public void registrarPagoFacturaCredito(){
       if(facturaCredito != null){
           try{
               //NUEVA TRANSACCION
                Transaccion transac = new Transaccion();
                transac.setFechaTransac(new funciones().getTime());
                transac.setHoraTransac(new funciones().getTime());
                transac.setResponsableTransac(new JsfUtil().getEmpleado());
                transac.setTipoTransac(5); //REGISTRO DE PAGO
                transac.setIdtransac(ejbFacadeTransac.getNextIdTransac());
                ejbFacadeTransac.create(transac);
                //REGISTRAR PAGO
                PagoVenta pagoVenta = new PagoVenta();
                pagoVenta.setIdtransac(transac);
                pagoVenta.setFechaPago(fechaPago);
                pagoVenta.setFactura(facturaCredito);
                pagoVenta.setInteres(BigDecimal.ZERO);
                pagoVenta.setMora(BigDecimal.ZERO);
                pagoVenta.setSerieRECIBO(facturaCredito.getCliente().getEmpleadoasignado().getSerieRECIBO());
                pagoVenta.setRecibo(numrecibo);
                pagoVenta.setResponsableCobro(facturaCredito.getCliente().getEmpleadoasignado());
                pagoVenta.setTipoPago(tipoPago);
                if(!pagoVenta.getTipoPago().equals("EFECTIVO")){
                    pagoVenta.setBanco(banco);
                    pagoVenta.setIdentificador(identificador);
                }
                //if(pagoCorte){
                //    pagoVenta.setEstado("ANTIGUO");
                //}else{
                    pagoVenta.setEstado("REGISTRADO");
               // }
                pagoVenta.setAbono(new BigDecimal(pago));
                pagoVenta.setDescuento(new BigDecimal(descuento));
                BigDecimal total = pagoVenta.getAbono().add(pagoVenta.getInteres()).add(pagoVenta.getMora().add(pagoVenta.getDescuento()));
                pagoVenta.setTotalPago(new BigDecimal(new funciones().redondearMas(total.floatValue(), 2)));
                pagoVenta.setDetallePago("ABONO A FACTURA");
                pagoVenta.setNota(notaPago);
                pagoVenta.setIdpago(ejbFacadePagoVenta.getNextIdPagoVenta());
                ejbFacadePagoVenta.create(pagoVenta); // Registrar Pago en la BD
                //Actualizar Factura
                facturaCredito.getPagoVentaCollection().add(pagoVenta); //Actualizar Contexto
                BigDecimal saldo = facturaCredito.getSaldo().subtract(pagoVenta.getTotalPago());
                facturaCredito.setSaldo(new BigDecimal(new funciones().redondearMas(saldo.floatValue(), 2)));
                if(facturaCredito.getSaldo().floatValue() <= 0.02){
                    pagoVenta.setDetallePago("CANCELACION DE FACTURA");
                    ejbFacadePagoVenta.edit(pagoVenta);
                    facturaCredito.setEstado("CANCELADA");
                    facturaCredito.setFechaCancelado(pagoVenta.getFechaPago());
                }
                ejbFacadeFactura.edit(facturaCredito);
                new funciones().setMsj(1, "PAGO REGISTRADO CORRECTAMENTE");
                if(facturaCredito.getEstado().equals("CANCELADA")){
                    RequestContext context = RequestContext.getCurrentInstance(); 
                    context.execute("alert('FACTURA CANCELADA POR COMPLETO');");
                }
                prepararPago();
           }catch(Exception e){
               new funciones().setMsj(3, "ERROR AL REGISTRAR PAGO");
           }
       }
   }
    
    
    
    
    
    /*
     * CONSULTA DE FACTURAS
     */
    private int month;
    private int year;
    
    private List<Factura> facturasEmitidas;
    
    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public List<Factura> getFacturasEmitidas() {
        return facturasEmitidas;
    }

    public void setFacturasEmitidas(List<Factura> facturasEmitidas) {
        this.facturasEmitidas = facturasEmitidas;
    }
    
    
    
    public void prepararConsultaFacturasEmitidas(){
        Calendar hoy = Calendar.getInstance();
        year = hoy.get(Calendar.YEAR);
        month = hoy.get(Calendar.MONTH) +1;
        actualizarConsultaFacturasEmitidas();
    }
    
    
    public void actualizarConsultaFacturasEmitidas(){
        facturasEmitidas = ejbFacadeFactura.consultarFacturasEmitidas(month, year);
        filtroFacturas = null;
    }
    
    public void prepararConsultaVentas(){
        Calendar hoy = Calendar.getInstance();
        year = hoy.get(Calendar.YEAR);
        month = hoy.get(Calendar.MONTH) +1;
        actualizarConsultaVentas();
    }
    
    
    public void actualizarConsultaVentas(){
        ventas = ejbFacadeFactura.listaVenta(month, year);
        filtroFacturas = null;
    }
    
    
    
    
    private boolean pagoCorte = false;

    public boolean isPagoCorte() {
        return pagoCorte;
    }

    public void setPagoCorte(boolean pagoCorte) {
        this.pagoCorte = pagoCorte;
    }
    
    
    
    //PAGOS POR VENDEDOR
    
    private List<Factura> facturasContadoActivasVendedor;
    private List<Factura> facturasCreditoActivasVendedor;

    public List<Factura> getFacturasContadoActivasVendedor() {
        facturasContadoActivasVendedor = ejbFacadeFactura.facturasContadoActivaVendedor(new JsfUtil().getEmpleado());
        return facturasContadoActivasVendedor;
    }

    public void setFacturasContadoActivasVendedor(List<Factura> facturasContadoActivasVendedor) {
        this.facturasContadoActivasVendedor = facturasContadoActivasVendedor;
    }

    public List<Factura> getFacturasCreditoActivasVendedor() {
        facturasCreditoActivasVendedor = ejbFacadeFactura.facturasCreditoActiva(new JsfUtil().getEmpleado());
        return facturasCreditoActivasVendedor;
    }

    public void setFacturasCreditoActivasVendedor(List<Factura> facturasCreditoActivasVendedor) {
        this.facturasCreditoActivasVendedor = facturasCreditoActivasVendedor;
    }
    
    
    
    public void prepararPagoVendedor(){
        pagoCorte = false;
        vendedor = new JsfUtil().getEmpleado();
        banco = null;
        numrecibo = null;
        identificador = null;
        pago = (float) 0.00;
        descuento = (float) 0.00;
        notaPago = null;
        nuevoSaldo = (float) 0.00;
        pagoValido = false;
        fechaPago = new funciones().getTime();
    }
    
    
    
    
    
    
    
    
    
    
    /** FUNCIONES TEMPORALES PARA INGRESAR HISTORIAL DEL CLIENTE **/
    
    private Factura facturaAnterior;
    private List<PagoVenta> pagosFacturaAnterior;
    private PagoVenta newItem;
    private BigDecimal totalPagos;

    public Factura getFacturaAnterior() {
        if(facturaAnterior == null){
            facturaAnterior = new Factura();
            facturaAnterior.setFacturaPK(new FacturaPK());
            pagosFacturaAnterior = new ArrayList<PagoVenta>();
        }
        return facturaAnterior;
    }

    
    public void setFacturaAnterior(Factura facturaAnterior) {
        this.facturaAnterior = facturaAnterior;
    }

    public List<PagoVenta> getPagosFacturaAnterior() {
        return pagosFacturaAnterior;
    }

    public void setPagosFacturaAnterior(List<PagoVenta> pagosFacturaAnterior) {
        this.pagosFacturaAnterior = pagosFacturaAnterior;
    }

    public PagoVenta getNewItem() {
        if(newItem == null){
            newItem = new PagoVenta();
        }
        return newItem;
    }

    public void setNewItem(PagoVenta newItem) {
        this.newItem = newItem;
    }

    public BigDecimal getTotalPagos() {
        return totalPagos;
    }

    public void setTotalPagos(BigDecimal totalPagos) {
        this.totalPagos = totalPagos;
    }
    
    
    
    public void prepareFacturaAnterior(){
        facturaAnterior = new Factura();
        facturaAnterior.setFacturaPK(new FacturaPK());
        facturaAnterior.setSumas(BigDecimal.ZERO);
        facturaAnterior.setPorcentajeDescuento((float)0.00);
        facturaAnterior.setDescuento(BigDecimal.ZERO);
        facturaAnterior.setSubTotal(BigDecimal.ZERO);
        facturaAnterior.setIva(BigDecimal.ZERO);
        facturaAnterior.setTotal(BigDecimal.ZERO);
        facturaAnterior.setSaldo(BigDecimal.ZERO);
        pagosFacturaAnterior = new ArrayList<PagoVenta>();
    }
    
    
    public void registrarFacturaAnterior(){
        RequestContext context = RequestContext.getCurrentInstance();
        if(facturaAnterior != null){
            try{
                if(validarDatosFacturaAnterior()){
                    if(facturaAnterior.getEstado().equals("CANCELADA")){
                        long diasTranscurridos = new funciones().difDias(facturaAnterior.getFacturaPK().getFechaFactura(), facturaAnterior.getFechaCancelado());
                        facturaAnterior.setPeriodoPago((int)diasTranscurridos);
                    }
                    ejbFacadeFactura.create(facturaAnterior);
                    for(PagoVenta pago: pagosFacturaAnterior){
                        pago.setIdpago(ejbFacadePagoVenta.getNextIdPagoVenta());
                        pago.setFactura(facturaAnterior);
                        ejbFacadePagoVenta.create(pago);
                    }
                    new funciones().setMsj(1, "FACTURA: " + facturaAnterior.getFacturaPK().getNumfactura() +" INGRESADA");
                    prepareFacturaAnterior();
                    context.execute("PF('facturaAnterior').hide();");
                }
            }catch(Exception e){
                
            }
        }
    }
    
    
    public boolean validarDatosFacturaAnterior(){
        boolean valido = true;
        if(facturaAnterior != null){
            if(ejbFacadeFactura.existeFactura(facturaAnterior.getFacturaPK())){
                valido = false;
                new funciones().setMsj(3, "FACTURA YA EXISTE EN EL SISTEMA");
            }
            if(facturaAnterior.getSumas().compareTo(BigDecimal.ZERO) <= 0){
                valido = false;
                new funciones().setMsj(3, "SUMAS DEBE SER > 0");
            }
            if(facturaAnterior.getPorcentajeDescuento() < 0){
                valido = false;
                new funciones().setMsj(3, "% DESCUENTO DEBE SER >= 0");
            }
            if(facturaAnterior.getDescuento().compareTo(BigDecimal.ZERO) < 0){
                valido = false;
                new funciones().setMsj(3, "DESCUENTO DEBE SER >= 0");
            }
            if(facturaAnterior.getSubTotal().compareTo(BigDecimal.ZERO) <= 0){
                valido = false;
                new funciones().setMsj(3, "SUBTOTAL DEBE SER > 0");
            }
            if(facturaAnterior.getIva().compareTo(BigDecimal.ZERO) < 0){
                valido = false;
                new funciones().setMsj(3, "IVA DEBE SER >= 0");
            }
            if(facturaAnterior.getTotal().compareTo(BigDecimal.ZERO) <= 0){
                valido = false;
                new funciones().setMsj(3, "TOTAL DEBE SER > 0");
            }
            if(facturaAnterior.getSaldo().compareTo(BigDecimal.ZERO) < 0){
                valido = false;
                new funciones().setMsj(3, "SALDO DEBE SER >= 0");
            }
            if(facturaAnterior.getTipoFactura().equals("CCF")){
                if(facturaAnterior.getIva().compareTo(BigDecimal.ZERO) <= 0){
                    valido = false;
                    new funciones().setMsj(3, "IVA DEBE SER > 0");
                }
            }
            if(facturaAnterior.getEstado().equals("ACTIVA")){
                if(facturaAnterior.getSaldo().compareTo(BigDecimal.ZERO) <= 0){
                    valido = false;
                    new funciones().setMsj(3, "SALDO DEBE SER > 0");
                }
            }
        }else{
            valido =false;
        }
        return valido;
    }
    
    public void prepareRegistrarPagoAnterior(){
        newItem = new PagoVenta();
        newItem.setAbono(BigDecimal.ZERO);
        newItem.setDescuento(BigDecimal.ZERO);
        newItem.setDetallePago("ABONO");
        newItem.setEstado("ANTIGUO");
        newItem.setIdentificador(null);
        newItem.setIdtransac(null);
        newItem.setRemesa(null);
    }
    
    
    public void registrarPagoAnterior(){
        boolean validar = true;
        if(newItem.getAbono().compareTo(BigDecimal.ZERO) < 0){
            validar = false;
        }
        if(newItem.getDescuento().compareTo(BigDecimal.ZERO) < 0){
            validar = false;
        }
        if(validar){
            RequestContext context = RequestContext.getCurrentInstance();
            newItem.setTotalPago(newItem.getAbono().add(newItem.getDescuento()));
            pagosFacturaAnterior.add(newItem);
            actualizarSaldoActual();
            context.execute("nuevoPago.hide();");
        }else{
            new funciones().setMsj(3, "ABONO o DESCUENTO NO VALIDO");
        }
        
    }
    
    public void actualizarSaldoActual(){
        BigDecimal nuevoSaldo = BigDecimal.ZERO;
        totalPagos = BigDecimal.ZERO;
        for(PagoVenta actual : pagosFacturaAnterior){
            totalPagos = totalPagos.add(actual.getTotalPago());
        }
        nuevoSaldo = facturaAnterior.getTotal().subtract(totalPagos);
        facturaAnterior.setSaldo(nuevoSaldo);
    }
    
    public void limpiarPagos(){
        pagosFacturaAnterior.clear();
        actualizarSaldoActual();
    }
    
    
    
    
    
    String pathServletReporteLiquidacion;
    InputStream stream;
    private StreamedContent fileXLS;

    public StreamedContent getFileXLS() {
        return this.fileXLS;
    }
    
    public String getPathServletReporteLiquidacion() {
        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        pathServletReporteLiquidacion = servletContext.getContextPath() + "/reporteLiquidacion";
        
        return pathServletReporteLiquidacion;
    }
    
    
    
            
            
   
    
    public void reporteLiquidacion(){
        FacesContext context = FacesContext.getCurrentInstance();  
        context.getExternalContext().getSessionMap().put("vendedor",vendedor);
    }
    
    
    public void reporteLiquidacionXLS(){
        try{
            FacesContext context = FacesContext.getCurrentInstance();  
            context.getExternalContext().getSessionMap().put("vendedor",vendedor);
            context.getExternalContext().getSessionMap().put("archivo",stream);
            context.getExternalContext().dispatch("/reporteLiquidacionXLS");
            stream = (InputStream) context.getExternalContext().getSessionMap().get("archivo");
            fileXLS = new DefaultStreamedContent(stream, "application/xls","liquidacion.xls");
        }catch(Exception ex){ } 
    }
    
}
