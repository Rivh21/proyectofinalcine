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
public class FacturaConcesion implements Comparable<FacturaConcesion> {

    private int idFacturaConcesion;
    private Double montoTotal;
    private Usuario usuarios;
    private MetodoPago metodoPago;
    private Empleado empleado;
    private List<DetalleConsecion> detalleconsecion;

    public FacturaConcesion() {
    }

    public FacturaConcesion(int idFacturaConcesion) {
        this.idFacturaConcesion = idFacturaConcesion;
    }

    public FacturaConcesion(Double montoTotal, Usuario usuarios, MetodoPago metodoPago, Empleado empleado, List<DetalleConsecion> detalleconsecion) {
        this.montoTotal = montoTotal;
        this.usuarios = usuarios;
        this.metodoPago = metodoPago;
        this.empleado = empleado;
        this.detalleconsecion = detalleconsecion;
    }

    public int getIdFacturaConcesion() {
        return idFacturaConcesion;
    }

    public void setIdFacturaConcesion(int idFacturaConcesion) {
        this.idFacturaConcesion = idFacturaConcesion;
    }

    public Double getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(Double montoTotal) {
        this.montoTotal = montoTotal;
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

    public List<DetalleConsecion> getDetalleconsecion() {
        return detalleconsecion;
    }

    public void setDetalleconsecion(List<DetalleConsecion> detalleconsecion) {
        this.detalleconsecion = detalleconsecion;
    }

    @Override
    public String toString() {
        return "FacturaConcesion{" + "idFacturaConcesion=" + idFacturaConcesion + ", montoTotal=" + montoTotal + ", usuarios=" + usuarios + ", metodoPago=" + metodoPago + ", empleado=" + empleado + ", detalleconsecion=" + detalleconsecion + '}';
    }

    @Override
    public int compareTo(FacturaConcesion o) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
