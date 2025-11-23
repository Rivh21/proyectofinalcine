/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ues.edu.interfaces;

import com.ues.edu.modelo.Boleto;
import com.ues.edu.modelo.FacturaTaquilla;
import com.ues.edu.modelo.estructuras.ListaSimple;
import java.util.HashSet;
import java.util.List;

/**
 *
 * @author jorge
 */
public interface IBoleto {

    public HashSet<Integer> getAsientosOcupados(int idFuncion);

    public boolean generarTransaccion(FacturaTaquilla factura, List<Boleto> boletosVenta);
    
    public ListaSimple<Boleto> getBoletosPorFactura(String idFactura);

    public ListaSimple<Boleto> selectAll();

    public ListaSimple<Boleto> selectAllTo(String atributo, String condicion);

    public boolean update(Boleto obj);

    public boolean delete(Boleto obj);
    
    public boolean existeIdFactura(String id);
}
