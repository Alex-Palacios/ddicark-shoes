/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ddicark.servlets;

import com.ddicark.auxiliares.funciones;
import com.ddicark.entidades.Empleado;
import com.ddicark.entidades.Factura;
import com.ddicark.jpaFacades.FacturaFacade;
import java.io.IOException;
import java.math.BigDecimal;
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
 * @author DDICARK-DEVELOPMENT
 */
public class reporteLiquidacion extends HttpServlet {

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
            throws ServletException, IOException {
        try {
            response.setContentType("application/pdf");
            response.setHeader("Cache-Control", "no-cache");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            ServletOutputStream out = response.getOutputStream();
            //PARAMETROS ENVIADOS AL SERVLET
            Empleado vendedor = ((Empleado) (request.getSession().getAttribute("vendedor")));
            if(vendedor != null){
                //Se crea una instacia de matriz para guardar y enviar los parametros que ocupa el reporte
                Map parametros = new HashMap();
                parametros.put("urlLogo", getServletContext().getRealPath("resources/imagenes/logoDD.png"));
                //Se especifíca la ruta del reporte de ireport a compilar por JasperReport
                JasperReport reporte = null;
                //Se especifíca la ruta del reporte de ireport a compilar por JasperReport
                reporte = (JasperReport) JRLoader.loadObject(getServletContext().getRealPath("resources/reportes/reporteLiquidacion.jasper"));
                parametros.put("vendedor",vendedor.getNombreEmpleado() +" "+ vendedor.getApellidoEmpleado());
                parametros.put("fechaHoy",new funciones().getTime());
                
                List<Factura> cuentas = ejbFacadeFactura.facturasByVendedor(vendedor);
                LiquidacionDatasource datasource = new LiquidacionDatasource();
                for(Factura f : cuentas){
                    BigDecimal limiteActual = BigDecimal.ZERO;
                    if(f.getCliente().getLimite() != null){
                        limiteActual = f.getCliente().getLimite().subtract(new BigDecimal(ejbFacadeFactura.getDeudaPorCreditos(f.getCliente())));
                    }
                    if(limiteActual.compareTo(BigDecimal.ZERO) <= 0){
                        limiteActual = BigDecimal.ZERO;
                    }
                    Liquidacion l = new Liquidacion(f.getTipoFactura(),f.getFacturaPK().getFechaFactura(),f.getFacturaPK().getNumfactura(),f.getCliente().obtenerNombreCliente(),f.getCliente().getMunicipioCliente(),f.getTotal(),f.getSaldo(),limiteActual,new funciones().difDias(f.getFacturaPK().getFechaFactura()));     
                    datasource.addLista(l);  
                }
                //GENERAR REPORTE
                JasperPrint jasperPrint = JasperFillManager.fillReport(reporte,parametros,datasource);
                JRExporter exporter = new JRPdfExporter();
                exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, out);
                exporter.exportReport();
            }else{
                //Se crea una instacia de matriz para guardar y enviar los parametros que ocupa el reporte
                Map parametros = new HashMap();
                parametros.put("urlLogo", getServletContext().getRealPath("resources/imagenes/logoDD.png"));
                //Se especifíca la ruta del reporte de ireport a compilar por JasperReport
                JasperReport reporte = null;
                //Se especifíca la ruta del reporte de ireport a compilar por JasperReport
                reporte = (JasperReport) JRLoader.loadObject(getServletContext().getRealPath("resources/reportes/reporteLiquidacion.jasper"));
                parametros.put("vendedor","TODOS LOS VENDEDORES");
                parametros.put("fechaHoy",new funciones().getTime());
                List<Factura> cuentas =  ejbFacadeFactura.facturasActivas();
                LiquidacionDatasource datasource = new LiquidacionDatasource();
                for(Factura f : cuentas){
                    BigDecimal limiteActual = BigDecimal.ZERO;
                    if(f.getCliente().getLimite() != null){
                        limiteActual = f.getCliente().getLimite().subtract(new BigDecimal(ejbFacadeFactura.getDeudaPorCreditos(f.getCliente())));
                    }
                    if(limiteActual.compareTo(BigDecimal.ZERO) <= 0){
                        limiteActual = BigDecimal.ZERO;
                    }
                    Liquidacion l = new Liquidacion(f.getTipoFactura(),f.getFacturaPK().getFechaFactura(),f.getFacturaPK().getNumfactura(),f.getCliente().obtenerNombreCliente(),f.getCliente().getMunicipioCliente(),f.getTotal(),f.getSaldo(), limiteActual,new funciones().difDias(f.getFacturaPK().getFechaFactura()));     
                    datasource.addLista(l);  
                }
                //GENERAR REPORTE
                JasperPrint jasperPrint = JasperFillManager.fillReport(reporte,parametros,datasource);
                JRExporter exporter = new JRPdfExporter();
                exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, out);
                exporter.exportReport();
            }
        }catch (JRException ex) {
            Logger.getLogger(reporteLiquidacion.class.getName()).log(Level.SEVERE, null, ex);
        } catch(Exception ex){
            Logger.getLogger(reporteLiquidacion.class.getName()).log(Level.SEVERE, null, ex);
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
