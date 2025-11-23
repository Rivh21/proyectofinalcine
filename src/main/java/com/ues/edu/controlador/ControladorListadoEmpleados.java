/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.controlador;

import com.ues.edu.modelo.Empleado;
import com.ues.edu.modelo.dao.EmpleadoDao;
import com.ues.edu.modelo.estructuras.ListaSimple;
import com.ues.edu.vista.VistaListado;
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
public class ControladorListadoEmpleados {

    DefaultTableModel modelo;
    ControladorModalUsuario cmu;
    EmpleadoDao daoEmpleado;
    VistaListado vistaLista;
    private Empleado empleadoSelect;
    private ListaSimple<Empleado> listaActualMostrada;

    public ControladorListadoEmpleados(ControladorModalUsuario cmu, VistaListado vistaLista) {
        this.cmu = cmu;
        daoEmpleado = new EmpleadoDao();
        this.vistaLista = vistaLista;
        this.listaActualMostrada = daoEmpleado.selectEmpleadosSinUsuario();
        onClickSeleccionar();
        onClickTabla();
        keyReleasedBuscar();
        mostrar(listaActualMostrada);
        vistaLista.btnSeleccionar.setEnabled(false);
    }

    private void onClickSeleccionar() {
        this.vistaLista.btnSeleccionar.addActionListener((e) -> {
            if (empleadoSelect != null) {
                String nombreCompleto = empleadoSelect.getNombre() + " " + empleadoSelect.getApellido();

                int respuesta = JOptionPane.showConfirmDialog(
                        vistaLista,
                        "Una vez asignado, este empleado no podrá ser modificado.\n¿Desea asignar a " + nombreCompleto.toUpperCase() + " al nuevo usuario?",
                        "Confirmar Asignación de Empleado",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );

                if (respuesta == JOptionPane.YES_OPTION) {
                    this.cmu.cargarEmpleado(empleadoSelect);

                    this.vistaLista.dispose();
                }
            }
            this.vistaLista.dispose();
        });
    }

    private void onClickTabla() {
        this.vistaLista.tbDatos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getSource() == vistaLista.tbDatos) {
                    int rowVista = vistaLista.tbDatos.getSelectedRow();
                    if (rowVista >= 0) {
                        int rowModelo = vistaLista.tbDatos.convertRowIndexToModel(rowVista);

                        Object idValue = vistaLista.tbDatos.getModel().getValueAt(rowModelo, 0);
                        int idEmpleado = (Integer) idValue;

                        empleadoSelect = null;
                        for (Empleado emp : listaActualMostrada.toArray()) {
                            if (emp.getIdEmpleado() == idEmpleado) {
                                empleadoSelect = emp;
                                break;
                            }
                        }
                        if (empleadoSelect != null) {
                            vistaLista.btnSeleccionar.setEnabled(true);
                        }
                    }
                }
            }
        });
    }

    private void keyReleasedBuscar() {
        this.vistaLista.tfBuscar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String textoBusqueda = vistaLista.tfBuscar.getText().trim();
                ListaSimple<Empleado> listaFinal = filtrarListaEnMemoria(textoBusqueda);
                mostrar(listaFinal);
            }
        });
    }

    public void mostrar(ListaSimple<Empleado> lista) {
        this.listaActualMostrada = lista;
        modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
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
        this.vistaLista.tbDatos.setRowSorter(sorter);
        this.vistaLista.tbDatos.setModel(modelo);
        int[] anchosFijos = {30, 200, 200, 50, 100, 50, 50};

        ajustarAnchoColumnas(anchosFijos);
        vistaLista.tbDatos.setModel(modelo);
    }

    private void ajustarAnchoColumnas(int[] anchos) {
        TableColumnModel columnModel = vistaLista.tbDatos.getColumnModel();
        for (int i = 0; i < Math.min(anchos.length, columnModel.getColumnCount()); i++) {
            columnModel.getColumn(i).setPreferredWidth(anchos[i]);
        }
    }

    private ListaSimple<Empleado> filtrarListaEnMemoria(String texto) {
        ListaSimple<Empleado> listaOrigen = daoEmpleado.selectEmpleadosSinUsuario();
        String textoLimpio = texto.trim();
        if (textoLimpio.isEmpty()) {
            return listaOrigen;
        }

        ListaSimple<Empleado> listaFiltrada = new ListaSimple<>();
        String busqueda = textoLimpio.toLowerCase();

        for (Empleado emp : listaOrigen.toArray()) {
            if (emp.getNombre().toLowerCase().contains(busqueda)
                    || emp.getApellido().toLowerCase().contains(busqueda)) {

                listaFiltrada.insertar(emp);
            }
        }
        return listaFiltrada;
    }
}
