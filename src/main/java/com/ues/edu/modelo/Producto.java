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
public class Producto {

    private int idProducto;
    private String nombre;
    private Double precioVenta;
    private String stockActual;
    private List<DetalleConsecion> detalleonsecion;

    public Producto() {
    }

    public Producto(int idProducto) {
        this.idProducto = idProducto;
    }

    public Producto(String nombre, Double precioVenta, String stockActual, List<DetalleConsecion> detalleonsecion) {
        this.nombre = nombre;
        this.precioVenta = precioVenta;
        this.stockActual = stockActual;
        this.detalleonsecion = detalleonsecion;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Double getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(Double precioVenta) {
        this.precioVenta = precioVenta;
    }

    public String getStockActual() {
        return stockActual;
    }

    public void setStockActual(String stockActual) {
        this.stockActual = stockActual;
    }

    public List<DetalleConsecion> getDetalleonsecion() {
        return detalleonsecion;
    }

    public void setDetalleonsecion(List<DetalleConsecion> detalleonsecion) {
        this.detalleonsecion = detalleonsecion;
    }

    @Override
    public String toString() {
        return "Producto{" + "idProducto=" + idProducto + ", nombre=" + nombre + ", precioVenta=" + precioVenta + ", stockActual=" + stockActual + ", detalleonsecion=" + detalleonsecion + '}';
    }

}
