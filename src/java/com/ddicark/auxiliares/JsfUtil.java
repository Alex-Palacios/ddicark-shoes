package com.ddicark.auxiliares;

import com.ddicark.controladores.CajaController;
import com.ddicark.controladores.EmpleadoController;
import com.ddicark.controladores.EnvioController;
import com.ddicark.controladores.FacturaController;
import com.ddicark.controladores.IngresoController;
import com.ddicark.controladores.InventarioController;
import com.ddicark.controladores.PreingresoController;
import com.ddicark.controladores.VentaController;
import com.ddicark.entidades.Empleado;
import com.ddicark.entidades.Envio;
import com.ddicark.entidades.Ingreso;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItem;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import org.primefaces.context.RequestContext;


public class JsfUtil {

    public static SelectItem[] getSelectItems(List<?> entities, boolean selectOne) {
        int size = selectOne ? entities.size() + 1 : entities.size();
        SelectItem[] items = new SelectItem[size];
        int i = 0;
        if (selectOne) {
            items[0] = new SelectItem("", "---");
            i++;
        }
        for (Object x : entities) {
            items[i++] = new SelectItem(x, x.toString());
        }
        return items;
    }

    public static void addErrorMessage(Exception ex, String defaultMsg) {
        String msg = ex.getLocalizedMessage();
        if (msg != null && msg.length() > 0) {
            addErrorMessage(msg);
        } else {
            addErrorMessage(defaultMsg);
        }
    }

    public static void addErrorMessages(List<String> messages) {
        for (String message : messages) {
            addErrorMessage(message);
        }
    }

    public static void addErrorMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
    }

    public static void addSuccessMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);
        FacesContext.getCurrentInstance().addMessage("successInfo", facesMsg);
    }

    public static String getRequestParameter(String key) {
        return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(key);
    }

    public static Object getObjectFromRequestParameter(String requestParameterName, Converter converter, UIComponent component) {
        String theId = JsfUtil.getRequestParameter(requestParameterName);
        return converter.getAsObject(FacesContext.getCurrentInstance(), component, theId);
    }
    
    public static Throwable getRootCause(Throwable cause) {
        if (cause != null) {
            Throwable source = cause.getCause();
            if (source != null) {
                return getRootCause(source);
            } else {
                return cause;
            }
        }
        return null;
    }

    public static boolean isValidationFailed() {
        return FacesContext.getCurrentInstance().isValidationFailed();
    }

    public static boolean isDummySelectItem(UIComponent component, String value) {
        for (UIComponent children : component.getChildren()) {
            if (children instanceof UISelectItem) {
                UISelectItem item = (UISelectItem) children;
                if (item.getItemValue() == null && item.getItemLabel().equals(value)) {
                    return true;
                }
                break;
            }
        }
        return false;
    }
    
    
    public static void cerrarSessionUsuario(){
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        session.invalidate();
    }
    
    public static void addParameterJavaScript(String nameParametro, Object value){
        RequestContext context = RequestContext.getCurrentInstance();
        context.addCallbackParam(nameParametro,value);
    }
    
    /**
     * *****************Inicio de modulo******************* 
     *      Nombre del módulo:
     *          - getSesion
     *      Objetivo: 
     *          - Obtener la instancia de la sesion actual de la aplicacion
     *      Parámetros de entrada:
     *          - no hay
     *      Parámetros de retorno: 
     *          - (HttpSession) context.getExternalContext().getSession(false);
     *             retorna la sesion actual del sistema
     */
    public HttpSession getSesion(){
        FacesContext context = javax.faces.context.FacesContext.getCurrentInstance();
        /*Objetivo:
         *      almacena el contexto de la intancia actual de navegación
         */
        return (HttpSession) context.getExternalContext().getSession(true);
    }
    
    /**
     * *****************Inicio de modulo******************* 
     *      Nombre del módulo:
     *          - getEmpleado
     *      Objetivo: 
     *          - Obtener la variable Empleado almacenada en el Bean 
     *            ddkClientesController el cual es un bean del tipo sessionScope
   */     
    public Empleado getEmpleado() {
        
        EmpleadoController empleado = (EmpleadoController) getSesion().getAttribute("empleadoController");
        /*Objetivo:
         *      obtiene el controlador ddkClientesController almacenado en la 
         *      sesión actual de la aplicación
         */
        return empleado.getUsuario();
    }
    
    public PreingresoController getPreingresoController() {
        
        PreingresoController preingresoController = (PreingresoController) getSesion().getAttribute("preingresoController");
        /*Objetivo:
         *      obtiene el controlador cajaController almacenado en la 
         *      sesión actual de la aplicación
         */
        return preingresoController;
    }
    
    public CajaController getCajaController() {
        
        CajaController cajaController = (CajaController) getSesion().getAttribute("cajaController");
        /*Objetivo:
         *      obtiene el controlador cajaController almacenado en la 
         *      sesión actual de la aplicación
         */
        return cajaController;
    }
    
    
    public IngresoController getIngresoController() {
        IngresoController ingresoController = (IngresoController) getSesion().getAttribute("ingresoController");
        /*Objetivo:
         *      obtiene el controlador ingresoController almacenado en la 
         *      sesión actual de la aplicación
         */
        return ingresoController;
    }
    
    
    public InventarioController getInventarioController() {
        
        InventarioController inventarioController = (InventarioController) getSesion().getAttribute("inventarioController");
        /*Objetivo:
         *      obtiene el controlador cajaController almacenado en la 
         *      sesión actual de la aplicación
         */
        return inventarioController;
    }
    
    
    public EnvioController getEnvioController() {
        
        EnvioController envioController = (EnvioController) getSesion().getAttribute("envioController");
        /*Objetivo:
         *      obtiene el controlador cajaController almacenado en la 
         *      sesión actual de la aplicación
         */
        return envioController;
    }
    
    public VentaController getVentaController() {
        
        VentaController ventaController = (VentaController) getSesion().getAttribute("ventasController");
        /*Objetivo:
         *      obtiene el controlador cajaController almacenado en la 
         *      sesión actual de la aplicación
         */
        return ventaController;
    }
    
     public FacturaController getFacturaController() {
        
        FacturaController facturaController = (FacturaController) getSesion().getAttribute("facturaController");
        /*Objetivo:
         *      obtiene el controlador cajaController almacenado en la 
         *      sesión actual de la aplicación
         */
        return facturaController;
    }
    
    /*
     * FUNCION QUE ENCRIPTA EL PASSWORD A MD5
     */
    public String getMD5(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        final MessageDigest algorithm = MessageDigest.getInstance("MD5");
        algorithm.reset();
        algorithm.update(text.getBytes("utf-8"));
        final StringBuilder hexStringBuilder = new StringBuilder();
        final byte[] digest = algorithm.digest();
        for (byte digestItem : digest) {
            String hex = Integer.toHexString(0xFF & digestItem);
            if (hex.length() == 1) {
                hexStringBuilder.append('0');
            }
            hexStringBuilder.append(hex.toUpperCase());
        }
        return hexStringBuilder.toString();
    }
    
    //FUNCION QUE RECUPERA EL PATH DEL CONTEXTO
    public static String getPathContext(){
        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        return servletContext.getContextPath();
    }
    
    public static void putObjectSession(String nombre, Object value){
        FacesContext context = FacesContext.getCurrentInstance();  
        context.getExternalContext().getSessionMap().put(nombre,value);
    }
    
}//FIN CLASE JSFUTIL