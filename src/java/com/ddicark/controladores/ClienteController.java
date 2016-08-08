package com.ddicark.controladores;


import com.ddicark.auxiliares.JsfUtil;
import com.ddicark.auxiliares.funciones;
import com.ddicark.entidades.Cliente;
import com.ddicark.entidades.Empleado;
import com.ddicark.jpaFacades.ClienteFacade;
import com.ddicark.jpaFacades.MunicipiosFacade;
import java.io.InputStream;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.ServletContext;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@ManagedBean(name = "clienteController")
@SessionScoped
public class ClienteController extends AbstractController<Cliente> implements Serializable {

    @EJB
    private ClienteFacade ejbFacadeCliente;
    
    @EJB
    private MunicipiosFacade ejbFacadeMunicipios;
    
    

    public ClienteController() {
        super(Cliente.class);
    }
    //Inicializa controlador de persistencia de proveedores
    @PostConstruct
    public void init() {
        super.setFacade(ejbFacadeCliente);
    }

    private List<Cliente> clientes;
    //Variables Control
    private List<Cliente> clientesNaturales;
    private List<Cliente> clientesJuridicas;
    private Cliente clienteSelec;
    private List<String> departamentos;
    private List<String> municipios;
    private List<Cliente> filtroClientes;
    private Empleado vendedor;
    //Pedidos nuevo cliente
    private int naturalezaCliente;
   
    //Cartera
    private int tipoReporte;
    private boolean editarCliente = false;
    private Cliente clienteEditado;
    private Empleado nuevoEmpleadoAsignado;
    
    
    private String pathServletFichaCliente;
            
            
    //GET AND SET
    public String getPathServletFichaCliente() {
        pathServletFichaCliente = JsfUtil.getPathContext() + "/fichaCliente";
        return pathServletFichaCliente;
    }
    
    public Empleado getNuevoEmpleadoAsignado() {
        return nuevoEmpleadoAsignado;
    }

    public void setNuevoEmpleadoAsignado(Empleado nuevoEmpleadoAsignado) {
        this.nuevoEmpleadoAsignado = nuevoEmpleadoAsignado;
    }
    
    
    public List<Cliente> getClientesNaturales() {
        clientesNaturales = ejbFacadeCliente.getClientesByNaturaleza("PERSONA NATURAL");
        return clientesNaturales;
    }

    public List<Cliente> getClientesJuridicas() {
        clientesJuridicas = ejbFacadeCliente.getClientesByNaturaleza("PERSONA JURIDICA");
        return clientesJuridicas;
    }

    public List<Cliente> getClientes() {
        clientes = ejbFacadeCliente.findAll();
        if(clientes == null){
            clientes = new ArrayList<Cliente>();
        }
        return clientes;
    }

    
    public Cliente getClienteEditado() {
        return clienteEditado;
    }

    public void setClienteEditado(Cliente clienteEditado) {
        this.clienteEditado = clienteEditado;
    }
    
    

    public Cliente getClienteSelec() {
        return clienteSelec;
    }

    public void setClienteSelec(Cliente clienteSelec) {
        this.clienteSelec = clienteSelec;
    }

    public List<Cliente> getFiltroClientes() {
        return filtroClientes;
    }

    public void setFiltroClientes(List<Cliente> filtroClientes) {
        this.filtroClientes = filtroClientes;
    }
    
    
    
    public int getTipoReporte() {
        return tipoReporte;
    }

    public void setTipoReporte(int tipoReporte) {
        this.tipoReporte = tipoReporte;
    }
    
    public int getNaturalezaCliente() {
        return naturalezaCliente;
    }

    public void setNaturalezaCliente(int naturalezaCliente) {
        this.naturalezaCliente = naturalezaCliente;
    }

    public boolean isEditarCliente() {
        return editarCliente;
    }

    public void setEditarCliente(boolean editarCliente) {
        this.editarCliente = editarCliente;
    }
    
    public List<String> getDepartamentos() {
        departamentos = ejbFacadeMunicipios.getDeptos();
        return departamentos;
    }

    public List<String> getMunicipios() {
        return municipios;
    }
    
    //Actualiza selectoneMenu de Municipios
    public void actualizarMunicipios(String depto){
        municipios = ejbFacadeMunicipios.getMunicipios(depto);
    }

    public Empleado getVendedor() {
        return vendedor;
    }

    public void setVendedor(Empleado vendedor) {
        this.vendedor = vendedor;
    }
    
    
    /*
     * FUNCIONES GENERALES
     */
    
    public void prepararCliente(ActionEvent event){
        naturalezaCliente = 1;
        super.prepararCrear(event);
    }
    
    /*
     * PERSISTIR CLIENTE EN LA BD
     */
    public void guardarCliente(){
        Cliente c = this.getSelected();
        int naturaleza = this.naturalezaCliente;
        RequestContext context = RequestContext.getCurrentInstance(); 
        if(!c.getDeptoCliente().equals("x") && !c.getMunicipioCliente().equals("x")){
//            if(naturaleza == 1 && "".equals(c.getDuiCliente()) && "".equals(c.getNitCliente()) && "".equals(c.getCarnetResidente())){
//                context.addCallbackParam("validar",false);
//                new funciones().setMsj(2, "Debe ingresar al menos un documento de identificacion DUI, NIT o CARNET");
//            }else{
//                
                //EMPLEADO ASIGNADO AL CLIENTE
                //c.setEmpleadoasignado(new JsfUtil().getEmpleado());
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
     * FUNCIONES CARTERA CLIENTES
     */
    
    //Cada vez que se quiere ver un nuevo cliente seleccionado
    public void showCliente(){
        editarCliente = false;
        clienteEditado = null;
        nuevoEmpleadoAsignado = null;
    }
    
    //Editar 
    public void cambiarEditar(){
        editarCliente = true;
        clienteEditado = new Cliente();
        clienteEditado.setNumcuenta(clienteSelec.getNumcuenta());
        clienteEditado.setNombreCliente(clienteSelec.getNombreCliente());
        clienteEditado.setApellidoCliente(clienteSelec.getApellidoCliente());
        clienteEditado.setDuiCliente(clienteSelec.getDuiCliente());
        clienteEditado.setNitCliente(clienteSelec.getNitCliente());
        clienteEditado.setCarnetResidente(clienteSelec.getCarnetResidente());
        clienteEditado.setGeneroCliente(clienteSelec.getGeneroCliente());
        clienteEditado.setTelCliente(clienteSelec.getTelCliente());
        clienteEditado.setDeptoCliente(clienteSelec.getDeptoCliente());
        clienteEditado.setMunicipioCliente(clienteSelec.getMunicipioCliente());
        clienteEditado.setDireccionCliente(clienteSelec.getDireccionCliente());
        clienteEditado.setOcupacionCliente(clienteSelec.getOcupacionCliente());
        clienteEditado.setComercioContacto(clienteSelec.getComercioContacto());
        clienteEditado.setNrcCliente(clienteSelec.getNrcCliente());
        clienteEditado.setTelComercio(clienteSelec.getTelComercio());
        clienteEditado.setEmpleadoasignado(clienteSelec.getEmpleadoasignado());
        clienteEditado.setNaturaleza(clienteSelec.getNaturaleza());
        actualizarMunicipios(clienteEditado.getDeptoCliente());
        clienteEditado.setLimite(clienteSelec.getLimite());
        clienteEditado.setDireccionNegocio(clienteSelec.getDireccionNegocio());
        clienteEditado.setDiaVisita(clienteSelec.getDiaVisita());
        clienteEditado.setNota(clienteSelec.getNota());
    }
    
    //cancelar Editar
    public void cancelarEditar(){
        editarCliente = false;
    }
    
    public void guardarCambiosCliente(){
        RequestContext context = RequestContext.getCurrentInstance(); 
        if(!clienteEditado.getDeptoCliente().equals("x") && !clienteEditado.getMunicipioCliente().equals("x")){
            if(clienteEditado.getNaturaleza().equals("PERSONA NATURAL") && "".equals(clienteEditado.getDuiCliente()) && "".equals(clienteEditado.getNitCliente()) && "".equals(clienteEditado.getCarnetResidente())){
                context.addCallbackParam("validar",false);
                new funciones().setMsj(2, "Debe ingresar al menos un documento de identificacion DUI, NIT o CARNET");
            }else{
                //MAYUSCULAS
                if(clienteEditado.getNombreCliente() != null){
                    clienteEditado.setNombreCliente(clienteEditado.getNombreCliente().toUpperCase());
                }
                if(clienteEditado.getApellidoCliente() != null){
                    clienteEditado.setApellidoCliente(clienteEditado.getApellidoCliente().toUpperCase());
                }
                if(clienteEditado.getOcupacionCliente() != null){
                    clienteEditado.setOcupacionCliente(clienteEditado.getOcupacionCliente().toUpperCase());
                }
                if(clienteEditado.getComercioContacto() != null){
                    clienteEditado.setComercioContacto(clienteEditado.getComercioContacto().toUpperCase());
                } 
                if(nuevoEmpleadoAsignado != null){
                    if(clienteEditado.getEmpleadoasignado() != null){
                        if(clienteEditado.getEmpleadoasignado().getCodempleado().equals(nuevoEmpleadoAsignado.getCodempleado())){
                            //NO HACE NADA PORQUE NO HAY CAMBIO DE EMPLEADO
                        }else{
                            clienteEditado.setEmpleadoasignado(nuevoEmpleadoAsignado);
                        }
                    }else{
                        clienteEditado.setEmpleadoasignado(nuevoEmpleadoAsignado);
                    }
                }
                try{
                    clienteSelec = clienteEditado;
                    ejbFacadeCliente.edit(clienteSelec);
                    context.addCallbackParam("validar",true);
                    new funciones().setMsj(1, "CLIENTE CON CUENTA N°: "+ clienteSelec.getNumcuenta() +" ACTUALIZADO");
                }catch(Exception e){
                    context.addCallbackParam("validar",false);
                    new funciones().setMsj(3, "ERROR: " +e.getMessage());
                }
            }
            
        }else{
            context.addCallbackParam("validar",false);
            new funciones().setMsj(2, "Ingrese el DEPARTAMENTO y MUNICIPIO del CLIENTE");
        }
    }
    
   
    
    
    public void prepararConsultaCliente(){
        filtroClientes = clientes;
    }
    
    
    public void printDatos(){
        FacesContext context = FacesContext.getCurrentInstance();  
        context.getExternalContext().getSessionMap().put("cliente",clienteSelec);
    }
    
    
    
    
    
    String pathServletReporteCxV;
    InputStream stream;
    private StreamedContent fileXLS;

    public StreamedContent getFileXLS() {
        return this.fileXLS;
    }
    
    
    public String getPathServletReporteCxV() {
        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        pathServletReporteCxV = servletContext.getContextPath() + "/reporteClienteVendedor";
        
        return pathServletReporteCxV;
    }
    
    
    public void reporteClienteVendedor(){
        FacesContext context = FacesContext.getCurrentInstance();  
        context.getExternalContext().getSessionMap().put("vendedor",vendedor);
    }
    
    
    public void reporteClienteVendedorXLS(){
        try{
            FacesContext context = FacesContext.getCurrentInstance();  
            context.getExternalContext().getSessionMap().put("vendedor",vendedor);
            context.getExternalContext().getSessionMap().put("archivo",stream);
            context.getExternalContext().dispatch("/reporteCVXLS");
            stream = (InputStream) context.getExternalContext().getSessionMap().get("archivo");
            fileXLS = new DefaultStreamedContent(stream, "application/xls","clienteXvendedor.xls");
        }catch(Exception ex){ } 
    }
}
