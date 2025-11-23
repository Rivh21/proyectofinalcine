/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.controlador;

/**
 *
 * @author radon
 */
import com.ues.edu.modelo.Producto;
import com.ues.edu.modelo.dao.ProductoDao;
import com.ues.edu.vista.VistaListado;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.table.DefaultTableModel;

import java.util.ArrayList;

public class ControladorVistaListadoProducto {

    DefaultTableModel modelo;
    private final VistaListado vista;
    private final ProductoDao dao;
    private final ControladorModalLotesInventario controladorModal;
    
    private ArrayList<Producto> listaProductos = new ArrayList<>();
    private Producto productoSeleccionado = null;

    public ControladorVistaListadoProducto(ControladorModalLotesInventario controladorModal,
            VistaListado vista) {

        this.controladorModal = controladorModal;
        this.vista = vista;
        this.dao = new ProductoDao();

        cargarTabla();
        eventoSeleccionTabla();
        eventoSeleccionBoton();
        eventoBuscar();
    }

    private void cargarTabla() {
        modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        String titulos[] = {"ID", "NOMBRE", "PRECIO"};
        modelo.setColumnIdentifiers(titulos);

        listaProductos = dao.selectAll().toArray();
        for (Producto p : listaProductos) {
            modelo.addRow(new Object[]{
                p.getIdProducto(),
                p.getNombre(),
                "$" + p.getPrecioVenta()
            });
        }

        vista.tbDatos.setModel(modelo);
        vista.btnSeleccionar.setEnabled(false);
    }

    private void eventoSeleccionTabla() {

        vista.tbDatos.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                int fila = vista.tbDatos.getSelectedRow();
                if (fila >= 0) {
                    productoSeleccionado = listaProductos.get(fila);
                    vista.btnSeleccionar.setEnabled(true);
                }
            }
        });
    }

    private void eventoSeleccionBoton() {

        vista.btnSeleccionar.addActionListener((e) -> {
            if (productoSeleccionado != null) {
                controladorModal.setProductoSeleccionado(productoSeleccionado);
                vista.dispose();
            }
        });
    }

    private void eventoBuscar() {

        vista.tfBuscar.addKeyListener(new KeyAdapter() {

            @Override
            public void keyReleased(KeyEvent e) {
                String texto = vista.tfBuscar.getText().trim().toLowerCase();
                DefaultTableModel modelo = (DefaultTableModel) vista.tbDatos.getModel();
                modelo.setRowCount(0);
                for (Producto p : listaProductos) {
                    if (p.getNombre().toLowerCase().contains(texto)
                            || String.valueOf(p.getIdProducto()).contains(texto)) {
                        modelo.addRow(new Object[]{
                            p.getIdProducto(),
                            p.getNombre(),
                            "$" + p.getPrecioVenta()
                        });
                    }
                }
                vista.btnSeleccionar.setEnabled(false);
                productoSeleccionado = null;
            }
        });
    }
}
