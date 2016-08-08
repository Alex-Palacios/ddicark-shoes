package com.ddicark.controladores;

import com.ddicark.entidades.Transaccion;
import com.ddicark.auxiliares.JsfUtil;
import com.ddicark.jpaFacades.TransaccionFacade;

import java.io.Serializable;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;

@ManagedBean(name = "transaccionesController")
@SessionScoped
public class TransaccionController extends AbstractController<Transaccion> implements Serializable {

    @EJB
    private TransaccionFacade ejbFacadeTransaccion;

    public TransaccionController() {
        super(Transaccion.class);
    }
     
    @PostConstruct
    public void init() {
        super.setFacade(ejbFacadeTransaccion);
    }
    
    
}
