package com.ddicark.controladores;

import com.ddicark.auxiliares.JsfUtil;
import com.ddicark.auxiliares.curvaColor;
import com.ddicark.entidades.Inventario;
import com.ddicark.auxiliares.funciones;
import com.ddicark.entidades.Caja;
import com.ddicark.entidades.Cliente;
import com.ddicark.entidades.DetalleEnvio;
import com.ddicark.entidades.DetalleEnvioPK;
import com.ddicark.entidades.DetallePedido;
import com.ddicark.entidades.Empleado;
import com.ddicark.entidades.Envio;
import com.ddicark.entidades.FacturaIngreso;
import com.ddicark.entidades.Pedido;
import com.ddicark.jpaFacades.CajaFacade;
import com.ddicark.jpaFacades.DetalleEnvioFacade;
import com.ddicark.jpaFacades.DetallePedidoFacade;
import com.ddicark.jpaFacades.EnvioFacade;
import com.ddicark.jpaFacades.FacturaFacade;
import com.ddicark.jpaFacades.InventarioFacade;
import com.ddicark.jpaFacades.PedidoFacade;
import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import java.io.File;
import java.io.IOException;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FlowEvent;


@ManagedBean(name = "inventarioController")
@SessionScoped
public class InventarioController extends AbstractController<Inventario> implements Serializable {

    @EJB
    private InventarioFacade ejbFacadeInventario;
    @EJB
    private PedidoFacade ejbFacadePedido;
    @EJB
    private FacturaFacade ejbFacadeFactura;
    @EJB
    private DetallePedidoFacade ejbFacadeDetallePedido;
    @EJB
    private CajaFacade ejbFacadeCaja;
    @EJB
    private EnvioFacade ejbFacadeEnvio;
    @EJB
    private DetalleEnvioFacade ejbFacadeDetalleEnvio;

    public InventarioController() {
        super(Inventario.class);
    }
    //Inicializa controlador de persistencia de proveedores
    @PostConstruct
    public void init() {
        super.setFacade(ejbFacadeInventario);
    }

    //Variables de control
    private int tipoProducto;
    private int tipoMuestra;
    private List<Inventario> muestras;
    private Inventario nuevaMuestra;
    private Empleado empleadoMuestra;
    private String codigoBarra;
    private Inventario articulo;
    private Caja cajaMuestra;
    private String colorMuestra;
    private List<Inventario> detalleCajaMuestra;
    private List<String> coloresCajaMuestra;
    private String tallaMuestra;
    private List<String> tallasCajaMuestra;
    
    //Variables para Reporte Pedidos vrs Existencias
    private int tipoReportePedidosExistencias = 1;
    
    //Variables para Despacho de Productos
    private Pedido ordenPedido;
    private List<DetallePedido> detalleOrdenPedido;
    private List<Inventario> detalleDespacho;
    private int unidadesEmpacadas;
    private int unidadesDespachadas;
    private int unidadesPedidas;
    private boolean buscarCaja = true;
    private boolean listarPrecioMayoreo = true;
    private float montoEnvio;
    private Inventario articuloEnvio;
    private Caja cajaEnvio;
    private List<Inventario> detalleCajaEnvio;
    private float porcentajeEmpacado;
    private List<String> cajasListadas = new ArrayList<String>();
    private int unidadesCajaListadas;
    private List<Inventario> noListadosEnvio;
    private Inventario productoAEliminar;
    private boolean eliminarEnvioCaja = false;
    private boolean preguntarEliminarCajaEnvio = false;
    private Empleado vendedor;
    private Envio ordenEnvio;
    private List<DetalleEnvio> detalleEnvio;
    private String observacionesEnvio;
    
    private Envio imprimirOrden;
    
    
    private curvaColor[] listarCurva = {new curvaColor(),new curvaColor(),new curvaColor(),new curvaColor(),new curvaColor(),new curvaColor()};
    private int unidadesAlistar;
    
    
    private List<Inventario> consultaEstiloUnitario;
    private String estiloUnitario;

    //GETTERS AND SETTERS
    public String getEstiloUnitario() {
        return estiloUnitario;
    }

    public void setEstiloUnitario(String estiloUnitario) {
        this.estiloUnitario = estiloUnitario;
    }
    
    
    public List<Inventario> getConsultaEstiloUnitario() {
        return consultaEstiloUnitario;
    }

    public void setConsultaEstiloUnitario(List<Inventario> consultaEstiloUnitario) {
        this.consultaEstiloUnitario = consultaEstiloUnitario;
    }
   
    
    
    
    public int getTipoProducto() {
        return tipoProducto;
    }

    public void setTipoProducto(int tipoProducto) {
        this.tipoProducto = tipoProducto;
    }

    public int getTipoMuestra() {
        return tipoMuestra;
    }

    public void setTipoMuestra(int tipoMuestra) {
        this.tipoMuestra = tipoMuestra;
    }
    
    public void iniciarMuestra(){
        tipoProducto = 1;
    }

    public Inventario getNuevaMuestra() {
        return nuevaMuestra;
    }

    public void setNuevaMuestra(Inventario nuevaMuestra) {
        this.nuevaMuestra = nuevaMuestra;
    }
    
    public List<Inventario> getMuestras() {
        muestras = ejbFacadeInventario.muestraList(tipoProducto);
        return muestras;
    }

    public Empleado getEmpleadoMuestra() {
        return empleadoMuestra;
    }

    public void setEmpleadoMuestra(Empleado empleadoMuestra) {
        this.empleadoMuestra = empleadoMuestra;
    }

    
    public String getCodigoBarra() {
        return codigoBarra;
    }

    public void setCodigoBarra(String codigoBarra) {
        this.codigoBarra = codigoBarra;
    }

    public Inventario getArticulo() {
        return articulo;
    }

    public void setArticulo(Inventario articulo) {
        this.articulo = articulo;
    }

    public Caja getCajaMuestra() {
        return cajaMuestra;
    }

    public void setCajaMuestra(Caja cajaMuestra) {
        this.cajaMuestra = cajaMuestra;
    }

    public String getColorMuestra() {
        return colorMuestra;
    }

    public void setColorMuestra(String colorMuestra) {
        this.colorMuestra = colorMuestra;
    }

    public List<Inventario> getDetalleCajaMuestra() {
        detalleCajaMuestra = ejbFacadeInventario.detalleDeCaja(cajaMuestra);
        return detalleCajaMuestra;
    }


    
    public List<String> getColoresCajaMuestra() {
        return coloresCajaMuestra;
    }


    public String getTallaMuestra() {
        return tallaMuestra;
    }

    public void setTallaMuestra(String tallaMuestra) {
        this.tallaMuestra = tallaMuestra;
    }

    public List<String> getTallasCajaMuestra() {
        return tallasCajaMuestra;
    }

    
    
    public int getTipoReportePedidosExistencias() {
        return tipoReportePedidosExistencias;
    }

    public void setTipoReportePedidosExistencias(int tipoReportePedidosExistencias) {
        this.tipoReportePedidosExistencias = tipoReportePedidosExistencias;
    }

    public Pedido getOrdenPedido() {
        return ordenPedido;
    }

    public void setOrdenPedido(Pedido ordenPedido) {
        this.ordenPedido = ordenPedido;
    }

    public List<DetallePedido> getDetalleOrdenPedido() {
        if(ordenPedido != null){
            detalleOrdenPedido = ejbFacadeDetallePedido.getDetallePedido(ordenPedido.getPedidoPK());
        }
        return detalleOrdenPedido;
    }

    public List<Inventario> getDetalleDespacho() {
        return detalleDespacho;
    }

    public void setDetalleDespacho(List<Inventario> detalleDespacho) {
        this.detalleDespacho = detalleDespacho;
    }

    public int getUnidadesEmpacadas() {
        unidadesEmpacadas = 0;
        if(detalleDespacho !=null){
            unidadesEmpacadas = detalleDespacho.size();
        }
        return unidadesEmpacadas;
    }

    public void setUnidadesEmpacadas(int unidadesEmpacadas) {
        this.unidadesEmpacadas = unidadesEmpacadas;
    }

    public int getUnidadesDespachadas() {
        unidadesDespachadas = ejbFacadeDetalleEnvio.unidadesDespachadas(ordenPedido);
        return unidadesDespachadas;
    }

    public void setUnidadesDespachadas(int unidadesDespachadas) {
        this.unidadesDespachadas = unidadesDespachadas;
    }
    
    

    public int getUnidadesPedidas() {
        unidadesPedidas = 0;
        if(detalleOrdenPedido != null){
            for(int i=0; i < detalleOrdenPedido.size();i++){
                unidadesPedidas = unidadesPedidas + detalleOrdenPedido.get(i).getCantidad();
            }
        }
        return unidadesPedidas;
    }

    public void setUnidadesPedidas(int unidadesPedidas) {
        this.unidadesPedidas = unidadesPedidas;
    }

    public boolean isBuscarCaja() {
        return buscarCaja;
    }

    public void setBuscarCaja(boolean buscarCaja) {
        this.buscarCaja = buscarCaja;
    }

    public boolean isListarPrecioMayoreo() {
        return listarPrecioMayoreo;
    }

    public void setListarPrecioMayoreo(boolean listarPrecioMayoreo) {
        this.listarPrecioMayoreo = listarPrecioMayoreo;
    }

    public float getMontoEnvio() {
        return montoEnvio;
    }

    public void setMontoEnvio(float montoEnvio) {
        this.montoEnvio = montoEnvio;
    }

    public Inventario getArticuloEnvio() {
        return articuloEnvio;
    }

    public void setArticuloEnvio(Inventario articuloEnvio) {
        this.articuloEnvio = articuloEnvio;
    }

    public float getPorcentajeEmpacado() {
        if(unidadesPedidas != 0){
            if(detalleDespacho != null){
                int empacadas = detalleDespacho.size() + unidadesDespachadas;
                porcentajeEmpacado= ((float) empacadas / (float) unidadesPedidas)*100;
            }
        }
        return porcentajeEmpacado;
    }

    public void setPorcentajeEmpacado(float porcentajeEmpacado) {
        this.porcentajeEmpacado = porcentajeEmpacado;
    }

    public List<String> getCajasListadas() {
        return cajasListadas;
    }

    public void setCajasListadas(List<String> cajasListadas) {
        this.cajasListadas = cajasListadas;
    }
    
    public boolean cajaListada(String codigo){
        boolean encontrada = false;
        if(cajasListadas != null){
            for(int i=0; i < cajasListadas.size(); i++){
                if(cajasListadas.get(i).equals(codigo)){
                    encontrada=true;
                    break;
                }
            }
        }
        return encontrada;
    }

    public Caja getCajaEnvio() {
        return cajaEnvio;
    }

    public void setCajaEnvio(Caja cajaEnvio) {
        this.cajaEnvio = cajaEnvio;
    }

    public List<Inventario> getDetalleCajaEnvio() {
        return detalleCajaEnvio;
    }

    public void setDetalleCajaEnvio(List<Inventario> detalleCajaEnvio) {
        this.detalleCajaEnvio = detalleCajaEnvio;
    }

    public int getUnidadesCajaListadas() {
        return unidadesCajaListadas;
    }

    public void setUnidadesCajaListadas(int unidadesCajaListadas) {
        this.unidadesCajaListadas = unidadesCajaListadas;
    }

    public List<Inventario> getNoListadosEnvio() {
        return noListadosEnvio;
    }

    public void setNoListadosEnvio(List<Inventario> noListadosEnvio) {
        this.noListadosEnvio = noListadosEnvio;
    }

    public boolean isEliminarEnvioCaja() {
        return eliminarEnvioCaja;
    }

    public void setEliminarEnvioCaja(boolean eliminarEnvioCaja) {
        this.eliminarEnvioCaja = eliminarEnvioCaja;
    }

    public boolean isPreguntarEliminarCajaEnvio() {
        return preguntarEliminarCajaEnvio;
    }

    public void setPreguntarEliminarCajaEnvio(boolean preguntarEliminarCajaEnvio) {
        this.preguntarEliminarCajaEnvio = preguntarEliminarCajaEnvio;
    }

    public Empleado getVendedor() {
        return vendedor;
    }

    public void setVendedor(Empleado vendedor) {
        this.vendedor = vendedor;
    }

    public Envio getOrdenEnvio() {
        return ordenEnvio;
    }

    public void setOrdenEnvio(Envio ordenEnvio) {
        this.ordenEnvio = ordenEnvio;
    }

    public List<DetalleEnvio> getDetalleEnvio() {
        return detalleEnvio;
    }

    public void setDetalleEnvio(List<DetalleEnvio> detalleEnvio) {
        this.detalleEnvio = detalleEnvio;
    }

    public String getObservacionesEnvio() {
        return observacionesEnvio;
    }

    public void setObservacionesEnvio(String observacionesEnvio) {
        this.observacionesEnvio = observacionesEnvio;
    }

    public Envio getImprimirOrden() {
        return imprimirOrden;
    }

    public void setImprimirOrden(Envio imprimirOrden) {
        this.imprimirOrden = imprimirOrden;
    }

   
    
    
    
    
  
    
    
    
   /////////////////////////////////////////////////////////////////////FUNCIONES DE GENERALES//////////////////////////////////////////////////////////////////////////////////////// 
    //Autocompletar MARCA
     public List<String> autoCompletarMarca(String query){ 
       query=query.toUpperCase();
       List<String> consulta = null;
       consulta = ejbFacadeInventario.getMarcaList(query);
       return consulta; 
    }
     
     //Autocompletar MATERIAL
     public List<String> autoCompletarMaterial(String query){ 
       query=query.toUpperCase();
       List<String> consulta = null;
       consulta = ejbFacadeInventario.getMaterialList(query);
       return consulta; 
    }
     
     //Autocompletar COLOR
     public List<String> autoCompletarColor(String query){ 
       query=query.toUpperCase();
       List<String> consulta = null;
       consulta = ejbFacadeInventario.getColorList(query);
       return consulta; 
    }
    
     //LISTA DE TODOS LOS COLORES
     public List<String> listaColoresTipo(int tipo){ 
       List<String> consulta = null;
       consulta = ejbFacadeInventario.getColorList(tipo);
       return consulta; 
    }
    
     /*
      * FUNCION QUE CALCULA EL PRECIO A VENDER DEL PRODUCTO 
      * a Precio mayoreo si precioMayoreo = true y
      * a precio al detalle si el precioMayoreo =false
      */
     public BigDecimal calcularPrecioVenta(Inventario articulo ,boolean precioMayoreo){
         BigDecimal precioEstilo = BigDecimal.ZERO;
         if(precioMayoreo){
             //Precio Mayoreo
             precioEstilo = articulo.getProducto().getPrecioventaMayoreo();
         }else{
             //Precio Detalle
             precioEstilo = articulo.getProducto().getPrecioventaUnidad();
         }
         if(precioEstilo != null){
             precioEstilo = new BigDecimal(new funciones().redondearMas(precioEstilo.floatValue(), 2));
         }else{
             precioEstilo = BigDecimal.ZERO;
         }
         return precioEstilo;
     }
     
     
     
     
     
     
     
    /////////////////////////////////////////////////////FUNCIONES PARA CONTROLAR MUESTRAS/////////////////////////////////////////////////////////////////////////////////////////////////
     
     //BUSCAR ARTICULO
     public void prepararBuscarArticulo(){
         codigoBarra = "";
         articulo = null;
         tipoMuestra = 3;
     }
     
     public void buscarArticulo(){
         RequestContext context = RequestContext.getCurrentInstance(); 
         if(codigoBarra != null && !"".equals(codigoBarra)){
             articulo = ejbFacadeInventario.getArticulo(codigoBarra);
             if(articulo != null){
                 if(articulo.getEstadoproducto().equals("MUESTRA")){
                     if(articulo.getResponsableMuestraDer() != null && articulo.getResponsableMuestraIzq() !=null){
                         new funciones().setMsj(2, "El Articulo completo ya se ha asignado como MUESTRA");
                         context.addCallbackParam("validar",false);
                     }else if(articulo.getResponsableMuestraDer() != null){
                            new funciones().setMsj(2, "PAR DERECHO ya esta en MUESTRA");
                            context.addCallbackParam("validar",true);
                            context.addCallbackParam("caja",false);
                        }else if(articulo.getResponsableMuestraIzq() != null){
                            new funciones().setMsj(2, "PAR IZQUIERDO ya esta en MUESTRA");
                            context.addCallbackParam("validar",true);
                            context.addCallbackParam("caja",false);
                        }
                 }else{
                     new funciones().setMsj(1, "Articulo ENCONTRADO");
                     context.addCallbackParam("validar",true);
                 }
                 codigoBarra = "";
                 empleadoMuestra = null;
             }else{
                 cajaMuestra = ejbFacadeCaja.getCaja(codigoBarra);
                 if(cajaMuestra != null){
                     new funciones().setMsj(1, "CAJA ENCONTRADA");
                     context.addCallbackParam("validar",true);
                     context.addCallbackParam("caja",true);
                     codigoBarra = "";
                 }else{
                     new funciones().setMsj(2, "Articulo NO EXISTE en Inventario Verifique el CODIGO");
                     codigoBarra = "";
                     context.addCallbackParam("validar",false);
                 }
             }
         }
     }
     
     /*
      * FUNCION QUE REGISTRA LA ASIGNACION DE MUESTRA
      */
     public void asignarMuestra(){
         RequestContext context = RequestContext.getCurrentInstance(); 
         articulo.setEstadoproducto("MUESTRA");
         switch(tipoMuestra){
             case 1: articulo.setResponsableMuestraDer(empleadoMuestra);break;
             case 2: articulo.setResponsableMuestraIzq(empleadoMuestra);break;
             case 3: 
                     articulo.setResponsableMuestraDer(empleadoMuestra);
                     articulo.setResponsableMuestraIzq(empleadoMuestra);break;
         }
         try{
             ejbFacadeInventario.edit(articulo);
             if(articulo.getNumcaja().getCompleta()){
                 articulo.getNumcaja().setCompleta(false);
                 ejbFacadeCaja.edit(articulo.getNumcaja());
             }
             new funciones().setMsj(1,"MUESTRA ASIGNADA CON EXITO");
             context.addCallbackParam("validar",true);
         }catch(Exception e){
             new funciones().setMsj(4,"No se pudo Actualizar Articulo de MUESTRA");
             context.addCallbackParam("validar",false);
         }
         
     }
     
     
      /*
      * FUNCION QUE REGISTRA LA DEVOLUCION DE MUESTRA
      */
     public void devolverMuestra(){
         RequestContext context = RequestContext.getCurrentInstance(); 
         switch(tipoMuestra){
             case 1: articulo.setResponsableMuestraDer(null);break;
             case 2: articulo.setResponsableMuestraIzq(null);break;
             case 3: 
                     articulo.setResponsableMuestraDer(null);
                     articulo.setResponsableMuestraIzq(null);break;
         }
         try{
             if(articulo.getResponsableMuestraDer() == null && articulo.getResponsableMuestraIzq() == null){
                 articulo.setEstadoproducto("EN EXISTENCIA");
             }
             ejbFacadeInventario.edit(articulo);
             context.addCallbackParam("validar",true);
             int diferencia = articulo.getNumcaja().getUnidadesCaja() - ejbFacadeInventario.countExistenciasCaja(articulo.getNumcaja(), articulo.getNumcaja().getProducto().getProductoPK().getTipoProducto());
             if(diferencia == 0){
                 articulo.getNumcaja().setCompleta(true);
                 ejbFacadeCaja.edit(articulo.getNumcaja());
             }
             new funciones().irA("faces/vistas/muestras/muestras.xhtml");
             new funciones().setMsj(1,"DEVOLUCION DE MUESTRA REGISTRADA EXITOSAMENTE");
         }catch(Exception e){
             new funciones().setMsj(4,"No se pudo Actualizar Articulo de MUESTRA");
             context.addCallbackParam("validar",false);
         }
         
     }
     
     
     
     
     
     
     
     
     
     
     
     
 
//////////////////////////////////////////////////////////FUNCIONES PARA REPORTE PEDIDOS VRS EXISTENCIAS/////////////////////////////////////////////////////////////////////////////////////////////
     
     public void recargarReportePedidosVrsExistencias(){
     }
     
      public List<Object[]> getPedidosVrsExistencias(){
          return ejbFacadeInventario.consultaPedidosVrsExistencias(tipoReportePedidosExistencias);
      }
     
     /*
    * FUNCIONES DE PRUEBA PARA DATA-EXPORTER
    */
   public void postProcessXLS(Object document) {  
        HSSFWorkbook wb = (HSSFWorkbook) document;  
        HSSFSheet sheet = wb.getSheetAt(0);  
        HSSFRow header = sheet.getRow(0);  

        HSSFCellStyle cellStyle = wb.createCellStyle();    
        cellStyle.setFillBackgroundColor(HSSFColor.GREEN.index); 
        //cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        HSSFFont font=wb.createFont();
        /* set the weight of the font */
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        /* attach the font to the style created earlier */
        cellStyle.setFont(font);

        for(int i=0; i < header.getPhysicalNumberOfCells();i++) {  
            HSSFCell cell = header.getCell(i);  
            cell.setCellStyle(cellStyle); 
        }  
   }
   
   public void preProcessPDF(Object document) throws IOException, BadElementException, DocumentException {  
        Document pdf = (Document) document;  
        pdf.open();    

        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();  
        String logoURL = servletContext.getRealPath("") + File.separator + "resources" + File.separator + "imagenes" + File.separator + "logoDD.png";  
        Image logo = Image.getInstance(logoURL);
        logo.scalePercent(25);
        pdf.add(logo);
        pdf.addCreationDate();
   }
   
   
 
   
   
   
   
   
   
   
   
   
 ////////////////////////////////////////////////////FUNCIONES PARA DESPACHO DE PRODUCTOS//////////////////////////////////////////////////////////////////////////////////////////////
 
   
   // FUNCION QUE SE EJECUTA CADA VEZ QUE SE PASA DE UN PASO A OTRO EN EL WIZARD
   public String onFlowProcessDespacho(FlowEvent event) {
        detalleDespacho = new ArrayList<Inventario>();
        montoEnvio = 0;
        cajasListadas.clear();
        return event.getNewStep();    
    }  
     
   /*
    * FUNCION que busca un articulo y lo lista en el detalle del envio
    */
   public void buscarArticuloEnvio(){
       RequestContext context = RequestContext.getCurrentInstance();
       context.addCallbackParam("caja",buscarCaja);
       articuloEnvio = null;
       codigoBarra = codigoBarra.toUpperCase();
       if(buscarCaja){
           //Busqueda de Caja
           if(!cajaListada(codigoBarra)){
               //CAJA NO LISTADA AUN
                cajaEnvio = ejbFacadeCaja.find(codigoBarra);
                if(cajaEnvio != null){
                    //CAJA EXISTE
                    context.addCallbackParam("existe",true);
                    if(ejbFacadeInventario.countArticulosCaja(cajaEnvio,1) > 0){
                        context.addCallbackParam("cajaVacia",false);
                        //Mostrar detalle de la caja y confirmar
                        contarUnidadesCajaListados();
                        //Antes calcular curva
                        new JsfUtil().getCajaController().consultarCurva(cajaEnvio);
                    }else{
                        //Caja Vacia
                        context.addCallbackParam("cajaVacia",true);
                        new funciones().setMsj(3,"Caja VACIA");
                    } 
                }else{
                    //CAJA NO EXISTE
                    context.addCallbackParam("existe",false);
                    new funciones().setMsj(3,"Caja NO EXISTE en Inventario");
                }
           }else{
               //CAJA LISTADA
               context.addCallbackParam("existe",false);
               new funciones().setMsj(2,"CAJA YA ESTA LISTADA EN LA ORDEN DE ENVIO");
           }
           
           
       }else{
            //Busqueda de Unidad
            consultaEstiloUnitario = ejbFacadeInventario.curvaEstiloUnitario(codigoBarra);
            if(consultaEstiloUnitario !=null && !consultaEstiloUnitario.isEmpty()  ){
                //Articulo existe
                context.addCallbackParam("existe",true);
                context.execute("consultaUnitario.show();");
                estiloUnitario = codigoBarra;
                codigoBarra = "";
                actualizarListasConsultaUnitario();
            }else{
                context.addCallbackParam("existe",false);
                new funciones().setMsj(3,"NO HAY ARTICULOS DISPONIBLES EN EL INVENTARIO");
            }
       }
       codigoBarra = "";
   }
   
   //funcion que agrega a la lista el articulo buscado
   public void confirmarListar(){
       try{
           detalleDespacho.add(articuloEnvio);
           actualizarMonto();
           new funciones().setMsj(1, "ARTICULO AGREGADO A ORDEN DE ENVIO");
       }catch(Exception e){
           new funciones().setMsj(3, "NO SE PUDO LISTAR ARTICULO");
       }
       
   }
   
   /*
    * FUNCION que lista todos los productos de la caja
    */
   public void listarCajaCompleta(){
       RequestContext context = RequestContext.getCurrentInstance();
       List<Inventario> productos = ejbFacadeInventario.unidadesByCaja(cajaEnvio, 1);
       if(productos != null){
           int errores = 0;
           int listados = 0;
           int yaexisten = 0;
           int enmuestra = 0;
           int defectuosos = 0;
           if(noListadosEnvio != null){
               noListadosEnvio.clear();
           }else{
               noListadosEnvio = new ArrayList<Inventario>();
           }
           for(Inventario actual : productos){
               if(actual.getEstadoproducto().equals("EN EXISTENCIA")){
                   if(!yaEstaListado(actual.getCodigo())){
                        detalleDespacho.add(actual);
                        listados++;
                   }else{
                        yaexisten++;
                   }
               }else if(actual.getEstadoproducto().equals("MUESTRA")){
                   enmuestra++;
                   detalleDespacho.add(actual);
               }else if(actual.getEstadoproducto().equals("DEFECTUOSO")){   
                   defectuosos++;
                   noListadosEnvio.add(actual);
               }else{
                   errores++;
               }
           }
           if(!noListadosEnvio.isEmpty()){
               context.addCallbackParam("hayNoListados", true);
               if(enmuestra > 0){
                    new funciones().setMsj(2,"SE LISTARON " + enmuestra +" productos que estan en MUESTRA");
                }
                if(defectuosos > 0){
                    new funciones().setMsj(2,"No se pudieron listar " + defectuosos +" productos porque estan DEFECTUOSOS");
                }
           }else{
               context.addCallbackParam("hayNoListados", false);
           }
           if(errores > 0){
               new funciones().setMsj(3,"No se pudieron listar " + errores +" productos");
           }
           if(yaexisten > 0){
               new funciones().setMsj(2,"No se listaron " + yaexisten +" productos porque ya se encuentra en lista");
           }
           actualizarMonto();
           new funciones().setMsj(1, listados +" Productos Listados");
           cajasListadas.add(cajaEnvio.getNumcaja());
       }else{
           new funciones().setMsj(4, "ERROR CAJA VACIA");
       }
   }
   
   
    //funcion que agrega a la lista el articulo buscado y que pertenece a una caja
   public void confirmarCajaCompleta(){
        RequestContext context = RequestContext.getCurrentInstance();
        if(ejbFacadeDetallePedido.productoFuePedido(ordenPedido.getPedidoPK(),articuloEnvio.getProducto(), articuloEnvio.getColor(), articuloEnvio.getTalla())){
            //Articulo SI fue pedido - listar
            context.addCallbackParam("confirmar",false);
            confirmarListar();
        }else{
            //Articulo NO fue pedido (Preguntar si desea listarlo)
            context.addCallbackParam("confirmar",true);
        }
   }
   
    //funcion que preapara para listar solo PARTE de una caja
   public void listarParteCaja(){
        RequestContext context = RequestContext.getCurrentInstance();
        List<Inventario> auxiliar = ejbFacadeInventario.detalleDeCaja(cajaEnvio);
        if(auxiliar != null){
            //depurar de lista productos que ya fueron listados
            detalleCajaEnvio = new ArrayList<Inventario>();
            for(Inventario actual: auxiliar){
                if(!yaEstaListado(actual.getCodigo())){
                    //agregar a lista
                    detalleCajaEnvio.add(actual);
                }else{
                    //No listar
                }
            }
            if(detalleCajaEnvio.isEmpty()){
                new funciones().setMsj(2, "Articulos de la caja ya estan listados o vendidos");
                context.addCallbackParam("mostrar", false);
            }else{
                context.addCallbackParam("mostrar", true);
                new funciones().setMsj(1, "Detalle de caja");
                curvaColor[] aux = new JsfUtil().getCajaController().getCurva();
                for(int e=0; e < listarCurva.length; e++){
                    listarCurva[e].resetear();
                    listarCurva[e].setColor(aux[e].getColor());
                }
            }
        }else{
            new funciones().setMsj(3,"La caja no contiene articulos disponibles para venta");
            context.addCallbackParam("mostrar", false);
        }
   }
   
   
   //Funcion que verifica si el articulo ya esta listado en la orden de envio
   public boolean yaEstaListado(String codigo){
       boolean listado=false;
       for(Inventario actual : detalleDespacho){
           if(actual.getCodigo().equals(codigo)){
               listado = true;
           }
       }
       return listado;
   }
   
   //Funcion que actualiza el monto total del envio
   public void actualizarMonto(){
       BigDecimal monto = BigDecimal.ZERO;
       for(Inventario actual : detalleDespacho){
           BigDecimal precio = calcularPrecioVenta(actual,true);
           //precio = new BigDecimal(precio.floatValue()*1.13);
           precio = new BigDecimal(new funciones().redondearMas(precio.floatValue(),2));
           monto = monto.add(precio);
       }
       montoEnvio = monto.floatValue();
   }
   
   /*
    * FUNCION que cancela el ENVIO
    */
   public void cancelarEnvio(){
       new funciones().irA("faces/vistas/bodega/despacho.xhtml");
       resetEnvio();
   }
   
   public void resetEnvio(){
       detalleDespacho.clear();
       porcentajeEmpacado = 0;
       unidadesEmpacadas = 0;
       unidadesPedidas = 0;
       montoEnvio = 0;
       cajasListadas.clear();
   }
   
   //Funcion que prepara para eliminar un producto o caja completa de la lista
   public void prepararEliminarDeLista(Inventario producto){
       productoAEliminar = producto;
       preguntarEliminarCajaEnvio = false;
       eliminarEnvioCaja = false;
       if(productoAEliminar.getNumcaja() != null){
           if(cajaListada(productoAEliminar.getNumcaja().getNumcaja())){
               preguntarEliminarCajaEnvio = true;
            }
       }
   }
   
   //Elimina un producto de la lista de envio
   public void eliminarDeLista(){
       if(eliminarEnvioCaja){
           List<Inventario> eliminar = new ArrayList<Inventario>();
           for(Inventario actual : detalleDespacho){
               if(actual.getNumcaja().getNumcaja().equals(productoAEliminar.getNumcaja().getNumcaja())){
                   eliminar.add(actual);
               }
           }
           detalleDespacho.removeAll(eliminar);
           cajasListadas.remove(productoAEliminar.getNumcaja().getNumcaja());
           new funciones().setMsj(1, "Productos de la caja N°: "+ productoAEliminar.getNumcaja().getNumcaja()+" ELIMINADOS de la la ORDEN DE ENVIO");
       }else{
           detalleDespacho.remove(productoAEliminar);
           new funciones().setMsj(1, "Producto: "+ productoAEliminar.getCodigo() +" ELIMINADO de la la ORDEN DE ENVIO");
       }
       actualizarMonto();
   }
   
   //CUENTA la unidades de la caja que ya han sido listados a la orden de envio
   public void contarUnidadesCajaListados(){
       unidadesCajaListadas = 0;
       if(detalleDespacho != null){
           for(Inventario actual: detalleDespacho){
               if(actual.getNumcaja() != null){
                   if(actual.getNumcaja().equals(cajaEnvio)){
                        unidadesCajaListadas++;
                    }
               }
           }
       }
   }
   
   
   //prepara el despacho del envio
   public void prepararDespacho(){
       vendedor = null;
       observacionesEnvio = null;
   }
   
   
   //prepara el despacho del envio
   public void resetListaEnvio(){
       if(detalleDespacho != null){
           detalleDespacho.clear();
           cajasListadas.clear();
           new funciones().setMsj(2, "DETALLE ENVIO VACIO");
       }
   }
   /*
    * FUNCION PARA DESPACHAR EL ENVIO
    */
   public void despacharEnvio(){
       if(detalleDespacho == null || detalleDespacho.isEmpty()){
           new funciones().setMsj(3,"NO A LISTADO PRODUCTOS A ENVIAR");
       }else{
          //PROCESAR ORDEN
          //CREAR ORDEN DE ENVIO EN LA BD
            BigDecimal deudaActual = totalDeudaCliente(ordenPedido.getClientePedido());
            BigDecimal deudaTotal = deudaActual.add(new BigDecimal(montoEnvio));
            if(ordenPedido.getTipoPago().equals("AL CONTADO") || ordenPedido.getClientePedido().getLimite() == null || ordenPedido.getClientePedido().getLimite().compareTo(BigDecimal.ZERO) == 0 || deudaTotal.compareTo(ordenPedido.getClientePedido().getLimite()) <=0){
                ordenEnvio =  new Envio();
                ordenEnvio.setPedido(ordenPedido);
                ordenEnvio.setFechaEmpaquetado(new funciones().getTime());
                ordenEnvio.setTotalUnidades(unidadesEmpacadas);
                ordenEnvio.setSubTotal(new BigDecimal(montoEnvio));
                ordenEnvio.setVendedor(vendedor);
                ordenEnvio.setDespacho(new JsfUtil().getEmpleado());
                ordenEnvio.setEstado("EMPACADO");
                ordenEnvio.setNota(observacionesEnvio);
                ordenEnvio.setNumenvio(ejbFacadeEnvio.getNextIdEnvio());
                try{
                  ejbFacadeEnvio.create(ordenEnvio);
                  //CAMBIAR ESTADO DE LOS PRODUCTOS DEL ENVIO Y LIGARLOS A LA NOTA DE ENVIO
                   try{
                      for(Inventario actual: detalleDespacho){
                          DetalleEnvio detalle = new DetalleEnvio(new DetalleEnvioPK());
                          detalle.getDetalleEnvioPK().setNumenvio(ordenEnvio.getNumenvio());
                          detalle.getDetalleEnvioPK().setCodigoProducto(actual.getCodigo());
                          detalle.setInventario(actual);
                          detalle.setPrecioFacturar(calcularPrecioVenta(actual,true));
                          ejbFacadeDetalleEnvio.create(detalle);
                          actual.setEstadoproducto("EMPACADO");
                          ejbFacadeInventario.edit(actual);
                          //Verificar si se afecta una caja
                          if(actual.getNumcaja() != null){ //pertenece a una caja
                              if(actual.getNumcaja().getCompleta()){
                                  actual.getNumcaja().setCompleta(false);
                                  ejbFacadeCaja.edit(actual.getNumcaja());
                              }
                          }
                      }
                      int totalUnidades = unidadesDespachadas + unidadesEmpacadas;
                      new funciones().setMsj(1,"ENVIO PROCESADO");
                      // Mostrar nota de envio
                      imprimirOrden = ordenEnvio;
                      if(totalUnidades  < unidadesPedidas){
                          RequestContext context = RequestContext.getCurrentInstance();
                          context.execute("cfdCompletarPedido.show();");
                      }else{
                          completarPedido();
                      }
                      resetEnvio();
                  }catch(Exception e){
                      //ejbFacadeEnvio.remove(ordenEnvio);
                      new funciones().setMsj(4, "ERROR AL DETALLAR ENVIO");
                  }
                }catch(Exception e){
                    new funciones().setMsj(4, "ERROR AL PROCESAR ORDEN DE ENVIO");
                }
            }else{
                new funciones().setMsj(2,"NO SE PUEDE PROCESAR ENVIO PORQUE EXCEDE LIMITE DE CREDITO DEL CLIENTE"); 
            }
       }
   }
   
   
   
   /*
    * FUNCION QUE SE OCUPA PARA REIMPRIMIR NOTA DE ENVIO
    */
    public void reimprimirNotaEnvio(Envio envio){
        if(envio != null){
            imprimirOrden = envio;
            new funciones().irA("faces/vistas/bodega/notaEnvio.xhtml"); 
        }
    }

    
     public void completarPedido(){
       ordenPedido.setEstadoPedido("PROCESADO");
       ejbFacadePedido.edit(ordenPedido);
       new funciones().irA("faces/vistas/bodega/notaEnvio.xhtml");
   }
     
     
      public void noCompletarPedido(){
          if(!ordenPedido.getEstadoPedido().equals("PROCESANDO")){
              ordenPedido.setEstadoPedido("PROCESANDO");
              ejbFacadePedido.edit(ordenPedido);
          }
       new funciones().irA("faces/vistas/bodega/notaEnvio.xhtml");
   }

    /*
     * LISTAR PARTE DE LA CAJA
     */
    public curvaColor[] getListarCurva() {
        return listarCurva;
    }

    public void setListarCurva(curvaColor[] listarCurva) {
        this.listarCurva = listarCurva;
    }

    public int getUnidadesAlistar() {
        unidadesAlistar = 0;
        int tallas = new JsfUtil().getCajaController().getNumTallas();
        int colores = new JsfUtil().getCajaController().getNumColores();
        for(int c=0; c < colores;c++){
            for(int t=0; t < tallas; t++){
                unidadesAlistar = unidadesAlistar + listarCurva[c].getCantidad(t);
            }
        }
        return unidadesAlistar;
    }

    public void setUnidadesAlistar(int unidadesAlistar) {
        this.unidadesAlistar = unidadesAlistar;
    }
      
      
    public void listarUnidades(){
        if(new JsfUtil().getCajaController() != null){
            int numT = new JsfUtil().getCajaController().getNumTallas();
            int numC = new JsfUtil().getCajaController().getNumColores();
            String[] tallas = new JsfUtil().getCajaController().getTallas();
            int noListados = 0;
            int listados = 0;
            for(int c=0; c < numC; c++){
                for(int t=0; t < numT; t++){
                    for(int l=0; l < listarCurva[c].getCantidad(t); l++){
                        Inventario listar = unidadAListar(cajaEnvio, listarCurva[c].getColor(), tallas[t]);
                        if(listar != null){
                            listarUnidadCaja(listar);
                            listados++;
                        }else{
                            noListados++;
                        }
                    }
                }
            }
            if(listados > 0 ){
                new funciones().setMsj(1,"SE LISTARON " + listados + " UNIDADES AL ENVIO");
            }
            if(noListados > 0 ){
                new funciones().setMsj(2,"NO SE LISTARON " + noListados + " UNIDADES PORQUE NO ESTAN DISPONIBLES");
            }
            
        }
    }
    
    public Inventario unidadAListar(Caja caja, String color, String talla){
        Inventario unidad = null;
        int muestras = 0;
        int defectuosos = 0;
        for(Inventario actual: detalleCajaEnvio){
            if(actual.getNumcaja().getNumcaja().equals(caja.getNumcaja()) && actual.getColor().equals(color) && actual.getTalla().equals(talla)){
                if(actual.getEstadoproducto().equals("EN EXISTENCIA")){
                    unidad = actual;
                }
            }
        }
        return unidad;
    }
    
    //funcion que lista una unidad escogida de una caja
   public void listarUnidadCaja(Inventario seleccion){
       try{
            if(seleccion != null){
                articuloEnvio = seleccion;
                detalleDespacho.add(articuloEnvio);
                actualizarMonto();
                detalleCajaEnvio.remove(articuloEnvio);
                contarUnidadesCajaListados();
            }
       }catch(Exception ex){
           
       }
         
   }
   
   
   
   /*
    * FUNCIONES PARA COMBINAR CAJAS
    */
      
   private String codigo1;
   private String codigo2;
   private Caja caja1;
   private Caja caja2;
   private List<Inventario> detalleCaja1;
   private List<Inventario> detalleCaja2;
   private List<Inventario> combinar1;
   private List<Inventario> combinar2;
   private boolean combinarItem;
   private int numTallas1;
   private int numTallas2;
   private int numColores1;
   private int numColores2;
   private curvaColor[] curva1 = {new curvaColor(),new curvaColor(),new curvaColor(),new curvaColor(),new curvaColor(),new curvaColor()};
   private curvaColor[] curva2 = {new curvaColor(),new curvaColor(),new curvaColor(),new curvaColor(),new curvaColor(),new curvaColor()};
   private String[] tallas1 = new String[10];
   private String[] tallas2 = new String[10];
   
    public String getCodigo1() {
        return codigo1;
    }

    public void setCodigo1(String codigo1) {
        this.codigo1 = codigo1;
    }

    public String getCodigo2() {
        return codigo2;
    }

    public void setCodigo2(String codigo2) {
        this.codigo2 = codigo2;
    }

    public Caja getCaja1() {
        return caja1;
    }

    public void setCaja1(Caja caja1) {
        this.caja1 = caja1;
    }

    public Caja getCaja2() {
        return caja2;
    }

    public void setCaja2(Caja caja2) {
        this.caja2 = caja2;
    }

    public List<Inventario> getDetalleCaja1() {
        return detalleCaja1;
    }

    public void setDetalleCaja1(List<Inventario> detalleCaja1) {
        this.detalleCaja1 = detalleCaja1;
    }

    public List<Inventario> getDetalleCaja2() {
        return detalleCaja2;
    }

    public void setDetalleCaja2(List<Inventario> detalleCaja2) {
        this.detalleCaja2 = detalleCaja2;
    }

    public boolean isCombinarItem() {
        return combinarItem;
    }

    public void setCombinarItem(boolean combinarItem) {
        this.combinarItem = combinarItem;
    }

    public int getNumTallas1() {
        return numTallas1;
    }

    public void setNumTallas1(int numTallas1) {
        this.numTallas1 = numTallas1;
    }

    public int getNumTallas2() {
        return numTallas2;
    }

    public void setNumTallas2(int numTallas2) {
        this.numTallas2 = numTallas2;
    }

    public int getNumColores1() {
        return numColores1;
    }

    public void setNumColores1(int numColores1) {
        this.numColores1 = numColores1;
    }

    public int getNumColores2() {
        return numColores2;
    }

    public void setNumColores2(int numColores2) {
        this.numColores2 = numColores2;
    }

    public curvaColor[] getCurva1() {
        return curva1;
    }

    public void setCurva1(curvaColor[] curva1) {
        this.curva1 = curva1;
    }

    public curvaColor[] getCurva2() {
        return curva2;
    }

    public void setCurva2(curvaColor[] curva2) {
        this.curva2 = curva2;
    }

    public String[] getTallas1() {
        return tallas1;
    }

    public void setTallas1(String[] tallas1) {
        this.tallas1 = tallas1;
    }

    public String[] getTallas2() {
        return tallas2;
    }

    public void setTallas2(String[] tallas2) {
        this.tallas2 = tallas2;
    }
    
    
   
   
    public void prepararCombinarCaja(){
        codigo1 = null;
        codigo2 = null;
        caja1 = null;
        caja2 = null;
        detalleCaja1 = new ArrayList<Inventario>();
        detalleCaja2 = new ArrayList<Inventario>();
        combinar1 = new ArrayList<Inventario>();
        combinar2 = new ArrayList<Inventario>();
        combinarItem = false;
        actualizarCurva(1);
        actualizarCurva(2);
    }
    
    public void buscarCaja1(){
        combinarItem = false;
        if(codigo1 != null && codigo1.length() <= 20){
            codigo1 = codigo1.toUpperCase();
            caja1 = ejbFacadeCaja.getCaja(codigo1);
            if(caja1 != null){
                detalleCaja1 = ejbFacadeInventario.detalleDeCaja(caja1);
                if(detalleCaja1.isEmpty()){
                    new funciones().setMsj(3, "CAJA VACIA");
                }else{
                    new funciones().setMsj(1, "DETALLE DE LA CAJA : " + codigo1 +" CARGADO");
                }
                
            }else{
                detalleCaja1 =  new ArrayList<Inventario>();
                new funciones().setMsj(3, "CODIGO CAJA NO EXISTE EN INVENTARIO");
            }
            combinar1 = new ArrayList<Inventario>();
        }
        actualizarCurva(1);
    }
    
    
    public void buscarCaja2(){
        combinarItem = false;
        if(codigo2 != null && codigo2.length() <= 20){
            codigo2 = codigo2.toUpperCase();
            caja2 = ejbFacadeCaja.getCaja(codigo2);
            if(caja2 != null){
                detalleCaja2 = ejbFacadeInventario.detalleDeCaja(caja2);
                if(detalleCaja2.isEmpty()){
                    new funciones().setMsj(3, "CAJA VACIA");
                }else{
                    new funciones().setMsj(1, "DETALLE DE LA CAJA : " + codigo2 +" CARGADO");
                }
            }else{
                detalleCaja2 =  new ArrayList<Inventario>();
                new funciones().setMsj(3, "CODIGO CAJA NO EXISTE EN INVENTARIO");
            }
            combinar2 = new ArrayList<Inventario>();
        }
        actualizarCurva(2);
    }
    
    
    //Agrega uno a la lista de seleccion
    public void agregarAlista(Inventario producto, int caja){
        if(producto != null){
            switch(caja){
                case 1:
                    if(combinarItem){
                        combinar1.add(producto);
                        new funciones().setMsj(1, combinar1.size() +" UNIDADES SELECCIONADAS");
                    }else{
                        combinar1.remove(producto);
                        new funciones().setMsj(1, combinar1.size() +" UNIDADES SELECCIONADAS");
                    }
                    break;
                case 2:
                    if(combinarItem){
                        combinar2.add(producto);
                        new funciones().setMsj(1, combinar2.size() +" UNIDADES SELECCIONADAS");
                    }else{
                        combinar2.remove(producto);
                        new funciones().setMsj(1, combinar2.size() +" UNIDADES SELECCIONADAS");
                    }
                    break;
            }
            
        }
    }
    
    
    
    public void actualizarCurva(int caja){
        switch(caja){
            case 1:
                if(detalleCaja1.isEmpty()){
                    resetTallas(tallas1);
                    resetearCurva(curva1);
                }else{
                    calcularCurva1();
                }
                break;
            case 2:
                if(detalleCaja2.isEmpty()){
                    resetTallas(tallas2);
                    resetearCurva(curva2);
                }else{
                    calcularCurva2();
                }
                break;
        }
    }
   
    
    public void resetearCurva(curvaColor[] curva){
        for(int i=0; i < curva.length; i++){
            curva[i].resetear();
        }
    }
    
    public void resetTallas(String[] tallas){
        for(int i=0; i < tallas.length; i++){
            tallas[i] = null;
        }
    }
    
    public List<String> tallasCurva(List<Inventario> lista){
        List<String> tallas = new ArrayList<String>();
        for(int i=0; i < lista.size(); i++){
            if(!tallas.contains(lista.get(i).getTalla())){
                tallas.add(lista.get(i).getTalla());
            }
        }
        return tallas;
    }
    
    public List<String> coloresCurva(List<Inventario> lista){
        List<String> colores = new ArrayList<String>();
        for(int i=0; i < lista.size(); i++){
            if(!colores.contains(lista.get(i).getColor())){
                colores.add(lista.get(i).getColor());
            }
        }
        return colores;
    }
    
    
    
    public int cantidadCurva(String color, String talla, List<Inventario> lista){
        int cantidad = 0;
        for(int i=0; i < lista.size(); i++){
            if(lista.get(i).getColor().equals(color) && lista.get(i).getTalla().equals(talla)){
                cantidad++;
            }
        }
        return cantidad;
    }
    
    
    public void calcularCurva1(){
        numTallas1 = 0;
        resetTallas(tallas1);
        numColores1 = 0;
        resetearCurva(curva1);
        //OBTENER TALLAS
        List<String> tallasList = tallasCurva(detalleCaja1);
        numTallas1 = tallasList.size();
        for(int t=0; t < numTallas1; t++){
            tallas1[t] = tallasList.get(t);
        }
        //OBTENER COLORES y CANTIDADES
        List<String> coloresList = coloresCurva(detalleCaja1);
        numColores1 = coloresList.size();
        for(int c=0; c < numColores1; c++){
            curva1[c].setColor(coloresList.get(c));
            for(int t=0; t < numTallas1; t++){
                switch(t){
                    case 0: 
                        curva1[c].setT1(cantidadCurva(curva1[c].getColor(), tallas1[t], detalleCaja1));break;
                    case 1: 
                        curva1[c].setT2(cantidadCurva(curva1[c].getColor(), tallas1[t], detalleCaja1));break;
                    case 2: 
                        curva1[c].setT3(cantidadCurva(curva1[c].getColor(), tallas1[t], detalleCaja1));break;
                    case 3: 
                        curva1[c].setT4(cantidadCurva(curva1[c].getColor(), tallas1[t], detalleCaja1));break;
                    case 4: 
                        curva1[c].setT5(cantidadCurva(curva1[c].getColor(), tallas1[t], detalleCaja1));break;
                    case 5: 
                        curva1[c].setT6(cantidadCurva(curva1[c].getColor(), tallas1[t], detalleCaja1));break;
                    case 6: 
                        curva1[c].setT7(cantidadCurva(curva1[c].getColor(), tallas1[t], detalleCaja1));break;
                    case 7: 
                        curva1[c].setT8(cantidadCurva(curva1[c].getColor(), tallas1[t], detalleCaja1));break;
                    case 8: 
                        curva1[c].setT9(cantidadCurva(curva1[c].getColor(), tallas1[t], detalleCaja1));break;
                    case 9: 
                        curva1[c].setT10(cantidadCurva(curva1[c].getColor(), tallas1[t], detalleCaja1));break;
                }
            }
        }     
    }
        
    public void calcularCurva2(){
        numTallas2 = 0;
        resetTallas(tallas2);
        numColores2 = 0;
        resetearCurva(curva2);
        //OBTENER TALLAS
        List<String> tallasList = tallasCurva(detalleCaja2);
        numTallas2 = tallasList.size();
        for(int t=0; t < numTallas2; t++){
            tallas2[t] = tallasList.get(t);
        }
        //OBTENER COLORES y CANTIDADES
        List<String> coloresList = coloresCurva(detalleCaja2);
        numColores2 = coloresList.size();
        for(int c=0; c < numColores2; c++){
            curva2[c].setColor(coloresList.get(c));
            for(int t=0; t < numTallas2; t++){
                switch(t){
                    case 0: 
                        curva2[c].setT1(cantidadCurva(curva2[c].getColor(), tallas2[t], detalleCaja2));break;
                    case 1: 
                        curva2[c].setT2(cantidadCurva(curva2[c].getColor(), tallas2[t], detalleCaja2));break;
                    case 2: 
                        curva2[c].setT3(cantidadCurva(curva2[c].getColor(), tallas2[t], detalleCaja2));break;
                    case 3: 
                        curva2[c].setT4(cantidadCurva(curva2[c].getColor(), tallas2[t], detalleCaja2));break;
                    case 4: 
                        curva2[c].setT5(cantidadCurva(curva2[c].getColor(), tallas2[t], detalleCaja2));break;
                    case 5: 
                        curva2[c].setT6(cantidadCurva(curva2[c].getColor(), tallas2[t], detalleCaja2));break;
                    case 6: 
                        curva2[c].setT7(cantidadCurva(curva2[c].getColor(), tallas2[t], detalleCaja2));break;
                    case 7: 
                        curva2[c].setT8(cantidadCurva(curva2[c].getColor(), tallas2[t], detalleCaja2));break;
                    case 8: 
                        curva2[c].setT9(cantidadCurva(curva2[c].getColor(), tallas2[t], detalleCaja2));break;
                    case 9: 
                        curva2[c].setT10(cantidadCurva(curva2[c].getColor(), tallas2[t], detalleCaja2));break;
                }
            }
        }     
    }

    
    
    public void combinarCajas(){
        if(validarCombinacion()){
            for(int c1= 0 ; c1 < combinar1.size(); c1++){
                if(detalleCaja1.remove(combinar1.get(c1))){
                    detalleCaja2.add(combinar1.get(c1));
                }
            }
            for(int c2= 0 ; c2 < combinar2.size(); c2++){
                if(detalleCaja2.remove(combinar2.get(c2))){
                    detalleCaja1.add(combinar2.get(c2));
                }
            }
            actualizarCurva(1);
            actualizarCurva(2);
            combinarItem = false;
            combinar1.clear();
            combinar2.clear();
            new funciones().setMsj(1,"COMBINACION EXITOSA");
        }
    }
    
    
    public boolean validarCombinacion(){
        try{
            boolean validar = true;
            String estilo1 = "";
            String estilo2 = "";
            BigDecimal precio1= BigDecimal.ZERO;
            BigDecimal precio2= BigDecimal.ZERO;
            if(detalleCaja1.isEmpty()){
                validar = false;
                new funciones().setMsj(3,"CAJA 1 VACIA");
            }else{
                estilo1 = caja1.getProducto().getProductoPK().getCodestilo();
                precio1= caja1.getPrecioventaUnidad();
            }
            if(detalleCaja2.isEmpty()){
                validar = false;
                new funciones().setMsj(3,"CAJA 2 VACIA");
            }else{
                estilo2 = caja2.getProducto().getProductoPK().getCodestilo();
                precio2= caja2.getPrecioventaUnidad();
            }
            if(!estilo1.equals(estilo2)){
                new funciones().setMsj(2,"SE VAN A COMBINAR ESTILOS DIFERENTES");
            }
            if(!(precio1.compareTo(precio2) == 0)){
                validar = false;
                new funciones().setMsj(3,"NO SE PUEDEN COMBINAR PRECIOS DIFERENTES");
            }
            if(combinar1.isEmpty() && combinar2.isEmpty()){
                validar = false;
                new funciones().setMsj(3,"NO HA SELECCIONADO PRODUCTO A COMBINAR");
            }
            return validar;
        }catch(Exception ex){
            new funciones().setMsj(3,"ERROR INESPERADO");
            return false;
        }
        
    }
    
    
    
    public void confirmarCombinacion(){
        if(detalleCaja1.isEmpty() || detalleCaja2.isEmpty()){
            new funciones().setMsj(3,"SELECCIONE CAJAS A COMBINAR");
        }else{
            for(int c1= 0 ; c1 < detalleCaja1.size(); c1++){
                if(!detalleCaja1.get(c1).getNumcaja().getNumcaja().equals(codigo1)){
                    detalleCaja1.get(c1).setNumcaja(caja1);
                    ejbFacadeInventario.edit(detalleCaja1.get(c1));
                }
            }
            String colores1 = null;
            for(int colores=0; colores < numColores1; colores++){
                if(colores == 0){
                    colores1 = curva1[colores].getColor();
                }else{
                    colores1 = colores1 + " - "+ curva1[colores].getColor();
                }
            }
            for(int c2= 0 ; c2 < detalleCaja2.size(); c2++){
                if(!detalleCaja2.get(c2).getNumcaja().getNumcaja().equals(codigo2)){
                    detalleCaja2.get(c2).setNumcaja(caja2);
                    ejbFacadeInventario.edit(detalleCaja2.get(c2));
                }
            }
            String colores2 = null;
            for(int colores=0; colores < numColores2; colores++){
                if(colores == 0){
                    colores2 = curva2[colores].getColor();
                }else{
                    colores2 = colores2 + " - "+curva2[colores].getColor();
                }
            }
            if(!caja1.getColoresCaja().equals(colores1)){
                caja1.setColoresCaja(colores1);
                if(caja1.getUnidadesCaja() <= detalleCaja1.size()){
                    if(!caja1.getCompleta()){
                        caja1.setCompleta(true);
                    }
                }else{
                    if(caja1.getCompleta()){
                        caja1.setCompleta(false);
                    }
                }
                caja1.setUbicacionCaja("COMBINADA");
                ejbFacadeCaja.edit(caja1);
            }
            if(!caja2.getColoresCaja().equals(colores2)){
                caja2.setColoresCaja(colores2);
                if(caja2.getUnidadesCaja() <= detalleCaja2.size()){
                    if(!caja2.getCompleta()){
                        caja2.setCompleta(true);
                    }
                }else{
                    if(caja2.getCompleta()){
                        caja2.setCompleta(false);
                    }
                }
                caja2.setUbicacionCaja("COMBINADA");
                ejbFacadeCaja.edit(caja2);
            }
            prepararCombinarCaja();
            new funciones().setMsj(1, "COMBINACION GUARDADA CON EXITO");
        }
        
            
    }
    
    
    
    
    //FUNCIONES CONSULTA UNITARIO
    private List<Inventario> subconsultaUnitario;
    private List<FacturaIngreso> facturasUnitario;
    private List<String> coloresUnitario;
    private List<String> materialesUnitario;
    private List<String> tallasUnitario;
    private FacturaIngreso facturaUnitario;
    private String colorUnitario;
    private String materialUnitario;
    private String tallaUnitario;
    private int numUnitario;
    
    private String[] tallas = {null,null,null,null,null,null,null,null,null,null};
    private int[] exist = {0,0,0,0,0,0,0,0,0,0};
    private curvaColor listar = new curvaColor();

    public String[] getTallas() {
        return tallas;
    }

    public void setTallas(String[] tallas) {
        this.tallas = tallas;
    }

    public int[] getExist() {
        return exist;
    }

    public void setExist(int[] exist) {
        this.exist = exist;
    }

    public curvaColor getListar() {
        return listar;
    }

    public void setListar(curvaColor listar) {
        this.listar = listar;
    }

    

    
    
    public List<FacturaIngreso> getFacturasUnitario() {
        return facturasUnitario;
    }

    public void setFacturasUnitario(List<FacturaIngreso> facturasUnitario) {
        this.facturasUnitario = facturasUnitario;
    }

    public FacturaIngreso getFacturaUnitario() {
        return facturaUnitario;
    }

    public void setFacturaUnitario(FacturaIngreso facturaUnitario) {
        this.facturaUnitario = facturaUnitario;
    }

    
    public List<String> getColoresUnitario() {
        return coloresUnitario;
    }

    public void setColoresUnitario(List<String> coloresUnitario) {
        this.coloresUnitario = coloresUnitario;
    }

    public List<String> getMaterialesUnitario() {
        return materialesUnitario;
    }

    public void setMaterialesUnitario(List<String> materialesUnitario) {
        this.materialesUnitario = materialesUnitario;
    }

    
    public String getColorUnitario() {
        return colorUnitario;
    }

    public void setColorUnitario(String colorUnitario) {
        this.colorUnitario = colorUnitario;
    }

    public String getMaterialUnitario() {
        return materialUnitario;
    }

    public void setMaterialUnitario(String materialUnitario) {
        this.materialUnitario = materialUnitario;
    }

    public List<String> getTallasUnitario() {
        return tallasUnitario;
    }

    public void setTallasUnitario(List<String> tallasUnitario) {
        this.tallasUnitario = tallasUnitario;
    }

    public List<Inventario> getSubconsultaUnitario() {
        return subconsultaUnitario;
    }

    public void setSubconsultaUnitario(List<Inventario> subconsultaUnitario) {
        this.subconsultaUnitario = subconsultaUnitario;
    }

    public String getTallaUnitario() {
        return tallaUnitario;
    }

    public void setTallaUnitario(String tallaUnitario) {
        this.tallaUnitario = tallaUnitario;
    }

    public int getNumUnitario() {
        numUnitario = 0;
        for(int i=0; i < 10; i++){
            numUnitario = numUnitario + listar.getCantidad(i);
        }
        return numUnitario;
    }

    public void setNumUnitario(int numUnitario) {
        this.numUnitario = numUnitario;
    }
    
    
    
    
    public void actualizarListasConsultaUnitario(){
        resetCurvaUnitario();
        resetExistenciaUnitario();
        facturasUnitario = new ArrayList<FacturaIngreso>();
        coloresUnitario = new ArrayList<String>();
        materialesUnitario = new ArrayList<String>();
        tallasUnitario = new ArrayList<String>();
        if(consultaEstiloUnitario != null && !consultaEstiloUnitario.isEmpty()){
            for(Inventario actual: consultaEstiloUnitario){
                if(!facturasUnitario.contains(actual.getNumingreso().getFacturaIngreso())){
                    facturasUnitario.add(actual.getNumingreso().getFacturaIngreso());
                }
                if(!coloresUnitario.contains(actual.getColor())){
                    coloresUnitario.add(actual.getColor());
                }
                if(!materialesUnitario.contains(actual.getMaterial())){
                    materialesUnitario.add(actual.getMaterial());
                }
                if(!tallasUnitario.contains(actual.getTalla())){
                    tallasUnitario.add(actual.getTalla());
                }
            }
        }
        colorUnitario = coloresUnitario.get(0);
        materialUnitario = materialesUnitario.get(0);
        for(int i=0; i< tallasUnitario.size(); i++){
            if(i < 10){
                tallas[i] = tallasUnitario.get(i);                
            }else{
                new funciones().setMsj(2, "HAY MAS DE 10 TALLAS DISTINTAS");
            }
        }
        actualizarSubconsultaUnitario();
    }
    
    public void resetCurvaUnitario(){
        for(int i=0; i< 10;i++){
            tallas[i] = null;
        }
    }
    
    public void resetExistenciaUnitario(){
        for(int i=0; i< 10;i++){
            exist[i] = 0;
            listar.resetear();
        }
    }
    
    
    public void updateExistUnitario(String talla){
        for(int i=0; i< 10;i++){
            if(tallas[i].equals(talla)){
              exist[i] = exist[i] + 1;
              break;
            }
        }
    }
    
    public void actualizarSubconsultaUnitario(){
        numUnitario = 0;
        resetExistenciaUnitario();
        subconsultaUnitario = new ArrayList<Inventario>();
        if(consultaEstiloUnitario != null && !consultaEstiloUnitario.isEmpty() && colorUnitario != null && materialUnitario != null){
            for(Inventario actual :consultaEstiloUnitario){
                if(!yaEstaListado(actual.getCodigo())){
                    if(actual.getColor().equals(colorUnitario) && actual.getMaterial().equals(materialUnitario)){
                        subconsultaUnitario.add(actual);
                        updateExistUnitario(actual.getTalla());
                    }
                }
            }
        }
        
    }
    
    
    public void listarUnitario(){
        if(subconsultaUnitario != null && !subconsultaUnitario.isEmpty()){
            int listadas = 0;
            if(numUnitario > 0){
                if(numUnitario <= subconsultaUnitario.size()){
                    for(int i=0; i < 10 ;i++){
                        for(int l=1; l <= listar.getCantidad(i) ; l++){
                            if(listar.getCantidad(i) <= exist[i]){
                                for(Inventario actual : subconsultaUnitario){
                                    if(actual.getTalla().equals(tallas[i])){
                                        if(!yaEstaListado(actual.getCodigo())){
                                            detalleDespacho.add(actual);
                                            listadas++;
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    actualizarMonto();
                    new funciones().setMsj(1, listadas + " UNIDADES LISTADAS");
                    actualizarSubconsultaUnitario();
                }else{
                    new funciones().setMsj(2, "SOLO HAY "+ subconsultaUnitario.size() +" UNIDADES EN EXISTENCIA");
                }
            }else{
                new funciones().setMsj(3, "NUMERO A LISTAR INVALIDO");
            }
        }
    }
    
    
    
    public BigDecimal totalDeudaCliente(Cliente cliente){
        BigDecimal deuda = BigDecimal.ZERO;
        if(cliente != null){
            deuda = new BigDecimal(ejbFacadeFactura.getDeudaPorCreditos(cliente));
        }
        return deuda;
    }
    
}
