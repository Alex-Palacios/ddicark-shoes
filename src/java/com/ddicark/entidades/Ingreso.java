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
@Table(name = "ingreso")
@XmlRootElement
@NamedQueries({
   
    @NamedQuery(name = "Ingreso.maxID", query = "SELECT MAX(i.numingreso) FROM Ingreso i")})
public class Ingreso implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "NUMINGRESO")
    private Integer numingreso;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_REGISTRO")
    @Temporal(TemporalType.DATE)
    private Date fechaRegistro;
    @Lob
    @Size(max = 65535)
    @Column(name = "NOTA_INGRESO")
    private String notaIngreso;
    @JoinColumns({
        @JoinColumn(name = "DOCUMENTO_COMPRA", referencedColumnName = "DOCUMENTO_COMPRA"),
        @JoinColumn(name = "FECHA_DOCUMENTO", referencedColumnName = "FECHA_DOCUMENTO")})
    @ManyToOne(optional = false)
    private FacturaIngreso facturaIngreso;
    @JoinColumn(name = "IDTRANSAC", referencedColumnName = "IDTRANSAC")
    @ManyToOne(optional = false)
    private Transaccion idtransac;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "numingreso")
    private Collection<Inventario> inventarioCollection;

    public Ingreso() {
    }

    public Ingreso(Integer numingreso) {
        this.numingreso = numingreso;
    }

    public Ingreso(Integer numingreso, Date fechaRegistro) {
        this.numingreso = numingreso;
        this.fechaRegistro = fechaRegistro;
    }

    public Integer getNumingreso() {
        return numingreso;
    }

    public void setNumingreso(Integer numingreso) {
        this.numingreso = numingreso;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getNotaIngreso() {
        return notaIngreso;
    }

    public void setNotaIngreso(String notaIngreso) {
        this.notaIngreso = notaIngreso;
    }

    public FacturaIngreso getFacturaIngreso() {
        return facturaIngreso;
    }

    public void setFacturaIngreso(FacturaIngreso facturaIngreso) {
        this.facturaIngreso = facturaIngreso;
    }

    public Transaccion getIdtransac() {
        return idtransac;
    }

    public void setIdtransac(Transaccion idtransac) {
        this.idtransac = idtransac;
    }

    @XmlTransient
    public Collection<Inventario> getInventarioCollection() {
        return inventarioCollection;
    }

    public void setInventarioCollection(Collection<Inventario> inventarioCollection) {
        this.inventarioCollection = inventarioCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (numingreso != null ? numingreso.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Ingreso)) {
            return false;
        }
        Ingreso other = (Ingreso) object;
        if ((this.numingreso == null && other.numingreso != null) || (this.numingreso != null && !this.numingreso.equals(other.numingreso))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ddicark.entidades.Ingreso[ numingreso=" + numingreso + " ]";
    }
    
}
