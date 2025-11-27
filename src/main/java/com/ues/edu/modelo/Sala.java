/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.modelo;

/**
 *
 * @author radon
 */
public class Sala implements Comparable<Sala> {

    private int idSala;
    private String nombreSala;

    public Sala() {
    }

    public Sala(int idSala, String nombreSala) {
        this.idSala = idSala;
        this.nombreSala = nombreSala;
    }

    public int getIdSala() {
        return idSala;
    }

    public void setIdSala(int idSala) {
        this.idSala = idSala;
    }

    public String getNombreSala() {
        return nombreSala;
    }

    public void setNombreSala(String nombreSala) {
        this.nombreSala = nombreSala;
    }

    @Override
    public int compareTo(Sala otra) {
        return this.nombreSala.compareToIgnoreCase(otra.getNombreSala());
    }

    @Override
    public String toString() {
        return nombreSala;
    }
}
