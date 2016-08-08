package com.ddicark.controladores;

import com.ddicark.entidades.FacturaIngreso;
import com.ddicark.auxiliares.JsfUtil;
import com.ddicark.auxiliares.funciones;
import com.ddicark.entidades.PagoCompra;
import com.ddicark.entidades.Proveedor;
import com.ddicark.entidades.Transaccion;
import com.ddicark.jpaFacades.FacturaIngresoFacade;
import com.ddicark.jpaFacades.PagoCompraFacade;
import com.ddicark.jpaFacades.TransaccionFacade;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.SelectItem;
import org.primefaces.context.RequestContext;

@ManagedBean(name = "facturaIngresoController")
@SessionScoped
public class FacturaIngresoController extends AbstractController<FacturaIngreso> implements Serializable {

    @EJB
    private FacturaIngresoFacade ejbFacadeFacturaIngreso;
    
    @EJB
    private PagoCompraFacade ejbFacadePagoCompra;
    
    @EJB
    private TransaccionFacade ejbFacadeTransac;
    
    private String[] condicionPagoItems;   
    private SelectItem[] condicionPagoOptions;  

    //Variables de control
    private List<FacturaIngreso> facturasPreIngreso;
    private List<FacturaIngreso> facturasProcesadas;
    private List<FacturaIngreso> facturasAnterioresProveedor;
    private List<FacturaIngreso> facturasCreditosActivos;
    private List<FacturaIngreso> compras;
    
    public FacturaIngresoController() {
       super(FacturaIngreso.class);
    }
    //Inicializa controlador de persistencia de proveedores
    @PostConstruct
    public void init() {
        super.setFacade(ejbFacadeFacturaIngreso);
    }

    @Override
    protected void setEmbeddableKeys() {
        //Ingresa los valores de los campos que forman parte de una llave primaria y al mismo tiempo son foraneas
    }

    @Override
    protected void initializeEmbeddableKey() {
        this.getSelected().setFacturaIngresoPK(new com.ddicark.entidades.FacturaIngresoPK());
    }
    
    //VARIBLES DE CONTROL PAGOS DEUDA
    private FacturaIngreso facturaCredito;
    private float pago;
    private float nuevoSaldo;
    private float intereses;
    private float mora;
    private boolean pagoValido = false;
    
    //GETTERS AND SETTERS
    public List<FacturaIngreso> getFacturasPreIngreso() {
        facturasPreIngreso = ejbFacadeFacturaIngreso.facturasPreIngreso();
        return facturasPreIngreso;
    }

    public List<FacturaIngreso> getFacturasProcesadas() {
        facturasProcesadas = ejbFacadeFacturaIngreso.facturasProcesando();
        return facturasProcesadas;
    }

    public List<FacturaIngreso> getFacturasAnterioresProveedor(Proveedor proveedor) {
        facturasAnterioresProveedor = ejbFacadeFacturaIngreso.facturasAnteriores(proveedor);
        return facturasAnterioresProveedor;
    }

    public List<FacturaIngreso> getFacturasCreditosActivos() {
        facturasCreditosActivos = ejbFacadeFacturaIngreso.facturasCreditosActivos();
        return facturasCreditosActivos;
    }

    public List<FacturaIngreso> getCompras() {
        compras = ejbFacadeFacturaIngreso.facturasCompra();
        return compras;
    }
    
    

    public FacturaIngreso getFacturaCredito() {
        return facturaCredito;
    }

    public void setFacturaCredito(FacturaIngreso facturaCredito) {
        this.facturaCredito = facturaCredito;
    }

    public float getPago() {
        return pago;
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

    public float getIntereses() {
        return intereses;
    }

    public void setIntereses(float intereses) {
        this.intereses = intereses;
    }

    public float getMora() {
        return mora;
    }

    public void setMora(float mora) {
        this.mora = mora;
    }

    
    public boolean isPagoValido() {
        return pagoValido;
    }

    public void setPagoValido(boolean pagoValido) {
        this.pagoValido = pagoValido;
    }

    public SelectItem[] getCondicionPagoOptions() {
        condicionPagoItems = new String[2];  
        condicionPagoItems[0] = "AL CONTADO";  
        condicionPagoItems[1] = "AL CREDITO";
        condicionPagoOptions = new funciones().createFilterOptions(condicionPagoItems);  
        return condicionPagoOptions;
    }
    
    
    
    
    
    /*
     * FUNCIONES PAGOS CREDITOS
     */

     /*
     * Calcula el total de pagos realizados por la EMPRESA de una factura
     */
    public float calcularTotalPagosFactura(FacturaIngreso factura){
        float total = (float) 0.00;
        if(factura != null){
            if(!factura.getPagoCompraCollection().isEmpty()){
                BigDecimal suma = BigDecimal.ZERO;
                for(PagoCompra actual : factura.getPagoCompraCollection()){
                    suma = suma.add(actual.getTotalPagoCompra());
                }
                total = new funciones().redondearMas(suma.floatValue(), 2);
            }
        }
        return total;
    }
    
   //Preparar pago de factura al credito
   public void prepararPago(){
       pago = (float) 0.00;
       nuevoSaldo = (float) 0.00;
       intereses = (float) 0.00;
       mora = (float) 0.00;
       pagoValido = false;
   }
   
   //Verifica que la cantidad ingresada como pago sea valido
   public void validarPago(){
       RequestContext context = RequestContext.getCurrentInstance(); 
       pago = new funciones().redondearMas(pago,2);
       intereses = new funciones().redondearMas(intereses,2);
       mora = new funciones().redondearMas(mora,2);
       if(pago < 0){
           pagoValido = false;
           new funciones().setMsj(3,"Pago no puede ser Negativo");
       }else if(pago == 0){
           new funciones().setMsj(3,"Pago debe ser mayor a 0.00");
           pagoValido = false;
       }else{
           float saldoActual = new funciones().redondearMas(facturaCredito.getSaldoCreditoCompra().floatValue(),2);
           if(pago <= saldoActual){
               if(intereses < 0 || mora < 0){
                   //pago invalido
                   pagoValido = false;
                   new funciones().setMsj(3,"Monto de INTERESES o MORA negativo");
               }else{
                   //PAGO VALIDO
                   pagoValido = true;
                   //calcular nuevo Saldo
                   nuevoSaldo = new funciones().redondearMas(saldoActual - pago,2);
               }
           }else{
               //pago invalido
               pagoValido = false;
               new funciones().setMsj(2,"Pago mayor que SALDO ACTUAL");
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
                transac.setTipoTransac(4); //REGISTRO DE PAGO
                transac.setIdtransac(ejbFacadeTransac.getNextIdTransac());
                ejbFacadeTransac.create(transac);
                //REGISTRAR PAGO
                PagoCompra pagoCompra = new PagoCompra();
                pagoCompra.setFacturaIngreso(facturaCredito);
                pagoCompra.setIdtransac(transac);
                pagoCompra.setInteresPagoCompra(new BigDecimal(intereses));
                pagoCompra.setMoraPagoCompra(new BigDecimal(mora));
                pagoCompra.setAbonoPagoCompra(new BigDecimal(pago));
                BigDecimal total = pagoCompra.getAbonoPagoCompra().add(pagoCompra.getInteresPagoCompra()).add(pagoCompra.getMoraPagoCompra());
                pagoCompra.setTotalPagoCompra(new BigDecimal(new funciones().redondearMas(total.floatValue(), 2)));
                pagoCompra.setIdpagoCompra(ejbFacadePagoCompra.getNextIdPagoCompra());
                ejbFacadePagoCompra.create(pagoCompra); // Registrar Pago en la BD
                //Actualizar Factura
                facturaCredito.getPagoCompraCollection().add(pagoCompra); //Actualizar Contexto
                BigDecimal saldo = facturaCredito.getSaldoCreditoCompra().subtract(pagoCompra.getAbonoPagoCompra());
                facturaCredito.setSaldoCreditoCompra(new BigDecimal(new funciones().redondearMas(saldo.floatValue(), 2)));
                if(facturaCredito.getSaldoCreditoCompra().compareTo(BigDecimal.ZERO)==0){
                    facturaCredito.setEstadoCreditoCompra("CANCELADO");
                    facturaCredito.setFechaCancelado(transac.getFechaTransac());
                }
                facturaCredito.setUltimopagoCreditoCompra(transac.getFechaTransac());
                ejbFacadeFacturaIngreso.edit(facturaCredito);
                new funciones().setMsj(1, "PAGO REGISTRADO CORRECTAMENTE");
                if(facturaCredito.getEstadoCreditoCompra().equals("CANCELADO")){
                    RequestContext context = RequestContext.getCurrentInstance(); 
                    context.execute("alert('FACTURA CANCELADA POR COMPLETO');");
                }
                prepararPago();
           }catch(Exception e){
               new funciones().setMsj(3, "ERROR AL REGISTRAR PAGO");
           }
       }
   }
    
    
    
    
}
