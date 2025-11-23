/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.controlador;

import com.ues.edu.modelo.FacturaTaquilla;
import com.ues.edu.modelo.dao.FacturaTaquillaDao;
import com.ues.edu.modelo.estructuras.ListaSimple;
import com.ues.edu.vista.Mantenimiento;
import com.ues.edu.vista.ModalFactura;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author jorge
 */
public class ControladorLecturaFactura {

    DefaultTableModel modelo;
    Mantenimiento mantto;
    FacturaTaquilla facturaSelect;
    FacturaTaquillaDao daoFactura;
    private ListaSimple<FacturaTaquilla> listaActualMostrada;

    public ControladorLecturaFactura(Mantenimiento mantto) {
        this.mantto = mantto;
        this.daoFactura = new FacturaTaquillaDao();
        this.listaActualMostrada = daoFactura.selectAll();

        this.mantto.btnAgregar.setText("DETALLES");
        this.mantto.btnAgregar.setEnabled(false);
        this.mantto.btnEditar.setVisible(false);
        this.mantto.btnEliminar.setVisible(false);

        configurarSeleccion();
        forzarBoton();
        onClickDetalles();
        keyReleasedBuscar();
        mostrar(listaActualMostrada);
    }

    private void configurarSeleccion() {
        this.mantto.tbDatos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int rowVista = mantto.tbDatos.getSelectedRow();

                if (rowVista != -1) {
                    int rowModelo = mantto.tbDatos.convertRowIndexToModel(rowVista);
                    String idFacturaTaquilla = (String) mantto.tbDatos.getModel().getValueAt(rowModelo, 0);

                    facturaSelect = null;
                    if (listaActualMostrada != null) {
                        for (Object obj : listaActualMostrada.toArray()) {
                            FacturaTaquilla fc = (FacturaTaquilla) obj;
                            if (fc.getIdFacturaTaquilla().equals(idFacturaTaquilla)) {
                                facturaSelect = fc;
                                break;
                            }
                        }
                    }
                    mantto.btnAgregar.setEnabled(true);
                } else {
                    facturaSelect = null;
                    mantto.btnAgregar.setEnabled(false);
                }
            }
        });
    }

    private void onClickDetalles() {
        this.mantto.btnAgregar.addActionListener(e -> {
            if (facturaSelect != null) {
                ModalFactura mf = new ModalFactura(new JFrame(), true, this.facturaSelect.getIdFacturaTaquilla());
                ControladorDetalleFactura cdf = new ControladorDetalleFactura(mf, facturaSelect);
                mf.setVisible(true);
                this.mantto.tbDatos.clearSelection();
            }
        });
    }

    private void keyReleasedBuscar() {
        this.mantto.tfBuscar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String textoBusqueda = mantto.tfBuscar.getText().trim();
                ListaSimple<FacturaTaquilla> lista;
                if (textoBusqueda.isEmpty()) {
                    lista = daoFactura.selectAll();
                } else {
                    lista = daoFactura.buscar(textoBusqueda);
                }
                mostrar(lista);
            }
        });
    }

    private void mostrar(ListaSimple<FacturaTaquilla> lista) {
        this.listaActualMostrada = lista;
        modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        String titulos[] = {"ID", "DATOS VENDEDOR", "MÉTODO DE PAGO", "MONTO TOTAL", "DESCUENTO"};
        modelo.setColumnIdentifiers(titulos);

        ArrayList<FacturaTaquilla> facturas = lista.toArray();
        if (facturas != null) {
            for (FacturaTaquilla fc : facturas) {
                Object[] fila = {
                    fc.getIdFacturaTaquilla(),
                    // Usamos los datos cargados por el JOIN del DAO corregido
                    fc.getEmpleado().getNombre() + " " + fc.getEmpleado().getApellido(),
                    fc.getMetodoPago().getnombreMetodo(),
                    String.format("$%.2f", fc.getMontoTotal()),
                    String.format("$%.2f", fc.getDescuentoAplicado())
                };
                modelo.addRow(fila);
            }
        }

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modelo);
        this.mantto.tbDatos.setRowSorter(sorter);
        this.mantto.tbDatos.setModel(modelo);

        int[] anchosFijos = {80, 250, 100, 80, 80};
        ajustarAnchoColumnas(anchosFijos);
    }

    private void ajustarAnchoColumnas(int[] anchos) {
        TableColumnModel columnModel = mantto.tbDatos.getColumnModel();
        for (int i = 0; i < Math.min(anchos.length, columnModel.getColumnCount()); i++) {
            columnModel.getColumn(i).setPreferredWidth(anchos[i]);
        }
    }

    private void forzarBoton() {
        this.mantto.fondo.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mantto.btnAgregar.setEnabled(false);
            }
        });
    }
}
