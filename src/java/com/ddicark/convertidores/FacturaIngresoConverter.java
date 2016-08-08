/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ddicark.convertidores;

import com.ddicark.auxiliares.JsfUtil;
import com.ddicark.controladores.FacturaIngresoController;
import com.ddicark.entidades.FacturaIngreso;
import com.ddicark.jpaFacades.FacturaIngresoFacade;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

@ManagedBean
public class FacturaIngresoConverter implements Converter {

    @EJB
    private FacturaIngresoFacade ejbFacade;
    private static final String SEPARATOR = "#";
    private static final String SEPARATOR_ESCAPED = "\\#";

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
        if (value == null || value.length() == 0 || JsfUtil.isDummySelectItem(component, value)) {
            return null;
        }
        return this.ejbFacade.find(getKey(value));
    }

    com.ddicark.entidades.FacturaIngresoPK getKey(String value) {
        com.ddicark.entidades.FacturaIngresoPK key;
        String values[] = value.split(SEPARATOR_ESCAPED);
        key = new com.ddicark.entidades.FacturaIngresoPK();
        key.setDocumentoCompra(values[0]);
        DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
        try {
            key.setFechaDocumento(dateFormat.parse(values[1]));
        } catch (ParseException ex) {
            Logger.getLogger(FacturaIngresoConverter.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return key;
    }

    String getStringKey(com.ddicark.entidades.FacturaIngresoPK value) {
        StringBuffer sb = new StringBuffer();
        sb.append(value.getDocumentoCompra());
        sb.append(SEPARATOR);
        sb.append(value.getFechaDocumento());
        return sb.toString();
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
        if (object == null
                || (object instanceof String && ((String) object).length() == 0)) {
            return null;
        }
        if (object instanceof FacturaIngreso) {
            FacturaIngreso o = (FacturaIngreso) object;
            return getStringKey(o.getFacturaIngresoPK());
        } else {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), FacturaIngreso.class.getName()});
            return null;
        }
    }
}
