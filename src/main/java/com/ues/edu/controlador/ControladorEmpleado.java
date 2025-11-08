/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.controlador;

import com.ues.edu.modelo.Empleado;
import com.ues.edu.modelo.dao.EmpleadoDao;
import com.ues.edu.modelo.estructuras.ListaSimple;
import com.ues.edu.vista.Mantenimiento;
import com.ues.edu.vista.ModalEmpleado;
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
public class ControladorEmpleado {

    Mantenimiento mantto;
    DefaultTableModel modelo;
    Empleado empleadoSelect;
    EmpleadoDao daoEmpleado;
    private ListaSimple<Empleado> listaActualMostrada;

    public ControladorEmpleado(Mantenimiento mantto) {
        daoEmpleado = new EmpleadoDao();
        this.mantto = mantto;
        this.listaActualMostrada = daoEmpleado.selectAll();
        onClickTabla();
        onClickAgregar();
        onClickEditar();
        onClickEliminar();
        keyReleasedBuscar();
        mostrar(listaActualMostrada);
        this.mantto.btnEditar.setEnabled(false);
        this.mantto.btnEliminar.setEnabled(false);
    }

    private void onClickAgregar() {
        mantto.btnAgregar.addActionListener((e) -> {
            ModalEmpleado me = new ModalEmpleado(new JFrame(), true, "Agregar Empleado");
            ControladorModalEmpleado cme = new ControladorModalEmpleado(this, me);
            me.setVisible(true);
        });
    }

    private void onClickEditar() {
        mantto.btnEditar.addActionListener((e) -> {
            ModalEmpleado me = new ModalEmpleado(new JFrame(), true, "Editar Empleado");
            ControladorModalEmpleado cme = new ControladorModalEmpleado(this, me, empleadoSelect);
            me.setVisible(true);
        });
    }

    private void onClickEliminar() {
        mantto.btnEliminar.addActionListener((e) -> {
            if (empleadoSelect == null) {
                JOptionPane.showMessageDialog(null, "Seleccione un empleado para eliminar",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int op = JOptionPane.showConfirmDialog(
                    this.mantto,
                    "¿Seguro que quiere eliminar permanentemente a " + empleadoSelect.getNombre() + "?\nEsta acción no se puede deshacer.",
                    "Confirmar Eliminación",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (op == JOptionPane.YES_OPTION) {

                if (daoEmpleado.delete(empleadoSelect)) {
                    DesktopNotify.setDefaultTheme(NotifyTheme.Green);
                    DesktopNotify.showDesktopMessage("OK", "Registro Eliminado de la Base de Datos",
                            DesktopNotify.SUCCESS, 3000);

                    mostrar(daoEmpleado.selectAll());

                } else {
                    JOptionPane.showMessageDialog(null,
                            "Error: No se pudo eliminar el registro en la base de datos.",
                            "Error de Operación", JOptionPane.ERROR_MESSAGE);
                }
            }

        });
    }

    private void keyReleasedBuscar() {
        this.mantto.tfBuscar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String textoBusqueda = mantto.tfBuscar.getText().trim();
                ListaSimple<Empleado> lista;

                if (textoBusqueda.isEmpty()) {
                    lista = daoEmpleado.selectAll();
                } else {
                    lista = daoEmpleado.buscar(textoBusqueda);
                }
                if (lista.isEmpty() && !textoBusqueda.isEmpty()) {
                    mostrar(new ListaSimple<Empleado>());
                } else {
                    mostrar(lista);
                }
            }
        });
    }

    public void mostrar(ListaSimple<Empleado> lista) {
        this.listaActualMostrada = lista;
        modelo = new DefaultTableModel();
        String titulos[] = {"N", "NOMBRE", "APELLIDO", "DUI", "EMAIL", "TELÉFONO", "SALARIO"};
        modelo.setColumnIdentifiers(titulos);
        ArrayList<Empleado> listaEmpleados = lista.toArray();
        for (Empleado emp : listaEmpleados) {
            Object[] fila = {
                emp.getIdEmpleado(),
                emp.getNombre(),
                emp.getApellido(),
                emp.getDui(),
                emp.getEmail(),
                emp.getTelefono(),
                String.format("$%.2f", emp.getSalario())
            };
            modelo.addRow(fila);
        }
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modelo);
        this.mantto.tbDatos.setRowSorter(sorter);
        this.mantto.tbDatos.setModel(modelo);
        int[] anchosFijos = {30, 300, 300, 50, 100, 50, 50};

        ajustarAnchoColumnas(anchosFijos);
        mantto.tbDatos.setModel(modelo);
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
                        int idEmpleado = (Integer) idValue;
                        empleadoSelect = null;
                        for (Empleado emp : listaActualMostrada.toArray()) {
                            if (emp.getIdEmpleado() == idEmpleado) {
                                empleadoSelect = emp;
                                break;
                            }
                        }
                        boolean seleccionado = (empleadoSelect != null);
                        mantto.btnEditar.setEnabled(seleccionado);
                        mantto.btnEliminar.setEnabled(seleccionado);
                        mantto.btnAgregar.setEnabled(!seleccionado);
                    }
                }
            }
        });
    }

    private void ajustarAnchoColumnas(int[] anchos) {
        TableColumnModel columnModel = mantto.tbDatos.getColumnModel();
        for (int i = 0; i < Math.min(anchos.length, columnModel.getColumnCount()); i++) {
            columnModel.getColumn(i).setPreferredWidth(anchos[i]);
        }
    }

}
