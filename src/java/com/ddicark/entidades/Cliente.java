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
 * @author DDICARK
 */
@Entity
@Table(name = "cliente")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Cliente.findByDuiCliente", query = "SELECT c FROM Cliente c WHERE c.duiCliente = :duiCliente"),
    @NamedQuery(name = "Cliente.maxID", query = "SELECT MAX(c.numcuenta) FROM Cliente c"),
    @NamedQuery(name = "Cliente.clientesByNaturaleza", query = "SELECT c FROM Cliente c WHERE c.naturaleza = :naturaleza"),
    @NamedQuery(name = "Cliente.clientesByVendedor", query = "SELECT c FROM Cliente c WHERE c.empleadoasignado = :vendedor ORDER BY c.municipioCliente,c.nombreCliente"),
    @NamedQuery(name = "Cliente.existeCliente", query = "SELECT c FROM Cliente c WHERE c.nombreCliente = :nombre AND c.apellidoCliente = :apellido ") })
public class Cliente implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "NUMCUENTA")
    private Integer numcuenta;
    @Basic(optional = false)
    @Size(max = 30)
    @Column(name = "NOMBRE_CLIENTE")
    private String nombreCliente;
    @Basic(optional = false)
    @Size(max = 25)
    @Column(name = "APELLIDO_CLIENTE")
    private String apellidoCliente;
    @Basic(optional = false)
    @Size(max = 10)
    @Column(name = "DUI_CLIENTE")
    private String duiCliente;
    @Size(max = 17)
    @Column(name = "NIT_CLIENTE")
    private String nitCliente;
    @Size(max = 15)
    @Column(name = "CARNET_RESIDENTE")
    private String carnetResidente;
    @Column(name = "GENERO_CLIENTE")
    private Character generoCliente;
    @Size(max = 9)
    @Column(name = "TEL_CLIENTE")
    private String telCliente;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "DIRECCION_CLIENTE")
    private String direccionCliente;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "MUNICIPIO_CLIENTE")
    private String municipioCliente;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "DEPTO_CLIENTE")
    private String deptoCliente;
    @Size(max = 20)
    @Column(name = "OCUPACION_CLIENTE")
    private String ocupacionCliente;
    @Size(max = 30)
    @Column(name = "COMERCIO_CONTACTO")
    private String comercioContacto;
    @Size(max = 12)
    @Column(name = "NRC_CLIENTE")
    private String nrcCliente;
    @Size(max = 9)
    @Column(name = "TEL_COMERCIO")
    private String telComercio;
    @Size(max = 20)
    @NotNull
    @Column(name = "NATURALEZA")
    private String naturaleza;
    @Column(name = "LIMITE")
    private BigDecimal limite;
    @Size(max = 20)
    @Column(name = "DIA_VISITA")
    private String diaVisita;
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "NOTA")
    private String nota;
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "DIRECCION_NEGOCIO")
    private String direccionNegocio;
    
    @JoinColumn(name = "EMPLEADOASIGNADO", referencedColumnName = "CODEMPLEADO")
    @ManyToOne
    private Empleado empleadoasignado;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "clientePedido")
    private Collection<Pedido> pedidoCollection;

    public Cliente() {
    }

    public Cliente(Integer numcuenta) {
        this.numcuenta = numcuenta;
    }

    public Cliente(Integer numcuenta, String nombreCliente, String apellidoCliente, String duiCliente, String direccionCliente, String municipioCliente, String deptoCliente) {
        this.numcuenta = numcuenta;
        this.nombreCliente = nombreCliente;
        this.apellidoCliente = apellidoCliente;
        this.duiCliente = duiCliente;
        this.direccionCliente = direccionCliente;
        this.municipioCliente = municipioCliente;
        this.deptoCliente = deptoCliente;
    }

    public Integer getNumcuenta() {
        return numcuenta;
    }

    public void setNumcuenta(Integer numcuenta) {
        this.numcuenta = numcuenta;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getApellidoCliente() {
        return apellidoCliente;
    }

    public void setApellidoCliente(String apellidoCliente) {
        this.apellidoCliente = apellidoCliente;
    }

    public String getDuiCliente() {
        return duiCliente;
    }

    public void setDuiCliente(String duiCliente) {
        this.duiCliente = duiCliente;
    }

    public String getNitCliente() {
        return nitCliente;
    }

    public void setNitCliente(String nitCliente) {
        this.nitCliente = nitCliente;
    }

    public String getCarnetResidente() {
        return carnetResidente;
    }

    public void setCarnetResidente(String carnetResidente) {
        this.carnetResidente = carnetResidente;
    }

    public Character getGeneroCliente() {
        return generoCliente;
    }

    public void setGeneroCliente(Character generoCliente) {
        this.generoCliente = generoCliente;
    }

    public String getTelCliente() {
        return telCliente;
    }

    public void setTelCliente(String telCliente) {
        this.telCliente = telCliente;
    }

    public String getDireccionCliente() {
        return direccionCliente;
    }

    public void setDireccionCliente(String direccionCliente) {
        this.direccionCliente = direccionCliente;
    }

    public String getMunicipioCliente() {
        return municipioCliente;
    }

    public void setMunicipioCliente(String municipioCliente) {
        this.municipioCliente = municipioCliente;
    }

    public String getDeptoCliente() {
        return deptoCliente;
    }

    public void setDeptoCliente(String deptoCliente) {
        this.deptoCliente = deptoCliente;
    }

    public String getOcupacionCliente() {
        return ocupacionCliente;
    }

    public void setOcupacionCliente(String ocupacionCliente) {
        this.ocupacionCliente = ocupacionCliente;
    }

    public String getComercioContacto() {
        return comercioContacto;
    }

    public void setComercioContacto(String comercioContacto) {
        this.comercioContacto = comercioContacto;
    }

    public String getNrcCliente() {
        return nrcCliente;
    }

    public void setNrcCliente(String nrcCliente) {
        this.nrcCliente = nrcCliente;
    }

    public String getTelComercio() {
        return telComercio;
    }

    public void setTelComercio(String telComercio) {
        this.telComercio = telComercio;
    }

    public String getNaturaleza() {
        return naturaleza;
    }

    public void setNaturaleza(String naturaleza) {
        this.naturaleza = naturaleza;
    }

    public BigDecimal getLimite() {
        return limite;
    }

    public void setLimite(BigDecimal limite) {
        this.limite = limite;
    }

    public String getDiaVisita() {
        return diaVisita;
    }

    public void setDiaVisita(String diaVisita) {
        this.diaVisita = diaVisita;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    public String getDireccionNegocio() {
        return direccionNegocio;
    }

    public void setDireccionNegocio(String direccionNegocio) {
        this.direccionNegocio = direccionNegocio;
    }

    public Empleado getEmpleadoasignado() {
        return empleadoasignado;
    }

    public void setEmpleadoasignado(Empleado empleadoasignado) {
        this.empleadoasignado = empleadoasignado;
    }

    @XmlTransient
    public Collection<Pedido> getPedidoCollection() {
        return pedidoCollection;
    }

    public void setPedidoCollection(Collection<Pedido> pedidoCollection) {
        this.pedidoCollection = pedidoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (numcuenta != null ? numcuenta.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Cliente)) {
            return false;
        }
        Cliente other = (Cliente) object;
        if ((this.numcuenta == null && other.numcuenta != null) || (this.numcuenta != null && !this.numcuenta.equals(other.numcuenta))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ddicark.entidades.Cliente[ numcuenta=" + numcuenta + " ]";
    }
    
    //FUNCIONES ADICIONALES
    public String obtenerNombreCliente(){
       String nombreCliente = "";
       if(this.naturaleza.equals("PERSONA NATURAL")){
           nombreCliente = this.nombreCliente + " " + this.apellidoCliente;
       }else{
           nombreCliente = this.nombreCliente;
       }
       
       return nombreCliente;
   }
    
}
