package com.ddicark.controladores;

import com.ddicark.auxiliares.funciones;
import com.ddicark.entidades.Configuraciones;
import com.ddicark.jpaFacades.ConfiguracionesFacade;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.context.RequestContext;

@ManagedBean(name = "configuracionesController")
@SessionScoped
public class ConfiguracionesController extends AbstractController<Configuraciones> implements Serializable {

    @EJB
    private ConfiguracionesFacade ejbFacadeConfig;

    
     public ConfiguracionesController() {
        super(Configuraciones.class);
    }
    //Inicializa controlador de persistencia de proveedores
    @PostConstruct
    public void init() {
        super.setFacade(ejbFacadeConfig);
    }

    
    //VARIABLES DE CONTROL
    
    //configuraciones de Precio
    private Configuraciones utilidad;
    private String utilidadTexto;
    private float utilidadValor;
    private Configuraciones comisiones;
    private String comisionesTexto;
    private float comisionesValor;
    private Configuraciones descuento;
    private String descuentoTexto;
    private float descuentoValor;
    private Configuraciones detalle;
    private String detalleTexto;
    private float detalleValor;
    
    //Configuraciones de Credito
    private Configuraciones plazo;
    private String plazoTexto;
    private int plazoValor;
    
    //Configuraciones Globales
    private Configuraciones iva;
    private String ivaTexto;
    private float ivaValor;

    //VARIBLES DE CONTROL
    private String simbolo;
    private boolean cambiarIva = false;
    private boolean cambiarUtilidad = false;
    private boolean cambiarComisiones = false;
    private boolean cambiarDescuento = false;
    private boolean cambiarDetalle = false;
    private boolean cambiarPlazo = false;
    
    //GET AND SET
    public Configuraciones getUtilidad() {
        return utilidad;
    }

    public void setUtilidad(Configuraciones utilidad) {
        this.utilidad = utilidad;
    }

    public Configuraciones getComisiones() {
        return comisiones;
    }

    public void setComisiones(Configuraciones comisiones) {
        this.comisiones = comisiones;
    }

    public Configuraciones getDescuento() {
        return descuento;
    }

    public void setDescuento(Configuraciones descuento) {
        this.descuento = descuento;
    }

    public Configuraciones getDetalle() {
        return detalle;
    }

    public void setDetalle(Configuraciones detalle) {
        this.detalle = detalle;
    }

    public Configuraciones getPlazo() {
        return plazo;
    }

    public void setPlazo(Configuraciones plazo) {
        this.plazo = plazo;
    }

    public Configuraciones getIva() {
        return iva;
    }

    public void setIva(Configuraciones iva) {
        this.iva = iva;
    }

    
    
    
    public String getUtilidadTexto() {
        utilidadTexto = "";
        if(utilidad != null){
            float porcent = new funciones().redondearMas(utilidad.getValorFloat()*100, 2);
            utilidadTexto = porcent + " %";
        }
        return utilidadTexto;
    }

    public String getComisionesTexto() {
        comisionesTexto = "";
        if(comisiones != null){
            float porcent = new funciones().redondearMas(comisiones.getValorFloat()*100, 2);
            comisionesTexto = porcent + " %";
        }
        return comisionesTexto;
    }


    public String getDescuentoTexto() {
        descuentoTexto = "";
        if(descuento != null){
            float porcent = new funciones().redondearMas(descuento.getValorFloat()*100, 2);
            descuentoTexto = porcent + " %";
        }
        return descuentoTexto;
    }

    public String getDetalleTexto() {
        detalleTexto = "";
        if(detalle != null){
            float porcent = new funciones().redondearMas(detalle.getValorFloat()*100, 2);
            detalleTexto = porcent + " %";
        }
        return detalleTexto;
    }

    public String getPlazoTexto() {
        plazoTexto = "";
        if(plazo != null){
            int dias = plazo.getValorInt();
            plazoTexto = dias + " DIAS";
        }
        return plazoTexto;
    }


    public String getIvaTexto() {
        ivaTexto = "";
        if(iva != null){
            float porcent = new funciones().redondearMas(iva.getValorFloat()*100, 2);
            ivaTexto = porcent + " %";
        }
        return ivaTexto;
    }

    public void setUtilidadTexto(String utilidadTexto) {
        this.utilidadTexto = utilidadTexto;
    }

    public void setComisionesTexto(String comisionesTexto) {
        this.comisionesTexto = comisionesTexto;
    }

    public void setDescuentoTexto(String descuentodTexto) {
        this.descuentoTexto = descuentodTexto;
    }

    public void setDetalleTexto(String detalleTexto) {
        this.detalleTexto = detalleTexto;
    }

    public void setPlazoTexto(String plazoTexto) {
        this.plazoTexto = plazoTexto;
    }

    public void setIvaTexto(String ivaTexto) {
        this.ivaTexto = ivaTexto;
    }
    
    

    public float getUtilidadValor() {
        return utilidadValor;
    }

    public void setUtilidadValor(float utilidadValor) {
        this.utilidadValor = utilidadValor;
    }

    public float getComisionesValor() {
        return comisionesValor;
    }

    public void setComisionesValor(float comisionesValor) {
        this.comisionesValor = comisionesValor;
    }

    public float getDescuentoValor() {
        return descuentoValor;
    }

    public void setDescuentoValor(float descuentoValor) {
        this.descuentoValor = descuentoValor;
    }

    public float getDetalleValor() {
        return detalleValor;
    }

    public void setDetalleValor(float detalleValor) {
        this.detalleValor = detalleValor;
    }

    public int getPlazoValor() {
        return plazoValor;
    }

    public void setPlazoValor(int plazoValor) {
        this.plazoValor = plazoValor;
    }

    public float getIvaValor() {
        return ivaValor;
    }

    public void setIvaValor(float ivaValor) {
        this.ivaValor = ivaValor;
    }

    public boolean isCambiarIva() {
        return cambiarIva;
    }

    public void setCambiarIva(boolean cambiarIva) {
        this.cambiarIva = cambiarIva;
    }

    public boolean isCambiarUtilidad() {
        return cambiarUtilidad;
    }

    public void setCambiarUtilidad(boolean cambiarUtilidad) {
        this.cambiarUtilidad = cambiarUtilidad;
    }

    public boolean isCambiarComisiones() {
        return cambiarComisiones;
    }

    public void setCambiarComisiones(boolean cambiarComisiones) {
        this.cambiarComisiones = cambiarComisiones;
    }

    public boolean isCambiarDescuento() {
        return cambiarDescuento;
    }

    public void setCambiarDescuento(boolean cambiarDescuento) {
        this.cambiarDescuento = cambiarDescuento;
    }

    public boolean isCambiarDetalle() {
        return cambiarDetalle;
    }

    public void setCambiarDetalle(boolean cambiarDetalle) {
        this.cambiarDetalle = cambiarDetalle;
    }

    public boolean isCambiarPlazo() {
        return cambiarPlazo;
    }

    public void setCambiarPlazo(boolean cambiarPlazo) {
        this.cambiarPlazo = cambiarPlazo;
    }

    public String getSimbolo() {
        return simbolo;
    }

    public void setSimbolo(String simbolo) {
        this.simbolo = simbolo;
    }

    
    
    
    
    
    
    /*
     * FUNCIONES GENERALES
     */
    
    
    public void consultarConfiguracionesActuales(){
        try{
            //GLOBALES
            iva = ejbFacadeConfig.getConfig("GLOBAL", "IVA");
            
            //DE PRECIOS
            utilidad = ejbFacadeConfig.getConfig("PRECIO VENTA", "UTILIDAD");
            comisiones = ejbFacadeConfig.getConfig("PRECIO VENTA", "COMISIONES Y COSTO DE DISTRIBUCION");
            descuento = ejbFacadeConfig.getConfig("PRECIO VENTA", "DESCUENTO");
            detalle = ejbFacadeConfig.getConfig("PRECIO VENTA", "DETALLE");
            
            //DE CREDITOS
            plazo = ejbFacadeConfig.getConfig("CREDITOS", "DIAS PLAZO");
            new funciones().setMsj(1,"CONFIGURACIONES DE REGLAS DEL NEGOCIO CARGADAS");
            resetBooleanCambios();
            resetFloatConfig();
        }catch(Exception e){
            new funciones().setMsj(4, "NO SE PUDIERON CARGAR TODAS LAS CONFIGURACIONES DE REGLAS DEL NEGOCIO");
        }
    }
    
    
    public void prepararCambiarConfig(int configuracion){
        resetBooleanCambios();
        resetFloatConfig();
        switch(configuracion){
            case 1: //IVA
                cambiarIva = true;
                simbolo = "%";
                break;
            case 2: //PLAZO
                cambiarPlazo = true;
                simbolo = "DIAS";
                break;
            case 3: //UTILIDAD
                cambiarUtilidad = true;
                simbolo = "%";
                break;
            case 4: //COMISIONES
                cambiarComisiones = true;
                simbolo = "%";
                break;
            case 5: //DESCUENTO
                cambiarDescuento = true;
                simbolo = "%";
                break;
            case 6: //DETALLE
                cambiarDetalle = true;
                simbolo = "%";
                break;
        }
    }
    
    
    public void resetBooleanCambios(){
        cambiarComisiones = false;
        cambiarDescuento = false;
        cambiarDetalle = false;
        cambiarIva = false;
        cambiarPlazo = false;
        cambiarUtilidad = false;
    }
    
    public void resetFloatConfig(){
        utilidadValor = (float) 0.00;
        comisionesValor = (float) 0.00;
        descuentoValor = (float) 0.00;
        detalleValor = (float) 0.00;
        plazoValor = 0;
        ivaValor = (float) 0.00;
    }
    
    public void validarNuevoValor(){
        RequestContext context = RequestContext.getCurrentInstance(); 
        if(cambiarIva){
            if(ivaValor < 0 || ivaValor > 100){
                new funciones().setMsj(3,"ERROR: Porcentaje de IVA debe de estar entre 0 y 100");
                context.addCallbackParam("validar",false);
            }else{
                context.addCallbackParam("validar",true);
            }
        }else if(cambiarPlazo){
            if(plazoValor <= 0){
                new funciones().setMsj(3,"ERROR: PLAZO debe de ser mayor a CERO");
                context.addCallbackParam("validar",false);
            }else{
                context.addCallbackParam("validar",true);
            }
        }else if(cambiarUtilidad){
            if(utilidadValor < 0 || utilidadValor > 100){
                new funciones().setMsj(3,"ERROR: Porcentaje de UTILIDAD debe de estar entre 0 y 100");
                context.addCallbackParam("validar",false);
            }else{
                context.addCallbackParam("validar",true);
            }
        }else if(cambiarComisiones){
            if(comisionesValor < 0 || comisionesValor > 100){
                new funciones().setMsj(3,"ERROR: Porcentaje para COMISIONES debe de estar entre 0 y 100");
                context.addCallbackParam("validar",false);
            }else{
                context.addCallbackParam("validar",true);
            }
        }else if(cambiarDescuento){
            if(descuentoValor < 0 || descuentoValor > 100){
                new funciones().setMsj(3,"ERROR: Porcentaje para DESCUENTO debe de estar entre 0 y 100");
                context.addCallbackParam("validar",false);
            }else{
                context.addCallbackParam("validar",true);
            }
        }else if(cambiarDetalle){
            if(detalleValor < 0 || detalleValor > 100){
                new funciones().setMsj(3,"ERROR: Porcentaje de PRECIO AL DETALLE debe de estar entre 0 y 100");
                context.addCallbackParam("validar",false);
            }else{
                context.addCallbackParam("validar",true);
            }
        }else{
            new funciones().setMsj(3,"ERROR DE VALIDACION");
            context.addCallbackParam("validar",false);
        }
    }
    
    
    public void cambiarConfiguracion(){
        try{
            if(cambiarIva){
                ivaValor = new funciones().redondearMas(ivaValor/100 ,4);
                iva.setValorFloat(ivaValor);
                ejbFacadeConfig.edit(iva);
                new funciones().setMsj(1,"PORCENTAJE DE IVA ACTUALIZADO");

            }else if(cambiarPlazo){
                plazo.setValorInt(plazoValor);
                ejbFacadeConfig.edit(plazo);
                new funciones().setMsj(1,"PLAZO DE CREDITO ACTUALIZADO");
            }else if(cambiarUtilidad){
                utilidadValor = new funciones().redondearMas(utilidadValor/100 ,4);
                utilidad.setValorFloat(utilidadValor);
                ejbFacadeConfig.edit(utilidad);
                new funciones().setMsj(1,"PORCENTAJE DE UTILIDAD ACTUALIZADO");
            }else if(cambiarComisiones){
                comisionesValor = new funciones().redondearMas(comisionesValor/100 ,4);
                comisiones.setValorFloat(comisionesValor);
                ejbFacadeConfig.edit(comisiones);
                new funciones().setMsj(1,"PORCENTAJE DE COMISIONES ACTUALIZADO");
            }else if(cambiarDescuento){
                descuentoValor = new funciones().redondearMas(descuentoValor/100 ,4);
                descuento.setValorFloat(descuentoValor);
                ejbFacadeConfig.edit(descuento);
                new funciones().setMsj(1,"PORCENTAJE PARA DESCUENTO ACTUALIZADO");
            }else if(cambiarDetalle){
                detalleValor = new funciones().redondearMas(detalleValor/100 ,4);
                detalle.setValorFloat(detalleValor);
                ejbFacadeConfig.edit(detalle);
                new funciones().setMsj(1,"PORCENTAJE DE PRECIO DE DETALLE ACTUALIZADO");
            }else{
                new funciones().setMsj(3,"NO SE GUARDO EL CAMBIO");
            }
            resetBooleanCambios();
            resetFloatConfig();
        }catch(Exception e){
            new funciones().setMsj(4,"ERROR AL GUARDAR CAMBIO EN CONFIGURACION GLOBAL");
            resetBooleanCambios();
            resetFloatConfig();
        }
        
    }
}
