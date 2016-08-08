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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author DDICARK
 */
@Entity
@Table(name = "detalle_pedido")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DetallePedido.detalleByPedido", query = "SELECT d FROM DetallePedido d WHERE d.pedido.pedidoPK = :pedidoPK"),
    @NamedQuery(name = "DetallePedido.existeProductoPedido", query = "SELECT d FROM DetallePedido d WHERE d.pedido.pedidoPK = :pedidoPK AND d.producto = :producto AND d.color = :color AND d.talla = :talla "),
    @NamedQuery(name = "DetallePedido.maxID", query = "SELECT MAX(d.correlativo) FROM DetallePedido d")})
public class DetallePedido implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "CORRELATIVO")
    private Integer correlativo;
    @Size(max = 30)
    @Column(name = "COLOR")
    private String color;
    @Size(max = 6)
    @Column(name = "TALLA")
    private String talla;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CANTIDAD")
    private int cantidad;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "PU")
    private BigDecimal pu;
    @Column(name = "MONTO")
    private BigDecimal monto;
    @JoinColumns({
        @JoinColumn(name = "NUMPEDIDO", referencedColumnName = "NUMPEDIDO"),
        @JoinColumn(name = "FECHA_PEDIDO", referencedColumnName = "FECHA_PEDIDO")})
    @ManyToOne(optional = false)
    private Pedido pedido;
    @JoinColumns({
        @JoinColumn(name = "CODESTILO", referencedColumnName = "CODESTILO"),
        @JoinColumn(name = "TIPO_PRODUCTO", referencedColumnName = "TIPO_PRODUCTO")})
    @ManyToOne(optional = false)
    private Producto producto;

    public DetallePedido() {
    }

    public DetallePedido(Integer correlativo) {
        this.correlativo = correlativo;
    }

    public DetallePedido(Integer correlativo, int cantidad) {
        this.correlativo = correlativo;
        this.cantidad = cantidad;
    }

    public Integer getCorrelativo() {
        return correlativo;
    }

    public void setCorrelativo(Integer correlativo) {
        this.correlativo = correlativo;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getTalla() {
        return talla;
    }

    public void setTalla(String talla) {
        this.talla = talla;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPu() {
        return pu;
    }

    public void setPu(BigDecimal pu) {
        this.pu = pu;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    
    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (correlativo != null ? correlativo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DetallePedido)) {
            return false;
        }
        DetallePedido other = (DetallePedido) object;
        if ((this.correlativo == null && other.correlativo != null) || (this.correlativo != null && !this.correlativo.equals(other.correlativo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ddicark.DetallePedido[ correlativo=" + correlativo + " ]";
    }
    
}
