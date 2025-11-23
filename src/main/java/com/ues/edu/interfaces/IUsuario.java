/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ues.edu.interfaces;

import com.ues.edu.modelo.Usuario;
import com.ues.edu.modelo.estructuras.ListaSimple;

/**
 *
 * @author jorge
 */
public interface IUsuario {

    ListaSimple<Usuario> selectAll();

    ListaSimple<Usuario> selectAllTo(String atributo, String condicion);

    ListaSimple<Usuario> buscar(String dato);

    public boolean insert(Usuario obj);

    public boolean update(Usuario obj);

    public boolean delete(Usuario obj);

    public Usuario login(String usuario, String claveEncriptada);
    
    public boolean existeNombreUsuario(String nombreUsuario);
}
