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
    private Sala salas;
    private int fila;
    private int numero;
    private List<Boleto> boletos;

    public Asiento() {
    }

    public Asiento(int idAsiento) {
        this.idAsiento = idAsiento;
    }

    public Asiento(Sala salas, int fila, int numero, List<Boleto> boletos) {
        this.salas = salas;
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

    public Sala getSalas() {
        return salas;
    }

    public void setSalas(Sala salas) {
        this.salas = salas;
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

    @Override
    public String toString() {
        return "Asientos{" + "idAsiento=" + idAsiento + ", salas=" + salas + ", fila=" + fila + ", numero=" + numero + ", boletos=" + boletos + '}';
    }

   
}
