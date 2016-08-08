/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ddicark.entidades;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author DDICARK
 */
@Entity
@Table(name = "transaccion")
@XmlRootElement
@NamedQueries({
    
    @NamedQuery(name = "Transaccion.findByTipoTransac", query = "SELECT t FROM Transaccion t WHERE t.tipoTransac = :tipoTransac"),
    @NamedQuery(name = "Transaccion.maxID", query = "SELECT MAX(t.idtransac) FROM Transaccion t")})
public class Transaccion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "IDTRANSAC")
    private Integer idtransac;
    @Basic(optional = false)
    @NotNull
    @Column(name = "TIPO_TRANSAC")
    private int tipoTransac;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_TRANSAC")
    @Temporal(TemporalType.DATE)
    private Date fechaTransac;
    @Column(name = "HORA_TRANSAC")
    @Temporal(TemporalType.TIME)
    private Date horaTransac;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idtransac")
    private Collection<PagoVenta> pagoVentaCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idtransac")
    private Collection<PagoCompra> pagoCompraCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idtransac")
    private Collection<Ingreso> ingresoCollection;
    @JoinColumn(name = "RESPONSABLE_TRANSAC", referencedColumnName = "CODEMPLEADO")
    @ManyToOne(optional = false)
    private Empleado responsableTransac;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idtransac")
    private Collection<Venta> ventaCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idtransac")
    private Collection<Retaceo> retaceoCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idtransac")
    private Collection<Remesa> remesasCollection;

    public Transaccion() {
    }

    public Transaccion(Integer idtransac) {
        this.idtransac = idtransac;
    }

    public Transaccion(Integer idtransac, int tipoTransac, Date fechaTransac) {
        this.idtransac = idtransac;
        this.tipoTransac = tipoTransac;
        this.fechaTransac = fechaTransac;
    }

    public Integer getIdtransac() {
        return idtransac;
    }

    public void setIdtransac(Integer idtransac) {
        this.idtransac = idtransac;
    }

    public int getTipoTransac() {
        return tipoTransac;
    }

    public void setTipoTransac(int tipoTransac) {
        this.tipoTransac = tipoTransac;
    }

    public Date getFechaTransac() {
        return fechaTransac;
    }

    public void setFechaTransac(Date fechaTransac) {
        this.fechaTransac = fechaTransac;
    }

    public Date getHoraTransac() {
        return horaTransac;
    }

    public void setHoraTransac(Date horaTransac) {
        this.horaTransac = horaTransac;
    }

    @XmlTransient
    public Collection<PagoVenta> getPagoVentaCollection() {
        return pagoVentaCollection;
    }

    public void setPagoVentaCollection(Collection<PagoVenta> pagoVentaCollection) {
        this.pagoVentaCollection = pagoVentaCollection;
    }
    
    @XmlTransient
    public Collection<PagoCompra> getPagoCompraCollection() {
        return pagoCompraCollection;
    }

    public void setPagoCompraCollection(Collection<PagoCompra> pagoCompraCollection) {
        this.pagoCompraCollection = pagoCompraCollection;
    }

    @XmlTransient
    public Collection<Ingreso> getIngresoCollection() {
        return ingresoCollection;
    }

    public void setIngresoCollection(Collection<Ingreso> ingresoCollection) {
        this.ingresoCollection = ingresoCollection;
    }

    public Empleado getResponsableTransac() {
        return responsableTransac;
    }

    public void setResponsableTransac(Empleado responsableTransac) {
        this.responsableTransac = responsableTransac;
    }

    @XmlTransient
    public Collection<Venta> getVentaCollection() {
        return ventaCollection;
    }

    public void setVentaCollection(Collection<Venta> ventaCollection) {
        this.ventaCollection = ventaCollection;
    }

    @XmlTransient
    public Collection<Retaceo> getRetaceoCollection() {
        return retaceoCollection;
    }

    public void setRetaceoCollection(Collection<Retaceo> retaceoCollection) {
        this.retaceoCollection = retaceoCollection;
    }

    @XmlTransient
    public Collection<Remesa> getRemesasCollection() {
        return remesasCollection;
    }

    public void setRemesasCollection(Collection<Remesa> remesasCollection) {
        this.remesasCollection = remesasCollection;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idtransac != null ? idtransac.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Transaccion)) {
            return false;
        }
        Transaccion other = (Transaccion) object;
        if ((this.idtransac == null && other.idtransac != null) || (this.idtransac != null && !this.idtransac.equals(other.idtransac))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ddicark.entidades.Transaccion[ idtransac=" + idtransac + " ]";
    }
    
}
