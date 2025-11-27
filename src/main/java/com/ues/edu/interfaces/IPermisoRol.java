/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ues.edu.interfaces;

import com.ues.edu.modelo.Permiso;
import com.ues.edu.modelo.PermisoRol;
import com.ues.edu.modelo.estructuras.ListaSimple;
import java.util.List;

/**
 *
 * @author radon
 */
public interface IPermisoRol {
     ListaSimple<PermisoRol> selectAll();

    ListaSimple<PermisoRol> selectAllTo(String atributo, String condicion);

    ListaSimple<PermisoRol> buscar(String dato);

    boolean insert(PermisoRol obj);

    boolean update(PermisoRol obj);

    boolean delete(PermisoRol obj);

    ListaSimple<PermisoRol> selectByRol(int idRol);

    boolean actualizarEstadoPermiso(int idRol, int idPermiso, boolean tienePermiso);

    ListaSimple<Permiso> selectAllPermisos();

    ListaSimple<Permiso> obtenerIdPorNombre(String nombrePermiso);

    boolean existePermisoRol(int idRol, int idPermiso);
    
    List<String> obtenerNombresPermisosPorRol(int idRol);

}
