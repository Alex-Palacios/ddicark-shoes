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
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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
@Table(name = "venta")
@XmlRootElement
@NamedQueries({
    
    @NamedQuery(name = "Venta.findByNumventa", query = "SELECT v FROM Venta v WHERE v.numventa = :numventa"),
    @NamedQuery(name = "Venta.findByTipoVenta", query = "SELECT v FROM Venta v WHERE v.tipoVenta = :tipoVenta"),
    @NamedQuery(name = "Venta.findByEstadoVenta", query = "SELECT v FROM Venta v WHERE v.estadoVenta = :estadoVenta"),
    @NamedQuery(name = "Venta.VentaByOrden", query = "SELECT v FROM Venta v WHERE v.ordenEnvio = :ordenEnvio"),
    @NamedQuery(name = "Venta.maxID", query = "SELECT MAX(v.numventa) FROM Venta v  ")})
public class Venta implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "NUMVENTA")
    private Integer numventa;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "MONTO_VENTA")
    private BigDecimal montoVenta;
    @Column(name = "DESCUENTO_VENTA")
    private Float descuentoVenta;
    @Column(name = "DEVOLUCIONES_VENTA")
    private BigDecimal devolucionesVenta;
    @Column(name = "VENTA_NETA")
    private BigDecimal ventaNeta;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "TIPO_VENTA")
    private String tipoVenta;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "ESTADO_VENTA")
    private String estadoVenta;
    @Lob
    @Size(max = 65535)
    @Column(name = "NOTA_VENTA")
    private String notaVenta;
    
    @JoinColumn(name = "DESPACHADO_A", referencedColumnName = "CODEMPLEADO")
    @ManyToOne
    private Empleado despachadoA;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "numventa")
    private Collection<Factura> facturaCollection;
    @JoinColumn(name = "ORDEN_ENVIO", referencedColumnName = "NUMENVIO")
    @OneToOne(optional = false)
    private Envio ordenEnvio;
    @JoinColumn(name = "IDTRANSAC", referencedColumnName = "IDTRANSAC")
    @ManyToOne(optional = false)
    private Transaccion idtransac;

    public Venta() {
    }

    public Venta(Integer numventa) {
        this.numventa = numventa;
    }

    public Venta(Integer numventa, BigDecimal montoVenta, String tipoVenta, String estadoVenta) {
        this.numventa = numventa;
        this.montoVenta = montoVenta;
        this.tipoVenta = tipoVenta;
        this.estadoVenta = estadoVenta;
    }

    public Integer getNumventa() {
        return numventa;
    }

    public void setNumventa(Integer numventa) {
        this.numventa = numventa;
    }

    public BigDecimal getMontoVenta() {
        return montoVenta;
    }

    public void setMontoVenta(BigDecimal montoVenta) {
        this.montoVenta = montoVenta;
    }

    public Float getDescuentoVenta() {
        return descuentoVenta;
    }

    public void setDescuentoVenta(Float descuentoVenta) {
        this.descuentoVenta = descuentoVenta;
    }

    public BigDecimal getDevolucionesVenta() {
        return devolucionesVenta;
    }

    public void setDevolucionesVenta(BigDecimal devolucionesVenta) {
        this.devolucionesVenta = devolucionesVenta;
    }

    public BigDecimal getVentaNeta() {
        return ventaNeta;
    }

    public void setVentaNeta(BigDecimal ventaNeta) {
        this.ventaNeta = ventaNeta;
    }

    public String getTipoVenta() {
        return tipoVenta;
    }

    public void setTipoVenta(String tipoVenta) {
        this.tipoVenta = tipoVenta;
    }

    public String getEstadoVenta() {
        return estadoVenta;
    }

    public void setEstadoVenta(String estadoVenta) {
        this.estadoVenta = estadoVenta;
    }

    public String getNotaVenta() {
        return notaVenta;
    }

    public void setNotaVenta(String notaVenta) {
        this.notaVenta = notaVenta;
    }

    public Empleado getDespachadoA() {
        return despachadoA;
    }

    public void setDespachadoA(Empleado despachadoA) {
        this.despachadoA = despachadoA;
    }
    
    

    @XmlTransient
    public Collection<Factura> getFacturaCollection() {
        return facturaCollection;
    }

    public void setFacturaCollection(Collection<Factura> facturaCollection) {
        this.facturaCollection = facturaCollection;
    }

    public Envio getOrdenEnvio() {
        return ordenEnvio;
    }

    public void setOrdenEnvio(Envio ordenEnvio) {
        this.ordenEnvio = ordenEnvio;
    }

    public Transaccion getIdtransac() {
        return idtransac;
    }

    public void setIdtransac(Transaccion idtransac) {
        this.idtransac = idtransac;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (numventa != null ? numventa.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Venta)) {
            return false;
        }
        Venta other = (Venta) object;
        if ((this.numventa == null && other.numventa != null) || (this.numventa != null && !this.numventa.equals(other.numventa))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Venta[ numventa=" + numventa + " ]";
    }
    
}
