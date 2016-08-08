package com.ddicark.controladores;

import com.ddicark.auxiliares.JsfUtil;
import com.ddicark.auxiliares.funciones;
import com.ddicark.entidades.Proveedor;
import com.ddicark.jpaFacades.ProveedorFacade;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import org.primefaces.context.RequestContext;

@ManagedBean(name = "proveedorController")
@SessionScoped
public class ProveedorController extends AbstractController<Proveedor> implements Serializable {

    
    @EJB
    private ProveedorFacade ejbFacadeProveedor; //Para consultas propias de la tabla Proveedor
    
    //Variables de contro
    private List<Proveedor> itemsProveedoresNacionales;
    private List<Proveedor> itemsProveedoresExtranjeros;
    //CONSTRUCTOR
    public ProveedorController() {
        super(Proveedor.class);
    }
    //Inicializa controlador de persistencia de proveedores
    @PostConstruct
    public void init() {
        super.setFacade(ejbFacadeProveedor);
    }

    //GETERS AND SETTERS

    //RECUPERA LOS POVEEDORES NACIONALES
    public List<Proveedor> getItemsProveedoresNacionales() {
        itemsProveedoresNacionales = this.ejbFacadeProveedor.getProveedores(true);
        return itemsProveedoresNacionales;
    }

    //RECUPERA LOS POVEEDORES EXTRANJEROS
    public List<Proveedor> getItemsProveedoresExtranjeros() {
        itemsProveedoresExtranjeros = this.ejbFacadeProveedor.getProveedores(false);
        return itemsProveedoresExtranjeros;
    }

   
    
    
    
    
    
    //FUNCIONES
    
    
    /*FUNCION GUARDAR PROVEEDOR
     * Verifica si el proveedor ya existe y sino lo crea
     */
    
    public void guardar(ActionEvent e) {
        RequestContext context = RequestContext.getCurrentInstance();
        boolean validar = false;
        List<Proveedor> R1 = ejbFacadeProveedor.existeProveedor(super.getSelected().getNombreProveedor().toUpperCase());
        if(R1.isEmpty()){
              upperSelected();//Convertir los string del selected a mayusculas
              super.getSelected().setIdproveedor(super.getFacade().count() + 1);
              super.saveNew(e);
              validar = true;
        }else{
            new funciones().setMsj(2,"EL PROVEEDOR YA EXISTE");
            validar = false;
        }
        context.addCallbackParam("validar",validar);
    }
    
    
    public void Actualizar(ActionEvent e){
        RequestContext context = RequestContext.getCurrentInstance();
        boolean validar = false;
        String nameAntes = ejbFacadeProveedor.find(this.getSelected().getIdproveedor()).getNombreProveedor();
        if(nameAntes.equals(this.getSelected().getNombreProveedor().toUpperCase())){
            upperSelected();//Convertir los string del selected a mayusculas
            super.save(e);
            validar = true;
        }else{
            List<Proveedor> R1 = ejbFacadeProveedor.existeProveedor(super.getSelected().getNombreProveedor().toUpperCase());
            if(R1.isEmpty()){
                upperSelected();//Convertir los string del selected a mayusculas
                super.save(e);
                validar = true;
            }else{
                new funciones().setMsj(3,"EL NOMBRE DEL PROVEEDOR YA EXISTE");
            }
        }
        context.addCallbackParam("validar",validar);
    }
    
   /*Funcion que pasa a mayusculas los campos string
    * de los proveedores
    */
    public void upperSelected(){
        super.getSelected().setNombreProveedor(super.getSelected().getNombreProveedor().toUpperCase());
        super.getSelected().setRubroProveedor(super.getSelected().getRubroProveedor().toUpperCase());
        //super.getSelected().setDireccionProveedor(super.getSelected().getDireccionProveedor().toUpperCase());
        super.getSelected().setPaisProveedor(super.getSelected().getPaisProveedor().toUpperCase());
        //super.getSelected().setNombreContacto(super.getSelected().getNombreContacto().toUpperCase());
    }
    
    
    
    
    //OTROS/////////////////////

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacadeProveedor.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacadeProveedor.findAll(), true);
    }

    @FacesConverter(forClass = Proveedor.class)
    public static class IngresosControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ProveedorController controller = (ProveedorController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "proveedoresController");
            return controller.ejbFacadeProveedor.find(value);
        }

        java.lang.String getKey(String value) {
            java.lang.String key;
            key = value;
            return key;
        }

        String getStringKey(java.lang.String value) {
            StringBuffer sb = new StringBuffer();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Proveedor) {
                Proveedor o = (Proveedor) object;
                return getStringKey(o.getIdproveedor().toString());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Proveedor.class.getName());
            }
        }
    }   
    
}
