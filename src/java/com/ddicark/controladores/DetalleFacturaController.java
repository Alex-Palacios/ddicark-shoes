package com.ddicark.controladores;

import com.ddicark.entidades.DetalleEnvio;
import com.ddicark.entidades.DetalleFactura;
import com.ddicark.jpaFacades.DetalleEnvioFacade;
import com.ddicark.jpaFacades.DetalleFacturaFacade;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name = "detalleFacturaController")
@SessionScoped
public class DetalleFacturaController  extends AbstractController<DetalleFactura>  implements Serializable {

    
    @EJB
    private DetalleFacturaFacade ejbFacadeDetalleFactura;
    
    @EJB
    private DetalleEnvioFacade ejbFacadeDetalleEnvio;
    
    public DetalleFacturaController() {
        super(DetalleFactura.class);
    }

    @PostConstruct
    public void init() {
        super.setFacade(ejbFacadeDetalleFactura);
    }

    //Variables de control
    
    List<DetalleEnvio> detalleLineaVenta;
    
    
    
    //Gettrs and setters
    public List<DetalleEnvio> getDetalleLineaVenta(DetalleFactura df) {
        detalleLineaVenta = ejbFacadeDetalleEnvio.detalleLineaVenta(df);
        return detalleLineaVenta;
    }
    
}
