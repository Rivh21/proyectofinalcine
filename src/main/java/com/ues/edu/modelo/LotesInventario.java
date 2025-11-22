package com.ues.edu.modelo;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class LotesInventario {

    private int idLote;
    private int idProducto;
    private int cantidadDisponible;
    private LocalDate fechaCaducidad;
    private LocalDate fechaRecepcion;
    private String productoNombre;
    private int prioridad;

    public LotesInventario() {}

    public LotesInventario(int idProducto, int cantidadDisponible, LocalDate fechaCaducidad, LocalDate fechaRecepcion) {
        this.idProducto = idProducto;
        this.cantidadDisponible = cantidadDisponible;
        this.fechaCaducidad = fechaCaducidad;
        this.fechaRecepcion = fechaRecepcion;
        calcularPrioridad(); 
    }

    public int getIdLote() {
        return idLote;
    }

    public void setIdLote(int idLote) {
        this.idLote = idLote;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public int getCantidadDisponible() {
        return cantidadDisponible;
    }

    public void setCantidadDisponible(int cantidadDisponible) {
        this.cantidadDisponible = cantidadDisponible;
    }

    public LocalDate getFechaCaducidad() {
        return fechaCaducidad;
    }

    public void setFechaCaducidad(LocalDate fechaCaducidad) {
        this.fechaCaducidad = fechaCaducidad;
        calcularPrioridad(); 
    }

    public LocalDate getFechaRecepcion() {
        return fechaRecepcion;
    }

    public void setFechaRecepcion(LocalDate fechaRecepcion) {
        this.fechaRecepcion = fechaRecepcion;
    }

    public String getProductoNombre() {
        return productoNombre;
    }

    public void setProductoNombre(String productoNombre) {
        this.productoNombre = productoNombre;
    }

    public int getPrioridad() {
        return prioridad;
    }

    private void setPrioridad(int prioridad) {
        this.prioridad = prioridad;
    }
    public void calcularPrioridad() {
    if (fechaCaducidad == null) {
        prioridad = Integer.MAX_VALUE; 
        return;
    }

    long dias = ChronoUnit.DAYS.between(LocalDate.now(), fechaCaducidad);

    if (dias <= 5) {
        prioridad = 0;  
    } else if (dias <= 15) {
        prioridad = 1; 
    } else {
        prioridad = 2;  
    }
}


    @Override
    public String toString() {
        return "LotesInventario{" +
                "idLote=" + idLote +
                ", idProducto=" + idProducto +
                ", productoNombre='" + productoNombre + '\'' +
                ", cantidadDisponible=" + cantidadDisponible +
                ", fechaCaducidad=" + fechaCaducidad +
                ", fechaRecepcion=" + fechaRecepcion +
                ", prioridad=" + prioridad +
                '}';
    }
}
