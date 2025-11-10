/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.modelo;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author jorge
 */
public class GenericTableModel extends AbstractTableModel {

    private final List<Object[]> data;
    private final String[] columnNames;
    private final Class<?>[] columnTypes;
    private final boolean[] columnEditable;

    public GenericTableModel(
            String[] columnNames,
            Class<?>[] columnTypes,
            boolean[] columnEditable,
            List<Object[]> initialData
    ) {
        this.columnNames = columnNames;
        this.columnTypes = columnTypes;
        this.columnEditable = columnEditable;
        this.data = new ArrayList<>(initialData);

        if (columnNames.length != columnTypes.length || columnNames.length != columnEditable.length) {
            throw new IllegalArgumentException("La longitud de los arrays de configuración de columnas debe ser la misma.");
        }
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return data.get(rowIndex)[columnIndex];
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return columnTypes[columnIndex];
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnEditable[columnIndex];
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        data.get(rowIndex)[columnIndex] = aValue;
        fireTableCellUpdated(rowIndex, columnIndex);
    }

    public void addRow(Object[] rowData) {
        if (rowData.length != columnNames.length) {
            throw new IllegalArgumentException("El número de elementos de la fila no coincide con el número de columnas.");
        }
        data.add(rowData);
        fireTableRowsInserted(data.size() - 1, data.size() - 1);
    }
}
