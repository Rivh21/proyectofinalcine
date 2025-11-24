/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.modelo;

import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author jorge
 */
public class FacturaTaquilla implements Comparable<FacturaTaquilla> {

    private String idFacturaTaquilla;
    private BigDecimal MontoTotal;
    private BigDecimal descuentoAplicado;
    private String estado; 
    private Usuario usuarios;
    private MetodoPago metodoPago;
    private Empleado empleado;
    private List<Boleto> boletos;

    public FacturaTaquilla() {
        this.descuentoAplicado = BigDecimal.ZERO;
        this.estado = "VALIDA"; 
    }

    public FacturaTaquilla(String idFacturaTaquilla, BigDecimal MontoTotal, BigDecimal descuentoAplicado, String estado, Usuario usuarios, MetodoPago metodoPago, Empleado empleado) {
        this.idFacturaTaquilla = idFacturaTaquilla;
        this.MontoTotal = MontoTotal;
        this.descuentoAplicado = descuentoAplicado;
        this.estado = estado;
        this.usuarios = usuarios;
        this.metodoPago = metodoPago;
        this.empleado = empleado;
    }

    public void calcularImportes(double precioUnitario, int cantidadBoletos, double porcentajeDescuento) {
        BigDecimal precio = BigDecimal.valueOf(precioUnitario);
        BigDecimal cantidad = BigDecimal.valueOf(cantidadBoletos);
        BigDecimal porcentaje = BigDecimal.valueOf(porcentajeDescuento);
        BigDecimal totalBruto = precio.multiply(cantidad);
        BigDecimal montoDescontado = totalBruto.multiply(porcentaje);
        BigDecimal totalNeto = totalBruto.subtract(montoDescontado);
        this.descuentoAplicado = montoDescontado;
        this.MontoTotal = totalNeto;
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
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
