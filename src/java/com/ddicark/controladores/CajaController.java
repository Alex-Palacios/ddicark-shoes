package com.ddicark.controladores;

import com.ddicark.entidades.Caja;
import com.ddicark.auxiliares.curvaColor;
import com.ddicark.auxiliares.funciones;
import com.ddicark.entidades.Inventario;
import com.ddicark.jpaFacades.CajaFacade;
import com.ddicark.jpaFacades.InventarioFacade;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name = "cajaController")
@SessionScoped
public class CajaController extends AbstractController<Caja> implements Serializable {

    @EJB
    private CajaFacade ejbFacadeCaja;
    
    @EJB
    private InventarioFacade ejbFacadeInventario;

    public CajaController() {
        super(Caja.class);
    }
    //Inicializa controlador de persistencia de proveedores
    @PostConstruct
    public void init() {
        super.setFacade(ejbFacadeCaja);
    }

    //VARIABLES DE CONTROL
    private String[] tallas = new String[10];
    private int numTallas;
    private curvaColor[] curva = {new curvaColor(),new curvaColor(),new curvaColor(),new curvaColor(),new curvaColor(),new curvaColor()};
    private int numColores;
    private int unidadesActuales;
    private List<Inventario> unidadesCaja;
    private Caja cajaSeleccionada;

    public String[] getTallas() {
        return tallas;
    }

    public void setTallas(String[] tallas) {
        this.tallas = tallas;
    }
    
    public void resetTallas(){
        for(int i=0; i < tallas.length; i++){
            tallas[i] = "";
        }
    }

    public int getNumTallas() {
        return numTallas;
    }

    public void setNumTallas(int numTallas) {
        this.numTallas = numTallas;
    }

    public curvaColor[] getCurva() {
        return curva;
    }

    public void setCurva(curvaColor[] curva) {
        this.curva = curva;
    }

    public void resetCurva(){
        for(curvaColor actual: curva){
            actual.resetear();
        }
    }
    
    public int getNumColores() {
        return numColores;
    }

    public void setNumColores(int numColores) {
        this.numColores = numColores;
    }

    public int getUnidadesActuales() {
        return unidadesActuales;
    }

    public void setUnidadesActuales(int unidadesActuales) {
        this.unidadesActuales = unidadesActuales;
    }

    public Caja getCajaSeleccionada() {
        return cajaSeleccionada;
    }

    public void setCajaSeleccionada(Caja cajaSeleccionada) {
        this.cajaSeleccionada = cajaSeleccionada;
    }
    
    
    
    
    public void mostrarDetalleCaja(Object idCaja){
        cajaSeleccionada = ejbFacadeCaja.find(idCaja);
        if(cajaSeleccionada != null){
            consultarCurva(cajaSeleccionada);
        }else{
            new funciones().setMsj(3, "CAJA NO ENCONTRADA");
        }
    }
    
    /*
     * FUNCIONES GENERALES
     */
    //Calcula los datos y curva de la caja del preingreso
    public void consultarCurva(Caja caja){
        unidadesActuales = 0;
        numTallas = 0;
        resetTallas();
        numColores = 0;
        resetCurva();
        List<String> tallasList = ejbFacadeInventario.getTallasCurva(caja,1);
        List<Object> curvasList = ejbFacadeInventario.getCurvaCaja(caja,1);
        if((tallasList != null) && (curvasList != null)){
            
            int menorLista = 0;
            if(tallas.length <= tallasList.size()){
                numTallas = tallas.length;
            }else{
                numTallas = tallasList.size();
            }
            //Llenar Lista de Tallas a mostrar en curva
            for(int t=0; t < numTallas; t++){
                tallas[t] = tallasList.get(t);
            }
            
            int c = -1; //posicion del arreglo de curva
            
            String colorActual = ""; //Color Actual
            //Recorremos el arreglo de la consulta
            for(Object consulta: curvasList){ // r: posicion en la consulta
               int t; //Posicion de la talla en el arreglo tallas
               Object[] actual = (Object[]) consulta;
               if(!(actual[0].equals(colorActual))){
                    //No es igual (cambio de color)
                    c++; //posicion del arreglo de curva
                    colorActual = (String) actual[0]; //Nuevo Color
                    numColores++;
                    curva[c].setColor(colorActual);
                }
                //Es igual al color actual
                for(t=0; t < tallas.length; t++){
                   if(actual[1].equals(tallas[t])){
                       break; //Encontrado
                   }
                }
                switch(t){
                    case 0: 
                        curva[c].setT1(Integer.parseInt(actual[2].toString()));
                        unidadesActuales = unidadesActuales + curva[c].getT1(); break;
                    case 1: 
                        curva[c].setT2(Integer.parseInt(actual[2].toString()));
                        unidadesActuales = unidadesActuales + curva[c].getT2(); break;
                    case 2: 
                        curva[c].setT3(Integer.parseInt(actual[2].toString())); 
                        unidadesActuales = unidadesActuales + curva[c].getT3(); break;
                    case 3: 
                        curva[c].setT4(Integer.parseInt(actual[2].toString())); 
                        unidadesActuales = unidadesActuales + curva[c].getT4(); break;
                    case 4: 
                        curva[c].setT5(Integer.parseInt(actual[2].toString())); 
                        unidadesActuales = unidadesActuales + curva[c].getT5(); break;
                    case 5: 
                        curva[c].setT6(Integer.parseInt(actual[2].toString())); 
                        unidadesActuales = unidadesActuales + curva[c].getT6(); break;
                    case 6: 
                        curva[c].setT7(Integer.parseInt(actual[2].toString())); 
                        unidadesActuales = unidadesActuales + curva[c].getT7(); break;
                    case 7: 
                        curva[c].setT8(Integer.parseInt(actual[2].toString())); 
                        unidadesActuales = unidadesActuales + curva[c].getT8(); break;
                    case 8: 
                        curva[c].setT9(Integer.parseInt(actual[2].toString())); 
                        unidadesActuales = unidadesActuales + curva[c].getT9(); break;
                    case 9: 
                        curva[c].setT10(Integer.parseInt(actual[2].toString())); 
                        unidadesActuales = unidadesActuales + curva[c].getT10();break;
                }
            }
        }
    }    

    //FUNCION que devuelve la lista de unidades de la caja que no estan vendidas
    public List<Inventario> getUnidadesCaja(Caja caja) {
        unidadesCaja = ejbFacadeInventario.detalleDeCaja(caja);
        return unidadesCaja;
    }
    
    
    
    
    
    
    
    
    
    public void mostrarDetalleCajaAnterior(Object idCaja){
        cajaSeleccionada = ejbFacadeCaja.find(idCaja);
        if(cajaSeleccionada != null){
            consultarCurvaAnterior(cajaSeleccionada);
        }else{
            new funciones().setMsj(3, "CAJA NO ENCONTRADA");
        }
    }
    
    public void consultarCurvaAnterior(Caja caja){
        unidadesActuales = 0;
        numTallas = 0;
        resetTallas();
        numColores = 0;
        resetCurva();
        List<String> tallasList = ejbFacadeInventario.getTallasCurvaAnterior(caja,1);
        List<Object> curvasList = ejbFacadeInventario.getCurvaCajaAnterior(caja,1);
        if((tallasList != null) && (curvasList != null)){
            
            int menorLista = 0;
            if(tallas.length <= tallasList.size()){
                numTallas = tallas.length;
            }else{
                numTallas = tallasList.size();
            }
            //Llenar Lista de Tallas a mostrar en curva
            for(int t=0; t < numTallas; t++){
                tallas[t] = tallasList.get(t);
            }
            
            int c = -1; //posicion del arreglo de curva
            
            String colorActual = ""; //Color Actual
            //Recorremos el arreglo de la consulta
            for(Object consulta: curvasList){ // r: posicion en la consulta
               int t; //Posicion de la talla en el arreglo tallas
               Object[] actual = (Object[]) consulta;
               if(!(actual[0].equals(colorActual))){
                    //No es igual (cambio de color)
                    c++; //posicion del arreglo de curva
                    colorActual = (String) actual[0]; //Nuevo Color
                    numColores++;
                    curva[c].setColor(colorActual);
                }
                //Es igual al color actual
                for(t=0; t < tallas.length; t++){
                   if(actual[1].equals(tallas[t])){
                       break; //Encontrado
                   }
                }
                switch(t){
                    case 0: 
                        curva[c].setT1(Integer.parseInt(actual[2].toString()));
                        unidadesActuales = unidadesActuales + curva[c].getT1(); break;
                    case 1: 
                        curva[c].setT2(Integer.parseInt(actual[2].toString()));
                        unidadesActuales = unidadesActuales + curva[c].getT2(); break;
                    case 2: 
                        curva[c].setT3(Integer.parseInt(actual[2].toString())); 
                        unidadesActuales = unidadesActuales + curva[c].getT3(); break;
                    case 3: 
                        curva[c].setT4(Integer.parseInt(actual[2].toString())); 
                        unidadesActuales = unidadesActuales + curva[c].getT4(); break;
                    case 4: 
                        curva[c].setT5(Integer.parseInt(actual[2].toString())); 
                        unidadesActuales = unidadesActuales + curva[c].getT5(); break;
                    case 5: 
                        curva[c].setT6(Integer.parseInt(actual[2].toString())); 
                        unidadesActuales = unidadesActuales + curva[c].getT6(); break;
                    case 6: 
                        curva[c].setT7(Integer.parseInt(actual[2].toString())); 
                        unidadesActuales = unidadesActuales + curva[c].getT7(); break;
                    case 7: 
                        curva[c].setT8(Integer.parseInt(actual[2].toString())); 
                        unidadesActuales = unidadesActuales + curva[c].getT8(); break;
                    case 8: 
                        curva[c].setT9(Integer.parseInt(actual[2].toString())); 
                        unidadesActuales = unidadesActuales + curva[c].getT9(); break;
                    case 9: 
                        curva[c].setT10(Integer.parseInt(actual[2].toString())); 
                        unidadesActuales = unidadesActuales + curva[c].getT10();break;
                }
            }
        }
    }    
    
    
    
}
