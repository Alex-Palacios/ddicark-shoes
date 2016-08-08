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
@Table(name = "retaceo")
@XmlRootElement
@NamedQueries({
    
    @NamedQuery(name = "Retaceo.findByNumretaceo", query = "SELECT r FROM Retaceo r WHERE r.numretaceo = :numretaceo"),
    @NamedQuery(name = "Retaceo.findByAprobado", query = "SELECT r FROM Retaceo r WHERE r.aprobado = :aprobado"),
    @NamedQuery(name = "Retaceo.countNumRetaceo", query = "SELECT COUNT(r.numretaceo) FROM Retaceo r WHERE r.numretaceo LIKE :busqueda"),
    @NamedQuery(name = "Retaceo.findByImportaciones", query = "SELECT r FROM Retaceo r WHERE r.numretaceo LIKE 'IMP-%' AND r.aprobado = :aprobado"),
    @NamedQuery(name = "Retaceo.findByNacionales", query = "SELECT r FROM Retaceo r WHERE r.numretaceo LIKE 'NAC-%' AND r.aprobado = :aprobado")})
public class Retaceo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "NUMRETACEO")
    private String numretaceo;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "MONTO_FACTURAS")
    private BigDecimal montoFacturas;
    @Basic(optional = false)
    @NotNull
    @Column(name = "MONTO_SALIDA")
    private BigDecimal montoSalida;
    @Column(name = "MONTO_FLETE")
    private BigDecimal montoFlete;
    @Column(name = "MONTO_DESCUENTOS")
    private BigDecimal montoDescuentos;
    @Column(name = "MONTO_TOTAL_FACTURAS")
    private BigDecimal montoTotalFacturas;
    @Column(name = "MONTO_AGENTE_ADUANAL")
    private BigDecimal montoAgenteAduanal;
    @Column(name = "MONTO_ARANCEL")
    private BigDecimal montoArancel;
    @Column(name = "MONTO_TRANSPORTE")
    private BigDecimal montoTransporte;
    @Column(name = "MONTO_BODEGAJE")
    private BigDecimal montoBodegaje;
    @Column(name = "MONTO_SEGURIDAD")
    private BigDecimal montoSeguridad;
    @Column(name = "MONTO_SEGURO")
    private BigDecimal montoSeguro;
    @Column(name = "OTROS_GASTOS_CCF")
    private BigDecimal otrosGastosCcf;
    @Column(name = "OTROS_GASTOS")
    private BigDecimal otrosGastos;
    @Column(name = "FACTOR_RETACEO")
    private BigDecimal factorRetaceo;
    @Column(name = "FACTOR_COSTO_VIAJE")
    private BigDecimal factorCostoViaje;
    @Lob
    @Size(max = 65535)
    @Column(name = "NOTA_RETACEO")
    private String notaRetaceo;
    @Column(name = "APROBADO")
    private Boolean aprobado;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "numretaceo")
    private Collection<FacturaIngreso> facturaIngresoCollection;
    @JoinColumn(name = "IDTRANSAC", referencedColumnName = "IDTRANSAC")
    @ManyToOne(optional = false)
    private Transaccion idtransac;

    public Retaceo() {
//        this.montoFacturas = BigDecimal.ZERO;
//        this.montoSalida = BigDecimal.ZERO;
        this.montoFlete= BigDecimal.ZERO;
//        this.montoDescuentos = BigDecimal.ZERO;
//        this.montoTotalFacturas = BigDecimal.ZERO;
        this.montoAgenteAduanal= BigDecimal.ZERO;
        this.montoArancel= BigDecimal.ZERO;
        this.montoTransporte= BigDecimal.ZERO;
        this.montoBodegaje= BigDecimal.ZERO;
        this.montoSeguridad= BigDecimal.ZERO;
        this.montoSeguro= BigDecimal.ZERO;
        this.otrosGastosCcf= BigDecimal.ZERO;
        this.otrosGastos= BigDecimal.ZERO;
    }

    public Retaceo(String numretaceo) {
        this.numretaceo = numretaceo;
    }

    public Retaceo(String numretaceo, BigDecimal montoFacturas, BigDecimal montoSalida) {
        this.numretaceo = numretaceo;
        this.montoFacturas = montoFacturas;
        this.montoSalida = montoSalida;
    }

    public String getNumretaceo() {
        return numretaceo;
    }

    public void setNumretaceo(String numretaceo) {
        this.numretaceo = numretaceo;
    }

    public BigDecimal getMontoFacturas() {
        return montoFacturas;
    }

    public void setMontoFacturas(BigDecimal montoFacturas) {
        this.montoFacturas = montoFacturas;
    }

    public BigDecimal getMontoSalida() {
        return montoSalida;
    }

    public void setMontoSalida(BigDecimal montoSalida) {
        this.montoSalida = montoSalida;
    }

    public BigDecimal getMontoFlete() {
        return montoFlete;
    }

    public void setMontoFlete(BigDecimal montoFlete) {
        this.montoFlete = montoFlete;
    }

    public BigDecimal getMontoDescuentos() {
        return montoDescuentos;
    }

    public void setMontoDescuentos(BigDecimal montoDescuentos) {
        this.montoDescuentos = montoDescuentos;
    }

    public BigDecimal getMontoTotalFacturas() {
        return montoTotalFacturas;
    }

    public void setMontoTotalFacturas(BigDecimal montoTotalFacturas) {
        this.montoTotalFacturas = montoTotalFacturas;
    }

    public BigDecimal getMontoAgenteAduanal() {
        return montoAgenteAduanal;
    }

    public void setMontoAgenteAduanal(BigDecimal montoAgenteAduanal) {
        this.montoAgenteAduanal = montoAgenteAduanal;
    }

    public BigDecimal getMontoArancel() {
        return montoArancel;
    }

    public void setMontoArancel(BigDecimal montoArancel) {
        this.montoArancel = montoArancel;
    }

    public BigDecimal getMontoTransporte() {
        return montoTransporte;
    }

    public void setMontoTransporte(BigDecimal montoTransporte) {
        this.montoTransporte = montoTransporte;
    }

    public BigDecimal getMontoBodegaje() {
        return montoBodegaje;
    }

    public void setMontoBodegaje(BigDecimal montoBodegaje) {
        this.montoBodegaje = montoBodegaje;
    }

    public BigDecimal getMontoSeguridad() {
        return montoSeguridad;
    }

    public void setMontoSeguridad(BigDecimal montoSeguridad) {
        this.montoSeguridad = montoSeguridad;
    }

    public BigDecimal getMontoSeguro() {
        return montoSeguro;
    }

    public void setMontoSeguro(BigDecimal montoSeguro) {
        this.montoSeguro = montoSeguro;
    }

    public BigDecimal getOtrosGastosCcf() {
        return otrosGastosCcf;
    }

    public void setOtrosGastosCcf(BigDecimal otrosGastosCcf) {
        this.otrosGastosCcf = otrosGastosCcf;
    }

    public BigDecimal getOtrosGastos() {
        return otrosGastos;
    }

    public void setOtrosGastos(BigDecimal otrosGastos) {
        this.otrosGastos = otrosGastos;
    }

    public BigDecimal getFactorRetaceo() {
        return factorRetaceo;
    }

    public void setFactorRetaceo(BigDecimal factorRetaceo) {
        this.factorRetaceo = factorRetaceo;
    }

    public BigDecimal getFactorCostoViaje() {
        return factorCostoViaje;
    }

    public void setFactorCostoViaje(BigDecimal factorCostoViaje) {
        this.factorCostoViaje = factorCostoViaje;
    }

    public String getNotaRetaceo() {
        return notaRetaceo;
    }

    public void setNotaRetaceo(String notaRetaceo) {
        this.notaRetaceo = notaRetaceo;
    }

    public Boolean getAprobado() {
        return aprobado;
    }

    public void setAprobado(Boolean aprobado) {
        this.aprobado = aprobado;
    }

    @XmlTransient
    public Collection<FacturaIngreso> getFacturaIngresoCollection() {
        return facturaIngresoCollection;
    }

    public void setFacturaIngresoCollection(Collection<FacturaIngreso> facturaIngresoCollection) {
        this.facturaIngresoCollection = facturaIngresoCollection;
    }

    public Transaccion getIdtransac() {
        return idtransac;
    }

    public void setIdtransac(Transaccion idtransac) {
        this.idtransac = idtransac;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (numretaceo != null ? numretaceo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Retaceo)) {
            return false;
        }
        Retaceo other = (Retaceo) object;
        if ((this.numretaceo == null && other.numretaceo != null) || (this.numretaceo != null && !this.numretaceo.equals(other.numretaceo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ddicark.entidades.Retaceo[ numretaceo=" + numretaceo + " ]";
    }
    
}
