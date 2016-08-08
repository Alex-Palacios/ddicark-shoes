package com.ddicark.controladores;

import com.ddicark.auxiliares.JsfUtil;
import com.ddicark.entidades.Empleado;
import com.ddicark.auxiliares.funciones;
import com.ddicark.entidades.Envio;
import com.ddicark.entidades.Permisos;
import com.ddicark.jpaFacades.EmpleadoFacade;
import com.ddicark.jpaFacades.PermisosFacade;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;

/* Beans Controlador encargado de la logica del negocio y se comunica
 * con las vistas
 */
@ManagedBean(name = "empleadoController")
@SessionScoped
public class EmpleadoController extends AbstractController<Empleado> implements Serializable {

    private Empleado usuario; // Guarda el empleado actual en Sesion
    private List<Permisos> permisos;
    private Permisos permiso;
    private List<Empleado> vendedores;
    
    
    @EJB
    private EmpleadoFacade ejbFacadeEmpleado; // Encargado de comunicarse con la BD con consultas propias a tabla empleado
    
    @EJB
    private PermisosFacade ejbFacadePermisos;
    
    //Variables de control de SESION
    private boolean isLogin = false; //Indica si se ha iniciado sesion o no
    private String username ="";
    private String password ="";
    
    private String nuevoUsername;
    private String nuevoPassword;
    
    //CONSTRUCTOR DE LA CLASE
    
     public EmpleadoController() {
        super(Empleado.class);
    }
    //Inicializa controlador de persistencia de proveedores
    @PostConstruct
    public void init() {
        super.setFacade(ejbFacadeEmpleado);
    }

    
    //Variables de control
    private Empleado cuenta; // Cuenta seleccionada de tabla usuarios
    private String puestoActual;
    private List<Empleado> usuarios;
    
    
    //METODOS GET para obtener las variables
    //METODOS SET para setear o cambiar el valor de las variable
    public List<Empleado> getVendedores() {
        vendedores = ejbFacadeEmpleado.listaVendedores();
        return vendedores;
    }

    public List<Empleado> getUsuarios() {
        usuarios = ejbFacadeEmpleado.listaUsuarios();
        return usuarios;
    }

    
    public Empleado getUsuario() {
        return usuario;
    }

    public void setUsuario(Empleado usuario) {
        this.usuario = usuario;
    }
    
        
    public boolean isIsLogin() {
        return isLogin;
    }

    public void setIsLogin(boolean isLogin) {
        this.isLogin = isLogin;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Empleado getCuenta() {
        return cuenta;
    }

    public void setCuenta(Empleado cuenta) {
        this.cuenta = cuenta;
    }

    public String getNuevoUsername() {
        return nuevoUsername;
    }

    public void setNuevoUsername(String nuevoUsername) {
        this.nuevoUsername = nuevoUsername;
    }

    public String getNuevoPassword() {
        return nuevoPassword;
    }

    public void setNuevoPassword(String nuevoPassword) {
        this.nuevoPassword = nuevoPassword;
    }

    public Permisos getPermiso() {
        return permiso;
    }

    public void setPermiso(Permisos permiso) {
        this.permiso = permiso;
    }
    
    
    
    
    
    // FUNCIONES DE LOGICA DEL NEGOCIO
    
    //FUNCION INICIAR SESION
    public void login(){
        List<Empleado> userX = ejbFacadeEmpleado.existeUser(username, password);
        if(!userX.isEmpty()){
            //SI LAS CREDENCIALES SON VALIDAS
            usuario = userX.get(0); // Carga los datos del Usuario(Empleado) actual
            if(usuario.getActivo()){
                isLogin = true;
                password = "";
                permisos = ejbFacadePermisos.getPermisosEmpleado(usuario);
                new funciones().irA("faces/vistas/home.xhtml");
            }else{
                //SI EL USUARIO ESTA INACTIVO
                password = "";
                isLogin = false;
                new funciones().setMsj(3, "CUENTA INACTIVA");
                new funciones().setMsj(3, "COMUNIQUESE CON EL ADMINISTRADOR DEL SISTEMA");
            }
            
        }else{
            //SI LAS CREDENCIALES NO SON VALIDAS
            password = "";
            isLogin = false;
            new funciones().setMsj(4, "Usuario o Password INCORRECTOS ");
            
        }
    }
    
    
     //FUNCION DE CERRAR SESION
    public void logout(){
        username = "";
        password = "";
        permisos = null;
        JsfUtil.cerrarSessionUsuario();
        isLogin = false;
        new funciones().irA("faces/index.xhtml");
    }
    

    
    /*
     * FUNCIONES PARA ADMINISTRACION DE CUENTAS
     */
    
    //funcion que activa o desactiva usuario
    public void activarDesactivarUsuario(){
        if(cuenta != null){
            String estado;
            if(cuenta.getActivo()){
                cuenta.setActivo(false);
                estado = "DESACTIVO";
            }else{
                cuenta.setActivo(true);
                estado = "ACTIVO";
            }
            ejbFacadeEmpleado.edit(cuenta);
            new funciones().setMsj(1,"SE " + estado +" CUENTA DE "+cuenta.getUsername());
        }else{
            new funciones().setMsj(3,"NO SE PUDO ACTIVAR/DESACTIVAR CUENTA");
        }
    }
    
    
    public void prepararEditar(){
        puestoActual = cuenta.getPuestoEmpleado();
    }
    
    //Guarda datos modificados del empleado
    public void editarDatosEmpleado(){
        RequestContext context = RequestContext.getCurrentInstance();
        if(cuenta != null){
            ejbFacadeEmpleado.edit(cuenta);
            new funciones().setMsj(1,"DATOS DEL EMPLEADO ACTUALIZADOS");
             context.addCallbackParam("validar", true);
            if(!cuenta.getPuestoEmpleado().equals(puestoActual)){
                new funciones().setMsj(2, "CAMBIO DE PUESTO DETECTADO");
                asignarPermisosPorDefecto(cuenta);
                new funciones().setMsj(1, "ACTUALIZADOS SEGUN NUEVO PUESTO");
            }
        }else{
            new funciones().setMsj(3, "ERROR VALOR NULL");
            context.addCallbackParam("validar", false);
        }
    }
    
    
    /*
     * FUNCION QUE CREA UN USUARIO
     */
    public void prepararCrear(){
        cuenta = new Empleado();
    }
    
    public void crearUsuario(){
        RequestContext context = RequestContext.getCurrentInstance();
        if(cuenta != null){
            cuenta.setCodempleado(cuenta.getCodempleado().toUpperCase());
            cuenta.setNombreEmpleado(cuenta.getNombreEmpleado().toUpperCase());
            cuenta.setApellidoEmpleado(cuenta.getApellidoEmpleado().toUpperCase());
            if(!ejbFacadeEmpleado.existeCodigo(cuenta.getCodempleado())){
                cuenta.setActivo(true);
                cuenta.setUsername(cuenta.getCodempleado());
                try {
                    cuenta.setPassword(new JsfUtil().getMD5(cuenta.getCodempleado().toLowerCase()));
                } catch (NoSuchAlgorithmException ex) {
                    Logger.getLogger(EmpleadoController.class.getName()).log(Level.SEVERE, null, ex);
                    new funciones().setMsj(3, "OCURRIO UN ERROR INESPERADO AL ENCRIPTAR CONTRASEÑA .. FAVOR COMUNIQUESE CON EL ADMINISTRADOR DEL SISTEMA");
                    new funciones().setMsj(3, "DETALLES DEL ERROR: " + ex.getMessage());
                } catch (UnsupportedEncodingException ex) {
                    Logger.getLogger(EmpleadoController.class.getName()).log(Level.SEVERE, null, ex);
                    new funciones().setMsj(3, "OCURRIO UN ERROR INESPERADO AL ENCRIPTAR CONTRASEÑA .. FAVOR COMUNIQUESE CON EL ADMINISTRADOR DEL SISTEMA");
                    new funciones().setMsj(3, "DETALLES DEL ERROR: " + ex.getMessage());
                }
                ejbFacadeEmpleado.create(cuenta);
                new funciones().setMsj(1, "USUARIO CREADO EXITOSAMENTE");
                asignarPermisos(cuenta);
                context.addCallbackParam("validar", true);
            }else{
                new funciones().setMsj(3, "CODIGO DEL EMPLEADO YA EXISTE EN EL SISTEMA");
                context.addCallbackParam("validar", false);
            }
        }
    }
    
    //Configura Permisos de usuarios segun puesto de trabajo
    public void asignarPermisos(Empleado usuario){
        if(usuario != null){
            for(int p=0; p < 30 ;p++){
                Permisos actual = crearPermiso(p);
                actual.setEmpleado(usuario);
                actual.setIdpermiso(ejbFacadePermisos.getNextIdPermiso());
                ejbFacadePermisos.create(actual);
                usuario.getPermisosCollection().add(actual);
            }
            asignarPermisosPorDefecto(usuario);
        }
        
    }
    
    
    
    
    
    //FUNCION que resetea el password de un Usuario 
    public void recuperarPassword(){
        if(cuenta != null){
            try {
                cuenta.setPassword(new JsfUtil().getMD5("ddicark"));
                ejbFacadeEmpleado.edit(cuenta);
                new funciones().setMsj(1, "Password de " +cuenta.getUsername() + " RECUPERADO");
                new funciones().setMsj(1, "NUEVO PASSWORD: ddicark" );
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(EmpleadoController.class.getName()).log(Level.SEVERE, null, ex);
                new funciones().setMsj(3, "ERROR INESPERADO AL RECUPERAR PASSWORD .... COMUNIQUESE CON EL ADMINISTRADOR DEL SISTEMA");
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(EmpleadoController.class.getName()).log(Level.SEVERE, null, ex);
                new funciones().setMsj(3, "ERROR INESPERADO AL RECUPERAR PASSWORD .... COMUNIQUESE CON EL ADMINISTRADOR DEL SISTEMA");
            }
        }
    }
    
    
    
    //Preparar editar cuenta
    public void prepararEditarCuenta(){
        cuenta = new Empleado();
        cuenta.setCodempleado(usuario.getCodempleado());
        cuenta.setNombreEmpleado(usuario.getNombreEmpleado());
        cuenta.setApellidoEmpleado(usuario.getApellidoEmpleado());
        cuenta.setDuiEmpleado(usuario.getDuiEmpleado());
        cuenta.setNitEmpleado(usuario.getNitEmpleado());
        cuenta.setTelEmpleado(usuario.getTelEmpleado());
        cuenta.setFechanacEmpleado(usuario.getFechanacEmpleado());
        cuenta.setPuestoEmpleado(usuario.getPuestoEmpleado());
        cuenta.setUsername(usuario.getUsername());
        cuenta.setPassword(usuario.getPassword());
        cuenta.setActivo(usuario.getActivo());
    }
    
    
    //GUARDAR CAMBIOS A CUENTA
    public void actualizarDatosCuenta(){
        try{
            if(cuenta != null && usuario != null){
                ejbFacadeEmpleado.edit(cuenta);
                usuario.setDuiEmpleado(cuenta.getDuiEmpleado());
                usuario.setNitEmpleado(cuenta.getNitEmpleado());
                usuario.setFechanacEmpleado(cuenta.getFechanacEmpleado());
                new funciones().setMsj(1,"DATOS ACTUALIZADOS CORRECTAMENTE");
                cuenta = new Empleado();
            }
        }catch(Exception ex){
            new funciones().setMsj(3,"ERROR INESPERADO AL GUARDAR CAMBIOS DE CUENTA");
            new funciones().setMsj(3,"DETALLES DEL ERROR: "+ ex.getMessage());
        }
        
    }
    
    //Preparar cambio de username
    public void prepararCambioUsername(){
        nuevoUsername = null;
    }
    
    //prepara el cambio a username
    public void mostrarConfirmarUsername(){
        RequestContext context = RequestContext.getCurrentInstance();
        if(nuevoUsername != null && !"".equals(nuevoUsername)){
            if(!ejbFacadeEmpleado.existeUsername(nuevoUsername)){
                new funciones().setMsj(1, "NOMBRE DE USUARIO DISPONIBLE");
                context.addCallbackParam("validar", true);
            }else{
                context.addCallbackParam("validar", false);
                new funciones().setMsj(3, "NOMBRE DE USUARIO NO DISPONIBLE");
                new funciones().setMsj(2, "INGRESE OTRO USERNAME");
            }
        }else{
            context.addCallbackParam("validar", false);
        }
    }
    
    
    
    public void cambiarUsername(){
        try{
            if(nuevoUsername != null && !"".equals(nuevoUsername)){
                usuario.setUsername(nuevoUsername);
                ejbFacadeEmpleado.edit(usuario);
                new funciones().setMsj(1, "SE CAMBIO SU USERNAME CORRECTAMENTE");
                new funciones().setMsj(1, "NUEVO USERNAME: " + usuario.getUsername());
                new funciones().setMsj(2, "USAR NUEVO USUARIO LA PROXIMA VEZ QUE INICIE SESION");
            }
        }catch(Exception ex){
            new funciones().setMsj(3, "ERROR INESPERADO");
            new funciones().setMsj(3, "DETALLES: " + ex.getMessage());
        }
        
    }
    
    
    
    
    //Preparar cambio de username
    public void prepararCambioPassword(){
        password = null;
        nuevoPassword = null;
    }
    
    //prepara el cambio a username
    public void mostrarConfirmarPassword(){
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            password = new JsfUtil().getMD5(password);
            if(usuario.getPassword().equals(password)){
                context.addCallbackParam("validar", true);
            }else{
                context.addCallbackParam("validar", false);
                new funciones().setMsj(3, "PASSWORD ACTUAL INCORRECTO");
                prepararCambioPassword();
            }
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(EmpleadoController.class.getName()).log(Level.SEVERE, null, ex);
            new funciones().setMsj(3, "ERROR INESPERADO");
            new funciones().setMsj(3, "DETALLES: " + ex.getMessage());
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(EmpleadoController.class.getName()).log(Level.SEVERE, null, ex);
            new funciones().setMsj(3, "ERROR INESPERADO");
            new funciones().setMsj(3, "DETALLES: " + ex.getMessage());
        }
        
    }
    
    
    
    public void cambiarPassword(){
        try{
            if(nuevoPassword != null && !"".equals(nuevoPassword)){
                nuevoPassword = new JsfUtil().getMD5(nuevoPassword);
                usuario.setPassword(nuevoPassword);
                ejbFacadeEmpleado.edit(usuario);
                new funciones().setMsj(1, "SE CAMBIO SU CONTRASEÑA DE ACCESO CORRECTAMENTE");
                new funciones().setMsj(2, "USAR NUEVA CLAVE LA PROXIMA VEZ QUE INICIE SESION");
                prepararCambioPassword();
            }
        }catch(Exception ex){
            new funciones().setMsj(3, "ERROR INESPERADO");
            new funciones().setMsj(3, "DETALLES: " + ex.getMessage());
        }
    }
    
    
    
    /*
     * FUNCION QUE DEVUELVE EL PERMISO DE UN SUBMENU
     */
    public boolean verificarPermiso(String menu, String submenu){
        boolean permiso = false;
        if(permisos != null){
            for(Permisos actual:permisos){
                if(actual.getMenu().equals(menu)){
                    if(actual.getSubmenu().equals(submenu)){
                        permiso = actual.getPermiso();
                        break;
                    }
                }
            }
        }
        return permiso;
    }
    
    
    
    
    //funcion que cambia un permiso de un usuario
    public void cambiarPermiso(){
        if(permiso != null){
            if(permiso.getPermiso()){
                permiso.setPermiso(false);
            }else{
                permiso.setPermiso(true);
            }
            ejbFacadePermisos.edit(permiso);
            new funciones().setMsj(1,"EL PERMISO DE " + permiso.getEmpleado().getUsername() +" SE CAMBIO CORRECTAMENTE");
        }else{
            new funciones().setMsj(3,"NO SE PUDO CAMBIAR EL PERMISO DEL USUARIO");
        }
    }
    
    
    
    
    
    
    /*
     * FUNCION QUE CREA UN PERMISO ESPECIFICO PARA EL NUEVO USUARIO
     */
    public Permisos crearPermiso(int numPermiso){
        //PERSISTE EN LA BD PERO NIEGA TODOS LOS PERMISOS
        Permisos permiso = new Permisos();
        switch(numPermiso){
            case 0: 
                permiso.setMenu("VER");
                permiso.setSubmenu("PROVEEDORES");
                permiso.setDescripcion("VER LISTA DE PROVEEDORES");
                permiso.setPermiso(false);
                break;
            case 1: 
                permiso.setMenu("VER");
                permiso.setSubmenu("CARTERA CLIENTES");
                permiso.setDescripcion("VER LISTA DE CLIENTES");
                permiso.setPermiso(false);
                break;
            case 2: 
                permiso.setMenu("VER");
                permiso.setSubmenu("CATALOGO");
                permiso.setDescripcion("VER LISTA DE ESTILOS");
                permiso.setPermiso(false);
                break;
            case 3: 
                permiso.setMenu("COMPRAS");
                permiso.setSubmenu("REGISTRAR IMPORTACION");
                permiso.setDescripcion("REGISTRAR NUEVA IMPORTACION Y RETACEO");
                permiso.setPermiso(false);
                break;
            case 4: 
                permiso.setMenu("COMPRAS");
                permiso.setSubmenu("REGISTRAR COMPRA");
                permiso.setDescripcion("REGISTRAR NUEVA COMPRA NACIONAL");
                permiso.setPermiso(false);
                break;
            case 5: 
                permiso.setMenu("COMPRAS");
                permiso.setSubmenu("PAGOS A PROVEEDORES");
                permiso.setDescripcion("REGISTRAR PAGOS REALIZADOS A LOS PROVEEDORES");
                permiso.setPermiso(false);
                break;
            case 6: 
                permiso.setMenu("COMPRAS");
                permiso.setSubmenu("COMPRAS");
                permiso.setDescripcion("VER LISTA DE COMPRAS REALIZADAS");
                permiso.setPermiso(false);
                break;
            case 7: 
                permiso.setMenu("INGRESOS");
                permiso.setSubmenu("APROBAR INGRESOS");
                permiso.setDescripcion("APROBAR RETACEOS E INGRESOS DE FACTURAS");
                permiso.setPermiso(false);
                break;
            case 8: 
                permiso.setMenu("INGRESOS");
                permiso.setSubmenu("PREINGRESO DE FACTURA");
                permiso.setDescripcion("INGRESAR DETALLE DE FACTURAS APROBADAS");
                permiso.setPermiso(false);
                break;
            case 9: 
                permiso.setMenu("INGRESOS");
                permiso.setSubmenu("CODIFICACION DE PRODUCTO");
                permiso.setDescripcion("CODIFICAR PRODUCTO E INGRESAR AL INVENTARIO");
                permiso.setPermiso(false);
                break;
            case 10: 
                permiso.setMenu("BODEGA");
                permiso.setSubmenu("MUESTRAS");
                permiso.setDescripcion("ASIGNAR MUESTRAS Y VER LISTADO DE MUESTRAS");
                permiso.setPermiso(false);
                break;
            case 11: 
                permiso.setMenu("BODEGA");
                permiso.setSubmenu("PROCESAR PEDIDOS");
                permiso.setDescripcion("DESPACHAR PEDIDOS APROBADOS");
                permiso.setPermiso(false);
                break;
            case 12: 
                permiso.setMenu("BODEGA");
                permiso.setSubmenu("ENVIOS");
                permiso.setDescripcion("VER LISTA DE ORDENES DE ENVIO");
                permiso.setPermiso(false);
                break;
            case 13: 
                permiso.setMenu("BODEGA");
                permiso.setSubmenu("REINGRESO DE PRODUCTOS");
                permiso.setDescripcion("REINGRESAR AL INVENTARIO PRODUCTOS DEVUELTOS POR ANULACION DE FACTURAS O DEVOLUCIONES");
                permiso.setPermiso(false);
                break;
            case 14: 
                permiso.setMenu("INVENTARIO");
                permiso.setSubmenu("FISICO");
                permiso.setDescripcion("VER LISTADO DE TODOS LOS PRODUCTOS DEL INVENTARIO Y MODIFICAR INFORMACION DE PRODUCTO");
                permiso.setPermiso(false);
                break;
            case 15: 
                permiso.setMenu("INVENTARIO");
                permiso.setSubmenu("EXISTENCIAS");
                permiso.setDescripcion("VER LISTADO DE PRODUCTOS EN EXISTENCIAS");
                permiso.setPermiso(false);
                break;
            case 16: 
                permiso.setMenu("INVENTARIO");
                permiso.setSubmenu("CORTE DE INVENTARIO");
                permiso.setDescripcion("INGRESAR PRODUCTO DEL CORTE DE INVENTARIO INICIAL");
                permiso.setPermiso(false);
                break;
            case 17: 
                permiso.setMenu("CONTROL GENERAL");
                permiso.setSubmenu("CONTROL GENERAL");
                permiso.setDescripcion("ACCESO A TODAS LAS VARIABLES Y CONFIGURACIONES GLOBALES, CONTROL DE PERMISOS Y CUENTAS DE USUARIOS");
                permiso.setPermiso(false);
                break;
            case 18: 
                permiso.setMenu("PEDIDOS");
                permiso.setSubmenu("REGISTRAR PEDIDOS");
                permiso.setDescripcion("REGISTRAR NUEVOS PEDIDOS DE CLIENTES EN EL SISTEMA");
                permiso.setPermiso(false);
                break;
            case 19: 
                permiso.setMenu("PEDIDOS");
                permiso.setSubmenu("PEDIDOS");
                permiso.setDescripcion("VER LISTA DE PEDIDOS REGISTRADOS");
                permiso.setPermiso(false);
                break;
            case 20: 
                permiso.setMenu("CREDITOS");
                permiso.setSubmenu("APROBAR CREDITOS");
                permiso.setDescripcion("APROBAR CREDITOS A CLIENTES");
                permiso.setPermiso(false);
                break;
            case 21: 
                permiso.setMenu("CREDITOS");
                permiso.setSubmenu("HISTORIAL DE CLIENTE");
                permiso.setDescripcion("CONSULTAR EL HISTORIAL DE CLIENTES");
                permiso.setPermiso(false);
                break;
            case 22: 
                permiso.setMenu("CREDITOS");
                permiso.setSubmenu("CREDITOS");
                permiso.setDescripcion("VER LISTA DE CREDITOS REGISTRADOS");
                permiso.setPermiso(false);
                break;
            case 23: 
                permiso.setMenu("VENTAS");
                permiso.setSubmenu("FACTURAR");
                permiso.setDescripcion("FACTURAR PRODUCTO A VENDER Y VER FACTURAS EMITIDAS");
                permiso.setPermiso(false);
                break;
            case 24: 
                permiso.setMenu("VENTAS");
                permiso.setSubmenu("REGISTRAR PAGOS");
                permiso.setDescripcion("REGISTRAR PAGOS DE CLIENTES");
                permiso.setPermiso(false);
                break;
            case 25: 
                permiso.setMenu("VENTAS");
                permiso.setSubmenu("VENTAS");
                permiso.setDescripcion("VER LISTA DE VENTAS REALIZADAS");
                permiso.setPermiso(false);
                break;
            case 26: 
                permiso.setMenu("CAD");
                permiso.setSubmenu("REGISTRAR CAMBIO DE PRODUCTO");
                permiso.setDescripcion("REGISTRAR CAMBIOS DE PRODUCTOS REALIZADOS A FACTURA");
                permiso.setPermiso(false);
                break;
            case 27: 
                permiso.setMenu("CAD");
                permiso.setSubmenu("REGISTRAR DEVOLUCIONES");
                permiso.setDescripcion("REGISTRAR Y APLICAR DEVOLUCIONES");
                permiso.setPermiso(false);
                break;
            case 28: 
                permiso.setMenu("CAD");
                permiso.setSubmenu("ANULAR FACTURAS");
                permiso.setDescripcion("ANULAR FACTURAS COMPLETAS Y REFACTURAR PRODUCTO");
                permiso.setPermiso(false);
                break;
            case 29: 
                permiso.setMenu("REPORTES");
                permiso.setSubmenu("REPORTES");
                permiso.setDescripcion("GENERAR REPORTES ESPECIALES");
                permiso.setPermiso(false);
                break;
            
            
                
        }
        return permiso;
    }
    
    
    
    /*
     * FUNCION QUE RESETEA LOS PERMISOS A PERMISOS POR DEFECTO SEGUN EL CARGO
     */
    
    public void asignarPermisosPorDefecto(Empleado empleado){
        List<Permisos> permisos = ejbFacadePermisos.getPermisosEmpleado(empleado);
        if(empleado != null && permisos != null && permisos.size()==30){
            boolean[] defecto = new boolean[30];
            for(int i=0; i < defecto.length; i++){
                defecto[i] = false;
            }
            
            //Asignar segun cargo
            if(empleado.getPuestoEmpleado().equals("GERENTE")){
                //ASIGNAR TODOS LOS PERMISOS
                for(int p=0 ; p < permisos.size();p++){
                    defecto[p] = true;
                    if(!permisos.get(p).equals(defecto[p])){
                        permisos.get(p).setPermiso(defecto[p]);
                        ejbFacadePermisos.edit(permisos.get(p));
                    }
                }
                new funciones().setMsj(1, "PERMISOS ASIGNADOS SEGUN PUESTO");
                
            }else if(empleado.getPuestoEmpleado().equals("CONTADOR")){
                //ASIGNAR PERMISOS DE CONTADOR
                for(int p=0 ; p < permisos.size();p++){
                    switch(p){
                        case 0: defecto[p] = true; break;
                        case 1: defecto[p] = true; break;
                        case 2: defecto[p] = true; break;
                        case 3: defecto[p] = true; break;
                        case 4: defecto[p] = true; break;
                        case 5: defecto[p] = true; break;
                        case 6: defecto[p] = true; break;
                        case 12: defecto[p] = true; break;
                        case 15: defecto[p] = true; break;
                        case 19: defecto[p] = true; break;
                        case 20: defecto[p] = true; break;
                        case 21: defecto[p] = true; break;
                        case 22: defecto[p] = true; break;
                        case 23: defecto[p] = true; break;
                        case 24: defecto[p] = true; break;
                        case 25: defecto[p] = true; break;
                        case 26: defecto[p] = true; break;
                        case 27: defecto[p] = true; break;
                        case 28: defecto[p] = true; break;
                        case 29: defecto[p] = true; break;
                            
                    }
                    if(!permisos.get(p).equals(defecto[p])){
                        permisos.get(p).setPermiso(defecto[p]);
                        ejbFacadePermisos.edit(permisos.get(p));
                    }
                }
                new funciones().setMsj(1, "PERMISOS ASIGNADOS SEGUN PUESTO");
                
                
                
            }else if(empleado.getPuestoEmpleado().equals("BODEGA")){
                //ASIGNAR PERMISOS DE CONTADOR
                for(int p=0 ; p < permisos.size();p++){
                    switch(p){
                        case 0: defecto[p] = true; break;
                        case 1: defecto[p] = true; break;
                        case 2: defecto[p] = true; break;
                        case 8: defecto[p] = true; break;
                        case 9: defecto[p] = true; break;
                        case 10: defecto[p] = true; break;
                        case 11: defecto[p] = true; break;
                        case 12: defecto[p] = true; break;
                        case 13: defecto[p] = true; break;
                        case 14: defecto[p] = true; break;
                        case 15: defecto[p] = true; break;
                        case 16: defecto[p] = true; break;
                        case 18: defecto[p] = true; break;
                        case 19: defecto[p] = true; break;
                        case 26: defecto[p] = true; break;
                    }
                    if(!permisos.get(p).equals(defecto[p])){
                        permisos.get(p).setPermiso(defecto[p]);
                        ejbFacadePermisos.edit(permisos.get(p));
                    }
                }
                new funciones().setMsj(1, "PERMISOS ASIGNADOS SEGUN PUESTO");
                
                
            }else if(empleado.getPuestoEmpleado().equals("VENDEDOR")){
                //ASIGNAR PERMISOS DE CONTADOR
                for(int p=0 ; p < permisos.size();p++){
                    switch(p){
                        case 1: defecto[p] = true; break;
                        case 2: defecto[p] = true; break;
                        case 15: defecto[p] = true; break;
                        case 18: defecto[p] = true; break;
                        case 19: defecto[p] = true; break;
                    }
                    if(!permisos.get(p).equals(defecto[p])){
                        permisos.get(p).setPermiso(defecto[p]);
                        ejbFacadePermisos.edit(permisos.get(p));
                    }
                }
                new funciones().setMsj(1, "PERMISOS ASIGNADOS SEGUN PUESTO");
            }
        }else{
            new funciones().setMsj(3, "ERROR INESPERADO AL ASIGNAR PERMISOS POR DEFECTO");
        }
    }
    
    
    
}
