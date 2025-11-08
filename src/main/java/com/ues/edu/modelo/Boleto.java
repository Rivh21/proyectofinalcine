/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.modelo;

import java.util.Date;

/**
 *
 * @author DELL LATITUDE
 */
public class Boleto implements Comparable<Boleto>{
    private int idBoleto;
    private Date fechaVenta;
    private Funcion funciones;
    private FacturaTaquilla facturas;
    private Asiento asiento;

    public Boleto() {
    }

    public Boleto(int idBoleto) {
        this.idBoleto = idBoleto;
    }

    public Boleto(Date fechaVenta, Funcion funciones, FacturaTaquilla facturas, Asiento asiento) {
        this.fechaVenta = fechaVenta;
        this.funciones = funciones;
        this.facturas = facturas;
        this.asiento = asiento;
    }

    public int getIdBoleto() {
        return idBoleto;
    }

    public void setIdBoleto(int idBoleto) {
        this.idBoleto = idBoleto;
    }

    public Date getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(Date fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public Funcion getFunciones() {
        return funciones;
    }

    public void setFunciones(Funcion funciones) {
        this.funciones = funciones;
    }

    public FacturaTaquilla getFacturas() {
        return facturas;
    }

    public void setFacturas(FacturaTaquilla facturas) {
        this.facturas = facturas;
    }

    public Asiento getAsiento() {
        return asiento;
    }

    public void setAsiento(Asiento asiento) {
        this.asiento = asiento;
    }

    @Override
    public String toString() {
        return "Boletos{" + "idBoleto=" + idBoleto + ", fechaVenta=" + fechaVenta + ", funciones=" + funciones + ", facturas=" + facturas + ", asiento=" + asiento + '}';
    }
   
    @Override
    public int compareTo(Boleto o) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    
    
    
}
