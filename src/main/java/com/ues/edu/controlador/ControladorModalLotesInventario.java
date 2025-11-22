package com.ues.edu.controlador;

import com.ues.edu.modelo.LotesInventario;
import com.ues.edu.modelo.Producto;
import com.ues.edu.modelo.dao.LotesInventarioDao;
import com.ues.edu.vista.ModalLotesInventario;
import com.ues.edu.vista.VistaListado;

import ds.desktop.notify.DesktopNotify;
import ds.desktop.notify.NotifyTheme;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class ControladorModalLotesInventario {

    private final ModalLotesInventario vista;
    private final LotesInventarioDao dao;
    private final ControladorLotesInventario p;
    private LotesInventario loteEditar = null;
    private Producto productoSeleccionado = null;

    public ControladorModalLotesInventario(ControladorLotesInventario p, ModalLotesInventario vista) {
        this.p = p;
        this.vista = vista;
        this.dao = new LotesInventarioDao();
        onClickGuardar();
        onClickProductos();
    }

    public ControladorModalLotesInventario(ControladorLotesInventario p, ModalLotesInventario vista, LotesInventario loteEditar) {
        this.p = p;
        this.vista = vista;
        this.dao = new LotesInventarioDao();
        this.loteEditar = loteEditar;
        onClickGuardar();
        onClickProductos();
        cargarDatosEditar();
    }

    private void cargarDatosEditar() {
        productoSeleccionado = new Producto();
        productoSeleccionado.setIdProducto(loteEditar.getIdProducto());
        productoSeleccionado.setNombre(loteEditar.getProductoNombre());
        vista.lbPeliculas.setText("Producto: " + loteEditar.getProductoNombre());
        vista.tfCantidadDis.setText(String.valueOf(loteEditar.getCantidadDisponible()));
        vista.FechaCaducida.setDate(
                Date.from(loteEditar.getFechaCaducidad().atStartOfDay(ZoneId.systemDefault()).toInstant())
        );
        vista.fechaRecepcion.setDate(
                Date.from(loteEditar.getFechaRecepcion().atStartOfDay(ZoneId.systemDefault()).toInstant())
        );
    }

    private void onClickGuardar() {
        vista.btnGuardar.addActionListener(e -> {
            if (!validarCampos()) return;
            if (loteEditar == null) {
                agregarLote();
            } else {
                actualizarLote();
            }
        });
    }

    private void onClickProductos() {
        vista.btnProductos.addActionListener(e -> abrirVistaListado());
    }

    private void abrirVistaListado() {
        VistaListado v = new VistaListado(new JFrame(), true, "Listado de Productos");
        new ControladorVistaListadoProducto(this, v);
        v.setVisible(true);
    }

    public void setProductoSeleccionado(Producto p) {
        this.productoSeleccionado = p;
        vista.lbPeliculas.setText("Producto: " + p.getNombre());
    }

    private boolean validarCampos() {
        if (productoSeleccionado == null) {
            JOptionPane.showMessageDialog(null, "Debe seleccionar un producto", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (vista.tfCantidadDis.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ingrese la cantidad");
            return false;
        }
        if (obtenerFecha(vista.FechaCaducida.getDate()) == null ||
            obtenerFecha(vista.fechaRecepcion.getDate()) == null) {
            JOptionPane.showMessageDialog(null, "Debe seleccionar las fechas");
            return false;
        }
        return true;
    }

    private void agregarLote() {
        LotesInventario nuevo = new LotesInventario();
        nuevo.setIdProducto(productoSeleccionado.getIdProducto());
        nuevo.setCantidadDisponible(Integer.parseInt(vista.tfCantidadDis.getText()));
        nuevo.setFechaCaducidad(obtenerFecha(vista.FechaCaducida.getDate()));
        nuevo.setFechaRecepcion(obtenerFecha(vista.fechaRecepcion.getDate()));
        nuevo.calcularPrioridad();

        if (dao.insert(nuevo)) {
            DesktopNotify.setDefaultTheme(NotifyTheme.Green);
            DesktopNotify.showDesktopMessage("OK", "Lote registrado", DesktopNotify.SUCCESS, 3000);

            p.refrescar();
            p.seleccionarLoteEnTabla(nuevo.getIdLote());
            vista.dispose();
        }
    }

    private void actualizarLote() {
        loteEditar.setIdProducto(productoSeleccionado.getIdProducto());
        loteEditar.setCantidadDisponible(Integer.parseInt(vista.tfCantidadDis.getText()));
        loteEditar.setFechaCaducidad(obtenerFecha(vista.FechaCaducida.getDate()));
        loteEditar.setFechaRecepcion(obtenerFecha(vista.fechaRecepcion.getDate()));
        loteEditar.calcularPrioridad();

        if (dao.update(loteEditar)) {
            DesktopNotify.setDefaultTheme(NotifyTheme.Green);
            DesktopNotify.showDesktopMessage("OK", "Lote actualizado", DesktopNotify.SUCCESS, 3000);

            p.refrescar();
            p.seleccionarLoteEnTabla(loteEditar.getIdLote());
            vista.dispose();
        }
    }
    private LocalDate obtenerFecha(Date date) {
        return (date == null) ? null : date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
}
