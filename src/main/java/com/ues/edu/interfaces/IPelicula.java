/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ues.edu.interfaces;
import com.ues.edu.modelo.Pelicula;
import com.ues.edu.modelo.estructuras.ListaSimpleCircular;
/**
 *
 * @author radon
 */
public interface IPelicula {

    ListaSimpleCircular<Pelicula> selectAll();

    ListaSimpleCircular<Pelicula> selectAllTo(String atributo, String condicion);

    ListaSimpleCircular<Pelicula> buscar(String textoBusqueda);

    boolean insert(Pelicula pelicula);

    boolean update(Pelicula pelicula);

    boolean delete(Pelicula pelicula);

    Pelicula buscarPorId(int id_pelicula);
}
