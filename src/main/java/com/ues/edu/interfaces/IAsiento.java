/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ues.edu.interfaces;

import com.ues.edu.modelo.Asiento;
import com.ues.edu.modelo.estructuras.ListaSimple;

/**
 *
 * @author jorge
 */
public interface IAsiento {

    public ListaSimple<Asiento> selectAll();
    
    public ListaSimple<Asiento> selectBySala(int idSala);

    public boolean generate(int idSala, char filaInicio, int numFilas, int asientosPorFila);

    public boolean update(Asiento obj);

    public boolean delete(Asiento obj);
}
