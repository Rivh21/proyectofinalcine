/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.controlador;

import com.ues.edu.modelo.Boleto;
import com.ues.edu.modelo.FacturaTaquilla;
import com.ues.edu.modelo.dao.BoletoDao;
import com.ues.edu.modelo.estructuras.ListaSimple;
import com.ues.edu.utilidades.CustomDateFormatter;
import com.ues.edu.vista.ModalFactura;
import java.math.BigDecimal;
import java.util.ArrayList;
import javax.swing.DefaultListModel;

/**
 *
 * @author jorge
 */
public class ControladorDetalleFactura {

    private ModalFactura vista;
    private FacturaTaquilla factura;
    private BoletoDao boletoDao;

    public ControladorDetalleFactura(ModalFactura vista, FacturaTaquilla factura) {
        this.vista = vista;
        this.factura = factura;
        this.boletoDao = new BoletoDao();
        cargarDatosVisuales();
        bloquearVista();
        this.vista.btnCancelar.addActionListener(e -> this.vista.dispose());
        this.vista.btnCancelar.setText("CERRAR");
    }

   private void cargarDatosVisuales() {
        vista.cbMetodoDePago.removeAllItems();
        vista.cbMetodoDePago.addItem(factura.getMetodoPago().getnombreMetodo());
        vista.lbMontoTotal.setText(String.format("$ %.2f", factura.getMontoTotal()));
        if (factura.getDescuentoAplicado().compareTo(BigDecimal.ZERO) > 0) {
            vista.tienePromocion.setSelected(true);
            vista.cbDescuento.removeAllItems();
            vista.cbDescuento.addItem("Descontado: $" + factura.getDescuentoAplicado());
        } else {
            vista.tienePromocion.setSelected(false);
        }

        ListaSimple<Boleto> listaBoletos = boletoDao.getBoletosPorFactura(factura.getIdFacturaTaquilla());
        ArrayList<Boleto> arrayBoletos = listaBoletos.toArray();
        
        DefaultListModel<Object> modeloLista = new DefaultListModel<>();
        
        if (arrayBoletos != null && !arrayBoletos.isEmpty()) {
            Boleto infoBase = arrayBoletos.get(0);

            String fechaVentaStr = "---";
            if(infoBase.getFechaVenta() != null) {
                fechaVentaStr = CustomDateFormatter.format(infoBase.getFechaVenta());
            }
            
            modeloLista.addElement("--- DETALLES DE LA FUNCIÓN ---");
            modeloLista.addElement("Película: " + infoBase.getFuncion().getPeliculaTitulo());
            modeloLista.addElement("Sala: " + infoBase.getFuncion().getSalaNombre());
            modeloLista.addElement("Horario Función: " + CustomDateFormatter.format(infoBase.getFuncion().getFechaHoraInicio()));
            modeloLista.addElement("Fecha Venta: " + fechaVentaStr);
            
            modeloLista.addElement(" ");
            modeloLista.addElement("--- ASIENTOS COMPRADOS (" + arrayBoletos.size() + ") ---");
            
            for (Boleto b : arrayBoletos) {
                String asiento = b.getAsiento().getFila() + "-" + b.getAsiento().getNumero();
                modeloLista.addElement("Asiento: " + asiento + "  (Cobrado: $" + b.getPrecioPagado() + ")");
            }
        } else {
            modeloLista.addElement("No se encontraron detalles de boletos.");
        }
 
        vista.listDetalleBoleto.setModel(modeloLista);
    }

    private void bloquearVista() {
        vista.btnPagar.setVisible(false);
        vista.cbMetodoDePago.setEnabled(false);
        vista.tienePromocion.setEnabled(false);
        vista.cbDescuento.setEnabled(false);
        vista.listDetalleBoleto.setEnabled(true);
    }
}
