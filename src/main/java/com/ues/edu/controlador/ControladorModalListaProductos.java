/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.controlador;
import com.ues.edu.modelo.Producto;
import com.ues.edu.modelo.dao.ProductoDao;
import com.ues.edu.modelo.estructuras.Pila;
import com.ues.edu.vista.ModalFactCon;
import com.ues.edu.vista.VistaListado;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
/**
 * 
 * @author radon
 */

public class ControladorModalListaProductos {

    private final VistaListado vista;
    private final ModalFactCon modalFact;
    private final ProductoDao dao;
    private Pila<Producto> listaProductos;

    public ControladorModalListaProductos(VistaListado vista, ModalFactCon modalFact, ProductoDao dao) {
        this.vista = vista;
        this.modalFact = modalFact;
        this.dao = dao;

        
        cargarProductos();
        onClickTabla();
        onClickSeleccionar();
    }

  
    private void onClickTabla() {
        vista.tbDatos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = vista.tbDatos.getSelectedRow();
                
                vista.btnSeleccionar.setEnabled(selectedRow != -1);

                if (e.getClickCount() == 2 && selectedRow != -1) {
                    asignarProductoSeleccionado();
                }
            }
        });
    }

    private void onClickSeleccionar() {
        vista.btnSeleccionar.addActionListener(e -> asignarProductoSeleccionado());
        vista.btnSeleccionar.setEnabled(false); 
    }

    private void cargarProductos() {
        DefaultTableModel modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        modelo.setColumnIdentifiers(new String[]{"ID", "Nombre", "Precio U."});

        listaProductos = dao.selectAll();

        if (listaProductos != null) {
            for (Producto p : listaProductos.toArray()) {
                Object[] fila = {
                    p.getIdProducto(),
                    p.getNombre(),
                    String.format("%.2f", p.getPrecioVenta())
                };
                modelo.addRow(fila);
            }
        }

        vista.tbDatos.setModel(modelo);
    }

    private void asignarProductoSeleccionado() {
        int fila = vista.tbDatos.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(vista, "Seleccione un producto", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        DefaultTableModel modelo = (DefaultTableModel) vista.tbDatos.getModel();

        String nombre = modelo.getValueAt(fila, 1).toString();
        String precio = modelo.getValueAt(fila, 2).toString();

        modalFact.lbProducto.setText("Producto: " + nombre);
        modalFact.tfPrecio.setText(precio);

        try {
            double precioDoble = Double.parseDouble(precio.replace(',', '.'));
            modalFact.tfPrecio1.setText(String.format("%.2f", precioDoble));
        } catch (NumberFormatException ignored) {
            modalFact.tfPrecio1.setText("0.00");
            JOptionPane.showMessageDialog(vista, "Error al procesar el precio.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }

        modalFact.spiCantidad.setValue(1);
        vista.dispose();
    }
}
