/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ddicark.entidades;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
@Table(name = "empleado")
@XmlRootElement
@NamedQueries({
    
    @NamedQuery(name = "Empleado.findByCodempleado", query = "SELECT e FROM Empleado e WHERE e.codempleado = :codempleado"),
    @NamedQuery(name = "Empleado.findByPuestoEmpleado", query = "SELECT e FROM Empleado e WHERE e.puestoEmpleado = :puestoEmpleado"),
    @NamedQuery(name = "Empleado.findByUsername", query = "SELECT e FROM Empleado e WHERE e.username = :username"),
    @NamedQuery(name = "Empleado.findByActivo", query = "SELECT e FROM Empleado e WHERE e.activo = :activo"),
    @NamedQuery(name = "Empleado.findByUsuario", query = "SELECT e FROM Empleado e WHERE e.username = :username AND e.password = :password"),
    @NamedQuery(name = "Empleado.vendedores", query = "SELECT e FROM Empleado e WHERE e.puestoEmpleado = 'VENDEDOR' AND e.activo = TRUE OR e.codempleado IN ('SP006','EM26')"),
    @NamedQuery(name = "Empleado.usuarios", query = "SELECT e FROM Empleado e WHERE e.codempleado NOT IN ('ADMIN','SUPER') ")})
public class Empleado implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "CODEMPLEADO")
    private String codempleado;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "NOMBRE_EMPLEADO")
    private String nombreEmpleado;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "APELLIDO_EMPLEADO")
    private String apellidoEmpleado;
    @Basic
    @Size(max = 10)
    @Column(name = "DUI_EMPLEADO")
    private String duiEmpleado;
    @Size(max = 17)
    @Column(name = "NIT_EMPLEADO")
    private String nitEmpleado;
    @Size(max = 9)
    @Column(name = "TEL_EMPLEADO")
    private String telEmpleado;
    @Column(name = "FECHANAC_EMPLEADO")
    @Temporal(TemporalType.DATE)
    private Date fechanacEmpleado;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "PUESTO_EMPLEADO")
    private String puestoEmpleado;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "USERNAME")
    private String username;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 32)
    @Column(name = "PASSWORD")
    private String password;
    @NotNull
    @Column(name = "ACTIVO")
    private Boolean activo;
    @Size(max = 5)
    @Column(name = "SERIE")
    private String serieRECIBO;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "responsableTransac")
    private Collection<Transaccion> transaccionCollection;
    @OneToMany(mappedBy = "empleadoasignado")
    private Collection<Cliente> clienteCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "responsablePedido")
    private Collection<Pedido> pedidoCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "responsableMuestraDer")
    private Collection<Inventario> muestraDerCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "responsableMuestraIzq")
    private Collection<Inventario> muestraIzqCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "vendedor")
    private Collection<Envio> vendedorCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "despacho")
    private Collection<Envio> despachoCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "responsable")
    private Collection<Devolucion> responsableDevolucionCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "empleado")
    private Collection<Permisos> permisosCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "responsableCobro")
    private Collection<PagoVenta> responsableCobroCollection;
    
    public Empleado() {
    }

    public Empleado(String codempleado) {
        this.codempleado = codempleado;
    }

    public Empleado(String codempleado, String nombreEmpleado, String apellidoEmpleado, String duiEmpleado, String puestoEmpleado, String username, String password) {
        this.codempleado = codempleado;
        this.nombreEmpleado = nombreEmpleado;
        this.apellidoEmpleado = apellidoEmpleado;
        this.duiEmpleado = duiEmpleado;
        this.puestoEmpleado = puestoEmpleado;
        this.username = username;
        this.password = password;
    }

    public String getCodempleado() {
        return codempleado;
    }

    public void setCodempleado(String codempleado) {
        this.codempleado = codempleado;
    }

    public String getNombreEmpleado() {
        return nombreEmpleado;
    }

    public void setNombreEmpleado(String nombreEmpleado) {
        this.nombreEmpleado = nombreEmpleado;
    }

    public String getApellidoEmpleado() {
        return apellidoEmpleado;
    }

    public void setApellidoEmpleado(String apellidoEmpleado) {
        this.apellidoEmpleado = apellidoEmpleado;
    }

    public String getDuiEmpleado() {
        return duiEmpleado;
    }

    public void setDuiEmpleado(String duiEmpleado) {
        this.duiEmpleado = duiEmpleado;
    }

    public String getNitEmpleado() {
        return nitEmpleado;
    }

    public void setNitEmpleado(String nitEmpleado) {
        this.nitEmpleado = nitEmpleado;
    }

    public String getTelEmpleado() {
        return telEmpleado;
    }

    public void setTelEmpleado(String telEmpleado) {
        this.telEmpleado = telEmpleado;
    }

    public Date getFechanacEmpleado() {
        return fechanacEmpleado;
    }

    public void setFechanacEmpleado(Date fechanacEmpleado) {
        this.fechanacEmpleado = fechanacEmpleado;
    }

    public String getPuestoEmpleado() {
        return puestoEmpleado;
    }

    public void setPuestoEmpleado(String puestoEmpleado) {
        this.puestoEmpleado = puestoEmpleado;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public String getSerieRECIBO() {
        return serieRECIBO;
    }

    public void setSerieRECIBO(String serieRECIBO) {
        this.serieRECIBO = serieRECIBO;
    }
    
    @XmlTransient
    public Collection<Transaccion> getTransaccionCollection() {
        return transaccionCollection;
    }

    public void setTransaccionCollection(Collection<Transaccion> transaccionCollection) {
        this.transaccionCollection = transaccionCollection;
    }

    @XmlTransient
    public Collection<Cliente> getClienteCollection() {
        return clienteCollection;
    }

    public void setClienteCollection(Collection<Cliente> clienteCollection) {
        this.clienteCollection = clienteCollection;
    }

    @XmlTransient
    public Collection<Pedido> getPedidoCollection() {
        return pedidoCollection;
    }

    public void setPedidoCollection(Collection<Pedido> pedidoCollection) {
        this.pedidoCollection = pedidoCollection;
    }
    
    @XmlTransient
    public Collection<Inventario> getMuestraDerCollection() {
        return muestraDerCollection;
    }

    public void setMuestraDerCollection(Collection<Inventario> muestraDerCollection) {
        this.muestraDerCollection = muestraDerCollection;
    }
    
    @XmlTransient
    public Collection<Inventario> getMuestraIzqCollection() {
        return muestraIzqCollection;
    }

    public void setMuestraIzqCollection(Collection<Inventario> muestraIzqCollection) {
        this.muestraIzqCollection = muestraIzqCollection;
    }
    
    @XmlTransient
    public Collection<Envio> getVendedorCollection() {
        return vendedorCollection;
    }

    public void setVendedorCollection(Collection<Envio> vendedorCollection) {
        this.vendedorCollection = vendedorCollection;
    }
    
    @XmlTransient
    public Collection<Envio> getDespachoCollection() {
        return despachoCollection;
    }

    public void setDespachoCollection(Collection<Envio> despachoCollection) {
        this.despachoCollection = despachoCollection;
    }

    @XmlTransient
    public Collection<Devolucion> getResponsableDevolucionCollection() {
        return responsableDevolucionCollection;
    }

    public void setResponsableDevolucionCollection(Collection<Devolucion> responsableDevolucionCollection) {
        this.responsableDevolucionCollection = responsableDevolucionCollection;
    }

    @XmlTransient
    public Collection<Permisos> getPermisosCollection() {
        return permisosCollection;
    }

    public void setPermisosCollection(Collection<Permisos> permisosCollection) {
        this.permisosCollection = permisosCollection;
    }

    @XmlTransient
    public Collection<PagoVenta> getResponsableCobroCollection() {
        return responsableCobroCollection;
    }

    public void setResponsableCobroCollection(Collection<PagoVenta> responsableCobroCollection) {
        this.responsableCobroCollection = responsableCobroCollection;
    }
    
    

    
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codempleado != null ? codempleado.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Empleado)) {
            return false;
        }
        Empleado other = (Empleado) object;
        if ((this.codempleado == null && other.codempleado != null) || (this.codempleado != null && !this.codempleado.equals(other.codempleado))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ddicark.entidades.Empleado[ codempleado=" + codempleado + " ]";
    }

    
    
}
