package com.ddicark.controladores;

import com.ddicark.entidades.Pedido;
import com.ddicark.auxiliares.JsfUtil;
import com.ddicark.auxiliares.curvaColor;
import com.ddicark.auxiliares.funciones;
import com.ddicark.entidades.Caja;
import com.ddicark.entidades.Cliente;
import com.ddicark.entidades.DetallePedido;
import com.ddicark.entidades.Producto;
import com.ddicark.jpaFacades.CajaFacade;
import com.ddicark.jpaFacades.ClienteFacade;
import com.ddicark.jpaFacades.DetallePedidoFacade;
import com.ddicark.jpaFacades.FacturaFacade;
import com.ddicark.jpaFacades.InventarioFacade;
import com.ddicark.jpaFacades.MunicipiosFacade;
import com.ddicark.jpaFacades.PedidoFacade;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FlowEvent;

@ManagedBean(name = "pedidoController")
@SessionScoped
public class PedidoController extends AbstractController<Pedido> implements Serializable {

    @EJB
    private MunicipiosFacade ejbFacadeMunicipios;
    
    @EJB
    private ClienteFacade ejbFacadeCliente;
    
    @EJB
    private PedidoFacade ejbFacadePedido;
    
     @EJB
    private DetallePedidoFacade ejbFacadeDetallePedido;
     
     @EJB
    private CajaFacade ejbFacadeCaja;
     
     @EJB
    private InventarioFacade ejbFacadeInventario;
    
        
     @EJB
    private FacturaFacade ejbFacadeFactura;
    
     
     public PedidoController() {
       super(Pedido.class);
    }
    //Inicializa controlador de persistencia de proveedores
    @PostConstruct
    public void init() {
        super.setFacade(ejbFacadePedido);
    }

    @Override
    protected void setEmbeddableKeys() {
        //Ingresa los valores de los campos que forman parte de una llave primaria y al mismo tiempo son foraneas
    }

    @Override
    protected void initializeEmbeddableKey() {
        this.getSelected().setPedidoPK(new com.ddicark.entidades.PedidoPK());
    }

    //VARIABLES DE CONSULTA
    List<Pedido> pedidosContado;
    List<Pedido> pedidosCredito;
    List<Pedido> pedidosEnCola;
    List<Pedido> pedidosRegistrados;
    List<Pedido> filtroPedidos;
    //VARIABLES CONTROL
    int tipoProducto;
    List<String> departamentos;
    List<String> municipios;
    Cliente cliente;
    Pedido pedido;
    Pedido seleccion;
    //Detalle de Pedido
    private List<DetallePedido> detalle;
    private Producto producto;
    private String color;
    private List<String> colores;
    private String[] tallas = new String[10];
    private Integer[] cantidades = new Integer[10];
    private BigDecimal pu;
    private boolean porCaja;
    private int numTallas;
    private curvaColor[] curva = {new curvaColor(),new curvaColor(),new curvaColor(),new curvaColor(),new curvaColor(),new curvaColor()};
    private int numColores;
    private int unidadesCaja;
    private int numCajas;
    
    private SelectItem[] condicionPagoOptions;
    private int month;
    private int year;

    public SelectItem[] getCondicionPagoOptions() {
        SelectItem[] options = new SelectItem[3]; 
        options[0] = new SelectItem("", "Select"); 
        options[1] = new SelectItem("AL CONTADO", "AL CONTADO");  
        options[2] = new SelectItem("AL CREDITO", "AL CREDITO");   
        condicionPagoOptions = options;
        return condicionPagoOptions;
    }

    public List<Pedido> getPedidosRegistrados() {
        return pedidosRegistrados;
    }

    public void setPedidosRegistrados(List<Pedido> pedidosRegistrados) {
        this.pedidosRegistrados = pedidosRegistrados;
    }

    public List<Pedido> getFiltroPedidos() {
        return filtroPedidos;
    }

    public void setFiltroPedidos(List<Pedido> filtroPedidos) {
        this.filtroPedidos = filtroPedidos;
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
    
    
    
    
    
    
    //GETTERS AND SETTERS
    public List<Pedido> getPedidosContado() {
        pedidosContado = ejbFacadePedido.getPedidosByTipo("AL CONTADO");
        return pedidosContado;
    }


    public List<Pedido> getPedidosCredito() {
        pedidosCredito = ejbFacadePedido.getPedidosByTipo("AL CREDITO");
        return pedidosCredito;
    }

    public List<Pedido> getPedidosEnCola() {
        pedidosEnCola = ejbFacadePedido.PedidosEnCola();
        return pedidosEnCola;
    }

    
    
    public int getTipoProducto() {
        return tipoProducto;
    }

    public void setTipoProducto(int tipoProducto) {
        this.tipoProducto = tipoProducto;
    }

    public List<String> getDepartamentos() {
        departamentos = ejbFacadeMunicipios.getDeptos();
        return departamentos;
    }

    public List<String> getMunicipios() {
        return municipios;
    }
    
    
    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    
    public List<DetallePedido> getDetalle() {
        return detalle;
    }

    public void setDetalle(List<DetallePedido> detalle) {
        this.detalle = detalle;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Pedido getSeleccion() {
        return seleccion;
    }

    public void setSeleccion(Pedido seleccion) {
        this.seleccion = seleccion;
    }

    
    public BigDecimal getPu() {
        return pu;
    }

    public void setPu(BigDecimal pu) {
        this.pu = pu;
    }
    

    public String[] getTallas() {
        return tallas;
    }

    public void setTallas(String[] tallas) {
        this.tallas = tallas;
    }

    public Integer[] getCantidades() {
        return cantidades;
    }

    public void setCantidades(Integer[] cantidades) {
        this.cantidades = cantidades;
    }

    public boolean isPorCaja() {
        return porCaja;
    }

    public void setPorCaja(boolean porCaja) {
        this.porCaja = porCaja;
    }

    public List<String> getColores() {
        return colores;
    }

    public void setColores(List<String> colores) {
        this.colores = colores;
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
        unidadesCaja = 0;
        for(int color = 0; color < numColores; color++){
            for(int talla=0; talla < numTallas; talla++){
                unidadesCaja = unidadesCaja + curva[color].getCantidad(talla);
            }
        }
        return unidadesCaja;
    }

    public void setUnidadesCaja(int unidadesCaja) {
        this.unidadesCaja = unidadesCaja;
    }

    public int getNumCajas() {
        return numCajas;
    }

    public void setNumCajas(int numCajas) {
        this.numCajas = numCajas;
    }

    
    
    
    //Actualiza selectoneMenu de Municipios
    public void actualizarColores(Producto estilo){
       colores = ejbFacadeCaja.coloresByEstilo(estilo);
       if(colores == null || colores.isEmpty()){
           colores = ejbFacadeInventario.coloresByEstilo(estilo);
       }
    }
    
    
    //FUNCIONES
    //Prepara todo para un nuevo Preingreso
    public void nuevoPedido(){
        cliente = null;
        detalle = new ArrayList<DetallePedido>();
    }
    
    // FUNCION QUE SE EJECUTA CADA VEZ QUE SE PASA DE UN PASO A OTRO EN EL WIZARD
    public String onFlowProcess(FlowEvent event) {
        resetearPedido();
        return event.getNewStep();    
    }  
    
    /*
     * FN PPL que guarda el Pedido con su detalle
     */
    public void guardarPEDIDO(){
        if(!detalle.isEmpty()){
            BigDecimal deudaActual = totalDeudaCliente(cliente);
            BigDecimal deudaTotal = deudaActual.add(pedido.getTotalPedido());
            if(pedido.getTipoPago().equals("AL CONTADO") || cliente.getLimite() == null || cliente.getLimite().compareTo(BigDecimal.ZERO) == 0 || deudaTotal.compareTo(cliente.getLimite()) <=0){
                pedido.setClientePedido(cliente);
                pedido.setTipoProducto(tipoProducto);
                pedido.setResponsablePedido(new JsfUtil().getEmpleado());
                pedido.setFechaIngreso(new funciones().getTime());
                if(pedido.getTipoPago().equals("AL CONTADO")){
                    pedido.setEstadoPedido("APROBADO");
                }else{
                    //ES AL CREDITO
                    pedido.setEstadoPedido("APROBADO");
                }
                try{
                    ejbFacadePedido.create(pedido);
                    for(int i=0; i < detalle.size(); i++){
                        detalle.get(i).setPedido(pedido);
                        detalle.get(i).setCorrelativo(ejbFacadeDetallePedido.getNextIdDetallePedido());
                        ejbFacadeDetallePedido.create(detalle.get(i));
                    }
                    resetearPedido();
                    new funciones().setMsj(1,"PEDIDO PUESTO EN COLA");
                    new funciones().irA("faces/vistas/pedidos/pedidos.xhtml");
                }catch(Exception e){
                    new funciones().setMsj(4,"Ha ocurrido un ERROR contacte con el administrador");
                }
            }else{
               new funciones().setMsj(2,"NO SE PUEDE LISTAR PEDIDO PORQUE EXCEDE LIMITE DE CREDITO DEL CLIENTE"); 
            }
        }else{
            new funciones().setMsj(2,"No a detallado el PEDIDO");
        }
    }
    
    
    
    public void ingresarCurva(){
        RequestContext context = RequestContext.getCurrentInstance();
        if(!existePedido()){
            if(pedido.getPedidoPK().getFechaPedido().after(new funciones().getTime())){
                context.addCallbackParam("mostrar",false);
                new funciones().setMsj(2, "LA FECHA DE PEDIDO ES INVALIDA");
            }else{
                if(pedido.getFechaEntrega().before(pedido.getPedidoPK().getFechaPedido()) ){
                    context.addCallbackParam("mostrar",false);
                    new funciones().setMsj(2, "LA FECHA DE ENTREGA DEBE SER MAYOR O IGUAL QUE LA FECHA DE PEDIDO");
                }else{
                    context.addCallbackParam("mostrar",true);
                    //Resetear curva
                    resetearCurva();
                }
            }
            
            
        }else{
            context.addCallbackParam("mostrar",false);
            new funciones().setMsj(3, "PEDIDO YA EXISTE EN EL SISTEMA");
        }
        
    }
    
    /*
     * FUNCION QUE GUARDA LA CURVA
     */
    public void listarCurvaDetalle(){
        RequestContext context = RequestContext.getCurrentInstance();
        if(validarCurva()){
            //Recorrer arreglo de tallas
            for(int c=0 ; c < numColores; c++){
                for(int t=0 ; t < numTallas; t++){
                    //Agregar a la lista detalle
                       DetallePedido nuevo = new DetallePedido();
                       nuevo.setProducto(producto);
                       nuevo.setColor(curva[c].getColor().toUpperCase());
                       nuevo.setTalla(tallas[t]);
                       nuevo.setCantidad(curva[c].getCantidad(t)*numCajas);
                       nuevo.setPu(new BigDecimal(new funciones().redondearMas(pu.floatValue(),2)));
                       //Calcular monto cantidad*PU
                       float monto = (float) (nuevo.getCantidad()*nuevo.getPu().doubleValue());
                       monto = new funciones().redondearMas(monto,2);
                       nuevo.setMonto(new BigDecimal(monto));
                       detalle.add(nuevo);
                       pedido.setTotalPedido(calcularTotalPedido());
                }
                       
                   } 
                
            
            new funciones().setMsj(1,"Detalle Pedido Actualizado");
            context.addCallbackParam("validar",true);
        }else{
           context.addCallbackParam("validar",false); 
        }
        
    }
    
    /*
     * Funcion que valida datos del detalle de la factura a ingresar a productos pre-ingresados
     */
    
    public boolean validarDetalle(){ 
        boolean valido = true;
        boolean cantValidas = true;
        int numTallas = 0; //numero de tallas ingresadas
        //Recorrer arreglo de tallas
        for(int i=0 ; i < tallas.length; i++){
            //Verificar que se haya ingresado una
            if((tallas[i] != null) && (!"".equals(tallas[i])) ){
                numTallas++;
                if(cantidades[i] <= 0){
                    cantValidas = false;
                }
            }
        }
        try{
            if(pu.floatValue() <= 0){
                valido = false;
                new funciones().setMsj(2,"Costo Unitario debe ser mayor a CERO");
            }
            if(numTallas <= 0){
                valido = false;
                new funciones().setMsj(2,"Debe ingresar al menos una Talla");
            }
            if(!cantValidas){
                valido = false;
                new funciones().setMsj(2,"Algunas de las cantidades ingresadas es CERO");
            }
            return valido;
        }catch(NullPointerException e){
            new funciones().setMsj(3,"HAY ALGUNOS VALORES NULOS");
            return false;
        }catch(Exception e){
            return false;
        }
    }
    
    
    
    public boolean validarCurva(){ 
        boolean valido = true;
        boolean validarTallas = true;
        boolean validarColores = true;
        //Recorrer arreglo de tallas
        for(int t=0 ; t < numTallas; t++){
            //Verificar que se haya ingresado una
            if((tallas[t] == null) || ("".equals(tallas[t])) ){
                validarTallas = false;
            }
        }
        //Recorrer arreglo de tallas
        for(int c=0 ; c < numColores; c++){
            //Verificar que se haya ingresado una
            if((tallas[c] == null) || ("".equals(tallas[c])) ){
                validarColores = false;
            }
        }
        if(numCajas <=0){
            valido = false;
            new funciones().setMsj(2,"Numero de Cajas invalido ebe ser mayor a CERO");
        }
        if(!validarTallas){
            valido = false;
            new funciones().setMsj(2,"Faltan tallas");
        }
        if(!validarColores){
            valido = false;
            new funciones().setMsj(2,"Faltan colores");
        }
        return valido;
    }
    
    
    /*
     * FUNCION QUE VERIFICA SI EL PEDIDO A INGRESAR YA EXISTE
     */
    public boolean existePedido(){
        try{
            if(ejbFacadePedido.existePedido(pedido.getPedidoPK())){
                return true;
            }else{
                return false;
            }
        }catch(Exception e){
            return false;
        }
        
    }
    //Actualiza selectoneMenu de Municipios
    public void actualizarMunicipios(String depto){
        municipios = ejbFacadeMunicipios.getMunicipios(depto);
    }
    
    public void guardarCliente(Cliente c , int naturaleza){
        RequestContext context = RequestContext.getCurrentInstance(); 
        if(!c.getDeptoCliente().equals("x") && !c.getMunicipioCliente().equals("x")){
//            if(naturaleza == 1 && "".equals(c.getDuiCliente()) && "".equals(c.getNitCliente()) ){
//                context.addCallbackParam("validar",false);
//                new funciones().setMsj(2, "Debe ingresar al menos un documento de identificacion DUI o NIT ");
//            }else{
//                
                //EMPLEADO ASIGNADO AL CLIENTE
                switch(naturaleza){
                    case 1: 
                        c.setNaturaleza("PERSONA NATURAL");
                        c.setNombreCliente(c.getNombreCliente().toUpperCase());
                        c.setApellidoCliente(c.getApellidoCliente().toUpperCase());
                        break;
                    case 2: 
                        c.setNaturaleza("PERSONA JURIDICA");
                        c.setNombreCliente(c.getNombreCliente().toUpperCase());
                        break;
                }
                //NUMCUENTA
                if(ejbFacadeCliente.existeCliente(c.getNombreCliente(), c.getApellidoCliente())){
                    new funciones().setMsj(3, "CLIENTE YA EXISTE EN A LA CARTERA");
                }else{
                    c.setNumcuenta(ejbFacadeCliente.getNextIdCliente());
                    try{
                        ejbFacadeCliente.create(c);
                        context.addCallbackParam("validar",true);
                        new funciones().setMsj(1, "CLIENTE GUARDADO EN CARTERA");
                        new funciones().setMsj(1, "CUENTA ASIGNADA: " +c.getNumcuenta());
                    }catch(Exception e){
                        context.addCallbackParam("validar",false);
                        new funciones().setMsj(3, "ERROR: " +e.getMessage());
                    }
                }
           // }
            
        }else{
            context.addCallbackParam("validar",false);
            new funciones().setMsj(2, "Ingrese el DEPARTAMENTO y MUNICIPIO del CLIENTE");
        }
        
    }
    /*
     * FUNCION QUE CALCULA EL TOTAL DEL PEDIDO
     * SUMANDO TODOS LOS DETALLE DEL PEDIDO
     */
    public BigDecimal calcularTotalPedido(){
        BigDecimal total;
        if(detalle == null || detalle.isEmpty()){
            total = BigDecimal.ZERO;
        }else{
            double suma = 0;
            for(int i=0; i < detalle.size(); i++){
                suma = suma + detalle.get(i).getMonto().doubleValue();
            }
            total = new BigDecimal(suma);
        }
        total = new BigDecimal(new funciones().redondearMas(total.floatValue(),2));
        return total;
    }
    /*
     * FUNCION QUE RESETEA EL PEDIDO
     */
    
    public void reset(){
        resetearPedido();
        new funciones().setMsj(1,"Campos Limpiados");
    }
    
    public void resetDetalle(){
        detalle.clear();
        new funciones().setMsj(2,"Detalle Vacio");
    }
    
    
    public void resetearPedido(){
        pedido = new Pedido(new com.ddicark.entidades.PedidoPK());
        //Resetear curva
        resetearCurva();
        for(int t=0; t < tallas.length; t++){
            tallas[t] = "";
            cantidades[t] = 0;
        }
        if(detalle != null){
            detalle.clear();
        }
    }
    
    public void resetearCurva(){
        producto = null;
        color = "";
        pu = BigDecimal.ZERO;
        for(int t=0; t < tallas.length; t++){
            tallas[t] = "";
            cantidades[t] = 0;
        }
    }
    
    
    /*
     * FUNCION QUE CREA LA CURVA DEL PEDIDO
     */
    public void prepararCurvaPedido(){
        numCajas = 1;
        RequestContext context = RequestContext.getCurrentInstance(); 
        if(producto != null){
            if(color != null){
                if(color.equals("NONE")){
                    unidadesCaja = 0;
                    numTallas = 6;
                    numColores = 1;
                    resetTallas();
                    resetCurva();
                    if(producto.getPrecioventaMayoreo().compareTo(BigDecimal.ZERO) > 0){
                        pu = new funciones().precioConIva(producto.getPrecioventaMayoreo());
                    }else{
                        pu = new funciones().precioConIva(precioVenta(producto));
                    }
                }else{
                    Caja caja = ejbFacadeCaja.cajaEstiloColor(producto, color);
                    if(caja != null){
                        consultarCurva(caja);
                    }
                }
                
                context.execute("curvaDialog.show();");
            }
        }
    }
    
    
    
    //Calcula los datos y curva de la caja del pedido
    public void consultarCurva(Caja caja){
        //unidadesCaja = caja.getUnidadesCaja();
        pu = new funciones().precioConIva(caja.getPrecioventaUnidad());
        numTallas = 0;
        resetTallas();
        numColores = 0;
        resetCurva();
        List<String> tallasList = ejbFacadeInventario.tallasCaja(caja);
        List<Object> curvasList = ejbFacadeInventario.curvaByCaja(caja);
        if((tallasList != null) && (curvasList != null)){
            int menorLista = 0;
            if(tallas.length <= tallasList.size()){
                numTallas = tallas.length;
            }else{
                numTallas = tallasList.size();
            }
            //Llenar Lista de Tallas a mostrar en curva
            for(int t=0; t < numTallas; t++){
                tallas[t] = tallasList.get(t);
            }
            int c = -1; //posicion del arreglo de curva
            String colorActual = ""; //Color Actual
            //Recorremos el arreglo de la consulta
            for(Object consulta: curvasList){ // r: posicion en la consulta
               int t; //Posicion de la talla en el arreglo tallas
               Object[] actual = (Object[]) consulta;
               if(!(actual[0].equals(colorActual))){
                    //No es igual (cambio de color)
                    c++; //posicion del arreglo de curva
                    colorActual = (String) actual[0]; //Nuevo Color
                    numColores++;
                    curva[c].setColor(colorActual);
                }
                //Es igual al color actual
                for(t=0; t < tallas.length; t++){
                   if(actual[1].equals(tallas[t])){
                       break; //Encontrado
                   }
                }
                switch(t){
                    case 0: 
                        curva[c].setT1(Integer.parseInt(actual[2].toString()));break;
                    case 1: 
                        curva[c].setT2(Integer.parseInt(actual[2].toString())); break;
                    case 2: 
                        curva[c].setT3(Integer.parseInt(actual[2].toString())); break;
                    case 3: 
                        curva[c].setT4(Integer.parseInt(actual[2].toString())); break;
                    case 4: 
                        curva[c].setT5(Integer.parseInt(actual[2].toString())); break;
                    case 5: 
                        curva[c].setT6(Integer.parseInt(actual[2].toString())); break;
                    case 6: 
                        curva[c].setT7(Integer.parseInt(actual[2].toString())); break;
                    case 7: 
                        curva[c].setT8(Integer.parseInt(actual[2].toString())); break;
                    case 8: 
                        curva[c].setT9(Integer.parseInt(actual[2].toString())); break;
                    case 9: 
                        curva[c].setT10(Integer.parseInt(actual[2].toString()));break;
                }
            }
        }
    }    
   
    public void resetTallas(){
        for(int i=0; i < tallas.length; i++){
            tallas[i] = "";
        }
    }
    
    public void resetCurva(){
        for(curvaColor actual: curva){
            actual.resetear();
        }
    }
    
    
    public BigDecimal precioVenta(Producto estilo){
        BigDecimal precio = ejbFacadeInventario.precioVentaByEstilo(estilo);
        return precio;
    }
    
    
    
    public int numCreditosActivosCliente(Cliente cliente){
        int creditos = 0;
        if(cliente != null){
            creditos = ejbFacadeFactura.getCreditosActivos(cliente);
        }
        return creditos;
    }
    
    
    public float deudaActualCliente(Cliente cliente){
        float deuda = (float) 0.00;
        if(cliente != null){
            deuda = ejbFacadeFactura.getDeudaPorCreditos(cliente);
        }
        return deuda;
    }
    
    
    
    
    public void prepararConsultaPedidos(){
        Calendar hoy = Calendar.getInstance();
        year = hoy.get(Calendar.YEAR);
        month = hoy.get(Calendar.MONTH) +1;
        actualizarConsultaPedidos();
    }
    
    
    public void actualizarConsultaPedidos(){
        pedidosRegistrados = ejbFacadePedido.consultarPedidos(month, year);
        filtroPedidos = null;
    }
    
    
    
    
    
    public BigDecimal totalDeudaCliente(Cliente cliente){
        BigDecimal deuda = BigDecimal.ZERO;
        if(cliente != null){
            deuda = new BigDecimal(ejbFacadeFactura.getDeudaPorCreditos(cliente));
        }
        return deuda;
    }
    
    
    
    public void prepareEliminarPedido(){
        if(seleccion != null){
            if(seleccion.getEnvio() == null || seleccion.getEnvio().size() == 0){
                if(seleccion.getEstadoPedido().equals("APROBADO")){
                    RequestContext context = RequestContext.getCurrentInstance(); 
                    context.execute("eliminarPedido.show();");
                }else{
                    new funciones().setMsj(3, "PEDIDO YA ESTA PROCESADO");
                }
            }else{
                new funciones().setMsj(3, "PEDIDO YA TIENE NOTA DE ENVIO");
            }
        }else{
            new funciones().setMsj(3, "ERROR AL ELIMINAR PEDIDO");
        }
    }
    
    
    public void eliminarPedido(){
        if(seleccion != null){
            List<DetallePedido> dp = ejbFacadeDetallePedido.getDetallePedido(seleccion.getPedidoPK());
            boolean salir=false;
            while(!dp.isEmpty()){
                ejbFacadeDetallePedido.remove(dp.remove(0));
            }
            ejbFacadePedido.remove(seleccion);
            seleccion = null;
            new funciones().setMsj(1, "PEDIDO ELIMINADO");
        }else{
            new funciones().setMsj(3, "ERROR AL ELIMINAR PEDIDO");
        }
    }
    
    
    
}
