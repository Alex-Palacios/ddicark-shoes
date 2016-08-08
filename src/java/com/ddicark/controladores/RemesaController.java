package com.ddicark.controladores;

import com.ddicark.auxiliares.JsfUtil;
import com.ddicark.auxiliares.funciones;
import com.ddicark.entidades.PagoVenta;
import com.ddicark.entidades.Remesa;
import com.ddicark.entidades.Transaccion;
import com.ddicark.jpaFacades.FacturaFacade;
import com.ddicark.jpaFacades.PagoVentaFacade;
import com.ddicark.jpaFacades.RemesaFacade;
import com.ddicark.jpaFacades.TransaccionFacade;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.context.RequestContext;

@ManagedBean(name = "remesaController")
@SessionScoped
public class RemesaController extends AbstractController<Remesa> implements Serializable {

    
    @EJB
    private RemesaFacade ejbFacadeRemesa;
    
    @EJB
    private PagoVentaFacade ejbFacadePagoVenta;
    
    @EJB
    private FacturaFacade ejbFacadeFactura;
    
    
    @EJB
    private TransaccionFacade ejbFacadeTransac;
    
    public RemesaController() {
        super(Remesa.class);
    }
    
    //Inicializa controlador de persistencia de proveedores
    @PostConstruct
    public void init() {
        super.setFacade(ejbFacadeRemesa);
    }

    
    private int month;
    private int year;
    private List<Remesa> remesas; 
    private List<PagoVenta> pagos;
    private List<PagoVenta> pagosSinRemesar; 
    private List<PagoVenta> pagosRemesa; 
    private List<PagoVenta> listaRemesar;
    private List<PagoVenta> filtroPagos;
    private boolean agregado;
    private List<Boolean> listaSeleccion;
    private boolean selecAll;
    
    private BigDecimal montoTotalRemesa = BigDecimal.ZERO;
    private String banco;
    private String cuenta;
    private String notaRemesa;
    private PagoVenta pv;
    private PagoVenta pvRemesa;
    private PagoVenta pagoRechazar;
    

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

    public List<Remesa> getRemesas() {
        return remesas;
    }

    public void setRemesas(List<Remesa> remesas) {
        this.remesas = remesas;
    }

    public List<PagoVenta> getPagos(Remesa remesa) {
        pagos = ejbFacadePagoVenta.consultarPagos(remesa);
        return pagos;
    }
    
    
    public List<PagoVenta> getPagosSinRemesar() {
        return pagosSinRemesar;
    }

    public void setPagosSinRemesar(List<PagoVenta> pagosSinRemesar) {
        this.pagosSinRemesar = pagosSinRemesar;
    }

    public List<PagoVenta> getListaRemesar() {
        return listaRemesar;
    }

    public void setListaRemesar(List<PagoVenta> listaRemesar) {
        this.listaRemesar = listaRemesar;
    }
 
    public boolean isAgregado() {
        return agregado;
    }

    public void setAgregado(boolean agregado) {
        this.agregado = agregado;
    }

    public boolean isSelecAll() {
        return selecAll;
    }

    public void setSelecAll(boolean selecAll) {
        this.selecAll = selecAll;
    }

    public List<PagoVenta> getFiltroPagos() {
        return filtroPagos;
    }

    public void setFiltroPagos(List<PagoVenta> filtroPagos) {
        this.filtroPagos = filtroPagos;
    }

    
    public BigDecimal getMontoTotalRemesa() {
        return montoTotalRemesa;
    }

    public void setMontoTotalRemesa(BigDecimal montoTotalRemesa) {
        this.montoTotalRemesa = montoTotalRemesa;
    }

    public String getBanco() {
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }

    public String getCuenta() {
        return cuenta;
    }

    public void setCuenta(String cuenta) {
        this.cuenta = cuenta;
    }

    public String getNotaRemesa() {
        return notaRemesa;
    }

    public void setNotaRemesa(String notaRemesa) {
        this.notaRemesa = notaRemesa;
    }

    

    public PagoVenta getPv() {
        return pv;
    }

    public void setPv(PagoVenta pv) {
        this.pv = pv;
    }

    public PagoVenta getPvRemesa() {
        return pvRemesa;
    }

    public void setPvRemesa(PagoVenta pvRemesa) {
        this.pvRemesa = pvRemesa;
    }

    
    public List<PagoVenta> getPagosRemesa() {
        return pagosRemesa;
    }

    public void setPagosRemesa(List<PagoVenta> pagosRemesa) {
        this.pagosRemesa = pagosRemesa;
    }

    public PagoVenta getPagoRechazar() {
        return pagoRechazar;
    }

    public void setPagoRechazar(PagoVenta pagoRechazar) {
        this.pagoRechazar = pagoRechazar;
    }
    
    
    
    
    
    
    public void prepararConsultaRemesas(){
        Calendar hoy = Calendar.getInstance();
        year = hoy.get(Calendar.YEAR);
        month = hoy.get(Calendar.MONTH) +1;
        actualizarConsultaRemesas();
    }
    public void actualizarConsultaRemesas(){
        remesas = ejbFacadeRemesa.consultarRemesas(month, year);
    }
    
    
    
    public void prepararConsultaPagosSinRemesar(){
        montoTotalRemesa = BigDecimal.ZERO;
        listaRemesar = new ArrayList<PagoVenta>();
        selecAll = false;
        seleccionarTodos();
        actualizarPagosSinRemesar();
    }
    
    public void actualizarPagosSinRemesar(){
        pagosSinRemesar = ejbFacadePagoVenta.consultarPagosSinRemesar();
        pagosRemesa = ejbFacadePagoVenta.consultarPagosRemesaSinVerificar();
        filtroPagos = null;
    }

    
    
    //Seleccionar/Deseleccionar Todos
    public void seleccionarTodos(){
        if(selecAll){
            listaRemesar.clear();
            for(int i=0; i < pagosSinRemesar.size();i++){
                listaRemesar.add(pagosSinRemesar.get(i));
            }
            agregado = true;
            actualizarMontoRemesa();
            new funciones().setMsj(1," SELECCIONADOS TODOS");
        }else{
            listaRemesar.clear();
            agregado = false;
            actualizarMontoRemesa();
            new funciones().setMsj(1,"NINGUNO SELECCIONADO");
        }
    }
    
    //Agrega uno a la lista de seleccion
    public void agregarAlista(PagoVenta pv){
        if(pv != null){
            if(agregado){
                listaRemesar.add(pv);
            }else{
                listaRemesar.remove(pv);
            }
            actualizarMontoRemesa();
        }
    }
    
    
    public void actualizarMontoRemesa(){
        montoTotalRemesa = BigDecimal.ZERO;
        if(listaRemesar != null){
            for(PagoVenta actual : listaRemesar){
                montoTotalRemesa = montoTotalRemesa.add(actual.getTotalPago());
            }
        }
    }
    
    public boolean listado(PagoVenta pv){
        return listaRemesar.contains(pv);
    }
    
    
    public void nuevaRemesa(){
        banco = null;
        cuenta = null;
        notaRemesa = null;
        if(listaRemesar == null || listaRemesar.isEmpty()){
            new funciones().setMsj(3, "NO HA SELECCIONADO PAGOS A REMESAR");
        }else{
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("registrarRemesaDialog.show();");
        }
    }
    
    public void registrarRemesa(){
        RequestContext context = RequestContext.getCurrentInstance();
        //NUEVA TRANSACCION
        try{
            Transaccion transac = new Transaccion();
            transac.setFechaTransac(new funciones().getTime());
            transac.setHoraTransac(new funciones().getTime());
            transac.setResponsableTransac(new JsfUtil().getEmpleado());
            transac.setTipoTransac(6); //REMESAR
            transac.setIdtransac(ejbFacadeTransac.getNextIdTransac());
            ejbFacadeTransac.create(transac);
            Remesa remesa = new Remesa();
            remesa.setIdtransac(transac);
            remesa.setBanco(banco);
            remesa.setCuenta(cuenta);
            remesa.setTotalRemesa(montoTotalRemesa);
            remesa.setNota(notaRemesa);
            remesa.setEstado("REGISTRADA");
            remesa.setIdremesa(ejbFacadeRemesa.getNextIdRemesa());
            ejbFacadeRemesa.create(remesa);
            for(PagoVenta actual : listaRemesar){
                actual.setRemesa(remesa);
                actual.setEstado("REMESADO");
                ejbFacadePagoVenta.edit(actual);
            }
            new funciones().setMsj(1,"SE HA REGISTRADO LA REMESA SATISFACTORIAMENTE");
            context.addCallbackParam("validar", true);
            prepararConsultaPagosSinRemesar();
        }catch(Exception ex){
            new funciones().setMsj(3, "ERROR AL REGISTRAR REMESA CONSULTE CON EL ADMINISTRADOR");
            new funciones().setMsj(2, " DETALLE : " + ex.getMessage());
            context.addCallbackParam("validar", false);
        }
        
    }
    
    
    
    public void confirmarRemesa(){
        //NUEVA TRANSACCION
        try{
            Transaccion transac = new Transaccion();
            transac.setFechaTransac(new funciones().getTime());
            transac.setHoraTransac(new funciones().getTime());
            transac.setResponsableTransac(new JsfUtil().getEmpleado());
            transac.setTipoTransac(6); //REMESAR
            transac.setIdtransac(ejbFacadeTransac.getNextIdTransac());
            ejbFacadeTransac.create(transac);
            Remesa remesa = new Remesa();
            remesa.setIdtransac(transac);
            remesa.setBanco(pvRemesa.getBanco());
            remesa.setCuenta(pvRemesa.getIdentificador());
            remesa.setTotalRemesa(pvRemesa.getTotalPago());
            //remesa.setNota(notaRemesa);
            remesa.setEstado("REGISTRADA");
            remesa.setIdremesa(ejbFacadeRemesa.getNextIdRemesa());
            ejbFacadeRemesa.create(remesa);
            pvRemesa.setRemesa(remesa);
            pvRemesa.setEstado("REMESADO");
            ejbFacadePagoVenta.edit(pvRemesa);
            new funciones().setMsj(1,"SE HA REGISTRADO LA REMESA SATISFACTORIAMENTE");
            prepararConsultaPagosSinRemesar();
        }catch(Exception ex){
            new funciones().setMsj(3, "ERROR AL CONFIRMAR REMESA CONSULTE CON EL ADMINISTRADOR");
            new funciones().setMsj(2, " DETALLE : " + ex.getMessage());
        }
        
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
                prepararConsultaPagosSinRemesar();
            }catch(Exception ex){
                pagoRechazar.getFactura().setSaldo(saldoAnterior);
                ejbFacadeFactura.edit(pagoRechazar.getFactura());
            }
            
        }
    }
}
