/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.interfaces;
import com.ues.edu.modelo.Funcion;
import com.ues.edu.modelo.estructuras.ListaSimpleCircular;
/**
 *
 * @author radon
 */
public interface IFuncion {

    public ListaSimpleCircular<Funcion> selectAll();

    public ListaSimpleCircular<Funcion> buscar(String textoBusqueda);

    public boolean insert(Funcion funcion);

    public boolean update(Funcion funcion);

    public boolean delete(Funcion funcion);

    public Funcion buscarPorId(int id);

    public ListaSimpleCircular<Funcion> selectProximasFunciones();
}
