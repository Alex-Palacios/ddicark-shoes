package com.ddicark.controladores;

import com.ddicark.auxiliares.JsfUtil;
import com.ddicark.entidades.PagoCompra;
import com.ddicark.jpaFacades.PagoCompraFacade;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.SelectItem;

@ManagedBean(name = "pagoCompraController")
@SessionScoped
public class PagoCompraController extends AbstractController<PagoCompra> implements Serializable {

    
    @EJB
    private PagoCompraFacade ejbFacadePagoCompra;

    public PagoCompraController() {
        super(PagoCompra.class);
    }
    //Inicializa controlador de persistencia de proveedores
    @PostConstruct
    public void init() {
        super.setFacade(ejbFacadePagoCompra);
    }


    
    
    
    
    
}
