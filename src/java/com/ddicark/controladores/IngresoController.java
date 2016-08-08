package com.ddicark.controladores;

import com.ddicark.auxiliares.JsfUtil;
import com.ddicark.entidades.Ingreso;
import com.ddicark.auxiliares.curvaColor;
import com.ddicark.auxiliares.funciones;
import com.ddicark.entidades.Caja;
import com.ddicark.entidades.Configuraciones;
import com.ddicark.entidades.FacturaIngreso;
import com.ddicark.entidades.Inventario;
import com.ddicark.entidades.Preingreso;
import com.ddicark.entidades.Producto;
import com.ddicark.entidades.Transaccion;
import com.ddicark.jpaFacades.CajaFacade;
import com.ddicark.jpaFacades.ConfiguracionesFacade;
import com.ddicark.jpaFacades.FacturaIngresoFacade;
import com.ddicark.jpaFacades.IngresoFacade;
import com.ddicark.jpaFacades.InventarioFacade;
import com.ddicark.jpaFacades.PreingresoFacade;
import com.ddicark.jpaFacades.TransaccionFacade;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FlowEvent;

@ManagedBean(name = "ingresoController")
@SessionScoped
public class IngresoController extends AbstractController<Ingreso> implements Serializable {

    @EJB
    private ConfiguracionesFacade ejbFacadeConfig;
    
    @EJB
    private TransaccionFacade ejbFacadeTransaccion;
    
    @EJB
    private FacturaIngresoFacade ejbFacadeFacturaIngreso;
    
    @EJB
    private IngresoFacade ejbFacadeIngreso;
    
    @EJB
    private PreingresoFacade ejbFacadePreingreso;
     
    @EJB
    private CajaFacade ejbFacadeCaja;
    
    @EJB
    private InventarioFacade ejbFacadeInventario;
    
    

     public IngresoController() {
        super(Ingreso.class);
    }
    //Inicializa controlador de persistencia de proveedores
    @PostConstruct
    public void init() {
        super.setFacade(ejbFacadeIngreso);
    }

    //VARIABLES DE CONTROL
    private FacturaIngreso factura;
    private String codigoBarraLectura;
    private Inventario producto;
    private List<Preingreso> preingresos;
    private List<Caja> cajasPreingresadas;
    private List<Caja> cajasPreingresadasAux;
    private List<Inventario> productos;
    private List<Caja> cajasCodificadas;
    
    //Caja Actual
    private Caja cajaActual;
    private String[] tallas = new String[10];
    private int numTallas;
    private curvaColor[] curva = {new curvaColor(),new curvaColor(),new curvaColor(),new curvaColor(),new curvaColor(),new curvaColor()};
    private int numColores;
    private int unidadesCaja;
    private float costoCaja;
    private float costoUnidad;
    private float costoUnitarioReal;
    private float precioventaUnidad;
    private String ubicacionCaja;
    private String ubicacionUnidad;
    private String detalle;
    private String colores;
    private Producto estilo;
    private int idCaja;
    private List<Preingreso> unidadesCajaSeleccionada;
    private List<Preingreso> filtro;
    
    //EDitar
    private boolean editandoProducto=false;
    //GET AND SET
   
    
    public FacturaIngreso getFactura() {
        return factura;
    }

    public void setFactura(FacturaIngreso factura) {
        this.factura = factura;
    }

    
    public String getCodigoBarraLectura() {
        return codigoBarraLectura;
    }

    public void setCodigoBarraLectura(String codigoBarraLectura) {
        this.codigoBarraLectura = codigoBarraLectura;
    }

    public List<Preingreso> getPreingresos() {
        return preingresos;
    }

    public void setPreingresos(List<Preingreso> preingresos) {
        this.preingresos = preingresos;
    }

    
    public Inventario getProducto() {
        return producto;
    }

    public void setProducto(Inventario producto) {
        this.producto = producto;
    }

    public List<Inventario> getProductos() {
        return productos;
    }

    public void setProductos(List<Inventario> productos) {
        this.productos = productos;
    }

    public Caja getCajaActual() {
        return cajaActual;
    }

    public void setCajaActual(Caja cajaActual) {
        this.cajaActual = cajaActual;
    }

    public String[] getTallas() {
        return tallas;
    }

    public void setTallas(String[] tallas) {
        this.tallas = tallas;
    }

    public int getNumTallas() {
        return numTallas;
    }

    public void setNumTallas(int numTallas) {
        this.numTallas = numTallas;
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

    
    public int getUnidadesCaja() {
        return unidadesCaja;
    }

    public void setUnidadesCaja(int unidadesCaja) {
        this.unidadesCaja = unidadesCaja;
    }

    public float getCostoCaja() {
        return costoCaja;
    }

    public void setCostoCaja(float costoCaja) {
        this.costoCaja = costoCaja;
    }

    public float getCostoUnidad() {
        return costoUnidad;
    }

    public void setCostoUnidad(float costoUnidad) {
        this.costoUnidad = costoUnidad;
    }

    public String getUbicacionCaja() {
        return ubicacionCaja;
    }

    public void setUbicacionCaja(String ubicacionCaja) {
        this.ubicacionCaja = ubicacionCaja;
    }

    public String getUbicacionUnidad() {
        return ubicacionUnidad;
    }

    public void setUbicacionUnidad(String ubicacionUnidad) {
        this.ubicacionUnidad = ubicacionUnidad;
    }
    
    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public String getColores() {
        return colores;
    }

    public void setColores(String colores) {
        this.colores = colores;
    }

    
    public Producto getEstilo() {
        return estilo;
    }

    public void setEstilo(Producto estilo) {
        this.estilo = estilo;
    }

    public boolean isEditandoProducto() {
        return editandoProducto;
    }

    public void setEditandoProducto(boolean editandoProducto) {
        this.editandoProducto = editandoProducto;
    }

    public float getCostoUnitarioReal() {
        return costoUnitarioReal;
    }

    public void setCostoUnitarioReal(float costoUnitarioReal) {
        this.costoUnitarioReal = costoUnitarioReal;
    }

    public float getPrecioventaUnidad() {
        return precioventaUnidad;
    }

    public void setPrecioventaUnidad(float precioventaUnidad) {
        this.precioventaUnidad = precioventaUnidad;
    }

    
    public int getIdCaja() {
        return idCaja;
    }

    public void setIdCaja(int idCaja) {
        this.idCaja = idCaja;
    }

    public List<Preingreso> getFiltro() {
        return filtro;
    }

    public void setFiltro(List<Preingreso> filtro) {
        this.filtro = filtro;
    }
    
    
    
    
    
    //FUNCIONES
    
    //Prepara todo para una nueva Importacion
    public void nuevoIngreso(ActionEvent event){
        productos = new ArrayList<Inventario>();
        this.cajaActual = null;
        this.ubicacionCaja = "";
        this.detalle = "";
        this.estilo = null;
        this.costoCaja = 0;
        this.costoUnidad = 0;
        this.unidadesCaja = 0;
        editandoProducto = false;
        super.prepararCrear(event);
    }
   
    // FUNCION QUE SE EJECUTA CADA VEZ QUE SE PASA DE UN PASO A OTRO EN EL WIZARD
    public String onFlowProcess(FlowEvent event) {
            factura = this.getSelected().getFacturaIngreso();
            productos = new ArrayList<Inventario>();
            if(event.getOldStep().equals("tbFactura")){
                preingresos = ejbFacadePreingreso.getItemsFactura(factura);
                if(preingresos != null){
                    for(int i=0; i < preingresos.size(); i++){
                        if(preingresos.get(i).getCodigoBarra() != null || preingresos.get(i).getCodigoCaja() != null ){
                            preingresos.get(i).setCodigoBarra(null);
                            preingresos.get(i).setCodigoCaja(null);
                            ejbFacadePreingreso.edit(preingresos.get(i));
                        }
                    }
                }
                getCajasPreingresadas();
                filtro = null;
            }else{
                this.getSelected().setFacturaIngreso(null);
            }
            cajaActual = null;
            editandoProducto = false;
            return event.getNewStep();    
    }  
      
    //Nuevo producto a codificar
    public void etiquetarProducto(){
        RequestContext context = RequestContext.getCurrentInstance(); 
        editandoProducto = false;
        //Verificar que la lista de preingresos para etiquetar no este vacio
        if( preingresos == null || preingresos.isEmpty() ){
            new funciones().setMsj(2,"No hay producto para etiquetar");
            context.addCallbackParam("vacio",true); //Indica que no hay producto que etiquetar por lo tanto no muestra ningun dialogo
        }else{
            context.addCallbackParam("vacio",false);  //Si hay producto para etiquetar y se mostrara un dialogo
            //Agarrar el primer registro de la lista y verificar si pertenece a una caja
            if(preingresos.get(0).getNumcajaPreingreso() == null || preingresos.get(0).getNumcajaPreingreso() == 0){
                //NO pertenece a una caja, leer por unidad y cargar los datos para leer su codigo
                if(preingresos.get(0).getEstilo().getProductoPK().getTipoProducto() == 2 || preingresos.get(0).getEstilo().getProductoPK().getTipoProducto() == 3 ){
                    new funciones().setMsj(2,"PRODUCTO ES TIPO ROPA O CARTERA");
                    context.addCallbackParam("vacio",true); //Indica que no hay producto que etiquetar por lo tanto no muestra ningun dialogo
                }else{
                    
                    context.addCallbackParam("leerCaja",false); //Se mostrará dialogo por unidad
                    //Preparar para Leer codigo caja unidad
                    cajaActual = null;
                    //prepararUnidad();
                }
            }else{
                //SI pertenece a una caja, Verificar que ya se haya leido el codigo de la caja
                if((preingresos.get(0).getCodigoCaja() == null) || "".equals(preingresos.get(0).getCodigoCaja()) ){
                    //NO SE HA LEIDO EL CODIGO DE LA CAJA AUN
                    context.addCallbackParam("leerCaja",true); //Se mostrará dialogo por caja
                    //Preparar datos de caja
                     prepararCaja();
                     //Calcular Datos de la Caja
                     consultarCurva();
                     new funciones().setMsj(1,"Nueva Caja a Codificar");
                }else{
                    //YA SE HA LEIDO EL CODIGO DE LA CAJA
                    context.addCallbackParam("leerCaja",false); //Se mostrará dialogo por unidad
                    //Preparar para Leer codigo caja unidad
                    prepararUnidad();
                }  
            }
        }   
    }
    
    
    
    
    /*
     * FUNCION QUE PREPARA LA LECTURA DEL CODIGO POR CAJA
     */
    public void prepararCaja(){
        cajaActual = new Caja();
        //Consultar Curva
        codigoBarraLectura = "";
        resetearCurva();
    }
    
    //Calcula los datos y curva de la caja del preingreso
    public void consultarCurva(){
        List<String> consultaTalla = ejbFacadePreingreso.curvaTallaList(factura,preingresos.get(0).getNumcajaPreingreso());
        List<String> consultaColores = ejbFacadePreingreso.cajaColoresList(factura,preingresos.get(0).getNumcajaPreingreso());
        List curvas = ejbFacadePreingreso.curvaList(factura, preingresos.get(0).getNumcajaPreingreso());
        if(!(consultaTalla == null) && !(curvas == null)){
           //TALLAS
           numTallas = 0;
           numColores = 0;
           //Detalle Caja
           estilo = preingresos.get(0).getEstilo();
           detalle = null;
           for(int t=0; t < consultaTalla.size(); t++){
                if(t==0){
                     detalle = consultaTalla.get(t);
                }else{
                     detalle = detalle + "-" + consultaTalla.get(t);
                }
                tallas[t] = consultaTalla.get(t);
                numTallas++;
           } 
           colores = null;
           for(int c=0; c < consultaColores.size(); c++){
                if(c==0){
                     colores = consultaColores.get(c);
                }else{
                     colores = colores + "-" + consultaColores.get(c);
                }
           } 
           //CURVA
           prepararUnidad();
           costoUnidad = producto.getCostounitario().floatValue();
           costoUnitarioReal = producto.getCostoreal().floatValue();
           precioventaUnidad = producto.getPreciomayoreo().floatValue();
           producto = null;
           //Variables iniciales
           int c = -1; //posicion del arreglo de curva
           String colorActual = ""; //Color Actual
           //Recorremos el arreglo de la consulta
           for(Object consulta: curvas){ // r: posicion en la consulta
               int t; //Posicion de la talla en el arreglo tallas
               Object[] actual = (Object[]) consulta;
               if(!(actual[0].equals(colorActual))){
                   //No es igual (cambio de color)
                   c++; //posicion del arreglo de curva
                   colorActual = (String) actual[0]; //Nuevo Color
                   numColores++;
                   curva[c].setColor(colorActual);
               }
//              //Es igual al color actual
               for(t=0; t < tallas.length; t++){
                   if(actual[1].equals(tallas[t])){
                       break; //Encontrado
                   }
               }
               switch(t){
                   case 0: 
                       curva[c].setT1(Integer.parseInt(actual[2].toString()));
                       unidadesCaja = unidadesCaja + curva[c].getT1(); break;
                   case 1: 
                       curva[c].setT2(Integer.parseInt(actual[2].toString()));
                       unidadesCaja = unidadesCaja + curva[c].getT2(); break;
                   case 2: 
                       curva[c].setT3(Integer.parseInt(actual[2].toString())); 
                       unidadesCaja = unidadesCaja + curva[c].getT3(); break;
                   case 3: 
                       curva[c].setT4(Integer.parseInt(actual[2].toString())); 
                       unidadesCaja = unidadesCaja + curva[c].getT4(); break;
                   case 4: 
                       curva[c].setT5(Integer.parseInt(actual[2].toString())); 
                       unidadesCaja = unidadesCaja + curva[c].getT5(); break;
                   case 5: 
                       curva[c].setT6(Integer.parseInt(actual[2].toString())); 
                       unidadesCaja = unidadesCaja + curva[c].getT6(); break;
                   case 6: 
                       curva[c].setT7(Integer.parseInt(actual[2].toString())); 
                       unidadesCaja = unidadesCaja + curva[c].getT7(); break;
                   case 7: 
                       curva[c].setT8(Integer.parseInt(actual[2].toString())); 
                       unidadesCaja = unidadesCaja + curva[c].getT8(); break;
                   case 8: 
                       curva[c].setT9(Integer.parseInt(actual[2].toString())); 
                       unidadesCaja = unidadesCaja + curva[c].getT9(); break;
                   case 9: 
                       curva[c].setT10(Integer.parseInt(actual[2].toString())); 
                       unidadesCaja = unidadesCaja + curva[c].getT10();break;
               }
           }
           costoCaja = unidadesCaja*costoUnidad;
        } 
    }
    
    
    /*
     * FUNCION QUE PREPARA LA LECTURA DEL CODIGO DE BARRA POR UNIDAD
     */
    
    public void prepararUnidad(){
            producto = new Inventario();
            codigoBarraLectura = "";
            producto.setNumcaja(cajaActual);
            producto.setProducto(preingresos.get(0).getEstilo());
            producto.setColor(preingresos.get(0).getColorPreingreso());
            producto.setGenero(preingresos.get(0).getGeneroPreingreso());
            producto.setMarca(preingresos.get(0).getMarcaPreingreso());
            producto.setMaterial(preingresos.get(0).getMaterialPreingreso());
            producto.setTalla(preingresos.get(0).getTallaPreingreso());
            producto.setCostounitario(preingresos.get(0).getCostounitarioPreingreso());
            producto.setClasepersona(preingresos.get(0).getClasepersonaPreingreso());
            //Calculo del costo real
            double costoReal = preingresos.get(0).getCostounitarioPreingreso().doubleValue() * preingresos.get(0).getFacturaIngreso().getNumretaceo().getFactorCostoViaje().doubleValue();
            producto.setCostoreal(new BigDecimal(costoReal));
            //Calculo del costo Contable
            double costoContable = preingresos.get(0).getCostounitarioPreingreso().doubleValue() * preingresos.get(0).getFacturaIngreso().getNumretaceo().getFactorRetaceo().doubleValue();
            producto.setCostoContable(new BigDecimal(costoContable));
            //Calculo de los precios de mayoreo y detalle
            producto.setPreciomayoreo(calcularPrecioMayoreo(producto.getCostoreal()));
            producto.setPreciounitario(calcularPrecioDetalle(producto.getPreciomayoreo()));
            //OTROS
            producto.setProveedor(factura.getProveedor());
            if(producto.getProveedor().getPaisProveedor().equals("EL SALVADOR")){
                producto.setProcedencia("NACIONAL");
            }else{
                producto.setProcedencia("IMPORTADO");
            }
            
    }
     
    /*
     * FUNCION QUE CODIFICA AUTOMATICAMENTE LAS UNIDADES DE UNA CAJA
     * CON EL MISMO CODGO DE LA CAJA + UN CORRELATIVO
     */
    
    
    
   /*
    * FUNCION QUE LEE Y VALIDA CODIGO POR UNIDAD
    */ 
   public void leerCodigo(){
       RequestContext context = RequestContext.getCurrentInstance(); 
       if(codigoBarraLectura.length() > 0 && codigoBarraLectura.length() <= 30){
           if(!existeUnidadCod(codigoBarraLectura)){
                producto.setCodigo(codigoBarraLectura);
                producto.setUbicacionProducto(ubicacionUnidad);
                preingresos.get(0).setCodigoBarra(codigoBarraLectura);
                codigoBarraLectura = "";
                //Validar codigo de barra
                context.addCallbackParam("validar",true);
                //Guardar producto
                productos.add(producto);
                //Persistir Cambios
                ejbFacadePreingreso.edit(preingresos.get(0));
                //Remover Producto de la lista de preingreso
                preingresos.remove(0);
                if(preingresos.isEmpty()){
                    context.addCallbackParam("vacio",true);
                    new funciones().setMsj(1,"Todos los productos ya han sido codificados");
                }else{
                     context.addCallbackParam("vacio",false);
                     etiquetarProducto();
                }
           }else{
                codigoBarraLectura = "";
                new funciones().setMsj(2, "Codigo de barra de la UNIDAD YA EXISTE , Por favor cambielo");
                context.addCallbackParam("validar",false);
           }
           
       }else{
           codigoBarraLectura = "";
           new funciones().setMsj(2, "Codigo Invalido debe de tener 10 digitos");
           context.addCallbackParam("validar",false);
       }
       
   }
   
   
   /*
    * FUNCION QUE LEE Y VALIDA CODIGO POR CAJA
    */ 
   public void leerCodigoCaja(){
       RequestContext context = RequestContext.getCurrentInstance(); 
       codigoBarraLectura = codigoBarraLectura.toUpperCase();
       if(codigoBarraLectura.length() <= 20){
           if(!existeCajaCod(codigoBarraLectura)){
                cajaActual.setNumcaja(codigoBarraLectura);
                cajaActual.setCostoUnitario(new BigDecimal(costoUnidad));
                cajaActual.setCostoUnitarioReal(new BigDecimal(costoUnitarioReal));
                cajaActual.setCostoCaja(new BigDecimal(costoCaja));
                cajaActual.setPrecioventaUnidad(new BigDecimal(precioventaUnidad));
                cajaActual.setUbicacionCaja(ubicacionCaja);
                cajaActual.setCompleta(true);
                cajaActual.setProducto(estilo);
                cajaActual.setUnidadesCaja(unidadesCaja);
                cajaActual.setDetalleCaja(detalle);
                cajaActual.setColoresCaja(colores);
                //actualizar codigos de caja en todos los preingresos de la misma caja
                int caja = preingresos.get(0).getNumcajaPreingreso();
                for(int pre=0; pre < preingresos.size(); pre++){
                     if(preingresos.get(pre).getNumcajaPreingreso() != null && preingresos.get(pre).getNumcajaPreingreso() != 0){
                         if(preingresos.get(pre).getNumcajaPreingreso().intValue() == caja ){
                             preingresos.get(pre).setCodigoCaja(cajaActual.getNumcaja());
                             preingresos.get(pre).setCodigoBarra(cajaActual.getNumcaja() + "-" + preingresos.get(pre).getTallaPreingreso()+ "-" +(pre +1));
                             ejbFacadePreingreso.edit(preingresos.get(pre));
                         }
                    }
                }
                listarUnidadesCaja(cajaActual);
                new funciones().setMsj(1,"Se ha Etiquetado la Caja con CODIGO: " + cajaActual.getNumcaja());
                actualizarListaPreingresos(caja);
                if(preingresos.isEmpty()){
                    new funciones().setMsj(1,"Todos los productos de la factura ya han sido codificados");
                }
                context.addCallbackParam("validar",true);
                codigoBarraLectura = "";
           }else{
                codigoBarraLectura = "";
                new funciones().setMsj(2, "Codigo de barra de la CAJA Ya EXISTE , Por favor cambielo");
                context.addCallbackParam("validar",false);
           }
           
       }else{
           codigoBarraLectura = "";
           new funciones().setMsj(2, "Codigo Invalido debe de tener 10 digitos");
           context.addCallbackParam("validar",false);
       }
   }
   
   
   
   
   
   /*
    * FUNCION QUE LEE Y VALIDA CODIGO POR UNIDAD
    */ 
   public void listarUnidadesCaja(Caja caja){
       boolean seguir = true;
       while(seguir){
            prepararUnidad();
            if(preingresos.get(0).getCodigoBarra().length() <= 30){
                if(!ejbFacadeInventario.existeCodigoUnidad(preingresos.get(0).getCodigoBarra())){
                     producto.setCodigo(preingresos.get(0).getCodigoBarra());
                     preingresos.get(0).setCodigoBarra(preingresos.get(0).getCodigoBarra());
                     //Guardar producto
                     productos.add(producto);
                     //Persistir Cambios
                     ejbFacadePreingreso.edit(preingresos.get(0));
                     //Remover Producto de la lista de preingreso
                     preingresos.remove(0);

                }else{
                     new funciones().setMsj(2, "Codigo de una UNIDAD YA EXISTE , Por favor cambielo manualmente");
                }
                if(!preingresos.isEmpty() && preingresos.get(0).getCodigoCaja() != null){
                    if(preingresos.get(0).getCodigoCaja().equals(caja.getNumcaja())){
                        seguir = true;
                    }else{
                        seguir = false;
                    }
                }else{
                    seguir = false;
                }
            }else{
                seguir = false;
            }
       }
       
   }
   
    
   /*
    * FUNCIONES QUE VERIFICAN QUE EL CODIGO DE BARRA LEIDO NO EXISTA
    */
   
   public boolean existeCajaCod(String codigo){
       boolean r = false;
       //Consultar si el codigo ya existe en preingreso
       try{
           if(ejbFacadePreingreso.existeCodigoCaja(codigo)){
               r = true;
            }else{
                if(ejbFacadeCaja.existeCodigoCaja(codigo)){
                    r = true;
                }else{
                    r=false;
                }
           }
           return r;
       }catch(Exception e){
           return r;
       }
   }
   
   public boolean existeUnidadCod(String codigo){
       boolean r = false;
       //Consultar si el codigo ya existe en preingreso
       try{
           if(ejbFacadePreingreso.existeCodigoUnidad(codigo)){
               r = true;
            }else{
               if(ejbFacadeInventario.existeCodigoUnidad(codigo)){
                    r = true;
                 }else{
                    r=false;
                }
           }
           return r;
       }catch(Exception e){
           return r;
       }
   }
   
   //Resetear curva
   public void resetearCurva(){
       //Resetear la curva
       
        for(int t=0; t < tallas.length;t++){
            tallas[t] = "T"+t;
        }
        for(int c=0; c < curva.length; c++){
            curva[c].resetear();
        }
        this.numColores = 0;
        numTallas =0;
        unidadesCaja = 0;
        costoUnidad = 0;
        costoCaja = 0;
        //ubicacionCaja = "";
        detalle = "";
        estilo = null;
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
    * FUNCION QUE GUARDA EN EL INVENTARIO
    */
   
   
   public void guardarInventario(){
       int cajas = 0;
       int inventario = 0;
       if(productos.isEmpty()){
           new funciones().setMsj(2,"No hay productos Codificados");
       }else{
           boolean seguir = true;
           while(seguir){
               Transaccion nuevaTransac = new Transaccion();
               nuevaTransac.setResponsableTransac(new JsfUtil().getEmpleado());
                nuevaTransac.setFechaTransac(new funciones().getTime());
                nuevaTransac.setHoraTransac(new funciones().getTime());
                nuevaTransac.setTipoTransac(2); //INGRESO A INVENTARIO
                nuevaTransac.setIdtransac(ejbFacadeTransaccion.getNextIdTransac());
                //Persistiendo transaccion en la BD
                ejbFacadeTransaccion.create(nuevaTransac);
               Ingreso nuevoIngreso = new Ingreso();
               boolean ingresoUnitario = false;
               if(productos.get(0).getNumcaja() != null){
                   Caja caja = productos.get(0).getNumcaja();
                   int conteo = productosCajaNoCodificados(caja.getNumcaja());
                   if(conteo == 0){
                       cajas++;
                       boolean salir = false;
                       //Persistir Caja
                       ejbFacadeCaja.create(caja);
                        nuevoIngreso.setIdtransac(nuevaTransac);
                        nuevoIngreso.setFacturaIngreso(factura);
                        nuevoIngreso.setFechaRegistro(new funciones().getTime());
                        nuevoIngreso.setNotaIngreso("INGRESO DE CAJA");
                        nuevoIngreso.setNumingreso(ejbFacadeIngreso.getNextIdIngreso());
                       //Persistir a inventario producto en la posicion 0
                        ejbFacadeIngreso.create(nuevoIngreso);
                       while(!salir){
                           if(productos.get(0).getNumcaja() != null){
                                if(productos.get(0).getNumcaja().getNumcaja().equals(caja.getNumcaja())){
                                    //persistir a inventario producto en la posicion 0
                                    //Realizar Nueva transaccion de Ingreso
                                    try{
                                         productos.get(0).setNumingreso(nuevoIngreso);
                                         productos.get(0).setFechaIngreso(nuevoIngreso.getFechaRegistro());
                                         productos.get(0).setNumcaja(caja);
                                         productos.get(0).setEstadoproducto("EN EXISTENCIA");
                                         ejbFacadeInventario.create(productos.get(0));
                                         actualizarEstadoPreingreso(productos.get(0));
                                         inventario++;
                                         productos.remove(0);
                                    }catch(Exception e){}
                                    if(productos.isEmpty()){
                                        salir = true;
                                        seguir = false;
                                    }
                                }else{
                                    salir = true;
                                }
                           }else{
                               salir = true;
                           } 
                       }  
                   }else{
                       seguir = false;
                       new funciones().setMsj(2,"Faltan "+ conteo + " productos que codificar De la caja numero: " + caja.getNumcaja());
                   }
               }else{
                   //Realizar Nueva transaccion de Ingreso
                   
                   try{
                       if(!ingresoUnitario){
                           //Ahora creamos el nuevo Ingreso
                            nuevoIngreso.setIdtransac(nuevaTransac);
                            nuevoIngreso.setFacturaIngreso(factura);
                            nuevoIngreso.setFechaRegistro(new funciones().getTime());
                            nuevoIngreso.setNotaIngreso("INGRESO UNITARIO");
                            nuevoIngreso.setNumingreso(ejbFacadeIngreso.getNextIdIngreso());
                           //Persistir a inventario producto en la posicion 0
                            ejbFacadeIngreso.create(nuevoIngreso);
                            ingresoUnitario = true;
                       }
                        productos.get(0).setNumingreso(nuevoIngreso);
                        productos.get(0).setFechaIngreso(nuevoIngreso.getFechaRegistro());
                        productos.get(0).setEstadoproducto("EN EXISTENCIA");
                        ejbFacadeInventario.create(productos.get(0));
                        actualizarEstadoPreingreso(productos.get(0));
                        inventario++;
                        productos.remove(0);
                   }catch(Exception e){}
                    
                    if(productos.isEmpty()){
                        seguir = false;
                        
                    }
               }
           }
           new funciones().setMsj(1,"Total Productos Inventariados: " + inventario );
           new funciones().setMsj(1,"Total Cajas Inventariadas: "+cajas);
       }
       
   }
   
   /*
    * FUNCION QUE AUTORIZA SI SE PUEDE PERSISTIR CAJA
    * CUENTA EL NUMERO DE PRODUCTOS DE LA CAJA QUE NO HAN SIDO CODIFICADOS
    */
   
   public int productosCajaNoCodificados(String codigoCaja){
       int num = 0;
       for(int i=0; i < preingresos.size(); i++){
           if(preingresos.get(i).getCodigoCaja() != null){
               if(preingresos.get(i).getCodigoCaja().equals(codigoCaja)){
                   num++;
               }
           }
           
       }
       return num;
   }
   
   
   /*
    * FUNCION QUE ACTUALIZA EL ESTADO DE PREINGRESO DE CODIFICANDO
    * A INGRESADO
    */
   public void actualizarEstadoPreingreso(Inventario inv){
       Preingreso buscar = null;
       if(inv.getNumcaja() != null){
            buscar= ejbFacadePreingreso.getPreingresoCodigo(inv.getCodigo(),inv.getNumcaja().getNumcaja());
       }
       if(buscar != null){
                buscar.setEstadoPreingreso("INVENTARIADO");
                ejbFacadePreingreso.edit(buscar);
       }
       
   }
   
   /*
    * FUNCION QUE CALCULA EL PORCENTAJE INVENTARIADO DE UNA FACTURA
    */
   
   public String calculoInventariado(FacturaIngreso F){
       String porcentaje;
       if(F != null){
            float total = ejbFacadePreingreso.countFactura(F);
            float inventariado = ejbFacadePreingreso.countInvFactura(F);
            float P = inventariado/total;
            P = new funciones().redondearMas(P,4);
            P = P*100;
            P = new funciones().redondearMas(P,2);
            porcentaje = P + " %";
            if(P == 100.00){
                RequestContext requestContext = RequestContext.getCurrentInstance();  
                requestContext.execute("facCompletaDialog.show();");
            }
       }else{
           porcentaje = "";
       }
       
       return porcentaje;
   }
   
   /*
    * FUNCION QUE CAMBIA EL ESTADO DE LA FACTURA ACTUAL EN INGRESADA
    * Y LA QUITA DE LA LISTA
    */
   public void facturaCompleta(){
       if(factura != null){
           factura.setEstadoFactura("INGRESADA");
           ejbFacadeFacturaIngreso.edit(factura);
           new funciones().irA("/faces/vistas/ingresos/ingresos.xhtml");
       }
   }
   
   
   /*
    * FUNCIONES PARA EDITAR UNA UNIDAD
    */
   public void editarInfoUnidad(){
       editandoProducto = true;
   }
   
    public void cancelarEditarInfoUnidad(){
       editandoProducto = false;
   }
    
   public void guardarCambiosUnidad(){
       RequestContext context = RequestContext.getCurrentInstance(); 
       try{
            preingresos.get(0).setColorPreingreso(producto.getColor().toUpperCase());
            preingresos.get(0).setTallaPreingreso(producto.getTalla());
            preingresos.get(0).setMarcaPreingreso(producto.getMarca().toUpperCase());
            preingresos.get(0).setMaterialPreingreso(producto.getMaterial().toUpperCase());
            preingresos.get(0).setClasepersonaPreingreso(producto.getClasepersona());
            preingresos.get(0).setGeneroPreingreso(producto.getGenero());
            ejbFacadePreingreso.edit(preingresos.get(0));
            if(preingresos.get(0).getCodigoCaja() != null && !"".equals(preingresos.get(0).getCodigoCaja())){
                consultarCurva();
                cajaActual.setDetalleCaja(detalle);
            }
            etiquetarProducto();
            new funciones().setMsj(1,"Se ha actualizado la informacion del producto exitosamente");
            context.addCallbackParam("validar",true);
       }catch(Exception e){
           new funciones().setMsj(4,"No se pudo actualizar el Articulo");
           context.addCallbackParam("validar",false);
       }
   }
   
   
   
   
   
   
   
   
   
   /*
    * FUNCIONES AGREGADAS PARA SELECCIONAR UNA CAJA A CODIFICAR
    */
   
   
   
   
    public List<Caja> getCajasPreingresadasAux() {
        cajasPreingresadasAux = cajasPreingresadas;
        return cajasPreingresadasAux;
    }

    public void setCajasPreingresadasAux(List<Caja> cajasPreingresadasAux) {
        this.cajasPreingresadasAux = cajasPreingresadasAux;
    }

   
   
   
   
    public List<Caja> getCajasPreingresadas() {
        cajasPreingresadas = new ArrayList<Caja>();
        if(preingresos != null){
            int numCajaActual = -1;
            Caja cajaActual;
            for(int i=0; i < preingresos.size(); i++){
                if(preingresos.get(i).getCodigoBarra() != null || preingresos.get(i).getCodigoCaja() != null ){
                    preingresos.get(i).setCodigoBarra(null);
                    preingresos.get(i).setCodigoCaja(null);
                    ejbFacadePreingreso.edit(preingresos.get(i));
                }
                if(preingresos.get(i).getNumcajaPreingreso() != numCajaActual){
                   //Nueva caja
                    cajaActual = new Caja();
                    numCajaActual = preingresos.get(i).getNumcajaPreingreso();
                    cajaActual.setNumcaja(preingresos.get(i).getNumcajaPreingreso().toString());
                    cajaActual.setProducto(preingresos.get(i).getEstilo());
                    cajaActual.setUnidadesCaja(ejbFacadePreingreso.countUnidadesCajaPreingresadasFactura(factura, preingresos.get(i).getNumcajaPreingreso()));
                    cajaActual.setCostoUnitario(preingresos.get(i).getCostounitarioPreingreso());
                    //Calculo del costo real
                    double costoReal = preingresos.get(i).getCostounitarioPreingreso().doubleValue() * preingresos.get(i).getFacturaIngreso().getNumretaceo().getFactorCostoViaje().doubleValue();
                    cajaActual.setCostoUnitarioReal(new BigDecimal(costoReal));
                    //Calculo de los precios de mayoreo
                    cajaActual.setPrecioventaUnidad(calcularPrecioMayoreo(cajaActual.getCostoUnitarioReal()));
                    List<String> tallas = ejbFacadePreingreso.curvaTallaList(factura, preingresos.get(i).getNumcajaPreingreso());
                    List<String> colores = ejbFacadePreingreso.cajaColoresList(factura, preingresos.get(i).getNumcajaPreingreso());
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


    //Nueva CAJA A CODIFICAR
    public void etiquetarCaja(Caja caja){
        RequestContext context = RequestContext.getCurrentInstance(); 
        editandoProducto = false;
        if(caja != null){
            int numcaja = 0;
            if(caja.getNumcaja() != null){
                numcaja = Integer.parseInt(caja.getNumcaja());
            }
            if(numcaja == 0){
                if(caja.getProducto().getProductoPK().getTipoProducto() != 1){
                    context.addCallbackParam("mostrarDialogCodigoUnidad",true); //Se mostrará dialogo por unidad
                }else{//NO ES UNA CAJA , QUIZAS UNITARIO
                    context.addCallbackParam("mostrar",false); //Se mostrará dialogo por unidad
                    //Preparar para Leer codigo caja unidad
                    cajaActual = null;
                    new funciones().setMsj(2,"NO ES UNA CAJA SINO PRODUCTO UNITARIO DE CLIC EN EL BOTON ETIQUETAR PRDUCTO PARA CODIFICARLO");
               
                }
            }else{
                //SI ES UNA CAJA 
                //NO SE HA LEIDO EL CODIGO DE LA CAJA AUN
                //Preparar datos de caja
                 prepararCaja();
                 //Calcular Datos de la Caja
                 consultarCurvaCaja(caja);
                 new funciones().setMsj(1,"Nueva Caja a Codificar");
                 context.addCallbackParam("mostrar",true); //Se mostrará dialogo por caja
            }
        }else{
            new funciones().setMsj(3, "ERRROR CAJA VALOR NULL");
        }
    }
    
    
    
     
    //Calcula los datos y curva de la caja SELECCIONADA
    public void consultarCurvaCaja(Caja caja){
        idCaja = Integer.parseInt(caja.getNumcaja());
        List<String> consultaTalla = ejbFacadePreingreso.curvaTallaList(factura,idCaja);
        List<String> consultaColores = ejbFacadePreingreso.cajaColoresList(factura,idCaja);
        List curvas = ejbFacadePreingreso.curvaList(factura, idCaja);
        if(!(consultaTalla == null) && !(curvas == null)){
           //TALLAS
           numTallas = 0;
           numColores = 0;
           //Detalle Caja
           estilo = caja.getProducto();
           detalle = null;
           for(int t=0; t < consultaTalla.size(); t++){
                if(t==0){
                     detalle = consultaTalla.get(t);
                }else{
                     detalle = detalle + "-" + consultaTalla.get(t);
                }
                tallas[t] = consultaTalla.get(t);
                numTallas++;
           } 
           colores = null;
           for(int c=0; c < consultaColores.size(); c++){
                if(c==0){
                     colores = consultaColores.get(c);
                }else{
                     colores = colores + "-" + consultaColores.get(c);
                }
           } 
           //COSTOS y PRECIOS
           costoUnidad = caja.getCostoUnitario().floatValue();
           costoUnitarioReal = caja.getCostoUnitarioReal().floatValue();
           precioventaUnidad = caja.getPrecioventaUnidad().floatValue();
           //Variables iniciales
           int c = -1; //posicion del arreglo de curva
           String colorActual = ""; //Color Actual
           //Recorremos el arreglo de la consulta
           for(Object consulta: curvas){ // r: posicion en la consulta
               int t; //Posicion de la talla en el arreglo tallas
               Object[] actual = (Object[]) consulta;
               if(!(actual[0].equals(colorActual))){
                   //No es igual (cambio de color)
                   c++; //posicion del arreglo de curva
                   colorActual = (String) actual[0]; //Nuevo Color
                   numColores++;
                   curva[c].setColor(colorActual);
               }
//              //Es igual al color actual
               for(t=0; t < tallas.length; t++){
                   if(actual[1].equals(tallas[t])){
                       break; //Encontrado
                   }
               }
               switch(t){
                   case 0: 
                       curva[c].setT1(Integer.parseInt(actual[2].toString()));
                       unidadesCaja = unidadesCaja + curva[c].getT1(); break;
                   case 1: 
                       curva[c].setT2(Integer.parseInt(actual[2].toString()));
                       unidadesCaja = unidadesCaja + curva[c].getT2(); break;
                   case 2: 
                       curva[c].setT3(Integer.parseInt(actual[2].toString())); 
                       unidadesCaja = unidadesCaja + curva[c].getT3(); break;
                   case 3: 
                       curva[c].setT4(Integer.parseInt(actual[2].toString())); 
                       unidadesCaja = unidadesCaja + curva[c].getT4(); break;
                   case 4: 
                       curva[c].setT5(Integer.parseInt(actual[2].toString())); 
                       unidadesCaja = unidadesCaja + curva[c].getT5(); break;
                   case 5: 
                       curva[c].setT6(Integer.parseInt(actual[2].toString())); 
                       unidadesCaja = unidadesCaja + curva[c].getT6(); break;
                   case 6: 
                       curva[c].setT7(Integer.parseInt(actual[2].toString())); 
                       unidadesCaja = unidadesCaja + curva[c].getT7(); break;
                   case 7: 
                       curva[c].setT8(Integer.parseInt(actual[2].toString())); 
                       unidadesCaja = unidadesCaja + curva[c].getT8(); break;
                   case 8: 
                       curva[c].setT9(Integer.parseInt(actual[2].toString())); 
                       unidadesCaja = unidadesCaja + curva[c].getT9(); break;
                   case 9: 
                       curva[c].setT10(Integer.parseInt(actual[2].toString())); 
                       unidadesCaja = unidadesCaja + curva[c].getT10();break;
               }
           }
           costoCaja = caja.getCostoCaja().floatValue();
        } 
    }
    
    
    
    /*
    * FUNCION QUE LEE Y VALIDA CODIGO DE CAJA SELECCIONADA
    */ 
   public void leerCodigoCajaSeleccionada(){
       RequestContext context = RequestContext.getCurrentInstance(); 
       filtro = null;
       codigoBarraLectura = codigoBarraLectura.toUpperCase();
       if(codigoBarraLectura.length() <= 20){
           if(!existeCajaCod(codigoBarraLectura)){
                cajaActual = new Caja();
                cajaActual.setNumcaja(codigoBarraLectura);
                cajaActual.setCostoUnitario(new BigDecimal(costoUnidad));
                cajaActual.setCostoUnitarioReal(new BigDecimal(costoUnitarioReal));
                cajaActual.setCostoCaja(new BigDecimal(costoCaja));
                cajaActual.setPrecioventaUnidad(new BigDecimal(precioventaUnidad));
                cajaActual.setUbicacionCaja(ubicacionCaja);
                cajaActual.setCompleta(true);
                cajaActual.setProducto(estilo);
                cajaActual.setUnidadesCaja(unidadesCaja);
                cajaActual.setDetalleCaja(detalle);
                cajaActual.setColoresCaja(colores);
                //actualizar codigos de caja en todos los preingresos de la misma caja
                int caja = idCaja;
                int correlativo = 1;
                for(int pre=0; pre < preingresos.size(); pre++){
                     if(preingresos.get(pre).getNumcajaPreingreso() != null && preingresos.get(pre).getNumcajaPreingreso() != 0){
                         if(preingresos.get(pre).getNumcajaPreingreso().intValue() == caja ){
                             preingresos.get(pre).setCodigoCaja(cajaActual.getNumcaja());
                             preingresos.get(pre).setCodigoBarra(cajaActual.getNumcaja() + "-" + preingresos.get(pre).getTallaPreingreso()+ "-" + correlativo);
                             correlativo++;
                             ejbFacadePreingreso.edit(preingresos.get(pre));
                         }
                    }
                }
                listarUnidadesCajaSeleccionada();
                new funciones().setMsj(1,"Se ha Etiquetado la Caja con CODIGO: " + cajaActual.getNumcaja());
                actualizarListaPreingresos(caja);
                if(preingresos.isEmpty()){
                    new funciones().setMsj(1,"Todos los productos de la factura ya han sido codificados");
                }
                context.addCallbackParam("validar",true);
                codigoBarraLectura = "";
           }else{
                codigoBarraLectura = "";
                new funciones().setMsj(2, "Codigo de barra de la CAJA Ya EXISTE , Por favor cambielo");
                context.addCallbackParam("validar",false);
           }
           
       }else{
           codigoBarraLectura = "";
           new funciones().setMsj(2, "Codigo Invalido debe de tener 10 digitos");
           context.addCallbackParam("validar",false);
       }
   }
   
   
   //FUNCION QUE ACTUALIZA LA LISTA DE PREINGRESOS
   public void actualizarListaPreingresos(int caja){
       Caja eliminar = null;
       if(cajasPreingresadasAux != null){
           for(Caja actual : cajasPreingresadasAux){
               if(actual.getNumcaja().equals(String.valueOf(caja))){
                   eliminar = actual;
                   break;
               }
           }
           cajasPreingresadasAux.remove(eliminar);
           cajasPreingresadas = cajasPreingresadasAux;
       }
   }
   
   
   /*
    * FUNCION QUE LEE Y VALIDA CODIGO POR UNIDAD
    */ 
   public void listarUnidadesCajaSeleccionada(){
       unidadesCajaSeleccionada = new ArrayList<Preingreso>();
       if(preingresos != null){
           for(Preingreso actual : preingresos){
               if(actual.getNumcajaPreingreso() == idCaja){
                   if(actual.getCodigoCaja() != null && !"".equals(actual.getCodigoCaja())){
                       //LISTAR
                       unidadesCajaSeleccionada.add(actual);
                   }else{
                       new funciones().setMsj(3,"PRODUCTO SIN CODIGO DE CAJA");
                   }
               }
           }
           
           if(unidadesCajaSeleccionada.size() == cajaActual.getUnidadesCaja()){
                //COMIENZA A LISTAR UNIDAD POR UNIDAD Y HA ELIMINAR DE PREINGRESO
                boolean seguir = true;
                while(seguir){
                     prepararUnidadCaja();
                     if(unidadesCajaSeleccionada.get(0).getCodigoBarra().length() <= 30){
                         if(!ejbFacadeInventario.existeCodigoUnidad(unidadesCajaSeleccionada.get(0).getCodigoBarra())){
                              producto.setCodigo(unidadesCajaSeleccionada.get(0).getCodigoBarra());
                              //preingresos.get(0).setCodigoBarra(preingresos.get(0).getCodigoBarra());
                              //Guardar producto
                              productos.add(producto);
                              //Persistir Cambios
                              ejbFacadePreingreso.edit(unidadesCajaSeleccionada.get(0));
                              //Remover Producto de la lista de preingreso
                              preingresos.remove(unidadesCajaSeleccionada.remove(0));

                         }else{
                              new funciones().setMsj(2, "Codigo de una UNIDAD YA EXISTE , Por favor cambielo manualmente");
                         }
                         if(!unidadesCajaSeleccionada.isEmpty()){
                             seguir = true;
                         }else{
                             seguir = false;
                         }
                     }else{
                         seguir = false;
                     }
               }
           }else{
               new funciones().setMsj(2,"NO TODOS LOS PRODUCTOS DE LA CAJA FUERON ETIQUETADOS");
           }
        } 
   }
   
   /*
     * FUNCION QUE PREPARA LA LECTURA DEL CODIGO DE BARRA POR UNIDAD
     */
    
    public void prepararUnidadCaja(){
            producto = new Inventario();
            codigoBarraLectura = "";
            producto.setNumcaja(cajaActual);
            producto.setProducto(unidadesCajaSeleccionada.get(0).getEstilo());
            producto.setColor(unidadesCajaSeleccionada.get(0).getColorPreingreso());
            producto.setGenero(unidadesCajaSeleccionada.get(0).getGeneroPreingreso());
            producto.setMarca(unidadesCajaSeleccionada.get(0).getMarcaPreingreso());
            producto.setMaterial(unidadesCajaSeleccionada.get(0).getMaterialPreingreso());
            producto.setTalla(unidadesCajaSeleccionada.get(0).getTallaPreingreso());
            producto.setCostounitario(cajaActual.getCostoUnitario());
            producto.setClasepersona(unidadesCajaSeleccionada.get(0).getClasepersonaPreingreso());
            producto.setCostoreal(cajaActual.getCostoUnitarioReal());
            //Calculo del costo Contable
            double costoContable = producto.getCostounitario().doubleValue() * unidadesCajaSeleccionada.get(0).getFacturaIngreso().getNumretaceo().getFactorRetaceo().doubleValue();
            producto.setCostoContable(new BigDecimal(costoContable));
            //Calculo de los precios de mayoreo y detalle
            producto.setPreciomayoreo(cajaActual.getPrecioventaUnidad());
            producto.setPreciounitario(calcularPrecioDetalle(producto.getPreciomayoreo()));
            //OTROS
            producto.setProveedor(factura.getProveedor());
            if(producto.getProveedor().getPaisProveedor().equals("EL SALVADOR")){
                producto.setProcedencia("NACIONAL");
            }else{
                producto.setProcedencia("IMPORTADO");
            } 
    }
    
    
    private int numUnitario;

    public int getNumUnitario() {
        numUnitario = 0;
        if(preingresos != null){
            for(Preingreso actual: preingresos){
                if(actual.getNumcajaPreingreso() == 0){
                    numUnitario++;
                }
            }
        }
        
        return numUnitario;
    }

    public void setNumUnitario(int numUnitario) {
        this.numUnitario = numUnitario;
    }
    
    
    
    public void ingresarUnitario(){
        List<Preingreso> aux = new ArrayList<Preingreso>();
        
        int numUnitarioFactura = ejbFacadeInventario.countUnitario(factura);
        for(Preingreso actual: preingresos){
            if(actual.getNumcajaPreingreso() == 0){
                aux.add(actual);
            }
        }
        while(!aux.isEmpty()){
            numUnitarioFactura++;
            prepararUnidad(aux.get(0));
            codigoBarraLectura = factura.getFacturaIngresoPK().getDocumentoCompra() + "-" + numUnitarioFactura;
            if(codigoBarraLectura.length() > 0 && codigoBarraLectura.length() <= 20){
                if(!existeUnidadCod(codigoBarraLectura)){
                     producto.setCodigo(codigoBarraLectura);
                     producto.setUbicacionProducto(ubicacionUnidad);
                     codigoBarraLectura = "";
                     //Guardar producto
                     productos.add(producto);
                     //Remover Producto de la lista de preingreso
                     aux.get(0).setEstadoPreingreso("INVENTARIADO");
                     ejbFacadePreingreso.edit(aux.get(0));
                     preingresos.remove(aux.remove(0));
                }

            }
            getCajasPreingresadas();
        }
    }
    
    
    public void prepararUnidad(Preingreso pre){
            producto = new Inventario();
            producto.setNumcaja(null);
            producto.setProducto(pre.getEstilo());
            producto.setColor(pre.getColorPreingreso());
            producto.setGenero(pre.getGeneroPreingreso());
            producto.setMarca(pre.getMarcaPreingreso());
            producto.setMaterial(pre.getMaterialPreingreso());
            producto.setTalla(pre.getTallaPreingreso());
            producto.setCostounitario(pre.getCostounitarioPreingreso());
            producto.setClasepersona(pre.getClasepersonaPreingreso());
            //Calculo del costo real
            double costoReal = pre.getCostounitarioPreingreso().doubleValue() * pre.getFacturaIngreso().getNumretaceo().getFactorCostoViaje().doubleValue();
            producto.setCostoreal(new BigDecimal(costoReal));
            //Calculo del costo Contable
            double costoContable = pre.getCostounitarioPreingreso().doubleValue() * pre.getFacturaIngreso().getNumretaceo().getFactorRetaceo().doubleValue();
            producto.setCostoContable(new BigDecimal(costoContable));
            //Calculo de los precios de mayoreo y detalle
            producto.setPreciomayoreo(calcularPrecioMayoreo(producto.getCostoreal()));
            producto.setPreciounitario(calcularPrecioDetalle(producto.getPreciomayoreo()));
            //OTROS
            producto.setProveedor(factura.getProveedor());
            if(producto.getProveedor().getPaisProveedor().equals("EL SALVADOR")){
                producto.setProcedencia("NACIONAL");
            }else{
                producto.setProcedencia("IMPORTADO");
            }
            
    }
    
    
    
}//FIN CLASE INGRESO CONTROLLER
