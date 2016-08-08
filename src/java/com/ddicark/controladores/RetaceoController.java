package com.ddicark.controladores;

import com.ddicark.entidades.Retaceo;
import com.ddicark.auxiliares.JsfUtil;
import com.ddicark.auxiliares.funciones;
import com.ddicark.entidades.FacturaIngreso;
import com.ddicark.entidades.FacturaIngresoPK;
import com.ddicark.entidades.Proveedor;
import com.ddicark.entidades.Transaccion;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import org.primefaces.context.RequestContext;

@ManagedBean(name = "retaceoController")
@SessionScoped
public class RetaceoController extends AbstractController<Retaceo> implements Serializable {
    
    @EJB
    private com.ddicark.jpaFacades.TransaccionFacade ejbFacadeTransaccion;
    @EJB
    private com.ddicark.jpaFacades.RetaceoFacade ejbFacadeRetaceo;
    @EJB
    private com.ddicark.jpaFacades.FacturaIngresoFacade ejbFacadeFacturaIngreso;
    @EJB
    private com.ddicark.jpaFacades.ProveedorFacade ejbFacadeProveedor;
    
    //VARIABLES DE CONTROL
    
    private List<FacturaIngreso> facturas = new ArrayList<FacturaIngreso>();; // Almacena Temporalmente las facturas de la importacion mientras no se guarden en la BD
    private FacturaIngreso F = new FacturaIngreso();                    //Variable auxiliar que permite crear y agregar a la lista anterior una factura
    private String tipoFactura;
    private String naturalezaCompra;
    private List<Proveedor> itemsProveedores;
    private List<Retaceo> itemsRetaceosIMP;
    private List<Retaceo> itemsRetaceosNAC;
    private List<Retaceo> itemsRetaceosIMPAprobados;
    private List<Retaceo> itemsRetaceosNACAprobados;
    
    
    public RetaceoController() {
        super(Retaceo.class);
    }
    //Inicializa controlador de persistencia de proveedores
    @PostConstruct
    public void init() {
        super.setFacade(ejbFacadeRetaceo);
    }
    
    
    
    
    //GETTERS AND SETTERS

    public List<FacturaIngreso> getFacturas() {
        return facturas;
    }

    public void setFacturas(List<FacturaIngreso> facturas) {
        this.facturas = facturas;
    }

    public FacturaIngreso getF() {
        return F;
    }

    public void setF(FacturaIngreso F) {
        this.F = F;
    }
    
    public String getTipoFactura() {
        return tipoFactura;
    }

    public void setTipoFactura(String tipoFactura) {
        this.tipoFactura = tipoFactura;
    }

    public String getNaturalezaCompra() {
        return naturalezaCompra;
    }

    public void setNaturalezaCompra(String naturalezaCompra) {
        this.naturalezaCompra = naturalezaCompra;
    }

    public List<Proveedor> getItemsProveedores() {
        if(naturalezaCompra.equals("IMPORTADO")){
            itemsProveedores = ejbFacadeProveedor.getProveedores(false);
        }else{
            itemsProveedores = ejbFacadeProveedor.getProveedores(true);
        }
        return itemsProveedores;
    }

    public List<Retaceo> getItemsRetaceosIMP() {
        itemsRetaceosIMP = ejbFacadeRetaceo.getItemsImportacion(false);
        return itemsRetaceosIMP;
    }

    public List<Retaceo> getItemsRetaceosNAC() {
        itemsRetaceosNAC = ejbFacadeRetaceo.getItemsNacionales(false);
        return itemsRetaceosNAC;
    }

    public List<Retaceo> getItemsRetaceosIMPAprobados() {
        itemsRetaceosIMPAprobados = ejbFacadeRetaceo.getItemsImportacion(true);
        return itemsRetaceosIMPAprobados;
    }

    public List<Retaceo> getItemsRetaceosNACAprobados() {
        itemsRetaceosNACAprobados = ejbFacadeRetaceo.getItemsNacionales(true);
        return itemsRetaceosNACAprobados;
    }

    
    
    
    
    
    
    //FUNCIONES 
    
    
    
    //Prepara todo para una nueva Importacion
    public void nuevaImportacion(ActionEvent event){
        facturas.clear();
        F = null;
        naturalezaCompra = "IMPORTADO";
        this.prepararCrear(event);
    }
    
    //Prepara todo para una nueva Compra Nacional
    public void nuevaCompra(ActionEvent event){
        facturas.clear();
        F = null;
        naturalezaCompra = "NACIONAL";
        this.prepararCrear(event);
    }
    
    //Cancelar Retaceo
    public void cancelarRetaceo(ActionEvent event){
        facturas.clear();
        F = null;
        this.prepararCrear(event);
        new funciones().setMsj(2, "Transaccion Cancelada");
    }
    
    
    //Prepara una nueva factura para agregar a la lista antes de ingresar
    public void newFactura(){
        this.F = new FacturaIngreso(new FacturaIngresoPK());
        this.F.setDescuentoCompra(BigDecimal.ZERO);
        this.F.setSalidaCompra(BigDecimal.ZERO);
    }
    
    //Guarda el proveedor elegido
    public void proveedor(Proveedor p){
        this.F.setProveedor(p);
    }
    
    
    //Agrega una factura a la lista de PRE-INGRESOS
    public void addFactura(){
        if(!ejbFacadeFacturaIngreso.existeFacturaIngreso(F.getFacturaIngresoPK(),tipoFactura)){
            if(!facturaListada(F.getFacturaIngresoPK())){
                //NUEVA FACTURA
                F.setTotalCompra(F.getMontoCompra().add(F.getSalidaCompra()).subtract(F.getDescuentoCompra()));
                //Verificar datos
                if(validarDatosFactura()){
                    F.setNaturalezaCompra(naturalezaCompra);  
                    F.setTipoDocumento(tipoFactura);
                    //Calcula total pagado por la factura
                   
                    if(F.getTipoCompra().equals("AL CREDITO")){  //Si la compra es al credito
                        F.setSaldoCreditoCompra(F.getTotalCompra()); //Actualiza el saldo Inicial con el total de la factura
                        F.setEstadoCreditoCompra("ACTIVO");          //Estado del credito
                    }
                    F.setEstadoFactura("AGREGADA"); //Estado de la factura
                    facturas.add(F);
                    actualizarTotales();
                    new funciones().setMsj(1, "Factura agregada a la lista");
                }
            }else{
                new funciones().setMsj(3, "La factura ya esta listada");
            }
        }else{
            new funciones().setMsj(3, "La factura ya existe en el sistema");
        }
        
    }
    
    
    /*
     * VERIFICA QUE LA FACTURA NO ESTE LISTADA
     */
    public boolean facturaListada(FacturaIngresoPK factura){
        boolean existe = false;
        FacturaIngresoPK consulta = new FacturaIngresoPK();
        consulta.setDocumentoCompra(factura.getDocumentoCompra());
        consulta.setFechaDocumento(factura.getFechaDocumento());
        if(facturas != null){
            for(FacturaIngreso actual : facturas){
                if(actual.getFacturaIngresoPK().equals(consulta)){
                    existe = true;
                    break;
                }
            }
        }
        return existe;
    }
    
    /*
     * FUNCION que elimina una factura de la lista de preingreso
     */
    
    public void deleteFactura(FacturaIngreso Factura){
        facturas.remove(Factura);
        actualizarTotales();
        new funciones().setMsj(2,"Factura Eliminada de la lista");
    } 
    
    
    /*
     * FUNCION que actualiza los totales de las facturas
     */
    public void actualizarTotales(){
        this.getSelected().setMontoFacturas(BigDecimal.ZERO);
        this.getSelected().setMontoSalida(BigDecimal.ZERO);
        this.getSelected().setMontoDescuentos(BigDecimal.ZERO);
        this.getSelected().setMontoTotalFacturas(BigDecimal.ZERO);
        // Recorrer lista y sacar totales
        for (FacturaIngreso actual : facturas) {
            this.getSelected().setMontoFacturas(this.getSelected().getMontoFacturas().add(actual.getMontoCompra()));             //Actualiza el monto total por facturas de la importacion total (suma todos los montos de facturas de la importacion)
            this.getSelected().setMontoSalida(this.getSelected().getMontoSalida().add(actual.getSalidaCompra()));                //Actualiza el monto total por salidas de la importacion total (suma todas las salidas de facturas de la importacion)
            this.getSelected().setMontoDescuentos(this.getSelected().getMontoDescuentos().add(actual.getDescuentoCompra()));     //Actualiza el monto total por descuentos de la importacion total (suma todos los descuentos de las facturas de la importacion)
            this.getSelected().setMontoTotalFacturas(this.getSelected().getMontoTotalFacturas().add(actual.getTotalCompra()));   //Actualiza el total pagado por facturas de la importacion total (suma todos los TOTALES de las facturas de la importacion)
        }
    }
    
    /*
     * Funcion que valida los datos de las facturas ingresadas
     */
    public boolean validarDatosFactura(){
        RequestContext context = RequestContext.getCurrentInstance(); 
        boolean valido = true;
        try{
            if(F.getProveedor() == null){
            valido = false;
            new funciones().setMsj(3,"Se requiere Proveedor");
            }
            if(!(F.getTotalUnidadesCompra() > 0)){
                valido = false;
                new funciones().setMsj(3,"El Total de Unidades debe ser > 0");
            }
             if(!(F.getTotalBultosCompra() >= 0)){
                valido = false;
                new funciones().setMsj(3,"El Total de Bultos debe ser >= 0");
            }
            if(!(F.getMontoCompra().doubleValue() >= 0)){
                valido = false;
                new funciones().setMsj(3,"Monto debe ser >= 0");
            }
            if(!(F.getSalidaCompra().doubleValue() >= 0)){
                valido = false;
                new funciones().setMsj(3,"Salida debe ser >= 0");
            }
            if(!(F.getDescuentoCompra().doubleValue() >= 0)){
                valido = false;
                new funciones().setMsj(3,"Descuento debe ser >= 0");
            }
            if(!(F.getTotalCompra().doubleValue() >= 0)){
                valido = false;
                new funciones().setMsj(3,"Total Factura no puede ser negativo verifique datos ingresados");
            }
            if(F.getTipoCompra().equals("AL CREDITO") && F.getFechavencCreditoCompra() == null){
                valido = false;
                new funciones().setMsj(3,"Debe Ingresar Fecha de Vencimiento");
            }
            context.addCallbackParam("validar",valido);
            return valido;
        }catch(NullPointerException e){
            new funciones().setMsj(3,"HAY ALGUNOS VALORES NULOS");
            return false;
        }catch(Exception e){
            return false;
        }
    }
    
  
    
    
    /* FUNCION QUE REALIZA LA TRANSACCION COMPLETA
     * DE RETACEO POR IMPORTACIONES INGRESANDO LAS FACTURAS DE INGRESO
     */
    public void generarRetaceo(ActionEvent event){
        //Validar datos de retaceo
        if(validarDatosRetaceo()){
            int paso =0;
            Transaccion nuevaTransac = new Transaccion(); //Gestiona la transaccion
            try{
                //Primero se debe de crear la transaccion
                nuevaTransac.setIdtransac(ejbFacadeTransaccion.getNextIdTransac());
                nuevaTransac.setResponsableTransac(new JsfUtil().getEmpleado());
                nuevaTransac.setFechaTransac(new funciones().getTime());
                nuevaTransac.setHoraTransac(new funciones().getTime());
                nuevaTransac.setTipoTransac(1); //INGRESO DE RETACEO
                //Persistiendo transaccion en la BD
                ejbFacadeTransaccion.create(nuevaTransac);
                paso = 1;  //Paso 1 completo realizacion de la transaccion
                // Ahora se procede a crear el Retaceo
                if(naturalezaCompra.equals("IMPORTADO")){//Crea el ID segun tipo de Importacion o Nacional
                    this.getSelected().setNumretaceo(ejbFacadeRetaceo.getNextIdRetaceoIMP()); 
                }else{
                    this.getSelected().setNumretaceo(ejbFacadeRetaceo.getNextIdRetaceoNAC()); 
                }
                this.getSelected().setIdtransac(nuevaTransac);  // Enlaza la transaccion con la operacion de Retaceo
                this.getSelected().setAprobado(false);    // Preparar aprobacion para el Admin
                //Calcular Factores de Retaceo
                //Retaceo ocupado para contabilidad
                this.getSelected().setFactorRetaceo(calcRetaceoCONTABLE(this.getSelected()));
                //Retaceo ocupado para calcular el precio de venta
                if(this.getSelected().getNumretaceo().startsWith("NAC")){
                    this.getSelected().setFactorCostoViaje(this.getSelected().getFactorRetaceo());
                    this.getSelected().setAprobado(true);
                }else{
                    this.getSelected().setFactorCostoViaje(BigDecimal.ZERO);
                }
                //Persistiendo Retaceo en la BD
                ejbFacadeRetaceo.create(this.getSelected());
                paso = 2; //Paso 2 completo ingreso de Retaceo
                //Ahora procedemos a guardar cada una de las facturas
                for (FacturaIngreso actual : facturas) {
                        actual.setNumretaceo(this.getSelected());
                        if(actual.getNumretaceo().getAprobado()){
                            actual.setEstadoFactura("PRE INGRESO");
                        }
                        ejbFacadeFacturaIngreso.create(actual);
                        this.getSelected().getFacturaIngresoCollection().add(actual);
                        ejbFacadeRetaceo.edit(this.getSelected());
                }
                paso = 3; //Paso 3 completo Ingreso de Facturas
               //Limpiamos datos para una nueva transaccion de importacion
                nuevaImportacion(event);
                new funciones().setMsj(1,"TRANSACCION REALIZADA CON EXITO");
            }catch(Exception e){
                switch(paso){
                    case 0:
                        new funciones().setMsj(3,"ERROR AL PROCESAR TRANSACCION");
                        break;
                    case 1:
                        new funciones().setMsj(3,"ERROR AL PROCESAR RETACEO");
                        ejbFacadeTransaccion.remove(nuevaTransac); // si hay error en registrar retaceo elimina la transaccion hecha
                        break;
                    case 2:
                        new funciones().setMsj(3,"ERROR AL GUARDAR FACTURAS");
                        // Elimina las facturas ingresadas
                        for (FacturaIngreso actual : facturas) {
                            actual.setNumretaceo(this.getSelected());
                            ejbFacadeFacturaIngreso.remove(actual);
                            this.getSelected().getFacturaIngresoCollection().remove(actual);
                            ejbFacadeRetaceo.edit(this.getSelected());
                        }
                        //Elimina Retaceo
                        ejbFacadeRetaceo.remove(this.getSelected());
                        //Elimina la transaccion
                         ejbFacadeTransaccion.remove(nuevaTransac);
                        break;
                }
            }
        }
    }
    
    /*
     * Funcion que calcula el factor de retaceo usado para la contabilidad
     *         parametros:
     *              -Entrada: Objeto Retaceo
     *              -Salidas: Factor de Retaceo
     */
    public BigDecimal calcRetaceoCONTABLE(Retaceo retaceo){
        BigDecimal factor = BigDecimal.ZERO;
        BigDecimal costoReal = BigDecimal.ZERO;
        BigDecimal totalGastos = BigDecimal.ZERO;
        if(retaceo.getNumretaceo() != null && validarDatosRetaceo()){
            //Calculamos el costo real de la importacion completa
            costoReal = retaceo.getMontoFacturas().subtract(retaceo.getMontoDescuentos());
            //Sumamos los costos incurridos con comprobante fiscales
            totalGastos = costoReal.add(retaceo.getMontoSalida()).add(retaceo.getMontoFlete()).add(retaceo.getMontoAgenteAduanal()).add(retaceo.getMontoTransporte()).add(retaceo.getMontoArancel()).add(retaceo.getMontoBodegaje()).add(retaceo.getMontoSeguridad()).add(retaceo.getMontoSeguro()).add(retaceo.getOtrosGastosCcf());
            //calculamos el factor de retaceo
            try{
                factor = new BigDecimal(totalGastos.doubleValue() / costoReal.doubleValue());
            }catch(Exception e){
                new funciones().setMsj(2,"Error al calcular el factor de retaceo posiblemente divicion por CERO");
                return BigDecimal.ZERO;
            }
            
        }
        return factor;
    }
    
    
    /*
     * Funcion que calcula el factor de retaceo usado para el calculo del precio de venta
     *         parametros:
     *              -Entrada: Objeto Retaceo
     *              -Salidas: Factor de Retaceo
     */
    public BigDecimal calcRetaceoVENTA(Retaceo retaceo){
        BigDecimal factor = BigDecimal.ZERO;
        BigDecimal costoReal = BigDecimal.ZERO;
        BigDecimal totalGastos = BigDecimal.ZERO;
        //Calculamos el costo real de la importacion completa
        costoReal = retaceo.getMontoFacturas().subtract(retaceo.getMontoDescuentos());
        //Sumamos los costos incurridos con comprobante fiscales y los que NO TIENEN COMPROBANTE FISCAL
        totalGastos = costoReal.add(retaceo.getMontoSalida()).add(retaceo.getMontoFlete()).add(retaceo.getMontoAgenteAduanal()).add(retaceo.getMontoTransporte()).add(retaceo.getMontoArancel()).add(retaceo.getMontoBodegaje()).add(retaceo.getOtrosGastosCcf()).add(retaceo.getMontoSeguridad()).add(retaceo.getMontoSeguro()).add(retaceo.getOtrosGastos());
        //calculamos el factor de retaceo para VENTAS
        try{
            factor = new BigDecimal(totalGastos.doubleValue() / costoReal.doubleValue());
        }catch(Exception e){
            new funciones().setMsj(2,"Error al calcular el factor de retaceo posiblemente divicion por CERO");
            return BigDecimal.ZERO;
        }
        return factor;
    }
    
    /*
     * Funcion que calcula el factor de retaceo usado para el precio de venta
     *         
     */
    public void calcularRetaceoCostoViaje(ActionEvent e){
        RequestContext context = RequestContext.getCurrentInstance(); 
        boolean datosValidos = true;
        if(!(this.getSelected().getOtrosGastos().doubleValue() >= 0)){
            new funciones().setMsj(3,"Otros Gastos debe ser >= 0");
            datosValidos = false;
        }
        if(datosValidos){
            this.getSelected().setFactorCostoViaje(calcRetaceoVENTA(this.getSelected()));
            //Actualiza la BD
            super.save(e);
            new funciones().setMsj(1,"FACTOR COSTO VIAJE CALCULADO");
        }
        context.addCallbackParam("validar",datosValidos);
    }
    
    /*
     * Funcion que aprueba el Retaceo para que las facturas pasen a bodega
     */
    public void aprobarRetaceo(){ 
        if(!(this.getSelected().getFactorCostoViaje().doubleValue() == 0)){
            this.getSelected().setAprobado(Boolean.TRUE);
            int paso = 0;
            try{
               ejbFacadeRetaceo.edit(this.getSelected());
               paso =1; // PASO 1 actualizar estado retaceo completo
               //Actualiza el estado de las facturas para que puedan ser ingresadas al inventario
                for (FacturaIngreso actual : this.getSelected().getFacturaIngresoCollection()) {
                    actual.setEstadoFactura("PRE INGRESO");
                    ejbFacadeFacturaIngreso.edit(actual);
                }
                paso=2; //Paso 2 actualizar estado facturas del retaceo completado
               new funciones().setMsj(1, "RETACEO NUM: " + this.getSelected().getNumretaceo()+" APROBADO"); 
            }catch(Exception e){
                switch(paso){
                    case 0:
                        this.getSelected().setAprobado(Boolean.FALSE);
                        new funciones().setMsj(3, "Error al actualizar Estado de RETACEO");
                        break;
                    
                    case 1:
                        //Actualiza el estado de las facturas para que puedan ser ingresadas al inventario
                        for (FacturaIngreso actual : this.getSelected().getFacturaIngresoCollection()) {
                            actual.setEstadoFactura("AGREGADA");
                            ejbFacadeFacturaIngreso.edit(actual);
                        }
                        this.getSelected().setAprobado(Boolean.FALSE);
                        ejbFacadeRetaceo.edit(this.getSelected());
                        break;
                }
            }
        }else{
            new funciones().setMsj(3, "El Factor Costo Viaje es CERO, debe de calcularlo antes de continuar");
        }
    }
    
    /*
     * FUNCION QUE ACTUALIZA EL RETACEO
     */
    public void actualizarRetaceo(ActionEvent e){
        this.save(e);
    }
    
    
    
    /*
     * Funcion que valida los datos del Retaceo a realizar
     */
    public boolean validarDatosRetaceo(){ 
        boolean valido = true;
        try{
            if(facturas.isEmpty()){
                valido = false;
                new funciones().setMsj(3,"DEBE INGRESAR AL MENOS UNA FACTURA");
            }
            if(!(this.getSelected().getMontoFlete().doubleValue() >= 0)){
                valido = false;
                new funciones().setMsj(3,"Flete debe ser >= 0");
            }
            if(!(this.getSelected().getMontoAgenteAduanal().doubleValue() >= 0)){
                valido = false;
                new funciones().setMsj(3,"Agente Aduanal debe ser >= 0");
            }
            if(!(this.getSelected().getMontoArancel().doubleValue() >= 0)){
                valido = false;
                new funciones().setMsj(3,"Arancel debe ser >= 0");
            }
            if(!(this.getSelected().getMontoTransporte().doubleValue() >= 0)){
                valido = false;
                new funciones().setMsj(3,"Monto Transporte debe ser >= 0");
            }
            if(!(this.getSelected().getMontoBodegaje().doubleValue() >= 0)){
                valido = false;
                new funciones().setMsj(3,"Monto Bodegaje debe ser >= 0");
            }
            if(!(this.getSelected().getMontoSeguridad().doubleValue() >= 0)){
                valido = false;
                new funciones().setMsj(3,"Monto Seguridad debe ser >= 0");
            }
            if(!(this.getSelected().getMontoSeguro().doubleValue() >= 0)){
                valido = false;
                new funciones().setMsj(3,"Monto Seguro debe ser >= 0");
            }
            if(!(this.getSelected().getOtrosGastosCcf().doubleValue() >= 0)){
                valido = false;
                new funciones().setMsj(3,"Otros Gastos Con Comprobante Fiscal debe ser >= 0");
            }
            if(!(this.getSelected().getOtrosGastos().doubleValue() >= 0)){
                valido = false;
                new funciones().setMsj(3,"Otros Gastos Con Comprobante Fiscal debe ser >= 0");
            }
            return valido;
        }catch(NullPointerException e){
            new funciones().setMsj(3,"HAY ALGUNOS VALORES NULOS");
            return false;
        }catch(Exception e){
            return false;
        }
    }
    
    
  
    
}
