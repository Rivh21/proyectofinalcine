/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.controlador;

import com.ues.edu.modelo.Asiento;
import com.ues.edu.modelo.Sala;
import com.ues.edu.modelo.dao.AsientoDao;
import com.ues.edu.modelo.estructuras.ListaSimple;
import com.ues.edu.vista.ModalAsientos;
import com.ues.edu.vista.VistaAsientos;
import com.ues.edu.vista.VistaListado;
import ds.desktop.notify.DesktopNotify;
import ds.desktop.notify.NotifyTheme;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author jorge
 */
public class ControladorAsiento {

    DefaultTableModel modelo;
    VistaAsientos vista;
    AsientoDao daoAsiento;

    private Sala salaParaGeneracion;
    private Asiento asientoSelect;
    private ListaSimple<Asiento> listaActualMostrada;

    public ControladorAsiento(VistaAsientos vista) {
        this.daoAsiento = new AsientoDao();
        this.vista = vista;

        onClickSeleccionarSala();
        onClickGenerar();
        onClickTabla();
        mostrar(new ListaSimple<>());
    }

    public void cargarSala(Sala sala) {
        this.salaParaGeneracion = sala;
        boolean tieneAsientos = daoAsiento.salaTieneAsientos(sala.getIdSala());

        this.vista.btnCrearAsiento.setEnabled(!tieneAsientos);

        if (this.salaParaGeneracion != null) {
            String sl = sala.getNombreSala();
            this.vista.lbSalaSelect.setText("Sala: " + sl.toUpperCase());

            this.listaActualMostrada = daoAsiento.selectBySala(sala.getIdSala());
            mostrar(this.listaActualMostrada);

        } else {
            this.vista.lbSalaSelect.setText("Ninguna sala seleccionada");
            mostrar(new ListaSimple<>());
        }
    }

    private void onClickSeleccionarSala() {
        this.vista.btnSalaSelect.addActionListener((e) -> {
            VistaListado vl = new VistaListado(new JFrame(), true, "Listado de Salas");
            ControladorListadoSalas cls = new ControladorListadoSalas(this, vl);
            vl.setVisible(true);
        });
    }

    private void onClickGenerar() {
        this.vista.btnCrearAsiento.addActionListener((e) -> {
            if (salaParaGeneracion == null) {
                DesktopNotify.setDefaultTheme(NotifyTheme.Red);
                DesktopNotify.showDesktopMessage("Advertencia", "Debe seleccionar una sala para generar asientos.", DesktopNotify.WARNING, 3000);
                return;
            }

            ModalAsientos ma = new ModalAsientos(new JFrame(), true, "Generar asientos");
            ControladorModalAsiento cma = new ControladorModalAsiento(ma, this.salaParaGeneracion);

            ma.setVisible(true);

            this.listaActualMostrada = daoAsiento.selectBySala(salaParaGeneracion.getIdSala());
            mostrar(this.listaActualMostrada);
        });
    }

    private void onClickTabla() {
        this.vista.tbDatosAsientos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getSource() == vista.tbDatosAsientos) {
                    int rowVista = vista.tbDatosAsientos.getSelectedRow();

                    if (rowVista >= 0) {
                        int rowModelo = vista.tbDatosAsientos.convertRowIndexToModel(rowVista);

                        Object idValue = vista.tbDatosAsientos.getModel().getValueAt(rowModelo, 0);
                        int idAsiento = (Integer) idValue;

                        asientoSelect = null;
                        for (Asiento asiento : listaActualMostrada.toArray()) {
                            if (asiento.getIdAsiento() == idAsiento) {
                                asientoSelect = asiento;
                                break;
                            }
                        }

                        boolean seleccionado = (asientoSelect != null);

                    }
                }
            }
        });
    }

    private void mostrar(ListaSimple<Asiento> lista) {
        this.listaActualMostrada = lista;
        modelo = new DefaultTableModel();
        String titulos[] = {"ID", "FILA", "NÚMERO DE ASIENTO"};
        modelo.setColumnIdentifiers(titulos);

        for (Asiento asiento : lista.toArray()) {
            Object[] fila = {
                asiento.getIdAsiento(),
                asiento.getFila(),
                asiento.getNumero(),};
            modelo.addRow(fila);
        }

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modelo);
        this.vista.tbDatosAsientos.setRowSorter(sorter);
        this.vista.tbDatosAsientos.setModel(modelo);

    }

}
