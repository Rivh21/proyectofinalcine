/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.controlador;

import com.ues.edu.modelo.Sala;
import com.ues.edu.modelo.dao.SalaDAO;
import com.ues.edu.modelo.estructuras.ListaSimpleCircular;
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
public class ControladorListadoSalas {

    DefaultTableModel modelo;
    ControladorAsiento ca;
    SalaDAO daoSala;
    VistaListado vistaLista;
    private Sala salaSelect;
    private ListaSimpleCircular<Sala> listaActualMostrada;

    public ControladorListadoSalas(ControladorAsiento ca, VistaListado vistaLista) {
        this.ca = ca;
        daoSala = new SalaDAO();
        this.vistaLista = vistaLista;
        this.listaActualMostrada = daoSala.selectAll();
        onClickSeleccionar();
        onClickTabla();
        keyReleasedBuscar();
        mostrar(listaActualMostrada);
        vistaLista.btnSeleccionar.setEnabled(false);
    }

    private void onClickSeleccionar() {
        this.vistaLista.btnSeleccionar.addActionListener((e) -> {
            if (salaSelect != null) {
                String nombreSala = salaSelect.getNombreSala();

                int respuesta = JOptionPane.showConfirmDialog(
                        vistaLista,
                        "Una vez seleccionada, esta sala no podrá ser modificada.\n¿Desea seleccionar  " + nombreSala.toUpperCase(),
                        "Confirmar Selección de Sala",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );
                if (respuesta == JOptionPane.YES_OPTION) {
                    this.ca.cargarSala(salaSelect);

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
                        int idSala = (Integer) idValue;

                        salaSelect = null;
                        for (Sala sala : listaActualMostrada.toArray()) {
                            if (sala.getIdSala() == idSala) {
                                salaSelect = sala;
                                break;
                            }
                        }
                        if (salaSelect != null) {
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
                ListaSimpleCircular<Sala> listaFinal = filtrarListaEnMemoria(textoBusqueda);
                mostrar(listaFinal);
            }
        });
    }

    private void mostrar(ListaSimpleCircular<Sala> lista) {
        this.listaActualMostrada = lista;
        modelo = new DefaultTableModel();
        String titulos[] = {"ID", "NOMBRE SALA"};
        modelo.setColumnIdentifiers(titulos);
        ArrayList<Sala> listaSalas = lista.toArray();
        for (Sala sala : listaSalas) {
            Object[] fila = {
                sala.getIdSala(),
                sala.getNombreSala(),};
            modelo.addRow(fila);
        }
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modelo);
        this.vistaLista.tbDatos.setRowSorter(sorter);
        this.vistaLista.tbDatos.setModel(modelo);
        int[] anchosFijos = {50, 1500};

        ajustarAnchoColumnas(anchosFijos);
        vistaLista.tbDatos.setModel(modelo);
    }

    private void ajustarAnchoColumnas(int[] anchos) {
        TableColumnModel columnModel = vistaLista.tbDatos.getColumnModel();
        for (int i = 0; i < Math.min(anchos.length, columnModel.getColumnCount()); i++) {
            columnModel.getColumn(i).setPreferredWidth(anchos[i]);
        }
    }

    private ListaSimpleCircular<Sala> filtrarListaEnMemoria(String texto) {
        ListaSimpleCircular<Sala> listaOrigen = daoSala.selectAll();
        String textoLimpio = texto.trim();
        if (textoLimpio.isEmpty()) {
            return listaOrigen;
        }

        ListaSimpleCircular<Sala> listaFiltrada = new ListaSimpleCircular<>();
        String busqueda = textoLimpio.toLowerCase();

        for (Sala sala : listaOrigen.toArray()) {
            if (sala.getNombreSala().toLowerCase().contains(busqueda)) {
                listaFiltrada.insertar(sala);
            }
        }
        return listaFiltrada;
    }

}
