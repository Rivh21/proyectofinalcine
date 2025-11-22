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

public interface ILotesInventario {

    PrioridadCola<LotesInventario> selectAll();

    boolean insert(LotesInventario lote);

    boolean update(LotesInventario lote);

    boolean delete(LotesInventario lote);

    LotesInventario buscar(int idLote);

    LotesInventario buscarPorNombreProducto(String nombre);
}

