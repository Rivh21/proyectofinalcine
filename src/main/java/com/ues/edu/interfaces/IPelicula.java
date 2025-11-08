package com.ues.edu.interfaces;

import com.ues.edu.modelo.Pelicula;
import com.ues.edu.modelo.estructuras.ListaSimpleCircular;

public interface IPelicula {

    ListaSimpleCircular<Pelicula> selectAll();

    ListaSimpleCircular<Pelicula> selectAllTo(String atributo, String condicion);

    ListaSimpleCircular<Pelicula> buscar(String textoBusqueda);

    boolean insert(Pelicula pelicula);

    boolean update(Pelicula pelicula);

    boolean delete(Pelicula pelicula);

    Pelicula buscarPorId(int id_pelicula);
}
