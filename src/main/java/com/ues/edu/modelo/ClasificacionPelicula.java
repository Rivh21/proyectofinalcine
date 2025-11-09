package com.ues.edu.modelo;

/**
 * Clasificaciones de películas según restricción de edad.
 */
public enum ClasificacionPelicula {

    TP("Todo Público. Puede ser vista por menores de 12 años."),

    B("Mayores de 12 años. Orientación parental sugerida."),

    C("Mayores de 15 años. Puede contener material de criterio maduro."),

    D("Mayores de 18 años. Puede contener horror detallado, violencia o sexo explícito."),

    E("Exclusivo para adultos. Mayores de 21 años.");

    private final String descripcion;

    ClasificacionPelicula(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public String toString() {
        return name();
    }
}
