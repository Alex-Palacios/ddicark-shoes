/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ddicark.servlets;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author Alex
 */
public class ReporteCV {
    
    private String cliente;
    private String direccion;
    private String telefono;
    private String negocio;
    private String municipio;
    private BigDecimal saldo;
    

    public ReporteCV() {
    }

    public ReporteCV(String municipio, String cliente, String direccion,String telefono, String negocio,BigDecimal saldo) {
        this.municipio = municipio;
        this.cliente = cliente;
        this.direccion = direccion;
        this.negocio = negocio;
        this.telefono = telefono;
        this.saldo = saldo;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getNegocio() {
        return negocio;
    }

    public void setNegocio(String negocio) {
        this.negocio = negocio;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    
}
