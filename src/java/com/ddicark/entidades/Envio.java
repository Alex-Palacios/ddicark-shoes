/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ddicark.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.persistence.OneToOne;
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
@Table(name = "envio")
@XmlRootElement
@NamedQueries({
    
    @NamedQuery(name = "Envio.maxID", query = "SELECT MAX(e.numenvio) FROM Envio e"),
    @NamedQuery(name = "Envio.enProcesoVenta", query = "SELECT e FROM Envio e WHERE e.estado in ('EMPACADO','FACTURADO') "),
    @NamedQuery(name = "Envio.byPedido", query = "SELECT e FROM Envio e WHERE e.pedido = :pedido  "),
    @NamedQuery(name = "Envio.byMonthYear", query = "SELECT e FROM Envio e WHERE FUNC('YEAR',e.fechaEmpaquetado) = :year AND  FUNC('MONTH',e.fechaEmpaquetado) = :month ") })
public class Envio implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "NUMENVIO")
    private Integer numenvio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_EMPAQUETADO")
    @Temporal(TemporalType.DATE)
    private Date fechaEmpaquetado;
    @Basic(optional = false)
    @NotNull
    @Column(name = "TOTAL_UNIDADES")
    private int totalUnidades;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "SUB_TOTAL")
    private BigDecimal subTotal;
    @Size(max = 20)
    @Column(name = "ESTADO")
    private String estado;
    @Lob
    @Size(max = 65535)
    @Column(name = "NOTA")
    private String nota;
    
    @JoinColumn(name = "VENDEDOR", referencedColumnName = "CODEMPLEADO")
    @ManyToOne(optional = false)
    private Empleado vendedor;
    @JoinColumn(name = "DESPACHO", referencedColumnName = "CODEMPLEADO")
    @ManyToOne(optional = false)
    private Empleado despacho;
    
    @JoinColumns({
        @JoinColumn(name = "NUMPEDIDO", referencedColumnName = "NUMPEDIDO"),
        @JoinColumn(name = "FECHA_PEDIDO", referencedColumnName = "FECHA_PEDIDO")})
    @ManyToOne(optional = false)
    private Pedido pedido;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "envio")
    private Collection<DetalleEnvio> detalleEnvioCollection;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "ordenEnvio")
    private Venta venta;

    public Envio() {
    }

    public Envio(Integer numenvio) {
        this.numenvio = numenvio;
    }

    public Envio(Integer numenvio, Date fechaEmpaquetado, int totalUnidades, BigDecimal subTotal, Empleado vendedor, Empleado despacho) {
        this.numenvio = numenvio;
        this.fechaEmpaquetado = fechaEmpaquetado;
        this.totalUnidades = totalUnidades;
        this.subTotal = subTotal;
        this.vendedor = vendedor;
        this.despacho = despacho;
    }

    public Integer getNumenvio() {
        return numenvio;
    }

    public void setNumenvio(Integer numenvio) {
        this.numenvio = numenvio;
    }

    public Date getFechaEmpaquetado() {
        return fechaEmpaquetado;
    }

    public void setFechaEmpaquetado(Date fechaEmpaquetado) {
        this.fechaEmpaquetado = fechaEmpaquetado;
    }

    public int getTotalUnidades() {
        return totalUnidades;
    }

    public void setTotalUnidades(int totalUnidades) {
        this.totalUnidades = totalUnidades;
    }

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(BigDecimal subTotal) {
        this.subTotal = subTotal;
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
    

    public Empleado getVendedor() {
        return vendedor;
    }

    public void setVendedor(Empleado vendedor) {
        this.vendedor = vendedor;
    }

    public Empleado getDespacho() {
        return despacho;
    }

    public void setDespacho(Empleado despacho) {
        this.despacho = despacho;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public Venta getVenta() {
        return venta;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }
    
    
    @XmlTransient
    public Collection<DetalleEnvio> getDetalleEnvioCollection() {
        return detalleEnvioCollection;
    }

    public void setDetalleEnvioCollection(Collection<DetalleEnvio> detalleEnvioCollection) {
        this.detalleEnvioCollection = detalleEnvioCollection;
    }

       
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (numenvio != null ? numenvio.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Envio)) {
            return false;
        }
        Envio other = (Envio) object;
        if ((this.numenvio == null && other.numenvio != null) || (this.numenvio != null && !this.numenvio.equals(other.numenvio))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ddicark.entidades.Envio[ numenvio=" + numenvio + " ]";
    }
    
}
