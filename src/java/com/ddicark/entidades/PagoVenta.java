/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ddicark.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author DDICARK
 */
@Entity
@Table(name = "pago_venta")
@XmlRootElement
@NamedQueries({
    
    @NamedQuery(name = "PagoVenta.maxID", query = "SELECT MAX(p.idpago) FROM PagoVenta p"),
    @NamedQuery(name = "PagoVenta.pagosFactura", query = "SELECT p FROM PagoVenta p WHERE p.factura = :factura AND p.estado <> 'RECHAZADO' "),
    @NamedQuery(name = "PagoVenta.byMonthYear", query = "SELECT p FROM PagoVenta p WHERE FUNC('YEAR',p.fechaPago) = :year AND  FUNC('MONTH',p.fechaPago) = :month "),
    @NamedQuery(name = "PagoVenta.byRangoFecha", query = "SELECT p FROM PagoVenta p WHERE p.fechaPago BETWEEN :fechaInicio AND :fechaFin "),
    @NamedQuery(name = "PagoVenta.byRangoFechaByVendedor", query = "SELECT p FROM PagoVenta p WHERE p.responsableCobro = :vendedor AND p.fechaPago BETWEEN :fechaInicio AND :fechaFin "),
    @NamedQuery(name = "PagoVenta.fecha", query = "SELECT p FROM PagoVenta p WHERE p.fechaPago = :fecha "),
    @NamedQuery(name = "PagoVenta.byRemesa", query = "SELECT p FROM PagoVenta p WHERE p.remesa = :remesa "),
    @NamedQuery(name = "PagoVenta.sinRemesar", query = "SELECT p FROM PagoVenta p WHERE p.remesa IS NULL AND (p.tipoPago = 'EFECTIVO' AND P.estado = 'RECIBIDO') OR (p.tipoPago = 'CHEQUE' AND P.estado = 'RESERVADO') ORDER BY p.responsableCobro.codempleado "),
    @NamedQuery(name = "PagoVenta.remesaSinVerificar", query = "SELECT p FROM PagoVenta p WHERE p.remesa IS NULL AND p.tipoPago IN ('REMESA','CHEQUE') AND P.estado = 'RECIBIDO' ORDER BY p.responsableCobro.codempleado "),
    @NamedQuery(name = "PagoVenta.registradosByVendedor", query = "SELECT p FROM PagoVenta p WHERE p.estado = 'REGISTRADO' AND p.responsableCobro = :vendedor "),
    @NamedQuery(name = "PagoVenta.chequesRemesasRegistradas", query = "SELECT p FROM PagoVenta p WHERE p.estado = 'REGISTRADO' AND p.tipoPago IN ('REMESA','CHEQUE') ") })

public class PagoVenta implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "IDPAGO")
    private Integer idpago;
    @Size(max = 5)
    @Column(name = "SERIE")
    private String serieRECIBO;
    @Size(max = 20)
    @Column(name = "RECIBO")
    private String recibo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_PAGO")
    @Temporal(TemporalType.DATE)
    private Date fechaPago;
    @Size(max = 20)
    @Column(name = "TIPO_PAGO")
    private String tipoPago;
    @Size(max = 50)
    @Column(name = "BANCO")
    private String banco;
    @Size(max = 50)
    @Column(name = "IDENTIFICADOR")
    private String identificador;
    @Lob
    @Size(max = 65535)
    @Column(name = "DETALLE_PAGO")
    private String detallePago;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "ABONO")
    private BigDecimal abono;
    @Column(name = "MORA")
    private BigDecimal mora;
    @Column(name = "INTERES")
    private BigDecimal interes;
    @Column(name = "DESCUENTO")
    private BigDecimal descuento;
    @Column(name = "TOTAL_PAGO")
    private BigDecimal totalPago;
    @Size(max = 20)
    @Column(name = "ESTADO")
    private String estado;
    @Lob
    @Size(max = 65535)
    @Column(name = "NOTA")
    private String nota;
    @Column(name = "FECHA_RECIBIDO")
    @Temporal(TemporalType.DATE)
    private Date fechaRecibido;
    @JoinColumn(name = "IDTRANSAC", referencedColumnName = "IDTRANSAC")
    private Transaccion idtransac;
    
    //FK FACTURA ACTUALIZADO
    @JoinColumns({
        @JoinColumn(name = "NUMFACTURA", referencedColumnName = "NUMFACTURA"),
        @JoinColumn(name = "FECHA_FACTURA", referencedColumnName = "FECHA_FACTURA")})
    @ManyToOne(optional = false)
    private Factura factura;

    @JoinColumn(name = "REMESA", referencedColumnName = "IDREMESA")
    @ManyToOne
    private Remesa remesa;
    
    @JoinColumn(name = "RESPONSABLE_COBRO", referencedColumnName = "CODEMPLEADO")
    @ManyToOne
    private Empleado responsableCobro;
    
    public PagoVenta() {
    }

    public PagoVenta(Integer idpago) {
        this.idpago = idpago;
    }

    public Integer getIdpago() {
        return idpago;
    }

    public String getSerieRECIBO() {
        return serieRECIBO;
    }

    public void setSerieRECIBO(String serieRECIBO) {
        this.serieRECIBO = serieRECIBO;
    }

    
    public String getRecibo() {
        return recibo;
    }

    public void setRecibo(String recibo) {
        this.recibo = recibo;
    }

    
    public void setIdpago(Integer idpago) {
        this.idpago = idpago;
    }

    public Date getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(Date fechaPago) {
        this.fechaPago = fechaPago;
    }

    
    public String getDetallePago() {
        return detallePago;
    }

    public void setDetallePago(String detallePago) {
        this.detallePago = detallePago;
    }

    public BigDecimal getAbono() {
        return abono;
    }

    public void setAbono(BigDecimal abono) {
        this.abono = abono;
    }

    public BigDecimal getMora() {
        return mora;
    }

    public void setMora(BigDecimal mora) {
        this.mora = mora;
    }

    public BigDecimal getInteres() {
        return interes;
    }

    public void setInteres(BigDecimal interes) {
        this.interes = interes;
    }

    
    
    public BigDecimal getTotalPago() {
        return totalPago;
    }

    public void setTotalPago(BigDecimal totalPago) {
        this.totalPago = totalPago;
    }

    public String getTipoPago() {
        return tipoPago;
    }

    public void setTipoPago(String tipoPago) {
        this.tipoPago = tipoPago;
    }

    public String getBanco() {
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public BigDecimal getDescuento() {
        return descuento;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    public Date getFechaRecibido() {
        return fechaRecibido;
    }

    public void setFechaRecibido(Date fechaRecibido) {
        this.fechaRecibido = fechaRecibido;
    }

    public Transaccion getIdtransac() {
        return idtransac;
    }

    public void setIdtransac(Transaccion idtransac) {
        this.idtransac = idtransac;
    }

    public Factura getFactura() {
        return factura;
    }

    public void setFactura(Factura factura) {
        this.factura = factura;
    }

    public Remesa getRemesa() {
        return remesa;
    }

    public void setRemesa(Remesa remesa) {
        this.remesa = remesa;
    }

    public Empleado getResponsableCobro() {
        return responsableCobro;
    }

    public void setResponsableCobro(Empleado responsableCobro) {
        this.responsableCobro = responsableCobro;
    }
    
    
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idpago != null ? idpago.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PagoVenta)) {
            return false;
        }
        PagoVenta other = (PagoVenta) object;
        if ((this.idpago == null && other.idpago != null) || (this.idpago != null && !this.idpago.equals(other.idpago))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.recibo;
    }
    
}
