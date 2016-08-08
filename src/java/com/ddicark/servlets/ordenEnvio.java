/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ddicark.servlets;

import com.ddicark.auxiliares.detalleNotaEnvio;
import com.ddicark.auxiliares.funciones;
import com.ddicark.entidades.Configuraciones;
import com.ddicark.entidades.Envio;
import com.ddicark.entidades.Factura;
import com.ddicark.jpaFacades.ConfiguracionesFacade;
import com.ddicark.jpaFacades.DetalleEnvioFacade;
import com.ddicark.jpaFacades.EnvioFacade;
import com.ddicark.jpaFacades.FacturaFacade;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.bean.RequestScoped;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.util.JRLoader;

/**
 *
 * @author DDICARK
 */
@RequestScoped
public class ordenEnvio extends HttpServlet {

    @EJB
    private DetalleEnvioFacade ejbFacadeDetalleEnvio;
    
    @EJB
    private EnvioFacade ejbFacadeEnvio;
    
    @EJB
    private FacturaFacade ejbFacadeFactura;
    
    @EJB
    private ConfiguracionesFacade ejbFacadeConfig;
    
    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            response.setContentType("application/pdf");
            response.setHeader("Cache-Control", "no-cache");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            ServletOutputStream out = response.getOutputStream();
            //Se especifíca la ruta del reporte de ireport a compilar por JasperReport
            JasperReport reporte = (JasperReport) JRLoader.loadObject(getServletContext().getRealPath("resources/reportes/NotaEnvio.jasper"));

            //Se crea una instacia de matriz para guardar y enviar los parametros que ocupa el reporte
            Map parametros = new HashMap();
            
            //DETALLE DE LA NOTA ENVIO PARA LA BANDA DETAILS DEL REPORTE
            detalleEnvioDatasource datasource = new detalleEnvioDatasource();
            detalleEnvioDatasource datasourceCopy = new detalleEnvioDatasource();
            detalleEnvioDatasource datasourceCopyCliente = new detalleEnvioDatasource();
                       
            
            String id = (String) request.getParameter("numEnvio");
            if(id != null){
                int num = Integer.parseInt(id);
                Envio envioActual = ejbFacadeEnvio.find(num);
                if(envioActual != null){
                    if(ejbFacadeDetalleEnvio.isDetalleNI(envioActual.getNumenvio())){
                        parametros.put("urlLogo", getServletContext().getRealPath("resources/imagenes/logoKarol.JPG"));
                        parametros.put("empresa", "IMPORTADORA KAROL SHOES");
                        parametros.put("direccionEmpresa", "SAN SALVADOR");
                        parametros.put("telEmpresa", envioActual.getVendedor().getTelEmpleado());
                    }else{
                        parametros.put("urlLogo", getServletContext().getRealPath("resources/imagenes/logoDD.png"));
                        parametros.put("empresa", "GRUPO D'DICARK S.A de C.V");
                        parametros.put("direccionEmpresa", "Av. Izalco Col. San Luis #33,San Salvador");
                        parametros.put("telEmpresa", "2274-6870");
                    }
                    parametros.put("numEnvio", envioActual.getNumenvio());
                    parametros.put("fechaEnvio", new funciones().setFechaFormateada(envioActual.getFechaEmpaquetado(),1));
                    parametros.put("cliente", new funciones().getNombreCliente(envioActual.getPedido().getClientePedido()));
                    parametros.put("nrc",envioActual.getPedido().getClientePedido().getNrcCliente() );
                    if(envioActual.getPedido().getClientePedido().getDuiCliente() == null){
                        parametros.put("duiNit", envioActual.getPedido().getClientePedido().getNitCliente());
                    }else{
                        parametros.put("duiNit", envioActual.getPedido().getClientePedido().getDuiCliente());
                    }
                    parametros.put("tel",envioActual.getPedido().getClientePedido().getTelCliente());
                    parametros.put("direccion", envioActual.getPedido().getClientePedido().getDireccionNegocio());
                    parametros.put("municipio", envioActual.getPedido().getClientePedido().getMunicipioCliente());
                    parametros.put("departamento", envioActual.getPedido().getClientePedido().getDeptoCliente());
                    parametros.put("condicionPago", envioActual.getPedido().getTipoPago());
                    
                    //FACTURAS
                    String facturasEnvio="";
                    List<Factura> facturas = ejbFacadeFactura.facturasByEnvio(envioActual);
                    for(Factura f : facturas){
                        facturasEnvio = facturasEnvio + " "+f.getTipoFactura() + "-" + f.getFacturaPK().getNumfactura();
                    }
                    parametros.put("facturas", facturasEnvio);
                    
                    if(envioActual.getPedido().getTipoPago().equals("AL CREDITO")){
                        //Configuraciones conf = ejbFacadeConfig.getConfig("CREDITOS", "DIAS PLAZO");
                        //if(conf != null){
                            parametros.put("plazo", 30+ " DIAS");
                        //}
                    }else{
                        parametros.put("plazo", null);
                    }
                    parametros.put("despacho", new funciones().getNombreEmpleado(envioActual.getDespacho()));
                    parametros.put("vendedor", new funciones().getNombreEmpleado(envioActual.getVendedor()));
                    parametros.put("copia", "ORIGINAL-EMISOR");
                    //OBTENER EL DETALLE DEL ENVIO ACTUAL
                    datasource = obtenerDetalleEnvioActual(envioActual);
                }
            }else{
                
            }
            
            //COPIAS DEL DATASOURCE PARA COPIAS DEL REPORTE
            for(detalleNotaEnvio actual : datasource.getListaDetalle()){
                datasourceCopy.addLista(actual);
                datasourceCopyCliente.addLista(actual);
            }
            //GENERAR LOS REPORTES
            //REPORTE ORIGINAL
            JasperPrint jasperPrint = JasperFillManager.fillReport(reporte,parametros,datasource);
            //REPORTE CONTABILIDAD
            parametros.remove("copia");
            parametros.put("copia", "DUPLICADO-CONTABILIDAD");
            JasperPrint copy = JasperFillManager.fillReport(reporte,parametros,datasourceCopy);
            //REPORTE COPIA CLIENTE
            parametros.remove("copia");
            parametros.put("copia", "TRIPLICADO-CLIENTE");
            JasperPrint copyCliente = JasperFillManager.fillReport(reporte,parametros,datasourceCopyCliente);
            //Operaciones para generar el reporte con varias copias
            //obtengo el numero de paginas 
            int total = jasperPrint.getPages().size(); 
            for(int count=0;count<(total);count++){ 
                jasperPrint.addPage(total, (JRPrintPage) copyCliente.getPages().get(count));
            }
            for(int count=0;count<(total);count++){ 
                jasperPrint.addPage(total, (JRPrintPage) copy.getPages().get(count));
            }
            JRExporter exporter = new JRPdfExporter();
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, out);
            exporter.exportReport();
               
        } catch (JRException ex) {
            Logger.getLogger(ordenEnvio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
    
    
    
    
    /*
     * FUNCION QUE OBTIENE Y ARREGLA EL DETALLE DE ENVIO A IMPRIMIR
     */
    public detalleEnvioDatasource obtenerDetalleEnvioActual(Envio envio){
        detalleEnvioDatasource notaEnvio = new detalleEnvioDatasource();
        List<Object> detalleEnvioActual = ejbFacadeDetalleEnvio.getDetalleCurvaEnvio(envio);
        
        if(detalleEnvioActual != null){
            List<String> estilos = new ArrayList<String>(); //Lista de estilos
            //Recorremos el arreglo de la consulta para obtener la lista de estilos en la orden
            for(Object curva: detalleEnvioActual){
                Object[] actual = (Object[]) curva;
                String estiloActual = (String) actual[0];
                if(!estilos.contains(estiloActual)){
                    estilos.add(estiloActual);
                }
            }
            if(!estilos.isEmpty()){
                //Recorrer cada uno de los estilos
                for(int e=0; e < estilos.size(); e++){
                    //ENCABEZADO DE ESTILO
                    List<String> tallas = ejbFacadeDetalleEnvio.curvaEstiloEnvio(envio,estilos.get(e));
                    detalleNotaEnvio encabezado = new detalleNotaEnvio(estilos.get(e),null,null,null,null,null,null,null,null,null,null,null,null,null,null);
                    //COLOCAR CURVA
                    for(int t=0; t<tallas.size();t++){
                        encabezado.setCurva(t,tallas.get(t));
                    }
                    notaEnvio.addLista(encabezado);
                    //DETALLE DE ESTILO
                    List<String> colores = new ArrayList<String>();
                    //OBTENER LISTA DE COLORES DEL ESTILO
                    for(Object curva: detalleEnvioActual){
                        Object[] actual = (Object[]) curva;
                        String estiloActual = (String) actual[0];
                        if(estiloActual.equals(estilos.get(e))){
                            String colorActual = (String) actual[1];
                            if(!colores.contains(colorActual)){
                                colores.add(colorActual);
                            }
                        }
                    }
                    //REGISTRAR CURVA DE CADA COLOR
                    for(int c=0; c < colores.size();c++){
                        detalleNotaEnvio detalle = new detalleNotaEnvio(estilos.get(e),colores.get(c),null,null,null,null,null,null,null,null,null,null,null,null,null);
                        //DEFINIR CURVA DEL COLOR
                        Integer unidades = 0;
                        for(Object curva: detalleEnvioActual){
                            Object[] actual = (Object[]) curva;
                            String estiloActual = (String) actual[0];
                            if(estiloActual.equals(detalle.getEstilo())){
                                String colorActual = (String) actual[1];
                                if(colorActual.equals(detalle.getColor())){
                                    String talla = (String) actual[2];
                                    String cantidad = actual[3].toString();
                                    for(int t=0; t < tallas.size(); t++){
                                        if(tallas.get(t).equals(talla)){
                                            detalle.setCurva(t,cantidad);
                                            unidades = unidades + Integer.parseInt(cantidad);
                                        }
                                    }
                                }
                            }
                        }
                        //TOTALES DEL DETALLE
                        BigDecimal precio = ejbFacadeDetalleEnvio.precioUnitario(envio,detalle.getEstilo(), detalle.getColor());
                        //IVA INCLUIDO
                        precio = new funciones().precioConIva(precio);
                        precio = new BigDecimal(new funciones().redondearMas(precio.floatValue(),2));
                        BigDecimal venta = new BigDecimal(precio.doubleValue()*unidades);
                        detalle.setUnidades(unidades);
                        detalle.setPrecio(precio);
                        detalle.setVenta(venta);
                        notaEnvio.addLista(detalle);
                    }
                }
            }else{
                new funciones().setMsj(3, "ERROR INESPERADO LISTA DE ESTILOS VACIA");
            }
        }else{
            new funciones().setMsj(3, "ERROR INESPERADO AL RECUPERAR DETALLE DE LA NOTA DE ENVIO ACTUAL");
        }
        return notaEnvio;
    }
}
