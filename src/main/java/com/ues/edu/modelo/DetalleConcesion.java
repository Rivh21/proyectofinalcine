/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.modelo;
/**
 *
 * @author radon
 */
public class DetalleConcesion implements Comparable<DetalleConcesion> {

    private int idDetalle;
    private int cantidad;
    private Double precioUnitario;
    private Double subtotal;
    private FacturaConcesion factura;
    private Producto producto;

    public DetalleConcesion() {}

    public DetalleConcesion(int idDetalle) {
        this.idDetalle = idDetalle;
    }

    public DetalleConcesion(int cantidad, Double precioUnitario, FacturaConcesion factura, Producto producto) {
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.factura = factura;
        this.producto = producto;
        recalcularSubtotal();
    }

    public int getIdDetalle() {
        return idDetalle;
    }

    public void setIdDetalle(int idDetalle) {
        this.idDetalle = idDetalle;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
        recalcularSubtotal();
    }

    public Double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(Double precioUnitario) {
        this.precioUnitario = precioUnitario;
        recalcularSubtotal();
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public void recalcularSubtotal() {
        if (precioUnitario != null) {
            this.subtotal = cantidad * precioUnitario;
        }
    }

    public FacturaConcesion getFactura() {
        return factura;
    }

    public void setFactura(FacturaConcesion factura) {
        this.factura = factura;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    // --------------------------------------------------------------------
    //   compareTo NECESARIO para que ListaSimple funcione sin errores
    // --------------------------------------------------------------------
    @Override
    public int compareTo(DetalleConcesion o) {
        if (o == null) return 1;                    // Evita NullPointer
        return Integer.compare(this.idDetalle, o.getIdDetalle());
    }

    @Override
    public String toString() {
        return "DetalleConcesion{" +
                "idDetalle=" + idDetalle +
                ", cantidad=" + cantidad +
                ", precioUnitario=" + precioUnitario +
                ", subtotal=" + subtotal +
                ", factura=" + factura +
                ", producto=" + producto +
                '}';
    }
}
