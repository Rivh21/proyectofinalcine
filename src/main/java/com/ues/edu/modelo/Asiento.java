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
public class Asiento implements Comparable<Asiento> {

    private int idAsiento;
    private Sala sala;
    private String fila;
    private int numero;
    private List<Boleto> boletos;
    private boolean disponible;
    private String colorEstado;

    public Asiento() {
    }

    public Asiento(int idAsiento, Sala sala, String fila, int numero) {
        this.idAsiento = idAsiento;
        this.sala = sala;
        this.fila = fila;
        this.numero = numero;
        this.disponible = true;
        this.colorEstado = "verde";
    }

    public Asiento(Sala sala, String fila, int numero) {
        this.sala = sala;
        this.fila = fila;
        this.numero = numero;
    }

    public int getIdAsiento() {
        return idAsiento;
    }

    public void setIdAsiento(int idAsiento) {
        this.idAsiento = idAsiento;
    }

    public Sala getSala() {
        return sala;
    }

    public void setSala(Sala sala) {
        this.sala = sala;
    }

    public String getFila() {
        return fila;
    }

    public void setFila(String fila) {
        this.fila = fila;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public List<Boleto> getBoletos() {
        return boletos;
    }

    public void setBoletos(List<Boleto> boletos) {
        this.boletos = boletos;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
        this.colorEstado = disponible ? "verde" : "rojo";
    }

    public String getColorEstado() {
        return colorEstado;
    }

    public void setColorEstado(String colorEstado) {
        this.colorEstado = colorEstado;
    }

    @Override
    public int compareTo(Asiento o) {
        int resultadoFila = this.fila.compareToIgnoreCase(o.getFila());

        if (resultadoFila == 0) {
            return Integer.compare(this.numero, o.getNumero());
        }
        return resultadoFila;
    }

}
