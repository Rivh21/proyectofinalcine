/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.controlador;

import com.ues.edu.modelo.Funcion;
import com.ues.edu.modelo.dao.FuncionDAO;
import com.ues.edu.modelo.estructuras.ListaSimpleCircular;
import com.ues.edu.utilidades.CustomDateFormatter;
import com.ues.edu.vista.VistaListado;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author jorge
 */
public class ControladorSeleccionarFuncion {

    private DefaultTableModel modelo;
    private ControladorBoletos controladorBoletos; // Referencia al controlador principal
    private FuncionDAO daoFuncion;
    private VistaListado vistaLista;
    private Funcion funcionSelect;
    private ListaSimpleCircular<Funcion> listaActualMostrada;

    public ControladorSeleccionarFuncion(ControladorBoletos controladorBoletos, VistaListado vistaLista) {
        this.controladorBoletos = controladorBoletos;
        this.vistaLista = vistaLista;
        this.daoFuncion = new FuncionDAO();
        this.listaActualMostrada = daoFuncion.selectProximasFunciones();
        onClickSeleccionar();
        onClickTabla();
        keyReleasedBuscar();
        mostrar(listaActualMostrada);
        vistaLista.btnSeleccionar.setEnabled(false);

    }

    private void onClickSeleccionar() {
        this.vistaLista.btnSeleccionar.addActionListener((e) -> {
            if (funcionSelect != null) {

                // Confirmación
                int respuesta = JOptionPane.showConfirmDialog(
                        vistaLista,
                        "¿Desea seleccionar la función de: " + funcionSelect.getPeliculaTitulo() + "?",
                        "Confirmar Selección",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );

                if (respuesta == JOptionPane.YES_OPTION) {
                    controladorBoletos.cargarFuncion(funcionSelect);
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
                        int idFuncion = (Integer) idValue;

                        funcionSelect = null;
                        for (Funcion func : listaActualMostrada.toArray()) {
                            if (func.getIdFuncion() == idFuncion) {
                                funcionSelect = func;
                                break;
                            }
                        }

                        if (funcionSelect != null) {
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
                ListaSimpleCircular<Funcion> listaFinal = filtrarListaEnMemoria(textoBusqueda);
                mostrar(listaFinal);
            }
        });
    }

    public void mostrar(ListaSimpleCircular<Funcion> lista) {
        this.listaActualMostrada = lista;
        modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        String titulos[] = {"ID", "PELÍCULA", "SALA", "FECHA Y HORA", "PRECIO"};
        modelo.setColumnIdentifiers(titulos);

        ArrayList<Funcion> listaFunciones = lista.toArray();

        for (Funcion f : listaFunciones) {
            Object[] fila = {
                f.getIdFuncion(),
                f.getPeliculaTitulo(),
                f.getSalaNombre(),
                CustomDateFormatter.format(f.getFechaHoraInicio()), // Formato legible
                String.format("$ %.2f", f.getPrecioBoleto())
            };
            modelo.addRow(fila);
        }

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modelo);
        this.vistaLista.tbDatos.setRowSorter(sorter);
        this.vistaLista.tbDatos.setModel(modelo);

        // Ajustar anchos visuales
        int[] anchosFijos = {30, 200, 100, 120, 60};
        ajustarAnchoColumnas(anchosFijos);
    }

    private void ajustarAnchoColumnas(int[] anchos) {
        TableColumnModel columnModel = vistaLista.tbDatos.getColumnModel();
        for (int i = 0; i < Math.min(anchos.length, columnModel.getColumnCount()); i++) {
            columnModel.getColumn(i).setPreferredWidth(anchos[i]);
        }
    }

    private ListaSimpleCircular<Funcion> filtrarListaEnMemoria(String texto) {
        // Si no hay texto, traemos todas las proximas funciones de nuevo
        if (texto == null || texto.trim().isEmpty()) {
            return daoFuncion.selectProximasFunciones();
        }

        // Obtenemos la lista original completa para filtrar sobre ella
        ListaSimpleCircular<Funcion> listaOrigen = daoFuncion.selectProximasFunciones();
        ListaSimpleCircular<Funcion> listaFiltrada = new ListaSimpleCircular<>();
        String busqueda = texto.trim().toLowerCase();

        for (Funcion f : listaOrigen.toArray()) {
            // Filtramos por Nombre de Película o Nombre de Sala
            if (f.getPeliculaTitulo().toLowerCase().contains(busqueda)
                    || f.getSalaNombre().toLowerCase().contains(busqueda)) {
                listaFiltrada.insertar(f);
            }
        }
        return listaFiltrada;
    }
}
