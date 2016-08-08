/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ddicark.entidades;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author DDICARK
 */
@Embeddable
public class FacturaPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "NUMFACTURA")
    private String numfactura;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_FACTURA")
    @Temporal(TemporalType.DATE)
    private Date fechaFactura;

    public FacturaPK() {
    }

    public FacturaPK(String numfactura, Date fechaFactura) {
        this.numfactura = numfactura;
        this.fechaFactura = fechaFactura;
    }

    public String getNumfactura() {
        return numfactura;
    }

    public void setNumfactura(String numfactura) {
        this.numfactura = numfactura;
    }

    public Date getFechaFactura() {
        return fechaFactura;
    }

    public void setFechaFactura(Date fechaFactura) {
        this.fechaFactura = fechaFactura;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (numfactura != null ? numfactura.hashCode() : 0);;
        hash += (fechaFactura != null ? fechaFactura.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FacturaPK)) {
            return false;
        }
        FacturaPK other = (FacturaPK) object;
        if (this.numfactura != other.numfactura) {
            return false;
        }
        if ((this.fechaFactura == null && other.fechaFactura != null) || (this.fechaFactura != null && !this.fechaFactura.equals(other.fechaFactura))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ddicark.entidades.FacturaPK[ numfactura=" + numfactura + ", fechaFactura=" + fechaFactura + " ]";
    }
    
}
