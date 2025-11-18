/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ues.edu.interfaces;

import com.ues.edu.modelo.Permiso;
import com.ues.edu.modelo.estructuras.ListaSimple;

/**
 *
 * @author jorge
 */
public interface IPermiso {

    public ListaSimple<Permiso> selectAll();

    public ListaSimple<Permiso> selectAllTo(String atributo, String condicion);

    public ListaSimple<Permiso> buscar(String dato);

    public boolean insert(Permiso obj);

    public boolean update(Permiso obj);

    public boolean delete(Permiso obj);
    
    ListaSimple<Permiso> selectAllPermisos();

    ListaSimple<Permiso> obtenerIdPorNombre(String nombrePermiso);
}
