/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ddicark.servlets;

import com.ddicark.auxiliares.funciones;
import com.ddicark.entidades.Cliente;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
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
 * @author ALEX
 */
public class estadoCuenta extends HttpServlet {

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
        ServletOutputStream out = response.getOutputStream();
        try {
            response.setContentType("application/pdf");
            response.setHeader("Cache-Control", "no-cache");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            //OBTENER DATOS
            List<ItemCuenta> dataCuenta = ((List<ItemCuenta>) (request.getSession().getAttribute("datosEstadoCuenta")));
            List<ItemCredito> dataCredito = ((List<ItemCredito>) (request.getSession().getAttribute("datosCredito")));
            Cliente cliente = (Cliente) (request.getSession().getAttribute("cliente"));
            estadoCuentaDatasource datasourceCuenta = new estadoCuentaDatasource();
            creditosDatasource datasourceCredito = new creditosDatasource();
            if(dataCuenta != null && dataCredito != null){
                //PARAMETROS
                Map parametros = new HashMap(); 
                parametros.put("logoDD", getServletContext().getRealPath("resources/imagenes/logoDD.png"));
                parametros.put("fechaAl", new funciones().getTime());
                parametros.put("cliente", cliente.getNombreCliente() + " " + cliente.getApellidoCliente());
                parametros.put("direccion", cliente.getDireccionCliente()+"   "+cliente.getMunicipioCliente()+"   "+cliente.getDeptoCliente());
                parametros.put("telefono", cliente.getTelCliente());
                if(cliente.getEmpleadoasignado() != null){
                    parametros.put("vendedor", cliente.getEmpleadoasignado().getNombreEmpleado() + " " + cliente.getEmpleadoasignado().getApellidoEmpleado());
                }
                if(cliente.getLimite()!= null){
                    parametros.put("limite", cliente.getLimite());
                }
                for(ItemCuenta actual : dataCuenta){
                    datasourceCuenta.addLista(actual);
                }
                BigDecimal deuda = BigDecimal.ZERO;
                for(ItemCredito actual : dataCredito){
                    datasourceCredito.addLista(actual);
                    deuda = deuda.add(actual.getSALDO());
                }
                parametros.put("creditos", datasourceCredito);
                parametros.put("deuda", deuda);
                //REPORTE
                JasperReport reporte = (JasperReport) JRLoader.loadObject(getServletContext().getRealPath("resources/reportes/estadoCuenta.jasper"));
                JasperPrint jasperPrint = JasperFillManager.fillReport(reporte, parametros, datasourceCuenta);
                JRExporter exporter = new JRPdfExporter();
                exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, out);
                exporter.exportReport();
            }
        }catch (JRException ex) {
            Logger.getLogger(estadoCuenta.class.getName()).log(Level.SEVERE, null, ex);
        }catch(Exception ex){
            Logger.getLogger(estadoCuenta.class.getName()).log(Level.SEVERE, null, ex);
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
