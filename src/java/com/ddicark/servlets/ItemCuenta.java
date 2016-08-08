/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ddicark.servlets;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author ALEX
 */
public class ItemCuenta {
    private Date FECHA;
    private String TRANSACCION;
    private String DOCUMENTO;
    private BigDecimal DEBITO;
    private BigDecimal CREDITO;
    private BigDecimal SALDO;

    public ItemCuenta() {
    }

    public ItemCuenta(Date FECHA, String TRANSACCION, String DOCUMENTO, BigDecimal DEBITO, BigDecimal CREDITO, BigDecimal SALDO) {
        this.FECHA = FECHA;
        this.TRANSACCION = TRANSACCION;
        this.DOCUMENTO = DOCUMENTO;
        this.DEBITO = DEBITO;
        this.CREDITO = CREDITO;
        this.SALDO = SALDO;
    }

    public Date getFECHA() {
        return FECHA;
    }

    public void setFECHA(Date FECHA) {
        this.FECHA = FECHA;
    }

    public String getTRANSACCION() {
        return TRANSACCION;
    }

    public void setTRANSACCION(String TRANSACCION) {
        this.TRANSACCION = TRANSACCION;
    }

    public String getDOCUMENTO() {
        return DOCUMENTO;
    }

    public void setDOCUMENTO(String DOCUMENTO) {
        this.DOCUMENTO = DOCUMENTO;
    }

    public BigDecimal getDEBITO() {
        return DEBITO;
    }

    public void setDEBITO(BigDecimal DEBITO) {
        this.DEBITO = DEBITO;
    }

    public BigDecimal getCREDITO() {
        return CREDITO;
    }

    public void setCREDITO(BigDecimal CREDITO) {
        this.CREDITO = CREDITO;
    }

    public BigDecimal getSALDO() {
        return SALDO;
    }

    public void setSALDO(BigDecimal SALDO) {
        this.SALDO = SALDO;
    }
    
    
    
}
