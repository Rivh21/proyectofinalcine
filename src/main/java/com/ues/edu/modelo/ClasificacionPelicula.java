/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.modelo;

/**
 * radon
 */
public enum ClasificacionPelicula {

    TP(0, "Todo Público. Puede ser vista por menores de 12 años."),
    B(12, "Mayores de 12 años. Orientación parental sugerida."),
    C(15, "Mayores de 15 años. Puede contener material de criterio maduro."),
    D(18, "Mayores de 18 años. Puede contener horror detallado, violencia o sexo explícito."),
    E(21, "Exclusivo para adultos. Mayores de 21 años.");

    private final int edadMinima;
    private final String descripcion;

    ClasificacionPelicula(int edadMinima, String descripcion) {
        this.edadMinima = edadMinima;
        this.descripcion = descripcion;
    }

    public int getEdadMinima() {
        return edadMinima;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public String toString() {
        return name() + "-" + edadMinima; 
    }
}
