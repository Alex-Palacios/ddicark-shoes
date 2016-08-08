package com.ddicark.controladores;

import com.ddicark.auxiliares.funciones;
import com.ddicark.entidades.Permisos;
import com.ddicark.jpaFacades.PermisosFacade;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

@ManagedBean(name = "permisosController")
@SessionScoped
public class PermisosController extends AbstractController<Permisos>  implements Serializable {

   
    @EJB
    private PermisosFacade ejbFacadePermisos;
    
    
    
    public PermisosController() {
        super(Permisos.class);
    }
    //Inicializa controlador de persistencia de proveedores
    @PostConstruct
    public void init() {
        super.setFacade(ejbFacadePermisos);
    }


    //Variables de control
    private List<Permisos> permisosUsuarios;
    
    
    private String[] menus;
    private SelectItem[] menusItems;
    private String[] submenus;
    private SelectItem[] submenusItems;
    private String menuActual;
    private List<Permisos> filtro;

    public SelectItem[] getMenusItems() {
        List<String> menuList = ejbFacadePermisos.getMenuList();
        if(menuList != null){
            menus = new String[menuList.size()];
            for(int i=0; i < menuList.size(); i++){
                menus[i] = menuList.get(i);
            }
        }
        menusItems = new funciones().createFilterOptions(menus);
        return menusItems;
    }

    public SelectItem[] getSubmenusItems() {
        List<String> submenuList = ejbFacadePermisos.getSubmenuList();
        if(submenuList != null){
            submenus = new String[submenuList.size()];
            for(int i=0; i < submenuList.size(); i++){
                submenus[i] = submenuList.get(i);
            }
        }
        submenusItems = new funciones().createFilterOptions(submenus);
        return submenusItems;
    }

    public String getMenuActual() {
        return menuActual;
    }

    public void setMenuActual(String menuActual) {
        this.menuActual = menuActual;
    }

    public List<Permisos> getFiltro() {
        return filtro;
    }

    public void setFiltro(List<Permisos> filtro) {
        this.filtro = filtro;
    }

    public List<Permisos> getPermisosUsuarios() {
        permisosUsuarios = ejbFacadePermisos.getPermisosUsuarios();
        return permisosUsuarios;
    }

    
    
    
    
}
