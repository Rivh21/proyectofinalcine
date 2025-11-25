/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ues.edu.interfaces;

/**
 *
 * @author radon
 */


import com.ues.edu.modelo.FacturaConcesion;
import com.ues.edu.modelo.estructuras.ListaSimple;

public interface IFacturaConcesion {

    ListaSimple<FacturaConcesion> selectAll();

    FacturaConcesion buscarPorId(int id);

    boolean insert(FacturaConcesion factura);

    boolean update(FacturaConcesion factura);

    boolean delete(FacturaConcesion factura);
}
