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
    
    private int idFuncion; 

    public FuncionHoy() {}
    @Override
    public int compareTo(FuncionHoy otraFuncion) {
        int horaComparison = this.horaInicio.compareTo(otraFuncion.horaInicio);
        if (horaComparison != 0) {
            return horaComparison;
        }
        return Integer.compare(this.idFuncion, otraFuncion.idFuncion); 
    }
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