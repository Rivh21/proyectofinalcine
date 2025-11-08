package com.ues.edu.interfaces;

import com.ues.edu.modelo.Sala;
import com.ues.edu.modelo.estructuras.ListaSimpleCircular;

public interface ISala {

    ListaSimpleCircular<Sala> selectAll();

    ListaSimpleCircular<Sala> selectAllTo(String atributo, String valor);

    ListaSimpleCircular<Sala> buscar(String textoBusqueda);

    boolean insert(Sala sala);

    boolean update(Sala sala);

    boolean delete(Sala sala);

    Sala buscarPorId(int idSala);

    Sala buscarPorNombre(String nombre);
}
