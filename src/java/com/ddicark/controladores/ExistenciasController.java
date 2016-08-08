package com.ddicark.controladores;

import com.ddicark.auxiliares.funciones;
import com.ddicark.auxiliares.InventarioConsulta;
import com.ddicark.auxiliares.JsfUtil;
import com.ddicark.entidades.Caja;
import com.ddicark.entidades.Inventario;
import com.ddicark.jpaFacades.CajaFacade;
import com.ddicark.jpaFacades.InventarioFacade;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;
import org.primefaces.context.RequestContext;
  


@ManagedBean(name = "existenciasController")
@SessionScoped
public class ExistenciasController implements Serializable {

    @EJB
    private InventarioFacade ejbFacadeInventario;
    
    @EJB
    private CajaFacade ejbFacadeCaja;
    
    public ExistenciasController() {
        
    }
        
    //VARIABLES PARA CONSULTAS INVENTARIO
    //EXISTENCIAS
    private List<InventarioConsulta> consultaExistencia;
    private List<InventarioConsulta> filtro;
    private int existencias;
    private int muestras;
    private int defectuosos;
    private String[] colors;  
    private SelectItem[] colorsOptions;
    private String[] marcasInv;  
    private SelectItem[] marcasOptions;
    private SelectItem[] estadosOptions;
    private SelectItem[] completaOptions;
    private int articulosCaja;
    private int tipoProducto;
    private List<String> ver;
    private List<String> mostrar;
    private String marca;
    private String material;
    private List<String> marcas;
    private List<String> materiales;
    private int consultarCaja;
    private int existenciasCajas;
    private int cajasCompletas;
    private int cajasIncompletas;
    
    //INVENTARIO FISICO
    private List<Inventario> unidadesFisico;
    private List<InventarioConsulta> cajasFisico;
    private List<InventarioConsulta> filtroCajasFisico;
    private InventarioConsulta actual;
    private int numUnidadesFisicas;
    private int numCajasFisicas;
    private Caja cajaFisica; 
    private List<Inventario> detalleCaja;
    private Inventario articuloFisico;
    private boolean editarArticuloFisico =false;
    

    
    
    //GETTERS AND SETTERS
    public List<InventarioConsulta> getConsultaExistencia() {
        return consultaExistencia;
    }

    public void setConsultaExistencia(List<InventarioConsulta> consultaExistencia) {
        this.consultaExistencia = consultaExistencia;
    }

    public List<InventarioConsulta> getFiltro() {
        return filtro;
    }

    public void setFiltro(List<InventarioConsulta> filtro) {
        this.filtro = filtro;
    }
   

     

    

    public int getExistencias() {
        return existencias;
    }

    public void setExistencias(int existencias) {
        this.existencias = existencias;
    }

    public int getMuestras() {
        return muestras;
    }

    public void setMuestras(int muestras) {
        this.muestras = muestras;
    }

    public int getDefectuosos() {
        return defectuosos;
    }

    public void setDefectuosos(int defectuosos) {
        this.defectuosos = defectuosos;
    }

    public SelectItem[] getColorsOptions() {
        List<String> colores = ejbFacadeInventario.getColorList(tipoProducto);
        if(colores != null){
            colors = new String[colores.size()];
            for(int i=0; i < colores.size(); i++){
                colors[i] = colores.get(i);
            }
        }
        colorsOptions = new funciones().createFilterOptions(colors);
        return colorsOptions;
    }

    public void setColorsOptions(SelectItem[] colorsOptions) {
        this.colorsOptions = colorsOptions;
    }

    public int getArticulosCaja() {
        return articulosCaja;
    }

    public void setArticulosCaja(int articulosCaja) {
        this.articulosCaja = articulosCaja;
    }

    public int getTipoProducto() {
        return tipoProducto;
    }

    public void setTipoProducto(int tipoProducto) {
        this.tipoProducto = tipoProducto;
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

    public List<String> getMarcas() {
        marcas = ejbFacadeInventario.getMarcaList(tipoProducto);
        return marcas;
    }

    public void setMarcas(List<String> marcas) {
        this.marcas = marcas;
    }

    public List<String> getMateriales() {
        materiales = ejbFacadeInventario.getMaterialList(tipoProducto);
        return materiales;
    }

    public void setMateriales(List<String> materiales) {
        this.materiales = materiales;
    }

    public List<String> getVer() {
        return ver;
    }

    public void setVer(List<String> ver) {
        this.ver = ver;
    }

    public List<String> getMostrar() {
        return mostrar;
    }

    public void setMostrar(List<String> mostrar) {
        this.mostrar = mostrar;
    }

    public int getConsultarCaja() {
        return consultarCaja;
    }

    public void setConsultarCaja(int consultarCaja) {
        this.consultarCaja = consultarCaja;
    }

    public int getExistenciasCajas() {
        return existenciasCajas;
    }

    public void setExistenciasCajas(int existenciasCajas) {
        this.existenciasCajas = existenciasCajas;
    }

    public int getCajasCompletas() {
        return cajasCompletas;
    }

    public void setCajasCompletas(int cajasCompletas) {
        this.cajasCompletas = cajasCompletas;
    }

    public int getCajasIncompletas() {
        return cajasIncompletas;
    }

    public void setCajasIncompletas(int cajasIncompletas) {
        this.cajasIncompletas = cajasIncompletas;
    }

    public List<Inventario> getUnidadesFisico() {
        return unidadesFisico;
    }

    public List<InventarioConsulta> getCajasFisico() {
        return cajasFisico;
    }

    public void setCajasFisico(List<InventarioConsulta> cajasFisico) {
        this.cajasFisico = cajasFisico;
    }

    public List<InventarioConsulta> getFiltroCajasFisico() {
        return filtroCajasFisico;
    }

    public void setFiltroCajasFisico(List<InventarioConsulta> filtroCajasFisico) {
        this.filtroCajasFisico = filtroCajasFisico;
    }

    public InventarioConsulta getActual() {
        return actual;
    }

    public void setActual(InventarioConsulta actual) {
        this.actual = actual;
    }

    

    

    

   
    


   

    public int getNumUnidadesFisicas() {
        return numUnidadesFisicas;
    }

    public void setNumUnidadesFisicas(int numUnidadesFisicas) {
        this.numUnidadesFisicas = numUnidadesFisicas;
    }

    public SelectItem[] getMarcasOptions() {
        List<String> marcasList = ejbFacadeInventario.getMarcaList(tipoProducto);
        if(marcasList != null){
            marcasInv = new String[marcasList.size()];
            for(int i=0; i < marcasList.size(); i++){
                marcasInv[i] = marcasList.get(i);
            }
        }
        marcasOptions = new funciones().createFilterOptions(marcasInv);
        return marcasOptions;
    }

    
    public SelectItem[] getEstadosOptions() {
        SelectItem[] options = new SelectItem[5]; 
        options[0] = new SelectItem("", "Select"); 
        options[1] = new SelectItem("EN EXISTENCIA", "EN EXISTENCIA");  
        options[2] = new SelectItem("MUESTRA", "MUESTRA");  
        options[3] = new SelectItem("DEFECTUOSO", "DEFECTUOSO"); 
        options[4] = new SelectItem("PERDIDA", "PERDIDA");
        estadosOptions = options;
        return estadosOptions;
    }

    public SelectItem[] getCompletaOptions() {
        SelectItem[] options = new SelectItem[3]; 
        options[0] = new SelectItem("", "Select"); 
        options[1] = new SelectItem("true", "COMPLETA");  
        options[2] = new SelectItem("false", "INCOMPLETA");   
        completaOptions = options;
        return completaOptions;
    }
    
    

    public int getNumCajasFisicas() {
        numCajasFisicas = 0;
        if(filtroCajasFisico != null){
            numCajasFisicas = filtroCajasFisico.size();
        }else{
            numCajasFisicas = cajasFisico.size();
        }
        return numCajasFisicas;
    }

    public void setNumCajasFisicas(int numCajasFisicas) {
        this.numCajasFisicas = numCajasFisicas;
    }

    public Caja getCajaFisica() {
        return cajaFisica;
    }

    public void setCajaFisica(Caja cajaFisica) {
        this.cajaFisica = cajaFisica;
    }

    public List<Inventario> getDetalleCaja() {
        detalleCaja = ejbFacadeInventario.detalleDeCaja(cajaFisica);
        return detalleCaja;
    }

    public void setDetalleCaja(List<Inventario> detalleCaja) {
        this.detalleCaja = detalleCaja;
    }

    public Inventario getArticuloFisico() {
        return articuloFisico;
    }

    public void setArticuloFisico(Inventario articuloFisico) {
        this.articuloFisico = articuloFisico;
    }

    public boolean isEditarArticuloFisico() {
        return editarArticuloFisico;
    }

    public void setEditarArticuloFisico(boolean editarArticuloFisico) {
        this.editarArticuloFisico = editarArticuloFisico;
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
    * FUNCIONES PARA CONSULTAS DE INVENTARIO POR UNIDAD
    */
   public void prepararConsultaExistenciasUnidades(){
       consultaExistencia = ejbFacadeInventario.consultaExistenciaUnidades(1,1,null,null);
       if(filtro != null){
           filtro.clear();
       }
       filtro = consultaExistencia;
       calcularTotalesFiltro();
       tipoProducto = 1;
   }
   
   /*
    * Funcion que se ejecuta cada vez que se filtra la tabla
    */
   public void onFilter(){
        calcularTotalesFiltro();
   }
   
   //Calcula los totales del filtro
   public void calcularTotalesFiltro(){
       resetTotales();
       if(filtro != null){
           for(InventarioConsulta obj : filtro){
               Object[] actual = obj.getColumna();
               existencias = existencias + Integer.parseInt(actual[4].toString());
               muestras = muestras + Integer.parseInt(actual[5].toString());
               defectuosos = defectuosos + Integer.parseInt(actual[6].toString());
           }
       }
   }
   
   //Resetea totales
   public void resetTotales(){
       existencias = 0;
       muestras = 0;
       defectuosos = 0;
   }
   
   /*
    * FUNCION que consulta con los filtros ingresados
    */
   
   public void consultarFiltro(){
       consultaExistencia = ejbFacadeInventario.consultaExistenciaUnidades(tipoProducto , articulosCaja , marca , material);
       if(filtro != null){
           filtro.clear();
       }
       filtro = consultaExistencia;
       calcularTotalesFiltro();
       new funciones().setMsj(1, "CONSULTA REALIZADA CON EXITO");
   }
   
    
   
   
   /*
    * FUNCION para mostrar columnas de muestra y defectuosos
    */
   public boolean seleccionado(String item){
       boolean r = false;
       if(mostrar != null){
           if(!mostrar.isEmpty()){
               for(int i=0; i< mostrar.size();i++){
                   if(mostrar.get(i).equals(item)){
                       r = true;
                   }
               }
           }
       }
       
       return r;
   }
   
   
   
   /*
    * FUNCIONES PARA CONSULTAS AL INVENTARIO POR CAJAS
    */
   public void prepararConsultaExistenciasCajas(){
       consultaExistencia = ejbFacadeInventario.consultaExistenciaCajas(1 ,1);
       if(filtro != null){
           filtro.clear();
       }
       filtro = consultaExistencia;
       tipoProducto = 1;
       consultarCaja = 1;
       calcularTotalesFiltroConsultaCajas();
  }
   
    /*
    * FUNCION que consulta con los filtros ingresados
    */
   
   public void consultarFiltroConsultaCajas(){
       consultaExistencia = ejbFacadeInventario.consultaExistenciaCajas(tipoProducto, consultarCaja);
       if(filtro != null){
           filtro.clear();
       }
       filtro = consultaExistencia;
       calcularTotalesFiltroConsultaCajas();
       new funciones().setMsj(1, "CONSULTA REALIZADA CON EXITO");
   }
   
    /*
    * Funcion que se ejecuta cada vez que se filtra la tabla
    */
   public void onFilterConsultaCajas(){
        calcularTotalesFiltroConsultaCajas();
   }
   
   //Calcula los totales del filtro
   public void calcularTotalesFiltroConsultaCajas(){
       resetTotalesConsultaCajas();
       if(filtro != null){
           for(InventarioConsulta obj : filtro){
               Object[] actual = obj.getColumna();
               existenciasCajas = existenciasCajas + Integer.parseInt(actual[4].toString());
               if(Boolean.valueOf(actual[5].toString())){
                   cajasCompletas = cajasCompletas + Integer.parseInt(actual[4].toString());
               }else{
                   cajasIncompletas = cajasIncompletas + Integer.parseInt(actual[4].toString());
               }
           }
       }
   }
   
   //Resetea totales
   public void resetTotalesConsultaCajas(){
       existenciasCajas = 0;
       cajasCompletas = 0;
       cajasIncompletas = 0;
   }
   
   
   /*
    * FUNCIONES PARA INVENTARIO FISICO
    */
   
   //UNIDADES
   public void prepararInventarioFisicoUnidad(){
       tipoProducto = 1;
       articulosCaja = 0;
        unidadesFisico = ejbFacadeInventario.unidadesFisicas(tipoProducto, articulosCaja);
        if(unidadesFisico != null){
            numUnidadesFisicas = 0;
            numUnidadesFisicas = unidadesFisico.size();
            filtro = null;
        }
   }
   
   public void onFilterUnidadesFisicas(){
       numUnidadesFisicas = 0;
        if(filtro != null){
            numUnidadesFisicas = filtro.size();
        }
   }
   
   public void consultarFiltroUnidadesFisico(){
       unidadesFisico = ejbFacadeInventario.unidadesFisicas(tipoProducto,articulosCaja);
        if(unidadesFisico != null){
            numUnidadesFisicas = 0;
            numUnidadesFisicas = unidadesFisico.size();
            filtro = null;
        }
       new funciones().setMsj(1, "CONSULTA REALIZADA CON EXITO");
   }
   
   
   
   
   
   
   
   //CAJAS
      
   public void prepararConsultaCajasFisicas(){
       cajasFisico = ejbFacadeCaja.consultaCajasFisicas();
       filtroCajasFisico = cajasFisico;
   }
      
    public void onFilterCajasFisicas(){
       numCajasFisicas = 0;
        if(filtroCajasFisico != null){
            numUnidadesFisicas = filtroCajasFisico.size();
        }
   }
         
    public void buscarCaja(Object idCaja){
        cajaFisica = ejbFacadeCaja.find(idCaja);
    }
   
     
    
    
    
    //Editar Unidad
    /*
    * FUNCIONES PARA EDITAR UNA UNIDAD
    */
   public void editarInfoUnidad(){
       if(articuloFisico.getEstadoproducto().equals("EN EXISTENCIA")){
           editarArticuloFisico = true;
       }else if(articuloFisico.getEstadoproducto().equals("DEFECTUOSO")){
           editarArticuloFisico = true;
       }else{
           editarArticuloFisico = false;
           new funciones().setMsj(2,"NO SE PUDE EDITAR ARTICULO");
       }
   }
   
    public void cancelarEditarInfoUnidad(){
       editarArticuloFisico = false;
   }
   /*
    * FUNCION que guarda los cambios en una  unidad funcion llmada desde unidades fisicas
    */
   public void guardarCambiosUnidad(){
       RequestContext context = RequestContext.getCurrentInstance(); 
       try{
            articuloFisico.setColor(articuloFisico.getColor().toUpperCase());
            articuloFisico.setMarca(articuloFisico.getMarca().toUpperCase());
            if(articuloFisico.getMaterial() != null){
                articuloFisico.setMaterial(articuloFisico.getMaterial().toUpperCase());
            }
            ejbFacadeInventario.edit(articuloFisico);
            //Actualizar Informacion de la caja
            if(articuloFisico.getNumcaja() != null){
                ejbFacadeCaja.actualizarCajaCompleta(articuloFisico.getNumcaja());
            }
            new funciones().setMsj(1,"Se ha actualizado la informacion del producto exitosamente");
            context.addCallbackParam("validar",true);
            editarArticuloFisico = false;
       }catch(Exception e){
           new funciones().setMsj(4,"No se pudo actualizar el Articulo");
           context.addCallbackParam("validar",false);
       }
   }
   
   
   
   /*
    * FUNCION que guarda los cambios en una  unidad de una caja funcion llmada desde  cajas fisicas
    */
   public void guardarCambiosUnidadCaja(){
       RequestContext context = RequestContext.getCurrentInstance(); 
       try{
            articuloFisico.setColor(articuloFisico.getColor().toUpperCase());
            articuloFisico.setMarca(articuloFisico.getMarca().toUpperCase());
            if(articuloFisico.getMaterial() != null){
                articuloFisico.setMaterial(articuloFisico.getMaterial().toUpperCase());
            }
            ejbFacadeInventario.edit(articuloFisico);
            //Actualizar Informacion de la caja
            ejbFacadeCaja.actualizarCajaCompleta(articuloFisico.getNumcaja());
            //Actualiza unidades actuales y comnpleta
            actual.getColumna()[7] = articuloFisico.getNumcaja().getCompleta();
            actual.getColumna()[8] = ejbFacadeInventario.countExistenciasCaja(articuloFisico.getNumcaja(), articuloFisico.getNumcaja().getProducto().getProductoPK().getTipoProducto());
            new funciones().setMsj(1,"Se ha actualizado la informacion del producto exitosamente");
            context.addCallbackParam("validar",true);
            editarArticuloFisico = false;
       }catch(Exception e){
           new funciones().setMsj(4,"No se pudo actualizar el Articulo");
           context.addCallbackParam("validar",false);
       }
   }
   
   
   /*
    * FUNCION que guarda los cambios en una  unidad de una caja funcion llmada desde  cajas fisicas
    */
   public void cambiarUbicacionCaja(){
       RequestContext context = RequestContext.getCurrentInstance(); 
       try{
            ejbFacadeCaja.edit(cajaFisica);
            //Actualiza ubicacion tabla renderizada
            actual.getColumna()[6] = cajaFisica.getUbicacionCaja();
            new funciones().setMsj(1,"Se ha actualizado la Ubicacion en Bodega de la Caja");
            context.addCallbackParam("validar",true);
       }catch(Exception e){
           new funciones().setMsj(4,"No se pudo actualizar la ubicacion de la Caja");
           context.addCallbackParam("validar",false);
       }
   }

   
   
    //VARIABLES
    private int numCajasAnteriores;
   
    private List<InventarioConsulta> cajasAnterior;
    private List<InventarioConsulta> filtroCajasAnterior;
   
    public int getNumCajasAnteriores() {
        return numCajasAnteriores;
    }

    
    public void setNumCajasAnteriores(int numCajasAnteriores) {
        this.numCajasAnteriores = numCajasAnteriores;
    }

    public List<InventarioConsulta> getCajasAnterior() {
        return cajasAnterior;
    }

    public void setCajasAnterior(List<InventarioConsulta> cajasAnterior) {
        this.cajasAnterior = cajasAnterior;
    }

    public List<InventarioConsulta> getFiltroCajasAnterior() {
        return filtroCajasAnterior;
    }

    public void setFiltroCajasAnterior(List<InventarioConsulta> filtroCajasAnterior) {
        this.filtroCajasAnterior = filtroCajasAnterior;
    }
   
    
     
   
   //ACCESO A LAS COPIAS
   public void prepararConsultaCajasAnteriores(){
       cajasAnterior = ejbFacadeCaja.consultaCajasFisicasAnteriores();
       filtroCajasAnterior = cajasAnterior;
   }
   
   
   
   
   
   
   
}
