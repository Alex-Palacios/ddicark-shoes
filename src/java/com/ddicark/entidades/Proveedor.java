/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ddicark.entidades;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
@Table(name = "proveedor")
@XmlRootElement
@NamedQueries({
    
    @NamedQuery(name = "Proveedor.findByNombreProveedor", query = "SELECT p FROM Proveedor p WHERE p.nombreProveedor = :nombreProveedor"),
    @NamedQuery(name = "Proveedor.nacionales", query = "SELECT p FROM Proveedor p WHERE p.paisProveedor = 'EL SALVADOR'"),
    @NamedQuery(name = "Proveedor.extranjeros", query = "SELECT p FROM Proveedor p WHERE p.paisProveedor NOT IN ('EL SALVADOR') AND p.paisProveedor IS NOT NULL "),
    @NamedQuery(name = "Proveedor.corteInventarioInicial", query = "SELECT p FROM Proveedor p WHERE p.idproveedor = 0 ") })
public class Proveedor implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "IDPROVEEDOR")
    private Integer idproveedor;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "NOMBRE_PROVEEDOR")
    private String nombreProveedor;
    @Size(max = 10)
    @Column(name = "DUI_PROVEEDOR")
    private String duiProveedor;
    @Size(max = 17)
    @Column(name = "NIT_PROVEEDOR")
    private String nitProveedor;
    @Size(max = 25)
    @Column(name = "NRC_PROVEEDOR")
    private String nrcProveedor;
    @Size(max = 40)
    @Column(name = "RUBRO_PROVEEDOR")
    private String rubroProveedor;
    @Lob
    @Size(max = 65535)
    @Column(name = "DIRECCION_PROVEEDOR")
    private String direccionProveedor;
    @Basic(optional = false)
    @NotNull
    @Size(max = 50)
    @Column(name = "PAIS_PROVEEDOR")
    private String paisProveedor;
    @Size(max = 25)
    @Column(name = "TEL_PROVEEDOR")
    private String telProveedor;
    @Size(max = 50)
    @Column(name = "NOMBRE_CONTACTO")
    private String nombreContacto;
    @Size(max = 25)
    @Column(name = "TEL_CONTACTO")
    private String telContacto;
    @Lob
    @Size(max = 65535)
    @Column(name = "NOTA_PROVEEDOR")
    private String notaProveedor;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "proveedor")
    private Collection<FacturaIngreso> facturaIngresoCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "proveedor")
    private Collection<Inventario> inventarioCollection;

    public Proveedor() {
    }

    public Proveedor(Integer idproveedor) {
        this.idproveedor = idproveedor;
    }

    public Proveedor(Integer idproveedor, String nombreProveedor) {
        this.idproveedor = idproveedor;
        this.nombreProveedor = nombreProveedor;
    }

    public Integer getIdproveedor() {
        return idproveedor;
    }

    public void setIdproveedor(Integer idproveedor) {
        this.idproveedor = idproveedor;
    }

    public String getNombreProveedor() {
        return nombreProveedor;
    }

    public void setNombreProveedor(String nombreProveedor) {
        this.nombreProveedor = nombreProveedor;
    }

    public String getDuiProveedor() {
        return duiProveedor;
    }

    public void setDuiProveedor(String duiProveedor) {
        this.duiProveedor = duiProveedor;
    }

    public String getNitProveedor() {
        return nitProveedor;
    }

    public void setNitProveedor(String nitProveedor) {
        this.nitProveedor = nitProveedor;
    }

    public String getNrcProveedor() {
        return nrcProveedor;
    }

    public void setNrcProveedor(String nrcProveedor) {
        this.nrcProveedor = nrcProveedor;
    }

    public String getRubroProveedor() {
        return rubroProveedor;
    }

    public void setRubroProveedor(String rubroProveedor) {
        this.rubroProveedor = rubroProveedor;
    }

    public String getDireccionProveedor() {
        return direccionProveedor;
    }

    public void setDireccionProveedor(String direccionProveedor) {
        this.direccionProveedor = direccionProveedor;
    }

    public String getPaisProveedor() {
        return paisProveedor;
    }

    public void setPaisProveedor(String paisProveedor) {
        this.paisProveedor = paisProveedor;
    }

    public String getTelProveedor() {
        return telProveedor;
    }

    public void setTelProveedor(String telProveedor) {
        this.telProveedor = telProveedor;
    }

    public String getNombreContacto() {
        return nombreContacto;
    }

    public void setNombreContacto(String nombreContacto) {
        this.nombreContacto = nombreContacto;
    }

    public String getTelContacto() {
        return telContacto;
    }

    public void setTelContacto(String telContacto) {
        this.telContacto = telContacto;
    }

    public String getNotaProveedor() {
        return notaProveedor;
    }

    public void setNotaProveedor(String notaProveedor) {
        this.notaProveedor = notaProveedor;
    }

    @XmlTransient
    public Collection<FacturaIngreso> getFacturaIngresoCollection() {
        return facturaIngresoCollection;
    }

    public void setFacturaIngresoCollection(Collection<FacturaIngreso> facturaIngresoCollection) {
        this.facturaIngresoCollection = facturaIngresoCollection;
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
        hash += (idproveedor != null ? idproveedor.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Proveedor)) {
            return false;
        }
        Proveedor other = (Proveedor) object;
        if ((this.idproveedor == null && other.idproveedor != null) || (this.idproveedor != null && !this.idproveedor.equals(other.idproveedor))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ddicark.entidades.Proveedor[ idproveedor=" + idproveedor + " ]";
    }
    
}
