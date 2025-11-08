package com.ues.edu.modelo;

/**
 *
 * @author radon
 */
public class Pelicula implements Comparable<Pelicula> {

    private int id_pelicula;
    private String titulo;
    private int duracion_minutos;
    private String genero;
    private String clasificacion;

    public Pelicula() {
    }

    public Pelicula(int id_pelicula, String titulo, int duracion_minutos, String genero, String clasificacion) {
        this.id_pelicula = id_pelicula;
        this.titulo = titulo;
        this.duracion_minutos = duracion_minutos;
        this.genero = genero;
        this.clasificacion = clasificacion;
    }

    public Pelicula(String titulo, int duracion_minutos, String genero, String clasificacion) {
        this.titulo = titulo;
        this.duracion_minutos = duracion_minutos;
        this.genero = genero;
        this.clasificacion = clasificacion;
    }

    public int getId_pelicula() {
        return id_pelicula;
    }

    public void setId_pelicula(int id_pelicula) {
        this.id_pelicula = id_pelicula;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getDuracion_minutos() {
        return duracion_minutos;
    }

    public void setDuracion_minutos(int duracion_minutos) {
        this.duracion_minutos = duracion_minutos;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getClasificacion() {
        return clasificacion;
    }

    public void setClasificacion(String clasificacion) {
        this.clasificacion = clasificacion;
    }

    @Override
    public int compareTo(Pelicula otraPelicula) {
        int tituloComp = this.titulo.compareToIgnoreCase(otraPelicula.titulo);

        if (tituloComp == 0) {
            return Integer.compare(this.id_pelicula, otraPelicula.id_pelicula);
        }
        return tituloComp;
    }

    @Override
    public String toString() {
        return titulo;
     }
}