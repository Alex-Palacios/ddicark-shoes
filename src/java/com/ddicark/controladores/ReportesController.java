/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ddicark.controladores;

import com.ddicark.auxiliares.VentasMes;
import com.ddicark.entidades.DetalleFactura;
import com.ddicark.jpaFacades.DetalleEnvioFacade;
import com.ddicark.jpaFacades.VentaFacade;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

@ManagedBean(name = "reportesController")
@SessionScoped
public class ReportesController implements Serializable {

    
    @EJB
    private DetalleEnvioFacade ejbFacadeDetalleEnvio;
    
   
    public ReportesController() {
        //super(Venta.class);
    }
     
    @PostConstruct
    public void init() {
        //super.setFacade(ejbFacadeVenta);
    }
    
    private int month;
    private int year;

    private List<VentasMes> ventasMes;
    private List<VentasMes> ventasMesFiltro;

    public List<VentasMes> getVentasMes() {
        return ventasMes;
    }

    public void setVentasMes(List<VentasMes> ventasMes) {
        this.ventasMes = ventasMes;
    }

    public List<VentasMes> getVentasMesFiltro() {
        return ventasMesFiltro;
    }

    public void setVentasMesFiltro(List<VentasMes> ventasMesFiltro) {
        this.ventasMesFiltro = ventasMesFiltro;
    }
    
    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
    
    
    
    public void prepararRptVentasMes(){
        ventasMes = null;
        ventasMesFiltro = null;
    }
    
    public void actualizarReporteVentasMes(){
        ventasMes = ejbFacadeDetalleEnvio.getVentasMes(month, year);
        ventasMesFiltro = null;
    }
    
    
    
    
     /*
    * FUNCIONES DE PRUEBA PARA DATA-EXPORTER
    */
   public void postProcessXLS(Object document) {  
        HSSFWorkbook wb = (HSSFWorkbook) document;  
        HSSFSheet sheet = wb.getSheetAt(0);  
        HSSFRow header = sheet.getRow(0);  

        HSSFCellStyle cellStyle = wb.createCellStyle();    
        cellStyle.setFillBackgroundColor(HSSFColor.GREEN.index); 
        //cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        HSSFFont font=wb.createFont();
        /* set the weight of the font */
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        /* attach the font to the style created earlier */
        cellStyle.setFont(font);

        for(int i=0; i < header.getPhysicalNumberOfCells();i++) {  
            HSSFCell cell = header.getCell(i);  
            cell.setCellStyle(cellStyle); 
        }  
   }
    
}
