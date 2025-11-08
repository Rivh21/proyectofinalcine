package com.ues.edu.modelo;

/**
 *
 * @author radon
 */
public class Sala implements Comparable<Sala> {
    private int id_sala;
    private String nombre_sala;

    public Sala() {
    }
    
    

    public Sala(int id_sala, String nombre_sala) {
        this.id_sala = id_sala;
        this.nombre_sala = nombre_sala;
    }

    public int getId_sala() {
        return id_sala;
    }

    public void setId_sala(int id_sala) {
        this.id_sala = id_sala;
    }

    public String getNombre_sala() {
        return nombre_sala;
    }

    public void setNombre_sala(String nombre_sala) {
        this.nombre_sala = nombre_sala;
    }

    @Override
    public int compareTo(Sala otra) {
        // Ordena por nombre_sala
        return this.nombre_sala.compareToIgnoreCase(otra.getNombre_sala());
    }

    @Override
    public String toString() {
        return nombre_sala;
    }
}
