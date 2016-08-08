/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ddicark.convertidores;

import com.ddicark.auxiliares.JsfUtil;
import com.ddicark.controladores.ProductoController;
import com.ddicark.entidades.Producto;
import com.ddicark.jpaFacades.ProductoFacade;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author DDICARK
 */

@ManagedBean
public class ProductoConverter implements Converter {

        @EJB
        private ProductoFacade ejbFacade;
        private static final String SEPARATOR = "#";
        private static final String SEPARATOR_ESCAPED = "\\#";

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0 || JsfUtil.isDummySelectItem(component, value)) {
                return null;
            }
            return this.ejbFacade.find(getKey(value));
        }

        com.ddicark.entidades.ProductoPK getKey(String value) {
            com.ddicark.entidades.ProductoPK key;
            String values[] = value.split(SEPARATOR_ESCAPED);
            key = new com.ddicark.entidades.ProductoPK();
            key.setTipoProducto(Integer.parseInt(values[0]));
            key.setCodestilo(values[1]);
            return key;
            
        }

        String getStringKey(com.ddicark.entidades.ProductoPK value) {
            StringBuffer sb = new StringBuffer();
            sb.append(value.getTipoProducto());
            sb.append(SEPARATOR);
            sb.append(value.getCodestilo());
            return sb.toString();
        }
        
        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null
                || (object instanceof String && ((String) object).length() == 0)) {
            return null;
            }
            if (object instanceof Producto) {
                Producto o = (Producto) object;
                return getStringKey(o.getProductoPK());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Producto.class.getName()});
                return null;
            }
        }
    }
