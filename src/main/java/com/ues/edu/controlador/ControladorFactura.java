/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.controlador;

import com.ues.edu.modelo.Asiento;
import com.ues.edu.modelo.Boleto;
import com.ues.edu.modelo.FacturaTaquilla;
import com.ues.edu.modelo.Funcion;
import com.ues.edu.modelo.MetodoPago;
import com.ues.edu.modelo.Usuario;
import com.ues.edu.modelo.dao.BoletoDao;
import com.ues.edu.modelo.dao.MetodoPagoDao;
import com.ues.edu.modelo.estructuras.ListaSimple;
import com.ues.edu.modelo.estructuras.Pila;
import com.ues.edu.utilidades.CustomDateFormatter;
import com.ues.edu.utilidades.GeneradorID;
import com.ues.edu.vista.ModalFactura;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

/**
 *
 * @author jorge
 */
public class ControladorFactura {

    private ModalFactura vista;
    private Usuario usuario;
    private Funcion funcion;
    private ArrayList<Asiento> listaAsientos;
    private BoletoDao boletoDao;
    private MetodoPagoDao metodoPagoDao;
    private double totalOriginal;
    private double totalConDescuento;
    private boolean ventaRealizada = false;

    public ControladorFactura(ModalFactura vista, Usuario usuario, Funcion funcion, Pila<Asiento> pilaAsientos) {
        this.vista = vista;
        this.usuario = usuario;
        this.funcion = funcion;
        this.listaAsientos = pilaAsientos.toArray();
        this.boletoDao = new BoletoDao();
        this.metodoPagoDao = new MetodoPagoDao();

        cargarListaDetalle();
        configurarMetodosPago();
        configurarDescuentos();
        configurarInputEfectivo();
        configurarBotones();

        calcularTotales();
    }

    public boolean isVentaRealizada() {
        return ventaRealizada;
    }

    private void cargarListaDetalle() {
        DefaultListModel<Object> modeloLista = new DefaultListModel<>();
        modeloLista.addElement("--- DETALLES DE LA FUNCIÓN ---");
        modeloLista.addElement("Película: " + funcion.getPeliculaTitulo());
        modeloLista.addElement("Sala: " + funcion.getSalaNombre());
        modeloLista.addElement("Horario: " + CustomDateFormatter.format(funcion.getFechaHoraInicio()));
        modeloLista.addElement("Precio Unitario: $" + funcion.getPrecioBoleto());
        modeloLista.addElement(" ");
        modeloLista.addElement("--- ASIENTOS SELECCIONADOS (" + listaAsientos.size() + ") ---");

        for (Asiento a : listaAsientos) {
            String info = (a.getFila() != null) ? a.getFila() + "-" + a.getNumero() : "ID: " + a.getIdAsiento();
            modeloLista.addElement("Asiento: " + info);
        }
        vista.listDetalleBoleto.setModel(modeloLista);
    }

    private void configurarMetodosPago() {
        vista.cbMetodoDePago.removeAllItems();
        vista.cbMetodoDePago.addItem(new MetodoPago(0, "--- Seleccione ---"));

        ListaSimple<MetodoPago> listaMetodos = metodoPagoDao.selectAll();
        ArrayList<MetodoPago> arrayMetodos = listaMetodos.toArray();

        if (arrayMetodos != null) {
            for (MetodoPago mp : arrayMetodos) {
                vista.cbMetodoDePago.addItem(mp);
            }
        }
        vista.cbMetodoDePago.addActionListener((e) -> {
            MetodoPago mp = (MetodoPago) vista.cbMetodoDePago.getSelectedItem();

            boolean esEfectivo = (mp != null && mp.getnombreMetodo().equalsIgnoreCase("EFECTIVO"));

            vista.tfEfectivo.setEnabled(esEfectivo);
            vista.tfEfectivo.setText("");

            if (esEfectivo) {
                vista.lbCambio.setVisible(true);
                vista.lbCambio.setForeground(java.awt.Color.BLACK);
                vista.lbCambio.setText("Cambio: $ 0.00");
            } else {
                vista.lbCambio.setVisible(false);
                vista.lbCambio.setText("");
            }
        });
    }

    private void configurarDescuentos() {
        vista.cbDescuento.setEnabled(false);

        vista.tienePromocion.addActionListener(e -> {
            boolean estado = vista.tienePromocion.isSelected();
            vista.cbDescuento.setEnabled(estado);
            calcularTotales();
        });

        vista.cbDescuento.addActionListener(e -> calcularTotales());
    }

    private void configurarInputEfectivo() {
        vista.tfEfectivo.setEnabled(false);
        vista.lbCambio.setVisible(false);

        vista.tfEfectivo.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent evt) {
                char c = evt.getKeyChar();
                if (!Character.isDigit(c) && c != '.') {
                    evt.consume();
                    return;
                }
                if (c == '.' && vista.tfEfectivo.getText().contains(".")) {
                    evt.consume();
                }
            }

            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                String texto = vista.tfEfectivo.getText().trim();
                if (texto.isEmpty() || texto.equals(".")) {
                    vista.lbCambio.setText("Cambio: $ 0.00");
                    vista.lbCambio.setForeground(java.awt.Color.BLACK);
                    return;
                }
                try {
                    double efectivo = Double.parseDouble(texto);
                    double cambio = efectivo - totalConDescuento;

                    if (cambio < 0) {
                        vista.lbCambio.setForeground(java.awt.Color.RED);
                        vista.lbCambio.setText("Faltan: $" + String.format("%.2f", Math.abs(cambio)));
                    } else {
                        vista.lbCambio.setForeground(new java.awt.Color(0, 150, 0));
                        vista.lbCambio.setText("Cambio: $" + String.format("%.2f", cambio));
                    }
                } catch (NumberFormatException e) {
                    vista.lbCambio.setText("Error");
                }
            }
        });
    }

    private void configurarBotones() {
        vista.btnCancelar.addActionListener(e -> vista.dispose());

        vista.btnPagar.addActionListener(e -> {
            if (usuario == null || usuario.getEmpleado() == null) {
                JOptionPane.showMessageDialog(vista, "Error crítico: No hay empleado en sesión.");
                return;
            }

            if (listaAsientos.isEmpty()) {
                return;
            }

            MetodoPago mp = (MetodoPago) vista.cbMetodoDePago.getSelectedItem();
            if (mp == null || mp.getidMetodoPago() <= 0) { 
                JOptionPane.showMessageDialog(vista, "Debe seleccionar un método de pago.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (mp.getnombreMetodo().equalsIgnoreCase("EFECTIVO")) {
                try {
                    String texto = vista.tfEfectivo.getText().trim();
                    double efectivo = texto.isEmpty() ? 0.0 : Double.parseDouble(texto);

                    if (BigDecimal.valueOf(efectivo).compareTo(BigDecimal.valueOf(totalConDescuento)) < 0) {
                        JOptionPane.showMessageDialog(vista, "El efectivo es insuficiente.", "Pago Incompleto", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(vista, "Monto de efectivo inválido.", "Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }

            FacturaTaquilla factura = new FacturaTaquilla();
            String codigoFactura;
            do {
                codigoFactura = GeneradorID.generarCodigoFactura();
            } while (boletoDao.existeIdFactura(codigoFactura));

            factura.setIdFacturaTaquilla(codigoFactura);
            factura.setEmpleado(usuario.getEmpleado());

            double descuentoAplicado = totalOriginal - totalConDescuento;
            factura.setMontoTotal(BigDecimal.valueOf(totalConDescuento));
            factura.setDescuentoAplicado(BigDecimal.valueOf(descuentoAplicado));
            factura.setMetodoPago(mp);

            List<Boleto> listaBoletos = new ArrayList<>();
            BigDecimal totalBD = BigDecimal.valueOf(totalConDescuento);
            BigDecimal cantidadBD = BigDecimal.valueOf(listaAsientos.size());
            BigDecimal precioUnitario = totalBD.divide(cantidadBD, 2, RoundingMode.HALF_UP);

            for (Asiento asiento : listaAsientos) {
                Boleto b = new Boleto();
                b.setAsiento(asiento);
                b.setFuncion(funcion);
                b.setPrecioPagado(precioUnitario);
                b.setFechaVenta(java.time.LocalDateTime.now());
                listaBoletos.add(b);
            }

            boolean exito = boletoDao.generarTransaccion(factura, listaBoletos);

            if (exito) {
                this.ventaRealizada = true;
                JOptionPane.showMessageDialog(vista,
                        "¡Venta Exitosa!\n"
                        + "Código: " + codigoFactura + "\n"
                        + "Total Pagado: $" + String.format("%.2f", totalConDescuento));
                vista.dispose();
            } else {
                JOptionPane.showMessageDialog(vista, "Error al guardar la venta.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void calcularTotales() {
        totalOriginal = listaAsientos.size() * funcion.getPrecioBoleto();
        totalConDescuento = totalOriginal;

        if (vista.tienePromocion.isSelected()) {
            int index = vista.cbDescuento.getSelectedIndex();
            double porcentaje = 0;
            switch (index) {
                case 0 ->
                    porcentaje = 0.10;
                case 1 ->
                    porcentaje = 0.30;
                case 2 ->
                    porcentaje = 0.50;
                case 3 ->
                    porcentaje = 0.60;
            }
            double descuento = totalOriginal * porcentaje;
            totalConDescuento = totalOriginal - descuento;
        }
        vista.lbMontoTotal.setText("Total: "+ String.format("$%.2f", totalConDescuento));
        if (vista.tfEfectivo.isEnabled() && !vista.tfEfectivo.getText().isEmpty()) {
            for (java.awt.event.KeyListener kl : vista.tfEfectivo.getKeyListeners()) {
                kl.keyReleased(null);
            }
        }
    }
}
