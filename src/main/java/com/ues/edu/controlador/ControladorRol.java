/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.controlador;

import com.ues.edu.modelo.Rol;
import com.ues.edu.modelo.dao.RolDao;
import com.ues.edu.modelo.estructuras.ListaSimple;
import com.ues.edu.vista.Mantenimiento;
import com.ues.edu.vista.ModalRol;
import ds.desktop.notify.DesktopNotify;
import ds.desktop.notify.NotifyTheme;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author jorge
 */
public class ControladorRol {

    DefaultTableModel modelo;
    Mantenimiento mantto;
    Rol rolSelect;
    RolDao daoRol;
    private ListaSimple<Rol> listaActualMostrada;

    public ControladorRol(Mantenimiento mantto) {
        this.mantto = mantto;
        daoRol = new RolDao();
        this.listaActualMostrada = daoRol.selectAll();
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
            ModalRol mr = new ModalRol(new JFrame(), true, "Agregar Rol");
            ControladorModalRol cmr = new ControladorModalRol(this, mr);
            mr.setVisible(true);
        });
    }

    private void onClickEditar() {
        this.mantto.btnEditar.addActionListener((e) -> {
            ModalRol mr = new ModalRol(new JFrame(), true, "Editar Rol");
            ControladorModalRol cmr = new ControladorModalRol(this, mr, rolSelect);
            mr.setVisible(true);
        });
    }

    private void onClickEliminar() {
        mantto.btnEliminar.addActionListener((e) -> {
            if (rolSelect == null) {
                JOptionPane.showMessageDialog(null, "Seleccione un rol para eliminar",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int op = JOptionPane.showConfirmDialog(
                    this.mantto,
                    "¿Seguro que quiere eliminar permanentemente el rol " + rolSelect.getNombreRol() + "?\nEsta acción no se puede deshacer.",
                    "Confirmar Eliminación",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (op == JOptionPane.YES_OPTION) {

                if (daoRol.delete(rolSelect)) {

                    DesktopNotify.setDefaultTheme(NotifyTheme.Green);
                    DesktopNotify.showDesktopMessage("OK", "Registro Eliminado de la Base de Datos",
                            DesktopNotify.SUCCESS, 3000);

                    mostrar(daoRol.selectAll());

                } else {
                    JOptionPane.showMessageDialog(null,
                            "Error: No se pudo eliminar el registro en la base de datos.",
                            "Error de Operación", JOptionPane.ERROR_MESSAGE);
                }

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
                        int idRol = (Integer) idValue;
                        rolSelect = null;
                        for (Rol rol : listaActualMostrada.toArray()) {
                            if (rol.getIdRol() == idRol) {
                                rolSelect = rol;
                                break;
                            }
                        }
                        boolean seleccionado = (rolSelect != null);
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
                ListaSimple<Rol> lista;
                if (textoBusqueda.isEmpty()) {
                    lista = daoRol.selectAll();
                } else {
                    lista = daoRol.buscar(textoBusqueda);
                }
                if (lista.isEmpty() && !textoBusqueda.isEmpty()) {
                    mostrar(new ListaSimple<>());
                } else {
                    mostrar(lista);
                }
            }
        });
    }

    public void mostrar(ListaSimple<Rol> lista) {
        this.listaActualMostrada = lista;
        modelo = new DefaultTableModel();
        String titulos[] = {"ID", "NOMBRE", "PERMISO"};
        modelo.setColumnIdentifiers(titulos);
        ArrayList<Rol> listaRoles = lista.toArray();
        for (Rol rol : listaRoles) {
            Object[] fila = {
                rol.getIdRol(),
                rol.getNombreRol(),
                rol.getPermisos()
            };
            modelo.addRow(fila);
        }
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modelo);
        this.mantto.tbDatos.setRowSorter(sorter);
        this.mantto.tbDatos.setModel(modelo);
        int[] anchosFijos = {10, 50, 1000};
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
