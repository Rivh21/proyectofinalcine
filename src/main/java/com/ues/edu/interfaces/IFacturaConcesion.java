/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ues.edu.interfaces;
import com.ues.edu.modelo.FacturaConcesion;
import com.ues.edu.modelo.estructuras.ListaSimple;

/**
 *
 * @author radon
 */
public interface IFacturaConcesion {

    ListaSimple<FacturaConcesion> selectAll();
    ListaSimple<FacturaConcesion> buscar(String texto);
    FacturaConcesion buscarPorId(int id);
    boolean insert(FacturaConcesion factura);
    boolean update(FacturaConcesion factura);
    boolean delete(FacturaConcesion factura);
    boolean anularFacturaConcesion(int idFactura);
}
