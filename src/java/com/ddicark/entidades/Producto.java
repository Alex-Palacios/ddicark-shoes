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
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Lob;
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
 * @author DDICARK
 */
@Entity
@Table(name = "producto")
@XmlRootElement
@NamedQueries({
   
    @NamedQuery(name = "Producto.findByTipoProducto", query = "SELECT p FROM Producto p WHERE p.productoPK.tipoProducto = :tipoProducto"),
    @NamedQuery(name = "Producto.findByCodestilo", query = "SELECT p FROM Producto p WHERE p.productoPK.codestilo = :codestilo") })
public class Producto implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ProductoPK productoPK;
    @Size(max = 30)
    @Column(name = "NOMBRE_ESTILO")
    private String nombreEstilo;
    @Lob
    @Size(max = 65535)
    @Column(name = "DETALLE_ESTILO")
    private String detalleEstilo;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "PRECIOVENTA_MAYOREO")
    private BigDecimal precioventaMayoreo;
    @Column(name = "PRECIOVENTA_UNIDAD")
    private BigDecimal precioventaUnidad;
    @Lob
    @Size(max = 65535)
    @Column(name = "PATH_PICTURE")
    private String pathPicture;
    @Size(max = 20)
    @Column(name = "TIPO_MIME")
    private String tipoMime;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "producto")
    private Collection<Inventario> inventarioCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "estilo")
    private Collection<Preingreso> preingresoCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "producto")
    private Collection<DetallePedido> detallePedidoCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "producto")
    private Collection<Caja> cajaCollection;
    
    
    public Producto() {
    }

    public Producto(ProductoPK productoPK) {
        this.productoPK = productoPK;
    }

    public Producto(ProductoPK productoPK, String nombreEstilo) {
        this.productoPK = productoPK;
        this.nombreEstilo = nombreEstilo;
    }

    public Producto(int tipoProducto, String codestilo) {
        this.productoPK = new ProductoPK(tipoProducto, codestilo);
    }

    public ProductoPK getProductoPK() {
        return productoPK;
    }

    public void setProductoPK(ProductoPK productoPK) {
        this.productoPK = productoPK;
    }

    public String getNombreEstilo() {
        return nombreEstilo;
    }

    public void setNombreEstilo(String nombreEstilo) {
        this.nombreEstilo = nombreEstilo;
    }

    public String getDetalleEstilo() {
        return detalleEstilo;
    }

    public void setDetalleEstilo(String detalleEstilo) {
        this.detalleEstilo = detalleEstilo;
    }

    public BigDecimal getPrecioventaMayoreo() {
        return precioventaMayoreo;
    }

    public void setPrecioventaMayoreo(BigDecimal precioventaMayoreo) {
        this.precioventaMayoreo = precioventaMayoreo;
    }

    public BigDecimal getPrecioventaUnidad() {
        return precioventaUnidad;
    }

    public void setPrecioventaUnidad(BigDecimal precioventaUnidad) {
        this.precioventaUnidad = precioventaUnidad;
    }

    public String getPathPicture() {
        return this.pathPicture;
    }

    public void setPathPicture(String pathPicture) {
        this.pathPicture = pathPicture;
    }

    public String getTipoMime() {
        return tipoMime;
    }

    public void setTipoMime(String tipoMime) {
        this.tipoMime = tipoMime;
    }

    @XmlTransient
    public Collection<Inventario> getInventarioCollection() {
        return inventarioCollection;
    }

    public void setInventarioCollection(Collection<Inventario> inventarioCollection) {
        this.inventarioCollection = inventarioCollection;
    }
    
    @XmlTransient
    public Collection<Preingreso> getPreingresoCollection() {
        return preingresoCollection;
    }

    public void setPreingresoCollection(Collection<Preingreso> preingresoCollection) {
        this.preingresoCollection = preingresoCollection;
    }

     @XmlTransient
    public Collection<DetallePedido> getDetallePedidoCollection() {
        return detallePedidoCollection;
    }

    public void setDetallePedidoCollection(Collection<DetallePedido> detallePedidoCollection) {
        this.detallePedidoCollection = detallePedidoCollection;
    }
    
     @XmlTransient
    public Collection<Caja> getCajaCollection() {
        return cajaCollection;
    }

    public void setCajaCollection(Collection<Caja> cajaCollection) {
        this.cajaCollection = cajaCollection;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (productoPK != null ? productoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Producto)) {
            return false;
        }
        Producto other = (Producto) object;
        if ((this.productoPK == null && other.productoPK != null) || (this.productoPK != null && !this.productoPK.equals(other.productoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ddicark.entidades.Producto[ productoPK=" + productoPK + " ]";
    }

   
    
}
