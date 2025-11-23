/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.modelo;

import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author DELL LATITUDE
 */
public class FacturaTaquilla implements Comparable<FacturaTaquilla>{

    private String idFacturaTaquilla;
    private BigDecimal MontoTotal;
    private BigDecimal descuentoAplicado;
    private Usuario usuarios;
    private MetodoPago metodoPago;
    private Empleado empleado;
    private List<Boleto> boletos;

    public FacturaTaquilla() {
        this.descuentoAplicado = BigDecimal.ZERO;
    }

    public FacturaTaquilla(String idFacturaTaquilla, BigDecimal MontoTotal, BigDecimal descuentoAplicado, Usuario usuarios, MetodoPago metodoPago, Empleado empleado) {
        this.idFacturaTaquilla = idFacturaTaquilla;
        this.MontoTotal = MontoTotal;
        this.descuentoAplicado = descuentoAplicado;
        this.usuarios = usuarios;
        this.metodoPago = metodoPago;
        this.empleado = empleado;
    }

    public FacturaTaquilla(BigDecimal MontoTotal, BigDecimal descuentoAplicado, Usuario usuarios, MetodoPago metodoPago, Empleado empleado) {
        this.MontoTotal = MontoTotal;
        this.descuentoAplicado = descuentoAplicado;
        this.usuarios = usuarios;
        this.metodoPago = metodoPago;
        this.empleado = empleado;
    }

    public String getIdFacturaTaquilla() {
        return idFacturaTaquilla;
    }

    public void setIdFacturaTaquilla(String idFacturaTaquilla) {
        this.idFacturaTaquilla = idFacturaTaquilla;
    }

    public BigDecimal getMontoTotal() {
        return MontoTotal;
    }

    public void setMontoTotal(BigDecimal MontoTotal) {
        this.MontoTotal = MontoTotal;
    }

    public BigDecimal getDescuentoAplicado() {
        return descuentoAplicado;
    }

    public void setDescuentoAplicado(BigDecimal descuentoAplicado) {
        this.descuentoAplicado = descuentoAplicado;
    }

    public Usuario getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(Usuario usuarios) {
        this.usuarios = usuarios;
    }

    public MetodoPago getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(MetodoPago metodoPago) {
        this.metodoPago = metodoPago;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public List<Boleto> getBoletos() {
        return boletos;
    }

    public void setBoletos(List<Boleto> boletos) {
        this.boletos = boletos;
    }

    @Override
    public int compareTo(FacturaTaquilla o) {
        return this.idFacturaTaquilla.compareTo(o.idFacturaTaquilla);
    }

}
