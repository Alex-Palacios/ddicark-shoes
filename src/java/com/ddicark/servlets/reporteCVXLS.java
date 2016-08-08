/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ddicark.servlets;

import com.ddicark.auxiliares.funciones;
import com.ddicark.entidades.Cliente;
import com.ddicark.entidades.Empleado;
import com.ddicark.jpaFacades.ClienteFacade;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
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
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.util.JRLoader;

/**
 *
 * @author INFORMATICA
 */
public class reporteCVXLS extends HttpServlet {

    @EJB
    private ClienteFacade ejbFacadeCliente;
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            
            //PARAMETROS ENVIADOS AL SERVLET
            //PARAMETROS ENVIADOS AL SERVLET
            Empleado vendedor = ((Empleado) (request.getSession().getAttribute("vendedor")));
            Map parametros = new HashMap();
            if(vendedor != null){
                //Se crea una instacia de matriz para guardar y enviar los parametros que ocupa el reporte
                //Se especifíca la ruta del reporte de ireport a compilar por JasperReport
                JasperReport reporte = null;
                //Se especifíca la ruta del reporte de ireport a compilar por JasperReport
                reporte = (JasperReport) JRLoader.loadObject(getServletContext().getRealPath("resources/reportes/reporteCV.jasper"));
                
                parametros.put("vendedor", vendedor.getNombreEmpleado() + " " + vendedor.getApellidoEmpleado());
                
                List<Cliente> clientes = ejbFacadeCliente.getClientesByVendedor(vendedor);
                ReporteCVDataSource datasource = new ReporteCVDataSource();
                for(Cliente c : clientes){
                    ReporteCV item = new ReporteCV(c.getMunicipioCliente(),
                                                   c.getNombreCliente()+" "+c.getApellidoCliente(),
                                                   c.getDireccionCliente() ,
                                                   c.getTelCliente(),
                                                   c.getComercioContacto()+ " ,"+c.getTelComercio(),
                                                   c.getLimite());     
                    datasource.addLista(item);  
                }
                //GENERAR REPORTE
                JasperPrint jasperPrint = JasperFillManager.fillReport(reporte,parametros,datasource);
                
                int bit;
                File f;
                InputStream in;
                ServletOutputStream out;
                String xlsFileName = "ListaClientes.xls";
                //Creacion del XLS
                JRXlsExporter exporter = new JRXlsExporter ();
                exporter.setParameter (JRExporterParameter.JASPER_PRINT, jasperPrint);
                exporter.setParameter (JRExporterParameter.OUTPUT_FILE_NAME, xlsFileName);
                exporter.setParameter (JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.TRUE);
                exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
                exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
                exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
                exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS, Boolean.TRUE);
                exporter.setParameter(JRXlsExporterParameter.IS_IGNORE_CELL_BACKGROUND, Boolean.TRUE);
                exporter.setParameter(JRXlsExporterParameter.IS_IGNORE_CELL_BACKGROUND, Boolean.TRUE);
                exporter.setParameter(JRXlsExporterParameter.IS_COLLAPSE_ROW_SPAN, Boolean.TRUE);
                exporter.setParameter(JRXlsExporterParameter.IGNORE_PAGE_MARGINS, Boolean.TRUE);
                
                exporter.exportReport ();
                // En este punto ya esta Creado el XLS
                // Ahora leemos el fichero y forzamos al navegador a que nos descargue el fichero.
                f = new File ( xlsFileName);
                response.setContentType ("application/vnd.ms-excel"); //Tipo de fichero.
                response.setHeader ("Content-Disposition", "attachment;filename=\"" + xlsFileName + "\""); //Configurar cabecera http
                in = new FileInputStream (f);
                out = response.getOutputStream ();
                bit = 256;
                while ((bit)>= 0)
                {
                   bit = in.read ();
                   out.write (bit);
                }
                out.flush ();
                out.close ();
                request.getSession().setAttribute("archivo",in);
            }
            
        }catch (JRException ex) {
            Logger.getLogger(reporteLiquidacionXLS.class.getName()).log(Level.SEVERE, null, ex);
        } catch(Exception ex){
            Logger.getLogger(reporteLiquidacionXLS.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
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
     * Handles the HTTP <code>POST</code> method.
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
