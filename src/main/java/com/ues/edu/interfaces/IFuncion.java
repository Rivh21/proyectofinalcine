package com.ues.edu.interfaces;

import com.ues.edu.modelo.Funcion;
import com.ues.edu.modelo.estructuras.ListaSimpleCircular;

public interface IFuncion {

    ListaSimpleCircular<Funcion> selectAll();

    ListaSimpleCircular<Funcion> buscar(String textoBusqueda);

    boolean insert(Funcion funcion);

    boolean update(Funcion funcion);

    boolean delete(Funcion funcion);

    Funcion buscarPorId(int id);
}
