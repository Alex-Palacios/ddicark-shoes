/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ddicark.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author ALEX
 */
@Entity
@Table(name = "remesa")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Remesa.findByBanco", query = "SELECT r FROM Remesa r WHERE r.banco = :banco"),
    @NamedQuery(name = "Remesa.maxID", query = "SELECT MAX(r.idremesa) FROM Remesa r"),
    @NamedQuery(name = "Remesa.byMonthYear", query = "SELECT r FROM Remesa r WHERE FUNC('YEAR',r.idtransac.fechaTransac) = :year AND  FUNC('MONTH',r.idtransac.fechaTransac) = :month ") })

public class Remesa implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "IDREMESA")
    private Integer idremesa;
    @Size(max = 50)
    @Column(name = "BANCO")
    private String banco;
    @Size(max = 50)
    @Column(name = "CUENTA")
    private String cuenta;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "TOTAL_REMESA")
    private BigDecimal totalRemesa;
    @Size(max = 20)
    @Column(name = "ESTADO")
    private String estado;
    @Lob
    @Size(max = 65535)
    @Column(name = "NOTA")
    private String nota;
    
    
    @JoinColumn(name = "IDTRANSAC", referencedColumnName = "IDTRANSAC")
    @ManyToOne(optional = false)
    private Transaccion idtransac;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "remesa")
    private Collection<PagoVenta> pagoVentaCollection;
    

    public Remesa() {
    }

    public Remesa(Integer idremesa) {
        this.idremesa = idremesa;
    }

    public Remesa(Integer idremesa, BigDecimal totalRemesa) {
        this.idremesa = idremesa;
        this.totalRemesa = totalRemesa;
    }

    public Integer getIdremesa() {
        return idremesa;
    }

    public void setIdremesa(Integer idremesa) {
        this.idremesa = idremesa;
    }

    public String getBanco() {
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }

    public String getCuenta() {
        return cuenta;
    }

    public void setCuenta(String cuenta) {
        this.cuenta = cuenta;
    }

    public BigDecimal getTotalRemesa() {
        return totalRemesa;
    }

    public void setTotalRemesa(BigDecimal totalRemesa) {
        this.totalRemesa = totalRemesa;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }
    
    public Transaccion getIdtransac() {
        return idtransac;
    }

    public void setIdtransac(Transaccion idtransac) {
        this.idtransac = idtransac;
    }

    @XmlTransient
    public Collection<PagoVenta> getPagoVentaCollection() {
        return pagoVentaCollection;
    }

    public void setPagoVentaCollection(Collection<PagoVenta> pagoVentaCollection) {
        this.pagoVentaCollection = pagoVentaCollection;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idremesa != null ? idremesa.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Remesa)) {
            return false;
        }
        Remesa other = (Remesa) object;
        if ((this.idremesa == null && other.idremesa != null) || (this.idremesa != null && !this.idremesa.equals(other.idremesa))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ddicark.entidades.Remesa[ idremesa=" + idremesa + " ]";
    }
    
}
