/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ddicark.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author DDICARK
 */
@Entity
@Table(name = "detalle_envio")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DetalleEnvio.findByNumenvio", query = "SELECT d FROM DetalleEnvio d WHERE d.detalleEnvioPK.numenvio = :numenvio"),
    @NamedQuery(name = "DetalleEnvio.findByLineaVenta", query = "SELECT d FROM DetalleEnvio d WHERE d.lineaVenta = :lineaVenta"),
    @NamedQuery(name = "DetalleEnvio.reingresosPorDevolucion", query = "SELECT d FROM DetalleEnvio d WHERE d.nota = 'REINGRESO' "),
    @NamedQuery(name = "DetalleEnvio.reingresoPorAnulacion", query = "SELECT d FROM DetalleEnvio d WHERE d.nota IN ('ANULADO','REFACTURAR') AND d.lineaVenta.factura = :factura "),
    @NamedQuery(name = "DetalleEnvio.curvaEstilo", query = "SELECT DISTINCT(d.inventario.talla) FROM DetalleEnvio d WHERE d.detalleEnvioPK.numenvio = :numenvio AND d.inventario.producto.productoPK.codestilo = :estilo ORDER BY d.inventario.talla "),
    @NamedQuery(name = "DetalleEnvio.precioUnitarioEstilo", query = "SELECT DISTINCT(d.precioFacturar) FROM DetalleEnvio d WHERE d.detalleEnvioPK.numenvio = :numenvio AND d.inventario.producto.productoPK.codestilo = :estilo AND d.inventario.color = :color "),
    @NamedQuery(name = "DetalleEnvio.unidadesDespachadasByPedido", query = "SELECT COUNT(d) FROM DetalleEnvio d WHERE d.envio.pedido = :pedido AND d.envio.estado <> 'CANCELADO' "),
    @NamedQuery(name = "DetalleEnvio.unidadesNI", query = "SELECT COUNT(d) FROM DetalleEnvio d WHERE d.detalleEnvioPK.numenvio = :numenvio AND d.detalleEnvioPK.codigoProducto LIKE 'NI%' ") })
public class DetalleEnvio implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected DetalleEnvioPK detalleEnvioPK;
    @Column(name = "PRECIO_FACTURAR")
    private BigDecimal precioFacturar;
    @Basic
    @Size(max = 30)
    @Column(name = "NOTA")
    private String nota;
    @JoinColumn(name = "CODIGO_PRODUCTO", referencedColumnName = "CODIGO", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Inventario inventario;
    @JoinColumn(name = "NUMENVIO", referencedColumnName = "NUMENVIO", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Envio envio;
    @JoinColumn(name = "LINEA_VENTA", referencedColumnName = "ID")
    @ManyToOne
    private DetalleFactura lineaVenta;
    
    @JoinColumn(name = "CAMBIO_PRODUCTO", referencedColumnName = "CODIGO")
    @ManyToOne(optional = true)
    private Inventario cambioProducto;

    public DetalleEnvio() {
    }

    public DetalleEnvio(DetalleEnvioPK detalleEnvioPK) {
        this.detalleEnvioPK = detalleEnvioPK;
    }

    public DetalleEnvio(int numenvio, String codigoProducto) {
        this.detalleEnvioPK = new DetalleEnvioPK(numenvio, codigoProducto);
    }

    public DetalleEnvioPK getDetalleEnvioPK() {
        return detalleEnvioPK;
    }

    public void setDetalleEnvioPK(DetalleEnvioPK detalleEnvioPK) {
        this.detalleEnvioPK = detalleEnvioPK;
    }

    public BigDecimal getPrecioFacturar() {
        return precioFacturar;
    }

    public void setPrecioFacturar(BigDecimal precioFacturar) {
        this.precioFacturar = precioFacturar;
    }
    
    public Inventario getInventario() {
        return inventario;
    }

    public void setInventario(Inventario inventario) {
        this.inventario = inventario;
    }
    
    public Envio getEnvio() {
        return envio;
    }

    public void setEnvio(Envio envio) {
        this.envio = envio;
    }

    public DetalleFactura getLineaVenta() {
        return lineaVenta;
    }

    public void setLineaVenta(DetalleFactura lineaVenta) {
        this.lineaVenta = lineaVenta;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    public Inventario getCambioProducto() {
        return cambioProducto;
    }

    public void setCambioProducto(Inventario cambioProducto) {
        this.cambioProducto = cambioProducto;
    }

    
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (detalleEnvioPK != null ? detalleEnvioPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DetalleEnvio)) {
            return false;
        }
        DetalleEnvio other = (DetalleEnvio) object;
        if ((this.detalleEnvioPK == null && other.detalleEnvioPK != null) || (this.detalleEnvioPK != null && !this.detalleEnvioPK.equals(other.detalleEnvioPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ddicark.entidades.DetalleEnvio[ detalleEnvioPK=" + detalleEnvioPK + " ]";
    }
    
}
