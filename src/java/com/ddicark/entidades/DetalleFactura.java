/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ddicark.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author DDICARK
 */
@Entity
@Table(name = "detalle_factura")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DetalleFactura.findByFactura", query = "SELECT d FROM DetalleFactura d WHERE d.factura = :factura "),
    @NamedQuery(name = "DetalleFactura.maxID", query = "SELECT MAX(d.id) FROM DetalleFactura d")})
public class DetalleFactura implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CANTIDAD")
    private int cantidad;
    @NotNull
    @Column(name = "TIPO")
    private Integer tipo;
    @Size(max = 15)
    @NotNull
    @Column(name = "ESTILO")
    private String estilo;
    @Size(max = 100)
    @Column(name = "COLORES")
    private String colores;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "PU")
    private BigDecimal pu;
    @Basic(optional = false)
    @NotNull
    @Column(name = "MONTO_LINEA")
    private BigDecimal montoLinea;
    @JoinColumns({
        @JoinColumn(name = "NUMFACTURA", referencedColumnName = "NUMFACTURA"),
        @JoinColumn(name = "FECHA_FACTURA", referencedColumnName = "FECHA_FACTURA")})
    @ManyToOne(optional = false)
    private Factura factura;
    @OneToMany(mappedBy = "lineaVenta")
    private Collection<DetalleEnvio> detalleEnvioCollection;

    public DetalleFactura() {
    }

    public DetalleFactura(Integer id) {
        this.id = id;
    }

    public DetalleFactura(Integer id, int cantidad, BigDecimal pu, BigDecimal montoLinea) {
        this.id = id;
        this.cantidad = cantidad;
        this.pu = pu;
        this.montoLinea = montoLinea;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }

    public String getEstilo() {
        return estilo;
    }

    public void setEstilo(String estilo) {
        this.estilo = estilo;
    }

    public String getColores() {
        return colores;
    }

    public void setColores(String colores) {
        this.colores = colores;
    }

    public BigDecimal getPu() {
        return pu;
    }

    public void setPu(BigDecimal pu) {
        this.pu = pu;
    }

    public BigDecimal getMontoLinea() {
        return montoLinea;
    }

    public void setMontoLinea(BigDecimal montoLinea) {
        this.montoLinea = montoLinea;
    }

    public Factura getFactura() {
        return factura;
    }

    public void setFactura(Factura factura) {
        this.factura = factura;
    }
    
    @XmlTransient
    public Collection<DetalleEnvio> getDetalleEnvioCollection() {
        return detalleEnvioCollection;
    }

    public void setDetalleEnvioCollection(Collection<DetalleEnvio> detalleEnvioCollection) {
        this.detalleEnvioCollection = detalleEnvioCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DetalleFactura)) {
            return false;
        }
        DetalleFactura other = (DetalleFactura) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ddicark.entidades.DetalleFactura[ id=" + id + " ]";
    }
    
}
