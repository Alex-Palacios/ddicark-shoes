package com.ddicark.controladores;

import com.ddicark.auxiliares.InventarioConsulta;
import com.ddicark.entidades.Preingreso;
import com.ddicark.auxiliares.JsfUtil;
import com.ddicark.auxiliares.curvaColor;
import com.ddicark.auxiliares.funciones;
import com.ddicark.entidades.Caja;
import com.ddicark.entidades.FacturaIngreso;
import com.ddicark.entidades.Inventario;
import com.ddicark.entidades.Producto;
import com.ddicark.jpaFacades.FacturaIngresoFacade;
import com.ddicark.jpaFacades.InventarioFacade;
import com.ddicark.jpaFacades.PreingresoFacade;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FlowEvent;

@ManagedBean(name = "preingresoController")
@SessionScoped
public class PreingresoController extends AbstractController<Preingreso> implements Serializable {

    @EJB
    private PreingresoFacade ejbFacadePreingreso;
    @EJB
    private FacturaIngresoFacade ejbFacadeFacturaIngreso;
    @EJB
    private InventarioFacade ejbFacadeInventario;
    
    public PreingresoController() {
        super(Preingreso.class);
    }
    //Inicializa controlador de persistencia de proveedores
    @PostConstruct
    public void init() {
        super.setFacade(ejbFacadePreingreso);
    }

    
    //Variables
    private List<Preingreso> preingresoFactura;
    private int unidades;
    private int cajas;
    private float costoTotal;
    //Variables de control
    private FacturaIngreso factura;
    //Para los preIngresos
    private int tipoProducto;
    private Producto estilo;
    private String marca;
    private String material;
    private char genero;
    private char clasePersona;
    private String ubicacion;
    private float costoUnitario;
    private int numCajas;
    private String[] tallas = new String[10];
    private curvaColor[] curva = {new curvaColor(),new curvaColor(),new curvaColor(),new curvaColor(),new curvaColor(),new curvaColor()};
    
    private int numColores;
    private Preingreso eliminar;
    private String claveAutorizacion;
    
    //CAMBIO EN PREINGRESO
    private List<Caja> cajasPreingresadas;
    private List<Caja> cajasPreingresadasAux;
    private List<Preingreso> detalleCaja;
    
    private boolean porCaja=true;
    
    
    //GET AND SET

    
    public boolean isPorCaja() {
        return porCaja;
    }

    public void setPorCaja(boolean porCaja) {
        this.porCaja = porCaja;
    }

    
    public List<Preingreso> getPreingresoFactura() {
        return preingresoFactura;
    }

    public int getUnidades() {
        unidades = preingresoFactura.size();
        return unidades;
    }

    public void setUnidades(int unidades) {
        this.unidades = unidades;
    }

    public int getCajas() {
        return cajas;
    }

    public void setCajas(int cajas) {
        this.cajas = cajas;
    }

    public FacturaIngreso getFactura() {
        return factura;
    }

    public void setFactura(FacturaIngreso factura) {
        this.factura = factura;
    }

    
    
    public int getTipoProducto() {
        return tipoProducto;
    }

    public void setTipoProducto(int tipoProducto) {
        this.tipoProducto = tipoProducto;
    }

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

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    

    public float getCostoUnitario() {
        return costoUnitario;
    }

    public void setCostoUnitario(float costoUnitario) {
        this.costoUnitario = costoUnitario;
    }

    public int getNumCajas() {
        return numCajas;
    }

    public void setNumCajas(int numCajas) {
        this.numCajas = numCajas;
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

    public int getNumColores() {
        return numColores;
    }

    public void setNumColores(int numColores) {
        this.numColores = numColores;
    }

    public Preingreso getEliminar() {
        return eliminar;
    }

    public void setEliminar(Preingreso eliminar) {
        this.eliminar = eliminar;
    }

    public String getClaveAutorizacion() {
        return claveAutorizacion;
    }

    public void setClaveAutorizacion(String claveAutorizacion) {
        this.claveAutorizacion = claveAutorizacion;
    }

    public List<Caja> getCajasPreingresadas() {
        cajasPreingresadas = new ArrayList<Caja>();
        preingresoFactura = ejbFacadePreingreso.getPreingresoFactura(factura);
        cajas = ejbFacadePreingreso.getNumCajasFactura(factura);
        if(preingresoFactura != null){
            int numCajaActual = -1;
            Caja cajaActual;
            for(int i=0; i < preingresoFactura.size(); i++){
                if(preingresoFactura.get(i).getNumcajaPreingreso() != numCajaActual){
                   //Nueva caja
                    cajaActual = new Caja();
                    numCajaActual = preingresoFactura.get(i).getNumcajaPreingreso();
                    cajaActual.setNumcaja(preingresoFactura.get(i).getNumcajaPreingreso().toString());
                    cajaActual.setProducto(preingresoFactura.get(i).getEstilo());
                    cajaActual.setUnidadesCaja(ejbFacadePreingreso.countPreingresoCajaFactura(factura, preingresoFactura.get(i).getNumcajaPreingreso()));
                    cajaActual.setCostoUnitario(preingresoFactura.get(i).getCostounitarioPreingreso());
                    List<String> tallas = ejbFacadePreingreso.curvaTallaList(factura, preingresoFactura.get(i).getNumcajaPreingreso());
                    List<String> colores = ejbFacadePreingreso.cajaColoresList(factura, preingresoFactura.get(i).getNumcajaPreingreso());
                    for(int t=0; t < tallas.size(); t++){
                        if(t==0){
                            cajaActual.setDetalleCaja(tallas.get(t));
                        }else{
                            cajaActual.setDetalleCaja(cajaActual.getDetalleCaja() + "-" +tallas.get(t));
                        }
                    }
                    for(int c=0; c < colores.size(); c++){
                        if(c==0){
                            cajaActual.setColoresCaja(colores.get(c));
                        }else{
                            cajaActual.setColoresCaja(cajaActual.getColoresCaja() + "; " +colores.get(c));
                        }
                    }
                    
                    //Costo de la caja
                    cajaActual.setCostoCaja(new BigDecimal(cajaActual.getCostoUnitario().doubleValue() * cajaActual.getUnidadesCaja()));
                    cajaActual.setCostoCaja(new BigDecimal(new funciones().redondearMas(cajaActual.getCostoCaja().floatValue(), 2)));
                    cajasPreingresadas.add(cajaActual);
                    
                }
            }
        }
        
        return cajasPreingresadas;
    }

    public List<Caja> getCajasPreingresadasAux() {
        cajasPreingresadasAux = cajasPreingresadas;
        return cajasPreingresadasAux;
    }

    public void setCajasPreingresadasAux(List<Caja> cajasPreingresadasAux) {
        this.cajasPreingresadasAux = cajasPreingresadasAux;
    }

    
    public List<Preingreso> getDetalleCaja(String numcaja) {
        detalleCaja = ejbFacadePreingreso.getPreingresoCaja(factura, Integer.parseInt(numcaja));
        return detalleCaja;
    }


    
    
    
    

    
    
    
    //Prepara todo para un nuevo Preingreso
    public void nuevoPreIngreso(ActionEvent event){
        tipoProducto = 1;
        //cajasPreingresadas = new ArrayList<Caja>();
        resetearDetalles(0);
        super.prepararCrear(event);
    }
    
    // FUNCION QUE SE EJECUTA CADA VEZ QUE SE PASA DE UN PASO A OTRO EN EL WIZARD
    public String onFlowProcess(FlowEvent event) {
            factura = this.getSelected().getFacturaIngreso();
            
            if(event.getOldStep().equals("tbFactura")){
                cajasPreingresadas = getCajasPreingresadas();
            }else{
                cajasPreingresadas = new ArrayList<Caja>();
                preingresoFactura = new ArrayList<Preingreso>();
            }
            return event.getNewStep();    
    }  
    
    /*
     * FUNCION PREPARAR DIALOG CURVA
     */
    
    public void prepararCurva(){
        RequestContext context = RequestContext.getCurrentInstance(); 
        upperPreingreso();
        boolean valido = true;
        if(estilo == null || marca == null ){
            valido = false;
        }
        context.addCallbackParam("mostrar",valido);
    }
    
    
    
    /*
     * FUNCION QUE LIMPIA LA LISTA DE PREINGRESO
     */
    public void cambiarEstilo(){
        resetearDetalles(0);
    }
    
    /*
     * FUNCION que guarda en la lista de Pre-Ingresos
     */
    public void listarProductos(){
        RequestContext context = RequestContext.getCurrentInstance(); 
        //VERIFICAR QUE LOS DATOS SEAN CORRECTOS
        if(validarDetalle()){
            int numProductoIngresados = 0;
            int numProductosNoIngresado = 0;
            String responsable = new JsfUtil().getEmpleado().getCodempleado();
            //REPETIR PROCESO POR CADA UNIDAD
            if(numCajas == 0){
                //Ingresar por unidad
                //Recorrer por color
                for(int c=0; c < numColores;c++ ){
                    if((curva[c].getColor() != null) && (!"".equals(curva[c].getColor())) ){
                         curva[c].setColor(curva[c].getColor().toUpperCase());
                         int numTalla = 0;
                         //Recorrer arreglo de tallas-cantidades
                         while(numTalla < 10){
                            //Verificar que se haya ingresado talla
                            if((tallas[numTalla] != null) && (!"".equals(tallas[numTalla]) )){
                                //Accion por Cantidad por Tallas
                                int cantidad = 0;
                                switch(numTalla){
                                    case 0: cantidad = curva[c].getT1();break;
                                    case 1: cantidad = curva[c].getT2();break;
                                    case 2: cantidad = curva[c].getT3();break;
                                    case 3: cantidad = curva[c].getT4();break;
                                    case 4: cantidad = curva[c].getT5();break;
                                    case 5: cantidad = curva[c].getT6();break;
                                    case 6: cantidad = curva[c].getT7();break;
                                    case 7: cantidad = curva[c].getT8();break;
                                    case 8: cantidad = curva[c].getT9();break;
                                    case 9: cantidad = curva[c].getT10();break;
                                }
                                for(int cant=1; cant <= cantidad; cant++){
                                    Preingreso pre = new Preingreso();
                                    pre.setFacturaIngreso(factura);
                                    pre.setEstilo(estilo);
                                    pre.setNumcajaPreingreso(0);
                                    pre.setColorPreingreso(curva[c].getColor());
                                    pre.setMarcaPreingreso(marca);
                                    pre.setMaterialPreingreso(material);
                                    pre.setGeneroPreingreso(genero);
                                    pre.setClasepersonaPreingreso(clasePersona);
                                    pre.setTallaPreingreso(tallas[numTalla]);
                                    pre.setCostounitarioPreingreso(new BigDecimal(new funciones().redondearMas(costoUnitario,2)));
                                    pre.setResponsable(responsable);
                                    pre.setCodigoPreingreso(ejbFacadePreingreso.getNextIdPreIngreso());
                                    pre.setEstadoPreingreso("PREINGRESO");
                                    try{
                                        ejbFacadePreingreso.create(pre);
                                        numProductoIngresados++;
                                    }catch(Exception e){
                                        numProductosNoIngresado++;
                                    }
                                }
                                       
                             }
                             numTalla++;
                         }
                    }
                   
                }
                
            }
             //REPETIR PROCESO POR CADA UNIDAD DE CADA CAJA
            else{
                int ultimaCaja = ejbFacadePreingreso.getMAXNumCajasFactura(factura);
                //ingresar por cajas
                //Recorrer arreglo por Caja
                for(int cajas=1; cajas <= numCajas; cajas++){
                    ultimaCaja++;
                    //Recorrer por color
                    for(int c=0; c < numColores;c++ ){
                        if((curva[c].getColor() != null) && (!"".equals(curva[c].getColor())) ){
                             curva[c].setColor(curva[c].getColor().toUpperCase());
                             int numTalla = 0;
                             //Recorrer arreglo de tallas-cantidades
                             while(numTalla < 10){
                                //Verificar que se haya ingresado talla
                                if((tallas[numTalla] != null) && (!"".equals(tallas[numTalla]) )){
                                    //Accion por Cantidad por Tallas
                                    int cantidad = 0;
                                    switch(numTalla){
                                        case 0: cantidad = curva[c].getT1();break;
                                        case 1: cantidad = curva[c].getT2();break;
                                        case 2: cantidad = curva[c].getT3();break;
                                        case 3: cantidad = curva[c].getT4();break;
                                        case 4: cantidad = curva[c].getT5();break;
                                        case 5: cantidad = curva[c].getT6();break;
                                        case 6: cantidad = curva[c].getT7();break;
                                        case 7: cantidad = curva[c].getT8();break;
                                        case 8: cantidad = curva[c].getT9();break;
                                        case 9: cantidad = curva[c].getT10();break;
                                    }
                                    for(int cant=1; cant <= cantidad; cant++){
                                        Preingreso pre = new Preingreso();
                                        pre.setFacturaIngreso(factura);
                                        pre.setEstilo(estilo);
                                        pre.setNumcajaPreingreso(ultimaCaja);
                                        pre.setColorPreingreso(curva[c].getColor());
                                        pre.setMarcaPreingreso(marca);
                                        pre.setMaterialPreingreso(material);
                                        pre.setGeneroPreingreso(genero);
                                        pre.setClasepersonaPreingreso(clasePersona);
                                        pre.setTallaPreingreso(tallas[numTalla]);
                                        pre.setCostounitarioPreingreso(new BigDecimal(new funciones().redondearMas(costoUnitario,2)));
                                        pre.setResponsable(responsable);
                                        pre.setCodigoPreingreso(ejbFacadePreingreso.getNextIdPreIngreso());
                                        pre.setEstadoPreingreso("PREINGRESO");
                                        try{
                                            ejbFacadePreingreso.create(pre);
                                            numProductoIngresados++;
                                        }catch(Exception e){
                                            numProductosNoIngresado++;
                                        }
                                    }

                                 }
                                 numTalla++;
                             }
                        }

                    }
                }
            }
            if(numProductosNoIngresado == 0){
                new funciones().setMsj(1,numProductoIngresados + " Productos Listados");
                getCajasPreingresadas();
                context.addCallbackParam("validar",true);
            }else{
                 new funciones().setMsj(2,"ERROR al Persistir agunos Productos en PREINGRESO\nProductos Ingresados : "+numProductoIngresados +"\nProductos No Ingresados:"+numProductosNoIngresado); 
            }
            resetearDetalles(1);
        }
    }
    
    /*
     * ELIMINA UN ARTICULO DE LA LISTA DE PREINGRESO SI PERTENECE A UNA CAJA ELIMINA LA CAJA COMPLETA
     */
    public void eliminarDeLista(){
        Preingreso pre = eliminar;
        if(pre.getNumcajaPreingreso() != null){
            //Pertenece a una caja
            int eliminados = 0;
            List<Preingreso> lista = ejbFacadePreingreso.getPreingresoCaja(pre.getFacturaIngreso(), pre.getNumcajaPreingreso());
            if(lista != null){
                for(int i=0; i < lista.size(); i++){
                    ejbFacadePreingreso.remove(lista.get(i));
                    eliminados++;
                }
                new funciones().setMsj(1, eliminados + " Productos Eliminados de la lista de Preingresos");
            }
        }else{
            //No pertenece a una caja
            ejbFacadePreingreso.remove(pre);
            new funciones().setMsj(1, "Producto Eliminado de la lista de Preingresos");
        }
    }
    
    /*
     * Funcion que valida datos del detalle de la factura a ingresar a productos pre-ingresados
     */
    
    public boolean validarDetalle(){ 
        boolean valido = true;
        int numTallas = 0; //numero de tallas ingresadas
        //Recorrer arreglo de tallas
        for(int i=0 ; i < tallas.length; i++){
            //Verificar que se haya ingresado una
            if((tallas[i] != null) && (!"".equals(tallas[i])) ){
                numTallas++;
            }
        }
        //Recorrer arreglo de colores
        int numColor = 0;
        for(int i=0 ; i < curva.length; i++){
            //Verificar que se haya ingresado una
            if((curva[i].getColor() != null) && (!"".equals(curva[i].getColor())) ){
                numColor++;
            }
        }
        try{
            if(costoUnitario <= 0){
                valido = false;
                new funciones().setMsj(2,"Costo Unitario no puede ser menor o igual a CERO");
            }
            if(numTallas <= 0){
                valido = false;
                new funciones().setMsj(2,"Debe ingresar al menos una Talla");
            }
            if(numColor <=0){
                valido = false;
                new funciones().setMsj(2,"Debe ingresar al menos un Color");
            }
            if(porCaja){
                if(numCajas <=0){
                    new funciones().setMsj(3, "NUMERO DE CAJAS DEBE SER >= 1");
                    valido = false;
                }
            }else{
                numCajas = 0;
            }
            
            return valido;
        }catch(NullPointerException e){
            new funciones().setMsj(3,"HAY ALGUNOS VALORES NULOS");
            return false;
        }catch(Exception e){
            return false;
        }
    }
    
    
    //RESETEAR VALORES DE DETALLES
    public void resetearDetalles(int action){
        if(action == 0){
            estilo = null;
            marca = null;
            material = null;
        }
        for(int i=0; i < curva.length; i++){
            curva[i].resetear();
        }
        for(int t=0; t < tallas.length; t++){
            tallas[t] = "";
        }
        numColores = 1;
        costoUnitario = 0;
        numCajas = 0;
    }
    
    /*Funcion que pasa a mayusculas los campos string
    * de los preingresos
    */
    public void upperPreingreso(){
        marca = marca.toUpperCase();
        if(material != null){
            material = material.toUpperCase();
        }
    }
    
  
    
    public float getCostoTotal() {
        BigDecimal costo = BigDecimal.ZERO;
        if(preingresoFactura != null){
            for(int i= 0 ; i < preingresoFactura.size(); i++){
                costo = costo.add(preingresoFactura.get(i).getCostounitarioPreingreso());
            }
        }
        costoTotal = costo.floatValue();
        costoTotal = new funciones().redondearMas(costoTotal,2);
        return costoTotal;
    }

    public void setCostoTotal(float costoTotal) {
        this.costoTotal = costoTotal;
    }

    /*
     * FUNCION que elimina una caja de la lista de preingreso
     */
    
    public void deleteCaja(Caja caja){
        List<Preingreso> preingresoCaja = ejbFacadePreingreso.getPreingresoCaja(factura, Integer.parseInt(caja.getNumcaja()));
        try{
            for(Preingreso pre: preingresoCaja){
                ejbFacadePreingreso.remove(pre);
                preingresoFactura.remove(pre);
            }
            new funciones().setMsj(2,"Caja Eliminada de la lista");
            Caja eliminar = null;
            for(Caja actual : cajasPreingresadas){
                if(actual.getNumcaja().equals(caja.getNumcaja())){
                    eliminar = actual;
                    break;
                }
            }
            if(eliminar != null){
                cajasPreingresadas.remove(eliminar);
            }
            if(cajasPreingresadas != null){
                cajas = cajasPreingresadas.size();
            }
        }catch(Exception ex){
            new funciones().setMsj(3,"ERROR AL ELIMINAR CAJA");
        }
    } 
    
    
    /*
     * FUNCION QUE LIMPIA LA LISTA DE PREINGRESO
     */
    public void vaciarPreingreso(){
        int noEliminados = 0;
        if(preingresoFactura.isEmpty() || preingresoFactura == null){
            new funciones().setMsj(2,"LA LISTA YA ESTA VACIA");
        }else{
            for(Preingreso actual : preingresoFactura){
                try{
                   ejbFacadePreingreso.remove(actual);
                }catch(Exception e){
                    noEliminados++;
                }
            }
            if(noEliminados == 0){
               new funciones().setMsj(1,"Lista de Preingreso Vacio");  
               getCajasPreingresadas();
            }else{
               new funciones().setMsj(1,"No se pudieron eliminar "+noEliminados +" Elementos de la lista"); 
               getCajasPreingresadas();
            }
        }
    }
   
   
    
    /*
     * FUNCION QUE CANCELA EL PREINGRESO
     */
    public void cancelarPreingreso(){
        //Limpiar Lista
        if(!preingresoFactura.isEmpty() && !(preingresoFactura == null)){
            for(Preingreso actual : preingresoFactura){
                try{
                   ejbFacadePreingreso.remove(actual);
                }catch(Exception e){ }
            }
        } 
        //Redirigir a preingreso
       new funciones().irA("faces/vistas/preingresos/preingresos.xhtml");
       new funciones().setMsj(1,"PREINGRESO CANCELADO");
            
    }
    
    /*
     * FUNCION QUE GUARDA EL PREINGRESO
     */
    public void guardarPreingreso(){
        RequestContext context = RequestContext.getCurrentInstance();
        //Verifica que haya preingresos
        if(!(preingresoFactura == null) && !preingresoFactura.isEmpty() ){
            //Verificar que coincida el total de unidades con la factura previamente ingresada
            if(unidades == factura.getTotalUnidadesCompra()){
                 //Verificar que coincida el numero de cajas con el total de bultos de la factura previamente ingresada
                if(cajas == factura.getTotalBultosCompra()){
                    //Verificar que coincida el costo total con el monto de la factura previamente ingresada
                    if(costoTotal == factura.getMontoCompra().floatValue()){
                        if(factura.getEstadoFactura().equals("PRE INGRESO")){
                            //TODO ESTA CORRECTO
                            //INGRESAR EL UNITARIO AL INVENTARIO
                           factura.setEstadoFactura("PROCESANDO");
                           ejbFacadeFacturaIngreso.edit(factura);
                           //ACTUALIZAR ESTADO DE PRODUCTOS PREINGRESADOS PARA CODIFICACION
                           ejbFacadePreingreso.updateEstadoPreingreso(factura,"CODIFICANDO");

                           new funciones().setMsj(1,"PREINGRESO GUARDADO CON EXITO");
                           context.addCallbackParam("mostrarConfirmacion", false);
                           new funciones().irA("faces/vistas/preingresos/preingresos.xhtml");
                        }else{
                            new funciones().setMsj(3,"LA COMPRA AUN NO HA SIDO APROBADA ... CONTACTE CON EL CONTADOR");
                            context.addCallbackParam("mostrarConfirmacion", false);
                        }
                    }else{
                        new funciones().setMsj(3,"COSTO TOTAL NO COINCIDE CON EL MONTO DE LA FACTURA");
                        context.addCallbackParam("mostrarConfirmacion", true);
                    }
                }else{
                    new funciones().setMsj(3,"EL NUMERO DE CAJAS NO COINCIDE CON EL DE LA FACTURA");
                    context.addCallbackParam("mostrarConfirmacion", false);
                }
            }else{
                new funciones().setMsj(3,"TOTAL DE UNIDADES NO COINCIDE CON TOTAL UNIDADES DE LA FACTURA");
                context.addCallbackParam("mostrarConfirmacion", false);
            }
        }else{
            new funciones().setMsj(2,"NO HA INGRESADO PRODUCTOS");
            context.addCallbackParam("mostrarConfirmacion", false);
        } 
            
    }
    
    
    /*
     * FUNCION QUE AUTORIZA EL PREINGRESO CON DIFERENTE COSTO SEGUN FACTURA
     */
    public void autorizarPreingreso(){
        RequestContext context = RequestContext.getCurrentInstance();
        //Verifica que haya preingresos
        if(validarAutorizar()){
            if(!preingresoFactura.isEmpty() && !(preingresoFactura == null)){
                //Verificar que coincida el total de unidades con la factura previamente ingresada
                if(unidades == factura.getTotalUnidadesCompra()){
                     //Verificar que coincida el numero de cajas con el total de bultos de la factura previamente ingresada
                    if(cajas == factura.getTotalBultosCompra()){
                        //Verificar que coincida el costo total con el monto de la factura previamente ingresada
                        //TODO ESTA CORRECTO
                        factura.setEstadoFactura("PROCESANDO");
                        ejbFacadeFacturaIngreso.edit(factura);
                        //ACTUALIZAR ESTADO DE PRODUCTOS PREINGRESADOS
                        ejbFacadePreingreso.updateEstadoPreingreso(factura,"CODIFICANDO");
                        context.addCallbackParam("validar", true);
                        new funciones().irA("faces/vistas/preingresos/preingresos.xhtml");
                    }else{
                        new funciones().setMsj(3,"EL NUMERO DE CAJAS NO COINCIDE CON EL DE LA FACTURA");
                        context.addCallbackParam("validar", false);
                    }
                }else{
                    new funciones().setMsj(3,"TOTAL DE UNIDADES NO COINCIDE CON TOTAL UNIDADES DE LA FACTURA");
                    context.addCallbackParam("validar", false);
                }
            }else{
                new funciones().setMsj(2,"NO HA INGRESADO PRODUCTOS");
                context.addCallbackParam("validar", false);
            } 
        }else{
            new funciones().setMsj(3,"CLAVE INCORRECTA");
            context.addCallbackParam("validar", false);
        }
        
            
    }
    
    /*
     * FUNCION QUE VALIDA LA CLAVE DE AUTORIZACION
     */
    
    public boolean validarAutorizar(){
        boolean autorizar = false;
        if(claveAutorizacion.equals("claveAdmin")){
            autorizar = true;
        }
        return autorizar;
    }
    
   
    
    //INGRESO UNIDAD
    private float costoTotalUnitario;

    public float getCostoTotalUnitario() {
        costoTotalUnitario = (float) 0.00;
        for(Preingreso actual: detalleCaja){
            float cu = new funciones().redondearMas(actual.getCostounitarioPreingreso().floatValue(),2);
            costoTotalUnitario = costoTotalUnitario + cu;
        }
        return costoTotalUnitario;
    }

    public void setCostoTotalUnitario(float costoTotalUnitario) {
        this.costoTotalUnitario = costoTotalUnitario;
    }

    
}
