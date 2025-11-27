/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.modelo;
/**
 *
 * @author radon
 */
public class FuncionHoy implements Comparable<FuncionHoy> { 
    
    private String horaInicio; 
    private String tituloPelicula;
    private String nombreSala;
    private String estado; 
    
    // Atributo interno, NO visible en la JTable
    private int idFuncion; 

    public FuncionHoy() {}

    // -------------------------------------------------------------------
    // MÉTODO compareTo (Implementación de Comparable)
    // -------------------------------------------------------------------
    @Override
    public int compareTo(FuncionHoy otraFuncion) {
        // 1. Comparamos por la hora de inicio (el campo clave para el orden de la cartelera)
        int horaComparison = this.horaInicio.compareTo(otraFuncion.horaInicio);
        if (horaComparison != 0) {
            return horaComparison;
        }
        // 2. Si las horas son idénticas, usamos el ID como desempate
        return Integer.compare(this.idFuncion, otraFuncion.idFuncion); 
    }

    // --- Getters y Setters ---

    public int getIdFuncion() {
        return idFuncion;
    }

    public void setIdFuncion(int idFuncion) {
        this.idFuncion = idFuncion;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getTituloPelicula() {
        return tituloPelicula;
    }

    public void setTituloPelicula(String tituloPelicula) {
        this.tituloPelicula = tituloPelicula;
    }

    public String getNombreSala() {
        return nombreSala;
    }

    public void setNombreSala(String nombreSala) {
        this.nombreSala = nombreSala;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}