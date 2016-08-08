package com.ddicark.controladores;

import com.ddicark.entidades.DetallePedido;
import com.ddicark.jpaFacades.DetallePedidoFacade;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name = "detallePedidoController")
@SessionScoped
public class DetallePedidoController extends AbstractController<DetallePedido> implements Serializable {

    @EJB
    private DetallePedidoFacade ejbFacadeDetallePedido;
    
     public DetallePedidoController() {
        super(DetallePedido.class);
    }
    //Inicializa controlador de persistencia de proveedores
    @PostConstruct
    public void init() {
        super.setFacade(ejbFacadeDetallePedido);
    }
    
    
    
    
    
    //FUNCIONES

    //VARIABLES DE CONTROL
    //GETTERS AND SETTERS
    public DetallePedidoFacade getEjbFacadeDetallePedido() {
        return ejbFacadeDetallePedido;
    }

    public void setEjbFacadeDetallePedido(DetallePedidoFacade ejbFacadeDetallePedido) {
        this.ejbFacadeDetallePedido = ejbFacadeDetallePedido;
    }
    
    
    
    
    
}
