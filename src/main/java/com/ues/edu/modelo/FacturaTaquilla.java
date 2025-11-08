/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.modelo;

import java.util.List;

/**
 *
 * @author DELL LATITUDE
 */
public class FacturaTaquilla implements Comparable<FacturaTaquilla> {

    private int idFacturaTaquilla;
    private Double MontoTotal;
    private Usuario usuarios;
    private MetodoPago metodopago;
    private Empleado empleado;
    private List<Boleto> boletos;

    public FacturaTaquilla() {
    }

    public FacturaTaquilla(int idFacturaTaquilla, Double MontoTotal, Usuario usuarios, MetodoPago metodopago, Empleado empleado) {
        this.idFacturaTaquilla = idFacturaTaquilla;
        this.MontoTotal = MontoTotal;
        this.usuarios = usuarios;
        this.metodopago = metodopago;
        this.empleado = empleado;
    }

    public FacturaTaquilla(Double MontoTotal, Usuario usuarios, MetodoPago metodopago, Empleado empleado) {
        this.MontoTotal = MontoTotal;
        this.usuarios = usuarios;
        this.metodopago = metodopago;
        this.empleado = empleado;
    }

    public int getIdFacturaTaquilla() {
        return idFacturaTaquilla;
    }

    public void setIdFacturaTaquilla(int idFacturaTaquilla) {
        this.idFacturaTaquilla = idFacturaTaquilla;
    }

    public Double getMontoTotal() {
        return MontoTotal;
    }

    public void setMontoTotal(Double MontoTotal) {
        this.MontoTotal = MontoTotal;
    }

    public Usuario getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(Usuario usuarios) {
        this.usuarios = usuarios;
    }

    public MetodoPago getMetodopago() {
        return metodopago;
    }

    public void setMetodopago(MetodoPago metodopago) {
        this.metodopago = metodopago;
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
    public String toString() {
        return "FacturasTaquillas{" + "idFacturaTaquilla=" + idFacturaTaquilla + ", MontoTotal=" + MontoTotal + ", usuarios=" + usuarios + ", metodopago=" + metodopago + ", empleado=" + empleado + ", boletos=" + boletos + '}';
    }

    @Override
    public int compareTo(FacturaTaquilla o) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
