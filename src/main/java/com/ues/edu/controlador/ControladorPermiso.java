/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.controlador;

import com.ues.edu.modelo.Permiso;
import com.ues.edu.modelo.dao.PermisoDao;
import com.ues.edu.modelo.estructuras.ListaSimple;
import com.ues.edu.vista.Mantenimiento;
import ds.desktop.notify.DesktopNotify;
import ds.desktop.notify.NotifyTheme;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JOptionPane;
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
        this.mantto.btnAgregar.addActionListener((e) -> {
            String permiso = JOptionPane.showInputDialog("Permiso");
            if (permiso == null) {
                DesktopNotify.setDefaultTheme(NotifyTheme.Red);
                DesktopNotify.showDesktopMessage("Cancelado", "Operación cancelada por el usuario",
                        DesktopNotify.WARNING, 3000);
                return;
            }
            if (permiso.trim().isEmpty()) {
                DesktopNotify.setDefaultTheme(NotifyTheme.Red);
                DesktopNotify.showDesktopMessage("Campo vacío", "Debe ingresar un permiso válido",
                        DesktopNotify.WARNING, 3000);
                return;
            }
            permisoSelect = new Permiso(permiso.toUpperCase());
            if (!existePermiso()) {
                if (daoPermiso.insert(permisoSelect)) {
                    DesktopNotify.setDefaultTheme(NotifyTheme.Green);
                    DesktopNotify.showDesktopMessage("Ok", "Registro creado correctamente",
                            DesktopNotify.SUCCESS, 3000);
                    mostrar(daoPermiso.selectAll());
                } else {
                    DesktopNotify.setDefaultTheme(NotifyTheme.Red);
                    DesktopNotify.showDesktopMessage("Error", "Error al guardar el registro",
                            DesktopNotify.ERROR, 3000);
                }
            } else {
                JOptionPane.showMessageDialog(null, "El método de pago ya existe", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void onClickEditar() {
        this.mantto.btnEditar.addActionListener((e) -> {
            String permiso = JOptionPane.showInputDialog("Editar permiso", permisoSelect.getNombrePermiso());

            if (permiso == null) {
                DesktopNotify.setDefaultTheme(NotifyTheme.Red);
                DesktopNotify.showDesktopMessage("Cancelado", "Edición cancelada por el usuario",
                        DesktopNotify.WARNING, 3000);
                return;
            }
            if (permiso.trim().isEmpty()) {
                DesktopNotify.setDefaultTheme(NotifyTheme.Red);
                DesktopNotify.showDesktopMessage("Campo vacío", "Debe ingresar un nombre válido",
                        DesktopNotify.WARNING, 3000);
                return;
            }
            permisoSelect.setNombrePermiso(permiso.toUpperCase());

            if (!existePermiso()) {
                if (daoPermiso.update(permisoSelect)) {
                    DesktopNotify.setDefaultTheme(NotifyTheme.Green);
                    DesktopNotify.showDesktopMessage("Ok", "Registro actualizado correctamente",
                            DesktopNotify.SUCCESS, 3000);
                } else {
                    DesktopNotify.setDefaultTheme(NotifyTheme.Red);
                    DesktopNotify.showDesktopMessage("Error", "Error al actualizar el registro",
                            DesktopNotify.ERROR, 3000);
                }
            } else {
                JOptionPane.showMessageDialog(null, "El método de pago ya existe", "Error", JOptionPane.ERROR_MESSAGE);
            }
            permisoSelect = null;
            mostrar(daoPermiso.selectAll());
        });
    }

    private void onClickEliminar() {
        this.mantto.btnEliminar.addActionListener((e) -> {
            int op = JOptionPane.showConfirmDialog(null, "Seguro eliminar el permiso "
                    + permisoSelect.getNombrePermiso());
            if (op == 0) {
                if (daoPermiso.delete(permisoSelect)) {
                    DesktopNotify.setDefaultTheme(NotifyTheme.Green);
                    DesktopNotify.showDesktopMessage("Ok", "Registro eliminado",
                            DesktopNotify.SUCCESS, 3000);
                }
                mostrar(daoPermiso.selectAll());
            }
        });
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
        modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
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

    private boolean existePermiso() {
        ListaSimple<Permiso> lista = daoPermiso.selectAllTo("nombre_permiso", permisoSelect.getNombrePermiso());
        return !lista.isEmpty();
    }
}
