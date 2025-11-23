package com.ues.edu.interfaces;

import com.ues.edu.modelo.Funcion;
import com.ues.edu.modelo.estructuras.ListaSimpleCircular;

public interface IFuncion {

    public ListaSimpleCircular<Funcion> selectAll();

    public ListaSimpleCircular<Funcion> buscar(String textoBusqueda);

    public boolean insert(Funcion funcion);

    public boolean update(Funcion funcion);

    public boolean delete(Funcion funcion);

    public Funcion buscarPorId(int id);

    public ListaSimpleCircular<Funcion> selectProximasFunciones();
}
