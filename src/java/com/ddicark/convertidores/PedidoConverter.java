/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ddicark.convertidores;

import com.ddicark.auxiliares.JsfUtil;
import com.ddicark.entidades.Pedido;
import com.ddicark.jpaFacades.PedidoFacade;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author DDICARK
 */
@ManagedBean
public class PedidoConverter implements Converter {
   
    @EJB
    private PedidoFacade ejbFacade;
    private static final String SEPARATOR = "#";
    private static final String SEPARATOR_ESCAPED = "\\#";

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
        if (value == null || value.length() == 0 || JsfUtil.isDummySelectItem(component, value)) {
            return null;
        }
        return this.ejbFacade.find(getKey(value));
    }

    com.ddicark.entidades.PedidoPK getKey(String value) {
        com.ddicark.entidades.PedidoPK key;
        String values[] = value.split(SEPARATOR_ESCAPED);
        key = new com.ddicark.entidades.PedidoPK();
        key.setNumpedido(values[0]);
        DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
        try {
            key.setFechaPedido(dateFormat.parse(values[1]));
        } catch (ParseException ex) {
            Logger.getLogger(PedidoConverter.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return key;
    }

    String getStringKey(com.ddicark.entidades.PedidoPK value) {
        StringBuffer sb = new StringBuffer();
        sb.append(value.getNumpedido());
        sb.append(SEPARATOR);
        sb.append(value.getFechaPedido());
        return sb.toString();
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
        if (object == null
                || (object instanceof String && ((String) object).length() == 0)) {
            return null;
        }
        if (object instanceof Pedido) {
            Pedido o = (Pedido) object;
            return getStringKey(o.getPedidoPK());
        } else {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Pedido.class.getName()});
            return null;
        }
    }
}
