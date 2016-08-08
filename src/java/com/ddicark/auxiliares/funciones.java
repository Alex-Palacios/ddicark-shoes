/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ddicark.auxiliares;

import com.ddicark.entidades.Cliente;
import com.ddicark.entidades.Empleado;
import com.ddicark.entidades.FacturaIngresoPK;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

/**
 *
 * @author DDICARK
 */

@ManagedBean(name = "funciones")
@SessionScoped
public class funciones implements Serializable {
    
    /*Funcion que redirecciona a otra pagina existente en la aplicacion
     * recibe como parametro la url relativa a partir del nodo raiz
     * de la pagina que se quiere servir
     */ 
    public void irA(String urlRelat) {
        try {
            FacesContext contex = FacesContext.getCurrentInstance();    // almacena el contexto de la intancia actual de navegación
            contex.getExternalContext().redirect("/ddicark-shoes/" + urlRelat);
        } catch (Exception e) {
            /*acción a realizar:
             *      no se realizará ninguna acción, ya que es un error que muy
             *      raras veces sucede.
             */
        }
    }
     
     
    /* Funcion que se utiliza para desplegar un mensaje instantaneo
     * en el contexto actual
     */
    public void setMsj(int tipo, String msj) {
        FacesContext context = FacesContext.getCurrentInstance();   // almacena el contexto de la intancia actual de navegación
        FacesMessage.Severity tipoMsj;                              // sirve para seleccionar entre los diferentes tipos de mensajes
        switch (tipo) {
            case 1:
                tipoMsj = FacesMessage.SEVERITY_INFO;
                break;
            case 2:
                tipoMsj = FacesMessage.SEVERITY_WARN;
                break;
            case 3:
                tipoMsj = FacesMessage.SEVERITY_ERROR;
                break;
            default:
                tipoMsj = FacesMessage.SEVERITY_FATAL;
                break;
        }
        context.addMessage(null, new FacesMessage(tipoMsj, msj, ""));
    }
    
    
    
    /* OBTIENE UNA IMAGEN A PARTIR DE UN ARRAY DE BYTES
     * 
     */
    
    public String createImage(byte [] b){ 
        InputStream input = new ByteArrayInputStream(b);
        //DefaultStreamedContent img = new DefaultStreamedContent(input,"image/jpg");
        return input.toString();
    } 
    
    
    //Obtener la zona horaria
    public TimeZone getTimeZone() {
        return TimeZone.getTimeZone("America/El_Salvador");
    }
    /**
     * *****************Inicio de modulo******************* 
     *      Nombre del módulo:
     *          - getTime
     *      Objetivo: 
     *          - Obtener la fecha u hora actual del sistema, utilizando la zona 
     *            horaria establecida para el sistema 
     *      Parámetros de entrada:
     *          - no hay
     *      Parámetros de retorno: 
     *          - Date calendario.getTime(), retorna la fecha actual del sistema
     *      Módulos de la clase utilizados: 
     *          - getTimeZone
     */
    public Date getTime() {
        TimeZone tz = getTimeZone();
        /*Objetivo:
        *       establecer la zona horaria en la que se está trabajando
        */
        Calendar calendario = Calendar.getInstance();
        /*Objetivo:
        *       poder establecer la fecha y hora actual en el calendario del
        *       sistema para luego establecer la zona horaria respectiva
        */
        calendario.setTimeZone(tz);
        return calendario.getTime();
    }
    
    //Formatea la fecha a dd/MM/yyyy para poder visualizarse
    public String setFechaFormateada(Date fecha,int format) {
            if (fecha != null) {
                SimpleDateFormat formato=new SimpleDateFormat("dd/mm/yyyy");;
                switch(format){
                    case 1:
                        formato = new SimpleDateFormat("dd/MM/yyyy");
                        break;
                    case 2:
                        formato = new SimpleDateFormat("yyyy-MM-dd"); //PARA LA BD
                }
                String fechaFormateada = formato.format(fecha);
                return fechaFormateada;
            } else {
                return "";
            }
    }
    
    //Formatea la fecha a dd/MM/yyyy para poder visualizarse
    public Date setFechaFormateadaBD(Date fecha) {
        try {
            //Convierte el String en tipo Date
            String F = setFechaFormateada(fecha,2);
            //Cambia el Formato de dd/MM/yyyy a yyyy-MM-dd para la BD
            DateFormat dfMysql = new SimpleDateFormat("yyyy-MM-dd");
            fecha = dfMysql.parse(F);
        } catch (ParseException ex) {
            Logger.getLogger(FacturaIngresoPK.class.getName()).log(Level.SEVERE, null, ex);
        }
        return fecha;
    }
    
    //Formatea la hora a hh:mm:ss a
    public String setHoraFormateada(Date hora) {
        if (hora != null) {
            SimpleDateFormat formato = new SimpleDateFormat("hh:mm:ss a");
            String fechaFormateada = formato.format(hora);
            return fechaFormateada;
        } else {
            return "";
        }
    }
    
     /**
     * *****************Inicio de modulo******************* 
     *      Nombre del módulo:
     *          - redondearDosMenos
     *      Objetivo: 
     *          -  Redondear un numero a su valor menor mas cercano con una presicion en decimales detallada
     *      Parámetros de entrada: valor a redondear
     */
    public float redondearMenos(float valor,int precision) {
        BigDecimal big = new BigDecimal(valor);
        /*Objetivo:
         *      realiza operaciones de alta precision con numeros flotantes
         */
        big = big.setScale(precision, RoundingMode.HALF_DOWN);
        return big.floatValue();
    }
    /* *****************Inicio de modulo******************* 
     *      Nombre del módulo:
     *          - redondearMas
     *      Objetivo: 
     *          - Redondear un numero a su valor mayor mas cercano con una presicion en decimales detallada
     *      Parámetros de entrada: valor a redondear
     */
    public float redondearMas(float valor,int precision) {
        BigDecimal big = new BigDecimal(valor);
        /*Objetivo:
         *      realiza operaciones de alta precision con numeros flotantes
         */
        big = big.setScale(precision, RoundingMode.HALF_UP);
        return big.floatValue();
    }
   
    //Retorna un Label segun el valor booleano
    public String aprobadoLabel(boolean aprobado){
        if(aprobado){
            return "APROBADO";
        }else{
            return "POR APROBAR";
        }
    }
    
    //Retorna un Label segun el valor del tipo de producto
    public String tipoLabel(int tipo){
        String label="";
        switch(tipo){
            case 1: label = "CALZADO";break;
            case 2: label = "CARTERA";break;
            case 3: label = "ROPA";break;
            case 4: label = "BOLZON";break;
        }
        return label;
    }
    
    //Retorna un Label segun el valor del genero
    public String generoLabel(String genero){
        String label="";
        if(genero.equals("M")){
            label = "MASCULINO";
        }
        if(genero.equals("F")){
            label = "FEMENINO";
        }
        if(genero.equals("U")){
            label = "UNISEX";
        }
        return label;
    }
    
    //Retorna un Label segun tipo persona
    public String clasePersonaLabel(String clase){
        String label="";
        if(clase.equals("I")){
            label = "BEBE";
        }
        if(clase.equals("N")){
            label = "NIÑOS/As";
        }
        if(clase.equals("J")){
            label = "JUVENIL";
        }
        if(clase.equals("A")){
            label = "ADULTOS";
        }
        return label;
    }
       
   /*
     * Funcion que recibe una entidad cliente y segun su
     * naturaleza devuelve el nombre del cliente
     *       P. JURIDICA : devuelve nombre del negocio
     *       P. NATURAL: devuelve nombre del cliente
     */
   public String getNombreCliente(Cliente cliente){
       String nombreCliente = "";
       if(cliente !=null){
           if(cliente.getNaturaleza().equals("PERSONA NATURAL")){
                nombreCliente = cliente.getNombreCliente() + " " + cliente.getApellidoCliente();
            }else{
                nombreCliente = cliente.getNombreCliente();
            }
       }
       
       return nombreCliente;
   }
    
   //DEVUELVE EL NOMBRE ABREVIADO DEL EMPLEADO PRIMER NOMBRE Y APELLIDO
   public String getNombreEmpleado(Empleado empleado){
       String nombreEmpleado = "";
       if(empleado !=null){
            String[] palabras = empleado.getNombreEmpleado().split(" ");
            nombreEmpleado = nombreEmpleado + palabras[0];
            palabras = empleado.getApellidoEmpleado().split(" ");
            nombreEmpleado = nombreEmpleado + " " + palabras[0];
       }
       return nombreEmpleado;
   }
   
   /*
    * FUNCIONES DE COMPARACION DE FECHAS SIN TOMAR EN CUENTA
    * LA HORA
    */
   
   //Compara si fecha1 es igual a fecha2
   public boolean esIgual(Date fecha1 , Date fecha2){
       boolean respuesta = false;
       Calendar F1 = Calendar.getInstance();
       Calendar F2 = Calendar.getInstance();
       F1.setTime(fecha1);
       F2.setTime(fecha2);
       if(F1.get(Calendar.YEAR) == F2.get(Calendar.YEAR)){
           if(F1.get(Calendar.DAY_OF_YEAR) == F2.get(Calendar.DAY_OF_YEAR)){
               respuesta = true;
           }
       }
       return respuesta;
   }
   
   
   //Compara si fecha1 es menor que fecha2
   public boolean esAntes(Date fecha1 , Date fecha2){
       boolean respuesta = false;
       Calendar F1 = Calendar.getInstance();
       Calendar F2 = Calendar.getInstance();
       F1.setTime(fecha1);
       F2.setTime(fecha2);
       if(F1.get(Calendar.YEAR) < F2.get(Calendar.YEAR)){
           respuesta = true;
       }else if(F1.get(Calendar.YEAR) == F2.get(Calendar.YEAR)) {
           if(F1.get(Calendar.DAY_OF_YEAR) < F2.get(Calendar.DAY_OF_YEAR)){
               respuesta = true;
           }
       }
       return respuesta;
   }
   
   
   //Compara si fecha1 es mayor que fecha2
   public boolean esDespues(Date fecha1 , Date fecha2){
       boolean respuesta = false;
       Calendar F1 = Calendar.getInstance();
       Calendar F2 = Calendar.getInstance();
       F1.setTime(fecha1);
       F2.setTime(fecha2);
       if(F1.get(Calendar.YEAR) > F2.get(Calendar.YEAR)){
           respuesta = true;
       }else if(F1.get(Calendar.YEAR) == F2.get(Calendar.YEAR)) {
           if(F1.get(Calendar.DAY_OF_YEAR) > F2.get(Calendar.DAY_OF_YEAR)){
               respuesta = true;
           }
       }
       return respuesta;
   }
   
   //Devuelve la diferencia en dias entre fecha1 y fecha2 fecha 2 > fecha 1
   // Nota si la diferencia es de horas devolvera cero aunque sean diferente dia
   public long difDias(Date fecha1 , Date fecha2){
       long dias = 0;
       Calendar F1 = Calendar.getInstance();
       Calendar F2 = Calendar.getInstance();
       F1.setTime(fecha1);
       F2.setTime(fecha2);
       long diferencia = F2.getTime().getTime() - F1.getTime().getTime();
       //Diferencia en dias
       dias = diferencia / (24 * 60 * 60 * 1000);
       return dias;
   }
   
   
   public long difDias(Date fecha){
       long dias = 0;
       Calendar F1 = Calendar.getInstance();
       Calendar F2 = Calendar.getInstance();
       F1.setTime(fecha);
       F2.setTime(getTime());
       long diferencia = F2.getTime().getTime() - F1.getTime().getTime();
       //Diferencia en dias
       dias = diferencia / (24 * 60 * 60 * 1000);
       return dias;
   }
   
   /*
    * FUNCION que dvuelve el nombre de un estilo CSS
    * para aplicar a una fila segun la fecha
    *   
    */
   public String estiloFilaFecha(Date fecha){
       String estiloFila = null;
       Date hoy = getTime();
       if(esAntes(fecha, hoy)){
           estiloFila = "fila-red";
       }else if(esIgual(fecha, hoy)){
           estiloFila = "fila-orange";
       }else if(esDespues(fecha, hoy)){
           long difDias = difDias(fecha, hoy);
           if(difDias >= 0 && difDias < 2){
                estiloFila = "fila-yellow";
           }
       }
       return estiloFila;
   }
   
   
   /*
    * FUNCION que devuelve N/E si el valor pasado es igual a un objeto BigDecimal.ZERO
    */
   public boolean precioIsZero(BigDecimal valor){
       boolean r = false;
       if(valor != null){
           if(valor.compareTo(BigDecimal.ZERO) == 0){
               r= true;
           }
       } 
       return r;
   }
   
   
   /*
    * FUNCION QUE CONVIERTE UN MONTO DE DINERO
    * A LETRAS PARA IMPRIMIR EN LAS FACTURAS
    */
   
   public String montoEnLetras(BigDecimal monto){
        float fraccion = new funciones().redondearMas(monto.floatValue() - (int) monto.floatValue(),2);
        int parteEntera = (int) monto.floatValue();
        String Moneda = "Dolar"; //Nombre de Moneda (Singular)
        String Monedas = "Dolares";      //Nombre de Moneda (Plural)
        String Centimo = "Centavo";    //Nombre de Céntimos (Singular)
        String Centimos = "Centavos";  //Nombre de Céntimos (Plural)
        String Preposicion = "Con";    //Preposición entre Moneda y Céntimos
        int NumCentimos;
        String Letra ="";
        double Maximo = 1999999999.99;
        //Validar que el Numero está dentro de los límites
        if(monto.floatValue() >= 0 && monto.floatValue() <= Maximo){
            Letra = convertirNumeroLetra(parteEntera);              //Convertir la parte Entera en letras
            
                        
            NumCentimos = (int) (fraccion * 100);   //Obtener los centimos del Numero
            
            //Si NumCentimos es mayor a cero inicar la conversión
            if(NumCentimos >= 0){
                //Si el parámetro CentimosEnLetra es VERDADERO obtener letras para los céntimos
                 //Mostrar los céntimos como número
                if(NumCentimos < 10){
                    Letra = Letra + " 0" + NumCentimos + "/100";
                }else{
                    Letra = Letra + " " + NumCentimos + "/100";
                }
            }
            
            //Si Numero = 1 agregar leyenda Moneda (Singular)
            if(parteEntera == 1){
                Letra = Letra + " " + Moneda;
            //De lo contrario agregar leyenda Monedas (Plural)
            }else{
                Letra = Letra + " " + Monedas;
            }
            
            return Letra;
        }else{
            return "ERROR: El número excede los limites";
        }  
   }

    public String convertirNumeroLetra(int Numero){
        String resultado="";
        
        //Nombre de los números
        List<String> Unidades = new ArrayList<String>();
        Unidades.add(""); Unidades.add("Un");Unidades.add("Dos");Unidades.add("Tres");Unidades.add("Cuatro");Unidades.add("Cinco");Unidades.add("Seis");Unidades.add("Siete");Unidades.add("Ocho");Unidades.add("Nueve");Unidades.add("Diez");Unidades.add("Once");Unidades.add("Doce");Unidades.add("Trece");Unidades.add("Catorce");Unidades.add("Quince");Unidades.add("Dieciséis");Unidades.add("Diecisiete");Unidades.add("Dieciocho");Unidades.add("Diecinueve");Unidades.add("Veinte");Unidades.add("Veintiuno");Unidades.add("Veintidos");Unidades.add("Veintitres");Unidades.add("Veinticuatro");Unidades.add("Veinticinco");Unidades.add("Veintiseis");Unidades.add("Veintisiete");Unidades.add("Veintiocho");Unidades.add("Veintinueve");
        List<String> Decenas = new ArrayList<String>();
        Decenas.add("");Decenas.add("Diez");Decenas.add("Veinte");Decenas.add("Treinta");Decenas.add("Cuarenta");Decenas.add("Cincuenta");Decenas.add("Sesenta");Decenas.add("Setenta");Decenas.add("Ochenta");Decenas.add("Noventa");Decenas.add("Cien");
        List<String> Centenas = new ArrayList<String>();
        Centenas.add("");Centenas.add("Ciento");Centenas.add("Doscientos");Centenas.add("Trescientos");Centenas.add("Cuatrocientos");Centenas.add("Quinientos");Centenas.add("Seiscientos");Centenas.add("Setecientos");Centenas.add("Ochocientos");Centenas.add("Novecientos");
        
        //Convertir a Letra
        if(Numero == 0){
            resultado = "Cero";
        }else if(Numero >= 1 && Numero <= 29){
            resultado = Unidades.get(Numero);
        }else if(Numero >= 30 && Numero <= 100){
            resultado = Decenas.get(Numero/10);
            if(Numero%10 != 0){
                resultado = resultado +" y ";
                resultado= resultado + convertirNumeroLetra(Numero%10);
            }
        }else if(Numero >= 101 && Numero <= 999){
            resultado = Centenas.get(Numero/100);
            if(Numero%100 != 0){
                resultado = resultado + " ";
                resultado = resultado + convertirNumeroLetra(Numero%100);
            }
        }
        else if(Numero >= 1000 && Numero <= 1999){
            resultado = "Mil";
            if(Numero%1000 != 0){
                resultado = resultado + " ";
                resultado = resultado + convertirNumeroLetra(Numero%1000);
            }
        }
        else if(Numero >= 2000 && Numero <= 999999){
            resultado = convertirNumeroLetra(Numero%1000);
            resultado = resultado + "Mil";
            if(Numero%1000 != 0){
                resultado = resultado + " ";
                resultado = resultado + convertirNumeroLetra(Numero%1000);
            }
        }
        else if(Numero >= 1000000 && Numero <= 1999999){
            resultado = "Un Millón";
            if(Numero%1000000 != 0){
                resultado = resultado + " ";
                resultado = resultado + convertirNumeroLetra(Numero%1000000);
            }
        }
        else if(Numero >= 2000000 && Numero <= 1999999999){
            resultado = convertirNumeroLetra(Numero%1000000);
            resultado = resultado + "Millones";
            if(Numero%1000000 != 0){
                resultado = resultado + " ";
                resultado = resultado + convertirNumeroLetra(Numero%1000000);
            }
        }
        
        return resultado;
    }
    
    
    /*
     * FUNCION QUE SUMA DIAS A UNA FECHA
     */
    public Date sumarDias(Date fecha, int dias){
        if(dias > 0){
            Calendar cal = Calendar.getInstance();
            cal.setTime(fecha);
            cal.add(Calendar.DAY_OF_YEAR, dias);
            return cal.getTime();
        }else{
            return null;
        }
        
    }
    
    /*
     * FUNCION QUE CREA UNA LISTA DE OPCIONES PARA USAR EN FILTROS EN DATATABLES
     */
    public SelectItem[] createFilterOptions(String[] data)  {  
        SelectItem[] options = new SelectItem[data.length + 1];  
  
        options[0] = new SelectItem("", "Select");  
        for(int i = 0; i < data.length; i++) {  
            options[i + 1] = new SelectItem(data[i], data[i]);  
        }  
  
        return options;  
    }  

    
    public String getMesAbreviado(int mes){
        String mesAbreviado=null;
        switch(mes){
            case 1: mesAbreviado = "ENE"; break;
            case 2: mesAbreviado = "FEB"; break;
            case 3: mesAbreviado = "MAR"; break;
            case 4: mesAbreviado = "ABR"; break;
            case 5: mesAbreviado = "MAY"; break;
            case 6: mesAbreviado = "JUN"; break;
            case 7: mesAbreviado = "JUL"; break;
            case 8: mesAbreviado = "AGO"; break;
            case 9: mesAbreviado = "SEP"; break;
            case 10: mesAbreviado = "OCT"; break;
            case 11: mesAbreviado = "NOV"; break;
            case 12: mesAbreviado = "DIC"; break;
            
        }
        return mesAbreviado;
    }
    
    
    
    
    //CALCULA EL IVA A UNA CANTIDAD
    public BigDecimal precioConIva(BigDecimal precioSinIva){
        BigDecimal precioConIva = BigDecimal.ZERO;
        if(precioSinIva != null){
            precioConIva = new BigDecimal(precioSinIva.doubleValue()*1.13);
        }
        return precioConIva;
    }
}
