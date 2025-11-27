/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.controlador;

/**
 *
 * @author radon
 */

import com.ues.edu.modelo.FuncionHoy;
import com.ues.edu.modelo.dao.FuncionHoyDAO;
import com.ues.edu.modelo.estructuras.ListaSimpleCircular;
import com.ues.edu.vista.MenuPrincipal; 
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class ControladorMenuPrincipal {
    
    private final MenuPrincipal vista;
    private final FuncionHoyDAO dao;
    private ListaSimpleCircular<FuncionHoy> funcionesConID; 

    public ControladorMenuPrincipal(MenuPrincipal vista) {
        this.vista = vista;
        this.dao = new FuncionHoyDAO();
        this.vista.lbTitulo.setText("CARTELERA PROGRAMACIÓN PARA HOY");
        cargarTablaFuncionesHoy();
    }

    public void cargarTablaFuncionesHoy() {
        DefaultTableModel modelo = new DefaultTableModel() {
            @Override 
            public boolean isCellEditable(int row, int column) { return false; }
        };
        
        modelo.setColumnIdentifiers(new Object[]{"Hora", "Película", "Sala", "Estado"});
        funcionesConID = dao.selectFuncionesDeHoy();
        
        ArrayList<FuncionHoy> arrayFunciones = (ArrayList<FuncionHoy>) funcionesConID.toArray();
        
        for (FuncionHoy funcion : arrayFunciones) {
            modelo.addRow(new Object[]{
                funcion.getHoraInicio(),
                funcion.getTituloPelicula(),
                funcion.getNombreSala(),
                funcion.getEstado()
            });
        }
        
        vista.tbDatos.setModel(modelo);
    }

    public int obtenerIdFuncionSeleccionada(int filaSeleccionada) {
        if (filaSeleccionada != -1 && funcionesConID != null) {
            ArrayList<FuncionHoy> arrayFunciones = (ArrayList<FuncionHoy>) funcionesConID.toArray();
            if (filaSeleccionada < arrayFunciones.size()) {
                return arrayFunciones.get(filaSeleccionada).getIdFuncion();
            }
        }
        return -1; 
    }
}

