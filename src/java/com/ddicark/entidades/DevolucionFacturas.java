/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ddicark.entidades;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author DDICARK
 */
@Entity
@Table(name = "devolucion_facturas")
@XmlRootElement
@NamedQueries({
    
    @NamedQuery(name = "DevolucionFacturas.maxID", query = "SELECT MAX(d.id) FROM DevolucionFacturas d"),
    @NamedQuery(name = "DevolucionFacturas.findByDevolucion", query = "SELECT d FROM DevolucionFacturas d WHERE d.devolucion = :devolucion") })
public class DevolucionFacturas implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Integer id;
    
    @JoinColumns({
        @JoinColumn(name = "FACTURA", referencedColumnName = "NUMFACTURA"),
        @JoinColumn(name = "FECHA", referencedColumnName = "FECHA_FACTURA")})
    @ManyToOne(optional = false)
    private Factura factura;

    @JoinColumn(name = "DEVOLUCION", referencedColumnName = "NUMDEVOLUCION")
    @ManyToOne(optional = false)
    private Devolucion devolucion;
    
    
    public DevolucionFacturas() {
    }

    public DevolucionFacturas(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Factura getFactura() {
        return factura;
    }

    public void setFactura(Factura factura) {
        this.factura = factura;
    }

    public Devolucion getDevolucion() {
        return devolucion;
    }

    public void setDevolucion(Devolucion devolucion) {
        this.devolucion = devolucion;
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
        if (!(object instanceof DevolucionFacturas)) {
            return false;
        }
        DevolucionFacturas other = (DevolucionFacturas) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ddicark.entidades.DevolucionFacturas[ id=" + id + " ]";
    }
    
}
