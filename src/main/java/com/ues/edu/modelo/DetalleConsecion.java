/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.modelo;

/**
 *
 * @author DELL LATITUDE
 */
public class DetalleConsecion implements Comparable<DetalleConsecion>{
    private int idDetalleConcesion;
    private int cantidad;
    private Double precioUnitario;
    private FacturaConcesion factura;
    private Producto producto;  

    public DetalleConsecion() {
    }

    public DetalleConsecion(int idDetalleConcesion) {
        this.idDetalleConcesion = idDetalleConcesion;
    }

    public DetalleConsecion(int cantidad, Double precioUnitario, FacturaConcesion factura, Producto producto) {
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.factura = factura;
        this.producto = producto;
    }

    public int getIdDetalleConcesion() {
        return idDetalleConcesion;
    }

    public void setIdDetalleConcesion(int idDetalleConcesion) {
        this.idDetalleConcesion = idDetalleConcesion;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(Double precioUnitario) {
        this.precioUnitario = precioUnitario;
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

    @Override
    public String toString() {
        return "DetalleConsecion{" + "idDetalleConcesion=" + idDetalleConcesion + ", cantidad=" + cantidad + ", precioUnitario=" + precioUnitario + ", factura=" + factura + ", producto=" + producto + '}';
    }
   
    @Override
    public int compareTo(DetalleConsecion o) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    
    
}
