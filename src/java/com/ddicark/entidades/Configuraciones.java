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
@Table(name = "configuraciones")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Configuraciones.configuracion", query = "SELECT c FROM Configuraciones c WHERE c.tipo = :tipo AND c.nombre = :nombre")})
public class Configuraciones implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "NUMCONFIG")
    private Integer numconfig;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "TIPO")
    private String tipo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "NOMBRE")
    private String nombre;
    @Size(max = 50)
    @Column(name = "VALOR_STRING")
    private String valorString;
    @Column(name = "VALOR_INT")
    private Integer valorInt;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "VALOR_FLOAT")
    private Float valorFloat;
    @Column(name = "VALOR_DECIMAL")
    private BigDecimal valorDecimal;

    public Configuraciones() {
    }

    public Configuraciones(Integer numconfig) {
        this.numconfig = numconfig;
    }

    public Configuraciones(Integer numconfig, String tipo, String nombre) {
        this.numconfig = numconfig;
        this.tipo = tipo;
        this.nombre = nombre;
    }

    public Integer getNumconfig() {
        return numconfig;
    }

    public void setNumconfig(Integer numconfig) {
        this.numconfig = numconfig;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getValorString() {
        return valorString;
    }

    public void setValorString(String valorString) {
        this.valorString = valorString;
    }

    public Integer getValorInt() {
        return valorInt;
    }

    public void setValorInt(Integer valorInt) {
        this.valorInt = valorInt;
    }

    public Float getValorFloat() {
        return valorFloat;
    }

    public void setValorFloat(Float valorFloat) {
        this.valorFloat = valorFloat;
    }

    public BigDecimal getValorDecimal() {
        return valorDecimal;
    }

    public void setValorDecimal(BigDecimal valorDecimal) {
        this.valorDecimal = valorDecimal;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (numconfig != null ? numconfig.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Configuraciones)) {
            return false;
        }
        Configuraciones other = (Configuraciones) object;
        if ((this.numconfig == null && other.numconfig != null) || (this.numconfig != null && !this.numconfig.equals(other.numconfig))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ddicark.entidades.Configuraciones[ numconfig=" + numconfig + " ]";
    }
    
}
