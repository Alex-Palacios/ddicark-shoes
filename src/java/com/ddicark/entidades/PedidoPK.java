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
public class PedidoPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "NUMPEDIDO")
    private String numpedido;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_PEDIDO")
    @Temporal(TemporalType.DATE)
    private Date fechaPedido;

    public PedidoPK() {
    }

    public PedidoPK(String numpedido, Date fechaPedido) {
        this.numpedido = numpedido;
        this.fechaPedido = fechaPedido;
    }

    public String getNumpedido() {
        return numpedido;
    }

    public void setNumpedido(String numpedido) {
        this.numpedido = numpedido;
    }

    public Date getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(Date fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    @Override
   public int hashCode() {
        int hash = 0;
        hash += (numpedido != null ? numpedido.hashCode() : 0);
        hash += (fechaPedido != null ? fechaPedido.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PedidoPK)) {
            return false;
        }
        PedidoPK other = (PedidoPK) object;
        if ((this.numpedido == null && other.numpedido != null) || (this.numpedido != null && !this.numpedido.equals(other.numpedido))) {
            return false;
        }
        if ((this.fechaPedido == null && other.fechaPedido != null) || (this.fechaPedido != null && !this.fechaPedido.equals(other.fechaPedido))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ddicark.entidades.PedidoPK[ numpedido=" + numpedido + ", fechaPedido=" + fechaPedido + " ]";
    }

    
}
