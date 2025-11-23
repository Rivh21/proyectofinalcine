/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ues.edu.interfaces;

import com.ues.edu.modelo.FacturaTaquilla;
import com.ues.edu.modelo.estructuras.ListaSimple;

/**
 *
 * @author jorge
 */
public interface IFacturaTaquilla {

    public ListaSimple<FacturaTaquilla> selectAll();

    public ListaSimple<FacturaTaquilla> selectAllTo(String atributo, String condicion);

    public ListaSimple<FacturaTaquilla> buscar(String dato);

    public boolean update(FacturaTaquilla obj);

    public boolean delete(FacturaTaquilla obj);
}
