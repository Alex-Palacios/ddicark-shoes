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
public class ProductoPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "TIPO_PRODUCTO")
    private int tipoProducto;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "CODESTILO")
    private String codestilo;

    public ProductoPK() {
    }

    public ProductoPK(int tipoProducto, String codestilo) {
        this.tipoProducto = tipoProducto;
        this.codestilo = codestilo;
    }

    public int getTipoProducto() {
        return tipoProducto;
    }

    public void setTipoProducto(int tipoProducto) {
        this.tipoProducto = tipoProducto;
    }

    public String getCodestilo() {
        return codestilo;
    }

    public void setCodestilo(String codestilo) {
        this.codestilo = codestilo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) tipoProducto;
        hash += (codestilo != null ? codestilo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProductoPK)) {
            return false;
        }
        ProductoPK other = (ProductoPK) object;
        if (this.tipoProducto != other.tipoProducto) {
            return false;
        }
        if ((this.codestilo == null && other.codestilo != null) || (this.codestilo != null && !this.codestilo.equals(other.codestilo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ddicark.entidades.ProductoPK[ tipoProducto=" + tipoProducto + ", codestilo=" + codestilo + " ]";
    }
    
}
