/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.controlador;
import com.ues.edu.vista.ModalFacturaInfo;
import com.ues.edu.modelo.FacturaConcesion;
import com.ues.edu.modelo.DetalleConcesion;
import javax.swing.DefaultListModel;
import java.text.DecimalFormat;
import java.util.ArrayList;
/**
 * 
 * @author radon
 */

public class ControladorModalFactura {

    private final ModalFacturaInfo vista;
    private final FacturaConcesion factura;
    private final DecimalFormat df = new DecimalFormat("0.00");

    public ControladorModalFactura(ModalFacturaInfo vista, FacturaConcesion factura) {
        this.vista = vista;
        this.factura = factura;

        cargarDatosFactura();
        bloquearCampos();
        onClickCancelar();
    }

  
    private void onClickCancelar() {
        vista.btnCancelar.addActionListener(e -> vista.dispose());
    }

 
    private void cargarDatosFactura() {
        cargarDetallesFactura();
        cargarMetodoPago();
        cargarTotal();
    }

    private void cargarDetallesFactura() {
        DefaultListModel<Object> modelo = new DefaultListModel<>();

     
        var listaDetalles = factura.getDetalleConcesion();
        ArrayList<DetalleConcesion> detalles = (listaDetalles != null) ? listaDetalles.toArray() : new ArrayList<>();

        int cantidad = detalles.size();

        
        modelo.addElement("--- DETALLES DE LA CONCESIÓN (" + cantidad + " productos) ---");

        if (cantidad == 0) {
            modelo.addElement("No hay productos registrados.");
        } else {
            modelo.addElement("-----------------------------------------------");

            for (DetalleConcesion d : detalles) {
                String nombre = (d.getProducto() != null) ? d.getProducto().getNombre() : "Producto desconocido";

                modelo.addElement("Producto: " + nombre);
                modelo.addElement("Cantidad: x" + d.getCantidad());
                modelo.addElement("Precio Unitario: $" + df.format(d.getPrecioUnitario()));
                modelo.addElement("Subtotal: $" + df.format(d.getSubtotal()));
                modelo.addElement("-----------------------------------------------");
            }
        }

        vista.listDetalleConcesion.setModel(modelo);
    }

    private void cargarMetodoPago() {
        vista.cbMetodoDePago.removeAllItems();
        String metodo = factura.getMetodoPago() != null ? factura.getMetodoPago().getnombreMetodo() : "N/A";
        vista.cbMetodoDePago.addItem(metodo);
    }

    private void cargarTotal() {
        double total = factura != null ? factura.getMontoTotal() : 0.0;
        vista.lbMontoTotal.setText("TOTAL: $" + df.format(total));
    }

    private void bloquearCampos() {
        vista.cbMetodoDePago.setEnabled(false);
        vista.btnCancelar.setText("CERRAR");
    }
}
