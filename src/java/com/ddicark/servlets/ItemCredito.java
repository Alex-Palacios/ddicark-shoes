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
public class ItemCredito {
    private String FACTURA;
    private Date FECHA;
    private BigDecimal TOTAL;
    private BigDecimal SALDO;
    private Integer DIAS;

    public ItemCredito() {
    }

    public ItemCredito(String FACTURA, Date FECHA, BigDecimal TOTAL, BigDecimal SALDO, Integer DIAS) {
        this.FACTURA = FACTURA;
        this.FECHA = FECHA;
        this.TOTAL = TOTAL;
        this.SALDO = SALDO;
        this.DIAS = DIAS;
    }

    public String getFACTURA() {
        return FACTURA;
    }

    public void setFACTURA(String FACTURA) {
        this.FACTURA = FACTURA;
    }

    public Date getFECHA() {
        return FECHA;
    }

    public void setFECHA(Date FECHA) {
        this.FECHA = FECHA;
    }

    public BigDecimal getTOTAL() {
        return TOTAL;
    }

    public void setTOTAL(BigDecimal TOTAL) {
        this.TOTAL = TOTAL;
    }

    public BigDecimal getSALDO() {
        return SALDO;
    }

    public void setSALDO(BigDecimal SALDO) {
        this.SALDO = SALDO;
    }

    public Integer getDIAS() {
        return DIAS;
    }

    public void setDIAS(Integer DIAS) {
        this.DIAS = DIAS;
    }
    
    
}
