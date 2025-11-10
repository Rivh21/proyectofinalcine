/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.vista.swing;

import com.ues.edu.modelo.GenericTableModel;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTable;

/**
 *
 * @author jorge
 */
public class TablaConCheckboxes extends JTable {

    private final GenericTableModel modelo;

    public TablaConCheckboxes() {
        String[] names = {"Columna 1", "Check"};
        Class<?>[] types = {String.class, Boolean.class};
        boolean[] editable = {false, true};
        List<Object[]> data = new ArrayList<>(); // Sin datos

        this.modelo = new GenericTableModel(names, types, editable, data);
        this.setModel(this.modelo);
    }

    public TablaConCheckboxes(
            String[] columnNames,
            Class<?>[] columnTypes,
            boolean[] columnEditable,
            List<Object[]> data
    ) {
        this.modelo = new GenericTableModel(columnNames, columnTypes, columnEditable, data);
        this.setModel(this.modelo);
    }

    public GenericTableModel getModeloPersonalizado() {
        return this.modelo;
    }

    public void agregarFila(Object[] rowData) {
        this.modelo.addRow(rowData);
    }
}
