/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.controlador;

import com.ues.edu.modelo.Permiso;
import com.ues.edu.modelo.dao.PermisoDao;
import com.ues.edu.modelo.estructuras.ListaSimple;
import com.ues.edu.vista.Mantenimiento;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author jorge
 */
public class ControladorPermiso {

    DefaultTableModel modelo;
    Mantenimiento mantto;
    Permiso permisoSelect;
    PermisoDao daoPermiso;
    private ListaSimple<Permiso> listaActualMostrada;

    public ControladorPermiso(Mantenimiento mantto) {
        this.mantto = mantto;
        daoPermiso = new PermisoDao();
        this.listaActualMostrada = daoPermiso.selectAll();
        onClickAgregar();
        onClickEditar();
        onClickEliminar();
        onClickTabla();
        keyReleasedBuscar();
        mostrar(listaActualMostrada);
        this.mantto.btnEditar.setEnabled(false);
        this.mantto.btnEliminar.setEnabled(false);
    }

    private void onClickAgregar() {

    }

    private void onClickEditar() {

    }

    private void onClickEliminar() {

    }

    private void onClickTabla() {
        this.mantto.tbDatos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getSource() == mantto.tbDatos) {
                    int rowVista = mantto.tbDatos.getSelectedRow();
                    if (rowVista >= 0) {
                        int rowModelo = mantto.tbDatos.convertRowIndexToModel(rowVista);
                        Object idValue = mantto.tbDatos.getModel().getValueAt(rowModelo, 0);
                        int idPermiso = (Integer) idValue;
                        permisoSelect = null;
                        for (Permiso permiso : listaActualMostrada.toArray()) {
                            if (permiso.getIdPermiso() == idPermiso) {
                                permisoSelect = permiso;
                                break;
                            }
                        }
                        boolean seleccionado = (permisoSelect != null);
                        mantto.btnEditar.setEnabled(seleccionado);
                        mantto.btnEliminar.setEnabled(seleccionado);
                        mantto.btnAgregar.setEnabled(!seleccionado);
                    }
                }
            }
        });
    }

    private void keyReleasedBuscar() {
        this.mantto.tfBuscar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String textoBusqueda = mantto.tfBuscar.getText().trim();
                ListaSimple<Permiso> lista;
                if (textoBusqueda.isEmpty()) {
                    lista = daoPermiso.selectAll();
                } else {
                    lista = daoPermiso.buscar(textoBusqueda);
                }
                if (lista.isEmpty() && !textoBusqueda.isEmpty()) {
                    mostrar(new ListaSimple<>());
                } else {
                    mostrar(lista);
                }
            }
        });
    }

    public void mostrar(ListaSimple<Permiso> lista) {
        this.listaActualMostrada = lista;
        modelo = new DefaultTableModel();
        String titulos[] = {"ID", "NOMBRE"};
        modelo.setColumnIdentifiers(titulos);
        ArrayList<Permiso> listaPermisos = lista.toArray();
        for (Permiso permiso : listaPermisos) {
            Object[] fila = {
                permiso.getIdPermiso(),
                permiso.getNombrePermiso()
            };
            modelo.addRow(fila);
        }
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modelo);
        this.mantto.tbDatos.setRowSorter(sorter);
        this.mantto.tbDatos.setModel(modelo);
        int[] anchosFijos = {10, 1500};
        ajustarAnchoColumnas(anchosFijos);
        mantto.tbDatos.setModel(modelo);
    }

    private void ajustarAnchoColumnas(int[] anchos) {
        TableColumnModel columnModel = mantto.tbDatos.getColumnModel();
        for (int i = 0; i < Math.min(anchos.length, columnModel.getColumnCount()); i++) {
            columnModel.getColumn(i).setPreferredWidth(anchos[i]);
        }
    }
}
