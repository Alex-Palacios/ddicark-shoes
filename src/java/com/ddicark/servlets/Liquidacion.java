/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ddicark.servlets;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author DDICARK-DEVELOPMENT
 */
public class Liquidacion {
    private String TIPO;
    private Date FECHA;
    private String FACTURA;
    private String CLIENTE;
    private String MUNICIPO;
    private BigDecimal MONTO;
    private BigDecimal SALDO;
     private BigDecimal LIMITE;
     private long DIAS;

    public Liquidacion() {
    }

    public Liquidacion(String TIPO, Date FECHA, String FACTURA, String CLIENTE, String MUNICIPO, BigDecimal MONTO,BigDecimal SALDO,BigDecimal LIMITE,long DIAS) {
        this.TIPO = TIPO;
        this.FECHA = FECHA;
        this.FACTURA = FACTURA;
        this.CLIENTE = CLIENTE;
        this.MUNICIPO = MUNICIPO;
        this.MONTO = MONTO;
        this.SALDO = SALDO;
        this.LIMITE = LIMITE;
        this.DIAS = DIAS;
    }

    public String getTIPO() {
        return TIPO;
    }

    public void setTIPO(String TIPO) {
        this.TIPO = TIPO;
    }

    public Date getFECHA() {
        return FECHA;
    }

    public void setFECHA(Date FECHA) {
        this.FECHA = FECHA;
    }

    public String getFACTURA() {
        return FACTURA;
    }

    public void setFACTURA(String FACTURA) {
        this.FACTURA = FACTURA;
    }

    public String getCLIENTE() {
        return CLIENTE;
    }

    public void setCLIENTE(String CLIENTE) {
        this.CLIENTE = CLIENTE;
    }

    public String getMUNICIPO() {
        return MUNICIPO;
    }

    public void setMUNICIPO(String MUNICIPO) {
        this.MUNICIPO = MUNICIPO;
    }

    public BigDecimal getMONTO() {
        return MONTO;
    }

    public void setMONTO(BigDecimal MONTO) {
        this.MONTO = MONTO;
    }

    
    public BigDecimal getSALDO() {
        return SALDO;
    }

    public void setSALDO(BigDecimal SALDO) {
        this.SALDO = SALDO;
    }

    public BigDecimal getLIMITE() {
        return LIMITE;
    }

    public void setLIMITE(BigDecimal LIMITE) {
        this.LIMITE = LIMITE;
    }

    public long getDIAS() {
        return DIAS;
    }

    public void setDIAS(long DIAS) {
        this.DIAS = DIAS;
    }
    
    
}
