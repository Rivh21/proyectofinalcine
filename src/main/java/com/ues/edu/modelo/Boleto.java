/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.modelo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 *
 * @author DELL LATITUDE
 */
public class Boleto implements Comparable<Boleto> {

    private int idBoleto;
    private LocalDateTime fechaVenta;
    private BigDecimal precioPagado;
    private Funcion funcion;
    private FacturaTaquilla factura;
    private Asiento asiento;

    public Boleto() {
    }

    public Boleto(int idBoleto, LocalDateTime fechaVenta, BigDecimal precioPagado, Funcion funcion, FacturaTaquilla factura, Asiento asiento) {
        this.idBoleto = idBoleto;
        this.fechaVenta = fechaVenta;
        this.precioPagado = precioPagado;
        this.funcion = funcion;
        this.factura = factura;
        this.asiento = asiento;
    }

    public Boleto(LocalDateTime fechaVenta, BigDecimal precioPagado, Funcion funcion, FacturaTaquilla factura, Asiento asiento) {
        this.fechaVenta = fechaVenta;
        this.precioPagado = precioPagado;
        this.funcion = funcion;
        this.factura = factura;
        this.asiento = asiento;
    }

    public int getIdBoleto() {
        return idBoleto;
    }

    public void setIdBoleto(int idBoleto) {
        this.idBoleto = idBoleto;
    }

    public LocalDateTime getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(LocalDateTime fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public BigDecimal getPrecioPagado() {
        return precioPagado;
    }

    public void setPrecioPagado(BigDecimal precioPagado) {
        this.precioPagado = precioPagado;
    }

    public Funcion getFuncion() {
        return funcion;
    }

    public void setFuncion(Funcion funcion) {
        this.funcion = funcion;
    }

    public FacturaTaquilla getFactura() {
        return factura;
    }

    public void setFactura(FacturaTaquilla factura) {
        this.factura = factura;
    }

    public Asiento getAsiento() {
        return asiento;
    }

    public void setAsiento(Asiento asiento) {
        this.asiento = asiento;
    }

    @Override
    public int compareTo(Boleto o) {
        return this.fechaVenta.compareTo(o.fechaVenta);
    }
}
