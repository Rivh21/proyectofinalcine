/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ues.edu.interfaces;

/**
 *
 * @author radon
 */


import com.ues.edu.modelo.Producto;
import com.ues.edu.modelo.estructuras.Pila;

public interface IProducto {

    Pila<Producto> selectAll();

    Pila<Producto> selectAllTo(String atributo, String condicion);

    Producto buscar(int id);

    Producto buscarNombre(String nombre);

    Producto buscarPorNombreExacto(String nombre);

    boolean insert(Producto obj);

    boolean update(Producto obj);

    boolean delete(Producto obj);
}


