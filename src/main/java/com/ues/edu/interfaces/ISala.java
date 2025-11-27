/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ues.edu.interfaces;
import com.ues.edu.modelo.Sala;
import com.ues.edu.modelo.estructuras.ListaSimpleCircular;
/**
 *
 * @author radon
 */
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
