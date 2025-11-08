/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ues.edu.interfaces;

import com.ues.edu.modelo.Rol;
import com.ues.edu.modelo.estructuras.ListaSimple;

/**
 *
 * @author jorge
 */
public interface IRol {

    public ListaSimple<Rol> selectAll();

    public ListaSimple<Rol> selectAllTo(String atributo, String condicion);

    public ListaSimple<Rol> buscar(String dato);

    public boolean insert(Rol obj);

    public boolean update(Rol obj);

    public boolean delete(Rol obj);
}
