/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ues.edu.interfaces;

/**
 *
 * @author radon
 */


import com.ues.edu.modelo.DetalleConcesion;
import com.ues.edu.modelo.estructuras.Pila;

public interface IDetalleConcesion {

    Pila<DetalleConcesion> selectAll();

    Pila<DetalleConcesion> selectAllTo(String atributo, String condicion);

    Pila<DetalleConcesion> selectByFactura(int idFactura);

    boolean insert(DetalleConcesion obj);

    boolean delete(DetalleConcesion obj);
}
