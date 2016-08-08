package com.ddicark.controladores;

import com.ddicark.jpaFacades.MunicipiosFacade;

import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name = "municipiosController")
@SessionScoped
public class MunicipiosController implements Serializable {

    @EJB
    MunicipiosFacade ejbFacade;
    

    public MunicipiosController() {
    }

    
}
