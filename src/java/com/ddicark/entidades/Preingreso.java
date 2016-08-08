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
@Table(name = "preingreso")
@XmlRootElement
@NamedQueries({
    
    @NamedQuery(name = "Preingreso.maxIDPreingreso", query = "SELECT MAX(p.codigoPreingreso) FROM Preingreso p "),
    @NamedQuery(name = "Preingreso.preingresoList", query = "SELECT p FROM Preingreso p WHERE p.facturaIngreso = :facturaIngreso AND p.estadoPreingreso = 'PREINGRESO'  ORDER BY p.numcajaPreingreso DESC"),
    @NamedQuery(name = "Preingreso.cajaList", query = "SELECT p FROM Preingreso p WHERE p.facturaIngreso = :facturaIngreso AND p.estadoPreingreso = 'PREINGRESO'  AND p.numcajaPreingreso = :numcaja"),
    @NamedQuery(name = "Preingreso.facturaList", query = "SELECT p FROM Preingreso p WHERE p.facturaIngreso = :facturaIngreso AND p.estadoPreingreso = 'CODIFICANDO'  ORDER BY p.numcajaPreingreso DESC"),
    @NamedQuery(name = "Preingreso.numCajaFactura", query = "SELECT MAX(p.numcajaPreingreso) FROM Preingreso p WHERE p.facturaIngreso = :facturaIngreso AND p.numcajaPreingreso > 0 AND p.numcajaPreingreso IS NOT NULL"),
    @NamedQuery(name = "Preingreso.consultaCurvaTallas", query = "SELECT DISTINCT(p.tallaPreingreso) FROM Preingreso p WHERE p.facturaIngreso = :facturaIngreso AND p.numcajaPreingreso = :numCaja "),
    @NamedQuery(name = "Preingreso.consultaColoresCaja", query = "SELECT DISTINCT(p.colorPreingreso) FROM Preingreso p WHERE p.facturaIngreso = :facturaIngreso AND p.numcajaPreingreso = :numCaja "),
    @NamedQuery(name = "Preingreso.existeCaja", query = "SELECT DISTINCT(p.codigoCaja) FROM Preingreso p WHERE p.codigoCaja = :codigoCaja"),
    @NamedQuery(name = "Preingreso.existeUnidad", query = "SELECT DISTINCT(p.codigoBarra) FROM Preingreso p WHERE p.codigoBarra = :codigoBarra"),
    @NamedQuery(name = "Preingreso.consultaCurva", query = "SELECT p.colorPreingreso, p.tallaPreingreso, COUNT(p) FROM Preingreso p WHERE p.facturaIngreso = :facturaIngreso AND p.numcajaPreingreso = :numCaja GROUP BY p.colorPreingreso , p.tallaPreingreso"),
    @NamedQuery(name = "Preingreso.findByCodigos", query = "SELECT p FROM Preingreso p WHERE p.codigoBarra = :codigoBarra AND p.codigoCaja = :codigoCaja"),
    @NamedQuery(name = "Preingreso.findByCodigoBarra", query = "SELECT p FROM Preingreso p WHERE p.codigoBarra = :codigoBarra AND p.codigoCaja IS NULL"),
    @NamedQuery(name = "Preingreso.contarPreFactura", query = "SELECT COUNT(p) FROM Preingreso p WHERE p.facturaIngreso = :facturaIngreso"),
    @NamedQuery(name = "Preingreso.contarInvFactura", query = "SELECT COUNT(p) FROM Preingreso p WHERE p.facturaIngreso = :facturaIngreso AND p.estadoPreingreso = 'INVENTARIADO'") ,
    @NamedQuery(name = "Preingreso.contarUnidadesCaja", query = "SELECT COUNT(p) FROM Preingreso p WHERE p.facturaIngreso = :facturaIngreso AND p.estadoPreingreso = 'PREINGRESO' AND p.numcajaPreingreso = :numcaja"),
    @NamedQuery(name = "Preingreso.contarUnidadesCajaPreingresadas", query = "SELECT COUNT(p) FROM Preingreso p WHERE p.facturaIngreso = :facturaIngreso AND p.estadoPreingreso = 'CODIFICANDO' AND p.numcajaPreingreso = :numcaja")})
public class Preingreso implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "CODIGO_PREINGRESO")
    private Integer codigoPreingreso;
    @Basic(optional = false)
    @NotNull
    @Size(max = 30)
    @Column(name = "COLOR_PREINGRESO")
    private String colorPreingreso;
    @Size(max = 6)
    @Column(name = "TALLA_PREINGRESO")
    private String tallaPreingreso;
    @Size(max = 15)
    @Column(name = "MARCA_PREINGRESO")
    private String marcaPreingreso;
    @Size(max = 20)
    @Column(name = "MATERIAL_PREINGRESO")
    private String materialPreingreso;
    @Column(name = "GENERO_PREINGRESO")
    private Character generoPreingreso;
    @Column(name = "CLASEPERSONA_PREINGRESO")
    private Character clasepersonaPreingreso;
    @Column(name = "NUMCAJA_PREINGRESO")
    private Integer numcajaPreingreso;
    // @Max(value=?) @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "COSTOUNITARIO_PREINGRESO")
    private BigDecimal costounitarioPreingreso;
    
    @Size(max = 30)
    @Column(name = "CODIGO_BARRA")
    private String codigoBarra;
    @Size(max = 20)
    @Column(name = "CODIGO_CAJA")
    private String codigoCaja;
    @Size(max = 5)
    @Column(name = "RESPONSABLE")
    private String responsable                     ;
    @Size(max = 15)
    @Column(name = "ESTADO_PREINGRESO")
    private String estadoPreingreso;
    
    @NotNull
    @JoinColumns({
        @JoinColumn(name = "DOCUMENTO_COMPRA", referencedColumnName = "DOCUMENTO_COMPRA"),
        @JoinColumn(name = "FECHA_DOCUMENTO", referencedColumnName = "FECHA_DOCUMENTO")})
    @ManyToOne(optional = false)
    private FacturaIngreso facturaIngreso;
    @NotNull
    @JoinColumns({
        @JoinColumn(name = "ESTILO_PREINGRESO", referencedColumnName = "CODESTILO"),
        @JoinColumn(name = "TIPO_PRODUCTO_PREINGRESO", referencedColumnName = "TIPO_PRODUCTO")})
    @ManyToOne(optional = false)
    private Producto estilo;

    public Preingreso() {
    }

    public Preingreso(Integer codigoPreingreso) {
        this.codigoPreingreso = codigoPreingreso;
    }

    public Preingreso(Integer codigoPreingreso, FacturaIngreso factura, Producto estilo, String colorPreingreso) {
        this.codigoPreingreso = codigoPreingreso;
        this.facturaIngreso = factura;
        this.estilo = estilo;
        this.colorPreingreso = colorPreingreso;
    }

    public Integer getCodigoPreingreso() {
        return codigoPreingreso;
    }

    public void setCodigoPreingreso(Integer codigoPreingreso) {
        this.codigoPreingreso = codigoPreingreso;
    }

    public Producto getEstilo() {
        return estilo;
    }

    public void setEstilo(Producto estilo) {
        this.estilo = estilo;
    }

    
    public String getColorPreingreso() {
        return colorPreingreso;
    }

    public void setColorPreingreso(String colorPreingreso) {
        this.colorPreingreso = colorPreingreso;
    }

    public String getTallaPreingreso() {
        return tallaPreingreso;
    }

    public void setTallaPreingreso(String tallaPreingreso) {
        this.tallaPreingreso = tallaPreingreso;
    }

    public String getMarcaPreingreso() {
        return marcaPreingreso;
    }

    public void setMarcaPreingreso(String marcaPreingreso) {
        this.marcaPreingreso = marcaPreingreso;
    }

    public String getMaterialPreingreso() {
        return materialPreingreso;
    }

    public void setMaterialPreingreso(String materialPreingreso) {
        this.materialPreingreso = materialPreingreso;
    }

    public Character getGeneroPreingreso() {
        return generoPreingreso;
    }

    public void setGeneroPreingreso(Character generoPreingreso) {
        this.generoPreingreso = generoPreingreso;
    }

    public Character getClasepersonaPreingreso() {
        return clasepersonaPreingreso;
    }

    public void setClasepersonaPreingreso(Character clasepersonaPreingreso) {
        this.clasepersonaPreingreso = clasepersonaPreingreso;
    }

    public Integer getNumcajaPreingreso() {
        return numcajaPreingreso;
    }

    public void setNumcajaPreingreso(Integer numcajaPreingreso) {
        this.numcajaPreingreso = numcajaPreingreso;
    }

    public BigDecimal getCostounitarioPreingreso() {
        return costounitarioPreingreso;
    }

    public void setCostounitarioPreingreso(BigDecimal costounitarioPreingreso) {
        this.costounitarioPreingreso = costounitarioPreingreso;
    }

    public String getCodigoBarra() {
        return codigoBarra;
    }

    public void setCodigoBarra(String codigoBarra) {
        this.codigoBarra = codigoBarra;
    }

    public String getCodigoCaja() {
        return codigoCaja;
    }

    public void setCodigoCaja(String codigoCaja) {
        this.codigoCaja = codigoCaja;
    }

    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }

    public String getEstadoPreingreso() {
        return estadoPreingreso;
    }

    public void setEstadoPreingreso(String estadoPreingreso) {
        this.estadoPreingreso = estadoPreingreso;
    }

    

    public FacturaIngreso getFacturaIngreso() {
        return facturaIngreso;
    }

    public void setFacturaIngreso(FacturaIngreso facturaIngreso) {
        this.facturaIngreso = facturaIngreso;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codigoPreingreso != null ? codigoPreingreso.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Preingreso)) {
            return false;
        }
        Preingreso other = (Preingreso) object;
        if ((this.codigoPreingreso == null && other.codigoPreingreso != null) || (this.codigoPreingreso != null && !this.codigoPreingreso.equals(other.codigoPreingreso))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ddicark.entidades.Preingreso[ codigoPreingreso=" + codigoPreingreso + " ]";
    }
    
}