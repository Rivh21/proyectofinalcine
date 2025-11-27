/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.controlador;
import com.ues.edu.modelo.FacturaConcesion;
import com.ues.edu.modelo.MetodoPago;
import com.ues.edu.modelo.Empleado;
import com.ues.edu.modelo.Producto;
import com.ues.edu.modelo.DetalleConcesion;
import com.ues.edu.modelo.dao.FacturaConcesionDao;
import com.ues.edu.modelo.dao.ProductoDao;
import com.ues.edu.modelo.dao.LotesInventarioDao;
import com.ues.edu.modelo.dao.MetodoPagoDao;
import com.ues.edu.vista.ModalFactCon;
import com.ues.edu.vista.VistaListado;
import com.ues.edu.modelo.estructuras.ListaSimple;
import com.ues.edu.utilidades.GeneradorID;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.Frame;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
/**
 * * @author radon
 */

public class ControladorModalFactCon {

    private final ModalFactCon vista;
    private final ProductoDao productoDao;
    private final FacturaConcesionDao facturaDao;
    private final LotesInventarioDao lotesDao;
    private final MetodoPagoDao metodoPagoDao;
    private Producto productoSeleccionado;
    private final ControladorVistaFactura controladorPrincipal;
    private final int idEmpleadoActual;

    public ControladorModalFactCon(ModalFactCon vista, ControladorVistaFactura controladorPrincipal, int idEmpleadoActual) {
        this.vista = vista;
        this.productoDao = new ProductoDao();
        this.facturaDao = new FacturaConcesionDao();
        this.lotesDao = new LotesInventarioDao();
        this.metodoPagoDao = new MetodoPagoDao();
        this.controladorPrincipal = controladorPrincipal;
        this.idEmpleadoActual = idEmpleadoActual;
        this.productoSeleccionado = null;

        inicializarTabla();
        inicializarCampos();
        cargarMetodosPago();
        onClickProducto();
        onClickAgregar();
        onClickEliminar();
        onClickCancelar();
        onClickPagoCliente();
        onClickGuardar();
        Cantidad();
        MetodoPago();
    }

    private void inicializarTabla() {
        DefaultTableModel modelo = new DefaultTableModel() {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        modelo.setColumnIdentifiers(new String[]{"Producto", "Cantidad", "P. Unitario", "Subtotal"});
        vista.tbDatos.setModel(modelo);
    }

    private void inicializarCampos() {
        vista.lbProducto.setText("Producto:");
        vista.tfPrecio.setText("0.00");
        vista.tfPrecio1.setText("0.00");
        vista.spiCantidad.setValue(1);
        vista.lbTotalProducto.setText("Total: $0.00");
        vista.tfCambio.setText("0.00");
        
        vista.lbSiesEfectivo.setVisible(false);
        vista.tfPagoCliente.setVisible(false);
        vista.lbCambio.setVisible(false);
        vista.tfCambio.setVisible(false);
    }

    private void cargarMetodosPago() {
        vista.cmbMetodoPago.removeAllItems();

        MetodoPago placeholder = new MetodoPago("--- SELECCIONAR ---");
        placeholder.setidMetodoPago(0);
        vista.cmbMetodoPago.addItem(placeholder);        
        ListaSimple<MetodoPago> listaSimpleMetodos = metodoPagoDao.selectAll();
                for (MetodoPago mp : listaSimpleMetodos.toArray()) {
            vista.cmbMetodoPago.addItem(mp);
        }
        vista.cmbMetodoPago.setSelectedIndex(0);
    }


    private void onClickProducto() {
        vista.btnProducto.addActionListener(e -> {
            Frame owner = JOptionPane.getFrameForComponent(vista);
            if (owner == null) return;
            VistaListado listado = new VistaListado(owner, true, "SELECCIONAR PRODUCTO");
            listado.setLocationRelativeTo(vista);
            DefaultTableModel modelo = new DefaultTableModel() {
                @Override public boolean isCellEditable(int row, int column) { return false; }
            };
            modelo.setColumnIdentifiers(new Object[]{"ID", "Producto", "Precio", "Disponible"});

            for (Producto p : productoDao.selectAll().toArray()) {
                int disponible = lotesDao.obtenerCantidadDisponible(p.getIdProducto());
                modelo.addRow(new Object[]{p.getIdProducto(), p.getNombre(), String.format("%.2f", p.getPrecioVenta()), disponible});
            }
            listado.tbDatos.setModel(modelo);
            TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modelo);
            listado.tbDatos.setRowSorter(sorter);
            listado.tfBuscar.addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    String texto = listado.tfBuscar.getText().trim();
                    if (texto.isEmpty()) sorter.setRowFilter(null);
                    else sorter.setRowFilter(RowFilter.regexFilter("(?i)" + texto));
                }
            });

             listado.btnSeleccionar.addActionListener(ev -> {
                int fila = listado.tbDatos.getSelectedRow();
                if (fila == -1) {
                    JOptionPane.showMessageDialog(listado, "Seleccione un producto", "Advertencia", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                fila = listado.tbDatos.convertRowIndexToModel(fila);
                int idProducto = (int) listado.tbDatos.getModel().getValueAt(fila, 0);
                String nombre = (String) listado.tbDatos.getModel().getValueAt(fila, 1);
                double precio = Double.parseDouble(listado.tbDatos.getModel().getValueAt(fila, 2).toString().replace(",", "."));
                int disponible = (int) listado.tbDatos.getModel().getValueAt(fila, 3);
                if (disponible <= 0) {
                    JOptionPane.showMessageDialog(listado, "El producto **" + nombre + "** no tiene existencias suficientes.", "Producto Agotado", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                productoSeleccionado = new Producto();
                productoSeleccionado.setIdProducto(idProducto);
                productoSeleccionado.setNombre(nombre);
                productoSeleccionado.setPrecioVenta(precio);

                vista.lbProducto.setText("Producto: " + nombre);
                vista.tfPrecio.setText(String.format("%.2f", precio));
                vista.spiCantidad.setValue(1);
                try {
                    int cantidad = (int) vista.spiCantidad.getValue();
                    vista.tfPrecio1.setText(String.format("%.2f", cantidad * precio));
                } catch (NumberFormatException ex) { vista.tfPrecio1.setText("0.00"); }

                listado.dispose();
            });

            listado.setVisible(true);
        });
    }

    private void Cantidad() {
        vista.spiCantidad.addChangeListener(e -> {
            try {
                int cantidad = (int) vista.spiCantidad.getValue();
                if (cantidad < 1) { vista.spiCantidad.setValue(1); cantidad = 1; }
                double precio = Double.parseDouble(vista.tfPrecio.getText().replace(",", "."));
                vista.tfPrecio1.setText(String.format("%.2f", cantidad * precio));
            } catch (NumberFormatException ex) { vista.tfPrecio1.setText("0.00"); }
        });
    }

    private void onClickAgregar() {
        vista.btnAgregar.addActionListener(e -> {
            if (productoSeleccionado == null) {
                JOptionPane.showMessageDialog(vista, "Seleccione un producto primero", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int cantidad = (int) vista.spiCantidad.getValue();
            if (cantidad <= 0) {
                JOptionPane.showMessageDialog(vista, "La cantidad debe ser mayor a 0", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int idProducto = productoSeleccionado.getIdProducto();
            String nombreProducto = productoSeleccionado.getNombre();
            int stockTotalDB = lotesDao.obtenerCantidadDisponible(idProducto);
            int cantidadEnTabla = 0;
            DefaultTableModel modeloTabla = (DefaultTableModel) vista.tbDatos.getModel();
            for (int i = 0; i < modeloTabla.getRowCount(); i++) {
                if (modeloTabla.getValueAt(i, 0).toString().equalsIgnoreCase(nombreProducto)) {
                    cantidadEnTabla += Integer.parseInt(modeloTabla.getValueAt(i, 1).toString());
                }
            }

            if (cantidadEnTabla + cantidad > stockTotalDB) {
                JOptionPane.showMessageDialog(vista, "El producto **" + nombreProducto + "** no tiene existencias suficientes.", "Producto Agotado", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double precio = Double.parseDouble(vista.tfPrecio.getText().replace(",", "."));
            double subtotal = Double.parseDouble(vista.tfPrecio1.getText().replace(",", "."));
            modeloTabla.addRow(new Object[]{nombreProducto, cantidad, String.format("%.2f", precio), String.format("%.2f", subtotal)});
            vista.lbTotalProducto.setText(String.format("Total: $%.2f", calcularTotal()));

            MetodoPago metodoSeleccionado = (MetodoPago) vista.cmbMetodoPago.getSelectedItem();
            if (metodoSeleccionado != null && "Efectivo".equalsIgnoreCase(metodoSeleccionado.getnombreMetodo())) {
                try {
                    double total = Double.parseDouble(vista.lbTotalProducto.getText().replace("Total: $", "").replace(",", "."));
                    double pago = Double.parseDouble(vista.tfPagoCliente.getText().trim().replace(",", "."));
                    double cambio = (pago >= total) ? (pago - total) : 0.00;
                    vista.tfCambio.setText(String.format("%.2f", cambio));
                } catch (Exception ex) { vista.tfCambio.setText("0.00"); }
            }

            productoSeleccionado = null;
            vista.lbProducto.setText("Producto:");
            vista.tfPrecio.setText("0.00");
            vista.tfPrecio1.setText("0.00");
            vista.spiCantidad.setValue(1);
        });
    }
      private void onClickEliminar() {
    vista.btnEliminar.addActionListener(e -> {
        DefaultTableModel modeloTabla = (DefaultTableModel) vista.tbDatos.getModel();
        int filaSeleccionada = vista.tbDatos.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(vista, "Seleccione la fila a eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        modeloTabla.removeRow(filaSeleccionada); 
        double nuevoTotal = calcularTotal();
        vista.lbTotalProducto.setText(String.format("Total: $%.2f", nuevoTotal));
        MetodoPago metodoSeleccionado = (MetodoPago) vista.cmbMetodoPago.getSelectedItem();
        if (metodoSeleccionado != null && "Efectivo".equalsIgnoreCase(metodoSeleccionado.getnombreMetodo())) {
            try {
                double pago = Double.parseDouble(vista.tfPagoCliente.getText().trim().replace(",", "."));
                double cambio = (pago >= nuevoTotal) ? (pago - nuevoTotal) : 0.00;
                vista.tfCambio.setText(String.format("%.2f", cambio));
            } catch (Exception ex) {
                vista.tfCambio.setText("0.00");
            }
        }
    });
}

    private void onClickCancelar() {
        vista.btnCancelar.addActionListener(e -> vista.dispose());
    }
    private void MetodoPago() {
        vista.cmbMetodoPago.addActionListener(e -> {
            MetodoPago metodoPago = (MetodoPago) vista.cmbMetodoPago.getSelectedItem();
            if (metodoPago == null) return;
                        boolean esEfectivoValido = metodoPago.getidMetodoPago() > 0 && "Efectivo".equalsIgnoreCase(metodoPago.getnombreMetodo());
            
            vista.lbSiesEfectivo.setVisible(esEfectivoValido);
            vista.tfPagoCliente.setVisible(esEfectivoValido);
            vista.lbCambio.setVisible(esEfectivoValido);
            vista.tfCambio.setVisible(esEfectivoValido);
            
            if (esEfectivoValido) {
                try {
                    double total = Double.parseDouble(vista.lbTotalProducto.getText().replace("Total: $", "").replace(",", "."));
                    double pago = 0.00;
                    try { pago = Double.parseDouble(vista.tfPagoCliente.getText().trim().replace(",", ".")); }
                    catch (NumberFormatException ignored) {} 
                    double cambio = (pago >= total) ? (pago - total) : 0.00;
                    vista.tfCambio.setText(String.format("%.2f", cambio));
                } catch (Exception ex) { vista.tfCambio.setText("0.00"); }
            } else { 
                vista.tfPagoCliente.setText(""); 
                vista.tfCambio.setText("0.00"); 
            }
        });
    }

    private void onClickPagoCliente() {
        vista.tfPagoCliente.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                try {
                    double total = Double.parseDouble(vista.lbTotalProducto.getText().replace("Total: $", "").replace(",", "."));
                    String pagoText = vista.tfPagoCliente.getText().trim();
                    if (pagoText.isEmpty()) { vista.tfCambio.setText("0.00"); return; }
                    double pago = Double.parseDouble(pagoText.replace(",", "."));
                    double cambio = (pago >= total) ? (pago - total) : 0.00;
                    vista.tfCambio.setText(String.format("%.2f", cambio));
                } catch (NumberFormatException ex) { vista.tfCambio.setText("0.00"); }
            }
        });
    }

    private void onClickGuardar() {
    vista.btnGuardar.addActionListener(e -> {
        if (vista.tbDatos.getRowCount() == 0) {
            JOptionPane.showMessageDialog(vista, "Debe agregar al menos un producto.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (idEmpleadoActual <= 0) {
            JOptionPane.showMessageDialog(vista, "Error: ID de empleado inválido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        MetodoPago metodoPago = (MetodoPago) vista.cmbMetodoPago.getSelectedItem();
        if (metodoPago == null || metodoPago.getidMetodoPago() <= 0) {
            JOptionPane.showMessageDialog(vista, "Debe seleccionar un método de pago válido.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        double total = calcularTotal();

        if ("Efectivo".equalsIgnoreCase(metodoPago.getnombreMetodo())) {
            try {
                double pago = Double.parseDouble(vista.tfPagoCliente.getText().trim().replace(",", "."));
                if (pago < total) {
                    JOptionPane.showMessageDialog(vista, "El pago del cliente ($" + String.format("%.2f", pago) + ") es menor al total ($" + String.format("%.2f", total) + ").", "Advertencia", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(vista, "Ingrese un monto de pago válido para Efectivo.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        ListaSimple<DetalleConcesion> listaDetalles = new ListaSimple<>();
        DefaultTableModel modeloTabla = (DefaultTableModel) vista.tbDatos.getModel();

        for (int i = 0; i < modeloTabla.getRowCount(); i++) {
            String nombreProducto = modeloTabla.getValueAt(i, 0).toString();
            int cantidad = Integer.parseInt(modeloTabla.getValueAt(i, 1).toString());
            double precioUnit = Double.parseDouble(modeloTabla.getValueAt(i, 2).toString().replace(",", "."));
            double subtotal = Double.parseDouble(modeloTabla.getValueAt(i, 3).toString().replace(",", "."));

            Producto productoReal = productoDao.buscarNombre(nombreProducto);

            DetalleConcesion det = new DetalleConcesion();
            det.setProducto(productoReal);
            det.setCantidad(cantidad);
            det.setPrecioUnitario(precioUnit);
            det.setSubtotal(subtotal);

            listaDetalles.insertar(det);
        }

        FacturaConcesion nueva = new FacturaConcesion();
        nueva.setIdFacturaConcesion(GeneradorID.generarCodigoFactura()); 
        nueva.setMontoTotal(total);
        nueva.setMetodoPago(metodoPago);
        nueva.setEmpleado(new Empleado(idEmpleadoActual));
        nueva.setDetalleConcesion(listaDetalles);

        boolean exito = facturaDao.insert(nueva);

        if (exito) {
            if (controladorPrincipal != null) controladorPrincipal.cargarTablaFacturas();
            vista.dispose();
            JOptionPane.showMessageDialog(null, "Factura guardada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(vista, "Error al guardar la factura.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    });
}


    private double calcularTotal() {
        double total = 0;
        DefaultTableModel modelo = (DefaultTableModel) vista.tbDatos.getModel();
        for (int i = 0; i < modelo.getRowCount(); i++) {
            try { total += Double.parseDouble(modelo.getValueAt(i, 3).toString().replace(",", ".")); }
            catch (NumberFormatException ignored) { }
        }
        return total;
    }

    
}