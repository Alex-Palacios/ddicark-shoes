/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ddicark.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import javax.persistence.Basic;
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
@Table(name = "caja")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Caja.findByNumcaja", query = "SELECT c FROM Caja c WHERE c.numcaja = :numcaja"),
    @NamedQuery(name = "Caja.coloresByEstilo", query = "SELECT DISTINCT(c.coloresCaja) FROM Caja c WHERE c.producto = :estilo"),
    @NamedQuery(name = "Caja.byEstiloColor", query = "SELECT c FROM Caja c WHERE c.producto = :estilo AND c.coloresCaja = :colores") })
public class Caja implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "NUMCAJA")
    private String numcaja;
    @Lob
    @Size(max = 65535)
    @Column(name = "COLORES")
    private String coloresCaja;
    @Lob
    @Size(max = 65535)
    @Column(name = "DETALLE_CAJA")
    private String detalleCaja;
    @Basic(optional = false)
    @NotNull
    @Column(name = "UNIDADES_CAJA")
    private int unidadesCaja;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "COSTO_UNITARIO")
    private BigDecimal costoUnitario;
    @Column(name = "COSTO_UNITARIO_REAL")
    private BigDecimal costoUnitarioReal;
    @Basic(optional = false)
    @NotNull
    @Column(name = "COSTO_CAJA")
    private BigDecimal costoCaja;
    @Column(name = "PRECIOVENTA_UNIDAD")
    private BigDecimal precioventaUnidad;
    @Lob
    @Size(max = 65535)
    @Column(name = "UBICACION_CAJA")
    private String ubicacionCaja;
    @Basic(optional = false)
    @NotNull
    @Column(name = "COMPLETA")
    private boolean completa;
    @JoinColumns({
        @JoinColumn(name = "ESTILO", referencedColumnName = "CODESTILO"),
        @JoinColumn(name = "TIPO_PRODUCTO", referencedColumnName = "TIPO_PRODUCTO")})
    @ManyToOne
    private Producto producto;
    @OneToMany(mappedBy = "numcaja")
    private Collection<Inventario> inventarioCollection;

    public Caja() {
    }

    public Caja(String numcaja) {
        this.numcaja = numcaja;
    }

    public Caja(String numcaja, int unidadesCaja, BigDecimal costoCaja, boolean completa) {
        this.numcaja = numcaja;
        this.unidadesCaja = unidadesCaja;
        this.costoCaja = costoCaja;
        this.completa = completa;
    }

    public String getNumcaja() {
        return numcaja;
    }

    public void setNumcaja(String numcaja) {
        this.numcaja = numcaja;
    }

    public String getDetalleCaja() {
        return detalleCaja;
    }

    public void setDetalleCaja(String detalleCaja) {
        this.detalleCaja = detalleCaja;
    }

    public int getUnidadesCaja() {
        return unidadesCaja;
    }

    public void setUnidadesCaja(int unidadesCaja) {
        this.unidadesCaja = unidadesCaja;
    }

    public BigDecimal getCostoCaja() {
        return costoCaja;
    }

    public void setCostoCaja(BigDecimal costoCaja) {
        this.costoCaja = costoCaja;
    }

    public String getColoresCaja() {
        return coloresCaja;
    }

    public void setColoresCaja(String coloresCaja) {
        this.coloresCaja = coloresCaja;
    }

    public BigDecimal getPrecioventaUnidad() {
        return precioventaUnidad;
    }

    public void setPrecioventaUnidad(BigDecimal precioventaUnidad) {
        this.precioventaUnidad = precioventaUnidad;
    }
    
    public String getUbicacionCaja() {
        return ubicacionCaja;
    }

    public void setUbicacionCaja(String ubicacionCaja) {
        this.ubicacionCaja = ubicacionCaja;
    }

    public boolean getCompleta() {
        return completa;
    }

    public void setCompleta(boolean completa) {
        this.completa = completa;
    }

    public BigDecimal getCostoUnitario() {
        return costoUnitario;
    }

    public void setCostoUnitario(BigDecimal costoUnitario) {
        this.costoUnitario = costoUnitario;
    }

    public BigDecimal getCostoUnitarioReal() {
        return costoUnitarioReal;
    }

    public void setCostoUnitarioReal(BigDecimal costoUnitarioReal) {
        this.costoUnitarioReal = costoUnitarioReal;
    }
    
    

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    
    @XmlTransient
    public Collection<Inventario> getInventarioCollection() {
        return inventarioCollection;
    }

    public void setInventarioCollection(Collection<Inventario> inventarioCollection) {
        this.inventarioCollection = inventarioCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (numcaja != null ? numcaja.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Caja)) {
            return false;
        }
        Caja other = (Caja) object;
        if ((this.numcaja == null && other.numcaja != null) || (this.numcaja != null && !this.numcaja.equals(other.numcaja))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ddicark.entidades.Caja[ numcaja=" + numcaja + " ]";
    }
    
}
