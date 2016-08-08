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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
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
@Table(name = "inventario")
@XmlRootElement
@NamedQueries({
   
    @NamedQuery(name = "Inventario.findByCodigo", query = "SELECT i FROM Inventario i WHERE i.codigo = :codigo"),
    @NamedQuery(name = "Inventario.findByMarca", query = "SELECT i FROM Inventario i WHERE i.marca = :marca"),
    @NamedQuery(name = "Inventario.findByMaterial", query = "SELECT i FROM Inventario i WHERE i.material = :material"),
    @NamedQuery(name = "Inventario.listaMaterial", query = "SELECT DISTINCT(i.material) FROM Inventario i WHERE i.material LIKE :query ORDER BY i.material "),
    @NamedQuery(name = "Inventario.listaMarca", query = "SELECT DISTINCT(i.marca) FROM Inventario i WHERE i.marca LIKE :query ORDER BY i.marca "),
    @NamedQuery(name = "Inventario.listaColoresQuery", query = "SELECT DISTINCT(i.color) FROM Inventario i WHERE i.color LIKE :query ORDER BY i.color "),
    @NamedQuery(name = "Inventario.listaColoresTipo", query = "SELECT DISTINCT(i.color) FROM Inventario i WHERE i.producto.productoPK.tipoProducto = :tipoProducto"),
    @NamedQuery(name = "Inventario.coloresByEstilo", query = "SELECT DISTINCT(i.color) FROM Inventario i WHERE i.producto = :estilo"),
    @NamedQuery(name = "Inventario.listaMarcasTipo", query = "SELECT DISTINCT(i.marca) FROM Inventario i WHERE i.producto.productoPK.tipoProducto = :tipoProducto"),
    @NamedQuery(name = "Inventario.listaMaterialesTipo", query = "SELECT DISTINCT(i.material) FROM Inventario i WHERE i.producto.productoPK.tipoProducto = :tipoProducto AND i.material IS NOT NULL"),
    @NamedQuery(name = "Inventario.listaMuestras", query = "SELECT i FROM Inventario i WHERE i.producto.productoPK.tipoProducto = :tipoProducto AND i.estadoproducto = 'MUESTRA'"),
    @NamedQuery(name = "Inventario.listaTallasCaja", query = "SELECT DISTINCT(i.talla) FROM Inventario i WHERE i.producto.productoPK.tipoProducto = :tipoProducto AND i.numcaja.numcaja = :numcaja AND i.estadoproducto in('EN EXISTENCIA','MUESTRA','DEFECTUOSO') "),
    @NamedQuery(name = "Inventario.curvaByCaja", query = "SELECT i.color, i.talla, COUNT(i) FROM Inventario i WHERE i.producto.productoPK.tipoProducto = :tipoProducto AND i.numcaja.numcaja = :numcaja AND i.estadoproducto in('EN EXISTENCIA','MUESTRA','DEFECTUOSO') GROUP BY i.color, i.talla "),
    @NamedQuery(name = "Inventario.contarArticulosCaja", query = "SELECT COUNT(i) FROM Inventario i WHERE i.producto.productoPK.tipoProducto = :tipoProducto AND i.numcaja.numcaja = :numcaja AND i.estadoproducto in('EN EXISTENCIA','MUESTRA','DEFECTUOSO')"),
    @NamedQuery(name = "Inventario.listarProductoCajaByEnvio", query = "SELECT i FROM Inventario i WHERE i.producto.productoPK.tipoProducto = :tipoProducto AND i.numcaja.numcaja = :numcaja AND i.estadoproducto in('EN EXISTENCIA','MUESTRA','DEFECTUOSO') "),
    @NamedQuery(name = "Inventario.detalleCaja", query = "SELECT i FROM Inventario i WHERE i.numcaja = :caja AND i.estadoproducto NOT IN('VENDIDO','PERDIDA')"),
    @NamedQuery(name = "Inventario.existenciasCaja", query = "SELECT COUNT(i) FROM Inventario i WHERE i.producto.productoPK.tipoProducto = :tipoProducto AND i.numcaja.numcaja = :caja AND i.estadoproducto = 'EN EXISTENCIA'"),
    @NamedQuery(name = "Inventario.unidadesFisicasByTipo", query = "SELECT i FROM Inventario i WHERE i.producto.productoPK.tipoProducto = :tipoProducto AND i.estadoproducto  IN ('EN EXISTENCIA') "),
    @NamedQuery(name = "Inventario.unidadesFisicasByTipoEnCaja", query = "SELECT i FROM Inventario i WHERE i.producto.productoPK.tipoProducto = :tipoProducto AND i.estadoproducto  IN ('EN EXISTENCIA','DEFECTUOSO') AND i.numcaja IS NOT NULL"),
    @NamedQuery(name = "Inventario.unidadesFisicasByTipoSinCaja", query = "SELECT i FROM Inventario i WHERE i.producto.productoPK.tipoProducto = :tipoProducto AND i.estadoproducto IN ('EN EXISTENCIA','DEFECTUOSO') AND i.numcaja IS NULL"),
    @NamedQuery(name = "Inventario.tallasByCaja", query = "SELECT DISTINCT(i.talla) FROM Inventario i WHERE i.numcaja =:caja"),
    @NamedQuery(name = "Inventario.curvaCaja", query = "SELECT i.color, i.talla, COUNT(i) FROM Inventario i WHERE i.numcaja =:caja GROUP BY i.color, i.talla "),
    @NamedQuery(name = "Inventario.precioByEstilo", query = "SELECT MAX(i.preciomayoreo) FROM Inventario i WHERE i.producto = :estilo AND i.estadoproducto <> 'VENDIDO' "),
    @NamedQuery(name = "Inventario.contarUnitFactura", query = "SELECT COUNT(i) FROM Inventario i WHERE i.numingreso.facturaIngreso = :factura "),
    @NamedQuery(name = "Inventario.unitarioByEstilo", query = "SELECT i FROM Inventario i WHERE i.producto.productoPK.tipoProducto = 1 AND i.producto.productoPK.codestilo = :estilo AND i.numcaja IS NULL AND i.estadoproducto  IN ('EN EXISTENCIA','MUESTRA')"),
    
    @NamedQuery(name = "Inventario.listaTallasCajaAnterior", query = "SELECT DISTINCT(i.talla) FROM Inventario i WHERE i.producto.productoPK.tipoProducto = :tipoProducto AND i.numcaja.numcaja = :numcaja AND i.estadoproducto LIKE 'X%' "),
    @NamedQuery(name = "Inventario.curvaByCajaAnterior", query = "SELECT i.color, i.talla, COUNT(i) FROM Inventario i WHERE i.producto.productoPK.tipoProducto = :tipoProducto AND i.numcaja.numcaja = :numcaja AND i.estadoproducto LIKE 'X%' ") })
public class Inventario implements Serializable {
    
   
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "CODIGO")
    private String codigo;
    @Size(max = 30)
    @Column(name = "COLOR")
    private String color;
    @Size(max = 6)
    @Column(name = "TALLA")
    private String talla;
    @Size(max = 15)
    @Column(name = "MARCA")
    private String marca;
    @Size(max = 20)
    @Column(name = "MATERIAL")
    private String material;
    @Column(name = "GENERO")
    private Character genero;
    @Column(name = "CLASEPERSONA")
    private Character clasepersona;
    @Size(max = 10)
    @Column(name = "PROCEDENCIA")
    private String procedencia;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "COSTOUNITARIO")
    private BigDecimal costounitario;
    @Column(name = "COSTOREAL")
    private BigDecimal costoreal;
    @Column(name = "COSTO_CONTABLE")
    private BigDecimal costoContable;
    @Column(name = "PRECIOMAYOREO")
    private BigDecimal preciomayoreo;
    @Column(name = "PRECIOUNITARIO")
    private BigDecimal preciounitario;
    @Column(name = "PRECIOVENDIDO")
    private BigDecimal preciovendido;
    @NotNull
    @Size(max = 30)
    @Column(name = "ESTADOPRODUCTO")
    private String estadoproducto;
    @Lob
    @Size(max = 65535)
    @Column(name = "NOTA_PRODUCTO")
    private String notaProducto;
    @Column(name = "FECHA_INGRESO")
    @Temporal(TemporalType.DATE)
    private Date fechaIngreso;
    @Column(name = "FECHA_REINGRESO")
    @Temporal(TemporalType.DATE)
    private Date fechaReingreso;
    @Column(name = "FECHA_EGRESO")
    @Temporal(TemporalType.DATE)
    private Date fechaEgreso;
    @Lob
    @Size(max = 65535)
    @Column(name = "UBICACION_PRODUCTO")
    private String ubicacionProducto;
    @JoinColumn(name = "PROVEEDOR", referencedColumnName = "IDPROVEEDOR")
    @ManyToOne(optional = false)
    private Proveedor proveedor;
    @JoinColumns({
        @JoinColumn(name = "ESTILO", referencedColumnName = "CODESTILO"),
        @JoinColumn(name = "TIPO_PRODUCTO", referencedColumnName = "TIPO_PRODUCTO")})
    @ManyToOne(optional = false)
    private Producto producto;
    @JoinColumn(name = "NUMCAJA", referencedColumnName = "NUMCAJA")
    @ManyToOne
    private Caja numcaja;
    @JoinColumn(name = "NUMINGRESO", referencedColumnName = "NUMINGRESO")
    @ManyToOne(optional = false)
    private Ingreso numingreso;
    //ELIMNACION DE FK NUMVENTA
    @JoinColumn(name = "MUESTRA_DER", referencedColumnName = "CODEMPLEADO")
    @ManyToOne
    private Empleado responsableMuestraDer;
    @JoinColumn(name = "MUESTRA_IZQ", referencedColumnName = "CODEMPLEADO")
    @ManyToOne
    private Empleado responsableMuestraIzq;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "inventario")
    private Collection<DetalleEnvio> detalleEnvioCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cambioProducto")
    private Collection<DetalleEnvio> cambioProductoCollection;
    
    
    public Inventario() {
    }

    public Inventario(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getTalla() {
        return talla;
    }

    public void setTalla(String talla) {
        this.talla = talla;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public Character getGenero() {
        return genero;
    }

    public void setGenero(Character genero) {
        this.genero = genero;
    }

    public Character getClasepersona() {
        return clasepersona;
    }

    public void setClasepersona(Character clasepersona) {
        this.clasepersona = clasepersona;
    }

    public String getProcedencia() {
        return procedencia;
    }

    public void setProcedencia(String procedencia) {
        this.procedencia = procedencia;
    }

    public BigDecimal getCostounitario() {
        return costounitario;
    }

    public void setCostounitario(BigDecimal costounitario) {
        this.costounitario = costounitario;
    }

    public BigDecimal getCostoreal() {
        return costoreal;
    }

    public void setCostoreal(BigDecimal costoreal) {
        this.costoreal = costoreal;
    }

    public BigDecimal getCostoContable() {
        return costoContable;
    }

    public void setCostoContable(BigDecimal costoContable) {
        this.costoContable = costoContable;
    }

    public BigDecimal getPreciomayoreo() {
        return preciomayoreo;
    }

    public void setPreciomayoreo(BigDecimal preciomayoreo) {
        this.preciomayoreo = preciomayoreo;
    }

    public BigDecimal getPreciounitario() {
        return preciounitario;
    }

    public void setPreciounitario(BigDecimal preciounitario) {
        this.preciounitario = preciounitario;
    }

    public BigDecimal getPreciovendido() {
        return preciovendido;
    }

    public void setPreciovendido(BigDecimal preciovendido) {
        this.preciovendido = preciovendido;
    }

    public String getEstadoproducto() {
        return estadoproducto;
    }

    public void setEstadoproducto(String estadoproducto) {
        this.estadoproducto = estadoproducto;
    }

    public String getNotaProducto() {
        return notaProducto;
    }

    public void setNotaProducto(String notaProducto) {
        this.notaProducto = notaProducto;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public Date getFechaEgreso() {
        return fechaEgreso;
    }

    public void setFechaEgreso(Date fechaEgreso) {
        this.fechaEgreso = fechaEgreso;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Caja getNumcaja() {
        return numcaja;
    }

    public void setNumcaja(Caja numcaja) {
        this.numcaja = numcaja;
    }

    public Ingreso getNumingreso() {
        return numingreso;
    }

    public void setNumingreso(Ingreso numingreso) {
        this.numingreso = numingreso;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    
    public Date getFechaReingreso() {
        return fechaReingreso;
    }

    public void setFechaReingreso(Date fechaReingreso) {
        this.fechaReingreso = fechaReingreso;
    }

    public Empleado getResponsableMuestraDer() {
        return responsableMuestraDer;
    }

    public void setResponsableMuestraDer(Empleado responsableMuestraDer) {
        this.responsableMuestraDer = responsableMuestraDer;
    }
    
    public Empleado getResponsableMuestraIzq() {
        return responsableMuestraIzq;
    }

    public void setResponsableMuestraIzq(Empleado responsableMuestraIzq) {
        this.responsableMuestraIzq = responsableMuestraIzq;
    }
    
    @XmlTransient
    public Collection<DetalleEnvio> getDetalleEnvioCollection() {
        return detalleEnvioCollection;
    }

    public void setDetalleEnvioCollection(Collection<DetalleEnvio> detalleEnvioCollection) {
        this.detalleEnvioCollection = detalleEnvioCollection;
    }

    @XmlTransient
    public Collection<DetalleEnvio> getCambioProductoCollection() {
        return cambioProductoCollection;
    }

    public void setCambioProductoCollection(Collection<DetalleEnvio> cambioProductoCollection) {
        this.cambioProductoCollection = cambioProductoCollection;
    }
    
    
   
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codigo != null ? codigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Inventario)) {
            return false;
        }
        Inventario other = (Inventario) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ddicark.entidades.Inventario[ codigo=" + codigo + " ]";
    }

    public String getUbicacionProducto() {
        return ubicacionProducto;
    }

    public void setUbicacionProducto(String ubicacionProducto) {
        this.ubicacionProducto = ubicacionProducto;
    }

    
    
    
    
    
    
    
    
    
    
    
    
    
    
   
}
