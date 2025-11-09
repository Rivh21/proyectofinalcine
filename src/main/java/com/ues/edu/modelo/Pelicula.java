package com.ues.edu.modelo;

/**
 *
 * @author radon
 */
public class Pelicula implements Comparable<Pelicula> {

    private int idPelicula;
    private String titulo;
    private int duracionMinutos;
    private GeneroPelicula genero;
    private ClasificacionPelicula clasificacion;

    public Pelicula() {
    }

    public Pelicula(int idPelicula, String titulo, int duracionMinutos, GeneroPelicula genero, ClasificacionPelicula clasificacion) {
        this.idPelicula = idPelicula;
        this.titulo = titulo;
        this.duracionMinutos = duracionMinutos;
        this.genero = genero;
        this.clasificacion = clasificacion;
    }

    public Pelicula(String titulo, int duracionMinutos, GeneroPelicula genero, ClasificacionPelicula clasificacion) {
        this.titulo = titulo;
        this.duracionMinutos = duracionMinutos;
        this.genero = genero;
        this.clasificacion = clasificacion;
    }

    public int getIdPelicula() {
        return idPelicula;
    }

    public void setIdPelicula(int idPelicula) {
        this.idPelicula = idPelicula;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getDuracionMinutos() {
        return duracionMinutos;
    }

    public void setDuracionMinutos(int duracionMinutos) {
        this.duracionMinutos = duracionMinutos;
    }

    public GeneroPelicula getGenero() {
        return genero;
    }

    public void setGenero(GeneroPelicula genero) {
        this.genero = genero;
    }

    public ClasificacionPelicula getClasificacion() {
        return clasificacion;
    }

    public void setClasificacion(ClasificacionPelicula clasificacion) {
        this.clasificacion = clasificacion;
    }

    @Override
    public int compareTo(Pelicula otra) {
        int cmpTitulo = this.titulo.compareToIgnoreCase(otra.titulo);
        return (cmpTitulo != 0) ? cmpTitulo : Integer.compare(this.idPelicula, otra.idPelicula);
    }

    @Override
    public String toString() {
        return titulo;
    }
}
