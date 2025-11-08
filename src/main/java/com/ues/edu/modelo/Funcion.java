package com.ues.edu.modelo;

import java.time.LocalDateTime;

/**
 *
 * @author radon
 */
public class Funcion implements Comparable<Funcion> {
    
    private int id_funcion;
    private int id_pelicula;
    private int id_sala;
    private LocalDateTime fecha_hora_inicio;
    private double precio_boleto;
    private String peliculaTitulo;
    private String salaNombre;

    public Funcion() {}

    public Funcion(int id_funcion, int id_pelicula, int id_sala, LocalDateTime fecha_hora_inicio, double precio_boleto) {
        this.id_funcion = id_funcion;
        this.id_pelicula = id_pelicula;
        this.id_sala = id_sala;
        this.fecha_hora_inicio = fecha_hora_inicio;
        this.precio_boleto = precio_boleto;
    }

    public Funcion(int id_pelicula, int id_sala, LocalDateTime fecha_hora_inicio, double precio_boleto) {
        this.id_pelicula = id_pelicula;
        this.id_sala = id_sala;
        this.fecha_hora_inicio = fecha_hora_inicio;
        this.precio_boleto = precio_boleto;
    }

    public int getId_funcion() {
        return id_funcion;
    }

    public void setId_funcion(int id_funcion) {
        this.id_funcion = id_funcion;
    }

    public int getId_pelicula() {
        return id_pelicula;
    }

    public void setId_pelicula(int id_pelicula) {
        this.id_pelicula = id_pelicula;
    }

    public int getId_sala() {
        return id_sala;
    }

    public void setId_sala(int id_sala) {
        this.id_sala = id_sala;
    }

    public LocalDateTime getFecha_hora_inicio() {
        return fecha_hora_inicio;
    }

    public void setFecha_hora_inicio(LocalDateTime fecha_hora_inicio) {
        this.fecha_hora_inicio = fecha_hora_inicio;
    }

    public double getPrecio_boleto() {
        return precio_boleto;
    }

    public void setPrecio_boleto(double precio_boleto) {
        this.precio_boleto = precio_boleto;
    }

    public String getPeliculaTitulo() {
        return peliculaTitulo;
    }

    public void setPeliculaTitulo(String peliculaTitulo) {
        this.peliculaTitulo = peliculaTitulo;
    }

    public String getSalaNombre() {
        return salaNombre;
    }

    public void setSalaNombre(String salaNombre) {
        this.salaNombre = salaNombre;
    }

    @Override
    public String toString() {
        String titulo = (peliculaTitulo != null) ? peliculaTitulo : "Película";
        String sala = (salaNombre != null) ? salaNombre : "Sala";
        String fecha = (fecha_hora_inicio != null) ? fecha_hora_inicio.toString() : "Fecha no definida";
        return titulo + " - " + sala + " - " + fecha;
    }

    @Override
    public int compareTo(Funcion otra) {
        if (this.fecha_hora_inicio == null && otra.fecha_hora_inicio == null) return 0;
        if (this.fecha_hora_inicio == null) return -1;
        if (otra.fecha_hora_inicio == null) return 1;
        return this.fecha_hora_inicio.compareTo(otra.fecha_hora_inicio);
    }
}
