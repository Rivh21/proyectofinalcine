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
public class Asiento {

    private int idAsiento;
    private Sala sala;
    private int fila;
    private int numero;
    private List<Boleto> boletos;

    //Atributo temporal para la vista
    private boolean disponible;
    private String colorEstado;

    public Asiento() {
    }

    public Asiento(int idAsiento) {
        this.idAsiento = idAsiento;
    }

    public Asiento(Sala sala, int fila, int numero, List<Boleto> boletos) {
        this.sala = sala;
        this.fila = fila;
        this.numero = numero;
        this.boletos = boletos;
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

    public int getFila() {
        return fila;
    }

    public void setFila(int fila) {
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

}
