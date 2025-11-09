package com.ues.edu.modelo;

import java.time.LocalDateTime;

/**
 *
 * @author radon
 */
public class Funcion implements Comparable<Funcion> {
    
    private int idFuncion;
    private int idPelicula;
    private int idSala;
    private LocalDateTime fechaHoraInicio;
    private double precioBoleto;
    private String peliculaTitulo;
    private String salaNombre;

    public Funcion() {}

    public Funcion(int idFuncion, int idPelicula, int idSala, LocalDateTime fechaHoraInicio, double precioBoleto) {
        this.idFuncion = idFuncion;
        this.idPelicula = idPelicula;
        this.idSala = idSala;
        this.fechaHoraInicio = fechaHoraInicio;
        this.precioBoleto = precioBoleto;
    }

    public Funcion(int idPelicula, int idSala, LocalDateTime fechaHoraInicio, double precioBoleto) {
        this.idPelicula = idPelicula;
        this.idSala = idSala;
        this.fechaHoraInicio = fechaHoraInicio;
        this.precioBoleto = precioBoleto;
    }

    public int getIdFuncion() {
        return idFuncion;
    }

    public void setIdFuncion(int idFuncion) {
        this.idFuncion = idFuncion;
    }

    public int getIdPelicula() {
        return idPelicula;
    }

    public void setIdPelicula(int idPelicula) {
        this.idPelicula = idPelicula;
    }

    public int getIdSala() {
        return idSala;
    }

    public void setIdSala(int idSala) {
        this.idSala = idSala;
    }

    public LocalDateTime getFechaHoraInicio() {
        return fechaHoraInicio;
    }

    public void setFechaHoraInicio(LocalDateTime fechaHoraInicio) {
        this.fechaHoraInicio = fechaHoraInicio;
    }

    public double getPrecioBoleto() {
        return precioBoleto;
    }

    public void setPrecioBoleto(double precioBoleto) {
        this.precioBoleto = precioBoleto;
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
        String fecha = (fechaHoraInicio != null) ? fechaHoraInicio.toString() : "Fecha no definida";
        return titulo + " - " + sala + " - " + fecha;
    }

    @Override
    public int compareTo(Funcion otra) {
        if (this.fechaHoraInicio == null && otra.fechaHoraInicio == null) return 0;
        if (this.fechaHoraInicio == null) return -1;
        if (otra.fechaHoraInicio == null) return 1;
        return this.fechaHoraInicio.compareTo(otra.fechaHoraInicio);
    }
}
