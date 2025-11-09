/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ues.edu.interfaces;

import com.ues.edu.modelo.PermisoRol;
import com.ues.edu.modelo.estructuras.ListaSimple;

/**
 *
 * @author jorge
 */
public interface IPermisoRol {

    public ListaSimple<PermisoRol> selectAll();

    public ListaSimple<PermisoRol> selectAllTo(String atributo, String condicion);

    public ListaSimple<PermisoRol> buscar(String dato);

    public boolean insert(PermisoRol obj);

    public boolean update(PermisoRol obj);

    public boolean delete(PermisoRol obj);
}
