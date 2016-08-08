/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ddicark.servlets;

import com.ddicark.auxiliares.funciones;
import com.ddicark.entidades.DetalleFactura;
import com.ddicark.entidades.Factura;
import com.ddicark.entidades.FacturaPK;
import com.ddicark.jpaFacades.DetalleFacturaFacade;
import com.ddicark.jpaFacades.FacturaFacade;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;

/**
 *
 * @author DDICARK
 */
public class factura extends HttpServlet {

    
    @EJB
    private DetalleFacturaFacade ejbFacadeDetalleFactura;
    
    @EJB
    private FacturaFacade ejbFacadeFactura;
    
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
            throws ServletException, IOException  {
        try {
            response.setContentType("application/pdf");
            response.setHeader("Cache-Control", "no-cache");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            ServletOutputStream out = response.getOutputStream();
            
            
            //PARAMETROS ENVIADOS AL SERVLET
            String numero = request.getParameter("numFactura");
            Date fecha = new Date(request.getParameter("fechaFactura"));
            if(numero != null && fecha != null){
                Factura factura = new Factura();
                FacturaPK id = new FacturaPK(numero,fecha);
                factura = ejbFacadeFactura.find(id);
                if(factura != null){
                    //Se crea una instacia de matriz para guardar y enviar los parametros que ocupa el reporte
                    Map parametros = new HashMap();
            
                    List<DetalleFactura> detalle = ejbFacadeDetalleFactura.detalleFactura(factura);
                    if(detalle != null){
                        //Se especifíca la ruta del reporte de ireport a compilar por JasperReport
                        JasperReport reporte = null;
                        if(factura.getTipoFactura().equals("FCF")){
                            //FACTURA CONSUMIDOR FINAL
                            //Se especifíca la ruta del reporte de ireport a compilar por JasperReport
                            reporte = (JasperReport) JRLoader.loadObject(getServletContext().getRealPath("resources/reportes/fcf_2.jasper"));
                            parametros.put("cliente",new funciones().getNombreCliente(factura.getCliente()));
                            if(factura.getCliente().getDuiCliente() == null){
                                parametros.put("duiNit", factura.getCliente().getNitCliente());
                            }else{
                                parametros.put("duiNit", factura.getCliente().getDuiCliente());
                            }
                            String f = new funciones().setFechaFormateada(factura.getFacturaPK().getFechaFactura(), 1);
                            String[] parte = f.split("/");
                            parametros.put("dia",parte[0]);
                            parametros.put("mes",new funciones().getMesAbreviado(Integer.parseInt(parte[1])));
                            parametros.put("year",parte[2]);
                            parametros.put("TipoPago",factura.getCondicionPago());
                            parametros.put("direccion",factura.getCliente().getMunicipioCliente()+","+factura.getCliente().getDeptoCliente());
                            
                            //DETALLE DE LA FACTURA
                            for(int i=0; i < detalle.size(); i++){
                                parametros.put("C"+(i+1), detalle.get(i).getCantidad());
                                parametros.put("DESCRIPCION"+(i+1), detalle.get(i).getEstilo()+" : "+ detalle.get(i).getColores());
                                parametros.put("PU"+(i+1), detalle.get(i).getPu());
                                parametros.put("VENTA"+(i+1), detalle.get(i).getMontoLinea());
                            }
                            /*if(factura.getDescuento() != null && (factura.getDescuento().compareTo(BigDecimal.ZERO) > 0 )){
                                parametros.put("C15", null);
                                float descuento = new funciones().redondearMas(factura.getPorcentajeDescuento().floatValue(), 2);
                                parametros.put("DESCRIPCION15", "DESCUENTO APLICADO "+ descuento +"%");
                                parametros.put("PU15",null);
                                parametros.put("VENTA15", factura.getDescuento().negate());
                            }*/
                            //TOTALES
                            parametros.put("sumas",factura.getSubTotal());
                            parametros.put("total",factura.getTotal());
                            parametros.put("montoLetras",new funciones().montoEnLetras(factura.getTotal()));
                            parametros.put("entregado",new funciones().getNombreEmpleado(factura.getNumventa().getOrdenEnvio().getVendedor()));

                        }else if(factura.getTipoFactura().equals("CCF")){
                            reporte = (JasperReport) JRLoader.loadObject(getServletContext().getRealPath("resources/reportes/ccf.jasper"));
                            //FACTURA CREDITO FISCAL
                            parametros.put("cliente",new funciones().getNombreCliente(factura.getCliente()));
                            parametros.put("fechaFactura",new funciones().setFechaFormateada(factura.getFacturaPK().getFechaFactura(), 1));
                            parametros.put("direccion",factura.getCliente().getDireccionCliente()+", "+factura.getCliente().getMunicipioCliente());
                            parametros.put("departamento",factura.getCliente().getDeptoCliente());
                            parametros.put("nrc",factura.getCliente().getNrcCliente());
                            parametros.put("nit",factura.getCliente().getNitCliente());
                            parametros.put("condicionPago",factura.getCondicionPago());
                            //DETALLE DE LA FACTURA
                            for(int i=0; i < detalle.size(); i++){
                                parametros.put("C"+(i+1), detalle.get(i).getCantidad());
                                parametros.put("DESCRIPCION"+(i+1), detalle.get(i).getEstilo()+" : "+ detalle.get(i).getColores());
                                parametros.put("PU"+(i+1), detalle.get(i).getPu());
                                parametros.put("VENTA"+(i+1), detalle.get(i).getMontoLinea());
                            }
                            //DESCUENTO
                           /* if(factura.getDescuento() != null && (factura.getDescuento().compareTo(BigDecimal.ZERO) > 0 )){
                                parametros.put("C6", null);
                                float descuento = new funciones().redondearMas(factura.getPorcentajeDescuento().floatValue(), 2);
                                parametros.put("DESCRIPCION6", "DESCUENTO APLICADO "+ descuento +"%");
                                parametros.put("PU6",null);
                                parametros.put("VENTA6", factura.getDescuento().negate());
                            }*/
                            //TOTALES
                            parametros.put("sumas",factura.getSubTotal());
                            parametros.put("iva", factura.getIva());
                            parametros.put("subtotal", factura.getTotal());
                            parametros.put("total",factura.getTotal());
                            parametros.put("montoLetras",new funciones().montoEnLetras(factura.getTotal()));
                            if(factura.getNumventa() != null){
                               parametros.put("entregado",new funciones().getNombreEmpleado(factura.getNumventa().getOrdenEnvio().getVendedor()));
                            }
                        }
                        
                        //GENERAR REPORTE
                        JasperPrint jasperPrint = JasperFillManager.fillReport(reporte,parametros);

                        JRExporter exporter = new JRPdfExporter();
                        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, out);
                        exporter.exportReport();
                    }else{
                        new funciones().setMsj(3,"DETALLE DE FACTURA NULO");
                    }
                }else{
                    new funciones().setMsj(3,"FACTURA NO EXISTE");
                }
            }else{
                new funciones().setMsj(3,"PARAMETROS NULOS");
            }
        } catch (JRException ex) {
            Logger.getLogger(ordenEnvio.class.getName()).log(Level.SEVERE, null, ex);
        } catch(Exception ex){
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
}
