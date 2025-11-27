/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ues.edu.interfaces;

import com.ues.edu.modelo.MetodoPago;
import com.ues.edu.modelo.estructuras.ListaSimple;

/**
 *
 * @author radon
 */
public interface IMetodoPago {
    public ListaSimple<MetodoPago> selectAll();

    public ListaSimple<MetodoPago> selectAllTo(String atributo, String condicion);

    public ListaSimple<MetodoPago> buscar(String dato);

    public boolean insert(MetodoPago obj);

    public boolean update(MetodoPago obj);

    public boolean delete(MetodoPago obj);
}
