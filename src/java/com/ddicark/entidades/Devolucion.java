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
@Table(name = "devolucion")
@XmlRootElement
@NamedQueries({

    @NamedQuery(name = "Devolucion.maxID", query = "SELECT MAX(d.numdevolucion) FROM Devolucion d"),
    @NamedQuery(name = "Devolucion.Registradas", query = "SELECT d FROM Devolucion d WHERE d.estadoDevolucion = 'REGISTRADA' ") })
public class Devolucion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "NUMDEVOLUCION")
    private Integer numdevolucion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "UNIDADES_DEVOLUCION")
    private int unidadesDevolucion;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "MONTO_DEVOLUCION")
    private BigDecimal montoDevolucion;
    @Basic
    @NotNull
    @Column(name = "DESCUENTO_DEVOLUCION")
    private BigDecimal descuentoDevolucion;
    @Basic
    @NotNull
    @Column(name = "TOTAL_DEVOLUCION")
    private BigDecimal totalDevolucion;
    @Basic
    @NotNull
    @Column(name = "SALDO_DEVOLUCION")
    private BigDecimal saldoDevolucion;
    @NotNull
    @Size(max = 20)
    @Column(name = "ESTADO_DEVOLUCION")
    private String estadoDevolucion;
    @Lob
    @Size(max = 65535)
    @Column(name = "NOTA_DEVOLUCION")
    private String notaDevolucion;
    @Column(name = "FECHA_DEVOLUCION")
    @Temporal(TemporalType.DATE)
    private Date fechaDevolucion;
    @JoinColumns({
        @JoinColumn(name = "NUMFACTURA", referencedColumnName = "NUMFACTURA"),
        @JoinColumn(name = "FECHA_FACTURA", referencedColumnName = "FECHA_FACTURA")})
    @ManyToOne(optional = false)
    private Factura factura;
    
    @JoinColumn(name = "RESPONSABLE", referencedColumnName = "CODEMPLEADO")
    @ManyToOne
    private Empleado responsable;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "devolucion")
    private Collection<DevolucionFacturas> devolucionFacturasCollection;

    public Devolucion() {
    }

    public Devolucion(Integer numdevolucion) {
        this.numdevolucion = numdevolucion;
    }

    public Devolucion(Integer numdevolucion, int unidadesDevolucion, BigDecimal montoDevolucion) {
        this.numdevolucion = numdevolucion;
        this.unidadesDevolucion = unidadesDevolucion;
        this.montoDevolucion = montoDevolucion;
    }

    public Integer getNumdevolucion() {
        return numdevolucion;
    }

    public void setNumdevolucion(Integer numdevolucion) {
        this.numdevolucion = numdevolucion;
    }

    public int getUnidadesDevolucion() {
        return unidadesDevolucion;
    }

    public void setUnidadesDevolucion(int unidadesDevolucion) {
        this.unidadesDevolucion = unidadesDevolucion;
    }

    

    public BigDecimal getMontoDevolucion() {
        return montoDevolucion;
    }

    public void setMontoDevolucion(BigDecimal montoDevolucion) {
        this.montoDevolucion = montoDevolucion;
    }

    public String getNotaDevolucion() {
        return notaDevolucion;
    }

    public void setNotaDevolucion(String notaDevolucion) {
        this.notaDevolucion = notaDevolucion;
    }

    public Factura getFactura() {
        return factura;
    }

    public void setFactura(Factura factura) {
        this.factura = factura;
    }

    public Empleado getResponsable() {
        return responsable;
    }

    public void setResponsable(Empleado responsable) {
        this.responsable = responsable;
    }

    public BigDecimal getDescuentoDevolucion() {
        return descuentoDevolucion;
    }

    public void setDescuentoDevolucion(BigDecimal descuentoDevolucion) {
        this.descuentoDevolucion = descuentoDevolucion;
    }


    public BigDecimal getTotalDevolucion() {
        return totalDevolucion;
    }

    public void setTotalDevolucion(BigDecimal totalDevolucion) {
        this.totalDevolucion = totalDevolucion;
    }

    public BigDecimal getSaldoDevolucion() {
        return saldoDevolucion;
    }

    public String getEstadoDevolucion() {
        return estadoDevolucion;
    }

    public void setEstadoDevolucion(String estadoDevolucion) {
        this.estadoDevolucion = estadoDevolucion;
    }
    
    

    public void setSaldoDevolucion(BigDecimal saldoDevolucion) {
        this.saldoDevolucion = saldoDevolucion;
    }

    public Date getFechaDevolucion() {
        return fechaDevolucion;
    }

    public void setFechaDevolucion(Date fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
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
        hash += (numdevolucion != null ? numdevolucion.hashCode() : 0);
        return hash;
    }


    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Devolucion)) {
            return false;
        }
        Devolucion other = (Devolucion) object;
        if ((this.numdevolucion == null && other.numdevolucion != null) || (this.numdevolucion != null && !this.numdevolucion.equals(other.numdevolucion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ddicark.entidades.Devolucion[ numdevolucion=" + numdevolucion + " ]";
    }
    
}
