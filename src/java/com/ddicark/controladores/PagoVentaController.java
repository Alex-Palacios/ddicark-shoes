package com.ddicark.controladores;

import com.ddicark.auxiliares.JsfUtil;
import com.ddicark.auxiliares.funciones;
import com.ddicark.entidades.Empleado;
import com.ddicark.entidades.PagoVenta;
import com.ddicark.jpaFacades.FacturaFacade;
import com.ddicark.jpaFacades.PagoVentaFacade;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.context.RequestContext;

@ManagedBean(name = "pagoVentaController")
@SessionScoped
public class PagoVentaController extends AbstractController<PagoVenta> implements Serializable {

    
    @EJB
    private PagoVentaFacade ejbFacadePagoVenta;
    
    @EJB
    private FacturaFacade ejbFacadeFactura;

    public PagoVentaController() {
        super(PagoVenta.class);
    }
    //Inicializa controlador de persistencia de proveedores
    @PostConstruct
    public void init() {
        super.setFacade(ejbFacadePagoVenta);
    }

    //VARIABLES DE CONTROL
    private boolean mostrarBanco;
    
    private int consultar=1;
    private int month;
    private int year;
    List<PagoVenta> pagos; 
    List<PagoVenta> filtroPagos; 
    
    List<PagoVenta> pagosRegistradosVendedor;
    
     
    private Date fecha;
    
    private Date fechaInicio;
    private Date fechaFin;
    
    

    //GETTERS AND SETTERS
    public boolean isMostrarBanco() {
        return mostrarBanco;
    }

    public void setMostrarBanco(boolean mostrarBanco) {
        this.mostrarBanco = mostrarBanco;
    }

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

    public List<PagoVenta> getPagos() {
        return pagos;
    }

    public void setPagos(List<PagoVenta> pagos) {
        this.pagos = pagos;
    }

    public int getConsultar() {
        return consultar;
    }

    public void setConsultar(int consultar) {
        this.consultar = consultar;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public List<PagoVenta> getFiltroPagos() {
        return filtroPagos;
    }

    public void setFiltroPagos(List<PagoVenta> filtroPagos) {
        this.filtroPagos = filtroPagos;
    }

    public List<PagoVenta> getPagosRegistradosVendedor() {
        return pagosRegistradosVendedor;
    }

    public void setPagosRegistradosVendedor(List<PagoVenta> pagosRegistradosVendedor) {
        this.pagosRegistradosVendedor = pagosRegistradosVendedor;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    
    
    
    
    
    
    /*
     * FUNCIONES GENERALES
     */
    
    public void actualizarVista(String tipopago){
        if(tipopago != null){
            if(tipopago.equals("EFECTIVO")){
                mostrarBanco = false;
            }else{
                mostrarBanco = true;
            }
        }else{
            mostrarBanco = false;
        }
    }
    
    
    public void prepararConsultaPagosVenta(){
        Calendar hoy = Calendar.getInstance();
        vendedor=null;
        fecha = null;
        fechaInicio = new funciones().getTime();
        fechaFin = fechaInicio;
        //year = hoy.get(Calendar.YEAR);
        //month = hoy.get(Calendar.MONTH) +1;
        actualizarConsultaPagos();
    }
    public void actualizarConsultaPagos(){
        if(fechaInicio != null && fechaFin != null){
            if(fechaInicio.compareTo(fechaFin) <= 0){
                if(vendedor != null){
                    pagos = ejbFacadePagoVenta.consultarPagosByRango(fechaInicio, fechaFin,vendedor);
                }else{
                    pagos = ejbFacadePagoVenta.consultarPagosByRango(fechaInicio, fechaFin);
                }
            }else{
                new funciones().setMsj(2, "RANGO DE FECHAS INVALIDO");
            }
            filtroPagos = null;
        }
        
    }
    
    
    //PAGOS VENTA POR VENDEDOR
    public void prepararConsultaPagosVendedor(){
        actualizarConsultaPagosVendedor();
    }
    public void actualizarConsultaPagosVendedor(){
        pagosRegistradosVendedor = ejbFacadePagoVenta.consultarPagosVendedor(new JsfUtil().getEmpleado());
        filtroPagos = null;
    }
    
    
    
    
    
    
    //RECIBIR PAGOS DE VENDEDORES
    private Empleado vendedor;
    private List<PagoVenta> pagosVendedor;
    private List<PagoVenta> listaRecibir;
    private boolean seleccionar;
    private boolean recibirTodos;
    private PagoVenta pagoRechazar;
    private PagoVenta pagoEditarDesc;
    private BigDecimal nuevoDescuento;
    
    private BigDecimal totalAbono;
    private BigDecimal totalDescuentos;
    private BigDecimal totalPagos;
    private BigDecimal totalEfectivo;
    private BigDecimal totalCheques;
    private BigDecimal totalRemesas;

    public Empleado getVendedor() {
        return vendedor;
    }

    public void setVendedor(Empleado vendedor) {
        this.vendedor = vendedor;
    }

    public List<PagoVenta> getPagosVendedor() {
        return pagosVendedor;
    }

    public void setPagosVendedor(List<PagoVenta> pagosVendedor) {
        this.pagosVendedor = pagosVendedor;
    }

    public boolean isSeleccionar() {
        return seleccionar;
    }

    public void setSeleccionar(boolean seleccionar) {
        this.seleccionar = seleccionar;
    }

    public boolean isRecibirTodos() {
        return recibirTodos;
    }

    public void setRecibirTodos(boolean recibirTodos) {
        this.recibirTodos = recibirTodos;
    }

    public PagoVenta getPagoRechazar() {
        return pagoRechazar;
    }

    public void setPagoRechazar(PagoVenta pagoRechazar) {
        this.pagoRechazar = pagoRechazar;
    }

    public PagoVenta getPagoEditarDesc() {
        return pagoEditarDesc;
    }

    public void setPagoEditarDesc(PagoVenta pagoEditarDesc) {
        this.pagoEditarDesc = pagoEditarDesc;
    }

    public BigDecimal getNuevoDescuento() {
        return nuevoDescuento;
    }

    public void setNuevoDescuento(BigDecimal nuevoDescuento) {
        this.nuevoDescuento = nuevoDescuento;
    }

    

    public List<PagoVenta> getListaRecibir() {
        return listaRecibir;
    }

    public void setListaRecibir(List<PagoVenta> listaRecibir) {
        this.listaRecibir = listaRecibir;
    }

    public BigDecimal getTotalAbono() {
        return totalAbono;
    }

    public void setTotalAbono(BigDecimal totalAbono) {
        this.totalAbono = totalAbono;
    }

    public BigDecimal getTotalDescuentos() {
        return totalDescuentos;
    }

    public void setTotalDescuentos(BigDecimal totalDescuentos) {
        this.totalDescuentos = totalDescuentos;
    }

    public BigDecimal getTotalPagos() {
        return totalPagos;
    }

    public void setTotalPagos(BigDecimal totalPagos) {
        this.totalPagos = totalPagos;
    }

    public BigDecimal getTotalEfectivo() {
        return totalEfectivo;
    }

    public void setTotalEfectivo(BigDecimal totalEfectivo) {
        this.totalEfectivo = totalEfectivo;
    }

    public BigDecimal getTotalCheques() {
        return totalCheques;
    }

    public void setTotalCheques(BigDecimal totalCheques) {
        this.totalCheques = totalCheques;
    }

    public BigDecimal getTotalRemesas() {
        return totalRemesas;
    }

    public void setTotalRemesas(BigDecimal totalRemesas) {
        this.totalRemesas = totalRemesas;
    }
    
    
    
    
    
    public void prepararRecibirPagosVendedor(){
        vendedor = null;
        seleccionar = false;
        recibirTodos = false;
        listaRecibir = new ArrayList<PagoVenta>();
        actualizarConsultaPagosVendedores();
    }
    
    public void actualizarConsultaPagosVendedores(){
        pagosVendedor = ejbFacadePagoVenta.consultarPagosVendedor(vendedor);
        filtroPagos = pagosVendedor;
        actualizarSeleccionFiltro();
    }
    
    public void seleccionarTodos(){
        if(recibirTodos){
            listaRecibir = filtroPagos;
            seleccionar = true;
            new funciones().setMsj(1,"TODOS SELECCIONADOS");
        }else{
            listaRecibir = new ArrayList<PagoVenta>();
            seleccionar = false;
            new funciones().setMsj(1,"NINGUNO SELECCIONADO");
        }
    }
    
    public void addListaRecibir(PagoVenta pago){
        if(pago != null){
            if(seleccionar){
                listaRecibir.add(pago);
                new funciones().setMsj(1, listaRecibir.size() +" PAGOS SELECCIONADOS");
            }else{
                listaRecibir.remove(pago);
                new funciones().setMsj(1, listaRecibir.size() +" PAGOS SELECCIONADOS");
            }
        }
    }
    
    public void actualizarSeleccionFiltro(){
        recibirTodos = false;
        seleccionarTodos();
    }
    
    
    public void rechazarPago(){
        if(pagoRechazar != null){
            BigDecimal saldoAnterior = pagoRechazar.getFactura().getSaldo();
            BigDecimal nuevoSaldo = saldoAnterior.add(pagoRechazar.getTotalPago());
            try{
                pagoRechazar.getFactura().setSaldo(new BigDecimal(new funciones().redondearMas(nuevoSaldo.floatValue(), 2)));
                if(pagoRechazar.getFactura().getEstado().equals("CANCELADA")){
                    pagoRechazar.getFactura().setEstado("ACTIVA");
                }
                ejbFacadeFactura.edit(pagoRechazar.getFactura());
                pagoRechazar.setEstado("RECHAZADO");
                ejbFacadePagoVenta.edit(pagoRechazar);
                new funciones().setMsj(1, "PAGO RECHAZADO CORRECTAMENTE");
                new funciones().setMsj(1, "SALDO DE FACTURA ACTUALIZADO");
                actualizarConsultaPagosVendedores();
            }catch(Exception ex){
                pagoRechazar.getFactura().setSaldo(saldoAnterior);
                ejbFacadeFactura.edit(pagoRechazar.getFactura());
            }
            
        }
    }
    
    
    
    public void prepararEditarPago(){
        nuevoDescuento = BigDecimal.ZERO;
    }
    
    
    
    public void editarPagoDesc(){
        RequestContext context = RequestContext.getCurrentInstance(); 
        if(pagoEditarDesc != null){
            BigDecimal saldo = pagoEditarDesc.getFactura().getSaldo().add(pagoEditarDesc.getDescuento());
            saldo = saldo.subtract(nuevoDescuento);
            float resultado =  new funciones().redondearMas(saldo.floatValue(), 2);
            if(resultado >= 0){
                pagoEditarDesc.setDescuento(nuevoDescuento);
                pagoEditarDesc.setTotalPago(pagoEditarDesc.getAbono().add(pagoEditarDesc.getDescuento()));
                ejbFacadePagoVenta.edit(pagoEditarDesc); 
                pagoEditarDesc.getFactura().setSaldo(saldo);
                if(pagoEditarDesc.getFactura().getSaldo().compareTo(BigDecimal.ZERO) == 0){
                    pagoEditarDesc.setDetallePago("CANCELACION DE FACTURA");
                    ejbFacadePagoVenta.edit(pagoEditarDesc);
                    pagoEditarDesc.getFactura().setEstado("CANCELADA");
                    pagoEditarDesc.getFactura().setFechaCancelado(pagoEditarDesc.getFechaPago());
                }else{
                    pagoEditarDesc.setDetallePago("ABONO A FACTURA");
                    ejbFacadePagoVenta.edit(pagoEditarDesc);
                    pagoEditarDesc.getFactura().setEstado("ACTIVA");
                    pagoEditarDesc.getFactura().setFechaCancelado(null);
                }
                ejbFacadeFactura.edit(pagoEditarDesc.getFactura());
                context.addCallbackParam("validar", true);
            }else{
                 new funciones().setMsj(3, "MONTO DESCUENTO EXCEDE SALDO FACTURA");
                 context.addCallbackParam("validar", false);
            }
        }
    }
    
    
    
    public void prepararRecibirPagos(){
        if(listaRecibir != null && !listaRecibir.isEmpty()){
            RequestContext context = RequestContext.getCurrentInstance(); 
            context.execute("recibirPagosDialog.show();");
            actualizarTotalesListaRecibir();
        }else{
            new funciones().setMsj(2, " SELECCIONE LOS PAGOS QUE DESEA CONFIRMAR COMO RECIBIDO");
        }
    }
    
    
    public void actualizarTotalesListaRecibir(){
        totalAbono = BigDecimal.ZERO;
        totalDescuentos  = BigDecimal.ZERO;
        totalPagos = BigDecimal.ZERO;
        totalEfectivo  = BigDecimal.ZERO;
        totalCheques  = BigDecimal.ZERO;
        totalRemesas  = BigDecimal.ZERO;
        for(PagoVenta pv : listaRecibir){
            if(pv.getTipoPago().equals("EFECTIVO")){
                totalAbono = totalAbono.add(pv.getAbono());
                totalDescuentos = totalDescuentos.add(pv.getDescuento());
                totalPagos = totalPagos.add(pv.getTotalPago());
                if(pv.getTipoPago().equals("EFECTIVO")){
                    totalEfectivo = totalEfectivo.add(pv.getAbono());
                }else if(pv.getTipoPago().equals("CHEQUE")){
                    totalCheques = totalCheques.add(pv.getAbono());
                }else if(pv.getTipoPago().equals("REMESA")){
                    totalRemesas = totalRemesas.add(pv.getAbono());
                }
            }
        }
    }
    
    
    
    public void confirmarPagosRecibidos(){
        int numPagos = 0;
        for(PagoVenta actual : listaRecibir){
            if(actual.getTipoPago().equals("EFECTIVO")){
               actual.setEstado("RECIBIDO");
                actual.setFechaRecibido(new funciones().getTime());
                ejbFacadePagoVenta.edit(actual);
                numPagos++;
            }
        }
        new funciones().setMsj(1, numPagos +" PAGOS CONFIRMADOS EXITOSAMENTE");
        actualizarConsultaPagosVendedores();
        listaRecibir = new ArrayList<PagoVenta>();
        seleccionar = false;
        recibirTodos = false;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    //RECIBIR CHEQUES Y REMESAS REGISTRADAS
    private PagoVenta cheque; //cHEQUE O REMESA
    private List<PagoVenta> chequesRegistrados;
    private List<PagoVenta> filtroChequesRegistrados;

    public PagoVenta getCheque() {
        return cheque;
    }

    public void setCheque(PagoVenta cheque) {
        this.cheque = cheque;
    }

    
    public List<PagoVenta> getChequesRegistrados() {
        if(chequesRegistrados == null){
            chequesRegistrados = ejbFacadePagoVenta.consultarChequesRemesasRegistradas();
        }
        return chequesRegistrados;
    }

    public void setChequesRegistrados(List<PagoVenta> chequesRegistrados) {
        this.chequesRegistrados = chequesRegistrados;
    }

    public List<PagoVenta> getFiltroChequesRegistrados() {
        return filtroChequesRegistrados;
    }

    public void setFiltroChequesRegistrados(List<PagoVenta> filtroChequesRegistrados) {
        this.filtroChequesRegistrados = filtroChequesRegistrados;
    }
    
    
    public void prepararChequesRemesasRegistrados(){
        actualizarConsultaChequesRegistrados();
    }
    
    public void actualizarConsultaChequesRegistrados(){
        chequesRegistrados = null;
        filtroChequesRegistrados = chequesRegistrados;
    }
    
    
    public void rechazarChequeRemesa(){
        if(cheque != null){
            BigDecimal saldoAnterior = cheque.getFactura().getSaldo();
            BigDecimal nuevoSaldo = saldoAnterior.add(cheque.getTotalPago());
            try{
                cheque.getFactura().setSaldo(new BigDecimal(new funciones().redondearMas(nuevoSaldo.floatValue(), 2)));
                if(cheque.getFactura().getEstado().equals("CANCELADA")){
                    cheque.getFactura().setEstado("ACTIVA");
                }
                ejbFacadeFactura.edit(cheque.getFactura());
                cheque.setEstado("RECHAZADO");
                ejbFacadePagoVenta.edit(cheque);
                new funciones().setMsj(1, cheque.getTipoPago() +" RECHAZADO CORRECTAMENTE");
                new funciones().setMsj(1, "SALDO DE FACTURA ACTUALIZADO");
            }catch(Exception ex){
                cheque.getFactura().setSaldo(saldoAnterior);
                ejbFacadeFactura.edit(cheque.getFactura());
                cheque.setEstado("REGISTRADO");
                ejbFacadePagoVenta.edit(cheque);
                new funciones().setMsj(3, "ERROR AL RECHAZAR PAGO");
            }
            actualizarConsultaChequesRegistrados();
        }
    }
    
    public void recibirChequeRemesa(){
        if(cheque != null){
            cheque.setEstado("RECIBIDO");
            ejbFacadePagoVenta.edit(cheque);
            new funciones().setMsj(1, cheque.getTipoPago()+" CONFIRMADO EXITOSAMENTE");
            actualizarConsultaChequesRegistrados();   
        }
    }
    
    
    public void editarPago(){
        new funciones().setMsj(1, "PAGO EDITADO");
    }
    
}
