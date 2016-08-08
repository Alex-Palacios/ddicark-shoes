package com.ddicark.controladores;

import com.ddicark.auxiliares.JsfUtil;
import com.ddicark.auxiliares.funciones;
import com.ddicark.entidades.DetalleEnvio;
import com.ddicark.entidades.Envio;
import com.ddicark.jpaFacades.DetalleEnvioFacade;
import com.ddicark.jpaFacades.EnvioFacade;
import com.ddicark.jpaFacades.FacturaFacade;
import com.ddicark.jpaFacades.InventarioFacade;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.context.RequestContext;

@ManagedBean(name = "envioController")
@SessionScoped
public class EnvioController extends AbstractController<Envio> implements Serializable {

    @EJB
    private EnvioFacade ejbFacadeEnvio;
    
    @EJB
    private FacturaFacade ejbFacadeFactura;
    
    @EJB
    private InventarioFacade ejbFacadeInventario;

    @EJB
    private DetalleEnvioFacade ejbFacadeDetalleEnvio;


     public EnvioController() {
        super(Envio.class);
    }
     
    @PostConstruct
    public void init() {
        super.setFacade(ejbFacadeEnvio);
    }
    
    
    //Variables de Control
    private List<Envio> enviosEnProcesoVentaFacturacion;
    
    private String pathServletNotaEnvio;
    
    private List<Envio> notasEnvios;
    private List<Envio> filtroEnvios;
    
    private int month;
    private int year;
    
    private Envio seleccion;
    
    //GETTERS AND SETTERS
    public Envio getSeleccion() {
        return seleccion;
    }

    public void setSeleccion(Envio seleccion) {
        this.seleccion = seleccion;
    }
    
    
    public List<Envio> getEnviosEnProcesoVentaFacturacion() {
        enviosEnProcesoVentaFacturacion = ejbFacadeEnvio.getEnviosEnProceso();
        return enviosEnProcesoVentaFacturacion;
    }

    public String getPathServletNotaEnvio() {
        pathServletNotaEnvio = new JsfUtil().getPathContext() + "/ordenEnvio";
        Integer num = 0;
        try{
            num = new JsfUtil().getInventarioController().getImprimirOrden().getNumenvio();
        }catch(Exception ex){
            new funciones().setMsj(3, "ERROR AL RECUPERAR ENVIO CONTROLADOR");
        }
        if(num != null){
            pathServletNotaEnvio = pathServletNotaEnvio+"?numEnvio="+num;
        }
        return pathServletNotaEnvio;
    }

    public void setPathServletNotaEnvio(String pathServletNotaEnvio) {
        this.pathServletNotaEnvio = pathServletNotaEnvio;
    }

    public List<Envio> getNotasEnvios() {
        return notasEnvios;
    }

    public void setNotasEnvios(List<Envio> notasEnvios) {
        this.notasEnvios = notasEnvios;
    }

    
    public void prepararConsultaNotasEnvios(){
        Calendar hoy = Calendar.getInstance();
        year = hoy.get(Calendar.YEAR);
        month = hoy.get(Calendar.MONTH) +1;
        actualizarConsultaEnvios();
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

    public List<Envio> getFiltroEnvios() {
        return filtroEnvios;
    }

    public void setFiltroEnvios(List<Envio> filtroEnvios) {
        this.filtroEnvios = filtroEnvios;
    }
    
    
    
    public void actualizarConsultaEnvios(){
        notasEnvios = ejbFacadeEnvio.consultarEnvios(month, year);
        filtroEnvios = null;
    }
    
    
    
    
    public void prepareCancelarEnvio(){
        if(seleccion != null){
            if(seleccion.getVenta() == null || seleccion.getVenta().getFacturaCollection()== null || seleccion.getVenta().getFacturaCollection().size() == 0 ){
                if(seleccion.getEstado().equals("EMPACADO")){
                    RequestContext context = RequestContext.getCurrentInstance(); 
                    context.execute("cancelarEnvio.show();");
                }else{
                    new funciones().setMsj(3, "ENVIO YA ESTA PROCESADO");
                }
            }else{
                new funciones().setMsj(3, "ENVIO YA ESTA FACTURADO");
            }
        }else{
            new funciones().setMsj(3, "ERROR AL CANCELAR ENVIO");
        }
    }
    
    
    public void cancelarEnvio(){
        if(seleccion != null){
            List<DetalleEnvio> de = ejbFacadeDetalleEnvio.getDetalleEnvio(seleccion);
            boolean salir=false;
            for(DetalleEnvio actual: de){
                if(actual.getInventario().getEstadoproducto().equals("EMPACADO")){
                    actual.getInventario().setEstadoproducto("EN EXISTENCIA");
                    ejbFacadeInventario.edit(actual.getInventario());
                }
            }
            seleccion.setEstado("CANCELADO");
            ejbFacadeEnvio.edit(seleccion);
            new funciones().setMsj(1, "ENVIO CANCELADO");
        }else{
            new funciones().setMsj(3, "ERROR AL CANCELAR ENVIO");
        }
    }
    
}
