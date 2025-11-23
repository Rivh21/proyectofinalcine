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
        cargarComboMetodosPago();
        configurarEstadoInicial();
        calcularTotales();

        // Eventos
        onClickPagar();
        onClickCancelar();
        onClickTieneDescuento();
        onClickComboDescuento();
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

    private void cargarComboMetodosPago() {
        vista.cbMetodoDePago.removeAllItems();
        ListaSimple<MetodoPago> listaMetodos = metodoPagoDao.selectAll();
        ArrayList<MetodoPago> arrayMetodos = listaMetodos.toArray();
        if (arrayMetodos != null) {
            for (MetodoPago mp : arrayMetodos) {
                vista.cbMetodoDePago.addItem(mp);
            }
        }
    }

    private void configurarEstadoInicial() {
        vista.cbDescuento.setEnabled(false);
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

        vista.lbMontoTotal.setText(String.format("$ %.2f", totalConDescuento));
    }

    private void onClickTieneDescuento() {
        vista.tienePromocion.addActionListener(e -> {
            boolean estado = vista.tienePromocion.isSelected();
            vista.cbDescuento.setEnabled(estado);
            calcularTotales();
        });
    }

    private void onClickComboDescuento() {
        vista.cbDescuento.addActionListener(e -> {
            calcularTotales();
        });
    }

    private void onClickPagar() {
        this.vista.btnPagar.addActionListener((e) -> {
            if (usuario == null || usuario.getEmpleado() == null) {
                JOptionPane.showMessageDialog(vista, "Error crítico: No hay empleado en sesión.");
                return;
            }

            // Validación extra de seguridad
            if (listaAsientos.isEmpty()) {
                return;
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

            Object item = vista.cbMetodoDePago.getSelectedItem();
            if (item instanceof MetodoPago) {
                factura.setMetodoPago((MetodoPago) item);
            }

            List<Boleto> listaBoletos = new ArrayList<>();

            // CORRECCIÓN: Redondeo seguro para evitar ArithmeticException
            BigDecimal totalBD = BigDecimal.valueOf(totalConDescuento);
            BigDecimal cantidadBD = BigDecimal.valueOf(listaAsientos.size());

            // Divide usando 2 decimales y redondeo estándar ("Half Up")
            BigDecimal precioUnitario = totalBD.divide(cantidadBD, 2, java.math.RoundingMode.HALF_UP);

            for (Asiento asiento : listaAsientos) {
                Boleto b = new Boleto();
                b.setAsiento(asiento);
                b.setFuncion(funcion);
                b.setPrecioPagado(precioUnitario);
                listaBoletos.add(b);
            }

            boolean exito = boletoDao.generarTransaccion(factura, listaBoletos);

            if (exito) {
                this.ventaRealizada = true;
                JOptionPane.showMessageDialog(vista,
                        "¡Venta Exitosa!\n"
                        + "Código: " + codigoFactura + "\n"
                        + "Total: $" + String.format("%.2f", totalConDescuento));
                vista.dispose();
            } else {
                JOptionPane.showMessageDialog(vista, "Error al guardar la venta en la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void onClickCancelar() {
        this.vista.btnCancelar.addActionListener((e) -> {
            this.vista.dispose();
        });
    }
}
