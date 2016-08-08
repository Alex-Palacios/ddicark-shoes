package com.ddicark.controladores;

import com.ddicark.auxiliares.funciones;
import com.ddicark.entidades.DetalleEnvio;
import com.ddicark.entidades.Envio;
import com.ddicark.entidades.Factura;
import com.ddicark.jpaFacades.CajaFacade;
import com.ddicark.jpaFacades.DetalleEnvioFacade;
import com.ddicark.jpaFacades.InventarioFacade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.context.RequestContext;

@ManagedBean(name = "detalleEnvioController")
@SessionScoped
public class DetalleEnvioController extends AbstractController<DetalleEnvio> implements Serializable {

    @EJB
    private DetalleEnvioFacade ejbFacadeDetalleEnvio;

    @EJB
    private InventarioFacade ejbFacadeInventario;
    
    @EJB
    private CajaFacade ejbFacadeCaja;

    public DetalleEnvioController() {
        super(DetalleEnvio.class);
    }

    @PostConstruct
    public void init() {
        super.setFacade(ejbFacadeDetalleEnvio);
    }

    @Override
    protected void setEmbeddableKeys() {
        this.getSelected().getDetalleEnvioPK().setNumenvio(this.getSelected().getEnvio().getNumenvio());
        this.getSelected().getDetalleEnvioPK().setCodigoProducto(this.getSelected().getInventario().getCodigo());
    }

    @Override
    protected void initializeEmbeddableKey() {
        this.getSelected().setDetalleEnvioPK(new com.ddicark.entidades.DetalleEnvioPK());
    }
    
    //VARIABLES DE CONTROL
    private List<DetalleEnvio> detalleOrdenEnvio;
    private List<DetalleEnvio> reingresosDevoluciones;
    private List<DetalleEnvio> productosAnulados;
    //Reingresar Producto al inventario
    private String nuevoEstado;
    private DetalleEnvio de = new DetalleEnvio();
    
    
    //GETTERS AND SETTERS
    public List<DetalleEnvio> getDetalleOrdenEnvio(Envio orden) {
        if(orden != null){
            detalleOrdenEnvio = ejbFacadeDetalleEnvio.detalleOrdenEnvio(orden);  
        }
        return detalleOrdenEnvio;
    }

    public List<DetalleEnvio> getReingresosDevoluciones() {
        return reingresosDevoluciones;
    }

    public String getNuevoEstado() {
        return nuevoEstado;
    }

    public void setNuevoEstado(String nuevoEstado) {
        this.nuevoEstado = nuevoEstado;
    }

    public DetalleEnvio getDe() {
        return de;
    }

    public void setDe(DetalleEnvio de) {
        this.de = de;
    }

    public List<DetalleEnvio> getProductosAnulados(Factura facturaAnulada) {
        if(facturaAnulada == null){
            productosAnulados = null;
        }else{
            productosAnulados = ejbFacadeDetalleEnvio.productosAnulados(facturaAnulada);
            for(DetalleEnvio actual: productosAnulados){
                if(actual.getNota().equals("REFACTURAR")){
                    actual.setNota("ANULADO");
                    ejbFacadeDetalleEnvio.edit(actual);
                }
            }
        }
        
        return productosAnulados;
    }
    
    
    
    /*
     * FUNCIONES REINGRESO POR DEVOLUCION
     */
    
    
    public void prepararReingreso(DetalleEnvio detalle){
        if(detalle != null){
            nuevoEstado = null;
            de = detalle;
        }else{
            nuevoEstado = null;
            de = null;
        }
    }
    
    
    public void reingresarProducto(){
        RequestContext context = RequestContext.getCurrentInstance();
        if(de != null){
            if(de.getInventario() != null){
                try{
                    de.getInventario().setEstadoproducto(nuevoEstado);
                    ejbFacadeInventario.edit(de.getInventario());
                    de.setNota("DEVOLUCION");
                    ejbFacadeDetalleEnvio.edit(de);
                    ejbFacadeCaja.actualizarCajaCompleta(de.getInventario().getNumcaja());
                    new funciones().setMsj(1, "PRODUCTO REINGRESADO CORRECTAMENTE COMO : " + de.getInventario().getEstadoproducto());
                    prepararConsultaReingreso();
                    context.addCallbackParam("validar", true);
                }catch(Exception e){
                    new funciones().setMsj(3, "ERROR: " + e.getMessage());
                    context.addCallbackParam("validar", false); 
                }
            }else{
               new funciones().setMsj(3, "PRODUCTO A REINGRESAR = NULL");
               context.addCallbackParam("validar", false); 
            }
        }else{
            new funciones().setMsj(3, "DETALLE ENVIO = NULL");
            context.addCallbackParam("validar", false);
        }
    }
    
    
    private boolean reingresar;
    private boolean reingresarTodos;
    
    private List<DetalleEnvio> listaReingreso;
    private List<DetalleEnvio> filtroReingreso;

    public boolean isReingresar() {
        return reingresar;
    }

    public void setReingresar(boolean reingresar) {
        this.reingresar = reingresar;
    }

    public List<DetalleEnvio> getListaReingreso() {
        return listaReingreso;
    }

    public void setListaReingreso(List<DetalleEnvio> listaReingreso) {
        this.listaReingreso = listaReingreso;
    }
    
    
    public List<DetalleEnvio> getFiltroReingreso() {
        return filtroReingreso;
    }

    public void setFiltroReingreso(List<DetalleEnvio> filtroReingreso) {
        this.filtroReingreso = filtroReingreso;
    }

    public boolean isReingresarTodos() {
        return reingresarTodos;
    }

    public void setReingresarTodos(boolean reingresarTodos) {
        this.reingresarTodos = reingresarTodos;
    }
    
    
    
    
    
    public void prepararConsultaReingreso(){
        reingresosDevoluciones = ejbFacadeDetalleEnvio.reingresosInventarioDevoluciones();
        filtroReingreso = reingresosDevoluciones;
        listaReingreso = new ArrayList<DetalleEnvio>();
        reingresar = false;
        reingresarTodos = false;
    }
    
    
    
    public void addListaReingreso(DetalleEnvio de){
        if(de != null){
            if(reingresar){
                listaReingreso.add(de);
                new funciones().setMsj(1, listaReingreso.size() +" UNIDADES SELECCIONADAS");
            }else{
                listaReingreso.remove(de);
                new funciones().setMsj(1, listaReingreso.size() +" UNIDADES SELECCIONADAS");
            }
        }
    }
    
    
    public void seleccionarTodos(){
        if(reingresarTodos){
            listaReingreso = filtroReingreso;
            reingresar = true;
            new funciones().setMsj(1,listaReingreso.size() +" UNIDADES SELECCIONADAS");
        }else{
            listaReingreso = new ArrayList<DetalleEnvio>();
            reingresar = false;
            new funciones().setMsj(1,listaReingreso.size() +" UNIDADES SELECCIONADAS");
        }
    }
    
    
    
    public void actualizarSeleccionFiltro(){
        reingresarTodos = false;
        seleccionarTodos();
    }
    
    public void prepararReingresarSeleccionados(){
        RequestContext context = RequestContext.getCurrentInstance();
        if(listaReingreso != null && !listaReingreso.isEmpty()){
            nuevoEstado = null;
            context.execute("cfdEstadoSeleccionados.show();");
        }else{
            new funciones().setMsj(2, "NO HA SELECCIONADO PRODUCTOS A REINGRESAR");
        }
    }
    
    public void reingresarSeleccionados(){
        int productosReingresados = 0;
        int productosNoReingresados = 0;
        for(DetalleEnvio de : listaReingreso){
            if(de.getInventario() != null){
                try{
                    de.getInventario().setEstadoproducto(nuevoEstado);
                    ejbFacadeInventario.edit(de.getInventario());
                    de.setNota("DEVOLUCION");
                    ejbFacadeDetalleEnvio.edit(de);
                    productosReingresados++;
                }catch(Exception e){
                    productosNoReingresados++;
                }
            }else{
               productosNoReingresados++; 
            }
        }
        if(productosReingresados < listaReingreso.size()){
            new funciones().setMsj(3,"NO SE PUDIERON INGRESAR" + productosNoReingresados +" PRODUCTOS AL INVENTARIO");
        }
        new funciones().setMsj(1, "SE REINGRESARON "+productosReingresados+" PRODUCTOS AL INVENTARIO");
        prepararConsultaReingreso();
        ejbFacadeCaja.updateEstadoCajaCompleta();
    }

}
