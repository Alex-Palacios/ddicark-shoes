/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ddicark.entidades;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author DDICARK
 */
@Embeddable
public class DetalleEnvioPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "NUMENVIO")
    private int numenvio;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "CODIGO_PRODUCTO")
    private String codigoProducto;

    public DetalleEnvioPK() {
    }

    public DetalleEnvioPK(int numenvio, String codigoProducto) {
        this.numenvio = numenvio;
        this.codigoProducto = codigoProducto;
    }

    public int getNumenvio() {
        return numenvio;
    }

    public void setNumenvio(int numenvio) {
        this.numenvio = numenvio;
    }

    public String getCodigoProducto() {
        return codigoProducto;
    }

    public void setCodigoProducto(String codigoProducto) {
        this.codigoProducto = codigoProducto;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) numenvio;
        hash += (codigoProducto != null ? codigoProducto.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DetalleEnvioPK)) {
            return false;
        }
        DetalleEnvioPK other = (DetalleEnvioPK) object;
        if (this.numenvio != other.numenvio) {
            return false;
        }
        if ((this.codigoProducto == null && other.codigoProducto != null) || (this.codigoProducto != null && !this.codigoProducto.equals(other.codigoProducto))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ddicark.entidades.DetalleEnvioPK[ numenvio=" + numenvio + ", codigoProducto=" + codigoProducto + " ]";
    }
    
}
