/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ddicark.entidades;

import com.ddicark.auxiliares.funciones;
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
public class FacturaIngresoPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "DOCUMENTO_COMPRA")
    private String documentoCompra;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_DOCUMENTO")
    @Temporal(TemporalType.DATE)
    private Date fechaDocumento;

    public FacturaIngresoPK() {
    }

    public FacturaIngresoPK(String documentoCompra, Date fechaDocumento) {
        this.documentoCompra = documentoCompra;
        this.fechaDocumento = fechaDocumento;
    }

    public String getDocumentoCompra() {
        return documentoCompra;
    }

    public void setDocumentoCompra(String documentoCompra) {
        this.documentoCompra = documentoCompra;
    }

    public Date getFechaDocumento() {
        return fechaDocumento;
    }

    public void setFechaDocumento(Date fechaDocumento) {
        this.fechaDocumento = new funciones().setFechaFormateadaBD(fechaDocumento);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (documentoCompra != null ? documentoCompra.hashCode() : 0);
        hash += (fechaDocumento != null ? fechaDocumento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FacturaIngresoPK)) {
            return false;
        }
        FacturaIngresoPK other = (FacturaIngresoPK) object;
        if ((this.documentoCompra == null && other.documentoCompra != null) || (this.documentoCompra != null && !this.documentoCompra.equals(other.documentoCompra))) {
            return false;
        }
        if ((this.fechaDocumento == null && other.fechaDocumento != null) || (this.fechaDocumento != null && !this.fechaDocumento.equals(other.fechaDocumento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ddicark.entidades.FacturaIngresoPK[ documentoCompra=" + documentoCompra + ", fechaDocumento=" + fechaDocumento + " ]";
    }

    
}
