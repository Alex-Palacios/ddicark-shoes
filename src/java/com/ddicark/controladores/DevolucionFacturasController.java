package com.ddicark.controladores;

import com.ddicark.entidades.DevolucionFacturas;
import com.ddicark.jpaFacades.DevolucionFacturasFacade;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name = "devolucionFacturasController")
@SessionScoped
public class DevolucionFacturasController extends AbstractController<DevolucionFacturas> implements Serializable {

    
    @EJB
    private DevolucionFacturasFacade ejbFacadeDevolucionFacturas;

     public DevolucionFacturasController() {
        super(DevolucionFacturas.class);
    }
    //Inicializa controlador de persistencia de proveedores
    @PostConstruct
    public void init() {
        super.setFacade(ejbFacadeDevolucionFacturas);
    }
}
