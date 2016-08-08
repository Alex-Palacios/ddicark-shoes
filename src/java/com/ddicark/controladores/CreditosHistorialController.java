package com.ddicark.controladores;


import com.ddicark.auxiliares.JsfUtil;
import com.ddicark.auxiliares.funciones;
import com.ddicark.entidades.Cliente;
import com.ddicark.entidades.Factura;
import com.ddicark.entidades.PagoVenta;
import com.ddicark.entidades.Pedido;
import com.ddicark.jpaFacades.ClienteFacade;
import com.ddicark.jpaFacades.FacturaFacade;
import com.ddicark.jpaFacades.PagoVentaFacade;
import com.ddicark.jpaFacades.PedidoFacade;
import com.ddicark.servlets.ItemCredito;
import com.ddicark.servlets.ItemCuenta;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name = "creditosHistorialController")
@SessionScoped
public class CreditosHistorialController implements Serializable {

    @EJB
    private ClienteFacade ejbFacadeCliente;
    
    @EJB
    private PedidoFacade ejbFacadePedido;
    
    @EJB
    private FacturaFacade ejbFacadeFactura;
    
    @EJB
    private PagoVentaFacade ejbFacadePagoVenta;
    
    public CreditosHistorialController() {
    }
    
    //Variables Control
    private List<Pedido> solicitudesDeCredito;
    private Pedido solicitud;
    private Cliente clienteHistorial;
    private List<Factura> creditosActivos;
    private float totalCreditos;
    private float totalDeuda;
    private int numCreditosActivos;
    private List<Factura> creditosAnteriores;
    private float totalPagado;
    private float promedioDiasPago;
    private int numCreditosAnteriores;
    private List<Factura> comprasContado;
    private float totalCompras;
    private int numComprasContado;

    private String pathServletEstadoCuenta;
    
    private List<ItemCuenta> estadoCuentaItems = new ArrayList<ItemCuenta>();
    private List<ItemCredito> creditosItems = new ArrayList<ItemCredito>();
    
    
    
    //GET AND SET
    public String getPathServletEstadoCuenta() {
        pathServletEstadoCuenta = JsfUtil.getPathContext() + "/estadoCuenta";
        return pathServletEstadoCuenta;
    }
    
     public List<Pedido> getSolicitudesDeCredito() {
        solicitudesDeCredito = ejbFacadePedido.getSolicitudesCredito();
        return solicitudesDeCredito;
    }
    
   
    public Pedido getSolicitud() {
        return solicitud;
    }

    public void setSolicitud(Pedido solicitud) {
        this.solicitud = solicitud;
    }

    public Cliente getClienteHistorial() {
        return clienteHistorial;
    }

    public void setClienteHistorial(Cliente clienteHistorial) {
        this.clienteHistorial = clienteHistorial;
    }

    public List<Factura> getCreditosActivos() {
        return creditosActivos;
    }

    public void setCreditosActivos(List<Factura> creditosActivos) {
        this.creditosActivos = creditosActivos;
    }

    public float getTotalCreditos() {
        return totalCreditos;
    }

    public void setTotalCreditos(float totalCreditos) {
        this.totalCreditos = totalCreditos;
    }

    public float getTotalDeuda() {
        return totalDeuda;
    }

    public void setTotalDeuda(float totalDeuda) {
        this.totalDeuda = totalDeuda;
    }

    public List<Factura> getCreditosAnteriores() {
        return creditosAnteriores;
    }

    public void setCreditosAnteriores(List<Factura> creditosAnteriores) {
        this.creditosAnteriores = creditosAnteriores;
    }

    public float getTotalPagado() {
        return totalPagado;
    }

    public void setTotalPagado(float totalPagado) {
        this.totalPagado = totalPagado;
    }

    public float getPromedioDiasPago() {
        return promedioDiasPago;
    }

    public void setPromedioDiasPago(float promedioDiasPago) {
        this.promedioDiasPago = promedioDiasPago;
    }

    public List<Factura> getComprasContado() {
        return comprasContado;
    }

    public void setComprasContado(List<Factura> comprasContado) {
        this.comprasContado = comprasContado;
    }

    public float getTotalCompras() {
        return totalCompras;
    }

    public void setTotalCompras(float totalCompras) {
        this.totalCompras = totalCompras;
    }

    public int getNumCreditosActivos() {
        return numCreditosActivos;
    }

    public void setNumCreditosActivos(int numCreditosActivos) {
        this.numCreditosActivos = numCreditosActivos;
    }

    public int getNumCreditosAnteriores() {
        return numCreditosAnteriores;
    }

    public void setNumCreditosAnteriores(int numCreditosAnteriores) {
        this.numCreditosAnteriores = numCreditosAnteriores;
    }

    public int getNumComprasContado() {
        return numComprasContado;
    }

    public void setNumComprasContado(int numComprasContado) {
        this.numComprasContado = numComprasContado;
    }
    
    
    
    
    /*
     * FUNCIONES GENERALES CREDITOS
     */
   
    //APRUEBA EL CREDITO ACTUAL
   public void aprobarCredito(){
       if(solicitud != null){
           solicitud.setAprobadoPor(new JsfUtil().getEmpleado());
           solicitud.setEstadoPedido("APROBADO");
           ejbFacadePedido.edit(solicitud);
           new funciones().setMsj(1, "CREDITO APROBADO");
       }
   }
   
   //RECHAZA EL CREDITO ACTUAL
   public void rechazarCredito(){
       if(solicitud != null){
           solicitud.setAprobadoPor(new JsfUtil().getEmpleado());
           solicitud.setEstadoPedido("DENEGADO");
           ejbFacadePedido.edit(solicitud);
           new funciones().setMsj(1, "CREDITO DENEGADO");
       }
   }
      
     
    /*
     * FUNCIONES GENERALES HISTORIAL
     */

    /*
     * FUNCION QUE PREPARA EL HISTORIAL DE UN CLIENTE
     */
    public void mostrarHistorialCliente(Cliente cliente){
        //Resetear historial
        resetHistorial();
        clienteHistorial = cliente;
        //LLenar Historial
        creditosActivos = ejbFacadeFactura.facturasClienteCredito(cliente, "ACTIVA");
        //calcular indices creditos activos
        calcularIndicesCreditosActivos();
        creditosAnteriores = ejbFacadeFactura.facturasClienteCredito(cliente, "CANCELADA");
        //calcular indices creditos anteriores
        calcularIndicesCreditosAnteriores();
        comprasContado = ejbFacadeFactura.facturasClienteContado(cliente);
        //calcular indices compras al contado
        calcularIndicesComprasContado();
        //Mostrar Historial
        new funciones().irA("faces/vistas/creditos/historial.xhtml");
    }
    
    /*
     * FUNCION QUE CALCULA INDICES DE CREDITOS ACTIVOS
     */
    
    public void calcularIndicesCreditosActivos(){
        BigDecimal sumaTotal = BigDecimal.ZERO;
        BigDecimal sumaSaldo = BigDecimal.ZERO;
        numCreditosActivos = 0;
        if(creditosActivos != null){
            for(Factura actual : creditosActivos){
                sumaTotal = sumaTotal.add(actual.getTotal());
                sumaSaldo = sumaSaldo.add(actual.getSaldo());
            }
            totalCreditos = new funciones().redondearMas(sumaTotal.floatValue(), 2);
            totalDeuda = new funciones().redondearMas(sumaSaldo.floatValue(), 2);
            numCreditosActivos = creditosActivos.size();
        }
    }
    
    /*
     * FUNCION QUE CALCULA INDICES DE CREDITOS ANTERIORES
     */
    
    public void calcularIndicesCreditosAnteriores(){
        BigDecimal sumaTotal = BigDecimal.ZERO;
        promedioDiasPago = 0;
        numCreditosAnteriores = 0;
        if(creditosAnteriores != null){
            long sumaDias = 0;
            for(Factura actual : creditosAnteriores){
                sumaTotal = sumaTotal.add(actual.getTotal());
                //long dif = new funciones().difDias(actual.getFacturaPK().getFechaFactura(), actual.getFechaCancelado());
                if(actual.getPeriodoPago() != null){
                    sumaDias = sumaDias + actual.getPeriodoPago();
                }
                else{
                    long dif = new funciones().difDias(actual.getFacturaPK().getFechaFactura(), actual.getFechaCancelado());
                    actual.setPeriodoPago((int)dif);
                    ejbFacadeFactura.edit(actual);
                    sumaDias = sumaDias + dif;
                }
            }
            totalPagado = new funciones().redondearMas(sumaTotal.floatValue(), 2);
            numCreditosAnteriores = creditosAnteriores.size();
            if(numCreditosAnteriores > 0){
                promedioDiasPago = sumaDias / numCreditosAnteriores;
                promedioDiasPago = new funciones().redondearMas(promedioDiasPago, 0);
            }
        }
    }
    
    /*
     * FUNCION QUE CALCULA INDICES DE COMPRAS CONTADO
     */
    
    public void calcularIndicesComprasContado(){
        BigDecimal sumaTotal = BigDecimal.ZERO;
        numComprasContado = 0;
        if(comprasContado != null){
            for(Factura actual : comprasContado){
                sumaTotal = sumaTotal.add(actual.getTotal());
            }
            totalCompras = new funciones().redondearMas(sumaTotal.floatValue(), 2);
            numComprasContado = comprasContado.size();
        }
    }
    
    //Resetea el historial
    public void resetHistorial(){
        creditosActivos = null;
        totalCreditos = (float) 0.00;
        totalDeuda = (float) 0.00;
        numCreditosActivos = 0;
        creditosAnteriores = null;
        totalPagado = (float) 0.00;
        promedioDiasPago = (float) 0.00;
        numCreditosAnteriores = 0;
        comprasContado = null;
        totalCompras = (float) 0.00;
        numComprasContado = 0;
    }
    
    
    public void prepararEstadoCuenta(){
        List<Factura> cuenta = new ArrayList<Factura>();
        List<Factura> pendientes = new ArrayList<Factura>();
        estadoCuentaItems.clear();
        creditosItems.clear();
        if(clienteHistorial != null){
            cuenta = ejbFacadeFactura.facturasActivasCliente(clienteHistorial);
            BigDecimal saldoAcumulado = BigDecimal.ZERO;
            for(Factura f: cuenta){
                ItemCuenta item = new ItemCuenta();
                item.setFECHA(f.getFacturaPK().getFechaFactura());
                item.setTRANSACCION("FACTURA");
                item.setDOCUMENTO(f.getTipoFactura()+" "+f.getFacturaPK().getNumfactura());
                item.setDEBITO(null);
                item.setCREDITO(f.getTotal());
                saldoAcumulado = saldoAcumulado.add(f.getTotal());
                item.setSALDO(saldoAcumulado);
                estadoCuentaItems.add(item);
                List<PagoVenta> pagos = ejbFacadePagoVenta.pagosFactura(f);
                for(PagoVenta p: pagos){
                    ItemCuenta itemPago = new ItemCuenta();
                    itemPago.setFECHA(p.getFechaPago());
                    itemPago.setTRANSACCION("PAGO");
                    itemPago.setDOCUMENTO(p.getRecibo());
                    itemPago.setDEBITO(p.getTotalPago());
                    itemPago.setCREDITO(null);
                    saldoAcumulado = saldoAcumulado.subtract(p.getTotalPago());
                    itemPago.setSALDO(saldoAcumulado);
                    estadoCuentaItems.add(itemPago);
                }
            }
            JsfUtil.putObjectSession("datosEstadoCuenta", estadoCuentaItems );
            
            //pendientes = ejbFacadeFactura.facturasActivasCliente(clienteHistorial);
            for(Factura activo: cuenta ){
                ItemCredito itemCredito = new ItemCredito();
                itemCredito.setFACTURA(activo.getTipoFactura() +" "+activo.getFacturaPK().getNumfactura());
                itemCredito.setFECHA(activo.getFacturaPK().getFechaFactura());
                itemCredito.setTOTAL(activo.getTotal());
                itemCredito.setSALDO(activo.getSaldo());
                itemCredito.setDIAS((int) new funciones().difDias(activo.getFacturaPK().getFechaFactura()));
                creditosItems.add(itemCredito);
            }
            JsfUtil.putObjectSession("datosCredito", creditosItems );
            JsfUtil.putObjectSession("cliente", clienteHistorial );
        }
        
    }
}
