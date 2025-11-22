/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ues.edu.interfaces;

/**
 *
 * @author radon
 */

import com.ues.edu.modelo.LotesInventario;
import com.ues.edu.modelo.estructuras.PrioridadCola;

public interface I_LoteInventario {

    PrioridadCola<LotesInventario> selectAll();
    PrioridadCola<LotesInventario> selectAllTo(String atributo, String condicion);

    LotesInventario buscar(int id);

    boolean insert(LotesInventario obj);
    boolean update(LotesInventario obj);
    boolean delete(LotesInventario obj);

    LotesInventario buscarPorNombreExacto(String nombre);
}

