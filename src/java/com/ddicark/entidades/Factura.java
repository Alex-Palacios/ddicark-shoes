/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ddicark.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author DDICARK
 */
@Entity
@Table(name = "factura")
@XmlRootElement
@NamedQueries({
    
    @NamedQuery(name = "Factura.findByCliente", query = "SELECT f FROM Factura f WHERE f.cliente = :cliente"),
    @NamedQuery(name = "Factura.findByTipoFactura", query = "SELECT f FROM Factura f WHERE f.tipoFactura = :tipoFactura"),
    @NamedQuery(name = "Factura.findByCondicionPago", query = "SELECT f FROM Factura f WHERE f.condicionPago = :condicionPago"),
    @NamedQuery(name = "Factura.findByEstado", query = "SELECT f FROM Factura f WHERE f.estado = :estado"),
    @NamedQuery(name = "Factura.existeFactura", query = "SELECT f FROM Factura f WHERE f.facturaPK = :facturaPK"),
    @NamedQuery(name = "Factura.alContadoActivas", query = "SELECT f FROM Factura f WHERE f.condicionPago = 'AL CONTADO' AND f.estado = 'ACTIVA' "),
    @NamedQuery(name = "Factura.alCreditoActivas", query = "SELECT f FROM Factura f WHERE f.condicionPago = 'AL CREDITO' AND f.estado = 'ACTIVA' "),
    @NamedQuery(name = "Factura.ByCliente", query = "SELECT f FROM Factura f WHERE f.cliente = :cliente ORDER BY f.facturaPK.fechaFactura "),
    @NamedQuery(name = "Factura.ByClienteCreditosEstado", query = "SELECT f FROM Factura f WHERE f.cliente = :cliente AND  f.condicionPago = 'AL CREDITO' AND f.estado = :estado"),
    @NamedQuery(name = "Factura.ByClienteContado", query = "SELECT f FROM Factura f WHERE f.cliente = :cliente AND  f.condicionPago = 'AL CONTADO' AND f.estado <> 'ANULADA'"),
    @NamedQuery(name = "Factura.ActivasByCliente", query = "SELECT f FROM Factura f WHERE f.cliente = :cliente AND f.estado = 'ACTIVA' "),
    @NamedQuery(name = "Factura.listaVentas", query = "SELECT f FROM Factura f WHERE  f.estado <> 'ANULADA' AND FUNC('YEAR',f.facturaPK.fechaFactura) = :year AND  FUNC('MONTH',f.facturaPK.fechaFactura) = :month "),
    @NamedQuery(name = "Factura.byMonthYear", query = "SELECT f FROM Factura f WHERE FUNC('YEAR',f.facturaPK.fechaFactura) = :year AND  FUNC('MONTH',f.facturaPK.fechaFactura) = :month "),
    @NamedQuery(name = "Factura.numCreditosActivosByCliente", query = "SELECT COUNT(f) FROM Factura f WHERE f.cliente = :cliente AND f.estado = 'ACTIVA' AND f.condicionPago = 'AL CREDITO' "),
    @NamedQuery(name = "Factura.deudaActualByCliente", query = "SELECT SUM(f.saldo) FROM Factura f WHERE f.cliente = :cliente AND f.estado = 'ACTIVA'"),
    @NamedQuery(name = "Factura.alCreditoActivasByVendedor", query = "SELECT f FROM Factura f WHERE f.condicionPago = 'AL CREDITO' AND f.estado = 'ACTIVA' AND f.cliente.empleadoasignado = :vendedor "),
    @NamedQuery(name = "Factura.alContadoActivasByVendedor", query = "SELECT f FROM Factura f WHERE f.condicionPago = 'AL CONTADO' AND f.estado = 'ACTIVA' AND f.cliente.empleadoasignado = :vendedor "),
    @NamedQuery(name = "Factura.findByEnvio", query = "SELECT f FROM Factura f WHERE f.numventa.ordenEnvio = :envio AND f.estado <> 'ANULADA'"),
    @NamedQuery(name = "Factura.findByVendedor", query = "SELECT f FROM Factura f WHERE f.cliente.empleadoasignado = :vendedor AND f.estado = 'ACTIVA' ORDER BY f.cliente.municipioCliente"),
    @NamedQuery(name = "Factura.findActivas", query = "SELECT f FROM Factura f WHERE  f.estado = 'ACTIVA' ORDER BY f.cliente.municipioCliente") })    

public class Factura implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected FacturaPK facturaPK;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "TIPO_FACTURA")
    private String tipoFactura;
    @Column(name = "SUMAS")
    private BigDecimal sumas;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "SUB_TOTAL")
    private BigDecimal subTotal;
    @Column(name = "PORCENTAJE_DESCUENTO")
    private Float porcentajeDescuento;
    @Column(name = "DESCUENTO")
    private BigDecimal descuento;
    @Column(name = "IVA")
    private BigDecimal iva;
    @Column(name = "TOTAL")
    private BigDecimal total;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "CONDICION_PAGO")
    private String condicionPago;
    @Column(name = "SALDO")
    private BigDecimal saldo;
    @Column(name = "TASA_INTERES")
    private Float tasaInteres;
    @Column(name = "FECHA_PROXIMO_PAGO")
    @Temporal(TemporalType.DATE)
    private Date fechaProximoPago;
    @Column(name = "FECHA_VENCIMIENTO")
    @Temporal(TemporalType.DATE)
    private Date fechaVencimiento;
    @Column(name = "FECHA_CANCELADO")
    @Temporal(TemporalType.DATE)
    private Date fechaCancelado;
    @Column(name = "PERIODO_PAGO")
    private Integer periodoPago;
    @Size(max = 10)
    @Column(name = "ESTADO")
    private String estado;
    @Lob
    @Size(max = 65535)
    @Column(name = "OBSERVACIONES")
    private String observaciones;
    @JoinColumn(name = "NUMVENTA", referencedColumnName = "NUMVENTA")
    private Venta numventa;
    @JoinColumn(name = "CLIENTE", referencedColumnName = "NUMCUENTA")
    @ManyToOne(optional = false)
    private Cliente cliente;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "factura")
    private Collection<PagoVenta> pagoVentaCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "factura")
    private Collection<Devolucion> devolucionCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "factura")
    private Collection<DetalleFactura> detalleFacturaCollection;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "factura")
    private Collection<DevolucionFacturas> devolucionFacturasCollection;

    public Factura() {
    }

    public Factura(FacturaPK facturaPK) {
        this.facturaPK = facturaPK;
    }

    public Factura(FacturaPK facturaPK, Cliente cliente, String tipoFactura, String condicionPago) {
        this.facturaPK = facturaPK;
        this.cliente = cliente;
        this.tipoFactura = tipoFactura;
        this.condicionPago = condicionPago;
    }

    public Factura(String numfactura, Date fechaFactura) {
        this.facturaPK = new FacturaPK(numfactura, fechaFactura);
    }

    public FacturaPK getFacturaPK() {
        return facturaPK;
    }

    public void setFacturaPK(FacturaPK facturaPK) {
        this.facturaPK = facturaPK;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public String getTipoFactura() {
        return tipoFactura;
    }

    public void setTipoFactura(String tipoFactura) {
        this.tipoFactura = tipoFactura;
    }

    public BigDecimal getSumas() {
        return sumas;
    }

    public void setSumas(BigDecimal sumas) {
        this.sumas = sumas;
    }

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(BigDecimal subTotal) {
        this.subTotal = subTotal;
    }

    public Float getPorcentajeDescuento() {
        return porcentajeDescuento;
    }

    public void setPorcentajeDescuento(Float porcentajeDescuento) {
        this.porcentajeDescuento = porcentajeDescuento;
    }

    
    
    public BigDecimal getDescuento() {
        return descuento;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }

    public BigDecimal getIva() {
        return iva;
    }

    public void setIva(BigDecimal iva) {
        this.iva = iva;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getCondicionPago() {
        return condicionPago;
    }

    public void setCondicionPago(String condicionPago) {
        this.condicionPago = condicionPago;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public Float getTasaInteres() {
        return tasaInteres;
    }

    public void setTasaInteres(Float tasaInteres) {
        this.tasaInteres = tasaInteres;
    }

    public Date getFechaProximoPago() {
        return fechaProximoPago;
    }

    public void setFechaProximoPago(Date fechaProximoPago) {
        this.fechaProximoPago = fechaProximoPago;
    }

    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public Date getFechaCancelado() {
        return fechaCancelado;
    }

    public void setFechaCancelado(Date fechaCancelado) {
        this.fechaCancelado = fechaCancelado;
    }

    public Integer getPeriodoPago() {
        return periodoPago;
    }

    public void setPeriodoPago(Integer periodoPago) {
        this.periodoPago = periodoPago;
    }

    
    
    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
    
    public Venta getNumventa() {
        return numventa;
    }

    public void setNumventa(Venta numventa) {
        this.numventa = numventa;
    }
    
    @XmlTransient
    public Collection<PagoVenta> getPagoVentaCollection() {
        return pagoVentaCollection;
    }

    public void setPagoVentaCollection(Collection<PagoVenta> pagoVentaCollection) {
        this.pagoVentaCollection = pagoVentaCollection;
    }

    @XmlTransient
    public Collection<Devolucion> getDevolucionCollection() {
        return devolucionCollection;
    }

    public void setDevolucionCollection(Collection<Devolucion> devolucionCollection) {
        this.devolucionCollection = devolucionCollection;
    }

    @XmlTransient
    public Collection<DetalleFactura> getDetalleFacturaCollection() {
        return detalleFacturaCollection;
    }

    public void setDetalleFacturaCollection(Collection<DetalleFactura> detalleFacturaCollection) {
        this.detalleFacturaCollection = detalleFacturaCollection;
    }

    @XmlTransient
    public Collection<DevolucionFacturas> getDevolucionFacturasCollection() {
        return devolucionFacturasCollection;
    }

    public void setDevolucionFacturasCollection(Collection<DevolucionFacturas> devolucionFacturasCollection) {
        this.devolucionFacturasCollection = devolucionFacturasCollection;
    }

    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (facturaPK != null ? facturaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Factura)) {
            return false;
        }
        Factura other = (Factura) object;
        if ((this.facturaPK == null && other.facturaPK != null) || (this.facturaPK != null && !this.facturaPK.equals(other.facturaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ddicark.entidades.Factura[ facturaPK=" + facturaPK + " ]";
    }
    
}
