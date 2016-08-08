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
@Table(name = "factura_ingreso")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FacturaIngreso.findByNaturalezaCompra", query = "SELECT f FROM FacturaIngreso f WHERE f.naturalezaCompra = :naturalezaCompra"),
    @NamedQuery(name = "FacturaIngreso.findByTipoCompra", query = "SELECT f FROM FacturaIngreso f WHERE f.tipoCompra = :tipoCompra"),
    @NamedQuery(name = "FacturaIngreso.findByEstadoFactura", query = "SELECT f FROM FacturaIngreso f WHERE f.estadoFactura = :estadoFactura "),
    @NamedQuery(name = "FacturaIngreso.findPREINGRESOS", query = "SELECT f FROM FacturaIngreso f WHERE f.estadoFactura IN ('PRE INGRESO','AGREGADA')"),
    @NamedQuery(name = "FacturaIngreso.facturaByProveedor", query = "SELECT f FROM FacturaIngreso f WHERE f.proveedor = :proveedor"),
    @NamedQuery(name = "FacturaIngreso.existeFactura", query = "SELECT f FROM FacturaIngreso f WHERE f.facturaIngresoPK = :facturaIngresoPK AND f.tipoDocumento = :tipoFactura"),
    @NamedQuery(name = "FacturaIngreso.creditosActivos", query = "SELECT f FROM FacturaIngreso f WHERE f.tipoCompra = 'AL CREDITO' AND f.estadoCreditoCompra = 'ACTIVO'"),
    @NamedQuery(name = "FacturaIngreso.corteInventarioInicial", query = "SELECT f FROM FacturaIngreso f WHERE f.facturaIngresoPK.documentoCompra = '00000' AND f.numretaceo.numretaceo = '00000' AND f.estadoFactura = 'INICIO' "),
    @NamedQuery(name = "FacturaIngreso.compras", query = "SELECT f FROM FacturaIngreso f WHERE f.facturaIngresoPK.documentoCompra <> '00000' AND f.numretaceo.numretaceo <> '00000' ") })
public class FacturaIngreso implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected FacturaIngresoPK facturaIngresoPK;
    @Size(max = 10)
    @Column(name = "TIPO_DOCUMENTO")
    private String tipoDocumento;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "NATURALEZA_COMPRA")
    private String naturalezaCompra;
    @Size(max = 10)
    @Column(name = "TIPO_COMPRA")
    private String tipoCompra;
    @Column(name = "MONTO_COMPRA")
    private BigDecimal montoCompra;
    @Column(name = "SALIDA_COMPRA")
    private BigDecimal salidaCompra;
    @Column(name = "DESCUENTO_COMPRA")
    private BigDecimal descuentoCompra;
    @Basic(optional = false)
    @NotNull
    @Column(name = "TOTAL_UNIDADES_COMPRA")
    private int totalUnidadesCompra;
    @Column(name = "TOTAL_BULTOS_COMPRA")
    private Integer totalBultosCompra;
    @Column(name = "TOTAL_COMPRA")
    private BigDecimal totalCompra;
    @Column(name = "SALDO_CREDITO_COMPRA")
    private BigDecimal saldoCreditoCompra;
    @Column(name = "INTERES_CREDITO_COMPRA")
    private BigDecimal interesCreditoCompra;
    @Column(name = "TOTALMORA_CREDITO_COMPRA")
    private BigDecimal totalmoraCreditoCompra;
    @Column(name = "ULTIMOPAGO_CREDITO_COMPRA")
    @Temporal(TemporalType.DATE)
    private Date ultimopagoCreditoCompra;
    @Column(name = "FECHAVENC_CREDITO_COMPRA")
    @Temporal(TemporalType.DATE)
    private Date fechavencCreditoCompra;
    @Size(max = 10)
    @Column(name = "ESTADO_CREDITO_COMPRA")
    private String estadoCreditoCompra;
    @Column(name = "FECHA_CANCELADO")
    @Temporal(TemporalType.DATE)
    private Date fechaCancelado;
    @Size(max = 15)
    @Column(name = "ESTADO_FACTURA")
    private String estadoFactura;
    @Lob
    @Size(max = 65535)
    @Column(name = "NOTA_FACTURA")
    private String notaFactura;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "facturaIngreso")
    private Collection<Ingreso> ingresoCollection;
    @JoinColumn(name = "PROVEEDOR", referencedColumnName = "IDPROVEEDOR")
    @ManyToOne(optional = false)
    private Proveedor proveedor;
    @JoinColumn(name = "NUMRETACEO", referencedColumnName = "NUMRETACEO")
    @ManyToOne(optional = false)
    private Retaceo numretaceo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "facturaIngreso")
    private Collection<Preingreso> preingresoCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "facturaIngreso")
    private Collection<PagoCompra> pagoCompraCollection;

    public FacturaIngreso() {
    }

    public FacturaIngreso(FacturaIngresoPK facturaIngresoPK) {
        this.facturaIngresoPK = facturaIngresoPK;
    }

    public FacturaIngreso(FacturaIngresoPK facturaIngresoPK, String naturalezaCompra) {
        this.facturaIngresoPK = facturaIngresoPK;
        this.naturalezaCompra = naturalezaCompra;
    }

    public FacturaIngreso(String documentoCompra, Date fechaDocumento) {
        this.facturaIngresoPK = new FacturaIngresoPK(documentoCompra, fechaDocumento);
    }

    public FacturaIngresoPK getFacturaIngresoPK() {
        return facturaIngresoPK;
    }

    public void setFacturaIngresoPK(FacturaIngresoPK facturaIngresoPK) {
        this.facturaIngresoPK = facturaIngresoPK;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    
    public String getNaturalezaCompra() {
        return naturalezaCompra;
    }

    public void setNaturalezaCompra(String naturalezaCompra) {
        this.naturalezaCompra = naturalezaCompra;
    }

    public String getTipoCompra() {
        return tipoCompra;
    }

    public void setTipoCompra(String tipoCompra) {
        this.tipoCompra = tipoCompra;
    }

    public Integer getTotalUnidadesCompra() {
        return totalUnidadesCompra;
    }

    public void setTotalUnidadesCompra(Integer totalUnidadesCompra) {
        this.totalUnidadesCompra = totalUnidadesCompra;
    }
    

    public Integer getTotalBultosCompra() {
        return totalBultosCompra;
    }

    public void setTotalBultosCompra(Integer totalBultosCompra) {
        this.totalBultosCompra = totalBultosCompra;
    }
    
    public BigDecimal getMontoCompra() {
        return montoCompra;
    }

    public void setMontoCompra(BigDecimal montoCompra) {
        this.montoCompra = montoCompra;
    }

    public BigDecimal getSalidaCompra() {
        return salidaCompra;
    }

    public void setSalidaCompra(BigDecimal salidaCompra) {
        this.salidaCompra = salidaCompra;
    }

    public BigDecimal getDescuentoCompra() {
        return descuentoCompra;
    }

    public void setDescuentoCompra(BigDecimal descuentoCompra) {
        this.descuentoCompra = descuentoCompra;
    }

    public BigDecimal getTotalCompra() {
        return totalCompra;
    }

    public void setTotalCompra(BigDecimal totalCompra) {
        this.totalCompra = totalCompra;
    }

    public BigDecimal getSaldoCreditoCompra() {
        return saldoCreditoCompra;
    }

    public void setSaldoCreditoCompra(BigDecimal saldoCreditoCompra) {
        this.saldoCreditoCompra = saldoCreditoCompra;
    }

    public BigDecimal getInteresCreditoCompra() {
        return interesCreditoCompra;
    }

    public void setInteresCreditoCompra(BigDecimal interesCreditoCompra) {
        this.interesCreditoCompra = interesCreditoCompra;
    }

    public BigDecimal getTotalmoraCreditoCompra() {
        return totalmoraCreditoCompra;
    }

    public void setTotalmoraCreditoCompra(BigDecimal totalmoraCreditoCompra) {
        this.totalmoraCreditoCompra = totalmoraCreditoCompra;
    }

    public Date getUltimopagoCreditoCompra() {
        return ultimopagoCreditoCompra;
    }

    public void setUltimopagoCreditoCompra(Date ultimopagoCreditoCompra) {
        this.ultimopagoCreditoCompra = ultimopagoCreditoCompra;
    }

    public Date getFechavencCreditoCompra() {
        return fechavencCreditoCompra;
    }

    public void setFechavencCreditoCompra(Date fechavencCreditoCompra) {
        this.fechavencCreditoCompra = fechavencCreditoCompra;
    }

    public String getEstadoCreditoCompra() {
        return estadoCreditoCompra;
    }

    public void setEstadoCreditoCompra(String estadoCreditoCompra) {
        this.estadoCreditoCompra = estadoCreditoCompra;
    }

    public Date getFechaCancelado() {
        return fechaCancelado;
    }

    public void setFechaCancelado(Date fechaCancelado) {
        this.fechaCancelado = fechaCancelado;
    }
    
    

    public String getEstadoFactura() {
        return estadoFactura;
    }

    public void setEstadoFactura(String estadoFactura) {
        this.estadoFactura = estadoFactura;
    }

    public String getNotaFactura() {
        return notaFactura;
    }

    public void setNotaFactura(String notaFactura) {
        this.notaFactura = notaFactura;
    }

    @XmlTransient
    public Collection<Ingreso> getIngresoCollection() {
        return ingresoCollection;
    }

    public void setIngresoCollection(Collection<Ingreso> ingresoCollection) {
        this.ingresoCollection = ingresoCollection;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public Retaceo getNumretaceo() {
        return numretaceo;
    }

    public void setNumretaceo(Retaceo numretaceo) {
        this.numretaceo = numretaceo;
    }

    @XmlTransient
    public Collection<Preingreso> getPreingresoCollection() {
        return preingresoCollection;
    }

    public void setPreingresoCollection(Collection<Preingreso> preingresoCollection) {
        this.preingresoCollection = preingresoCollection;
    }

    @XmlTransient
    public Collection<PagoCompra> getPagoCompraCollection() {
        return pagoCompraCollection;
    }

    public void setPagoCompraCollection(Collection<PagoCompra> pagoCompraCollection) {
        this.pagoCompraCollection = pagoCompraCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (facturaIngresoPK != null ? facturaIngresoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FacturaIngreso)) {
            return false;
        }
        FacturaIngreso other = (FacturaIngreso) object;
        if ((this.facturaIngresoPK == null && other.facturaIngresoPK != null) || (this.facturaIngresoPK != null && !this.facturaIngresoPK.equals(other.facturaIngresoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ddicark.entidades.FacturaIngreso[ facturaIngresoPK=" + facturaIngresoPK + " ]";
    }

    
    
}
