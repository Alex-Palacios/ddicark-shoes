/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ddicark.entidades;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
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
@Table(name = "permisos")
@XmlRootElement
@NamedQueries({
    
    @NamedQuery(name = "Permisos.maxID", query = "SELECT MAX(p.idpermiso) FROM Permisos p "),
    @NamedQuery(name = "Permisos.empleado", query = "SELECT p FROM Permisos p WHERE p.empleado = :usuario "),
    @NamedQuery(name = "Permisos.listaMenus", query = "SELECT DISTINCT(p.menu) FROM Permisos p WHERE p.empleado.codempleado = 'SUPER' "),
    @NamedQuery(name = "Permisos.listaSubmenus", query = "SELECT DISTINCT(p.submenu) FROM Permisos p WHERE p.empleado.codempleado = 'SUPER' "),
    @NamedQuery(name = "Permisos.usuarios", query = "SELECT p FROM Permisos p WHERE p.empleado.codempleado NOT IN('SUPER','ADMIN') ") })
public class Permisos implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "IDPERMISO")
    private Integer idpermiso;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "MENU")
    private String menu;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "SUBMENU")
    private String submenu;
    @Lob
    @Size(max = 65535)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "PERMISO")
    private boolean permiso;
    
    @JoinColumn(name = "EMPLEADO", referencedColumnName = "CODEMPLEADO")
    @ManyToOne(optional = false)
    private Empleado empleado;

    public Permisos() {
    }

    public Permisos(Integer idpermiso) {
        this.idpermiso = idpermiso;
    }

    public Permisos(Integer idpermiso, String menu, boolean permiso) {
        this.idpermiso = idpermiso;
        this.menu = menu;
        this.permiso = permiso;
    }

    public Integer getIdpermiso() {
        return idpermiso;
    }

    public void setIdpermiso(Integer idpermiso) {
        this.idpermiso = idpermiso;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public String getSubmenu() {
        return submenu;
    }

    public void setSubmenu(String submenu) {
        this.submenu = submenu;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

   
    public boolean getPermiso() {
        return permiso;
    }

    public void setPermiso(boolean permiso) {
        this.permiso = permiso;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idpermiso != null ? idpermiso.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Permisos)) {
            return false;
        }
        Permisos other = (Permisos) object;
        if ((this.idpermiso == null && other.idpermiso != null) || (this.idpermiso != null && !this.idpermiso.equals(other.idpermiso))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ddicark.entidades.Permisos[ idpermiso=" + idpermiso + " ]";
    }
    
}
