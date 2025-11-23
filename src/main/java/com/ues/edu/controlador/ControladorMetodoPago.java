/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.controlador;

import com.ues.edu.modelo.MetodoPago;
import com.ues.edu.modelo.dao.MetodoPagoDao;
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
public class ControladorMetodoPago {

    DefaultTableModel modelo;
    Mantenimiento mantto;
    MetodoPago metodoPagoSelect;
    MetodoPagoDao daoMetodoPago;
    private ListaSimple<MetodoPago> listaActualMostrada;

    public ControladorMetodoPago(Mantenimiento mantto) {
        this.mantto = mantto;
        daoMetodoPago = new MetodoPagoDao();
        this.listaActualMostrada = daoMetodoPago.selectAll();
        onClickAgregar();
        onClickEditar();
        onClickEliminar();
        keyReleasedBuscar();
        mostrar(listaActualMostrada);
        onClickTabla();
        mantto.btnEditar.setEnabled(false);
        mantto.btnEliminar.setEnabled(false);
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
                        int idMetodoPago = (Integer) idValue;
                        metodoPagoSelect = null;
                        for (MetodoPago mp : listaActualMostrada.toArray()) {
                            if (mp.getidMetodoPago() == idMetodoPago) {
                                metodoPagoSelect = mp;
                                break;
                            }
                        }
                        boolean seleccionado = (metodoPagoSelect != null);
                        mantto.btnEditar.setEnabled(seleccionado);
                        mantto.btnEliminar.setEnabled(seleccionado);
                        mantto.btnAgregar.setEnabled(!seleccionado);
                    }
                }
            }
        });
    }

    private void onClickAgregar() {
        this.mantto.btnAgregar.addActionListener((e) -> {
            String metodoPago = JOptionPane.showInputDialog("Método de pago");

            if (metodoPago == null) {
                DesktopNotify.setDefaultTheme(NotifyTheme.Red);
                DesktopNotify.showDesktopMessage("Cancelado", "Operación cancelada por el usuario",
                        DesktopNotify.WARNING, 3000);
                return;
            }

            if (metodoPago.trim().isEmpty()) {
                DesktopNotify.setDefaultTheme(NotifyTheme.Red);
                DesktopNotify.showDesktopMessage("Campo vacío", "Debe ingresar un método de pago válido",
                        DesktopNotify.WARNING, 3000);
                return;
            }

            metodoPagoSelect = new MetodoPago(metodoPago.toUpperCase());
            if (!existeMetodoPago()) {
                if (daoMetodoPago.insert(metodoPagoSelect)) {
                    DesktopNotify.setDefaultTheme(NotifyTheme.Green);
                    DesktopNotify.showDesktopMessage("Ok", "Registro creado correctamente",
                            DesktopNotify.SUCCESS, 3000);
                    mostrar(daoMetodoPago.selectAll());
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
            String metodoPago = JOptionPane.showInputDialog("Editar método de pago", metodoPagoSelect.getnombreMetodo());

            if (metodoPago == null) {
                DesktopNotify.setDefaultTheme(NotifyTheme.Red);
                DesktopNotify.showDesktopMessage("Cancelado", "Edición cancelada por el usuario",
                        DesktopNotify.WARNING, 3000);
                return;
            }

            if (metodoPago.trim().isEmpty()) {
                DesktopNotify.setDefaultTheme(NotifyTheme.Red);
                DesktopNotify.showDesktopMessage("Campo vacío", "Debe ingresar un nombre válido",
                        DesktopNotify.WARNING, 3000);
                return;
            }

            metodoPagoSelect.setnombreMetodo(metodoPago.toUpperCase());

            if (!existeMetodoPago()) {
                if (daoMetodoPago.update(metodoPagoSelect)) {
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

            metodoPagoSelect = null;
            mostrar(daoMetodoPago.selectAll());
        });
    }

    private void onClickEliminar() {
        this.mantto.btnEliminar.addActionListener((e) -> {
            int op = JOptionPane.showConfirmDialog(null, "Seguro eliminar el metodo de pago "
                    + metodoPagoSelect.getnombreMetodo());
            if (op == 0) {
                if (daoMetodoPago.delete(metodoPagoSelect)) {
                    DesktopNotify.setDefaultTheme(NotifyTheme.Green);
                    DesktopNotify.showDesktopMessage("Ok", "Registro eliminado",
                            DesktopNotify.SUCCESS, 3000);
                }
                mostrar(daoMetodoPago.selectAll());
            }
        });
    }

    private void keyReleasedBuscar() {
        this.mantto.tfBuscar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String textoBusqueda = mantto.tfBuscar.getText().trim();
                ListaSimple<MetodoPago> lista;

                if (textoBusqueda.isEmpty()) {
                    lista = daoMetodoPago.selectAll();
                } else {
                    lista = daoMetodoPago.buscar(textoBusqueda);
                }
                if (lista.isEmpty() && !textoBusqueda.isEmpty()) {
                    mostrar(new ListaSimple<>());
                } else {
                    mostrar(lista);
                }
            }
        });
    }

    public void mostrar(ListaSimple<MetodoPago> lista) {
        this.listaActualMostrada = lista;
        modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        String titulos[] = {"ID", "METODO DE PAGO"};
        modelo.setColumnIdentifiers(titulos);
        ArrayList<MetodoPago> listaMetodos = lista.toArray();
        for (MetodoPago metodoPago : listaMetodos) {
            Object[] fila = {
                metodoPago.getidMetodoPago(),
                metodoPago.getnombreMetodo()
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

    private boolean existeMetodoPago() {
        ListaSimple<MetodoPago> lista = daoMetodoPago.selectAllTo("nombre_metodo", metodoPagoSelect.getnombreMetodo());
        return !lista.isEmpty();
    }

    private void ajustarAnchoColumnas(int[] anchos) {
        TableColumnModel columnModel = mantto.tbDatos.getColumnModel();
        for (int i = 0; i < Math.min(anchos.length, columnModel.getColumnCount()); i++) {
            columnModel.getColumn(i).setPreferredWidth(anchos[i]);
        }
    }

}
