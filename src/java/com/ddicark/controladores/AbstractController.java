package com.ddicark.controladores;

import com.ddicark.auxiliares.JsfUtil;
import com.ddicark.auxiliares.funciones;
import com.ddicark.jpaFacades.AbstractFacade;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.event.ActionEvent;

import javax.ejb.EJBException;

/**
 * Representa una clase abstracta para ser utilizado con JSF controlador y se utiliza en:
 * Aplicaciones habilitadas para AJAX. No hay salidas y los resultados se generan a partir de sus métodos
 * Puesto que la manipulación está diseñado para ser hecho dentro de una página.
 */
public abstract class AbstractController<T> {

    private AbstractFacade<T> ejbFacade; //Controlador encargado de la persistencia de los datos
    private Class<T> itemClass;          //Es representa una entidad
    private T selected;                  //Es la entidad actualmente seleccionada
    private List<T> items;               //Lista de todas las entidades

    private enum PersistAction {         //Acciones de persistencia
        CREATE,
        DELETE,
        UPDATE
    }
    //Constructor sin parametros
    public AbstractController() {
    }
    //Constructor con parametros
    public AbstractController(Class<T> itemClass) {
        this.itemClass = itemClass;
    }
    
    //Recupera el controlador de persistencia
    protected AbstractFacade<T> getFacade() {
        return ejbFacade;
    }
    //Setea el controlador de persistencia
    protected void setFacade(AbstractFacade<T> ejbFacade) {
        this.ejbFacade = ejbFacade;
    }
    //Obtiene la entidad actualmente seleccionada
    public T getSelected() {
        return selected;
    }
    //Cambia la entidad seleccionada
    public void setSelected(T selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
        // Nothing to do if entity does not have any embeddable key.
    }

    ;

    protected void initializeEmbeddableKey() {
        // Nothing to do if entity does not have any embeddable key.
    }

   
    /**
     * Retorna la lista completa de las entidades
     *
     * @return
     */
    public List<T> getItems() {
       // if (items == null) {
        items = ejbFacade.findAll();
       // }
        return items;
    }
    
    /**
     * Crea una nueva instancia de una entidad subyacente y lo asigna a Selected (Entidad Seleccionada)
     * 
     *
     * @return
     */
    public void prepararCrear(ActionEvent event) {
        T newItem;
        try {
            newItem = itemClass.newInstance();
            this.selected = newItem;
            initializeEmbeddableKey();
        } catch (InstantiationException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    //Guarda los cambios hechos en una entidad
    public void save(ActionEvent event) {
        String msg = itemClass.getSimpleName() + " fue actualizado exitosamente";
        persist(PersistAction.UPDATE, msg);
    }
    
    //Crea una nueva entidad
    public void saveNew(ActionEvent event) {
        String msg = itemClass.getSimpleName() + " se creo satisfactoriamente";
        persist(PersistAction.CREATE, msg);
        if (!isValidationFailed()) {
            items = null; //Invalida la lista para generarla de nuevo y asi actualizarla
        }
    }
    //Elimina una entidad
    public void delete(ActionEvent event) {
        String msg = itemClass.getSimpleName() + " fue eliminado de la lista";
        persist(PersistAction.DELETE, msg);
        if (!isValidationFailed()) {
            selected = null; // Remueve la seleccion eliminada
            items = null; // Invalida la lista para actualizarla.
        }
    }

    // Funcion encargada de persistir en la Base de Datos
    private void persist(PersistAction persistAction, String successMessage) {
        
        if (selected != null) { //Verifica que haya una seleccion
            this.setEmbeddableKeys();//Solo se ejecuta si es de llave compuesta
            try {
                if(persistAction == PersistAction.CREATE){
                    this.ejbFacade.create(selected);
                }else if(persistAction == PersistAction.UPDATE){
                    this.ejbFacade.edit(selected);
                }else if(persistAction == PersistAction.DELETE){
                    this.ejbFacade.remove(selected);
                }else{
                    new funciones().setMsj(3,"NO SE ESPECIFICO PersistAction VALIDO");
                }
                new funciones().setMsj(1,successMessage);
                
            } catch (EJBException ex) { //Manejo de error en PERSISTENCIA
                    new funciones().setMsj(4,"ERROR DE CONEXION A LA BASE DE DATOS O DE PERSISTENCIA\n DETALLE: " + ex.getMessage());
            } catch (Exception ex) {
               Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                new funciones().setMsj(4,"ERROR DE EXCEPCION\n DETALLE: " + ex.getMessage());
            }
        }
    }

    
    //Verifica si la validacion fallo en un contexto
    public boolean isValidationFailed() {
        return JsfUtil.isValidationFailed();
    }
}