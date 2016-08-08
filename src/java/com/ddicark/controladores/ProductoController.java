package com.ddicark.controladores;

import com.ddicark.entidades.Producto;
import com.ddicark.auxiliares.JsfUtil;
import com.ddicark.auxiliares.funciones;
import com.ddicark.entidades.ProductoPK;
import com.ddicark.jpaFacades.ProductoFacade;
import com.lowagie.text.BadElementException;
import com.lowagie.text.Image;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.io.Serializable;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
        

@ManagedBean(name = "productoController")
@SessionScoped
public class ProductoController extends AbstractController<Producto> implements Serializable {
    
    @EJB
    private ProductoFacade ejbFacadeProducto;

    public ProductoController() {
      super(Producto.class);
    }
    //Inicializa controlador de persistencia de proveedores
    @PostConstruct
    public void init() {
        super.setFacade(ejbFacadeProducto);
    }
    
    @Override
    protected void setEmbeddableKeys() {
        //this.getSelected().getProductoPK().setCodestilo(this.getSelected().getProductoPK().getCodestilo());
        //this.getSelected().getProductoPK().setTipoProducto(this.getSelected().getProductoPK().getTipoProducto());
    }

    @Override
    protected void initializeEmbeddableKey() {
        this.getSelected().setProductoPK(new com.ddicark.entidades.ProductoPK());
    }

    //Variables Funcionales del Bean
    private File imageTmp;
    private String urlImgTmp;
    private List<Producto> estilos;
    private List<Producto> catalogo;
    private List<Producto> filtroCatalogo;
    
    //Variables Catalogo
    private int tipoProducto;
    private int tipoAnterior=0;
    private Producto estiloEditar;
    private String filtroValue;
    
    
    //GETTERS AND SETTERS
    public File getImageTmp() {
        return imageTmp;
    }

    public String getUrlImgTmp() {
        if(urlImgTmp == null){
            urlImgTmp = "#";
        }
        return urlImgTmp;
    }

    public void setUrlImgTmp(String urlImgTmp) {
        this.urlImgTmp = urlImgTmp;
    }

    public List<Producto> getFiltroCatalogo() {
        return filtroCatalogo;
    }

    public void setFiltroCatalogo(List<Producto> filtroCatalogo) {
        this.filtroCatalogo = filtroCatalogo;
    }

    public void resetFiltroCatalogo() {
        this.filtroCatalogo = catalogo;
    }
    

    public void actualizarCatalogo() {
        filtroCatalogo = null;     
        catalogo = ejbFacadeProducto.getEstilosTipoProducto(tipoProducto);
        filtroCatalogo = catalogo;
    }

    public List<Producto> getCatalogo() {
        return catalogo;
    }

    
    public List<Producto> getEstilos(int tipoProducto) {
        this.estilos = ejbFacadeProducto.getEstilosTipoProducto(tipoProducto);
        return estilos;
    }

    public int getTipoProducto() {
        return tipoProducto;
    }

    public void setTipoProducto(int tipoProducto) {
        this.tipoProducto = tipoProducto;
    }

    public Producto getEstiloEditar() {
        return estiloEditar;
    }

    public void setEstiloEditar(Producto estiloEditar) {
        this.estiloEditar = estiloEditar;
    }

    public String getFiltroValue() {
        return filtroValue;
    }

    public void setFiltroValue(String filtroValue) {
        this.filtroValue = filtroValue;
    }
    
    
    /*
     * PREpARA EL CATALOGO
     */
    public void prepararCatalogo(){
        actualizarCatalogo();
        filtroCatalogo = catalogo;
    }
    /*
     * Funcion que prepara el entorno para crear un nuevo estilo
     */
    
    public void nuevoEstilo(ActionEvent event){
        super.setSelected(null);
        if(!(this.imageTmp == null)){
            if(this.imageTmp.exists() ){
                this.imageTmp.delete();
            }
            this.imageTmp = null;
        }
        super.prepararCrear(event);
    }
    
    // Cancelar crear estilo y eliminar la imagen tmp si lo huviere
    public void cancelarEstilo(){
        if(!(this.imageTmp == null)){
            if(this.imageTmp.exists() ){
                this.imageTmp.delete();
            }
            this.imageTmp = null;
        }
    }
    
    /*
     * FUNCION QUE CREA UN NUEVO ESTILO
     */
    
    public void guardarEstilo(int tipo){
        boolean valido= false;
        RequestContext context = RequestContext.getCurrentInstance(); 
        int tipoProducto = 0; 
        //
        tipoProducto = tipo;
        
        if(!(tipoProducto == 0)){
            this.getSelected().getProductoPK().setTipoProducto(tipoProducto);
            //Mayuscula el codestilo y nombre
            this.getSelected().getProductoPK().setCodestilo(this.getSelected().getProductoPK().getCodestilo().toUpperCase());
            Producto existe = this.ejbFacadeProducto.find(this.getSelected().getProductoPK()); 
            //Verifica que no exista el codigo
            if(existe == null){
                if(this.getSelected().getNombreEstilo() != null){
                    this.getSelected().setNombreEstilo(this.getSelected().getNombreEstilo().toUpperCase());
                }
                if(this.getSelected().getDetalleEstilo() != null){
                    this.getSelected().setDetalleEstilo(this.getSelected().getDetalleEstilo().toUpperCase());
                }
                //Renombra la imagen con el codigo del producto
                this.getSelected().setPathPicture(renombrarImgTmp(tipoProducto,this.getSelected().getProductoPK().getCodestilo()));
                this.getSelected().setPrecioventaMayoreo(BigDecimal.ZERO);
                this.getSelected().setPrecioventaUnidad(BigDecimal.ZERO);
                this.ejbFacadeProducto.create(this.getSelected());
                actualizarCatalogo();
                valido = true;
                try{
                    if(new JsfUtil().getPreingresoController() != null){
                        new JsfUtil().getPreingresoController().setEstilo(this.getSelected());
                    }
                }catch(Exception ex){
                    Logger.getLogger(ProductoController.class.getName()).log(Level.SEVERE, null, ex);
                }
                new funciones().setMsj(1, "Estilo Agregado Exitosamente");
            }else{
                new funciones().setMsj(3, "CODIGO Estilo ya existe");
            }
        }else{
            new funciones().setMsj(3, "Tipo de Producto NO EXISTE");
        }
        context.addCallbackParam("validar",valido);
    }
     
    /* FUNCIONES QUE GESTIONA Y ALMACENA ARCHIVO EN SERVIDOR
     * SUBIDO POR FILEUPLOAD
     */
    
    //Metodo para subir imagen al servidor de forma TMP mientras se finaliza la transaccion
    public void subirImagen(FileUploadEvent event){
        if(!(this.imageTmp == null)){
            if(this.imageTmp.exists() ){
                this.imageTmp.delete();
            }
            this.imageTmp = null;
        }
        FacesMessage mensaje = new FacesMessage();
        try{
            this.urlImgTmp = guardarFicheroTemporal(event.getFile().getContents(), event.getFile().getFileName());
            this.getSelected().setTipoMime(event.getFile().getContentType());
            mensaje.setSeverity(FacesMessage.SEVERITY_INFO);
            mensaje.setSummary("Imagen subida con exito");
            
        }catch(Exception e){
            mensaje.setSeverity(FacesMessage.SEVERITY_ERROR);
            mensaje.setSummary("Error al subir imagen : \nDetalles: "+ e.getMessage());
        }
        
        FacesContext.getCurrentInstance().addMessage("Mensaje",mensaje);
    }
    
    /*
     * Funcion que guarda el archivo temporal en una carpeta del disco local c
     * Especificamente en C:\ddicark-estilos\calzado\
     *  y devuelve la direccion absoluta del archivo en el servidor
     *             Entradas:
     *                  - bytes : imagen en bytes
     *                  - nombreArchivo : nombre del archivo con extencion
     *             Salidas:
     *                  -ubicacionImagen : Ruta del archivo temporal para que lo pueda ubicar el componente
     *                                      graphicImage 
     */
     public String guardarFicheroTemporal(byte[] bytes, String nombreArchivo){
        String ubicacionImagen = null;
        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        String path = servletContext.getRealPath("") + File.separatorChar 
                + "resources" + File.separatorChar + "catalogo" + File.separatorChar
                + "tmp" + File.separatorChar + nombreArchivo;
        
        InputStream in = null;
        
        try{
            this.imageTmp = new File(path);
            in = new ByteArrayInputStream(bytes);
            //this.imageTmp = new DefaultStreamedContent(in,"image/gif");
            FileOutputStream out = new FileOutputStream(imageTmp.getAbsolutePath());
            int c = 0;
            while ((c = in.read()) >= 0){
                out.write(c);
            }
            out.flush();
            out.close();
            ubicacionImagen = nombreArchivo;
            
        }catch(Exception e){
            System.err.println("No se pudo cargar la imagen");
        }
        
        return ubicacionImagen;
    }
    
    /*
     * Funcion que mueve y renombra el archivo de imagen Temporal y la guarda en la carpeta correspondiente
     * y setea el path relativo que se guardara en la BD
     */
    public String renombrarImgTmp(int tipoProducto, String codigo){
        String urlRelativa="#";
        //Verifica si hay algun archivo
        if(!(imageTmp == null)){
            String ext;
            ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
            int posicionInicialExt=0; // Posicion de la cadena donde inicia la extension
            for(int i=imageTmp.getName().length()-1;i>=0;i--){
                if(imageTmp.getName().charAt(i) == '.'){
                    posicionInicialExt = i;
                    break;
                }
            }
            if(!(posicionInicialExt == 0)){
                ext = imageTmp.getName().substring(posicionInicialExt);
                String path = servletContext.getRealPath("") + File.separatorChar 
                + "resources" + File.separatorChar + "catalogo" + File.separatorChar;
                switch(tipoProducto){
                    case 1:
                        path = path + "calzado" + File.separatorChar + codigo+ext;
                        urlRelativa = "/resources/catalogo/calzado/" + codigo+ext;
                        break;
                    case 2:
                        path = path + "carteras" + File.separatorChar + codigo+ext;
                        urlRelativa = "/resources/catalogo/carteras/" + codigo+ext;
                        break;
                    case 3:
                        path = path + "ropa" + File.separatorChar + codigo+ext;
                        urlRelativa = "/resources/catalogo/ropa/" + codigo+ext;
                        break;
                }
                File renameMove = new File(path);
                imageTmp.renameTo(renameMove);
                urlImgTmp = "#";
            }
            
        }
        return urlRelativa;
    }
    
    /*
     * Funcion Elimina archivo
     */
    public boolean eliminarArchivo(int tipoProducto, String codigo){
        boolean eliminado = false;
        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        if(!this.getSelected().getPathPicture().equals("#")){
            String ext = "";
            int posicionInicialExt=0; // Posicion de la cadena donde inicia la extension
            for(int i=this.getSelected().getPathPicture().length()-1;i>=0;i--){
                if(this.getSelected().getPathPicture().charAt(i) == '.'){
                    posicionInicialExt = i;
                    break;
                }
            }
            if(!(posicionInicialExt == 0)){
                ext = this.getSelected().getPathPicture().substring(posicionInicialExt);
            }
            String path = servletContext.getRealPath("") + File.separatorChar 
                    + "resources" + File.separatorChar + "catalogo" + File.separatorChar;
            switch(tipoProducto){
                case 1:
                    path = path + "calzado" + File.separatorChar + codigo+ext;
                    break;
                case 2:
                    path = path + "carteras" + File.separatorChar + codigo+ext;
                    break;
                case 3:
                    path = path + "ropa" + File.separatorChar + codigo+ext;
                    break;
            }
            File imageEliminar = new File(path);
            if(imageEliminar.delete()){
                //archivo Eliminado
                eliminado = true;
            }
        }
        return eliminado;
    }
   
   /*
    * FUNCIONES CATALOGO
    */

    public void prepararEditar(Producto estilo){
        if(!(this.imageTmp == null)){
            if(this.imageTmp.exists() ){
                this.imageTmp.delete();
            }
            this.imageTmp = null;
        }
        urlImgTmp = null;
        super.setSelected(estilo);
    }
    
    /*
     * FUNCION QUE GUARDA LOS CAMBIOS REALIZADOS EN EDITAR ESTILO DEL CATALOGO
     */
    public void editarEstilo(){
        boolean valido= false;
        RequestContext context = RequestContext.getCurrentInstance(); 
        try{
            
            //Mayuscula el codestilo y nombre
            if(this.getSelected().getNombreEstilo() != null){
                this.getSelected().setNombreEstilo(this.getSelected().getNombreEstilo().toUpperCase());
            }
            if(this.getSelected().getDetalleEstilo() != null){
                this.getSelected().setDetalleEstilo(this.getSelected().getDetalleEstilo().toUpperCase());
            }
            //Renombra la imagen con el codigo del producto
            if(urlImgTmp != null){
                eliminarArchivo(this.getSelected().getProductoPK().getTipoProducto(),this.getSelected().getProductoPK().getCodestilo());
                this.getSelected().setPathPicture(renombrarImgTmp(this.getSelected().getProductoPK().getTipoProducto(),this.getSelected().getProductoPK().getCodestilo()));
            }
            
            ejbFacadeProducto.edit(this.getSelected());
            valido = true;
            new funciones().setMsj(1, "Estilo ACTUALIZADO Exitosamente");
            //context.execute("alert('ESTILO ACTUALIZADO');");
            context.addCallbackParam("validar",valido);
            actualizarCatalogo();
            //new funciones().irA("faces/vistas/estilos/catalogo.xhtml");
        }catch(Exception e){
            new funciones().setMsj(3, "NO SE PUDO ACTUALIZAR EL ESTILO");
            context.addCallbackParam("validar",valido);
        }
    }
    
    
    
    /*
     * FUNCIONES CONTROL DE PRECIOS
     */
    public void validarPreciosYConfirmar(){
        RequestContext context = RequestContext.getCurrentInstance(); 
        if(validarPrecios(this.getSelected())){
            context.addCallbackParam("mostrar",true);
        }else{
            context.addCallbackParam("mostrar",false);
        }
    }
    
    public void cambiarPrecios(){
        try{
            //Mayuscula el codestilo y nombre
            if(this.getSelected().getNombreEstilo() != null){
                this.getSelected().setNombreEstilo(this.getSelected().getNombreEstilo().toUpperCase());
            }
            if(this.getSelected().getDetalleEstilo() != null){
                this.getSelected().setDetalleEstilo(this.getSelected().getDetalleEstilo().toUpperCase());
            }
            //Guardar cambios en la BD
            ejbFacadeProducto.edit(this.getSelected());
            new funciones().setMsj(1, "PRECIOS DEL ESTILO : " + this.getSelected().getProductoPK().getCodestilo()+" ACTUALIZADOS Exitosamente");
            
            //new funciones().irA("faces/vistas/configuraciones/precios.xhtml");
            
        }catch(Exception e){
            new funciones().setMsj(3, "NO SE PUDO ACTUALIZAR PRECIOS");
        }
    }
    
    //FUNCION que valida precios
    public boolean validarPrecios(Producto precios){
        boolean validar = true;
        //Verificar PrecioMayoreo
        if(precios.getPrecioventaMayoreo() != null && precios.getPrecioventaUnidad() != null){
            if(precios.getPrecioventaMayoreo().compareTo(BigDecimal.ZERO) == -1){
                //precio mayoreo negativo
                validar = false;
                new funciones().setMsj(3,"PRECIO MAYOREO debe ser > = 0");
            }
            if(precios.getPrecioventaUnidad().compareTo(BigDecimal.ZERO) == -1){
                //precio detalle negativo
                validar = false;
                new funciones().setMsj(3,"PRECIO UNIDAD debe ser > = 0");
            }
            if(precios.getPrecioventaUnidad().compareTo(precios.getPrecioventaMayoreo()) == -1){
                //precio detalle negativo
                validar = false;
                new funciones().setMsj(3,"PRECIO DETALLE debe ser >= PRECIO MAYOREO");
            }
        }else{
            validar = false;
            new funciones().setMsj(3,"PRECIOS NO PUEDEN SER NULOS");
        }
        
        return validar;
    }
    
    public void resetFiltro(){
        this.filtroCatalogo = null;
    }
    
}
