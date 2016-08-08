/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ddicark.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "pago_compra")
@XmlRootElement
@NamedQueries({
    
    @NamedQuery(name = "PagoCompra.maxID", query = "SELECT MAX(p.idpagoCompra) FROM PagoCompra p") })
public class PagoCompra implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "IDPAGO_COMPRA")
    private Integer idpagoCompra;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "ABONO_PAGO_COMPRA")
    private BigDecimal abonoPagoCompra;
    @Column(name = "MORA_PAGO_COMPRA")
    private BigDecimal moraPagoCompra;
    @Column(name = "INTERES_PAGO_COMPRA")
    private BigDecimal interesPagoCompra;
    @Column(name = "TOTAL_PAGO_COMPRA")
    private BigDecimal totalPagoCompra;
    @JoinColumn(name = "IDTRANSAC", referencedColumnName = "IDTRANSAC")
    @ManyToOne(optional = false)
    private Transaccion idtransac;
    @JoinColumns({
        @JoinColumn(name = "DOCUMENTO_COMPRA", referencedColumnName = "DOCUMENTO_COMPRA"),
        @JoinColumn(name = "FECHA_DOCUMENTO", referencedColumnName = "FECHA_DOCUMENTO")})
    @ManyToOne(optional = false)
    private FacturaIngreso facturaIngreso;

    public PagoCompra() {
    }

    public PagoCompra(Integer idpagoCompra) {
        this.idpagoCompra = idpagoCompra;
    }

    public Integer getIdpagoCompra() {
        return idpagoCompra;
    }

    public void setIdpagoCompra(Integer idpagoCompra) {
        this.idpagoCompra = idpagoCompra;
    }

    public BigDecimal getAbonoPagoCompra() {
        return abonoPagoCompra;
    }

    public void setAbonoPagoCompra(BigDecimal abonoPagoCompra) {
        this.abonoPagoCompra = abonoPagoCompra;
    }

    public BigDecimal getMoraPagoCompra() {
        return moraPagoCompra;
    }

    public void setMoraPagoCompra(BigDecimal moraPagoCompra) {
        this.moraPagoCompra = moraPagoCompra;
    }

    public BigDecimal getInteresPagoCompra() {
        return interesPagoCompra;
    }

    public void setInteresPagoCompra(BigDecimal interesPagoCompra) {
        this.interesPagoCompra = interesPagoCompra;
    }

    public BigDecimal getTotalPagoCompra() {
        return totalPagoCompra;
    }

    public void setTotalPagoCompra(BigDecimal totalPagoCompra) {
        this.totalPagoCompra = totalPagoCompra;
    }

    public Transaccion getIdtransac() {
        return idtransac;
    }

    public void setIdtransac(Transaccion idtransac) {
        this.idtransac = idtransac;
    }

    
    public FacturaIngreso getFacturaIngreso() {
        return facturaIngreso;
    }

    public void setFacturaIngreso(FacturaIngreso facturaIngreso) {
        this.facturaIngreso = facturaIngreso;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idpagoCompra != null ? idpagoCompra.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PagoCompra)) {
            return false;
        }
        PagoCompra other = (PagoCompra) object;
        if ((this.idpagoCompra == null && other.idpagoCompra != null) || (this.idpagoCompra != null && !this.idpagoCompra.equals(other.idpagoCompra))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ddicark.entidades.PagoCompra[ idpagoCompra=" + idpagoCompra + " ]";
    }
    
}
