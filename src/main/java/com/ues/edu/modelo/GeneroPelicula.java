/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.modelo;
/**
 *
 * @author radon
 */

public enum GeneroPelicula {

    ACCION("Acción"),
    COMEDIA("Comedia"),
    DRAMA("Drama"),
    TERROR("Terror"),
    CIENCIA_FICCION("Ciencia ficción"),
    ROMANCE("Romance"),
    DOCUMENTAL("Documental"),
    ANIMACION("Animación");

    private final String etiqueta;

    GeneroPelicula(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    @Override
    public String toString() {
        return etiqueta;
    }
}

